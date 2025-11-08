<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  // If googleClientId is not set by servlet, load it from ConfigManager
  if (request.getAttribute("googleClientId") == null) {
    try {
      Utils.ConfigManager configManager = Utils.ConfigManager.getInstance();
      String clientId = configManager.getGoogleClientId();
      request.setAttribute("googleClientId", clientId);
    } catch (Exception e) {
      System.err.println("[register.jsp] Error loading Google Client ID: " + e.getMessage());
      request.setAttribute("googleClientId", null);
    }
  }
%>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng Ký - GymFit</title>

    <style>
      :root {
        --primary: #141a49;
        --accent: #ec8b5a;
        --support: #ffde59;
        --white: #ffffff;
        --text-dark: #333333;
        --text-light: #666666;
        --border: #e0e0e0;
        --error: #e74c3c;
        --gray-bg: #f5f5f5;
      }

      * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
      }

      body {
        font-family: 'Inter', sans-serif;
        background: url('${pageContext.request.contextPath}/images/home/backrough.jpg')
          no-repeat center center fixed;
        background-size: cover;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
        color: var(--white);
        position: relative;
      }

      body::before {
        content: '';
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: linear-gradient(
          135deg,
          rgba(20, 26, 70, 0.95) 0%,
          rgba(30, 42, 92, 0.9) 100%
        );
        z-index: 0;
      }

      .main-content {
        position: relative;
        z-index: 1;
      }

      /* Header */
      .header {
        background: var(--primary);
        height: 8px;
        width: 100%;
      }

      /* Main Content */
      .main-content {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 40px 20px;
      }

      .register-card {
        background: var(--primary);
        border-radius: 15px;
        padding: 40px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        width: 100%;
        max-width: 500px;
        border: none;
        position: relative;
        animation: fadeInUp 0.6s ease-out;
      }

      @keyframes borderGradient {
        0% {
          background-position: 0% 50%;
        }
        50% {
          background-position: 100% 50%;
        }
        100% {
          background-position: 0% 50%;
        }
      }

      @keyframes fadeInUp {
        from {
          opacity: 0;
          transform: translateY(20px);
        }
        to {
          opacity: 1;
          transform: translateY(0);
        }
      }

      /* Logo */
      .logo {
        text-align: center;
        margin-bottom: 20px;
      }

      .logo-image {
        width: 150px;
        height: auto;
        margin-bottom: 30px;
        filter: drop-shadow(0 4px 6px rgba(0, 0, 0, 0.1));
        animation: logoFloat 6s ease-in-out infinite;
        transition: all 0.3s ease;
      }

      .logo-image:hover {
        filter: drop-shadow(0 6px 8px rgba(236, 139, 90, 0.2));
        transform: scale(1.05);
      }

      @keyframes logoFloat {
        0%,
        100% {
          transform: translateY(0);
        }
        50% {
          transform: translateY(-10px);
        }
      }

      @keyframes bounce {
        0%,
        100% {
          transform: translateY(0);
        }
        50% {
          transform: translateY(-5px);
        }
      }

      /* Title */
      .page-title {
        text-align: center;
        color: var(--white);
        font-size: 1.1rem;
        font-weight: 600;
        margin-bottom: 30px;
        text-transform: uppercase;
        letter-spacing: 0.5px;
      }

      /* Form */
      .form-row {
        display: flex;
        gap: 15px;
        margin-bottom: 20px;
      }

      .form-group {
        flex: 1;
        margin-bottom: 20px;
      }

      .form-input {
        width: 100%;
        padding: 12px 15px;
        background: white;
        border: 2px solid transparent;
        border-radius: 4px;
        font-size: 0.9rem;
        color: var(--text-dark);
        transition: all 0.3s ease;
      }

      .form-input:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 2px rgba(236, 139, 90, 0.1);
      }

      .form-input::placeholder {
        color: var(--text-light);
        text-transform: uppercase;
        font-size: 0.8rem;
      }

      /* Date Input Styling */
      .form-group {
        position: relative;
      }

      .date-label {
        position: absolute;
        left: 15px;
        top: 50%;
        transform: translateY(-50%);
        color: var(--text-light);
        font-size: 0.8rem;
        text-transform: uppercase;
        pointer-events: none;
        transition: all 0.3s ease;
      }

      input[type='date'] {
        position: relative;
        padding-left: 90px;
      }

      input[type='date']::-webkit-calendar-picker-indicator {
        position: absolute;
        right: 10px;
        cursor: pointer;
      }

      /* Select Styling */
      select.form-input {
        appearance: none;
        padding-right: 30px;
        background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' fill='%23666' viewBox='0 0 16 16'%3E%3Cpath d='M8 11.5l-5-5h10l-5 5z'/%3E%3C/svg%3E");
        background-repeat: no-repeat;
        background-position: right 10px center;
        text-transform: uppercase;
        font-size: 0.8rem;
      }

      select.form-input option {
        color: var(--text-dark);
        font-size: 0.9rem;
      }

      /* Checkbox */
      .checkbox-group {
        display: flex;
        align-items: center;
        gap: 10px;
        margin: 20px 0;
      }

      .checkbox-input {
        appearance: none;
        width: 20px;
        height: 20px;
        border: 2px solid rgba(255, 255, 255, 0.3);
        border-radius: 4px;
        cursor: pointer;
        position: relative;
        transition: all 0.3s ease;
      }

      .checkbox-input:checked {
        background: var(--accent);
        border-color: var(--accent);
      }

      .checkbox-input:checked::before {
        content: '✓';
        position: absolute;
        color: white;
        font-size: 14px;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
      }

      .checkbox-label {
        color: rgba(255, 255, 255, 0.8);
        font-size: 0.9rem;
        cursor: pointer;
      }

      /* Register Button */
      .register-btn {
        width: 100%;
        background: var(--support);
        color: var(--primary);
        border: none;
        padding: 15px 30px;
        border-radius: 4px;
        font-size: 0.9rem;
        font-weight: 700;
        cursor: pointer;
        transition: all 0.3s ease;
        margin: 20px 0;
        text-transform: uppercase;
        letter-spacing: 1px;
      }

      .register-btn:hover:not(:disabled) {
        background: #f4d03f;
        transform: translateY(-1px);
        box-shadow: 0 4px 8px rgba(255, 222, 89, 0.4);
      }

      .register-btn:disabled {
        opacity: 0.7;
        cursor: not-allowed;
        transform: none;
      }

      /* Google Button */
      .google-btn {
        width: 100%;
        background: white;
        color: var(--text-dark);
        border: 1px solid var(--border);
        padding: 12px 20px;
        border-radius: 4px;
        font-size: 0.9rem;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        margin: 15px 0 25px;
        display: flex;
        align-items: center;
        justify-content: center;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        position: relative;
        overflow: hidden;
      }

      .google-btn::after {
        content: '';
        position: absolute;
        top: 50%;
        left: 50%;
        width: 120%;
        height: 120%;
        background: radial-gradient(
          circle,
          rgba(255, 255, 255, 0.8) 0%,
          transparent 60%
        );
        transform: translate(-50%, -50%) scale(0);
        opacity: 0;
        transition: transform 0.5s ease, opacity 0.3s ease;
      }

      .google-btn:hover {
        background: #f8f9fa;
        border-color: var(--accent);
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }

      .google-btn:active::after {
        transform: translate(-50%, -50%) scale(2);
        opacity: 1;
        transition: 0s;
      }

      .google-icon {
        width: 24px;
        height: 24px;
        margin-right: 12px;
        transition: transform 0.3s ease;
      }

      .google-btn:hover .google-icon {
        transform: scale(1.1) rotate(-5deg);
      }

      /* Login Section */
      .login-section {
        text-align: center;
        padding-top: 20px;
        border-top: 1px solid var(--accent);
      }

      .login-text {
        color: var(--white);
        margin-bottom: 15px;
        font-size: 0.9rem;
        text-transform: uppercase;
        letter-spacing: 0.5px;
      }

      .login-btn {
        background: var(--accent);
        color: var(--white);
        border: none;
        padding: 12px 30px;
        border-radius: 4px;
        font-size: 0.9rem;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        text-transform: uppercase;
        letter-spacing: 0.5px;
      }

      .login-btn:hover {
        background: #d67a4f;
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(236, 139, 90, 0.4);
      }

      /* Error Messages */
      .error-message {
        color: var(--error);
        font-size: 0.8rem;
        margin-top: 5px;
        display: none;
      }

      .error-message.show {
        display: block;
      }

      /* Footer */
      .footer {
        background: var(--primary);
        padding: 15px;
        text-align: center;
        height: 8px;
      }

      .footer-text {
        color: var(--white);
        font-size: 0.8rem;
      }

      /* Loading Spinner */
      .loading-spinner {
        display: none;
        width: 16px;
        height: 16px;
        border: 2px solid var(--primary);
        border-top: 2px solid transparent;
        border-radius: 50%;
        animation: spin 1s linear infinite;
        margin-left: 8px;
      }

      @keyframes spin {
        0% {
          transform: rotate(0deg);
        }
        100% {
          transform: rotate(360deg);
        }
      }

      /* Responsive */
      @media (max-width: 480px) {
        .register-card {
          padding: 40px 30px;
          margin: 20px;
        }

        .logo-text {
          font-size: 1.8rem;
        }

        .form-input {
          padding: 12px 15px;
        }

        .register-btn {
          padding: 15px;
        }
      }

      /* Accessibility */
      .sr-only {
        position: absolute;
        width: 1px;
        height: 1px;
        padding: 0;
        margin: -1px;
        overflow: hidden;
        clip: rect(0, 0, 0, 0);
        white-space: nowrap;
        border: 0;
      }

      .error-region {
        min-height: 18px;
      }
    </style>
  </head>
  <body>
    <!-- Header -->
    <header class="header"></header>

    <!-- Main Content -->
    <main class="main-content">
      <div class="register-card">
        <!-- Logo -->
        <div class="logo">
          <img
            src="${pageContext.request.contextPath}/images/logo/logo.png"
            alt="GymFit Logo"
            class="logo-image"
          />
        </div>

        <!-- Title -->
        <div class="page-title">ĐĂNG KÝ TÀI KHOẢN</div>

        <!-- Success/Error Messages -->
        <c:if test="${registerSuccess == true}">
          <div style="background: #d4edda; color: #155724; padding: 15px; border-radius: 4px; margin-bottom: 20px; border: 1px solid #c3e6cb;">
            <strong>Thành công!</strong> ${successMessage}
          </div>
        </c:if>

        <c:if test="${registerSuccess == false && errors != null}">
          <div style="background: #f8d7da; color: #721c24; padding: 15px; border-radius: 4px; margin-bottom: 20px; border: 1px solid #f5c6cb;">
            <strong>Lỗi đăng ký:</strong>
            <ul style="margin: 5px 0 0 20px;">
              <c:forEach var="error" items="${errors}">
                <li>${error}</li>
              </c:forEach>
            </ul>
          </div>
        </c:if>

        <!-- Register Form -->
        <form
          id="registerForm"
          method="post"
          action="${pageContext.request.contextPath}/auth?action=register"
        >
          <div class="form-group">
            <input
              type="text"
              id="username"
              name="username"
              class="form-input"
              placeholder="Tên đăng nhập"
              value="${username != null ? username : ''}"
              required
            />
            <div class="error-message" id="username-error"></div>
          </div>

          <div class="form-group">
            <input
              type="text"
              id="name"
              name="name"
              class="form-input"
              placeholder="Tên đầy đủ"
              value="${name != null ? name : ''}"
              required
            />
            <div class="error-message" id="name-error"></div>
          </div>

          <div class="form-group">
            <input
              type="email"
              id="email"
              name="email"
              class="form-input"
              placeholder="Email"
              value="${email != null ? email : ''}"
              required
            />
          </div>

          <div class="form-group">
            <input
              type="password"
              id="password"
              name="password"
              class="form-input"
              placeholder="Mật khẩu"
              required
            />
            <div class="error-message" id="password-error"></div>
          </div>

          <div class="form-group">
            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              class="form-input"
              placeholder="Xác nhận mật khẩu"
              required
            />
          </div>

          <!-- Register Button -->
          <button type="submit" class="register-btn" id="registerBtn">
            <span id="registerText">ĐĂNG KÝ</span>
            <div class="loading-spinner" id="loadingSpinner"></div>
          </button>
        </form>

        <!-- Google Register -->
        <div id="g_id_signin_register" style="margin-top: 10px;"></div>

        <!-- Login Section -->
        <div class="login-section">
          <div class="login-text">BẠN ĐÃ CÓ TÀI KHOẢN?</div>
          <a href="${pageContext.request.contextPath}/login" class="login-btn" style="display:inline-block; text-align:center;">
            ĐĂNG NHẬP
          </a>
        </div>
      </div>
    </main>

    <!-- Footer -->
    <footer class="footer">
      <div class="footer-text">
        FOOTER - nhưng cái này ngắn gọn hơn footer trên có thông tin cơ bản thôi
      </div>
    </footer>

    <script src="https://accounts.google.com/gsi/client" async defer></script>
    <script>
      document.addEventListener('DOMContentLoaded', function () {
        const form = document.getElementById('registerForm');
        const usernameInput = document.getElementById('username');
        const nameInput = document.getElementById('name');
        const passwordInput = document.getElementById('password');
        const registerBtn = document.getElementById('registerBtn');
        const registerText = document.getElementById('registerText');
        const loadingSpinner = document.getElementById('loadingSpinner');
        const usernameError = document.getElementById('username-error');
        const nameError = document.getElementById('name-error');
        const passwordError = document.getElementById('password-error');

        // Form validation
        function validateForm() {
          let isValid = true;

          // Clear previous errors
          usernameError.classList.remove('show');
          nameError.classList.remove('show');
          passwordError.classList.remove('show');

          // Validate username
          if (!usernameInput.value.trim()) {
            usernameError.textContent = 'Vui lòng nhập tên đăng nhập';
            usernameError.classList.add('show');
            isValid = false;
          }

          // Validate name
          if (!nameInput.value.trim()) {
            nameError.textContent = 'Vui lòng nhập tên đầy đủ';
            nameError.classList.add('show');
            isValid = false;
          } else if (nameInput.value.trim().length < 2) {
            nameError.textContent = 'Tên đầy đủ phải có ít nhất 2 ký tự';
            nameError.classList.add('show');
            isValid = false;
          }

          // Validate password
          if (!passwordInput.value.trim()) {
            passwordError.textContent = 'Vui lòng nhập mật khẩu';
            passwordError.classList.add('show');
            isValid = false;
          }

          return isValid;
        }

        // Form submission
        form.addEventListener('submit', function (e) {
          if (!validateForm()) {
            e.preventDefault();
            return;
          }

          // Show loading state
          registerBtn.disabled = true;
          registerText.style.display = 'none';
          loadingSpinner.style.display = 'inline-block';

          // Form will submit normally to backend
        });

        // Real-time validation
        usernameInput.addEventListener('blur', function () {
          if (!this.value.trim()) {
            usernameError.textContent = 'Vui lòng nhập tên đăng nhập';
            usernameError.classList.add('show');
          } else {
            usernameError.classList.remove('show');
          }
        });

        nameInput.addEventListener('blur', function () {
          if (!this.value.trim()) {
            nameError.textContent = 'Vui lòng nhập tên đầy đủ';
            nameError.classList.add('show');
          } else if (this.value.trim().length < 2) {
            nameError.textContent = 'Tên đầy đủ phải có ít nhất 2 ký tự';
            nameError.classList.add('show');
          } else {
            nameError.classList.remove('show');
          }
        });

        passwordInput.addEventListener('blur', function () {
          if (!this.value.trim()) {
            passwordError.textContent = 'Vui lòng nhập mật khẩu';
            passwordError.classList.add('show');
          } else {
            passwordError.classList.remove('show');
          }
        });

        // Clear errors on input
        usernameInput.addEventListener('input', function () {
          usernameError.classList.remove('show');
        });

        nameInput.addEventListener('input', function () {
          nameError.classList.remove('show');
        });

        passwordInput.addEventListener('input', function () {
          passwordError.classList.remove('show');
        });

        // Initialize Google Sign-In (Register)
        function initializeGoogleRegister() {
          var clientId = '${googleClientId}';
          console.log('[Google Register] Client ID:', clientId ? 'SET' : 'NOT SET');
          
          if (!clientId || clientId.trim() === '' || clientId === 'YOUR_GOOGLE_CLIENT_ID_HERE' || clientId === 'null') {
            console.warn('[Google Register] Google Client ID is not configured');
            var el = document.getElementById('g_id_signin_register');
            if (el) {
              el.innerHTML = '<p style="color: #ff6b6b; font-size: 0.85rem; text-align: center;">Google Sign-In chưa được cấu hình</p>';
            }
            return;
          }
          
          // Check if Google API is loaded
          if (typeof google === 'undefined' || !google.accounts || !google.accounts.id) {
            console.log('[Google Register] Google API not loaded yet, retrying...');
            setTimeout(initializeGoogleRegister, 500);
            return;
          }
          
          try {
            google.accounts.id.initialize({
              client_id: clientId,
              callback: function (response) {
              fetch('${pageContext.request.contextPath}/auth/google-register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ credential: response.credential })
              })
                .then(function (r) { return r.json(); })
                .then(function (data) {
                  if (data && data.success) {
                    window.location.href = data.redirectUrl;
                  } else {
                    alert('Đăng ký Google thất bại' + (data && data.message ? ': ' + data.message : ''));
                  }
                })
                .catch(function (err) { console.error(err); alert('Có lỗi xảy ra khi đăng ký Google'); });
            },
            auto_select: false,
            cancel_on_tap_outside: true
          });
              var el = document.getElementById('g_id_signin_register');
              if (el) {
                google.accounts.id.renderButton(el, { 
                  theme: 'outline', 
                  size: 'large', 
                  shape: 'pill', 
                  text: 'continue_with' 
                });
                console.log('[Google Register] Button rendered successfully');
              } else {
                console.error('[Google Register] Element g_id_signin_register not found');
              }
            } catch (error) {
              console.error('[Google Register] Initialization error:', error);
              var el = document.getElementById('g_id_signin_register');
              if (el) {
                el.innerHTML = '<p style="color: #ff6b6b; font-size: 0.85rem; text-align: center;">Lỗi khởi tạo Google Sign-In</p>';
              }
            }
          }
          
          // Wait for Google script to load
          function waitForGoogleAPI() {
            if (typeof google !== 'undefined' && google.accounts && google.accounts.id) {
              initializeGoogleRegister();
            } else {
              setTimeout(waitForGoogleAPI, 100);
            }
          }
          
          // Start initialization when DOM is ready
          if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', function() {
              setTimeout(waitForGoogleAPI, 500);
            });
          } else {
            setTimeout(waitForGoogleAPI, 500);
          }

        // Login button handled by anchor link (no JS redirect to avoid delay)

        // Keyboard navigation
        document.addEventListener('keydown', function (e) {
          if (e.key === 'Enter' && e.target.tagName !== 'BUTTON') {
            const inputs = [usernameInput, nameInput, passwordInput];
            const currentIndex = inputs.indexOf(e.target);
            if (currentIndex < inputs.length - 1) {
              inputs[currentIndex + 1].focus();
            } else {
              form.dispatchEvent(new Event('submit'));
            }
          }
        });
      });
    </script>
  </body>
</html>
