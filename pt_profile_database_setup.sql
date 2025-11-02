-- =====================================================
-- SCRIPT DATABASE HOÀN CHỈNH CHO CHỨC NĂNG PT PROFILE
-- =====================================================

-- Đảm bảo database sử dụng UTF-8
ALTER DATABASE gym_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gym_management;

-- Đảm bảo kết nối sử dụng UTF-8
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- =====================================================
-- 1. XÓA CÁC BẢNG CŨ (NẾU CÓ)
-- =====================================================

-- Xóa bảng pt_certificates trước (vì có foreign key)
DROP TABLE IF EXISTS pt_certificates;

-- Xóa bảng pt_profiles
DROP TABLE IF EXISTS pt_profiles;

SELECT 'OLD_TABLES_DROPPED' as status;

-- =====================================================
-- 2. TẠO LẠI BẢNG PT_PROFILES
-- =====================================================

CREATE TABLE pt_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(20),
    date_of_birth VARCHAR(20),
    gender VARCHAR(10),
    specialization TEXT,
    address TEXT,
    avatar VARCHAR(255),
    experience_years INT DEFAULT 0,
    students_trained INT DEFAULT 0,
    average_rating DECIMAL(3,1) DEFAULT 0.0,
    sessions_this_month INT DEFAULT 0,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_profile (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 3. TẠO BẢNG PT_CERTIFICATES (TÙY CHỌN)
-- =====================================================

CREATE TABLE pt_certificates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pt_profile_id BIGINT NOT NULL,
    certificate_name VARCHAR(100) NOT NULL,
    certificate_type VARCHAR(50) NOT NULL,
    issuing_organization VARCHAR(100),
    certificate_number VARCHAR(50),
    issue_date TIMESTAMP NULL,
    expiry_date TIMESTAMP NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    certificate_image VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pt_profile_id) REFERENCES pt_profiles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 4. KIỂM TRA CẤU TRÚC BẢNG
-- =====================================================

SELECT 'TABLE_STRUCTURE_CHECK:' as status;

-- Kiểm tra bảng đã được tạo
SHOW TABLES LIKE 'pt_%';

-- Kiểm tra cấu trúc bảng pt_profiles
DESCRIBE pt_profiles;

-- Kiểm tra cấu trúc bảng pt_certificates
DESCRIBE pt_certificates;

-- =====================================================
-- 5. KIỂM TRA USERS CÓ ROLE PT
-- =====================================================

SELECT 'PT_USERS_CHECK:' as status;

SELECT u.id, u.username, u.email, u.password_hash, u.salt, GROUP_CONCAT(r.role_name) as roles
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
WHERE r.role_name = 'PT'
GROUP BY u.id, u.username, u.email, u.password_hash, u.salt;

-- =====================================================
-- 6. TẠO DỮ LIỆU MẪU CHO TEST (TÙY CHỌN)
-- =====================================================

-- Lấy user_id của PT đầu tiên
SET @pt_user_id = (SELECT u.id FROM users u 
                   JOIN user_roles ur ON u.id = ur.user_id 
                   JOIN roles r ON ur.role_id = r.id 
                   WHERE r.role_name = 'PT' 
                   LIMIT 1);

-- Tạo profile mẫu nếu có PT user
INSERT IGNORE INTO pt_profiles 
(user_id, full_name, email, phone_number, date_of_birth, gender, specialization, address, 
 experience_years, students_trained, average_rating, sessions_this_month)
SELECT 
    @pt_user_id,
    CONCAT('PT ', u.username),
    u.email,
    '0123456789',
    '1990-01-01',
    'Nam',
    'Cardio, Yoga, Bodybuilding',
    '123 Gym Street, City',
    3,
    25,
    4.8,
    12
FROM users u 
WHERE u.id = @pt_user_id;

-- =====================================================
-- 7. KIỂM TRA DỮ LIỆU CUỐI CÙNG
-- =====================================================

SELECT 'FINAL_DATA_CHECK:' as status;

-- Kiểm tra pt_profiles
SELECT * FROM pt_profiles;

-- Kiểm tra pt_certificates (sẽ trống)
SELECT * FROM pt_certificates;

-- =====================================================
-- 8. TEST QUERIES CHO CHỨC NĂNG
-- =====================================================

SELECT 'TEST_QUERIES:' as status;

-- Test query tìm profile theo user_id
SELECT 'Test find profile by user_id:' as test_name;
SELECT * FROM pt_profiles WHERE user_id = @pt_user_id;

-- Test query update profile
SELECT 'Test update profile:' as test_name;
UPDATE pt_profiles 
SET full_name = 'Test Update',
    email = 'test.update@gymfit.vn',
    phone_number = '0987654321',
    specialization = 'Updated Specialization',
    updated_date = NOW()
WHERE user_id = @pt_user_id;

SELECT CONCAT('Update affected rows: ', ROW_COUNT()) as update_result;

-- Test query update password
SELECT 'Test update password:' as test_name;
UPDATE users 
SET password_hash = '$2a$12$test.new.hash.for.password.change',
    salt = 'new_salt_123',
    updated_date = NOW()
WHERE id = @pt_user_id;

SELECT CONCAT('Password update affected rows: ', ROW_COUNT()) as password_update_result;

-- =====================================================
-- 9. KIỂM TRA KẾT QUẢ CUỐI CÙNG
-- =====================================================

SELECT 'FINAL_RESULTS:' as status;

-- Kiểm tra profile sau khi update
SELECT * FROM pt_profiles WHERE user_id = @pt_user_id;

-- Kiểm tra user sau khi update password
SELECT id, username, email, password_hash, salt, updated_date 
FROM users WHERE id = @pt_user_id;

SELECT 'DATABASE_SETUP_COMPLETED!' as final_status;
