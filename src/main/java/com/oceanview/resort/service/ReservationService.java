package com.oceanview.resort.service;

import com.oceanview.resort.dto.ReservationRequestDTO;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.dto.ReservationUpdateRequestDTO;

import java.util.List;

public interface ReservationService {
    ReservationResponseDTO createReservation(ReservationRequestDTO request);
    ReservationResponseDTO updateReservation(String reservationNo, ReservationUpdateRequestDTO request);
    ReservationResponseDTO getReservationByNo(String reservationNo);
    List<ReservationResponseDTO> getAllReservations();
    void cancelReservation(String reservationNo);
    void deleteReservation(String reservationNo);
}

