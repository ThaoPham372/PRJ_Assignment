-- =============================================
-- Fix Login Issues - Reset User Passwords and Status
-- =============================================

USE Gym_Manager;
GO

-- Check current user status
SELECT 
    username,
    password,
    full_name,
    role,
    status,
    last_login,
    join_date
FROM users
ORDER BY role, username;
GO

-- Reset all user passwords to simple values for testing
UPDATE users SET password = 'admin123' WHERE username = 'admin';
UPDATE users SET password = 'manager123' WHERE username = 'manager';
UPDATE users SET password = 'emp123' WHERE username = 'employee';
UPDATE users SET password = 'cust123' WHERE username = 'customer';
GO

-- Ensure all users are active
UPDATE users SET status = 'active' WHERE status != 'active';
GO

-- Add additional test users if needed
IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'testadmin')
    INSERT INTO users (username, password, full_name, email, role, status) 
    VALUES ('testadmin', 'test123', 'Test Admin', 'testadmin@gym.com', 'admin', 'active');

IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'testmember')
    INSERT INTO users (username, password, full_name, email, role, status) 
    VALUES ('testmember', 'test123', 'Test Member', 'testmember@gym.com', 'member', 'active');
GO

-- Clear any problematic sessions (if you have a sessions table)
-- This is optional and depends on your session management implementation

-- Test login for each account type
DECLARE @test_user NVARCHAR(50)
DECLARE @test_pass NVARCHAR(255)

-- Test admin login
SET @test_user = 'admin'
SET @test_pass = 'admin123'

IF EXISTS (SELECT 1 FROM users WHERE username = @test_user AND password = @test_pass AND status = 'active')
    PRINT 'Admin login: SUCCESS'
ELSE
    PRINT 'Admin login: FAILED'

-- Test member login
SET @test_user = 'customer'
SET @test_pass = 'cust123'

IF EXISTS (SELECT 1 FROM users WHERE username = @test_user AND password = @test_pass AND status = 'active')
    PRINT 'Member login: SUCCESS'
ELSE
    PRINT 'Member login: FAILED'

-- Test new test accounts
SET @test_user = 'testadmin'
SET @test_pass = 'test123'

IF EXISTS (SELECT 1 FROM users WHERE username = @test_user AND password = @test_pass AND status = 'active')
    PRINT 'Test Admin login: SUCCESS'
ELSE
    PRINT 'Test Admin login: FAILED'
GO

-- Show final user list
SELECT 
    'Account Type' = CASE role
        WHEN 'admin' THEN 'Administrator'
        WHEN 'manager' THEN 'Manager'
        WHEN 'employee' THEN 'Employee'
        WHEN 'member' THEN 'Member'
        ELSE role
    END,
    username,
    password,
    full_name,
    status
FROM users
WHERE status = 'active'
ORDER BY role, username;
GO

-- Check database connection settings
SELECT 
    'Database Name' = DB_NAME(),
    'Server Name' = @@SERVERNAME,
    'SQL Server Version' = @@VERSION,
    'Current User' = USER_NAME(),
    'Current Time' = GETDATE();
GO

PRINT '=============================================';
PRINT 'LOGIN FIX COMPLETED';
PRINT '=============================================';
PRINT 'Available Accounts:';
PRINT 'Admin: admin/admin123 or testadmin/test123';
PRINT 'Manager: manager/manager123';
PRINT 'Employee: employee/emp123';
PRINT 'Member: customer/cust123 or testmember/test123';
PRINT '=============================================';
GO
