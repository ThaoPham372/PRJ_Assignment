<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
        --success: #28a745;
        --warning: #ffc107;
        --danger: #dc3545;
        --info: #17a2b8;
    }

    .profile-container {
        max-width: 1400px;
        margin: 0 auto;
        padding: 30px 15px;
    }

    .btn-back-top {
        background: var(--gradient-primary);
        color: white;
        border: none;
        border-radius: 25px;
        padding: 10px 20px;
        font-weight: 600;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        font-size: 0.9rem;
    }

    .btn-back-top:hover {
        transform: translateX(-5px);
        box-shadow: 0 4px 15px rgba(20, 26, 70, 0.3);
        color: white;
    }

    .profile-hero {
        background: var(--gradient-primary);
        border-radius: 20px;
        padding: 40px;
        margin-bottom: 30px;
        color: white;
        position: relative;
        overflow: hidden;
        box-shadow: 0 10px 40px var(--shadow);
    }

    .profile-hero::before {
        content: '';
        position: absolute;
        top: -50%;
        right: -10%;
        width: 400px;
        height: 400px;
        background: radial-gradient(circle, rgba(236, 139, 94, 0.2) 0%, transparent 70%);
        border-radius: 50%;
    }

    .profile-hero-content {
        position: relative;
        z-index: 1;
        text-align: center;
    }

    .edit-icon {
        font-size: 4rem;
        margin-bottom: 20px;
        opacity: 0.9;
    }

    .profile-title {
        font-size: 2.5rem;
        font-weight: 900;
        margin-bottom: 10px;
        text-transform: uppercase;
        letter-spacing: 1px;
    }

    .info-card {
        background: var(--card);
        border-radius: 15px;
        padding: 30px;
        margin-bottom: 20px;
        box-shadow: 0 4px 15px var(--shadow);
    }

    .info-card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 25px;
        padding-bottom: 15px;
        border-bottom: 2px solid var(--muted);
    }

    .info-card-title {
        font-size: 1.3rem;
        font-weight: 800;
        color: var(--text);
        display: flex;
        align-items: center;
        gap: 10px;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }

    .info-card-title i {
        color: var(--accent);
    }

    .form-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        gap: 20px;
    }

    .form-group {
        margin-bottom: 0;
    }

    .form-group.full-width {
        grid-column: 1 / -1;
    }

    .form-label {
        font-size: 0.85rem;
        color: var(--text-light);
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        margin-bottom: 8px;
        display: flex;
        align-items: center;
        gap: 6px;
    }

    .form-label i {
        font-size: 0.9rem;
        opacity: 0.7;
    }

    .form-label .required {
        color: var(--danger);
    }

    .form-control {
        width: 100%;
        padding: 12px 15px;
        border: 2px solid #e9ecef;
        border-radius: 10px;
        font-size: 1rem;
        transition: all 0.3s ease;
        background: white;
    }

    .form-control:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 0.2rem rgba(236, 139, 94, 0.25);
    }

    .form-control:disabled,
    .form-control[readonly] {
        background: var(--muted);
        cursor: not-allowed;
        color: var(--text-light);
    }

    textarea.form-control {
        resize: vertical;
        min-height: 80px;
    }

    .emergency-alert {
        background: linear-gradient(135deg, #fff3cd 0%, #ffeaa7 100%);
        border-left: 4px solid var(--warning);
        padding: 20px;
        border-radius: 10px;
        margin-bottom: 20px;
    }

    .emergency-alert-header {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 10px;
    }

    .emergency-alert-icon {
        font-size: 1.8rem;
        color: var(--warning);
    }

    .action-buttons {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 40px;
        padding-top: 30px;
        border-top: 2px solid var(--muted);
    }

    .btn-group {
        display: flex;
        gap: 15px;
    }

    .btn-save {
        background: var(--gradient-accent);
        color: white;
        border: none;
        border-radius: 25px;
        padding: 12px 30px;
        font-weight: 600;
        transition: all 0.3s ease;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        cursor: pointer;
        font-size: 1rem;
    }

    .btn-save:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
    }

    .btn-cancel {
        background: transparent;
        color: var(--text);
        border: 2px solid var(--text-light);
        border-radius: 25px;
        padding: 12px 30px;
        font-weight: 600;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
    }

    .btn-cancel:hover {
        border-color: var(--text);
        color: var(--text);
        background: var(--muted);
    }

    .btn-reset {
        background: transparent;
        color: var(--info);
        border: 2px solid var(--info);
        border-radius: 25px;
        padding: 10px 20px;
        font-weight: 600;
        transition: all 0.3s ease;
        cursor: pointer;
        display: inline-flex;
        align-items: center;
        gap: 8px;
    }

    .btn-reset:hover {
        background: var(--info);
        color: white;
    }

    .help-text {
        font-size: 0.8rem;
        color: var(--text-light);
        margin-top: 5px;
        font-style: italic;
    }


    .alert {
        padding: 15px 20px;
        border-radius: 10px;
        margin-bottom: 20px;
        display: flex;
        align-items: center;
        gap: 12px;
    }

    .alert-success {
        background: rgba(40, 167, 69, 0.1);
        border-left: 4px solid var(--success);
        color: var(--success);
    }

    .alert-danger {
        background: rgba(220, 53, 69, 0.1);
        border-left: 4px solid var(--danger);
        color: var(--danger);
    }

    @media (max-width: 768px) {
        .profile-title {
            font-size: 1.8rem;
        }

        .form-grid {
            grid-template-columns: 1fr;
        }

        .action-buttons {
            flex-direction: column;
            gap: 15px;
        }

        .btn-group {
            width: 100%;
            flex-direction: column;
        }

        .btn-save,
        .btn-cancel,
        .btn-reset {
            width: 100%;
            justify-content: center;
        }
    }
</style>

<div class="profile-container">
    <!-- Back Button -->
    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/member/profile" class="btn-back-top">
            <i class="fas fa-arrow-left"></i>
            Quay lại xem thông tin
        </a>
    </div>

    <!-- Profile Hero Section -->
    <div class="profile-hero">
        <div class="profile-hero-content">
            <i class="fas fa-user-edit edit-icon"></i>
            <h1 class="profile-title">Chỉnh sửa thông tin cá nhân</h1>
            <p class="mb-0">Cập nhật thông tin và cài đặt tài khoản của bạn</p>
        </div>
    </div>

    <!-- Alert Messages -->
    <c:if test="${not empty success}">
        <div class="alert alert-success">
            <i class="fas fa-check-circle"></i>
            <span>${success}</span>
        </div>
    </c:if>
    
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            <i class="fas fa-exclamation-circle"></i>
            <span>${error}</span>
        </div>
    </c:if>

    <!-- Edit Form -->
    <form action="${pageContext.request.contextPath}/member/profile-edit" method="POST" id="profileEditForm">
        
        <!-- Basic Information Card -->
        <div class="info-card">
            <div class="info-card-header">
                <h2 class="info-card-title">
                    <i class="fas fa-user-circle"></i>
                    Thông tin cơ bản
                </h2>
            </div>
            <div class="form-grid">
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-signature"></i>
                        Username
                    </label>
                    <input type="text" class="form-control" 
                           value="<c:out value='${member.username}'/>" readonly>
                    <div class="help-text">Username không thể thay đổi</div>
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-user"></i>
                        Tên đầy đủ
                    </label>
                    <input type="text" class="form-control" name="name" 
                           value="<c:out value='${member.name}'/>" 
                           placeholder="Nhập tên đầy đủ của bạn">
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-envelope"></i>
                        Email
                    </label>
                    <input type="email" class="form-control" 
                           value="<c:out value='${member.email}'/>" readonly>
                    <div class="help-text">Email không thể thay đổi</div>
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-calendar-plus"></i>
                        Ngày tham gia
                    </label>
                    <input type="text" class="form-control" 
                           value="<c:choose><c:when test='${member.createdDate != null}'><fmt:formatDate value='${member.createdDate}' pattern='dd/MM/yyyy'/></c:when><c:otherwise>N/A</c:otherwise></c:choose>" 
                           readonly>
                    <div class="help-text">Ngày bạn đăng ký tài khoản</div>
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-phone-alt"></i>
                        Số điện thoại
                    </label>
                    <input type="tel" class="form-control" name="phone" 
                           value="<c:out value='${member.phone}'/>" 
                           placeholder="VD: 0987654321">
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-birthday-cake"></i>
                        Ngày sinh
                    </label>
                    <input type="date" class="form-control" name="dob" 
                           value="<c:choose><c:when test='${member.dob != null}'><fmt:formatDate value='${member.dob}' pattern='yyyy-MM-dd'/></c:when><c:otherwise></c:otherwise></c:choose>">
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-venus-mars"></i>
                        Giới tính
                    </label>
                    <select class="form-control" name="gender">
                        <option value="">-- Chọn giới tính --</option>
                        <option value="Nam" ${member.gender == 'Nam' || member.gender == 'male' || member.gender == 'Male' ? 'selected' : ''}>Nam</option>
                        <option value="Nữ" ${member.gender == 'Nữ' || member.gender == 'female' || member.gender == 'Female' ? 'selected' : ''}>Nữ</option>
                        <option value="Khác" ${member.gender == 'Khác' || member.gender == 'other' || member.gender == 'Other' ? 'selected' : ''}>Khác</option>
                    </select>
                </div>
                
                <div class="form-group full-width">
                    <label class="form-label">
                        <i class="fas fa-map-marker-alt"></i>
                        Địa chỉ
                    </label>
                    <input type="text" class="form-control" name="address" 
                           value="<c:out value='${member.address}'/>" 
                           placeholder="Nhập địa chỉ của bạn...">
                </div>
            </div>
        </div>

        <!-- Physical Information Card -->
        <div class="info-card">
            <div class="info-card-header">
                <h2 class="info-card-title">
                    <i class="fas fa-heartbeat"></i>
                    Chỉ số cơ thể
                </h2>
            </div>
            <div class="form-grid">
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-ruler-vertical"></i>
                        Chiều cao (cm)
                    </label>
                    <input type="number" class="form-control" name="height" 
                           value="<c:out value='${member.height}'/>" 
                           min="100" max="250" step="0.1" placeholder="VD: 175">
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-weight"></i>
                        Cân nặng (kg)
                    </label>
                    <input type="number" class="form-control" name="weight" 
                           value="<c:out value='${member.weight}'/>" 
                           min="30" max="200" step="0.1" placeholder="VD: 70">
                    <div class="help-text">BMI sẽ tự động tính toán khi lưu</div>
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-balance-scale"></i>
                        Chỉ số BMI
                    </label>
                    <input type="text" class="form-control" 
                           value="<c:choose><c:when test='${member.bmi != null}'><fmt:formatNumber value='${member.bmi}' pattern='#,##0.00'/></c:when><c:otherwise>--</c:otherwise></c:choose>" 
                           readonly>
                    <div class="help-text">Tự động tính từ chiều cao & cân nặng</div>
                </div>
            </div>
        </div>

        <!-- Emergency Contact Card -->
        <div class="info-card">
            <div class="emergency-alert">
                <div class="emergency-alert-header">
                    <i class="fas fa-exclamation-triangle emergency-alert-icon"></i>
                    <div>
                        <strong>Thông tin liên hệ khẩn cấp</strong>
                        <div style="font-size: 0.9rem; margin-top: 5px;">
                            Thông tin này sẽ được sử dụng trong trường hợp khẩn cấp khi bạn gặp sự cố tại phòng gym
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="form-grid">
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-user-friends"></i>
                        Họ tên người thân
                    </label>
                    <input type="text" class="form-control" name="emergencyContactName" 
                           value="<c:out value='${member.emergencyContactName}'/>" 
                           placeholder="VD: Nguyễn Văn A">
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-phone-alt"></i>
                        Số điện thoại người thân
                    </label>
                    <input type="tel" class="form-control" name="emergencyContactPhone" 
                           value="<c:out value='${member.emergencyContactPhone}'/>" 
                           placeholder="VD: 0987654321">
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-heart"></i>
                        Mối quan hệ
                    </label>
                    <input type="text" class="form-control" name="emergencyContactRelation" 
                           value="<c:out value='${member.emergencyContactRelation}'/>" 
                           placeholder="VD: Bố/Mẹ, Vợ/Chồng, Anh/Chị/Em">
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-home"></i>
                        Địa chỉ người thân
                    </label>
                    <input type="text" class="form-control" name="emergencyContactAddress" 
                           value="<c:out value='${member.emergencyContactAddress}'/>" 
                           placeholder="Địa chỉ liên lạc khẩn cấp">
                </div>
            </div>
        </div>

        <!-- Action Buttons -->
        <div class="action-buttons">
            <div class="btn-group">
                <button type="button" class="btn-reset" onclick="resetForm()">
                    <i class="fas fa-undo"></i>
                    Đặt lại
                </button>
            </div>
            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/member/profile" class="btn-cancel">
                    <i class="fas fa-times"></i>
                    Hủy bỏ
                </a>
                <button type="submit" class="btn-save">
                    <i class="fas fa-save"></i>
                    Lưu thay đổi
                </button>
            </div>
        </div>
    </form>
</div>

<script>
    function resetForm() {
        if (confirm('Bạn có muốn đặt lại form về giá trị ban đầu không?')) {
            document.getElementById('profileEditForm').reset();
        }
    }

    // Form validation
    document.getElementById('profileEditForm').addEventListener('submit', function(e) {
        const submitBtn = this.querySelector('button[type="submit"]');
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang lưu...';
        submitBtn.disabled = true;
    });
</script>

<%@ include file="/views/common/footer.jsp" %>



