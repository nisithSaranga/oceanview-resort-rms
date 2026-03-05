package com.oceanview.resort.dao;

import com.oceanview.resort.entity.Invoice;

import java.sql.SQLException;
import java.util.Optional;

public interface InvoiceDAO {
    int save(Invoice inv) throws SQLException;
    Optional<Invoice> findByReservationNo(String reservationNo) throws SQLException;
}
