package com.oceanview.resort.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceLineItem {

    private int lineItemId;
    private int invoiceId;                 // FK to invoice.invoice_id
    private String description;
    private int nights;
    private BigDecimal ratePerNight;
    private BigDecimal subTotal;

    public InvoiceLineItem() { }

    // create-before-insert (lineItemId unknown yet)
    public InvoiceLineItem(int invoiceId,
                           String description,
                           int nights,
                           BigDecimal ratePerNight,
                           BigDecimal subTotal) {
        this.invoiceId = invoiceId;
        this.description = requireText(description, "description");
        this.nights = nights;
        this.ratePerNight = Objects.requireNonNull(ratePerNight, "ratePerNight must not be null");
        this.subTotal = Objects.requireNonNull(subTotal, "subTotal must not be null");
    }

    // read-from-db
    public InvoiceLineItem(int lineItemId,
                           int invoiceId,
                           String description,
                           int nights,
                           BigDecimal ratePerNight,
                           BigDecimal subTotal) {
        this.lineItemId = lineItemId;
        this.invoiceId = invoiceId;
        this.description = requireText(description, "description");
        this.nights = nights;
        this.ratePerNight = Objects.requireNonNull(ratePerNight, "ratePerNight must not be null");
        this.subTotal = Objects.requireNonNull(subTotal, "subTotal must not be null");
    }

    public int getLineItemId() { return lineItemId; }
    public int getInvoiceId() { return invoiceId; }
    public String getDescription() { return description; }
    public int getNights() { return nights; }
    public BigDecimal getRatePerNight() { return ratePerNight; }

    // ✅ UML explicitly shows this one
    public BigDecimal getSubTotal() { return subTotal; }

    // minimal setters for DAO convenience (same style as Invoice)
    public void setLineItemId(int lineItemId) { this.lineItemId = lineItemId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    private static String requireText(String s, String field) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return s.trim();
    }
}