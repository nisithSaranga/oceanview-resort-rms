package com.oceanview.resort.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionManager {

    private static volatile DBConnectionManager instance;
    private final Properties dbProperties = new Properties();

    private DBConnectionManager() {
        loadProperties();
        loadDriver();
    }

    public static DBConnectionManager getInstance() {
        if (instance == null) {
            synchronized (DBConnectionManager.class) {
                if (instance == null) {
                    instance = new DBConnectionManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                dbProperties.getProperty("db.url"),
                dbProperties.getProperty("db.username"),
                dbProperties.getProperty("db.password")
        );
    }

    private void loadProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("db.properties not found in classpath (src/main/resources)");
            }
            dbProperties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load db.properties", e);
        }
    }

    private void loadDriver() {
        String driverClass = dbProperties.getProperty("db.driver");
        if (driverClass == null || driverClass.isBlank()) {
            throw new IllegalStateException("db.driver is missing in db.properties");
        }

        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC Driver not found: " + driverClass, e);
        }
    }
}