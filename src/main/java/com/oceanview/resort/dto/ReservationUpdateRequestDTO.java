package com.oceanview.resort.dto;

import com.oceanview.resort.enums.RoomType;

import java.time.LocalDate;

public class ReservationUpdateRequestDTO {

    private String reservationNo;
    private String fullName;
    private String address;
    private String contactNumber;
    private RoomType roomType;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public ReservationUpdateRequestDTO() {
        // no-arg constructor for JSON deserialization
    }

    public ReservationUpdateRequestDTO(String reservationNo,
                                       String fullName,
                                       String address,
                                       String contactNumber,
                                       RoomType roomType,
                                       LocalDate checkIn,
                                       LocalDate checkOut) {
        this.reservationNo = reservationNo;
        this.fullName = fullName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.roomType = roomType;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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
}