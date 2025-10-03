<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Báo Cáo Doanh Thu - Stamina Gym</title>

    <!-- Bootstrap CSS -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <!-- Font Awesome -->
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
      rel="stylesheet"
    />
    <!-- Custom CSS -->
    <link
      href="${pageContext.request.contextPath}/css/styles.css"
      rel="stylesheet"
    />
  </head>
  <body>
    <!-- Admin Sidebar -->
    <nav class="admin-sidebar" id="sidebar">
      <div class="sidebar-header">
        <a href="#" class="sidebar-brand">
          <i class="fas fa-dumbbell me-2"></i>STAMINA GYM
        </a>
        <p class="text-center mb-0 opacity-75">Admin Panel</p>
      </div>

      <ul class="nav flex-column mt-4">
        <li class="nav-item">
          <a
            class="nav-link"
            href="${pageContext.request.contextPath}/views/admin/dashboard.jsp"
          >
            <i class="fas fa-tachometer-alt"></i>
            Dashboard
          </a>
        </li>
        <li class="nav-item">
          <a
            class="nav-link"
            href="${pageContext.request.contextPath}/views/admin/members.jsp"
          >
            <i class="fas fa-users"></i>
            Quản Lý Thành Viên
          </a>
        </li>
        <li class="nav-item">
          <a
            class="nav-link"
            href="${pageContext.request.contextPath}/views/admin/coaches.jsp"
          >
            <i class="fas fa-user-tie"></i>
            Huấn Luyện Viên
          </a>
        </li>
        <li class="nav-item">
          <a
            class="nav-link"
            href="${pageContext.request.contextPath}/views/admin/equipment.jsp"
          >
            <i class="fas fa-dumbbell"></i>
            Thiết Bị & Tồn Kho
          </a>
        </li>
        <li class="nav-item">
          <a
            class="nav-link active"
            href="${pageContext.request.contextPath}/views/admin/sales-report.jsp"
          >
            <i class="fas fa-chart-line"></i>
            Báo Cáo Doanh Thu
          </a>
        </li>
        <li class="nav-item">
          <a
            class="nav-link"
            href="${pageContext.request.contextPath}/views/admin/point-of-sale.jsp"
          >
            <i class="fas fa-cash-register"></i>
            Point of Sale
          </a>
        </li>
        <hr class="mx-3 my-3" style="border-color: rgba(255, 255, 255, 0.2)" />
        <li class="nav-item">
          <a
            class="nav-link"
            href="${pageContext.request.contextPath}/views/admin/settings.jsp"
          >
            <i class="fas fa-cog"></i>
            Cài Đặt
          </a>
        </li>
        <li class="nav-item">
          <a
            class="nav-link"
            href="${pageContext.request.contextPath}/index.jsp"
          >
            <i class="fas fa-home"></i>
            Về Trang Chủ
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/logout">
            <i class="fas fa-sign-out-alt"></i>
            Đăng Xuất
          </a>
        </li>
      </ul>
    </nav>

    <!-- Main Content -->
    <div class="admin-content">
      <!-- Top Navigation -->
      <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm mb-4">
        <div class="container-fluid">
          <button
            class="btn btn-outline-primary d-lg-none"
            type="button"
            id="sidebarToggle"
          >
            <i class="fas fa-bars"></i>
          </button>

          <h4 class="navbar-brand mb-0 fw-bold text-primary">
            <i class="fas fa-chart-line me-2"></i>Báo Cáo Doanh Thu
          </h4>

          <div class="navbar-nav ms-auto">
            <div class="nav-item dropdown">
              <a
                class="nav-link dropdown-toggle d-flex align-items-center"
                href="#"
                id="userDropdown"
                role="button"
                data-bs-toggle="dropdown"
              >
                <img
                  src="https://via.placeholder.com/32x32/3B1E78/FFD700?text=A"
                  class="rounded-circle me-2"
                  alt="Admin"
                />
                <span class="fw-semibold">Admin User</span>
              </a>
              <ul class="dropdown-menu dropdown-menu-end">
                <li>
                  <a class="dropdown-item" href="#"
                    ><i class="fas fa-user me-2"></i>Hồ Sơ</a
                  >
                </li>
                <li>
                  <a class="dropdown-item" href="#"
                    ><i class="fas fa-cog me-2"></i>Cài Đặt</a
                  >
                </li>
                <li><hr class="dropdown-divider" /></li>
                <li>
                  <a
                    class="dropdown-item"
                    href="${pageContext.request.contextPath}/logout"
                  >
                    <i class="fas fa-sign-out-alt me-2"></i>Đăng Xuất</a
                  >
                </li>
              </ul>
            </div>
          </div>
        </div>
      </nav>

      <!-- Page Header -->
      <div class="page-header">
        <div class="container-fluid">
          <div class="row align-items-center">
            <div class="col">
              <h1 class="page-title">Báo Cáo Doanh Thu</h1>
              <p class="page-subtitle">
                Theo dõi và phân tích doanh thu kinh doanh
              </p>
            </div>
            <div class="col-auto">
              <button class="btn btn-secondary me-2" onclick="exportReport()">
                <i class="fas fa-download me-2"></i>Xuất Báo Cáo
              </button>
              <button class="btn btn-primary" onclick="printReport()">
                <i class="fas fa-print me-2"></i>In Báo Cáo
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Sales Report Content -->
      <div class="container-fluid">
        <!-- Date Filter -->
        <div class="card border-radius-custom shadow-custom mb-4">
          <div class="card-body">
            <form class="row g-3" id="dateFilterForm">
              <div class="col-md-3">
                <label class="form-label">Từ Ngày</label>
                <input
                  type="date"
                  class="form-control"
                  id="fromDate"
                  value="2024-09-01"
                />
              </div>
              <div class="col-md-3">
                <label class="form-label">Đến Ngày</label>
                <input
                  type="date"
                  class="form-control"
                  id="toDate"
                  value="2024-09-30"
                />
              </div>
              <div class="col-md-3">
                <label class="form-label">Loại Báo Cáo</label>
                <select class="form-control" id="reportType">
                  <option value="daily">Theo Ngày</option>
                  <option value="weekly">Theo Tuần</option>
                  <option value="monthly" selected>Theo Tháng</option>
                  <option value="yearly">Theo Năm</option>
                </select>
              </div>
              <div class="col-md-3 d-flex align-items-end">
                <button type="submit" class="btn btn-primary w-100">
                  <i class="fas fa-search me-1"></i>Tạo Báo Cáo
                </button>
              </div>
            </form>
          </div>
        </div>

        <!-- Revenue Summary Cards -->
        <div class="row mb-4">
          <c:set var="totalRevenue" value="450000000" />
          <c:set var="membershipRevenue" value="350000000" />
          <c:set var="ptRevenue" value="75000000" />
          <c:set var="otherRevenue" value="25000000" />

          <div class="col-xl-3 col-md-6 mb-3">
            <div class="stat-card">
              <div class="stat-icon bg-gradient-purple">
                <i class="fas fa-dollar-sign"></i>
              </div>
              <div class="stat-number">
                <fmt:formatNumber
                  value="${totalRevenue}"
                  type="currency"
                  currencySymbol="₫"
                  pattern="#,##0₫"
                />
              </div>
              <div class="stat-label">Tổng Doanh Thu</div>
              <small class="text-success">
                <i class="fas fa-arrow-up me-1"></i>+12.5% so với tháng trước
              </small>
            </div>
          </div>
          <div class="col-xl-3 col-md-6 mb-3">
            <div class="stat-card">
              <div
                class="stat-icon"
                style="background: linear-gradient(135deg, #28a745, #20c997)"
              >
                <i class="fas fa-users"></i>
              </div>
              <div class="stat-number">
                <fmt:formatNumber
                  value="${membershipRevenue}"
                  type="currency"
                  currencySymbol="₫"
                  pattern="#,##0₫"
                />
              </div>
              <div class="stat-label">Doanh Thu Membership</div>
              <small class="text-success">
                <i class="fas fa-arrow-up me-1"></i>+8.2% so với tháng trước
              </small>
            </div>
          </div>
          <div class="col-xl-3 col-md-6 mb-3">
            <div class="stat-card">
              <div
                class="stat-icon"
                style="background: linear-gradient(135deg, #17a2b8, #6f42c1)"
              >
                <i class="fas fa-user-tie"></i>
              </div>
              <div class="stat-number">
                <fmt:formatNumber
                  value="${ptRevenue}"
                  type="currency"
                  currencySymbol="₫"
                  pattern="#,##0₫"
                />
              </div>
              <div class="stat-label">Doanh Thu PT</div>
              <small class="text-success">
                <i class="fas fa-arrow-up me-1"></i>+15.7% so với tháng trước
              </small>
            </div>
          </div>
          <div class="col-xl-3 col-md-6 mb-3">
            <div class="stat-card">
              <div
                class="stat-icon"
                style="background: linear-gradient(135deg, #ffc107, #fd7e14)"
              >
                <i class="fas fa-plus-circle"></i>
              </div>
              <div class="stat-number">
                <fmt:formatNumber
                  value="${otherRevenue}"
                  type="currency"
                  currencySymbol="₫"
                  pattern="#,##0₫"
                />
              </div>
              <div class="stat-label">Doanh Thu Khác</div>
              <small class="text-info">
                <i class="fas fa-info-circle me-1"></i>Phụ kiện, nước uống
              </small>
            </div>
          </div>
        </div>

        <!-- Charts Row -->
        <div class="row mb-4">
          <!-- Revenue Trend Chart -->
          <div class="col-lg-8 mb-4">
            <div class="card border-radius-custom shadow-custom">
              <div class="card-header bg-gradient-purple text-white">
                <h5 class="card-title mb-0">
                  <i class="fas fa-chart-line me-2"></i>Xu Hướng Doanh Thu
                </h5>
              </div>
              <div class="card-body">
                <canvas
                  id="revenueChart"
                  style="
                    height: 350px;
                    background: linear-gradient(45deg, #f8f9fa, #e9ecef);
                    border-radius: 10px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                  "
                >
                  <div style="text-align: center; padding: 50px">
                    <i class="fas fa-chart-line fa-4x text-primary mb-3"></i>
                    <h5 class="text-primary">Biểu Đồ Xu Hướng Doanh Thu</h5>
                    <p class="text-muted">
                      Chart.js hoặc D3.js sẽ được tích hợp ở đây
                    </p>
                    <%-- Mock revenue trend data using JSTL --%>
                    <c:set var="months" value="T1,T2,T3,T4,T5,T6,T7,T8,T9" />
                    <c:set
                      var="revenues"
                      value="380M,395M,410M,425M,440M,455M,420M,435M,450M"
                    />
                    <small class="text-muted">
                      Dữ liệu mẫu: ${revenues} (${months})
                    </small>
                  </div>
                </canvas>
              </div>
            </div>
          </div>

          <!-- Revenue by Package Type -->
          <div class="col-lg-4 mb-4">
            <div class="card border-radius-custom shadow-custom">
              <div class="card-header bg-gradient-purple text-white">
                <h5 class="card-title mb-0">
                  <i class="fas fa-chart-pie me-2"></i>Doanh Thu Theo Gói
                </h5>
              </div>
              <div class="card-body">
                <%-- Mock package revenue data using JSTL --%>
                <c:set var="basicRevenue" value="120000000" />
                <c:set var="premiumRevenue" value="180000000" />
                <c:set var="vipRevenue" value="150000000" />

                <div class="mb-3">
                  <div
                    class="d-flex justify-content-between align-items-center mb-2"
                  >
                    <span class="fw-semibold">Basic Package</span>
                    <span class="text-primary fw-bold">
                      <fmt:formatNumber
                        value="${basicRevenue}"
                        type="currency"
                        currencySymbol="₫"
                        pattern="#,##0₫"
                      />
                    </span>
                  </div>
                  <div class="progress mb-2" style="height: 10px">
                    <div
                      class="progress-bar bg-primary"
                      style="width: ${(basicRevenue * 100) / membershipRevenue}%"
                    ></div>
                  </div>
                  <small class="text-muted"
                    >${String.format("%.1f", (basicRevenue * 100.0) /
                    membershipRevenue)}% tổng doanh thu membership</small
                  >
                </div>

                <div class="mb-3">
                  <div
                    class="d-flex justify-content-between align-items-center mb-2"
                  >
                    <span class="fw-semibold">Premium Package</span>
                    <span class="text-success fw-bold">
                      <fmt:formatNumber
                        value="${premiumRevenue}"
                        type="currency"
                        currencySymbol="₫"
                        pattern="#,##0₫"
                      />
                    </span>
                  </div>
                  <div class="progress mb-2" style="height: 10px">
                    <div
                      class="progress-bar bg-success"
                      style="width: ${(premiumRevenue * 100) / membershipRevenue}%"
                    ></div>
                  </div>
                  <small class="text-muted"
                    >${String.format("%.1f", (premiumRevenue * 100.0) /
                    membershipRevenue)}% tổng doanh thu membership</small
                  >
                </div>

                <div class="mb-3">
                  <div
                    class="d-flex justify-content-between align-items-center mb-2"
                  >
                    <span class="fw-semibold">VIP Package</span>
                    <span class="text-warning fw-bold">
                      <fmt:formatNumber
                        value="${vipRevenue}"
                        type="currency"
                        currencySymbol="₫"
                        pattern="#,##0₫"
                      />
                    </span>
                  </div>
                  <div class="progress mb-2" style="height: 10px">
                    <div
                      class="progress-bar bg-warning"
                      style="width: ${(vipRevenue * 100) / membershipRevenue}%"
                    ></div>
                  </div>
                  <small class="text-muted"
                    >${String.format("%.1f", (vipRevenue * 100.0) /
                    membershipRevenue)}% tổng doanh thu membership</small
                  >
                </div>

                <div class="text-center mt-4">
                  <canvas
                    id="packagePieChart"
                    style="
                      height: 200px;
                      background: linear-gradient(45deg, #f8f9fa, #e9ecef);
                      border-radius: 10px;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                    "
                  >
                    <div style="text-align: center; padding: 30px">
                      <i class="fas fa-chart-pie fa-3x text-success mb-2"></i>
                      <p class="text-muted small">
                        Pie Chart sẽ hiển thị ở đây
                      </p>
                    </div>
                  </canvas>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Detailed Sales Table -->
        <div class="card border-radius-custom shadow-custom">
          <div class="card-header bg-gradient-purple text-white">
            <h5 class="card-title mb-0">
              <i class="fas fa-table me-2"></i>Chi Tiết Doanh Thu Theo Ngày
            </h5>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-hover mb-0" id="salesTable">
                <thead>
                  <tr>
                    <th>Ngày</th>
                    <th>Membership</th>
                    <th>Personal Training</th>
                    <th>Phụ Kiện</th>
                    <th>Nước Uống</th>
                    <th>Tổng Cộng</th>
                    <th>Ghi Chú</th>
                  </tr>
                </thead>
                <tbody>
                  <%-- Mock daily sales data using JSTL forEach --%>
                  <c:forEach var="day" begin="1" end="15">
                    <tr>
                      <td>
                        <strong>2024-09-${String.format("%02d", day)}</strong>
                      </td>
                      <td>
                        <c:set
                          var="membershipDaily"
                          value="${8000000 + (day * 500000)}"
                        />
                        <fmt:formatNumber
                          value="${membershipDaily}"
                          type="currency"
                          currencySymbol="₫"
                          pattern="#,##0₫"
                        />
                      </td>
                      <td>
                        <c:set
                          var="ptDaily"
                          value="${1500000 + (day * 100000)}"
                        />
                        <fmt:formatNumber
                          value="${ptDaily}"
                          type="currency"
                          currencySymbol="₫"
                          pattern="#,##0₫"
                        />
                      </td>
                      <td>
                        <c:set
                          var="accessoryDaily"
                          value="${300000 + (day * 50000)}"
                        />
                        <fmt:formatNumber
                          value="${accessoryDaily}"
                          type="currency"
                          currencySymbol="₫"
                          pattern="#,##0₫"
                        />
                      </td>
                      <td>
                        <c:set
                          var="beverageDaily"
                          value="${150000 + (day * 25000)}"
                        />
                        <fmt:formatNumber
                          value="${beverageDaily}"
                          type="currency"
                          currencySymbol="₫"
                          pattern="#,##0₫"
                        />
                      </td>
                      <td class="fw-bold text-success">
                        <c:set
                          var="totalDaily"
                          value="${membershipDaily + ptDaily + accessoryDaily + beverageDaily}"
                        />
                        <fmt:formatNumber
                          value="${totalDaily}"
                          type="currency"
                          currencySymbol="₫"
                          pattern="#,##0₫"
                        />
                      </td>
                      <td>
                        <c:choose>
                          <c:when test="${day % 7 == 0 || day % 7 == 6}">
                            <span class="badge bg-warning">Cuối tuần</span>
                          </c:when>
                          <c:when test="${day == 15}">
                            <span class="badge bg-success">Khuyến mãi</span>
                          </c:when>
                          <c:otherwise>
                            <span class="badge bg-info">Bình thường</span>
                          </c:otherwise>
                        </c:choose>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
                <tfoot class="table-light">
                  <tr class="fw-bold">
                    <td>Tổng Cộng</td>
                    <td class="text-primary">
                      <fmt:formatNumber
                        value="${membershipRevenue}"
                        type="currency"
                        currencySymbol="₫"
                        pattern="#,##0₫"
                      />
                    </td>
                    <td class="text-success">
                      <fmt:formatNumber
                        value="${ptRevenue}"
                        type="currency"
                        currencySymbol="₫"
                        pattern="#,##0₫"
                      />
                    </td>
                    <td class="text-warning">
                      <fmt:formatNumber
                        value="${15000000}"
                        type="currency"
                        currencySymbol="₫"
                        pattern="#,##0₫"
                      />
                    </td>
                    <td class="text-info">
                      <fmt:formatNumber
                        value="${10000000}"
                        type="currency"
                        currencySymbol="₫"
                        pattern="#,##0₫"
                      />
                    </td>
                    <td class="text-danger">
                      <fmt:formatNumber
                        value="${totalRevenue}"
                        type="currency"
                        currencySymbol="₫"
                        pattern="#,##0₫"
                      />
                    </td>
                    <td>15 ngày</td>
                  </tr>
                </tfoot>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
      document.addEventListener('DOMContentLoaded', function () {
        // Sidebar toggle for mobile
        const sidebarToggle = document.getElementById('sidebarToggle');
        const sidebar = document.getElementById('sidebar');

        if (sidebarToggle) {
          sidebarToggle.addEventListener('click', function () {
            sidebar.classList.toggle('show');
          });
        }

        // Date filter form
        const dateFilterForm = document.getElementById('dateFilterForm');
        dateFilterForm.addEventListener('submit', function (e) {
          e.preventDefault();
          generateReport();
        });

        // Initialize with current month data
        generateReport();
      });

      function generateReport() {
        const fromDate = document.getElementById('fromDate').value;
        const toDate = document.getElementById('toDate').value;
        const reportType = document.getElementById('reportType').value;

        console.log('Generating report:', { fromDate, toDate, reportType });

        // In real app, this would make AJAX call to backend
        alert(
          'Đang tạo báo cáo từ ' +
            fromDate +
            ' đến ' +
            toDate +
            ' theo ' +
            reportType,
        );

        // Simulate loading and updating the table/charts
        updateCharts();
      }

      function updateCharts() {
        // In real app, this would update Chart.js charts with new data
        console.log('Updating charts with new data...');

        // Animate revenue numbers
        const statNumbers = document.querySelectorAll('.stat-number');
        statNumbers.forEach(function (statNumber) {
          statNumber.style.transform = 'scale(1.1)';
          setTimeout(() => {
            statNumber.style.transform = 'scale(1)';
          }, 200);
        });
      }

      function exportReport() {
        const fromDate = document.getElementById('fromDate').value;
        const toDate = document.getElementById('toDate').value;

        alert(
          'Đang xuất báo cáo doanh thu từ ' +
            fromDate +
            ' đến ' +
            toDate +
            ' định dạng Excel...',
        );
        // In real app, this would generate and download Excel file
      }

      function printReport() {
        // Hide sidebar and buttons for printing
        const sidebar = document.getElementById('sidebar');
        const buttons = document.querySelectorAll('.btn');

        sidebar.style.display = 'none';
        buttons.forEach((btn) => (btn.style.display = 'none'));

        window.print();

        // Restore after printing
        setTimeout(() => {
          sidebar.style.display = '';
          buttons.forEach((btn) => (btn.style.display = ''));
        }, 1000);
      }
    </script>
  </body>
</html>
