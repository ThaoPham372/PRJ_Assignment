<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý học viên - PT</title>
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
      rel="stylesheet"
    />
    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap"
      rel="stylesheet"
    />

    <style>
      :root {
        --primary: #141a49;
        --accent: #ec8b5a;
        --success: #28a745;
        --text: #2c3e50;
        --text-light: #5a6c7d;
        --card: #ffffff;
        --shadow: rgba(0, 0, 0, 0.1);
        --gradient-primary: linear-gradient(135deg, #141a49 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5a 0%, #d67a4f 100%);
      }

      * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
      }

      body {
        font-family: 'Inter', sans-serif;
        background: #f9f9f9;
        color: var(--text);
      }

      .container {
        max-width: 1400px;
        margin: 0 auto;
        padding: 40px 20px;
      }

      .page-header {
        background: var(--gradient-primary);
        color: #fff;
        padding: 30px 40px;
        border-radius: 15px;
        margin-bottom: 40px;
        box-shadow: 0 8px 30px var(--shadow);
      }

      .page-header h1 {
        font-size: 2.5rem;
        margin-bottom: 10px;
      }

      .breadcrumb {
        display: flex;
        gap: 10px;
        font-size: 0.9rem;
        opacity: 0.9;
      }

      .breadcrumb a {
        color: #fff;
        text-decoration: none;
      }

      .breadcrumb a:hover {
        color: var(--accent);
      }

      .btn {
        background: var(--gradient-accent);
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 12px 24px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        text-decoration: none;
      }

      .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 90, 0.4);
      }

      .btn-back {
        background: #6c757d;
        margin-bottom: 20px;
      }

      .btn-sm {
        padding: 8px 16px;
        font-size: 0.85rem;
      }

      .btn-info {
        background: #17a2b8;
      }

      .search-filter {
        background: var(--card);
        border-radius: 15px;
        padding: 25px;
        margin-bottom: 30px;
        box-shadow: 0 4px 20px var(--shadow);
      }

      .search-bar {
        display: grid;
        grid-template-columns: 1fr auto auto;
        gap: 15px;
        align-items: end;
      }

      .form-group {
        display: flex;
        flex-direction: column;
        gap: 8px;
      }

      .form-group label {
        font-weight: 600;
        color: var(--text);
        font-size: 0.9rem;
      }

      .form-group input,
      .form-group select {
        padding: 12px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 1rem;
        transition: all 0.3s;
      }

      .form-group input:focus,
      .form-group select:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 3px rgba(236, 139, 90, 0.1);
      }

      .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 20px;
        margin-bottom: 30px;
      }

      .stat-card {
        background: var(--card);
        border-radius: 15px;
        padding: 25px;
        box-shadow: 0 4px 20px var(--shadow);
        display: flex;
        align-items: center;
        gap: 20px;
        transition: all 0.3s;
      }

      .stat-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 30px var(--shadow);
      }

      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 15px;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.8rem;
        color: #fff;
      }

      .stat-content h3 {
        font-size: 2rem;
        color: var(--primary);
        margin-bottom: 5px;
      }

      .stat-content p {
        color: var(--text-light);
        font-size: 0.9rem;
      }

      .students-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
        gap: 25px;
      }

      .student-card {
        background: var(--card);
        border-radius: 15px;
        padding: 25px;
        box-shadow: 0 4px 20px var(--shadow);
        transition: all 0.3s;
        cursor: pointer;
      }

      .student-card:hover {
        transform: translateY(-8px);
        box-shadow: 0 12px 40px var(--shadow);
      }

      .student-header {
        display: flex;
        align-items: center;
        gap: 15px;
        margin-bottom: 20px;
        padding-bottom: 15px;
        border-bottom: 2px solid #f0f0f0;
      }

      .student-avatar {
        width: 70px;
        height: 70px;
        border-radius: 50%;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 2rem;
        color: #fff;
        flex-shrink: 0;
      }

      .student-info h3 {
        font-size: 1.3rem;
        color: var(--primary);
        margin-bottom: 5px;
      }

      .student-info .package {
        color: var(--accent);
        font-size: 0.9rem;
        font-weight: 600;
      }

      .student-details {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 15px;
        margin-bottom: 20px;
      }

      .detail-item {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 0.9rem;
        color: var(--text-light);
      }

      .detail-item i {
        color: var(--accent);
        width: 20px;
      }

      .progress-section {
        margin-bottom: 15px;
      }

      .progress-label {
        display: flex;
        justify-content: space-between;
        margin-bottom: 8px;
        font-size: 0.85rem;
        color: var(--text-light);
      }

      .progress-bar {
        height: 8px;
        background: #e9ecef;
        border-radius: 10px;
        overflow: hidden;
      }

      .progress-fill {
        height: 100%;
        background: var(--gradient-accent);
        border-radius: 10px;
        transition: width 0.3s;
      }

      .student-actions {
        display: flex;
        gap: 10px;
      }

      .modal {
        display: none;
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        z-index: 1000;
        align-items: center;
        justify-content: center;
      }

      .modal.active {
        display: flex;
      }

      .modal-content {
        background: var(--card);
        border-radius: 20px;
        padding: 40px;
        max-width: 800px;
        width: 90%;
        max-height: 90vh;
        overflow-y: auto;
      }

      .modal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 30px;
        padding-bottom: 20px;
        border-bottom: 2px solid #f0f0f0;
      }

      .modal-header h2 {
        color: var(--primary);
        font-size: 2rem;
      }

      .close-btn {
        background: none;
        border: none;
        font-size: 2rem;
        cursor: pointer;
        color: var(--text-light);
      }

      .profile-section {
        margin-bottom: 30px;
      }

      .profile-section h3 {
        color: var(--primary);
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 2px solid var(--accent);
      }

      .info-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20px;
      }

      .info-item {
        background: #f8f9fa;
        padding: 15px;
        border-radius: 10px;
        border-left: 4px solid var(--accent);
      }

      .info-label {
        font-size: 0.85rem;
        color: var(--text-light);
        margin-bottom: 5px;
      }

      .info-value {
        font-size: 1.1rem;
        font-weight: 600;
        color: var(--primary);
      }

      .notes-section {
        margin-top: 20px;
      }

      .note-input {
        width: 100%;
        padding: 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 1rem;
        resize: vertical;
        min-height: 100px;
      }

      @media (max-width: 968px) {
        .search-bar {
          grid-template-columns: 1fr;
        }

        .students-grid {
          grid-template-columns: 1fr;
        }

        .student-details,
        .info-grid {
          grid-template-columns: 1fr;
        }
      }
    </style>
  </head>
  <body>
    <div class="container">
      <a
        href="${pageContext.request.contextPath}/views/PT/homePT.jsp"
        class="btn btn-back"
      >
        <i class="fas fa-arrow-left"></i> Quay lại
      </a>

      <div class="page-header">
        <h1><i class="fas fa-users"></i> Quản lý học viên</h1>
        <div class="breadcrumb">
          <a href="${pageContext.request.contextPath}/views/PT/homePT.jsp"
            >Home</a
          >
          <span>/</span>
          <span>Quản lý học viên</span>
        </div>
      </div>

      <!-- Stats -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon">
            <i class="fas fa-users"></i>
          </div>
          <div class="stat-content">
            <h3>${totalStudents != null ? totalStudents : 0}</h3>
            <p>Tổng học viên</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">
            <i class="fas fa-user-check"></i>
          </div>
          <div class="stat-content">
            <h3>${activeStudents != null ? activeStudents : 0}</h3>
            <p>Đang hoạt động</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">
            <i class="fas fa-trophy"></i>
          </div>
          <div class="stat-content">
            <h3>${achievedGoalCount != null ? achievedGoalCount : 0}</h3>
            <p>Đạt mục tiêu</p>
          </div>
        </div>
      </div>

      <!-- Search & Filter -->
      <div class="search-filter">
        <form action="${pageContext.request.contextPath}/pt/students/search" method="get">
          <div class="search-bar">
            <div class="form-group">
              <label>Tìm kiếm học viên</label>
              <input
                type="text"
                name="keyword"
                placeholder="Nhập tên, ID hoặc số điện thoại..."
                value="${searchTerm != null ? searchTerm : ''}"
              />
            </div>
            <div class="form-group">
              <label>Gói tập</label>
              <select name="package">
                <option value="">Tất cả</option>
                <option value="Personal Training" ${packageFilter == 'Personal Training' ? 'selected' : ''}>Personal Training</option>
                <option value="Weight Loss" ${packageFilter == 'Weight Loss' ? 'selected' : ''}>Weight Loss</option>
                <option value="Bodybuilding" ${packageFilter == 'Bodybuilding' ? 'selected' : ''}>Bodybuilding</option>
                <option value="Yoga" ${packageFilter == 'Yoga' ? 'selected' : ''}>Yoga</option>
              </select>
            </div>
            <button type="submit" class="btn"><i class="fas fa-search"></i> Tìm kiếm</button>
          </div>
        </form>
      </div>

      <!-- Students Grid -->
      <div class="students-grid">
        <c:choose>
          <c:when test="${students != null && !empty students}">
            <c:forEach var="student" items="${students}">
              <div class="student-card" onclick="openStudentDetail(${student.id})">
                <div class="student-header">
                  <div class="student-avatar">
                    <i class="fas fa-user"></i>
                  </div>
                  <div class="student-info">
                    <h3>${student.name != null ? student.name : 'N/A'}</h3>
                    <p class="package">${student.packageName != null ? student.packageName : 'Personal Training'}</p>
                  </div>
                </div>
                <div class="student-details">
                  <div class="detail-item">
                    <i class="fas fa-phone"></i>
                    <span>${student.phone != null && !empty student.phone ? student.phone : 'Chưa có'}</span>
                  </div>
                  <div class="detail-item">
                    <i class="fas fa-envelope"></i>
                    <span>${student.email != null ? student.email : 'N/A'}</span>
                  </div>
                  <div class="detail-item">
                    <i class="fas fa-calendar"></i>
                    <span>${student.trainingMonths != null ? student.trainingMonths : 0} tháng</span>
                  </div>
                  <div class="detail-item">
                    <i class="fas fa-dumbbell"></i>
                    <span>${student.sessionCount != null ? student.sessionCount : 0} buổi tập</span>
                  </div>
                </div>
                <div class="progress-section">
                  <div class="progress-label">
                    <span>Tiến độ ${student.progressType != null ? student.progressType : 'tập luyện'}</span>
                    <span>${student.progress != null ? student.progress : 0}%</span>
                  </div>
                  <div class="progress-bar">
                    <div class="progress-fill" style="width: ${student.progress != null ? student.progress : 0}%"></div>
                  </div>
                </div>
                <div class="student-actions">
                  <button
                    class="btn btn-info btn-sm"
                    onclick="event.stopPropagation(); openStudentDetail(${student.id})"
                  >
                    <i class="fas fa-eye"></i> Chi tiết
                  </button>
                  <button
                    class="btn btn-sm"
                    onclick="event.stopPropagation(); updateProgress(${student.id})"
                  >
                    <i class="fas fa-edit"></i> Cập nhật
                  </button>
                </div>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <div style="grid-column: 1 / -1; text-align: center; padding: 40px; color: var(--text-light);">
              <i class="fas fa-users" style="font-size: 3rem; margin-bottom: 20px; opacity: 0.3;"></i>
              <p style="font-size: 1.2rem;">Không tìm thấy học viên phù hợp.</p>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

    <!-- Modal chi tiết học viên -->
    <div id="studentModal" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <h2><i class="fas fa-user-circle"></i> Hồ sơ học viên</h2>
          <button class="close-btn" onclick="closeModal()">&times;</button>
        </div>

        <div class="profile-section">
          <h3>Thông tin cá nhân</h3>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">Họ và tên</div>
              <div class="info-value">Nguyễn Văn A</div>
            </div>
            <div class="info-item">
              <div class="info-label">Số điện thoại</div>
              <div class="info-value">0912345678</div>
            </div>
            <div class="info-item">
              <div class="info-label">Email</div>
              <div class="info-value">nguyenvana@gmail.com</div>
            </div>
            <div class="info-item">
              <div class="info-label">Ngày sinh</div>
              <div class="info-value">15/05/1995</div>
            </div>
          </div>
        </div>

        <div class="profile-section">
          <h3>Chỉ số cơ thể</h3>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">Cân nặng hiện tại</div>
              <div class="info-value">75 kg</div>
            </div>
            <div class="info-item">
              <div class="info-label">Chiều cao</div>
              <div class="info-value">175 cm</div>
            </div>
            <div class="info-item">
              <div class="info-label">BMI</div>
              <div class="info-value">24.5</div>
            </div>
            <div class="info-item">
              <div class="info-label">Mục tiêu</div>
              <div class="info-value">Giảm xuống 70 kg</div>
            </div>
          </div>
        </div>

        <div class="profile-section">
          <h3>Tiến độ tập luyện</h3>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">Gói tập</div>
              <div class="info-value">Personal Training Premium</div>
            </div>
            <div class="info-item">
              <div class="info-label">Thời gian tập</div>
              <div class="info-value">3 tháng</div>
            </div>
            <div class="info-item">
              <div class="info-label">Số buổi đã tập</div>
              <div class="info-value">48 buổi</div>
            </div>
            <div class="info-item">
              <div class="info-label">Tiến độ</div>
              <div class="info-value">75%</div>
            </div>
          </div>
        </div>

        <div class="notes-section">
          <h3>Ghi chú của PT</h3>
          <textarea
            class="note-input"
            placeholder="Nhập ghi chú về học viên..."
          >
Học viên rất chăm chỉ, tiến bộ tốt. Cần tăng cường bài tập cardio.</textarea
          >
        </div>

        <div
          style="
            display: flex;
            gap: 10px;
            justify-content: flex-end;
            margin-top: 30px;
          "
        >
          <button
            class="btn"
            style="background: #6c757d"
            onclick="closeModal()"
          >
            Đóng
          </button>
          <button class="btn" onclick="saveNotes()">
            <i class="fas fa-save"></i> Lưu thay đổi
          </button>
        </div>
      </div>
    </div>

    <script>
      function openStudentDetail(id) {
        document.getElementById('studentModal').classList.add('active');
      }

      function closeModal() {
        document.getElementById('studentModal').classList.remove('active');
      }

      function updateProgress(id) {
        alert('Mở form cập nhật tiến độ cho học viên #' + id);
      }

      function saveNotes() {
        alert('Lưu ghi chú thành công!');
        closeModal();
      }
    </script>
  </body>
</html>
