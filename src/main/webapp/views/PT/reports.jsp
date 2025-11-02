<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
        <button class="btn">
          <i class="fas fa-download"></i> Xuất báo cáo
        </button>
      </div>

      <!-- Filter Bar -->
      <div class="filter-bar">
        <div class="form-group">
          <label>Từ ngày</label>
          <input type="date" value="2025-09-01" />
        </div>
        <div class="form-group">
          <label>Đến ngày</label>
          <input type="date" value="2025-10-15" />
        </div>
        <div class="form-group">
          <label>Loại báo cáo</label>
          <select>
            <option>Tất cả</option>
            <option>Học viên</option>
            <option>Buổi tập</option>
            <option>Doanh thu</option>
          </select>
        </div>
        <button class="btn"><i class="fas fa-filter"></i> Lọc</button>
      </div>

      <!-- Stats Overview -->
      <div class="stats-overview">
        <div class="stat-card">
          <div class="stat-icon">
            <i class="fas fa-users"></i>
          </div>
          <div class="stat-number">24</div>
          <div class="stat-label">Tổng học viên phụ trách</div>
          <div class="stat-change positive">
            <i class="fas fa-arrow-up"></i>
            <span>+12% so với tháng trước</span>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon success">
            <i class="fas fa-calendar-check"></i>
          </div>
          <div class="stat-number">156</div>
          <div class="stat-label">Buổi tập hoàn thành (tháng này)</div>
          <div class="stat-change positive">
            <i class="fas fa-arrow-up"></i>
            <span>+8% so với tháng trước</span>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon warning">
            <i class="fas fa-times-circle"></i>
          </div>
          <div class="stat-number">8</div>
          <div class="stat-label">Buổi tập bị hủy</div>
          <div class="stat-change negative">
            <i class="fas fa-arrow-down"></i>
            <span>-5% so với tháng trước</span>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon info">
            <i class="fas fa-star"></i>
          </div>
          <div class="stat-number">4.8</div>
          <div class="stat-label">Đánh giá trung bình</div>
          <div class="stat-change positive">
            <i class="fas fa-arrow-up"></i>
            <span>+0.3 điểm</span>
          </div>
        </div>
      </div>

      <!-- Charts -->
      <div class="charts-grid">
        <div class="chart-card">
          <h3>
            <i class="fas fa-chart-bar"></i>
            Số buổi tập theo tháng
          </h3>
          <div class="chart-placeholder">
            Biểu đồ cột: Số buổi tập theo từng tháng
          </div>
        </div>

        <div class="chart-card">
          <h3>
            <i class="fas fa-chart-pie"></i>
            Phân bổ loại hình tập
          </h3>
          <div class="chart-placeholder">
            Biểu đồ tròn: Cardio, Strength, Yoga, etc.
          </div>
        </div>

        <div class="chart-card">
          <h3>
            <i class="fas fa-chart-line"></i>
            Xu hướng học viên mới
          </h3>
          <div class="chart-placeholder">
            Biểu đồ đường: Số học viên mới theo tháng
          </div>
        </div>

        <div class="chart-card">
          <h3>
            <i class="fas fa-chart-area"></i>
            Tỷ lệ hoàn thành buổi tập
          </h3>
          <div class="chart-placeholder">
            Biểu đồ vùng: % hoàn thành theo tuần
          </div>
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
            <tr>
              <td>14/10/2025</td>
              <td>9:00 - 10:00</td>
              <td>Nguyễn Văn A</td>
              <td>Cardio & Strength</td>
              <td><span class="badge completed">Hoàn thành</span></td>
              <td><span class="rating-stars">⭐⭐⭐⭐⭐</span></td>
            </tr>
            <tr>
              <td>14/10/2025</td>
              <td>15:00 - 16:00</td>
              <td>Trần Thị B</td>
              <td>Weight Loss</td>
              <td><span class="badge completed">Hoàn thành</span></td>
              <td><span class="rating-stars">⭐⭐⭐⭐⭐</span></td>
            </tr>
            <tr>
              <td>13/10/2025</td>
              <td>10:00 - 11:00</td>
              <td>Lê Văn C</td>
              <td>Bodybuilding</td>
              <td><span class="badge completed">Hoàn thành</span></td>
              <td><span class="rating-stars">⭐⭐⭐⭐</span></td>
            </tr>
            <tr>
              <td>12/10/2025</td>
              <td>8:00 - 9:00</td>
              <td>Phạm Thị D</td>
              <td>Yoga</td>
              <td><span class="badge cancelled">Đã hủy</span></td>
              <td>-</td>
            </tr>
            <tr>
              <td>11/10/2025</td>
              <td>16:00 - 17:00</td>
              <td>Hoàng Văn E</td>
              <td>Personal Training</td>
              <td><span class="badge completed">Hoàn thành</span></td>
              <td><span class="rating-stars">⭐⭐⭐⭐⭐</span></td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Achievements -->
      <div class="table-card">
        <h3><i class="fas fa-trophy"></i> Thành tích</h3>
        <div class="achievements">
          <div class="achievement-card">
            <div class="achievement-icon">🏆</div>
            <div class="achievement-title">Top PT tháng 9</div>
            <div class="achievement-desc">
              Huấn luyện viên xuất sắc nhất tháng
            </div>
          </div>
          <div class="achievement-card">
            <div class="achievement-icon">⭐</div>
            <div class="achievement-title">100+ Buổi tập</div>
            <div class="achievement-desc">Hoàn thành hơn 100 buổi tập</div>
          </div>
          <div class="achievement-card">
            <div class="achievement-icon">💪</div>
            <div class="achievement-title">20+ Học viên</div>
            <div class="achievement-desc">Đang phụ trách 20+ học viên</div>
          </div>
          <div class="achievement-card">
            <div class="achievement-icon">❤️</div>
            <div class="achievement-title">Đánh giá 4.8/5</div>
            <div class="achievement-desc">Được học viên đánh giá cao</div>
          </div>
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
  </body>
</html>








