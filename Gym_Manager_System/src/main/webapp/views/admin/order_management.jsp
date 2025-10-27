<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý đơn hàng - GymFit</title>

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

      /* Sidebar */
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

      /* Stats Grid */
      .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        gap: 20px;
        margin-bottom: 25px;
      }

      .stat-card {
        background: #fff;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
        display: flex;
        align-items: center;
        gap: 15px;
      }

      .stat-icon {
        width: 50px;
        height: 50px;
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.3rem;
        color: #fff;
      }

      .stat-info h3 {
        font-size: 1.8rem;
        font-weight: 700;
        color: var(--primary);
      }

      .stat-info p {
        font-size: 0.85rem;
        color: #5a6c7d;
      }

      /* Product Grid */
      .products-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
        gap: 20px;
        margin-top: 20px;
      }

      .product-card {
        background: #fff;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 2px 10px var(--shadow);
        transition: all 0.3s ease;
      }

      .product-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
      }

      .product-image {
        width: 100%;
        height: 200px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 3rem;
        color: #fff;
      }

      .product-body {
        padding: 15px;
      }

      .product-name {
        font-size: 1.1rem;
        font-weight: 700;
        color: var(--primary);
        margin-bottom: 8px;
      }

      .product-price {
        font-size: 1.3rem;
        color: var(--accent);
        font-weight: 700;
        margin-bottom: 8px;
      }

      .product-stock {
        font-size: 0.9rem;
        color: #5a6c7d;
        margin-bottom: 15px;
      }

      .product-actions {
        display: flex;
        gap: 8px;
      }

      .btn-small {
        padding: 8px 16px;
        font-size: 0.85rem;
        flex: 1;
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

      /* Table */
      .table-container {
        background: #fff;
        border-radius: 12px;
        box-shadow: 0 2px 10px var(--shadow);
        overflow: hidden;
      }

      .table {
        width: 100%;
        border-collapse: collapse;
      }

      .table thead {
        background: var(--gradient-primary);
        color: #fff;
      }

      .table th {
        padding: 15px;
        text-align: left;
        font-weight: 600;
      }

      .table td {
        padding: 15px;
        border-bottom: 1px solid #e0e0e0;
      }

      .table tbody tr:hover {
        background: #f8f9fa;
      }

      .badge {
        display: inline-block;
        padding: 5px 12px;
        border-radius: 20px;
        font-size: 0.8rem;
        font-weight: 600;
      }

      .badge-success {
        background: #27ae60;
        color: #fff;
      }

      .badge-warning {
        background: #f39c12;
        color: #fff;
      }

      .badge-danger {
        background: #e74c3c;
        color: #fff;
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
        .products-grid {
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
            href="${pageContext.request.contextPath}/views/admin/admin_home.jsp"
            class="sidebar-brand"
          >
            <i class="fas fa-dumbbell"></i>
            <span>FITZ GYM</span>
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
              href="${pageContext.request.contextPath}/views/admin/dashboard.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-home"></i><span>Trang chủ</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/profile.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-user-circle"></i><span>Profile của Admin</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/account_management.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users-cog"></i><span>Quản lý tài khoản</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/member_management.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-users"></i><span>Quản lý hội viên</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/service_schedule.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-calendar-alt"></i><span>Dịch vụ & Lịch tập</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/trainer_management.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-chalkboard-teacher"></i><span>Quản lý PT</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/order_management.jsp"
              class="sidebar-menu-link active"
            >
              <i class="fas fa-box"></i><span>Quản lý đơn hàng</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/payment_finance.jsp"
              class="sidebar-menu-link"
            >
              <i class="fas fa-money-bill-wave"></i
              ><span>Thanh toán & Tài chính</span>
            </a>
          </li>
          <li class="sidebar-menu-item">
            <a
              href="${pageContext.request.contextPath}/views/admin/reports.jsp"
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
          <h1><i class="fas fa-box"></i> Quản lý đơn hàng & Sản phẩm</h1>
          <div class="top-bar-actions">
            <a
              href="${pageContext.request.contextPath}/views/admin/dashboard.jsp"
              class="btn btn-outline"
            >
              <i class="fas fa-arrow-left"></i> Quay lại
            </a>
          </div>
        </div>

        <div class="content-area">
          <!-- Stats -->
          <div class="stats-grid">
            <div class="stat-card">
              <div
                class="stat-icon"
                style="
                  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                "
              >
                <i class="fas fa-box"></i>
              </div>
              <div class="stat-info">
                <h3>156</h3>
                <p>Sản phẩm</p>
              </div>
            </div>

            <div class="stat-card">
              <div
                class="stat-icon"
                style="
                  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
                "
              >
                <i class="fas fa-shopping-cart"></i>
              </div>
              <div class="stat-info">
                <h3>89</h3>
                <p>Đơn hàng tháng này</p>
              </div>
            </div>

            <div class="stat-card">
              <div class="stat-icon" style="background: var(--gradient-accent)">
                <i class="fas fa-dollar-sign"></i>
              </div>
              <div class="stat-info">
                <h3>45M</h3>
                <p>Doanh thu</p>
              </div>
            </div>

            <div class="stat-card">
              <div
                class="stat-icon"
                style="
                  background: linear-gradient(135deg, #f39c12 0%, #d68910 100%);
                "
              >
                <i class="fas fa-exclamation-triangle"></i>
              </div>
              <div class="stat-info">
                <h3>12</h3>
                <p>Sắp hết hàng</p>
              </div>
            </div>
          </div>

          <!-- Tabs -->
          <div class="tabs">
            <button class="tab active" onclick="switchTab('products')">
              <i class="fas fa-box"></i> Quản lý sản phẩm
            </button>
            <button class="tab" onclick="switchTab('orders')">
              <i class="fas fa-shopping-cart"></i> Đơn hàng
            </button>
          </div>

          <!-- Products Tab -->
          <div id="products" class="tab-content active">
            <div style="margin-bottom: 15px">
              <button class="btn">
                <i class="fas fa-plus"></i> Thêm sản phẩm mới
              </button>
            </div>

            <div class="products-grid">
              <!-- Product 1 -->
              <div class="product-card">
                <div class="product-image">
                  <i class="fas fa-dumbbell"></i>
                </div>
                <div class="product-body">
                  <div class="product-name">Whey Protein 1kg</div>
                  <div class="product-price">850.000đ</div>
                  <div class="product-stock">
                    <i class="fas fa-warehouse"></i> Tồn kho: 45
                  </div>
                  <div class="product-actions">
                    <button class="btn btn-small" style="background: #3498db">
                      <i class="fas fa-edit"></i> Sửa
                    </button>
                    <button class="btn btn-small" style="background: #e74c3c">
                      <i class="fas fa-trash"></i> Xóa
                    </button>
                  </div>
                </div>
              </div>

              <!-- Product 2 -->
              <div class="product-card">
                <div
                  class="product-image"
                  style="
                    background: linear-gradient(
                      135deg,
                      #11998e 0%,
                      #38ef7d 100%
                    );
                  "
                >
                  <i class="fas fa-bottle-water"></i>
                </div>
                <div class="product-body">
                  <div class="product-name">Shaker Bottle</div>
                  <div class="product-price">120.000đ</div>
                  <div class="product-stock">
                    <i class="fas fa-warehouse"></i> Tồn kho: 120
                  </div>
                  <div class="product-actions">
                    <button class="btn btn-small" style="background: #3498db">
                      <i class="fas fa-edit"></i> Sửa
                    </button>
                    <button class="btn btn-small" style="background: #e74c3c">
                      <i class="fas fa-trash"></i> Xóa
                    </button>
                  </div>
                </div>
              </div>

              <!-- Product 3 -->
              <div class="product-card">
                <div
                  class="product-image"
                  style="background: var(--gradient-accent)"
                >
                  <i class="fas fa-tshirt"></i>
                </div>
                <div class="product-body">
                  <div class="product-name">Gym T-Shirt</div>
                  <div class="product-price">250.000đ</div>
                  <div class="product-stock" style="color: #e74c3c">
                    <i class="fas fa-exclamation-triangle"></i> Tồn kho: 8 (Sắp
                    hết)
                  </div>
                  <div class="product-actions">
                    <button class="btn btn-small" style="background: #3498db">
                      <i class="fas fa-edit"></i> Sửa
                    </button>
                    <button class="btn btn-small" style="background: #e74c3c">
                      <i class="fas fa-trash"></i> Xóa
                    </button>
                  </div>
                </div>
              </div>

              <!-- Product 4 -->
              <div class="product-card">
                <div
                  class="product-image"
                  style="
                    background: linear-gradient(
                      135deg,
                      #f39c12 0%,
                      #d68910 100%
                    );
                  "
                >
                  <i class="fas fa-mitten"></i>
                </div>
                <div class="product-body">
                  <div class="product-name">Gym Gloves</div>
                  <div class="product-price">180.000đ</div>
                  <div class="product-stock">
                    <i class="fas fa-warehouse"></i> Tồn kho: 35
                  </div>
                  <div class="product-actions">
                    <button class="btn btn-small" style="background: #3498db">
                      <i class="fas fa-edit"></i> Sửa
                    </button>
                    <button class="btn btn-small" style="background: #e74c3c">
                      <i class="fas fa-trash"></i> Xóa
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Orders Tab -->
          <div id="orders" class="tab-content">
            <div class="table-container">
              <table class="table">
                <thead>
                  <tr>
                    <th>Mã ĐH</th>
                    <th>Khách hàng</th>
                    <th>Sản phẩm</th>
                    <th>Số lượng</th>
                    <th>Tổng tiền</th>
                    <th>Ngày đặt</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>#DH001</td>
                    <td>Nguyễn Văn A</td>
                    <td>Whey Protein 1kg</td>
                    <td>2</td>
                    <td>1.700.000đ</td>
                    <td>10/02/2025</td>
                    <td><span class="badge badge-success">Hoàn thành</span></td>
                    <td>
                      <button class="btn btn-small" style="background: #3498db">
                        <i class="fas fa-eye"></i>
                      </button>
                    </td>
                  </tr>
                  <tr>
                    <td>#DH002</td>
                    <td>Trần Thị B</td>
                    <td>Shaker Bottle</td>
                    <td>1</td>
                    <td>120.000đ</td>
                    <td>11/02/2025</td>
                    <td><span class="badge badge-warning">Đang giao</span></td>
                    <td>
                      <button class="btn btn-small" style="background: #3498db">
                        <i class="fas fa-eye"></i>
                      </button>
                    </td>
                  </tr>
                  <tr>
                    <td>#DH003</td>
                    <td>Lê Văn C</td>
                    <td>Gym T-Shirt</td>
                    <td>3</td>
                    <td>750.000đ</td>
                    <td>12/02/2025</td>
                    <td>
                      <span class="badge badge-danger">Chờ xác nhận</span>
                    </td>
                    <td>
                      <button class="btn btn-small" style="background: #3498db">
                        <i class="fas fa-eye"></i>
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </main>
    </div>

    <script>
      function switchTab(tabName) {
        const tabs = document.querySelectorAll('.tab-content');
        tabs.forEach((tab) => tab.classList.remove('active'));

        const tabButtons = document.querySelectorAll('.tab');
        tabButtons.forEach((btn) => btn.classList.remove('active'));

        document.getElementById(tabName).classList.add('active');
        event.target.closest('.tab').classList.add('active');
      }
    </script>
  </body>
</html>
