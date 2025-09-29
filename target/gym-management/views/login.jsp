<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng Nhập - Gym Management</title>

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
      body {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        min-height: 100vh;
        display: flex;
        align-items: center;
      }

      .login-card {
        background: rgba(255, 255, 255, 0.95);
        backdrop-filter: blur(10px);
        border-radius: 20px;
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
        overflow: hidden;
      }

      .login-header {
        background: linear-gradient(135deg, #2c3e50, #3498db);
        color: white;
        padding: 40px;
        text-align: center;
      }

      .login-form {
        padding: 40px;
      }

      .form-control {
        border-radius: 15px;
        border: 2px solid #e0e0e0;
        padding: 15px 20px;
        font-size: 16px;
        transition: all 0.3s ease;
      }

      .form-control:focus {
        border-color: #3498db;
        box-shadow: 0 0 0 0.2rem rgba(52, 152, 219, 0.25);
      }

      .btn-login {
        background: linear-gradient(135deg, #3498db, #2c3e50);
        border: none;
        border-radius: 15px;
        padding: 15px;
        font-size: 16px;
        font-weight: 600;
        color: white;
        transition: all 0.3s ease;
      }

      .btn-login:hover {
        transform: translateY(-2px);
        box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
        color: white;
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
        background: rgba(255, 255, 255, 0.1);
        border-radius: 50%;
        animation: float 6s ease-in-out infinite;
      }

      .shape:nth-child(1) {
        width: 80px;
        height: 80px;
        top: 20%;
        left: 10%;
        animation-delay: 0s;
      }

      .shape:nth-child(2) {
        width: 120px;
        height: 120px;
        top: 60%;
        right: 15%;
        animation-delay: 2s;
      }

      .shape:nth-child(3) {
        width: 60px;
        height: 60px;
        bottom: 20%;
        left: 20%;
        animation-delay: 4s;
      }

      @keyframes float {
        0%,
        100% {
          transform: translateY(0px);
        }
        50% {
          transform: translateY(-20px);
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
              <h2 class="fw-bold">Gym Manager</h2>
              <p class="mb-0 opacity-75">Đăng nhập vào hệ thống</p>
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
                  href="${pageContext.request.contextPath}/views/home.jsp"
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
