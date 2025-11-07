<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Lịch huấn luyện - PT</title>
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
        --warning: #ffc107;
        --danger: #dc3545;
        --info: #17a2b8;
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
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .page-header-content h1 {
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

      .btn-success {
        background: var(--success);
      }

      .btn-danger {
        background: var(--danger);
      }

      .btn-info {
        background: var(--info);
      }

      .calendar-controls {
        background: var(--card);
        border-radius: 15px;
        padding: 25px;
        margin-bottom: 30px;
        box-shadow: 0 4px 20px var(--shadow);
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
        gap: 20px;
      }

      .calendar-nav {
        display: flex;
        align-items: center;
        gap: 20px;
      }

      .calendar-nav button {
        background: var(--primary);
        color: #fff;
        border: none;
        width: 40px;
        height: 40px;
        border-radius: 50%;
        cursor: pointer;
        transition: all 0.3s;
      }

      .calendar-nav button:hover {
        background: var(--accent);
        transform: scale(1.1);
      }

      .current-month {
        font-size: 1.5rem;
        font-weight: 700;
        color: var(--primary);
        min-width: 250px;
        text-align: center;
      }

      .view-options {
        display: flex;
        gap: 10px;
      }

      .view-btn {
        padding: 10px 20px;
        background: #e9ecef;
        border: 2px solid transparent;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s;
        font-weight: 600;
      }

      .view-btn.active {
        background: var(--accent);
        color: #fff;
        border-color: var(--accent);
      }

      .calendar-grid {
        background: var(--card);
        border-radius: 15px;
        padding: 30px;
        box-shadow: 0 8px 30px var(--shadow);
      }

      .calendar-header {
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        gap: 10px;
        margin-bottom: 15px;
      }

      .calendar-day-name {
        text-align: center;
        font-weight: 700;
        color: var(--primary);
        padding: 15px;
        background: #f8f9fa;
        border-radius: 8px;
      }

      .calendar-days {
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        gap: 10px;
      }

      .calendar-day {
        min-height: 120px;
        padding: 10px;
        border: 2px solid #e9ecef;
        border-radius: 10px;
        background: #fff;
        transition: all 0.3s;
        cursor: pointer;
      }

      .calendar-day:hover {
        border-color: var(--accent);
        box-shadow: 0 4px 15px var(--shadow);
        transform: translateY(-2px);
      }

      .calendar-day.other-month {
        background: #f8f9fa;
        opacity: 0.5;
      }

      .calendar-day.today {
        border-color: var(--accent);
        background: linear-gradient(
          135deg,
          rgba(236, 139, 90, 0.1),
          rgba(236, 139, 90, 0.05)
        );
      }

      .day-number {
        font-weight: 700;
        font-size: 1.1rem;
        color: var(--primary);
        margin-bottom: 8px;
      }

      .session-item {
        background: var(--accent);
        color: #fff;
        padding: 6px 8px;
        border-radius: 5px;
        font-size: 0.75rem;
        margin-bottom: 4px;
        cursor: pointer;
        transition: all 0.2s;
      }

      .session-item:hover {
        transform: scale(1.05);
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
      }

      .session-item.pending {
        background: var(--warning);
        color: #000;
      }

      .session-item.confirmed {
        background: #ff9800;
        color: #fff;
      }

      .session-item.completed {
        background: var(--success);
      }

      .session-item.cancelled {
        background: var(--danger);
      }

      .session-item.rejected {
        background: #f44336;
        color: #fff;
      }

      .calendar-day.highlight-user {
        border: 3px solid #141a49;
        box-shadow: 0 0 12px rgba(20, 26, 73, 0.6);
        background: linear-gradient(135deg, rgba(236, 139, 90, 0.15), rgba(236, 139, 90, 0.05));
        transform: scale(1.02);
      }

      .session-list {
        background: var(--card);
        border-radius: 15px;
        padding: 30px;
        box-shadow: 0 8px 30px var(--shadow);
      }

      .session-list h3 {
        color: var(--primary);
        margin-bottom: 20px;
        font-size: 1.5rem;
      }

      .session-card {
        background: #f8f9fa;
        border-left: 4px solid var(--accent);
        border-radius: 10px;
        padding: 20px;
        margin-bottom: 15px;
        transition: all 0.3s;
      }

      .session-card:hover {
        box-shadow: 0 4px 15px var(--shadow);
        transform: translateX(5px);
      }

      .session-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;
      }

      .session-title {
        font-weight: 700;
        font-size: 1.1rem;
        color: var(--primary);
      }

      .session-time {
        color: var(--accent);
        font-weight: 600;
      }

      .session-info {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 15px;
        margin: 15px 0;
      }

      .info-item {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 0.9rem;
        color: var(--text-light);
      }

      .info-item i {
        color: var(--accent);
      }

      .session-actions {
        display: flex;
        gap: 10px;
        margin-top: 15px;
      }

      .status-badge {
        padding: 5px 12px;
        border-radius: 20px;
        font-size: 0.8rem;
        font-weight: 600;
      }

      .status-badge.pending {
        background: #fff3cd;
        color: #856404;
      }

      .status-badge.confirmed {
        background: #ff9800;
        color: #fff;
      }

      .status-badge.completed {
        background: #d4edda;
        color: #155724;
      }

      .status-badge.cancelled {
        background: #f8d7da;
        color: #721c24;
      }

      .status-badge.rejected {
        background: #f44336;
        color: #fff;
      }

      .highlight-info {
        margin-bottom: 20px;
        padding: 12px 20px;
        background: linear-gradient(135deg, #141a49 0%, #1e2a5c 100%);
        color: #fff;
        border-radius: 10px;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 10px;
      }

      /* Modal */
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
        max-width: 600px;
        width: 90%;
        max-height: 90vh;
        overflow-y: auto;
      }

      .modal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 25px;
      }

      .modal-header h2 {
        color: var(--primary);
      }

      .close-btn {
        background: none;
        border: none;
        font-size: 2rem;
        cursor: pointer;
        color: var(--text-light);
      }

      .form-group {
        margin-bottom: 20px;
      }

      .form-group label {
        display: block;
        font-weight: 600;
        margin-bottom: 8px;
        color: var(--text);
      }

      .form-group input,
      .form-group select,
      .form-group textarea {
        width: 100%;
        padding: 12px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 1rem;
        transition: all 0.3s;
      }

      .form-group input:focus,
      .form-group select:focus,
      .form-group textarea:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 3px rgba(236, 139, 90, 0.1);
      }

      .error-message {
        background-color: #f8d7da;
        color: #721c24;
        padding: 12px 16px;
        border-radius: 8px;
        border: 1px solid #f5c6cb;
        margin-top: 15px;
        margin-bottom: 15px;
        display: flex;
        align-items: center;
        gap: 8px;
        font-weight: 500;
      }

      .error-message::before {
        content: '⚠️';
        font-size: 1.2rem;
      }

      .success-message {
        background-color: #d4edda;
        color: #155724;
        padding: 12px 16px;
        border-radius: 8px;
        border: 1px solid #c3e6cb;
        margin-top: 15px;
        margin-bottom: 15px;
        display: flex;
        align-items: center;
        gap: 8px;
        font-weight: 500;
      }

      .success-message::before {
        content: '✅';
        font-size: 1.2rem;
      }

      @media (max-width: 968px) {
        .calendar-days {
          grid-template-columns: 1fr;
        }

        .calendar-header {
          display: none;
        }

        .session-info {
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
        <div class="page-header-content">
          <h1><i class="fas fa-calendar-alt"></i> Lịch huấn luyện</h1>
          <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/views/PT/homePT.jsp"
              >Home</a
            >
            <span>/</span>
            <span>Lịch huấn luyện</span>
          </div>
        </div>
        <button class="btn" onclick="openModal()">
          <i class="fas fa-plus"></i> Tạo buổi tập mới
        </button>
      </div>

      <!-- Calendar Controls -->
      <div class="calendar-controls">
        <div class="calendar-nav">
          <button onclick="previousMonth()">
            <i class="fas fa-chevron-left"></i>
          </button>
          <div class="current-month" id="currentMonth">
            <c:choose>
              <c:when test="${not empty month}">
                Tháng ${month}, ${year}
              </c:when>
              <c:otherwise> Tháng 10, 2025 </c:otherwise>
            </c:choose>
          </div>
          <button onclick="nextMonth()">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
        <div class="view-options">
          <button class="view-btn active" onclick="switchView('calendar')">
            <i class="fas fa-calendar"></i> Lịch
          </button>
          <button class="view-btn" onclick="switchView('list')">
            <i class="fas fa-list"></i> Danh sách
          </button>
        </div>
      </div>

      <!-- Calendar View -->
      <div id="calendarView" class="calendar-grid">
        <c:if test="${not empty highlightStudentName}">
          <div class="highlight-info">
            <i class="fas fa-user-circle"></i>
            <span>Đang xem lịch của: <strong>${highlightStudentName}</strong></span>
          </div>
        </c:if>
        <div class="calendar-header">
          <div class="calendar-day-name">Thứ 2</div>
          <div class="calendar-day-name">Thứ 3</div>
          <div class="calendar-day-name">Thứ 4</div>
          <div class="calendar-day-name">Thứ 5</div>
          <div class="calendar-day-name">Thứ 6</div>
          <div class="calendar-day-name">Thứ 7</div>
          <div class="calendar-day-name">Chủ nhật</div>
        </div>
        <div class="calendar-days" id="calendarDays">
          <c:set var="daysInMonth" value="${daysInMonth}" />
          <c:set var="firstDayOffset" value="${firstDayOffset}" />
          <c:set var="lastCell" value="${firstDayOffset + daysInMonth}" />
          <!-- Tính số ô cần render: nếu tháng chỉ cần 5 hàng (<= 35) thì render 35 ô -->
          <!-- Nếu cần 6 hàng (> 35) nhưng hàng cuối trống (lastCell < 42), chỉ render đến ô cuối có ngày -->
          <!-- Nếu hàng cuối đầy đủ (lastCell = 42), render 42 ô -->
          <c:choose>
            <c:when test="${lastCell <= 35}">
              <c:set var="maxCellsToRender" value="35" />
            </c:when>
            <c:when test="${lastCell < 42}">
              <!-- Hàng cuối có ô trống, chỉ render đến ô cuối có ngày -->
              <c:set var="maxCellsToRender" value="${lastCell}" />
            </c:when>
            <c:otherwise>
              <!-- Hàng cuối đầy đủ -->
              <c:set var="maxCellsToRender" value="42" />
            </c:otherwise>
          </c:choose>

          <c:forEach var="cell" begin="1" end="${maxCellsToRender}">
            <c:choose>
              <c:when
                test="${cell <= firstDayOffset || cell > firstDayOffset + daysInMonth}"
              >
                <div class="calendar-day other-month"></div>
              </c:when>
              <c:otherwise>
                <c:set var="day" value="${cell - firstDayOffset}" />
                <c:set
                  var="dateKey"
                  value="${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}"
                />
                <div class="calendar-day">
                  <div class="day-number" data-date="${dateKey}">${day}</div>
                  <c:forEach var="item" items="${schedulesByDate[dateKey]}">
                    <div class="session-item ${item.status}">
                      ${item.startTime} - ${item.student.user.name}
                    </div>
                  </c:forEach>
                </div>
              </c:otherwise>
            </c:choose>
          </c:forEach>
        </div>
      </div>

      <!-- List View -->
      <div id="listView" class="session-list" style="display: none">
        <h3>Danh sách buổi tập</h3>

        <c:if test="${not empty successMessage}">
          <div id="success-msg" class="success-message">${successMessage}</div>
        </c:if>

        <c:forEach var="s" items="${schedules}">
          <div class="session-card">
            <div class="session-header">
              <div class="session-title">
                Buổi tập với ${s.student.user.name}
              </div>
              <span class="status-badge ${s.status}"> ${s.status} </span>
            </div>
            <div class="session-time">
              <i class="fas fa-clock"></i>
              ${s.trainingDate} - ${s.startTime} - ${s.endTime}
            </div>
            <div class="session-info">
              <div class="info-item">
                <i class="fas fa-user"></i>
                <span class="student-name-link" 
                      data-date="${s.trainingDate}"
                      data-student-name="${s.student.user.name}"
                      style="cursor: pointer; text-decoration: underline; color: var(--accent);">
                  ${s.student.user.name}
                </span>
              </div>
              <div class="info-item">
                <i class="fas fa-dumbbell"></i>
                <span>${s.trainingType}</span>
              </div>
              <div class="info-item">
                <i class="fas fa-map-marker-alt"></i>
                <span>${s.location}</span>
              </div>
            </div>
            <div class="session-actions">
              <c:choose>
                <c:when test="${s.status == 'pending'}">
                  <a href="${pageContext.request.contextPath}/ScheduleServlet?action=confirm&id=${s.id}" class="btn btn-success btn-sm">Xác nhận</a>
                  <a href="${pageContext.request.contextPath}/ScheduleServlet?action=reject&id=${s.id}" class="btn btn-danger btn-sm">Từ chối</a>
                </c:when>
                <c:when test="${s.status == 'confirmed'}">
                  <a href="${pageContext.request.contextPath}/ScheduleServlet?action=complete&id=${s.id}" class="btn btn-success btn-sm">Hoàn thành</a>
                  <a href="${pageContext.request.contextPath}/ScheduleServlet?action=cancel&id=${s.id}" class="btn btn-danger btn-sm">Hủy</a>
                </c:when>
              </c:choose>
              <a href="${pageContext.request.contextPath}/ScheduleServlet?action=edit&id=${s.id}&sourceView=list" class="btn btn-info btn-sm"><i class="fas fa-edit"></i> Chỉnh sửa</a>
              <a href="${pageContext.request.contextPath}/ScheduleServlet?action=delete&id=${s.id}" class="btn btn-danger btn-sm"><i class="fas fa-trash"></i> Xóa</a>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>

    <!-- Modal tạo/chỉnh sửa buổi tập -->
    <div id="sessionModal" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>
            <i class="fas ${not empty editSchedule ? 'fa-edit' : 'fa-plus-circle'}"></i>
            ${not empty editSchedule ? 'Chỉnh sửa buổi tập' : 'Tạo buổi tập mới'}
          </h2>
          <button class="close-btn" onclick="closeModal()">&times;</button>
        </div>
        <form
          method="post"
          action="${pageContext.request.contextPath}/ScheduleServlet?action=${not empty editSchedule ? 'update' : 'create'}"
        >
          <c:if test="${not empty editSchedule}">
            <input type="hidden" name="scheduleId" value="${editSchedule.id}" />
          </c:if>
          <c:if test="${not empty sourceView}">
            <input type="hidden" name="sourceView" value="${sourceView}" />
          </c:if>
          <c:if test="${empty sourceView && not empty editSchedule}">
            <input type="hidden" name="sourceView" value="list" />
          </c:if>
          <div class="form-group">
            <label>Học viên</label>
            <input
              type="text"
              name="studentUsername"
              id="studentUsername"
              placeholder="Nhập tên học viên"
              class="form-control"
              value="${not empty editSchedule ? editSchedule.student.user.name : ''}"
              required
            />
          </div>
          <div class="form-group">
            <label>Ngày tập</label>
            <input 
              type="date" 
              name="trainingDate" 
              value="${not empty editSchedule ? editSchedule.trainingDate : ''}"
              required 
            />
          </div>
          <div class="form-group">
            <label>Giờ bắt đầu</label>
            <input 
              type="time" 
              name="startTime" 
              value="${not empty editSchedule ? editSchedule.startTime : ''}"
              required 
            />
          </div>
          <div class="form-group">
            <label>Giờ kết thúc</label>
            <input 
              type="time" 
              name="endTime" 
              value="${not empty editSchedule ? editSchedule.endTime : ''}"
              required 
            />
          </div>
          <div class="form-group">
            <label>Loại tập</label>
            <select name="trainingType" required>
              <option value="">Chọn loại tập</option>
              <option value="Cardio" ${not empty editSchedule && editSchedule.trainingType == 'Cardio' ? 'selected' : ''}>Cardio</option>
              <option value="Strength Training" ${not empty editSchedule && editSchedule.trainingType == 'Strength Training' ? 'selected' : ''}>Strength Training</option>
              <option value="Yoga" ${not empty editSchedule && editSchedule.trainingType == 'Yoga' ? 'selected' : ''}>Yoga</option>
              <option value="Weight Loss" ${not empty editSchedule && editSchedule.trainingType == 'Weight Loss' ? 'selected' : ''}>Weight Loss</option>
              <option value="Bodybuilding" ${not empty editSchedule && editSchedule.trainingType == 'Bodybuilding' ? 'selected' : ''}>Bodybuilding</option>
            </select>
          </div>
          <div class="form-group">
            <label>Địa điểm</label>
            <input
              type="text"
              name="location"
              placeholder="Nhập phòng tập"
              value="${not empty editSchedule ? editSchedule.location : ''}"
              required
            />
          </div>
          <div class="form-group">
            <label>Ghi chú</label>
            <textarea
              rows="3"
              placeholder="Nhập ghi chú (không bắt buộc)"
              name="note"
            >${not empty editSchedule ? editSchedule.note : ''}</textarea>
          </div>
          <c:if test="${not empty errorMessage}">
            <div id="error-msg" class="error-message">${errorMessage}</div>
          </c:if>
          <div style="display: flex; gap: 10px; justify-content: flex-end">
            <button
              type="button"
              class="btn"
              style="background: #6c757d"
              onclick="closeModal()"
            >
              Hủy
            </button>
            <button type="submit" class="btn">
              <i class="fas fa-save"></i> ${not empty editSchedule ? 'Lưu' : 'Tạo buổi tập'}
            </button>
          </div>
        </form>
      </div>
    </div>

    <script>
      <c:set var="currentYear" value="${not empty year ? year : 2025}" />
      <c:set var="currentMonth" value="${not empty month ? month : 10}" />
      const currentYear = ${currentYear};
      const currentMonth = ${currentMonth};

      function switchView(view) {
        const calendarView = document.getElementById('calendarView');
        const listView = document.getElementById('listView');
        const viewBtns = document.querySelectorAll('.view-btn');

        viewBtns.forEach((btn) => btn.classList.remove('active'));

        if (view === 'calendar') {
          calendarView.style.display = 'block';
          listView.style.display = 'none';
          viewBtns[0].classList.add('active');
        } else {
          calendarView.style.display = 'none';
          listView.style.display = 'block';
          viewBtns[1].classList.add('active');
        }
      }

      // Tự động switch sang tab list nếu có success message
      <c:if test="${not empty defaultView && defaultView == 'list'}">
      switchView('list');
      </c:if>

      function previousMonth() {
        let newYear = currentYear;
        let newMonth = currentMonth - 1;
        if (newMonth < 1) {
          newMonth = 12;
          newYear--;
        }
        window.location.href = '${pageContext.request.contextPath}/ScheduleServlet?action=calendar&year=' + newYear + '&month=' + newMonth;
      }

      function nextMonth() {
        let newYear = currentYear;
        let newMonth = currentMonth + 1;
        if (newMonth > 12) {
          newMonth = 1;
          newYear++;
        }
        window.location.href = '${pageContext.request.contextPath}/ScheduleServlet?action=calendar&year=' + newYear + '&month=' + newMonth;
      }

      function openModal() {
        document.getElementById('sessionModal').classList.add('active');
      }

      function closeModal() {
        document.getElementById('sessionModal').classList.remove('active');
      }

      window.addEventListener('DOMContentLoaded', () => {
        const error = document.getElementById('error-msg');
        const success = document.getElementById('success-msg');
        const studentInput = document.getElementById('studentUsername');

        // Mở modal nếu có editSchedule hoặc có lỗi
        <c:if test="${not empty editSchedule || not empty openEditModal}">
        document.getElementById('sessionModal').classList.add('active');
        </c:if>

        if (error && error.innerText.trim() !== '') {
          // Mở modal nếu có lỗi
          document.getElementById('sessionModal').classList.add('active');

          // Scroll đến error message
          setTimeout(() => {
            error.scrollIntoView({ behavior: 'smooth', block: 'center' });
          }, 100);

          // Focus vào ô nhập tên học viên
          setTimeout(() => {
            if (studentInput) {
              studentInput.focus();
              studentInput.select(); // Select text nếu có để dễ sửa
            }
          }, 200);

          // Tự động ẩn error message sau 5 giây
          setTimeout(() => {
            error.style.display = 'none';
          }, 5000);
        }

        // Tự động ẩn success message sau 5 giây
        if (success && success.innerText.trim() !== '') {
          setTimeout(() => {
            success.style.display = 'none';
          }, 5000);
        }
        
        // Highlight date after redirect
        const params = new URLSearchParams(window.location.search);
        const highlightDate = params.get('highlightDate');
        if (highlightDate) {
          const dayEls = document.querySelectorAll('[data-date]');
          dayEls.forEach(el => {
            if (el.getAttribute('data-date') === highlightDate) {
              el.parentElement.classList.add('highlight-user');
              el.parentElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
          });
        }

        // Click vào tên học viên trong danh sách để chuyển đến lịch
        document.querySelectorAll('.student-name-link').forEach(link => {
          link.addEventListener('click', function() {
            const date = this.getAttribute('data-date');
            const studentName = this.getAttribute('data-student-name');
            const [year, month, day] = date.split('-');
            window.location.href = '${pageContext.request.contextPath}/ScheduleServlet?action=calendar&year=' + year + '&month=' + parseInt(month) + '&highlightDate=' + date + '&highlightStudentName=' + encodeURIComponent(studentName);
          });
        });
      });
    </script>
  </body>
</html>
