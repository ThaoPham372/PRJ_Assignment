# 🏋️ KIẾN TRÚC MODEL - HỆ THỐNG QUẢN LÝ PHÒNG GYM

## 📋 MỤC LỤC
1. [Tổng quan](#tổng-quan)
2. [Sơ đồ quan hệ](#sơ-đồ-quan-hệ)
3. [Chi tiết từng Model](#chi-tiết-từng-model)
4. [Gợi ý Database Schema](#gợi-ý-database-schema)
5. [Roadmap phát triển](#roadmap-phát-triển)

---

## 🎯 TỔNG QUAN

### Kiến trúc Model hiện tại gồm:

#### **Core Models (Đã triển khai)**
1. **User** - Quản lý tài khoản người dùng (Authentication)
2. **Member** - Thông tin chi tiết thành viên gym
3. **Coach** - Thông tin huấn luyện viên/nhân viên
4. **MembershipPackage** - Các gói thành viên
5. **WorkoutSession** - Buổi tập luyện
6. **Payment** - Giao dịch thanh toán

#### **Extended Models (Đề xuất phát triển tiếp)**
7. **Attendance** - Điểm danh vào/ra phòng gym
8. **Exercise** - Danh mục bài tập
9. **WorkoutPlan** - Kế hoạch tập luyện
10. **NutritionPlan** - Kế hoạch dinh dưỡng
11. **BodyMetrics** - Số đo cơ thể theo thời gian
12. **Schedule** - Lịch làm việc của coach và lịch tập của member
13. **Notification** - Thông báo hệ thống
14. **Review** - Đánh giá và phản hồi
15. **Equipment** - Thiết bị gym
16. **Class** - Lớp học nhóm

---

## 🔗 SƠ ĐỒ QUAN HỆ

```
┌──────────────┐
│     User     │ (1 : 1 hoặc 1 : n)
└──────┬───────┘
       │
       ├─────────┬──────────────┐
       │         │              │
       ▼         ▼              ▼
┌──────────┐ ┌─────────┐  ┌──────────┐
│  Member  │ │  Coach  │  │  Admin   │
└────┬─────┘ └────┬────┘  └──────────┘
     │            │
     │            │
     ├────────────┼──────────────┐
     │            │              │
     ▼            ▼              ▼
┌──────────────────────────────────┐
│      WorkoutSession              │
│  (Member + Coach + Session)      │
└──────────────┬───────────────────┘
               │
               ▼
┌──────────────────────────────────┐
│         WorkoutPlan              │
│      (Kế hoạch tập)              │
└──────────────────────────────────┘

┌──────────────┐     ┌──────────────┐
│   Payment    │────▶│ Membership   │
│              │     │   Package    │
└──────────────┘     └──────────────┘
```

---

## 📊 CHI TIẾT TỪNG MODEL

### 1. **User** (Tài khoản chính)
**Mục đích**: Quản lý authentication và authorization

**Vai trò (Roles)**:
- `admin`: Quản trị viên (toàn quyền)
- `manager`: Quản lý (quản lý nhân sự, doanh thu)
- `coach`: Huấn luyện viên
- `receptionist`: Lễ tân
- `member`: Thành viên

**Quan hệ**:
- 1 User → 1 Member (nếu role = member)
- 1 User → 1 Coach (nếu role = coach/manager)

---

### 2. **Member** (Thành viên)
**Mục đích**: Lưu trữ thông tin chi tiết của thành viên

**Các tính năng chính**:
- ✅ Thông tin cá nhân (cao, cân, BMI, nhóm máu)
- ✅ Mục tiêu tập luyện
- ✅ Thông tin khẩn cấp
- ✅ Gói thành viên hiện tại
- ✅ Thống kê tập luyện (streak, calories, sessions)
- ✅ Coach được phân công
- ✅ Tình trạng sức khỏe (dị ứng, chấn thương)

**Business Logic**:
```java
- calculateBMI(): Tính BMI tự động
- getBMICategory(): Phân loại BMI
- isPackageActive(): Kiểm tra gói còn hạn không
- incrementWorkoutSession(): Tăng số buổi tập
```

---

### 3. **Coach** (Huấn luyện viên / Nhân viên)
**Mục đích**: Quản lý thông tin nhân viên

**Các tính năng chính**:
- ✅ Thông tin nhân sự (mã NV, chức vụ, phòng ban)
- ✅ Chuyên môn (chứng chỉ, kinh nghiệm, specialization)
- ✅ Lương và hợp đồng (lương cơ bản, hoa hồng, thưởng)
- ✅ Thống kê làm việc (số khách, doanh thu, đánh giá)
- ✅ Lịch làm việc
- ✅ Thông tin ngân hàng
- ✅ Đánh giá hiệu suất

**Vị trí (Positions)**:
- `personal_trainer`: Huấn luyện viên cá nhân
- `group_instructor`: Giảng viên lớp nhóm
- `manager`: Quản lý
- `receptionist`: Lễ tân

**Business Logic**:
```java
- calculateMonthlySalary(): Tính lương tháng
- getCompletionRate(): Tỷ lệ hoàn thành buổi tập
- isContractActive(): Kiểm tra hợp đồng còn hiệu lực
- addClient(), removeClient(): Quản lý số lượng khách
```

---

### 4. **MembershipPackage** (Gói thành viên)
**Mục đích**: Quản lý các gói membership

**Loại gói (Package Types)**:
- `basic`: Cơ bản
- `premium`: Cao cấp
- `vip`: VIP
- `student`: Học sinh - Sinh viên
- `senior`: Người cao tuổi
- `family`: Gia đình

**Các tính năng chính**:
- ✅ Giá và thời hạn
- ✅ Quyền lợi chi tiết (PT, group class, sauna, pool...)
- ✅ Giờ và ngày sử dụng
- ✅ Khuyến mãi và ưu đãi
- ✅ Điều khoản sử dụng
- ✅ Thống kê (số người đăng ký, doanh thu)
- ✅ UI config (icon, màu sắc, badge)

**Business Logic**:
```java
- getEffectivePrice(): Giá sau giảm giá
- getDiscountPercentage(): % giảm giá
- isPromotionActive(): Kiểm tra khuyến mãi còn hạn
- getPricePerDay(): Giá/ngày
```

---

### 5. **WorkoutSession** (Buổi tập)
**Mục đích**: Tracking buổi tập của member

**Loại buổi tập (Session Types)**:
- `personal_training`: PT 1-1
- `group_class`: Lớp nhóm
- `self_training`: Tự tập
- `online_training`: Online

**Trạng thái (Status)**:
- `scheduled`: Đã đặt lịch
- `in_progress`: Đang tập
- `completed`: Hoàn thành
- `cancelled`: Đã hủy
- `no_show`: Không đến

**Các tính năng chính**:
- ✅ Lịch và thời gian
- ✅ Loại và cường độ tập
- ✅ Thống kê (calories, tim, bước chân)
- ✅ Đánh giá 2 chiều (member ↔ coach)
- ✅ Check-in/Check-out

---

### 6. **Payment** (Thanh toán)
**Mục đích**: Quản lý giao dịch tài chính

**Loại thanh toán (Payment Types)**:
- `membership`: Đăng ký/gia hạn gói
- `personal_training`: PT thêm
- `product`: Mua sản phẩm
- `service`: Dịch vụ khác
- `penalty`: Phạt

**Phương thức (Payment Methods)**:
- `cash`: Tiền mặt
- `credit_card`: Thẻ tín dụng
- `debit_card`: Thẻ ghi nợ
- `bank_transfer`: Chuyển khoản
- `e_wallet`: Ví điện tử
- `qr_code`: QR Code

**Trạng thái**:
- `pending`: Chờ thanh toán
- `completed`: Đã thanh toán
- `failed`: Thất bại
- `refunded`: Đã hoàn tiền
- `cancelled`: Đã hủy

---

## 💾 GỢI Ý DATABASE SCHEMA

### SQL Script để tạo tables:

```sql
-- 1. Users table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    gender ENUM('male', 'female', 'other'),
    address NVARCHAR(255),
    role ENUM('admin', 'manager', 'coach', 'receptionist', 'member') NOT NULL,
    status ENUM('active', 'inactive', 'suspended') DEFAULT 'active',
    profile_image VARCHAR(255),
    last_login DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Members table
CREATE TABLE members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE NOT NULL,
    member_code VARCHAR(50) UNIQUE NOT NULL,
    registration_date DATE NOT NULL,
    
    -- Physical info
    height DECIMAL(5,2),
    weight DECIMAL(5,2),
    bmi DECIMAL(4,2),
    blood_type VARCHAR(5),
    medical_conditions TEXT,
    
    -- Fitness goals
    fitness_goal ENUM('lose_weight', 'gain_weight', 'maintain', 'build_muscle', 'improve_health'),
    target_weight DECIMAL(5,2),
    activity_level ENUM('sedentary', 'light', 'moderate', 'active', 'very_active'),
    
    -- Membership
    membership_package_id INT,
    package_start_date DATE,
    package_end_date DATE,
    
    -- Emergency contact
    emergency_contact_name NVARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relation NVARCHAR(50),
    emergency_contact_address NVARCHAR(255),
    
    -- Coach assignment
    assigned_coach_id INT,
    preferred_training_time ENUM('morning', 'afternoon', 'evening'),
    
    -- Statistics
    total_workout_sessions INT DEFAULT 0,
    current_streak INT DEFAULT 0,
    longest_streak INT DEFAULT 0,
    total_calories_burned DECIMAL(10,2) DEFAULT 0,
    last_workout_date DATE,
    
    -- Health notes
    allergies TEXT,
    injuries TEXT,
    notes TEXT,
    
    status ENUM('active', 'inactive', 'suspended', 'expired') DEFAULT 'active',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_coach_id) REFERENCES coaches(coach_id),
    FOREIGN KEY (membership_package_id) REFERENCES membership_packages(package_id)
);

-- 3. Coaches table
CREATE TABLE coaches (
    coach_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE NOT NULL,
    employee_code VARCHAR(50) UNIQUE NOT NULL,
    position ENUM('personal_trainer', 'group_instructor', 'manager', 'receptionist') NOT NULL,
    department ENUM('training', 'management', 'administration'),
    hire_date DATE NOT NULL,
    employment_type ENUM('full_time', 'part_time', 'contract', 'intern') DEFAULT 'full_time',
    
    -- Professional info
    specialization VARCHAR(100),
    certifications TEXT,
    years_of_experience INT,
    education NVARCHAR(255),
    languages VARCHAR(100),
    
    -- Salary
    base_salary DECIMAL(15,2),
    hourly_rate DECIMAL(10,2),
    commission DECIMAL(5,2),
    bonus DECIMAL(15,2),
    payment_frequency ENUM('monthly', 'bi_weekly', 'weekly') DEFAULT 'monthly',
    
    -- Contract
    contract_start_date DATE,
    contract_end_date DATE,
    
    -- Statistics
    total_clients_assigned INT DEFAULT 0,
    active_clients INT DEFAULT 0,
    total_sessions INT DEFAULT 0,
    completed_sessions INT DEFAULT 0,
    cancelled_sessions INT DEFAULT 0,
    total_revenue DECIMAL(15,2) DEFAULT 0,
    average_rating DECIMAL(3,2) DEFAULT 0,
    total_reviews INT DEFAULT 0,
    
    -- Schedule
    working_days JSON,
    working_hours VARCHAR(50),
    max_clients_per_day INT,
    available_for_new_clients BOOLEAN DEFAULT TRUE,
    
    -- Emergency contact
    emergency_contact_name NVARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relation NVARCHAR(50),
    
    -- Bank info
    bank_name NVARCHAR(100),
    bank_account_number VARCHAR(50),
    bank_account_name NVARCHAR(100),
    
    -- Performance
    performance_score DECIMAL(5,2),
    last_performance_review DATE,
    
    bio TEXT,
    profile_image VARCHAR(255),
    status ENUM('active', 'on_leave', 'suspended', 'terminated') DEFAULT 'active',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 4. Membership Packages table
CREATE TABLE membership_packages (
    package_id INT PRIMARY KEY AUTO_INCREMENT,
    package_code VARCHAR(50) UNIQUE NOT NULL,
    package_name NVARCHAR(100) NOT NULL,
    description TEXT,
    package_type ENUM('basic', 'premium', 'vip', 'student', 'senior', 'family') NOT NULL,
    
    -- Pricing
    price DECIMAL(15,2) NOT NULL,
    discount_price DECIMAL(15,2),
    duration_days INT NOT NULL,
    registration_fee DECIMAL(15,2),
    
    -- Benefits (stored as boolean)
    unlimited_access BOOLEAN DEFAULT FALSE,
    gym_access_count INT,
    personal_training BOOLEAN DEFAULT FALSE,
    pt_sessions_included INT,
    group_classes_included BOOLEAN DEFAULT FALSE,
    group_classes_count INT,
    sauna_access BOOLEAN DEFAULT FALSE,
    locker_access BOOLEAN DEFAULT FALSE,
    pool_access BOOLEAN DEFAULT FALSE,
    nutrition_consultation BOOLEAN DEFAULT FALSE,
    free_wifi BOOLEAN DEFAULT FALSE,
    free_drinks BOOLEAN DEFAULT FALSE,
    free_parking BOOLEAN DEFAULT FALSE,
    guest_pass BOOLEAN DEFAULT FALSE,
    guest_pass_count INT,
    
    -- Access restrictions
    access_hours ENUM('all_day', 'morning_only', 'evening_only', 'off_peak'),
    access_days ENUM('all_week', 'weekdays_only', 'weekends_only'),
    
    -- Promotion
    is_promotional BOOLEAN DEFAULT FALSE,
    promotion_start_date DATE,
    promotion_end_date DATE,
    
    -- Terms
    auto_renewal BOOLEAN DEFAULT FALSE,
    refundable BOOLEAN DEFAULT FALSE,
    cancellation_notice_days INT,
    terms_and_conditions TEXT,
    
    -- Requirements
    min_age INT,
    max_age INT,
    max_members INT,
    requires_medical_certificate BOOLEAN DEFAULT FALSE,
    
    -- Statistics
    total_subscribers INT DEFAULT 0,
    active_subscribers INT DEFAULT 0,
    total_revenue DECIMAL(15,2) DEFAULT 0,
    average_rating DECIMAL(3,2) DEFAULT 0,
    
    -- Display
    status ENUM('active', 'inactive', 'discontinued') DEFAULT 'active',
    is_visible BOOLEAN DEFAULT TRUE,
    is_featured BOOLEAN DEFAULT FALSE,
    display_order INT,
    icon_url VARCHAR(255),
    color_code VARCHAR(7),
    badge_text VARCHAR(50),
    
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 5. Workout Sessions table
CREATE TABLE workout_sessions (
    session_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    coach_id INT,
    
    session_type ENUM('personal_training', 'group_class', 'self_training', 'online_training') NOT NULL,
    session_date DATE NOT NULL,
    start_time DATETIME,
    end_time DATETIME,
    duration_minutes INT,
    
    status ENUM('scheduled', 'in_progress', 'completed', 'cancelled', 'no_show') DEFAULT 'scheduled',
    cancellation_reason TEXT,
    cancelled_at DATETIME,
    cancelled_by VARCHAR(50),
    
    -- Workout details
    workout_type ENUM('cardio', 'strength', 'flexibility', 'hiit', 'crossfit', 'yoga', 'pilates'),
    intensity ENUM('low', 'moderate', 'high', 'extreme'),
    focus VARCHAR(100),
    
    -- Statistics
    calories_burned INT,
    distance DECIMAL(5,2),
    heart_rate_avg INT,
    heart_rate_max INT,
    steps INT,
    
    -- Notes and ratings
    notes TEXT,
    coach_notes TEXT,
    member_rating INT CHECK (member_rating BETWEEN 1 AND 5),
    member_feedback TEXT,
    coach_rating INT CHECK (coach_rating BETWEEN 1 AND 5),
    
    -- Check-in/out
    check_in_time DATETIME,
    check_out_time DATETIME,
    check_in_method ENUM('qr_code', 'card', 'manual'),
    
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE,
    FOREIGN KEY (coach_id) REFERENCES coaches(coach_id)
);

-- 6. Payments table
CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    membership_package_id INT,
    
    payment_code VARCHAR(50) UNIQUE NOT NULL,
    payment_type ENUM('membership', 'personal_training', 'product', 'service', 'penalty') NOT NULL,
    
    amount DECIMAL(15,2) NOT NULL,
    discount_amount DECIMAL(15,2),
    final_amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'VND',
    
    payment_method ENUM('cash', 'credit_card', 'debit_card', 'bank_transfer', 'e_wallet', 'qr_code') NOT NULL,
    payment_status ENUM('pending', 'completed', 'failed', 'refunded', 'cancelled') DEFAULT 'pending',
    
    payment_date DATETIME,
    due_date DATE,
    transaction_id VARCHAR(100),
    bank_transaction_id VARCHAR(100),
    
    -- Refund
    is_refunded BOOLEAN DEFAULT FALSE,
    refund_amount DECIMAL(15,2),
    refund_date DATETIME,
    refund_reason TEXT,
    
    description TEXT,
    notes TEXT,
    receipt_url VARCHAR(255),
    
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE,
    FOREIGN KEY (membership_package_id) REFERENCES membership_packages(package_id)
);

-- Create indexes for better performance
CREATE INDEX idx_members_user_id ON members(user_id);
CREATE INDEX idx_members_status ON members(status);
CREATE INDEX idx_coaches_user_id ON coaches(user_id);
CREATE INDEX idx_sessions_member_date ON workout_sessions(member_id, session_date);
CREATE INDEX idx_sessions_coach_date ON workout_sessions(coach_id, session_date);
CREATE INDEX idx_payments_member_id ON payments(member_id);
CREATE INDEX idx_payments_status ON payments(payment_status);
```

---

## 🚀 ROADMAP PHÁT TRIỂN

### **Phase 1: Core Foundation** (Hiện tại) ✅
- ✅ User authentication
- ✅ Member management
- ✅ Coach management
- ✅ Membership packages
- ✅ Basic workout tracking
- ✅ Payment processing

### **Phase 2: Enhanced Features** (Gợi ý tiếp theo)
- 📋 **Attendance System**: Check-in/Check-out tự động
- 📊 **Body Metrics Tracking**: Theo dõi số đo cơ thể theo thời gian
- 📅 **Advanced Scheduling**: Lịch phức tạp cho coach và member
- 🍎 **Nutrition Planning**: Kế hoạch dinh dưỡng chi tiết
- 💪 **Workout Plans**: Kế hoạch tập luyện tuỳ chỉnh
- 📝 **Exercise Library**: Thư viện bài tập với hướng dẫn

### **Phase 3: Business Intelligence**
- 📈 **Analytics Dashboard**: Dashboard thống kê doanh thu, member
- 🎯 **Goal Tracking**: Theo dõi mục tiêu của member
- ⭐ **Review System**: Đánh giá và phản hồi chi tiết
- 🔔 **Notification System**: Thông báo đa kênh (email, SMS, push)
- 📊 **Reports**: Báo cáo tài chính, hiệu suất

### **Phase 4: Advanced Features**
- 🏪 **E-commerce**: Bán sản phẩm (protein, thiết bị...)
- 👥 **Group Classes**: Quản lý lớp học nhóm
- 🎥 **Video Training**: Hướng dẫn video
- 📱 **Mobile App Integration**: API cho mobile
- 🤖 **AI Recommendations**: Gợi ý workout và nutrition

---

## 📌 GỢI Ý BỔ SUNG

### 1. **Security Best Practices**
```java
// Mã hóa mật khẩu
- Sử dụng BCrypt hoặc Argon2
- Không lưu plain text password

// JWT Token
- Implement JWT cho authentication
- Refresh token mechanism

// Role-based Access Control
- Annotation-based security
- Method-level security
```

### 2. **API Design**
```
RESTful API Structure:
- GET    /api/members - Lấy danh sách members
- GET    /api/members/{id} - Lấy chi tiết member
- POST   /api/members - Tạo member mới
- PUT    /api/members/{id} - Cập nhật member
- DELETE /api/members/{id} - Xóa member
- GET    /api/members/{id}/sessions - Lấy sessions của member
```

### 3. **Validation**
```java
// Sử dụng Bean Validation
@NotNull
@Email
@Size(min = 10, max = 10)
@Pattern(regexp = "^[0-9]{10}$")
```

### 4. **Audit Trail**
Thêm các trường audit cho mọi table:
- `created_at`: Thời gian tạo
- `updated_at`: Thời gian cập nhật
- `created_by`: Người tạo
- `updated_by`: Người cập nhật
- `version`: Version control (Optimistic Locking)

---

## 🎓 KẾT LUẬN

Hệ thống Model đã được thiết kế:
- ✅ **Scalable**: Dễ mở rộng
- ✅ **Maintainable**: Dễ bảo trì
- ✅ **Comprehensive**: Đầy đủ tính năng
- ✅ **Business-oriented**: Phù hợp nghiệp vụ
- ✅ **Best practices**: Theo chuẩn industry

**Bước tiếp theo:**
1. Implement DAO layer cho từng model
2. Tạo Service layer với business logic
3. Build RESTful API Controllers
4. Unit testing và Integration testing
5. Deploy và monitoring

---

📝 **Document version**: 1.0  
📅 **Last updated**: 2024-10-10  
👨‍💻 **Author**: AI Assistant

