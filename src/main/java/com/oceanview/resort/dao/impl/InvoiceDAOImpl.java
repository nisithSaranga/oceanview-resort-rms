package com.oceanview.resort.dao.impl;

import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.InvoiceDAO;
import com.oceanview.resort.entity.Invoice;

import java.sql.SQLException;
import java.util.Optional;

public class InvoiceDAOImpl implements InvoiceDAO {

    private final DBConnectionManager db;

    public InvoiceDAOImpl() {
        this.db = DBConnectionManager.getInstance();
    }

    @Override
    public int create(Invoice invoice) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement create");

    }

    @Override
    public Optional<Invoice> findById(int invoiceId) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement findById");
    }

    @Override
    public Optional<Invoice> findByReservationNo(String reservationNo) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement findByReservationNo");
    }
}
