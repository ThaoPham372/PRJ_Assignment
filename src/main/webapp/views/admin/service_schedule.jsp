<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý dịch vụ & Lịch tập - GymFit</title>

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

      /* Reuse sidebar from other pages */
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

      /* Tabs */
      .tabs {
        display: flex;
        gap: 5px;
        background: #fff;
        padding: 10px;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
        margin-bottom: 25px;
      }

      .tab {
        flex: 1;
        padding: 12px 20px;
        text-align: center;
        background: transparent;
        border: none;
        border-radius: 8px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        color: var(--text);
      }

      .tab.active {
        background: var(--gradient-accent);
        color: #fff;
      }

      .tab-content {
        display: none;
      }

      .tab-content.active {
        display: block;
      }

      /* Calendar */
      .calendar-container {
        background: #fff;
        padding: 25px;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
      }

      .calendar-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
      }

      .calendar-header h3 {
        font-size: 1.3rem;
        color: var(--primary);
      }

      .calendar-nav {
        display: flex;
        gap: 10px;
      }

      .calendar-grid {
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        gap: 10px;
      }

      .calendar-day {
        text-align: center;
        padding: 10px;
        border-radius: 8px;
        background: #f8f9fa;
        font-weight: 600;
        color: var(--text);
      }

      .calendar-cell {
        min-height: 100px;
        padding: 10px;
        border: 1px solid #e0e0e0;
        border-radius: 8px;
        background: #fff;
      }

      .calendar-date {
        font-weight: 600;
        margin-bottom: 5px;
        color: var(--primary);
      }

      .calendar-event {
        background: var(--gradient-accent);
        color: #fff;
        padding: 5px;
        margin: 3px 0;
        border-radius: 5px;
        font-size: 0.75rem;
        cursor: pointer;
      }

      .calendar-event.confirmed {
        background: linear-gradient(135deg, #27ae60 0%, #229954 100%);
        color: #fff;
      }

      .calendar-event.completed {
        background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
        color: #fff;
      }

      /* Booking List */
      .booking-list {
        background: #fff;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
      }

      .booking-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 20px;
        border-bottom: 1px solid #e0e0e0;
      }

      .booking-item:last-child {
        border-bottom: none;
      }

      .booking-info h4 {
        font-size: 1.1rem;
        color: var(--primary);
        margin-bottom: 5px;
      }

      .booking-info p {
        font-size: 0.9rem;
        color: var(--text);
      }

      .booking-actions {
        display: flex;
        gap: 10px;
      }

      .btn-small {
        padding: 8px 16px;
        font-size: 0.85rem;
      }

      /* Custom Modal */
      .custom-modal {
        display: none;
        position: fixed;
        z-index: 1000;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        animation: fadeIn 0.3s ease;
      }

      .custom-modal.show {
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .custom-modal-content {
        background-color: #fff;
        border-radius: 12px;
        padding: 0;
        max-width: 450px;
        width: 90%;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
        animation: slideDown 0.3s ease;
        overflow: hidden;
      }

      .custom-modal-header {
        padding: 20px 25px;
        background: var(--gradient-primary);
        color: #fff;
        display: flex;
        align-items: center;
        gap: 12px;
      }

      .custom-modal-header i {
        font-size: 1.5rem;
      }

      .custom-modal-header h3 {
        margin: 0;
        font-size: 1.3rem;
        font-weight: 600;
      }

      .custom-modal-body {
        padding: 25px;
        color: var(--text);
        font-size: 1rem;
        line-height: 1.6;
      }

      .custom-modal-footer {
        padding: 15px 25px;
        background: #f8f9fa;
        display: flex;
        justify-content: flex-end;
        gap: 10px;
        border-top: 1px solid #e0e0e0;
      }

      .modal-btn {
        padding: 10px 20px;
        border: none;
        border-radius: 6px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        font-size: 0.95rem;
      }

      .modal-btn-cancel {
        background: #e0e0e0;
        color: #333;
      }

      .modal-btn-cancel:hover {
        background: #d0d0d0;
      }

      .modal-btn-confirm {
        background: var(--gradient-accent);
        color: #fff;
      }

      .modal-btn-confirm:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(236, 139, 94, 0.4);
      }

      .modal-btn-danger {
        background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
        color: #fff;
      }

      .modal-btn-danger:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(231, 76, 60, 0.4);
      }

      @keyframes fadeIn {
        from {
          opacity: 0;
        }
        to {
          opacity: 1;
        }
      }

      @keyframes slideDown {
        from {
          transform: translateY(-50px);
          opacity: 0;
        }
        to {
          transform: translateY(0);
          opacity: 1;
        }
      }

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
        .calendar-grid {
          grid-template-columns: 1fr;
        }
        .custom-modal-content {
          width: 95%;
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
              class="sidebar-menu-link active"
            >
              <i class="fas fa-calendar-alt"></i><span>Dịch vụ & Lịch tập</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/trainer-management"
              class="sidebar-menu-link"
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
            <i class="fas fa-calendar-alt"></i> Quản lý dịch vụ & Lịch tập
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
          <!-- Success message -->
          <c:if test="${param.success == 'confirmed'}">
            <div id="success-message" style="background: #d4edda; color: #155724; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #c3e6cb; transition: opacity 0.5s ease-out;">
              <i class="fas fa-check-circle"></i> Đã xác nhận booking thành công!
            </div>
          </c:if>
          <c:if test="${param.success == 'cancelled'}">
            <div id="success-message" style="background: #f8d7da; color: #721c24; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #f5c6cb; transition: opacity 0.5s ease-out;">
              <i class="fas fa-times-circle"></i> Đã hủy booking thành công!
            </div>
          </c:if>
          
          <!-- Tabs -->
          <div class="tabs">
            <button class="tab active" onclick="switchTab('booking')">
              <i class="fas fa-calendar-check"></i> Quản lý Booking
            </button>
            <button class="tab" onclick="switchTab('schedule')">
              <i class="fas fa-calendar"></i> Lịch tập
            </button>
          </div>

          <!-- Booking Tab -->
          <div id="booking" class="tab-content active">
            <div class="booking-list">
              <c:choose>
                <c:when test="${not empty allBookings}">
                  <c:forEach var="booking" items="${allBookings}">
                    <div class="booking-item">
                      <div class="booking-info">
                        <h4>
                          Personal Training - 
                          <c:choose>
                            <c:when test="${not empty booking.member}">
                              ${booking.member.name != null ? booking.member.name : booking.member.username}
                            </c:when>
                            <c:otherwise>N/A</c:otherwise>
                          </c:choose>
                        </h4>
                        <p>
                          <i class="fas fa-calendar"></i> 
                          ${booking.bookingDate} 
                          <c:if test="${not empty booking.timeSlot}">
                            - ${booking.timeSlot.startTime} - ${booking.timeSlot.endTime}
                          </c:if>
                          | 
                          <i class="fas fa-user-tie"></i> PT: 
                          <c:choose>
                            <c:when test="${not empty booking.trainer}">
                              ${booking.trainer.name != null ? booking.trainer.name : booking.trainer.username}
                            </c:when>
                            <c:otherwise>N/A</c:otherwise>
                          </c:choose>
                        </p>
                        <p>
                          <c:choose>
                            <c:when test="${booking.bookingStatus.name() == 'CONFIRMED'}">
                              <i class="fas fa-check-circle" style="color: #27ae60"></i>
                              ${booking.bookingStatus.displayName}
                            </c:when>
                            <c:when test="${booking.bookingStatus.name() == 'PENDING'}">
                              <i class="fas fa-clock" style="color: #f39c12"></i>
                              ${booking.bookingStatus.displayName}
                            </c:when>
                            <c:when test="${booking.bookingStatus.name() == 'COMPLETED'}">
                              <i class="fas fa-check-double" style="color: #3498db"></i>
                              ${booking.bookingStatus.displayName}
                            </c:when>
                            <c:when test="${booking.bookingStatus.name() == 'CANCELLED'}">
                              <i class="fas fa-times-circle" style="color: #e74c3c"></i>
                              ${booking.bookingStatus.displayName}
                            </c:when>
                            <c:otherwise>
                              <i class="fas fa-info-circle" style="color: #95a5a6"></i>
                              ${booking.bookingStatus != null ? booking.bookingStatus.displayName : 'N/A'}
                            </c:otherwise>
                          </c:choose>
                        </p>
                        <c:if test="${not empty booking.notes}">
                          <p style="font-size: 0.85rem; color: #7f8c8d; margin-top: 5px;">
                            <i class="fas fa-sticky-note"></i> ${booking.notes}
                          </p>
                        </c:if>
                      </div>
                      <div class="booking-actions">
                        <c:if test="${booking.bookingStatus.name() == 'PENDING'}">
                          <button class="btn btn-small" style="background: #27ae60" onclick="confirmBooking(${booking.bookingId})">
                            <i class="fas fa-check"></i> Xác nhận
                          </button>
                        </c:if>
                        <c:if test="${booking.bookingStatus.name() != 'CANCELLED' && booking.bookingStatus.name() != 'COMPLETED'}">
                          <button class="btn btn-small" style="background: #e74c3c" onclick="cancelBooking(${booking.bookingId})">
                            <i class="fas fa-times"></i> Hủy
                          </button>
                        </c:if>
                      </div>
                    </div>
                  </c:forEach>
                </c:when>
                <c:otherwise>
                  <div class="booking-item" style="text-align: center; padding: 40px;">
                    <p style="color: #95a5a6; font-size: 1.1rem;">
                      <i class="fas fa-calendar-times" style="font-size: 2rem; margin-bottom: 10px; display: block;"></i>
                      Chưa có booking nào
                    </p>
                  </div>
                </c:otherwise>
              </c:choose>
            </div>
          </div>

          <!-- Schedule Tab -->
          <div id="schedule" class="tab-content">
            <div class="calendar-container">
              <div class="calendar-header">
                <h3 id="calendar-month-year">Tháng 2, 2025</h3>
                <div class="calendar-nav">
                  <button class="btn btn-small btn-outline" onclick="changeMonth(-1)">
                    <i class="fas fa-chevron-left"></i>
                  </button>
                  <button class="btn btn-small btn-outline" onclick="changeMonth(1)">
                    <i class="fas fa-chevron-right"></i>
                  </button>
                </div>
              </div>

              <!-- Legend -->
              <div style="display: flex; gap: 20px; margin-bottom: 20px; padding: 15px; background: #f8f9fa; border-radius: 8px; flex-wrap: wrap;">
                <div style="display: flex; align-items: center; gap: 8px;">
                  <div class="calendar-event confirmed" style="width: 20px; height: 20px; margin: 0;"></div>
                  <span style="font-size: 0.9rem; color: var(--text); font-weight: 500;">Đã xác nhận (CONFIRMED)</span>
                </div>
                <div style="display: flex; align-items: center; gap: 8px;">
                  <div class="calendar-event completed" style="width: 20px; height: 20px; margin: 0;"></div>
                  <span style="font-size: 0.9rem; color: var(--text); font-weight: 500;">Hoàn thành (COMPLETED)</span>
                </div>
              </div>

              <div class="calendar-grid" id="calendar-grid">
                <div class="calendar-day">CN</div>
                <div class="calendar-day">T2</div>
                <div class="calendar-day">T3</div>
                <div class="calendar-day">T4</div>
                <div class="calendar-day">T5</div>
                <div class="calendar-day">T6</div>
                <div class="calendar-day">T7</div>
                <!-- Calendar cells will be generated by JavaScript -->
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>

    <!-- Custom Modal for Confirmations -->
    <div id="customModal" class="custom-modal">
      <div class="custom-modal-content">
        <div class="custom-modal-header" id="modalHeader">
          <i class="fas fa-question-circle" id="modalIcon"></i>
          <h3 id="modalTitle">Xác nhận</h3>
        </div>
        <div class="custom-modal-body">
          <p id="modalMessage">Bạn có chắc chắn muốn thực hiện hành động này?</p>
        </div>
        <div class="custom-modal-footer">
          <button class="modal-btn modal-btn-cancel" onclick="closeCustomModal()">Hủy</button>
          <button class="modal-btn" id="modalConfirmBtn" onclick="executeModalAction()">Xác nhận</button>
        </div>
      </div>
    </div>

    <script>
      function switchTab(tabName) {
        // Hide all tabs
        const tabs = document.querySelectorAll('.tab-content');
        tabs.forEach((tab) => tab.classList.remove('active'));

        // Remove active from all tab buttons
        const tabButtons = document.querySelectorAll('.tab');
        tabButtons.forEach((btn) => btn.classList.remove('active'));

        // Show selected tab
        document.getElementById(tabName).classList.add('active');

        // Activate button
        event.target.closest('.tab').classList.add('active');
        
        // Render calendar if schedule tab is selected
        if (tabName === 'schedule') {
          setTimeout(function() {
            if (typeof renderCalendar === 'function') {
              renderCalendar();
            }
          }, 100);
        }
      }

      // Modal state
      let currentModalAction = null;
      let currentBookingId = null;

      function showCustomModal(title, message, icon, confirmText, confirmClass, action) {
        document.getElementById('modalTitle').textContent = title;
        document.getElementById('modalMessage').textContent = message;
        document.getElementById('modalIcon').className = icon;
        document.getElementById('modalConfirmBtn').textContent = confirmText;
        document.getElementById('modalConfirmBtn').className = 'modal-btn ' + confirmClass;
        currentModalAction = action;
        
        const modal = document.getElementById('customModal');
        modal.classList.add('show');
      }

      function closeCustomModal() {
        const modal = document.getElementById('customModal');
        modal.classList.remove('show');
        currentModalAction = null;
        currentBookingId = null;
      }

      function executeModalAction() {
        if (currentModalAction === 'confirm') {
          submitBookingAction('confirm', currentBookingId);
        } else if (currentModalAction === 'cancel') {
          submitBookingAction('cancel', currentBookingId);
        }
        closeCustomModal();
      }

      function submitBookingAction(action, bookingId) {
        // Create form and submit POST request
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '${pageContext.request.contextPath}/admin/service-schedule';
        
        const actionInput = document.createElement('input');
        actionInput.type = 'hidden';
        actionInput.name = 'action';
        actionInput.value = action;
        form.appendChild(actionInput);
        
        const bookingIdInput = document.createElement('input');
        bookingIdInput.type = 'hidden';
        bookingIdInput.name = 'bookingId';
        bookingIdInput.value = bookingId;
        form.appendChild(bookingIdInput);
        
        if (action === 'cancel') {
          const reasonInput = document.createElement('input');
          reasonInput.type = 'hidden';
          reasonInput.name = 'reason';
          reasonInput.value = 'Cancelled by admin';
          form.appendChild(reasonInput);
        }
        
        document.body.appendChild(form);
        form.submit();
      }

      function confirmBooking(bookingId) {
        currentBookingId = bookingId;
        showCustomModal(
          'Xác nhận Booking',
          'Bạn có chắc chắn muốn xác nhận booking này?',
          'fas fa-check-circle',
          'Xác nhận',
          'modal-btn-confirm',
          'confirm'
        );
      }

      function cancelBooking(bookingId) {
        currentBookingId = bookingId;
        showCustomModal(
          'Hủy Booking',
          'Bạn có chắc chắn muốn hủy booking này? Hành động này không thể hoàn tác.',
          'fas fa-exclamation-triangle',
          'Hủy Booking',
          'modal-btn-danger',
          'cancel'
        );
      }

      // Close modal when clicking outside
      document.addEventListener('click', function(event) {
        const modal = document.getElementById('customModal');
        if (event.target === modal) {
          closeCustomModal();
        }
      });

      // Close modal with Escape key
      document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
          closeCustomModal();
        }
      });

      // Calendar functionality
      let currentDate = new Date();
      
      // Booking data from server - filter only CONFIRMED and COMPLETED
      const allBookingsData = [
        <c:forEach var="booking" items="${allBookings}" varStatus="loop">
        {
          id: ${booking.bookingId},
          date: '${booking.bookingDate}',
          startTime: '${booking.timeSlot != null ? booking.timeSlot.startTime : ""}',
          endTime: '${booking.timeSlot != null ? booking.timeSlot.endTime : ""}',
          memberName: '${booking.member != null ? (booking.member.name != null ? fn:escapeXml(booking.member.name) : fn:escapeXml(booking.member.username)) : "N/A"}',
          trainerName: '${booking.trainer != null ? (booking.trainer.name != null ? fn:escapeXml(booking.trainer.name) : fn:escapeXml(booking.trainer.username)) : "N/A"}',
          status: '${booking.bookingStatus != null ? booking.bookingStatus.name() : "PENDING"}'
        }<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
      ];
      
      // Filter bookings to show only CONFIRMED and COMPLETED in calendar
      const bookingsData = allBookingsData.filter(b => b.status === 'CONFIRMED' || b.status === 'COMPLETED');

      function renderCalendar() {
        const year = currentDate.getFullYear();
        const month = currentDate.getMonth();
        
        // Update month/year display
        const monthNames = ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6',
                           'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'];
        document.getElementById('calendar-month-year').textContent = monthNames[month] + ', ' + year;
        
        // Get first day of month and number of days
        const firstDay = new Date(year, month, 1).getDay();
        const daysInMonth = new Date(year, month + 1, 0).getDate();
        
        // Get calendar grid
        const grid = document.getElementById('calendar-grid');
        
        // Clear existing cells (keep day headers)
        const dayHeaders = grid.querySelectorAll('.calendar-day');
        grid.innerHTML = '';
        dayHeaders.forEach(header => grid.appendChild(header));
        
        // Add empty cells for days before month starts
        for (let i = 0; i < firstDay; i++) {
          const cell = document.createElement('div');
          cell.className = 'calendar-cell';
          grid.appendChild(cell);
        }
        
        // Add cells for each day of the month
        for (let day = 1; day <= daysInMonth; day++) {
          const cell = document.createElement('div');
          cell.className = 'calendar-cell';
          
          const dateDiv = document.createElement('div');
          dateDiv.className = 'calendar-date';
          dateDiv.textContent = day;
          cell.appendChild(dateDiv);
          
          // Find bookings for this date (only CONFIRMED and COMPLETED)
          const dateStr = year + '-' + String(month + 1).padStart(2, '0') + '-' + String(day).padStart(2, '0');
          const dayBookings = bookingsData.filter(b => b.date === dateStr);
          
          // Add events for this day
          dayBookings.forEach(booking => {
            const event = document.createElement('div');
            // Add class based on status
            if (booking.status === 'CONFIRMED') {
              event.className = 'calendar-event confirmed';
            } else if (booking.status === 'COMPLETED') {
              event.className = 'calendar-event completed';
            } else {
              event.className = 'calendar-event';
            }
            event.title = booking.memberName + ' - ' + booking.trainerName + ' (' + booking.startTime + ') - ' + booking.status;
            event.textContent = (booking.startTime ? booking.startTime.substring(0, 5) : '') + ' - ' + booking.memberName;
            cell.appendChild(event);
          });
          
          grid.appendChild(cell);
        }
      }
      
      function changeMonth(direction) {
        currentDate.setMonth(currentDate.getMonth() + direction);
        renderCalendar();
      }
      
      // Initialize calendar on page load
      document.addEventListener('DOMContentLoaded', function() {
        // Render calendar if schedule tab is active
        if (document.getElementById('schedule').classList.contains('active')) {
          renderCalendar();
        }
        
        // Auto-hide success message after 3 seconds
        const successMessage = document.getElementById('success-message');
        if (successMessage) {
          setTimeout(function() {
            successMessage.style.opacity = '0';
            setTimeout(function() {
              successMessage.style.display = 'none';
            }, 500); // Wait for fade out animation to complete
          }, 3000); // Hide after 3 seconds
        }
      });
    </script>
  </body>
</html>
