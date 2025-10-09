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
  }

  body {
    font-family: 'Arial', sans-serif;
    background: var(--white);
    color: var(--text-dark);
    line-height: 1.6;
  }

  /* Page Title */
  .page-title {
    position: absolute;
    top: 20px;
    left: 20px;
    font-size: 1.2rem;
    font-weight: 800;
    color: var(--text-dark);
    text-transform: uppercase;
    letter-spacing: 1px;
    z-index: 10;
  }

  /* Hero Section */
  .hero-section {
    background: var(--gray-bg);
    background-image: radial-gradient(circle, #ddd 1px, transparent 1px);
    background-size: 20px 20px;
    padding: 100px 50px 80px;
    text-align: center;
    position: relative;
  }

  .hero-logo {
    font-size: 3.5rem;
    font-weight: 900;
    color: var(--accent);
    margin-bottom: 20px;
    text-transform: uppercase;
    letter-spacing: 2px;
  }

  .hero-name {
    font-size: 2.5rem;
    font-weight: 800;
    color: var(--accent);
    margin-bottom: 15px;
    text-transform: uppercase;
    letter-spacing: 1px;
  }

  .hero-slogan {
    font-size: 1.3rem;
    color: var(--text-dark);
    font-weight: 600;
    margin-bottom: 30px;
    max-width: 600px;
    margin-left: auto;
    margin-right: auto;
  }

  .hero-divider {
    width: 200px;
    height: 2px;
    background: var(--accent);
    margin: 30px auto;
  }

  .hero-intro {
    font-size: 1.1rem;
    color: var(--text-dark);
    max-width: 800px;
    margin: 0 auto 40px;
    font-style: italic;
  }

  .hero-image {
    width: 100%;
    max-width: 800px;
    height: 400px;
    background: var(--primary);
    border-radius: 20px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--white);
    font-size: 1.5rem;
    font-weight: 600;
    box-shadow: 0 15px 35px var(--shadow);
  }

  /* Content Sections */
  .content-section {
    padding: 80px 50px;
    max-width: 1200px;
    margin: 0 auto;
  }

  .section-title {
    font-size: 2rem;
    font-weight: 800;
    color: var(--text-dark);
    margin-bottom: 30px;
    text-transform: uppercase;
    letter-spacing: 1px;
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
    background: var(--gray-bg);
    background-image: radial-gradient(circle, #ddd 1px, transparent 1px);
    background-size: 20px 20px;
    padding: 80px 50px;
    text-align: center;
  }

  .cta-title {
    font-size: 2rem;
    font-weight: 800;
    color: var(--accent);
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
    color: var(--accent);
    margin-right: 20px;
  }

  .support-buttons {
    display: flex;
    flex-direction: column;
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
    background: var(--accent);
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
    .page-title {
      position: static;
      text-align: center;
      margin-bottom: 20px;
    }

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
      flex-direction: row;
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

    .support-buttons {
      flex-direction: column;
    }
  }
</style>

<!-- Page Title -->
<div class="page-title">ABOUT US</div>

<!-- Hero Section -->
<section class="hero-section">
  <div class="hero-logo">LOGO</div>
  <div class="hero-name">TÊN GYM</div>
  <div class="hero-slogan">Không Gian Tập Luyện Đẳng Cấp</div>

  <div class="hero-divider"></div>

  <div class="hero-intro">Lời giới thiệu hấp dẫn</div>

  <div class="hero-image">ảnh</div>
</section>

<!-- Mission & Vision Section -->
<section class="content-section">
  <h2 class="section-title">Sứ mệnh và Tầm nhìn:</h2>

  <div class="section-content">
    <p><strong>Tại sao phòng gym này được thành lập?</strong></p>
    <p><strong>Mục tiêu của bạn là gì?</strong></p>
    <p>
      <span class="highlight-text">Ví dụ:</span> "Không chỉ là nơi tập luyện,
      chúng tôi là nơi truyền cảm hứng sống khỏe. Sứ mệnh của GymFit là giúp bạn
      đạt được phiên bản tốt nhất của chính mình, không chỉ về hình thể mà còn
      cả tinh thần. Chúng tôi tin rằng sức khỏe là nền tảng của hạnh phúc và
      thành công."
    </p>
  </div>

  <div class="image-grid image-grid-3">
    <div class="image-placeholder square">ảnh</div>
    <div class="image-placeholder square">ảnh</div>
    <div class="image-placeholder square">ảnh</div>
  </div>
</section>

<!-- Core Values & Facilities Section -->
<section class="content-section">
  <h2 class="section-title">Giá trị cốt lõi & Cơ sở vật chất:</h2>

  <div class="section-content">
    <p>
      Mô tả máy móc, không gian, lớp học, dịch vụ (chú trọng từ khóa như "máy
      tập nhập khẩu", "phòng xông hơi", "studio yoga").
    </p>
    <p>
      Tại GymFit, chúng tôi tự hào mang đến không gian tập luyện hiện đại với
      <span class="highlight-text"
        >thiết bị nhập khẩu từ các thương hiệu hàng đầu thế giới</span
      >. Hệ thống phòng tập được thiết kế khoa học bao gồm:
    </p>
    <ul>
      <li>
        <strong>Phòng tập chính:</strong> Máy móc đa năng, khu vực tập tự do
      </li>
      <li>
        <strong>Studio Yoga:</strong> Không gian yên tĩnh với ánh sáng tự nhiên
      </li>
      <li>
        <strong>Phòng xông hơi:</strong> Thư giãn và phục hồi sau tập luyện
      </li>
      <li>
        <strong>Phòng tập nhóm:</strong> Lớp học đa dạng từ HIIT đến Dance
      </li>
    </ul>
  </div>

  <div class="image-grid image-grid-4">
    <div class="image-placeholder rectangle">ảnh</div>
    <div class="image-placeholder rectangle">ảnh</div>
    <div class="image-placeholder rectangle">ảnh</div>
    <div class="image-placeholder rectangle">ảnh</div>
  </div>
</section>

<!-- Trainer Team Section -->
<section class="content-section">
  <h2 class="section-title">Đội ngũ Huấn luyện viên (PT):</h2>

  <div class="section-content">
    <p>
      Đây là phần rất quan trọng. Nhấn mạnh kinh nghiệm, chứng chỉ và lĩnh vực
      chuyên môn của PT.
    </p>
    <p>
      Đội ngũ huấn luyện viên tại GymFit là những chuyên gia
      <span class="highlight-text">có chứng chỉ quốc tế</span> và
      <span class="highlight-text">kinh nghiệm trên 5 năm</span> trong lĩnh vực
      fitness. Mỗi PT đều được đào tạo chuyên sâu về:
    </p>
    <ul>
      <li><strong>Kỹ thuật tập luyện:</strong> Đảm bảo an toàn và hiệu quả</li>
      <li><strong>Dinh dưỡng thể thao:</strong> Tư vấn chế độ ăn phù hợp</li>
      <li>
        <strong>Tâm lý học thể thao:</strong> Động viên và truyền cảm hứng
      </li>
      <li>
        <strong>Phục hồi chức năng:</strong> Hỗ trợ phục hồi sau chấn thương
      </li>
    </ul>
  </div>

  <div class="image-grid image-grid-2">
    <div class="image-placeholder portrait">ảnh</div>
    <div class="image-placeholder portrait">ảnh</div>
  </div>
</section>

<!-- CTA Section -->
<section class="cta-section">
  <div class="cta-title">Đăng ký tập thử miễn phí ngay!</div>

  <div class="cta-buttons">
    <div class="cta-main">TƯ VẤN & CHAT BOT</div>

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
  </div>

  <a
    href="${pageContext.request.contextPath}/views/register.jsp"
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

<script>
  document.addEventListener('DOMContentLoaded', function () {
    // Support button interactions
    const supportBtns = document.querySelectorAll('.support-btn');
    supportBtns.forEach((btn) => {
      btn.addEventListener('click', function () {
        const btnText = this.textContent.trim();
        if (btnText.includes('TƯ VẤN')) {
          alert('Chức năng tư vấn sẽ được triển khai sớm!');
        } else if (btnText.includes('CHAT BOT')) {
          alert('Chat Bot đang được phát triển!');
        }
      });
    });

    // Register button interaction
    const registerBtn = document.querySelector('.register-btn');
    registerBtn.addEventListener('click', function (e) {
      // Link will navigate to register page
      console.log('Navigating to register page...');
    });

    // Image placeholder interactions
    const imagePlaceholders = document.querySelectorAll('.image-placeholder');
    imagePlaceholders.forEach((placeholder) => {
      placeholder.addEventListener('click', function () {
        // In a real implementation, this would open a lightbox or gallery
        console.log('Image placeholder clicked - would open image gallery');
      });
    });

    // Smooth scrolling for internal links
    document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
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

    // Intersection Observer for animations
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

    // Observe sections for animation
    document
      .querySelectorAll('.content-section, .cta-section')
      .forEach((section) => {
        section.style.opacity = '0';
        section.style.transform = 'translateY(30px)';
        section.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(section);
      });
  });
</script>

<%@ include file="/views/common/footer.jsp" %>
