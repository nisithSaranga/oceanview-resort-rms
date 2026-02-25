-- OceanView Resort RMS schema setup
-- Day 1: create core tables (system_user, room)

CREATE DATABASE IF NOT EXISTS oceanview_resort;
USE oceanview_resort;

-- 1) System users (Admin/Staff)
CREATE TABLE IF NOT EXISTS system_user (
                                           user_id INT AUTO_INCREMENT PRIMARY KEY,
                                           username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,   -- SHA-256 hex = 64 chars
    role VARCHAR(10) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_user_role CHECK (role IN ('ADMIN','STAFF'))
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2) Rooms (availability + base rate)
CREATE TABLE IF NOT EXISTS room (
                                    room_id INT AUTO_INCREMENT PRIMARY KEY,
                                    room_number VARCHAR(10) NOT NULL UNIQUE,
    room_type VARCHAR(10) NOT NULL,
    base_rate_per_night DECIMAL(10,2) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_room_type CHECK (room_type IN ('STANDARD','DELUXE','SUITE'))
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;