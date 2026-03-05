package com.oceanview.resort.service;

import com.oceanview.resort.dto.ReservationRequestDTO;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.dto.ReservationUpdateRequestDTO;

public interface ReservationService {

    ReservationResponseDTO createReservation(ReservationRequestDTO req);

    ReservationResponseDTO getReservation(String reservationNo);

    ReservationResponseDTO updateReservation(ReservationUpdateRequestDTO req);

    void cancelReservation(String reservationNo);

    void deleteReservation(String reservationNo);
}

