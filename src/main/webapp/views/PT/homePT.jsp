<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>GymFit - PT Dashboard</title>

    <!-- Font Awesome Icons -->
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
      rel="stylesheet"
    />

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap"
      rel="stylesheet"
    />

    <style>
      :root {
        --primary: #141a49;
        --primary-light: #1e2a5c;
        --accent: #ec8b5a;
        --accent-hover: #d67a4f;
        --chat: #ffde59;
        --text: #2c3e50;
        --text-light: #5a6c7d;
        --muted: #f8f9fa;
        --card: #ffffff;
        --shadow: rgba(0, 0, 0, 0.1);
        --shadow-hover: rgba(0, 0, 0, 0.15);
        --gradient-primary: linear-gradient(135deg, #141a49 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5a 0%, #d67a4f 100%);
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
      }

      /* HEADER với PT Menu */
      header {
        background: var(--gradient-primary);
        color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 20px 50px;
        position: sticky;
        top: 0;
        z-index: 100;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
      }

      .logo {
        font-size: 1.8rem;
        font-weight: 900;
        letter-spacing: 0.5px;
        display: flex;
        align-items: center;
        gap: 8px;
        color: #fff;
        text-decoration: none;
        transition: all 0.3s ease;
      }

      .logo img {
        max-height: 50px;
        object-fit: contain;
      }

      .logo:hover {
        color: var(--accent);
        transform: scale(1.05);
      }

      .header-info {
        display: flex;
        align-items: center;
        gap: 20px;
      }

      .pt-name {
        font-size: 1.1rem;
        font-weight: 600;
        color: #fff;
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .pt-name i {
        color: var(--accent);
      }

      /* PT Avatar Dropdown */
      .pt-dropdown {
        position: relative;
      }

      .pt-avatar {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: all 0.3s ease;
        box-shadow: 0 4px 15px rgba(236, 139, 90, 0.4);
        font-size: 1.3rem;
        color: #fff;
        overflow: hidden;
      }

      .pt-avatar img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .pt-avatar:hover {
        transform: scale(1.1);
        box-shadow: 0 6px 20px rgba(236, 139, 90, 0.5);
      }

      .pt-menu {
        position: absolute;
        top: 65px;
        right: 0;
        background: #fff;
        border-radius: 15px;
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2);
        min-width: 280px;
        opacity: 0;
        visibility: hidden;
        transform: translateY(-10px);
        transition: all 0.3s ease;
        z-index: 200;
      }

      .pt-menu.active {
        opacity: 1;
        visibility: visible;
        transform: translateY(0);
      }

      .pt-menu::before {
        content: '';
        position: absolute;
        top: -8px;
        right: 20px;
        width: 0;
        height: 0;
        border-left: 8px solid transparent;
        border-right: 8px solid transparent;
        border-bottom: 8px solid #fff;
      }

      .pt-menu-header {
        padding: 20px;
        border-bottom: 2px solid #f0f0f0;
        text-align: center;
      }

      .pt-menu-header h4 {
        color: var(--primary);
        font-size: 1.1rem;
        margin-bottom: 5px;
      }

      .pt-menu-header p {
        color: var(--text-light);
        font-size: 0.85rem;
      }

      .pt-menu-list {
        list-style: none;
        padding: 10px 0;
      }

      .pt-menu-item {
        transition: background 0.2s ease;
      }

      .pt-menu-item:hover {
        background: #f8f9fa;
      }

      .pt-menu-link {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px 20px;
        color: var(--text);
        text-decoration: none;
        font-size: 0.95rem;
        transition: all 0.2s ease;
      }

      .pt-menu-link:hover {
        color: var(--accent);
        padding-left: 25px;
      }

      .pt-menu-link i {
        font-size: 1.1rem;
        width: 20px;
        color: var(--accent);
      }

      .pt-menu-footer {
        padding: 15px 20px;
        border-top: 2px solid #f0f0f0;
      }

      .btn-logout {
        width: 100%;
        background: var(--gradient-accent);
        color: #fff;
        border: none;
        border-radius: 8px;
        padding: 10px;
        font-weight: 600;
        font-size: 0.9rem;
        cursor: pointer;
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
      }

      .btn-logout:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 90, 0.4);
      }

      /* HERO SECTION */
      .hero {
        background: linear-gradient(
            135deg,
            rgba(20, 26, 73, 0.9) 0%,
            rgba(30, 42, 92, 0.8) 100%
          ),
          url('${pageContext.request.contextPath}/images/home/backrough.jpg')
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
      .hero .hero-actions {
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
        max-width: 700px;
        animation: fadeInUp 1s ease-out 0.2s both;
      }

      .hero-actions {
        display: flex;
        gap: 15px;
        animation: fadeInUp 1s ease-out 0.4s both;
      }

      .btn {
        background: var(--gradient-accent);
        padding: 18px 40px;
        border: none;
        border-radius: 50px;
        color: #fff;
        font-size: 1.1rem;
        font-weight: 700;
        cursor: pointer;
        transition: all 0.3s ease;
        box-shadow: 0 8px 25px rgba(236, 139, 90, 0.4);
        text-decoration: none;
        display: inline-block;
      }

      .btn:hover {
        transform: translateY(-3px) scale(1.05);
        box-shadow: 0 12px 35px rgba(236, 139, 90, 0.5);
      }

      .btn-secondary {
        background: rgba(255, 255, 255, 0.2);
        backdrop-filter: blur(10px);
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

      /* QUICK STATS */
      .quick-stats {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 30px;
        padding: 60px 40px;
        max-width: 1200px;
        margin: -80px auto 0;
        position: relative;
        z-index: 10;
      }

      .stat-card {
        background: var(--card);
        border-radius: 20px;
        padding: 30px;
        box-shadow: 0 10px 40px var(--shadow);
        transition: all 0.3s ease;
        text-align: center;
        position: relative;
        overflow: hidden;
      }

      .stat-card::before {
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

      .stat-card:hover::before {
        transform: scaleX(1);
      }

      .stat-card:hover {
        transform: translateY(-10px);
        box-shadow: 0 20px 50px var(--shadow-hover);
      }

      .stat-icon {
        font-size: 3rem;
        color: var(--accent);
        margin-bottom: 15px;
      }

      .stat-number {
        font-size: 2.5rem;
        font-weight: 800;
        color: var(--primary);
        margin-bottom: 10px;
      }

      .stat-label {
        color: var(--text-light);
        font-size: 1rem;
        font-weight: 500;
      }

      /* FEATURES SECTION */
      h2 {
        text-align: center;
        margin: 80px 0 40px;
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

      .features {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        gap: 30px;
        padding: 60px 40px;
        max-width: 1200px;
        margin: 0 auto;
      }

      .feature-card {
        background: var(--card);
        border-radius: 20px;
        padding: 40px;
        box-shadow: 0 8px 30px var(--shadow);
        transition: all 0.4s ease;
        cursor: pointer;
        text-align: center;
      }

      .feature-card:hover {
        transform: translateY(-10px);
        box-shadow: 0 20px 40px var(--shadow-hover);
      }

      .feature-icon {
        font-size: 3.5rem;
        color: var(--accent);
        margin-bottom: 20px;
        display: inline-block;
        transition: transform 0.3s ease;
      }

      .feature-card:hover .feature-icon {
        transform: scale(1.1) rotate(5deg);
      }

      .feature-card h3 {
        font-size: 1.5rem;
        margin-bottom: 15px;
        color: var(--primary);
        font-weight: 700;
      }

      .feature-card p {
        color: var(--text-light);
        font-size: 1rem;
        line-height: 1.6;
      }

      /* FOOTER */
      footer {
        background: var(--primary);
        color: #fff;
        padding: 50px 60px 20px;
        margin-top: 80px;
      }

      .footer-container {
        display: flex;
        gap: 40px;
        flex-wrap: wrap;
        justify-content: space-between;
      }

      .footer-column {
        flex: 1;
        min-width: 220px;
      }

      .footer-brand {
        font-size: 1.5rem;
        font-weight: 800;
        margin-bottom: 15px;
      }

      .footer-column h3 {
        color: var(--chat);
        margin-bottom: 15px;
        font-size: 1.1rem;
        font-weight: 700;
      }

      .footer-column p,
      .footer-column li {
        margin-bottom: 8px;
        font-size: 0.9rem;
        line-height: 1.4;
      }

      .footer-column ul {
        list-style: none;
        padding: 0;
      }

      .footer-social a {
        margin-right: 12px;
        color: #fff;
        font-size: 1.2rem;
        transition: all 0.25s;
        display: inline-block;
        width: 40px;
        height: 40px;
        background: rgba(255, 255, 255, 0.1);
        border-radius: 50%;
        text-align: center;
        line-height: 40px;
      }

      .footer-social a:hover {
        background: var(--chat);
        color: var(--primary);
      }

      .footer-bottom {
        text-align: center;
        margin-top: 30px;
        border-top: 1px solid rgba(255, 255, 255, 0.2);
        padding-top: 15px;
        font-size: 0.85rem;
        opacity: 0.8;
      }

      /* RESPONSIVE */
      @media (max-width: 768px) {
        header {
          flex-direction: column;
          gap: 15px;
          padding: 20px;
        }

        .header-info {
          flex-direction: column;
          gap: 10px;
        }

        .hero h1 {
          font-size: 2.5rem;
        }

        .hero p {
          font-size: 1.1rem;
        }

        .quick-stats {
          grid-template-columns: 1fr;
          margin-top: -60px;
        }

        .features {
          grid-template-columns: 1fr;
        }
      }
    </style>
  </head>
  <body>
    <!-- HEADER with PT Dropdown -->
    <header>
      <a
        href="${pageContext.request.contextPath}/views/PT/homePT.jsp"
        class="logo"
      >
        <img
          src="${pageContext.request.contextPath}/images/logo/logo.png"
          alt="GymFit Logo"
          style="
            height: 2.5em;
            width: auto;
            margin-right: 12px;
            max-height: 50px;
            object-fit: contain;
          "
        />
        GymFit
      </a>

      <div class="header-info">
        <div class="pt-name">
          <i class="fas fa-user-tie"></i>
          <span
            >PT ${sessionScope.user != null ? sessionScope.user.username :
            'Trainer'}</span
          >
        </div>

        <!-- PT Avatar Dropdown -->
        <div class="pt-dropdown">
          <div class="pt-avatar" id="ptAvatar">
            <i class="fas fa-dumbbell"></i>
          </div>

          <div class="pt-menu" id="ptMenu">
            <div class="pt-menu-header">
              <h4>
                ${sessionScope.user != null ? sessionScope.user.username :
                'Personal Trainer'}
              </h4>
              <p>Huấn luyện viên cá nhân</p>
            </div>

            <ul class="pt-menu-list">
              <li class="pt-menu-item">
                <a
                  href="${pageContext.request.contextPath}/pt/profile"
                  class="pt-menu-link"
                >
                  <i class="fas fa-user-circle"></i>
                  <span>Hồ sơ cá nhân</span>
                </a>
              </li>
              <li class="pt-menu-item">
                <a
                  href="${pageContext.request.contextPath}/pt/schedule"
                  class="pt-menu-link"
                >
                  <i class="fas fa-calendar-alt"></i>
                  <span>Lịch huấn luyện</span>
                </a>
              </li>
              <li class="pt-menu-item">
                <a
                  href="${pageContext.request.contextPath}/pt/students"
                  class="pt-menu-link"
                >
                  <i class="fas fa-users"></i>
                  <span>Quản lý học viên</span>
                </a>
              </li>
              <li class="pt-menu-item">
                <a
                  href="${pageContext.request.contextPath}/pt/chat"
                  class="pt-menu-link"
                >
                  <i class="fas fa-comments"></i>
                  <span>Chat với học viên</span>
                </a>
              </li>
              <li class="pt-menu-item">
                <a
                  href="${pageContext.request.contextPath}/pt/reports"
                  class="pt-menu-link"
                >
                  <i class="fas fa-chart-line"></i>
                  <span>Thống kê & Báo cáo</span>
                </a>
              </li>
            </ul>

            <div class="pt-menu-footer">
              <a
                  href="${pageContext.request.contextPath}/logout"
                class="btn-logout"
               
              >
                <i class="fas fa-sign-out-alt"></i>
                Đăng xuất
                </a>
            </div>
          </div>
        </div>
      </div>
    </header>

    <!-- HERO -->
    <section class="hero">
      <h1>Chào mừng Personal Trainer</h1>
      <p>
        Quản lý học viên, lịch tập và theo dõi tiến độ huấn luyện của bạn một
        cách chuyên nghiệp
      </p>
      <div class="hero-actions">
        <a href="${pageContext.request.contextPath}/pt/schedule" class="btn">
          Xem lịch tập
        </a>
        <a
          href="${pageContext.request.contextPath}/pt/students"
          class="btn btn-secondary"
        >
          Quản lý học viên
        </a>
      </div>
    </section>

    <!-- QUICK STATS -->
    <section class="quick-stats">
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-users"></i>
        </div>
        <div class="stat-number">24</div>
        <div class="stat-label">Học viên đang phụ trách</div>
      </div>

      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-calendar-check"></i>
        </div>
        <div class="stat-number">48</div>
        <div class="stat-label">Buổi tập đã hoàn thành</div>
      </div>

      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-clock"></i>
        </div>
        <div class="stat-number">8</div>
        <div class="stat-label">Buổi tập hôm nay</div>
      </div>

      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-star"></i>
        </div>
        <div class="stat-number">4.8</div>
        <div class="stat-label">Đánh giá trung bình</div>
      </div>
    </section>

    <!-- FEATURES -->
    <h2>Chức năng chính</h2>
    <section class="features">
      <div
        class="feature-card"
        onclick="window.location.href='${pageContext.request.contextPath}/pt/profile'"
      >
        <div class="feature-icon">
          <i class="fas fa-user-circle"></i>
        </div>
        <h3>Hồ sơ cá nhân</h3>
        <p>
          Quản lý thông tin cá nhân, chỉnh sửa email, số điện thoại, avatar và
          đổi mật khẩu.
        </p>
      </div>

      <div
        class="feature-card"
        onclick="window.location.href='${pageContext.request.contextPath}/pt/schedule'"
      >
        <div class="feature-icon">
          <i class="fas fa-calendar-alt"></i>
        </div>
        <h3>Lịch huấn luyện</h3>
        <p>
          Xem và quản lý lịch tập, xác nhận/từ chối buổi tập, cập nhật trạng
          thái và tạo buổi tập mới.
        </p>
      </div>

      <div
        class="feature-card"
        onclick="window.location.href='${pageContext.request.contextPath}/pt/students'"
      >
        <div class="feature-icon">
          <i class="fas fa-users"></i>
        </div>
        <h3>Quản lý học viên</h3>
        <p>
          Xem danh sách học viên, theo dõi tiến độ, cập nhật chỉ số cơ thể và
          ghi chú cho từng học viên.
        </p>
      </div>

      <div
        class="feature-card"
        onclick="window.location.href='${pageContext.request.contextPath}/pt/chat'"
      >
        <div class="feature-icon">
          <i class="fas fa-comments"></i>
        </div>
        <h3>Chat với học viên</h3>
        <p>
          Giao tiếp trực tiếp với học viên, nhận thông báo và hỗ trợ kịp thời.
        </p>
      </div>

      <div
        class="feature-card"
        onclick="window.location.href='${pageContext.request.contextPath}/pt/reports'"
      >
        <div class="feature-icon">
          <i class="fas fa-chart-line"></i>
        </div>
        <h3>Thống kê & Báo cáo</h3>
        <p>
          Xem tổng số học viên, buổi tập hoàn thành, đánh giá và lịch sử làm
          việc.
        </p>
      </div>

      <div class="feature-card" onclick="alert('Chức năng đang phát triển!')">
        <div class="feature-icon">
          <i class="fas fa-bell"></i>
        </div>
        <h3>Thông báo</h3>
        <p>
          Nhận thông báo về lịch tập mới, thay đổi lịch và tin nhắn từ học viên.
        </p>
      </div>
    </section>

    <!-- FOOTER -->
    <footer>
      <div class="footer-container">
        <div class="footer-column">
          <div class="footer-brand">
            <img
              src="${pageContext.request.contextPath}/images/logo/logo.png"
              alt="GymFit Logo"
              style="
                height: 2em;
                width: auto;
                margin-right: 10px;
                vertical-align: middle;
                max-height: 40px;
                object-fit: contain;
              "
            />
            GymFit
          </div>
          <p>Nơi thay đổi sức khỏe và vóc dáng của bạn.</p>
          <p><i class="fas fa-phone"></i> 0123-456-789</p>
          <p><i class="fas fa-envelope"></i> contact@gymfit.vn</p>
          <p><i class="fas fa-clock"></i> 6:00 - 22:00 (Hàng ngày)</p>
        </div>
        <div class="footer-column">
          <h3>Địa chỉ</h3>
          <ul>
            <li>123 Nguyễn Văn Linh, Đà Nẵng</li>
            <li>45 Lý Thường Kiệt, Hà Nội</li>
            <li>98 Trần Hưng Đạo, TP.HCM</li>
            <li>56 Võ Văn Kiệt, Cần Thơ</li>
          </ul>
        </div>
        <div class="footer-column">
          <h3>Dịch vụ</h3>
          <ul>
            <li>Personal Training</li>
            <li>Group Classes</li>
            <li>Yoga & Dance</li>
            <li>Nutrition Products</li>
          </ul>
        </div>
        <div class="footer-column">
          <h3>Theo dõi chúng tôi</h3>
          <div class="footer-social">
            <a href="#"><i class="fab fa-facebook"></i></a>
            <a href="#"><i class="fab fa-instagram"></i></a>
            <a href="#"><i class="fab fa-youtube"></i></a>
          </div>
        </div>
      </div>
      <div class="footer-bottom">© 2025 GymFit. All rights reserved.</div>
    </footer>

    <!-- JavaScript -->
    <script>
      // PT Menu Toggle
      const ptAvatar = document.getElementById('ptAvatar');
      const ptMenu = document.getElementById('ptMenu');

      ptAvatar.addEventListener('click', function (e) {
        e.stopPropagation();
        ptMenu.classList.toggle('active');
      });

      // Close menu when clicking outside
      document.addEventListener('click', function (e) {
        if (!ptMenu.contains(e.target) && !ptAvatar.contains(e.target)) {
          ptMenu.classList.remove('active');
        }
      });
    </script>
  </body>
</html>
