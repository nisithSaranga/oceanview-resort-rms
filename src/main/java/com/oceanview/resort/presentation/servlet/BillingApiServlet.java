package com.oceanview.resort.presentation.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.oceanview.resort.controller.BillingController;
import com.oceanview.resort.dao.InvoiceDAO;
import com.oceanview.resort.dao.InvoiceLineItemDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dto.InvoiceResponseDTO;
import com.oceanview.resort.factory.DAOFactory;
import com.oceanview.resort.service.BillingService;
import com.oceanview.resort.service.impl.BillingServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/billing")
public class BillingApiServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);

    private BillingController billingController;

    @Override
    public void init() {
        ReservationDAO reservationDAO = DAOFactory.getReservationDAO();
        InvoiceDAO invoiceDAO = DAOFactory.getInvoiceDAO();
        InvoiceLineItemDAO invoiceLineItemDAO = DAOFactory.getInvoiceLineItemDAO();

        BillingService billingService = new BillingServiceImpl(reservationDAO, invoiceDAO, invoiceLineItemDAO);
        this.billingController = new BillingController(billingService);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String reservationNo = req.getParameter("reservationNo");
        if (isBlank(reservationNo)) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail("reservationNo query param is required"));
            return;
        }

        try {
            InvoiceResponseDTO out = billingController.generateInvoice(reservationNo.trim());
            writeJson(resp, HttpServletResponse.SC_OK, out);
        } catch (IllegalArgumentException ex) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail(ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, fail("Server error"));
        }
    }

    // expects JSON body: { "reservationNo": "RES-..." }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try {
            BillingRequest body = readJsonBody(req, BillingRequest.class);
            if (body == null || isBlank(body.reservationNo)) {
                writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail("reservationNo is required"));
                return;
            }

            InvoiceResponseDTO out = billingController.generateInvoice(body.reservationNo.trim());
            writeJson(resp, HttpServletResponse.SC_OK, out);

        } catch (JsonProcessingException ex) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail("Invalid JSON"));
        } catch (IllegalArgumentException ex) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST, fail(ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, fail("Server error"));
        }
    }

    // ------- helpers -------

    private static class BillingRequest {
        public String reservationNo; // keep it simple for Jackson
    }

    private <T> T readJsonBody(HttpServletRequest req, Class<T> clazz) throws IOException {
        byte[] bytes = req.getInputStream().readAllBytes();
        if (bytes.length == 0) throw new IllegalArgumentException("Missing JSON body");
        return mapper.readValue(bytes, clazz);
    }

    private void writeJson(HttpServletResponse resp, int status, InvoiceResponseDTO dto) throws IOException {
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(status);
        mapper.writeValue(resp.getOutputStream(), dto);
    }

    private InvoiceResponseDTO fail(String message) {
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        dto.setMessage(message);
        return dto;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}