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

    .avatar-upload-section {
        display: flex;
        align-items: center;
        gap: 30px;
        padding: 20px;
        background: var(--muted);
        border-radius: 15px;
        margin-bottom: 20px;
    }

    .avatar-preview {
        width: 150px;
        height: 150px;
        border-radius: 50%;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 4rem;
        color: white;
        border: 5px solid white;
        box-shadow: 0 4px 15px var(--shadow);
        overflow: hidden;
        flex-shrink: 0;
    }

    .avatar-preview img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .avatar-upload-controls {
        flex: 1;
    }

    .avatar-upload-btn {
        background: var(--accent);
        color: white;
        border: none;
        border-radius: 10px;
        padding: 10px 20px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        display: inline-flex;
        align-items: center;
        gap: 8px;
    }

    .avatar-upload-btn:hover {
        background: var(--accent-hover);
        transform: translateY(-2px);
        box-shadow: 0 4px 10px rgba(236, 139, 94, 0.3);
    }

    .avatar-url-input {
        margin-top: 15px;
    }

    @media (max-width: 768px) {
        .avatar-upload-section {
            flex-direction: column;
            text-align: center;
        }
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
    <form action="${pageContext.request.contextPath}/member/profile/update" method="POST" id="profileEditForm" enctype="multipart/form-data">
        
        <!-- Avatar Upload Card -->
        <div class="info-card">
            <div class="info-card-header">
                <h2 class="info-card-title">
                    <i class="fas fa-image"></i>
                    Ảnh đại diện
                </h2>
            </div>
            <div class="avatar-upload-section">
                <div class="avatar-preview" id="avatarPreview">
                    <c:choose>
                        <c:when test="${not empty profileData.avatarUrl}">
                            <img src="<c:out value='${profileData.avatarUrl}'/>" alt="Avatar" id="avatarImg">
                        </c:when>
                        <c:otherwise>
                            <i class="fas fa-user" id="avatarIcon"></i>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="avatar-upload-controls">
                    <label class="avatar-upload-btn">
                        <i class="fas fa-upload"></i>
                        Chọn ảnh
                        <input type="file" name="avatarFile" id="avatarFile" accept="image/*" style="display: none;" onchange="handleAvatarUpload(event)">
                    </label>
                    <div class="avatar-url-input">
                        <label class="form-label">
                            <i class="fas fa-link"></i>
                            Hoặc nhập URL ảnh
                        </label>
                        <input type="url" class="form-control" name="avatarUrl" id="avatarUrl" 
                               value="<c:out value='${profileData.avatarUrl}'/>" 
                               placeholder="https://example.com/avatar.jpg"
                               onchange="updateAvatarPreview(this.value)">
                        <div class="help-text">Nhập URL ảnh hoặc chọn file từ máy tính</div>
                    </div>
                </div>
            </div>
        </div>

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
                           value="<c:out value='${profileData.username}'/>" readonly>
                    <div class="help-text">Username không thể thay đổi</div>
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-envelope"></i>
                        Email
                    </label>
                    <input type="email" class="form-control" 
                           value="<c:out value='${profileData.email}'/>" readonly>
                    <div class="help-text">Email không thể thay đổi</div>
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-venus-mars"></i>
                        Giới tính
                    </label>
                    <select class="form-control" name="gender">
                        <option value="">-- Chọn giới tính --</option>
                        <option value="male" ${profileData.gender == 'male' || profileData.gender == 'Male' ? 'selected' : ''}>Nam</option>
                        <option value="female" ${profileData.gender == 'female' || profileData.gender == 'Female' ? 'selected' : ''}>Nữ</option>
                        <option value="other" ${profileData.gender == 'other' || profileData.gender == 'Other' ? 'selected' : ''}>Khác</option>
                    </select>
                </div>
                
                <div class="form-group full-width">
                    <label class="form-label">
                        <i class="fas fa-map-marker-alt"></i>
                        Địa chỉ
                    </label>
                    <textarea class="form-control" name="address" rows="2" 
                              placeholder="Nhập địa chỉ của bạn..."><c:out value="${profileData.address}"/></textarea>
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
                           value="${profileData.height}" 
                           min="100" max="250" step="0.1" placeholder="VD: 175">
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-weight"></i>
                        Cân nặng (kg)
                    </label>
                    <input type="number" class="form-control" name="weight" 
                           value="${profileData.weight}" 
                           min="30" max="200" step="0.1" placeholder="VD: 70">
                    <div class="help-text">BMI sẽ tự động tính toán</div>
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-balance-scale"></i>
                        Chỉ số BMI
                    </label>
                    <input type="text" class="form-control" 
                           value="<c:choose><c:when test='${profileData.bmi != null}'><fmt:formatNumber value='${profileData.bmi}' pattern='#,##0.00'/></c:when><c:otherwise>--</c:otherwise></c:choose>" 
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
                           value="<c:out value='${profileData.emergencyContactName}'/>" 
                           placeholder="VD: Nguyễn Văn A">
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-phone-alt"></i>
                        Số điện thoại người thân
                    </label>
                    <input type="tel" class="form-control" name="emergencyContactPhone" 
                           value="<c:out value='${profileData.emergencyContactPhone}'/>" 
                           pattern="[0-9]{10}" placeholder="VD: 0987654321">
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-heart"></i>
                        Mối quan hệ
                    </label>
                    <input type="text" class="form-control" name="emergencyContactRelation" 
                           value="<c:out value='${profileData.emergencyContactRelation}'/>" 
                           placeholder="VD: Bố/Mẹ, Vợ/Chồng, Anh/Chị/Em">
                </div>
                
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-home"></i>
                        Địa chỉ người thân
                    </label>
                    <input type="text" class="form-control" name="emergencyContactAddress" 
                           value="<c:out value='${profileData.emergencyContactAddress}'/>" 
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

    // Avatar upload and preview
    function handleAvatarUpload(event) {
        const file = event.target.files[0];
        if (file) {
            // Check file type
            if (!file.type.startsWith('image/')) {
                alert('Vui lòng chọn file ảnh!');
                event.target.value = '';
                return;
            }
            
            // Check file size (max 5MB)
            if (file.size > 5 * 1024 * 1024) {
                alert('Kích thước file quá lớn! Vui lòng chọn ảnh nhỏ hơn 5MB.');
                event.target.value = '';
                return;
            }
            
            // Preview image
            const reader = new FileReader();
            reader.onload = function(e) {
                const avatarPreview = document.getElementById('avatarPreview');
                const avatarImg = document.getElementById('avatarImg');
                const avatarIcon = document.getElementById('avatarIcon');
                
                if (avatarImg) {
                    avatarImg.src = e.target.result;
                } else {
                    // Create img element if not exists
                    const img = document.createElement('img');
                    img.id = 'avatarImg';
                    img.src = e.target.result;
                    img.alt = 'Avatar';
                    if (avatarIcon) {
                        avatarIcon.remove();
                    }
                    avatarPreview.appendChild(img);
                }
                
                // Clear URL input when file is selected
                document.getElementById('avatarUrl').value = '';
            };
            reader.readAsDataURL(file);
        }
    }
    
    function updateAvatarPreview(url) {
        if (url && url.trim() !== '') {
            const avatarPreview = document.getElementById('avatarPreview');
            const avatarImg = document.getElementById('avatarImg');
            const avatarIcon = document.getElementById('avatarIcon');
            
            // Clear file input when URL is entered
            document.getElementById('avatarFile').value = '';
            
            if (avatarImg) {
                avatarImg.src = url;
            } else {
                // Create img element if not exists
                const img = document.createElement('img');
                img.id = 'avatarImg';
                img.src = url;
                img.alt = 'Avatar';
                img.onerror = function() {
                    alert('Không thể tải ảnh từ URL này!');
                    this.remove();
                    if (!document.getElementById('avatarIcon')) {
                        const icon = document.createElement('i');
                        icon.id = 'avatarIcon';
                        icon.className = 'fas fa-user';
                        avatarPreview.appendChild(icon);
                    }
                };
                if (avatarIcon) {
                    avatarIcon.remove();
                }
                avatarPreview.appendChild(img);
            }
        } else {
            // If URL is empty, show default icon
            const avatarImg = document.getElementById('avatarImg');
            const avatarIcon = document.getElementById('avatarIcon');
            if (avatarImg && !avatarIcon) {
                avatarImg.remove();
                const icon = document.createElement('i');
                icon.id = 'avatarIcon';
                icon.className = 'fas fa-user';
                document.getElementById('avatarPreview').appendChild(icon);
            }
        }
    }

    // Form validation
    document.getElementById('profileEditForm').addEventListener('submit', function(e) {
        const emergencyPhone = document.querySelector('input[name="emergencyContactPhone"]').value;
        
        // Validate phone numbers if provided
        const phoneRegex = /^[0-9]{10}$/;
        
        if (emergencyPhone && !phoneRegex.test(emergencyPhone.replace(/\s/g, ''))) {
            e.preventDefault();
            alert('Số điện thoại người thân không hợp lệ! Vui lòng nhập 10 chữ số.');
            return false;
        }
        
        // Show loading
        const submitBtn = this.querySelector('button[type="submit"]');
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang lưu...';
        submitBtn.disabled = true;
    });
</script>

<%@ include file="/views/common/footer.jsp" %>



