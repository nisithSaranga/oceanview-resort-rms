package com.oceanview.resort.dao.impl;

<<<<<<< Updated upstream
import com.oceanview.resort.dao.SystemUserDAO;
import com.oceanview.resort.entity.SystemUser;

import java.sql.SQLException;
import java.util.Optional;

public class SystemUserDAOImpl implements SystemUserDAO {

    @Override
    public Optional<SystemUser> findByUsername(String username) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement findByUsername");
    }

    @Override
    public Optional<SystemUser> findById(int userId) throws SQLException {
        throw new UnsupportedOperationException("TODO: implement findById");
    }
=======
import com.oceanview.resort.config.DBConnectionManager;
import com.oceanview.resort.dao.SystemUserDAO;

public class SystemUserDAOImpl implements SystemUserDAO {
    private final DBConnectionManager db;

    public SystemUserDAOImpl() {
        this.db = DBConnectionManager.getInstance();
    }

>>>>>>> Stashed changes
}
