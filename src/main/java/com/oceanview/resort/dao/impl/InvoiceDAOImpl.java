package com.oceanview.resort.dao.impl;

import com.oceanview.resort.dao.InvoiceDAO;
import com.oceanview.resort.entity.Invoice;

import java.sql.SQLException;
import java.util.Optional;

public class InvoiceDAOImpl implements InvoiceDAO {
    @Override
    public int create(Invoice invoice) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");
    }
    @Override
    public Optional<Invoice> findById(int invoiceId) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");
    }

    @Override
    public Optional<Invoice> findByReservationNo(String reservationNo) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement <methodName>");
    }
}
