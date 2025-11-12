# TÀI LIỆU TRÌNH BÀY CHỨC NĂNG PT (PERSONAL TRAINER)
## Hệ thống quản lý phòng gym GymFit

---

## MỤC LỤC

1. [Tổng quan hệ thống PT](#1-tổng-quan-hệ-thống-pt)
2. [Cấu trúc thư mục và file](#2-cấu-trúc-thư-mục-và-file)
3. [Luồng hoạt động chính](#3-luồng-hoạt-động-chính)
4. [Chi tiết các chức năng](#4-chi-tiết-các-chức-năng)
   - [4.1. Trang chủ PT (homePT.jsp)](#41-trang-chủ-pt-homeptjsp)
   - [4.2. Quản lý lịch huấn luyện (training_schedule.jsp)](#42-quản-lý-lịch-huấn-luyện-training_schedulejsp)
   - [4.3. Quản lý học viên (student_management.jsp)](#43-quản-lý-học-viên-student_managementjsp)
   - [4.4. Hồ sơ cá nhân (profile.jsp)](#44-hồ-sơ-cá-nhân-profilejsp)
   - [4.5. Chat với học viên (chat.jsp)](#45-chat-với-học-viên-chatjsp)

---

## 1. TỔNG QUAN HỆ THỐNG PT

Hệ thống PT (Personal Trainer) cho phép huấn luyện viên cá nhân:
- Quản lý lịch làm việc và buổi tập
- Quản lý học viên được phân công
- Cập nhật thông tin cá nhân
- Chat với học viên
- Xem thống kê và báo cáo

**Kiến trúc:** JSP (View) → Servlet (Controller) → Service (Business Logic) → DAO (Data Access)

---

## 2. CẤU TRÚC THƯ MỤC VÀ FILE

### 2.1. File JSP (View Layer)
```
src/main/webapp/views/PT/
├── homePT.jsp              # Trang chủ dashboard
├── training_schedule.jsp   # Quản lý lịch huấn luyện
├── student_management.jsp  # Quản lý học viên
├── profile.jsp             # Hồ sơ cá nhân
└── chat.jsp                # Chat với học viên
```

### 2.2. File Servlet (Controller Layer)
```
src/main/java/controller/
├── TrainerDashboardServlet.java    # Điều hướng trang chủ, profile, chat
├── TrainerScheduleServlet.java     # Xử lý lịch huấn luyện
└── TrainerStudentServlet.java      # Xử lý quản lý học viên
```

### 2.3. File Service (Business Logic Layer)
```
src/main/java/service/
├── TrainerDashboardService.java
├── TrainerScheduleService.java
└── TrainerStudentService.java
```

---

## 3. LUỒNG HOẠT ĐỘNG CHÍNH

### 3.1. Luồng tổng quát

```
User (Browser)
    ↓
JSP Page (View)
    ↓ [Click button/link]
HTTP Request (GET/POST)
    ↓
Servlet (Controller)
    ↓ [Validate & Process]
Service (Business Logic)
    ↓ [Query/Update]
DAO (Data Access)
    ↓ [SQL Query]
Database
    ↓ [Result]
DAO → Service → Servlet → JSP → User
```

### 3.2. Xác thực người dùng

**Luồng xác thực:**
1. User truy cập trang PT
2. Servlet kiểm tra session: `session.getAttribute("user")`
3. Nếu chưa đăng nhập → Redirect về `/login`
4. Nếu đã đăng nhập → Kiểm tra role (Trainer)
5. Load thông tin Trainer từ database
6. Set vào request attribute để JSP hiển thị

**Code trong Servlet:**
```java
HttpSession session = req.getSession(false);
if (!isAuthenticated(session)) {
    resp.sendRedirect(req.getContextPath() + "/login");
    return;
}
Trainer currentTrainer = getCurrentTrainer(session);
```

---

## 4. CHI TIẾT CÁC CHỨC NĂNG

### 4.1. TRANG CHỦ PT (homePT.jsp)

#### 4.1.1. Luồng truy cập

**URL:** `/pt/home` hoặc `/pt/dashboard`

**Luồng đi:**
1. User nhấn link hoặc truy cập trực tiếp URL
2. Request → `TrainerDashboardServlet.doGet()`
3. Servlet kiểm tra authentication
4. Servlet gọi `TrainerDashboardService.getQuickStats(trainerId)`
5. Service gọi DAO để lấy thống kê:
   - Tổng số học viên
   - Số buổi tập đã hoàn thành
   - Số buổi tập hôm nay
6. Servlet set attributes vào request:
   ```java
   req.setAttribute("totalStudents", quickStats.get("totalStudents"));
   req.setAttribute("completedSessions", quickStats.get("completedSessions"));
   req.setAttribute("todaySessions", quickStats.get("todaySessions"));
   req.setAttribute("trainer", currentTrainer);
   ```
7. Servlet forward đến JSP: `req.getRequestDispatcher("/views/PT/homePT.jsp").forward(req, resp)`
8. JSP hiển thị dữ liệu từ request attributes

#### 4.1.2. Các nút và điều hướng

**a) Avatar Dropdown Menu:**
- **Vị trí:** Header, góc phải
- **Khi click:** JavaScript toggle class `active` cho menu
- **Các link trong menu:**
  - "Hồ sơ cá nhân" → `/pt/profile`
  - "Lịch huấn luyện" → `/pt/schedule`
  - "Quản lý học viên" → `/pt/students`
  - "Chat với học viên" → `/pt/chat`
  - "Đăng xuất" → `/logout`

**Code JSP:**
```jsp
<div class="pt-avatar" id="ptAvatar" onclick="toggleMenu()">
    <i class="fas fa-dumbbell"></i>
</div>
<div class="pt-menu" id="ptMenu">
    <a href="${pageContext.request.contextPath}/pt/profile">Hồ sơ cá nhân</a>
    <a href="${pageContext.request.contextPath}/pt/schedule">Lịch huấn luyện</a>
    <!-- ... -->
</div>
```

**JavaScript:**
```javascript
ptAvatar.addEventListener('click', function(e) {
    e.stopPropagation();
    ptMenu.classList.toggle('active');
});
```

**b) Feature Cards:**
- **Vị trí:** Phần "Chức năng chính"
- **Khi click card:** `onclick="window.location.href='...'"`
- **Điều hướng:**
  - Card "Hồ sơ cá nhân" → `/pt/profile`
  - Card "Lịch huấn luyện" → `/pt/schedule`
  - Card "Quản lý học viên" → `/pt/students`
  - Card "Chat với học viên" → `/pt/chat`

**c) Hero Actions:**
- Nút "Xem lịch tập" → `/pt/schedule`
- Nút "Quản lý học viên" → `/pt/students`

---

### 4.2. QUẢN LÝ LỊCH HUẤN LUYỆN (training_schedule.jsp)

#### 4.2.1. Luồng truy cập

**URL:** `/pt/schedule`

**Luồng đi:**
1. Request → `TrainerScheduleServlet.doGet()`
2. Servlet lấy trainer từ session
3. Servlet gọi service để lấy dữ liệu:
   ```java
   req.setAttribute("bookings", scheduleService.getTrainerBookings(trainerId));
   req.setAttribute("exceptions", scheduleService.getTrainerExceptions(trainerId));
   req.setAttribute("timeSlots", scheduleService.getActiveTimeSlots());
   req.setAttribute("fixedMap", fixedMap); // Lịch tuần
   ```
4. Servlet forward đến JSP: `/views/PT/training_schedule.jsp`
5. JSP hiển thị:
   - Danh sách buổi tập (bookings)
   - Form đăng ký ngày nghỉ/bận
   - Lịch nghỉ/bận đã đăng ký (exceptions)
   - Lịch tuần (weekly schedule)

#### 4.2.2. Cập nhật trạng thái buổi tập

**Luồng khi PT nhấn nút "Cập nhật":**

1. **JSP Form:**
   ```jsp
   <form action="${pageContext.request.contextPath}/pt/update-booking" method="post">
       <input type="hidden" name="action" value="update" />
       <input type="hidden" name="bookingId" value="${b.bookingId}" />
       <select name="status">
           <option value="CONFIRMED">Xác nhận</option>
           <option value="COMPLETED">Hoàn thành</option>
           <option value="CANCELLED">Hủy</option>
       </select>
       <button type="submit">Cập nhật</button>
   </form>
   ```

2. **POST Request → TrainerScheduleServlet.doPost():**
   ```java
   if ("update".equals(action)) {
       int bookingId = Integer.parseInt(req.getParameter("bookingId"));
       BookingStatus status = BookingStatus.valueOf(req.getParameter("status"));
       scheduleService.updateBookingStatus(bookingId, status);
   }
   ```

3. **Service xử lý:**
   - Gọi DAO để cập nhật trạng thái booking trong database
   - Trả về kết quả

4. **Servlet redirect:**
   ```java
   resp.sendRedirect(req.getContextPath() + "/pt/schedule");
   ```

5. **JSP reload và hiển thị trạng thái mới**

#### 4.2.3. Đăng ký ngày nghỉ/bận

**Luồng khi PT nhấn nút "Đăng ký ngày nghỉ":**

1. **JSP Form:**
   ```jsp
   <form action="${pageContext.request.contextPath}/pt/add-exception" method="post">
       <input type="hidden" name="action" value="exception" />
       <input type="hidden" name="trainerId" value="${sessionScope.user.id}" />
       <input type="date" name="date" required />
       <select name="slotId" required>
           <option value="">-- Chọn slot --</option>
           <c:forEach var="slot" items="${timeSlots}">
               <option value="${slot.slotId}">${slot.slotName}</option>
           </c:forEach>
       </select>
       <select name="type" required>
           <option value="OFF">Nghỉ (OFF)</option>
           <option value="BUSY">Bận (BUSY)</option>
           <option value="SPECIAL">Đặc biệt (SPECIAL)</option>
       </select>
       <input type="text" name="reason" placeholder="Lý do" />
       <button type="submit">Đăng ký ngày nghỉ</button>
   </form>
   ```

2. **POST Request → TrainerScheduleServlet.doPost():**
   ```java
   if ("exception".equals(action)) {
       int trainerId = Integer.parseInt(req.getParameter("trainerId"));
       LocalDate date = LocalDate.parse(req.getParameter("date"));
       int slotId = Integer.parseInt(req.getParameter("slotId"));
       ExceptionType type = ExceptionType.valueOf(req.getParameter("type"));
       String reason = req.getParameter("reason");
       
       scheduleService.addException(trainerId, date, slotId, type, reason);
       
       session.setAttribute("addSuccess", "Đăng ký ngày nghỉ/bận thành công!");
       resp.sendRedirect(req.getContextPath() + "/pt/schedule");
   }
   ```

3. **Service xử lý:**
   - Validate dữ liệu
   - Gọi DAO để insert exception vào database
   - Trả về kết quả

4. **JSP hiển thị thông báo:**
   - JavaScript check session attribute `addSuccess`
   - Hiển thị toast notification
   - Scroll đến phần exceptions section

#### 4.2.4. Xóa ngày nghỉ/bận

**Luồng khi PT nhấn nút "Xóa":**

1. **JSP Button với Modal:**
   ```jsp
   <button onclick="showDeleteModal('${exception.exceptionId}')">
       <i class="fas fa-trash"></i>
   </button>
   ```

2. **JavaScript hiển thị modal xác nhận:**
   ```javascript
   function showDeleteModal(exceptionId) {
       document.getElementById('deleteExceptionId').value = exceptionId;
       document.getElementById('deleteModal').classList.add('show');
   }
   ```

3. **Khi xác nhận, submit form:**
   ```jsp
   <form action="${pageContext.request.contextPath}/pt/delete-exception" method="post">
       <input type="hidden" name="action" value="delete-exception" />
       <input type="hidden" name="exceptionId" id="deleteExceptionId" />
       <button type="submit">Xóa</button>
   </form>
   ```

4. **POST Request → TrainerScheduleServlet.doPost():**
   ```java
   if ("delete-exception".equals(action)) {
       int exceptionId = Integer.parseInt(req.getParameter("exceptionId"));
       scheduleService.deleteException(exceptionId);
       
       session.setAttribute("deleteSuccess", "Xóa ngày nghỉ/bận thành công!");
       resp.sendRedirect(req.getContextPath() + "/pt/schedule");
   }
   ```

5. **JSP reload và hiển thị danh sách mới**

#### 4.2.5. Lọc buổi tập theo trạng thái

**Luồng khi PT nhấn nút lọc:**

1. **JSP Filter Buttons:**
   ```jsp
   <button class="filter-btn" data-status="pending" onclick="filterBookings('pending')">
       <i class="fas fa-clock"></i> Chờ xác nhận
   </button>
   ```

2. **JavaScript xử lý (Client-side):**
   ```javascript
   function filterBookings(status) {
       const cards = document.querySelectorAll('.booking-card');
       cards.forEach((card) => {
           const cardStatus = card.getAttribute('data-status');
           if (status === 'all' || cardStatus === status) {
               card.style.display = 'block';
           } else {
               card.style.display = 'none';
           }
       });
   }
   ```

**Lưu ý:** Lọc này chỉ thực hiện trên client-side, không gọi lại server.

#### 4.2.6. Chuyển đổi view (Grid/List)

**Luồng khi PT nhấn nút "Lưới" hoặc "Danh sách":**

1. **JSP Toggle Buttons:**
   ```jsp
   <button class="view-btn active" onclick="switchBookingView('grid')">
       <i class="fas fa-th"></i> Lưới
   </button>
   <button class="view-btn" onclick="switchBookingView('list')">
       <i class="fas fa-list"></i> Danh sách
   </button>
   ```

2. **JavaScript xử lý:**
   ```javascript
   function switchBookingView(viewType) {
       const bookingsView = document.getElementById('bookingsView');
       if (viewType === 'grid') {
           bookingsView.className = 'bookings-grid';
       } else {
           bookingsView.className = 'bookings-list';
       }
   }
   ```

**Lưu ý:** Chỉ thay đổi CSS class, không reload dữ liệu.

---

### 4.3. QUẢN LÝ HỌC VIÊN (student_management.jsp)

#### 4.3.1. Luồng truy cập

**URL:** `/pt/students`

**Luồng đi:**
1. Request → `TrainerStudentServlet.doGet()`
2. Servlet lấy trainer từ session
3. Servlet lấy parameters (keyword, package filter)
4. Servlet gọi service:
   ```java
   List<Object[]> students;
   if (keyword != null || packageFilter != null) {
       students = studentService.getTrainerStudentsWithFilter(trainerId, keyword, packageFilter);
   } else {
       students = studentService.getTrainerStudents(trainerId);
   }
   ```
5. Servlet set attributes:
   ```java
   req.setAttribute("students", students);
   req.setAttribute("totalStudents", totalStudents);
   req.setAttribute("activeStudents", activeStudents);
   ```
6. Servlet forward đến JSP: `/views/PT/student_management.jsp`
7. JSP hiển thị danh sách học viên

#### 4.3.2. Tìm kiếm học viên

**Luồng khi PT nhấn nút "Tìm kiếm":**

1. **JSP Form:**
   ```jsp
   <form action="${pageContext.request.contextPath}/pt/students/search" method="get">
       <input type="text" name="keyword" placeholder="Nhập tên, ID hoặc số điện thoại..." />
       <select name="package">
           <option value="">Tất cả</option>
           <c:forEach var="pkg" items="${packages}">
               <option value="${pkg.name}">${pkg.name}</option>
           </c:forEach>
       </select>
       <button type="submit">Tìm kiếm</button>
   </form>
   ```

2. **GET Request → TrainerStudentServlet.doGet():**
   ```java
   String keyword = req.getParameter("keyword");
   String packageFilter = req.getParameter("package");
   
   if (keyword != null || packageFilter != null) {
       students = studentService.getTrainerStudentsWithFilter(trainerId, keyword, packageFilter);
   }
   ```

3. **Service gọi DAO với điều kiện WHERE**
4. **Servlet forward lại JSP với kết quả tìm kiếm**

#### 4.3.3. Xem chi tiết học viên

**Luồng khi PT nhấn nút "Chi tiết":**

1. **JSP Button:**
   ```jsp
   <button onclick="openStudentDetail(${memberId})">
       <i class="fas fa-eye"></i> Chi tiết
   </button>
   ```

2. **JavaScript gọi API:**
   ```javascript
   function openStudentDetail(memberId) {
       fetch(CTX + '/pt/students/detail?id=' + memberId)
           .then(r => r.json())
           .then(data => {
               updateModalContent(data);
               document.getElementById('studentModal').classList.add('active');
           });
   }
   ```

3. **GET Request → TrainerStudentServlet.doGet():**
   ```java
   if ("/pt/students/detail".equals(path)) {
       int memberId = Integer.parseInt(req.getParameter("id"));
       Object[] detail = studentService.getStudentDetail(memberId, trainerId);
       
       // Trả về JSON
       resp.setContentType("application/json;charset=UTF-8");
       String json = "{\"success\":true,\"memberId\":...}";
       resp.getWriter().write(json);
   }
   ```

4. **JavaScript hiển thị modal với dữ liệu JSON**

#### 4.3.4. Chỉnh sửa học viên

**Luồng khi PT nhấn nút "Chỉnh sửa":**

1. **JSP Button:**
   ```jsp
   <button onclick="openStudentEdit(${memberId})">
       <i class="fas fa-edit"></i> Chỉnh sửa
   </button>
   ```

2. **JavaScript mở modal chỉnh sửa:**
   ```javascript
   function openStudentEdit(memberId) {
       fetchStudentData(memberId)
           .then(() => {
               // Fill form với dữ liệu hiện tại
               document.getElementById('edit-weight').value = currentStudentData.weight;
               document.getElementById('edit-height').value = currentStudentData.height;
               document.getElementById('edit-bmi').value = currentStudentData.bmi;
               document.getElementById('edit-goal').value = currentStudentData.goal;
               document.getElementById('edit-ptnote').value = currentStudentData.ptNote;
               document.getElementById('studentEditModal').classList.add('active');
           });
   }
   ```

3. **Khi PT nhấn "Lưu thay đổi":**
   ```javascript
   function saveStudentEdit() {
       const params = new URLSearchParams();
       params.append('memberId', currentStudentData.memberId);
       params.append('weight', document.getElementById('edit-weight').value);
       params.append('height', document.getElementById('edit-height').value);
       params.append('bmi', document.getElementById('edit-bmi').value);
       params.append('goal', document.getElementById('edit-goal').value);
       params.append('ptNote', document.getElementById('edit-ptnote').value);
       
       fetch(CTX + '/pt/students/update', {
           method: 'POST',
           headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
           body: params.toString()
       })
       .then(r => r.json())
       .then(res => {
           if (res.success) {
               // Cập nhật UI
               updateCardUI();
               showToast('Lưu thay đổi thành công');
           }
       });
   }
   ```

4. **POST Request → TrainerStudentServlet.doPost():**
   ```java
   if ("/pt/students/update".equals(path)) {
       int memberId = Integer.parseInt(req.getParameter("memberId"));
       String weightStr = req.getParameter("weight");
       String heightStr = req.getParameter("height");
       // ... các tham số khác
       
       var member = memberService.getById(memberId);
       member.setWeight(Float.parseFloat(weightStr));
       member.setHeight(Float.parseFloat(heightStr));
       // ... cập nhật các trường khác
       
       memberService.update(member);
       
       resp.setContentType("application/json;charset=UTF-8");
       resp.getWriter().write("{\"success\":true}");
   }
   ```

5. **Service gọi DAO để update database**
6. **JavaScript cập nhật UI mà không reload trang**

#### 4.3.5. Chuyển đổi view (Cards/List)

**Luồng khi PT nhấn nút "Thẻ" hoặc "Danh sách":**

1. **JSP Toggle Buttons:**
   ```jsp
   <button onclick="switchView('cards')">
       <i class="fas fa-grip"></i> Thẻ
   </button>
   <button onclick="switchView('list')">
       <i class="fas fa-list"></i> Danh sách
   </button>
   ```

2. **JavaScript xử lý:**
   ```javascript
   function switchView(mode) {
       const cards = document.getElementById('cards-view');
       const list = document.getElementById('list-view');
       if (mode === 'list') {
           cards.style.display = 'none';
           list.style.display = 'block';
           localStorage.setItem('ptStudentsView', 'list');
       } else {
           list.style.display = 'none';
           cards.style.display = 'grid';
           localStorage.setItem('ptStudentsView', 'cards');
       }
   }
   ```

**Lưu ý:** Lưu preference vào localStorage để giữ khi reload trang.

---

### 4.4. HỒ SƠ CÁ NHÂN (profile.jsp)

#### 4.4.1. Luồng truy cập

**URL:** `/pt/profile`

**Luồng đi:**
1. Request → `TrainerDashboardServlet.doGet()`
2. Servlet lấy trainer từ session và reload từ DB
3. Servlet set attribute: `req.setAttribute("trainer", currentTrainer)`
4. Servlet forward đến JSP: `/views/PT/profile.jsp`
5. JSP hiển thị form với dữ liệu trainer

#### 4.4.2. Cập nhật thông tin cá nhân

**Luồng khi PT nhấn nút "Cập nhật thông tin":**

1. **JSP Form:**
   ```jsp
   <form id="profileForm" action="${pageContext.request.contextPath}/pt/update-profile" method="POST">
       <input type="text" name="fullName" value="${trainer.name}" />
       <input type="email" name="email" value="${trainer.email}" />
       <input type="tel" name="phoneNumber" value="${trainer.phone}" />
       <!-- ... các trường khác -->
       <button type="submit">Cập nhật thông tin</button>
   </form>
   ```

2. **POST Request → TrainerDashboardServlet.doPost():**
   ```java
   if ("/pt/update-profile".equals(path)) {
       updateTrainerProfile(req, resp, currentTrainer);
   }
   
   private void updateTrainerProfile(...) {
       // Cập nhật các trường User
       updateUserFields(trainer, req);
       // Cập nhật các trường Trainer
       updateTrainerFields(trainer, req);
       // Lưu vào database
       int result = trainerService.update(trainer);
       if (result > 0) {
           Trainer updatedTrainer = trainerService.getTrainerById(trainer.getId());
           session.setAttribute("user", updatedTrainer);
           session.setAttribute("successMessage", "Cập nhật thông tin thành công!");
           resp.sendRedirect(req.getContextPath() + "/pt/profile");
       }
   }
   ```

3. **Service gọi DAO để update database**
4. **Servlet redirect về `/pt/profile` với success message**
5. **JSP hiển thị thông báo thành công**

#### 4.4.3. Đổi mật khẩu

**Luồng khi PT nhấn nút "Đổi mật khẩu":**

1. **JSP Button:**
   ```jsp
   <button onclick="openChangePasswordModal()">
       <i class="fas fa-key"></i> Đổi mật khẩu
   </button>
   ```

2. **JavaScript hiển thị modal xác nhận:**
   ```javascript
   function openChangePasswordModal() {
       document.getElementById('changePasswordModal').classList.add('show');
   }
   ```

3. **Khi xác nhận:**
   ```javascript
   function confirmChangePassword() {
       const form = document.createElement('form');
       form.method = 'POST';
       form.action = '/pt/change-password';
       const actionInput = document.createElement('input');
       actionInput.type = 'hidden';
       actionInput.name = 'action';
       actionInput.value = 'changePassword';
       form.appendChild(actionInput);
       document.body.appendChild(form);
       form.submit();
   }
   ```

4. **POST Request → TrainerDashboardServlet.doPost():**
   ```java
   if ("changePassword".equals(action)) {
       handleChangePassword(req, resp, currentTrainer);
   }
   
   private void handleChangePassword(...) {
       String email = trainer.getEmail();
       // Kiểm tra rate limiting
       boolean hasPending = passwordService.hasPendingResetRequest(email);
       if (hasPending) {
           session.setAttribute("passwordErrorMessage", "Bạn đã yêu cầu đổi mật khẩu gần đây...");
           resp.sendRedirect(req.getContextPath() + "/pt/profile");
           return;
       }
       // Tạo password reset token và gửi email
       String verificationCode = passwordService.requestPasswordReset(email);
       session.setAttribute("resetEmail", email);
       session.setAttribute("successMessage", "Mã xác nhận đã được gửi đến email: " + email);
       resp.sendRedirect(req.getContextPath() + "/auth/reset-password");
   }
   ```

5. **Service tạo token và gửi email**
6. **Redirect đến trang reset password**

---

### 4.5. CHAT VỚI HỌC VIÊN (chat.jsp)

#### 4.5.1. Luồng truy cập

**URL:** `/pt/chat`

**Luồng đi:**
1. Request → `TrainerDashboardServlet.doGet()`
2. Servlet forward đến JSP: `/views/PT/chat.jsp`
3. JSP hiển thị giao diện chat (hiện tại là static, chưa tích hợp backend)

#### 4.5.2. Gửi tin nhắn (Frontend only - chưa tích hợp backend)

**Luồng khi PT nhấn nút "Gửi":**

1. **JSP Textarea và Button:**
   ```jsp
   <textarea id="messageInput" placeholder="Nhập tin nhắn..."></textarea>
   <button class="send-btn" onclick="sendMessage()">
       <i class="fas fa-paper-plane"></i>
   </button>
   ```

2. **JavaScript xử lý:**
   ```javascript
   function sendMessage() {
       const input = document.getElementById('messageInput');
       const message = input.value.trim();
       if (message === '') return;
       
       // Tạo HTML message
       const messageHTML = `
           <div class="message sent">
               <div class="message-avatar">
                   <i class="fas fa-dumbbell"></i>
               </div>
               <div class="message-content">
                   <div class="message-bubble">
                       <p class="message-text">${message}</p>
                   </div>
                   <div class="message-time">${getCurrentTime()}</div>
               </div>
           </div>
       `;
       
       // Thêm vào DOM
       chatMessages.insertAdjacentHTML('beforeend', messageHTML);
       input.value = '';
       chatMessages.scrollTop = chatMessages.scrollHeight;
   }
   ```

**Lưu ý:** Hiện tại chức năng chat chỉ là frontend demo, chưa có tích hợp backend để lưu và load tin nhắn thực tế.

---

## 5. TỔNG KẾT LUỒNG ĐI CỦA CODE

### 5.1. Luồng GET Request (Hiển thị trang)

```
Browser → JSP Link/URL
    ↓
HTTP GET Request
    ↓
Servlet.doGet()
    ↓ [Kiểm tra authentication]
    ↓ [Lấy trainer từ session]
    ↓ [Gọi Service]
Service.method()
    ↓ [Gọi DAO]
DAO.query()
    ↓ [SQL Query]
Database
    ↓ [ResultSet]
DAO → Service → Servlet
    ↓ [Set request attributes]
Servlet.forward("/views/PT/xxx.jsp")
    ↓
JSP render HTML
    ↓ [Hiển thị dữ liệu từ request attributes]
Browser hiển thị trang
```

### 5.2. Luồng POST Request (Xử lý form)

```
Browser → JSP Form Submit
    ↓
HTTP POST Request (với parameters)
    ↓
Servlet.doPost()
    ↓ [Kiểm tra authentication]
    ↓ [Lấy parameters từ request]
    ↓ [Validate dữ liệu]
    ↓ [Gọi Service]
Service.method()
    ↓ [Business logic]
    ↓ [Gọi DAO]
DAO.insert/update/delete()
    ↓ [SQL Query]
Database
    ↓ [Result]
DAO → Service → Servlet
    ↓ [Set session message]
Servlet.sendRedirect("/pt/xxx")
    ↓
Browser redirect
    ↓
Servlet.doGet() (reload trang)
    ↓
JSP hiển thị với message
```

### 5.3. Luồng AJAX Request (Không reload trang)

```
Browser → JavaScript fetch()
    ↓
HTTP GET/POST Request (AJAX)
    ↓
Servlet.doGet()/doPost()
    ↓ [Xử lý]
    ↓ [Trả về JSON]
resp.setContentType("application/json")
resp.getWriter().write(json)
    ↓
JavaScript nhận JSON response
    ↓ [Cập nhật DOM]
Browser hiển thị thay đổi (không reload)
```

---

## 6. CÁC ĐIỂM QUAN TRỌNG CẦN LƯU Ý

### 6.1. Session Management
- Mọi request đều kiểm tra session trước
- Trainer object được lưu trong session: `session.getAttribute("user")`
- Session được cập nhật sau mỗi lần update thông tin

### 6.2. URL Mapping
- URL pattern được định nghĩa trong `@WebServlet` annotation
- Mỗi servlet có thể xử lý nhiều URL patterns
- Routing dựa trên `req.getServletPath()` hoặc `req.getRequestURI()`

### 6.3. Error Handling
- Try-catch trong servlet để bắt exception
- Set error message vào session
- Redirect về trang với error message
- JSP hiển thị error message từ session

### 6.4. Data Flow
- **Request → Servlet:** `req.getParameter()`, `req.getAttribute()`
- **Servlet → JSP:** `req.setAttribute()`, `req.getRequestDispatcher().forward()`
- **JSP → User:** EL expressions `${attributeName}`, JSTL tags `<c:forEach>`

### 6.5. JavaScript Integration
- JavaScript xử lý UI interactions (toggle, filter, modal)
- AJAX calls để load dữ liệu không đồng bộ
- Form validation trên client-side
- Dynamic DOM manipulation

---

## 7. KẾT LUẬN

Hệ thống PT được xây dựng theo mô hình MVC:
- **Model:** Database, Entity classes
- **View:** JSP files
- **Controller:** Servlet classes

Luồng hoạt động rõ ràng:
1. User tương tác với JSP
2. Request được gửi đến Servlet
3. Servlet xử lý và gọi Service
4. Service gọi DAO để truy vấn database
5. Kết quả được trả về và hiển thị trên JSP

Mỗi chức năng đều có luồng xử lý riêng, từ GET request để hiển thị trang đến POST request để xử lý dữ liệu, và AJAX request để cập nhật không đồng bộ.

---

**Tài liệu này mô tả chi tiết luồng hoạt động của hệ thống PT, giúp giáo viên hiểu rõ cách code hoạt động từ khi người dùng nhấn nút đến khi kết quả được hiển thị.**

