# Dashboard PT (Personal Trainer) - GymFit

Dashboard chuyên dụng cho Huấn luyện viên cá nhân (PT) trong hệ thống quản lý phòng gym GymFit.

## 📋 Mục lục

- [Tổng quan](#tổng-quan)
- [Cấu trúc thư mục](#cấu-trúc-thư-mục)
- [Các chức năng chính](#các-chức-năng-chính)
- [Hướng dẫn sử dụng](#hướng-dẫn-sử-dụng)
- [Giao diện](#giao-diện)
- [Tích hợp Backend](#tích-hợp-backend)

---

## 🎯 Tổng quan

Dashboard PT được thiết kế để giúp các huấn luyện viên cá nhân:

- Quản lý học viên được phân công
- Lên lịch và theo dõi các buổi tập
- Chat trực tiếp với học viên
- Theo dõi tiến độ và thống kê hiệu suất

**Phong cách thiết kế:**

- Màu sắc chủ đạo:
  - Xanh đậm: `#141a49`
  - Cam: `#ec8b5a`
- Responsive, hiện đại, dễ sử dụng
- Tương đồng với admin dashboard

---

## 📁 Cấu trúc thư mục

```
views/PT/
├── homePT.jsp              # Trang chủ PT Dashboard
├── profile.jsp             # Quản lý hồ sơ cá nhân
├── training_schedule.jsp   # Quản lý lịch huấn luyện
├── student_management.jsp  # Quản lý học viên
├── chat.jsp                # Chat với học viên
├── reports.jsp             # Thống kê & Báo cáo
└── README.md               # Tài liệu hướng dẫn
```

**Servlet Controller:**

```
src/main/java/com/gym/controller/
└── PTDashboardServlet.java
```

---

## ⚙️ Các chức năng chính

### 1. **Trang chủ (homePT.jsp)**

- Hiển thị logo GymFit và tên PT
- Avatar dropdown menu (click để hiển thị chức năng)
- Quick stats: Số học viên, buổi tập, đánh giá
- Feature cards: Liên kết nhanh đến các chức năng chính

**URL:** `/views/PT/homePT.jsp` hoặc `/pt/home`

### 2. **Hồ sơ cá nhân (profile.jsp)**

- Chỉnh sửa thông tin: Email, SĐT, Avatar
- Đổi mật khẩu
- Xem chứng chỉ & kinh nghiệm
- Thống kê cá nhân

**URL:** `/views/PT/profile.jsp` hoặc `/pt/profile`

### 3. **Lịch huấn luyện (training_schedule.jsp)**

- Xem lịch dạng calendar hoặc danh sách
- Xác nhận/từ chối buổi tập do học viên đặt
- Tạo buổi tập thủ công
- Cập nhật trạng thái: Hoàn thành, Hủy, Dời lịch

**URL:** `/views/PT/training_schedule.jsp` hoặc `/pt/schedule`

### 4. **Quản lý học viên (student_management.jsp)**

- Xem danh sách học viên được phân công
- Tìm kiếm theo tên, ID, gói tập
- Xem hồ sơ chi tiết học viên
- Theo dõi tiến độ: Chỉ số cơ thể, mục tiêu
- Cập nhật ghi chú cho học viên

**URL:** `/views/PT/student_management.jsp` hoặc `/pt/students`

### 5. **Chat (chat.jsp)**

- Chat trực tiếp với học viên
- Danh sách học viên với trạng thái online
- Hiển thị tin nhắn mới (unread badge)
- Gửi tin nhắn, file đính kèm

**URL:** `/views/PT/chat.jsp` hoặc `/pt/chat`

### 6. **Thống kê & Báo cáo (reports.jsp)**

- Tổng số học viên đang phụ trách
- Số buổi tập hoàn thành/hủy
- Đánh giá trung bình từ học viên
- Biểu đồ: Số buổi tập theo tháng, phân bổ loại hình
- Lịch sử buổi tập
- Tiến độ học viên nổi bật

**URL:** `/views/PT/reports.jsp` hoặc `/pt/reports`

---

## 🚀 Hướng dẫn sử dụng

### Cách truy cập

**1. Trực tiếp qua JSP:**

```
http://localhost:8080/gym_management/views/PT/homePT.jsp
```

**2. Qua Servlet (khuyến nghị):**

```
http://localhost:8080/gym_management/pt/home
http://localhost:8080/gym_management/pt/profile
http://localhost:8080/gym_management/pt/schedule
http://localhost:8080/gym_management/pt/students
http://localhost:8080/gym_management/pt/chat
http://localhost:8080/gym_management/pt/reports
```

### Menu Navigation

1. **Avatar Dropdown:**

   - Click vào avatar (góc phải header) để mở menu
   - Chọn chức năng cần truy cập
   - Click bên ngoài để đóng menu

2. **Breadcrumb:**

   - Mỗi trang có breadcrumb để điều hướng
   - Ví dụ: Home / Quản lý học viên

3. **Nút Quay lại:**
   - Mỗi trang con đều có nút "Quay lại" về trang chủ PT

---

## 🎨 Giao diện

### Màu sắc

```css
--primary: #141a49; /* Xanh đậm */
--accent: #ec8b5a; /* Cam */
--success: #28a745; /* Xanh lá */
--warning: #ffc107; /* Vàng */
--danger: #dc3545; /* Đỏ */
```

### Components chính

- **Header:** Logo, tên PT, avatar dropdown
- **Stats Cards:** Hiển thị số liệu quan trọng
- **Feature Cards:** Liên kết đến các chức năng
- **Tables:** Danh sách học viên, buổi tập
- **Calendar:** Lịch huấn luyện
- **Chat Interface:** Giao diện chat real-time
- **Charts:** Biểu đồ thống kê (placeholder)

### Responsive

- Desktop: Hiển thị đầy đủ tính năng
- Tablet: Grid layout thích ứng
- Mobile: Single column, menu collapsed

---

## 🔧 Tích hợp Backend

### Session Management

```java
// Trong PTDashboardServlet.java
HttpSession session = request.getSession();
session.setAttribute("user", ptUser);
```

### JSP sử dụng session

```jsp
${sessionScope.user != null ? sessionScope.user.fullName : 'PT Trainer'}
```

### Dữ liệu cần thiết

**1. User Session:**

```java
- id: int
- fullName: String
- email: String
- phone: String
- role: String (PT)
- avatar: String
```

**2. Students List:**

```java
- studentId: int
- name: String
- email: String
- phone: String
- package: String
- progress: int (%)
- sessionsCount: int
```

**3. Training Sessions:**

```java
- sessionId: int
- date: Date
- time: String
- studentName: String
- type: String (Cardio, Yoga, etc.)
- status: String (confirmed, pending, completed, cancelled)
- rating: int (1-5)
```

**4. Stats:**

```java
- totalStudents: int
- completedSessions: int
- cancelledSessions: int
- averageRating: double
```

### API Endpoints cần phát triển

```java
// Lấy danh sách học viên
GET /api/pt/students

// Lấy lịch tập
GET /api/pt/schedule?month=10&year=2025

// Cập nhật trạng thái buổi tập
POST /api/pt/session/update
{
    "sessionId": 123,
    "status": "completed"
}

// Gửi tin nhắn
POST /api/pt/chat/send
{
    "studentId": 456,
    "message": "Hello"
}

// Lấy thống kê
GET /api/pt/stats?from=2025-09-01&to=2025-10-15
```

---

## 📝 Ghi chú

### Các tính năng đang phát triển

- [ ] Upload avatar
- [ ] Tích hợp biểu đồ thực (Chart.js, ApexCharts)
- [ ] Chat real-time (WebSocket)
- [ ] Notification system
- [ ] Export báo cáo PDF/Excel
- [ ] Video call integration

### Tối ưu hóa cần làm

- [ ] Lazy loading cho images
- [ ] Pagination cho danh sách dài
- [ ] Cache dữ liệu thống kê
- [ ] Optimize SQL queries

---

## 🤝 Đóng góp

Nếu bạn muốn thêm chức năng hoặc cải thiện giao diện, vui lòng:

1. Giữ nguyên phong cách thiết kế hiện tại
2. Đảm bảo responsive
3. Comment code rõ ràng
4. Test trên nhiều trình duyệt

---

## 📞 Liên hệ

Nếu có thắc mắc hoặc cần hỗ trợ:

- Email: support@gymfit.vn
- Phone: 0123-456-789

---

**Version:** 1.0.0  
**Last Updated:** 14/10/2025  
**Author:** GymFit Development Team

