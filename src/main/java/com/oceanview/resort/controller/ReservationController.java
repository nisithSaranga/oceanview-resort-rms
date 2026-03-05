package com.oceanview.resort.controller;

import com.oceanview.resort.dto.ReservationRequestDTO;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.dto.ReservationUpdateRequestDTO;
import com.oceanview.resort.service.ReservationService;

import java.util.Objects;

public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = Objects.requireNonNull(reservationService, "reservationService must not be null");
    }

    public ReservationResponseDTO createReservation(ReservationRequestDTO req) {
        return reservationService.createReservation(req);
    }

    public ReservationResponseDTO getReservation(String reservationNo) {
        return reservationService.getReservation(reservationNo);
    }

    public ReservationResponseDTO updateReservation(ReservationUpdateRequestDTO req) {
        return reservationService.updateReservation(req);
    }

    public void cancelReservation(String reservationNo) {
        reservationService.cancelReservation(reservationNo);
    }

    public void deleteReservation(String reservationNo) {
        reservationService.deleteReservation(reservationNo);
    }
}