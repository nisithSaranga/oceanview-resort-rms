package com.oceanview.resort.service;

import com.oceanview.resort.dto.LoginRequestDTO;
import com.oceanview.resort.dto.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO req);
}
