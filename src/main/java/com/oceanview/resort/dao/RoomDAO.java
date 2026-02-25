package com.oceanview.resort.dao;

import com.oceanview.resort.entity.Room;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomDAO {
    Optional<Room> findById(int roomId) throws SQLException;
    List<Room> findAll() throws SQLException;
    List<Room> findAvailable(LocalDate checkIn, LocalDate checkOut) throws SQLException;
    boolean updateAvailability(int roomId, boolean available) throws SQLException;
}
