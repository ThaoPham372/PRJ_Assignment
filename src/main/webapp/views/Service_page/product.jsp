<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/views/common/header.jsp" %>

<style>
  :root {
    --primary: #141a49;
    --accent: #ec8b5a;
    --support: #ffde59;
    --text: #2c3e50;
    --text-light: #5a6c7d;
    --card: #ffffff;
    --shadow: rgba(0, 0, 0, 0.1);
    --shadow-hover: rgba(0, 0, 0, 0.15);
    --gradient-primary: linear-gradient(135deg, #141a49 0%, #1e2a5c 100%);
    --gradient-accent: linear-gradient(135deg, #ec8b5a 0%, #d67a4f 100%);
    --gradient-support: linear-gradient(135deg, #ffde59 0%, #f4d03f 100%);
  }

  .main-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 40px 50px;
  }

  .back-btn {
    margin-bottom: 30px;
    display: inline-flex;
    align-items: center;
    gap: 8px;
    color: var(--accent);
    text-decoration: none;
    font-weight: 600;
    transition: all 0.3s ease;
  }

  .back-btn:hover {
    transform: translateX(-5px);
    color: var(--primary);
  }

  .hero-section {
    position: relative;
    color: #fff;
    padding: 100px 50px;
    text-align: center;
    margin-bottom: 60px;
    border-radius: 20px;
    overflow: hidden;
    min-height: 400px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .hero-section::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url('${pageContext.request.contextPath}/images/service/pro7.jpg');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    filter: brightness(0.9) contrast(1.1);
    z-index: 1;
  }

  .hero-section::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(
      135deg,
      rgba(20, 26, 73, 0.85) 0%,
      rgba(20, 26, 73, 0.75) 100%
    );
    z-index: 2;
  }

  .hero-content {
    position: relative;
    z-index: 3;
    width: 100%;
    max-width: 1000px;
  }

  .hero-section h1 {
    font-size: 3.5rem;
    font-weight: 900;
    margin-bottom: 20px;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
    letter-spacing: -0.5px;
  }

  .hero-section p {
    font-size: 1.3rem;
    opacity: 0.95;
    max-width: 700px;
    margin: 0 auto;
    line-height: 1.6;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
  }

  .products-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 30px;
    margin-bottom: 60px;
  }

  .product-card {
    background: var(--card);
    border-radius: 20px;
    padding: 30px;
    box-shadow: 0 8px 30px var(--shadow);
    transition: all 0.3s ease;
    text-align: center;
  }

  .product-card:hover {
    transform: translateY(-10px);
    box-shadow: 0 15px 40px var(--shadow-hover);
  }

  .product-card img {
    width: 100%;
    height: 250px;
    object-fit: contain;
    border-radius: 15px;
    margin-bottom: 20px;
    padding: 20px;
    background: rgba(20, 26, 73, 0.05);
    transition: all 0.3s ease;
  }

  .product-card:hover img {
    background: rgba(20, 26, 73, 0.1);
    transform: scale(1.05);
  }

  .product-card h3 {
    color: var(--primary);
    font-size: 1.5rem;
    font-weight: 700;
    margin-bottom: 15px;
  }

  .product-card p {
    color: var(--text);
    font-size: 1rem;
    line-height: 1.6;
    margin-bottom: 20px;
  }

  .product-price {
    font-size: 1.5rem;
    font-weight: 800;
    color: var(--accent);
    margin-bottom: 20px;
  }

  .product-actions {
    display: flex;
    gap: 15px;
    justify-content: center;
  }

  .btn-buy {
    background: var(--gradient-accent);
    color: #fff;
    padding: 12px 24px;
    border-radius: 25px;
    font-weight: 600;
    text-decoration: none;
    transition: all 0.3s ease;
    flex: 1;
  }

  .btn-buy:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(236, 139, 90, 0.4);
  }

  .btn-cart {
    background: var(--primary);
    color: #fff;
    padding: 12px;
    border-radius: 50%;
    text-decoration: none;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 48px;
    height: 48px;
  }

  .btn-cart:hover {
    background: var(--accent);
    transform: scale(1.1);
  }

  .cta-section {
    background: var(--gradient-primary);
    color: #fff;
    padding: 50px;
    border-radius: 20px;
    text-align: center;
    margin-top: 40px;
  }

  .cta-section h3 {
    font-size: 2rem;
    margin-bottom: 20px;
    color: #fff;
  }

  .cta-section p {
    margin-bottom: 30px;
    opacity: 0.9;
    font-size: 1.1rem;
  }

  .cta-btn {
    background: var(--gradient-accent);
    color: #fff;
    padding: 18px 36px;
    border-radius: 50px;
    font-weight: 700;
    font-size: 1.1rem;
    text-decoration: none;
    display: inline-block;
    transition: all 0.3s ease;
    box-shadow: 0 8px 25px rgba(236, 139, 90, 0.4);
  }

  .cta-btn:hover {
    transform: translateY(-3px);
    box-shadow: 0 12px 35px rgba(236, 139, 90, 0.5);
  }

  /* Pagination Styles */
  .pagination {
    display: flex;
    justify-content: center;
    gap: 10px;
    margin-top: 40px;
    flex-wrap: wrap;
  }

  .pagination .btn {
    padding: 10px 18px;
    border-radius: 8px;
    text-decoration: none;
    font-weight: 600;
    font-size: 1rem;
    transition: all 0.3s ease;
    display: inline-block;
    min-width: 44px;
    text-align: center;
    border: 2px solid transparent;
  }

  .pagination .btn-outline {
    background: var(--card);
    color: var(--primary);
    border: 2px solid var(--primary);
  }

  .pagination .btn-outline:hover {
    background: var(--primary);
    color: #fff;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(20, 26, 73, 0.3);
  }

  .pagination .btn-primary {
    background: var(--gradient-accent);
    color: #fff;
    border: 2px solid var(--accent);
    box-shadow: 0 4px 12px rgba(236, 139, 90, 0.3);
  }

  .pagination .btn-primary:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(236, 139, 90, 0.4);
  }

  @media (max-width: 768px) {
    .main-content {
      padding: 20px;
    }

    .hero-section {
      padding: 60px 30px;
    }

    .hero-section h1 {
      font-size: 2.5rem;
    }

    .products-grid {
      grid-template-columns: 1fr;
      gap: 20px;
    }

    .product-card {
      padding: 20px;
    }

    .cta-section {
      padding: 40px 30px;
    }
  }
</style>

<!-- MAIN CONTENT -->
<main class="main-content">
  <a
    href="${pageContext.request.contextPath}/services"
    class="back-btn"
  >
    <i class="fas fa-arrow-left"></i>
    Quay lại Dịch vụ
  </a>

  <section class="hero-section">
    <div class="hero-content">
      <h1>SẢN PHẨM DINH DƯỠNG</h1>
      <p>
        Bổ sung dinh dưỡng chất lượng cao để hỗ trợ quá trình tập luyện và phát
        triển cơ bắp của bạn.
      </p>
    </div>
  </section>

  <!-- Search and Filter -->
  <div style="margin-bottom: 30px; display: flex; gap: 15px; flex-wrap: wrap;">
    <form method="get" action="${pageContext.request.contextPath}/products" style="flex: 1; min-width: 300px;">
      <input type="text" name="q" value="${searchKeyword}" placeholder="Tìm kiếm sản phẩm..." 
             style="width: 100%; padding: 12px; border: 2px solid #ddd; border-radius: 8px; font-size: 1rem;">
    </form>
    <select name="type" onchange="window.location.href='${pageContext.request.contextPath}/products?type=' + this.value + (document.querySelector('input[name=q]').value ? '&q=' + document.querySelector('input[name=q]').value : '')" 
            style="padding: 12px; border: 2px solid #ddd; border-radius: 8px; font-size: 1rem;">
      <option value="">Tất cả loại</option>
      <option value="SUPPLEMENT" ${selectedType == 'SUPPLEMENT' || selectedType == 'supplement' ? 'selected' : ''}>Thực phẩm bổ sung</option>
      <option value="EQUIPMENT" ${selectedType == 'EQUIPMENT' || selectedType == 'equipment' ? 'selected' : ''}>Thiết bị</option>
      <option value="APPAREL" ${selectedType == 'APPAREL' || selectedType == 'clothing' ? 'selected' : ''}>Trang phục</option>
      <option value="ACCESSORY" ${selectedType == 'ACCESSORY' || selectedType == 'accessory' ? 'selected' : ''}>Phụ kiện</option>
      <option value="OTHER" ${selectedType == 'OTHER' || selectedType == 'other' ? 'selected' : ''}>Khác</option>
    </select>
  </div>

  <c:if test="${not empty message}">
    <div style="background: #d4edda; color: #155724; padding: 15px; border-radius: 8px; margin-bottom: 20px;">
      ${message}
    </div>
  </c:if>
  <c:if test="${not empty error}">
    <div style="background: #f8d7da; color: #721c24; padding: 15px; border-radius: 8px; margin-bottom: 20px;">
      ${error}
    </div>
  </c:if>

  <div class="products-grid">
    <c:choose>
      <c:when test="${not empty products}">
        <c:forEach var="p" items="${products}">
          <div class="product-card">
            <img
              src="${pageContext.request.contextPath}/images/products/${p.productId}.png"
              onerror="this.src='${pageContext.request.contextPath}/images/placeholder.png';"
              alt="${fn:escapeXml(p.productName)}"
              loading="lazy"
            />
            <h3>${fn:escapeXml(p.productName)}</h3>
            <p>${fn:escapeXml(p.productType.displayName)}</p>
            <div class="product-price">
              <fmt:formatNumber value="${p.price}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
              <span style="font-size: 0.9rem; color: #666;">/ ${fn:escapeXml(p.unit)}</span>
            </div>
            <div style="margin: 10px 0; color: ${p.stockQuantity > 0 ? '#28a745' : '#dc3545'}; font-weight: 600;">
              <c:choose>
                <c:when test="${p.stockQuantity > 0}">✓ Còn hàng (${p.stockQuantity})</c:when>
                <c:otherwise>✗ Hết hàng</c:otherwise>
              </c:choose>
            </div>
            <div class="product-actions">
              <form method="post" action="${pageContext.request.contextPath}/cart/add" style="display: inline;" id="addToCartForm_${p.productId}">
                <input type="hidden" name="productId" value="${p.productId}"/>
                <input type="hidden" name="quantity" value="1"/>
                <button type="submit" class="btn-cart" ${p.stockQuantity <= 0 ? 'disabled' : ''} 
                        title="${p.stockQuantity <= 0 ? 'Sản phẩm hết hàng' : 'Thêm vào giỏ hàng'}">
                  <i class="fas fa-shopping-cart"></i>
                </button>
              </form>
            </div>
          </div>
        </c:forEach>
      </c:when>
      <c:otherwise>
        <div style="grid-column: 1 / -1; text-align: center; padding: 60px 20px; color: #666;">
          <i class="fas fa-search" style="font-size: 3rem; margin-bottom: 20px; opacity: 0.5;"></i>
          <h3>Không tìm thấy sản phẩm</h3>
          <p>Vui lòng thử tìm kiếm với từ khóa khác hoặc chọn loại sản phẩm khác.</p>
        </div>
      </c:otherwise>
    </c:choose>
  </div>

  <!-- Pagination -->
  <c:if test="${totalPages > 1}">
    <div class="pagination">
      <c:if test="${page > 1}">
        <a href="${pageContext.request.contextPath}/products?page=${page - 1}${searchKeyword != null ? '&q=' : ''}${searchKeyword != null ? fn:escapeXml(searchKeyword) : ''}${selectedType != null ? '&type=' : ''}${selectedType != null ? fn:escapeXml(selectedType) : ''}" 
           class="btn btn-outline">Trước</a>
      </c:if>
      <c:forEach var="i" begin="${page > 3 ? page - 2 : 1}" end="${page < totalPages - 2 ? page + 2 : totalPages}">
        <a href="${pageContext.request.contextPath}/products?page=${i}${searchKeyword != null ? '&q=' : ''}${searchKeyword != null ? fn:escapeXml(searchKeyword) : ''}${selectedType != null ? '&type=' : ''}${selectedType != null ? fn:escapeXml(selectedType) : ''}"
           class="btn ${i == page ? 'btn-primary' : 'btn-outline'}">${i}</a>
      </c:forEach>
      <c:if test="${page < totalPages}">
        <a href="${pageContext.request.contextPath}/products?page=${page + 1}${searchKeyword != null ? '&q=' : ''}${searchKeyword != null ? fn:escapeXml(searchKeyword) : ''}${selectedType != null ? '&type=' : ''}${selectedType != null ? fn:escapeXml(selectedType) : ''}"
           class="btn btn-outline">Sau</a>
      </c:if>
    </div>
  </c:if>

  <div class="cta-section">
    <h3>Mua sản phẩm dinh dưỡng ngay hôm nay!</h3>
    <p>
      Đăng ký ngay để được tư vấn miễn phí về sản phẩm phù hợp và nhận ưu đãi
      đặc biệt.
    </p>
  
    <a
      href="javascript:void(0)"
      onclick="window.location.href='${pageContext.request.contextPath}/advisory'"
      class="cta-btn"
      >TƯ VẤN NGAY</a
    >
  </div>
</main>

<script>
  // Ensure forms submit correctly
  document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form[action*="/cart/add"]');
    forms.forEach(form => {
      form.addEventListener('submit', function(e) {
        // Allow form to submit naturally - servlet will handle redirect
        console.log('Submitting to cart:', this.action);
        // Don't preventDefault - let the form submit normally
      });
    });
  });
</script>

<%@ include file="/views/common/footer.jsp" %>
