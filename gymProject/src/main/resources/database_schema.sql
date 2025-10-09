-- Gym Management System Database Schema
-- Database: Dino_Mutant

-- Create users table
CREATE TABLE users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    phone NVARCHAR(20),
    date_of_birth DATE,
    gender NVARCHAR(10),
    address NVARCHAR(255),
    role NVARCHAR(20) NOT NULL DEFAULT 'member', -- admin, manager, employee, member
    package_type NVARCHAR(20), -- basic, premium, vip
    emergency_contact_name NVARCHAR(100),
    emergency_contact_phone NVARCHAR(20),
    join_date DATETIME2 DEFAULT GETDATE(),
    last_login DATETIME2,
    status NVARCHAR(20) DEFAULT 'active', -- active, inactive, suspended
    profile_image NVARCHAR(255),
    notes NVARCHAR(MAX)
);

-- Create coaches table
CREATE TABLE coaches (
    id INT IDENTITY(1,1) PRIMARY KEY,
    full_name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    phone NVARCHAR(20),
    date_of_birth DATE,
    gender NVARCHAR(10),
    address NVARCHAR(255),
    specialization NVARCHAR(100),
    experience_years INT DEFAULT 0,
    salary DECIMAL(15,2),
    certifications NVARCHAR(MAX),
    description NVARCHAR(MAX),
    profile_image NVARCHAR(255),
    join_date DATETIME2 DEFAULT GETDATE(),
    status NVARCHAR(20) DEFAULT 'active', -- active, inactive, busy
    notes NVARCHAR(MAX),
    rating INT DEFAULT 5,
    total_clients INT DEFAULT 0,
    sessions_per_week INT DEFAULT 0
);

-- Create equipment table
CREATE TABLE equipment (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    category NVARCHAR(50), -- Cardio, Strength, Functional, Phụ kiện
    manufacturer NVARCHAR(100),
    model NVARCHAR(100),
    location NVARCHAR(100), -- Khu Cardio, Khu Tạ, etc.
    status NVARCHAR(20) DEFAULT 'working', -- working, maintenance, broken
    purchase_date DATE,
    purchase_price DECIMAL(15,2),
    warranty_months INT,
    last_maintenance_date DATE,
    next_maintenance_date DATE,
    description NVARCHAR(MAX),
    notes NVARCHAR(MAX)
);

-- Create membership packages table
CREATE TABLE membership_packages (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL, -- Basic, Premium, VIP
    price DECIMAL(15,2) NOT NULL,
    duration_months INT DEFAULT 1,
    description NVARCHAR(MAX),
    features NVARCHAR(MAX),
    status NVARCHAR(20) DEFAULT 'active'
);

-- Create transactions table
CREATE TABLE transactions (
    id INT IDENTITY(1,1) PRIMARY KEY,
    transaction_code NVARCHAR(20) NOT NULL UNIQUE,
    customer_id INT,
    customer_name NVARCHAR(100),
    items NVARCHAR(MAX), -- JSON or comma-separated
    total_amount DECIMAL(15,2) NOT NULL,
    discount_amount DECIMAL(15,2) DEFAULT 0,
    tax_amount DECIMAL(15,2) DEFAULT 0,
    final_amount DECIMAL(15,2) NOT NULL,
    payment_method NVARCHAR(20), -- cash, card, bank, qr
    payment_status NVARCHAR(20) DEFAULT 'completed', -- pending, completed, failed, refunded
    transaction_date DATETIME2 DEFAULT GETDATE(),
    cashier_id INT,
    notes NVARCHAR(MAX),
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (cashier_id) REFERENCES users(id)
);

-- Create payment schedules table
CREATE TABLE payment_schedules (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    package_id INT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    due_date DATE NOT NULL,
    paid_date DATE,
    status NVARCHAR(20) DEFAULT 'pending', -- pending, paid, overdue, cancelled
    payment_method NVARCHAR(20),
    transaction_id INT,
    notes NVARCHAR(MAX),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (package_id) REFERENCES membership_packages(id),
    FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);

-- Create training sessions table
CREATE TABLE training_sessions (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    coach_id INT,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    session_type NVARCHAR(50), -- PT, Group Class, etc.
    status NVARCHAR(20) DEFAULT 'scheduled', -- scheduled, completed, cancelled, no_show
    notes NVARCHAR(MAX),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (coach_id) REFERENCES coaches(id)
);

-- Create maintenance records table
CREATE TABLE maintenance_records (
    id INT IDENTITY(1,1) PRIMARY KEY,
    equipment_id INT NOT NULL,
    maintenance_type NVARCHAR(50), -- routine, repair, inspection
    description NVARCHAR(MAX),
    maintenance_date DATE NOT NULL,
    next_maintenance_date DATE,
    cost DECIMAL(15,2),
    technician NVARCHAR(100),
    status NVARCHAR(20) DEFAULT 'completed', -- scheduled, in_progress, completed
    notes NVARCHAR(MAX),
    FOREIGN KEY (equipment_id) REFERENCES equipment(id)
);

-- Create system settings table
CREATE TABLE system_settings (
    id INT IDENTITY(1,1) PRIMARY KEY,
    setting_key NVARCHAR(100) NOT NULL UNIQUE,
    setting_value NVARCHAR(MAX),
    description NVARCHAR(255),
    updated_by INT,
    updated_date DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (updated_by) REFERENCES users(id)
);

-- Insert default data
INSERT INTO membership_packages (name, price, duration_months, description, features) VALUES
('Basic', 300000, 1, 'Gói cơ bản - Truy cập tất cả thiết bị và lớp học nhóm', 'Sử dụng tất cả thiết bị,Lớp học nhóm,Phòng tắm & tủ đồ'),
('Premium', 500000, 1, 'Gói cao cấp - Bao gồm 2 buổi PT và tư vấn dinh dưỡng', 'Tất cả quyền lợi Basic,2 buổi PT/tháng,Tư vấn dinh dưỡng,Massage thư giãn'),
('VIP', 800000, 1, 'Gói VIP - PT không giới hạn và phòng tập riêng', 'Tất cả quyền lợi Premium,PT không giới hạn,Phòng tập riêng,Sauna & Steam');

-- Insert default admin user
INSERT INTO users (username, password, full_name, email, role, status) VALUES
('admin', 'admin123', 'System Administrator', 'admin@gym.com', 'admin', 'active'),
('manager', 'manager123', 'Gym Manager', 'manager@gym.com', 'manager', 'active'),
('employee', 'emp123', 'Gym Employee', 'employee@gym.com', 'employee', 'active');

-- Insert default system settings
INSERT INTO system_settings (setting_key, setting_value, description) VALUES
('gym_name', 'Stamina Gym', 'Tên phòng gym'),
('gym_address', '123 Đường Fitness, Quận 1, TP.HCM', 'Địa chỉ phòng gym'),
('gym_phone', '(028) 1234-5678', 'Số điện thoại phòng gym'),
('gym_email', 'info@staminagym.vn', 'Email phòng gym'),
('gym_hours', 'Thứ 2 - Chủ Nhật: 5:00 - 23:00', 'Giờ hoạt động'),
('vat_rate', '10', 'Tỷ lệ VAT (%)'),
('currency', 'VND', 'Đơn vị tiền tệ');

-- Create indexes for better performance
CREATE INDEX IX_users_username ON users(username);
CREATE INDEX IX_users_email ON users(email);
CREATE INDEX IX_users_role ON users(role);
CREATE INDEX IX_users_status ON users(status);
CREATE INDEX IX_transactions_date ON transactions(transaction_date);
CREATE INDEX IX_transactions_customer ON transactions(customer_id);
CREATE INDEX IX_payment_schedules_user ON payment_schedules(user_id);
CREATE INDEX IX_payment_schedules_due_date ON payment_schedules(due_date);
CREATE INDEX IX_training_sessions_user ON training_sessions(user_id);
CREATE INDEX IX_training_sessions_date ON training_sessions(session_date);
CREATE INDEX IX_equipment_status ON equipment(status);
CREATE INDEX IX_maintenance_records_equipment ON maintenance_records(equipment_id);

