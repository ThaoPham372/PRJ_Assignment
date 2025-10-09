# Hướng Dẫn Thiết Lập Database - Stamina Gym Management System

## 📋 Tổng Quan
Hệ thống sử dụng SQL Server để lưu trữ dữ liệu. Dưới đây là hướng dẫn chi tiết để thiết lập database.

## 🚀 Bước 1: Thiết Lập SQL Server

### Yêu Cầu Hệ Thống
- SQL Server 2016 trở lên
- SQL Server Management Studio (SSMS)
- Port 1433 được mở

### Cài Đặt SQL Server Express (Nếu chưa có)
1. Download SQL Server Express từ Microsoft
2. Cài đặt với các tùy chọn mặc định
3. Ghi nhớ password cho tài khoản `sa`

## 🔧 Bước 2: Tạo Database

### Phương Pháp 1: Sử dụng SQL Script (Khuyến nghị)

1. **Mở SQL Server Management Studio**
2. **Kết nối đến SQL Server Instance**
   - Server name: `localhost` hoặc `localhost\SQLEXPRESS`
   - Authentication: SQL Server Authentication
   - Login: `sa`
   - Password: `123456` (hoặc password bạn đã đặt)

3. **Chạy script tạo database**
   ```sql
   -- Mở file: src/main/resources/create_database.sql
   -- Copy toàn bộ nội dung và chạy trong SSMS
   ```

4. **Kiểm tra database đã tạo thành công**
   ```sql
   USE Dino_Mutant;
   SELECT name FROM sys.tables;
   ```

### Phương Pháp 2: Sử dụng Command Line

```cmd
# Kết nối SQL Server
sqlcmd -S localhost\SQLEXPRESS -U sa -P 123456

# Chạy script
sqlcmd -S localhost\SQLEXPRESS -U sa -P 123456 -i "src\main\resources\create_database.sql"
```

## 🧪 Bước 3: Kiểm Tra và Sửa Lỗi Đăng Nhập

### Chạy Script Kiểm Tra
```sql
-- Mở file: src/main/resources/test_connection.sql
-- Chạy để kiểm tra kết nối và dữ liệu
```

### Sửa Lỗi Đăng Nhập (Nếu có)
```sql
-- Mở file: src/main/resources/fix_login_issues.sql
-- Chạy để reset password và trạng thái user
```

## 👤 Tài Khoản Mặc Định

Sau khi chạy script, bạn sẽ có các tài khoản sau:

| Username | Password | Role | Mô Tả |
|----------|----------|------|-------|
| `admin` | `admin123` | Admin | Quản trị viên hệ thống |
| `manager` | `manager123` | Manager | Quản lý phòng gym |
| `employee` | `emp123` | Employee | Nhân viên |
| `customer` | `cust123` | Member | Thành viên |
| `testadmin` | `test123` | Admin | Tài khoản test admin |
| `testmember` | `test123` | Member | Tài khoản test member |

## 🔧 Bước 4: Cấu Hình Java Application

### Kiểm Tra DBConnection.java
Đảm bảo file `src/main/java/DAO/DBConnection.java` có cấu hình đúng:

```java
public static String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=Dino_Mutant;encrypt=true;trustServerCertificate=true;";
public static String userDB = "sa";
public static String passDB = "123456";
```

### Test Kết Nối Java
Chạy method `main()` trong `DBConnection.java` để kiểm tra kết nối:

```java
public static void main(String[] args) {
    try (Connection con = getConnection()) {
        if (con != null) {
            System.out.println("Connect to Dino_Mutant Success");
        }
    } catch (SQLException ex) {
        Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
}
```

## 🚨 Khắc Phục Sự Cố

### Lỗi Kết Nối Database
1. **Kiểm tra SQL Server đang chạy**
   ```cmd
   # Windows Services
   services.msc
   # Tìm "SQL Server (SQLEXPRESS)" và đảm bảo đang "Running"
   ```

2. **Kiểm tra port 1433**
   ```cmd
   netstat -an | findstr 1433
   ```

3. **Kiểm tra firewall**
   - Đảm bảo port 1433 được mở trong Windows Firewall

### Lỗi Authentication
1. **Kiểm tra SQL Server Authentication Mode**
   ```sql
   -- Trong SSMS, right-click server -> Properties -> Security
   -- Chọn "SQL Server and Windows Authentication mode"
   -- Restart SQL Server service
   ```

2. **Reset password sa**
   ```sql
   ALTER LOGIN sa WITH PASSWORD = '123456';
   ALTER LOGIN sa ENABLE;
   ```

### Lỗi Login trong Ứng Dụng
1. **Kiểm tra dữ liệu user**
   ```sql
   USE Dino_Mutant;
   SELECT username, password, status FROM users;
   ```

2. **Reset user status**
   ```sql
   UPDATE users SET status = 'active' WHERE username = 'admin';
   ```

3. **Kiểm tra UserDAO**
   - Đảm bảo method `login()` trong `UserDAO.java` hoạt động đúng
   - Kiểm tra SQL query trong method

## 📊 Cấu Trúc Database

### Các Bảng Chính
- **users**: Thông tin người dùng
- **coaches**: Thông tin huấn luyện viên
- **equipment**: Thiết bị phòng gym
- **membership_packages**: Gói thành viên
- **transactions**: Giao dịch thanh toán
- **payment_schedules**: Lịch thanh toán
- **training_sessions**: Buổi tập luyện
- **maintenance_records**: Bảo trì thiết bị
- **system_settings**: Cài đặt hệ thống

### Quan Hệ Giữa Các Bảng
```
users (1) -----> (N) transactions
users (1) -----> (N) payment_schedules
users (1) -----> (N) training_sessions
coaches (1) ----> (N) training_sessions
equipment (1) --> (N) maintenance_records
membership_packages (1) --> (N) payment_schedules
```

## 🎯 Bước 5: Test Toàn Bộ Hệ Thống

### 1. Test Database Connection
```java
// Chạy DBConnection.main()
```

### 2. Test Login
- Truy cập `http://localhost:8080/gymProject/views/login.jsp`
- Đăng nhập với tài khoản `admin/admin123`

### 3. Test Các Chức Năng
- Admin Dashboard
- Quản lý thành viên
- Quản lý huấn luyện viên
- Quản lý thiết bị
- Point of Sale
- Báo cáo doanh thu

## 📝 Ghi Chú Quan Trọng

1. **Bảo Mật**: Đổi password mặc định trong môi trường production
2. **Backup**: Tạo backup database thường xuyên
3. **Performance**: Các index đã được tạo để tối ưu hiệu suất
4. **Logging**: Hệ thống có logging để debug

## 🆘 Hỗ Trợ

Nếu gặp vấn đề:
1. Kiểm tra log file trong thư mục `logs/`
2. Xem console output khi chạy ứng dụng
3. Kiểm tra SQL Server error log
4. Đảm bảo tất cả dependencies đã được cài đặt

---

**Chúc bạn thành công trong việc thiết lập hệ thống! 🎉**
