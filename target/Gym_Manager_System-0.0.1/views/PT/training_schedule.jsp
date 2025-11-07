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

      .session-item.completed {
        background: var(--success);
      }

      .session-item.cancelled {
        background: var(--danger);
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
          <div class="current-month" id="currentMonth">Tháng 10, 2025</div>
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
          <!-- Days will be generated by JavaScript -->
          <div class="calendar-day">
            <div class="day-number">14</div>
            <div class="session-item">9:00 - Nguyễn Văn A</div>
            <div class="session-item">15:00 - Trần Thị B</div>
          </div>
          <div class="calendar-day today">
            <div class="day-number">15</div>
            <div class="session-item pending">
              8:00 - Lê Văn C (Chờ xác nhận)
            </div>
            <div class="session-item">10:00 - Phạm Thị D</div>
            <div class="session-item">14:00 - Hoàng Văn E</div>
          </div>
          <div class="calendar-day">
            <div class="day-number">16</div>
            <div class="session-item">7:00 - Vũ Thị F</div>
          </div>
          <div class="calendar-day">
            <div class="day-number">17</div>
            <div class="session-item">9:00 - Đỗ Văn G</div>
            <div class="session-item completed">16:00 - Ngô Thị H</div>
          </div>
          <div class="calendar-day">
            <div class="day-number">18</div>
          </div>
          <div class="calendar-day">
            <div class="day-number">19</div>
            <div class="session-item">10:00 - Bùi Văn I</div>
          </div>
          <div class="calendar-day">
            <div class="day-number">20</div>
            <div class="session-item cancelled">9:00 - Đinh Thị K (Đã hủy)</div>
          </div>
        </div>
      </div>

      <!-- List View -->
      <div id="listView" class="session-list" style="display: none">
        <h3>Danh sách buổi tập</h3>

        <div class="session-card">
          <div class="session-header">
            <div class="session-title">Buổi tập với Nguyễn Văn A</div>
            <span class="status-badge confirmed">Đã xác nhận</span>
          </div>
          <div class="session-time">
            <i class="fas fa-clock"></i> Thứ 2, 14/10/2025 - 9:00 - 10:00
          </div>
          <div class="session-info">
            <div class="info-item">
              <i class="fas fa-user"></i>
              <span>Nguyễn Văn A</span>
            </div>
            <div class="info-item">
              <i class="fas fa-dumbbell"></i>
              <span>Cardio & Strength</span>
            </div>
            <div class="info-item">
              <i class="fas fa-map-marker-alt"></i>
              <span>Phòng 1</span>
            </div>
          </div>
          <div class="session-actions">
            <button class="btn btn-success btn-sm">
              <i class="fas fa-check"></i> Hoàn thành
            </button>
            <button class="btn btn-info btn-sm">
              <i class="fas fa-edit"></i> Chỉnh sửa
            </button>
            <button class="btn btn-danger btn-sm">
              <i class="fas fa-times"></i> Hủy
            </button>
          </div>
        </div>

        <div class="session-card">
          <div class="session-header">
            <div class="session-title">Buổi tập với Lê Văn C</div>
            <span class="status-badge pending">Chờ xác nhận</span>
          </div>
          <div class="session-time">
            <i class="fas fa-clock"></i> Thứ 3, 15/10/2025 - 8:00 - 9:00
          </div>
          <div class="session-info">
            <div class="info-item">
              <i class="fas fa-user"></i>
              <span>Lê Văn C</span>
            </div>
            <div class="info-item">
              <i class="fas fa-dumbbell"></i>
              <span>Yoga</span>
            </div>
            <div class="info-item">
              <i class="fas fa-map-marker-alt"></i>
              <span>Phòng 2</span>
            </div>
          </div>
          <div class="session-actions">
            <button class="btn btn-success btn-sm">
              <i class="fas fa-check"></i> Xác nhận
            </button>
            <button class="btn btn-danger btn-sm">
              <i class="fas fa-times"></i> Từ chối
            </button>
          </div>
        </div>

        <div class="session-card">
          <div class="session-header">
            <div class="session-title">Buổi tập với Phạm Thị D</div>
            <span class="status-badge confirmed">Đã xác nhận</span>
          </div>
          <div class="session-time">
            <i class="fas fa-clock"></i> Thứ 3, 15/10/2025 - 10:00 - 11:00
          </div>
          <div class="session-info">
            <div class="info-item">
              <i class="fas fa-user"></i>
              <span>Phạm Thị D</span>
            </div>
            <div class="info-item">
              <i class="fas fa-dumbbell"></i>
              <span>Weight Loss Program</span>
            </div>
            <div class="info-item">
              <i class="fas fa-map-marker-alt"></i>
              <span>Phòng 3</span>
            </div>
          </div>
          <div class="session-actions">
            <button class="btn btn-success btn-sm">
              <i class="fas fa-check"></i> Hoàn thành
            </button>
            <button class="btn btn-info btn-sm">
              <i class="fas fa-edit"></i> Chỉnh sửa
            </button>
            <button class="btn btn-danger btn-sm">
              <i class="fas fa-times"></i> Hủy
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal tạo buổi tập -->
    <div id="sessionModal" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <h2><i class="fas fa-plus-circle"></i> Tạo buổi tập mới</h2>
          <button class="close-btn" onclick="closeModal()">&times;</button>
        </div>
        <form onsubmit="createSession(event)">
          <div class="form-group">
            <label>Học viên</label>
            <select required>
              <option value="">Chọn học viên</option>
              <option>Nguyễn Văn A</option>
              <option>Trần Thị B</option>
              <option>Lê Văn C</option>
            </select>
          </div>
          <div class="form-group">
            <label>Ngày tập</label>
            <input type="date" required />
          </div>
          <div class="form-group">
            <label>Giờ bắt đầu</label>
            <input type="time" required />
          </div>
          <div class="form-group">
            <label>Giờ kết thúc</label>
            <input type="time" required />
          </div>
          <div class="form-group">
            <label>Loại tập</label>
            <select required>
              <option value="">Chọn loại tập</option>
              <option>Cardio</option>
              <option>Strength Training</option>
              <option>Yoga</option>
              <option>Weight Loss</option>
              <option>Bodybuilding</option>
            </select>
          </div>
          <div class="form-group">
            <label>Địa điểm</label>
            <input type="text" placeholder="Nhập phòng tập" required />
          </div>
          <div class="form-group">
            <label>Ghi chú</label>
            <textarea
              rows="3"
              placeholder="Nhập ghi chú (không bắt buộc)"
            ></textarea>
          </div>
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
              <i class="fas fa-save"></i> Tạo buổi tập
            </button>
          </div>
        </form>
      </div>
    </div>

    <script>
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

      function previousMonth() {
        alert('Chuyển về tháng trước');
      }

      function nextMonth() {
        alert('Chuyển sang tháng sau');
      }

      function openModal() {
        document.getElementById('sessionModal').classList.add('active');
      }

      function closeModal() {
        document.getElementById('sessionModal').classList.remove('active');
      }

      function createSession(e) {
        e.preventDefault();
        alert('Tạo buổi tập thành công!');
        closeModal();
      }
    </script>
  </body>
</html>

