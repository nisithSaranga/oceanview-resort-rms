package com.oceanview.resort.dto;

import com.oceanview.resort.enums.ReservationStatus;

import java.time.LocalDateTime;

public class ReservationResponseDTO {

    private String reservationNo;
    private ReservationStatus status;
    private int roomId;
    private String message;
    private LocalDateTime createdAt;

    public ReservationResponseDTO() {
        // Required for JSON deserialization / frameworks
    }

    public ReservationResponseDTO(String reservationNo,
                                  ReservationStatus status,
                                  int roomId,
                                  String message,
                                  LocalDateTime createdAt) {
        this.reservationNo = reservationNo;
        this.status = status;
        this.roomId = roomId;
        this.message = message;
        this.createdAt = createdAt;
    }

    public String getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}