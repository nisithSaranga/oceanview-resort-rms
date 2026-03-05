-- Seed data placeholder (Commit 2/DB setup)
-- OceanView Resort RMS - Seed data (Day 1)
USE oceanview_resort;

-- Seed system users (hashed)
DELETE FROM system_user WHERE username IN ('admin', 'staff1');

INSERT INTO system_user (username, password_hash, role, active)
VALUES
    ('admin',  '<_ADMIN_HASH_>',  'ADMIN', 1),
    ('staff1', '<_STAFF_HASH_>',  'STAFF',  1);

INSERT INTO room (room_number, room_type, base_rate_per_night, available)
VALUES
    ('101', 'STANDARD', 120.00, 1),
    ('102', 'DELUXE', 180.00, 1),
    ('201', 'SUITE', 320.00, 1);
