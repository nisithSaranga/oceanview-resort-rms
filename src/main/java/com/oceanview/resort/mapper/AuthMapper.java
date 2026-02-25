package com.oceanview.resort.mapper;

import com.oceanview.resort.dto.LoginResponseDTO;
import com.oceanview.resort.entity.SystemUser;

public class AuthMapper {

    private AuthMapper() {
    }

    public static LoginResponseDTO toLoginResponseDTO(SystemUser user) {
        throw new UnsupportedOperationException("TODO: implement AuthMapper.toLoginResponseDTO");
    }
}
