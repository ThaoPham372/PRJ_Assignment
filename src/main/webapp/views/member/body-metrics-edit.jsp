<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/views/common/header.jsp" %>

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
    }

    .hero-section {
        background: var(--gradient-primary);
        color: white;
        padding: 50px 0;
        margin-bottom: 40px;
        border-radius: 0 0 25px 25px;
        position: relative;
        overflow: hidden;
    }
    
    .hero-section::before {
        content: '';
        position: absolute;
        top: -30%;
        right: -10%;
        width: 400px;
        height: 400px;
        background: radial-gradient(circle, rgba(236, 139, 94, 0.2) 0%, transparent 70%);
        border-radius: 50%;
    }
    
    .hero-content {
        position: relative;
        z-index: 1;
    }

    .hero-section h1 {
        font-size: 2.8rem;
        font-weight: 800;
        margin-bottom: 15px;
        text-transform: uppercase;
        letter-spacing: 1px;
    }

    .hero-section p {
        font-size: 1.2rem;
        opacity: 0.95;
        font-weight: 400;
    }

    .form-card {
        background: var(--card);
        border-radius: 20px;
        box-shadow: 0 8px 30px var(--shadow);
        margin-bottom: 30px;
        overflow: hidden;
        transition: all 0.3s ease;
        border: 1px solid #f0f0f0;
    }
    
    .form-card:hover {
        box-shadow: 0 12px 40px var(--shadow-hover);
    }

    .form-card-header {
        background: linear-gradient(135deg, var(--primary-light) 0%, var(--primary) 100%);
        padding: 25px 35px;
        border-bottom: none;
        color: white;
    }

    .form-card-header h5 {
        color: white;
        font-weight: 700;
        margin: 0;
        display: flex;
        align-items: center;
        gap: 12px;
        font-size: 1.3rem;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }
    
    .form-card-header h5 i {
        font-size: 1.4rem;
        opacity: 0.9;
    }

    .form-card-body {
        padding: 40px;
        background: linear-gradient(to bottom, #ffffff 0%, #fafafa 100%);
    }
    
    @media (max-width: 768px) {
        .form-card-body {
            padding: 25px;
        }
        
        .hero-section {
            padding: 35px 0;
        }
        
        .hero-section h1 {
            font-size: 2rem;
        }
        
        .hero-section p {
            font-size: 1rem;
        }
    }

    .form-group {
        margin-bottom: 25px;
    }

    .form-label {
        font-weight: 700;
        color: var(--primary);
        margin-bottom: 10px;
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 0.95rem;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }
    
    .form-label i {
        color: var(--accent);
        font-size: 1.1rem;
    }

    .form-control {
        border: 2px solid #e9ecef;
        border-radius: 12px;
        padding: 15px 20px;
        font-size: 1.05rem;
        transition: all 0.3s ease;
        background: white;
        font-weight: 500;
    }

    .form-control:focus {
        border-color: var(--accent);
        box-shadow: 0 0 0 0.3rem rgba(236, 139, 94, 0.2);
        background: #fff;
        outline: none;
    }
    
    .form-control:hover {
        border-color: #d0d7de;
    }

    .form-control:readonly {
        background-color: var(--muted);
        color: var(--text-light);
    }

    .btn-primary {
        background: var(--gradient-accent);
        border: none;
        border-radius: 10px;
        padding: 12px 30px;
        font-weight: 600;
        transition: all 0.3s ease;
    }

    .btn-primary:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
    }

    .btn-secondary {
        background: #6c757d;
        border: none;
        border-radius: 10px;
        padding: 12px 30px;
        font-weight: 600;
        transition: all 0.3s ease;
    }

    .btn-secondary:hover {
        background: #5a6268;
        transform: translateY(-2px);
    }

    .btn-outline-secondary {
        border: 2px solid #6c757d;
        color: #6c757d;
        border-radius: 10px;
        padding: 12px 30px;
        font-weight: 600;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-block;
    }

    .btn-outline-secondary:hover {
        background: #6c757d;
        color: white;
        transform: translateY(-2px);
    }

    .alert {
        border-radius: 10px;
        border: none;
        padding: 15px 20px;
        margin-bottom: 20px;
    }

    .alert-success {
        background: #d4edda;
        color: #155724;
    }

    .alert-danger {
        background: #f8d7da;
        color: #721c24;
    }

    .bmi-display {
        background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        border-radius: 15px;
        padding: 30px;
        text-align: center;
        margin-top: 25px;
        border: 2px solid var(--accent);
        box-shadow: 0 4px 15px rgba(236, 139, 94, 0.2);
        position: relative;
        overflow: hidden;
    }
    
    .bmi-display::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 4px;
        background: var(--gradient-accent);
    }

    .bmi-value {
        font-size: 3rem;
        font-weight: 900;
        color: var(--accent);
        margin-bottom: 10px;
        text-shadow: 0 2px 10px rgba(236, 139, 94, 0.3);
    }

    .bmi-category {
        font-size: 1.3rem;
        font-weight: 700;
        margin-top: 10px;
        padding: 8px 20px;
        border-radius: 25px;
        display: inline-block;
    }

    .bmi-category.underweight { 
        color: #17a2b8; 
        background: rgba(23, 162, 184, 0.1);
        border: 2px solid #17a2b8;
    }
    .bmi-category.normal { 
        color: #28a745; 
        background: rgba(40, 167, 69, 0.1);
        border: 2px solid #28a745;
    }
    .bmi-category.overweight { 
        color: #ffc107; 
        background: rgba(255, 193, 7, 0.1);
        border: 2px solid #ffc107;
    }
    .bmi-category.obese { 
        color: #dc3545; 
        background: rgba(220, 53, 69, 0.1);
        border: 2px solid #dc3545;
    }

    .action-buttons {
        display: flex;
        gap: 15px;
        justify-content: center;
        margin-top: 30px;
        flex-wrap: wrap;
    }
    
    @media (max-width: 768px) {
        .action-buttons {
            flex-direction: column;
        }
        
        .action-buttons .btn {
            width: 100%;
        }
    }
</style>

<div class="hero-section">
    <div class="container">
        <div class="hero-content">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h1><i class="fas fa-ruler-combined me-3"></i>Cập nhật chỉ số cơ thể</h1>
                    <p>Chỉnh sửa thông tin về chiều cao, cân nặng và các chỉ số sức khỏe của bạn</p>
                </div>
                <div class="col-md-4 text-end">
                    <a href="${pageContext.request.contextPath}/member/dashboard" class="btn-back" style="border-color: white; color: white;">
                        <i class="fas fa-arrow-left"></i>
                        <span>Quay lại Dashboard</span>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <!-- Success/Error Messages -->
    <c:if test="${not empty success}">
        <div class="alert alert-success">
            <i class="fas fa-check-circle me-2"></i>
            <span>${success}</span>
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            <i class="fas fa-exclamation-circle me-2"></i>
            <span>${error}</span>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/member/body-metrics/update" method="post" id="bodyMetricsForm">
        <!-- Physical Information Card -->
        <div class="form-card">
            <div class="form-card-header">
                <h5><i class="fas fa-user-circle"></i>Thông tin thể chất</h5>
            </div>
            <div class="form-card-body">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="height" class="form-label">
                                <i class="fas fa-ruler-vertical me-2"></i>Chiều cao (cm)
                            </label>
                            <input type="number" 
                                   class="form-control" 
                                   id="height" 
                                   name="height" 
                                   value="${profileData.height != null ? profileData.height : ''}"
                                   min="100" 
                                   max="250" 
                                   step="0.1"
                                   placeholder="Nhập chiều cao của bạn">
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="weight" class="form-label">
                                <i class="fas fa-weight me-2"></i>Cân nặng (kg)
                            </label>
                            <input type="number" 
                                   class="form-control" 
                                   id="weight" 
                                   name="weight" 
                                   value="${profileData.weight != null ? profileData.weight : ''}"
                                   min="20" 
                                   max="300" 
                                   step="0.1"
                                   placeholder="Nhập cân nặng của bạn">
                        </div>
                    </div>
                </div>
                
                <!-- BMI Display -->
                <div class="bmi-display" id="bmiDisplay" style="display: none;">
                    <div class="bmi-value" id="bmiValue">--</div>
                    <div class="bmi-category" id="bmiCategory">--</div>
                </div>
            </div>
        </div>


        <!-- Action Buttons -->
        <div class="action-buttons">
            <button type="button" class="btn btn-secondary" onclick="resetForm()">
                <i class="fas fa-undo me-2"></i>Đặt lại
            </button>
            <a href="${pageContext.request.contextPath}/member/profile" class="btn btn-outline-secondary">
                <i class="fas fa-times me-2"></i>Hủy bỏ
            </a>
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-save me-2"></i>Lưu thay đổi
            </button>
        </div>
    </form>
</div>

<script>
    // BMI Calculation
    function calculateBMI() {
        const height = parseFloat(document.getElementById('height').value);
        const weight = parseFloat(document.getElementById('weight').value);
        
        if (height && weight && height > 0 && weight > 0) {
            const heightInMeters = height / 100;
            const bmi = weight / (heightInMeters * heightInMeters);
            const bmiRounded = bmi.toFixed(1);
            
            document.getElementById('bmiValue').textContent = bmiRounded;
            
            let category = '';
            let categoryClass = '';
            
            if (bmi < 18.5) {
                category = 'Thiếu cân';
                categoryClass = 'underweight';
            } else if (bmi < 25) {
                category = 'Bình thường';
                categoryClass = 'normal';
            } else if (bmi < 30) {
                category = 'Thừa cân';
                categoryClass = 'overweight';
            } else {
                category = 'Béo phì';
                categoryClass = 'obese';
            }
            
            document.getElementById('bmiCategory').textContent = category;
            document.getElementById('bmiCategory').className = 'bmi-category ' + categoryClass;
            document.getElementById('bmiDisplay').style.display = 'block';
        } else {
            document.getElementById('bmiDisplay').style.display = 'none';
        }
    }

    // Event listeners for BMI calculation
    document.getElementById('height').addEventListener('input', calculateBMI);
    document.getElementById('weight').addEventListener('input', calculateBMI);

    // Form reset function
    function resetForm() {
        if (confirm('Bạn có chắc chắn muốn đặt lại tất cả các thay đổi?')) {
            document.getElementById('bodyMetricsForm').reset();
            document.getElementById('bmiDisplay').style.display = 'none';
        }
    }

    // Form validation
    document.getElementById('bodyMetricsForm').addEventListener('submit', function(e) {
        const height = parseFloat(document.getElementById('height').value);
        const weight = parseFloat(document.getElementById('weight').value);
        
        if (height && (height < 100 || height > 250)) {
            alert('Chiều cao phải nằm trong khoảng 100-250 cm');
            e.preventDefault();
            return;
        }
        
        if (weight && (weight < 20 || weight > 300)) {
            alert('Cân nặng phải nằm trong khoảng 20-300 kg');
            e.preventDefault();
            return;
        }
    });

    // Calculate BMI on page load if values exist
    document.addEventListener('DOMContentLoaded', function() {
        calculateBMI();
    });
</script>

<%@ include file="/views/common/footer.jsp" %>


