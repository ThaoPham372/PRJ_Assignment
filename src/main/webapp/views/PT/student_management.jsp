<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
        border-radius: 18px;
        padding: 22px;
        margin-bottom: 18px;
        box-shadow: 0 10px 30px rgba(20,26,73,.08);
        border: 1px solid #eef0f3;
      }
      .search-title {
        font-weight: 800;
        color: var(--primary);
        font-size: 1.05rem;
        margin-bottom: 10px;
      }
      /* Toolbar chế độ hiển thị (đặt ngoài form tìm kiếm) */
      .view-toolbar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin: 0 0 12px 0;
      }
      .view-toolbar .title {
        font-weight: 700;
        color: var(--primary);
      }
      .view-toolbar .group {
        display: flex;
        gap: 8px;
      }
      .view-toolbar .group .btn {
        padding: 8px 14px;
      }

      .search-bar {
        display: grid;
        grid-template-columns: 1fr minmax(180px, 240px) minmax(120px, 160px);
        gap: 12px;
        align-items: end;
      }
      .input-icon {
        position: relative;
      }
      .input-icon i {
        position: absolute;
        right: 14px;
        top: 50%;
        transform: translateY(-50%);
        color: var(--text-light);
      }
      .input-icon input {
        padding-right: 42px;
        padding-left: 16px;
        width: 100%;
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
        padding: 13px 16px;
        border: 1px solid #e7e9ee;
        border-radius: 12px;
        font-size: 1rem;
        transition: all 0.2s ease;
        background: #fff;
        box-shadow: inset 0 1px 0 rgba(20,26,73,0.02);
      }

      .form-group input:focus,
      .form-group select:focus {
        outline: none;
        border-color: rgba(236,139,90,.7);
        box-shadow: 0 0 0 4px rgba(236,139,90,0.12);
      }
      .search-actions {
        display: flex;
        gap: 10px;
        align-items: end;
        justify-content: flex-end;
        align-self: end;
      }
      .btn-search {
        background: linear-gradient(135deg, #ec8b5a 0%, #d67a4f 100%);
        color: #fff;
        border: none;
        border-radius: 12px;
        padding: 12px 18px;
        font-weight: 700;
        box-shadow: 0 10px 24px rgba(236,139,90,.28);
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
        background: rgba(20, 26, 73, 0.25);
        backdrop-filter: blur(2px);
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
        box-shadow: 0 20px 60px rgba(20, 26, 73, 0.25);
        border: 1px solid rgba(20, 26, 73, 0.08);
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
        display: flex;
        align-items: center;
        gap: 10px;
      }

      .close-btn {
        background: none;
        border: none;
        font-size: 2rem;
        cursor: pointer;
        color: var(--text-light);
        transition: transform .15s ease, color .15s ease;
      }
      .close-btn:hover {
        transform: rotate(90deg);
        color: var(--accent);
      }

      .profile-section {
        margin-bottom: 28px;
      }

      .profile-section h3 {
        color: var(--primary);
        margin-bottom: 16px;
        padding-bottom: 12px;
        border-bottom: 2px solid rgba(236, 139, 90, 0.6);
        font-weight: 700;
        letter-spacing: .2px;
      }

      .info-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20px;
      }

      .info-item {
        background: linear-gradient(0deg, #ffffff, #ffffff),
                    radial-gradient(1200px circle at -20% -20%, rgba(236,139,90,0.12), transparent 40%),
                    radial-gradient(1200px circle at 120% 120%, rgba(20,26,73,0.06), transparent 40%);
        padding: 16px 16px 14px 16px;
        border-radius: 12px;
        border: 1px solid #eef0f3;
        box-shadow: 0 2px 12px rgba(20, 26, 73, 0.04);
      }

      .info-label {
        font-size: 0.8rem;
        color: var(--text-light);
        margin-bottom: 6px;
        letter-spacing: .15px;
      }

      .info-value {
        font-size: 1.05rem;
        font-weight: 700;
        color: var(--primary);
        word-break: break-word;
      }

      .notes-section {
        margin-top: 20px;
      }

      /* List view (table) */
      .list-wrap {
        background: var(--card);
        border-radius: 15px;
        box-shadow: 0 6px 26px rgba(20,26,73,0.08);
        overflow: hidden;
      }
      .list-table {
        width: 100%;
        border-collapse: collapse;
      }
      .list-table thead tr {
        background: linear-gradient(90deg, #141a49, #1c2c6d);
        color: #fff;
        position: sticky;
        top: 0;
        z-index: 1;
      }
      .list-table th,
      .list-table td {
        padding: 14px 18px;
        text-align: left;
        font-size: 0.95rem;
        white-space: nowrap;
      }
      .list-table tbody tr {
        border-bottom: 1px solid #eef0f3;
        transition: background .2s ease;
        background: #fff;
      }
      .list-table tbody tr:hover {
        background: rgba(236,139,90,0.06);
      }
      .list-table .num {
        text-align: right;
        color: var(--primary);
        font-weight: 600;
      }
      .list-table .center {
        text-align: center;
      }
      .name-cell {
        color: var(--primary);
        font-weight: 700;
        max-width: 320px;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      .sub {
        display: block;
        font-size: .82rem;
        color: var(--text-light);
        font-weight: 500;
        margin-top: 3px;
        max-width: 320px;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      .goal-badge {
        display: inline-block;
        padding: 6px 10px;
        border-radius: 999px;
        background: rgba(236,139,90,0.12);
        color: var(--primary);
        font-weight: 600;
        font-size: .85rem;
      }
      .actions-cell .btn {
        padding: 8px 12px;
        min-width: 92px;
      }
      @media (max-width: 1100px) {
        .list-table th:nth-child(3),
        .list-table td:nth-child(3) { display: none; } /* Ẩn cột Email trên màn nhỏ */
      }

      .note-input {
        width: 100%;
        padding: 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 1rem;
        resize: vertical;
        min-height: 100px;
        transition: border-color .2s ease, box-shadow .2s ease;
      }
      .note-input:focus {
        border-color: var(--accent);
        box-shadow: 0 0 0 4px rgba(236,139,90,0.12);
      }

      /* Subtle button polish */
      .btn {
        box-shadow: 0 8px 20px rgba(236,139,90,0.25);
      }
      .btn:active {
        transform: translateY(-1px) scale(0.99);
      }

      /* Edit modal tweaks */
      #studentEditModal .profile-section h3 {
        border-bottom-color: rgba(236,139,90,0.6);
      }
      #studentEditModal .info-item {
        border-left: none;
      }
      #studentEditModal .note-input {
        min-height: 80px;
      }

      /* Scrollbar (webkit) for nicer feel */
      .modal-content::-webkit-scrollbar {
        width: 10px;
      }
      .modal-content::-webkit-scrollbar-thumb {
        background: rgba(20,26,73,.15);
        border-radius: 10px;
      }
      .modal-content::-webkit-scrollbar-thumb:hover {
        background: rgba(20,26,73,.25);
      }

      /* Toast */
      .toast {
        position: fixed;
        right: 24px;
        bottom: 24px;
        min-width: 260px;
        max-width: 90vw;
        background: #ffffff;
        color: var(--primary);
        border-left: 6px solid var(--success);
        box-shadow: 0 10px 30px rgba(20,26,73,.18);
        border-radius: 10px;
        padding: 14px 16px;
        z-index: 1100;
        display: none;
        align-items: center;
        gap: 10px;
        font-weight: 600;
      }
      .toast.show {
        display: flex;
        animation: slideUp .25s ease;
      }
      @keyframes slideUp {
        from { transform: translateY(10px); opacity: 0; }
        to { transform: translateY(0); opacity: 1; }
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
      </div>

      <!-- Search & Filter -->
      <div class="search-filter">
        <div class="search-title">Tìm kiếm học viên</div>
        <form action="${pageContext.request.contextPath}/pt/students/search" method="get" style="margin:0">
          <div class="search-bar">
            <div class="form-group">
              <label>Từ khóa</label>
              <div class="input-icon">
                <i class="fas fa-search"></i>
                <input
                type="text"
                name="keyword"
                placeholder="Nhập tên, ID hoặc số điện thoại..."
                value="${searchTerm != null ? searchTerm : ''}"
                />
              </div>
            </div>
            <div class="form-group">
              <label>Gói tập</label>
              <select name="package">
                <option value="">Tất cả</option>
                <c:if test="${packages != null && !empty packages}">
                  <c:forEach var="pkg" items="${packages}">
                    <option value="${pkg.name}" ${packageFilter == pkg.name ? 'selected' : ''}>${pkg.name}</option>
                  </c:forEach>
                </c:if>
              </select>
            </div>
            <div class="search-actions">
              <button type="submit" class="btn btn-search"><i class="fas fa-search"></i> Tìm kiếm</button>
            </div>
          </div>
        </form>
      </div>

      <!-- View Mode Toolbar - đặt dưới form tìm kiếm -->
      <div class="view-toolbar">
        <div class="title">Chế độ hiển thị</div>
        <div class="group">
          <button type="button" class="btn btn-sm" id="btnCards" onclick="switchView('cards')">
            <i class="fas fa-grip"></i> Thẻ
          </button>
          <button type="button" class="btn btn-sm" id="btnList" style="background:#6c757d" onclick="switchView('list')">
            <i class="fas fa-list"></i> Danh sách
          </button>
        </div>
      </div>

      <!-- Students - Cards View -->
      <div id="cards-view" class="students-grid">
        <c:choose>
          <c:when test="${students != null && !empty students}">
            <c:forEach var="student" items="${students}">
              <c:set var="memberId" value="${student[0]}" />
              <c:set var="name" value="${student[1]}" />
              <c:set var="phone" value="${student[2]}" />
              <c:set var="email" value="${student[3]}" />
              <c:set var="gender" value="${student[4]}" />
              <c:set var="dob" value="${student[5]}" />
              <c:set var="weight" value="${student[6]}" />
              <c:set var="height" value="${student[7]}" />
              <c:set var="bmi" value="${student[8]}" />
              <c:set var="goal" value="${student[9]}" />
              <c:set var="ptNote" value="${student[10]}" />
              <c:choose>
                <c:when test="${fn:length(student) > 12}">
                  <c:set var="packageName" value="${student[11]}" />
                  <c:set var="totalBookings" value="${student[12]}" />
                  <c:set var="completedSessions" value="${student[13]}" />
                  <c:set var="confirmedSessions" value="${student[14]}" />
                  <c:set var="pendingSessions" value="${student[15]}" />
                </c:when>
                <c:otherwise>
                  <c:set var="packageName" value="" />
                  <c:set var="totalBookings" value="${student[11]}" />
                  <c:set var="completedSessions" value="${student[12]}" />
                  <c:set var="confirmedSessions" value="${student[13]}" />
                  <c:set var="pendingSessions" value="${student[14]}" />
                </c:otherwise>
              </c:choose>
              <c:set var="totalSessions" value="${completedSessions + confirmedSessions}" />
              <c:set var="progress" value="${totalBookings > 0 ? (completedSessions * 100 / totalBookings) : 0}" />
              <div class="student-card" onclick="openStudentDetail(${memberId})" 
                   data-student-id="${memberId}"
                   data-student-name="${name != null ? fn:escapeXml(name) : ''}"
                   data-student-phone="${phone != null ? fn:escapeXml(phone) : ''}"
                   data-student-email="${email != null ? fn:escapeXml(email) : ''}"
                   data-student-gender="${gender != null ? fn:escapeXml(gender) : ''}"
                   data-student-dob="${dob != null ? dob : ''}"
                   data-student-weight="${weight != null ? weight : 0}"
                   data-student-height="${height != null ? height : 0}"
                   data-student-bmi="${bmi != null ? bmi : 0}"
                   data-student-goal="${goal != null ? fn:escapeXml(goal) : ''}"
                   data-student-note="${ptNote != null ? fn:escapeXml(ptNote) : ''}"
                   data-student-package="${packageName != null ? fn:escapeXml(packageName) : ''}"
                   data-student-total="${totalBookings}"
                   data-student-completed="${completedSessions}"
                   data-student-confirmed="${confirmedSessions}">
                <div class="student-header">
                  <div class="student-avatar">
                    <i class="fas fa-user"></i>
                  </div>
                  <div class="student-info">
                    <h3>${name != null ? name : 'N/A'}</h3>
                    <p class="package">${packageName != null && !empty packageName ? packageName : 'Chưa có gói tập'}</p>
                  </div>
                </div>
                <div class="student-details">
                  <div class="detail-item">
                    <i class="fas fa-phone"></i>
                    <span>${phone != null && !empty phone ? phone : 'Chưa có'}</span>
                  </div>
                  <div class="detail-item">
                    <i class="fas fa-envelope"></i>
                    <span>${email != null ? email : 'N/A'}</span>
                  </div>
                  <div class="detail-item">
                    <i class="fas fa-weight-scale"></i>
                    <span>${weight != null ? weight : 0} kg</span>
                  </div>
                  <div class="detail-item">
                    <i class="fas fa-heart-pulse"></i>
                    <span>${bmi != null ? bmi : 0}</span>
                  </div>
                </div>
                <div class="student-actions">
                  <button
                    class="btn btn-info btn-sm"
                    onclick="event.stopPropagation(); openStudentDetail(${memberId})"
                    data-student-id="${memberId}"
                    data-student-name="${name != null ? fn:escapeXml(name) : ''}"
                    data-student-phone="${phone != null ? fn:escapeXml(phone) : ''}"
                    data-student-email="${email != null ? fn:escapeXml(email) : ''}"
                    data-student-gender="${gender != null ? fn:escapeXml(gender) : ''}"
                    data-student-dob="${dob != null ? dob : ''}"
                    data-student-weight="${weight != null ? weight : 0}"
                    data-student-height="${height != null ? height : 0}"
                    data-student-bmi="${bmi != null ? bmi : 0}"
                    data-student-goal="${goal != null ? fn:escapeXml(goal) : ''}"
                    data-student-note="${ptNote != null ? fn:escapeXml(ptNote) : ''}"
                    data-student-package="${packageName != null ? fn:escapeXml(packageName) : ''}"
                    data-student-total="${totalBookings}"
                    data-student-completed="${completedSessions}"
                    data-student-confirmed="${confirmedSessions}"
                  >
                    <i class="fas fa-eye"></i> Chi tiết
                  </button>
                  <button
                    class="btn btn-sm"
                    onclick="event.stopPropagation(); openStudentEdit(${memberId})"
                  >
                    <i class="fas fa-edit"></i> Chỉnh sửa
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

      <!-- Students - List View -->
       <div id="list-view" style="display:none;">
         <div class="list-wrap">
           <table class="list-table">
             <thead>
               <tr>
                 <th>Họ tên</th>
                 <th>SĐT</th>
                 <th>Email</th>
                 <th class="center">Cân nặng</th>
                 <th class="center">Chiều cao</th>
                 <th class="center">BMI</th>
                 <th>Mục tiêu</th>
                 <th class="center" style="width:190px;">Thao tác</th>
               </tr>
             </thead>
             <tbody>
            <c:choose>
              <c:when test="${students != null && !empty students}">
                <c:forEach var="student" items="${students}">
                  <c:set var="memberId" value="${student[0]}" />
                  <c:set var="name" value="${student[1]}" />
                  <c:set var="phone" value="${student[2]}" />
                  <c:set var="email" value="${student[3]}" />
                  <c:set var="weight" value="${student[6]}" />
                  <c:set var="height" value="${student[7]}" />
                  <c:set var="bmi" value="${student[8]}" />
                  <c:set var="goal" value="${student[9]}" />
                    <tr data-member-id="${memberId}">
                     <td class="name-cell">
                       ${name != null ? name : 'N/A'}
                       <span class="sub">${email != null ? email : 'N/A'}</span>
                     </td>
                     <td>${phone != null && !empty phone ? phone : 'Chưa có'}</td>
                     <td>${email != null ? email : 'N/A'}</td>
                     <td class="num">${weight != null ? weight : 0} kg</td>
                     <td class="num">${height != null ? height : 0} cm</td>
                     <td class="num">${bmi != null ? bmi : 0}</td>
                     <td>
                       <c:choose>
                         <c:when test="${goal != null && !goal.isEmpty()}">
                           <span class="goal-badge">${goal}</span>
                         </c:when>
                         <c:otherwise>
                           <span class="sub">Chưa có mục tiêu</span>
                         </c:otherwise>
                       </c:choose>
                     </td>
                     <td class="actions-cell center">
                       <button class="btn btn-info btn-sm" onclick="openStudentDetail(${memberId})" type="button">
                         <i class="fas fa-eye"></i> Chi tiết
                       </button>
                       <button class="btn btn-sm" style="margin-left:6px" onclick="openStudentEdit(${memberId})" type="button">
                         <i class="fas fa-edit"></i> Chỉnh sửa
                       </button>
                    </td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <tr>
                   <td colspan="8" style="padding:24px; text-align:center; color: var(--text-light); font-weight:600;">Không có học viên để hiển thị</td>
                </tr>
              </c:otherwise>
            </c:choose>
             </tbody>
           </table>
         </div>
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
              <div class="info-value">-</div>
            </div>
            <div class="info-item">
              <div class="info-label">Số điện thoại</div>
              <div class="info-value">-</div>
            </div>
            <div class="info-item">
              <div class="info-label">Email</div>
              <div class="info-value">-</div>
            </div>
            <div class="info-item">
              <div class="info-label">Ngày sinh</div>
              <div class="info-value">-</div>
            </div>
          </div>
        </div>

        <div class="profile-section">
          <h3>Chỉ số cơ thể</h3>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">Cân nặng hiện tại</div>
              <div class="info-value">-</div>
            </div>
            <div class="info-item">
              <div class="info-label">Chiều cao</div>
              <div class="info-value">-</div>
            </div>
            <div class="info-item">
              <div class="info-label">BMI</div>
              <div class="info-value">-</div>
            </div>
            <div class="info-item">
              <div class="info-label">Mục tiêu</div>
              <div class="info-value">-</div>
            </div>
          </div>
        </div>

        <div class="profile-section">
          <!-- Đã bỏ phần tiến độ tập luyện theo yêu cầu -->
        </div>

        <div class="notes-section">
          <h3>Ghi chú của PT</h3>
          <div id="modal-notes-view" class="info-item" style="background:#f8f9fa; padding:15px; border-radius:10px; border-left:4px solid var(--accent); min-height:60px;">
            <div class="info-value">-</div>
          </div>
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
        </div>
      </div>
    </div>

    <!-- Modal chỉnh sửa học viên -->
    <div id="studentEditModal" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <h2><i class="fas fa-user-edit"></i> Cập nhật học viên</h2>
          <button class="close-btn" onclick="closeEditModal()">&times;</button>
        </div>

        <div class="profile-section">
          <h3>Thông tin cá nhân</h3>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">Họ và tên</div>
              <div class="info-value" id="edit-name">-</div>
            </div>
            <div class="info-item">
              <div class="info-label">Số điện thoại</div>
              <div class="info-value" id="edit-phone">-</div>
            </div>
            <div class="info-item">
              <div class="info-label">Email</div>
              <div class="info-value" id="edit-email">-</div>
            </div>
            <div class="info-item">
              <div class="info-label">Ngày sinh</div>
              <div class="info-value" id="edit-dob">-</div>
            </div>
          </div>
        </div>

        <div class="profile-section">
          <h3>Chỉ số cơ thể (có thể chỉnh sửa)</h3>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">Cân nặng (kg)</div>
              <input id="edit-weight" type="number" step="0.1" class="note-input" style="min-height:auto;" />
            </div>
            <div class="info-item">
              <div class="info-label">Chiều cao (cm)</div>
              <input id="edit-height" type="number" step="0.1" class="note-input" style="min-height:auto;" />
            </div>
            <div class="info-item">
              <div class="info-label">BMI</div>
              <input id="edit-bmi" type="number" step="0.1" class="note-input" style="min-height:auto;" readonly />
            </div>
            <div class="info-item">
              <div class="info-label">Mục tiêu</div>
              <input id="edit-goal" type="text" class="note-input" style="min-height:auto;" />
            </div>
          </div>
        </div>

        <div class="notes-section">
          <h3>Ghi chú của PT</h3>
          <textarea id="edit-ptnote" class="note-input" placeholder="Nhập ghi chú về học viên..."></textarea>
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
            onclick="closeEditModal()"
          >
            Đóng
          </button>
          <button class="btn" onclick="saveStudentEdit()">
            <i class="fas fa-save"></i> Lưu thay đổi
          </button>
        </div>
      </div>
    </div>
    <!-- Toast -->
    <div id="globalToast" class="toast">
      <i class="fas fa-check-circle" style="color:#28a745;font-size:1.2rem;"></i>
      <span id="toastText">Lưu thay đổi thành công</span>
    </div>

    <script>
      let currentStudentData = null;
      const CTX = '${pageContext.request.contextPath}';

      function mapStudentResponse(data) {
        currentStudentData = {
          memberId: data.memberId,
          name: data.name || '',
          phone: data.phone || '',
          email: data.email || '',
          gender: data.gender || '',
          dob: data.dob || '',
          address: data.address || '',
          weight: data.weight != null ? parseFloat(data.weight) : null,
          height: data.height != null ? parseFloat(data.height) : null,
          bmi: data.bmi != null ? parseFloat(data.bmi) : null,
          goal: data.goal || '',
          ptNote: data.ptNote || ''
        };
        return currentStudentData;
      }

      function fetchStudentData(memberId) {
        return fetch(CTX + '/pt/students/detail?id=' + encodeURIComponent(memberId))
          .then(r => r.json())
          .then(data => {
            if (!data || !data.success) {
              throw new Error(data && data.message ? data.message : 'Không tải được dữ liệu học viên');
            }
            return mapStudentResponse(data);
          });
      }

      function openStudentDetail(memberId) {
        fetchStudentData(memberId)
          .then(() => {
            updateModalContent();
            document.getElementById('studentModal').classList.add('active');
          })
          .catch(err => showToast(err.message || 'Lỗi mạng khi tải dữ liệu học viên'));
      }

      function updateModalContent() {
        if (!currentStudentData) return;

        // Update personal info
        const modal = document.getElementById('studentModal');
        const infoValues = modal.querySelectorAll('.info-value');
        
        // Personal info: name, phone, email, dob
        if (infoValues.length >= 4) {
          infoValues[0].textContent = currentStudentData.name || 'N/A';
          infoValues[1].textContent = currentStudentData.phone || 'Chưa có';
          infoValues[2].textContent = currentStudentData.email || 'N/A';
          infoValues[3].textContent = currentStudentData.dob ? formatDate(currentStudentData.dob) : 'N/A';
        }

        // Body metrics: weight, height, bmi, goal
        if (infoValues.length >= 8) {
          infoValues[4].textContent = currentStudentData.weight != null ? currentStudentData.weight + ' kg' : 'N/A';
          infoValues[5].textContent = currentStudentData.height != null ? currentStudentData.height + ' cm' : 'N/A';
          infoValues[6].textContent = currentStudentData.bmi != null ? currentStudentData.bmi.toFixed(1) : 'N/A';
          infoValues[7].textContent = currentStudentData.goal || 'Chưa có mục tiêu';
        }

        // Update notes (read-only)
        const notesBox = document.getElementById('modal-notes-view');
        if (notesBox) {
          const valueEl = notesBox.querySelector('.info-value');
          if (valueEl) {
            valueEl.textContent = currentStudentData.ptNote && currentStudentData.ptNote.trim().length > 0
              ? currentStudentData.ptNote
              : 'Chưa có ghi chú';
          }
        }
      }

      function formatDate(dateStr) {
        if (!dateStr || dateStr === 'null') return 'N/A';
        try {
          const date = new Date(dateStr);
          const day = String(date.getDate()).padStart(2, '0');
          const month = String(date.getMonth() + 1).padStart(2, '0');
          const year = date.getFullYear();
          return day + '/' + month + '/' + year;
        } catch (e) {
          return dateStr;
        }
      }

      function closeModal() {
        document.getElementById('studentModal').classList.remove('active');
        currentStudentData = null;
      }

      // Open edit modal - allow updating body metrics and PT note
      function openStudentEdit(memberId) {
        fetchStudentData(memberId)
          .then(() => {
            // Fill readonly personal fields
            document.getElementById('edit-name').textContent = currentStudentData.name || 'N/A';
            document.getElementById('edit-phone').textContent = currentStudentData.phone || 'Chưa có';
            document.getElementById('edit-email').textContent = currentStudentData.email || 'N/A';
            document.getElementById('edit-dob').textContent = currentStudentData.dob ? formatDate(currentStudentData.dob) : 'N/A';
            // Fill editable fields
            document.getElementById('edit-weight').value = currentStudentData.weight != null ? currentStudentData.weight : '';
            document.getElementById('edit-height').value = currentStudentData.height != null ? currentStudentData.height : '';
            document.getElementById('edit-bmi').value = currentStudentData.bmi != null ? currentStudentData.bmi.toFixed(1) : '';
            recomputeBMI();
            document.getElementById('edit-goal').value = currentStudentData.goal || '';
            document.getElementById('edit-ptnote').value = currentStudentData.ptNote || '';
            document.getElementById('studentEditModal').classList.add('active');
          })
          .catch(err => showToast(err.message || 'Không tải được thông tin để chỉnh sửa'));
      }

      function closeEditModal() {
        document.getElementById('studentEditModal').classList.remove('active');
      }

      function recomputeBMI() {
        const w = parseFloat(document.getElementById('edit-weight').value);
        const h = parseFloat(document.getElementById('edit-height').value);
        const bmiInput = document.getElementById('edit-bmi');
        if (w > 0 && h > 0) {
          const bmi = w / Math.pow(h / 100, 2);
          bmiInput.value = bmi.toFixed(1);
        } else {
          if (currentStudentData && currentStudentData.bmi != null) {
            bmiInput.value = currentStudentData.bmi.toFixed(1);
          } else {
            bmiInput.value = '';
          }
        }
      }

      document.addEventListener('input', function(e) {
        if (e.target && (e.target.id === 'edit-weight' || e.target.id === 'edit-height')) {
          recomputeBMI();
        }
      });

      function saveStudentEdit() {
        if (!currentStudentData) return;
        // Lấy giá trị mới
        const newWeight = parseFloat(document.getElementById('edit-weight').value) || 0;
        const newHeight = parseFloat(document.getElementById('edit-height').value) || 0;
        const newBMI = parseFloat(document.getElementById('edit-bmi').value) || 0;
        const newGoal = (document.getElementById('edit-goal').value || '').trim();
        const newNote = (document.getElementById('edit-ptnote').value || '').trim();

        // Gọi API để lưu thay đổi xuống server
        const params = new URLSearchParams();
        params.append('memberId', currentStudentData.memberId);
        params.append('weight', newWeight);
        params.append('height', newHeight);
        params.append('bmi', newBMI);
        params.append('goal', newGoal);
        params.append('ptNote', newNote);

        fetch(CTX + '/pt/students/update', {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
          body: params.toString()
        })
        .then(r => r.json())
        .then(res => {
          if (res && res.success) {
            // Cập nhật UI card tương ứng
            const card = document.querySelector(`.student-card[data-student-id="${currentStudentData.memberId}"]`);
            if (card) {
              card.setAttribute('data-student-weight', newWeight);
              card.setAttribute('data-student-height', newHeight);
              card.setAttribute('data-student-bmi', newBMI);
              card.setAttribute('data-student-goal', newGoal);
              card.setAttribute('data-student-note', newNote);

              // Cập nhật 2 ô hiển thị weight & BMI trong card
              const detailItems = card.querySelectorAll('.student-details .detail-item span');
              if (detailItems && detailItems.length >= 4) {
                detailItems[2].textContent = (newWeight || 0) + ' kg';
                detailItems[3].textContent = (newBMI || 0);
              }
            }

            // Cập nhật bảng danh sách (list-view) nếu đang hiển thị
            const row = document.querySelector(`#list-view tr[data-member-id="${currentStudentData.memberId}"]`);
            if (row) {
              const tds = row.querySelectorAll('td');
              // cột: 0 name,1 phone,2 email,3 weight,4 height,5 bmi,6 goal,7 actions
              if (tds.length >= 7) {
                tds[3].textContent = (newWeight || 0) + ' kg';
                tds[4].textContent = (newHeight || 0) + ' cm';
                tds[5].textContent = (newBMI || 0);
                tds[6].textContent = newGoal || 'Chưa có mục tiêu';
              }
            }

            // Cập nhật dữ liệu hiện tại để modal chi tiết sync
            currentStudentData.weight = newWeight;
            currentStudentData.height = newHeight;
            currentStudentData.bmi = newBMI;
            currentStudentData.goal = newGoal;
            currentStudentData.ptNote = newNote;

            showToast('Lưu thay đổi thành công');
            closeEditModal();

            // Nếu modal xem chi tiết đang mở, cập nhật phần ghi chú và body metrics
            const detailModal = document.getElementById('studentModal');
            if (detailModal && detailModal.classList.contains('active')) {
              updateModalContent();
            }
          } else {
            showToast('Lưu thất bại, vui lòng thử lại');
          }
        })
        .catch(() => {
          showToast('Có lỗi khi lưu dữ liệu');
        });
      }

      function showToast(message) {
        const toast = document.getElementById('globalToast');
        const text = document.getElementById('toastText');
        text.textContent = message || 'Thao tác thành công';
        toast.classList.add('show');
        setTimeout(() => {
          toast.classList.remove('show');
        }, 2500);
      }

      // Không còn cần tự suy ra contextPath vì đã có CTX từ server

      // View toggle
      function switchView(mode) {
        const cards = document.getElementById('cards-view');
        const list = document.getElementById('list-view');
        const btnCards = document.getElementById('btnCards');
        const btnList = document.getElementById('btnList');
        if (mode === 'list') {
          cards.style.display = 'none';
          list.style.display = 'block';
          btnCards.style.background = '#6c757d';
          btnList.style.background = 'var(--accent)';
          localStorage.setItem('ptStudentsView', 'list');
        } else {
          list.style.display = 'none';
          cards.style.display = 'grid';
          btnList.style.background = '#6c757d';
          btnCards.style.background = 'var(--accent)';
          localStorage.setItem('ptStudentsView', 'cards');
        }
      }
      // init view based on saved preference
      (function initViewToggle(){
        const pref = localStorage.getItem('ptStudentsView') || 'cards';
        switchView(pref);
      })();
    </script>
  </body>
</html>
