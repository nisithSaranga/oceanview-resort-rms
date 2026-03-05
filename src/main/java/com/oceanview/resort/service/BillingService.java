package com.oceanview.resort.service;

import com.oceanview.resort.dto.InvoiceResponseDTO;

public interface BillingService {
    InvoiceResponseDTO generateInvoice(String reservationNo);
}



