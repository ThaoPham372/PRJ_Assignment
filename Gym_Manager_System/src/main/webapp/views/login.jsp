<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng Nhập - GymFit</title>

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
        font-family: 'Arial', sans-serif;
        background: var(--gray-bg);
        background-image: radial-gradient(circle, #ddd 1px, transparent 1px);
        background-size: 20px 20px;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
        color: var(--white);
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
        border-radius: 25px;
        padding: 50px 40px;
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
        width: 100%;
        max-width: 420px;
        border: 2px solid var(--primary);
      }

      /* Logo */
      .logo {
        text-align: center;
        margin-bottom: 40px;
      }

      .logo-text {
        font-size: 2.2rem;
        font-weight: 900;
        color: var(--accent);
        text-transform: lowercase;
        letter-spacing: 1px;
      }

      /* Form */
      .form-group {
        margin-bottom: 20px;
      }

      .form-input {
        width: 100%;
        padding: 15px 20px;
        border: 1px solid var(--border);
        border-radius: 8px;
        font-size: 1rem;
        background: var(--white);
        color: var(--text-dark);
        transition: all 0.3s ease;
      }

      .form-input:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 2px rgba(236, 139, 90, 0.2);
      }

      .form-input::placeholder {
        color: var(--text-light);
      }

      /* Checkbox */
      .checkbox-group {
        display: flex;
        align-items: center;
        margin-bottom: 25px;
      }

      .checkbox-input {
        margin-right: 10px;
        transform: scale(1.1);
      }

      .checkbox-label {
        color: var(--white);
        font-size: 0.9rem;
        cursor: pointer;
      }

      /* Login Button */
      .login-btn {
        width: 100%;
        background: var(--support);
        color: var(--primary);
        border: none;
        padding: 18px;
        border-radius: 8px;
        font-size: 1rem;
        font-weight: 700;
        cursor: pointer;
        transition: all 0.3s ease;
        margin-bottom: 15px;
        text-transform: uppercase;
        letter-spacing: 1px;
      }

      .login-btn:hover:not(:disabled) {
        background: #f4d03f;
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(255, 222, 89, 0.4);
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
        background: var(--white);
        color: var(--text-dark);
        border: 1px solid var(--border);
        padding: 15px;
        border-radius: 8px;
        font-size: 0.9rem;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        margin-bottom: 25px;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        text-transform: uppercase;
        letter-spacing: 0.5px;
      }

      .google-btn:hover {
        background: #f8f9fa;
        border-color: var(--accent);
        transform: translateY(-1px);
      }

      .google-icon {
        width: 18px;
        height: 18px;
        background: conic-gradient(
          from 0deg,
          #ea4335,
          #fbbc05,
          #34a853,
          #4285f4,
          #ea4335
        );
        border-radius: 50%;
        display: inline-block;
      }

      /* Register Section */
      .register-section {
        text-align: center;
        padding-top: 20px;
        border-top: 1px solid var(--accent);
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
        border-radius: 8px;
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
          <div class="logo-text">logo</div>
        </div>

        <!-- Login Form -->
        <form
          id="loginForm"
          method="post"
          action="${pageContext.request.contextPath}/login"
        >
          <!-- Username -->
          <div class="form-group">
            <label for="username" class="sr-only">User Name</label>
            <input
              type="text"
              id="username"
              name="username"
              class="form-input"
              placeholder="user name"
              required
              aria-describedby="username-error"
            />
            <div
              id="username-error"
              class="error-message error-region"
              role="alert"
              aria-live="polite"
            ></div>
          </div>

          <!-- Password -->
          <div class="form-group">
            <label for="password" class="sr-only">Mật khẩu</label>
            <input
              type="password"
              id="password"
              name="password"
              class="form-input"
              placeholder="mật khẩu"
              required
              aria-describedby="password-error"
            />
            <div
              id="password-error"
              class="error-message error-region"
              role="alert"
              aria-live="polite"
            ></div>
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
              >ghi nhớ đăng nhập</label
            >
          </div>

          <!-- Login Button -->
          <button type="submit" class="login-btn" id="loginBtn">
            <span id="loginText">ĐĂNG NHẬP</span>
            <div class="loading-spinner" id="loadingSpinner"></div>
          </button>
        </form>

        <!-- Forgot Password -->
        <div class="forgot-link">
          <a href="#" id="forgotPassword">quên mật khẩu?</a>
        </div>

        <!-- Google Login -->
        <button type="button" class="google-btn" id="googleBtn">
          <div class="google-icon"></div>
          TIẾP TỤC VỚI GOOGLE
        </button>

        <!-- Register Section -->
        <div class="register-section">
          <div class="register-text">BẠN CHƯA CÓ TÀI KHOẢN?</div>
          <button type="button" class="register-btn" id="registerBtn">
            ĐĂNG KÝ
          </button>
        </div>
      </div>
    </main>

    <!-- Footer -->
    <footer class="footer">
      <div class="footer-text">
        FOOTER - nhưng cái này ngắn gọn hơn footer trên có thông tin cơ bản thôi
      </div>
    </footer>

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
          e.preventDefault();

          if (!validateForm()) {
            return;
          }

          // Show loading state
          loginBtn.disabled = true;
          loginText.style.display = 'none';
          loadingSpinner.style.display = 'inline-block';

          // Simulate form submission
          setTimeout(() => {
            // Reset button state
            loginBtn.disabled = false;
            loginText.style.display = 'inline';
            loadingSpinner.style.display = 'none';

            // In real application, form would submit here
            // form.submit();

            // For demo purposes, show success message
            alert('Đăng nhập thành công! (Demo)');
          }, 2000);
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

        // Google login button
        document
          .getElementById('googleBtn')
          .addEventListener('click', function () {
            alert('Chức năng đăng nhập Google sẽ được triển khai');
          });

        // Register button
        document
          .getElementById('registerBtn')
          .addEventListener('click', function () {
            alert('Chức năng đăng ký sẽ được triển khai');
          });

        // Forgot password link
        document
          .getElementById('forgotPassword')
          .addEventListener('click', function (e) {
            e.preventDefault();
            alert('Chức năng quên mật khẩu sẽ được triển khai');
          });

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
