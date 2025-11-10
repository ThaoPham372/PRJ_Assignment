<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@taglib prefix= "c" uri="http://java.sun.com/jsp/jstl/core" %> 

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng Nhập - GymFit</title>

    <!-- Font Awesome -->
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
    />

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

      .login-card {
        background: var(--primary);
        border-radius: 15px;
        padding: 40px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        width: 100%;
        max-width: 400px;
        border: none;
        position: relative;
        animation: fadeInUp 0.6s ease-out;
        overflow: hidden;
        
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
        margin-bottom: 40px;
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

      /* Form */
      .form-group {
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
        transform-origin: top;
        animation: formIn 0.6s ease-out backwards;
      }

      .form-input:nth-child(1) {
        animation-delay: 0.2s;
      }
      .form-input:nth-child(2) {
        animation-delay: 0.4s;
      }

      @keyframes formIn {
        from {
          opacity: 0;
          transform: translateX(-20px);
        }
        to {
          opacity: 1;
          transform: translateX(0);
        }
      }

      .form-input:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 2px rgba(236, 139, 90, 0.1);
        transform: translateY(-2px);
      }

      .form-input::placeholder {
        color: var(--text-light);
        text-transform: lowercase;
        font-size: 0.9rem;
        transition: all 0.3s ease;
      }

      .form-input:focus::placeholder {
        opacity: 0.7;
        transform: translateX(5px);
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

      /* Login Button */
      .login-btn {
        width: 100%;
        background: var(--support);
        color: var(--primary);
        border: none;
        padding: 12px;
        border-radius: 4px;
        font-size: 0.9rem;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        margin: 15px 0;
        text-transform: uppercase;
        letter-spacing: 0.5px;
      }

      .login-btn:hover:not(:disabled) {
        background: #f4d03f;
        transform: translateY(-1px);
        box-shadow: 0 4px 8px rgba(255, 222, 89, 0.4);
      }

      .login-btn:disabled {
        opacity: 0.7;
        cursor: not-allowed;
        transform: none;
      }

      /* Forgot Password Link */
      .forgot-link {
        text-align: center;
        margin-bottom: 25px;
      }

      .forgot-link a {
        color: var(--white);
        text-decoration: none;
        font-size: 0.9rem;
        transition: color 0.3s ease;
      }

      .forgot-link a:hover {
        color: var(--accent);
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

      /* Register Section */
      .register-section {
        text-align: center;
        padding-top: 20px;
        
      }

      .register-text {
        color: var(--white);
        margin-bottom: 15px;
        font-size: 0.9rem;
        text-transform: uppercase;
        letter-spacing: 0.5px;
      }

      .register-btn {
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

      .register-btn:hover {
        background: #d67a4f;
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(236, 139, 90, 0.4);
      }

      /* Error Messages */
      .error-message {
        color: #ff6b6b;
        font-size: 0.8rem;
        margin-top: 8px;
        padding: 8px;
        border-radius: 4px;
        background: rgba(255, 107, 107, 0.1);
        border-left: 3px solid #ff6b6b;
        display: none;
        animation: fadeIn 0.3s ease;
      }

      .error-message.show {
        display: block;
      }

      .success-message {
        background: #d4edda;
        color: #155724;
        padding: 15px;
        border-radius: 8px;
        margin-bottom: 20px;
        border: 1px solid #c3e6cb;
        border-left: 4px solid #28a745;
        display: flex;
        align-items: center;
        gap: 10px;
        animation: slideDown 0.5s ease-out;
      }

      .success-message i {
        font-size: 1.2rem;
      }

      @keyframes slideDown {
        from {
          opacity: 0;
          transform: translateY(-20px);
        }
        to {
          opacity: 1;
          transform: translateY(0);
        }
      }

      @keyframes fadeIn {
        from {
          opacity: 0;
          transform: translateY(-5px);
        }
        to {
          opacity: 1;
          transform: translateY(0);
        }
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
        width: 20px;
        height: 20px;
        border: 2px solid rgba(255, 255, 255, 0.3);
        border-top: 2px solid white;
        border-radius: 50%;
        animation: spin 0.8s linear infinite;
        margin-left: 10px;
      }

      @keyframes spin {
        0% {
          transform: rotate(0deg);
        }
        100% {
          transform: rotate(360deg);
        }
      }

      .loading-spinner::before {
        content: '';
        position: absolute;
        top: -4px;
        left: -4px;
        right: -4px;
        bottom: -4px;
        border-radius: 50%;
        border: 2px solid transparent;
        animation: spin 1.5s linear infinite;
      }

      /* Responsive */
      @media (max-width: 480px) {
        .login-card {
          padding: 40px 30px;
          margin: 20px;
        }

        .logo-text {
          font-size: 1.8rem;
        }

        .form-input {
          padding: 12px 15px;
        }

        .login-btn {
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
      <div class="login-card">
        <!-- Logo -->
        <div class="logo">
          <img
            src="${pageContext.request.contextPath}/images/logo/logo.png"
            alt="GymFit Logo"
            class="logo-image"
          />
        </div>

        <!-- Success Message -->
        <c:if test="${not empty sessionScope.loginSuccessMessage}">
          <div class="success-message">
            <i class="fas fa-check-circle"></i>
            <span>${sessionScope.loginSuccessMessage}</span>
          </div>
          <c:remove var="loginSuccessMessage" scope="session" />
        </c:if>

        <!-- Error Messages -->
        <c:if test="${loginError == true && errors != null}">
          <div
            style="
              background: #f8d7da;
              color: #721c24;
              padding: 15px;
              border-radius: 4px;
              margin-bottom: 20px;
              border: 1px solid #f5c6cb;
            "
          >
            <strong>Lỗi đăng nhập:</strong>
            <ul style="margin: 5px 0 0 20px">
              <c:forEach var="error" items="${errors}">
                <li>${error}</li>
              </c:forEach>
            </ul>
          </div>
        </c:if>

        <!-- Login Form -->
        <form
          id="loginForm"
          method="post"
          action="${pageContext.request.contextPath}/login"
        >
          <!-- Username -->
          <div class="form-group">
            <input
              type="text"
              id="username"
              name="username"
              class="form-input"
              placeholder="user name"
              value="${username}"
              required
            />
            <div id="username-error" class="error-message"></div>
          </div>

          <!-- Password -->
          <div class="form-group">
            <input
              type="password"
              id="password"
              name="password"
              class="form-input"
              placeholder="mật khẩu"
              required
            />
            <div id="password-error" class="error-message"></div>
          </div>

          <!-- Remember Me -->
          <div class="checkbox-group">
            <input
              type="checkbox"
              id="rememberMe"
              name="rememberMe"
              class="checkbox-input"
            />
            <label for="rememberMe" class="checkbox-label"
              >Ghi nhớ đăng nhập</label
            >
          </div>

          <!-- Login Button -->
          <button type="submit" class="login-btn" id="loginBtn">
            ĐĂNG NHẬP
          </button>
        </form>

        <!-- Forgot Password -->
        <div class="forgot-link">
          <a href="${pageContext.request.contextPath}/auth/forgot-password"
            >Quên mật khẩu?</a
          >
        </div>

        <!-- Google Login -->
        <div id="g_id_signin_login" style="margin-top: 10px"></div>

        <!-- Register Section -->
        <div class="register-section">
          <div class="register-text">BẠN CHƯA CÓ TÀI KHOẢN?</div>
          <a
            href="${pageContext.request.contextPath}/register"
            class="register-btn" 
            style="display: inline-block; text-align: center; text-decoration: none;"
          > 
            ĐĂNG KÝ
          </a>
        </div>
      </div>
    </main>

    <script src="https://accounts.google.com/gsi/client" async defer></script>
    <script>
      document.addEventListener('DOMContentLoaded', function () {
        const form = document.getElementById('loginForm');
        const usernameInput = document.getElementById('username');
        const passwordInput = document.getElementById('password');
        const loginBtn = document.getElementById('loginBtn');
        const loginText = document.getElementById('loginText');
        const loadingSpinner = document.getElementById('loadingSpinner');
        const usernameError = document.getElementById('username-error');
        const passwordError = document.getElementById('password-error');

        // Form validation
        function validateForm() {
          let isValid = true;

          // Clear previous errors
          usernameError.classList.remove('show');
          passwordError.classList.remove('show');

          // Validate username
          if (!usernameInput.value.trim()) {
            usernameError.textContent = 'Vui lòng nhập tên đăng nhập';
            usernameError.classList.add('show');
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

          // Show loading state and let the form submit to backend
          loginBtn.disabled = true;
          if (loginText) loginText.style.display = 'none';
          if (loadingSpinner) loadingSpinner.style.display = 'inline-block';
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

        passwordInput.addEventListener('input', function () {
          passwordError.classList.remove('show');
        });

        // Initialize Google Sign-In
        function initializeGoogleSignIn() {
          var clientId = '${googleClientId}';
          console.log(
            '[Google Sign-In] Client ID:',
            clientId ? 'SET' : 'NOT SET',
          );

          if (
            !clientId ||
            clientId.trim() === '' ||
            clientId === 'YOUR_GOOGLE_CLIENT_ID_HERE' ||
            clientId === 'null'
          ) {
            console.warn('[Google Sign-In] Google Client ID is not configured');
            var el = document.getElementById('g_id_signin_login');
            if (el) {
              el.innerHTML =
                '<p style="color: #ff6b6b; font-size: 0.85rem; text-align: center;">Google Sign-In chưa được cấu hình</p>';
            }
            return;
          }

          // Check if Google API is loaded
          if (
            typeof google === 'undefined' ||
            !google.accounts ||
            !google.accounts.id
          ) {
            console.log(
              '[Google Sign-In] Google API not loaded yet, retrying...',
            );
            setTimeout(initializeGoogleSignIn, 500);
            return;
          }

          try {
            google.accounts.id.initialize({
              client_id: clientId,
              callback: function (response) {
                console.log('[Google Sign-In] Callback received');
                fetch('${pageContext.request.contextPath}/auth/google-login', {
                  method: 'POST',
                  headers: { 'Content-Type': 'application/json' },
                  body: JSON.stringify({ credential: response.credential }),
                })
                  .then(function (r) {
                    return r.json();
                  })
                  .then(function (data) {
                    if (data && data.success) {
                      window.location.href = data.redirectUrl;
                    } else {
                      alert(
                        'Đăng nhập Google thất bại' +
                          (data && data.message ? ': ' + data.message : ''),
                      );
                    }
                  })
                  .catch(function (err) {
                    console.error('[Google Sign-In] Error:', err);
                    alert('Có lỗi xảy ra khi đăng nhập Google');
                  });
              },
              auto_select: false,
              cancel_on_tap_outside: true,
            });

            var el = document.getElementById('g_id_signin_login');
            if (el) {
              google.accounts.id.renderButton(el, {
                theme: 'outline',
                size: 'large',
                shape: 'pill',
                text: 'continue_with',
              });
              console.log('[Google Sign-In] Button rendered successfully');
            } else {
              console.error(
                '[Google Sign-In] Element g_id_signin_login not found',
              );
            }
          } catch (error) {
            console.error('[Google Sign-In] Initialization error:', error);
            var el = document.getElementById('g_id_signin_login');
            if (el) {
              el.innerHTML =
                '<p style="color: #ff6b6b; font-size: 0.85rem; text-align: center;">Lỗi khởi tạo Google Sign-In</p>';
            }
          }
        }

        // Wait for Google script to load
        function waitForGoogleAPI() {
          if (
            typeof google !== 'undefined' &&
            google.accounts &&
            google.accounts.id
          ) {
            initializeGoogleSignIn();
          } else {
            setTimeout(waitForGoogleAPI, 100);
          }
        }

        // Start initialization when DOM is ready
        if (document.readyState === 'loading') {
          document.addEventListener('DOMContentLoaded', function () {
            setTimeout(waitForGoogleAPI, 500);
          });
        } else {
          setTimeout(waitForGoogleAPI, 500);
        }

        // Register button handled by anchor link (no JS redirect to avoid delay)

        // Forgot password link - handled by href, no JS needed

        // Keyboard navigation
        document.addEventListener('keydown', function (e) {
          if (e.key === 'Enter' && e.target.tagName !== 'BUTTON') {
            const inputs = [usernameInput, passwordInput];
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
