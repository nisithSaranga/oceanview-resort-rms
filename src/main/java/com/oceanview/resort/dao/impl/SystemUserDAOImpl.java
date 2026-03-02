package com.oceanview.resort.dao.impl;

import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.SystemUserDAO;
import com.oceanview.resort.entity.SystemUser;
import com.oceanview.resort.enums.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                "SELECT user_id, username, password_hash, role, active " +
                        "FROM system_user " +
                        "WHERE username = ? " +
                        "LIMIT 1";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapRow(rs));
            }
        } catch (IllegalArgumentException ex) {
            // If role in DB is invalid/unexpected -> treat as not found (safe failure)
            return Optional.empty();
        }
    }

    @Override
    public Optional<SystemUser> findById(Integer userId) throws SQLException {
        if (userId <= 0) {
            return Optional.empty();
        }

        final String sql =
                "SELECT user_id, username, password_hash, role, active " +
                        "FROM system_user " +
                        "WHERE user_id = ? " +
                        "LIMIT 1";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapRow(rs));
            }
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    private SystemUser mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String username = rs.getString("username");
        String passwordHash = rs.getString("password_hash");

        String roleRaw = rs.getString("role");
        if (roleRaw == null) {
            throw new IllegalArgumentException("role is null");
        }
        UserRole role = UserRole.valueOf(roleRaw.trim().toUpperCase());

        boolean active = rs.getBoolean("active");

        return new SystemUser(id, username, passwordHash, role, active);
    }
}
