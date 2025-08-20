-- Habit Tracker Database Setup Script

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS habit_tracker 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Use the database
USE habit_tracker;

CREATE USER IF NOT EXISTS 'habittracker'@'localhost' IDENTIFIED BY 'HabitTracker@123';

-- Grant permissions to the user
GRANT ALL PRIVILEGES ON habit_tracker.* TO 'habittracker'@'localhost';

-- Flush privileges to ensure they take effect
FLUSH PRIVILEGES;

-- Display confirmation
SELECT 'Database setup completed successfully!' AS Status;

-- Show databases to confirm creation
SHOW DATABASES LIKE 'habit_tracker';

-- Show user to confirm creation
SELECT User, Host FROM mysql.user WHERE User = 'habittracker';
