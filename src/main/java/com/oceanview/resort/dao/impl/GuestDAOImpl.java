package com.oceanview.resort.dao.impl;

import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.GuestDAO;
import com.oceanview.resort.entity.Guest;

import java.sql.SQLException;
import java.util.Optional;

public class GuestDAOImpl implements GuestDAO {

    private final DBConnectionManager db;

    public GuestDAOImpl() {
        this.db = DBConnectionManager.getInstance();
    }
    @Override
    public int create(Guest guest) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement create");
    }

    @Override
    public Optional<Guest> findById(int guestId) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement findById");
    }
}
