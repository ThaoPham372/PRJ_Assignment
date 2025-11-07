<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên Mật Khẩu - GymFit</title>
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

        .forgot-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            max-width: 450px;
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

        .forgot-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .forgot-header i {
            font-size: 60px;
            color: #667eea;
            margin-bottom: 20px;
        }

        .forgot-header h1 {
            color: #333;
            font-size: 28px;
            margin-bottom: 10px;
        }

        .forgot-header p {
            color: #666;
            font-size: 14px;
            line-height: 1.6;
        }

        .alert {
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .alert-error {
            background: #fee;
            border-left: 4px solid #f44336;
            color: #c62828;
        }

        .alert i {
            font-size: 20px;
        }

        .form-group {
            margin-bottom: 25px;
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
            padding: 15px 15px 15px 45px;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            font-size: 16px;
            transition: all 0.3s;
        }

        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
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
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
        }

        .btn-secondary {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
            margin-top: 10px;
        }

        .btn-secondary:hover {
            background: #f8f9ff;
        }

        .divider {
            text-align: center;
            margin: 25px 0;
            position: relative;
        }

        .divider::before {
            content: '';
            position: absolute;
            left: 0;
            top: 50%;
            width: 100%;
            height: 1px;
            background: #e0e0e0;
        }

        .divider span {
            background: white;
            padding: 0 15px;
            color: #999;
            font-size: 14px;
            position: relative;
            z-index: 1;
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

        .info-box {
            background: #e3f2fd;
            border-left: 4px solid #2196f3;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
        }

        .info-box strong {
            color: #1976d2;
        }

        .info-box p {
            color: #555;
            font-size: 14px;
            margin: 5px 0;
        }
    </style>
</head>
<body>
    <div class="forgot-container">
        <div class="forgot-header">
            <i class="fas fa-lock"></i>
            <h1>Quên Mật Khẩu?</h1>
            <p>Nhập email đăng ký của bạn, chúng tôi sẽ gửi mã xác nhận để đặt lại mật khẩu</p>
            <c:if test="${not empty email}">
                <p style="color: #667eea; font-weight: 600; margin-top: 10px;">
                    <i class="fas fa-info-circle"></i> Email của tài khoản: ${email}
                </p>
            </c:if>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <span>${error}</span>
            </div>
        </c:if>

        <div class="info-box">
            <strong><i class="fas fa-info-circle"></i> Lưu ý:</strong>
            <p>• Mã xác nhận sẽ được gửi đến email của bạn</p>
            <p>• Mã có hiệu lực trong 15 phút</p>
            <p>• Kiểm tra cả hộp thư Spam/Junk nếu không thấy email</p>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/auth/forgot-password">
            <div class="form-group">
                <label for="email">
                    <i class="fas fa-envelope"></i> Email đã đăng ký
                </label>
                <div class="input-wrapper">
                    <i class="fas fa-envelope"></i>
                    <input 
                        type="email" 
                        id="email" 
                        name="email" 
                        class="form-control" 
                        placeholder="your-email@example.com"
                        value="${email != null ? email : ''}"
                        required
                        ${empty email ? 'autofocus' : ''}
                        ${not empty email ? 'readonly style="background-color: #f5f5f5;"' : ''}
                    />
                </div>
            </div>

            <button type="submit" class="btn btn-primary">
                <i class="fas fa-paper-plane"></i>
                Gửi Mã Xác Nhận
            </button>
        </form>

        <div class="divider">
            <span>hoặc</span>
        </div>

        <div style="text-align: center;">
            <c:if test="${not empty email}">
                <a href="${pageContext.request.contextPath}/admin/profile" class="back-link" style="margin-bottom: 10px;">
                    <i class="fas fa-arrow-left"></i>
                    Quay lại Profile
                </a>
            </c:if>
            <a href="${pageContext.request.contextPath}/auth/login" class="back-link">
                <i class="fas fa-arrow-left"></i>
                Quay lại Đăng nhập
            </a>
        </div>
    </div>
</body>
</html>

