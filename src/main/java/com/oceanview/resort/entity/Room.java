package com.oceanview.resort.entity;

import com.oceanview.resort.enums.RoomType;

import java.math.BigDecimal;

public class Room {
    private int roomId;
    private String roomNumber;
    private RoomType roomType;
    private BigDecimal baseRatePerNight;
    private boolean available;

    public Room() {
    }

    public Room(int roomId, String roomNumber, RoomType roomType, BigDecimal baseRatePerNight, boolean available) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.baseRatePerNight = baseRatePerNight;
        this.available = available;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getBaseRatePerNight() {
        return baseRatePerNight;
    }

    public void setBaseRatePerNight(BigDecimal baseRatePerNight) {
        this.baseRatePerNight = baseRatePerNight;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
