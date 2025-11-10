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

  /* SERVICES SECTION */
  .services-section {
    background: var(--primary);
    margin: 0;
    padding: 60px 50px;
    border: 3px solid var(--primary);
  }

  .services-container {
    max-width: 1200px;
    margin: 0 auto;
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 0;
    background: var(--card);
    border-radius: 15px;
    overflow: hidden;
    box-shadow: 0 8px 30px var(--shadow);
  }

  .service-item {
    padding: 60px 40px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    position: relative;
    min-height: 400px;
    text-decoration: none;
    color: inherit;
    transition: all 0.3s ease;
    cursor: pointer;
  }

  .service-item:hover {
    background: rgba(236, 139, 90, 0.05);
    transform: translateY(-5px);
  }

  .service-item:first-child {
    border-right: 2px solid var(--primary);
  }

  .service-item img {
    width: 100%;
    max-width: 300px;
    height: 200px;
    object-fit: cover;
    border-radius: 12px;
    margin-bottom: 30px;
    box-shadow: 0 8px 25px var(--shadow);
    transition: transform 0.3s ease;
  }

  .service-item:hover img {
    transform: scale(1.05);
  }

  .service-item h2 {
    color: var(--primary);
    font-size: 2rem;
    font-weight: 800;
    margin-bottom: 20px;
    letter-spacing: -0.02em;
  }

  .service-item p {
    color: var(--text-light);
    font-size: 1.1rem;
    line-height: 1.6;
    max-width: 300px;
  }

  /* PRODUCT SECTION */
  .product-section {
    padding: 80px 50px;
    background: #ffffff;
  }

  .product-container {
    max-width: 1200px;
    margin: 0 auto;
  }

  .product-title {
    text-align: center;
    color: var(--primary);
    font-size: 2.5rem;
    font-weight: 800;
    margin-bottom: 60px;
    position: relative;
  }

  .product-title::after {
    content: '';
    position: absolute;
    bottom: -15px;
    left: 50%;
    transform: translateX(-50%);
    width: 80px;
    height: 4px;
    background: var(--gradient-accent);
    border-radius: 2px;
  }

  .product-carousel {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 30px;
  }

  .carousel-nav {
    background: var(--primary);
    color: #fff;
    border: none;
    padding: 15px 20px;
    border-radius: 50%;
    font-size: 1.5rem;
    cursor: pointer;
    transition: all 0.3s ease;
    box-shadow: 0 4px 15px var(--shadow);
  }

  .carousel-nav:hover {
    background: var(--accent);
    transform: scale(1.1);
  }

  .carousel-nav:disabled {
    opacity: 0.5;
    cursor: not-allowed;
    transform: none;
  }

  .product-items {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
    align-items: stretch;
    
  }

  .product-item {
    background: var(--primary);
    color: #fff;
    padding: 25px 20px;
    border-radius: 20px;
    text-align: center;
    min-width: 280px;
    max-width: 300px;
    min-height: 380px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    box-shadow: 0 8px 25px var(--shadow);
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;
    flex: 1;
    text-decoration: none;
    cursor: pointer;
    border: 1px solid rgba(255, 255, 255, 0.1);
    gap: 15px;
  }

  .product-item:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 35px var(--shadow-hover);
    border-color: rgba(255, 255, 255, 0.2);
    background: linear-gradient(145deg, var(--primary), #1a2159);
  }

  .product-item:hover .img-wrapper {
    background: rgba(255, 255, 255, 0.15);
    transform: scale(1.02);
  }

  .product-item .img-wrapper {
    width: 100%;
    height: 220px;
    margin-bottom: 15px;
    padding: 20px;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
  }

  .product-item img {
    width: 100%;
    height: 100%;
    object-fit: contain;
    transition: all 0.3s ease;
  }

  .product-item:hover img {
    transform: scale(1.05);
  }

  .product-item h3 {
    font-size: 1.4rem;
    font-weight: 700;
    margin-bottom: 10px;
    color: var(--accent);
    text-transform: uppercase;
    letter-spacing: 0.5px;
  }

  .product-item p {
    font-size: 1rem;
    opacity: 0.9;
    line-height: 1.4;
    color: rgba(255, 255, 255, 0.9);
    font-weight: 500;
  }

  /* SUPPORT BUTTONS */
  .support-buttons {
    position: fixed;
    bottom: 30px;
    right: 30px;
    display: flex;
    flex-direction: column;
    gap: 15px;
    z-index: 50;
  }

  .support-btn {
    background: var(--gradient-support);
    color: #000;
    padding: 18px 24px;
    border-radius: 50px;
    font-weight: 700;
    font-size: 0.95rem;
    cursor: pointer;
    box-shadow: 0 8px 25px rgba(255, 222, 89, 0.4);
    display: flex;
    align-items: center;
    gap: 10px;
    transition: all 0.3s ease;
    border: none;
    text-decoration: none;
    justify-content: center;
    min-width: 140px;
  }

  .support-btn:hover {
    transform: scale(1.1);
    box-shadow: 0 12px 35px rgba(255, 222, 89, 0.5);
  }

  .support-btn i {
    font-size: 1.2rem;
  }

  /* RESPONSIVE */
  @media (max-width: 1024px) {
    .services-container {
      grid-template-columns: 1fr;
    }

    .service-item:first-child {
      border-right: none;
      border-bottom: 2px solid var(--primary);
    }

    .product-carousel {
      flex-direction: column;
      gap: 20px;
    }

    .product-items {
      grid-template-columns: repeat(2, 1fr);
      gap: 15px;
    }
  }

  @media (max-width: 768px) {
    .services-section {
      padding: 40px 20px;
    }

    .service-item {
      padding: 40px 20px;
      min-height: 300px;
    }

    .service-item h2 {
      font-size: 1.5rem;
    }

    .product-section {
      padding: 60px 20px;
    }

    .product-title {
      font-size: 2rem;
    }

    .support-buttons {
      bottom: 20px;
      right: 20px;
      gap: 12px;
    }

    .support-btn {
      padding: 15px 20px;
      font-size: 0.9rem;
      min-width: 120px;
    }
  }

  @media (max-width: 480px) {
    .service-item {
      padding: 30px 15px;
    }

    .service-item img {
      height: 150px;
    }

    .product-item {
      min-width: 250px;
      padding: 30px 20px;
    }

    .product-items {
      grid-template-columns: 1fr;
      gap: 15px;
    }
  }
  
  /* Thêm khối CSS cho nút TƯ VẤN (floating-btn.tu-van) */
.floating-btn {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 15px 25px;
    border: none;
    border-radius: 50px;
    font-size: 1.1rem;
    font-weight: 700;
    cursor: pointer;
    transition: all 0.3s ease;
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
    text-decoration: none;
    color: var(--primary); /* Màu chữ đậm từ trang chủ */
}

.floating-btn.tu-van {
    background: var(--chat); /* Màu vàng #ffde59 */
    box-shadow: 0 8px 15px rgba(255, 222, 89, 0.4);
}

.floating-btn.tu-van:hover {
    background: #f0c84c;
    transform: translateY(-2px);
    box-shadow: 0 12px 35px rgba(255, 222, 89, 0.5);
}

.floating-btn i {
    font-size: 1.2rem;
    color: var(--primary);
}
  
  
</style>

<!-- SERVICES SECTION -->
<section class="services-section">
  <div class="services-container">
    <a
      href="${pageContext.request.contextPath}/views/Service_page/personalTraining.jsp"
      class="service-item"
    >
      <img
        src="https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80"
        alt="Personal Training"
        loading="lazy"
        decoding="async"
        referrerpolicy="no-referrer"
      />
      <h2>PERSONAL TRAINING</h2>
      <p>
        Huấn luyện cá nhân 1-1 với HLV chuyên nghiệp, chương trình tập luyện
        được thiết kế riêng cho từng cá nhân để đạt được mục tiêu fitness hiệu
        quả nhất.
      </p>
    </a>
    <a
      href="${pageContext.request.contextPath}/views/Service_page/groupTraining.jsp"
      class="service-item"
    >
      <img
        src="https://images.unsplash.com/photo-1540497077202-7c8a3999166f?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80"
        alt="Group Training"
        loading="lazy"
        decoding="async"
        referrerpolicy="no-referrer"
      />
      <h2>GROUP TRAINING</h2>
      <p>
        Lớp tập nhóm tạo động lực và gắn kết, giúp bạn có thêm bạn bè và động
        lực tập luyện mỗi ngày trong môi trường thân thiện và chuyên nghiệp.
      </p>
    </a>
  </div>
</section>

<!-- PRODUCT SECTION -->
<section class="product-section">
  <div class="product-container">
    <h2 class="product-title">PRODUCT</h2>
    <div class="product-carousel">
      <button class="carousel-nav" id="prevBtn" onclick="changeProduct(-1)">
        <i class="fas fa-chevron-left"></i>
      </button>

      <div class="product-items">
        <c:choose>
          <c:when test="${not empty products}">
            <c:forEach var="p" items="${products}" varStatus="loop" end="3">
              <c:if test="${loop.index < 4}">
                <a
                  href="${pageContext.request.contextPath}/products"
                  class="product-item"
                  id="product${loop.index + 1}"
                  data-product-id="${p.productId}"
                >
                  <div class="img-wrapper">
                    <img
                      src="${pageContext.request.contextPath}/images/products/${p.productId}.png"
                      onerror="this.src='${pageContext.request.contextPath}/images/placeholder.png';"
                      alt="${fn:escapeXml(p.productName)}"
                      loading="lazy"
                      decoding="async"
                    />
                  </div>
                  <h3>${fn:escapeXml(p.productName)}</h3>
                  <p>${fn:escapeXml(p.productType.displayName)}</p>
                </a>
              </c:if>
            </c:forEach>
            <!-- Đảm bảo luôn có đủ 4 items -->
            <c:if test="${products.size() < 4}">
              <c:forEach begin="${products.size()}" end="3" varStatus="emptyLoop">
                <a
                  href="${pageContext.request.contextPath}/products"
                  class="product-item"
                  id="product${emptyLoop.index + 1}"
                >
                  <div class="img-wrapper">
                    <img
                      src="${pageContext.request.contextPath}/images/placeholder.png"
                      onerror="this.src='${pageContext.request.contextPath}/images/placeholder.png';"
                      alt="Sản phẩm"
                      loading="lazy"
                      decoding="async"
                    />
                  </div>
                  <h3>Sản phẩm sắp có</h3>
                  <p>Đang cập nhật</p>
                </a>
              </c:forEach>
            </c:if>
          </c:when>
          <c:otherwise>
            <!-- Fallback khi không có sản phẩm -->
            <a
              href="${pageContext.request.contextPath}/views/Service_page/product.jsp"
              class="product-item"
              id="product1"
            >
              <div class="img-wrapper">
                <img
                  src="${pageContext.request.contextPath}/images/placeholder.png"
                  onerror="this.src='${pageContext.request.contextPath}/images/placeholder.png';"
                  alt="Sản phẩm"
                  loading="lazy"
                  decoding="async"
                />
              </div>
              <h3>Không có sản phẩm</h3>
              <p>Vui lòng quay lại sau</p>
            </a>
            <a
              href="${pageContext.request.contextPath}/views/Service_page/product.jsp"
              class="product-item"
              id="product2"
            >
              <div class="img-wrapper">
                <img
                  src="${pageContext.request.contextPath}/images/placeholder.png"
                  onerror="this.src='${pageContext.request.contextPath}/images/placeholder.png';"
                  alt="Sản phẩm"
                  loading="lazy"
                  decoding="async"
                />
              </div>
              <h3>Không có sản phẩm</h3>
              <p>Vui lòng quay lại sau</p>
            </a>
            <a
              href="${pageContext.request.contextPath}/views/Service_page/product.jsp"
              class="product-item"
              id="product3"
            >
              <div class="img-wrapper">
                <img
                  src="${pageContext.request.contextPath}/images/placeholder.png"
                  onerror="this.src='${pageContext.request.contextPath}/images/placeholder.png';"
                  alt="Sản phẩm"
                  loading="lazy"
                  decoding="async"
                />
              </div>
              <h3>Không có sản phẩm</h3>
              <p>Vui lòng quay lại sau</p>
            </a>
            <a
              href="${pageContext.request.contextPath}/views/Service_page/product.jsp"
              class="product-item"
              id="product4"
            >
              <div class="img-wrapper">
                <img
                  src="${pageContext.request.contextPath}/images/placeholder.png"
                  onerror="this.src='${pageContext.request.contextPath}/images/placeholder.png';"
                  alt="Sản phẩm"
                  loading="lazy"
                  decoding="async"
                />
              </div>
              <h3>Không có sản phẩm</h3>
              <p>Vui lòng quay lại sau</p>
            </a>
          </c:otherwise>
        </c:choose>
      </div>

      <button class="carousel-nav" id="nextBtn" onclick="changeProduct(1)">
        <i class="fas fa-chevron-right"></i>
      </button>
    </div>
  </div>
</section>

<%@ include file="/views/common/footer.jsp" %>

<!-- Floating Buttons -->
<style>
  .floating-buttons1 {
    position: fixed !important;
    bottom: 30px !important;
    right: 30px !important;
    display: flex !important;
    flex-direction: column !important;
    gap: 18px !important;
    z-index: 10000 !important;
    visibility: visible !important;
    opacity: 1 !important;
  }
  .floating-btn, #chatToggleButton {
    display: flex !important;
    visibility: visible !important;
    opacity: 1 !important;
  }
</style>
 <div class="floating-buttons1">
    <button
      class="floating-btn tu-van"
      onclick="window.location.href='${pageContext.request.contextPath}/advisory'"
    >
      <i class="fas fa-user-tie"></i> TƯ VẤN
    </button>
    <%@ include file="/views/common/chatbot.jsp" %>
  </div>

<!-- SCRIPT -->
<script>
  // Product carousel data from server
  const products = [
    <c:choose>
      <c:when test="${not empty products}">
        <c:forEach var="p" items="${products}" varStatus="loop">
          {
            id: ${p.productId},
            name: '<c:out value="${p.productName}" escapeXml="true" />',
            description: '<c:out value="${p.productType.displayName}" escapeXml="true" />',
            image: '${pageContext.request.contextPath}/images/products/${p.productId}.png'
          }<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
      </c:when>
      <c:otherwise>
        // Fallback products khi không có dữ liệu
        {
          id: 0,
          name: 'Sản phẩm mẫu 1',
          description: 'Thực phẩm bổ sung',
          image: '${pageContext.request.contextPath}/images/placeholder.png',
        },
        {
          id: 0,
          name: 'Sản phẩm mẫu 2',
          description: 'Thiết bị',
          image: '${pageContext.request.contextPath}/images/placeholder.png',
        },
        {
          id: 0,
          name: 'Sản phẩm mẫu 3',
          description: 'Trang phục',
          image: '${pageContext.request.contextPath}/images/placeholder.png',
        },
        {
          id: 0,
          name: 'Sản phẩm mẫu 4',
          description: 'Khác',
          image: '${pageContext.request.contextPath}/images/placeholder.png',
        }
      </c:otherwise>
    </c:choose>
  ];

  let currentIndex = 0;

  function changeProduct(direction) {
    if (products.length === 0) return;
    
    currentIndex += direction;

    // Loop through products
    if (currentIndex >= products.length) {
      currentIndex = 0;
    } else if (currentIndex < 0) {
      currentIndex = products.length - 1;
    }

    // Update products with animation
    const productItems = ['product1', 'product2', 'product3', 'product4'];

    productItems.forEach((id, index) => {
      const product = document.getElementById(id);
      if (!product) return;
      
      const productIndex = (currentIndex + index) % products.length;
      const productData = products[productIndex];

      product.style.opacity = '0';

      setTimeout(() => {
        const img = product.querySelector('img');
        const h3 = product.querySelector('h3');
        const p = product.querySelector('p');
        
        if (img) {
          img.src = productData.image || '${pageContext.request.contextPath}/images/placeholder.png';
          img.alt = productData.name || 'Sản phẩm';
          // Reset onerror handler
          img.onerror = function() {
            this.src = '${pageContext.request.contextPath}/images/placeholder.png';
          };
        }
        if (h3) {
          h3.textContent = productData.name || 'Sản phẩm';
        }
        if (p) {
          p.textContent = productData.description || 'Sản phẩm';
        }
        
        // Update product ID if exists
        if (productData.id) {
          product.setAttribute('data-product-id', productData.id);
        }
        
        product.style.opacity = '1';
      }, 150);
    });
  }

  // Enhanced image fallback handling
  function attachImageFallback() {
    document.querySelectorAll('img').forEach(function (img) {
      img.addEventListener(
        'error',
        function () {
          if (!img.dataset.fallback) {
            img.dataset.fallback = '1';
            img.src =
              'https://via.placeholder.com/400x300/141a49/ffffff?text=Gym+Product';
            img.style.opacity = '0.7';
          }
        },
        { once: true },
      );
    });
  }

  attachImageFallback();

  // Add intersection observer for animations
  const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -50px 0px',
  };

  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.style.opacity = '1';
        entry.target.style.transform = 'translateY(0)';
      }
    });
  }, observerOptions);

  // Observe service items and product items for animation (exclude floating buttons)
  document.querySelectorAll('.service-item, .product-item').forEach((el) => {
    if (!el.closest('.floating-buttons')) {
      el.style.opacity = '0';
      el.style.transform = 'translateY(30px)';
      el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
      observer.observe(el);
    }
  });
</script>
