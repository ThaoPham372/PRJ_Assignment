<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Admin Dashboard - Stamina Gym</title>

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
            class="nav-link active"
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
            class="nav-link"
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
            <i class="fas fa-tachometer-alt me-2"></i>Dashboard
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

      <!-- Dashboard Content -->
      <div class="container-fluid">
        <!-- Stats Cards Row -->
        <div class="row mb-4">
          <%-- Mock data for dashboard statistics using JSTL --%>
          <c:set var="totalMembers" value="1247" />
          <c:set var="activeMembers" value="892" />
          <c:set var="monthlyRevenue" value="125000000" />
          <c:set var="newMembersThisMonth" value="45" />

          <div class="col-xl-3 col-md-6 mb-4">
            <div class="stat-card">
              <div class="stat-icon bg-gradient-purple">
                <i class="fas fa-users"></i>
              </div>
              <div class="stat-number">${totalMembers}</div>
              <div class="stat-label">Tổng Thành Viên</div>
              <small class="text-success">
                <i class="fas fa-arrow-up me-1"></i>+${newMembersThisMonth}
                tháng này
              </small>
            </div>
          </div>

          <div class="col-xl-3 col-md-6 mb-4">
            <div class="stat-card">
              <div
                class="stat-icon"
                style="background: linear-gradient(135deg, #28a745, #20c997)"
              >
                <i class="fas fa-user-check"></i>
              </div>
              <div class="stat-number">${activeMembers}</div>
              <div class="stat-label">Thành Viên Hoạt Động</div>
              <small class="text-success">
                <i class="fas fa-arrow-up me-1"></i>+12% so với tháng trước
              </small>
            </div>
          </div>

          <div class="col-xl-3 col-md-6 mb-4">
            <div class="stat-card">
              <div
                class="stat-icon"
                style="background: linear-gradient(135deg, #ffc107, #fd7e14)"
              >
                <i class="fas fa-dollar-sign"></i>
              </div>
              <div class="stat-number">
                <fmt:formatNumber
                  value="${monthlyRevenue}"
                  type="currency"
                  currencySymbol="₫"
                  pattern="#,##0₫"
                />
              </div>
              <div class="stat-label">Doanh Thu Tháng</div>
              <small class="text-success">
                <i class="fas fa-arrow-up me-1"></i>+8.2% so với tháng trước
              </small>
            </div>
          </div>

          <div class="col-xl-3 col-md-6 mb-4">
            <div class="stat-card">
              <div
                class="stat-icon"
                style="background: linear-gradient(135deg, #17a2b8, #6f42c1)"
              >
                <i class="fas fa-calendar-plus"></i>
              </div>
              <div class="stat-number">${newMembersThisMonth}</div>
              <div class="stat-label">Thành Viên Mới</div>
              <small class="text-info">
                <i class="fas fa-info-circle me-1"></i>Trong tháng này
              </small>
            </div>
          </div>
        </div>

        <!-- Charts and Recent Activities Row -->
        <div class="row">
          <!-- Revenue Chart -->
          <div class="col-xl-8 col-lg-7 mb-4">
            <div class="card border-radius-custom shadow-custom">
              <div class="card-header bg-gradient-purple text-white">
                <h5 class="card-title mb-0">
                  <i class="fas fa-chart-area me-2"></i>Biểu Đồ Doanh Thu
                </h5>
              </div>
              <div class="card-body">
                <!-- Mock chart placeholder -->
                <canvas
                  id="revenueChart"
                  style="
                    height: 300px;
                    background: linear-gradient(45deg, #f8f9fa, #e9ecef);
                    border-radius: 10px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                  "
                >
                  <!-- This would be replaced with actual Chart.js implementation -->
                  <div style="text-align: center; padding: 50px">
                    <i class="fas fa-chart-line fa-3x text-primary mb-3"></i>
                    <h5 class="text-primary">Biểu Đồ Doanh Thu</h5>
                    <p class="text-muted">Chart.js sẽ được tích hợp ở đây</p>
                    <%-- Mock data for recent months using JSTL --%>
                    <c:set var="months" value="T1,T2,T3,T4,T5,T6" />
                    <c:set
                      var="revenues"
                      value="95M,108M,112M,125M,118M,125M"
                    />
                    <small class="text-muted">
                      Dữ liệu mẫu: ${revenues} (${months})
                    </small>
                  </div>
                </canvas>
              </div>
            </div>
          </div>

          <!-- Recent Activities -->
          <div class="col-xl-4 col-lg-5 mb-4">
            <div class="card border-radius-custom shadow-custom">
              <div class="card-header bg-gradient-purple text-white">
                <h5 class="card-title mb-0">
                  <i class="fas fa-bell me-2"></i>Hoạt Động Gần Đây
                </h5>
              </div>
              <div class="card-body">
                <%-- Mock data for recent activities using JSTL --%>
                <c:forEach var="i" begin="1" end="5">
                  <div
                    class="d-flex align-items-center mb-3 pb-3 ${i < 5 ? 'border-bottom' : ''}"
                  >
                    <div class="me-3">
                      <c:choose>
                        <c:when test="${i == 1}">
                          <div
                            class="stat-icon"
                            style="
                              width: 40px;
                              height: 40px;
                              font-size: 0.9rem;
                              background: var(--success);
                            "
                          >
                            <i class="fas fa-user-plus"></i>
                          </div>
                        </c:when>
                        <c:when test="${i == 2}">
                          <div
                            class="stat-icon"
                            style="
                              width: 40px;
                              height: 40px;
                              font-size: 0.9rem;
                              background: var(--warning);
                            "
                          >
                            <i class="fas fa-credit-card"></i>
                          </div>
                        </c:when>
                        <c:when test="${i == 3}">
                          <div
                            class="stat-icon"
                            style="
                              width: 40px;
                              height: 40px;
                              font-size: 0.9rem;
                              background: var(--info);
                            "
                          >
                            <i class="fas fa-dumbbell"></i>
                          </div>
                        </c:when>
                        <c:when test="${i == 4}">
                          <div
                            class="stat-icon"
                            style="
                              width: 40px;
                              height: 40px;
                              font-size: 0.9rem;
                              background: var(--primary-purple);
                            "
                          >
                            <i class="fas fa-calendar-check"></i>
                          </div>
                        </c:when>
                        <c:otherwise>
                          <div
                            class="stat-icon"
                            style="
                              width: 40px;
                              height: 40px;
                              font-size: 0.9rem;
                              background: var(--danger);
                            "
                          >
                            <i class="fas fa-exclamation-triangle"></i>
                          </div>
                        </c:otherwise>
                      </c:choose>
                    </div>
                    <div class="flex-grow-1">
                      <h6 class="mb-1">
                        <c:choose>
                          <c:when test="${i == 1}"
                            >Thành viên mới đăng ký</c:when
                          >
                          <c:when test="${i == 2}"
                            >Thanh toán thành công</c:when
                          >
                          <c:when test="${i == 3}"
                            >Thiết bị mới được thêm</c:when
                          >
                          <c:when test="${i == 4}">Lịch tập được đặt</c:when>
                          <c:otherwise>Cảnh báo bảo trì thiết bị</c:otherwise>
                        </c:choose>
                      </h6>
                      <small class="text-muted">
                        <c:choose>
                          <c:when test="${i == 1}"
                            >Nguyễn Văn A vừa đăng ký gói Premium</c:when
                          >
                          <c:when test="${i == 2}"
                            >Trần Thị B thanh toán 500K cho gói tháng</c:when
                          >
                          <c:when test="${i == 3}"
                            >Máy chạy bộ Model X được thêm vào khu
                            cardio</c:when
                          >
                          <c:when test="${i == 4}"
                            >Lê Văn C đặt lịch PT với HLV Minh</c:when
                          >
                          <c:otherwise
                            >Máy leg press cần bảo trì định kỳ</c:otherwise
                          >
                        </c:choose>
                      </small>
                      <div class="text-muted small">
                        <i class="fas fa-clock me-1"></i>
                        ${i == 1 ? '5 phút trước' : (i == 2 ? '15 phút trước' :
                        (i == 3 ? '1 giờ trước' : (i == 4 ? '2 giờ trước' : '3
                        giờ trước')))}
                      </div>
                    </div>
                  </div>
                </c:forEach>

                <div class="text-center mt-3">
                  <a href="#" class="btn btn-outline-primary btn-sm">
                    Xem Tất Cả <i class="fas fa-arrow-right ms-1"></i>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Quick Actions and Member Stats -->
        <div class="row">
          <!-- Quick Actions -->
          <div class="col-lg-6 mb-4">
            <div class="card border-radius-custom shadow-custom">
              <div class="card-header bg-gradient-purple text-white">
                <h5 class="card-title mb-0">
                  <i class="fas fa-bolt me-2"></i>Thao Tác Nhanh
                </h5>
              </div>
              <div class="card-body">
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <a
                      href="${pageContext.request.contextPath}/views/admin/members.jsp"
                      class="btn btn-outline-primary w-100 p-3"
                    >
                      <i class="fas fa-user-plus fa-2x mb-2 d-block"></i>
                      Thêm Thành Viên Mới
                    </a>
                  </div>
                  <div class="col-md-6 mb-3">
                    <a
                      href="${pageContext.request.contextPath}/views/admin/point-of-sale.jsp"
                      class="btn btn-outline-success w-100 p-3"
                    >
                      <i class="fas fa-cash-register fa-2x mb-2 d-block"></i>
                      Thanh Toán
                    </a>
                  </div>
                  <div class="col-md-6 mb-3">
                    <a
                      href="${pageContext.request.contextPath}/views/admin/coaches.jsp"
                      class="btn btn-outline-warning w-100 p-3"
                    >
                      <i class="fas fa-user-tie fa-2x mb-2 d-block"></i>
                      Quản Lý HLV
                    </a>
                  </div>
                  <div class="col-md-6 mb-3">
                    <a
                      href="${pageContext.request.contextPath}/views/admin/equipment.jsp"
                      class="btn btn-outline-info w-100 p-3"
                    >
                      <i class="fas fa-tools fa-2x mb-2 d-block"></i>
                      Kiểm Tra Thiết Bị
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Member Statistics -->
          <div class="col-lg-6 mb-4">
            <div class="card border-radius-custom shadow-custom">
              <div class="card-header bg-gradient-purple text-white">
                <h5 class="card-title mb-0">
                  <i class="fas fa-users me-2"></i>Thống Kê Thành Viên
                </h5>
              </div>
              <div class="card-body">
                <%-- Mock member statistics using JSTL --%>
                <c:set var="basicMembers" value="450" />
                <c:set var="premiumMembers" value="312" />
                <c:set var="vipMembers" value="128" />
                <c:set var="expiredMembers" value="357" />

                <div class="row text-center">
                  <div class="col-6 mb-3">
                    <h4 class="text-primary fw-bold">${basicMembers}</h4>
                    <small class="text-muted">Basic Members</small>
                  </div>
                  <div class="col-6 mb-3">
                    <h4 class="text-success fw-bold">${premiumMembers}</h4>
                    <small class="text-muted">Premium Members</small>
                  </div>
                  <div class="col-6 mb-3">
                    <h4 class="text-warning fw-bold">${vipMembers}</h4>
                    <small class="text-muted">VIP Members</small>
                  </div>
                  <div class="col-6 mb-3">
                    <h4 class="text-danger fw-bold">${expiredMembers}</h4>
                    <small class="text-muted">Hết Hạn</small>
                  </div>
                </div>

                <!-- Simple Progress Bars -->
                <div class="mt-3">
                  <div class="d-flex justify-content-between mb-1">
                    <small>Basic</small>
                    <small>${basicMembers}</small>
                  </div>
                  <div class="progress mb-3" style="height: 8px">
                    <div
                      class="progress-bar bg-primary"
                      style="width: ${(basicMembers * 100) / totalMembers}%"
                    ></div>
                  </div>

                  <div class="d-flex justify-content-between mb-1">
                    <small>Premium</small>
                    <small>${premiumMembers}</small>
                  </div>
                  <div class="progress mb-3" style="height: 8px">
                    <div
                      class="progress-bar bg-success"
                      style="width: ${(premiumMembers * 100) / totalMembers}%"
                    ></div>
                  </div>

                  <div class="d-flex justify-content-between mb-1">
                    <small>VIP</small>
                    <small>${vipMembers}</small>
                  </div>
                  <div class="progress" style="height: 8px">
                    <div
                      class="progress-bar bg-warning"
                      style="width: ${(vipMembers * 100) / totalMembers}%"
                    ></div>
                  </div>
                </div>
              </div>
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

        // Auto-refresh stats every 30 seconds (for demo)
        // In real app, this would make AJAX calls
        setInterval(function () {
          console.log('Auto-refreshing dashboard stats...');
          // updateDashboardStats();
        }, 30000);

        // Animate stats on page load
        const statNumbers = document.querySelectorAll('.stat-number');
        statNumbers.forEach(function (statNumber) {
          const finalValue = parseInt(
            statNumber.textContent.replace(/[^\d]/g, ''),
          );
          if (!isNaN(finalValue)) {
            animateValue(statNumber, 0, finalValue, 1500);
          }
        });
      });

      // Animate number counting effect
      function animateValue(element, start, end, duration) {
        let startTimestamp = null;
        const step = (timestamp) => {
          if (!startTimestamp) startTimestamp = timestamp;
          const progress = Math.min((timestamp - startTimestamp) / duration, 1);
          const currentValue = Math.floor(progress * (end - start) + start);

          // Preserve original formatting
          if (element.textContent.includes('₫')) {
            element.textContent = currentValue.toLocaleString('vi-VN') + '₫';
          } else {
            element.textContent = currentValue.toLocaleString('vi-VN');
          }

          if (progress < 1) {
            window.requestAnimationFrame(step);
          }
        };
        window.requestAnimationFrame(step);
      }
    </script>
  </body>
</html>
