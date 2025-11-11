#!/bin/bash

# Script to setup and run Gym Management System in Local Development Environment
# Tập lệnh thiết lập và chạy Hệ thống Quản lý Phòng Gym trong Môi trường Phát triển Local

echo "======================================"
echo "Gym Management System - Local Setup"
echo "======================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Java is installed
print_status "Checking Java installation..."
if ! command -v java &> /dev/null; then
    print_error "Java is not installed. Please install Java 17 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    print_error "Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi
print_status "Java version: $JAVA_VERSION ✓"

# Check if Maven is installed
print_status "Checking Maven installation..."
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed. Please install Maven."
    exit 1
fi
print_status "Maven is installed ✓"

# Check if MySQL is running
print_status "Checking MySQL connection..."
if ! mysql -u root -e "SELECT 1;" &> /dev/null; then
    print_warning "MySQL is not running or not accessible with root user."
    print_status "Please ensure MySQL is running and accessible."
    echo ""
    echo "To start MySQL on macOS:"
    echo "  brew services start mysql"
    echo ""
    echo "To create database:"
    echo "  mysql -u root -e \"CREATE DATABASE IF NOT EXISTS gym_management_local CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;\""
    echo ""
    read -p "Do you want to continue anyway? (y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
else
    print_status "MySQL connection successful ✓"
    
    # Create database if not exists
    print_status "Creating local database..."
    mysql -u root -e "CREATE DATABASE IF NOT EXISTS gym_management_local CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    print_status "Database setup complete ✓"
fi

# Clean and compile the project
print_status "Cleaning and compiling the project..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    print_error "Maven compile failed. Please check the errors above."
    exit 1
fi
print_status "Project compiled successfully ✓"

# Package the application
print_status "Packaging the application..."
mvn package -DskipTests -q

if [ $? -ne 0 ]; then
    print_error "Maven package failed. Please check the errors above."
    exit 1
fi
print_status "Application packaged successfully ✓"

echo ""
print_status "Setup completed successfully!"
echo ""
echo "Next steps:"
echo "1. Configure your email settings in: src/main/resources/email-local.properties"
echo "2. Start your application server (Tomcat/GlassFish/etc.)"
echo "3. Deploy the WAR file from target/GymManagement.war"
echo "4. Access the application at: http://localhost:8080/gym"
echo ""
echo "For Tomcat:"
echo "  mvn tomcat10:run"
echo ""
echo "For manual deployment:"
echo "  Copy target/GymManagement.war to your server's webapps directory"
echo ""

print_status "Local development environment is ready!"
