package com.oceanview.resort.dto;

import com.oceanview.resort.enums.UserRole;

public class LoginResponseDTO {

    private boolean success;
    private Integer userId;
    private String username;
    private UserRole role;
    private String message;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(boolean success, Integer userId, String username, UserRole role, String message) {
        this.success = success;
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoginResponseDTO{" +
                "success=" + success +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", message='" + message + '\'' +
                '}';
    }
}

