package com.oceanview.resort.dao.impl;

import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.entity.Room;
import com.oceanview.resort.enums.RoomType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    private final DBConnectionManager db;

    public RoomDAOImpl() {
        this.db = DBConnectionManager.getInstance();
    }

    @Override
    public Room findById(int id) throws SQLException {
        if (id <= 0) {
            return null;
        }

        final String sql =
                "SELECT room_id, room_number, room_type, base_rate_per_night, available " +
                        "FROM room " +
                        "WHERE room_id = ? " +
                        "LIMIT 1";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapRow(rs);
            }
        }
    }

    @Override
    public List<Room> findAvailableByType(RoomType t, LocalDate checkIn, LocalDate checkOut, String excludeReservationNo) throws SQLException {
        if (t == null || checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            return Collections.emptyList();
        }

        StringBuilder sql = new StringBuilder("""
        SELECT rm.room_id, rm.room_number, rm.room_type, rm.base_rate_per_night, rm.available
        FROM room rm
        WHERE rm.room_type = ?
          AND rm.available = ?
          AND NOT EXISTS (
              SELECT 1
              FROM reservation r
              WHERE r.room_id = rm.room_id
                AND r.status <> 'CANCELLED'
        """);

        if (excludeReservationNo != null && !excludeReservationNo.isBlank()) {
            sql.append(" AND r.reservation_no <> ? ");
        }

        sql.append("""
                AND ? < r.check_out
                AND ? > r.check_in
          )
        ORDER BY rm.room_id
        """);

        List<Room> rooms = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setString(1, t.name());
            ps.setBoolean(2, true);

            int index = 3;

            if (excludeReservationNo != null && !excludeReservationNo.isBlank()) {
                ps.setString(index++, excludeReservationNo);
            }

            ps.setDate(index++, java.sql.Date.valueOf(checkIn));
            ps.setDate(index, java.sql.Date.valueOf(checkOut));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRow(rs));
                }
            }
        }

        return rooms;
    }

    @Override
    public boolean updateAvailability(int roomId, boolean available) throws SQLException {
        if (roomId <= 0) {
            throw new IllegalArgumentException("roomId must be > 0");
        }

        final String sql = "UPDATE room SET available = ? WHERE room_id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, roomId);

            int updated = ps.executeUpdate();
            return updated > 0;
        }
    }

    private Room mapRow(ResultSet rs) throws SQLException {
        int roomId = rs.getInt("room_id");
        String roomNumber = rs.getString("room_number");

        String typeRaw = rs.getString("room_type");
        if (typeRaw == null || typeRaw.trim().isEmpty()) {
            throw new SQLException("room_type is NULL/blank for room_id=" + roomId);
        }

        final RoomType roomType;
        try {
            roomType = RoomType.valueOf(typeRaw.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new SQLException("Invalid room_type in DB: " + typeRaw + " (room_id=" + roomId + ")", ex);
        }

        BigDecimal baseRatePerNight = rs.getBigDecimal("base_rate_per_night");
        boolean isAvailable = rs.getBoolean("available");

        return new Room(roomId, roomNumber, roomType, baseRatePerNight, isAvailable);
    }
}

