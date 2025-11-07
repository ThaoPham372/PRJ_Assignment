<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Thống kê & Báo cáo - PT</title>
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

      .filter-bar {
        background: var(--card);
        border-radius: 15px;
        padding: 25px;
        margin-bottom: 30px;
        box-shadow: 0 4px 20px var(--shadow);
        display: flex;
        gap: 15px;
        align-items: end;
        flex-wrap: wrap;
      }

      .form-group {
        display: flex;
        flex-direction: column;
        gap: 8px;
        flex: 1;
        min-width: 200px;
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
      }

      .stats-overview {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
        gap: 25px;
        margin-bottom: 40px;
      }

      .stat-card {
        background: var(--card);
        border-radius: 20px;
        padding: 30px;
        box-shadow: 0 8px 30px var(--shadow);
        transition: all 0.3s;
        position: relative;
        overflow: hidden;
      }

      .stat-card::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 4px;
        background: var(--gradient-accent);
      }

      .stat-card:hover {
        transform: translateY(-8px);
        box-shadow: 0 12px 40px var(--shadow);
      }

      .stat-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
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

      .stat-icon.success {
        background: linear-gradient(135deg, #28a745, #20c997);
      }

      .stat-icon.warning {
        background: linear-gradient(135deg, #ffc107, #ffb300);
      }

      .stat-icon.info {
        background: linear-gradient(135deg, #17a2b8, #138496);
      }

      .stat-number {
        font-size: 3rem;
        font-weight: 800;
        color: var(--primary);
        margin-bottom: 10px;
      }

      .stat-label {
        color: var(--text-light);
        font-size: 1rem;
        margin-bottom: 15px;
      }

      .stat-change {
        display: inline-flex;
        align-items: center;
        gap: 5px;
        padding: 5px 12px;
        border-radius: 20px;
        font-size: 0.85rem;
        font-weight: 600;
      }

      .stat-change.positive {
        background: #d4edda;
        color: #155724;
      }

      .stat-change.negative {
        background: #f8d7da;
        color: #721c24;
      }

      .charts-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
        gap: 30px;
        margin-bottom: 40px;
      }

      .chart-card {
        background: var(--card);
        border-radius: 20px;
        padding: 30px;
        box-shadow: 0 8px 30px var(--shadow);
      }

      .chart-card h3 {
        color: var(--primary);
        margin-bottom: 25px;
        font-size: 1.5rem;
        display: flex;
        align-items: center;
        gap: 10px;
      }

      .chart-placeholder {
        background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        height: 300px;
        border-radius: 15px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: var(--text-light);
        font-style: italic;
      }

      canvas {
        max-height: 300px;
      }

      .table-card {
        background: var(--card);
        border-radius: 20px;
        padding: 30px;
        box-shadow: 0 8px 30px var(--shadow);
        margin-bottom: 40px;
      }

      .table-card h3 {
        color: var(--primary);
        margin-bottom: 20px;
        font-size: 1.5rem;
      }

      table {
        width: 100%;
        border-collapse: collapse;
      }

      thead {
        background: #f8f9fa;
      }

      th {
        padding: 15px;
        text-align: left;
        font-weight: 700;
        color: var(--primary);
        border-bottom: 2px solid #e9ecef;
      }

      td {
        padding: 15px;
        border-bottom: 1px solid #e9ecef;
      }

      tbody tr:hover {
        background: #f8f9fa;
      }

      .rating-stars {
        color: #ffc107;
      }

      .badge {
        padding: 5px 12px;
        border-radius: 20px;
        font-size: 0.85rem;
        font-weight: 600;
      }

      .badge.completed {
        background: #d4edda;
        color: #155724;
      }

      .badge.cancelled {
        background: #f8d7da;
        color: #721c24;
      }

      .badge.pending {
        background: #fff3cd;
        color: #856404;
      }

      .achievements {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 20px;
      }

      .achievement-card {
        background: var(--card);
        border-radius: 15px;
        padding: 25px;
        box-shadow: 0 4px 20px var(--shadow);
        text-align: center;
        transition: all 0.3s;
      }

      .achievement-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 30px var(--shadow);
      }

      .achievement-icon {
        font-size: 3rem;
        margin-bottom: 15px;
      }

      .achievement-title {
        font-weight: 700;
        color: var(--primary);
        margin-bottom: 5px;
      }

      .achievement-desc {
        font-size: 0.9rem;
        color: var(--text-light);
      }

      @media (max-width: 968px) {
        .charts-grid {
          grid-template-columns: 1fr;
        }

        .filter-bar {
          flex-direction: column;
          align-items: stretch;
        }

        table {
          font-size: 0.85rem;
        }

        th,
        td {
          padding: 10px;
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
          <h1><i class="fas fa-chart-line"></i> Thống kê & Báo cáo</h1>
          <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/views/PT/homePT.jsp"
              >Home</a
            >
            <span>/</span>
            <span>Thống kê & Báo cáo</span>
          </div>
        </div>
        <div style="display: flex; gap: 10px;">
          <a href="${pageContext.request.contextPath}/trainer/report?export=excel&fromDate=${fromDate != null ? fromDate : ''}&toDate=${toDate != null ? toDate : ''}" class="btn" style="background: #28a745;">
            <i class="fas fa-file-excel"></i> Xuất Excel
          </a>
          <a href="${pageContext.request.contextPath}/trainer/report?export=pdf&fromDate=${fromDate != null ? fromDate : ''}&toDate=${toDate != null ? toDate : ''}" class="btn" style="background: #dc3545;">
            <i class="fas fa-file-pdf"></i> Xuất PDF
          </a>
        </div>
      </div>

      <!-- Filter Bar -->
      <form method="GET" action="${pageContext.request.contextPath}/trainer/report" class="filter-bar">
        <div class="form-group">
          <label>Từ ngày</label>
          <input type="date" name="fromDate" value="${fromDate != null ? fromDate : ''}" />
        </div>
        <div class="form-group">
          <label>Đến ngày</label>
          <input type="date" name="toDate" value="${toDate != null ? toDate : ''}" />
        </div>
        <div class="form-group">
          <label>Loại báo cáo</label>
          <select name="reportType">
            <option value="" ${reportType == null || reportType == '' ? 'selected' : ''}>Tất cả</option>
            <option value="students" ${reportType == 'students' ? 'selected' : ''}>Học viên</option>
            <option value="sessions" ${reportType == 'sessions' ? 'selected' : ''}>Buổi tập</option>
            <option value="revenue" ${reportType == 'revenue' ? 'selected' : ''}>Doanh thu</option>
          </select>
        </div>
        <div class="form-group">
          <label>Gói tập</label>
          <select name="packageName">
            <option value="">Tất cả</option>
            <option value="Weight Loss" ${packageName == 'Weight Loss' ? 'selected' : ''}>Weight Loss</option>
            <option value="Bodybuilding" ${packageName == 'Bodybuilding' ? 'selected' : ''}>Bodybuilding</option>
            <option value="Cardio" ${packageName == 'Cardio' ? 'selected' : ''}>Cardio</option>
            <option value="Yoga" ${packageName == 'Yoga' ? 'selected' : ''}>Yoga</option>
          </select>
        </div>
        <div class="form-group">
          <label>Loại hình tập</label>
          <select name="trainingType">
            <option value="">Tất cả</option>
            <option value="Personal Training" ${trainingType == 'Personal Training' ? 'selected' : ''}>Personal Training</option>
            <option value="Group Class" ${trainingType == 'Group Class' ? 'selected' : ''}>Group Class</option>
            <option value="Online" ${trainingType == 'Online' ? 'selected' : ''}>Online</option>
          </select>
        </div>
        <button type="submit" class="btn"><i class="fas fa-filter"></i> Lọc</button>
      </form>

      <!-- Stats Overview -->
      <div class="stats-overview">
        <div class="stat-card">
          <div class="stat-icon">
            <i class="fas fa-users"></i>
          </div>
          <div class="stat-number">${totalStudents != null ? totalStudents : 0}</div>
          <div class="stat-label">Tổng học viên phụ trách</div>
          <c:if test="${totalStudents != null && totalStudents > 0}">
            <div class="stat-change positive">
              <i class="fas fa-arrow-up"></i>
              <span>Đang hoạt động</span>
            </div>
          </c:if>
        </div>

        <div class="stat-card">
          <div class="stat-icon success">
            <i class="fas fa-calendar-check"></i>
          </div>
          <div class="stat-number">${completedSessions != null ? completedSessions : 0}</div>
          <div class="stat-label">Buổi tập hoàn thành</div>
          <c:if test="${completedSessions != null && completedSessions > 0}">
            <div class="stat-change positive">
              <i class="fas fa-arrow-up"></i>
              <span>Tỷ lệ hoàn thành: <fmt:formatNumber value="${completionRate != null ? completionRate * 100 : 0}" maxFractionDigits="1" />%</span>
            </div>
          </c:if>
          <!-- Hiển thị performance trend theo tuần -->
          <c:if test="${weeklyTrends != null && !empty weeklyTrends}">
            <c:forEach var="trend" items="${weeklyTrends}" varStatus="status">
              <c:if test="${status.last}">
                <c:choose>
                  <c:when test="${trend.trendDirection.name() == 'UP'}">
                    <div class="stat-change positive" style="margin-top: 5px;">
                      <i class="fas fa-arrow-up"></i>
                      <span>+<fmt:formatNumber value="${trend.changePercent}" maxFractionDigits="1" />% so với tuần trước</span>
                    </div>
                  </c:when>
                  <c:when test="${trend.trendDirection.name() == 'DOWN'}">
                    <div class="stat-change negative" style="margin-top: 5px;">
                      <i class="fas fa-arrow-down"></i>
                      <span><fmt:formatNumber value="${trend.changePercent}" maxFractionDigits="1" />% so với tuần trước</span>
                    </div>
                  </c:when>
                </c:choose>
              </c:if>
            </c:forEach>
          </c:if>
        </div>

        <div class="stat-card">
          <div class="stat-icon warning">
            <i class="fas fa-times-circle"></i>
          </div>
          <div class="stat-number">${cancelledSessions != null ? cancelledSessions : 0}</div>
          <div class="stat-label">Buổi tập bị hủy</div>
          <c:if test="${cancelledSessions != null && cancelledSessions > 0}">
            <div class="stat-change negative">
              <i class="fas fa-arrow-down"></i>
              <span>Cần cải thiện</span>
            </div>
          </c:if>
        </div>

        <div class="stat-card">
          <div class="stat-icon info">
            <i class="fas fa-star"></i>
          </div>
          <div class="stat-number">
            <fmt:formatNumber value="${averageRating != null ? averageRating : 0.0}" maxFractionDigits="1" />
          </div>
          <div class="stat-label">Đánh giá trung bình</div>
          <c:if test="${averageRating != null && averageRating > 0}">
            <div class="stat-change positive">
              <i class="fas fa-arrow-up"></i>
              <span>Trên 5.0 điểm</span>
            </div>
          </c:if>
        </div>
      </div>

      <!-- Charts -->
      <div class="charts-grid">
        <div class="chart-card">
          <h3>
            <i class="fas fa-chart-bar"></i>
            Số buổi tập theo tháng
          </h3>
          <canvas id="monthlySessionsChart"></canvas>
        </div>

        <div class="chart-card">
          <h3>
            <i class="fas fa-chart-pie"></i>
            Phân bổ loại hình tập
          </h3>
          <canvas id="trainingTypeChart"></canvas>
        </div>

        <div class="chart-card">
          <h3>
            <i class="fas fa-chart-line"></i>
            Xu hướng học viên mới
          </h3>
          <canvas id="newStudentsChart"></canvas>
        </div>

        <div class="chart-card">
          <h3>
            <i class="fas fa-chart-area"></i>
            Tỷ lệ hoàn thành buổi tập
          </h3>
          <canvas id="completionRateChart"></canvas>
        </div>

        <div class="chart-card">
          <h3>
            <i class="fas fa-star"></i>
            Đánh giá trung bình theo tháng
          </h3>
          <canvas id="monthlyRatingChart"></canvas>
        </div>
      </div>

      <!-- Recent Sessions Table -->
      <div class="table-card">
        <h3><i class="fas fa-history"></i> Lịch sử buổi tập gần đây</h3>
        <table>
          <thead>
            <tr>
              <th>Ngày</th>
              <th>Giờ</th>
              <th>Học viên</th>
              <th>Loại tập</th>
              <th>Trạng thái</th>
              <th>Đánh giá</th>
            </tr>
          </thead>
          <tbody>
            <c:choose>
              <c:when test="${recentSessions != null && !empty recentSessions}">
                <c:forEach var="session" items="${recentSessions}">
                  <tr>
                    <td>${session.trainingDate}</td>
                    <td>${session.startTime} - ${session.endTime}</td>
                    <td>${session.student != null && session.student.user != null ? session.student.user.name : 'N/A'}</td>
                    <td>${session.trainingType != null ? session.trainingType : 'Không xác định'}</td>
                    <td>
                      <c:choose>
                        <c:when test="${session.status.name() == 'completed'}">
                          <span class="badge completed">Hoàn thành</span>
                        </c:when>
                        <c:when test="${session.status.name() == 'cancelled'}">
                          <span class="badge cancelled">Đã hủy</span>
                        </c:when>
                        <c:when test="${session.status.name() == 'pending'}">
                          <span class="badge pending">Chờ xác nhận</span>
                        </c:when>
                        <c:when test="${session.status.name() == 'confirmed'}">
                          <span class="badge completed">Đã xác nhận</span>
                        </c:when>
                        <c:otherwise>
                          <span class="badge pending">${session.status.name()}</span>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <c:choose>
                        <c:when test="${session.status.name() == 'completed'}">
                          <c:set var="ratingValue" value="${session.rating != null ? session.rating.intValue() : 0}" />
                          <c:if test="${ratingValue > 0}">
                            <c:forEach begin="1" end="${ratingValue}">
                              <span class="rating-stars">⭐</span>
                            </c:forEach>
                          </c:if>
                          <c:if test="${ratingValue == 0}">
                            <span>-</span>
                          </c:if>
                        </c:when>
                        <c:otherwise>
                          <span>-</span>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <tr>
                  <td colspan="6" style="text-align: center; padding: 40px; color: var(--text-light);">
                    <i class="fas fa-info-circle"></i> Chưa có dữ liệu buổi tập gần đây
                  </td>
                </tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>

      <!-- Achievements -->
      <div class="table-card">
        <h3><i class="fas fa-trophy"></i> Thành tích</h3>
        <div class="achievements">
          <c:choose>
            <c:when test="${awards != null && !empty awards}">
              <c:forEach var="award" items="${awards}">
                <div class="achievement-card">
                  <div class="achievement-icon">
                    <c:choose>
                      <c:when test="${award.awardType.name() == 'TOP_SESSIONS_MONTH'}">🏆</c:when>
                      <c:when test="${award.awardType.name() == 'TOP_RATING'}">⭐</c:when>
                      <c:when test="${award.awardType.name() == 'TOP_COMPLETION_RATE'}">💪</c:when>
                      <c:otherwise>🎖️</c:otherwise>
                    </c:choose>
                  </div>
                  <div class="achievement-title">${award.awardName}</div>
                  <div class="achievement-desc">${award.description != null ? award.description : 'Danh hiệu xuất sắc'}</div>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <!-- Hiển thị danh hiệu mặc định nếu chưa có -->
              <div class="achievement-card">
                <div class="achievement-icon">🏆</div>
                <div class="achievement-title">Top PT tháng ${month != null ? month : 'hiện tại'}</div>
                <div class="achievement-desc">Huấn luyện viên xuất sắc nhất tháng</div>
              </div>
              <div class="achievement-card">
                <div class="achievement-icon">⭐</div>
                <div class="achievement-title">${completedSessions != null && completedSessions > 0 ? completedSessions : 0}+ Buổi tập</div>
                <div class="achievement-desc">Hoàn thành nhiều buổi tập</div>
              </div>
              <div class="achievement-card">
                <div class="achievement-icon">💪</div>
                <div class="achievement-title">${totalStudents != null && totalStudents > 0 ? totalStudents : 0}+ Học viên</div>
                <div class="achievement-desc">Đang phụ trách nhiều học viên</div>
              </div>
              <div class="achievement-card">
                <div class="achievement-icon">❤️</div>
                <div class="achievement-title">Đánh giá <fmt:formatNumber value="${averageRating != null ? averageRating : 0.0}" maxFractionDigits="1" />/5</div>
                <div class="achievement-desc">Được học viên đánh giá cao</div>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <!-- Student Progress Table -->
      <div class="table-card">
        <h3><i class="fas fa-chart-line"></i> Tiến độ học viên nổi bật</h3>
        <table>
          <thead>
            <tr>
              <th>Học viên</th>
              <th>Gói tập</th>
              <th>Thời gian</th>
              <th>Cân nặng ban đầu</th>
              <th>Cân nặng hiện tại</th>
              <th>Kết quả</th>
              <th>Tiến độ</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Nguyễn Văn A</td>
              <td>Weight Loss</td>
              <td>3 tháng</td>
              <td>85 kg</td>
              <td>75 kg</td>
              <td style="color: var(--success); font-weight: 700">-10 kg</td>
              <td><span class="badge completed">Đạt mục tiêu</span></td>
            </tr>
            <tr>
              <td>Trần Thị B</td>
              <td>Weight Loss</td>
              <td>2 tháng</td>
              <td>70 kg</td>
              <td>65 kg</td>
              <td style="color: var(--success); font-weight: 700">-5 kg</td>
              <td><span class="badge pending">Đang tiến bộ</span></td>
            </tr>
            <tr>
              <td>Lê Văn C</td>
              <td>Bodybuilding</td>
              <td>6 tháng</td>
              <td>70 kg</td>
              <td>78 kg</td>
              <td style="color: var(--success); font-weight: 700">
                +8 kg (cơ)
              </td>
              <td><span class="badge completed">Xuất sắc</span></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Chart.js Library -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>

    <script>
      // 1. Biểu đồ cột: Số buổi tập theo tháng
      <c:if test="${monthlySessionCount != null && !empty monthlySessionCount}">
        const monthlyLabels = [];
        const monthlyCounts = [];
        <c:forEach var="entry" items="${monthlySessionCount}">
          monthlyLabels.push('${entry.key}');
          monthlyCounts.push(${entry.value});
        </c:forEach>

        const monthlyCtx = document.getElementById('monthlySessionsChart');
        if (monthlyCtx) {
          new Chart(monthlyCtx, {
            type: 'bar',
            data: {
              labels: monthlyLabels.reverse(),
              datasets: [{
                label: 'Số buổi tập',
                data: monthlyCounts.reverse(),
                backgroundColor: 'rgba(236, 139, 90, 0.8)',
                borderColor: 'rgba(236, 139, 90, 1)',
                borderWidth: 1
              }]
            },
            options: {
              responsive: true,
              maintainAspectRatio: true,
              plugins: {
                legend: {
                  display: false
                }
              },
              scales: {
                y: {
                  beginAtZero: true
                }
              }
            }
          });
        }
      </c:if>

      // 2. Biểu đồ tròn: Phân bổ loại hình tập
      <c:if test="${trainingTypeDistribution != null && !empty trainingTypeDistribution}">
        const trainingTypeLabels = [];
        const trainingTypeCounts = [];
        <c:forEach var="entry" items="${trainingTypeDistribution}">
          trainingTypeLabels.push('${entry.key}');
          trainingTypeCounts.push(${entry.value});
        </c:forEach>

        const pieCtx = document.getElementById('trainingTypeChart');
        if (pieCtx) {
          new Chart(pieCtx, {
            type: 'pie',
            data: {
              labels: trainingTypeLabels,
              datasets: [{
                data: trainingTypeCounts,
                backgroundColor: [
                  'rgba(236, 139, 90, 0.8)',
                  'rgba(40, 167, 69, 0.8)',
                  'rgba(23, 162, 184, 0.8)',
                  'rgba(255, 193, 7, 0.8)',
                  'rgba(220, 53, 69, 0.8)',
                  'rgba(108, 117, 125, 0.8)'
                ]
              }]
            },
            options: {
              responsive: true,
              maintainAspectRatio: true,
              plugins: {
                legend: {
                  position: 'bottom'
                }
              }
            }
          });
        }
      </c:if>

      // 3. Biểu đồ đường: Xu hướng học viên mới (tạm thời dùng dữ liệu mẫu)
      // Có thể thay bằng dữ liệu từ newStudentsTrendByMonth sau
      const newStudentsChart = document.getElementById('newStudentsChart');
      if (newStudentsChart) {
        new Chart(newStudentsChart, {
          type: 'line',
          data: {
            labels: ['Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10'],
            datasets: [{
              label: 'Học viên mới',
              data: [2, 3, 5, 4, 6],
              borderColor: 'rgba(23, 162, 184, 1)',
              backgroundColor: 'rgba(23, 162, 184, 0.1)',
              tension: 0.4,
              fill: true
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
              legend: {
                display: false
              }
            },
            scales: {
              y: {
                beginAtZero: true
              }
            }
          }
        });
      }

      // 4. Biểu đồ vùng: Tỷ lệ hoàn thành theo tuần
      <c:if test="${weeklyCompletionRate != null && !empty weeklyCompletionRate}">
        const weeklyLabels = [];
        const weeklyRates = [];
        <c:forEach var="entry" items="${weeklyCompletionRate}">
          weeklyLabels.push('Tuần ${entry.key}');
          weeklyRates.push(${entry.value * 100});
        </c:forEach>

        const areaCtx = document.getElementById('completionRateChart');
        if (areaCtx) {
          new Chart(areaCtx, {
            type: 'line',
            data: {
              labels: weeklyLabels,
              datasets: [{
                label: 'Tỷ lệ hoàn thành (%)',
                data: weeklyRates,
                borderColor: 'rgba(40, 167, 69, 1)',
                backgroundColor: 'rgba(40, 167, 69, 0.1)',
                tension: 0.4,
                fill: true
              }]
            },
            options: {
              responsive: true,
              maintainAspectRatio: true,
              plugins: {
                legend: {
                  display: false
                }
              },
              scales: {
                y: {
                  beginAtZero: true,
                  max: 100,
                  ticks: {
                    callback: function(value) {
                      return value + '%';
                    }
                  }
                }
              }
            }
          });
        }
      </c:if>

      // 5. Biểu đồ đường: Đánh giá trung bình theo tháng
      <c:if test="${monthlyAverageRating != null && !empty monthlyAverageRating}">
        const ratingLabels = [];
        const ratingValues = [];
        <c:forEach var="entry" items="${monthlyAverageRating}">
          ratingLabels.push('${entry.key}');
          ratingValues.push(${entry.value});
        </c:forEach>

        const ratingCtx = document.getElementById('monthlyRatingChart');
        if (ratingCtx) {
          // Tìm tháng có điểm cao nhất và thấp nhất
          let maxRating = Math.max(...ratingValues);
          let minRating = Math.min(...ratingValues);
          let maxIndex = ratingValues.indexOf(maxRating);
          let minIndex = ratingValues.indexOf(minRating);

          new Chart(ratingCtx, {
            type: 'line',
            data: {
              labels: ratingLabels.reverse(),
              datasets: [{
                label: 'Đánh giá trung bình',
                data: ratingValues.reverse(),
                borderColor: 'rgba(255, 193, 7, 1)',
                backgroundColor: 'rgba(255, 193, 7, 0.1)',
                tension: 0.4,
                fill: true,
                pointRadius: 5,
                pointHoverRadius: 7,
                pointBackgroundColor: function(context) {
                  const index = context.dataIndex;
                  if (index === maxIndex) return 'rgba(40, 167, 69, 1)'; // Xanh cho điểm cao nhất
                  if (index === minIndex) return 'rgba(220, 53, 69, 1)'; // Đỏ cho điểm thấp nhất
                  return 'rgba(255, 193, 7, 1)';
                }
              }]
            },
            options: {
              responsive: true,
              maintainAspectRatio: true,
              plugins: {
                legend: {
                  display: false
                },
                tooltip: {
                  callbacks: {
                    afterLabel: function(context) {
                      const index = context.dataIndex;
                      if (index === maxIndex) return ' (Cao nhất)';
                      if (index === minIndex) return ' (Thấp nhất)';
                      return '';
                    }
                  }
                }
              },
              scales: {
                y: {
                  beginAtZero: true,
                  max: 5,
                  ticks: {
                    callback: function(value) {
                      return value.toFixed(1);
                    }
                  }
                }
              }
            }
          });
        }
      </c:if>
    </script>













