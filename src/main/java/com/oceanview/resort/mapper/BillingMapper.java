package com.oceanview.resort.mapper;

import com.oceanview.resort.dto.InvoiceResponseDTO;
import com.oceanview.resort.entity.Invoice;
import com.oceanview.resort.entity.InvoiceLineItem;

import java.util.List;

public class BillingMapper {

    private BillingMapper() {
    }

    public static InvoiceResponseDTO toInvoiceResponseDTO(Invoice invoice, List<InvoiceLineItem> lineItems) {
        throw new UnsupportedOperationException("TODO: implement BillingMapper.toInvoiceResponseDTO");
    }
}
