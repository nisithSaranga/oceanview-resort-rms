package com.oceanview.resort.presentation.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oceanview.resort.controller.ReservationController;
import com.oceanview.resort.dao.GuestDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.RoomDAO;
import com.oceanview.resort.dto.ReservationRequestDTO;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.dto.ReservationUpdateRequestDTO;
import com.oceanview.resort.enums.ReservationStatus;
import com.oceanview.resort.enums.RoomType;
import com.oceanview.resort.factory.DAOFactory;
import com.oceanview.resort.service.ReservationService;
import com.oceanview.resort.service.impl.ReservationServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@WebServlet("/reservations")
public class ReservationApiServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);

    // UML chain: Servlet -> Controller -> Service -> DAO
    private ReservationController reservationController;

    @Override
    public void init() {
        ReservationDAO reservationDAO = DAOFactory.getReservationDAO();
        RoomDAO roomDAO = DAOFactory.getRoomDAO();
        GuestDAO guestDAO = DAOFactory.getGuestDAO();

        ReservationService reservationService = new ReservationServiceImpl(reservationDAO, roomDAO, guestDAO);
        this.reservationController = new ReservationController(reservationService);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String reservationNo = req.getParameter("reservationNo");
        if (isBlank(reservationNo)) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail("reservationNo query param is required"));
            return;
        }

        try {
            ReservationResponseDTO dto = reservationController.getReservation(reservationNo.trim());
            writeJson(resp, HttpServletResponse.SC_OK, dto);
        } catch (IllegalArgumentException ex) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail(ex.getMessage()));
        } catch (RuntimeException ex) {
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, fail("Server error"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try {
            // ReservationRequestDTO is Builder-based (immutable)
            ReservationRequestDTO dto = readCreateRequestViaBuilder(req);

            ReservationResponseDTO out = reservationController.createReservation(dto);
            writeJson(resp, HttpServletResponse.SC_OK, out);

        } catch (IllegalArgumentException ex) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail(ex.getMessage()));
        } catch (JsonProcessingException ex) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail("Invalid JSON"));
        } catch (Exception ex) {
            getServletContext().log("Reservation POST failed", ex);
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, fail("Server error"));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try {
            ReservationUpdateRequestDTO body = readJsonBody(req, ReservationUpdateRequestDTO.class);

            if (isBlank(body.getReservationNo())) {
                writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail("reservationNo is required"));
                return;
            }

            if (body.getStatus() == ReservationStatus.CANCELLED) {
                reservationController.cancelReservation(body.getReservationNo().trim());

                ReservationResponseDTO out = new ReservationResponseDTO();
                out.setReservationNo(body.getReservationNo().trim());
                out.setStatus(ReservationStatus.CANCELLED);
                out.setMessage("Reservation cancelled");

                writeJson(resp, HttpServletResponse.SC_OK, out);
                return;
            }

            ReservationResponseDTO dto = reservationController.updateReservation(body);
            writeJson(resp, HttpServletResponse.SC_OK, dto);

        } catch (IllegalArgumentException ex) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail(ex.getMessage()));
        } catch (JsonProcessingException ex) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail("Invalid JSON"));
        } catch (RuntimeException ex) {
            getServletContext().log("Reservation PUT failed", ex);
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, fail("Server error"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String reservationNo = req.getParameter("reservationNo");
        if (isBlank(reservationNo)) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail("reservationNo query param is required"));
            return;
        }

        try {
            reservationController.deleteReservation(reservationNo.trim());

            ReservationResponseDTO out = new ReservationResponseDTO();
            out.setReservationNo(reservationNo.trim());
            out.setMessage("Reservation deleted");

            writeJson(resp, HttpServletResponse.SC_OK, out);

        } catch (IllegalArgumentException ex) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail(ex.getMessage()));
        } catch (RuntimeException ex) {
            getServletContext().log("Reservation DELETE failed", ex);
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, fail("Server error"));
        }
    }

    // ---------- helpers ----------

    private ReservationRequestDTO readCreateRequestViaBuilder(HttpServletRequest req) throws IOException {
        byte[] bytes = readBodyBytes(req);
        JsonNode root = mapper.readTree(bytes);

        String fullName = textRequired(root, "fullName");
        String address = textRequired(root, "address");
        String contactNumber = textRequired(root, "contactNumber");

        String roomTypeRaw = textRequired(root, "roomType");
        RoomType roomType;
        try {
            roomType = RoomType.valueOf(roomTypeRaw.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid roomType: " + roomTypeRaw);
        }

        LocalDate checkIn = dateRequired(root, "checkIn");
        LocalDate checkOut = dateRequired(root, "checkOut");

        return ReservationRequestDTO.builder()
                .withFullName(fullName)
                .withAddress(address)
                .withContactNumber(contactNumber)
                .withRoomType(roomType)
                .withCheckIn(checkIn)
                .withCheckOut(checkOut)
                .build();
    }

    private <T> T readJsonBody(HttpServletRequest req, Class<T> clazz) throws IOException {
        byte[] bytes = readBodyBytes(req);
        return mapper.readValue(bytes, clazz);
    }

    private byte[] readBodyBytes(HttpServletRequest req) throws IOException {
        byte[] bytes = req.getInputStream().readAllBytes();
        if (bytes.length == 0) throw new IllegalArgumentException("Missing JSON body");
        return bytes;
    }

    private void writeJson(HttpServletResponse resp, int status, ReservationResponseDTO dto) throws IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(status);
        mapper.writeValue(resp.getOutputStream(), dto);
    }

    private ReservationResponseDTO fail(String message) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setMessage(message);
        return dto;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String textRequired(JsonNode root, String field) {
        JsonNode n = root.get(field);
        if (n == null || n.isNull()) throw new IllegalArgumentException(field + " is required");
        String v = n.asText();
        if (isBlank(v)) throw new IllegalArgumentException(field + " is required");
        return v.trim();
    }

    private LocalDate dateRequired(JsonNode root, String field) {
        String v = textRequired(root, field);
        try {
            return LocalDate.parse(v);
        } catch (Exception ex) {
            throw new IllegalArgumentException(field + " must be in ISO format yyyy-MM-dd");
        }
    }
}