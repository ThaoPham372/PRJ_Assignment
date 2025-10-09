<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Trang không tìm thấy | Stamina Gym</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .error-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        .error-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 60px 40px;
            text-align: center;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            max-width: 600px;
            width: 100%;
        }
        .error-icon {
            font-size: 120px;
            color: #667eea;
            margin-bottom: 30px;
            animation: float 3s ease-in-out infinite;
        }
        .error-code {
            font-size: 80px;
            font-weight: 900;
            background: linear-gradient(135deg, #667eea, #764ba2);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            margin: 20px 0;
        }
        .error-title {
            font-size: 32px;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 20px;
        }
        .error-description {
            font-size: 18px;
            color: #6c757d;
            margin-bottom: 40px;
            line-height: 1.6;
        }
        .btn-custom {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none;
            padding: 15px 30px;
            border-radius: 50px;
            color: white;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            margin: 10px;
            transition: all 0.3s ease;
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }
        .btn-custom:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 30px rgba(102, 126, 234, 0.4);
            color: white;
            text-decoration: none;
        }
        .btn-outline-custom {
            background: transparent;
            border: 2px solid #667eea;
            color: #667eea;
            padding: 13px 28px;
            border-radius: 50px;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            margin: 10px;
            transition: all 0.3s ease;
        }
        .btn-outline-custom:hover {
            background: #667eea;
            color: white;
            transform: translateY(-3px);
            text-decoration: none;
        }
        .gym-icon {
            position: absolute;
            top: 20px;
            left: 20px;
            color: rgba(255, 255, 255, 0.8);
            font-size: 24px;
        }
        @keyframes float {
            0%, 100% { transform: translateY(0px); }
            50% { transform: translateY(-20px); }
        }
        .error-suggestions {
            background: #f8f9fa;
            border-radius: 15px;
            padding: 30px;
            margin-top: 40px;
            text-align: left;
        }
        .suggestion-item {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
            padding: 10px;
            border-radius: 10px;
            transition: all 0.3s ease;
        }
        .suggestion-item:hover {
            background: #e9ecef;
        }
        .suggestion-icon {
            color: #667eea;
            margin-right: 15px;
            font-size: 20px;
        }
    </style>
</head>
<body>
    <div class="gym-icon">
        <i class="fas fa-dumbbell"></i> STAMINA GYM
    </div>
    
    <div class="error-container">
        <div class="error-card">
            <div class="error-icon">
                <i class="fas fa-search"></i>
            </div>
            
            <div class="error-code">404</div>
            
            <h1 class="error-title">Trang không tìm thấy</h1>
            
            <p class="error-description">
                Xin lỗi, trang bạn đang tìm kiếm không tồn tại hoặc đã bị di chuyển.<br>
                Hãy kiểm tra lại URL hoặc quay về trang chủ.
            </p>
            
            <div class="mb-4">
                <a href="${pageContext.request.contextPath}/home" class="btn-custom">
                    <i class="fas fa-home me-2"></i>Về trang chủ
                </a>
                <a href="${pageContext.request.contextPath}/login" class="btn-outline-custom">
                    <i class="fas fa-sign-in-alt me-2"></i>Đăng nhập
                </a>
            </div>
            
            <div class="error-suggestions">
                <h5 class="mb-3">
                    <i class="fas fa-lightbulb text-warning me-2"></i>
                    Gợi ý:
                </h5>
                
                <div class="suggestion-item">
                    <i class="fas fa-check-circle suggestion-icon"></i>
                    <div>
                        <strong>Kiểm tra URL:</strong> Đảm bảo địa chỉ web được nhập chính xác
                    </div>
                </div>
                
                <div class="suggestion-item">
                    <i class="fas fa-arrow-left suggestion-icon"></i>
                    <div>
                        <strong>Quay lại:</strong> Sử dụng nút Back của trình duyệt
                    </div>
                </div>
                
                <div class="suggestion-item">
                    <i class="fas fa-search suggestion-icon"></i>
                    <div>
                        <strong>Tìm kiếm:</strong> Sử dụng tính năng tìm kiếm trên trang chủ
                    </div>
                </div>
                
                <div class="suggestion-item">
                    <i class="fas fa-phone suggestion-icon"></i>
                    <div>
                        <strong>Liên hệ:</strong> Gọi hotline <strong>1900-1234</strong> để được hỗ trợ
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>