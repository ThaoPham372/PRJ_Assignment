-- =============================================
-- Test Database Connection and Login
-- =============================================

USE Gym_Manager;
GO

-- Test if database exists and is accessible
IF DB_NAME() = 'Gym_Manager'
    PRINT 'Database connection successful!'
ELSE
    PRINT 'Database connection failed!'
GO

-- Check all tables exist
SELECT 
    TABLE_NAME,
    TABLE_TYPE
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = 'dbo'
ORDER BY TABLE_NAME;
GO

-- Test user login function
-- This simulates the login process used in your Java code
DECLARE @username NVARCHAR(50) = 'admin'
DECLARE @password NVARCHAR(255) = 'admin123'

-- Check if user exists
SELECT 
    id,
    username,
    full_name,
    email,
    role,
    status,
    last_login
FROM users 
WHERE username = @username 
  AND password = @password 
  AND status = 'active';

-- Update last login (simulate successful login)
UPDATE users 
SET last_login = GETDATE() 
WHERE username = @username;

PRINT 'Login test completed for user: ' + @username;
GO

-- Show all available test accounts
SELECT 
    username,
    password,
    full_name,
    role,
    status
FROM users 
WHERE status = 'active'
ORDER BY role, username;
GO

-- Test equipment data
SELECT TOP 5
    name,
    category,
    location,
    status,
    purchase_price
FROM equipment
ORDER BY name;
GO

-- Test coaches data
SELECT TOP 5
    full_name,
    specialization,
    experience_years,
    rating,
    status
FROM coaches
ORDER BY rating DESC;
GO

-- Test transactions data
SELECT TOP 5
    transaction_code,
    customer_name,
    total_amount,
    final_amount,
    payment_method,
    transaction_date
FROM transactions
ORDER BY transaction_date DESC;
GO

PRINT 'Database test completed successfully!';
PRINT 'You can now use these accounts to login:';
PRINT '- admin/admin123 (Admin access)';
PRINT '- manager/manager123 (Manager access)';
PRINT '- employee/emp123 (Employee access)';
PRINT '- customer/cust123 (Member access)';
GO
