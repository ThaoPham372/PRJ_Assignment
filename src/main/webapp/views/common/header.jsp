<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Utils.SessionUtil" %>
<%@ page import="service.shop.CartService" %>
<%@ page import="service.shop.CartServiceImpl" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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

      /* Global Back Button Style */
      .btn-back {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        padding: 10px 20px;
        background: white;
        border: 2px solid #e9ecef;
        border-radius: 10px;
        color: var(--text);
        text-decoration: none;
        font-weight: 600;
        transition: all 0.3s ease;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
      }

      .btn-back:hover {
        border-color: var(--accent);
        color: var(--accent);
        transform: translateX(-5px);
        box-shadow: 0 4px 12px rgba(236, 139, 94, 0.2);
      }

      .btn-back i {
        font-size: 1rem;
        transition: transform 0.3s ease;
      }

      .btn-back:hover i {
        transform: translateX(-3px);
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
        border-bottom: 3px solid #ec8b5a;
      }

      .logo {
        font-size: 1.8rem;
        font-weight: 900;
        letter-spacing: 0.5px;
        display: flex;
        align-items: center;
        gap: 12px;
        color: #fff;
        text-decoration: none;
        transition: all 0.3s ease;
      }

      .logo:hover {
        color: var(--accent);
        transform: scale(1.05);
      }

      .logo img {
        height: 60px;
        width: auto;
        object-fit: contain;
        transition: all 0.3s ease;
      }

      .logo:hover img {
        transform: scale(1.05);
        filter: brightness(1.1);
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

      /* Auth actions layout */
      .auth-actions {
        display: flex;
        align-items: center;
        gap: 8px;
      }

      /* Cart Icon */
      .cart-icon-wrapper {
        position: relative;
        margin-right: 10px;
      }

      .cart-icon-link {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 48px;
        height: 48px;
        background: rgba(255, 255, 255, 0.15);
        border-radius: 50%;
        color: #fff;
        text-decoration: none;
        transition: all 0.3s ease;
        border: 2px solid rgba(255, 255, 255, 0.2);
      }

      .cart-icon-link:hover {
        background: rgba(255, 255, 255, 0.25);
        transform: scale(1.1);
      }

      .cart-icon-link i {
        font-size: 1.3rem;
      }

      .cart-badge {
        position: absolute;
        top: -5px;
        right: -5px;
        background: #dc3545;
        color: #fff;
        border-radius: 50%;
        width: 22px;
        height: 22px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 0.75rem;
        font-weight: 700;
        border: 2px solid var(--primary);
        box-shadow: 0 2px 8px rgba(220, 53, 69, 0.4);
      }

      .cart-badge.empty {
        display: none;
      }

      /* Username Display Button */
      .btn-username {
        background: rgba(255, 255, 255, 0.95);
        color: var(--primary);
        border: 2px solid rgba(255, 255, 255, 0.3);
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.15);
        font-weight: 700;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        padding: 10px 20px;
        border-radius: 50px;
        text-decoration: none;
        transition: all 0.3s ease;
      }

      .btn-username:before {
        content: '👤';
        font-size: 1.1rem;
      }

      .btn-username:hover {
        background: white;
        color: var(--accent);
        transform: translateY(-2px);
        box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
        border-color: var(--accent);
      }

      /* Outline button for other cases */
      .btn-outline {
        background: transparent;
        color: #fff;
        border: 2px solid rgba(255, 255, 255, 0.6);
        box-shadow: none;
      }
      .btn-outline:hover {
        background: rgba(255, 255, 255, 0.12);
        box-shadow: 0 4px 12px rgba(255, 255, 255, 0.15);
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
      <a href="${pageContext.request.contextPath}/home.jsp" class="logo">
        <img
          src="${pageContext.request.contextPath}/images/logo/logoGymfit.png"
          alt="GymFit Logo"
        />
        GymFit
      </a>
      <nav>
        <ul>
          <li>
            <a href="${pageContext.request.contextPath}/home.jsp">HOME</a>
          </li>
          <li>
            <a href="${pageContext.request.contextPath}/services">SERVICES</a>
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
              
              
                
          <%-- Load cart count if user is logged in --%>
          <c:set var="cartCount" value="0" />
          <c:if test="${sessionScope.user != null && sessionScope.user.id != null}">
            <%
              try {
                // Use SessionUtil from Utils package (correct package structure)
                Integer userIdInt = SessionUtil.getUserId(request);
                if (userIdInt != null) {
                  // Convert Integer to Long for CartService
                  Long userId = userIdInt.longValue();
                  
                  // Use CartService from service.shop package (correct package structure)
                  // Reuse existing service layer following MVC pattern
                  CartService cartService = new CartServiceImpl();
                  
                  // Use getCartItemCount() method - optimized method instead of view().size()
                  // This method is more efficient as it only counts items without loading all data
                  int count = cartService.getCartItemCount(userId);
                  pageContext.setAttribute("cartCount", count);
                }
              } catch (Exception e) {
                // Silently fail, cart count will be 0
                // Log error for debugging but don't break the page
                // This ensures the header always renders even if cart service fails
                System.err.println("[Header] Error loading cart count: " + e.getMessage());
                e.printStackTrace();
                pageContext.setAttribute("cartCount", 0);
              }
            %>
          </c:if>
          
          <div class="auth-actions">
            <%-- Cart Icon --%>
            <div class="cart-icon-wrapper">
              <a href="${pageContext.request.contextPath}/cart" class="cart-icon-link" title="Giỏ hàng">
                <i class="fas fa-shopping-cart"></i>
                <c:if test="${cartCount > 0}">
                  <span class="cart-badge">${cartCount > 99 ? '99+' : cartCount}</span>
                </c:if>
              </a>
            </div>
      <c:choose>
        <c:when test="${sessionScope.user != null}">
          <c:set
            var="dashboardHref"
            value="${pageContext.request.contextPath}/home"
          />
          <c:if test="${not empty sessionScope.userRoles}">
            <c:choose>
              <c:when test="${fn:contains(sessionScope.userRoles, 'ADMIN')}">
                <c:set
                  var="dashboardHref"
                  value="${pageContext.request.contextPath}/admin/dashboard"
                />
              </c:when>
              <c:when test="${fn:contains(sessionScope.userRoles, 'PT')}">
                <c:set
                  var="dashboardHref"
                  value="${pageContext.request.contextPath}/pt/dashboard"
                />
              </c:when>
              <c:when test="${fn:contains(sessionScope.userRoles, 'USER')}">
                <c:set
                  var="dashboardHref"
                  value="${pageContext.request.contextPath}/member/dashboard"
                />
              </c:when>
              <c:when test="${fn:contains(sessionScope.userRoles, 'MEMBER')}">
                <c:set
                  var="dashboardHref"
                  value="${pageContext.request.contextPath}/member/dashboard"
                />
              </c:when>
            </c:choose>
          </c:if>

          <div class="auth-actions">
            <a
              href="${dashboardHref}"
              class="btn-username"
              title="Xem Dashboard"
            >
              ${sessionScope.user.username != null ? sessionScope.user.username
              : 'User'}
            </a>
            <a href="${pageContext.request.contextPath}/logout" 
               class="btn"
               onclick="sessionStorage.removeItem('gymfit_chat_session'); return true;"
              >ĐĂNG XUẤT</a
            >
          </div>
        </c:when>
        <c:otherwise>
          <a href="${pageContext.request.contextPath}/login" class="btn"
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
