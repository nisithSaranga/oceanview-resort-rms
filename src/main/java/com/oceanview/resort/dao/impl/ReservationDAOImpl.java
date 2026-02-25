package com.oceanview.resort.dao.impl;

import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.entity.Reservation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReservationDAOImpl implements ReservationDAO {
    @Override
    public String create(Reservation reservation) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");
    }

    @Override
    public Optional<Reservation> findByReservationNo(String reservationNo) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");
    }

    @Override
    public List<Reservation> findAll() throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");
    }

    @Override
    public boolean update(Reservation reservation) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");
    }

    @Override
    public boolean deleteByReservationNo(String reservationNo) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");
    }

    @Override
    public boolean existsOverlappingReservation(int roomId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");
    }
}
