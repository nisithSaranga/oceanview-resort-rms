package com.oceanview.resort.service.impl;

import com.oceanview.resort.dao.InvoiceDAO;
import com.oceanview.resort.dao.InvoiceLineItemDAO;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dto.InvoiceResponseDTO;
import com.oceanview.resort.entity.Invoice;
import com.oceanview.resort.entity.InvoiceLineItem;
import com.oceanview.resort.entity.Reservation;
import com.oceanview.resort.entity.Room;
import com.oceanview.resort.enums.ReservationStatus;
import com.oceanview.resort.enums.RoomType;
import com.oceanview.resort.mapper.InvoiceMapper;
import com.oceanview.resort.service.BillingService;
import com.oceanview.resort.strategy.DeluxePricingStrategy;
import com.oceanview.resort.strategy.PricingStrategy;
import com.oceanview.resort.strategy.StandardPricingStrategy;
import com.oceanview.resort.strategy.SuitePricingStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BillingServiceImpl implements BillingService {

    private final ReservationDAO reservationDAO;
    private final InvoiceDAO invoiceDAO;
    private final InvoiceLineItemDAO invoiceLineItemDAO;

    private final Map<RoomType, PricingStrategy> strategies = new EnumMap<>(RoomType.class);

    public BillingServiceImpl(ReservationDAO reservationDAO,
                              InvoiceDAO invoiceDAO,
                              InvoiceLineItemDAO invoiceLineItemDAO) {

        this.reservationDAO = Objects.requireNonNull(reservationDAO, "reservationDAO must not be null");
        this.invoiceDAO = Objects.requireNonNull(invoiceDAO, "invoiceDAO must not be null");
        this.invoiceLineItemDAO = Objects.requireNonNull(invoiceLineItemDAO, "invoiceLineItemDAO must not be null");

        strategies.put(RoomType.STANDARD, new StandardPricingStrategy());
        strategies.put(RoomType.DELUXE, new DeluxePricingStrategy());
        strategies.put(RoomType.SUITE, new SuitePricingStrategy());
    }

    @Override
    public InvoiceResponseDTO generateInvoice(String reservationNo) {
        if (isBlank(reservationNo)) return fail("reservationNo is required");
        String resNo = reservationNo.trim();

        try {
            // 1) Retrieve reservation
            Reservation res;
            try {
                res = reservationDAO.findByReservationNo(resNo);
            } catch (SQLException ex) {
                return fail("Reservation not found");
            }
            if (res.getStatus() == ReservationStatus.CANCELLED) {
                return fail("Cannot generate invoice for CANCELLED reservation");
            }

            Optional<Invoice> existingOpt = invoiceDAO.findByReservationNo(resNo);
            if (existingOpt.isPresent()) {
                Invoice existing = existingOpt.get();
                InvoiceResponseDTO dto = InvoiceMapper.toResponseDTO(existing);
                dto.setMessage("Invoice already exists");
                return dto;
            }

            // 2) Calculate nights
            int nights = calculateNights(res.getCheckIn(), res.getCheckOut());
            if (nights <= 0) return fail("Invalid reservation dates");

            // 3) Calculate total (Strategy)
            Room room = res.getRoom();
            if (room == null || room.getRoomType() == null) return fail("Reservation has no room");

            PricingStrategy strategy = getStrategy(room.getRoomType());
            BigDecimal baseRate = room.getBaseRatePerNight(); // must exist on Room entity
            if (baseRate == null) return fail("Room base rate missing");

            BigDecimal total = strategy.calculateTotal(baseRate, nights)
                    .setScale(2, RoundingMode.HALF_UP);

            // 4) Save invoice
            Invoice inv = new Invoice(LocalDateTime.now(), total, res);
            int invoiceId = invoiceDAO.save(inv); // UML: save(inv): int

            // Keep in-memory object consistent (helps mapping)
            inv.setInvoiceId(invoiceId);
            if (inv.getIssuedAt() == null) inv.setIssuedAt(LocalDateTime.now());

            // 5) Save line items (1 item is enough for this assignment)
            String desc = room.getRoomType().name() + " room (" + nights + " nights)";
            InvoiceLineItem li = new InvoiceLineItem(
                    0,                 // lineItemId unknown before insert
                    invoiceId,
                    desc,
                    nights,
                    baseRate,
                    total
            );

            invoiceLineItemDAO.saveAll(Collections.singletonList(li)); // UML: saveAll(items)

            // Response
            InvoiceResponseDTO out = InvoiceMapper.toResponseDTO(inv);
            out.setInvoiceId(invoiceId);
            out.setReservationNo(resNo);
            out.setTotalAmount(total);
            out.setMessage("Invoice generated");
            return out;

        } catch (SQLException ex) {
            return fail("Invoice generation failed");
        }
    }

    // --- helpers (allowed even if not on UML) ---

    private PricingStrategy getStrategy(RoomType type) {
        PricingStrategy s = strategies.get(type);
        if (s == null) throw new IllegalArgumentException("No strategy for room type: " + type);
        return s;
    }

    private int calculateNights(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) return 0;
        return (int) ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    private InvoiceResponseDTO fail(String msg) {
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        dto.setMessage(msg);
        return dto;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}