-- Seed data placeholder (Commit 2/DB setup)
-- OceanView Resort RMS - Seed data (Day 1)
USE oceanview_resort;

INSERT INTO system_user (username, password_hash, role, active)
VALUES
    ('admin', 'admin123', 'ADMIN', 1),
    ('staff1', 'staff123', 'STAFF', 1);

INSERT INTO room (room_number, room_type, base_rate_per_night, available)
VALUES
    ('101', 'STANDARD', 120.00, 1),
    ('102', 'DELUXE', 180.00, 1),
    ('201', 'SUITE', 320.00, 1);
