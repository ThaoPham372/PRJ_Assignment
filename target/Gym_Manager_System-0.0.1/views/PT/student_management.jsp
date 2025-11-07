<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>

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
        opacity: 0;
        transition: opacity .2s ease;
      }

      .modal.active {
        display: flex;
        opacity: 1;
      }

      .modal-content {
        background: var(--card);
        border-radius: 20px;
        padding: 40px;
        max-width: 800px;
        width: 90%;
        max-height: 90vh;
        overflow-y: auto;
        transform: translateY(10px);
        transition: transform .2s ease, box-shadow .2s ease;
        box-shadow: 0 10px 40px rgba(0,0,0,0.2);
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
        position: relative;
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

      /* Edit mode input styling */
      .info-item input[type="text"],
      .info-item input[type="email"],
      .info-item input[type="date"],
      .info-item input[type="number"],
      .info-item select,
      .info-item textarea {
        width: 100%;
        padding: 8px 12px;
        border-radius: 6px;
        border: 2px solid #ccc;
        font-size: 1rem;
        font-family: inherit;
        transition: all 0.3s;
        background: #fff;
      }

      .info-item input:focus,
      .info-item select:focus,
      .info-item textarea:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 3px rgba(236, 139, 90, 0.1);
      }

      .alert-success {
        background-color: #d4edda;
        color: #155724;
        padding: 12px 16px;
        border-radius: 6px;
        border: 1px solid #c3e6cb;
        margin-bottom: 20px;
        font-weight: 500;
        display: flex;
        align-items: center;
        gap: 10px;
      }

      .alert-error {
        background-color: #f8d7da;
        color: #721c24;
        padding: 12px 16px;
        border-radius: 6px;
        border: 1px solid #f5c6cb;
        margin-bottom: 20px;
        font-weight: 500;
        display: flex;
        align-items: center;
        gap: 10px;
      }

      .notes-section {
        margin-top: 20px;
      }

      .note-input {
        width: 100%;
        padding: 12px;
        border: 2px solid #ccc;
        border-radius: 6px;
        font-size: 1rem;
        font-family: inherit;
        resize: vertical;
        min-height: 100px;
        transition: all 0.3s;
        background: #fff;
      }
      
      .note-input:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 3px rgba(236, 139, 90, 0.1);
      }

      .student-list-row:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
        background: #fafafa;
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

      <!-- Messages -->
      <c:if test="${not empty successMessage}">
        <div style="background: #d4edda; color: #155724; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #c3e6cb;">
          <i class="fas fa-check-circle"></i> ${successMessage}
        </div>
      </c:if>
      <c:if test="${not empty errorMessage}">
        <div style="background: #f8d7da; color: #721c24; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #f5c6cb;">
          <i class="fas fa-exclamation-circle"></i> ${errorMessage}
        </div>
      </c:if>

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

      <!-- View Tabs -->
      <div style="display:flex; gap:10px; margin-bottom: 16px;">
        <button id="tab-grid" class="btn btn-sm" onclick="switchView('grid')" style="background:#ec8b5a;"><i class="fas fa-th-large"></i> Lưới</button>
        <button id="tab-list" class="btn btn-sm" onclick="switchView('list')" style="background:#6c757d;"><i class="fas fa-list"></i> Danh sách</button>
      </div>

      <!-- Students Grid -->
      <div class="students-grid">
        <c:choose>
          <c:when test="${students != null && !empty students}">
            <c:forEach var="student" items="${students}">
              <div class="student-card" onclick="openStudentDetail(${student.userId})">
                <div class="student-header">
                  <div class="student-avatar">
                    <i class="fas fa-user"></i>
                  </div>
                  <div class="student-info">
                    <h3>${student.user != null && student.user.name != null ? student.user.name : 'N/A'}</h3>
                    <p class="package">${student.trainingPackage != null ? student.trainingPackage : 'Personal Training'}</p>
                  </div>
                </div>
                <div class="student-details">
                  <div class="detail-item">
                    <i class="fas fa-phone"></i>
                    <span>${student.user != null && student.user.phone != null && !empty student.user.phone ? student.user.phone : 'Chưa có'}</span>
                  </div>
                  <div class="detail-item">
                    <i class="fas fa-envelope"></i>
                    <span>${student.user != null && student.user.email != null ? student.user.email : 'N/A'}</span>
                  </div>
                  <div class="detail-item">
                    <i class="fas fa-calendar"></i>
                    <span>${student.trainingDuration != null ? student.trainingDuration : '0'} tháng</span>
                  </div>
                  <div class="detail-item">
                    <i class="fas fa-dumbbell"></i>
                    <span>${student.trainingSessions != null ? student.trainingSessions : 0} buổi tập</span>
                  </div>
                </div>
                <div class="progress-section">
                  <div class="progress-label">
                    <span>Tiến độ ${student.goal != null ? student.goal : 'tập luyện'}</span>
                    <span>${student.trainingProgress != null ? student.trainingProgress : 0}%</span>
                  </div>
                  <div class="progress-bar">
                    <div class="progress-fill" style="width: ${student.trainingProgress != null ? student.trainingProgress : 0}%"></div>
                  </div>
                </div>
                <div class="student-actions">
                  <button
                    class="btn btn-info btn-sm"
                    onclick="event.stopPropagation(); openStudentDetail(${student.userId})"
                  >
                    <i class="fas fa-eye"></i> Chi tiết
                  </button>
                </div>
                <!-- Preload student data for instant modal -->
                <div id="studentData-${student.userId}" style="display:none"
                     data-user-id="${student.userId}"
                     data-name="<c:out value='${student.user != null ? student.user.name : ""}'/>"
                     data-phone="<c:out value='${student.user != null ? student.user.phone : ""}'/>"
                     data-email="<c:out value='${student.user != null ? student.user.email : ""}'/>"
                     data-dob="<c:out value='${student.user != null && student.user.dob != null ? student.user.dob : ""}'/>"
                     data-address="<c:out value='${student.user != null ? student.user.address : ""}'/>"
                     data-weight="${student.weight != null ? student.weight : 0}"
                     data-height="${student.height != null ? student.height : 0}"
                     data-bmi="${student.bmi != null ? student.bmi : 0}"
                     data-goal="<c:out value='${student.goal != null ? student.goal : ""}'/>"
                     data-package="<c:out value='${student.trainingPackage != null ? student.trainingPackage : ""}'/>"
                     data-duration="<c:out value='${student.trainingDuration != null ? student.trainingDuration : ""}'/>"
                     data-sessions="${student.trainingSessions != null ? student.trainingSessions : 0}"
                     data-progress="${student.trainingProgress != null ? student.trainingProgress : 0}"
                     data-note="<c:out value='${student.ptNote != null ? student.ptNote : ""}'/>">
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

    <!-- Students List (hidden by default) -->
    <div id="students-list" style="display:none; max-width: 1310px; margin: 0 auto;">
      <c:choose>
        <c:when test="${students != null && !empty students}">
          <!-- Header -->
          <div style="display:grid; grid-template-columns: 240px 160px 140px 220px 180px 120px 220px; gap:16px; background:#141a49; color:#fff; font-weight:600; padding:16px 20px; border-radius:12px 12px 0 0; margin-bottom:16px; align-items:center;">
            <div>Họ và tên</div>
            <div>Gói tập</div>
            <div>Điện thoại</div>
            <div>Email</div>
            <div>Thời gian / Buổi tập</div>
            <div style="text-align:right;">Tiến độ</div>
            <div style="text-align:right; white-space:nowrap;">Thao tác</div>
          </div>
          
          <!-- Student Rows -->
          <div style="display:flex; flex-direction:column; gap:16px;">
            <c:forEach var="student" items="${students}">
              <div class="student-list-row" style="background:var(--card); border-radius:12px; box-shadow:0 4px 20px var(--shadow); padding:20px; transition:all 0.3s; cursor:pointer;">
                <div style="display:grid; grid-template-columns: 240px 160px 140px 220px 180px 120px 220px; gap:16px; align-items:center;">
                  <!-- Họ và tên -->
                  <div style="display:flex; align-items:center; gap:12px; min-width:0;">
                    <span style="width:40px; height:40px; border-radius:50%; background:var(--gradient-accent); color:#fff; display:inline-flex; align-items:center; justify-content:center; flex-shrink:0;"><i class="fas fa-user"></i></span>
                    <span style="font-weight:600; color:var(--primary); white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">${student.user != null && student.user.name != null ? student.user.name : 'N/A'}</span>
                  </div>
                  
                  <!-- Gói tập -->
                  <div style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis; color:var(--text);">${student.trainingPackage != null ? student.trainingPackage : '—'}</div>
                  
                  <!-- Điện thoại -->
                  <div style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis; color:var(--text);">${student.user != null && student.user.phone != null && !empty student.user.phone ? student.user.phone : '—'}</div>
                  
                  <!-- Email -->
                  <div style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis; color:var(--text);" title="${student.user != null && student.user.email != null ? student.user.email : '—'}">${student.user != null && student.user.email != null ? student.user.email : '—'}</div>
                  
                  <!-- Thời gian / Buổi tập -->
                  <div style="white-space:nowrap; color:var(--text);">${student.trainingDuration != null ? student.trainingDuration : '0'} tháng • ${student.trainingSessions != null ? student.trainingSessions : 0} buổi</div>
                  
                  <!-- Tiến độ -->
                  <div style="text-align:right;">
                    <span style="font-weight:600; color:var(--primary);">${student.trainingProgress != null ? student.trainingProgress : 0}%</span>
                  </div>
                  
                  <!-- Thao tác -->
                  <div style="display:flex; gap:10px; justify-content:flex-start; align-items:center; width:100%; padding-right:0;">
                    <button class="btn btn-info btn-sm" onclick="openStudentDetail(${student.userId})" style="white-space:nowrap; padding:8px 12px;"><i class="fas fa-eye"></i> Chi tiết</button>
                  </div>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:when>
        <c:otherwise>
          <div style="text-align:center; padding: 40px; color: var(--text-light); background:var(--card); border-radius:12px; box-shadow:0 4px 20px var(--shadow);">
            <i class="fas fa-users" style="font-size: 3rem; margin-bottom: 20px; opacity: 0.3;"></i>
            <p style="font-size: 1.2rem;">Không tìm thấy học viên phù hợp.</p>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
    <!-- Modal chi tiết học viên -->
    <div id="studentModal" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <h2><i class="fas fa-user-circle"></i> <span id="modalTitle">Hồ sơ học viên</span></h2>
          <button class="close-btn" onclick="closeModal()">&times;</button>
        </div>

        <div class="profile-section">
          <h3>Thông tin cá nhân</h3>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">Họ và tên</div>
              <div class="info-value" id="view-name"></div>
            </div>
            <div class="info-item">
              <div class="info-label">Số điện thoại</div>
              <div class="info-value" id="view-phone"></div>
            </div>
            <div class="info-item">
              <div class="info-label">Email</div>
              <div class="info-value" id="view-email"></div>
            </div>
            <div class="info-item">
              <div class="info-label">Ngày sinh</div>
              <div class="info-value" id="view-dob"></div>
            </div>
            <div class="info-item">
              <div class="info-label">Địa chỉ</div>
              <div class="info-value" id="view-address"></div>
            </div>
          </div>
        </div>

        <div class="profile-section">
          <h3>Chỉ số cơ thể</h3>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">Cân nặng hiện tại (kg)</div>
              <div class="info-value" id="view-weight"></div>
            </div>
            <div class="info-item">
              <div class="info-label">Chiều cao (cm)</div>
              <div class="info-value" id="view-height"></div>
            </div>
            <div class="info-item">
              <div class="info-label">BMI</div>
              <div class="info-value" id="view-bmi"></div>
            </div>
            <div class="info-item">
              <div class="info-label">Mục tiêu</div>
              <div class="info-value" id="view-goal"></div>
            </div>
          </div>
        </div>

        <div class="profile-section">
          <h3>Tiến độ tập luyện</h3>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">Gói tập</div>
              <div class="info-value" id="view-package"></div>
            </div>
            <div class="info-item">
              <div class="info-label">Thời gian tập (tháng)</div>
              <div class="info-value" id="view-duration"></div>
            </div>
            <div class="info-item">
              <div class="info-label">Số buổi đã tập</div>
              <div class="info-value" id="view-sessions"></div>
            </div>
            <div class="info-item">
              <div class="info-label">Tiến độ (%)</div>
              <div class="info-value" id="view-progress"></div>
            </div>
          </div>
        </div>

        <div class="notes-section">
          <h3>Ghi chú của PT</h3>
          <div id="view-note" style="padding: 15px; background: #f8f9fa; border-radius: 8px; min-height: 100px; white-space: pre-wrap;"></div>
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
            type="button"
            class="btn"
            style="background: #6c757d"
            onclick="closeModal()"
          >
            Đóng
          </button>
        </div>
      </div>
    </div>

    <script>
      async function openStudentDetail(id) {
        // Try preload first
        const dataEl = document.getElementById('studentData-' + id);
        if (dataEl) {
          const student = extractStudentFromDataEl(dataEl);
          populateModal(student);
          document.getElementById('studentModal').classList.add('active');
          return;
        }
        // Fallback to AJAX fetch JSON
        try {
          const res = await fetch('${pageContext.request.contextPath}/pt/students/view?id=' + id + '&format=json', {
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
          });
          const json = await res.json();
          if (json && json.success && json.data) {
            populateModal(json.data);
            document.getElementById('studentModal').classList.add('active');
          }
        } catch (e) {
          console.error('Load student detail failed', e);
        }
      }

      function extractStudentFromDataEl(el) {
        return {
          userId: parseInt(el.getAttribute('data-user-id')) || 0,
          name: el.getAttribute('data-name') || '',
          phone: el.getAttribute('data-phone') || '',
          email: el.getAttribute('data-email') || '',
          dob: el.getAttribute('data-dob') || '',
          address: el.getAttribute('data-address') || '',
          weight: parseFloat(el.getAttribute('data-weight')) || 0,
          height: parseFloat(el.getAttribute('data-height')) || 0,
          bmi: parseFloat(el.getAttribute('data-bmi')) || 0,
          goal: el.getAttribute('data-goal') || '',
          trainingPackage: el.getAttribute('data-package') || '',
          trainingDuration: el.getAttribute('data-duration') || '',
          trainingSessions: parseInt(el.getAttribute('data-sessions')) || 0,
          trainingProgress: parseInt(el.getAttribute('data-progress')) || 0,
          ptNote: el.getAttribute('data-note') || ''
        };
      }

      function closeModal() {
        document.getElementById('studentModal').classList.remove('active');
      }

      // Toggle between grid and list views
      function switchView(view) {
        const gridEl = document.querySelector('.students-grid');
        const listEl = document.getElementById('students-list');
        const tabGrid = document.getElementById('tab-grid');
        const tabList = document.getElementById('tab-list');

        // Check if elements exist before manipulating
        if (!gridEl || !listEl || !tabGrid || !tabList) {
          console.error('Elements not found for view switching');
          return;
        }

        if (view === 'list') {
          if (gridEl) gridEl.style.display = 'none';
          if (listEl) listEl.style.display = 'block';
          if (tabGrid) tabGrid.style.background = '#6c757d';
          if (tabList) tabList.style.background = '#ec8b5a';
          window.location.hash = '#list';
        } else {
          if (listEl) listEl.style.display = 'none';
          if (gridEl) gridEl.style.display = 'grid';
          if (tabList) tabList.style.background = '#6c757d';
          if (tabGrid) tabGrid.style.background = '#ec8b5a';
          window.location.hash = '#grid';
        }
      }

      // Initialize view based on hash
      window.addEventListener('DOMContentLoaded', function() {
        const hash = window.location.hash;
        if (hash === '#list') {
          switchView('list');
        } else {
          switchView('grid');
        }
      });
    </script>

    <!-- Populate modal with student data from server (set by JSP) -->
    <c:if test="${not empty studentDetail}">
      <c:set var="user" value="${studentDetail.user}" />
      <!-- Hidden element to store student data -->
      <div id="studentData" style="display: none;"
           data-user-id="${studentDetail.userId}"
           data-name="<c:out value='${user != null && user.name != null ? user.name : ""}' />"
           data-phone="<c:out value='${user != null && user.phone != null ? user.phone : ""}' />"
           data-email="<c:out value='${user != null && user.email != null ? user.email : ""}' />"
           data-dob="<c:out value='${user != null && user.dob != null ? user.dob : ""}' />"
           data-address="<c:out value='${user != null && user.address != null ? user.address : ""}' />"
           data-weight="${studentDetail.weight != null ? studentDetail.weight : 0}"
           data-height="${studentDetail.height != null ? studentDetail.height : 0}"
           data-bmi="${studentDetail.bmi != null ? studentDetail.bmi : 0}"
           data-goal="<c:out value='${studentDetail.goal != null ? studentDetail.goal : ""}' />"
           data-package="<c:out value='${studentDetail.trainingPackage != null ? studentDetail.trainingPackage : ""}' />"
           data-duration="<c:out value='${studentDetail.trainingDuration != null ? studentDetail.trainingDuration : ""}' />"
           data-sessions="${studentDetail.trainingSessions != null ? studentDetail.trainingSessions : 0}"
           data-progress="${studentDetail.trainingProgress != null ? studentDetail.trainingProgress : 0}"
           data-note="<c:out value='${studentDetail.ptNote != null ? studentDetail.ptNote : ""}' />">
      </div>
      <script>
        window.addEventListener('DOMContentLoaded', function() {
          const dataEl = document.getElementById('studentData');
          if (dataEl) {
            const student = {
              userId: parseInt(dataEl.getAttribute('data-user-id')) || 0,
              name: dataEl.getAttribute('data-name') || '',
              phone: dataEl.getAttribute('data-phone') || '',
              email: dataEl.getAttribute('data-email') || '',
              dob: dataEl.getAttribute('data-dob') || '',
              address: dataEl.getAttribute('data-address') || '',
              weight: parseFloat(dataEl.getAttribute('data-weight')) || 0,
              height: parseFloat(dataEl.getAttribute('data-height')) || 0,
              bmi: parseFloat(dataEl.getAttribute('data-bmi')) || 0,
              goal: dataEl.getAttribute('data-goal') || '',
              trainingPackage: dataEl.getAttribute('data-package') || '',
              trainingDuration: dataEl.getAttribute('data-duration') || '',
              trainingSessions: parseInt(dataEl.getAttribute('data-sessions')) || 0,
              trainingProgress: parseInt(dataEl.getAttribute('data-progress')) || 0,
              ptNote: dataEl.getAttribute('data-note') || ''
            };
            populateModal(student);
            document.getElementById('studentModal').classList.add('active');
            
            // Check if edit mode
            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.get('mode') === 'edit') {
              isEditMode = true;
              toggleEditMode();
            }
          }
        });
      </script>
    </c:if>

    <script>

      function populateModal(student) {
        document.getElementById('view-name').textContent = student.name || 'N/A';
        document.getElementById('view-phone').textContent = student.phone || 'Chưa có';
        document.getElementById('view-email').textContent = student.email || 'N/A';
        
        // Format date for display
        let dobDisplay = 'Chưa có';
        if (student.dob) {
          try {
            // If dob is already in YYYY-MM-DD format, use it directly
            if (student.dob.match(/^\d{4}-\d{2}-\d{2}$/)) {
              // Format for display: DD/MM/YYYY
              const [year, month, day] = student.dob.split('-');
              dobDisplay = day + '/' + month + '/' + year;
            } else {
              // Try to parse other formats
              const date = new Date(student.dob);
              if (!isNaN(date.getTime())) {
                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
                const day = String(date.getDate()).padStart(2, '0');
                dobDisplay = day + '/' + month + '/' + year;
              }
            }
          } catch (e) {
            console.error('Error parsing date:', e);
          }
        }
        document.getElementById('view-dob').textContent = dobDisplay;
        
        document.getElementById('view-address').textContent = student.address || 'Chưa có';
        document.getElementById('view-weight').textContent = student.weight ? student.weight + ' kg' : 'Chưa có';
        document.getElementById('view-height').textContent = student.height ? student.height + ' cm' : 'Chưa có';
        document.getElementById('view-bmi').textContent = student.bmi ? student.bmi.toFixed(2) : 'Chưa có';
        document.getElementById('view-goal').textContent = student.goal || 'Chưa có';
        document.getElementById('view-package').textContent = student.trainingPackage || 'Chưa có';
        document.getElementById('view-duration').textContent = student.trainingDuration ? student.trainingDuration + ' tháng' : 'Chưa có';
        document.getElementById('view-sessions').textContent = student.trainingSessions ? student.trainingSessions + ' buổi' : '0 buổi';
        document.getElementById('view-progress').textContent = student.trainingProgress ? student.trainingProgress + '%' : '0%';
        document.getElementById('view-note').textContent = student.ptNote || 'Chưa có ghi chú';
      }

      function showInlineMessage(message, isError) {
        // Create or update a banner on the page
        let banner = document.getElementById('inlineMessageBanner');
        if (!banner) {
          banner = document.createElement('div');
          banner.id = 'inlineMessageBanner';
          banner.style.position = 'fixed';
          banner.style.top = '20px';
          banner.style.right = '20px';
          banner.style.zIndex = '2000';
          banner.style.padding = '12px 16px';
          banner.style.borderRadius = '8px';
          banner.style.boxShadow = '0 6px 20px rgba(0,0,0,0.15)';
          document.body.appendChild(banner);
        }
        banner.style.background = isError ? '#f8d7da' : '#d4edda';
        banner.style.color = isError ? '#721c24' : '#155724';
        banner.style.border = '1px solid ' + (isError ? '#f5c6cb' : '#c3e6cb');
        banner.innerHTML = '<i class="fas ' + (isError ? 'fa-exclamation-circle' : 'fa-check-circle') + '"></i> ' + message;
        clearTimeout(window.__bannerTimeout);
        window.__bannerTimeout = setTimeout(() => {
          if (banner && banner.parentNode) banner.parentNode.removeChild(banner);
        }, 3500);
      }
    </script>
  </body>
</html>
