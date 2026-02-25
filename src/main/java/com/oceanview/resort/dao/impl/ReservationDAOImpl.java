package com.oceanview.resort.dao.impl;

import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.entity.Reservation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReservationDAOImpl implements ReservationDAO {
    private final DBConnectionManager db;

    public ReservationDAOImpl() {
        this.db = DBConnectionManager.getInstance();
    }
    @Override
    public String create(Reservation reservation) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement create");
    }

    @Override
    public Optional<Reservation> findByReservationNo(String reservationNo) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement findByReservationNo");
    }

    @Override
    public List<Reservation> findAll() throws SQLException {
        throw new UnsupportedOperationException("TODO: implement findAll");
    }

    @Override
    public boolean update(Reservation reservation) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement update");
    }

    @Override
    public boolean deleteByReservationNo(String reservationNo) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement deleteByReservationNo");
    }

    @Override
    public boolean existsOverlappingReservation(int roomId, LocalDate checkIn, LocalDate checkOut) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement existsOverlappingReservation");
    }
}

