package com.oceanview.resort.dao.impl;

import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.InvoiceDAO;
import com.oceanview.resort.entity.Invoice;
import com.oceanview.resort.entity.Reservation;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class InvoiceDAOImpl implements InvoiceDAO {

    private final DBConnectionManager db;

    public InvoiceDAOImpl() {
        this.db = DBConnectionManager.getInstance();
    }

    @Override
    public int save(Invoice inv) throws SQLException {
        Objects.requireNonNull(inv, "inv must not be null");

        if (inv.getReservation() == null || inv.getReservation().getReservationNo() == null
                || inv.getReservation().getReservationNo().trim().isEmpty()) {
            throw new IllegalArgumentException("inv.reservation.reservationNo is required");
        }
        if (inv.getTotalAmount() == null) {
            throw new IllegalArgumentException("inv.totalAmount is required");
        }

        // If caller didn't set issuedAt, set one so DB + object stay consistent
        LocalDateTime issuedAt = (inv.getIssuedAt() != null) ? inv.getIssuedAt() : LocalDateTime.now();

        final String sql =
                "INSERT INTO invoice (reservation_no, issued_at, total_amount) VALUES (?, ?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, inv.getReservation().getReservationNo().trim());
            ps.setTimestamp(2, Timestamp.valueOf(issuedAt));
            ps.setBigDecimal(3, inv.getTotalAmount());

            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new SQLException("Failed to save invoice (rows affected=" + affected + ")");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    inv.setInvoiceId(id);
                    inv.setIssuedAt(issuedAt);
                    return id;
                }
            }
        }

        throw new SQLException("Failed to save invoice (no generated key returned)");
    }

    @Override
    public Optional<Invoice> findByReservationNo(String reservationNo) throws SQLException {
        if (reservationNo == null || reservationNo.trim().isEmpty()) return Optional.empty();

        final String sql =
                "SELECT invoice_id, reservation_no, issued_at, total_amount " +
                        "FROM invoice WHERE reservation_no = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reservationNo.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                int id = rs.getInt("invoice_id");
                String resNo = rs.getString("reservation_no");

                Timestamp issuedAtTs = rs.getTimestamp("issued_at");
                LocalDateTime issuedAt = (issuedAtTs != null) ? issuedAtTs.toLocalDateTime() : null;

                // Reservation stub is enough here (service can fully load if needed)
                Reservation reservationStub = new Reservation(resNo, null, null, null, null, null, null);

                Invoice inv = new Invoice(id, issuedAt, rs.getBigDecimal("total_amount"), reservationStub);
                return Optional.of(inv);
            }
        }
    }
}