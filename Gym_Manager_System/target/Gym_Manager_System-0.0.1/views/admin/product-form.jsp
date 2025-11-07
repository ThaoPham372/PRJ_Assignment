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
    
    .form-check {
        display: flex;
        align-items: center;
        gap: 10px;
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
</style>

<div class="form-container">
    <div class="mb-4">
        <a href="${pageContext.request.contextPath}/admin/products" class="btn-back">
            <i class="fas fa-arrow-left"></i>
            <span>Quay lại</span>
        </a>
    </div>

    <h2><i class="fas fa-box"></i> ${mode == 'add' ? 'Thêm Sản Phẩm Mới' : 'Chỉnh Sửa Sản Phẩm'}</h2>
    
    <form method="post" action="${pageContext.request.contextPath}/admin/products">
        <input type="hidden" name="action" value="${mode == 'add' ? 'create' : 'update'}"/>
        <c:if test="${mode == 'edit'}">
            <input type="hidden" name="productId" value="${product.productId}"/>
        </c:if>
        
        <div class="form-group">
            <label class="form-label">Tên Sản Phẩm *</label>
            <input type="text" name="productName" class="form-control" required
                   value="${fn:escapeXml(product.productName)}"/>
        </div>
        
        <div class="form-group">
            <label class="form-label">Loại Sản Phẩm *</label>
            <select name="productType" class="form-control" required>
                <option value="">-- Chọn Loại --</option>
                <option value="SUPPLEMENT" ${product.productType == 'SUPPLEMENT' ? 'selected' : ''}>Thực Phẩm Bổ Sung</option>
                <option value="EQUIPMENT" ${product.productType == 'EQUIPMENT' ? 'selected' : ''}>Thiết Bị</option>
                <option value="APPAREL" ${product.productType == 'APPAREL' ? 'selected' : ''}>Trang Phục</option>
                <option value="ACCESSORY" ${product.productType == 'ACCESSORY' ? 'selected' : ''}>Phụ Kiện</option>
                <option value="OTHER" ${product.productType == 'OTHER' ? 'selected' : ''}>Khác</option>
            </select>
        </div>
        
        <div class="form-group">
            <label class="form-label">Giá Bán *</label>
            <input type="number" name="price" class="form-control" required min="0" step="1000"
                   value="${product.price}"/>
        </div>
        
        <div class="form-group">
            <label class="form-label">Giá Vốn</label>
            <input type="number" name="costPrice" class="form-control" min="0" step="1000"
                   value="${product.costPrice}"/>
        </div>
        
        <div class="form-group">
            <label class="form-label">Mô Tả</label>
            <textarea name="description" class="form-control" rows="4">${fn:escapeXml(product.description)}</textarea>
        </div>
        
        <div class="form-group">
            <label class="form-label">Số Lượng Tồn Kho *</label>
            <input type="number" name="stockQuantity" class="form-control" required min="0"
                   value="${product.stockQuantity != null ? product.stockQuantity : 0}"/>
        </div>
        
        <div class="form-group">
            <label class="form-label">Đơn Vị *</label>
            <input type="text" name="unit" class="form-control" required
                   value="${fn:escapeXml(product.unit)}" placeholder="VD: cái, hộp, kg..."/>
        </div>
        
        <div class="form-group">
            <div class="form-check">
                <input type="checkbox" name="active" id="active" 
                       ${mode == 'add' || product.active ? 'checked' : ''}/>
                <label for="active">Kích hoạt (hiển thị trên trang bán hàng)</label>
            </div>
        </div>
        
        <button type="submit" class="btn-submit">
            <i class="fas fa-save"></i> ${mode == 'add' ? 'Tạo Sản Phẩm' : 'Cập Nhật'}
        </button>
    </form>
</div>

<%@ include file="/views/common/footer.jsp" %>

