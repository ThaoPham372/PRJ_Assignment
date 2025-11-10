<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page pageEncoding="UTF-8" %>

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
      .form-group textarea,
      .form-group select {
        padding: 12px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 1rem;
        transition: all 0.3s;
      }

      .form-group input:focus,
      .form-group textarea:focus,
      .form-group select:focus {
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

      /* NOTIFICATION STYLES */
      .notification {
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
        z-index: 1000;
        display: flex;
        align-items: center;
        gap: 10px;
        max-width: 400px;
        animation: slideInRight 0.3s ease-out;
      }

      .notification.success {
        background: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
      }

      .notification.error {
        background: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
      }

      .notification i {
        font-size: 1.2rem;
      }

      .notification .close-btn {
        background: none;
        border: none;
        color: inherit;
        cursor: pointer;
        padding: 5px;
        margin-left: auto;
        border-radius: 4px;
        transition: background 0.2s;
      }

      .notification .close-btn:hover {
        background: rgba(0, 0, 0, 0.1);
      }

      @keyframes slideInRight {
        from {
          transform: translateX(100%);
          opacity: 0;
        }
        to {
          transform: translateX(0);
          opacity: 1;
        }
      }

      @keyframes slideInDown {
        from {
          transform: translateY(-20px);
          opacity: 0;
        }
        to {
          transform: translateY(0);
          opacity: 1;
        }
      }

      @keyframes slideOutLeft {
        from {
          transform: translateX(0);
          opacity: 1;
        }
        to {
          transform: translateX(-100%);
          opacity: 0;
        }
      }

      /* FILE UPLOAD STYLES */
      .file-upload {
        position: relative;
        display: inline-block;
        cursor: pointer;
      }

      .file-upload input[type="file"] {
        position: absolute;
        left: -9999px;
        opacity: 0;
      }

      .upload-btn {
        background: var(--gradient-accent);
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 8px 16px;
        font-size: 0.9rem;
        cursor: pointer;
        transition: all 0.3s;
        display: inline-flex;
        align-items: center;
        gap: 8px;
      }

      .upload-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 90, 0.4);
      }

      /* CARD MESSAGE STYLES */
      .card-message {
        margin-bottom: 20px;
        border-radius: 8px;
        overflow: hidden;
        display: none;
      }

      .card-message.show {
        display: block;
        animation: slideDown 0.3s ease-out;
      }

      .card-message.success {
        background: #d4edda;
        border: 1px solid #c3e6cb;
      }

      .card-message.error {
        background: #f8d7da;
        border: 1px solid #f5c6cb;
      }

      .message-content {
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 12px 15px;
      }

      .message-icon {
        font-size: 1.1rem;
        flex-shrink: 0;
      }

      .card-message.success .message-icon {
        color: #155724;
      }

      .card-message.error .message-icon {
        color: #721c24;
      }

      .message-text {
        flex: 1;
        font-size: 0.9rem;
        font-weight: 500;
      }

      .card-message.success .message-text {
        color: #155724;
      }

      .card-message.error .message-text {
        color: #721c24;
      }

      .message-close {
        background: none;
        border: none;
        color: inherit;
        cursor: pointer;
        padding: 5px;
        border-radius: 4px;
        transition: background 0.2s;
        flex-shrink: 0;
      }

      .message-close:hover {
        background: rgba(0, 0, 0, 0.1);
      }

      @keyframes slideDown {
        from {
          opacity: 0;
          transform: translateY(-10px);
        }
        to {
          opacity: 1;
          transform: translateY(0);
        }
      }

      @keyframes slideUp {
        from {
          opacity: 1;
          transform: translateY(0);
        }
        to {
          opacity: 0;
          transform: translateY(-10px);
        }
      }

      @keyframes slideOutLeft {
        from {
          opacity: 1;
          transform: translateX(0);
        }
        to {
          opacity: 0;
          transform: translateX(-100%);
        }
      }

      /* MODAL STYLES */
      .modal {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        z-index: 2000;
        display: none; /* Changed from flex to none */
        align-items: center;
        justify-content: center;
      }

      .modal.show {
        display: flex; /* Show when .show class is added */
        animation: fadeIn 0.3s ease-out;
      }

      .modal-content {
        background: #fff;
        border-radius: 15px;
        padding: 30px;
        max-width: 600px;
        width: 90%;
        max-height: 80vh;
        overflow-y: auto;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
        animation: slideInUp 0.3s ease-out;
      }

      @keyframes fadeIn {
        from { opacity: 0; }
        to { opacity: 1; }
      }

      @keyframes slideInUp {
        from {
          transform: translateY(50px);
          opacity: 0;
        }
        to {
          transform: translateY(0);
          opacity: 1;
        }
      }

      @keyframes fadeOut {
        from {
          opacity: 1;
        }
        to {
          opacity: 0;
        }
      }

      .modal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
        padding-bottom: 15px;
        border-bottom: 2px solid #f0f0f0;
      }

      .modal-header h3 {
        color: var(--primary);
        margin: 0;
      }

      .modal-header .close-btn {
        background: none;
        border: none;
        font-size: 1.5rem;
        color: #999;
        cursor: pointer;
        padding: 5px;
        border-radius: 4px;
        transition: all 0.2s;
      }

      .modal-header .close-btn:hover {
        background: #f0f0f0;
        color: #333;
      }

      .image-preview {
        max-width: 200px;
        max-height: 200px;
        border-radius: 8px;
        border: 2px solid #e0e0e0;
        margin-top: 10px;
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
            <!-- Avatar upload chức năng sẽ implement sau -->
          </div>
          <h2>
            ${not empty trainer.name ? trainer.name : sessionScope.user.username}
          </h2>
          <p class="role">Personal Trainer</p>

          <div class="profile-stats">
            <div class="stat-item">
              <span class="stat-label">Chuyên môn</span>
              <span class="stat-value">${not empty trainer.specialization ? trainer.specialization : 'Chưa cập nhật'}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">Cấp độ chứng chỉ</span>
              <span class="stat-value">${not empty trainer.certificationLevel ? trainer.certificationLevel : 'Chưa có'}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">Thâm niên</span>
              <span class="stat-value">${trainer.yearsOfExperience != null ? trainer.yearsOfExperience : 0} năm</span>
            </div>

          </div>
        </div>

        <!-- MAIN CONTENT -->
        <div class="profile-main">
          <!-- Thông tin cá nhân -->
          <div class="card">
            <div class="card-header">
              <h3><i class="fas fa-info-circle"></i> Thông tin cá nhân</h3>
            </div>

            <!-- Success/Error message for personal info -->
            <div id="personalInfoMessage" class="card-message" style="display: none;">
              <div class="message-content">
                <i class="message-icon"></i>
                <span class="message-text"></span>
                <button onclick="hideCardMessage('personalInfoMessage')" class="message-close">
                  <i class="fas fa-times"></i>
              </button>
              </div>
            </div>

            <form id="profileForm" action="${pageContext.request.contextPath}/pt/update-profile" method="POST">
              <div class="form-grid">
                <div class="form-group">
                  <label>Họ và tên</label>
                  <input
                    type="text"
                    name="fullName"
                    id="fullName"
                    value="${not empty trainer.name ? trainer.name : sessionScope.user.username}"
                    required
                  />
                </div>
                <div class="form-group">
                  <label>Email</label>
                  <input
                    type="email"
                    name="email"
                    id="email"
                    value="${not empty trainer.email ? trainer.email : sessionScope.user.email}"
                    required
                  />
                </div>
                <div class="form-group">
                  <label>Số điện thoại</label>
                  <input 
                    type="tel" 
                    name="phoneNumber"
                    id="phoneNumber" 
                    value="${not empty trainer.phone ? trainer.phone : ''}"
                    placeholder="Nhập số điện thoại"
                  />
                </div>
                <div class="form-group">
                  <label>Ngày sinh</label>
                  <input 
                    type="date" 
                    name="dateOfBirth"
                    id="dateOfBirth" 
                    value="${trainer.dob}"
                  />
                </div>
                <div class="form-group">
                  <label>Giới tính</label>
                  <select name="gender" id="gender">
                    <option value="">Chọn giới tính</option>
                    <option value="Nam" ${trainer.gender == 'Nam' ? 'selected' : ''}>Nam</option>
                    <option value="Nữ" ${trainer.gender == 'Nữ' ? 'selected' : ''}>Nữ</option>
                    <option value="Khác" ${trainer.gender == 'Khác' ? 'selected' : ''}>Khác</option>
                  </select>
                </div>
                <div class="form-group">
                  <label>Chuyên môn</label>
                  <input
                    type="text"
                    name="specialization"
                    id="specialization"
                    value="${not empty trainer.specialization ? trainer.specialization : ''}"
                    placeholder="VD: Cardio, Yoga, Bodybuilding"
                  />
                </div>
                <div class="form-group">
                  <label>Số năm kinh nghiệm</label>
                  <input
                    type="number"
                    name="yearsOfExperience"
                    id="yearsOfExperience"
                    value="${trainer.yearsOfExperience != null ? trainer.yearsOfExperience : ''}"
                    placeholder="VD: 5"
                    min="0"
                    max="100"
                  />
                </div>
                <div class="form-group">
                  <label>Cấp độ chứng chỉ</label>
                  <input
                    type="text"
                    name="certificationLevel"
                    id="certificationLevel"
                    value="${not empty trainer.certificationLevel ? trainer.certificationLevel : ''}"
                    placeholder="VD: ACE, NASM, CPT"
                  />
                </div>
                <div class="form-group full-width">
                  <label>Địa chỉ</label>
                  <input
                    type="text"
                    name="address"
                    id="address"
                    value="${not empty trainer.address ? trainer.address : ''}"
                    placeholder="Nhập địa chỉ"
                  />
                </div>
              </div>
              <div style="margin-top: 20px;">
                <div style="text-align: right;">
                <button type="submit" class="btn">
                    <i class="fas fa-save"></i> Cập nhật thông tin
                </button>
                </div>
                
                <!-- Thông báo hiển thị ngay dưới nút -->
                <c:if test="${not empty successMessage}">
                  <div id="updateSuccessMessage" class="success-message" style="margin-top: 15px; padding: 12px 16px; background: #d4edda; color: #155724; border: 1px solid #c3e6cb; border-radius: 8px; display: flex; align-items: center; gap: 8px;">
                    <i class="fas fa-check-circle"></i>
                    <span>${successMessage}</span>
                  </div>
                </c:if>
                
                <c:if test="${not empty errorMessage}">
                  <div id="updateErrorMessage" class="error-message" style="margin-top: 15px; padding: 12px 16px; background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; border-radius: 8px; display: flex; align-items: center; gap: 8px;">
                    <i class="fas fa-exclamation-circle"></i>
                    <span>${errorMessage}</span>
                  </div>
                </c:if>
              </div>
            </form>
          </div>

          <!-- Đổi mật khẩu -->
          <div class="card">
            <div class="card-header">
              <h3><i class="fas fa-lock"></i> Đổi mật khẩu</h3>
            </div>

            <!-- Success/Error message for password change -->
            <div id="passwordMessage" class="card-message" style="display: none;">
              <div class="message-content">
                <i class="message-icon"></i>
                <span class="message-text"></span>
                <button onclick="hideCardMessage('passwordMessage')" class="message-close">
                  <i class="fas fa-times"></i>
                </button>
              </div>
            </div>

            <form id="passwordForm" action="${pageContext.request.contextPath}/pt/change-password" method="POST">
              <div class="form-grid">
                <div class="form-group full-width">
                  <label>Mật khẩu hiện tại</label>
                  <input
                    type="password"
                    name="currentPassword"
                    id="currentPassword"
                    placeholder="Nhập mật khẩu hiện tại"
                    required
                  />
                </div>
                <div class="form-group">
                  <label>Mật khẩu mới</label>
                  <input
                    type="password"
                    name="newPassword"
                    id="newPassword"
                    placeholder="Nhập mật khẩu mới"
                    required
                  />
                </div>
                <div class="form-group">
                  <label>Xác nhận mật khẩu mới</label>
                  <input
                    type="password"
                    name="confirmPassword"
                    id="confirmPassword"
                    placeholder="Nhập lại mật khẩu mới"
                    required
                  />
                </div>
              </div>
              <div style="margin-top: 20px;">
                <div style="text-align: right;">
                <button type="submit" class="btn">
                  <i class="fas fa-key"></i> Đổi mật khẩu
                </button>
                </div>
                
                <!-- Thông báo hiển thị ngay dưới nút đổi mật khẩu -->
                <c:if test="${not empty passwordSuccessMessage}">
                  <div id="passwordSuccessMessage" class="success-message" style="margin-top: 15px; padding: 12px 16px; background: #d4edda; color: #155724; border: 1px solid #c3e6cb; border-radius: 8px; display: flex; align-items: center; gap: 8px;">
                    <i class="fas fa-check-circle"></i>
                    <span>${passwordSuccessMessage}</span>
                  </div>
                </c:if>
                
                <c:if test="${not empty passwordErrorMessage}">
                  <div id="passwordErrorMessage" class="error-message" style="margin-top: 15px; padding: 12px 16px; background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; border-radius: 8px; display: flex; align-items: center; gap: 8px;">
                    <i class="fas fa-exclamation-circle"></i>
                    <span>${passwordErrorMessage}</span>
                  </div>
                </c:if>
              </div>
            </form>
          </div>

        </div>
      </div>
    </div>

    <script>


      // Function to update personal info fields without reloading
      function updatePersonalInfoFields(formData) {
        // Update form fields with new values
        document.getElementById('fullName').value = formData.get('fullName');
        document.getElementById('email').value = formData.get('email');
        document.getElementById('phoneNumber').value = formData.get('phoneNumber');
        document.getElementById('dateOfBirth').value = formData.get('dateOfBirth');
        document.getElementById('gender').value = formData.get('gender');
        document.getElementById('specialization').value = formData.get('specialization');
        document.getElementById('address').value = formData.get('address');
        
        // Update sidebar name if it exists
        const sidebarName = document.querySelector('.profile-sidebar h2');
        if (sidebarName) {
          sidebarName.textContent = formData.get('fullName');
        }
      }

      // Upload avatar function
      function uploadAvatar() {
        const fileInput = document.getElementById('avatarFile');
        const file = fileInput.files[0];
        
        if (file) {
          // Validate file type
          if (!file.type.startsWith('image/')) {
            alert('Vui lòng chọn file ảnh!');
            return;
          }
          
          // Validate file size (max 5MB)
          if (file.size > 5 * 1024 * 1024) {
            alert('File ảnh không được vượt quá 5MB!');
            return;
          }
          
          // Create FormData and submit
          const formData = new FormData();
          formData.append('avatar', file);
          
          // Show loading
          const avatarContainer = document.querySelector('.avatar');
          avatarContainer.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
          
          // Submit to servlet
          fetch('${pageContext.request.contextPath}/upload-avatar', {
            method: 'POST',
            body: formData
          })
          .then(response => response.text())
          .then(data => {
            if (data.includes('success')) {
              // Reload page to show new avatar
              location.reload();
            } else {
              alert('Có lỗi khi upload avatar!');
              avatarContainer.innerHTML = '<i class="fas fa-dumbbell"></i>';
            }
          })
          .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi khi upload avatar!');
            avatarContainer.innerHTML = '<i class="fas fa-dumbbell"></i>';
          });
        }
      }

      // Show card message
      function showCardMessage(messageId, type, text, iconClass) {
        const messageElement = document.getElementById(messageId);
        if (messageElement) {
          messageElement.className = `card-message ${type} show`;
          messageElement.querySelector('.message-icon').className = `message-icon ${iconClass}`;
          messageElement.querySelector('.message-text').textContent = text;
          
          // Auto-hide after 5 seconds
          setTimeout(() => {
            hideCardMessage(messageId);
          }, 5000);
        }
      }

      // Hide card message
      function hideCardMessage(messageId) {
        const messageElement = document.getElementById(messageId);
        if (messageElement) {
          messageElement.style.animation = 'slideUp 0.3s ease-out forwards';
          setTimeout(() => {
            messageElement.classList.remove('show');
          }, 300);
        }
      }

      // Auto-hide success/error messages after 3 seconds
      function autoHideMessages() {
        // Auto-hide personal info messages
        const successMessage = document.getElementById('updateSuccessMessage');
        const errorMessage = document.getElementById('updateErrorMessage');
        
        if (successMessage) {
          setTimeout(function() {
            successMessage.style.animation = 'fadeOut 0.5s ease-out';
            setTimeout(function() {
              successMessage.style.display = 'none';
            }, 500);
          }, 3000);
        }
        
        if (errorMessage) {
          setTimeout(function() {
            errorMessage.style.animation = 'fadeOut 0.5s ease-out';
            setTimeout(function() {
              errorMessage.style.display = 'none';
            }, 500);
          }, 3000);
        }
        
        // Auto-hide password change messages
        const passwordSuccessMessage = document.getElementById('passwordSuccessMessage');
        const passwordErrorMessage = document.getElementById('passwordErrorMessage');
        
        if (passwordSuccessMessage) {
          setTimeout(function() {
            passwordSuccessMessage.style.animation = 'fadeOut 0.5s ease-out';
            setTimeout(function() {
              passwordSuccessMessage.style.display = 'none';
            }, 500);
          }, 3000);
        }
        
        if (passwordErrorMessage) {
          setTimeout(function() {
            passwordErrorMessage.style.animation = 'fadeOut 0.5s ease-out';
            setTimeout(function() {
              passwordErrorMessage.style.display = 'none';
            }, 500);
          }, 3000);
        }
      }

      // Scroll to password form after page load if there's a password message
      function scrollToPasswordForm() {
        const passwordSuccessMessage = document.getElementById('passwordSuccessMessage');
        const passwordErrorMessage = document.getElementById('passwordErrorMessage');
        
        // If there's a password message, scroll to the form
        if (passwordSuccessMessage || passwordErrorMessage) {
          const passwordForm = document.getElementById('passwordForm');
          if (passwordForm) {
            // Small delay to ensure page is fully loaded
            setTimeout(function() {
              passwordForm.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }, 100);
          }
        }
      }

      // Handle form submissions
      document.addEventListener('DOMContentLoaded', function() {
        // Auto-hide messages after 3 seconds
        autoHideMessages();
        
        // Scroll to password form if there's a password message
        scrollToPasswordForm();
        
        // Forms will submit normally without additional JavaScript
        // Success/Error messages are handled by the server and session
      });

    </script>
  </body>
</html>

