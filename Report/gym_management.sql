CREATE DATABASE gym_management
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;
USE gym_management;

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;


-- TABLE GYMS

CREATE TABLE `gyms` (
  `gym_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `hotline` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `created_date` date DEFAULT NULL,
  `updated_date` date DEFAULT NULL,
  PRIMARY KEY (`gym_id`),
  KEY `idx_gym_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


INSERT INTO gyms (name, address, hotline, email, description) VALUES
('GymFit Đà Nẵng', '123 Nguyễn Văn Linh, Đà Nẵng', '1900 1234', 'danang@gymfit.vn', 'Phòng gym hiện đại, view sông Hàn'),
('GymFit Hà Nội', '45 Lý Thường Kiệt, Hà Nội', '1900 5678', 'hanoi@gymfit.vn', 'Trung tâm thành phố, đầy đủ tiện ích'),
('GymFit TP.HCM', '98 Trần Hưng Đạo, TP.HCM', '1900 9012', 'hcm@gymfit.vn', 'Chi nhánh lớn nhất, có yoga & bơi lội'),
('GymFit Cần Thơ', '56 Võ Văn Kiệt, Cần Thơ', '1900 3456', 'cantho@gymfit.vn', 'Phòng gym mới mở, không gian rộng');



-- TABLE USER

CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `DTYPE` varchar(31) COLLATE utf8mb4_unicode_ci DEFAULT 'User',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdDate` date DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastLogin` date DEFAULT NULL,
  `lastUpdate` date DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'USER',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `unique_username` (`username`),
  UNIQUE KEY `unique_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=229 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE PASSWORD RESET TOKEN

CREATE TABLE `password_reset_tokens` (
  `token_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `token` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `expires_at` timestamp NOT NULL,
  `used` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`token_id`),
  UNIQUE KEY `token` (`token`),
  KEY `idx_token` (`token`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_expires_at` (`expires_at`),
  CONSTRAINT `password_reset_tokens_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- TABLE ADMIN

CREATE TABLE `admin` (
  `admin_id` int NOT NULL,
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`admin_id`),
  CONSTRAINT `fk_admin_user` FOREIGN KEY (`admin_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE TRAINER

CREATE TABLE `trainer` (
  `trainer_id` int NOT NULL,
  `specialization` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Chuyên môn - Specialization areas (VD: Cardio, Yoga, Bodybuilding)',
  `years_of_experience` int DEFAULT NULL COMMENT 'Kinh nghiệm - Years of experience (Thâm niên)',
  `certification_level` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Trình độ - Certification level',
  `salary` decimal(10,2) DEFAULT '0.00',
  `workAt` int DEFAULT NULL,
  PRIMARY KEY (`trainer_id`),
  KEY `idx_trainer_experience` (`years_of_experience`),
  KEY `idx_trainer_gym` (`workAt`),
  CONSTRAINT `fk_trainer_gym` FOREIGN KEY (`workAt`) REFERENCES `gyms` (`gym_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `trainer_ibfk_1` FOREIGN KEY (`trainer_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `trainer_chk_1` CHECK ((`salary` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Trainer table - extends User with JOINED inheritance. Linked via role=''Trainer''';



-- TABLE MEMBERS

CREATE TABLE `members` (
  `member_id` int NOT NULL,
  `weight` float DEFAULT NULL,
  `height` float DEFAULT NULL,
  `bmi` float DEFAULT NULL,
  `emergency_contact_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `emergency_contact_phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `emergency_contact_relation` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `emergency_contact_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `goal` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pt_note` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`member_id`),
  CONSTRAINT `members_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE PACKAGE

CREATE TABLE `packages` (
  `package_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `duration_months` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `max_sessions` int DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`package_id`),
  KEY `idx_pkg_name` (`name`),
  KEY `idx_pkg_active` (`is_active`),
  CONSTRAINT `ck_duration` CHECK ((`duration_months` > 0)),
  CONSTRAINT `ck_price_packages` CHECK ((`price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE PRODUCT

CREATE TABLE `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `product_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `product_type` enum('SUPPLEMENT','EQUIPMENT','APPAREL','ACCESSORY','OTHER') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OTHER',
  `price` decimal(10,2) NOT NULL,
  `cost_price` decimal(10,2) DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `stock_quantity` int NOT NULL DEFAULT '0',
  `unit` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'cái',
  `is_active` tinyint(1) DEFAULT '1',
  `is_virtual` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_id`),
  KEY `idx_prod_type` (`product_type`),
  KEY `idx_prod_name` (`product_name`),
  KEY `idx_prod_active` (`is_active`),
  CONSTRAINT `ck_cost` CHECK (((`cost_price` is null) or (`cost_price` >= 0))),
  CONSTRAINT `ck_price` CHECK ((`price` >= 0)),
  CONSTRAINT `ck_stock` CHECK ((`stock_quantity` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE ORDERS

CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `member_id` int NOT NULL,
  `order_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `order_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total_amount` decimal(10,2) NOT NULL,
  `discount_amount` decimal(10,2) DEFAULT '0.00',
  `order_status` enum('PENDING','CONFIRMED','PREPARING','READY','COMPLETED','CANCELLED') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `delivery_method` enum('PICKUP','DELIVERY') COLLATE utf8mb4_unicode_ci DEFAULT 'PICKUP',
  `delivery_address` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `delivery_phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `delivery_notes` text COLLATE utf8mb4_unicode_ci,
  `notes` text COLLATE utf8mb4_unicode_ci,
  `confirmed_at` timestamp NULL DEFAULT NULL,
  `completed_at` timestamp NULL DEFAULT NULL,
  `cancelled_at` timestamp NULL DEFAULT NULL,
  `cancellation_reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `order_number` (`order_number`),
  KEY `idx_ord_user` (`member_id`),
  KEY `idx_ord_date` (`order_date`),
  KEY `idx_ord_status` (`order_status`),
  KEY `idx_ord_number` (`order_number`),
  CONSTRAINT `fk_orders_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
  CONSTRAINT `ck_amounts` CHECK (((`total_amount` >= 0) and (`discount_amount` >= 0)))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE ORDER_DETAILS

CREATE TABLE `order_details` (
  `order_detail_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int DEFAULT NULL,
  `package_id` int DEFAULT NULL,
  `product_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `discount_percent` decimal(5,2) DEFAULT '0.00',
  `discount_amount` decimal(10,2) DEFAULT '0.00',
  `subtotal` decimal(10,2) GENERATED ALWAYS AS (((`quantity` * `unit_price`) - `discount_amount`)) STORED,
  `notes` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`order_detail_id`),
  KEY `idx_od_order` (`order_id`),
  KEY `idx_od_product` (`product_id`),
  KEY `idx_od_package` (`package_id`),
  CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE RESTRICT,
  CONSTRAINT `order_details_ibfk_3` FOREIGN KEY (`package_id`) REFERENCES `packages` (`package_id`) ON DELETE RESTRICT,
  CONSTRAINT `ck_disc_amount` CHECK ((`discount_amount` >= 0)),
  CONSTRAINT `ck_disc_percent` CHECK (((`discount_percent` >= 0) and (`discount_percent` <= 100))),
  CONSTRAINT `ck_product_or_package` CHECK ((((`product_id` is not null) and (`package_id` is null)) or ((`product_id` is null) and (`package_id` is not null)))),
  CONSTRAINT `ck_qty` CHECK ((`quantity` > 0)),
  CONSTRAINT `ck_unit_price` CHECK ((`unit_price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE CART

CREATE TABLE `cart` (
  `cart_id` int NOT NULL AUTO_INCREMENT,
  `member_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `added_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`cart_id`),
  UNIQUE KEY `uq_cart_user_product` (`member_id`,`product_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cart_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
  CONSTRAINT `ck_cart_qty` CHECK ((`quantity` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE PAYMENT

CREATE TABLE `payments` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `member_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `method` enum('CASH','CREDIT_CARD','BANK_TRANSFER','MOMO','VNPAY') COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('PENDING','PAID','FAILED','REFUNDED','CANCELED','EXPIRED','CHARGEBACK') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING' COMMENT 'PENDING=chờ, PAID=đã thanh toán, FAILED=thất bại, REFUNDED=hoàn tiền, CANCELED=hủy, EXPIRED=hết hạn, CHARGEBACK=tranh chấp',
  `reference_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `transaction_type` enum('PACKAGE','PRODUCT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `membership_id` int DEFAULT NULL,
  `order_id` int DEFAULT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci,
  `paid_at` date DEFAULT NULL COMMENT 'Thời điểm payment được xác nhận (status = PAID)',
  `external_ref` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Mã tham chiếu từ payment provider (VNPay, MoMo, etc.)',
  PRIMARY KEY (`payment_id`),
  UNIQUE KEY `reference_id` (`reference_id`),
  KEY `idx_pay_user` (`member_id`),
  KEY `idx_pay_status` (`status`),
  KEY `idx_pay_mem` (`membership_id`),
  KEY `idx_pay_order` (`order_id`),
  KEY `idx_pay_type` (`transaction_type`),
  KEY `idx_pay_paid_at` (`paid_at`),
  CONSTRAINT `fk_payments_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
  CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`membership_id`) REFERENCES `memberships` (`membership_id`) ON DELETE RESTRICT,
  CONSTRAINT `payments_ibfk_3` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE RESTRICT,
  CONSTRAINT `ck_amount` CHECK ((`amount` > 0)),
  CONSTRAINT `ck_one_target` CHECK ((((`transaction_type` = _utf8mb4'PACKAGE') and (`membership_id` is not null) and (`order_id` is null)) or ((`transaction_type` = _utf8mb4'PRODUCT') and (`order_id` is not null) and (`membership_id` is null))))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE MEMBERSHIPS

CREATE TABLE `memberships` (
  `membership_id` int NOT NULL AUTO_INCREMENT,
  `member_id` int NOT NULL,
  `package_id` int DEFAULT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `status` enum('INACTIVE','ACTIVE','EXPIRED','CANCELLED','SUSPENDED') COLLATE utf8mb4_unicode_ci DEFAULT 'INACTIVE' COMMENT 'INACTIVE=chờ thanh toán, ACTIVE=đã kích hoạt, EXPIRED=hết hạn, CANCELLED=hủy, SUSPENDED=tạm ngưng',
  `notes` text COLLATE utf8mb4_unicode_ci,
  `created_date` date DEFAULT (curdate()),
  `updated_date` date DEFAULT (curdate()),
  `activated_at` date DEFAULT NULL COMMENT 'Thời điểm membership được activate (khi payment = PAID)',
  `suspended_at` date DEFAULT NULL COMMENT 'Thời điểm membership bị suspend (khi payment refunded/chargeback)',
  PRIMARY KEY (`membership_id`),
  KEY `idx_mem_user` (`member_id`),
  KEY `idx_mem_pkg` (`package_id`),
  KEY `idx_mem_status` (`status`),
  KEY `idx_mem_activated` (`activated_at`),
  KEY `idx_mem_suspended` (`suspended_at`),
  CONSTRAINT `fk_memberships_member_id` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `memberships_ibfk_2` FOREIGN KEY (`package_id`) REFERENCES `packages` (`package_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `ck_dates` CHECK ((`end_date` > `start_date`))
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE TIMESLOTS các khung giờ để book PT

CREATE TABLE `time_slots` (
  `slot_id` int NOT NULL AUTO_INCREMENT,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `duration_minutes` int NOT NULL,
  `slot_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`slot_id`),
  UNIQUE KEY `uk_time_range` (`start_time`,`end_time`),
  CONSTRAINT `ck_duration2` CHECK ((`duration_minutes` > 0)),
  CONSTRAINT `ck_time_valid` CHECK ((`end_time` > `start_time`))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Các khung giờ tập cố định trong ngày';



-- TABLE TRAINER SCHEDULES

CREATE TABLE `trainer_schedules` (
  `schedule_id` int NOT NULL AUTO_INCREMENT,
  `trainer_id` int NOT NULL COMMENT 'user_id của trainer',
  `gym_id` int NOT NULL,
  `day_of_week` enum('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') COLLATE utf8mb4_unicode_ci NOT NULL,
  `slot_id` int NOT NULL,
  `is_available` tinyint(1) DEFAULT '1' COMMENT '1: Có thể book, 0: Nghỉ/Bận',
  `max_bookings` int DEFAULT '1' COMMENT 'Số lượng member tối đa có thể book (thường là 1)',
  `notes` text COLLATE utf8mb4_unicode_ci COMMENT 'Ghi chú của trainer',
  `created_at` date DEFAULT (curdate()),
  `updated_at` date DEFAULT (curdate()),
  PRIMARY KEY (`schedule_id`),
  UNIQUE KEY `uk_trainer_schedule` (`trainer_id`,`gym_id`,`day_of_week`,`slot_id`),
  KEY `slot_id` (`slot_id`),
  KEY `idx_trainer_day` (`trainer_id`,`day_of_week`),
  KEY `idx_gym_day` (`gym_id`,`day_of_week`),
  CONSTRAINT `trainer_schedules_ibfk_1` FOREIGN KEY (`trainer_id`) REFERENCES `trainer` (`trainer_id`) ON DELETE CASCADE,
  CONSTRAINT `trainer_schedules_ibfk_2` FOREIGN KEY (`gym_id`) REFERENCES `gyms` (`gym_id`) ON DELETE CASCADE,
  CONSTRAINT `trainer_schedules_ibfk_3` FOREIGN KEY (`slot_id`) REFERENCES `time_slots` (`slot_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Lịch làm việc định kỳ của trainer theo tuần';



-- TABLE TRAINER EXCEPTIONS đăng ký lịch nghỉ cho PT

CREATE TABLE `trainer_exceptions` (
  `exception_id` int NOT NULL AUTO_INCREMENT,
  `trainer_id` int NOT NULL COMMENT 'user_id của trainer',
  `exception_date` date NOT NULL,
  `slot_id` int DEFAULT NULL COMMENT 'NULL = nghỉ cả ngày',
  `exception_type` enum('OFF','BUSY','SPECIAL') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'OFF: Nghỉ, BUSY: Bận, SPECIAL: Lịch đặc biệt',
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  PRIMARY KEY (`exception_id`),
  KEY `slot_id` (`slot_id`),
  KEY `idx_trainer_date` (`trainer_id`,`exception_date`),
  CONSTRAINT `trainer_exceptions_ibfk_1` FOREIGN KEY (`trainer_id`) REFERENCES `trainer` (`trainer_id`) ON DELETE CASCADE,
  CONSTRAINT `trainer_exceptions_ibfk_2` FOREIGN KEY (`slot_id`) REFERENCES `time_slots` (`slot_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Lịch nghỉ hoặc thay đổi đột xuất của trainer';



-- TABLE PT BOOKINGS member đặt lịch PT

CREATE TABLE `pt_bookings` (
  `booking_id` int NOT NULL AUTO_INCREMENT,
  `member_id` int NOT NULL COMMENT 'user_id của member',
  `trainer_id` int NOT NULL COMMENT 'user_id của trainer',
  `gym_id` int NOT NULL,
  `slot_id` int NOT NULL,
  `booking_date` date NOT NULL,
  `booking_status` enum('PENDING','CONFIRMED','COMPLETED','CANCELLED') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `notes` text COLLATE utf8mb4_unicode_ci COMMENT 'Ghi chú của member',
  `cancelled_reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cancelled_by` enum('MEMBER','TRAINER','ADMIN') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` date NOT NULL,
  `confirmed_at` date DEFAULT NULL,
  `completed_at` date DEFAULT NULL,
  `cancelled_at` date DEFAULT NULL,
  PRIMARY KEY (`booking_id`),
  UNIQUE KEY `uk_booking_unique` (`trainer_id`,`gym_id`,`booking_date`,`slot_id`),
  KEY `slot_id` (`slot_id`),
  KEY `idx_member_bookings` (`member_id`,`booking_date`),
  KEY `idx_trainer_bookings` (`trainer_id`,`booking_date`),
  KEY `idx_gym_date` (`gym_id`,`booking_date`),
  KEY `idx_status` (`booking_status`),
  CONSTRAINT `pt_bookings_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
  CONSTRAINT `pt_bookings_ibfk_2` FOREIGN KEY (`trainer_id`) REFERENCES `trainer` (`trainer_id`) ON DELETE CASCADE,
  CONSTRAINT `pt_bookings_ibfk_3` FOREIGN KEY (`gym_id`) REFERENCES `gyms` (`gym_id`) ON DELETE CASCADE,
  CONSTRAINT `pt_bookings_ibfk_4` FOREIGN KEY (`slot_id`) REFERENCES `time_slots` (`slot_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Lịch đặt PT của member';



-- TABLE MEMBER DAILY TARGETS

CREATE TABLE `member_daily_targets` (
  `member_id` int NOT NULL,
  `target_date` date NOT NULL,
  `calories_kcal` decimal(8,2) NOT NULL,
  `protein_g` decimal(8,2) NOT NULL,
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`member_id`,`target_date`),
  CONSTRAINT `fk_udt_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
  CONSTRAINT `CK_udt_nonneg` CHECK (((`calories_kcal` >= 0) and (`protein_g` >= 0)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE MEMBER NUTRITION GOALS

CREATE TABLE `member_nutrition_goals` (
  `member_id` int NOT NULL,
  `goal_type` enum('giam can','tang can','giu dang') COLLATE utf8mb4_unicode_ci NOT NULL,
  `activity_factor` decimal(4,2) NOT NULL DEFAULT '1.55',
  `daily_calories_target` decimal(8,2) DEFAULT NULL,
  `daily_protein_target` decimal(8,2) DEFAULT NULL,
  `updated_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`member_id`),
  CONSTRAINT `fk_goals_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
  CONSTRAINT `CK_goals_nonneg` CHECK ((((`daily_calories_target` is null) or (`daily_calories_target` >= 0)) and ((`daily_protein_target` is null) or (`daily_protein_target` >= 0))))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE MEMBER MEALS

CREATE TABLE `member_meals` (
  `id` int NOT NULL AUTO_INCREMENT,
  `member_id` int NOT NULL,
  `food_id` int NOT NULL,
  `meal_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `eaten_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `servings` decimal(8,2) NOT NULL,
  `snap_calories` decimal(8,2) NOT NULL,
  `snap_protein_g` decimal(8,2) NOT NULL,
  `snap_carbs_g` decimal(8,2) NOT NULL,
  `snap_fat_g` decimal(8,2) NOT NULL,
  `total_calories` decimal(10,2) GENERATED ALWAYS AS (round((`servings` * `snap_calories`),2)) STORED,
  `total_protein_g` decimal(10,2) GENERATED ALWAYS AS (round((`servings` * `snap_protein_g`),2)) STORED,
  `total_carbs_g` decimal(10,2) GENERATED ALWAYS AS (round((`servings` * `snap_carbs_g`),2)) STORED,
  `total_fat_g` decimal(10,2) GENERATED ALWAYS AS (round((`servings` * `snap_fat_g`),2)) STORED,
  PRIMARY KEY (`id`),
  KEY `FK_user_meals_food` (`food_id`),
  KEY `IX_user_meals_user_time` (`member_id`,`eaten_at`),
  CONSTRAINT `FK_user_meals_food` FOREIGN KEY (`food_id`) REFERENCES `foods` (`id`),
  CONSTRAINT `fk_user_meals_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
  CONSTRAINT `CK_user_meals_servings` CHECK ((`servings` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE FOODS

CREATE TABLE `foods` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `serving_label` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `calories` decimal(8,2) NOT NULL,
  `protein_g` decimal(8,2) NOT NULL,
  `carbs_g` decimal(8,2) NOT NULL,
  `fat_g` decimal(8,2) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_foods_name` (`name`),
  KEY `IX_foods_active_name` (`is_active`,`name`),
  CONSTRAINT `CK_foods_nonneg` CHECK (((`calories` >= 0) and (`protein_g` >= 0) and (`carbs_g` >= 0) and (`fat_g` >= 0)))
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- TABLE ADVISORY REQUEST yêu cầu tư vấn từ khách hàng

CREATE TABLE `advisory_requests` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID tự động tăng',
  `full_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Họ và tên khách hàng',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Số điện thoại',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Địa chỉ email',
  `address` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Địa chỉ',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo yêu cầu',
  PRIMARY KEY (`id`),
  KEY `idx_email` (`email`),
  KEY `idx_phone` (`phone`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Bảng lưu trữ yêu cầu tư vấn từ khách hàng';



-- TRIGGERS

-- ========================================
-- TRIGGER 1: KHI PAYMENT = PAID → MEMBERSHIP = ACTIVE
-- ========================================

CREATE TRIGGER trg_activate_membership_on_payment
AFTER UPDATE ON payments
FOR EACH ROW
BEGIN
    -- Chỉ xử lý khi:
    -- 1. Status chuyển từ khác PAID sang PAID
    -- 2. Transaction type là PACKAGE
    -- 3. Có membership_id
    IF NEW.status = 'PAID' 
       AND OLD.status != 'PAID' 
       AND NEW.transaction_type = 'PACKAGE'
       AND NEW.membership_id IS NOT NULL THEN
        
        -- Chỉ cập nhật status sang ACTIVE
        UPDATE memberships
        SET status = 'ACTIVE',
            activated_at = CURRENT_DATE
        WHERE membership_id = NEW.membership_id;
        
        -- Cập nhật paid_at trong payments
        UPDATE payments
        SET paid_at = CURRENT_TIMESTAMP
        WHERE payment_id = NEW.payment_id;
    END IF;
END;

-- ========================================
-- TRIGGER 2: KHI PAYMENT = REFUNDED/CHARGEBACK → MEMBERSHIP = SUSPENDED
-- ========================================

CREATE TRIGGER trg_suspend_membership_on_refund
AFTER UPDATE ON payments
FOR EACH ROW
BEGIN
    -- Chỉ xử lý khi status chuyển sang REFUNDED hoặc CHARGEBACK
    IF (NEW.status = 'REFUNDED' OR NEW.status = 'CHARGEBACK')
       AND (OLD.status != 'REFUNDED' AND OLD.status != 'CHARGEBACK')
       AND NEW.transaction_type = 'PACKAGE'
       AND NEW.membership_id IS NOT NULL THEN
        
        -- Suspend membership
        UPDATE memberships
        SET status = 'SUSPENDED',
            suspended_at = CURRENT_DATE
        WHERE membership_id = NEW.membership_id;
    END IF;
END;

-- ========================================
-- EVENT: TỰ ĐỘNG EXPIRE KHI HẾT HẠN
-- ========================================

-- Xóa event cũ nếu có
DROP EVENT IF EXISTS evt_auto_expire_memberships;

-- Tạo event mới (chỉ chạy nếu có quyền)
CREATE EVENT IF NOT EXISTS evt_auto_expire_memberships
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_DATE + INTERVAL 1 DAY + INTERVAL 1 MINUTE
COMMENT 'Tự động chuyển status sang EXPIRED khi end_date < CURRENT_DATE'
DO
UPDATE memberships
SET status = 'EXPIRED'
WHERE end_date < CURRENT_DATE
  AND status = 'ACTIVE';



SHOW TRIGGERS WHERE `Table` IN ('memberships', 'payments');

SHOW EVENTS WHERE Name = 'evt_auto_expire_memberships';

SHOW VARIABLES LIKE 'event_scheduler';


-- ========================================
-- TRIGGER 3: KHI PAYMENT = PAID → STOCK_QUANTITY ...
-- ========================================

CREATE TRIGGER trg_after_payment_update_stock
AFTER UPDATE ON payments
FOR EACH ROW
BEGIN
    -- Khi:
    -- 1. Status payment chuyển sang PAID
    -- 2. Với transaction type là PRODUCT
    -- 3. Có order_id hợp lệ
    IF NEW.status = 'PAID' 
       AND OLD.status != 'PAID' 
       AND NEW.transaction_type = 'PRODUCT'
       AND NEW.order_id IS NOT NULL THEN
        
        -- Cập nhật stock_quantity cho từng sản phẩm trong đơn hàng
        UPDATE products p
        INNER JOIN order_details od ON p.product_id = od.product_id
        SET p.stock_quantity = p.stock_quantity - od.quantity
        WHERE od.order_id = NEW.order_id
          AND od.product_id IS NOT NULL  -- Chỉ xử lý product, không xử lý package
          AND p.stock_quantity >= od.quantity;  -- Đảm bảo không âm
        
        -- Cập nhật trạng thái đơn hàng thành CONFIRMED
        UPDATE orders
        SET order_status = 'CONFIRMED',
            confirmed_at = CURRENT_TIMESTAMP
        WHERE order_id = NEW.order_id
          AND order_status = 'PENDING';
          
    END IF;
END;

SHOW TRIGGERS LIKE 'payments';
