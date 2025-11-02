<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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

      .calendar-nav button,
      .calendar-nav a.calendar-nav-btn {
        background: var(--primary);
        color: #fff;
        border: none;
        width: 40px;
        height: 40px;
        border-radius: 50%;
        cursor: pointer;
        transition: all 0.3s;
        display: flex;
        align-items: center;
        justify-content: center;
        text-decoration: none;
      }

      .calendar-nav button:hover,
      .calendar-nav a.calendar-nav-btn:hover {
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

      .session-item.completed {
        background: var(--success);
      }

      .session-item.cancelled {
        background: var(--danger);
      }

      /* Monthly calendar schedule chips */
      .schedule-chip { color: #fff; padding: 6px 8px; border-radius: 5px; font-size: 0.75rem; margin-bottom: 4px; display: inline-block; }
      .schedule-pending { background-color: #fbc02d; color: #000; }
      .schedule-confirmed { background-color: #ff9800; }
      .schedule-completed { background-color: #4caf50; }
      .schedule-cancelled { background-color: #f44336; }
      
      /* Highlight user's sessions on calendar */
      .schedule-chip.highlight-user {
        border: 3px solid #141a49;
        box-shadow: 0 0 8px rgba(20, 26, 73, 0.6);
        font-weight: 700;
        transform: scale(1.05);
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
        background: #d1ecf1;
        color: #0c5460;
      }

      .status-badge.completed {
        background: #d4edda;
        color: #155724;
      }

      .status-badge.cancelled {
        background: #f8d7da;
        color: #721c24;
      }

      /* Unified status styles for list view */
      .status-confirmed {
        background-color: #ff9800;
        color: #ffffff;
        border-radius: 10px;
        padding: 5px 10px;
        font-size: 0.8rem;
        font-weight: 600;
      }
      .status-rejected {
        background-color: #f44336;
        color: #ffffff;
        border-radius: 10px;
        padding: 5px 10px;
        font-size: 0.8rem;
        font-weight: 600;
      }

      /* Inline error message below submit button */
      .error-message {
        margin-top: 8px;
        color: #dc3545;
        font-size: 0.9rem;
        font-weight: 600;
        text-align: left;
        opacity: 1;
        transition: opacity 0.3s ease;
      }
      .error-message.fade-out { opacity: 0; }

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
    <!-- Optional header include removed to avoid missing file error -->

    <div class="container">
      <c:if test="${not empty error}">
        <div style="margin-bottom:16px;padding:12px 16px;border-radius:8px;background:#f8d7da;color:#721c24;">
          <i class="fas fa-exclamation-triangle"></i>
          ${error}
        </div>
      </c:if>
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
          <c:set var="navParams" value="&month=${prev_month}&year=${prev_year}"/>
          <c:if test="${not empty defaultView}">
            <c:set var="navParams" value="${navParams}&view=${defaultView}"/>
          </c:if>
          <c:if test="${not empty highlightUserId}">
            <c:set var="navParams" value="${navParams}&user=${highlightUserId}"/>
          </c:if>
          <a href="ScheduleServlet?action=list${navParams}" class="calendar-nav-btn">
            <i class="fas fa-chevron-left"></i>
          </a>
          <div class="current-month" id="currentMonth">
            Tháng ${calendar_month}, ${calendar_year}
          </div>
          <c:set var="navParamsNext" value="&month=${next_month}&year=${next_year}"/>
          <c:if test="${not empty defaultView}">
            <c:set var="navParamsNext" value="${navParamsNext}&view=${defaultView}"/>
          </c:if>
          <c:if test="${not empty highlightUserId}">
            <c:set var="navParamsNext" value="${navParamsNext}&user=${highlightUserId}"/>
          </c:if>
          <a href="ScheduleServlet?action=list${navParamsNext}" class="calendar-nav-btn">
            <i class="fas fa-chevron-right"></i>
          </a>
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

     <!-- Calendar View (rendered from DB via JS) -->
     <div id="calendarView" class="calendar-grid">
       <c:if test="${not empty highlightUserName}">
         <div style="margin-bottom: 20px; padding: 12px 20px; background: linear-gradient(135deg, #141a49 0%, #1e2a5c 100%); color: #fff; border-radius: 10px; font-weight: 600; display: flex; align-items: center; gap: 10px;">
           <i class="fas fa-user-circle"></i>
           <span>Đang xem lịch của học viên: <strong>${highlightUserName}</strong></span>
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
         <!-- Leading blank cells to align the first day (Mon=1..Sun=7) -->
         <c:if test="${not empty calendar_firstIso and calendar_firstIso gt 1}">
           <c:forEach var="i" begin="1" end="${calendar_firstIso - 1}">
             <div class="calendar-day other-month"></div>
           </c:forEach>
         </c:if>
         <!-- Real days -->
         <c:forEach var="d" begin="1" end="${calendar_days}">
           <c:set var="monthStr" value="${calendar_month}"/>
           <c:if test="${calendar_month lt 10}"><c:set var="monthStr" value="0${calendar_month}"/></c:if>
           <c:set var="dayStr" value="${d}"/>
           <c:if test="${d lt 10}"><c:set var="dayStr" value="0${d}"/></c:if>
           <c:set var="dateKey" value="${calendar_year}-${monthStr}-${dayStr}" />
           <div class="calendar-day">
             <div class="day-number">${d}</div>
             <c:forEach var="item" items="${scheduleMap[dateKey]}">
               <c:set var="chipClass" value="schedule-chip schedule-${item.status}"/>
               <c:if test="${not empty highlightUserId and item.userId == highlightUserId}">
                 <c:set var="chipClass" value="${chipClass} highlight-user"/>
               </c:if>
               <div class="${chipClass}">${item.time} - ${item.userName}</div>
             </c:forEach>
           </div>
         </c:forEach>
       </div>
     </div>

      <!-- List View -->
       <div id="listView" class="session-list" style="display: none">
         <h3>Danh sách buổi tập</h3>
         <c:if test="${empty schedules}"><i>Không có buổi tập nào.</i></c:if>
         <c:forEach var="s" items="${schedules}">
           <div class="session-card">
             <div class="session-header">
               <div class="session-title">Buổi tập với 
                 <b>
                   <c:forEach var="u" items="${users}"><c:if test="${u.id == s.userId}">${u.username}</c:if></c:forEach>
                 </b>
               </div>
               <c:choose>
                 <c:when test="${s.status == 'confirmed'}">
                   <span class="status-confirmed">Đã xác nhận</span>
                 </c:when>
                 <c:when test="${s.status == 'rejected'}">
                   <span class="status-rejected">Từ chối</span>
                 </c:when>
                 <c:otherwise>
                   <span class="status-badge ${s.status}">
                     <c:choose>
                       <c:when test="${s.status == 'pending'}">Chờ xác nhận</c:when>
                       <c:when test="${s.status == 'completed'}">Hoàn thành</c:when>
                       <c:when test="${s.status == 'cancelled'}">Đã hủy</c:when>
                       <c:otherwise>${s.status}</c:otherwise>
                     </c:choose>
                   </span>
                 </c:otherwise>
               </c:choose>
             </div>
             <div class="session-time">
               <i class="fas fa-clock"></i> ${s.trainingDate} - ${s.startTime} ~ ${s.endTime}
             </div>
             <div class="session-info">
               <div class="info-item"><i class="fas fa-user"></i>
                 <c:forEach var="u" items="${users}"><c:if test="${u.id == s.userId}">${u.username}</c:if></c:forEach>
               </div>
               <div class="info-item"><i class="fas fa-dumbbell"></i> ${s.trainingType}</div>
               <div class="info-item"><i class="fas fa-map-marker-alt"></i> ${s.location}</div>
             </div>
             <div class="session-actions">
               <c:set var="monthYearParams2" value=""/>
               <c:if test="${not empty calendar_month and not empty calendar_year}">
                 <c:set var="monthYearParams2" value="&month=${calendar_month}&year=${calendar_year}"/>
               </c:if>
               <c:if test="${s.status == 'pending'}">
                 <a href="ScheduleServlet?action=confirm&id=${s.id}${monthYearParams2}" class="btn btn-success btn-sm"><i class="fas fa-check"></i> Xác nhận</a>
                 <a href="ScheduleServlet?action=reject&id=${s.id}${monthYearParams2}" class="btn btn-danger btn-sm"><i class="fas fa-times"></i> Từ chối</a>
               </c:if>
               <c:if test="${s.status == 'confirmed'}">
                 <a href="ScheduleServlet?action=complete&id=${s.id}${monthYearParams2}" class="btn btn-success btn-sm"><i class="fas fa-check"></i> Hoàn thành</a>
                 <a href="ScheduleServlet?action=cancel&id=${s.id}${monthYearParams2}" class="btn btn-danger btn-sm"><i class="fas fa-times"></i> Huỷ</a>
               </c:if>
               <a href="ScheduleServlet?action=edit&id=${s.id}${monthYearParams2}" class="btn btn-info btn-sm"><i class="fas fa-edit"></i> Chỉnh sửa</a>
               <a href="ScheduleServlet?action=delete&id=${s.id}${monthYearParams2}" class="btn btn-danger btn-sm"><i class="fas fa-trash"></i> Xóa</a>
             </div>
           </div>
         </c:forEach>
       </div>
    </div>

    <!-- Modal tạo buổi tập -->
     <div id="sessionModal" class="modal${(not empty editSchedule or showCreateModal) ? ' active' : ''}">
      <div class="modal-content">
        <div class="modal-header">
          <h2><i class="fas fa-plus-circle"></i> 
            <c:choose>
              <c:when test="${not empty editSchedule}">Chỉnh sửa buổi tập</c:when>
              <c:otherwise>Tạo buổi tập mới</c:otherwise>
            </c:choose>
          </h2>
          <button class="close-btn" onclick="closeModal()">&times;</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/ScheduleServlet" >
          <input type="hidden" name="action" value="${not empty editSchedule ? 'update' : 'create'}"/>
          <c:if test="${not empty editSchedule}">
            <input type="hidden" name="id" value="${editSchedule.id}"/>
          </c:if>
      <c:set var="selectedUserName" value=""/>
      <c:if test="${not empty editSchedule}">
        <c:forEach var="u" items="${users}">
          <c:if test="${u.id == editSchedule.userId}">
            <c:set var="selectedUserName" value="${u.username}"/>
          </c:if>
        </c:forEach>
      </c:if>
      <div class="form-group">
        <label>Học viên</label>
         <input id="studentNameInput" type="text" name="user_name" list="usersDataList" placeholder="Nhập tên học viên" value="${not empty form_user_name ? form_user_name : selectedUserName}" required />
        <datalist id="usersDataList">
          <c:forEach var="u" items="${users}">
            <option value="${u.username}"></option>
          </c:forEach>
        </datalist>
      </div>
          <div class="form-group">
            <label>Ngày tập</label>
             <input type="date" name="training_date" value="${not empty form_training_date ? form_training_date : editSchedule.trainingDate}" required/>
          </div>
          <div class="form-group">
            <label>Giờ bắt đầu</label>
             <input type="time" name="start_time" value="${not empty form_start_time ? form_start_time : editSchedule.startTime}" required/>
          </div>
          <div class="form-group">
            <label>Giờ kết thúc</label>
             <input type="time" name="end_time" value="${not empty form_end_time ? form_end_time : editSchedule.endTime}" required/>
          </div>
          <div class="form-group">
            <label>Loại tập</label>
             <select name="training_type" required>
              <option value="">Chọn loại tập</option>
               <c:set var="typeValue" value="${not empty form_training_type ? form_training_type : editSchedule.trainingType}"/>
               <option value="Cardio" ${typeValue == 'Cardio' ? 'selected' : ''}>Cardio</option>
               <option value="Strength Training" ${typeValue == 'Strength Training' ? 'selected' : ''}>Strength Training</option>
               <option value="Yoga" ${typeValue == 'Yoga' ? 'selected' : ''}>Yoga</option>
               <option value="Weight Loss" ${typeValue == 'Weight Loss' ? 'selected' : ''}>Weight Loss</option>
               <option value="Bodybuilding" ${typeValue == 'Bodybuilding' ? 'selected' : ''}>Bodybuilding</option>
            </select>
          </div>
          <div class="form-group">
            <label>Địa điểm</label>
             <input type="text" name="location" placeholder="Nhập phòng tập" value="${not empty form_location ? form_location : editSchedule.location}" required/>
          </div>
          <div class="form-group">
            <label>Ghi chú</label>
             <textarea name="note" rows="3" placeholder="Nhập ghi chú (không bắt buộc)">${not empty form_note ? form_note : editSchedule.note}</textarea>
          </div>
           <div style="display: flex; gap: 10px; justify-content: flex-end; align-items: center;">
            <button type="button" class="btn" style="background: #6c757d" onclick="closeModal()">Hủy</button>
            <button type="submit" class="btn"><i class="fas fa-save"></i> <c:choose><c:when test="${not empty editSchedule}">Lưu</c:when><c:otherwise>Tạo buổi tập</c:otherwise></c:choose></button>
          </div>
           <c:if test="${not empty error}">
             <div id="error-message" class="error-message">${error}</div>
           </c:if>
         </form>
      </div>
    </div>
    <script>
      // Tự động chuyển view dựa trên defaultView parameter từ server
      (function() {
        const defaultView = '${defaultView}';
        if (defaultView === 'calendar') {
          switchView('calendar');
        } else if (defaultView === 'list') {
          switchView('list');
        }
      })();

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

      function openModal() {
        document.getElementById('sessionModal').classList.add('active');
      }

      function closeModal() {
        document.getElementById('sessionModal').classList.remove('active');
        window.location = 'ScheduleServlet?action=list';
      }

      // Auto-hide error and focus back to student input
      (function (){
        const err = document.getElementById('error-message');
        if (err) {
          // Scroll mượt tới thông báo lỗi
          try { err.scrollIntoView({ behavior: 'smooth', block: 'center' }); } catch (_) {}
          // Tự ẩn sau 3 giây rồi focus vào ô nhập tên học viên
          setTimeout(()=>{
            err.classList.add('fade-out');
            setTimeout(()=>{
              if (err) err.style.display = 'none';
              const input = document.getElementById('studentNameInput');
              if (input) input.focus();
            }, 300);
          }, 3000);
        }
      })();
    </script>
  </body>
</html>

