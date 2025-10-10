<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/views/common/header.jsp" %>

<style>
    :root {
        --primary: #141a46;
        --primary-light: #1e2a5c;
        --accent: #ec8b5e;
        --accent-hover: #d67a4f;
        --text: #2c3e50;
        --text-light: #5a6c7d;
        --muted: #f8f9fa;
        --card: #ffffff;
        --shadow: rgba(0, 0, 0, 0.1);
        --shadow-hover: rgba(0, 0, 0, 0.15);
        --gradient-primary: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
    }

    .profile-card {
        background: var(--card);
        border-radius: 15px;
        box-shadow: 0 8px 25px var(--shadow);
        padding: 30px;
        margin-bottom: 30px;
    }

    .profile-header {
        background: var(--gradient-primary);
        color: white;
        border-radius: 15px;
        padding: 30px;
        margin-bottom: 30px;
        text-align: center;
    }

    .profile-avatar {
        width: 120px;
        height: 120px;
        border-radius: 50%;
        background: var(--accent);
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 20px;
        font-size: 3rem;
        color: white;
    }

    .form-group {
        margin-bottom: 20px;
    }

    .form-label {
        color: var(--text);
        font-weight: 600;
        margin-bottom: 8px;
    }

    .form-control {
        border: 2px solid #e9ecef;
        border-radius: 10px;
        padding: 12px 15px;
        transition: all 0.3s ease;
    }

    .form-control:focus {
        border-color: var(--accent);
        box-shadow: 0 0 0 0.2rem rgba(236, 139, 94, 0.25);
    }

    .btn-save {
        background: var(--gradient-accent);
        color: white;
        border: none;
        border-radius: 25px;
        padding: 12px 30px;
        font-weight: 600;
        transition: all 0.3s ease;
    }

    .btn-save:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
        color: white;
    }

    .btn-back {
        background: var(--gradient-primary);
        color: white;
        border: none;
        border-radius: 25px;
        padding: 12px 30px;
        font-weight: 600;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-block;
    }

    .btn-back:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(20, 26, 70, 0.4);
        color: white;
    }

    .profile-title {
        color: var(--text);
        font-weight: 800;
        margin-bottom: 30px;
        text-transform: uppercase;
        letter-spacing: 1px;
    }

    .section-divider {
        border-top: 2px solid var(--muted);
        margin: 30px 0;
        padding-top: 30px;
    }

    .emergency-section {
        background: #fff3cd;
        border-left: 4px solid #ffc107;
        padding: 15px;
        border-radius: 10px;
        margin-bottom: 20px;
    }

    .emergency-section .alert-icon {
        color: #ffc107;
        font-size: 1.5rem;
    }

    .btn-edit {
        background: transparent;
        color: var(--accent);
        border: 2px solid var(--accent);
        border-radius: 20px;
        padding: 8px 20px;
        font-weight: 600;
        transition: all 0.3s ease;
    }

    .btn-edit:hover {
        background: var(--accent);
        color: white;
    }

    .btn-delete {
        background: transparent;
        color: #dc3545;
        border: 2px solid #dc3545;
        border-radius: 20px;
        padding: 8px 20px;
        font-weight: 600;
        transition: all 0.3s ease;
    }

    .btn-delete:hover {
        background: #dc3545;
        color: white;
    }

    .form-control:disabled {
        background-color: #f8f9fa;
        cursor: not-allowed;
    }
</style>

<div class="container mt-5">
    

    <!-- Profile Header -->
    <div class="profile-header">
        <div class="profile-avatar">
            <i class="fas fa-user-edit"></i>
        </div>
        <h2 class="mb-2">Chỉnh Sửa Thông Tin Cá Nhân</h2>
        <p class="mb-0">Cập nhật thông tin và cài đặt tài khoản của bạn</p>
    </div>

    <!-- Profile Information -->
    <div class="row justify-content-center">
        <div class="col-md-12">
            <div class="profile-card">
                <h4 class="profile-title">
                    <i class="fas fa-user-circle me-2"></i>Thông Tin Cá Nhân
                </h4>
                
                <form id="profileForm">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-user me-1"></i>Họ và Tên <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="fullName" name="fullName" value="Nguyễn Văn A" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-envelope me-1"></i>Email <span class="text-danger">*</span>
                                </label>
                                <input type="email" class="form-control" id="email" name="email" value="nguyenvana@email.com" required>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-phone me-1"></i>Số Điện Thoại <span class="text-danger">*</span>
                                </label>
                                <input type="tel" class="form-control" id="phone" name="phone" value="0123456789" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-calendar me-1"></i>Ngày Sinh <span class="text-danger">*</span>
                                </label>
                                <input type="date" class="form-control" id="dob" name="dob" value="1990-01-01" required>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-venus-mars me-1"></i>Giới Tính <span class="text-danger">*</span>
                                </label>
                                <select class="form-control" id="gender" name="gender" required>
                                    <option value="">-- Chọn giới tính --</option>
                                    <option value="male" selected>Nam</option>
                                    <option value="female">Nữ</option>
                                    <option value="other">Khác</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-ruler-vertical me-1"></i>Chiều Cao (cm)
                                </label>
                                <input type="number" class="form-control" id="height" name="height" value="175" min="100" max="250">
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-weight me-1"></i>Cân Nặng (kg)
                                </label>
                                <input type="number" class="form-control" id="weight" name="weight" value="70" min="30" max="200" step="0.1">
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-bullseye me-1"></i>Mục Tiêu Tập Luyện
                                </label>
                                <select class="form-control" id="goal" name="goal">
                                    <option value="">-- Chọn mục tiêu --</option>
                                    <option value="lose">Giảm Cân</option>
                                    <option value="gain">Tăng Cân</option>
                                    <option value="maintain" selected>Duy Trì</option>
                                    <option value="muscle">Tăng Cơ</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="form-label">
                                    <i class="fas fa-camera me-1"></i>Ảnh Đại Diện
                                </label>
                                <input type="file" class="form-control" id="avatar" name="avatar" accept="image/*">
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">
                            <i class="fas fa-map-marker-alt me-1"></i>Địa Chỉ
                        </label>
                        <textarea class="form-control" id="address" name="address" rows="2" placeholder="Nhập địa chỉ của bạn...">123 Đường ABC, Quận XYZ, TP.HCM</textarea>
                    </div>

                    <!-- Emergency Contact Section -->
                    <div class="section-divider">
                        <div class="emergency-section mb-3">
                            <div class="d-flex align-items-center">
                                <i class="fas fa-exclamation-triangle alert-icon me-3"></i>
                                <div>
                                    <h6 class="mb-0"><strong>Thông Tin Liên Hệ Khẩn Cấp</strong></h6>
                                    <small class="text-muted">Thông tin này sẽ được sử dụng trong trường hợp khẩn cấp khi bạn gặp sự cố tại phòng gym</small>
                                </div>
                            </div>
                        </div>

                        <h4 class="profile-title">
                            <i class="fas fa-user-shield me-2"></i>Người Thân Liên Hệ Khẩn Cấp
                        </h4>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">
                                        <i class="fas fa-user-friends me-1"></i>Họ Tên Người Thân <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="emergencyName" name="emergencyName" placeholder="VD: Nguyễn Thị B" required>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">
                                        <i class="fas fa-phone-alt me-1"></i>Số Điện Thoại Người Thân <span class="text-danger">*</span>
                                    </label>
                                    <input type="tel" class="form-control" id="emergencyPhone" name="emergencyPhone" placeholder="VD: 0987654321" required>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">
                                        <i class="fas fa-heart me-1"></i>Mối Quan Hệ
                                    </label>
                                    <select class="form-control" id="emergencyRelation" name="emergencyRelation">
                                        <option value="">-- Chọn mối quan hệ --</option>
                                        <option value="parent">Bố/Mẹ</option>
                                        <option value="spouse">Vợ/Chồng</option>
                                        <option value="sibling">Anh/Chị/Em</option>
                                        <option value="friend">Bạn bè</option>
                                        <option value="other">Khác</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">
                                        <i class="fas fa-home me-1"></i>Địa Chỉ Người Thân
                                    </label>
                                    <input type="text" class="form-control" id="emergencyAddress" name="emergencyAddress" placeholder="VD: 456 Đường XYZ, Quận ABC">
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Action Buttons -->
                    <div class="d-flex justify-content-between align-items-center mt-4">
                        <div>
                            <button type="button" class="btn-delete me-2" onclick="clearForm()">
                                <i class="fas fa-trash-alt me-2"></i>Xóa Thông Tin
                            </button>
                            <button type="button" class="btn-edit" onclick="resetForm()">
                                <i class="fas fa-undo me-2"></i>Đặt Lại
                            </button>
                        </div>
                        <div>
                            <a href="${pageContext.request.contextPath}/views/member/dashboard.jsp" class="btn-back me-2">
                                <i class="fas fa-times me-2"></i>Hủy
                            </a>
                            <button type="submit" class="btn-save">
                                <i class="fas fa-save me-2"></i>Lưu Thay Đổi
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // Form validation and submission
    document.getElementById('profileForm').addEventListener('submit', function(e) {
        e.preventDefault();
        
        // Validate emergency contact
        const emergencyName = document.getElementById('emergencyName').value;
        const emergencyPhone = document.getElementById('emergencyPhone').value;
        
        if (!emergencyName || !emergencyPhone) {
            alert('Vui lòng nhập đầy đủ thông tin liên hệ khẩn cấp!');
            return;
        }
        
        // Validate phone numbers
        const phoneRegex = /^[0-9]{10}$/;
        const phone = document.getElementById('phone').value.replace(/\s/g, '');
        const emergPhone = emergencyPhone.replace(/\s/g, '');
        
        if (!phoneRegex.test(phone)) {
            alert('Số điện thoại không hợp lệ! Vui lòng nhập 10 chữ số.');
            return;
        }
        
        if (!phoneRegex.test(emergPhone)) {
            alert('Số điện thoại người thân không hợp lệ! Vui lòng nhập 10 chữ số.');
            return;
        }
        
        // Submit form
        alert('Thông tin đã được lưu thành công!');
        // Here you would normally send the data to the server
        // window.location.href = '${pageContext.request.contextPath}/views/member/dashboard.jsp';
    });
    
    function clearForm() {
        if (confirm('Bạn có chắc chắn muốn xóa tất cả thông tin? Hành động này không thể hoàn tác!')) {
            document.getElementById('profileForm').reset();
            document.getElementById('fullName').value = '';
            document.getElementById('email').value = '';
            document.getElementById('phone').value = '';
            document.getElementById('address').value = '';
        }
    }
    
    function resetForm() {
        if (confirm('Bạn có muốn đặt lại form về giá trị ban đầu?')) {
            document.getElementById('profileForm').reset();
        }
    }
</script>

<%@ include file="/views/common/footer.jsp" %>