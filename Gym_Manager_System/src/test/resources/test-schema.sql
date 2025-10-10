-- Test Database Schema for H2 Database
-- This file contains the database schema for testing

-- Drop tables if they exist (for clean test runs)
DROP TABLE IF EXISTS Members;
DROP TABLE IF EXISTS Users;

-- Create Users table
CREATE TABLE Users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    gender VARCHAR(10),
    address TEXT,
    role VARCHAR(20) DEFAULT 'member',
    package_type VARCHAR(20),
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    status VARCHAR(20) DEFAULT 'active',
    profile_image VARCHAR(255),
    avatar_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

-- Create Members table
CREATE TABLE Members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    member_code VARCHAR(20) UNIQUE NOT NULL,
    height DECIMAL(5,2),
    weight DECIMAL(5,2),
    body_fat_percentage DECIMAL(5,2),
    muscle_mass DECIMAL(5,2),
    fitness_goal VARCHAR(50),
    activity_level VARCHAR(20),
    medical_conditions TEXT,
    allergies TEXT,
    injuries TEXT,
    current_streak INT DEFAULT 0,
    longest_streak INT DEFAULT 0,
    total_workout_sessions INT DEFAULT 0,
    coach_id INT,
    membership_start_date DATE,
    membership_end_date DATE,
    membership_status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- Insert test data
INSERT INTO Users (id, username, password, full_name, email, phone, role, status) VALUES
(1, 'testuser1', 'hashedpassword1', 'Test User 1', 'test1@example.com', '0123456789', 'member', 'active'),
(2, 'testuser2', 'hashedpassword2', 'Test User 2', 'test2@example.com', '0987654321', 'member', 'active'),
(3, 'admin', 'hashedpassword3', 'Admin User', 'admin@example.com', '0111222333', 'admin', 'active');

INSERT INTO Members (member_id, user_id, member_code, height, weight, fitness_goal, activity_level) VALUES
(1, 1, 'MEM001', 175.0, 70.0, 'weight_loss', 'moderate'),
(2, 2, 'MEM002', 180.0, 80.0, 'muscle_gain', 'high');

-- Create indexes for better performance
CREATE INDEX idx_users_username ON Users(username);
CREATE INDEX idx_users_email ON Users(email);
CREATE INDEX idx_members_user_id ON Members(user_id);
CREATE INDEX idx_members_member_code ON Members(member_code);
