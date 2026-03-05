package com.oceanview.resort.mapper;

import com.oceanview.resort.dto.InvoiceResponseDTO;
import com.oceanview.resort.entity.Invoice;

public final class InvoiceMapper {

    private InvoiceMapper() {}

    public static InvoiceResponseDTO toResponseDTO(Invoice inv) {
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        if (inv == null) return dto;

        dto.setInvoiceId(inv.getInvoiceId());
        dto.setTotalAmount(inv.getTotalAmount());

        if (inv.getReservation() != null) {
            dto.setReservationNo(inv.getReservation().getReservationNo());
        }
        return dto;
    }
}