# 🔧 HƯỚNG DẪN DEBUG VÀ KIỂM TRA CHỨC NĂNG PT PROFILE

## 📋 Tổng quan

Đã cải thiện các servlet để có debug logging chi tiết và xử lý lỗi tốt hơn.

## 🔍 Debug Logging đã thêm

### 1. UpdatePTProfileServlet

- ✅ Log userId từ session
- ✅ Log tất cả form parameters
- ✅ Log kết quả tìm kiếm profile existing
- ✅ Log kết quả update/create
- ✅ Log chi tiết lỗi nếu có

### 2. PTProfileDAO

- ✅ Log userId và dữ liệu profile trước khi update
- ✅ Log số dòng bị ảnh hưởng bởi UPDATE query
- ✅ Log warning nếu không có dòng nào được update
- ✅ Log chi tiết SQL error (message, state, error code)

### 3. ChangePasswordServlet

- ✅ Log userId từ session
- ✅ Log các tham số password (không log giá trị thực)
- ✅ Log kết quả verify password hiện tại
- ✅ Log kết quả hash password mới
- ✅ Log số dòng bị ảnh hưởng bởi UPDATE password
- ✅ Log chi tiết lỗi nếu có

## 🚀 Cách test và debug

### Bước 1: Chạy ứng dụng với debug logging

```bash
./mvnw.cmd tomcat7:run "-Dmaven.tomcat.port=9091"
```

### Bước 2: Kiểm tra console logs

Khi test chức năng, quan sát console để thấy các debug messages:

#### Khi cập nhật profile:

```
DEBUG: Processing PT profile update for user_id = 123
DEBUG: Form parameters received:
  - fullName: John Doe
  - email: john@example.com
  - phoneNumber: 0123456789
  ...
DEBUG: Existing profile found: true
DEBUG: Updating existing profile with ID: 456
DEBUG: Updating PT profile for user_id = 123
DEBUG: Profile data - FullName: John Doe, Email: john@example.com, Phone: 0123456789
DEBUG: UPDATE query affected 1 rows
DEBUG: Update result: true
DEBUG: Profile update successful
```

#### Khi đổi mật khẩu:

```
DEBUG: Processing password change for user_id = 123
DEBUG: Password change parameters received:
  - currentPassword: [PROVIDED]
  - newPassword: [PROVIDED]
  - confirmPassword: [PROVIDED]
DEBUG: User found - ID: 123, Username: johnpt
DEBUG: Current password verification result: true
DEBUG: New password hashed successfully
DEBUG: Password UPDATE query affected 1 rows
DEBUG: Password update result: true
DEBUG: Password change successful for user_id: 123
```

### Bước 3: Kiểm tra database

Chạy script test:

```sql
mysql -u root -p gym_management < test_pt_profile_and_password.sql
```

### Bước 4: Test thực tế

#### Test cập nhật profile:

1. Đăng nhập với tài khoản PT
2. Truy cập: `http://localhost:9091/pt/profile`
3. Điền form và click "Cập nhật thông tin"
4. Kiểm tra console logs và database

#### Test đổi mật khẩu:

1. Từ trang profile, điền form đổi mật khẩu
2. Click "Đổi mật khẩu"
3. Kiểm tra console logs
4. Thử đăng nhập lại với mật khẩu mới

## ⚠️ Troubleshooting

### Lỗi "No rows were updated"

```
WARNING: No rows were updated for user_id = 123
This might mean the PT profile doesn't exist in the database
```

**Giải pháp:** Kiểm tra xem profile có tồn tại trong database không:

```sql
SELECT * FROM pt_profiles WHERE user_id = 123;
```

### Lỗi "userId not found in session"

```
ERROR: userId not found in session
```

**Giải pháp:** Kiểm tra LoginServlet có set userId vào session không

### Lỗi "Current password verification result: false"

```
DEBUG: Current password verification result: false
```

**Giải pháp:** Kiểm tra mật khẩu hiện tại có đúng không, hoặc kiểm tra PasswordService

### Lỗi SQL

```
ERROR: Failed to update PT profile for user_id = 123
SQL Error: [error message]
SQL State: [state]
Error Code: [code]
```

**Giải pháp:** Kiểm tra cấu trúc database và kết nối

## 📊 Database Queries để kiểm tra

### Kiểm tra PT profiles:

```sql
SELECT p.*, u.username, u.email as user_email
FROM pt_profiles p
JOIN users u ON p.user_id = u.id;
```

### Kiểm tra users với roles:

```sql
SELECT u.id, u.username, u.email, GROUP_CONCAT(r.role_name) as roles
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON ur.role_id = r.id
GROUP BY u.id, u.username, u.email;
```

### Kiểm tra password changes:

```sql
SELECT id, username, password_hash, salt, updated_date
FROM users
WHERE updated_date > DATE_SUB(NOW(), INTERVAL 1 HOUR);
```

## 🎯 Kết quả mong đợi

### Thành công cập nhật profile:

- Console: `DEBUG: Profile update successful`
- Database: 1 row updated trong pt_profiles
- UI: Thông báo "Cập nhật thông tin thành công!"

### Thành công đổi mật khẩu:

- Console: `DEBUG: Password change successful`
- Database: 1 row updated trong users với password_hash và salt mới
- UI: Thông báo "Đổi mật khẩu thành công!"
- Có thể đăng nhập lại với mật khẩu mới

