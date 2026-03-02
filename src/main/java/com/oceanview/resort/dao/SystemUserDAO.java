package com.oceanview.resort.dao;

import com.oceanview.resort.entity.SystemUser;

import java.sql.SQLException;
import java.util.Optional;

public interface SystemUserDAO {
    Optional<SystemUser> findByUsername(String username) throws SQLException;
    Optional<SystemUser> findById(Integer userId) throws SQLException;
}
