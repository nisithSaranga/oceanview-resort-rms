package com.oceanview.resort.dao.impl;

import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.InvoiceLineItemDAO;
import com.oceanview.resort.entity.InvoiceLineItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvoiceLineItemDAOImpl implements InvoiceLineItemDAO {

    private final DBConnectionManager db;

    public InvoiceLineItemDAOImpl() {
        this.db = DBConnectionManager.getInstance();
    }

    @Override
    public void saveAll(List<InvoiceLineItem> items) throws SQLException {
        if (items == null || items.isEmpty()) return;

        for (InvoiceLineItem it : items) {
            Objects.requireNonNull(it, "line item must not be null");
            if (it.getInvoiceId() <= 0) throw new IllegalArgumentException("invoiceId is required for line item");
            if (it.getDescription() == null || it.getDescription().trim().isEmpty())
                throw new IllegalArgumentException("description is required for line item");
            if (it.getRatePerNight() == null || it.getSubTotal() == null)
                throw new IllegalArgumentException("ratePerNight and subTotal are required for line item");
        }

        final String sql =
                "INSERT INTO invoice_line_item (invoice_id, description, nights, rate_per_night, sub_total) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (InvoiceLineItem it : items) {
                ps.setInt(1, it.getInvoiceId());
                ps.setString(2, it.getDescription());
                ps.setInt(3, it.getNights());
                ps.setBigDecimal(4, it.getRatePerNight());
                ps.setBigDecimal(5, it.getSubTotal());
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    @Override
    public List<InvoiceLineItem> findByInvoiceId(int invoiceId) throws SQLException {
        final String sql =
                "SELECT line_item_id, invoice_id, description, nights, rate_per_night, sub_total " +
                        "FROM invoice_line_item WHERE invoice_id = ? ORDER BY line_item_id";

        List<InvoiceLineItem> out = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, invoiceId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InvoiceLineItem it = new InvoiceLineItem(
                            rs.getInt("line_item_id"),
                            rs.getInt("invoice_id"),
                            rs.getString("description"),
                            rs.getInt("nights"),
                            rs.getBigDecimal("rate_per_night"),
                            rs.getBigDecimal("sub_total")
                    );
                    out.add(it);
                }
            }
        }

        return out;
    }
}

