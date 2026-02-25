package com.oceanview.resort.dao.impl;

import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.InvoiceLineItemDAO;
import com.oceanview.resort.entity.InvoiceLineItem;

import java.sql.SQLException;
import java.util.List;

public class InvoiceLineItemDAOImpl implements InvoiceLineItemDAO {

    private final DBConnectionManager db;

    public InvoiceLineItemDAOImpl() {
        this.db = DBConnectionManager.getInstance();
    }
    @Override
    public int[] createBatch(List<InvoiceLineItem> lineItems) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement createBatch");
    }

    @Override
    public List<InvoiceLineItem> findByInvoiceId(int invoiceId) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement findByInvoiceId");
    }
}

