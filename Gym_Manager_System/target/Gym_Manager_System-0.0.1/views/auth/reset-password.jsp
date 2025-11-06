<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt Lại Mật Khẩu - GymFit</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .reset-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            max-width: 500px;
            width: 100%;
            padding: 40px;
            animation: slideUp 0.5s ease;
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .reset-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .reset-header i {
            font-size: 60px;
            color: #667eea;
            margin-bottom: 20px;
        }

        .reset-header h1 {
            color: #333;
            font-size: 28px;
            margin-bottom: 10px;
        }

        .reset-header p {
            color: #666;
            font-size: 14px;
        }

        .alert {
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .alert-success {
            background: #e8f5e9;
            border-left: 4px solid #4caf50;
            color: #2e7d32;
        }

        .alert-error {
            background: #fee;
            border-left: 4px solid #f44336;
            color: #c62828;
        }

        .alert i {
            font-size: 20px;
        }

        .email-display {
            background: #f5f5f5;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            text-align: center;
        }

        .email-display i {
            color: #667eea;
            margin-right: 8px;
        }

        .email-display strong {
            color: #333;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            color: #333;
            font-weight: 600;
            margin-bottom: 8px;
            font-size: 14px;
        }

        .input-wrapper {
            position: relative;
        }

        .input-wrapper i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #999;
        }

        .form-control {
            width: 100%;
            padding: 15px 50px 15px 45px; /* ✅ Thêm padding-right để tránh text bị che bởi icon */
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            font-size: 16px;
            transition: all 0.3s;
            box-sizing: border-box; /* ✅ Ensure padding included in width */
        }

        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        /* ✅ Improved password toggle button */
        .password-toggle {
            position: absolute;
            right: 12px;
            top: 50%;
            transform: translateY(-50%);
            cursor: pointer;
            color: #999;
            transition: all 0.3s;
            padding: 0;
            border: none;
            border-radius: 5px;
            background: transparent;
            z-index: 10;
            display: flex;
            align-items: center;
            justify-content: center;
            width: 36px;
            height: 36px;
            outline: none; /* ✅ Remove focus outline */
        }

        .password-toggle:hover {
            color: #667eea;
            background: rgba(102, 126, 234, 0.1);
        }

        .password-toggle:active {
            transform: translateY(-50%) scale(0.95);
        }

        .password-toggle:focus {
            outline: 2px solid rgba(102, 126, 234, 0.3);
            outline-offset: 2px;
        }

        .password-toggle i {
            font-size: 18px;
        }

        .btn {
            width: 100%;
            padding: 15px;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            margin-top: 10px;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
        }

        .password-requirements {
            background: #fff3cd;
            border-left: 4px solid #ffc107;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .password-requirements strong {
            color: #856404;
        }

        .password-requirements ul {
            margin: 10px 0 0 20px;
            color: #856404;
        }

        .back-link {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 5px;
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
            margin-top: 20px;
            transition: all 0.3s;
        }

        .back-link:hover {
            transform: translateX(-5px);
        }

        .token-input {
            text-align: center;
            font-size: 24px;
            letter-spacing: 5px;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="reset-container">
        <div class="reset-header">
            <i class="fas fa-key"></i>
            <h1>Đặt Lại Mật Khẩu</h1>
            <p>Nhập mã xác nhận từ email và mật khẩu mới</p>
        </div>

        <c:if test="${not empty success}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <span>${success}</span>
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <span>${error}</span>
            </div>
        </c:if>

        <div class="email-display">
            <i class="fas fa-envelope"></i>
            Gửi đến: <strong>${email}</strong>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/auth/reset-password" id="resetForm">
            <div class="form-group">
                <label for="token">
                    <i class="fas fa-shield-alt"></i> Mã Xác Nhận (6 số)
                </label>
                <div class="input-wrapper">
                    <i class="fas fa-shield-alt"></i>
                    <input 
                        type="text" 
                        id="token" 
                        name="token" 
                        class="form-control token-input" 
                        placeholder="123456"
                        maxlength="6"
                        pattern="\d{6}"
                        required
                        autofocus
                    />
                </div>
            </div>

            <div class="password-requirements">
                <strong><i class="fas fa-info-circle"></i> Yêu cầu mật khẩu:</strong>
                <ul>
                    <li>Tối thiểu 6 ký tự</li>
                    <li>Nên kết hợp chữ, số và ký tự đặc biệt</li>
                </ul>
            </div>

            <div class="form-group">
                <label for="newPassword">
                    <i class="fas fa-lock"></i> Mật Khẩu Mới
                </label>
                <div class="input-wrapper">
                    <i class="fas fa-lock"></i>
                    <input 
                        type="password" 
                        id="newPassword" 
                        name="newPassword" 
                        class="form-control" 
                        placeholder="Nhập mật khẩu mới"
                        minlength="6"
                        required
                        autocomplete="new-password"
                    />
                    <button type="button" class="password-toggle" id="togglePassword1" aria-label="Show password">
                        <i class="fas fa-eye"></i>
                    </button>
                </div>
            </div>

            <div class="form-group">
                <label for="confirmPassword">
                    <i class="fas fa-lock"></i> Xác Nhận Mật Khẩu
                </label>
                <div class="input-wrapper">
                    <i class="fas fa-lock"></i>
                    <input 
                        type="password" 
                        id="confirmPassword" 
                        name="confirmPassword" 
                        class="form-control" 
                        placeholder="Nhập lại mật khẩu mới"
                        minlength="6"
                        required
                        autocomplete="new-password"
                    />
                    <button type="button" class="password-toggle" id="togglePassword2" aria-label="Show password">
                        <i class="fas fa-eye"></i>
                    </button>
                </div>
            </div>

            <button type="submit" class="btn btn-primary">
                <i class="fas fa-check"></i>
                Đặt Lại Mật Khẩu
            </button>
        </form>

        <a href="${pageContext.request.contextPath}/auth/forgot-password" class="back-link">
            <i class="fas fa-arrow-left"></i>
            Gửi lại mã xác nhận
        </a>
    </div>

    <script>
        // ✅ Improved password toggle functionality
        function setupPasswordToggle(toggleId, passwordId) {
            const toggle = document.getElementById(toggleId);
            const password = document.getElementById(passwordId);
            const icon = toggle.querySelector('i');
            
            toggle.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                
                const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
                password.setAttribute('type', type);
                
                // Toggle icon
                if (type === 'text') {
                    icon.classList.remove('fa-eye');
                    icon.classList.add('fa-eye-slash');
                    toggle.setAttribute('aria-label', 'Hide password');
                } else {
                    icon.classList.remove('fa-eye-slash');
                    icon.classList.add('fa-eye');
                    toggle.setAttribute('aria-label', 'Show password');
                }
            });
        }
        
        // Setup both password toggles
        setupPasswordToggle('togglePassword1', 'newPassword');
        setupPasswordToggle('togglePassword2', 'confirmPassword');

        // Only allow numbers in token field
        document.getElementById('token').addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '');
        });

        // Form validation
        document.getElementById('resetForm').addEventListener('submit', function(e) {
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (newPassword !== confirmPassword) {
                e.preventDefault();
                alert('Mật khẩu xác nhận không khớp!');
                return false;
            }
        });
    </script>
</body>
</html>

