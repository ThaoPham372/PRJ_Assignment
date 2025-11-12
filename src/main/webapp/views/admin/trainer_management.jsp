<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý huấn luyện viên - GymFit</title>

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
        --text: #2c3e50;
        --text-light: #5a6c7d;
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
        color: var(--text);
        background: #f6f6f8;
      }

      .admin-container {
        display: flex;
        min-height: 100vh;
      }

      /* Sidebar - same as other pages */
      .sidebar {
        width: 280px;
        background: var(--gradient-primary);
        color: #fff;
        position: fixed;
        height: 100vh;
        overflow-y: auto;
        box-shadow: 4px 0 20px rgba(0, 0, 0, 0.15);
        z-index: 100;
      }

      .sidebar-header {
        padding: 30px 25px;
        border-bottom: 1px solid rgba(255, 255, 255, 0.1);
      }

      .sidebar-brand {
        font-size: 1.8rem;
        font-weight: 900;
        display: flex;
        align-items: center;
        gap: 10px;
        color: #fff;
        text-decoration: none;
      }

      .sidebar-user {
        margin-top: 15px;
        padding: 15px;
        background: rgba(255, 255, 255, 0.1);
        border-radius: 10px;
        display: flex;
        align-items: center;
        gap: 12px;
      }

      .sidebar-user-avatar {
        width: 45px;
        height: 45px;
        border-radius: 50%;
        background: var(--accent);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.2rem;
      }

      .sidebar-user-info h4 {
        font-size: 0.95rem;
        margin-bottom: 3px;
      }
      .sidebar-user-info p {
        font-size: 0.75rem;
        opacity: 0.8;
      }

      .sidebar-menu {
        padding: 20px 0;
      }
      .sidebar-menu-item {
        list-style: none;
      }

      .sidebar-menu-link {
        display: flex;
        align-items: center;
        gap: 15px;
        padding: 15px 25px;
        color: #fff;
        text-decoration: none;
        font-weight: 500;
        font-size: 0.95rem;
        transition: all 0.3s ease;
        border-left: 3px solid transparent;
      }

      .sidebar-menu-link:hover,
      .sidebar-menu-link.active {
        background: rgba(255, 255, 255, 0.1);
        border-left-color: var(--accent);
        color: var(--accent);
      }

      .sidebar-menu-link i {
        font-size: 1.1rem;
        width: 20px;
      }

      /* Main Content */
      .main-content {
        flex: 1;
        margin-left: 280px;
        background: #f6f6f8;
      }

      .top-bar {
        background: #fff;
        padding: 20px 40px;
        box-shadow: 0 2px 10px var(--shadow);
        display: flex;
        justify-content: space-between;
        align-items: center;
        position: sticky;
        top: 0;
        z-index: 90;
      }

      .top-bar h1 {
        font-size: 1.8rem;
        font-weight: 700;
        color: var(--primary);
      }

      .top-bar-actions {
        display: flex;
        gap: 15px;
        align-items: center;
      }

      .btn {
        background: var(--gradient-accent);
        color: #fff;
        border: none;
        border-radius: 50px;
        padding: 10px 20px;
        font-weight: 600;
        font-size: 0.9rem;
        cursor: pointer;
        transition: all 0.3s ease;
        box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
      }

      .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
      }

      .btn-outline {
        background: transparent;
        border: 2px solid var(--primary);
        color: var(--primary);
        box-shadow: none;
      }

      .btn-outline:hover {
        background: var(--primary);
        color: #fff;
      }

      .content-area {
        padding: 30px 40px;
      }

      /* Trainer Cards Grid */
      .trainers-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 25px;
      }

      .trainer-card {
        background: #fff;
        border-radius: 15px;
        padding: 25px;
        box-shadow: 0 4px 15px var(--shadow);
        transition: all 0.3s ease;
        position: relative;
      }

      .trainer-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 25px rgba(0, 0, 0, 0.15);
      }

      .trainer-card.inactive {
        opacity: 0.7;
        border: 2px dashed #ccc;
        background: #f8f9fa;
      }

      .trainer-card.inactive:hover {
        opacity: 0.9;
      }

      .status-badge {
        position: absolute;
        top: 15px;
        right: 15px;
        padding: 5px 12px;
        border-radius: 20px;
        font-size: 0.75rem;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.5px;
      }

      .status-badge.active {
        background: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
      }

      .status-badge.inactive {
        background: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
      }

      .trainer-header {
        display: flex;
        align-items: center;
        gap: 15px;
        margin-bottom: 20px;
      }

      .trainer-avatar {
        width: 70px;
        height: 70px;
        border-radius: 50%;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 2rem;
        color: #fff;
      }

      .trainer-info h3 {
        font-size: 1.2rem;
        margin-bottom: 5px;
        color: var(--primary);
      }

      .trainer-info p {
        font-size: 0.85rem;
        color: var(--text-light);
      }

      .trainer-stats {
        display: flex;
        justify-content: space-between;
        margin: 15px 0;
        padding: 15px;
        background: #f8f9fa;
        border-radius: 10px;
      }

      .stat-item {
        text-align: center;
      }

      .stat-item strong {
        display: block;
        font-size: 1.3rem;
        color: var(--primary);
      }

      .stat-item span {
        font-size: 0.8rem;
        color: var(--text-light);
      }

      .trainer-schedule {
        margin-top: 15px;
      }

      .trainer-schedule h4 {
        font-size: 0.9rem;
        margin-bottom: 10px;
        color: var(--text);
      }

      .schedule-item {
        display: flex;
        justify-content: space-between;
        padding: 8px 0;
        border-bottom: 1px solid #e0e0e0;
        font-size: 0.85rem;
      }

      .trainer-actions {
        display: flex;
        gap: 10px;
        margin-top: 15px;
      }

      .btn-small {
        padding: 8px 16px;
        font-size: 0.85rem;
        flex: 1;
      }

      /* Actions Bar */
      .actions-bar {
        background: #fff;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
        margin-bottom: 25px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
        gap: 15px;
      }

      .filter-select {
        padding: 10px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 0.9rem;
      }

      /* Responsive */
      @media (max-width: 768px) {
        .sidebar {
          width: 70px;
        }
        .sidebar-brand span,
        .sidebar-menu-link span,
        .sidebar-user-info {
          display: none;
        }
        .main-content {
          margin-left: 70px;
        }
        .trainers-grid {
          grid-template-columns: 1fr;
        }
      }
    </style>
  </head>
  <body>
    <div class="admin-container">
      <!-- Sidebar -->
      <aside class="sidebar">
        <div class="sidebar-header">
          <a
            href="${pageContext.request.contextPath}/admin/admin-home"
            class="sidebar-brand"
          >
            <i class="fas fa-dumbbell"></i>
            <span>GYMFIT</span>
          </a>

          <div class="sidebar-user">
            <div class="sidebar-user-avatar">
              <i class="fas fa-user-shield"></i>
            </div>
            <div class="sidebar-user-info">
              <h4>Admin User</h4>
              <p>Administrator</p>
            </div>
          </div>
        </div>

        <ul class="sidebar-menu">
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/dashboard"
              class="sidebar-menu-link"
            >
              <i class="fas fa-home"></i><span>Trang chủ</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/profile"
              class="sidebar-menu-link"
            >
              <i class="fas fa-user-circle"></i><span>Profile của Admin</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/account-management"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users-cog"></i><span>Quản lý tài khoản</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/membership-management"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users"></i><span>Quản lý hội viên</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/service-schedule"
              class="sidebar-menu-link"
            >
              <i class="fas fa-calendar-alt"></i><span>Dịch vụ & Lịch tập</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/trainer-management"
              class="sidebar-menu-link active"
            >
              <i class="fas fa-chalkboard-teacher"></i><span>Quản lý PT</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/sales-management"
              class="sidebar-menu-link"
            >
              <i class="fas fa-box"></i><span>Quản lý đơn hàng</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/reports"
              class="sidebar-menu-link"
            >
              <i class="fas fa-chart-line"></i><span>Báo cáo & Thống kê</span>
            </a>
          </li>
        </ul>
      </aside>

      <!-- Main Content -->
      <main class="main-content">
        <div class="top-bar">
          <h1>
            <i class="fas fa-chalkboard-teacher"></i> Quản lý huấn luyện viên
          </h1>
          <div class="top-bar-actions">
            <a
              href="${pageContext.request.contextPath}/admin/dashboard"
              class="btn btn-outline"
            >
              <i class="fas fa-arrow-left"></i> Quay lại
            </a>
          </div>
        </div>

        <div class="content-area">
          <!-- Messages -->
          <c:if test="${not empty success}">
            <div style="background: #d4edda; color: #155724; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #c3e6cb;">
              <i class="fas fa-check-circle"></i> ${success}
            </div>
          </c:if>
          <c:if test="${not empty error}">
            <div style="background: #f8d7da; color: #721c24; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #f5c6cb;">
              <i class="fas fa-exclamation-circle"></i> ${error}
            </div>
          </c:if>

          <!-- Actions Bar -->
          <div class="actions-bar">
            <input
              type="text"
              class="filter-select"
              id="searchInput"
              placeholder="Tìm kiếm PT..."
              style="width: 250px"
              onkeyup="filterTrainers()"
            />
            <button class="btn" onclick="openAddTrainerModal()">
              <i class="fas fa-user-plus"></i> Thêm PT mới
            </button>
          </div>

          <!-- Trainers Grid -->
          <div class="trainers-grid" id="trainersGrid">
            <c:choose>
              <c:when test="${not empty trainers}">
                <c:forEach var="trainer" items="${trainers}">
                  <c:set var="trainerStatus" value="${trainer.status != null ? trainer.status : ''}" />
                  <c:set var="isActive" value="${trainerStatus == 'active' || trainerStatus == 'ACTIVE'}" />
                  <div class="trainer-card ${isActive ? '' : 'inactive'}" data-name="${trainer.name}" data-email="${trainer.email}">
                    <span class="status-badge ${isActive ? 'active' : 'inactive'}">
                      ${isActive ? 'ACTIVE' : 'INACTIVE'}
                    </span>
                    <div class="trainer-header">
                      <div class="trainer-avatar">
                        <i class="fas fa-user-tie"></i>
                      </div>
                      <div class="trainer-info">
                        <h3>${trainer.name}</h3>
                        <p><i class="fas fa-envelope"></i> ${trainer.email}</p>
                        <c:if test="${not empty trainer.phone}">
                          <p><i class="fas fa-phone"></i> ${trainer.phone}</p>
                        </c:if>
                      </div>
                    </div>

                    <div class="trainer-stats">
                      <div class="stat-item">
                        <strong>${trainer.specialization != null ? trainer.specialization : 'N/A'}</strong>
                        <span>Chuyên môn</span>
                      </div>
                      <div class="stat-item">
                        <strong>${trainer.yearsOfExperience != null ? trainer.yearsOfExperience : 0}</strong>
                        <span>Năm kinh nghiệm</span>
                      </div>
                      <div class="stat-item">
                        <strong>
                          <c:choose>
                            <c:when test="${trainer.workAt != null}">
                              <c:forEach var="gym" items="${gyms}">
                                <c:if test="${gym.gymId.toString() == trainer.workAt}">
                                  ${gym.name}
                                </c:if>
                              </c:forEach>
                            </c:when>
                            <c:otherwise>N/A</c:otherwise>
                          </c:choose>
                        </strong>
                        <span>Cơ sở</span>
                      </div>
                    </div>

                    <div class="trainer-actions">
                      <a href="${pageContext.request.contextPath}/admin/trainer-management?action=edit&id=${trainer.id}" 
                         class="btn btn-small" style="background: #3498db; text-decoration: none;">
                        <i class="fas fa-edit"></i> Sửa
                      </a>
                      <c:choose>
                        <c:when test="${isActive}">
                          <button class="btn btn-small" style="background: #e74c3c" onclick="deleteTrainer(${trainer.id})">
                            <i class="fas fa-trash"></i> Xóa
                          </button>
                        </c:when>
                        <c:otherwise>
                          <button class="btn btn-small" style="background: #28a745" onclick="activateTrainer(${trainer.id})">
                            <i class="fas fa-check-circle"></i> Kích Hoạt
                          </button>
                        </c:otherwise>
                      </c:choose>
                    </div>
                  </div>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <div style="grid-column: 1 / -1; text-align: center; padding: 40px; color: var(--text-light);">
                  <i class="fas fa-user-slash" style="font-size: 3rem; margin-bottom: 15px;"></i>
                  <p>Chưa có PT nào trong hệ thống</p>
                </div>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </main>
    </div>

    <!-- Add/Edit Trainer Modal -->
    <div class="modal" id="trainerModal" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 1000; align-items: center; justify-content: center;">
      <div class="modal-content" style="background: white; padding: 30px; border-radius: 15px; max-width: 600px; width: 90%; max-height: 90vh; overflow-y: auto;">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
          <h2 id="trainerModalTitle">Thêm PT mới</h2>
          <button onclick="closeModal('trainerModal')" style="background: none; border: none; font-size: 1.5rem; cursor: pointer;">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/admin/trainer-management" method="post" id="trainerForm">
          <input type="hidden" name="action" id="trainerAction" value="add" />
          <input type="hidden" name="id" id="trainerId" value="${editTrainer != null ? editTrainer.id : ''}" />
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Họ và tên *</label>
            <input type="text" name="name" class="filter-select" style="width: 100%;" value="${editTrainer != null ? editTrainer.name : ''}" required />
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Email *</label>
            <input type="email" name="email" class="filter-select" style="width: 100%;" value="${editTrainer != null ? editTrainer.email : ''}" required />
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Số điện thoại *</label>
            <input type="text" name="phone" id="phoneInput" class="filter-select" style="width: 100%;" 
                   value="${editTrainer != null ? editTrainer.phone : ''}" 
                   pattern="[0-9]{10,11}" 
                   title="Số điện thoại phải có 10-11 chữ số" 
                   required />
            <small style="color: #666;">Nhập số điện thoại 10-11 chữ số</small>
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Username *</label>
            <input type="text" name="username" id="usernameInput" class="filter-select" style="width: 100%;" 
                   value="${editTrainer != null ? editTrainer.username : ''}" 
                   pattern="[a-zA-Z0-9_]{3,20}" 
                   title="Username phải có 3-20 ký tự, chỉ chứa chữ, số và dấu gạch dưới" 
                   required />
            <small style="color: #666;">3-20 ký tự, chỉ chứa chữ, số và _</small>
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Mật khẩu <span id="passwordRequired">*</span></label>
            <input type="password" name="password" id="passwordInput" class="filter-select" style="width: 100%;" 
                   minlength="6" 
                   title="Mật khẩu phải có ít nhất 6 ký tự" />
            <small style="color: #666;" id="passwordHelp">(Mật khẩu tối thiểu 6 ký tự. Để trống nếu không muốn đổi mật khẩu khi sửa)</small>
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Chuyên môn</label>
            <input type="text" name="specialization" class="filter-select" style="width: 100%;" value="${editTrainer != null ? editTrainer.specialization : ''}" />
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Số năm kinh nghiệm</label>
            <input type="number" name="yearsOfExperience" class="filter-select" style="width: 100%;" min="0" value="${editTrainer != null ? editTrainer.yearsOfExperience : ''}" />
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Trình độ chứng chỉ</label>
            <input type="text" name="certificationLevel" class="filter-select" style="width: 100%;" value="${editTrainer != null ? editTrainer.certificationLevel : ''}" />
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Lương</label>
            <input type="number" name="salary" class="filter-select" style="width: 100%;" step="0.01" min="0" value="${editTrainer != null ? editTrainer.salary : ''}" />
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Cơ sở làm việc *</label>
            <select name="workAt" id="workAtSelect" class="filter-select" style="width: 100%;" required>
              <option value="">-- Chọn cơ sở --</option>
              <c:forEach var="gym" items="${gyms}">
                <option value="${gym.gymId}" 
                  <c:if test="${editTrainer != null && editTrainer.workAt != null && (editTrainer.workAt == gym.gymId.toString() || editTrainer.workAt.equals(gym.gymId.toString()))}">selected</c:if>>
                  ${gym.name}<c:if test="${gym.address != null && !gym.address.isEmpty()}"> - ${gym.address}</c:if>
                </option>
              </c:forEach>
            </select>
            <small style="color: #666;">Chọn cơ sở gym mà PT sẽ làm việc</small>
          </div>
          
          <div style="display: flex; gap: 10px; justify-content: flex-end; margin-top: 20px;">
            <button type="button" class="btn btn-outline" onclick="closeModal('trainerModal')">Hủy</button>
            <button type="submit" class="btn" onclick="return validateTrainerForm()">Lưu</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Schedule Modal -->
    <div class="modal" id="scheduleModal" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 1000; align-items: center; justify-content: center;">
      <div class="modal-content" style="background: white; padding: 30px; border-radius: 15px; max-width: 800px; width: 90%; max-height: 90vh; overflow-y: auto;">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
          <h2>Quản lý lịch PT</h2>
          <button onclick="closeModal('scheduleModal')" style="background: none; border: none; font-size: 1.5rem; cursor: pointer;">&times;</button>
        </div>
        
        <div id="scheduleList" style="margin-bottom: 20px;">
          <h3 style="margin-bottom: 15px;">Lịch hiện tại</h3>
          <div id="scheduleItems">
            <c:if test="${not empty viewSchedules}">
              <c:forEach var="schedule" items="${viewSchedules}">
                <div style="background: #f8f9fa; padding: 15px; border-radius: 8px; margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
                  <div>
                    <strong>${schedule.dayOfWeek.displayName}</strong> - 
                    <c:forEach var="slot" items="${timeSlots}">
                      <c:if test="${slot.slotId == schedule.slotId}">
                        ${slot.slotName} (${slot.startTime} - ${slot.endTime})
                      </c:if>
                    </c:forEach>
                    <br/>
                    <small style="color: #666;">
                      Cơ sở: 
                      <c:forEach var="gym" items="${gyms}">
                        <c:if test="${gym.gymId.intValue() == schedule.gymId}">
                          ${gym.name}
                        </c:if>
                      </c:forEach>
                      | Trạng thái: ${schedule.isAvailable ? 'Có sẵn' : 'Không có sẵn'}
                    </small>
                  </div>
                  <button class="btn btn-small" style="background: #e74c3c; padding: 5px 10px;" onclick="deleteSchedule(${schedule.scheduleId})">
                    <i class="fas fa-trash"></i> Xóa
                  </button>
                </div>
              </c:forEach>
            </c:if>
            <c:if test="${empty viewSchedules}">
              <p style="color: #666; text-align: center; padding: 20px;">Chưa có lịch nào</p>
            </c:if>
          </div>
        </div>
        
        <h3 style="margin-bottom: 15px;">Thêm lịch mới</h3>
        <form action="${pageContext.request.contextPath}/admin/trainer-management" method="post" id="scheduleForm">
          <input type="hidden" name="action" value="addSchedule" />
          <input type="hidden" name="trainerId" id="scheduleTrainerId" />
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Cơ sở *</label>
            <select name="gymId" class="filter-select" style="width: 100%;" required>
              <option value="">-- Chọn cơ sở --</option>
              <c:forEach var="gym" items="${gyms}">
                <option value="${gym.gymId}">${gym.name}</option>
              </c:forEach>
            </select>
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Thứ trong tuần *</label>
            <select name="dayOfWeek" class="filter-select" style="width: 100%;" required>
              <option value="">-- Chọn thứ --</option>
              <option value="MONDAY">Thứ Hai</option>
              <option value="TUESDAY">Thứ Ba</option>
              <option value="WEDNESDAY">Thứ Tư</option>
              <option value="THURSDAY">Thứ Năm</option>
              <option value="FRIDAY">Thứ Sáu</option>
              <option value="SATURDAY">Thứ Bảy</option>
              <option value="SUNDAY">Chủ Nhật</option>
            </select>
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Ca tập *</label>
            <select name="slotId" class="filter-select" style="width: 100%;" required>
              <option value="">-- Chọn ca tập --</option>
              <c:forEach var="slot" items="${timeSlots}">
                <option value="${slot.slotId}">${slot.slotName} (${slot.startTime} - ${slot.endTime})</option>
              </c:forEach>
            </select>
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Số lượng đặt tối đa</label>
            <input type="number" name="maxBookings" class="filter-select" style="width: 100%;" value="1" min="1" />
          </div>
          
          <div style="margin-bottom: 15px;">
            <label style="display: block; margin-bottom: 5px; font-weight: 600;">Ghi chú</label>
            <textarea name="notes" class="filter-select" style="width: 100%;" rows="3"></textarea>
          </div>
          
          <div style="display: flex; gap: 10px; justify-content: flex-end; margin-top: 20px;">
            <button type="button" class="btn btn-outline" onclick="closeModal('scheduleModal')">Đóng</button>
            <button type="submit" class="btn">Thêm lịch</button>
          </div>
        </form>
      </div>
    </div>

    <script>
      const contextPath = "${pageContext.request.contextPath}";
      
      function filterTrainers() {
        const input = document.getElementById('searchInput');
        const filter = input.value.toLowerCase();
        const cards = document.querySelectorAll('.trainer-card');
        
        cards.forEach(card => {
          const name = card.getAttribute('data-name')?.toLowerCase() || '';
          const email = card.getAttribute('data-email')?.toLowerCase() || '';
          if (name.includes(filter) || email.includes(filter)) {
            card.style.display = '';
          } else {
            card.style.display = 'none';
          }
        });
      }
      
      function openAddTrainerModal() {
        document.getElementById('trainerModalTitle').textContent = 'Thêm PT mới';
        document.getElementById('trainerAction').value = 'add';
        document.getElementById('trainerId').value = '';
        document.getElementById('trainerForm').reset();
        document.getElementById('passwordRequired').style.display = '';
        document.getElementById('passwordInput').required = true;
        document.getElementById('passwordInput').minLength = 6;
        document.getElementById('trainerModal').style.display = 'flex';
      }
      
      function validateTrainerForm() {
        const form = document.getElementById('trainerForm');
        const action = document.getElementById('trainerAction').value;
        
        // Validate required fields
        const name = form.querySelector('input[name="name"]').value.trim();
        const email = form.querySelector('input[name="email"]').value.trim();
        const username = form.querySelector('input[name="username"]').value.trim();
        const phone = form.querySelector('input[name="phone"]').value.trim();
        const password = form.querySelector('input[name="password"]').value;
        const workAt = form.querySelector('select[name="workAt"]').value;
        
        if (!name) {
          alert('Vui lòng nhập họ và tên');
          return false;
        }
        
        if (!email) {
          alert('Vui lòng nhập email');
          return false;
        }
        
        // Validate email format
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
          alert('Email không hợp lệ');
          return false;
        }
        
        if (!username) {
          alert('Vui lòng nhập username');
          return false;
        }
        
        // Validate username format
        const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;
        if (!usernameRegex.test(username)) {
          alert('Username phải có 3-20 ký tự, chỉ chứa chữ, số và dấu gạch dưới');
          return false;
        }
        
        if (!phone) {
          alert('Vui lòng nhập số điện thoại');
          return false;
        }
        
        // Validate phone format (10-11 digits)
        const phoneRegex = /^[0-9]{10,11}$/;
        if (!phoneRegex.test(phone)) {
          alert('Số điện thoại phải có 10-11 chữ số');
          return false;
        }
        
        // Validate password for add action
        if (action === 'add') {
          if (!password) {
            alert('Vui lòng nhập mật khẩu');
            return false;
          }
          if (password.length < 6) {
            alert('Mật khẩu phải có ít nhất 6 ký tự');
            return false;
          }
        }
        
        if (!workAt) {
          alert('Vui lòng chọn cơ sở làm việc');
          return false;
        }
        
        return true;
      }
      
      function deleteSchedule(scheduleId) {
        if (confirm('Bạn có chắc chắn muốn xóa lịch này?')) {
          const form = document.createElement('form');
          form.method = 'POST';
          form.action = contextPath + '/admin/trainer-management';
          
          const actionInput = document.createElement('input');
          actionInput.type = 'hidden';
          actionInput.name = 'action';
          actionInput.value = 'deleteSchedule';
          form.appendChild(actionInput);
          
          const idInput = document.createElement('input');
          idInput.type = 'hidden';
          idInput.name = 'scheduleId';
          idInput.value = scheduleId;
          form.appendChild(idInput);
          
          document.body.appendChild(form);
          form.submit();
        }
      }
      
      function deleteTrainer(trainerId) {
        if (!trainerId) {
          console.error('deleteTrainer: trainerId is missing');
          alert('Lỗi: Không tìm thấy ID của PT');
          return;
        }
        
        if (confirm('Bạn có chắc chắn muốn xóa PT này? (PT sẽ được đánh dấu là INACTIVE)')) {
          // Đảm bảo contextPath được định nghĩa
          const basePath = contextPath || '';
          const actionUrl = basePath + '/admin/trainer-management';
          
          console.log('deleteTrainer: Submitting form to', actionUrl, 'with trainerId:', trainerId);
          
          const form = document.createElement('form');
          form.method = 'POST';
          form.action = actionUrl;
          
          const actionInput = document.createElement('input');
          actionInput.type = 'hidden';
          actionInput.name = 'action';
          actionInput.value = 'delete';
          form.appendChild(actionInput);
          
          const idInput = document.createElement('input');
          idInput.type = 'hidden';
          idInput.name = 'id';
          idInput.value = trainerId;
          form.appendChild(idInput);
          
          document.body.appendChild(form);
          form.submit();
        }
      }

      function activateTrainer(trainerId) {
        if (!trainerId) {
          console.error('activateTrainer: trainerId is missing');
          alert('Lỗi: Không tìm thấy ID của PT');
          return;
        }
        
        if (confirm('Bạn có chắc chắn muốn kích hoạt lại PT này? (PT sẽ được đánh dấu là ACTIVE)')) {
          // Đảm bảo contextPath được định nghĩa
          const basePath = contextPath || '';
          const actionUrl = basePath + '/admin/trainer-management';
          
          console.log('activateTrainer: Submitting form to', actionUrl, 'with trainerId:', trainerId);
          
          const form = document.createElement('form');
          form.method = 'POST';
          form.action = actionUrl;
          
          const actionInput = document.createElement('input');
          actionInput.type = 'hidden';
          actionInput.name = 'action';
          actionInput.value = 'activate';
          form.appendChild(actionInput);
          
          const idInput = document.createElement('input');
          idInput.type = 'hidden';
          idInput.name = 'id';
          idInput.value = trainerId;
          form.appendChild(idInput);
          
          document.body.appendChild(form);
          form.submit();
        }
      }
      
      
      function closeModal(modalId) {
        document.getElementById(modalId).style.display = 'none';
      }
      
      // Close modal when clicking outside
      window.onclick = function(event) {
        const modals = document.querySelectorAll('.modal');
        modals.forEach(modal => {
          if (event.target === modal) {
            modal.style.display = 'none';
          }
        });
      }
    </script>
  </body>
</html>
