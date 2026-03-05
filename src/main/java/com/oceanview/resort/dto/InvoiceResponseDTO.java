package com.oceanview.resort.dto;

import java.math.BigDecimal;

public class InvoiceResponseDTO {

    private int invoiceId;
    private String reservationNo;
    private BigDecimal totalAmount;
    private String message;

    public InvoiceResponseDTO() {
    }

    public InvoiceResponseDTO(int invoiceId, String reservationNo, BigDecimal totalAmount, String message) {
        this.invoiceId = invoiceId;
        this.reservationNo = reservationNo;
        this.totalAmount = totalAmount;
        this.message = message;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

