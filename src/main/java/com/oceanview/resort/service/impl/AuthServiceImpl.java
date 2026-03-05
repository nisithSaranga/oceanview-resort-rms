package com.oceanview.resort.service.impl;

import com.oceanview.resort.dao.SystemUserDAO;
import com.oceanview.resort.dto.LoginRequestDTO;
import com.oceanview.resort.dto.LoginResponseDTO;
import com.oceanview.resort.entity.SystemUser;
import com.oceanview.resort.enums.UserRole;
import com.oceanview.resort.mapper.AuthMapper;
import com.oceanview.resort.service.AuthService;
import com.oceanview.resort.util.PasswordHashUtil;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class AuthServiceImpl implements AuthService {

    private final SystemUserDAO systemUserDAO;

    public AuthServiceImpl(SystemUserDAO systemUserDAO) {
        this.systemUserDAO = Objects.requireNonNull(systemUserDAO, "systemUserDAO must not be null");
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO req) {
        if (req == null) {
            return AuthMapper.fail("Request body is missing");
        }

        String username = safeTrim(req.getUsername());
        String password = req.getPassword(); // do NOT trim passwords

        if (username == null || username.isBlank()) {
            return AuthMapper.fail("Username is required");
        }
        if (password == null || password.isBlank()) {
            return AuthMapper.fail("Password is required");
        }

        final Optional<SystemUser> userOpt;
        try {
            userOpt = systemUserDAO.findByUsername(username);
        } catch (SQLException ex) {
            return AuthMapper.fail("Invalid credentials");
        }

        if (userOpt.isEmpty()) {
            return AuthMapper.fail("Invalid credentials");
        }

        SystemUser user = userOpt.get();

        if (!user.isActive()) {
            return AuthMapper.fail("Invalid credentials");
        }

        final boolean ok;
        try {
            ok = PasswordHashUtil.verify(password, user.getPasswordHash());
        } catch (RuntimeException ex) {
            return AuthMapper.fail("Invalid credentials");
        }

        if (!ok) {
            return AuthMapper.fail("Invalid credentials");
        }

        UserRole role = user.getRole();
        return AuthMapper.ok(user.getUserId(), user.getUsername(), role, "Login successful");
    }

    private String safeTrim(String s) {
        return (s == null) ? null : s.trim();
    }
}