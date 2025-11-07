<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>GymFit - Admin Home</title>

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
      }

      /* HEADER với Admin Menu */
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

      .logo:hover {
        color: var(--accent);
        transform: scale(1.05);
      }

      nav ul {
        list-style: none;
        display: flex;
        gap: 32px;
        margin: 0;
        padding: 0;
      }

      nav a {
        color: #fff;
        text-decoration: none;
        font-weight: 500;
        font-size: 1rem;
        transition: all 0.3s ease;
        position: relative;
        padding: 8px 0;
      }

      nav a::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        width: 0;
        height: 2px;
        background: var(--accent);
        transition: width 0.3s ease;
      }

      nav a:hover,
      nav a.active {
        color: var(--accent);
        transform: translateY(-2px);
      }

      nav a:hover::after,
      nav a.active::after {
        width: 100%;
      }

      /* Admin Avatar Dropdown */
      .admin-dropdown {
        position: relative;
      }

      .admin-avatar {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: all 0.3s ease;
        box-shadow: 0 4px 15px rgba(236, 139, 94, 0.4);
        font-size: 1.3rem;
        color: #fff;
      }

      .admin-avatar:hover {
        transform: scale(1.1);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.5);
      }

      .admin-menu {
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

      .admin-menu.active {
        opacity: 1;
        visibility: visible;
        transform: translateY(0);
      }

      .admin-menu::before {
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

      .admin-menu-header {
        padding: 20px;
        border-bottom: 2px solid #f0f0f0;
        text-align: center;
      }

      .admin-menu-header h4 {
        color: var(--primary);
        font-size: 1.1rem;
        margin-bottom: 5px;
      }

      .admin-menu-header p {
        color: var(--text-light);
        font-size: 0.85rem;
      }

      .admin-menu-list {
        list-style: none;
        padding: 10px 0;
      }

      .admin-menu-item {
        transition: background 0.2s ease;
      }

      .admin-menu-item:hover {
        background: #f8f9fa;
      }

      .admin-menu-link {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px 20px;
        color: var(--text);
        text-decoration: none;
        font-size: 0.95rem;
        transition: all 0.2s ease;
      }

      .admin-menu-link:hover {
        color: var(--accent);
        padding-left: 25px;
      }

      .admin-menu-link i {
        font-size: 1.1rem;
        width: 20px;
        color: var(--accent);
      }

      .admin-menu-footer {
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
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
      }

      /* Rest of the styles from home.jsp */
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
        max-width: 600px;
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
        box-shadow: 0 8px 25px rgba(236, 139, 94, 0.4);
        text-decoration: none;
        display: inline-block;
      }

      .btn:hover {
        transform: translateY(-3px) scale(1.05);
        box-shadow: 0 12px 35px rgba(236, 139, 94, 0.5);
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
        padding: 20px 25px 15px;
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
        margin: 0 auto;
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
        transition: all 0.8s ease;
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

      /* FOOTER */
      footer {
        background: var(--primary);
        color: #fff;
        padding: 50px 60px 20px;
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
        transition: color 0.25s;
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

        nav ul {
          gap: 20px;
          flex-wrap: wrap;
          justify-content: center;
        }

        .hero h1 {
          font-size: 2.5rem;
        }

        .services,
        .news {
          flex-wrap: wrap;
        }

        .service-box,
        .news-box {
          width: 100%;
          max-width: 350px;
        }
      }
    </style>
  </head>
  <body>
    <!-- HEADER with Admin Dropdown -->
    <header>
      <a
        href="${pageContext.request.contextPath}/views/admin/admin_home.jsp"
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
      <nav>
        <ul>
          <li>
            <a
              href="${pageContext.request.contextPath}/views/admin/admin_home.jsp"
              class="active"
              >HOME</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/views/Service_page/services_main.jsp"
              >SERVICES</a
            >
          </li>
          <li>
            <a href="${pageContext.request.contextPath}/views/calendar.jsp"
              >CALENDAR</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/views/News_page/news_main.jsp"
              >NEWS</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/views/About_us/AboutUs.jsp"
              >ABOUT US</a
            >
          </li>
        </ul>
      </nav>

      <!-- Admin Avatar Dropdown -->
      <div class="admin-dropdown">
        <div class="admin-avatar" id="adminAvatar">
          <i class="fas fa-user-shield"></i>
        </div>

        <div class="admin-menu" id="adminMenu">
          <div class="admin-menu-header">
            <h4>
              ${sessionScope.user != null ? sessionScope.user.username : 'Admin
              User'}
            </h4>
            <p>Administrator</p>
          </div>

          <ul class="admin-menu-list">
            <li class="admin-menu-item">
              <a
                href="${pageContext.request.contextPath}/views/admin/dashboard.jsp"
                class="admin-menu-link"
              >
                <i class="fas fa-tachometer-alt"></i>
                <span>Dashboard</span>
              </a>
            </li>
            <li class="admin-menu-item">
              <a
                href="${pageContext.request.contextPath}/views/admin/profile.jsp"
                class="admin-menu-link"
              >
                <i class="fas fa-user-circle"></i>
                <span>Profile của Admin</span>
              </a>
            </li>
            <li class="admin-menu-item">
              <a
                href="${pageContext.request.contextPath}/admin/users"
                class="admin-menu-link"
              >
                <i class="fas fa-users-cog"></i>
                <span>Quản lý tài khoản</span>
              </a>
            </li>
            <li class="admin-menu-item">
              <a
                href="${pageContext.request.contextPath}/admin/products"
                class="admin-menu-link"
              >
                <i class="fas fa-box"></i>
                <span>Quản lý sản phẩm</span>
              </a>
            </li>
            <li class="admin-menu-item">
              <a
                href="${pageContext.request.contextPath}/views/admin/member_management.jsp"
                class="admin-menu-link"
              >
                <i class="fas fa-users"></i>
                <span>Quản lý hội viên</span>
              </a>
            </li>
            <li class="admin-menu-item">
              <a
                href="${pageContext.request.contextPath}/views/admin/service_schedule.jsp"
                class="admin-menu-link"
              >
                <i class="fas fa-calendar-alt"></i>
                <span>Dịch vụ & Lịch tập</span>
              </a>
            </li>
            <li class="admin-menu-item">
              <a
                href="${pageContext.request.contextPath}/views/admin/trainer_management.jsp"
                class="admin-menu-link"
              >
                <i class="fas fa-chalkboard-teacher"></i>
                <span>Quản lý PT</span>
              </a>
            </li>
            <li class="admin-menu-item">
              <a
                href="${pageContext.request.contextPath}/views/admin/order_management.jsp"
                class="admin-menu-link"
              >
                <i class="fas fa-box"></i>
                <span>Quản lý đơn hàng</span>
              </a>
            </li>
            <li class="admin-menu-item">
              <a
                href="${pageContext.request.contextPath}/views/admin/payment_finance.jsp"
                class="admin-menu-link"
              >
                <i class="fas fa-money-bill-wave"></i>
                <span>Thanh toán & Tài chính</span>
              </a>
            </li>
            <li class="admin-menu-item">
              <a
                href="${pageContext.request.contextPath}/views/admin/reports.jsp"
                class="admin-menu-link"
              >
                <i class="fas fa-chart-line"></i>
                <span>Báo cáo & Thống kê</span>
              </a>
            </li>
          </ul>

          <div class="admin-menu-footer">
            <button
              class="btn-logout"
              onclick="window.location.href='${pageContext.request.contextPath}/views/login.jsp'"
            >
              <i class="fas fa-sign-out-alt"></i>
              Đăng xuất
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- HERO -->
    <section class="hero">
      <h1>Chào mừng Admin - GymFit</h1>
      <p>Quản lý phòng gym của bạn một cách chuyên nghiệp</p>
      <div class="hero-actions">
        <a
          href="${pageContext.request.contextPath}/views/admin/dashboard.jsp"
          class="btn"
        >
          Dashboard Admin
        </a>
        <a
          href="${pageContext.request.contextPath}/views/admin/member_management.jsp"
          class="btn btn-secondary"
        >
          Quản lý Hội viên
        </a>
      </div>
    </section>

    <!-- SERVICES -->
    <h2>Dịch vụ của chúng tôi</h2>
    <section class="services">
      <div
        class="service-box"
        onclick="window.location.href='${pageContext.request.contextPath}/views/Service_page/personalTraining.jsp'"
      >
        <img
          src="${pageContext.request.contextPath}/images/home/personalTraining.jpg"
          alt="Personal Training"
          loading="lazy"
        />
        <h3>Personal Training</h3>
        <p>
          Huấn luyện cá nhân 1-1 cùng HLV chuyên nghiệp với chương trình tập
          luyện được thiết kế riêng cho từng cá nhân.
        </p>
      </div>
      <div
        class="service-box"
        onclick="window.location.href='${pageContext.request.contextPath}/views/Service_page/groupTraining.jsp'"
      >
        <img
          src="${pageContext.request.contextPath}/images/home/groupTraining.jpg"
          alt="Group Training"
          loading="lazy"
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
      >
        <img
          src="${pageContext.request.contextPath}/images/home/product.jpg"
          alt="Nutrition Products"
          loading="lazy"
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
          src="${pageContext.request.contextPath}/images/home/opening.jpg"
          alt="Khai trương cơ sở mới"
          loading="lazy"
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
          src="${pageContext.request.contextPath}/images/home/loseWeight.jpg"
          alt="Khóa học Yoga"
          loading="lazy"
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
          src="${pageContext.request.contextPath}/images/home/loseWeight.jpg"
          alt="Chương trình giảm cân"
          loading="lazy"
        />
        <div class="news-content">
          <h4>Chương trình giảm cân</h4>
          <p>
            Giảm cân hiệu quả cùng chuyên gia dinh dưỡng với phương pháp khoa
            học và an toàn.
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
          src="${pageContext.request.contextPath}/images/home/feedback.png"
          alt="Transformation"
        />
      </div>
      <div class="feedback">
        <div class="feedback-box active">
          <div class="feedback-header">
            <img
              src="${pageContext.request.contextPath}/images/home/ava1.jpg"
              alt="Anh Nam"
            />
            <div class="info">
              <b>Anh Nam</b>, 28 tuổi<br />3 năm tập GymFit
            </div>
          </div>
          <p>
            Nhờ GymFit tôi đã tự tin hơn và có sức khỏe tốt hơn! Huấn luyện viên
            rất chuyên nghiệp và tận tâm.
          </p>
        </div>
        <div class="feedback-box">
          <div class="feedback-header">
            <img
              src="${pageContext.request.contextPath}/images/home/ava2.jpg"
              alt="Chị Linh"
            />
            <div class="info">
              <b>Chị Linh</b>, 32 tuổi<br />1.5 năm tập GymFit
            </div>
          </div>
          <p>
            Dịch vụ tận tâm, HLV chuyên nghiệp, rất hài lòng với kết quả đạt
            được sau thời gian tập luyện.
          </p>
        </div>
        <div class="feedback-box">
          <div class="feedback-header">
            <img
              src="${pageContext.request.contextPath}/images/home/ava3.jpg"
              alt="Tuấn"
            />
            <div class="info"><b>Tuấn</b>, 24 tuổi<br />2 năm tập GymFit</div>
          </div>
          <p>
            Không khí phòng tập hiện đại, tạo động lực mỗi ngày. Thiết bị đầy đủ
            và chất lượng cao.
          </p>
        </div>
      </div>
    </div>

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
      // Admin Menu Toggle
      const adminAvatar = document.getElementById('adminAvatar');
      const adminMenu = document.getElementById('adminMenu');

      adminAvatar.addEventListener('click', function (e) {
        e.stopPropagation();
        adminMenu.classList.toggle('active');
      });

      // Close menu when clicking outside
      document.addEventListener('click', function (e) {
        if (!adminMenu.contains(e.target) && !adminAvatar.contains(e.target)) {
          adminMenu.classList.remove('active');
        }
      });

      // Feedback & Transformation rotation
      const imgs = [
        '${pageContext.request.contextPath}/images/home/feedback.png',
        '${pageContext.request.contextPath}/images/home/feedback2.jpg',
        '${pageContext.request.contextPath}/images/home/feedback3.jpg',
        '${pageContext.request.contextPath}/images/home/personalTraining.jpg',
        '${pageContext.request.contextPath}/images/home/groupTraining.jpg',
        '${pageContext.request.contextPath}/images/home/product.jpg',
      ];

      let imgIdx = 0;
      const imgEl = document.getElementById('transform-img');

      const feedbacks = document.querySelectorAll('.feedback-box');
      let fbIdx = 0;

      setInterval(() => {
        imgIdx = (imgIdx + 1) % imgs.length;
        imgEl.src = imgs[imgIdx];

        feedbacks[fbIdx].classList.remove('active');
        fbIdx = (fbIdx + 1) % feedbacks.length;

        setTimeout(() => {
          feedbacks[fbIdx].classList.add('active');
        }, 200);
      }, 3000);
    </script>
  </body>
</html>
