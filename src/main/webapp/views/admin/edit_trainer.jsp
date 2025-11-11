<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Chỉnh sửa PT - GymFit</title>

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
        padding: 20px;
      }

      .container {
        max-width: 800px;
        margin: 0 auto;
        background: white;
        padding: 30px;
        border-radius: 15px;
        box-shadow: 0 4px 15px var(--shadow);
      }

      .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 30px;
        padding-bottom: 20px;
        border-bottom: 2px solid #e0e0e0;
      }

      .header h1 {
        font-size: 1.8rem;
        color: var(--primary);
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
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
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

      .form-group {
        margin-bottom: 20px;
      }

      .form-label {
        display: block;
        margin-bottom: 8px;
        font-weight: 600;
        color: var(--text);
      }

      .form-input, .form-select {
        width: 100%;
        padding: 12px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 0.95rem;
        transition: border-color 0.3s ease;
      }

      .form-input:focus, .form-select:focus {
        outline: none;
        border-color: var(--accent);
      }

      .form-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 20px;
      }

      .form-actions {
        display: flex;
        gap: 15px;
        justify-content: flex-end;
        margin-top: 30px;
        padding-top: 20px;
        border-top: 2px solid #e0e0e0;
      }

      .message {
        padding: 15px;
        border-radius: 8px;
        margin-bottom: 20px;
      }

      .message.success {
        background: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
      }

      .message.error {
        background: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
      }

      small {
        color: #666;
        font-size: 0.85rem;
      }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1><i class="fas fa-user-edit"></i> Chỉnh sửa thông tin PT</h1>
            <a href="${pageContext.request.contextPath}/admin/trainer-management" class="btn btn-outline">
                <i class="fas fa-arrow-left"></i> Quay lại
            </a>
        </div>

        <c:if test="${not empty success}">
            <div class="message success">
                <i class="fas fa-check-circle"></i> ${success}
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message error">
                <i class="fas fa-exclamation-circle"></i> ${error}
            </div>
        </c:if>

        <c:if test="${not empty editTrainer}">
            <form action="${pageContext.request.contextPath}/admin/trainer-management" method="post">
                <input type="hidden" name="action" value="update" />
                <input type="hidden" name="id" value="${editTrainer.id}" />
                
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Họ và tên *</label>
                        <input type="text" name="name" class="form-input" value="${editTrainer.name}" required />
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Email *</label>
                        <input type="email" name="email" class="form-input" value="${editTrainer.email}" required />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Số điện thoại *</label>
                        <input type="text" name="phone" id="phoneInput" class="form-input" 
                               value="${editTrainer.phone != null ? editTrainer.phone : ''}" 
                               pattern="[0-9]{10,11}" 
                               title="Số điện thoại phải có 10-11 chữ số" 
                               required />
                        <small>Số điện thoại 10-11 chữ số</small>
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Username</label>
                        <input type="text" name="username" id="usernameInput" class="form-input" 
                               value="${editTrainer.username}" 
                               readonly 
                               style="background-color: #f5f5f5; cursor: not-allowed;" />
                        <small style="color: #999;">Username không thể thay đổi</small>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Mật khẩu</label>
                    <input type="text" class="form-input" 
                           value="********" 
                           readonly 
                           style="background-color: #f5f5f5; cursor: not-allowed;" />
                    <input type="hidden" name="password" value="" />
                    <small style="color: #999;">Mật khẩu không thể thay đổi từ đây</small>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Địa chỉ</label>
                        <input type="text" name="address" class="form-input" value="${editTrainer.address != null ? editTrainer.address : ''}" />
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Ngày sinh</label>
                        <c:choose>
                            <c:when test="${editTrainer.dob != null}">
                                <fmt:formatDate value="${editTrainer.dob}" pattern="yyyy-MM-dd" var="formattedDob" />
                                <input type="date" name="birthday" class="form-input" value="${formattedDob}" />
                            </c:when>
                            <c:otherwise>
                                <input type="date" name="birthday" class="form-input" />
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Giới tính</label>
                    <select name="gender" class="form-select">
                        <option value="">-- Chọn giới tính --</option>
                        <option value="Nam" ${editTrainer.gender == 'Nam' ? 'selected' : ''}>Nam</option>
                        <option value="Nữ" ${editTrainer.gender == 'Nữ' ? 'selected' : ''}>Nữ</option>
                        <option value="Khác" ${editTrainer.gender == 'Khác' ? 'selected' : ''}>Khác</option>
                    </select>
                </div>

                <hr style="margin: 30px 0; border: none; border-top: 2px solid #e0e0e0;" />

                <h2 style="margin-bottom: 20px; color: var(--primary);">Thông tin chuyên môn</h2>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Chuyên môn</label>
                        <input type="text" name="specialization" class="form-input" 
                            value="${editTrainer.specialization != null ? editTrainer.specialization : ''}" 
                            placeholder="VD: Cardio, Yoga, Bodybuilding" />
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Số năm kinh nghiệm</label>
                        <input type="number" name="yearsOfExperience" class="form-input" 
                            value="${editTrainer.yearsOfExperience != null ? editTrainer.yearsOfExperience : ''}" 
                            min="0" max="100" />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Trình độ chứng chỉ</label>
                        <input type="text" name="certificationLevel" class="form-input" 
                            value="${editTrainer.certificationLevel != null ? editTrainer.certificationLevel : ''}" 
                            placeholder="VD: ACE, NASM, CPT" />
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Lương</label>
                        <input type="number" name="salary" class="form-input" 
                            value="${editTrainer.salary != null ? editTrainer.salary : ''}" 
                            step="0.01" min="0" />
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Cơ sở làm việc *</label>
                    <select name="workAt" id="workAtSelect" class="form-select" required>
                        <option value="">-- Chọn cơ sở --</option>
                        <c:forEach var="gym" items="${gyms}">
                            <option value="${gym.gymId}" 
                                <c:if test="${editTrainer.workAt != null && (editTrainer.workAt == gym.gymId.toString() || editTrainer.workAt.equals(gym.gymId.toString()))}">selected</c:if>>
                                ${gym.name}<c:if test="${gym.address != null && !gym.address.isEmpty()}"> - ${gym.address}</c:if>
                            </option>
                        </c:forEach>
                    </select>
                    <small>Chọn cơ sở gym mà PT sẽ làm việc</small>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/admin/trainer-management" class="btn btn-outline">
                        <i class="fas fa-times"></i> Hủy
                    </a>
                    <button type="submit" class="btn" onclick="return validateEditTrainerForm()">
                        <i class="fas fa-save"></i> Lưu thay đổi
                    </button>
                </div>
            </form>
            
            <script>
                function validateEditTrainerForm() {
                    const form = document.querySelector('form');
                    
                    // Validate required fields (username và password không cần validate vì đã bị disable)
                    const name = form.querySelector('input[name="name"]').value.trim();
                    const email = form.querySelector('input[name="email"]').value.trim();
                    const phone = form.querySelector('input[name="phone"]').value.trim();
                    const workAt = form.querySelector('select[name="workAt"]').value;
                    
                    if (!name) {
                        alert('Vui lòng nhập họ và tên');
                        return false;
                    }
                    
                    if (!email) {
                        alert('Vui lòng nhập email');
                        return false;
                    }
                    
                    // Validate email format
                    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    if (!emailRegex.test(email)) {
                        alert('Email không hợp lệ');
                        return false;
                    }
                    
                    if (!phone) {
                        alert('Vui lòng nhập số điện thoại');
                        return false;
                    }
                    
                    // Validate phone format (10-11 digits)
                    const phoneRegex = /^[0-9]{10,11}$/;
                    if (!phoneRegex.test(phone)) {
                        alert('Số điện thoại phải có 10-11 chữ số');
                        return false;
                    }
                    
                    if (!workAt) {
                        alert('Vui lòng chọn cơ sở làm việc');
                        return false;
                    }
                    
                    return true;
                }
            </script>
        </c:if>

        <c:if test="${empty editTrainer}">
            <div class="message error">
                <i class="fas fa-exclamation-circle"></i> Không tìm thấy thông tin PT
            </div>
            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/admin/trainer-management" class="btn btn-outline">
                    <i class="fas fa-arrow-left"></i> Quay lại
                </a>
            </div>
        </c:if>
    </div>
</body>
</html>

