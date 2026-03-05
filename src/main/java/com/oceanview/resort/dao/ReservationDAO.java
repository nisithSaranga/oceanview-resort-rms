package com.oceanview.resort.dao;

import com.oceanview.resort.entity.Reservation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationDAO {
    String create(Reservation reservation) throws SQLException;
    Optional<Reservation> findByReservationNo(String reservationNo) throws SQLException;
    List<Reservation> findAll() throws SQLException;
    boolean update(Reservation reservation) throws SQLException;
    boolean deleteByReservationNo(String reservationNo) throws SQLException;

    // Overlap check is handled in DAO SQL (aligned with your UML-phase assumptions)
    boolean existsOverlappingReservation(int roomId, LocalDate checkIn, LocalDate checkOut) throws SQLException;
}
