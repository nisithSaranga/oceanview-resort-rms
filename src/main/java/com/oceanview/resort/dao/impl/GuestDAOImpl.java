package com.oceanview.resort.dao.impl;

import com.oceanview.resort.dao.GuestDAO;
import com.oceanview.resort.entity.Guest;

import java.sql.SQLException;
import java.util.Optional;

public class GuestDAOImpl implements GuestDAO {
    @Override
    public int create(Guest guest) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");
    }

    @Override
    public Optional<Guest> findById(int guestId) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");

    }
}
