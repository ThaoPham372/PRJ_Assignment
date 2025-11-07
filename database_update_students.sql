-- =====================================================
-- UPDATE STUDENTS TABLE - Add new columns for PT management
-- =====================================================

USE gym_management;

-- Add new columns to students table if they don't exist
ALTER TABLE students
ADD COLUMN IF NOT EXISTS training_package VARCHAR(100),
ADD COLUMN IF NOT EXISTS training_duration VARCHAR(50),
ADD COLUMN IF NOT EXISTS goal VARCHAR(255),
ADD COLUMN IF NOT EXISTS training_progress INT DEFAULT 0,
ADD COLUMN IF NOT EXISTS training_sessions INT DEFAULT 0,
ADD COLUMN IF NOT EXISTS pt_note TEXT,
ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20);

-- Note: IF NOT EXISTS might not be supported in all MySQL versions
-- If you get an error, use this version instead (check each column manually):

-- Check and add training_package
-- ALTER TABLE students ADD COLUMN training_package VARCHAR(100);

-- Check and add training_duration
-- ALTER TABLE students ADD COLUMN training_duration VARCHAR(50);

-- Check and add goal
-- ALTER TABLE students ADD COLUMN goal VARCHAR(255);

-- Check and add training_progress
-- ALTER TABLE students ADD COLUMN training_progress INT DEFAULT 0;

-- Check and add training_sessions
-- ALTER TABLE students ADD COLUMN training_sessions INT DEFAULT 0;

-- Check and add pt_note
-- ALTER TABLE students ADD COLUMN pt_note TEXT;

-- Check and add phone_number
-- ALTER TABLE students ADD COLUMN phone_number VARCHAR(20);

