package com.oceanview.resort.dao.impl;

import com.oceanview.resort.dao.InvoiceLineItemDAO;
import com.oceanview.resort.entity.InvoiceLineItem;

import java.sql.SQLException;
import java.util.List;

public class InvoiceLineItemDAOImpl implements InvoiceLineItemDAO {
    @Override
    public int[] createBatch(List<InvoiceLineItem> lineItems) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");
    }

    @Override
    public List<InvoiceLineItem> findByInvoiceId(int invoiceId) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");
    }
}
