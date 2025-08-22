# 🗄️ MySQL Database Setup Guide

This application **exclusively uses MySQL 8.x** as its database. No other databases are supported.

## 📋 Prerequisites

- **MySQL 8.0+** installed and running
- **Port 3306** available (default MySQL port)
- **Root access** or dedicated user with full privileges

## 🚀 Quick Setup

### Option 1: Using Docker (Recommended)
```bash
# Start MySQL with Docker Compose
docker-compose up mysql -d

# Check if MySQL is running
docker-compose ps
```

### Option 2: Local MySQL Installation

#### Windows
1. Download MySQL Installer from [mysql.com](https://dev.mysql.com/downloads/installer/)
2. Run installer and choose "Developer Default"
3. Set root password during installation
4. Start MySQL service from Services panel

#### macOS
```bash
# Using Homebrew
brew install mysql
brew services start mysql

# Secure installation
mysql_secure_installation
```

#### Ubuntu/Debian
```bash
# Install MySQL
sudo apt update
sudo apt install mysql-server

# Start MySQL service
sudo systemctl start mysql
sudo systemctl enable mysql

# Secure installation
sudo mysql_secure_installation
```

## ⚙️ Configuration

### 1. Create Database
```sql
-- Connect to MySQL
mysql -u root -p

-- Create database (optional - app will create it)
CREATE DATABASE neighborhood_help_desk;

-- Create dedicated user (recommended)
CREATE USER 'helpdesk_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON neighborhood_help_desk.* TO 'helpdesk_user'@'localhost';
FLUSH PRIVILEGES;

-- Exit MySQL
EXIT;
```

### 2. Update Application Configuration
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/neighborhood_help_desk?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=helpdesk_user
spring.datasource.password=secure_password
```

## 🔍 Troubleshooting

### Common Issues

#### 1. Connection Refused
```
Error: java.sql.SQLException: Connection refused
```
**Solution:**
- Ensure MySQL is running: `sudo systemctl status mysql`
- Check port 3306 is open: `netstat -an | grep 3306`
- Verify MySQL is listening: `sudo ss -tlnp | grep 3306`

#### 2. Access Denied
```
Error: Access denied for user 'root'@'localhost'
```
**Solution:**
- Reset MySQL root password
- Check user privileges: `SHOW GRANTS FOR 'root'@'localhost';`
- Create new user with proper privileges

#### 3. Database Not Found
```
Error: Unknown database 'neighborhood_help_desk'
```
**Solution:**
- Ensure `createDatabaseIfNotExist=true` in connection URL
- Manually create database: `CREATE DATABASE neighborhood_help_desk;`

#### 4. Character Set Issues
```
Error: Incorrect string value
```
**Solution:**
- Set proper character set:
```sql
ALTER DATABASE neighborhood_help_desk CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Verification Commands

```bash
# Check MySQL version
mysql --version

# Test connection
mysql -u root -p -e "SELECT VERSION();"

# Check databases
mysql -u root -p -e "SHOW DATABASES;"

# Check if application database exists
mysql -u root -p -e "USE neighborhood_help_desk; SHOW TABLES;"
```

## 📊 Database Schema

The application will automatically create these tables:
- `users` - Community members
- `requests` - Help requests
- `assignments` - Request assignments
- `reviews` - User reviews and ratings

## 📞 Support

If you encounter MySQL-specific issues:
1. Check MySQL error logs: `/var/log/mysql/error.log`
2. Verify configuration in `application.properties`
3. Test connection manually with MySQL client
4. Create an issue with full error details

---

**Note**: This application is designed specifically for MySQL and will not work with other databases like PostgreSQL, SQLite, or H2.
