package com.oceanview.resort.service.impl;

import com.oceanview.resort.dao.GuestDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.dto.ReservationRequestDTO;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.dto.ReservationUpdateRequestDTO;
import com.oceanview.resort.entity.Guest;
import com.oceanview.resort.entity.Reservation;
import com.oceanview.resort.entity.Room;
import com.oceanview.resort.enums.ReservationStatus;
import com.oceanview.resort.enums.RoomType;
import com.oceanview.resort.mapper.ReservationMapper;
import com.oceanview.resort.service.ReservationService;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOG = Logger.getLogger(ReservationServiceImpl.class.getName());
    private static final SecureRandom RNG = new SecureRandom();

    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;
    private final GuestDAO guestDAO;

    public ReservationServiceImpl(ReservationDAO reservationDAO, RoomDAO roomDAO, GuestDAO guestDAO) {
        this.reservationDAO = Objects.requireNonNull(reservationDAO, "reservationDAO must not be null");
        this.roomDAO = Objects.requireNonNull(roomDAO, "roomDAO must not be null");
        this.guestDAO = Objects.requireNonNull(guestDAO, "guestDAO must not be null");
    }

    @Override
    public ReservationResponseDTO createReservation(ReservationRequestDTO req) {
        validateCreateRequest(req);

        try {
            // 1) Pick an available room by type
            List<Room> availableRooms = roomDAO.findAvailableByType(req.getRoomType());
            if (availableRooms == null || availableRooms.isEmpty()) {
                return fail("No available rooms for type: " + req.getRoomType());
            }
            Room selectedRoom = availableRooms.get(0);

            // 2) Save guest (DAO should set generated id into Guest object)
            Guest guest = ReservationMapper.toGuest(req);
            guestDAO.save(guest);

            if (guest.getGuestId() <= 0) {
                return fail("Guest save failed (no generated ID)");
            }

            // 3) Create reservation (createdAt DB-managed)
            String reservationNo = generateReservationNo();
            Reservation reservation = new Reservation(
                    reservationNo,
                    req.getCheckIn(),
                    req.getCheckOut(),
                    ReservationStatus.CREATED,
                    null, // DB DEFAULT CURRENT_TIMESTAMP
                    guest,
                    selectedRoom
            );

            // 4) Persist + reserve room
            reservationDAO.save(reservation);
            roomDAO.updateAvailability(selectedRoom.getRoomId(), false);

            // 5) Response
            ReservationResponseDTO out = ReservationMapper.toResponseDTO(reservation);
            out.setMessage("Reservation created");
            return out;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE,
                    "createReservation failed. SQLState=" + ex.getSQLState() + ", Code=" + ex.getErrorCode(),
                    ex);
            return fail("Reservation creation failed");
        } catch (RuntimeException ex) {
            LOG.log(Level.SEVERE, "createReservation runtime failure", ex);
            return fail("Reservation creation failed");
        }
    }

    @Override
    public ReservationResponseDTO getReservation(String reservationNo) {
        if (isBlank(reservationNo)) return fail("reservationNo is required");

        try {
            Reservation r = reservationDAO.findByReservationNo(reservationNo.trim());
            ReservationResponseDTO dto = ReservationMapper.toResponseDTO(r);
            dto.setMessage("OK");
            return dto;
        } catch (SQLException ex) {
            return fail("Reservation not found");
        }
    }

    @Override
    public ReservationResponseDTO updateReservation(ReservationUpdateRequestDTO req) {
        if (req == null) return fail("Request body is missing");
        if (isBlank(req.getReservationNo())) return fail("reservationNo is required");

        try {
            Reservation existing = reservationDAO.findByReservationNo(req.getReservationNo().trim());
            Room oldRoom = existing.getRoom();

            LocalDate newCheckIn = (req.getCheckIn() != null) ? req.getCheckIn() : existing.getCheckIn();
            LocalDate newCheckOut = (req.getCheckOut() != null) ? req.getCheckOut() : existing.getCheckOut();
            if (newCheckIn == null || newCheckOut == null || !newCheckOut.isAfter(newCheckIn)) {
                return fail("checkOut must be after checkIn");
            }

            ReservationStatus newStatus = (req.getStatus() != null) ? req.getStatus() : existing.getStatus();
            Room newRoom = oldRoom;

            // Room relocation logic
            RoomType requestedType = req.getRoomType();
            if (requestedType != null && (oldRoom == null || requestedType != oldRoom.getRoomType())) {
                List<Room> candidates = roomDAO.findAvailableByType(requestedType);
                if (candidates == null || candidates.isEmpty()) {
                    return fail("No available rooms for type: " + requestedType);
                }
                newRoom = candidates.get(0);

                // Release old + reserve new
                if (oldRoom != null) {
                    roomDAO.updateAvailability(oldRoom.getRoomId(), true);
                }
                roomDAO.updateAvailability(newRoom.getRoomId(), false);
            }

            Reservation updated = new Reservation(
                    existing.getReservationNo(),
                    newCheckIn,
                    newCheckOut,
                    newStatus,
                    existing.getCreatedAt(),
                    existing.getGuest(),
                    newRoom
            );

            reservationDAO.update(updated);

            ReservationResponseDTO dto = ReservationMapper.toResponseDTO(updated);
            dto.setMessage("Reservation updated");
            return dto;

        } catch (SQLException ex) {
            return fail("Reservation update failed");
        }
    }

    // UML: void
    @Override
    public void cancelReservation(String reservationNo) {
        if (isBlank(reservationNo)) throw new IllegalArgumentException("reservationNo is required");

        try {
            Reservation existing = reservationDAO.findByReservationNo(reservationNo.trim());
            reservationDAO.updateStatus(existing.getReservationNo(), ReservationStatus.CANCELLED);

            if (existing.getRoom() != null) {
                roomDAO.updateAvailability(existing.getRoom().getRoomId(), true);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Reservation cancellation failed", ex);
        }
    }

    // UML: void
    @Override
    public void deleteReservation(String reservationNo) {
        if (isBlank(reservationNo)) throw new IllegalArgumentException("reservationNo is required");

        try {
            Reservation existing = reservationDAO.findByReservationNo(reservationNo.trim());

            if (existing.getStatus() != ReservationStatus.CANCELLED) {
                throw new IllegalArgumentException("Delete allowed only for CANCELLED reservations");
            }

            reservationDAO.deleteByReservationNo(existing.getReservationNo());
        } catch (SQLException ex) {
            throw new IllegalStateException("Reservation deletion failed", ex);
        }
    }

    // ---------------- helpers ----------------

    private void validateCreateRequest(ReservationRequestDTO req) {
        if (req == null) throw new IllegalArgumentException("Request body is missing");
        if (isBlank(req.getFullName())) throw new IllegalArgumentException("fullName is required");
        if (isBlank(req.getAddress())) throw new IllegalArgumentException("address is required");
        if (isBlank(req.getContactNumber())) throw new IllegalArgumentException("contactNumber is required");
        if (req.getRoomType() == null) throw new IllegalArgumentException("roomType is required");

        if (req.getCheckIn() == null || req.getCheckOut() == null) {
            throw new IllegalArgumentException("checkIn and checkOut are required");
        }
        if (!req.getCheckOut().isAfter(req.getCheckIn())) {
            throw new IllegalArgumentException("checkOut must be after checkIn");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private ReservationResponseDTO fail(String message) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setMessage(message);
        return dto;
    }

    private String generateReservationNo() {
        int suffix = 1000 + RNG.nextInt(9000);
        return "RES-" + System.currentTimeMillis() + "-" + suffix;
    }
}