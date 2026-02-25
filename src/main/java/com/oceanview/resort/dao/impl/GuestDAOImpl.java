package com.oceanview.resort.dao.impl;

<<<<<<< Updated upstream
import com.oceanview.resort.dao.GuestDAO;
import com.oceanview.resort.entity.Guest;

import java.sql.SQLException;
import java.util.Optional;

public class GuestDAOImpl implements GuestDAO {
    @Override
    public int create(Guest guest) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement create");
    }

    @Override
    public Optional<Guest> findById(int guestId) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement findById");
=======
import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.GuestDAO;

public class GuestDAOImpl implements GuestDAO {
    private final DBConnectionManager db;

    public GuestDAOImpl() {
        this.db = DBConnectionManager.getInstance();
>>>>>>> Stashed changes
    }
}

