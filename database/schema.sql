-- OceanView Resort RMS - Schema setup (Day 1 / DB setup)

CREATE DATABASE IF NOT EXISTS oceanview_resort;
USE oceanview_resort;

DROP TABLE IF EXISTS system_user;
DROP TABLE IF EXISTS room;

-- =========================
-- Table: system_user
-- =========================
CREATE TABLE system_user (
                             user_id INT AUTO_INCREMENT PRIMARY KEY,
                             username VARCHAR(50) NOT NULL UNIQUE,
                             password_hash VARCHAR(255) NOT NULL,
                             role ENUM('ADMIN', 'STAFF') NOT NULL,
                             active BOOLEAN NOT NULL DEFAULT TRUE,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- Table: room
-- Aligns with UML Room entity
-- =========================
CREATE TABLE room (
                      room_id INT AUTO_INCREMENT PRIMARY KEY,
                      room_number VARCHAR(20) NOT NULL UNIQUE,
                      room_type ENUM('STANDARD', 'DELUXE', 'SUITE') NOT NULL,
                      base_rate_per_night DECIMAL(10,2) NOT NULL,
                      available BOOLEAN NOT NULL DEFAULT TRUE
);
