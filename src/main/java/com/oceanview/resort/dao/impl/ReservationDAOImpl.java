package com.oceanview.resort.dao.impl;

import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.entity.Guest;
import com.oceanview.resort.entity.Reservation;
import com.oceanview.resort.entity.Room;
import com.oceanview.resort.enums.ReservationStatus;
import com.oceanview.resort.enums.RoomType;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    private final DBConnectionManager db;

    public ReservationDAOImpl() {
        this.db = DBConnectionManager.getInstance();
    }

    @Override
    public void save(Reservation reservation) throws SQLException {
        if (reservation == null) throw new IllegalArgumentException("reservation must not be null");
        if (reservation.getReservationNo() == null || reservation.getReservationNo().isBlank()) {
            throw new IllegalArgumentException("reservationNo is required");
        }
        if (reservation.getGuest() == null) throw new IllegalArgumentException("guest must not be null");
        if (reservation.getRoom() == null) throw new IllegalArgumentException("room must not be null");
        if (reservation.getCheckIn() == null || reservation.getCheckOut() == null) {
            throw new IllegalArgumentException("checkIn/checkOut are required");
        }
        if (reservation.getStatus() == null) {
            throw new IllegalArgumentException("status is required");
        }

        final String sql =
                "INSERT INTO reservation (reservation_no, guest_id, room_id, check_in, check_out, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reservation.getReservationNo());
            ps.setInt(2, reservation.getGuest().getGuestId());
            ps.setInt(3, reservation.getRoom().getRoomId());
            ps.setDate(4, Date.valueOf(reservation.getCheckIn()));
            ps.setDate(5, Date.valueOf(reservation.getCheckOut()));
            ps.setString(6, reservation.getStatus().name());

            int rows = ps.executeUpdate();
            if (rows != 1) {
                throw new SQLException("Insert failed: expected 1 row, got " + rows);
            }
        }
    }

    @Override
    public Reservation findByReservationNo(String reservationNo) throws SQLException {
        if (reservationNo == null || reservationNo.isBlank()) {
            throw new SQLException("reservationNo is required");
        }

        final String sql =
                "SELECT r.reservation_no, r.check_in, r.check_out, r.status, r.created_at, " +
                        "       g.guest_id, g.full_name, g.address, g.contact_number, " +
                        "       rm.room_id, rm.room_number, rm.room_type, rm.base_rate_per_night, rm.available " +
                        "FROM reservation r " +
                        "JOIN guest g ON g.guest_id = r.guest_id " +
                        "JOIN room rm ON rm.room_id = r.room_id " +
                        "WHERE r.reservation_no = ? " +
                        "LIMIT 1";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reservationNo.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Reservation not found: " + reservationNo);
                }
                return mapRow(rs);
            }
        }
    }

    @Override
    public List<Reservation> findByGuestId(int guestId) throws SQLException {
        if (guestId <= 0) return List.of();

        final String sql =
                "SELECT r.reservation_no, r.check_in, r.check_out, r.status, r.created_at, " +
                        "       g.guest_id, g.full_name, g.address, g.contact_number, " +
                        "       rm.room_id, rm.room_number, rm.room_type, rm.base_rate_per_night, rm.available " +
                        "FROM reservation r " +
                        "JOIN guest g ON g.guest_id = r.guest_id " +
                        "JOIN room rm ON rm.room_id = r.room_id " +
                        "WHERE g.guest_id = ? " +
                        "ORDER BY r.created_at DESC";

        List<Reservation> out = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, guestId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
            }
        }
        return out;
    }

    @Override
    public void update(Reservation reservation) throws SQLException {
        if (reservation == null) throw new IllegalArgumentException("reservation must not be null");
        if (reservation.getReservationNo() == null || reservation.getReservationNo().isBlank()) {
            throw new IllegalArgumentException("reservationNo is required");
        }
        if (reservation.getRoom() == null) throw new IllegalArgumentException("room must not be null");
        if (reservation.getCheckIn() == null || reservation.getCheckOut() == null) {
            throw new IllegalArgumentException("checkIn/checkOut are required");
        }
        if (reservation.getStatus() == null) {
            throw new IllegalArgumentException("status is required");
        }

        final String sql =
                "UPDATE reservation " +
                        "SET room_id = ?, check_in = ?, check_out = ?, status = ? " +
                        "WHERE reservation_no = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservation.getRoom().getRoomId());
            ps.setDate(2, Date.valueOf(reservation.getCheckIn()));
            ps.setDate(3, Date.valueOf(reservation.getCheckOut()));
            ps.setString(4, reservation.getStatus().name());
            ps.setString(5, reservation.getReservationNo().trim());

            int rows = ps.executeUpdate();
            if (rows != 1) {
                throw new SQLException("Update failed: expected 1 row, got " + rows);
            }
        }
    }

    @Override
    public void updateStatus(String reservationNo, ReservationStatus status) throws SQLException {
        if (reservationNo == null || reservationNo.isBlank()) {
            throw new SQLException("reservationNo is required");
        }
        if (status == null) {
            throw new SQLException("status is required");
        }

        final String sql =
                "UPDATE reservation SET status = ? WHERE reservation_no = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setString(2, reservationNo.trim());

            int rows = ps.executeUpdate();
            if (rows != 1) {
                throw new SQLException("Update status failed: expected 1 row, got " + rows);
            }
        }
    }

    @Override
    public void deleteByReservationNo(String reservationNo) throws SQLException {
        if (reservationNo == null || reservationNo.isBlank()) {
            throw new SQLException("reservationNo is required");
        }

        final String sql =
                "DELETE FROM reservation WHERE reservation_no = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reservationNo.trim());

            int rows = ps.executeUpdate();
            if (rows != 1) {
                throw new SQLException("Delete failed: expected 1 row, got " + rows);
            }
        }
    }

    private Reservation mapRow(ResultSet rs) throws SQLException {
        String reservationNo = rs.getString("reservation_no");
        LocalDate checkIn = rs.getDate("check_in").toLocalDate();
        LocalDate checkOut = rs.getDate("check_out").toLocalDate();

        String statusStr = rs.getString("status");
        if (statusStr == null || statusStr.isBlank()) {
            throw new SQLException("reservation.status is NULL/blank for reservationNo=" + reservationNo);
        }

        final ReservationStatus status;
        try {
            status = ReservationStatus.valueOf(statusStr.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new SQLException("Invalid reservation.status in DB: " + statusStr, ex);
        }

        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime createdAt = (ts != null) ? ts.toLocalDateTime() : null;

        Guest guest = new Guest(
                rs.getInt("guest_id"),
                rs.getString("full_name"),
                rs.getString("address"),
                rs.getString("contact_number")
        );

        final RoomType roomType;
        try {
            roomType = RoomType.valueOf(rs.getString("room_type").trim().toUpperCase());
        } catch (Exception ex) {
            throw new SQLException("Invalid room_type in DB", ex);
        }

        BigDecimal rate = rs.getBigDecimal("base_rate_per_night");

        Room room = new Room(
                rs.getInt("room_id"),
                rs.getString("room_number"),
                roomType,
                rate,
                rs.getBoolean("available")
        );

        return new Reservation(reservationNo, checkIn, checkOut, status, createdAt, guest, room);
    }
}
