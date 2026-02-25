package com.oceanview.resort.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Invoice {

    private int invoiceId;
    private LocalDateTime issuedAt;
    private BigDecimal totalAmount;
    private Reservation reservation;

    public Invoice() {
    }

    public Invoice(LocalDateTime issuedAt, BigDecimal totalAmount, Reservation reservation) {
        this.issuedAt = issuedAt;
        this.totalAmount = totalAmount;
        this.reservation = reservation;
    }

    public Invoice(int invoiceId, LocalDateTime issuedAt, BigDecimal totalAmount, Reservation reservation) {
        this.invoiceId = invoiceId;
        this.issuedAt = issuedAt;
        this.totalAmount = totalAmount;
        this.reservation = reservation;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Reservation getReservation() {
        return reservation;
    }
}

