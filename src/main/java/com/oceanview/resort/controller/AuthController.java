package com.oceanview.resort.controller;

import com.oceanview.resort.dto.LoginRequestDTO;
import com.oceanview.resort.dto.LoginResponseDTO;
import com.oceanview.resort.service.AuthService;

import java.util.Objects;

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = Objects.requireNonNull(authService, "authService must not be null");
    }

    public LoginResponseDTO login(LoginRequestDTO req) {
        try {
            return authService.login(req);
        } catch (RuntimeException ex) {
            return fail("Login failed due to a server error");
        }
    }

    private static LoginResponseDTO fail(String message) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setSuccess(false);
        dto.setUserId(null);
        dto.setUsername(null);
        dto.setRole(null);
        dto.setMessage(message);
        return dto;
    }
}