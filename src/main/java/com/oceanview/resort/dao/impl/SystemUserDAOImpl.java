package com.oceanview.resort.dao.impl;

import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.SystemUserDAO;
import com.oceanview.resort.entity.SystemUser;
import com.oceanview.resort.enums.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class SystemUserDAOImpl implements SystemUserDAO {

    private final DBConnectionManager db;

    public SystemUserDAOImpl() {
        this.db = DBConnectionManager.getInstance();
    }

    @Override
    public Optional<SystemUser> findByUsername(String username) throws SQLException {
        if (username == null || username.isBlank()) {
            return Optional.empty();
        }

        final String sql =
                "SELECT user_id, username, password_hash, role, active, created_at " +
                        "FROM system_user " +
                        "WHERE username = ? " +
                        "LIMIT 1";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        }
    }

    @Override
    public Optional<SystemUser> findById(int userId) throws SQLException {
        if (userId <= 0) {
            return Optional.empty();
        }

        final String sql =
                "SELECT user_id, username, password_hash, role, active, created_at " +
                        "FROM system_user " +
                        "WHERE user_id = ? " +
                        "LIMIT 1";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapRow(rs));
            }
        }
    }

    private SystemUser mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String username = rs.getString("username");
        String passwordHash = rs.getString("password_hash");

        String roleStr = rs.getString("role");
        UserRole role = parseRole(roleStr);

        boolean active = rs.getBoolean("active");

        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime createdAt = (ts != null) ? ts.toLocalDateTime() : null;

        return new SystemUser(id, username, passwordHash, role, active, createdAt);
    }

    private UserRole parseRole(String roleStr) throws SQLException {
        if (roleStr == null || roleStr.isBlank()) {
            throw new SQLException("system_user.role is NULL/blank in DB");
        }
        try {
            return UserRole.valueOf(roleStr.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new SQLException("Invalid role value in DB: " + roleStr, ex);
        }
    }
}

