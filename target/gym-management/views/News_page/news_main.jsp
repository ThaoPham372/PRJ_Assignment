<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ include
file="/views/common/header.jsp" %>

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
    --gradient-hero: linear-gradient(
      180deg,
      var(--accent) 0%,
      #c47455 35%,
      #3b335c 70%,
      var(--primary) 100%
    );
  }

  /* HERO */
  .hero {
    background: var(--gradient-hero);
    color: #fff;
    padding: 60px 50px;
    position: relative;
    overflow: hidden;
  }

  .hero::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(
      45deg,
      transparent 30%,
      rgba(255, 255, 255, 0.05) 50%,
      transparent 70%
    );
    animation: shimmer 3s infinite;
  }

  @keyframes shimmer {
    0% {
      transform: translateX(-100%);
    }
    100% {
      transform: translateX(100%);
    }
  }

  .hero .wrap {
    max-width: 1200px;
    margin: 0 auto;
    position: relative;
    z-index: 2;
  }

  .hero h1 {
    margin: 0 0 20px;
    font-size: 3rem;
    font-weight: 900;
    letter-spacing: -0.02em;
    text-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
    animation: fadeInUp 1s ease-out;
  }

  .hero p {
    margin: 0;
    max-width: 700px;
    font-size: 1.2rem;
    opacity: 0.95;
    animation: fadeInUp 1s ease-out 0.2s both;
  }

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(30px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  /* SECTION */
  .section {
    max-width: 1200px;
    margin: 60px auto;
    padding: 0 50px;
  }

  .highlight {
    background: var(--card);
    border-radius: 20px;
    padding: 40px;
    box-shadow: 0 8px 30px var(--shadow);
    transition: all 0.4s ease;
    position: relative;
    overflow: hidden;
  }

  .highlight::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: var(--gradient-accent);
    transform: scaleX(0);
    transition: transform 0.3s ease;
  }

  .highlight:hover::before {
    transform: scaleX(1);
  }

  .highlight:hover {
    transform: translateY(-5px);
    box-shadow: 0 20px 40px var(--shadow-hover);
  }

  .hl-grid {
    display: grid;
    grid-template-columns: 1fr 1.2fr;
    gap: 40px;
    align-items: center;
  }

  .hl-img {
    width: 100%;
    height: 300px;
    border-radius: 15px;
    object-fit: cover;
    background: linear-gradient(45deg, #f0f0f0 25%, transparent 25%),
      linear-gradient(-45deg, #f0f0f0 25%, transparent 25%),
      linear-gradient(45deg, transparent 75%, #f0f0f0 75%),
      linear-gradient(-45deg, transparent 75%, #f0f0f0 75%);
    background-size: 20px 20px;
    background-position: 0 0, 0 10px, 10px -10px, -10px 0px;
    transition: transform 0.3s ease;
    box-shadow: 0 8px 25px var(--shadow);
  }

  .highlight:hover .hl-img {
    transform: scale(1.02);
  }

  .hl-title {
    margin: 0 0 15px;
    color: var(--primary);
    font-size: 2rem;
    font-weight: 800;
    line-height: 1.2;
  }

  .hl-text {
    margin: 0 0 25px;
    font-size: 1.1rem;
    color: var(--text-light);
    line-height: 1.6;
  }

  /* DIVIDER */
  hr {
    border: none;
    border-top: 2px solid var(--shadow);
    margin: 50px 0;
    border-radius: 1px;
  }

  /* CARDS */
  .cards {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 30px;
    margin-top: 30px;
  }

  .card {
    background: var(--card);
    border-radius: 20px;
    overflow: hidden;
    box-shadow: 0 8px 30px var(--shadow);
    transition: all 0.4s ease;
    position: relative;
  }

  .card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: var(--gradient-accent);
    transform: scaleX(0);
    transition: transform 0.3s ease;
  }

  .card:hover::before {
    transform: scaleX(1);
  }

  .card:hover {
    transform: translateY(-10px);
    box-shadow: 0 20px 40px var(--shadow-hover);
  }

  .card img {
    width: 100%;
    height: 200px;
    object-fit: cover;
    background: linear-gradient(45deg, #f0f0f0 25%, transparent 25%),
      linear-gradient(-45deg, #f0f0f0 25%, transparent 25%),
      linear-gradient(45deg, transparent 75%, #f0f0f0 75%),
      linear-gradient(-45deg, transparent 75%, #f0f0f0 75%);
    background-size: 20px 20px;
    background-position: 0 0, 0 10px, 10px -10px, -10px 0px;
    transition: transform 0.3s ease;
  }

  .card:hover img {
    transform: scale(1.05);
  }

  .card .c-body {
    padding: 25px;
    display: flex;
    flex-direction: column;
  }

  .card h3 {
    margin: 0 0 12px;
    font-size: 1.3rem;
    color: var(--primary);
    font-weight: 700;
    line-height: 1.3;
  }

  .card p {
    margin: 0 0 20px;
    font-size: 0.95rem;
    color: var(--text-light);
    line-height: 1.6;
    flex: 1;
  }

  /* RESPONSIVE */
  @media (max-width: 1024px) {
    .section {
      padding: 0 30px;
    }

    .cards {
      gap: 25px;
    }

    .card .c-body {
      padding: 20px;
    }
  }

  @media (max-width: 900px) {
    .hero {
      padding: 40px 20px;
    }

    .hero h1 {
      font-size: 2.5rem;
    }

    .hero p {
      font-size: 1.1rem;
    }

    .section {
      padding: 0 20px;
    }

    .highlight {
      padding: 30px;
    }

    .hl-grid {
      grid-template-columns: 1fr;
      gap: 30px;
    }

    .hl-img {
      height: 250px;
    }

    .cards {
      grid-template-columns: 1fr;
      gap: 20px;
    }
  }

  @media (max-width: 480px) {
    .hero h1 {
      font-size: 2rem;
    }

    .hero p {
      font-size: 1rem;
    }

    .highlight {
      padding: 25px;
    }

    .hl-title {
      font-size: 1.5rem;
    }

    .card .c-body {
      padding: 20px;
    }

    .card h3 {
      font-size: 1.2rem;
    }
  }
</style>

<!-- HERO -->
<section class="hero">
  <div class="wrap">
    <h1>Bài viết nổi bật</h1>
    <p>
      Nội dung đoạn văn bản của bạn — mô tả ngắn về chuyên mục tin tức, sự kiện
      và khuyến mãi của GymFit.
    </p>
  </div>
</section>

<!-- FEATURED + 3 CARDS -->
<div class="section">
  <!-- Featured article -->
  <article class="highlight">
    <div class="hl-grid">
      <img
        class="hl-img"
        src="https://images.unsplash.com/photo-1534438327276-14e5300c3a48?ixlib=rb-4.0.3&auto=format&fit=crop&w=900&q=80"
        alt="Bài viết nổi bật"
        loading="lazy"
        decoding="async"
        referrerpolicy="no-referrer"
      />
      <div>
        <h2 class="hl-title">Khai trương cơ sở mới tại Đà Nẵng</h2>
        <p class="hl-text">
          Không gian tập 1.200m², máy móc thế hệ mới, phòng studio yoga & dance.
          Tuần đầu khai trương tặng 01 buổi PT trải nghiệm và voucher ưu đãi đến
          40%.
        </p>
        <a class="btn" href="${pageContext.request.contextPath}/news4.jsp"
          >XEM CHI TIẾT</a
        >
      </div>
    </div>
  </article>

  <!-- Divider -->
  <hr />

  <!-- Three cards -->
  <section class="cards">
    <article class="card">
      <img
        src="https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
        alt="Yoga buổi sáng"
        loading="lazy"
        decoding="async"
        referrerpolicy="no-referrer"
      />
      <div class="c-body">
        <h3>Yoga buổi sáng 6:00</h3>
        <p>Lịch mới 2-4-6. Tập nhẹ nhàng – căn chỉnh – giảm căng cơ vai gáy.</p>
        <a
          class="btn"
          href="${pageContext.request.contextPath}/news1.jsp"
          style="align-self: center"
          >XEM THÊM</a
        >
      </div>
    </article>

    <article class="card">
      <img
        src="https://images.unsplash.com/photo-1551698618-1dfe5d97d256?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
        alt="Chương trình giảm cân"
        loading="lazy"
        decoding="async"
        referrerpolicy="no-referrer"
      />
      <div class="c-body">
        <h3>8 tuần giảm cân khoa học</h3>
        <p>Kết hợp tập luyện – dinh dưỡng – theo dõi InBody mỗi 2 tuần.</p>
        <a
          class="btn"
          href="${pageContext.request.contextPath}/news2.jsp"
          style="align-self: center"
          >XEM THÊM</a
        >
      </div>
    </article>

    <article class="card">
      <img
        src="https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
        alt="Khuyến mãi thành viên"
        loading="lazy"
        decoding="async"
        referrerpolicy="no-referrer"
      />
      <div class="c-body">
        <h3>Ưu đãi hội viên tháng này</h3>
        <p>Voucher tới 30% cho gói 6–12 tháng. Số lượng có hạn.</p>
        <a
          class="btn"
          href="${pageContext.request.contextPath}/news3.jsp"
          style="align-self: center"
          >XEM THÊM</a
        >
      </div>
    </article>
  </section>
</div>

<!-- Enhanced image fallback -->
<script>
  // Enhanced image fallback handling
  function attachImageFallback() {
    document.querySelectorAll('img').forEach(function (img) {
      img.addEventListener(
        'error',
        function () {
          if (!img.dataset.fallback) {
            img.dataset.fallback = '1';
            // Use a reliable placeholder service
            img.src =
              'https://via.placeholder.com/800x600/141a46/ffffff?text=Gym+Image';
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

  // Observe cards for animation
  document.querySelectorAll('.card, .highlight').forEach((el) => {
    el.style.opacity = '0';
    el.style.transform = 'translateY(30px)';
    el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
    observer.observe(el);
  });
</script>

<%@ include file="/views/common/footer.jsp" %>
