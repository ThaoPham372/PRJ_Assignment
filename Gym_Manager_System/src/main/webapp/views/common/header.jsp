<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${pageTitle != null ? pageTitle : 'GymFit'}</title>

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

    <!-- Custom CSS -->
    <link
      href="${pageContext.request.contextPath}/css/styles.css"
      rel="stylesheet"
    />

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
      }

      * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
      }

      body {
        font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI',
          Roboto, sans-serif;
        color: var(--text);
        background: #f6f6f8;
        line-height: 1.6;
        font-weight: 400;
      }

      /* HEADER */
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
        backdrop-filter: blur(10px);
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

      .btn {
        background: var(--gradient-accent);
        color: #fff;
        border: none;
        border-radius: 50px;
        padding: 12px 24px;
        font-weight: 600;
        font-size: 0.95rem;
        cursor: pointer;
        transition: all 0.3s ease;
        box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
        text-decoration: none;
        display: inline-block;
      }

      .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
        filter: brightness(1.05);
      }

      @media (max-width: 900px) {
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
      }
    </style>
  </head>
  <body>
    <!-- HEADER -->
    <header>
      <a href="${pageContext.request.contextPath}/home.jsp" class="logo"
        >🏋️ GymFit</a
      >
      <nav>
        <ul>
          <li>
            <a href="${pageContext.request.contextPath}/home.jsp">HOME</a>
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
      <c:choose>
        <c:when test="${sessionScope.user != null}">
          <div class="btn">
            ${sessionScope.user.username != null ? sessionScope.user.username :
            'User'}
          </div>
        </c:when>
        <c:otherwise>
          <a
            href="${pageContext.request.contextPath}/views/login.jsp"
            class="btn"
            >ĐĂNG NHẬP</a
          >
        </c:otherwise>
      </c:choose>
    </header>

    <!-- Alert Messages -->
    <c:if test="${not empty sessionScope.successMessage}">
      <div
        class="alert alert-success alert-dismissible fade show m-3"
        role="alert"
      >
        <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="alert"
        ></button>
      </div>
      <c:remove var="successMessage" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.errorMessage}">
      <div
        class="alert alert-danger alert-dismissible fade show m-3"
        role="alert"
      >
        <i class="fas fa-exclamation-circle"></i> ${sessionScope.errorMessage}
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="alert"
        ></button>
      </div>
      <c:remove var="errorMessage" scope="session" />
    </c:if>

    <!-- Main Content Container -->
    <main class="container-fluid py-4"></main>
  </body>
</html>
