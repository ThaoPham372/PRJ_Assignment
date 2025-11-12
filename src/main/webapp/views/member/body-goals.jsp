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

    .goals-page {
        background: linear-gradient(135deg, #f5f7fa 0%, #e9ecef 100%);
        min-height: 100vh;
        padding: 30px 0;
    }

    .goals-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 20px;
    }

    .hero-section {
        background: var(--gradient-primary);
        color: white;
        padding: 35px;
        margin-bottom: 25px;
        border-radius: 20px;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
        position: relative;
        overflow: hidden;
    }

    .hero-section::before {
        content: '';
        position: absolute;
        top: -30%;
        right: -10%;
        width: 300px;
        height: 300px;
        background: radial-gradient(circle, rgba(236, 139, 94, 0.2) 0%, transparent 70%);
        border-radius: 50%;
    }

    .hero-content {
        position: relative;
        z-index: 1;
    }

    .hero-section h1 {
        font-size: 2rem;
        font-weight: 800;
        margin-bottom: 10px;
        text-transform: uppercase;
        letter-spacing: 1px;
    }

    .hero-section p {
        font-size: 1rem;
        opacity: 0.9;
        margin: 0;
    }

    .form-card {
        background: var(--card);
        border-radius: 20px;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
        margin-bottom: 25px;
        overflow: hidden;
        transition: all 0.3s ease;
        border: 1px solid rgba(0, 0, 0, 0.05);
    }

    .form-card:hover {
        transform: translateY(-3px);
        box-shadow: 0 15px 50px rgba(0, 0, 0, 0.12);
    }

    .form-card-header {
        background: linear-gradient(135deg, var(--primary-light) 0%, var(--primary) 100%);
        padding: 25px 30px;
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
        font-size: 1.2rem;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }

    .form-card-header h5 i {
        font-size: 1.3rem;
        opacity: 0.9;
    }

    .form-card-body {
        padding: 30px;
        background: linear-gradient(to bottom, #ffffff 0%, #fafafa 100%);
    }

    .form-group {
        margin-bottom: 25px;
    }

    .form-label {
        font-weight: 700;
        color: var(--primary);
        margin-bottom: 12px;
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
        padding: 14px 18px;
        font-size: 1rem;
        transition: all 0.3s ease;
        background: white;
        font-weight: 500;
    }

    .form-control:focus {
        border-color: var(--accent);
        box-shadow: 0 0 0 0.3rem rgba(236, 139, 94, 0.2);
        outline: none;
    }

    .form-control:hover {
        border-color: #d0d7de;
    }

    .btn-primary {
        background: var(--gradient-accent);
        color: white;
        border: none;
        border-radius: 12px;
        padding: 14px 35px;
        font-weight: 700;
        font-size: 1rem;
        transition: all 0.3s ease;
        box-shadow: 0 5px 15px rgba(236, 139, 94, 0.3);
        cursor: pointer;
        display: inline-flex;
        align-items: center;
        gap: 8px;
    }

    .btn-primary:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 25px rgba(236, 139, 94, 0.4);
        color: white;
    }

    .btn-primary:active {
        transform: translateY(-1px);
    }

    .btn-secondary {
        background: linear-gradient(135deg, #6c757d 0%, #5a6268 100%);
        color: white;
        border: none;
        border-radius: 12px;
        padding: 14px 35px;
        font-weight: 700;
        font-size: 1rem;
        transition: all 0.3s ease;
        box-shadow: 0 5px 15px rgba(108, 117, 125, 0.3);
        cursor: pointer;
        display: inline-flex;
        align-items: center;
        gap: 8px;
    }

    .btn-secondary:hover {
        background: linear-gradient(135deg, #5a6268 0%, #495057 100%);
        transform: translateY(-3px);
        box-shadow: 0 8px 25px rgba(108, 117, 125, 0.4);
        color: white;
    }

    .btn-outline-secondary {
        border: 2px solid #6c757d;
        color: #6c757d;
        border-radius: 12px;
        padding: 12px 30px;
        font-weight: 600;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        background: white;
    }

    .btn-outline-secondary:hover {
        background: #6c757d;
        color: white;
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(108, 117, 125, 0.3);
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

    .goal-preview {
        background: var(--muted);
        border-radius: 10px;
        padding: 20px;
        margin-top: 15px;
    }

    .goal-preview h6 {
        color: var(--primary);
        font-weight: 600;
        margin-bottom: 10px;
    }

    .goal-preview p {
        margin-bottom: 5px;
        color: var(--text-light);
    }

    .action-buttons {
        display: flex;
        gap: 15px;
        justify-content: center;
        margin-top: 35px;
        flex-wrap: wrap;
        padding-top: 25px;
        border-top: 1px solid #e9ecef;
    }

    .goal-options {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 15px;
        margin-top: 15px;
    }

    .goal-option {
        background: white;
        border: 2px solid #e9ecef;
        border-radius: 12px;
        padding: 20px;
        transition: all 0.3s ease;
        cursor: pointer;
        position: relative;
        text-align: center;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    }

    .goal-option:hover {
        border-color: var(--accent);
        background: rgba(236, 139, 94, 0.08);
        box-shadow: 0 5px 20px rgba(236, 139, 94, 0.2);
        transform: translateY(-3px);
    }

    .goal-option.selected {
        border-color: var(--accent);
        background: rgba(236, 139, 94, 0.15);
        box-shadow: 0 5px 20px rgba(236, 139, 94, 0.3);
        transform: translateY(-2px);
    }

    .goal-option input[type="radio"] {
        position: absolute;
        opacity: 0;
        width: 0;
        height: 0;
    }

    .goal-option label {
        margin: 0;
        cursor: pointer;
        font-weight: 600;
        font-size: 1rem;
        color: var(--text);
        display: block;
        width: 100%;
        padding: 0;
    }

    .goal-option.selected label {
        color: var(--accent);
    }

    .bmi-display {
        background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
        border-radius: 15px;
        padding: 30px;
        margin-top: 20px;
        text-align: center;
        border: 2px solid var(--accent);
        box-shadow: 0 4px 15px rgba(236, 139, 94, 0.15);
        transition: all 0.3s ease;
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
        transform: scaleX(0);
        transform-origin: left;
        transition: transform 0.5s ease;
    }

    .bmi-display.has-value::before {
        transform: scaleX(1);
    }

    .bmi-display:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.2);
    }

    .bmi-label {
        color: var(--text-light);
        font-size: 0.9rem;
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 1px;
        margin-bottom: 15px;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
    }

    .bmi-label i {
        color: var(--accent);
        font-size: 1.1rem;
    }

    .bmi-value {
        font-size: 3rem;
        font-weight: 900;
        color: var(--accent);
        margin-bottom: 15px;
        min-height: 60px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s ease;
        line-height: 1;
    }

    .bmi-value.empty {
        font-size: 2rem;
        color: var(--text-light);
        font-weight: 500;
        font-style: italic;
    }

    .bmi-category {
        font-size: 1.1rem;
        font-weight: 600;
        color: var(--text);
        padding: 10px 20px;
        border-radius: 25px;
        display: inline-block;
        background: rgba(236, 139, 94, 0.1);
        transition: all 0.3s ease;
        min-height: 40px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
    }

    .bmi-category.empty {
        background: rgba(108, 117, 125, 0.1);
        color: var(--text-light);
        font-style: italic;
        font-weight: 500;
    }

    .bmi-category.underweight {
        background: rgba(23, 162, 184, 0.15);
        color: #17a2b8;
        border: 2px solid rgba(23, 162, 184, 0.3);
    }

    .bmi-category.normal {
        background: rgba(40, 167, 69, 0.15);
        color: #28a745;
        border: 2px solid rgba(40, 167, 69, 0.3);
    }

    .bmi-category.overweight {
        background: rgba(255, 193, 7, 0.15);
        color: #ffc107;
        border: 2px solid rgba(255, 193, 7, 0.3);
    }

    .bmi-category.obese {
        background: rgba(220, 53, 69, 0.15);
        color: #dc3545;
        border: 2px solid rgba(220, 53, 69, 0.3);
    }

    .bmi-info {
        margin-top: 15px;
        padding-top: 15px;
        border-top: 1px solid rgba(0, 0, 0, 0.1);
        font-size: 0.85rem;
        color: var(--text-light);
        line-height: 1.6;
    }

    @keyframes pulse {
        0%, 100% {
            opacity: 1;
        }
        50% {
            opacity: 0.6;
        }
    }

    .bmi-value.empty {
        animation: pulse 2s ease-in-out infinite;
    }

    @media (max-width: 768px) {
        .goals-container {
            padding: 0 15px;
        }

        .hero-section {
            padding: 25px;
        }

        .hero-section h1 {
            font-size: 1.6rem;
        }

        .form-card-body {
            padding: 20px;
        }
        
        .action-buttons {
            flex-direction: column;
        }
        
        .action-buttons .btn {
            width: 100%;
            justify-content: center;
        }

        .goal-options {
            grid-template-columns: 1fr;
        }

        .btn-outline-secondary {
            width: 100%;
            justify-content: center;
        }
    }
</style>

<div class="goals-page">
    <div class="goals-container">
        <!-- Page Header -->
        <div class="hero-section">
            <div class="hero-content">
                <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 15px;">
                    <div>
                        <h1><i class="fas fa-chart-line me-2"></i>Chỉ số & Mục tiêu</h1>
                        <p>Cập nhật chỉ số cơ thể và thiết lập mục tiêu tập luyện của bạn</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/member/dashboard" class="btn-outline-secondary" style="color: white; border-color: rgba(255,255,255,0.5); background: rgba(255,255,255,0.1);">
                        <i class="fas fa-arrow-left"></i>
                        <span>Quay lại</span>
                    </a>
                </div>
            </div>
        </div>
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

    <form action="${pageContext.request.contextPath}/member/body-goals" method="post" id="bodyGoalsForm">
        <!-- Body Metrics Card -->
        <div class="form-card">
            <div class="form-card-header">
                <h5><i class="fas fa-ruler-combined"></i>Chỉ số cơ thể</h5>
            </div>
            <div class="form-card-body">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="weight" class="form-label">
                                <i class="fas fa-weight"></i>Cân nặng (kg)
                            </label>
                            <input type="number" 
                                   class="form-control" 
                                   id="weight" 
                                   name="weight" 
                                   value="${member.weight != null ? member.weight : ''}"
                                   min="20" 
                                   max="300" 
                                   step="0.1"
                                   placeholder="Nhập cân nặng (kg)"
                                   oninput="calculateBMI(); updateCaloriesPreview()" 
                                   onchange="calculateBMI(); updateCaloriesPreview()">
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="height" class="form-label">
                                <i class="fas fa-ruler-vertical"></i>Chiều cao (cm)
                            </label>
                            <input type="number" 
                                   class="form-control" 
                                   id="height" 
                                   name="height" 
                                   value="${member.height != null ? member.height : ''}"
                                   min="100" 
                                   max="250" 
                                   step="0.1"
                                   placeholder="Nhập chiều cao (cm)"
                                   oninput="calculateBMI(); updateCaloriesPreview()" 
                                   onchange="calculateBMI(); updateCaloriesPreview()">
                        </div>
                    </div>
                </div>

                <!-- BMI Display - Always Visible -->
                <div class="bmi-display" id="bmiDisplay">
                    <div class="bmi-label">
                        <i class="fas fa-heartbeat"></i>
                        <span>Chỉ số BMI</span>
                    </div>
                    <div class="bmi-value ${member.bmi == null ? 'empty' : ''}" id="bmiValue">
                        <c:choose>
                            <c:when test="${member.bmi != null}">
                                <fmt:formatNumber value="${member.bmi}" maxFractionDigits="1" />
                            </c:when>
                            <c:otherwise>
                                <span>--</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="bmi-category ${empty bmiCategory ? 'empty' : ''}" id="bmiCategory">
                        <c:choose>
                            <c:when test="${not empty bmiCategory}">
                                ${bmiCategory}
                            </c:when>
                            <c:otherwise>
                                <span>Nhập chiều cao và cân nặng để tính BMI</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="bmi-info">
                        <i class="fas fa-info-circle"></i>
                        <span>BMI = Cân nặng (kg) / [Chiều cao (m)]²</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- Fitness Goals Card -->
        <div class="form-card">
            <div class="form-card-header">
                <h5><i class="fas fa-dumbbell"></i>Mục tiêu tập luyện</h5>
            </div>
            <div class="form-card-body">
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-target"></i>Mục tiêu chính của bạn
                    </label>
                    <div class="goal-options">
                        <div class="goal-option ${member.goal == 'lose_weight' ? 'selected' : ''}" onclick="selectGoal('goal_lose_weight')">
                            <input type="radio" id="goal_lose_weight" name="fitnessGoal" value="lose_weight" 
                                   ${member.goal == 'lose_weight' ? 'checked' : ''}>
                            <label for="goal_lose_weight">Giảm cân</label>
                        </div>
                        <div class="goal-option ${member.goal == 'gain_muscle' ? 'selected' : ''}" onclick="selectGoal('goal_gain_muscle')">
                            <input type="radio" id="goal_gain_muscle" name="fitnessGoal" value="gain_muscle" 
                                   ${member.goal == 'gain_muscle' ? 'checked' : ''}>
                            <label for="goal_gain_muscle">Tăng cơ bắp</label>
                        </div>
                        <div class="goal-option ${member.goal == 'maintain' ? 'selected' : ''}" onclick="selectGoal('goal_maintain')">
                            <input type="radio" id="goal_maintain" name="fitnessGoal" value="maintain" 
                                   ${member.goal == 'maintain' ? 'checked' : ''}>
                            <label for="goal_maintain">Duy trì sức khỏe</label>
                        </div>
                        <div class="goal-option ${member.goal == 'improve_health' ? 'selected' : ''}" onclick="selectGoal('goal_improve_health')">
                            <input type="radio" id="goal_improve_health" name="fitnessGoal" value="improve_health" 
                                   ${member.goal == 'improve_health' ? 'checked' : ''}>
                            <label for="goal_improve_health">Cải thiện sức khỏe</label>
                        </div>
                        <div class="goal-option ${member.goal == 'athletic_performance' ? 'selected' : ''}" onclick="selectGoal('goal_athletic_performance')">
                            <input type="radio" id="goal_athletic_performance" name="fitnessGoal" value="athletic_performance" 
                                   ${member.goal == 'athletic_performance' ? 'checked' : ''}>
                            <label for="goal_athletic_performance">Nâng cao thể lực</label>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Training Preferences Card -->
        <div class="form-card">
            <div class="form-card-header">
                <h5><i class="fas fa-clock"></i>Mức độ hoạt động</h5>
            </div>
            <div class="form-card-body">
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label for="activityLevel" class="form-label">
                                <i class="fas fa-running"></i>Mức độ hoạt động hiện tại
                            </label>
                            <select class="form-control" id="activityLevel" name="activityLevel" onchange="updateCaloriesPreview()">
                                <option value="">Chọn mức độ hoạt động</option>
                                <option value="sedentary" <c:if test="${currentActivityLevel == 'sedentary'}">selected</c:if>>Ít vận động</option>
                                <option value="light" <c:if test="${currentActivityLevel == 'light'}">selected</c:if>>Vận động nhẹ</option>
                                <option value="moderate" <c:if test="${currentActivityLevel == 'moderate'}">selected</c:if>>Vận động vừa phải</option>
                                <option value="active" <c:if test="${currentActivityLevel == 'active'}">selected</c:if>>Vận động nhiều</option>
                                <option value="very_active" <c:if test="${currentActivityLevel == 'very_active'}">selected</c:if>>Rất năng động</option>
                            </select>
                            <small class="text-muted" style="font-size: 0.85rem; display: block; margin-top: 5px;">
                                <i class="fas fa-info-circle"></i> Mức độ hoạt động này sẽ được sử dụng để tính toán calories mục tiêu
                            </small>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Goal Preview Card -->
        <div class="form-card">
            <div class="form-card-header">
                <h5><i class="fas fa-eye"></i>Xem trước mục tiêu</h5>
            </div>
            <div class="form-card-body">
                <div class="goal-preview" id="goalPreview">
                    <h6>Mục tiêu của bạn:</h6>
                    <p id="previewGoal">Chưa chọn mục tiêu</p>
                    <p id="previewActivity">Mức độ hoạt động: Chưa chọn</p>
                    
                    <!-- Calculated Nutrition Goals -->
                    <div id="nutritionPreview" style="display: none;">
                        <hr style="margin: 15px 0; border-color: #e9ecef;">
                        <h6 style="color: var(--accent); margin-top: 15px;">
                            <i class="fas fa-fire"></i> Mục tiêu dinh dưỡng đã tính toán:
                        </h6>
                        <p style="margin-bottom: 8px;">
                            <strong>Calories mục tiêu:</strong> 
                            <span id="previewCalories" style="color: var(--accent); font-size: 1.1rem; font-weight: 700;">
                                -- kcal/ngày
                            </span>
                        </p>
                        <p style="margin-bottom: 8px;">
                            <strong>Protein mục tiêu:</strong> 
                            <span id="previewProtein" style="color: var(--accent); font-size: 1.1rem; font-weight: 700;">
                                -- g/ngày
                            </span>
                        </p>
                    </div>
                    
                    <!-- Existing Nutrition Goal (if saved) -->
                    <c:if test="${not empty nutritionGoal}">
                        <hr style="margin: 15px 0; border-color: #e9ecef;">
                        <h6 style="color: var(--accent); margin-top: 15px;">
                            <i class="fas fa-check-circle"></i> Mục tiêu dinh dưỡng hiện tại:
                        </h6>
                        <p style="margin-bottom: 8px;">
                            <strong>Calories mục tiêu:</strong> 
                            <span style="color: var(--accent); font-size: 1.1rem; font-weight: 700;">
                                <fmt:formatNumber value="${nutritionGoal.dailyCaloriesTarget}" maxFractionDigits="0" /> kcal/ngày
                            </span>
                        </p>
                        <p style="margin-bottom: 8px;">
                            <strong>Protein mục tiêu:</strong> 
                            <span style="color: var(--accent); font-size: 1.1rem; font-weight: 700;">
                                <fmt:formatNumber value="${nutritionGoal.dailyProteinTarget}" maxFractionDigits="1" /> g/ngày
                            </span>
                        </p>
                        <p style="margin-bottom: 0; font-size: 0.9rem; color: var(--text-light);">
                            <i class="fas fa-info-circle"></i> Cập nhật lần cuối: 
                            <c:if test="${not empty nutritionGoal.updatedAtAsDate}">
                                <fmt:formatDate value="${nutritionGoal.updatedAtAsDate}" pattern="dd/MM/yyyy HH:mm" type="both" />
                            </c:if>
                        </p>
                    </c:if>
                </div>
            </div>
        </div>

        <!-- Action Buttons -->
        <div class="action-buttons">
            <button type="button" class="btn-secondary" onclick="resetForm()">
                <i class="fas fa-undo"></i>
                <span>Đặt lại</span>
            </button>
            <a href="${pageContext.request.contextPath}/member/dashboard" class="btn-outline-secondary">
                <i class="fas fa-times"></i>
                <span>Hủy bỏ</span>
            </a>
            <button type="submit" class="btn-primary">
                <i class="fas fa-save"></i>
                <span>Lưu thay đổi</span>
            </button>
        </div>
    </form>
    </div>
</div>

<script>
    // Function to select goal option (easier clicking)
    function selectGoal(goalId) {
        const radio = document.getElementById(goalId);
        if (radio) {
            radio.checked = true;
            // Update selected state for all options
            document.querySelectorAll('.goal-option').forEach(opt => {
                opt.classList.remove('selected');
            });
            radio.closest('.goal-option').classList.add('selected');
            updateGoalPreview();
        }
    }

    // Calculate BMI when height or weight changes
    function calculateBMI() {
        const height = parseFloat(document.getElementById('height').value);
        const weight = parseFloat(document.getElementById('weight').value);
        const bmiDisplay = document.getElementById('bmiDisplay');
        const bmiValue = document.getElementById('bmiValue');
        const bmiCategory = document.getElementById('bmiCategory');
        
        if (height > 0 && weight > 0) {
            const heightM = height / 100; // Convert cm to meters
            const bmi = weight / (heightM * heightM);
            
            // Update BMI value
            bmiValue.textContent = bmi.toFixed(1);
            bmiValue.classList.remove('empty');
            
            // Determine category and styling
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
            
            // Update category
            bmiCategory.textContent = category;
            bmiCategory.className = 'bmi-category ' + categoryClass;
            bmiCategory.classList.remove('empty');
            
            // Add has-value class for animation
            bmiDisplay.classList.add('has-value');
            
            // Update value color based on category
            const categoryColors = {
                'underweight': '#17a2b8',
                'normal': '#28a745',
                'overweight': '#ffc107',
                'obese': '#dc3545'
            };
            bmiValue.style.color = categoryColors[categoryClass] || '#ec8b5e';
        } else {
            // Reset to empty state
            bmiValue.innerHTML = '<span>--</span>';
            bmiValue.classList.add('empty');
            bmiValue.style.color = '';
            
            bmiCategory.innerHTML = '<span>Nhập chiều cao và cân nặng để tính BMI</span>';
            bmiCategory.className = 'bmi-category empty';
            
            bmiDisplay.classList.remove('has-value');
        }
    }

    // Goal preview update function
    function updateGoalPreview() {
        const selectedGoal = document.querySelector('input[name="fitnessGoal"]:checked');
        const activityLevel = document.getElementById('activityLevel').value;

        const goalTexts = {
            'lose_weight': 'Giảm cân',
            'gain_muscle': 'Tăng cơ bắp',
            'maintain': 'Duy trì sức khỏe',
            'improve_health': 'Cải thiện sức khỏe',
            'athletic_performance': 'Nâng cao thể lực'
        };

        const activityTexts = {
            'sedentary': 'Ít vận động',
            'light': 'Vận động nhẹ',
            'moderate': 'Vận động vừa phải',
            'active': 'Vận động nhiều',
            'very_active': 'Rất năng động'
        };

        document.getElementById('previewGoal').textContent = 
            selectedGoal ? goalTexts[selectedGoal.value] || 'Mục tiêu khác' : 'Chưa chọn mục tiêu';
        
        document.getElementById('previewActivity').textContent = 
            activityLevel ? `Mức độ hoạt động: ${activityTexts[activityLevel] || activityLevel}` : 'Mức độ hoạt động: Chưa chọn';
        
        updateCaloriesPreview();
    }
    
    // Calculate and update calories preview
    function updateCaloriesPreview() {
        const weight = parseFloat(document.getElementById('weight').value);
        const height = parseFloat(document.getElementById('height').value);
        const selectedGoal = document.querySelector('input[name="fitnessGoal"]:checked');
        const activityLevel = document.getElementById('activityLevel').value;
        const nutritionPreview = document.getElementById('nutritionPreview');
        
        if (weight > 0 && height > 0 && selectedGoal && activityLevel) {
            // Calculate BMR (Mifflin-St Jeor Equation)
            // BMR = 10 * weight(kg) + 6.25 * height(cm) - 5 * age + s
            // For preview, use default age 30 and gender M
            const age = 30; // Default for preview
            const gender = 'M'; // Default for preview
            let bmr = (10 * weight) + (6.25 * height) - (5 * age);
            bmr += (gender === 'M' ? 5 : -161);
            
            // Activity factors
            const activityFactors = {
                'sedentary': 1.2,
                'light': 1.375,
                'moderate': 1.55,
                'active': 1.725,
                'very_active': 1.9
            };
            
            const activityFactor = activityFactors[activityLevel] || 1.55;
            let tdee = bmr * activityFactor;
            
            // Adjust for goal
            if (selectedGoal.value === 'lose_weight') {
                tdee -= 500; // Deficit
            } else if (selectedGoal.value === 'gain_muscle') {
                tdee += 400; // Surplus
            }
            
            // Calculate protein (g per kg body weight)
            let proteinPerKg = 1.6;
            if (selectedGoal.value === 'lose_weight') {
                proteinPerKg = 2.0;
            } else if (selectedGoal.value === 'gain_muscle') {
                proteinPerKg = 2.2;
            }
            const protein = weight * proteinPerKg;
            
            // Update preview
            document.getElementById('previewCalories').textContent = Math.round(tdee) + ' kcal/ngày';
            document.getElementById('previewProtein').textContent = protein.toFixed(1) + ' g/ngày';
            nutritionPreview.style.display = 'block';
        } else {
            nutritionPreview.style.display = 'none';
        }
    }

    // Initialize on page load
    document.addEventListener('DOMContentLoaded', function() {
        // Set initial BMI display state if values exist
        const height = parseFloat(document.getElementById('height').value);
        const weight = parseFloat(document.getElementById('weight').value);
        const bmiDisplay = document.getElementById('bmiDisplay');
        const bmiValue = document.getElementById('bmiValue');
        const bmiCategory = document.getElementById('bmiCategory');
        
        // If BMI exists from server, add has-value class
        if (bmiValue && !bmiValue.classList.contains('empty')) {
            bmiDisplay.classList.add('has-value');
            
            // Set category color based on existing category
            const categoryText = bmiCategory.textContent.trim();
            if (categoryText.includes('Thiếu cân')) {
                bmiValue.style.color = '#17a2b8';
                bmiCategory.className = 'bmi-category underweight';
            } else if (categoryText.includes('Bình thường')) {
                bmiValue.style.color = '#28a745';
                bmiCategory.className = 'bmi-category normal';
            } else if (categoryText.includes('Thừa cân')) {
                bmiValue.style.color = '#ffc107';
                bmiCategory.className = 'bmi-category overweight';
            } else if (categoryText.includes('Béo phì')) {
                bmiValue.style.color = '#dc3545';
                bmiCategory.className = 'bmi-category obese';
            }
        }
        
        // Calculate BMI on load (will update if values exist)
        calculateBMI();
        
        // Add event listeners for goal radio buttons to update selected state
        document.querySelectorAll('input[name="fitnessGoal"]').forEach(radio => {
            radio.addEventListener('change', function() {
                // Update selected state
                document.querySelectorAll('.goal-option').forEach(opt => {
                    opt.classList.remove('selected');
                });
                if (this.checked) {
                    this.closest('.goal-option').classList.add('selected');
                }
                updateGoalPreview();
            });
        });
        
        // Add event listener for activity level
        const activityLevelSelect = document.getElementById('activityLevel');
        if (activityLevelSelect) {
            activityLevelSelect.addEventListener('change', function() {
                updateGoalPreview();
                updateCaloriesPreview();
            });
        }
        
        // Ensure selected state is set on page load
        document.querySelectorAll('input[name="fitnessGoal"]:checked').forEach(radio => {
            radio.closest('.goal-option').classList.add('selected');
        });
        
        // Initial preview update
        updateGoalPreview();
    });

    // Form reset function
    function resetForm() {
        if (confirm('Bạn có chắc chắn muốn đặt lại tất cả các thay đổi?')) {
            document.getElementById('bodyGoalsForm').reset();
            // Reset selected state
            document.querySelectorAll('.goal-option').forEach(opt => {
                opt.classList.remove('selected');
            });
            calculateBMI();
            updateGoalPreview();
        }
    }

    // Form validation
    document.getElementById('bodyGoalsForm').addEventListener('submit', function(e) {
        const fitnessGoal = document.querySelector('input[name="fitnessGoal"]:checked');
        const activityLevel = document.getElementById('activityLevel').value;
        const weight = parseFloat(document.getElementById('weight').value);
        const height = parseFloat(document.getElementById('height').value);
        
        if (!weight || weight < 20 || weight > 300) {
            alert('Vui lòng nhập cân nặng hợp lệ (20-300 kg)');
            e.preventDefault();
            return false;
        }
        
        if (!height || height < 100 || height > 250) {
            alert('Vui lòng nhập chiều cao hợp lệ (100-250 cm)');
            e.preventDefault();
            return false;
        }
        
        if (!fitnessGoal) {
            alert('Vui lòng chọn mục tiêu tập luyện');
            e.preventDefault();
            return false;
        }
        
        if (!activityLevel || activityLevel.trim() === '') {
            alert('Vui lòng chọn mức độ hoạt động');
            e.preventDefault();
            return false;
        }
    });
</script>

<%@ include file="/views/common/footer.jsp" %>
