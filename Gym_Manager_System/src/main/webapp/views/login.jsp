<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập - GymFit</title>
    
    <!-- Font Awesome Icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet">

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
            --gradient-bg: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        * {
            box-sizing: border-box;
        margin: 0;
        padding: 0;
      }

      body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: var(--gradient-bg);
        min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: var(--text);
            line-height: 1.6;
        }
        
        .login-container {
            background: var(--card);
            border-radius: 20px;
            box-shadow: 0 20px 40px var(--shadow);
            overflow: hidden;
            max-width: 1000px;
            width: 100%;
            margin: 20px;
            display: flex;
            min-height: 600px;
        }
        
        .login-left {
            background: var(--gradient-primary);
            color: white;
            padding: 60px 40px;
        display: flex;
        flex-direction: column;
            justify-content: center;
            align-items: center;
            text-align: center;
            flex: 1;
            position: relative;
            overflow: hidden;
        }
        
        .login-left::before {
            content: '';
            position: absolute;
            top: -50%;
            right: -50%;
            width: 200%;
            height: 200%;
            background: radial-gradient(circle, rgba(236, 139, 94, 0.1) 0%, transparent 70%);
            animation: float 6s ease-in-out infinite;
        }
        
        @keyframes float {
            0%, 100% { transform: translateY(0px) rotate(0deg); }
            50% { transform: translateY(-20px) rotate(180deg); }
        }
        
        .login-right {
            padding: 60px 40px;
        flex: 1;
        display: flex;
            flex-direction: column;
        justify-content: center;
        }
        
        .login-icon {
            font-size: 4rem;
            margin-bottom: 20px;
            opacity: 0.9;
            position: relative;
            z-index: 1;
        }
        
        .login-title {
            font-size: 2.5rem;
            font-weight: 900;
            margin-bottom: 15px;
            position: relative;
            z-index: 1;
        }
        
        .login-subtitle {
            font-size: 1.1rem;
            opacity: 0.9;
            margin-bottom: 30px;
            position: relative;
            z-index: 1;
        }
        
      .form-group {
            margin-bottom: 25px;
            position: relative;
      }

        .form-control {
        width: 100%;
        padding: 15px 20px;
            border: 2px solid #e9ecef;
            border-radius: 12px;
        font-size: 1rem;
            font-family: inherit;
        transition: all 0.3s ease;
            background: var(--muted);
            color: var(--text);
      }

        .form-control:focus {
        outline: none;
        border-color: var(--accent);
            box-shadow: 0 0 0 3px rgba(236, 139, 94, 0.1);
            background: var(--card);
      }

        .form-label {
            position: absolute;
            top: 15px;
            left: 20px;
        color: var(--text-light);
            font-weight: 500;
            transition: all 0.3s ease;
            pointer-events: none;
            background: var(--muted);
            padding: 0 8px;
        }
        
        .form-control:focus + .form-label,
        .form-control:not(:placeholder-shown) + .form-label {
            top: -8px;
            left: 15px;
            font-size: 0.85rem;
            color: var(--accent);
            background: var(--card);
        }
        
        .btn {
            border: none;
            border-radius: 12px;
            padding: 15px 30px;
            font-size: 1rem;
            font-weight: 600;
        cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            font-family: inherit;
        }
        
        .btn-primary {
            background: var(--gradient-accent);
            color: white;
        width: 100%;
            margin-bottom: 20px;
            box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(236, 139, 94, 0.4);
            filter: brightness(1.05);
        }
        
        .btn-outline {
            background: transparent;
            border: 2px solid var(--accent);
            color: var(--accent);
            width: 100%;
        }
        
        .btn-outline:hover {
            background: var(--accent);
            color: white;
            transform: translateY(-2px);
        }
        
        .btn-google {
            background: white;
            color: var(--text);
            border: 2px solid #e9ecef;
            width: 100%;
            margin-top: 0;
            margin-bottom: 20px;
            position: relative;
            overflow: hidden;
            font-weight: 600;
            letter-spacing: 0.5px;
        }
        
        .btn-google::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.4), transparent);
            transition: left 0.5s;
        }
        
        .btn-google:hover::before {
            left: 100%;
        }
        
        .btn-google:hover {
            background: #f8f9fa;
            border-color: #dee2e6;
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0,0,0,0.1);
        }
        
        .btn-google:active {
            transform: translateY(0);
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .alert {
            border-radius: 12px;
            border: none;
            padding: 15px 20px;
            margin-bottom: 20px;
        display: flex;
        align-items: center;
        gap: 10px;
            font-weight: 500;
        }
        
        .alert-danger {
            background: linear-gradient(135deg, #ff6b6b, #ee5a52);
            color: white;
        }
        
        .alert-success {
            background: linear-gradient(135deg, #51cf66, #40c057);
            color: white;
        }
        
        .form-check {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 20px;
        }
        
        .form-check-input {
            width: 18px;
            height: 18px;
            border: 2px solid #e9ecef;
            border-radius: 4px;
        cursor: pointer;
        }
        
        .form-check-input:checked {
            background-color: var(--accent);
            border-color: var(--accent);
        }
        
        .form-check-input:focus {
            box-shadow: 0 0 0 3px rgba(236, 139, 94, 0.1);
        }
        
        .form-check-label {
            color: var(--text-light);
            font-size: 0.9rem;
            cursor: pointer;
        }
        
        .divider {
        text-align: center;
            margin: 30px 0;
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .divider::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 0;
            right: 0;
            height: 1px;
            background: #e9ecef;
            z-index: 1;
        }
        
        .divider span {
            background: var(--card);
            padding: 0 20px;
            color: var(--text-light);
            font-size: 0.9rem;
            font-weight: 500;
            position: relative;
            z-index: 2;
        }
        
        .features {
            margin-top: 40px;
        }
        
        .feature-item {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
            position: relative;
            z-index: 1;
        }
        
        .feature-item i {
            font-size: 1.2rem;
            margin-right: 15px;
            color: rgba(255,255,255,0.8);
        }
        
        .forgot-password {
            text-align: center;
            margin-top: 20px;
        }
        
        .forgot-password a {
            color: var(--text-light);
            text-decoration: none;
            font-size: 0.9rem;
            transition: color 0.3s ease;
        }
        
        .forgot-password a:hover {
            color: var(--accent);
        }
        
        @media (max-width: 768px) {
            .login-container {
                flex-direction: column;
                margin: 10px;
                min-height: auto;
            }
            
            .login-left {
                padding: 40px 20px;
                order: 2;
            }
            
            .login-right {
                padding: 40px 20px;
                order: 1;
            }
            
            .login-title {
                font-size: 2rem;
            }
            
            .login-icon {
                font-size: 3rem;
            }
      }
    </style>
  </head>
  <body>
    <div class="login-container">
        <!-- Left Side - Branding -->
        <div class="login-left">
            <div class="login-icon">
                <i class="fas fa-dumbbell"></i>
            </div>
            <h1 class="login-title">🏋️ GymFit</h1>
            <p class="login-subtitle">Hệ thống quản lý phòng gym hiện đại</p>
            
            <div class="features">
                <div class="feature-item">
                    <i class="fas fa-users"></i>
                    <span>Quản lý thành viên</span>
                </div>
                <div class="feature-item">
                    <i class="fas fa-dumbbell"></i>
                    <span>Theo dõi buổi tập</span>
                </div>
                <div class="feature-item">
                    <i class="fas fa-chart-line"></i>
                    <span>Báo cáo thống kê</span>
                </div>
                <div class="feature-item">
                    <i class="fas fa-credit-card"></i>
                    <span>Quản lý thanh toán</span>
                </div>
            </div>
        </div>
        
        <!-- Right Side - Login Form -->
        <div class="login-right">
            <div class="text-center mb-4">
                <h2 style="font-weight: 800; color: var(--text); margin-bottom: 10px;">Đăng Nhập</h2>
                <p style="color: var(--text-light);">Chào mừng bạn trở lại!</p>
            </div>
                            
            <!-- Error/Success Messages -->
            <!-- Only show error when there is no success to avoid mixed alerts -->
            <c:if test="${empty success and not empty error}">
                <div class="alert alert-danger" role="alert">
                    <i class="fas fa-exclamation-triangle"></i>
                    ${error}
                </div>
            </c:if>
            
            <c:if test="${not empty success}">
                <div class="alert alert-success" role="alert">
                    <i class="fas fa-check-circle"></i>
                    ${success}
                </div>
            </c:if>
            
            <!-- Google Login Button -->
            <div style="margin-top: 25px;">
                <div id="g_id_signin_login"></div>
            </div>
            
            <div class="divider">
                <span>hoặc</span>
        </div>

        <!-- Login Form -->
            <form action="${pageContext.request.contextPath}/auth/login" method="POST" id="loginForm">
          <div class="form-group">
                    <input type="text" class="form-control" id="username" name="username" 
                           placeholder="Tên đăng nhập" value="${username}" required>
                    <label for="username" class="form-label">
                        <i class="fas fa-user"></i> Tên đăng nhập
                    </label>
          </div>

          <div class="form-group">
                    <input type="password" class="form-control" id="password" name="password" 
                           placeholder="Mật khẩu" required>
                    <label for="password" class="form-label">
                        <i class="fas fa-lock"></i> Mật khẩu
                    </label>
          </div>

                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="remember" name="remember">
                    <label class="form-check-label" for="remember">
                        Ghi nhớ đăng nhập
                    </label>
          </div>

                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-sign-in-alt"></i>
                    Đăng Nhập
          </button>
        </form>

            <div class="forgot-password">
                <a href="#" onclick="showForgotPassword()">
                    <i class="fas fa-key"></i>
                    Quên mật khẩu?
                </a>
        </div>

            <div class="divider">
                <span>Chưa có tài khoản?</span>
            </div>
            
            <a href="${pageContext.request.contextPath}/auth/register" class="btn btn-outline">
                <i class="fas fa-user-plus"></i>
                Tạo Tài Khoản Mới
            </a>
        </div>
      </div>

    <!-- Google OAuth -->
    <script src="https://accounts.google.com/gsi/client" async defer></script>
    
    <!-- Form Validation -->
    <script>
        // Google OAuth Configuration
        function initializeGoogleSignIn() {
            google.accounts.id.initialize({
                client_id: '${applicationScope.googleClientId}', 
                callback: handleGoogleResponse,
                auto_select: false,
                cancel_on_tap_outside: true,
                itp_support: true
            });
            // Render official Google button (popup-based, avoids third-party cookie issues)
            var el = document.getElementById('g_id_signin_login');
            if (el) {
                google.accounts.id.renderButton(el, {
                    type: 'standard',
                    theme: 'outline',
                    size: 'large',
                    shape: 'pill',
                    text: 'continue_with',
                    logo_alignment: 'left'
                });
            }
        }
        
        // Handle Google OAuth response
        function handleGoogleResponse(response) {
            // Send the credential to your server for verification
            fetch('${pageContext.request.contextPath}/auth/google-login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    credential: response.credential
                })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    window.location.href = data.redirectUrl;
                } else {
                    alert('Đăng nhập Google thất bại: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi đăng nhập với Google');
            });
        }
        
        // No-op: button is rendered by Google
        
        // Forgot password function
        function showForgotPassword() {
            alert('Chức năng quên mật khẩu đang được phát triển. Vui lòng liên hệ admin để được hỗ trợ.');
        }
        
        // Form validation
        document.getElementById('loginForm').addEventListener('submit', function(e) {
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value;
            
            if (!username || !password) {
          e.preventDefault();
                showAlert('Vui lòng nhập đầy đủ thông tin đăng nhập', 'error');
                return;
            }

            if (username.length < 4) {
                e.preventDefault();
                showAlert('Tên đăng nhập phải có ít nhất 4 ký tự', 'error');
            return;
          }

            if (password.length < 6) {
                e.preventDefault();
                showAlert('Mật khẩu phải có ít nhất 6 ký tự', 'error');
                return;
            }
        });
        
        // Show alert function
        function showAlert(message, type) {
            const alertDiv = document.createElement('div');
            const alertClass = type === 'error' ? 'alert-danger' : 'alert-success';
            const iconClass = type === 'error' ? 'exclamation-triangle' : 'check-circle';
            
            alertDiv.className = 'alert ' + alertClass;
            alertDiv.innerHTML = '<i class="fas fa-' + iconClass + '"></i> ' + message;
            
            const form = document.getElementById('loginForm');
            form.parentNode.insertBefore(alertDiv, form);
            
            setTimeout(function() {
                alertDiv.remove();
            }, 5000);
        }
        
        // Auto focus on username field
        document.getElementById('username').focus();
        
        // Show/hide password toggle
        const passwordField = document.getElementById('password');
        const passwordLabel = document.querySelector('label[for="password"]');
        
        passwordLabel.addEventListener('click', function() {
            if (passwordField.type === 'password') {
                passwordField.type = 'text';
                passwordLabel.innerHTML = '<i class="fas fa-eye-slash"></i> Mật khẩu';
            } else {
                passwordField.type = 'password';
                passwordLabel.innerHTML = '<i class="fas fa-lock"></i> Mật khẩu';
            }
        });
        
        // Initialize Google Sign-In when page loads
        window.onload = function() {
            if (typeof google !== 'undefined') {
                initializeGoogleSignIn();
            }
        };
    </script>
  </body>
</html>