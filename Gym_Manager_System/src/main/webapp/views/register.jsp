<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký - GymFit</title>
    
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
            padding: 20px 0;
        }
        
        .register-container {
            background: var(--card);
            border-radius: 20px;
            box-shadow: 0 20px 40px var(--shadow);
            overflow: hidden;
            max-width: 1200px;
        width: 100%;
            margin: 20px;
            display: flex;
            min-height: 700px;
        }
        
        .register-left {
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
        
        .register-left::before {
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
        
        .register-right {
            padding: 60px 40px;
            flex: 1.5;
            display: flex;
            flex-direction: column;
            justify-content: center;
            overflow-y: auto;
            max-height: 700px;
        }
        
        .register-icon {
            font-size: 4rem;
        margin-bottom: 20px;
            opacity: 0.9;
            position: relative;
            z-index: 1;
      }

        .register-title {
            font-size: 2.5rem;
        font-weight: 900;
            margin-bottom: 15px;
            position: relative;
            z-index: 1;
        }
        
        .register-subtitle {
        font-size: 1.1rem;
            opacity: 0.9;
        margin-bottom: 30px;
            position: relative;
            z-index: 1;
      }

      .form-group {
        margin-bottom: 20px;
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
        
        .form-control.is-invalid {
            border-color: #e74c3c;
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
            position: sticky;
            top: 10px;
            z-index: 5;
            opacity: 1;
            transition: opacity 0.6s ease, transform 0.4s ease;
            transform: translateY(0);
        }
        
        .alert-danger {
            background: linear-gradient(135deg, #ff6b6b, #ee5a52);
            color: white;
        }
        
        .alert-success {
            background: linear-gradient(135deg, #51cf66, #40c057);
            color: white;
        }

        .alert.hide {
            opacity: 0;
            transform: translateY(-10px);
            pointer-events: none;
        }
        
        .form-check {
            display: flex;
            align-items: flex-start;
            gap: 10px;
            margin-bottom: 20px;
        }
        
        .form-check-input {
        width: 18px;
        height: 18px;
            border: 2px solid #e9ecef;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 2px;
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
            line-height: 1.4;
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
        
        .benefits {
            margin-top: 40px;
        }
        
        .benefit-item {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
            position: relative;
            z-index: 1;
        }
        
        .benefit-item i {
            font-size: 1.2rem;
            margin-right: 15px;
            color: rgba(255,255,255,0.8);
        }
        
        .password-strength {
            margin-top: 5px;
            font-size: 0.85rem;
        }
        
        .strength-weak { color: #e74c3c; }
        .strength-medium { color: #f39c12; }
        .strength-strong { color: #27ae60; }
        
        .invalid-feedback {
            display: block;
            font-size: 0.85rem;
            margin-top: 5px;
            color: #e74c3c;
        }
        
        .row {
            display: flex;
            gap: 15px;
        }
        
        .col {
            flex: 1;
        }
        
        @media (max-width: 768px) {
            .register-container {
                flex-direction: column;
                margin: 10px;
                min-height: auto;
            }
            
            .register-left {
                padding: 40px 20px;
                order: 2;
            }
            
            .register-right {
                padding: 40px 20px;
                order: 1;
                max-height: none;
            }
            
            .register-title {
                font-size: 2rem;
            }
            
            .register-icon {
                font-size: 3rem;
            }
            
            .row {
                flex-direction: column;
                gap: 0;
            }
      }
    </style>
  </head>
  <body>
    <div class="register-container">
        <!-- Left Side - Benefits -->
        <div class="register-left">
            <div class="register-icon">
                <i class="fas fa-user-plus"></i>
            </div>
            <h1 class="register-title">🏋️ GymFit</h1>
            <p class="register-subtitle">Tham gia ngay để trải nghiệm dịch vụ tốt nhất</p>
            
            <div class="benefits">
                <div class="benefit-item">
                    <i class="fas fa-dumbbell"></i>
                    <span>Truy cập đầy đủ trang thiết bị</span>
                </div>
                <div class="benefit-item">
                    <i class="fas fa-chart-line"></i>
                    <span>Theo dõi tiến độ tập luyện</span>
                </div>
                <div class="benefit-item">
                    <i class="fas fa-user-tie"></i>
                    <span>Hỗ trợ từ huấn luyện viên</span>
                </div>
                <div class="benefit-item">
                    <i class="fas fa-mobile-alt"></i>
                    <span>Quản lý qua ứng dụng di động</span>
                </div>
                <div class="benefit-item">
                    <i class="fas fa-gift"></i>
                    <span>Ưu đãi đặc biệt cho thành viên</span>
                </div>
            </div>
        </div>

        <!-- Right Side - Register Form -->
        <div class="register-right">
            <div class="text-center mb-4">
                <h2 style="font-weight: 800; color: var(--text); margin-bottom: 10px;">Tạo Tài Khoản</h2>
                <p style="color: var(--text-light);">Điền thông tin để bắt đầu hành trình tập luyện</p>
            </div>
            
            <!-- Error/Success Messages -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert" id="form-alert">
                    <i class="fas fa-exclamation-triangle"></i>
                    ${error}
                </div>
            </c:if>
            
            <c:if test="${not empty success}">
                <div class="alert alert-success" role="alert" id="form-alert">
                    <i class="fas fa-check-circle"></i>
                    ${success}
                </div>
            </c:if>
            
            <!-- Google Register Button -->
            <div style="margin-top: 25px;">
                <div id="g_id_signin_register"></div>
            </div>
            
            <div class="divider">
                <span>hoặc</span>
            </div>

        <!-- Register Form -->
            <form action="${pageContext.request.contextPath}/auth/register" method="POST" id="registerForm" novalidate>
                <div class="row">
                    <div class="col">
                        <div class="form-group">
                            <input type="text" class="form-control" id="username" name="username" 
                                   placeholder="Tên đăng nhập" value="${username}" required>
                            <label for="username" class="form-label">
                                <i class="fas fa-user"></i> Tên đăng nhập
                            </label>
                            <div class="invalid-feedback" id="username-error"></div>
                        </div>
                    </div>
                    <div class="col">
        <div class="form-group">
                            <input type="email" class="form-control" id="email" name="email" 
                                   placeholder="Email" value="${email}" required>
                            <label for="email" class="form-label">
                                <i class="fas fa-envelope"></i> Email
                            </label>
                            <div class="invalid-feedback" id="email-error"></div>
                        </div>
                    </div>
        </div>

          <div class="form-group">
                    <input type="text" class="form-control" id="fullName" name="fullName" 
                           placeholder="Họ và tên" value="${fullName}" required>
                    <label for="fullName" class="form-label">
                        <i class="fas fa-id-card"></i> Họ và tên
                    </label>
                    <div class="invalid-feedback" id="fullName-error"></div>
          </div>
              
                <div class="row">
                    <div class="col">
                        <div class="form-group">
                            <input type="tel" class="form-control" id="phone" name="phone" 
                                   placeholder="Số điện thoại" value="${phone}">
                            <label for="phone" class="form-label">
                                <i class="fas fa-phone"></i> Số điện thoại
                            </label>
                        </div>
                    </div>
                    <div class="col">
          <div class="form-group">
                            <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth" 
                                   value="${dateOfBirth}">
                            <label for="dateOfBirth" class="form-label">
                                <i class="fas fa-calendar"></i> Ngày sinh
                            </label>
                        </div>
                    </div>
          </div>
              
        <div class="form-group">
                    <select class="form-control" id="gender" name="gender">
                        <option value="">-- Chọn giới tính --</option>
                        <option value="male" ${gender == 'male' ? 'selected' : ''}>Nam</option>
                        <option value="female" ${gender == 'female' ? 'selected' : ''}>Nữ</option>
                        <option value="other" ${gender == 'other' ? 'selected' : ''}>Khác</option>
                    </select>
                    <label for="gender" class="form-label">
                        <i class="fas fa-venus-mars"></i> Giới tính
                    </label>
        </div>

                <div class="row">
                    <div class="col">
        <div class="form-group">
                            <input type="password" class="form-control" id="password" name="password" 
                                   placeholder="Mật khẩu" required>
                            <label for="password" class="form-label">
                                <i class="fas fa-lock"></i> Mật khẩu
                            </label>
                            <div class="password-strength" id="password-strength"></div>
                            <div class="invalid-feedback" id="password-error"></div>
                        </div>
        </div>
                    <div class="col">
        <div class="form-group">
                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                                   placeholder="Xác nhận mật khẩu" required>
                            <label for="confirmPassword" class="form-label">
                                <i class="fas fa-lock"></i> Xác nhận mật khẩu
                            </label>
                            <div class="invalid-feedback" id="confirmPassword-error"></div>
                        </div>
                    </div>
        </div>
          
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="terms" required>
                    <label class="form-check-label" for="terms">
                        Tôi đồng ý với <a href="#" class="text-decoration-none" style="color: var(--accent);">Điều khoản sử dụng</a> 
                        và <a href="#" class="text-decoration-none" style="color: var(--accent);">Chính sách bảo mật</a>
                    </label>
                    <div class="invalid-feedback" id="terms-error"></div>
      </div>
  
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-user-plus"></i>
                    Tạo Tài Khoản
          </button>
        </form>

            <div class="divider">
                <span>Đã có tài khoản?</span>
            </div>
            
            <a href="${pageContext.request.contextPath}/auth/login" class="btn btn-outline">
                <i class="fas fa-sign-in-alt"></i>
                Đăng Nhập Ngay
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
            var el = document.getElementById('g_id_signin_register');
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
            fetch('${pageContext.request.contextPath}/auth/google-register', {
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
                    alert('Đăng ký Google thất bại: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi đăng ký với Google');
            });
        }
        
        // No-op: button is rendered by Google
        
        // Password strength checker
        function checkPasswordStrength(password) {
            var strength = 0;
            var message = '';
            
            if (password.length >= 8) strength++;
            if (/[a-z]/.test(password)) strength++;
            if (/[A-Z]/.test(password)) strength++;
            if (/[0-9]/.test(password)) strength++;
            if (/[^A-Za-z0-9]/.test(password)) strength++;
            
            if (strength < 3) {
                message = '<span class="strength-weak">Mật khẩu yếu</span>';
            } else if (strength < 4) {
                message = '<span class="strength-medium">Mật khẩu trung bình</span>';
            } else {
                message = '<span class="strength-strong">Mật khẩu mạnh</span>';
            }
            
            return { strength: strength, message: message };
        }
        
        // Real-time password strength
        document.getElementById('password').addEventListener('input', function() {
            var password = this.value;
            var strengthDiv = document.getElementById('password-strength');
            
            if (password.length > 0) {
                var result = checkPasswordStrength(password);
                strengthDiv.innerHTML = result.message;
            } else {
                strengthDiv.innerHTML = '';
            }
        });

        // Form validation
        document.getElementById('registerForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            var isValid = true;

          // Clear previous errors
            var invalidElements = document.querySelectorAll('.is-invalid');
            for (var i = 0; i < invalidElements.length; i++) {
                invalidElements[i].classList.remove('is-invalid');
            }
            
            var feedbackElements = document.querySelectorAll('.invalid-feedback');
            for (var i = 0; i < feedbackElements.length; i++) {
                feedbackElements[i].textContent = '';
            }
            
            // Username validation
            var username = document.getElementById('username').value.trim();
            if (!username || username.length < 4) {
                document.getElementById('username').classList.add('is-invalid');
                document.getElementById('username-error').textContent = 'Tên đăng nhập phải có ít nhất 4 ký tự';
            isValid = false;
          }

            // Email validation
            var email = document.getElementById('email').value.trim();
            var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!email || !emailRegex.test(email)) {
                document.getElementById('email').classList.add('is-invalid');
                document.getElementById('email-error').textContent = 'Email không hợp lệ';
            isValid = false;
          }

            // Full name validation
            var fullName = document.getElementById('fullName').value.trim();
            if (!fullName || fullName.length < 2) {
                document.getElementById('fullName').classList.add('is-invalid');
                document.getElementById('fullName-error').textContent = 'Họ tên phải có ít nhất 2 ký tự';
                isValid = false;
            }
            
            // Password validation
            var password = document.getElementById('password').value;
            var result = checkPasswordStrength(password);
            if (!password || result.strength < 3) {
                document.getElementById('password').classList.add('is-invalid');
                document.getElementById('password-error').textContent = 'Mật khẩu phải đủ mạnh (ít nhất 8 ký tự, có chữ hoa, chữ thường, số và ký tự đặc biệt)';
                isValid = false;
            }
            
            // Confirm password validation
            var confirmPassword = document.getElementById('confirmPassword').value;
            if (password !== confirmPassword) {
                document.getElementById('confirmPassword').classList.add('is-invalid');
                document.getElementById('confirmPassword-error').textContent = 'Mật khẩu xác nhận không khớp';
                isValid = false;
            }
            
            // Terms validation
            var terms = document.getElementById('terms').checked;
            if (!terms) {
                document.getElementById('terms').classList.add('is-invalid');
                document.getElementById('terms-error').textContent = 'Bạn phải đồng ý với điều khoản sử dụng';
                isValid = false;
            }
            
            if (isValid) {
                this.submit();
            }
        });
        
        // Auto focus on username field
        document.getElementById('username').focus();
        
        // Initialize Google Sign-In when page loads
        window.onload = function() {
            if (typeof google !== 'undefined') {
                initializeGoogleSignIn();
            }
            // If server set an alert, scroll into view
            var alertEl = document.getElementById('form-alert');
            if (alertEl) {
                alertEl.scrollIntoView({ behavior: 'smooth', block: 'start' });
                // Auto hide after 4 seconds
                setTimeout(function(){
                    alertEl.classList.add('hide');
                }, 4000);
            }
        };
    </script>
  </body>
</html>