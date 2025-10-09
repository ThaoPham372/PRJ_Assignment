# Stamina Gym Management System

Hệ thống quản lý phòng gym được phát triển bằng Java JSP/Servlet với giao diện hiện đại và đầy đủ chức năng.

## Tính năng chính

### 1. Authentication & Authorization
- **LoginServlet**: Xử lý đăng nhập với phân quyền theo role
- **RegisterServlet**: Đăng ký thành viên mới
- **LogoutServlet**: Đăng xuất và xóa session
- **SecurityFilter**: Bảo vệ các trang admin/member

### 2. Admin Panel
- **AdminDashboardServlet**: Dashboard tổng quan với thống kê
- **MemberManagementServlet**: Quản lý thành viên (CRUD)
- **CoachManagementServlet**: Quản lý huấn luyện viên
- **EquipmentManagementServlet**: Quản lý thiết bị & bảo trì
- **POSServlet**: Point of Sale - xử lý thanh toán
- **SalesReportServlet**: Báo cáo doanh thu và thống kê

### 3. Database & Models
- **User Model**: Model cho người dùng (admin, manager, employee, member)
- **Coach Model**: Model cho huấn luyện viên
- **UserDAO**: Data Access Object cho User
- **Database Schema**: SQL schema đầy đủ với indexes

### 4. Security & Error Handling
- **SecurityFilter**: Filter bảo vệ theo role
- **ErrorHandlerServlet**: Xử lý lỗi tập trung
- **Error Pages**: 403, 404, 500 với giao diện đẹp

## Cấu trúc dự án

```
src/
├── main/
│   ├── java/
│   │   ├── controller/          # Các servlet xử lý request
│   │   ├── DAO/                # Data Access Objects
│   │   ├── model/              # Model classes
│   │   └── service/            # Business logic services
│   ├── resources/
│   │   ├── database_schema.sql # Database schema
│   │   └── META-INF/
│   └── webapp/
│       ├── css/                # Stylesheets
│       ├── views/              # JSP pages
│       │   ├── admin/          # Admin panel pages
│       │   ├── member/         # Member pages
│       │   ├── error/          # Error pages
│       │   └── common/         # Shared components
│       └── WEB-INF/
└── test/
```

## Cài đặt và chạy

### 1. Yêu cầu hệ thống
- Java 11+
- Apache Tomcat 10+
- SQL Server (hoặc database tương thích)
- Maven 3.6+

### 2. Cấu hình database
1. Tạo database `Dino_Mutant` trong SQL Server
2. Chạy script `src/main/resources/database_schema.sql`
3. Cập nhật thông tin kết nối trong `src/main/java/DAO/DBConnection.java`

### 3. Build và deploy
```bash
# Build project
mvn clean compile

# Package WAR file
mvn clean package

# Deploy WAR file vào Tomcat
cp target/gymProject-1.0-SNAPSHOT.war $TOMCAT_HOME/webapps/
```

### 4. Truy cập ứng dụng
- URL: `http://localhost:8080/gymProject-1.0-SNAPSHOT/`
- Tài khoản demo:
  - Admin: `admin/admin123`
  - Manager: `manager/manager123`
  - Employee: `employee/emp123`

## Các trang chính

### Public Pages
- `/` - Trang chủ
- `/home` - Trang chủ với thông tin gym
- `/login` - Đăng nhập
- `/register` - Đăng ký thành viên

### Admin Pages
- `/admin/dashboard` - Dashboard tổng quan
- `/admin/members` - Quản lý thành viên
- `/admin/coaches` - Quản lý huấn luyện viên
- `/admin/equipment` - Quản lý thiết bị
- `/admin/pos` - Point of Sale
- `/admin/sales-report` - Báo cáo doanh thu

### Security
- Tất cả trang admin được bảo vệ bởi `SecurityFilter`
- Phân quyền theo role: admin > manager > employee > member
- Session management tự động

## Database Schema

### Bảng chính
- `users` - Thông tin người dùng
- `coaches` - Thông tin huấn luyện viên
- `equipment` - Thiết bị phòng gym
- `membership_packages` - Gói membership
- `transactions` - Giao dịch thanh toán
- `training_sessions` - Lịch tập
- `maintenance_records` - Lịch sử bảo trì

### Quan hệ
- User có thể có nhiều transactions
- User có thể có nhiều training sessions
- Coach có thể có nhiều training sessions
- Equipment có thể có nhiều maintenance records

## Tính năng nổi bật

### 1. Giao diện hiện đại
- Bootstrap 5.3
- Font Awesome icons
- Responsive design
- Custom CSS với theme Stamina Gym

### 2. Bảo mật
- Session-based authentication
- Role-based authorization
- SQL injection protection
- XSS protection

### 3. Quản lý đầy đủ
- CRUD operations cho tất cả entities
- Pagination và search
- Export reports
- Real-time statistics

### 4. Point of Sale
- Xử lý thanh toán
- Quản lý khách hàng
- In hóa đơn
- Theo dõi giao dịch

## Phát triển thêm

### Thêm tính năng mới
1. Tạo Model class trong `src/main/java/model/`
2. Tạo DAO class trong `src/main/java/DAO/`
3. Tạo Servlet trong `src/main/java/controller/`
4. Tạo JSP page trong `src/main/webapp/views/`
5. Cập nhật `SecurityFilter` nếu cần

### Cấu hình mới
- Thêm dependency trong `pom.xml`
- Cập nhật `web.xml` nếu cần
- Cập nhật database schema

## Troubleshooting

### Lỗi thường gặp
1. **Database connection failed**: Kiểm tra `DBConnection.java`
2. **404 Not Found**: Kiểm tra URL mapping trong servlet
3. **403 Forbidden**: Kiểm tra role permissions
4. **500 Internal Error**: Kiểm tra logs và database schema

### Logs
- Application logs: `$TOMCAT_HOME/logs/`
- Database logs: SQL Server logs
- Servlet logs: Console output

## Đóng góp

1. Fork project
2. Tạo feature branch
3. Commit changes
4. Push to branch
5. Tạo Pull Request

## License

MIT License - Xem file LICENSE để biết thêm chi tiết.

## Liên hệ

- Email: info@staminagym.vn
- Website: https://staminagym.vn
- Phone: (028) 1234-5678

