package com.oceanview.resort.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Invoice {

    private int invoiceId;
    private LocalDateTime issuedAt;
    private BigDecimal totalAmount;
    private Reservation reservation;

    public Invoice() { }

    // create-before-insert
    public Invoice(LocalDateTime issuedAt, BigDecimal totalAmount, Reservation reservation) {
        this.issuedAt = Objects.requireNonNull(issuedAt, "issuedAt must not be null");
        this.totalAmount = Objects.requireNonNull(totalAmount, "totalAmount must not be null");
        this.reservation = Objects.requireNonNull(reservation, "reservation must not be null");
    }

    // read-from-db
    public Invoice(int invoiceId, LocalDateTime issuedAt, BigDecimal totalAmount, Reservation reservation) {
        this.invoiceId = invoiceId;
        this.issuedAt = Objects.requireNonNull(issuedAt, "issuedAt must not be null");
        this.totalAmount = Objects.requireNonNull(totalAmount, "totalAmount must not be null");
        this.reservation = Objects.requireNonNull(reservation, "reservation must not be null");
    }

    public int getInvoiceId() { return invoiceId; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Reservation getReservation() { return reservation; }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }
}