package com.oceanview.resort.dto;

import com.oceanview.resort.enums.RoomType;

import java.time.LocalDate;

public class ReservationRequestDTO {

    private final String fullName;
    private final String address;
    private final String contactNumber;

    private final RoomType roomType;
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    private ReservationRequestDTO(Builder b) {
        this.fullName = b.fullName;
        this.address = b.address;
        this.contactNumber = b.contactNumber;
        this.roomType = b.roomType;
        this.checkIn = b.checkIn;
        this.checkOut = b.checkOut;
    }

    // getters only (DTO immutability)
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getContactNumber() { return contactNumber; }
    public RoomType getRoomType() { return roomType; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String fullName;
        private String address;
        private String contactNumber;
        private RoomType roomType;
        private LocalDate checkIn;
        private LocalDate checkOut;

        public Builder withFullName(String name) {
            this.fullName = name;
            return this;
        }

        public Builder withAddress(String addr) {
            this.address = addr;
            return this;
        }

        public Builder withContactNumber(String no) {
            this.contactNumber = no;
            return this;
        }

        public Builder withRoomType(RoomType t) {
            this.roomType = t;
            return this;
        }

        public Builder withCheckIn(LocalDate d) {
            this.checkIn = d;
            return this;
        }

        public Builder withCheckOut(LocalDate d) {
            this.checkOut = d;
            return this;
        }

        public ReservationRequestDTO build() {
            return new ReservationRequestDTO(this);
        }
    }
}
