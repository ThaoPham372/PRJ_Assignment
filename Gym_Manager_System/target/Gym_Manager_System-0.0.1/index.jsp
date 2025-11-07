<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ include
file="/views/common/header.jsp" %>

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
    font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,
      sans-serif;
    background: #f9f9f9;
    color: var(--text);
    line-height: 1.6;
    font-weight: 400;
    overflow-x: hidden;
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
    background: var(--gradient-primary),
      url('https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3&auto=format&fit=crop&w=2070&q=80')
        no-repeat center/cover;
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
    cursor: pointer;
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

  /* CHAT ICON */
  .chat-icon {
    position: fixed;
    bottom: 30px;
    right: 30px;
    background: var(--chat);
    padding: 18px 24px;
    border-radius: 50px;
    font-weight: 700;
    color: #000;
    cursor: pointer;
    box-shadow: 0 8px 25px rgba(255, 222, 89, 0.4);
    display: flex;
    align-items: center;
    gap: 10px;
    transition: all 0.3s ease;
    z-index: 50;
    font-size: 0.95rem;
  }

  .chat-icon:hover {
    transform: scale(1.1);
    box-shadow: 0 12px 35px rgba(255, 222, 89, 0.5);
  }

  .chat-icon i {
    font-size: 1.2rem;
  }

  /* RESPONSIVE */
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

    .services {
      gap: 20px;
      flex-wrap: wrap;
    }

    .service-box {
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
      src="https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
      alt="Personal Training"
      loading="lazy"
      decoding="async"
      referrerpolicy="no-referrer"
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
      src="https://images.unsplash.com/photo-1540497077202-7c8a3999166f?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
      alt="Group Training"
      loading="lazy"
      decoding="async"
      referrerpolicy="no-referrer"
    />
    <h3>Group Training</h3>
    <p>
      Lớp tập nhóm tạo động lực và gắn kết, giúp bạn có thêm bạn bè và động lực
      tập luyện mỗi ngày.
    </p>
  </div>
  <div
    class="service-box"
    onclick="window.location.href='${pageContext.request.contextPath}/views/Service_page/product.jsp'"
    style="cursor: pointer"
  >
    <img
      src="https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
      alt="Nutrition Products"
      loading="lazy"
      decoding="async"
      referrerpolicy="no-referrer"
    />
    <h3>Nutrition Products</h3>
    <p>
      Sản phẩm dinh dưỡng, dụng cụ thể hình chính hãng với chất lượng cao và giá
      cả hợp lý.
    </p>
  </div>
</section>

<!-- CHAT ICON -->
<div
  class="chat-icon"
  onclick="alert('Chức năng AI Chat đang được phát triển!')"
>
  <i class="fas fa-comments"></i> AI Chat
</div>

<script>
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

  // Observe service boxes for animation
  document.querySelectorAll('.service-box').forEach((el) => {
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
