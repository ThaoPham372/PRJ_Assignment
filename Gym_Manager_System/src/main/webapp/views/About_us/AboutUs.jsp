<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ include
file="/views/common/header.jsp" %>

<style>
  :root {
    --primary: #141a49;
    --accent: #ec8b5a;
    --support: #ffde59;
    --white: #ffffff;
    --text-dark: #333333;
    --text-light: #666666;
    --gray-bg: #f5f5f5;
    --shadow: rgba(0, 0, 0, 0.1);
    --shadow-hover: rgba(0, 0, 0, 0.15);
    --gradient-primary: linear-gradient(135deg, #141a49 0%, #1e2a5c 100%);
    --gradient-accent: linear-gradient(135deg, #ec8b5a 0%, #d67a4f 100%);
  }

  body {
    font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,
      sans-serif;
    background: var(--white);
    color: var(--text-dark);
    line-height: 1.6;
  }

  /* Hero Section */
  .hero-section {
    background: var(--primary);
    min-height: 100vh;
    text-align: center;
    position: relative;
    color: #fff;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 0;
    overflow: hidden;
  }

  .hero-section::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: url('${pageContext.request.contextPath}/images/home/backrough.jpg')
      no-repeat center center;
    background-size: cover;
    filter: brightness(0.3);
    z-index: 0;
    animation: zoomEffect 20s infinite alternate;
  }

  @keyframes zoomEffect {
    0% {
      transform: scale(1);
    }
    100% {
      transform: scale(1.1);
    }
  }

  .hero-content {
    position: relative;
    z-index: 1;
    padding: 0 50px;
    animation: fadeInUp 1s ease-out;
  }

  .hero-logo {
    font-size: 4rem;
    font-weight: 900;
    margin-bottom: 20px;
    text-transform: uppercase;
    letter-spacing: 2px;
    background: linear-gradient(135deg, var(--accent), var(--support));
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    animation: gradientText 3s ease infinite;
  }

  @keyframes gradientText {
    0% {
      filter: hue-rotate(0deg);
    }
    100% {
      filter: hue-rotate(360deg);
    }
  }

  .hero-name {
    font-size: 3rem;
    font-weight: 800;
    margin-bottom: 15px;
    text-transform: uppercase;
    letter-spacing: 2px;
    text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
    animation: fadeInDown 1s ease-out 0.5s backwards;
  }

  .hero-slogan {
    font-size: 1.5rem;
    font-weight: 600;
    margin-bottom: 30px;
    max-width: 600px;
    margin-left: auto;
    margin-right: auto;
    opacity: 0;
    animation: fadeInUp 1s ease-out 1s forwards;
  }

  .scroll-down {
    position: absolute;
    bottom: 50px;
    left: 50%;
    transform: translateX(-50%);
    color: white;
    text-decoration: none;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    opacity: 0;
    animation: fadeIn 1s ease-out 1.5s forwards;
  }

  .scroll-down span {
    font-size: 1rem;
    text-transform: uppercase;
    letter-spacing: 2px;
  }

  .scroll-down i {
    font-size: 1.5rem;
    animation: bounce 2s infinite;
  }

  @keyframes bounce {
    0%,
    20%,
    50%,
    80%,
    100% {
      transform: translateY(0);
    }
    40% {
      transform: translateY(-10px);
    }
    60% {
      transform: translateY(-5px);
    }
  }

  .hero-divider {
    width: 200px;
    height: 2px;
    background: var(--accent);
    margin: 30px auto;
  }

  .hero-intro {
    font-size: 1.1rem;
    max-width: 800px;
    margin: 0 auto 40px;
    font-style: italic;
    opacity: 0.9;
  }

  .hero-image {
    width: 100%;
    max-width: 800px;
    height: 400px;
    object-fit: cover;
    border-radius: 20px;
    margin: 0 auto;
    display: block;
    box-shadow: 0 15px 35px var(--shadow);
  }

  /* Content Sections */
  .content-section {
    padding: 100px 50px;
    max-width: 1200px;
    margin: 0 auto;
    opacity: 0;
    transform: translateY(30px);
    transition: all 1s ease;
  }

  .content-section.visible {
    opacity: 1;
    transform: translateY(0);
  }

  .content-section:nth-child(even) {
    background: rgba(20, 26, 73, 0.02);
  }

  .section-title {
    font-size: 2.5rem;
    font-weight: 800;
    color: var(--primary);
    margin-bottom: 40px;
    text-transform: uppercase;
    letter-spacing: 2px;
    text-align: center;
    position: relative;
    padding-bottom: 20px;
  }

  .section-title::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 100px;
    height: 4px;
    background: linear-gradient(90deg, var(--accent), var(--support));
    border-radius: 2px;
  }

  .section-title::before {
    content: '';
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 200px;
    height: 1px;
    background: linear-gradient(90deg, transparent, var(--accent), transparent);
  }

  .section-title span {
    display: block;
    font-size: 1rem;
    color: var(--accent);
    margin-top: 10px;
    text-transform: none;
    font-weight: 500;
    letter-spacing: 1px;
    opacity: 0.8;
  }

  .section-content {
    font-size: 1.1rem;
    color: var(--text-dark);
    line-height: 1.8;
    margin-bottom: 30px;
  }

  .section-content p {
    margin-bottom: 20px;
  }

  .section-content ul {
    list-style: none;
    padding-left: 0;
  }

  .section-content li {
    padding-left: 30px;
    position: relative;
    margin-bottom: 15px;
  }

  .section-content li::before {
    content: '✓';
    position: absolute;
    left: 0;
    color: var(--accent);
    font-weight: bold;
    font-size: 1.2rem;
  }

  .highlight-text {
    color: var(--accent);
    font-weight: 700;
  }

  /* Image Grid */
  .image-grid {
    display: grid;
    gap: 20px;
    margin: 40px 0;
  }

  .image-grid-3 {
    grid-template-columns: repeat(3, 1fr);
  }

  .image-grid-4 {
    grid-template-columns: repeat(2, 1fr);
  }

  .image-grid-2 {
    grid-template-columns: repeat(2, 1fr);
  }

  .image-grid img {
    width: 100%;
    height: 300px;
    object-fit: cover;
    border-radius: 15px;
    box-shadow: 0 8px 25px var(--shadow);
    transition: all 0.5s ease;
    position: relative;
    overflow: hidden;
  }

  .hover-zoom {
    position: relative;
    overflow: hidden;
  }

  .hover-zoom::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(
      45deg,
      rgba(236, 139, 90, 0.2),
      rgba(20, 26, 73, 0.2)
    );
    opacity: 0;
    transition: all 0.5s ease;
    z-index: 1;
  }

  .hover-zoom:hover {
    transform: scale(1.05);
    box-shadow: 0 15px 35px var(--shadow-hover);
  }

  .hover-zoom:hover::before {
    opacity: 1;
  }

  .image-placeholder {
    background: var(--primary);
    border-radius: 15px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--white);
    font-weight: 600;
    box-shadow: 0 8px 25px var(--shadow);
    transition: transform 0.3s ease;
  }

  .image-placeholder:hover {
    transform: translateY(-5px);
  }

  .image-placeholder.square {
    aspect-ratio: 1;
    min-height: 200px;
  }

  .image-placeholder.rectangle {
    aspect-ratio: 4/3;
    min-height: 250px;
  }

  .image-placeholder.portrait {
    aspect-ratio: 3/4;
    min-height: 300px;
  }

  /* CTA Section */
  .cta-section {
    background: var(--gradient-primary);
    padding: 80px 50px;
    text-align: center;
    color: #fff;
  }

  .cta-title {
    font-size: 2rem;
    font-weight: 800;
    margin-bottom: 40px;
    text-transform: uppercase;
    letter-spacing: 1px;
  }

  .cta-buttons {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 30px;
    flex-wrap: wrap;
  }

  .cta-main {
    font-size: 1.2rem;
    font-weight: 700;
    margin-right: 20px;
  }

  .support-buttons {
    display: flex;
    gap: 15px;
  }

  .support-btn {
    background: var(--support);
    color: var(--text-dark);
    padding: 15px 25px;
    border-radius: 25px;
    font-weight: 700;
    font-size: 1rem;
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
    transform: scale(1.05);
    box-shadow: 0 12px 35px rgba(255, 222, 89, 0.5);
  }

  .register-btn {
    background: var(--gradient-accent);
    color: var(--white);
    padding: 18px 35px;
    border-radius: 25px;
    font-weight: 700;
    font-size: 1.1rem;
    cursor: pointer;
    box-shadow: 0 8px 25px rgba(236, 139, 90, 0.4);
    transition: all 0.3s ease;
    border: none;
    text-decoration: none;
    display: inline-block;
    margin-top: 30px;
    text-transform: uppercase;
    letter-spacing: 1px;
  }

  .register-btn:hover {
    transform: translateY(-3px);
    box-shadow: 0 12px 35px rgba(236, 139, 90, 0.5);
  }

  /* Google Map */
  .map-section {
    padding: 0;
    margin: 0;
  }

  .map-container {
    width: 100%;
    height: 400px;
    border: none;
  }

  /* Responsive */
  @media (max-width: 768px) {
    .hero-section {
      padding: 80px 20px 60px;
    }

    .hero-logo {
      font-size: 2.5rem;
    }

    .hero-name {
      font-size: 2rem;
    }

    .hero-slogan {
      font-size: 1.1rem;
    }

    .content-section {
      padding: 60px 20px;
    }

    .section-title {
      font-size: 1.5rem;
    }

    .image-grid-3,
    .image-grid-4,
    .image-grid-2 {
      grid-template-columns: 1fr;
    }

    .cta-section {
      padding: 60px 20px;
    }

    .cta-buttons {
      flex-direction: column;
      gap: 20px;
    }

    .cta-main {
      margin-right: 0;
      margin-bottom: 10px;
    }

    .support-buttons {
      flex-direction: column;
      justify-content: center;
    }
  }

  @media (max-width: 480px) {
    .hero-logo {
      font-size: 2rem;
    }

    .hero-name {
      font-size: 1.5rem;
    }

    .hero-slogan {
      font-size: 1rem;
    }
  }
</style>

<!-- Hero Section -->
<section class="hero-section">
  <div class="hero-content">
    <div class="hero-logo">🏋️ GYMFIT</div>
    <div class="hero-name"></div>
    <div class="hero-slogan">Không Gian Tập Luyện Đẳng Cấp</div>

    <div class="hero-divider"></div>

    <div class="hero-intro">
      "Không chỉ là nơi tập luyện, chúng tôi là nơi truyền cảm hứng sống khỏe.
      GymFit mang đến cho bạn trải nghiệm tập luyện hoàn hảo với trang thiết bị
      hiện đại và đội ngũ huấn luyện viên chuyên nghiệp."
    </div>

    <a href="#mission" class="scroll-down">
      <span>Khám phá thêm</span>
      <i class="fas fa-chevron-down"></i>
    </a>
  </div>
</section>

<!-- Mission & Vision Section -->
<section class="content-section">
  <h2 class="section-title">
    Sứ mệnh và Tầm nhìn
    <span>Our Mission & Vision</span>
  </h2>

  <div class="section-content">
    <p><strong>Tại sao phòng gym này được thành lập?</strong></p>
    <p>
      <span class="highlight-text">GymFit</span> được thành lập với sứ mệnh giúp
      mọi người đạt được phiên bản tốt nhất của chính mình. Chúng tôi tin rằng
      sức khỏe là nền tảng của hạnh phúc và thành công trong cuộc sống.
    </p>
    <p><strong>Mục tiêu của chúng tôi:</strong></p>
    <p>
      Không chỉ là nơi tập luyện, chúng tôi là nơi truyền cảm hứng sống khỏe. Sứ
      mệnh của GymFit là giúp bạn đạt được mục tiêu fitness hiệu quả nhất, không
      chỉ về hình thể mà còn cả tinh thần. Chúng tôi cam kết mang đến môi trường
      tập luyện an toàn, chuyên nghiệp và thân thiện nhất.
    </p>
  </div>

  <div class="image-grid image-grid-3">
    <img
      src="${pageContext.request.contextPath}/images/aboutUs/PT1.jpeg"
      alt="Personal Training"
      loading="lazy"
      class="hover-zoom"
    />
    <img
      src="${pageContext.request.contextPath}/images/aboutUs/PT2.webp"
      alt="Group Training"
      loading="lazy"
      class="hover-zoom"
    />
    <img
      src="${pageContext.request.contextPath}/images/aboutUs/cardio.jpg"
      alt="Cardio Area"
      loading="lazy"
      class="hover-zoom"
    />
  </div>
</section>

<!-- Core Values & Facilities Section -->
<section class="content-section">
  <h2 class="section-title">
    Giá trị cốt lõi & Cơ sở vật chất
    <span>Core Values & Facilities</span>
  </h2>

  <div class="section-content">
    <p>
      Tại GymFit, chúng tôi tự hào mang đến không gian tập luyện hiện đại với
      <span class="highlight-text"
        >thiết bị nhập khẩu từ các thương hiệu hàng đầu thế giới</span
      >. Hệ thống phòng tập được thiết kế khoa học bao gồm:
    </p>
    <ul>
      <li>
        <strong>Phòng tập chính:</strong> Máy móc đa năng, khu vực tập tự do với
        thiết bị hiện đại
      </li>
      <li>
        <strong>Studio Yoga:</strong> Không gian yên tĩnh với ánh sáng tự nhiên,
        phù hợp cho các lớp Yoga và Dance
      </li>
      <li>
        <strong>Phòng xông hơi:</strong> Thư giãn và phục hồi sau tập luyện
        cường độ cao
      </li>
      <li>
        <strong>Phòng tập nhóm:</strong> Lớp học đa dạng từ HIIT, CrossFit đến
        Dance Fitness
      </li>
      <li>
        <strong>Khu vực Cardio:</strong> Máy chạy bộ, xe đạp spinning, máy leo
        núi hiện đại
      </li>
    </ul>
  </div>

  <div class="image-grid image-grid-4">
    <img
      src="${pageContext.request.contextPath}/images/aboutUs/room.jpg"
      alt="Phòng tập chính"
      loading="lazy"
      class="hover-zoom"
    />
    <img
      src="${pageContext.request.contextPath}/images/aboutUs/cardio.jpg"
      alt="Khu vực Cardio"
      loading="lazy"
      class="hover-zoom"
    />
    <img
      src="${pageContext.request.contextPath}/images/aboutUs/PT1.jpeg"
      alt="Khu vực PT"
      loading="lazy"
      class="hover-zoom"
    />
    <img
      src="${pageContext.request.contextPath}/images/aboutUs/xonghoi.webp"
      alt="Phòng xông hơi"
      loading="lazy"
      class="hover-zoom"
    />
  </div>
</section>

<!-- Trainer Team Section -->
<section class="content-section">
  <h2 class="section-title">
    Đội ngũ Huấn luyện viên
    <span>Our Professional Trainers</span>
  </h2>

  <div class="section-content">
    <p>
      Đội ngũ huấn luyện viên tại GymFit là những chuyên gia
      <span class="highlight-text">có chứng chỉ quốc tế</span> và
      <span class="highlight-text">kinh nghiệm trên 5 năm</span> trong lĩnh vực
      fitness. Mỗi PT đều được đào tạo chuyên sâu về:
    </p>
    <ul>
      <li>
        <strong>Kỹ thuật tập luyện:</strong> Đảm bảo an toàn và hiệu quả cho
        từng học viên
      </li>
      <li>
        <strong>Dinh dưỡng thể thao:</strong> Tư vấn chế độ ăn phù hợp với mục
        tiêu cá nhân
      </li>
      <li>
        <strong>Tâm lý học thể thao:</strong> Động viên và truyền cảm hứng cho
        học viên
      </li>
      <li>
        <strong>Phục hồi chức năng:</strong> Hỗ trợ phục hồi sau chấn thương một
        cách an toàn
      </li>
      <li>
        <strong>Chuyên môn cao:</strong> Cập nhật kiến thức và xu hướng fitness
        mới nhất
      </li>
    </ul>
  </div>

  <div class="image-grid image-grid-2">
    <img
      src="${pageContext.request.contextPath}/images/aboutUs/PT1.jpeg"
      alt="Huấn luyện viên 1"
      loading="lazy"
      class="hover-zoom"
    />
    <img
      src="${pageContext.request.contextPath}/images/aboutUs/PT2.webp"
      alt="Huấn luyện viên 2"
      loading="lazy"
      class="hover-zoom"
    />
  </div>
</section>

<!-- CTA Section -->
<section class="cta-section">
  <div class="cta-title">Đăng ký tập thử miễn phí ngay!</div>

  <div class="cta-buttons">
    <div class="cta-main">TƯ VẤN & CHAT BOT</div>

    <div class="support-buttons">
      <button
        class="support-btn"
        onclick="alert('Chức năng tư vấn sẽ được triển khai sớm!')"
      >
        <i class="fas fa-user-tie"></i>
        TƯ VẤN
      </button>
      <button
        class="support-btn"
        onclick="alert('Chat Bot đang được phát triển!')"
      >
        <i class="fas fa-comments"></i>
        CHAT BOT
      </button>
    </div>
  </div>

  <a
    href="${pageContext.request.contextPath}/register"
    class="register-btn"
  >
    ĐĂNG KÝ NGAY
  </a>
</section>

<!-- Google Map Section -->
<section class="map-section">
  <iframe
    src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3833.924!2d108.220!3d16.047!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x314219c8a0777a4b%3A0x4b4b4b4b4b4b4b4b!2sGymFit%20Da%20Nang!5e0!3m2!1svi!2s!4v1234567890"
    class="map-container"
    allowfullscreen=""
    loading="lazy"
    referrerpolicy="no-referrer-when-downgrade"
  >
  </iframe>
</section>

<!-- Floating Buttons -->
<div class="floating-buttons">
  <button
    class="floating-btn tu-van"
    onclick="alert('Chức năng tư vấn sẽ được triển khai sớm!')"
  >
    <i class="fas fa-user-tie"></i> TƯ VẤN
  </button>
  <button
    class="floating-btn chat-bot"
    onclick="alert('Chức năng chat bot đang được phát triển!')"
  >
    <i class="fas fa-comments"></i> CHAT BOT
  </button>
</div>

<script>
  // Add floating buttons CSS
  const link = document.createElement('link');
  link.rel = 'stylesheet';
  link.href = '${pageContext.request.contextPath}/css/floating-buttons.css';
  document.head.appendChild(link);
  // Enhanced image fallback handling
  function attachImageFallback() {
    document.querySelectorAll('img').forEach(function (img) {
      img.addEventListener(
        'error',
        function () {
          if (!img.dataset.fallback) {
            img.dataset.fallback = '1';
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

  // Intersection Observer for animations
  const observerOptions = {
    threshold: 0.2,
    rootMargin: '0px 0px -100px 0px',
  };

  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');

        // Animate section title after section becomes visible
        const title = entry.target.querySelector('.section-title');
        if (title) {
          title.style.opacity = '1';
          title.style.transform = 'translateY(0)';
        }

        // Animate images with delay
        const images = entry.target.querySelectorAll('.hover-zoom');
        images.forEach((img, index) => {
          setTimeout(() => {
            img.style.opacity = '1';
            img.style.transform = 'translateY(0)';
          }, index * 200);
        });
      }
    });
  }, observerOptions);

  // Initialize animations
  document
    .querySelectorAll('.content-section, .cta-section')
    .forEach((section) => {
      // Initialize section
      observer.observe(section);

      // Initialize section title
      const title = section.querySelector('.section-title');
      if (title) {
        title.style.opacity = '0';
        title.style.transform = 'translateY(20px)';
        title.style.transition = 'all 0.8s ease';
      }

      // Initialize images
      const images = section.querySelectorAll('.hover-zoom');
      images.forEach((img) => {
        img.style.opacity = '0';
        img.style.transform = 'translateY(30px)';
        img.style.transition = 'all 0.8s ease';
      });
    });
</script>

<%@ include file="/views/common/footer.jsp" %>
