# Gym Management System - Development Setup

Hướng dẫn thiết lập môi trường phát triển cho Hệ thống Quản lý Phòng Gym.

## Yêu cầu hệ thống

### Bắt buộc:
- **Java 17+** 
- **Maven 3.6+**
- **MySQL 8.0+** hoặc **MariaDB 10.6+**
- **Apache Tomcat 10.1+** hoặc **Eclipse GlassFish 7+**

### Tùy chọn:
- **IntelliJ IDEA** hoặc **Eclipse IDE**
- **VS Code** với Java Extension Pack
- **MySQL Workbench** để quản lý database

## Thiết lập môi trường Local

### 1. Cấu hình Database

```bash
# Khởi động MySQL
brew services start mysql  # macOS
# hoặc
sudo systemctl start mysql  # Linux

# Tạo database local
mysql -u root -p
```

```sql
CREATE DATABASE gym_management_local 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Tạo user riêng (tùy chọn)
CREATE USER 'gym_user'@'localhost' IDENTIFIED BY 'gym_password';
GRANT ALL PRIVILEGES ON gym_management_local.* TO 'gym_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Cấu hình Email (Tùy chọn)

Sửa file `src/main/resources/email-local.properties`:

```properties
# Gmail App Password (khuyến nghị)
mail.username=your-email@gmail.com
mail.password=your-16-digit-app-password

# Hoặc sử dụng MailHog cho testing local
# mail.smtp.host=localhost
# mail.smtp.port=1025
```

### 3. Chạy setup script

```bash
# Chạy script tự động
./scripts/setup-local.sh

# Hoặc setup thủ công
mvn clean compile package -DskipTests
```

### 4. Chạy ứng dụng

#### Cách 1: Sử dụng Tomcat Maven Plugin
```bash
mvn tomcat10:run
```

#### Cách 2: Deploy lên Tomcat
```bash
# Copy WAR file
cp target/GymManagement.war $CATALINA_HOME/webapps/

# Khởi động Tomcat
$CATALINA_HOME/bin/startup.sh
```

#### Cách 3: Sử dụng IDE
1. Import project vào IDE
2. Cấu hình server (Tomcat/GlassFish)
3. Deploy và chạy

## Cấu trúc File mới

```
PRJ_Assignment/
├── config/                    # File cấu hình mới
│   ├── server.xml             # Cấu hình Tomcat
│   └── context.xml            # Cấu hình DataSource
├── scripts/                   # Script tiện ích
│   └── setup-local.sh         # Script thiết lập local
├── pom-dev.xml               # Maven config cho development
└── src/main/resources/
    ├── META-INF/
    │   └── persistence-local.xml  # JPA config cho local
    └── email-local.properties    # Email config cho local
```

## Profiles Maven

### Local Development (default)
```bash
mvn clean install -Plocal
```

### Testing
```bash
mvn clean test -Ptest
```

### Production
```bash
mvn clean install -Pproduction
```

## URL truy cập

- **Ứng dụng**: http://localhost:8080/gym
- **Admin Panel**: http://localhost:8080/gym/admin
- **API Endpoints**: http://localhost:8080/gym/api/*

## Database Schema

Ứng dụng sử dụng JPA với EclipseLink, schema sẽ được tự động tạo khi khởi động.

## Debugging

### Enable SQL Logging
Trong `persistence-local.xml`:
```xml
<property name="eclipselink.logging.level.sql" value="FINE"/>
<property name="eclipselink.logging.parameters" value="true"/>
```

### Java Debug
```bash
mvn tomcat10:run -Dmaven.opts="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

## Troubleshooting

### Lỗi Database Connection
1. Kiểm tra MySQL đã khởi động: `brew services list | grep mysql`
2. Kiểm tra credentials trong `persistence-local.xml`
3. Kiểm tra firewall/port 3306

### Lỗi Compile
1. Kiểm tra Java version: `java -version`
2. Clean project: `mvn clean`
3. Reimport dependencies: `mvn dependency:resolve`

### Lỗi Deployment
1. Kiểm tra Tomcat version (cần 10.1+)
2. Kiểm tra port conflict: `lsof -i :8080`
3. Xem log: `tail -f $CATALINA_HOME/logs/catalina.out`

## Development Tips

1. **Hot Reload**: Sử dụng IDE debugging hoặc JRebel
2. **Database GUI**: MySQL Workbench hoặc phpMyAdmin
3. **Testing API**: Postman hoặc curl
4. **Log Monitoring**: `tail -f logs/application.log`

## Testing

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=UserServiceTest

# Skip tests
mvn package -DskipTests
```

## Production Deployment

Xem file `DEPLOYMENT.md` cho hướng dẫn deploy production.

---

**Lưu ý**: File cấu hình development này tách biệt hoàn toàn với production, không ảnh hưởng đến code hiện tại.
