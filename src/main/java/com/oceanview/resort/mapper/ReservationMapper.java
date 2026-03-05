package com.oceanview.resort.mapper;

import com.oceanview.resort.dto.ReservationRequestDTO;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.entity.Guest;
import com.oceanview.resort.entity.Reservation;
import com.oceanview.resort.entity.Room;
import com.oceanview.resort.enums.ReservationStatus;

public final class ReservationMapper {

    private ReservationMapper() {}

    public static Guest toGuest(ReservationRequestDTO dto) {
        if (dto == null) return null;

        Guest g = new Guest();
        g.setFullName(dto.getFullName());
        g.setAddress(dto.getAddress());
        g.setContactNumber(dto.getContactNumber());
        return g;
    }

    public static Reservation toReservation(ReservationRequestDTO dto, Guest guest, Room room) {
        if (dto == null) return null;

        return new Reservation(
                null, // reservationNo generated in service/DAO later
                dto.getCheckIn(),
                dto.getCheckOut(),
                ReservationStatus.CREATED,
                null,
                guest,
                room
        );
    }

    public static ReservationResponseDTO toResponseDTO(Reservation entity) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        if (entity == null) {
            dto.setMessage("Reservation not found");
            return dto;
        }

        dto.setReservationNo(entity.getReservationNo());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getRoom() != null) {
            dto.setRoomId(entity.getRoom().getRoomId());
        }

        dto.setMessage("OK");
        return dto;
    }
}
