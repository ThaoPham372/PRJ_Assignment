-- ============================================
-- GYM MANAGEMENT SYSTEM - DATABASE SCHEMA (3NF)
-- SQL Server 2019+
-- Chuẩn Third Normal Form (3NF)
-- ============================================
create database Gym_Management_System;
go

use Gym_Management_System;
go

-- Drop existing tables nếu có (theo thứ tự dependency)
IF OBJECT_ID('workout_sessions', 'U') IS NOT NULL DROP TABLE workout_sessions;
IF OBJECT_ID('payments', 'U') IS NOT NULL DROP TABLE payments;
IF OBJECT_ID('members', 'U') IS NOT NULL DROP TABLE members;
IF OBJECT_ID('coaches', 'U') IS NOT NULL DROP TABLE coaches;
IF OBJECT_ID('membership_packages', 'U') IS NOT NULL DROP TABLE membership_packages;
IF OBJECT_ID('users', 'U') IS NOT NULL DROP TABLE users;
IF OBJECT_ID('roles', 'U') IS NOT NULL DROP TABLE roles;
GO

-- ============================================
-- TABLE 1: ROLES (Chuẩn 3NF)
-- Lưu các vai trò trong hệ thống
-- ============================================
CREATE TABLE roles (
    role_id INT PRIMARY KEY IDENTITY(1,1),
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description NVARCHAR(255),
    created_at DATETIME DEFAULT GETDATE()
);

-- Insert default roles
INSERT INTO roles (role_name, description) VALUES
('admin', N'Quản trị viên hệ thống'),
('manager', N'Quản lý phòng gym'),
('coach', N'Huấn luyện viên'),
('staff', N'Nhân viên'),
('member', N'Thành viên');
GO

-- ============================================
-- TABLE 2: USERS (Chuẩn 3NF)
-- Lưu thông tin đăng nhập và thông tin cá nhân cơ bản
-- ============================================
CREATE TABLE users (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    
    -- Thông tin đăng nhập
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, -- BCrypt hash
    email VARCHAR(100) NOT NULL UNIQUE,
    
    -- Thông tin cá nhân
    full_name NVARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    gender VARCHAR(10) CHECK (gender IN ('male', 'female', 'other')),
    address NVARCHAR(255),
    avatar_url VARCHAR(255),
    
    -- Role và Status
    role_id INT NOT NULL,
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'suspended', 'pending')),
    
    -- Security
    last_login DATETIME,
    failed_login_attempts INT DEFAULT 0,
    account_locked_until DATETIME,
    password_reset_token VARCHAR(255),
    password_reset_expires DATETIME,
    
    -- Metadata
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    created_by INT,
    updated_by INT,
    
    -- Foreign Keys
    CONSTRAINT FK_users_role FOREIGN KEY (role_id) REFERENCES roles(role_id),
    CONSTRAINT FK_users_created_by FOREIGN KEY (created_by) REFERENCES users(user_id),
    CONSTRAINT FK_users_updated_by FOREIGN KEY (updated_by) REFERENCES users(user_id)
);

-- Indexes cho performance
CREATE INDEX IDX_users_username ON users(username);
CREATE INDEX IDX_users_email ON users(email);
CREATE INDEX IDX_users_role ON users(role_id);
CREATE INDEX IDX_users_status ON users(status);
GO

-- ============================================
-- TABLE 3: MEMBERSHIP_PACKAGES (Chuẩn 3NF)
-- Lưu các gói tập gym
-- ============================================
CREATE TABLE membership_packages (
    package_id INT PRIMARY KEY IDENTITY(1,1),
    
    -- Thông tin gói
    package_name NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(500),
    
    -- Giá và thời hạn
    price DECIMAL(10,2) NOT NULL,
    discount_price DECIMAL(10,2),
    duration_months INT NOT NULL, -- Số tháng
    
    -- Tính năng
    features NVARCHAR(MAX), -- JSON hoặc comma-separated
    access_hours VARCHAR(50), -- VD: "5AM-11PM" hoặc "24/7"
    max_sessions_per_month INT,
    personal_training_sessions INT DEFAULT 0,
    
    -- Phí bổ sung
    registration_fee DECIMAL(10,2) DEFAULT 0,
    
    -- Trạng thái
    is_active BIT DEFAULT 1,
    is_popular BIT DEFAULT 0,
    
    -- Metadata
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- Insert default packages
INSERT INTO membership_packages (package_name, description, price, duration_months, features) VALUES
(N'Gói Cơ Bản', N'Phù hợp cho người mới bắt đầu', 500000, 1, N'Phòng gym,Phòng cardio,Tủ khóa'),
(N'Gói Tiêu Chuẩn', N'Gói phổ biến nhất', 1200000, 3, N'Phòng gym,Phòng cardio,Yoga,Tủ khóa,Nước uống'),
(N'Gói Premium', N'Đầy đủ tiện ích', 2500000, 6, N'Tất cả tiện ích,PT 4 buổi,Massage,Sauna');
GO

-- ============================================
-- TABLE 4: MEMBERS (Chuẩn 3NF)
-- Lưu thông tin chi tiết của thành viên
-- Phụ thuộc vào users
-- ============================================
CREATE TABLE members (
    member_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL UNIQUE, -- 1-1 relationship với users
    
    -- Mã thành viên
    member_code VARCHAR(20) NOT NULL UNIQUE, -- VD: GYM2024001
    registration_date DATE DEFAULT CAST(GETDATE() AS DATE),
    
    -- Thông tin thể chất
    height DECIMAL(5,2), -- cm
    weight DECIMAL(5,2), -- kg
    bmi DECIMAL(4,2),
    blood_type VARCHAR(5),
    medical_conditions NVARCHAR(500),
    allergies NVARCHAR(255),
    injuries NVARCHAR(255),
    
    -- Mục tiêu
    fitness_goal VARCHAR(50) CHECK (fitness_goal IN ('lose_weight', 'gain_weight', 'maintain', 'build_muscle', 'improve_health')),
    target_weight DECIMAL(5,2),
    activity_level VARCHAR(20) CHECK (activity_level IN ('sedentary', 'light', 'moderate', 'active', 'very_active')),
    
    -- Membership Package
    membership_package_id INT,
    package_start_date DATE,
    package_end_date DATE,
    package_status VARCHAR(20) DEFAULT 'active' CHECK (package_status IN ('active', 'expired', 'cancelled', 'paused')),
    
    -- Thông tin khẩn cấp
    emergency_contact_name NVARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relation NVARCHAR(50),
    emergency_contact_address NVARCHAR(255),
    
    -- Huấn luyện viên
    assigned_coach_id INT,
    preferred_training_time VARCHAR(20),
    
    -- Thống kê
    total_workout_sessions INT DEFAULT 0,
    current_streak INT DEFAULT 0,
    longest_streak INT DEFAULT 0,
    total_calories_burned DECIMAL(10,2) DEFAULT 0,
    last_workout_date DATE,
    
    -- Ghi chú
    notes NVARCHAR(MAX),
    
    -- Metadata
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'suspended')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    
    -- Foreign Keys
    CONSTRAINT FK_members_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT FK_members_package FOREIGN KEY (membership_package_id) REFERENCES membership_packages(package_id),
    CONSTRAINT FK_members_coach FOREIGN KEY (assigned_coach_id) REFERENCES users(user_id)
);

-- Indexes
CREATE INDEX IDX_members_user ON members(user_id);
CREATE INDEX IDX_members_code ON members(member_code);
CREATE INDEX IDX_members_package ON members(membership_package_id);
CREATE INDEX IDX_members_coach ON members(assigned_coach_id);
CREATE INDEX IDX_members_status ON members(status);
GO

-- ============================================
-- TABLE 5: COACHES (Chuẩn 3NF)
-- Lưu thông tin chi tiết của huấn luyện viên/nhân viên
-- Phụ thuộc vào users
-- ============================================
CREATE TABLE coaches (
    coach_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL UNIQUE, -- 1-1 relationship với users
    
    -- Thông tin công việc
    employee_id VARCHAR(20) NOT NULL UNIQUE,
    hire_date DATE NOT NULL,
    job_title NVARCHAR(100),
    department NVARCHAR(50),
    
    -- Lương và thu nhập
    base_salary DECIMAL(10,2),
    hourly_rate DECIMAL(10,2),
    commission_rate DECIMAL(5,2), -- %
    
    -- Chuyên môn
    certifications NVARCHAR(500), -- JSON hoặc comma-separated
    experience_years INT,
    specialization NVARCHAR(255),
    
    -- Quản lý khách hàng
    max_clients INT DEFAULT 10,
    current_clients INT DEFAULT 0,
    
    -- Đánh giá
    average_rating DECIMAL(3,2),
    total_reviews INT DEFAULT 0,
    
    -- Thống kê
    total_sessions_conducted INT DEFAULT 0,
    total_revenue_generated DECIMAL(12,2) DEFAULT 0,
    
    -- Ghi chú
    work_schedule NVARCHAR(255),
    notes NVARCHAR(MAX),
    
    -- Metadata
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'on_leave', 'terminated')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    
    -- Foreign Key
    CONSTRAINT FK_coaches_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Indexes
CREATE INDEX IDX_coaches_user ON coaches(user_id);
CREATE INDEX IDX_coaches_employee ON coaches(employee_id);
CREATE INDEX IDX_coaches_status ON coaches(status);
GO

-- ============================================
-- TABLE 6: WORKOUT_SESSIONS (Chuẩn 3NF)
-- Lưu lịch sử buổi tập
-- ============================================
CREATE TABLE workout_sessions (
    session_id INT PRIMARY KEY IDENTITY(1,1),
    
    -- Thông tin cơ bản
    member_id INT NOT NULL,
    coach_id INT,
    session_type VARCHAR(50) NOT NULL, -- cardio, strength, yoga, personal_training, group_class
    
    -- Thời gian
    session_date DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    duration_minutes INT,
    
    -- Thống kê buổi tập
    calories_burned DECIMAL(8,2),
    heart_rate_avg INT,
    distance_km DECIMAL(6,2),
    steps_count INT,
    
    -- Bài tập thực hiện
    exercises_performed NVARCHAR(MAX), -- JSON format
    
    -- Đánh giá
    member_rating INT CHECK (member_rating BETWEEN 1 AND 5),
    member_feedback NVARCHAR(500),
    coach_notes NVARCHAR(500),
    
    -- Trạng thái
    status VARCHAR(20) DEFAULT 'completed' CHECK (status IN ('scheduled', 'in_progress', 'completed', 'cancelled', 'no_show')),
    
    -- Check-in/out
    check_in_time DATETIME,
    check_out_time DATETIME,
    
    -- Metadata
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    
    -- Foreign Keys
    CONSTRAINT FK_sessions_member FOREIGN KEY (member_id) REFERENCES members(member_id),
    CONSTRAINT FK_sessions_coach FOREIGN KEY (coach_id) REFERENCES coaches(coach_id)
);

-- Indexes
CREATE INDEX IDX_sessions_member ON workout_sessions(member_id);
CREATE INDEX IDX_sessions_coach ON workout_sessions(coach_id);
CREATE INDEX IDX_sessions_date ON workout_sessions(session_date);
CREATE INDEX IDX_sessions_status ON workout_sessions(status);
GO

-- ============================================
-- TABLE 7: PAYMENTS (Chuẩn 3NF)
-- Lưu lịch sử thanh toán
-- ============================================
CREATE TABLE payments (
    payment_id INT PRIMARY KEY IDENTITY(1,1),
    
    -- Thông tin thanh toán
    member_id INT NOT NULL,
    membership_package_id INT,
    
    -- Số tiền
    amount DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2) DEFAULT 0,
    tax_amount DECIMAL(10,2) DEFAULT 0,
    total_amount DECIMAL(10,2) NOT NULL,
    
    -- Phương thức
    payment_method VARCHAR(50) NOT NULL CHECK (payment_method IN ('cash', 'card', 'bank_transfer', 'momo', 'vnpay', 'other')),
    transaction_id VARCHAR(100),
    
    -- Thời gian
    payment_date DATETIME DEFAULT GETDATE(),
    due_date DATE,
    
    -- Trạng thái
    status VARCHAR(20) DEFAULT 'completed' CHECK (status IN ('pending', 'completed', 'failed', 'refunded', 'cancelled')),
    
    -- Hoàn tiền
    refund_amount DECIMAL(10,2),
    refund_date DATETIME,
    refund_reason NVARCHAR(255),
    
    -- Hóa đơn
    invoice_number VARCHAR(50) UNIQUE,
    invoice_url VARCHAR(255),
    
    -- Ghi chú
    notes NVARCHAR(500),
    
    -- Metadata
    created_at DATETIME DEFAULT GETDATE(),
    created_by INT,
    
    -- Foreign Keys
    CONSTRAINT FK_payments_member FOREIGN KEY (member_id) REFERENCES members(member_id),
    CONSTRAINT FK_payments_package FOREIGN KEY (membership_package_id) REFERENCES membership_packages(package_id),
    CONSTRAINT FK_payments_created_by FOREIGN KEY (created_by) REFERENCES users(user_id)
);

-- Indexes
CREATE INDEX IDX_payments_member ON payments(member_id);
CREATE INDEX IDX_payments_package ON payments(membership_package_id);
CREATE INDEX IDX_payments_date ON payments(payment_date);
CREATE INDEX IDX_payments_status ON payments(status);
CREATE INDEX IDX_payments_invoice ON payments(invoice_number);
GO

-- ============================================
-- TRIGGERS - Auto update timestamps
-- ============================================

-- Trigger cho users
CREATE TRIGGER trg_users_update
ON users
AFTER UPDATE
AS
BEGIN
    UPDATE users
    SET updated_at = GETDATE()
    WHERE user_id IN (SELECT user_id FROM inserted);
END;
GO

-- Trigger cho members
CREATE TRIGGER trg_members_update
ON members
AFTER UPDATE
AS
BEGIN
    UPDATE members
    SET updated_at = GETDATE()
    WHERE member_id IN (SELECT member_id FROM inserted);
END;
GO

-- Trigger cho coaches
CREATE TRIGGER trg_coaches_update
ON coaches
AFTER UPDATE
AS
BEGIN
    UPDATE coaches
    SET updated_at = GETDATE()
    WHERE coach_id IN (SELECT coach_id FROM inserted);
END;
GO

-- Trigger auto calculate BMI
CREATE TRIGGER trg_members_bmi
ON members
AFTER INSERT, UPDATE
AS
BEGIN
    UPDATE members
    SET bmi = CASE 
        WHEN height > 0 AND weight > 0 
        THEN ROUND((weight / POWER(height/100, 2)), 2)
        ELSE NULL
    END
    WHERE member_id IN (SELECT member_id FROM inserted)
    AND (height IS NOT NULL AND weight IS NOT NULL);
END;
GO

-- ============================================
-- STORED PROCEDURES - Các thủ tục hữu ích
-- ============================================

-- SP: Tạo member code tự động
CREATE PROCEDURE sp_generate_member_code
    @new_code VARCHAR(20) OUTPUT
AS
BEGIN
    DECLARE @year INT = YEAR(GETDATE());
    DECLARE @last_number INT;
    
    SELECT @last_number = ISNULL(MAX(CAST(SUBSTRING(member_code, 8, LEN(member_code)-7) AS INT)), 0)
    FROM members
    WHERE member_code LIKE 'GYM' + CAST(@year AS VARCHAR(4)) + '%';
    
    SET @new_code = 'GYM' + CAST(@year AS VARCHAR(4)) + RIGHT('00000' + CAST(@last_number + 1 AS VARCHAR(5)), 5);
END;
GO

-- SP: Authenticate user
CREATE PROCEDURE sp_authenticate_user
    @username VARCHAR(50),
    @password_hash VARCHAR(255),
    @user_id INT OUTPUT,
    @role_name VARCHAR(50) OUTPUT,
    @is_locked BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT 
        @user_id = u.user_id,
        @role_name = r.role_name,
        @is_locked = CASE 
            WHEN u.account_locked_until IS NOT NULL AND u.account_locked_until > GETDATE() 
            THEN 1 
            ELSE 0 
        END
    FROM users u
    INNER JOIN roles r ON u.role_id = r.role_id
    WHERE u.username = @username 
    AND u.password_hash = @password_hash
    AND u.status = 'active';
    
    IF @user_id IS NOT NULL AND @is_locked = 0
    BEGIN
        -- Reset failed attempts và update last login
        UPDATE users 
        SET last_login = GETDATE(),
            failed_login_attempts = 0
        WHERE user_id = @user_id;
    END
END;
GO

-- SP: Register new member
CREATE PROCEDURE sp_register_member
    @username VARCHAR(50),
    @password_hash VARCHAR(255),
    @email VARCHAR(100),
    @full_name NVARCHAR(100),
    @phone VARCHAR(20),
    @date_of_birth DATE,
    @gender VARCHAR(10),
    @user_id INT OUTPUT,
    @member_id INT OUTPUT,
    @member_code VARCHAR(20) OUTPUT,
    @error_message NVARCHAR(255) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRANSACTION;
    
    BEGIN TRY
        -- Check username exists
        IF EXISTS (SELECT 1 FROM users WHERE username = @username)
        BEGIN
            SET @error_message = N'Username đã tồn tại';
            ROLLBACK TRANSACTION;
            RETURN;
        END
        
        -- Check email exists
        IF EXISTS (SELECT 1 FROM users WHERE email = @email)
        BEGIN
            SET @error_message = N'Email đã tồn tại';
            ROLLBACK TRANSACTION;
            RETURN;
        END
        
        -- Get member role_id
        DECLARE @role_id INT;
        SELECT @role_id = role_id FROM roles WHERE role_name = 'member';
        
        -- Insert user
        INSERT INTO users (username, password_hash, email, full_name, phone, date_of_birth, gender, role_id, status)
        VALUES (@username, @password_hash, @email, @full_name, @phone, @date_of_birth, @gender, @role_id, 'active');
        
        SET @user_id = SCOPE_IDENTITY();
        
        -- Generate member code
        EXEC sp_generate_member_code @member_code OUTPUT;
        
        -- Insert member
        INSERT INTO members (user_id, member_code, registration_date, status)
        VALUES (@user_id, @member_code, CAST(GETDATE() AS DATE), 'active');
        
        SET @member_id = SCOPE_IDENTITY();
        
        COMMIT TRANSACTION;
        SET @error_message = NULL;
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SET @error_message = ERROR_MESSAGE();
    END CATCH
END;
GO

-- SP: Get member dashboard data
CREATE PROCEDURE sp_get_member_dashboard
    @user_id INT
AS
BEGIN
    SET NOCOUNT ON;
    
    -- Member info
    SELECT 
        u.user_id,
        u.username,
        u.email,
        u.full_name,
        u.phone,
        u.avatar_url,
        m.member_id,
        m.member_code,
        m.height,
        m.weight,
        m.bmi,
        m.fitness_goal,
        m.current_streak,
        m.longest_streak,
        m.total_workout_sessions,
        m.total_calories_burned,
        m.last_workout_date,
        m.package_start_date,
        m.package_end_date,
        m.package_status,
        mp.package_name,
        mp.duration_months,
        DATEDIFF(DAY, GETDATE(), m.package_end_date) as days_remaining,
        CASE 
            WHEN m.package_end_date >= CAST(GETDATE() AS DATE) THEN 1
            ELSE 0
        END as is_package_active
    FROM users u
    INNER JOIN members m ON u.user_id = m.user_id
    LEFT JOIN membership_packages mp ON m.membership_package_id = mp.package_id
    WHERE u.user_id = @user_id;
    
    -- Recent workout sessions
    SELECT TOP 5
        ws.session_id,
        ws.session_type,
        ws.session_date,
        ws.duration_minutes,
        ws.calories_burned,
        ws.status
    FROM workout_sessions ws
    INNER JOIN members m ON ws.member_id = m.member_id
    WHERE m.user_id = @user_id
    ORDER BY ws.session_date DESC, ws.session_id DESC;
END;
GO

-- ============================================
-- VIEWS - Các view hữu ích
-- ============================================

-- View: Member full info
CREATE VIEW vw_members_full AS
SELECT 
    u.user_id,
    u.username,
    u.email,
    u.full_name,
    u.phone,
    u.date_of_birth,
    u.gender,
    u.address,
    u.avatar_url,
    u.status as user_status,
    m.member_id,
    m.member_code,
    m.registration_date,
    m.height,
    m.weight,
    m.bmi,
    m.fitness_goal,
    m.total_workout_sessions,
    m.current_streak,
    m.package_start_date,
    m.package_end_date,
    m.package_status,
    mp.package_name,
    DATEDIFF(DAY, GETDATE(), m.package_end_date) as days_remaining
FROM users u
INNER JOIN members m ON u.user_id = m.user_id
LEFT JOIN membership_packages mp ON m.membership_package_id = mp.package_id
WHERE u.role_id = (SELECT role_id FROM roles WHERE role_name = 'member');
GO

-- View: Active members
CREATE VIEW vw_active_members AS
SELECT *
FROM vw_members_full
WHERE user_status = 'active' AND package_status = 'active';
GO

-- ============================================
-- SAMPLE DATA - Dữ liệu mẫu
-- ============================================

-- Insert admin user (password: Admin123!)
INSERT INTO users (username, password_hash, email, full_name, role_id, status)
VALUES ('admin', '$2a$10$XZ5s7.mE6xGGPJ8QYJz8h.kQPQf8Ov5YkR7zJ9xqPNLv6K9qM8KXy', 'admin@gym.com', N'Administrator', 
    (SELECT role_id FROM roles WHERE role_name = 'admin'), 'active');

-- ============================================
-- VERIFICATION QUERIES
-- ============================================
PRINT '=== Database schema created successfully! ===';
PRINT 'Tables created: ' + CAST((SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE') AS VARCHAR);
PRINT 'Views created: ' + CAST((SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS) AS VARCHAR);
PRINT 'Stored procedures created: ' + CAST((SELECT COUNT(*) FROM INFORMATION_SCHEMA.ROUTINES WHERE ROUTINE_TYPE = 'PROCEDURE') AS VARCHAR);
GO

-- Show all tables
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' ORDER BY TABLE_NAME;
GO

