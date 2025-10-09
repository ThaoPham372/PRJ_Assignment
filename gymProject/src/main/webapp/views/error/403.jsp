<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Truy Cập Bị Từ Chối - Stamina Gym</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    
    <style>
        body {
            background: linear-gradient(135deg, var(--primary-purple) 0%, var(--secondary-purple) 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Poppins', sans-serif;
        }
        
        .error-container {
            background: var(--white);
            border-radius: 30px;
            box-shadow: 0 30px 60px rgba(59, 30, 120, 0.2);
            padding: 50px;
            text-align: center;
            max-width: 500px;
            width: 90%;
        }
        
        .error-icon {
            font-size: 6rem;
            color: var(--danger);
            margin-bottom: 20px;
        }
        
        .error-title {
            font-size: 2.5rem;
            font-weight: 800;
            color: var(--primary-purple);
            margin-bottom: 20px;
        }
        
        .error-message {
            font-size: 1.1rem;
            color: var(--gray-dark);
            margin-bottom: 30px;
            line-height: 1.6;
        }
        
        .btn-home {
            background: linear-gradient(135deg, var(--primary-purple), var(--secondary-purple));
            border: none;
            border-radius: 25px;
            padding: 15px 30px;
            font-size: 16px;
            font-weight: 600;
            color: var(--white);
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
        }
        
        .btn-home:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 30px rgba(59, 30, 120, 0.3);
            color: var(--white);
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">
            <i class="fas fa-ban"></i>
        </div>
        
        <h1 class="error-title">403</h1>
        
        <p class="error-message">
            Xin lỗi, bạn không có quyền truy cập vào trang này.<br>
            Vui lòng liên hệ quản trị viên nếu bạn cho rằng đây là lỗi.
        </p>
        
        <div class="mb-3">
            <a href="${pageContext.request.contextPath}/home" class="btn-home">
                <i class="fas fa-home me-2"></i>Về Trang Chủ
            </a>
        </div>
        
        <div class="mt-4">
            <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">
                <i class="fas fa-sign-in-alt me-2"></i>Đăng Nhập Lại
            </a>
        </div>
    </div>
</body>
</html>

