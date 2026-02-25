package com.oceanview.resort.dao.impl;

<<<<<<< Updated upstream
import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.entity.Room;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RoomDAOImpl implements RoomDAO {
    @Override
    public Optional<Room> findById(int roomId) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement findById");
    }

    @Override
    public List<Room> findAll() throws SQLException {
        throw new UnsupportedOperationException("TODO: implement findAll");
    }

    @Override
    public List<Room> findAvailable(LocalDate checkIn, LocalDate checkOut) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement findAvailable");
    }

    @Override
    public boolean updateAvailability(int roomId, boolean available) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement updateAvailability");
    }
=======
import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.RoomDAO;

public class RoomDAOImpl implements RoomDAO {

    private final DBConnectionManager db;

    public RoomDAOImpl() {
        this.db = DBConnectionManager.getInstance();
    }

>>>>>>> Stashed changes
}


