package com.oceanview.resort.entity;

import java.math.BigDecimal;

public class InvoiceLineItem {

    private int lineItemId;
    private String description;
    private int nights;
    private BigDecimal ratePerNight;
    private BigDecimal subTotal;

    public InvoiceLineItem() {
    }

    public InvoiceLineItem(String description, int nights, BigDecimal ratePerNight, BigDecimal subTotal) {
        this.description = description;
        this.nights = nights;
        this.ratePerNight = ratePerNight;
        this.subTotal = subTotal;
    }

    public InvoiceLineItem(int lineItemId, String description, int nights, BigDecimal ratePerNight, BigDecimal subTotal) {
        this.lineItemId = lineItemId;
        this.description = description;
        this.nights = nights;
        this.ratePerNight = ratePerNight;
        this.subTotal = subTotal;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }
}

