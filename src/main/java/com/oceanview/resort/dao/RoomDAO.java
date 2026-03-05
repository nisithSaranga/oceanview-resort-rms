package com.oceanview.resort.dao;

import com.oceanview.resort.entity.Room;
import com.oceanview.resort.enums.RoomType;

import java.sql.SQLException;
import java.util.List;

public interface RoomDAO {

    Room findById(int id) throws SQLException;

    List<Room> findAvailableByType(RoomType t) throws SQLException;

    boolean updateAvailability(int roomId, boolean available) throws SQLException;
}