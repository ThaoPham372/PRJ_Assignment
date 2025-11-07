-- Tạo bảng trainer_awards để lưu danh hiệu của Trainer
CREATE TABLE IF NOT EXISTS trainer_awards (
  id INT AUTO_INCREMENT PRIMARY KEY,
  trainer_id INT NOT NULL,
  award_name VARCHAR(100) NOT NULL,
  awarded_month DATE NOT NULL,
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (trainer_id) REFERENCES trainer(user_id) ON DELETE CASCADE,
  INDEX idx_trainer_id (trainer_id),
  INDEX idx_awarded_month (awarded_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Bảng lưu danh hiệu của Trainer';

-- Kiểm tra bảng đã được tạo
SELECT 'Table trainer_awards created successfully' AS status;

-- ============================================
-- DỮ LIỆU MẪU CHO BẢNG trainer_awards
-- ============================================

-- Trainer ID 1 - Top PT trong tháng 9/2024
INSERT INTO trainer_awards (trainer_id, award_name, awarded_month, description) VALUES
(1, 'Top PT trong tháng 9/2024', '2024-09-01', 'Huấn luyện viên có nhiều buổi tập nhất trong tháng (45 buổi)');

-- Trainer ID 1 - PT được đánh giá cao nhất tháng 10/2024
INSERT INTO trainer_awards (trainer_id, award_name, awarded_month, description) VALUES
(1, 'PT được đánh giá cao nhất tháng 10/2024', '2024-10-01', 'Huấn luyện viên có đánh giá trung bình cao nhất trong tháng (4.9 điểm)');

-- Trainer ID 2 - Top PT trong tháng 10/2024
INSERT INTO trainer_awards (trainer_id, award_name, awarded_month, description) VALUES
(2, 'Top PT trong tháng 10/2024', '2024-10-01', 'Huấn luyện viên có nhiều buổi tập nhất trong tháng (52 buổi)');

-- Trainer ID 2 - PT có tỷ lệ hoàn thành cao nhất tháng 11/2024
INSERT INTO trainer_awards (trainer_id, award_name, awarded_month, description) VALUES
(2, 'PT có tỷ lệ hoàn thành cao nhất tháng 11/2024', '2024-11-01', 'Huấn luyện viên có tỷ lệ hoàn thành cao nhất trong tháng (98.5%)');

-- Trainer ID 41 - Top PT trong tháng 11/2024
INSERT INTO trainer_awards (trainer_id, award_name, awarded_month, description) VALUES
(41, 'Top PT trong tháng 11/2024', '2024-11-01', 'Huấn luyện viên có nhiều buổi tập nhất trong tháng (38 buổi)');

-- Trainer ID 41 - PT được đánh giá cao nhất tháng 12/2024
INSERT INTO trainer_awards (trainer_id, award_name, awarded_month, description) VALUES
(41, 'PT được đánh giá cao nhất tháng 12/2024', '2024-12-01', 'Huấn luyện viên có đánh giá trung bình cao nhất trong tháng (4.8 điểm)');

-- Trainer ID 117 - PT có tỷ lệ hoàn thành cao nhất tháng 9/2024
INSERT INTO trainer_awards (trainer_id, award_name, awarded_month, description) VALUES
(117, 'PT có tỷ lệ hoàn thành cao nhất tháng 9/2024', '2024-09-01', 'Huấn luyện viên có tỷ lệ hoàn thành cao nhất trong tháng (97.2%)');

-- Trainer ID 117 - Top PT trong tháng 12/2024
INSERT INTO trainer_awards (trainer_id, award_name, awarded_month, description) VALUES
(117, 'Top PT trong tháng 12/2024', '2024-12-01', 'Huấn luyện viên có nhiều buổi tập nhất trong tháng (48 buổi)');

-- Trainer ID 149 - PT được đánh giá cao nhất tháng 9/2024
INSERT INTO trainer_awards (trainer_id, award_name, awarded_month, description) VALUES
(149, 'PT được đánh giá cao nhất tháng 9/2024', '2024-09-01', 'Huấn luyện viên có đánh giá trung bình cao nhất trong tháng (4.7 điểm)');

-- Trainer ID 149 - PT có tỷ lệ hoàn thành cao nhất tháng 10/2024
INSERT INTO trainer_awards (trainer_id, award_name, awarded_month, description) VALUES
(149, 'PT có tỷ lệ hoàn thành cao nhất tháng 10/2024', '2024-10-01', 'Huấn luyện viên có tỷ lệ hoàn thành cao nhất trong tháng (96.8%)');

-- Kiểm tra dữ liệu đã insert
SELECT 
    ta.id,
    ta.trainer_id,
    t.user_id AS trainer_user_id,
    u.name AS trainer_name,
    ta.award_name,
    ta.awarded_month,
    ta.description,
    ta.created_at
FROM trainer_awards ta
LEFT JOIN trainer t ON ta.trainer_id = t.user_id
LEFT JOIN user u ON t.user_id = u.user_id
ORDER BY ta.awarded_month DESC, ta.trainer_id;

