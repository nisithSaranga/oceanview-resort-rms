package com.oceanview.resort.dao;

import com.oceanview.resort.entity.Invoice;

import java.sql.SQLException;
import java.util.Optional;

public interface InvoiceDAO {
    int create(Invoice invoice) throws SQLException;
    Optional<Invoice> findById(int invoiceId) throws SQLException;
    Optional<Invoice> findByReservationNo(String reservationNo) throws SQLException;
}
