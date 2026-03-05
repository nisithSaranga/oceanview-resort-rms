package com.oceanview.resort.entity;

import com.oceanview.resort.enums.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private String reservationNo;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ReservationStatus status;
    private LocalDateTime createdAt;

    private Guest guest;
    private Room room;

    public Reservation() {
        // required for JDBC mapping / Jackson safety
    }

    public Reservation(String reservationNo,
                       LocalDate checkIn,
                       LocalDate checkOut,
                       ReservationStatus status,
                       LocalDateTime createdAt,
                       Guest guest,
                       Room room) {
        this.reservationNo = reservationNo;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.createdAt = createdAt;
        this.guest = guest;
        this.room = room;
    }

    public String getReservationNo() {
        return reservationNo;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus s) {
        this.status = s;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }
}