-- MySQL initialization script for Neighborhood Help Desk
-- This script will be executed when MySQL container starts

-- Create database if it doesn't exist (redundant but safe)
CREATE DATABASE IF NOT EXISTS neighborhood_help_desk;

-- Use the database
USE neighborhood_help_desk;

-- Set proper character set and collation for UTF-8 support
ALTER DATABASE neighborhood_help_desk CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create a dedicated user for the application (optional)
-- CREATE USER IF NOT EXISTS 'helpdesk_user'@'%' IDENTIFIED BY 'helpdesk_password';
-- GRANT ALL PRIVILEGES ON neighborhood_help_desk.* TO 'helpdesk_user'@'%';
-- FLUSH PRIVILEGES;

-- Note: Tables will be created automatically by Hibernate/JPA
-- This script is mainly for database initialization and user setup
