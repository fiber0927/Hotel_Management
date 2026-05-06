-- =====================================================
-- Hotel Management System Database Schema
-- =====================================================

-- Create database
CREATE DATABASE IF NOT EXISTS hotel_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hotel_management;

-- =====================================================
-- 1. Admin table
-- =====================================================
DROP TABLE IF EXISTS admin;
CREATE TABLE admin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Default admin (password: admin123)
INSERT INTO admin (username, password) VALUES ('admin', 'admin123');

-- =====================================================
-- 2. Guest table
-- =====================================================
DROP TABLE IF EXISTS guest;
CREATE TABLE guest (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 3. Room table
-- =====================================================
DROP TABLE IF EXISTS room;
CREATE TABLE room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(10) NOT NULL UNIQUE,
    room_type VARCHAR(20) NOT NULL COMMENT 'standard/suite',
    floor INT NOT NULL,
    status VARCHAR(20) DEFAULT 'available' COMMENT 'available/occupied/maintenance',
    price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Sample room data
INSERT INTO room (room_number, room_type, floor, status, price) VALUES
('101', 'standard', 1, 'available', 198.00),
('102', 'standard', 1, 'available', 198.00),
('103', 'standard', 1, 'available', 198.00),
('201', 'standard', 2, 'available', 228.00),
('202', 'standard', 2, 'available', 228.00),
('301', 'suite', 3, 'available', 398.00),
('302', 'suite', 3, 'available', 398.00),
('401', 'suite', 4, 'available', 498.00);

-- =====================================================
-- 4. Booking table
-- =====================================================
DROP TABLE IF EXISTS booking;
CREATE TABLE booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    guest_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'confirmed' COMMENT 'confirmed/cancelled/completed',
    special_requests TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (guest_id) REFERENCES guest(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 5. Check-in record table
-- =====================================================
DROP TABLE IF EXISTS check_in_record;
CREATE TABLE check_in_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    check_in_time DATETIME NOT NULL,
    check_out_time DATETIME,
    status VARCHAR(20) DEFAULT 'checked_in' COMMENT 'checked_in/checked_out',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES booking(id),
    FOREIGN KEY (room_id) REFERENCES room(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 6. Housekeeping task table
-- =====================================================
DROP TABLE IF EXISTS housekeeping_task;
CREATE TABLE housekeeping_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_id BIGINT NOT NULL,
    task_type VARCHAR(30) NOT NULL COMMENT 'cleaning/repair',
    priority VARCHAR(10) DEFAULT 'normal' COMMENT 'high/normal/low',
    status VARCHAR(20) DEFAULT 'pending' COMMENT 'pending/in_progress/completed',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES room(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 7. Bill table
-- =====================================================
DROP TABLE IF EXISTS bill;
CREATE TABLE bill (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    check_in_id BIGINT NOT NULL,
    room_charge DECIMAL(10,2) DEFAULT 0,
    other_charges DECIMAL(10,2) DEFAULT 0,
    total_amount DECIMAL(10,2) NOT NULL,
    is_paid TINYINT DEFAULT 0 COMMENT '0-unpaid 1-paid',
    payment_method VARCHAR(20) COMMENT 'cash',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    paid_at TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (check_in_id) REFERENCES check_in_record(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;