-- Script SQL để tạo bảng advisory_requests
-- Bảng này lưu trữ các yêu cầu tư vấn từ khách hàng

-- Tạo database nếu chưa tồn tại (uncomment nếu cần)
-- CREATE DATABASE IF NOT EXISTS gym_management;
-- USE gym_management;

-- Tạo bảng advisory_requests
CREATE TABLE IF NOT EXISTS advisory_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID tự động tăng',
    full_name VARCHAR(255) NOT NULL COMMENT 'Họ và tên khách hàng',
    phone VARCHAR(20) NOT NULL COMMENT 'Số điện thoại',
    email VARCHAR(255) NOT NULL COMMENT 'Địa chỉ email',
    address TEXT NOT NULL COMMENT 'Địa chỉ',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo yêu cầu',
    
    -- Index để tìm kiếm nhanh hơn
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Bảng lưu trữ yêu cầu tư vấn từ khách hàng';

-- Kiểm tra bảng đã được tạo thành công
-- SELECT * FROM advisory_requests LIMIT 10;

