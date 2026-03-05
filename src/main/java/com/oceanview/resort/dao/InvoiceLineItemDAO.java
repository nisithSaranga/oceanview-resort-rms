package com.oceanview.resort.dao;

import com.oceanview.resort.entity.InvoiceLineItem;

import java.sql.SQLException;
import java.util.List;

public interface InvoiceLineItemDAO {

    void saveAll(List<InvoiceLineItem> items) throws SQLException;

    List<InvoiceLineItem> findByInvoiceId(int invoiceId) throws SQLException;
}