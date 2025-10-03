<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng Nhập - Stamina Gym</title>

    <!-- Bootstrap CSS -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />

    <!-- Font Awesome -->
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
      rel="stylesheet"
    />

    <!-- Custom CSS -->
    <link
      href="${pageContext.request.contextPath}/css/styles.css"
      rel="stylesheet"
    />

    <style>
      /* Override CSS with Stamina Gym theme for this specific page */
      body {
        background: linear-gradient(
          135deg,
          var(--primary-purple) 0%,
          var(--secondary-purple) 100%
        );
        min-height: 100vh;
        display: flex;
        align-items: center;
        font-family: 'Poppins', sans-serif;
      }

      .login-card {
        background: var(--white);
        backdrop-filter: blur(10px);
        border-radius: 30px;
        box-shadow: 0 30px 60px rgba(59, 30, 120, 0.2);
        overflow: hidden;
        max-width: 450px;
      }

      .login-header {
        background: linear-gradient(
          135deg,
          var(--primary-purple),
          var(--secondary-purple)
        );
        color: var(--white);
        padding: 50px 40px;
        text-align: center;
        position: relative;
        overflow: hidden;
      }

      .login-header::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="login-pattern" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="25" cy="25" r="2" fill="%23FFD700" opacity="0.2"/><circle cx="75" cy="75" r="2" fill="%23FFD700" opacity="0.2"/></pattern></defs><rect width="100" height="100" fill="url(%23login-pattern)"/></svg>');
      }

      .login-header h1,
      .login-header h2,
      .login-header p {
        position: relative;
        z-index: 2;
        color: var(--white);
      }

      .login-header .fa-dumbbell {
        color: var(--accent-yellow);
      }

      .login-form {
        padding: 50px 40px;
      }

      .form-control {
        border-radius: 20px;
        border: 2px solid var(--gray-medium);
        padding: 18px 25px;
        font-size: 16px;
        font-family: 'Poppins', sans-serif;
        transition: all 0.3s ease;
        background-color: var(--white);
      }

      .form-control:focus {
        border-color: var(--primary-purple);
        box-shadow: 0 0 0 0.2rem rgba(59, 30, 120, 0.25);
        outline: none;
      }

      .form-label {
        font-family: 'Poppins', sans-serif;
        font-weight: 600;
        color: var(--primary-purple);
        margin-bottom: 10px;
      }

      .btn-login {
        background: linear-gradient(
          135deg,
          var(--primary-purple),
          var(--secondary-purple)
        );
        border: none;
        border-radius: 25px;
        padding: 18px;
        font-size: 16px;
        font-weight: 600;
        color: var(--white);
        font-family: 'Poppins', sans-serif;
        transition: all 0.3s ease;
      }

      .btn-login:hover {
        transform: translateY(-3px);
        box-shadow: 0 15px 30px rgba(59, 30, 120, 0.3);
        color: var(--white);
      }

      .btn-outline-danger {
        border-color: #dc3545;
        color: #dc3545;
        border-radius: 20px;
        padding: 12px;
        font-weight: 500;
        transition: all 0.3s ease;
      }

      .btn-outline-danger:hover {
        background-color: #dc3545;
        transform: translateY(-2px);
      }

      .btn-outline-primary {
        border-color: var(--primary-purple);
        color: var(--primary-purple);
        border-radius: 20px;
        padding: 12px;
        font-weight: 500;
        transition: all 0.3s ease;
      }

      .btn-outline-primary:hover {
        background-color: var(--primary-purple);
        transform: translateY(-2px);
      }

      .btn-outline-secondary {
        border-color: var(--primary-purple);
        color: var(--primary-purple);
        border-radius: 0 20px 20px 0;
        transition: all 0.3s ease;
      }

      .btn-outline-secondary:hover {
        background-color: var(--primary-purple);
        border-color: var(--primary-purple);
        color: var(--white);
      }

      .input-group .form-control {
        border-radius: 20px 0 0 20px;
      }

      .bg-light {
        background: linear-gradient(
          135deg,
          var(--gray-light),
          #f8f9fa
        ) !important;
        border: 2px solid var(--accent-yellow);
        border-radius: 15px;
      }

      .text-muted {
        color: var(--gray-dark) !important;
      }

      .form-check-input:checked {
        background-color: var(--primary-purple);
        border-color: var(--primary-purple);
      }

      .floating-shapes {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        pointer-events: none;
        z-index: -1;
      }

      .shape {
        position: absolute;
        background: rgba(255, 215, 0, 0.1);
        border-radius: 50%;
        animation: float 8s ease-in-out infinite;
      }

      .shape:nth-child(1) {
        width: 100px;
        height: 100px;
        top: 15%;
        left: 8%;
        animation-delay: 0s;
      }

      .shape:nth-child(2) {
        width: 150px;
        height: 150px;
        top: 60%;
        right: 10%;
        animation-delay: 3s;
      }

      .shape:nth-child(3) {
        width: 80px;
        height: 80px;
        bottom: 15%;
        left: 15%;
        animation-delay: 6s;
      }

      .shape:nth-child(4) {
        width: 60px;
        height: 60px;
        top: 40%;
        right: 30%;
        animation-delay: 1.5s;
      }

      @keyframes float {
        0%,
        100% {
          transform: translateY(0px) rotate(0deg);
        }
        25% {
          transform: translateY(-30px) rotate(90deg);
        }
        50% {
          transform: translateY(-15px) rotate(180deg);
        }
        75% {
          transform: translateY(-25px) rotate(270deg);
        }
      }
    </style>
  </head>
  <body>
    <!-- Floating Shapes Background -->
    <div class="floating-shapes">
      <div class="shape"></div>
      <div class="shape"></div>
      <div class="shape"></div>
      <div class="shape"></div>
    </div>

    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
          <div class="login-card">
            <!-- Header -->
            <div class="login-header">
              <h1 class="mb-3">
                <i class="fas fa-dumbbell fa-2x"></i>
              </h1>
              <h2 class="fw-bold">STAMINA GYM</h2>
            </div>

            <!-- Login Form -->
            <div class="login-form">
              <!-- Error Message -->
              <c:if test="${not empty error}">
                <div
                  class="alert alert-danger alert-dismissible fade show"
                  role="alert"
                >
                  <i class="fas fa-exclamation-circle me-2"></i>
                  ${error}
                  <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert"
                  ></button>
                </div>
              </c:if>

              <!-- Success Message -->
              <c:if test="${not empty success}">
                <div
                  class="alert alert-success alert-dismissible fade show"
                  role="alert"
                >
                  <i class="fas fa-check-circle me-2"></i>
                  ${success}
                  <button
                    type="button"
                    class="btn-close"
                    data-bs-dismiss="alert"
                  ></button>
                </div>
              </c:if>

              <form
                action="${pageContext.request.contextPath}/login"
                method="post"
                id="loginForm"
              >
                <!-- Username -->
                <div class="mb-4">
                  <label for="username" class="form-label fw-bold">
                    <i class="fas fa-user me-2"></i>Tên đăng nhập
                  </label>
                  <input
                    type="text"
                    class="form-control"
                    id="username"
                    name="username"
                    placeholder="Nhập tên đăng nhập"
                    value="${cookie.rememberedUsername.value}"
                    required
                  />
                </div>

                <!-- Password -->
                <div class="mb-4">
                  <label for="password" class="form-label fw-bold">
                    <i class="fas fa-lock me-2"></i>Mật khẩu
                  </label>
                  <div class="input-group">
                    <input
                      type="password"
                      class="form-control"
                      id="password"
                      name="password"
                      placeholder="Nhập mật khẩu"
                      value="${cookie.rememberedPassword.value}"
                      required
                    />
                    <button
                      class="btn btn-outline-secondary"
                      type="button"
                      id="togglePassword"
                    >
                      <i class="fas fa-eye"></i>
                    </button>
                  </div>
                </div>

                <!-- Remember Me -->
                <div class="mb-4">
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox"
                    id="rememberMe" name="rememberMe" ${not empty
                    cookie.rememberedUsername ? 'checked' : ''}>
                    <label class="form-check-label" for="rememberMe">
                      Ghi nhớ đăng nhập
                    </label>
                  </div>
                </div>

                <!-- Login Button -->
                <button type="submit" class="btn btn-login w-100 mb-4">
                  <i class="fas fa-sign-in-alt me-2"></i>
                  <span id="loginText">Đăng Nhập</span>
                  <span id="loginSpinner" class="loading d-none"></span>
                </button>
              </form>

              <!-- Divider -->
              <div class="text-center mb-4">
                <hr class="my-4" />
                <span class="text-muted bg-white px-3">Hoặc đăng nhập với</span>
              </div>

              <!-- Social Login -->
              <div class="row">
                <div class="col-6">
                  <a
                    href="${pageContext.request.contextPath}/auth/google"
                    class="btn btn-outline-danger w-100"
                  >
                    <i class="fab fa-google me-2"></i>Google
                  </a>
                </div>
                <div class="col-6">
                  <a
                    href="${pageContext.request.contextPath}/auth/facebook"
                    class="btn btn-outline-primary w-100"
                  >
                    <i class="fab fa-facebook me-2"></i>Facebook
                  </a>
                </div>
              </div>

              <!-- Demo Accounts -->
              <div class="mt-4 p-3 bg-light rounded">
                <h6 class="fw-bold mb-2">
                  <i class="fas fa-info-circle text-info me-2"></i>Tài khoản
                  demo:
                </h6>
                <small class="text-muted">
                  <strong>Manager:</strong> admin/admin123<br />
                  <strong>Employee:</strong> employee/emp123<br />
                  <strong>Customer:</strong> customer/cust123
                </small>
              </div>

              <!-- Links -->
              <div class="text-center mt-4">
                <a
                  href="${pageContext.request.contextPath}/forgot-password"
                  class="text-decoration-none"
                >
                  Quên mật khẩu?
                </a>
                <span class="mx-2">|</span>
                <a
                  href="${pageContext.request.contextPath}/index.jsp"
                  class="text-decoration-none"
                >
                  Về trang chủ
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
      document.addEventListener('DOMContentLoaded', function () {
        // Toggle password visibility
        const togglePassword = document.getElementById('togglePassword');
        const passwordInput = document.getElementById('password');

        togglePassword.addEventListener('click', function () {
          const type =
            passwordInput.getAttribute('type') === 'password'
              ? 'text'
              : 'password';
          passwordInput.setAttribute('type', type);

          const icon = this.querySelector('i');
          icon.classList.toggle('fa-eye');
          icon.classList.toggle('fa-eye-slash');
        });

        // Form submission with loading state
        const loginForm = document.getElementById('loginForm');
        const loginText = document.getElementById('loginText');
        const loginSpinner = document.getElementById('loginSpinner');
        const submitButton = loginForm.querySelector('button[type="submit"]');

        loginForm.addEventListener('submit', function (e) {
          // Show loading state
          submitButton.disabled = true;
          loginText.classList.add('d-none');
          loginSpinner.classList.remove('d-none');

          // For demo purposes, simulate server delay
          // In real app, form will submit normally
          setTimeout(() => {
            // Reset button state after demo delay
            submitButton.disabled = false;
            loginText.classList.remove('d-none');
            loginSpinner.classList.add('d-none');
          }, 1000);
        });

        // Demo account quick fill
        document.addEventListener('click', function (e) {
          if (e.target.closest('.bg-light')) {
            const text = e.target.textContent;
            if (text.includes('admin/admin123')) {
              document.getElementById('username').value = 'admin';
              document.getElementById('password').value = 'admin123';
            } else if (text.includes('employee/emp123')) {
              document.getElementById('username').value = 'employee';
              document.getElementById('password').value = 'emp123';
            } else if (text.includes('customer/cust123')) {
              document.getElementById('username').value = 'customer';
              document.getElementById('password').value = 'cust123';
            }
          }
        });

        // Auto-hide alerts
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach((alert) => {
          setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
          }, 5000);
        });
      });
    </script>
  </body>
</html>
