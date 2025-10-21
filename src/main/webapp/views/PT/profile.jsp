<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Hồ sơ cá nhân - PT</title>
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
        max-width: 1200px;
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
        transition: color 0.3s;
      }

      .breadcrumb a:hover {
        color: var(--accent);
      }

      .profile-grid {
        display: grid;
        grid-template-columns: 350px 1fr;
        gap: 30px;
      }

      .profile-sidebar {
        background: var(--card);
        border-radius: 20px;
        padding: 40px;
        box-shadow: 0 8px 30px var(--shadow);
        text-align: center;
        height: fit-content;
      }

      .avatar-container {
        position: relative;
        width: 150px;
        height: 150px;
        margin: 0 auto 20px;
      }

      .avatar {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 4rem;
        color: #fff;
        box-shadow: 0 8px 30px rgba(236, 139, 90, 0.4);
      }

      .avatar-upload {
        position: absolute;
        bottom: 5px;
        right: 5px;
        background: var(--accent);
        color: #fff;
        width: 40px;
        height: 40px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: all 0.3s;
      }

      .avatar-upload:hover {
        transform: scale(1.1);
        box-shadow: 0 4px 15px rgba(236, 139, 90, 0.5);
      }

      .profile-sidebar h2 {
        font-size: 1.8rem;
        color: var(--primary);
        margin-bottom: 5px;
      }

      .profile-sidebar .role {
        color: var(--accent);
        font-weight: 600;
        margin-bottom: 20px;
      }

      .profile-stats {
        display: flex;
        flex-direction: column;
        gap: 15px;
        margin-top: 30px;
      }

      .stat-item {
        display: flex;
        justify-content: space-between;
        padding: 12px 0;
        border-bottom: 1px solid #f0f0f0;
      }

      .stat-label {
        color: var(--text-light);
        font-size: 0.9rem;
      }

      .stat-value {
        font-weight: 700;
        color: var(--primary);
      }

      .profile-main {
        display: flex;
        flex-direction: column;
        gap: 30px;
      }

      .card {
        background: var(--card);
        border-radius: 20px;
        padding: 35px;
        box-shadow: 0 8px 30px var(--shadow);
      }

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 25px;
        padding-bottom: 15px;
        border-bottom: 2px solid #f0f0f0;
      }

      .card-header h3 {
        font-size: 1.5rem;
        color: var(--primary);
      }

      .btn {
        background: var(--gradient-accent);
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 10px 20px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s;
        display: inline-flex;
        align-items: center;
        gap: 8px;
      }

      .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 90, 0.4);
      }

      .btn-secondary {
        background: #6c757d;
      }

      .form-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20px;
      }

      .form-group {
        display: flex;
        flex-direction: column;
        gap: 8px;
      }

      .form-group.full-width {
        grid-column: 1 / -1;
      }

      .form-group label {
        font-weight: 600;
        color: var(--text);
        font-size: 0.95rem;
      }

      .form-group input,
      .form-group textarea {
        padding: 12px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 1rem;
        transition: all 0.3s;
      }

      .form-group input:focus,
      .form-group textarea:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 3px rgba(236, 139, 90, 0.1);
      }

      .form-group input:disabled {
        background: #f5f5f5;
        cursor: not-allowed;
      }

      .info-display {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20px;
      }

      .info-item {
        padding: 15px;
        background: #f8f9fa;
        border-radius: 10px;
        border-left: 4px solid var(--accent);
      }

      .info-label {
        font-size: 0.85rem;
        color: var(--text-light);
        margin-bottom: 5px;
      }

      .info-value {
        font-size: 1.1rem;
        font-weight: 600;
        color: var(--primary);
      }

      .back-btn {
        background: #6c757d;
        margin-bottom: 20px;
      }

      @media (max-width: 968px) {
        .profile-grid {
          grid-template-columns: 1fr;
        }

        .form-grid,
        .info-display {
          grid-template-columns: 1fr;
        }
      }
    </style>
  </head>
  <body>
    <div class="container">
      <a
        href="${pageContext.request.contextPath}/views/PT/homePT.jsp"
        class="btn back-btn"
      >
        <i class="fas fa-arrow-left"></i> Quay lại
      </a>

      <div class="page-header">
        <h1><i class="fas fa-user-circle"></i> Hồ sơ cá nhân</h1>
        <div class="breadcrumb">
          <a href="${pageContext.request.contextPath}/views/PT/homePT.jsp"
            >Home</a
          >
          <span>/</span>
          <span>Hồ sơ cá nhân</span>
        </div>
      </div>

      <div class="profile-grid">
        <!-- SIDEBAR -->
        <div class="profile-sidebar">
          <div class="avatar-container">
            <div class="avatar">
              <i class="fas fa-dumbbell"></i>
            </div>
            <div class="avatar-upload" title="Thay đổi avatar">
              <i class="fas fa-camera"></i>
            </div>
          </div>
          <h2>
            ${sessionScope.user != null ? sessionScope.user.fullName : 'Nguyễn
            Văn An'}
          </h2>
          <p class="role">Personal Trainer</p>

          <div class="profile-stats">
            <div class="stat-item">
              <span class="stat-label">Học viên phụ trách</span>
              <span class="stat-value">24</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">Buổi tập tháng này</span>
              <span class="stat-value">156</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">Đánh giá trung bình</span>
              <span class="stat-value">4.8 ⭐</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">Thâm niên</span>
              <span class="stat-value">3 năm</span>
            </div>
          </div>
        </div>

        <!-- MAIN CONTENT -->
        <div class="profile-main">
          <!-- Thông tin cá nhân -->
          <div class="card">
            <div class="card-header">
              <h3><i class="fas fa-info-circle"></i> Thông tin cá nhân</h3>
              <button class="btn" onclick="enableEdit()">
                <i class="fas fa-edit"></i> Chỉnh sửa
              </button>
            </div>

            <form id="profileForm">
              <div class="form-grid">
                <div class="form-group">
                  <label>Họ và tên</label>
                  <input
                    type="text"
                    id="fullName"
                    value="Nguyễn Văn An"
                    disabled
                  />
                </div>
                <div class="form-group">
                  <label>Email</label>
                  <input
                    type="email"
                    id="email"
                    value="nguyenvanan.pt@gymfit.vn"
                    disabled
                  />
                </div>
                <div class="form-group">
                  <label>Số điện thoại</label>
                  <input type="tel" id="phone" value="0912345678" disabled />
                </div>
                <div class="form-group">
                  <label>Ngày sinh</label>
                  <input type="date" id="dob" value="1995-05-15" disabled />
                </div>
                <div class="form-group">
                  <label>Giới tính</label>
                  <input type="text" id="gender" value="Nam" disabled />
                </div>
                <div class="form-group">
                  <label>Chuyên môn</label>
                  <input
                    type="text"
                    id="specialty"
                    value="Cardio, Yoga, Bodybuilding"
                    disabled
                  />
                </div>
                <div class="form-group full-width">
                  <label>Địa chỉ</label>
                  <input
                    type="text"
                    id="address"
                    value="123 Nguyễn Văn Linh, Đà Nẵng"
                    disabled
                  />
                </div>
              </div>
              <div
                id="formActions"
                style="display: none; margin-top: 20px; text-align: right"
              >
                <button
                  type="button"
                  class="btn btn-secondary"
                  onclick="cancelEdit()"
                  style="margin-right: 10px"
                >
                  <i class="fas fa-times"></i> Hủy
                </button>
                <button type="submit" class="btn">
                  <i class="fas fa-save"></i> Lưu thay đổi
                </button>
              </div>
            </form>
          </div>

          <!-- Đổi mật khẩu -->
          <div class="card">
            <div class="card-header">
              <h3><i class="fas fa-lock"></i> Đổi mật khẩu</h3>
            </div>

            <form id="passwordForm" onsubmit="changePassword(event)">
              <div class="form-grid">
                <div class="form-group full-width">
                  <label>Mật khẩu hiện tại</label>
                  <input
                    type="password"
                    id="currentPassword"
                    placeholder="Nhập mật khẩu hiện tại"
                    required
                  />
                </div>
                <div class="form-group">
                  <label>Mật khẩu mới</label>
                  <input
                    type="password"
                    id="newPassword"
                    placeholder="Nhập mật khẩu mới"
                    required
                  />
                </div>
                <div class="form-group">
                  <label>Xác nhận mật khẩu mới</label>
                  <input
                    type="password"
                    id="confirmPassword"
                    placeholder="Nhập lại mật khẩu mới"
                    required
                  />
                </div>
              </div>
              <div style="margin-top: 20px; text-align: right">
                <button type="submit" class="btn">
                  <i class="fas fa-key"></i> Đổi mật khẩu
                </button>
              </div>
            </form>
          </div>

          <!-- Thông tin bổ sung -->
          <div class="card">
            <div class="card-header">
              <h3>
                <i class="fas fa-certificate"></i> Chứng chỉ & Kinh nghiệm
              </h3>
              <button class="btn">
                <i class="fas fa-plus"></i> Thêm chứng chỉ
              </button>
            </div>

            <div class="info-display">
              <div class="info-item">
                <div class="info-label">Chứng chỉ PT</div>
                <div class="info-value">ACE Certified</div>
              </div>
              <div class="info-item">
                <div class="info-label">Chứng chỉ Yoga</div>
                <div class="info-value">RYT 200</div>
              </div>
              <div class="info-item">
                <div class="info-label">Kinh nghiệm</div>
                <div class="info-value">3 năm</div>
              </div>
              <div class="info-item">
                <div class="info-label">Số học viên đã huấn luyện</div>
                <div class="info-value">150+</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <script>
      function enableEdit() {
        const inputs = document.querySelectorAll('#profileForm input');
        inputs.forEach((input) => {
          if (input.id !== 'fullName') {
            // Không cho phép sửa tên
            input.disabled = false;
          }
        });
        document.getElementById('formActions').style.display = 'block';
      }

      function cancelEdit() {
        const inputs = document.querySelectorAll('#profileForm input');
        inputs.forEach((input) => (input.disabled = true));
        document.getElementById('formActions').style.display = 'none';
      }

      document
        .getElementById('profileForm')
        .addEventListener('submit', function (e) {
          e.preventDefault();
          alert('Cập nhật thông tin thành công!');
          cancelEdit();
        });

      function changePassword(e) {
        e.preventDefault();
        const newPass = document.getElementById('newPassword').value;
        const confirmPass = document.getElementById('confirmPassword').value;

        if (newPass !== confirmPass) {
          alert('Mật khẩu xác nhận không khớp!');
          return;
        }

        alert('Đổi mật khẩu thành công!');
        e.target.reset();
      }
    </script>
  </body>
</html>

