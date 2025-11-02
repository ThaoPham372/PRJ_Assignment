<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ include
file="/views/common/header.jsp" %>

<main>
  <style>
    :root {
      --primary: #141a46;
      --primary-light: #1e2a5c;
      --accent: #ec8b5e;
      --accent-hover: #d67a4f;
      --chat: #ffde59;
      --text: #2c3e50;
      --text-light: #5a6c7d;
      --muted: #f8f9fa;
      --card: #ffffff;
      --shadow: rgba(0, 0, 0, 0.1);
      --shadow-hover: rgba(0, 0, 0, 0.15);
      --gradient-primary: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
      --gradient-accent: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
    }

    * {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
    }

    body {
      font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI',
        Roboto, sans-serif;
      background: #f9f9f9;
      color: var(--text);
      line-height: 1.6;
      font-weight: 400;
      overflow-x: hidden;
      min-height: 100vh;
      display: flex;
      flex-direction: column;
    }

    main {
      flex: 1;
    }

    h1,
    h2,
    h3,
    h4,
    h5,
    h6 {
      font-weight: 700;
      line-height: 1.2;
    }

    h2 {
      text-align: center;
      margin: 60px 0 40px;
      color: var(--primary);
      font-size: 2.5rem;
      font-weight: 800;
      position: relative;
    }

    h2::after {
      content: '';
      position: absolute;
      bottom: -10px;
      left: 50%;
      transform: translateX(-50%);
      width: 60px;
      height: 4px;
      background: var(--gradient-accent);
      border-radius: 2px;
    }

    /* HERO */
    .hero {
      background: linear-gradient(
          135deg,
          rgba(20, 26, 70, 0.8) 0%,
          rgba(30, 42, 92, 0.7) 100%
        ),
        url('images/home/backrough.jpg') no-repeat center/cover;
      height: 85vh;
      min-height: 600px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      text-align: center;
      color: #fff;
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

    .hero h1,
    .hero p,
    .hero button {
      position: relative;
      z-index: 2;
    }

    .hero h1 {
      font-size: 3.5rem;
      margin-bottom: 20px;
      font-weight: 900;
      letter-spacing: -0.02em;
      text-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
      animation: fadeInUp 1s ease-out;
    }

    .hero p {
      font-size: 1.3rem;
      margin: 0 0 30px;
      font-weight: 400;
      opacity: 0.95;
      max-width: 600px;
      animation: fadeInUp 1s ease-out 0.2s both;
    }

    .hero button {
      background: var(--gradient-accent);
      padding: 18px 40px;
      border: none;
      border-radius: 50px;
      color: #fff;
      font-size: 1.1rem;
      font-weight: 700;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 8px 25px rgba(236, 139, 94, 0.4);
      animation: fadeInUp 1s ease-out 0.4s both;
    }

    .hero button:hover {
      transform: translateY(-3px) scale(1.05);
      box-shadow: 0 12px 35px rgba(236, 139, 94, 0.5);
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

    /* SERVICES */
    .services {
      display: flex;
      justify-content: center;
      gap: 30px;
      padding: 60px 40px;
      flex-wrap: nowrap;
      max-width: 1200px;
      margin: 0 auto;
    }

    .service-box {
      background: var(--card);
      border-radius: 20px;
      box-shadow: 0 8px 30px var(--shadow);
      width: 300px;
      flex: 1;
      padding: 30px;
      text-align: center;
      transition: all 0.4s ease;
      position: relative;
      overflow: hidden;
    }

    .service-box::before {
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

    .service-box:hover::before {
      transform: scaleX(1);
    }

    .service-box:hover {
      transform: translateY(-10px);
      box-shadow: 0 20px 40px var(--shadow-hover);
    }

    .service-box img {
      width: 100%;
      height: 220px;
      object-fit: cover;
      border-radius: 15px;
      margin-bottom: 20px;
      transition: transform 0.3s ease;
    }

    .service-box:hover img {
      transform: scale(1.05);
    }

    .service-box h3 {
      font-size: 1.4rem;
      margin-bottom: 15px;
      color: var(--primary);
      font-weight: 700;
    }

    .service-box p {
      color: var(--text-light);
      font-size: 1rem;
      line-height: 1.6;
    }

    /* NEWS */
    .news {
      padding: 60px 40px;
      display: flex;
      gap: 30px;
      flex-wrap: nowrap;
      justify-content: center;
      background: var(--muted);
      max-width: 1200px;
      margin: 0 auto;
    }

    .news-box {
      width: 300px;
      flex: 1;
      background: var(--card);
      border-radius: 20px;
      box-shadow: 0 8px 30px var(--shadow);
      overflow: hidden;
      display: flex;
      flex-direction: column;
      transition: all 0.4s ease;
      position: relative;
    }

    .news-box:hover {
      transform: translateY(-8px);
      box-shadow: 0 20px 40px var(--shadow-hover);
    }

    .news-box img {
      width: 100%;
      height: 220px;
      object-fit: cover;
      transition: transform 0.3s ease;
    }

    .news-box:hover img {
      transform: scale(1.05);
    }

    .news-content {
      padding: 20px 25px 10px;
      flex: 1;
      display: flex;
      flex-direction: column;
    }

    .news-content h4 {
      font-size: 1.3rem;
      margin-bottom: 12px;
      color: var(--primary);
      font-weight: 700;
    }

    .news-content p {
      color: var(--text-light);
      font-size: 0.95rem;
      line-height: 1.6;
      margin-bottom: 0;
      flex: 1;
    }

    .see-more-btn {
      margin: 15px auto 20px;
      background: var(--gradient-accent);
      border: none;
      padding: 12px 24px;
      border-radius: 25px;
      color: #fff;
      font-weight: 600;
      font-size: 0.9rem;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
      display: block;
    }

    .see-more-btn:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
    }

    /* TRANSFORMATION + FEEDBACK */
    .tf-feedback {
      display: flex;
      gap: 60px;
      padding: 60px 40px;
      background: var(--card);
      border-radius: 20px;
      box-shadow: 0 8px 30px var(--shadow);
      margin: 60px auto;
      align-items: center;
      flex-wrap: wrap;
      max-width: 1200px;
    }

    .transformation {
      flex: 1;
      display: flex;
      justify-content: center;
      min-width: 300px;
    }

    .transformation img {
      width: 100%;
      max-width: 500px;
      border-radius: 20px;
      box-shadow: 0 10px 30px var(--shadow-hover);
      opacity: 1;
      transition: all 0.8s ease;
      object-fit: cover;
      height: auto;
      min-height: 300px;
    }

    .transformation img.loaded {
      opacity: 1;
    }

    .feedback {
      flex: 1;
      min-width: 300px;
    }

    .feedback-box {
      background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
      padding: 30px;
      border-radius: 20px;
      box-shadow: 0 8px 25px var(--shadow);
      display: none;
      border-left: 4px solid var(--accent);
      transition: all 0.5s ease;
    }

    .feedback-box.active {
      display: block;
      animation: slideInRight 0.6s ease-out;
    }

    .feedback-header {
      display: flex;
      align-items: center;
      gap: 20px;
      margin-bottom: 20px;
    }

    .feedback-header img {
      width: 70px;
      height: 70px;
      border-radius: 50%;
      object-fit: cover;
      border: 3px solid var(--accent);
      box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
    }

    .feedback-header .info {
      font-size: 0.95rem;
      line-height: 1.4;
    }

    .feedback-header .info b {
      color: var(--primary);
      font-weight: 700;
      font-size: 1.1rem;
    }

    .feedback p {
      margin: 0;
      font-size: 1rem;
      line-height: 1.6;
      color: var(--text);
      font-style: italic;
      position: relative;
      padding-left: 20px;
    }

    .feedback p::before {
      content: '"';
      position: absolute;
      left: 0;
      top: -5px;
      font-size: 2rem;
      color: var(--accent);
      font-weight: bold;
    }

    @keyframes slideInRight {
      from {
        opacity: 0;
        transform: translateX(30px);
      }
      to {
        opacity: 1;
        transform: translateX(0);
      }
    }

    /* RESPONSIVE DESIGN */
    @media (max-width: 1024px) {
      .tf-feedback {
        flex-direction: column;
        margin: 40px 20px;
        padding: 40px 30px;
      }

      .services,
      .news {
        padding: 40px 20px;
        flex-wrap: wrap;
      }

      .service-box,
      .news-box {
        width: 300px;
        flex: none;
      }
    }

    @media (max-width: 768px) {
      .hero h1 {
        font-size: 2.5rem;
      }

      .hero p {
        font-size: 1.1rem;
      }

      h2 {
        font-size: 2rem;
        margin: 40px 0 30px;
      }

      .services,
      .news {
        gap: 20px;
        flex-wrap: wrap;
      }

      .service-box,
      .news-box {
        width: 100%;
        max-width: 350px;
        flex: none;
      }

      .chat-icon {
        bottom: 20px;
        right: 20px;
        padding: 15px 20px;
        font-size: 0.9rem;
      }
    }

    @media (max-width: 480px) {
      .hero h1 {
        font-size: 2rem;
      }

      .hero p {
        font-size: 1rem;
      }

      .hero button {
        padding: 15px 30px;
        font-size: 1rem;
      }

      .service-box,
      .news-box {
        padding: 25px;
      }

      .tf-feedback {
        padding: 30px 20px;
      }
    }
  </style>

  <!-- HERO -->
  <section class="hero">
    <h1>Chào mừng đến với GymFit</h1>
    <p>Nơi thay đổi bản thân và sức khỏe của bạn</p>
    <button
      onclick="window.location.href='${pageContext.request.contextPath}/register'"
    >
      Đăng ký ngay
    </button>
  </section>

  <!-- SERVICES -->
  <h2>Dịch vụ của chúng tôi</h2>
  <section class="services">
    <div
      class="service-box"
      onclick="window.location.href='${pageContext.request.contextPath}/views/Service_page/personalTraining.jsp'"
      style="cursor: pointer"
    >
      <img
        src="images/home/personalTraining.jpg"
        alt="Personal Training"
        loading="lazy"
        decoding="async"
      />
      <h3>Personal Training</h3>
      <p>
        Huấn luyện cá nhân 1-1 cùng HLV chuyên nghiệp với chương trình tập luyện
        được thiết kế riêng cho từng cá nhân.
      </p>
    </div>
    <div
      class="service-box"
      onclick="window.location.href='${pageContext.request.contextPath}/views/Service_page/groupTraining.jsp'"
      style="cursor: pointer"
    >
      <img
        src="images/home/groupTraining.jpg"
        alt="Group Training"
        loading="lazy"
        decoding="async"
      />
      <h3>Group Training</h3>
      <p>
        Lớp tập nhóm tạo động lực và gắn kết, giúp bạn có thêm bạn bè và động
        lực tập luyện mỗi ngày.
      </p>
    </div>
    <div
      class="service-box"
      onclick="window.location.href='${pageContext.request.contextPath}/views/Service_page/product.jsp'"
      style="cursor: pointer"
    >
      <img
        src="images/home/product.jpg"
        alt="Nutrition Products"
        loading="lazy"
        decoding="async"
      />
      <h3>Nutrition Products</h3>
      <p>
        Sản phẩm dinh dưỡng, dụng cụ thể hình chính hãng với chất lượng cao và
        giá cả hợp lý.
      </p>
    </div>
  </section>

  <!-- NEWS -->
  <h2>Tin tức mới nhất</h2>
  <section class="news">
    <div class="news-box">
      <img
        src="images/home/opening.jpg"
        alt="Khai trương cơ sở mới"
        loading="lazy"
        decoding="async"
      />
      <div class="news-content">
        <h4>Khai trương cơ sở mới</h4>
        <p>
          Tham gia buổi khai trương và nhận ưu đãi hấp dẫn với trang thiết bị
          hiện đại và không gian tập luyện rộng rãi.
        </p>
      </div>
      <button
        class="see-more-btn"
        onclick="window.location.href='${pageContext.request.contextPath}/views/News_page/news_main.jsp'"
      >
        XEM THÊM
      </button>
    </div>
    <div class="news-box">
      <img
        src="images/home/yoga.jpeg"
        alt="Khóa học Yoga"
        loading="lazy"
        decoding="async"
      />
      <div class="news-content">
        <h4>Khóa học Yoga</h4>
        <p>
          Cải thiện sức khỏe với lớp Yoga mỗi tuần, giúp bạn thư giãn và cân
          bằng cuộc sống.
        </p>
      </div>
      <button
        class="see-more-btn"
        onclick="window.location.href='${pageContext.request.contextPath}/views/News_page/news_main.jsp'"
      >
        XEM THÊM
      </button>
    </div>
    <div class="news-box">
      <img
        src="images/home/loseWeight.jpg"
        alt="Chương trình giảm cân"
        loading="lazy"
        decoding="async"
      />
      <div class="news-content">
        <h4>Chương trình giảm cân</h4>
        <p>
          Giảm cân hiệu quả cùng chuyên gia dinh dưỡng với phương pháp khoa học
          và an toàn.
        </p>
      </div>
      <button
        class="see-more-btn"
        onclick="window.location.href='${pageContext.request.contextPath}/views/News_page/news_main.jsp'"
      >
        XEM THÊM
      </button>
    </div>
  </section>

  <!-- TRANSFORMATION + FEEDBACK -->
  <h2>Hành trình thay đổi & Feedback</h2>
  <div class="tf-feedback">
    <div class="transformation">
      <img
        id="transform-img"
        src="images/home/feedback3.jpg"
        alt="Transformation"
        loading="lazy"
      />
    </div>
    <div class="feedback">
      <div class="feedback-box active">
        <div class="feedback-header">
          <img src="images/home/ava1.jpg" alt="Anh Nam" />
          <div class="info"><b>Anh Nam</b>, 28 tuổi<br />3 năm tập GymFit</div>
        </div>
        <p>
          Nhờ GymFit tôi đã tự tin hơn và có sức khỏe tốt hơn! Huấn luyện viên
          rất chuyên nghiệp và tận tâm.
        </p>
      </div>
      <div class="feedback-box">
        <div class="feedback-header">
          <img src="images/home/ava2.jpg" alt="Anh Tài" />
          <div class="info">
            <b>Anh Tài</b>, 32 tuổi<br />1.5 năm tập GymFit
          </div>
        </div>
        <p>
          Sau 6 tháng tập luyện, tôi đã giảm được 15kg và có body săn chắc như
          mong muốn. HLV rất chuyên nghiệp và tận tâm!
        </p>
      </div>
      <div class="feedback-box">
        <div class="feedback-header">
          <img src="images/home/ava3.jpg" alt="Chị Hương" />
          <div class="info">
            <b>Chị Hương</b>, 30 tuổi<br />2 năm tập GymFit
          </div>
        </div>
        <p>
          Tôi đã giảm được 10kg sau 3 tháng tập luyện. Cảm ơn GymFit đã giúp tôi
          lấy lại vóc dáng và sự tự tin!
        </p>
      </div>
    </div>
  </div>

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

  <!-- Add floating buttons CSS -->
  <link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/css/floating-buttons.css"
  />

  <script>
    // Danh sách ảnh transformation từ thư mục local (chỉ dùng ảnh liên quan đến sự thay đổi)
    const imgs = [
      'images/home/feedback3.jpg', // Ảnh nam (Anh Nam)
      'images/home/feedback2.jpg', // Ảnh nam 2 (Anh Tài)
      'images/home/feedback.png', // Ảnh nữ (Chị Hương)
    ];

    // Khởi tạo ảnh transformation
    let imgIdx = 0;
    const imgEl = document.getElementById('transform-img');

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

    // Enhanced feedback rotation with smooth transitions
    const feedbacks = document.querySelectorAll('.feedback-box');
    let fbIdx = 0;

    setInterval(() => {
      // Smooth image transition
      imgEl.style.opacity = '0.3';

      setTimeout(() => {
        imgIdx = (imgIdx + 1) % imgs.length;
        imgEl.src = imgs[imgIdx];
        setTimeout(() => {
          imgEl.style.opacity = '1';
        }, 100);
      }, 400);

      // Smooth feedback transition
      feedbacks[fbIdx].classList.remove('active');
      fbIdx = (fbIdx + 1) % feedbacks.length;

      setTimeout(() => {
        feedbacks[fbIdx].classList.add('active');
      }, 200);
    }, 3000);

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

    // Observe service boxes and news boxes for animation
    document.querySelectorAll('.service-box, .news-box').forEach((el) => {
      el.style.opacity = '0';
      el.style.transform = 'translateY(30px)';
      el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
      observer.observe(el);
    });

    // Chat icon interaction
    document.querySelector('.chat-icon').addEventListener('click', function () {
      alert('Chức năng AI Chat đang được phát triển!');
    });
  </script>

  <%@ include file="/views/common/footer.jsp" %>
</main>
