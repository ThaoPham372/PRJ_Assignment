<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/views/common/header.jsp" %>

<style>
    .form-container {
        max-width: 800px;
        margin: 40px auto;
        background: white;
        padding: 40px;
        border-radius: 15px;
        box-shadow: 0 2px 20px rgba(0,0,0,0.1);
    }
    
    .form-group {
        margin-bottom: 20px;
    }
    
    .form-label {
        display: block;
        font-weight: 600;
        margin-bottom: 8px;
        color: #141a46;
    }
    
    .form-control {
        width: 100%;
        padding: 12px;
        border: 2px solid #ddd;
        border-radius: 8px;
        font-size: 1rem;
        transition: all 0.3s;
    }
    
    .form-control:focus {
        outline: none;
        border-color: #ec8b5e;
        box-shadow: 0 0 0 3px rgba(236, 139, 94, 0.1);
    }
    
    .btn-submit {
        background: #ec8b5e;
        color: white;
        padding: 12px 30px;
        border: none;
        border-radius: 8px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s;
    }
    
    .btn-submit:hover {
        background: #d67a4f;
        transform: translateY(-2px);
    }
    
    .info-box {
        background: #e7f3ff;
        border-left: 4px solid #2196F3;
        padding: 15px;
        margin-bottom: 20px;
        border-radius: 4px;
    }
    
    .info-box strong {
        color: #1976D2;
    }
</style>

<div class="form-container">
    <div class="mb-4">
        <a href="${pageContext.request.contextPath}/admin/users" class="btn-back">
            <i class="fas fa-arrow-left"></i>
            <span>Quay lại</span>
        </a>
    </div>

    <h2><i class="fas fa-user"></i> ${mode == 'add' ? 'Thêm User Mới' : 'Chỉnh Sửa User'}</h2>
    
    <c:if test="${mode == 'add'}">
        <div class="info-box">
            <strong><i class="fas fa-info-circle"></i> Lưu ý:</strong><br/>
            Hệ thống sẽ <strong>tự động tạo record</strong> trong bảng tương ứng dựa trên Role:<br/>
            • <strong>MEMBER</strong> → Tự động tạo record trong bảng <strong>members</strong><br/>
            • <strong>ADMIN</strong> → Tự động tạo record trong bảng <strong>admin</strong><br/>
            • <strong>TRAINER</strong> → Tự động tạo record trong bảng <strong>trainer</strong>
        </div>
    </c:if>
    
    <form method="post" action="${pageContext.request.contextPath}/admin/users">
        <input type="hidden" name="action" value="${mode == 'add' ? 'create' : 'update'}"/>
        <c:if test="${mode == 'edit'}">
            <input type="hidden" name="userId" value="${user.userId}"/>
        </c:if>
        
        <div class="form-group">
            <label class="form-label">Username *</label>
            <input type="text" name="username" class="form-control" required
                   value="${fn:escapeXml(user.username)}"
                   ${mode == 'edit' ? 'readonly style="background:#f5f5f5"' : ''}/>
        </div>
        
        <div class="form-group">
            <label class="form-label">Tên Đầy Đủ *</label>
            <input type="text" name="name" class="form-control" required
                   value="${fn:escapeXml(user.name)}"/>
        </div>
        
        <div class="form-group">
            <label class="form-label">Email *</label>
            <input type="email" name="email" class="form-control" required
                   value="${fn:escapeXml(user.email)}"/>
        </div>
        
        <div class="form-group">
            <label class="form-label">Phone</label>
            <input type="tel" name="phone" class="form-control"
                   value="${fn:escapeXml(user.phone)}"/>
        </div>
        
        <c:choose>
            <c:when test="${mode == 'add'}">
                <div class="form-group">
                    <label class="form-label">Password *</label>
                    <input type="password" name="password" class="form-control" required
                           minlength="6" placeholder="Tối thiểu 6 ký tự"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="form-group">
                    <label class="form-label">Password Mới (để trống nếu không đổi)</label>
                    <input type="password" name="newPassword" class="form-control"
                           minlength="6" placeholder="Nhập password mới hoặc để trống"/>
                </div>
            </c:otherwise>
        </c:choose>
        
        <div class="form-group">
            <label class="form-label">Role * 
                <c:if test="${mode == 'edit' && user.role == 'USER'}">
                    <span style="color: #2196F3;">(Member record đã tồn tại)</span>
                </c:if>
            </label>
            <select name="role" class="form-control" required>
                <option value="">-- Chọn Role --</option>
                <option value="USER" ${user.role == 'USER' ? 'selected' : ''}>MEMBER (Tự động tạo Member record)</option>
                <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>ADMIN (Tự động tạo Admin record)</option>
                <option value="TRAINER" ${user.role == 'TRAINER' || user.role == 'PT' ? 'selected' : ''}>TRAINER (Tự động tạo Trainer record)</option>
            </select>
        </div>
        
        <div class="form-group">
            <label class="form-label">Trạng Thái *</label>
            <select name="status" class="form-control" required>
                <option value="ACTIVE" ${user.status == 'ACTIVE' || empty user ? 'selected' : ''}>ACTIVE</option>
                <option value="INACTIVE" ${user.status == 'INACTIVE' ? 'selected' : ''}>INACTIVE</option>
                <option value="LOCKED" ${user.status == 'LOCKED' ? 'selected' : ''}>LOCKED</option>
            </select>
        </div>
        
        <button type="submit" class="btn-submit">
            <i class="fas fa-save"></i> ${mode == 'add' ? 'Tạo User' : 'Cập Nhật'}
        </button>
    </form>
</div>

<%@ include file="/views/common/footer.jsp" %>

