# Admin Dashboard - GymFit Management System

## Giới thiệu

Đây là giao diện quản lý admin cho hệ thống quản lý phòng gym GymFit. Giao diện được thiết kế dựa trên mockup đã cung cấp với đầy đủ các chức năng quản lý.

## Cấu trúc thư mục

```
views/admin/
├── dashboard.jsp              # Trang chính admin với overview
├── profile.jsp                # Quản lý profile admin
├── account_management.jsp     # Quản lý tài khoản & phân quyền (CRUD)
├── member_management.jsp      # Quản lý hội viên
├── trainer_management.jsp     # Quản lý huấn luyện viên (PT)
├── service_schedule.jsp       # Quản lý dịch vụ & lịch tập
├── order_management.jsp       # Quản lý đơn hàng & sản phẩm
├── payment_finance.jsp        # Quản lý thanh toán & tài chính
├── reports.jsp                # Báo cáo & thống kê
└── README.md                  # File này
```

## Servlet

- **AdminDashboardServlet.java** - Servlet để điều hướng các trang admin
- Đường dẫn: `src/main/java/com/gym/controller/AdminDashboardServlet.java`

## URL Mapping

Các URL để truy cập admin dashboard:

- Dashboard: `/admin/dashboard`
- Profile: `/admin/profile`
- Quản lý tài khoản: `/admin/account-management`
- Quản lý hội viên: `/admin/member-management`
- Dịch vụ & lịch tập: `/admin/service-schedule`
- Quản lý PT: `/admin/trainer-management`
- Quản lý đơn hàng: `/admin/order-management`
- Thanh toán & tài chính: `/admin/payment-finance`
- Báo cáo & thống kê: `/admin/reports`

## Chức năng chính

### 1. Dashboard (dashboard.jsp)

- Hiển thị thống kê tổng quan
- Số lượng hội viên, doanh thu, check-in, lớp training
- Cards cho từng chức năng quản lý với liên kết nhanh

### 2. Profile Admin (profile.jsp)

- Chỉnh sửa thông tin cá nhân (tên, email, SĐT, địa chỉ)
- Đổi mật khẩu

### 3. Quản lý tài khoản (account_management.jsp)

- Hiển thị danh sách tài khoản (Admin, User, PT)
- CRUD tài khoản: Thêm, Sửa, Xóa
- Set quyền cho tài khoản
- Filter theo vai trò và trạng thái

### 4. Quản lý hội viên (member_management.jsp)

- Xem danh sách hội viên
- Thêm/sửa hội viên
- Quản lý gói tập và hạn sử dụng thẻ
- Thống kê số lượng hội viên (tổng, hoạt động, sắp hết hạn, hết hạn)
- Gia hạn thẻ hội viên

### 5. Quản lý huấn luyện viên (trainer_management.jsp)

- Xem danh sách PT với thông tin chi tiết
- Hiển thị số học viên, số lớp/tuần, rating
- Xem lịch làm việc của từng PT
- Sửa thông tin PT

### 6. Dịch vụ & Lịch tập (service_schedule.jsp)

- **Tab Booking**: Quản lý booking của hội viên
- **Tab Lớp Training**: Quản lý các lớp training, phân công PT
- **Tab Lịch tập**: Xem lịch tập theo dạng calendar
- Tạo booking mới, tạo lớp mới

### 7. Quản lý đơn hàng (order_management.jsp)

- **Tab Sản phẩm**: Quản lý sản phẩm (thêm/xóa/sửa)
- **Tab Đơn hàng**: Xem danh sách đơn hàng
- Quản lý tồn kho
- Thống kê doanh thu sản phẩm

### 8. Thanh toán & Tài chính (payment_finance.jsp)

- Thống kê doanh thu (hôm nay, tháng này, năm nay)
- Biểu đồ doanh thu theo thời gian
- Quản lý hóa đơn
- Phương thức thanh toán (thẻ, tiền mặt, ví điện tử, chuyển khoản)

### 9. Báo cáo & Thống kê (reports.jsp)

- Số lượng hội viên theo thời gian
- Doanh thu theo dịch vụ/gói tập
- Tần suất check-in
- Biểu đồ doanh thu và chi phí
- Thống kê lớp training & PT sessions
- Xuất báo cáo

## Thiết kế UI

### Màu sắc chủ đạo

- **Primary**: `#141a49` (Xanh đậm - Navy)
- **Accent**: `#ec8b5a` (Cam)
- **Text**: `#2c3e50`
- **Background**: `#f6f6f8`

### Layout

- Sidebar cố định bên trái (280px)
- Top bar sticky với tiêu đề trang và actions
- Content area responsive
- Cards với shadow và hover effects

### Responsive

- Desktop: Sidebar đầy đủ (280px)
- Tablet/Mobile: Sidebar thu gọn (70px), chỉ hiển thị icon

## Các tính năng bổ sung

### Modal/Popup

- Thêm/sửa tài khoản
- Thêm/sửa hội viên
- Set quyền
- Các form nhập liệu khác

### Filter & Search

- Tìm kiếm theo tên, email
- Filter theo vai trò, trạng thái, gói tập
- Filter theo khoảng thời gian

### Interactive Elements

- Tab switching (không cần reload trang)
- Active menu highlighting
- Hover effects trên cards và buttons
- Modal open/close animations

## Lưu ý khi phát triển

### Backend Integration

1. Hiện tại chỉ là front-end, chưa kết nối database
2. Cần implement authentication & authorization
3. Cần tạo API endpoints cho CRUD operations
4. Cần implement session management

### Bảo mật

- Kiểm tra quyền truy cập trước khi hiển thị trang admin
- Validate input từ form
- Implement CSRF protection
- Secure session handling

### Performance

- Có thể tích hợp thư viện chart (Chart.js, ApexCharts) cho biểu đồ
- Lazy loading cho data table lớn
- Pagination cho danh sách dài
- Cache dữ liệu thống kê

## Testing

### Để test giao diện:

1. Build project:

```bash
mvn clean package
```

2. Deploy lên Tomcat

3. Truy cập:

```
http://localhost:8080/gym-management/admin/dashboard
```

hoặc truy cập trực tiếp JSP:

```
http://localhost:8080/gym-management/views/admin/dashboard.jsp
```

## Công nghệ sử dụng

- **JSP** (JavaServer Pages)
- **JSTL** (JSP Standard Tag Library)
- **CSS3** (Inline styles)
- **JavaScript** (Vanilla JS cho interactions)
- **Font Awesome 6.5.0** (Icons)
- **Google Fonts - Inter** (Typography)

## Tương lai

### Cần bổ sung:

1. Backend logic với Servlet & DAO pattern
2. Database connection (Hibernate/JPA)
3. Authentication & Authorization
4. Chart library integration
5. Export functionality (PDF, Excel)
6. Real-time updates (WebSocket)
7. Email notifications
8. File upload (avatar, images)

## Liên hệ & Support

Để biết thêm chi tiết về project, xem file:

- `text/plan_working.txt`
- `text/chi_tiet_chuc_nang.txt`
- `text/PLAN_THUC_HIEN_CHI_TIET_FITZ_GYM.txt`

---

**Version**: 1.0  
**Last Updated**: February 2025  
**Author**: GymFit Development Team


