package com.oceanview.resort.dao;

import com.oceanview.resort.entity.InvoiceLineItem;

import java.sql.SQLException;
import java.util.List;

public interface InvoiceLineItemDAO {
    int[] createBatch(List<InvoiceLineItem> lineItems) throws SQLException;
    List<InvoiceLineItem> findByInvoiceId(int invoiceId) throws SQLException;
}
