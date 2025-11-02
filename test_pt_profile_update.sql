-- Test script to check PT profile update functionality
-- Run this script to verify the database structure and data

-- 1. Check if pt_profiles table exists and has correct structure
DESCRIBE pt_profiles;

-- 2. Check current PT profiles
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

-- 3. Check if there are any PT users
SELECT 
    u.id as user_id,
    u.username,
    u.email as user_email,
    r.role_name,
    p.id as profile_id,
    p.full_name,
    p.email as profile_email
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
LEFT JOIN pt_profiles p ON u.id = p.user_id
WHERE r.role_name = 'PT'
ORDER BY u.id;

-- 4. Test update query (replace USER_ID with actual user ID)
-- UPDATE pt_profiles 
-- SET full_name = 'Test Update', 
--     email = 'test@example.com',
--     updated_date = NOW()
-- WHERE user_id = USER_ID;

-- 5. Check certificates for PT profiles
SELECT 
    c.id,
    c.pt_profile_id,
    c.certificate_name,
    c.certificate_type,
    c.status,
    c.created_date
FROM pt_certificates c
JOIN pt_profiles p ON c.pt_profile_id = p.id
ORDER BY c.created_date DESC;

