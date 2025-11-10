<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
              href="${pageContext.request.contextPath}/admin/order-management"
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

            <div class="report-card">
              <div class="report-card-header">
                <h3 class="report-card-title">Tần suất check-in</h3>
                <div
                  class="report-card-icon"
                  style="background: var(--gradient-accent)"
                >
                  <i class="fas fa-calendar-check"></i>
                </div>
              </div>
              <div class="report-chart-container">
                <canvas id="checkinBarChart"></canvas>
              </div>
              <div class="report-summary">
                <div class="summary-item">
                  <div class="summary-label">Hôm nay</div>
                  <div class="summary-value">${summary.checkInsToday}</div>
                </div>
                <div class="summary-item">
                  <div class="summary-label">Trung bình/ngày</div>
                  <div class="summary-value">${summary.avgCheckIns}</div>
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

      // --- LẤY DỮ LIỆU TỪ SERVER ---
      const revenueDataRaw = '${revenueChartJson}'
      const packageDataRaw = '${packageChartJson}'
      let revenueData = [],
        packageData = []

      try {
        if (revenueDataRaw) revenueData = JSON.parse(revenueDataRaw)
        if (packageDataRaw) packageData = JSON.parse(packageDataRaw)
      } catch (e) {
        console.error('Lỗi parse JSON:', e)
      }

      // --- 1. BIỂU ĐỒ HỘI VIÊN (Dữ liệu giả lập) ---
      const memberCtx = document
        .getElementById('memberGrowthChart')
        .getContext('2d')
      const memberGradient = memberCtx.createLinearGradient(0, 0, 0, 200)
      memberGradient.addColorStop(0, 'rgba(102, 126, 234, 0.5)')
      memberGradient.addColorStop(1, 'rgba(118, 75, 162, 0.0)')

      new Chart(memberCtx, {
        type: 'line',
        data: {
          labels: ['T6', 'T7', 'T8', 'T9', 'T10', 'T11'],
          datasets: [
            {
              label: 'Hội viên mới',
              data: [5, 8, 12, 7, 15, 17], // Dummy data
              borderColor: '#667eea',
              backgroundColor: memberGradient,
              fill: true,
              tension: 0.4,
              pointRadius: 0, // Ẩn điểm cho gọn
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: false }, tooltip: { enabled: true } },
          scales: { x: { display: false }, y: { display: false } }, // Ẩn trục cho biểu đồ nhỏ
        },
      })

      // --- 2. BIỂU ĐỒ TRÒN (DOANH THU THEO GÓI) ---
      // Nếu không có dữ liệu thật, dùng dữ liệu giả để test hiển thị
      if (packageData.length === 0) {
        packageData = [{ label: 'Chưa có dữ liệu', value: 1 }] // Dummy để hiện vòng tròn trống
      }

      const packageCtx = document
        .getElementById('packagePieChart')
        .getContext('2d')
      new Chart(packageCtx, {
        type: 'doughnut',
        data: {
          labels: packageData.map((item) => item.label),
          datasets: [
            {
              data: packageData.map((item) => item.value),
              backgroundColor: [
                '#11998e',
                '#38ef7d',
                '#0575E6',
                '#ff9966',
                '#ec8b5a',
                '#e0e0e0',
              ],
              borderWidth: 1,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'bottom',
              labels: { boxWidth: 12, font: { size: 11 } },
            },
          },
        },
      })

      // --- 3. BIỂU ĐỒ CỘT (CHECK-IN - Dữ liệu giả lập) ---
      const checkinCtx = document
        .getElementById('checkinBarChart')
        .getContext('2d')
      new Chart(checkinCtx, {
        type: 'bar',
        data: {
          labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
          datasets: [
            {
              label: 'Check-in',
              data: [650, 720, 892, 810, 750, 980, 1024],
              backgroundColor: '#ec8b5a',
              borderRadius: 4,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: { x: { grid: { display: false } }, y: { display: false } },
          plugins: { legend: { display: false } },
        },
      })

      // --- 4. BIỂU ĐỒ ĐƯỜNG LỚN (DOANH THU 12 THÁNG) ---
      const revenueCtx = document
        .getElementById('revenueLineChart')
        .getContext('2d')
      const revenueGradient = revenueCtx.createLinearGradient(0, 0, 0, 400)
      revenueGradient.addColorStop(0, 'rgba(20, 26, 73, 0.5)')
      revenueGradient.addColorStop(1, 'rgba(20, 26, 73, 0.0)')

      new Chart(revenueCtx, {
        type: 'line',
        data: {
          labels: revenueData.map((item) => item.label),
          datasets: [
            {
              label: 'Doanh thu',
              data: revenueData.map((item) => item.value),
              borderColor: '#141a49',
              backgroundColor: revenueGradient,
              fill: true,
              tension: 0.4,
              pointBackgroundColor: '#ec8b5a',
              pointBorderColor: '#fff',
              pointHoverRadius: 6,
              pointRadius: 4,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
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
            x: { grid: { display: false } },
          },
          plugins: {
            legend: { display: false },
            tooltip: {
              callbacks: {
                label: (ctx) => 'Doanh thu: ' + formatCurrency(ctx.parsed.y),
              },
            },
          },
        },
      })
    </script>
  </body>
</html>
