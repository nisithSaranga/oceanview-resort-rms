package com.oceanview.resort.dao.impl;

import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.GuestDAO;
import com.oceanview.resort.entity.Guest;

import java.sql.*;
import java.util.Optional;

public class GuestDAOImpl implements GuestDAO {

    private static final String TABLE = "guest";
    private static final String COL_ID = "guest_id";
    private static final String COL_FULL_NAME = "full_name";
    private static final String COL_ADDRESS = "address";
    private static final String COL_CONTACT = "contact_number";

    private final DBConnectionManager db;

    public GuestDAOImpl() {
        this.db = DBConnectionManager.getInstance();
    }

    @Override
    public void save(Guest guest) throws SQLException {
        if (guest == null) {
            throw new IllegalArgumentException("guest must not be null");
        }

        String fullName = safeTrim(guest.getFullName());
        String address = safeTrim(guest.getAddress());
        String contact = safeTrim(guest.getContactNumber());

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("guest.fullName is required");
        }
        if (contact == null || contact.isBlank()) {
            throw new IllegalArgumentException("guest.contactNumber is required");
        }

        final String sql =
                "INSERT INTO " + TABLE + " (" + COL_FULL_NAME + ", " + COL_ADDRESS + ", " + COL_CONTACT + ") " +
                        "VALUES (?, ?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, fullName);
            ps.setString(2, address);
            ps.setString(3, contact);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Saving guest failed: no rows affected");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    guest.setGuestId(newId); // key point: keep id without returning it
                    return;
                }
            }

            throw new SQLException("Saving guest failed: no generated key returned");
        }
    }

    @Override
    public Optional<Guest> findById(int guestId) throws SQLException {
        if (guestId <= 0) {
            return Optional.empty();
        }

        final String sql =
                "SELECT " + COL_ID + ", " + COL_FULL_NAME + ", " + COL_ADDRESS + ", " + COL_CONTACT + " " +
                        "FROM " + TABLE + " " +
                        "WHERE " + COL_ID + " = ? " +
                        "LIMIT 1";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, guestId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapRow(rs));
            }
        }
    }

    private Guest mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt(COL_ID);
        String fullName = rs.getString(COL_FULL_NAME);
        String address = rs.getString(COL_ADDRESS);
        String contact = rs.getString(COL_CONTACT);
        return new Guest(id, fullName, address, contact);
    }

    private String safeTrim(String s) {
        return (s == null) ? null : s.trim();
    }
}