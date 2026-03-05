package com.oceanview.resort.controller;

import com.oceanview.resort.dto.InvoiceResponseDTO;
import com.oceanview.resort.service.BillingService;

import java.util.Objects;

public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = Objects.requireNonNull(billingService, "billingService must not be null");
    }

    public InvoiceResponseDTO generateInvoice(String reservationNo) {
        return billingService.generateInvoice(reservationNo);
    }
}