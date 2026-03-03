package com.oceanview.resort.dto;

import com.oceanview.resort.enums.ReservationStatus;
import com.oceanview.resort.enums.RoomType;

import java.time.LocalDate;

public class ReservationUpdateRequestDTO {

    private String reservationNo;
    private RoomType roomType;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ReservationStatus status;

    public ReservationUpdateRequestDTO() {
    }

    public String getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}