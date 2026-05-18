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

-- Room data (77 rooms total)
-- Floor 1: 18 standard rooms (101-118), price 198
-- Floor 2: 28 standard rooms (201-228), price 228
-- Floor 3: 12 suite rooms (301-312), price 398
-- Floor 4: 19 suite rooms (401-419), price 498
INSERT INTO room (room_number, room_type, floor, status, price) VALUES
('101', 'standard', 1, 'available', 198.00),
('102', 'standard', 1, 'available', 198.00),
('103', 'standard', 1, 'available', 198.00),
('104', 'standard', 1, 'available', 198.00),
('105', 'standard', 1, 'available', 198.00),
('106', 'standard', 1, 'available', 198.00),
('107', 'standard', 1, 'available', 198.00),
('108', 'standard', 1, 'available', 198.00),
('109', 'standard', 1, 'available', 198.00),
('110', 'standard', 1, 'available', 198.00),
('111', 'standard', 1, 'available', 198.00),
('112', 'standard', 1, 'available', 198.00),
('113', 'standard', 1, 'available', 198.00),
('114', 'standard', 1, 'available', 198.00),
('115', 'standard', 1, 'available', 198.00),
('116', 'standard', 1, 'available', 198.00),
('117', 'standard', 1, 'available', 198.00),
('118', 'standard', 1, 'available', 198.00),
('201', 'standard', 2, 'available', 228.00),
('202', 'standard', 2, 'available', 228.00),
('203', 'standard', 2, 'available', 228.00),
('204', 'standard', 2, 'available', 228.00),
('205', 'standard', 2, 'available', 228.00),
('206', 'standard', 2, 'available', 228.00),
('207', 'standard', 2, 'available', 228.00),
('208', 'standard', 2, 'available', 228.00),
('209', 'standard', 2, 'available', 228.00),
('210', 'standard', 2, 'available', 228.00),
('211', 'standard', 2, 'available', 228.00),
('212', 'standard', 2, 'available', 228.00),
('213', 'standard', 2, 'available', 228.00),
('214', 'standard', 2, 'available', 228.00),
('215', 'standard', 2, 'available', 228.00),
('216', 'standard', 2, 'available', 228.00),
('217', 'standard', 2, 'available', 228.00),
('218', 'standard', 2, 'available', 228.00),
('219', 'standard', 2, 'available', 228.00),
('220', 'standard', 2, 'available', 228.00),
('221', 'standard', 2, 'available', 228.00),
('222', 'standard', 2, 'available', 228.00),
('223', 'standard', 2, 'available', 228.00),
('224', 'standard', 2, 'available', 228.00),
('225', 'standard', 2, 'available', 228.00),
('226', 'standard', 2, 'available', 228.00),
('227', 'standard', 2, 'available', 228.00),
('228', 'standard', 2, 'available', 228.00),
('301', 'suite', 3, 'available', 398.00),
('302', 'suite', 3, 'available', 398.00),
('303', 'suite', 3, 'available', 398.00),
('304', 'suite', 3, 'available', 398.00),
('305', 'suite', 3, 'available', 398.00),
('306', 'suite', 3, 'available', 398.00),
('307', 'suite', 3, 'available', 398.00),
('308', 'suite', 3, 'available', 398.00),
('309', 'suite', 3, 'available', 398.00),
('310', 'suite', 3, 'available', 398.00),
('311', 'suite', 3, 'available', 398.00),
('312', 'suite', 3, 'available', 398.00),
('401', 'suite', 4, 'available', 498.00),
('402', 'suite', 4, 'available', 498.00),
('403', 'suite', 4, 'available', 498.00),
('404', 'suite', 4, 'available', 498.00),
('405', 'suite', 4, 'available', 498.00),
('406', 'suite', 4, 'available', 498.00),
('407', 'suite', 4, 'available', 498.00),
('408', 'suite', 4, 'available', 498.00),
('409', 'suite', 4, 'available', 498.00),
('410', 'suite', 4, 'available', 498.00),
('411', 'suite', 4, 'available', 498.00),
('412', 'suite', 4, 'available', 498.00),
('413', 'suite', 4, 'available', 498.00),
('414', 'suite', 4, 'available', 498.00),
('415', 'suite', 4, 'available', 498.00),
('416', 'suite', 4, 'available', 498.00),
('417', 'suite', 4, 'available', 498.00),
('418', 'suite', 4, 'available', 498.00),
('419', 'suite', 4, 'available', 498.00);

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