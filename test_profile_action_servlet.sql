-- Test script để kiểm tra ProfileActionServlet
-- Chạy script này để verify database và test các chức năng

-- 1. Kiểm tra cấu trúc bảng pt_profiles
DESCRIBE pt_profiles;

-- 2. Kiểm tra dữ liệu hiện tại trong pt_profiles
SELECT 
    id, 
    user_id, 
    full_name, 
    email, 
    phone_number, 
    date_of_birth, 
    gender, 
    specialization, 
    address,
    created_date,
    updated_date
FROM pt_profiles 
ORDER BY updated_date DESC;

-- 3. Kiểm tra users có role PT
SELECT 
    u.id as user_id,
    u.username,
    u.email as user_email,
    r.role_name,
    p.id as profile_id,
    p.full_name,
    p.email as profile_email,
    p.updated_date
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
LEFT JOIN pt_profiles p ON u.id = p.user_id
WHERE r.role_name = 'PT'
ORDER BY u.id;

-- 4. Test update profile query (thay USER_ID bằng ID thực tế)
-- UPDATE pt_profiles 
-- SET full_name = 'Test Update Name', 
--     email = 'test@example.com',
--     phone_number = '0123456789',
--     specialization = 'Test Specialization',
--     updated_date = NOW()
-- WHERE user_id = USER_ID;

-- 5. Kiểm tra password hash format
SELECT 
    u.id,
    u.username,
    u.password_hash,
    u.salt,
    u.updated_date
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
WHERE r.role_name = 'PT'
ORDER BY u.updated_date DESC;

-- 6. Test password update query (thay USER_ID bằng ID thực tế)
-- UPDATE users 
-- SET password_hash = 'new_hash_here', 
--     salt = 'new_salt_here',
--     updated_date = NOW()
-- WHERE id = USER_ID;

-- 7. Kiểm tra certificates
SELECT 
    c.id,
    c.pt_profile_id,
    c.certificate_name,
    c.certificate_type,
    c.status,
    c.created_date,
    p.full_name as pt_name
FROM pt_certificates c
JOIN pt_profiles p ON c.pt_profile_id = p.id
ORDER BY c.created_date DESC;

