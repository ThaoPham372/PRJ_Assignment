-- =====================================================
-- SCRIPT TEST ENCODING TIẾNG VIỆT
-- =====================================================

USE gym_management;

-- Đảm bảo kết nối UTF-8
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- =====================================================
-- 1. KIỂM TRA CHARSET DATABASE VÀ BẢNG
-- =====================================================

SELECT 'DATABASE_CHARSET:' as status;
SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME 
FROM information_schema.SCHEMATA 
WHERE SCHEMA_NAME = 'gym_management';

SELECT 'TABLE_CHARSET:' as status;
SELECT TABLE_NAME, TABLE_COLLATION 
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'gym_management' 
AND TABLE_NAME IN ('pt_profiles', 'users');

-- =====================================================
-- 2. TEST INSERT DỮ LIỆU TIẾNG VIỆT
-- =====================================================

-- Lấy PT user đầu tiên
SET @pt_user_id = (SELECT u.id FROM users u 
                   JOIN user_roles ur ON u.id = ur.user_id 
                   JOIN roles r ON ur.role_id = r.id 
                   WHERE r.role_name = 'PT' 
                   LIMIT 1);

-- Xóa profile cũ nếu có
DELETE FROM pt_profiles WHERE user_id = @pt_user_id;

-- Insert dữ liệu tiếng Việt
INSERT INTO pt_profiles 
(user_id, full_name, email, phone_number, date_of_birth, gender, specialization, address, 
 experience_years, students_trained, average_rating, sessions_this_month)
VALUES 
(@pt_user_id, 
 'Nguyễn Văn Thảo', 
 'thaopham123@gmail.com', 
 '0935364372', 
 '2005-01-31', 
 'Nam', 
 'Cardio, Yoga, Thể hình', 
 'Bình Thái 4, Cẩm Lệ, Đà Nẵng', 
 3, 25, 4.8, 12);

-- =====================================================
-- 3. KIỂM TRA DỮ LIỆU ĐÃ INSERT
-- =====================================================

SELECT 'VIETNAMESE_DATA_CHECK:' as status;
SELECT * FROM pt_profiles WHERE user_id = @pt_user_id;

-- =====================================================
-- 4. TEST UPDATE DỮ LIỆU TIẾNG VIỆT
-- =====================================================

UPDATE pt_profiles 
SET full_name = 'Phạm Thị Thảo',
    specialization = 'Yoga, Pilates, Aerobic',
    address = '123 Nguyễn Văn Linh, Quận 7, TP.HCM',
    updated_date = NOW()
WHERE user_id = @pt_user_id;

SELECT 'UPDATE_RESULT:' as status;
SELECT CONCAT('Updated rows: ', ROW_COUNT()) as result;

-- =====================================================
-- 5. KIỂM TRA DỮ LIỆU SAU UPDATE
-- =====================================================

SELECT 'FINAL_VIETNAMESE_DATA:' as status;
SELECT id, user_id, full_name, specialization, address, updated_date 
FROM pt_profiles WHERE user_id = @pt_user_id;

-- =====================================================
-- 6. TEST CÁC KÝ TỰ ĐẶC BIỆT
-- =====================================================

UPDATE pt_profiles 
SET specialization = 'Thể hình, Cardio, Yoga, Pilates, Aerobic, Kickboxing',
    address = '456 Lê Duẩn, Quận 1, TP.HCM - Việt Nam'
WHERE user_id = @pt_user_id;

SELECT 'SPECIAL_CHARACTERS_TEST:' as status;
SELECT full_name, specialization, address 
FROM pt_profiles WHERE user_id = @pt_user_id;

SELECT 'ENCODING_TEST_COMPLETED!' as final_status;

