<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        align-items: center;
        justify-content: center;
      }

      .product-item {
        background: var(--primary);
        color: #fff;
        padding: 30px 20px;
        border-radius: 20px;
        text-align: center;
        min-width: 250px;
        max-width: 280px;
        min-height: 200px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        box-shadow: 0 8px 25px var(--shadow);
        transition: all 0.3s ease;
        position: relative;
        overflow: hidden;
        flex: 1;
        text-decoration: none;
        cursor: pointer;
      }

      .product-item:hover {
        transform: translateY(-5px);
        box-shadow: 0 15px 35px var(--shadow-hover);
      }

      .product-item img {
        width: 100%;
        height: 120px;
        object-fit: cover;
        border-radius: 10px;
        margin-bottom: 15px;
        transition: transform 0.3s ease;
      }

      .product-item:hover img {
        transform: scale(1.05);
      }

      .product-item h3 {
        font-size: 1.3rem;
        font-weight: 700;
        margin-bottom: 10px;
      }

      .product-item p {
        font-size: 0.9rem;
        opacity: 0.9;
        line-height: 1.4;
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

      /* FOOTER */
      footer {
        background: var(--primary);
        color: #fff;
        text-align: center;
        padding: 30px;
        font-size: 0.9rem;
        opacity: 0.9;
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
        header {
          flex-direction: column;
          gap: 15px;
          padding: 20px;
        }

        nav ul {
          gap: 20px;
          flex-wrap: wrap;
          justify-content: center;
        }

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
    </style>
  </head>
  <body>

    <!-- SERVICES SECTION -->
    <section class="services-section">
      <div class="services-container">
        <a href="${pageContext.request.contextPath}/views/Service_page/personalTraining.jsp" class="service-item">
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
            được thiết kế riêng cho từng cá nhân để đạt được mục tiêu fitness
            hiệu quả nhất.
          </p>
        </a>
        <a href="${pageContext.request.contextPath}/views/Service_page/groupTraining.jsp" class="service-item">
          <img
            src="https://images.unsplash.com/photo-1540497077202-7c8a3999166f?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80"
            alt="Group Training"
            loading="lazy"
            decoding="async"
            referrerpolicy="no-referrer"
          />
          <h2>GROUP TRAINING</h2>
          <p>
            Lớp tập nhóm tạo động lực và gắn kết, giúp bạn có thêm bạn bè và
            động lực tập luyện mỗi ngày trong môi trường thân thiện và chuyên
            nghiệp.
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
            <a href="${pageContext.request.contextPath}/views/Service_page/product.jsp" class="product-item" id="product1">
              <img
                src="https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"
                alt="Sản phẩm 1"
                loading="lazy"
                decoding="async"
                referrerpolicy="no-referrer"
              />
              <h3>SẢN PHẨM</h3>
              <p>Whey Protein cao cấp</p>
            </a>
            <a href="${pageContext.request.contextPath}/views/Service_page/product.jsp" class="product-item" id="product2">
              <img
                src="https://images.unsplash.com/photo-1594736797933-d0c29c8a0a8e?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"
                alt="Sản phẩm 2"
                loading="lazy"
                decoding="async"
                referrerpolicy="no-referrer"
              />
              <h3>SẢN PHẨM</h3>
              <p>Creatine Monohydrate</p>
            </a>
            <a href="${pageContext.request.contextPath}/views/Service_page/product.jsp" class="product-item" id="product3">
              <img
                src="https://images.unsplash.com/photo-1594736797933-d0c29c8a0a8e?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"
                alt="Sản phẩm"
                loading="lazy"
                decoding="async"
                referrerpolicy="no-referrer"
              />
              <h3>SẢN PHẨM</h3>
              <p>BCAA Essential</p>
            </a>
            <a href="${pageContext.request.contextPath}/views/Service_page/product.jsp" class="product-item" id="product4">
              <img
                src="https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"
                alt="Sản phẩm"
                loading="lazy"
                decoding="async"
                referrerpolicy="no-referrer"
              />
              <h3>SẢN PHẨM</h3>
              <p>Pre-Workout Energy</p>
            </a>
          </div>

          <button class="carousel-nav" id="nextBtn" onclick="changeProduct(1)">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>
    </section>

    <!-- SUPPORT BUTTONS -->
    <div class="support-buttons">
      <button class="support-btn">
        <i class="fas fa-user-tie"></i>
        TƯ VẤN
      </button>
      <button class="support-btn">
        <i class="fas fa-comments"></i>
        CHAT BOT
      </button>
    </div>

<%@ include file="/views/common/footer.jsp" %>

    <!-- SCRIPT -->
    <script>
      // Product carousel data
      const products = [
        {
          name: 'SẢN PHẨM',
          description: 'Whey Protein cao cấp',
          image:
            'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80',
        },
        {
          name: 'SẢN PHẨM',
          description: 'Creatine Monohydrate',
          image:
            'https://images.unsplash.com/photo-1594736797933-d0c29c8a0a8e?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80',
        },
        {
          name: 'SẢN PHẨM',
          description: 'BCAA Essential',
          image:
            'https://images.unsplash.com/photo-1594736797933-d0c29c8a0a8e?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80',
        },
        {
          name: 'SẢN PHẨM',
          description: 'Pre-Workout Energy',
          image:
            'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80',
        },
        {
          name: 'SẢN PHẨM',
          description: 'Mass Gainer',
          image:
            'https://images.unsplash.com/photo-1594736797933-d0c29c8a0a8e?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80',
        },
        {
          name: 'SẢN PHẨM',
          description: 'Multivitamin',
          image:
            'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80',
        },
      ];

      let currentIndex = 0;

      function changeProduct(direction) {
        currentIndex += direction;

        // Loop through products
        if (currentIndex >= products.length) {
          currentIndex = 0;
        } else if (currentIndex < 0) {
          currentIndex = products.length - 1;
        }

        // Update product 1
        const product1 = document.getElementById('product1');
        const product1Data = products[currentIndex];
        product1.querySelector('img').src = product1Data.image;
        product1.querySelector('img').alt = product1Data.name;
        product1.querySelector('h3').textContent = product1Data.name;
        product1.querySelector('p').textContent = product1Data.description;

        // Update product 2
        const product2 = document.getElementById('product2');
        const product2Index = (currentIndex + 1) % products.length;
        const product2Data = products[product2Index];
        product2.querySelector('img').src = product2Data.image;
        product2.querySelector('img').alt = product2Data.name;
        product2.querySelector('h3').textContent = product2Data.name;
        product2.querySelector('p').textContent = product2Data.description;

        // Update product 3
        const product3 = document.getElementById('product3');
        const product3Index = (currentIndex + 2) % products.length;
        const product3Data = products[product3Index];
        product3.querySelector('img').src = product3Data.image;
        product3.querySelector('img').alt = product3Data.name;
        product3.querySelector('h3').textContent = product3Data.name;
        product3.querySelector('p').textContent = product3Data.description;

        // Update product 4
        const product4 = document.getElementById('product4');
        const product4Index = (currentIndex + 3) % products.length;
        const product4Data = products[product4Index];
        product4.querySelector('img').src = product4Data.image;
        product4.querySelector('img').alt = product4Data.name;
        product4.querySelector('h3').textContent = product4Data.name;
        product4.querySelector('p').textContent = product4Data.description;

        // Add transition effect
        product1.style.opacity = '0';
        product2.style.opacity = '0';
        product3.style.opacity = '0';
        product4.style.opacity = '0';

        setTimeout(() => {
          product1.style.opacity = '1';
          product2.style.opacity = '1';
          product3.style.opacity = '1';
          product4.style.opacity = '1';
        }, 150);
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

      // Add smooth scrolling for navigation links
      document.querySelectorAll('nav a[href^="#"]').forEach((anchor) => {
        anchor.addEventListener('click', function (e) {
          e.preventDefault();
          const target = document.querySelector(this.getAttribute('href'));
          if (target) {
            target.scrollIntoView({
              behavior: 'smooth',
              block: 'start',
            });
          }
        });
      });

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

      // Observe service items and product items for animation
      document
        .querySelectorAll('.service-item, .product-item')
        .forEach((el) => {
          el.style.opacity = '0';
          el.style.transform = 'translateY(30px)';
          el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
          observer.observe(el);
        });
    </script>
  </body>
</html>
