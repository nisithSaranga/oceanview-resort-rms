package com.oceanview.resort.dao;

import com.oceanview.resort.entity.Guest;

import java.sql.SQLException;
import java.util.Optional;

public interface GuestDAO {

    void save(Guest guest) throws SQLException;

    Optional<Guest> findById(int guestId) throws SQLException;
}
