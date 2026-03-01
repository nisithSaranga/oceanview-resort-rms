package com.oceanview.resort.mapper;

import com.oceanview.resort.dto.LoginResponseDTO;
import com.oceanview.resort.enums.UserRole;

public final class AuthMapper {

    private AuthMapper() {
        // utility class
    }
    public static LoginResponseDTO toLoginResponseDTO(
            boolean success,
            Integer userId,
            String username,
            UserRole role,
            String message
    ) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setSuccess(success);
        dto.setUserId(userId);
        dto.setUsername(username);
        dto.setRole(role);
        dto.setMessage(message);
        return dto;
    }

    public static LoginResponseDTO fail(String message) {
        return toLoginResponseDTO(false, null, null, null, message);
    }

    public static LoginResponseDTO ok(Integer userId, String username, UserRole role, String message) {
        return toLoginResponseDTO(true, userId, username, role, message);
    }
}