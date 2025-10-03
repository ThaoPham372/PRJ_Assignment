<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký Thành Viên - Stamina Gym</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    
    <style>
        /* Override CSS with Stamina Gym theme for register page */
        body {
            background: linear-gradient(135deg, var(--primary-purple) 0%, var(--secondary-purple) 100%);
            min-height: 100vh;
            padding: 20px 0;
            font-family: 'Poppins', sans-serif;
        }
        
        .register-card {
            background: var(--white);
            border-radius: 30px;
            box-shadow: 0 30px 60px rgba(59, 30, 120, 0.2);
            overflow: hidden;
            max-width: 800px;
            margin: 20px auto;
        }
        
        .register-header {
            background: linear-gradient(135deg, var(--primary-purple), var(--secondary-purple));
            color: var(--white);
            padding: 40px;
            text-align: center;
            position: relative;
            overflow: hidden;
        }
        
        .register-header::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="register-pattern" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="25" cy="25" r="2" fill="%23FFD700" opacity="0.2"/><circle cx="75" cy="75" r="2" fill="%23FFD700" opacity="0.2"/></pattern></defs><rect width="100" height="100" fill="url(%23register-pattern)"/></svg>');
        }
        
        .register-header h1, .register-header h2, .register-header p {
            position: relative;
            z-index: 2;
            color: var(--white);
        }
        
        .register-header .fa-dumbbell {
            color: var(--accent-yellow);
        }
        
        .register-form {
            padding: 50px 40px;
        }
        
        .form-control {
            border-radius: 15px;
            border: 2px solid var(--gray-medium);
            padding: 15px 20px;
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
            margin-bottom: 8px;
        }
        
        .btn-register {
            background: linear-gradient(135deg, var(--primary-purple), var(--secondary-purple));
            border: none;
            border-radius: 25px;
            padding: 18px;
            font-size: 16px;
            font-weight: 600;
            color: var(--white);
            font-family: 'Poppins', sans-serif;
            transition: all 0.3s ease;
        }
        
        .btn-register:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 30px rgba(59, 30, 120, 0.3);
            color: var(--white);
        }
        
        .package-selection {
            background: var(--gray-light);
            border-radius: 15px;
            padding: 20px;
            margin: 20px 0;
        }
        
        .package-option {
            border: 2px solid var(--gray-medium);
            border-radius: 15px;
            padding: 20px;
            margin: 10px 0;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .package-option:hover {
            border-color: var(--primary-purple);
            transform: translateY(-2px);
        }
        
        .package-option.selected {
            border-color: var(--accent-yellow);
            background-color: rgba(255, 215, 0, 0.1);
        }
        
        .package-price {
            font-size: 1.5rem;
            font-weight: 800;
            color: var(--primary-purple);
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
            animation: float 10s ease-in-out infinite;
        }
        
        .shape:nth-child(1) {
            width: 120px;
            height: 120px;
            top: 10%;
            left: 5%;
            animation-delay: 0s;
        }
        
        .shape:nth-child(2) {
            width: 80px;
            height: 80px;
            top: 70%;
            right: 8%;
            animation-delay: 4s;
        }
        
        .shape:nth-child(3) {
            width: 100px;
            height: 100px;
            bottom: 10%;
            left: 10%;
            animation-delay: 8s;
        }
        
        @keyframes float {
            0%, 100% {
                transform: translateY(0px) scale(1);
            }
            50% {
                transform: translateY(-30px) scale(1.1);
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
            <div class="col-lg-10">
                <div class="register-card">
                    <!-- Header -->
                    <div class="register-header">
                        <h1 class="mb-3">
                            <i class="fas fa-dumbbell fa-2x"></i>
                        </h1>
                        <h2 class="fw-bold">STAMINA GYM</h2>
                        <p class="mb-0 opacity-75">Đăng ký thành viên mới</p>
                    </div>
                    
                    <!-- Registration Form -->
                    <div class="register-form">
                        <!-- Error Message -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-circle me-2"></i>
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <!-- Success Message -->
                        <c:if test="${not empty success}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="fas fa-check-circle me-2"></i>
                                ${success}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm">
                            <div class="row">
                                <!-- Personal Information -->
                                <div class="col-lg-6">
                                    <h5 class="text-primary mb-4">
                                        <i class="fas fa-user me-2"></i>Thông Tin Cá Nhân
                                    </h5>
                                    
                                    <div class="mb-3">
                                        <label for="fullName" class="form-label">
                                            <i class="fas fa-id-card me-2"></i>Họ và Tên *
                                        </label>
                                        <input type="text" class="form-control" id="fullName" name="fullName" 
                                               placeholder="Nhập họ và tên đầy đủ" required>
                                    </div>
                                    
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="email" class="form-label">
                                                <i class="fas fa-envelope me-2"></i>Email *
                                            </label>
                                            <input type="email" class="form-control" id="email" name="email" 
                                                   placeholder="example@email.com" required>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="phone" class="form-label">
                                                <i class="fas fa-phone me-2"></i>Số Điện Thoại *
                                            </label>
                                            <input type="tel" class="form-control" id="phone" name="phone" 
                                                   placeholder="0123456789" required>
                                        </div>
                                    </div>
                                    
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="dateOfBirth" class="form-label">
                                                <i class="fas fa-calendar me-2"></i>Ngày Sinh
                                            </label>
                                            <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth">
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="gender" class="form-label">
                                                <i class="fas fa-venus-mars me-2"></i>Giới Tính
                                            </label>
                                            <select class="form-control" id="gender" name="gender">
                                                <option value="">Chọn giới tính</option>
                                                <option value="male">Nam</option>
                                                <option value="female">Nữ</option>
                                                <option value="other">Khác</option>
                                            </select>
                                        </div>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="address" class="form-label">
                                            <i class="fas fa-map-marker-alt me-2"></i>Địa Chỉ
                                        </label>
                                        <textarea class="form-control" id="address" name="address" rows="3" 
                                                  placeholder="Nhập địa chỉ đầy đủ"></textarea>
                                    </div>
                                </div>
                                
                                <!-- Account & Package Information -->
                                <div class="col-lg-6">
                                    <h5 class="text-primary mb-4">
                                        <i class="fas fa-key me-2"></i>Thông Tin Tài Khoản
                                    </h5>
                                    
                                    <div class="mb-3">
                                        <label for="username" class="form-label">
                                            <i class="fas fa-user-circle me-2"></i>Tên Đăng Nhập *
                                        </label>
                                        <input type="text" class="form-control" id="username" name="username" 
                                               placeholder="Nhập tên đăng nhập" required>
                                    </div>
                                    
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="password" class="form-label">
                                                <i class="fas fa-lock me-2"></i>Mật Khẩu *
                                            </label>
                                            <input type="password" class="form-control" id="password" name="password" 
                                                   placeholder="Nhập mật khẩu" required>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="confirmPassword" class="form-label">
                                                <i class="fas fa-lock me-2"></i>Xác Nhận Mật Khẩu *
                                            </label>
                                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                                                   placeholder="Nhập lại mật khẩu" required>
                                        </div>
                                    </div>
                                    
                                    <!-- Package Selection -->
                                    <div class="package-selection">
                                        <h6 class="text-primary mb-3">
                                            <i class="fas fa-crown me-2"></i>Chọn Gói Membership
                                        </h6>
                                        
                                        <%-- Mock data for packages using JSTL --%>
                                        <c:set var="packages" value="basic,premium,vip" />
                                        <c:set var="packageNames" value="Basic,Premium,VIP" />
                                        <c:set var="packagePrices" value="300K,500K,800K" />
                                        <c:set var="packageDescriptions" value="Sử dụng cơ bản|Tất cả + PT 2 buổi/tháng|Tất cả + PT không giới hạn" />
                                        
                                        <c:forEach var="i" begin="0" end="2">
                                            <div class="package-option" data-package="${i}">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="packageType" 
                                                           id="package${i}" value="${i == 0 ? 'basic' : (i == 1 ? 'premium' : 'vip')}">
                                                    <label class="form-check-label w-100" for="package${i}">
                                                        <div class="d-flex justify-content-between align-items-center">
                                                            <div>
                                                                <h6 class="mb-1">${i == 0 ? 'Basic' : (i == 1 ? 'Premium' : 'VIP')}</h6>
                                                                <small class="text-muted">
                                                                    ${i == 0 ? 'Sử dụng cơ bản' : (i == 1 ? 'Tất cả + PT 2 buổi/tháng' : 'Tất cả + PT không giới hạn')}
                                                                </small>
                                                            </div>
                                                            <div class="package-price">
                                                                ${i == 0 ? '300K' : (i == 1 ? '500K' : '800K')}/tháng
                                                            </div>
                                                        </div>
                                                    </label>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                    
                                    <!-- Emergency Contact -->
                                    <h6 class="text-primary mb-3">
                                        <i class="fas fa-phone-alt me-2"></i>Liên Hệ Khẩn Cấp
                                    </h6>
                                    
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="emergencyName" class="form-label">Tên Người Liên Hệ</label>
                                            <input type="text" class="form-control" id="emergencyName" name="emergencyName" 
                                                   placeholder="Tên người thân">
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="emergencyPhone" class="form-label">Số Điện Thoại</label>
                                            <input type="tel" class="form-control" id="emergencyPhone" name="emergencyPhone" 
                                                   placeholder="Số điện thoại khẩn cấp">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Terms and Conditions -->
                            <div class="mt-4">
                                <div class="form-check mb-3">
                                    <input class="form-check-input" type="checkbox" id="agreeTerms" name="agreeTerms" required>
                                    <label class="form-check-label" for="agreeTerms">
                                        Tôi đồng ý với <a href="#" class="text-decoration-none">Điều khoản và Điều kiện</a> 
                                        của Stamina Gym *
                                    </label>
                                </div>
                                
                                <div class="form-check mb-4">
                                    <input class="form-check-input" type="checkbox" id="receiveUpdates" name="receiveUpdates">
                                    <label class="form-check-label" for="receiveUpdates">
                                        Tôi muốn nhận thông tin cập nhật về các chương trình và ưu đãi từ Stamina Gym
                                    </label>
                                </div>
                            </div>
                            
                            <!-- Submit Buttons -->
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <button type="submit" class="btn btn-register w-100">
                                        <i class="fas fa-user-plus me-2"></i>
                                        <span id="registerText">Đăng Ký Thành Viên</span>
                                        <span id="registerSpinner" class="loading d-none"></span>
                                    </button>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <a href="${pageContext.request.contextPath}/views/login.jsp" 
                                       class="btn btn-outline-primary w-100">
                                        <i class="fas fa-sign-in-alt me-2"></i>Đã có tài khoản? Đăng nhập
                                    </a>
                                </div>
                            </div>
                        </form>
                        
                        <!-- Links -->
                        <div class="text-center mt-4">
                            <a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none">
                                <i class="fas fa-home me-1"></i>Về trang chủ
                            </a>
                            <span class="mx-2">|</span>
                            <a href="#" class="text-decoration-none">
                                <i class="fas fa-question-circle me-1"></i>Cần hỗ trợ?
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Package selection
            const packageOptions = document.querySelectorAll('.package-option');
            const packageRadios = document.querySelectorAll('input[name="packageType"]');
            
            packageOptions.forEach((option, index) => {
                option.addEventListener('click', function() {
                    // Remove selected class from all options
                    packageOptions.forEach(opt => opt.classList.remove('selected'));
                    // Add selected class to clicked option
                    this.classList.add('selected');
                    // Check the corresponding radio button
                    packageRadios[index].checked = true;
                });
            });
            
            // Form validation
            const form = document.getElementById('registerForm');
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');
            
            confirmPassword.addEventListener('input', function() {
                if (password.value !== confirmPassword.value) {
                    confirmPassword.setCustomValidity('Mật khẩu không khớp');
                } else {
                    confirmPassword.setCustomValidity('');
                }
            });
            
            // Form submission with loading state
            const registerText = document.getElementById('registerText');
            const registerSpinner = document.getElementById('registerSpinner');
            const submitButton = form.querySelector('button[type="submit"]');
            
            form.addEventListener('submit', function(e) {
                // Show loading state
                submitButton.disabled = true;
                registerText.classList.add('d-none');
                registerSpinner.classList.remove('d-none');
                
                // For demo purposes, simulate server delay
                // In real app, form will submit normally
                setTimeout(() => {
                    // Reset button state after demo delay
                    submitButton.disabled = false;
                    registerText.classList.remove('d-none');
                    registerSpinner.classList.add('d-none');
                }, 2000);
            });
            
            // Auto-hide alerts
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }, 5000);
            });
            
            // Phone number formatting
            const phoneInput = document.getElementById('phone');
            phoneInput.addEventListener('input', function(e) {
                // Remove all non-digits
                let value = e.target.value.replace(/\D/g, '');
                // Format as needed (simple example)
                if (value.length > 3) {
                    value = value.slice(0, 4) + '-' + value.slice(4, 7) + '-' + value.slice(7, 10);
                }
                e.target.value = value;
            });
        });
    </script>
</body>
</html>
