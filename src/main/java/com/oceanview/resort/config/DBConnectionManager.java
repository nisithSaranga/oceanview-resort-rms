package com.oceanview.resort.config;

import com.mysql.cj.jdbc.Driver;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnectionManager {

    private static final DBConnectionManager INSTANCE = new DBConnectionManager();

    private final String url;
    private final String username;
    private final String password;

    static {
        try {
            // Required in your Tomcat setup: ensure DriverManager can see MySQL driver
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBConnectionManager() {
        Properties props = new Properties();

        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("db.properties")) {

            if (in == null) {
                throw new IllegalStateException("db.properties not found in classpath");
            }
            props.load(in);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load db.properties", e);
        }

        this.url = require(props, "db.url");
        this.username = require(props, "db.username");
        this.password = require(props, "db.password");
    }

    private static String require(Properties props, String key) {
        String v = props.getProperty(key);
        if (v == null || v.trim().isEmpty()) {
            throw new IllegalStateException("Missing required property: " + key);
        }
        return v.trim();
    }

    public static DBConnectionManager getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}