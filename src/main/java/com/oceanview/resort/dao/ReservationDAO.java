package com.oceanview.resort.dao;

import com.oceanview.resort.entity.Reservation;
import com.oceanview.resort.enums.ReservationStatus;

import java.sql.SQLException;
import java.util.List;

public interface ReservationDAO {

    Reservation findByReservationNo(String no) throws SQLException;

    void save(Reservation r) throws SQLException;

    List<Reservation> findByGuestId(int guestId) throws SQLException;

    void update(Reservation r) throws SQLException;

    void updateStatus(String reservationNo, ReservationStatus status) throws SQLException;

    void deleteByReservationNo(String no) throws SQLException;
}