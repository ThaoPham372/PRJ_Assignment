<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Báo cáo & Thống kê - GymFit</title>
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
      rel="stylesheet"
    />
    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap"
      rel="stylesheet"
    />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

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
      .filter-bar {
        background: #fff;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
        margin-bottom: 25px;
        display: flex;
        gap: 15px;
        flex-wrap: wrap;
        align-items: center;
      }
      .filter-select {
        padding: 10px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 0.9rem;
        cursor: pointer;
      }
      .reports-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        gap: 25px;
        margin-bottom: 30px;
      }
      .report-card {
        background: #fff;
        border-radius: 15px;
        padding: 25px;
        box-shadow: 0 4px 15px var(--shadow);
        display: flex;
        flex-direction: column;
      }
      .report-card-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 20px;
      }
      .report-card-title {
        font-size: 1.1rem;
        font-weight: 700;
        color: var(--primary);
      }
      .report-card-icon {
        width: 45px;
        height: 45px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.3rem;
        color: #fff;
      }
      .report-chart-container {
        flex: 1;
        min-height: 200px;
        position: relative;
        margin-bottom: 15px;
      }
      .report-summary {
        display: flex;
        justify-content: space-between;
        padding-top: 15px;
        border-top: 2px solid #f0f0f0;
      }
      .summary-item {
        text-align: center;
        flex: 1;
      }
      .summary-label {
        font-size: 0.8rem;
        color: #5a6c7d;
        margin-bottom: 5px;
      }
      .summary-value {
        font-size: 1.3rem;
        font-weight: 700;
        color: var(--primary);
      }
      .large-chart {
        background: #fff;
        border-radius: 15px;
        padding: 25px;
        box-shadow: 0 4px 15px var(--shadow);
        margin-bottom: 25px;
      }
      .large-chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
      }
      .large-chart-title {
        font-size: 1.3rem;
        font-weight: 700;
        color: var(--primary);
      }
      .large-chart-container {
        height: 400px;
        position: relative;
      }

      /* Revenue Cards */
      .revenue-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
        gap: 20px;
        margin-bottom: 30px;
      }

      .revenue-card {
        background: #fff;
        padding: 25px;
        border-radius: 15px;
        box-shadow: 0 4px 15px var(--shadow);
      }

      .revenue-card-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 15px;
      }

      .revenue-card-title {
        font-size: 0.95rem;
        color: #5a6c7d;
        font-weight: 600;
      }

      .revenue-card-icon {
        width: 45px;
        height: 45px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.3rem;
        color: #fff;
      }

      .revenue-amount {
        font-size: 2rem;
        font-weight: 700;
        color: var(--primary);
        margin-bottom: 10px;
      }

      .revenue-change {
        font-size: 0.85rem;
        display: flex;
        align-items: center;
        gap: 5px;
      }

      .revenue-change.up {
        color: #27ae60;
      }

      .revenue-change.down {
        color: #e74c3c;
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
        .reports-grid {
          grid-template-columns: 1fr;
        }
        .filter-bar {
          flex-direction: column;
          align-items: stretch;
        }
      }
    </style>
  </head>
  <body>
    <div class="admin-container">
      <aside class="sidebar">
        <div class="sidebar-header">
          <a
            href="${pageContext.request.contextPath}/admin/home"
            class="sidebar-brand"
          >
            <i class="fas fa-dumbbell"></i><span>GYMFIT</span>
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
              href="${pageContext.request.contextPath}/admin/payment-finance"
              class="sidebar-menu-link"
            >
              <i class="fas fa-money-bill-wave"></i
              ><span>Thanh toán & Tài chính</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/admin/reports"
              class="sidebar-menu-link active"
            >
              <i class="fas fa-chart-line"></i><span>Báo cáo & Thống kê</span>
            </a>
          </li>
        </ul>
      </aside>

      <main class="main-content">
        <div class="top-bar">
          <h1><i class="fas fa-chart-line"></i> Báo cáo & Thống kê</h1>
          <div class="top-bar-actions">
            <button class="btn" onclick="window.print()">
              <i class="fas fa-download"></i> Xuất báo cáo
            </button>
            <a
              href="${pageContext.request.contextPath}/admin/dashboard"
              class="btn btn-outline"
            >
              <i class="fas fa-arrow-left"></i> Quay lại
            </a>
          </div>
        </div>

        <div class="content-area">
          <div class="filter-bar">
            <label style="font-weight: 600">Chọn khoảng thời gian:</label>
            <select
              id="timeRangeSelect"
              class="filter-select"
              onchange="toggleCustomDate()"
            >
              <option value="7days">7 ngày qua</option>
              <option value="30days">30 ngày qua</option>
              <option value="3months">3 tháng qua</option>
              <option value="6months">6 tháng qua</option>
              <option value="12months" selected>12 tháng qua</option>
              <option value="custom">Tùy chỉnh...</option>
            </select>

            <div
              id="customDateRange"
              style="display: none; gap: 10px; align-items: center"
            >
              <input type="date" id="startDate" class="filter-select" />
              <span>đến</span>
              <input type="date" id="endDate" class="filter-select" />
            </div>

            <button class="btn" onclick="applyFilter()">
              <i class="fas fa-filter"></i> Áp dụng
            </button>
          </div>

          <!-- Revenue Cards -->
          <div class="revenue-grid">
            <div class="revenue-card">
              <div class="revenue-card-header">
                <span class="revenue-card-title">Doanh thu hôm nay</span>
                <div
                  class="revenue-card-icon"
                  style="
                    background: linear-gradient(
                      135deg,
                      #667eea 0%,
                      #764ba2 100%
                    );
                  "
                >
                  <i class="fas fa-dollar-sign"></i>
                </div>
              </div>
              <div class="revenue-amount">
                <fmt:formatNumber value="${revenueToday}" type="number" maxFractionDigits="0" /> đ
              </div>
              <div class="revenue-change <c:choose><c:when test="${todayGrowthRate >= 0}">up</c:when><c:otherwise>down</c:otherwise></c:choose>">
                <i class="fas fa-arrow-<c:choose><c:when test="${todayGrowthRate >= 0}">up</c:when><c:otherwise>down</c:otherwise></c:choose>"></i>
                <span><c:if test="${todayGrowthRate >= 0}">+</c:if><fmt:formatNumber value="${todayGrowthRate}" type="number" maxFractionDigits="1" />% so với hôm qua</span>
              </div>
            </div>

            <div class="revenue-card">
              <div class="revenue-card-header">
                <span class="revenue-card-title">Doanh thu tháng này</span>
                <div
                  class="revenue-card-icon"
                  style="
                    background: linear-gradient(
                      135deg,
                      #11998e 0%,
                      #38ef7d 100%
                    );
                  "
                >
                  <i class="fas fa-chart-line"></i>
                </div>
              </div>
              <div class="revenue-amount">
                <fmt:formatNumber value="${revenueThisMonth}" type="number" maxFractionDigits="0" /> đ
              </div>
              <div class="revenue-change <c:choose><c:when test="${monthGrowthRate >= 0}">up</c:when><c:otherwise>down</c:otherwise></c:choose>">
                <i class="fas fa-arrow-<c:choose><c:when test="${monthGrowthRate >= 0}">up</c:when><c:otherwise>down</c:otherwise></c:choose>"></i>
                <span><c:if test="${monthGrowthRate >= 0}">+</c:if><fmt:formatNumber value="${monthGrowthRate}" type="number" maxFractionDigits="1" />% so với tháng trước</span>
              </div>
            </div>

            <div class="revenue-card">
              <div class="revenue-card-header">
                <span class="revenue-card-title">Doanh thu năm nay</span>
                <div
                  class="revenue-card-icon"
                  style="background: var(--gradient-accent)"
                >
                  <i class="fas fa-coins"></i>
                </div>
              </div>
              <div class="revenue-amount">
                <fmt:formatNumber value="${revenueThisYear}" type="number" maxFractionDigits="0" /> đ
              </div>
              <div class="revenue-change <c:choose><c:when test="${yearGrowthRate >= 0}">up</c:when><c:otherwise>down</c:otherwise></c:choose>">
                <i class="fas fa-arrow-<c:choose><c:when test="${yearGrowthRate >= 0}">up</c:when><c:otherwise>down</c:otherwise></c:choose>"></i>
                <span><c:if test="${yearGrowthRate >= 0}">+</c:if><fmt:formatNumber value="${yearGrowthRate}" type="number" maxFractionDigits="1" />% so với năm trước</span>
              </div>
            </div>
          </div>

          <div class="reports-grid">
            <div class="report-card">
              <div class="report-card-header">
                <h3 class="report-card-title">Hội viên</h3>
                <div
                  class="report-card-icon"
                  style="
                    background: linear-gradient(
                      135deg,
                      #667eea 0%,
                      #764ba2 100%
                    );
                  "
                >
                  <i class="fas fa-users"></i>
                </div>
              </div>
              <div class="report-chart-container">
                <canvas id="memberGrowthChart"></canvas>
              </div>
              <div class="report-summary">
                <div class="summary-item">
                  <div class="summary-label">Tổng HV</div>
                  <div class="summary-value">
                    <fmt:formatNumber
                      value="${summary.totalMembers}"
                      type="number"
                    />
                  </div>
                </div>
                <div class="summary-item">
                  <div class="summary-label">HV mới (tháng này)</div>
                  <div class="summary-value">
                    +<fmt:formatNumber
                      value="${summary.newMembersThisMonth}"
                      type="number"
                    />
                  </div>
                </div>
                <div class="summary-item">
                  <div class="summary-label">Tăng trưởng</div>
                  <div
                    class="summary-value"
                    style="color: ${summary.memberGrowthRate >= 0 ? '#38ef7d' : '#ff5b5b'}"
                  >
                    ${summary.memberGrowthRate > 0 ? '+' :
                    ''}${summary.memberGrowthRate}%
                  </div>
                </div>
              </div>
            </div>

            <div class="report-card">
              <div class="report-card-header">
                <h3 class="report-card-title">Doanh thu theo gói</h3>
                <div
                  class="report-card-icon"
                  style="
                    background: linear-gradient(
                      135deg,
                      #11998e 0%,
                      #38ef7d 100%
                    );
                  "
                >
                  <i class="fas fa-dollar-sign"></i>
                </div>
              </div>
              <div class="report-chart-container">
                <canvas id="packagePieChart"></canvas>
              </div>
              <div class="report-summary">
                <div class="summary-item">
                  <div class="summary-label">Tháng này</div>
                  <div class="summary-value">
                    <fmt:setLocale value="vi_VN" />
                    <fmt:formatNumber
                      value="${summary.revenueThisMonth}"
                      type="currency"
                      currencySymbol="₫"
                    />
                  </div>
                </div>
                <div class="summary-item">
                  <div class="summary-label">Tăng trưởng</div>
                  <div
                    class="summary-value"
                    style="color: ${summary.revenueGrowthRate >= 0 ? '#38ef7d' : '#ff5b5b'}"
                  >
                    ${summary.revenueGrowthRate > 0 ? '+' :
                    ''}${summary.revenueGrowthRate}%
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="large-chart">
            <div class="large-chart-header">
              <h3 class="large-chart-title">Biểu đồ doanh thu 12 tháng qua</h3>
            </div>
            <div class="large-chart-container">
              <canvas id="revenueLineChart"></canvas>
            </div>
          </div>
        </div>
      </main>
    </div>

    <script>
      // --- HÀM HỖ TRỢ ---
      const formatCurrency = (amount) =>
        new Intl.NumberFormat('vi-VN', {
          style: 'currency',
          currency: 'VND',
        }).format(amount)
      function toggleCustomDate() {
        const select = document.getElementById('timeRangeSelect')
        document.getElementById('customDateRange').style.display =
          select.value === 'custom' ? 'flex' : 'none'
      }
      function applyFilter() {
        // Chức năng này sẽ cần gọi về Servlet với tham số lọc
        alert('Chức năng lọc đang được phát triển!')
      }

      // --- HÀM HỖ TRỢ FORMAT ---
      function formatMonthLabel(monthStr) {
        // monthStr format: "YYYY-MM"
        const months = ['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12']
        const parts = monthStr.split('-')
        if (parts.length === 2) {
          const monthIndex = parseInt(parts[1]) - 1
          return months[monthIndex] || monthStr
        }
        return monthStr
      }

      function formatMonthLabelFull(monthStr) {
        // monthStr format: "YYYY-MM" -> "Tháng X/YYYY"
        const months = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12']
        const parts = monthStr.split('-')
        if (parts.length === 2) {
          return 'Tháng ' + months[parseInt(parts[1]) - 1] + '/' + parts[0]
        }
        return monthStr
      }

      // --- LẤY DỮ LIỆU TỪ SERVER ---
      const revenueDataRaw = '${revenueChartJson}'
      const packageDataRaw = '${packageChartJson}'
      const memberDataRaw = '${memberChartJson}'
      let revenueData = [],
        packageData = [],
        memberData = []

      try {
        if (revenueDataRaw && revenueDataRaw !== '' && revenueDataRaw !== 'null') {
          revenueData = JSON.parse(revenueDataRaw)
        }
        if (packageDataRaw && packageDataRaw !== '' && packageDataRaw !== 'null') {
          packageData = JSON.parse(packageDataRaw)
        }
        if (memberDataRaw && memberDataRaw !== '' && memberDataRaw !== 'null') {
          memberData = JSON.parse(memberDataRaw)
        }
      } catch (e) {
        console.error('Lỗi parse JSON:', e)
        console.error('revenueDataRaw:', revenueDataRaw)
        console.error('packageDataRaw:', packageDataRaw)
        console.error('memberDataRaw:', memberDataRaw)
      }

      // --- 1. BIỂU ĐỒ HỘI VIÊN (Bar Chart - Dữ liệu thật) ---
      const memberCtx = document
        .getElementById('memberGrowthChart')
        .getContext('2d')

      // Xử lý dữ liệu member: convert value từ BigDecimal sang number và format label
      let memberLabels = []
      let memberValues = []
      
      if (memberData && memberData.length > 0) {
        memberLabels = memberData.map(item => {
          if (item && item.label) {
            return formatMonthLabel(item.label)
          }
          return ''
        }).filter(label => label !== '')
        
        memberValues = memberData.map(item => {
          if (item && item.value !== undefined && item.value !== null) {
            return Number(item.value)
          }
          return 0
        })
      } else {
        // Fallback data nếu không có dữ liệu
        memberLabels = ['T6', 'T7', 'T8', 'T9', 'T10', 'T11']
        memberValues = [0, 0, 0, 0, 0, 0]
      }

      new Chart(memberCtx, {
        type: 'bar',
        data: {
          labels: memberLabels,
          datasets: [
            {
              label: 'Hội viên mới',
              data: memberValues,
              backgroundColor: 'rgba(102, 126, 234, 0.8)',
              borderColor: '#667eea',
              borderWidth: 2,
              borderRadius: 4,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { 
            legend: { display: false }, 
            tooltip: { 
              enabled: true,
              callbacks: {
                label: (ctx) => 'Hội viên mới: ' + ctx.parsed.y
              }
            } 
          },
          scales: { 
            x: { 
              display: true,
              grid: { display: false }
            }, 
            y: { 
              display: true,
              beginAtZero: true,
              ticks: {
                stepSize: 1,
                precision: 0
              },
              grid: { color: '#f0f0f0' }
            } 
          },
        },
      })

      // --- 2. BIỂU ĐỒ TRÒN (DOANH THU THEO GÓI) ---
      const packageCtx = document
        .getElementById('packagePieChart')
        .getContext('2d')

      // Xử lý dữ liệu package
      let packageLabels = []
      let packageValues = []
      
      if (packageData && packageData.length > 0) {
        packageLabels = packageData.map((item) => {
          return item && item.label ? item.label : 'Không xác định'
        })
        packageValues = packageData.map((item) => {
          if (item && item.value !== undefined && item.value !== null) {
            return Number(item.value)
          }
          return 0
        })
      } else {
        // Nếu không có dữ liệu, hiển thị thông báo
        packageLabels = ['Chưa có dữ liệu']
        packageValues = [1]
      }

      new Chart(packageCtx, {
        type: 'pie',
        data: {
          labels: packageLabels,
          datasets: [
            {
              data: packageValues,
              backgroundColor: [
                '#11998e',
                '#38ef7d',
                '#0575E6',
                '#ff9966',
                '#ec8b5a',
                '#e0e0e0',
                '#9b59b6',
                '#3498db',
              ],
              borderWidth: 2,
              borderColor: '#fff',
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'bottom',
              labels: { 
                boxWidth: 12, 
                font: { size: 11 },
                padding: 10
              },
            },
            tooltip: {
              callbacks: {
                label: (ctx) => {
                  const label = ctx.label || ''
                  const value = ctx.parsed !== undefined ? formatCurrency(ctx.parsed) : formatCurrency(ctx.raw)
                  const percentage = ((ctx.parsed !== undefined ? ctx.parsed : ctx.raw) / packageValues.reduce((a, b) => a + b, 0) * 100).toFixed(1)
                  return label + ': ' + value + ' (' + percentage + '%)'
                },
              },
            },
          },
        },
      })

      // --- 3. BIỂU ĐỒ ĐƯỜNG LỚN (DOANH THU 12 THÁNG) ---
      const revenueCtx = document
        .getElementById('revenueLineChart')
        .getContext('2d')
      const revenueGradient = revenueCtx.createLinearGradient(0, 0, 0, 400)
      revenueGradient.addColorStop(0, 'rgba(20, 26, 73, 0.5)')
      revenueGradient.addColorStop(1, 'rgba(20, 26, 73, 0.0)')

      // Xử lý dữ liệu revenue: format label và convert value
      let revenueLabels = []
      let revenueValues = []
      
      if (revenueData && revenueData.length > 0) {
        revenueLabels = revenueData.map((item) => {
          if (item && item.label) {
            return formatMonthLabelFull(item.label)
          }
          return ''
        }).filter(label => label !== '')
        
        revenueValues = revenueData.map((item) => {
          if (item && item.value !== undefined && item.value !== null) {
            return Number(item.value)
          }
          return 0
        })
      }

      // Nếu không có dữ liệu, tạo mảng rỗng để chart không bị lỗi
      if (revenueLabels.length === 0) {
        revenueLabels = []
        revenueValues = []
      }

      new Chart(revenueCtx, {
        type: 'line',
        data: {
          labels: revenueLabels,
          datasets: [
            {
              label: 'Doanh thu',
              data: revenueValues,
              borderColor: '#141a49',
              backgroundColor: revenueGradient,
              fill: true,
              tension: 0.4,
              pointBackgroundColor: '#ec8b5a',
              pointBorderColor: '#fff',
              pointHoverRadius: 6,
              pointRadius: 4,
              pointHoverBackgroundColor: '#ec8b5a',
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          interaction: {
            intersect: false,
            mode: 'index'
          },
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                callback: function (value) {
                  return formatCurrency(value).replace('₫', '') + 'đ'
                },
              },
              grid: { color: '#f0f0f0' },
            },
            x: { 
              grid: { display: false },
              ticks: {
                maxRotation: 45,
                minRotation: 45
              }
            },
          },
          plugins: {
            legend: { display: false },
            tooltip: {
              callbacks: {
                label: (ctx) => {
                  if (ctx.parsed.y !== null && ctx.parsed.y !== undefined) {
                    return 'Doanh thu: ' + formatCurrency(ctx.parsed.y)
                  }
                  return 'Doanh thu: 0 đ'
                },
              },
            },
          },
        },
      })
    </script>
  </body>
</html>
