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

    .body-goals-page {
        background: linear-gradient(135deg, #f5f7fa 0%, #e9ecef 100%);
        min-height: 100vh;
        padding: 30px 0;
    }

    .body-goals-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 20px;
    }

    .form-card {
        background: var(--card);
        border-radius: 20px;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
        margin-bottom: 20px;
        transition: all 0.3s ease;
        border: 1px solid rgba(0, 0, 0, 0.05);
        overflow: hidden;
    }

    .form-card:hover {
        transform: translateY(-3px);
        box-shadow: 0 15px 50px rgba(0, 0, 0, 0.12);
    }

    .form-card-header {
        background: linear-gradient(135deg, var(--primary-light) 0%, var(--primary) 100%);
        padding: 20px 25px;
        border-bottom: none;
        color: white;
    }

    .form-card-header h5 {
        color: white;
        font-weight: 700;
        margin: 0;
        display: flex;
        align-items: center;
        gap: 10px;
        font-size: 1.1rem;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }
    
    .form-card-header h5 i {
        font-size: 1.2rem;
        opacity: 0.9;
    }

    .form-card-body {
        padding: 25px;
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

    .btn-nutrition {
        background: var(--gradient-accent);
        color: white;
        border: none;
        border-radius: 25px;
        padding: 14px 30px;
        font-weight: 700;
        font-size: 1rem;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        cursor: pointer;
        box-shadow: 0 5px 15px rgba(236, 139, 94, 0.3);
    }

    .btn-nutrition:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 25px rgba(236, 139, 94, 0.4);
        color: white;
    }

    .btn-nutrition:active {
        transform: translateY(-1px);
    }

    .btn-secondary-nutrition {
        background: linear-gradient(135deg, #6c757d 0%, #5a6268 100%);
        color: white;
        border: none;
        border-radius: 25px;
        padding: 14px 30px;
        font-weight: 700;
        font-size: 1rem;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        cursor: pointer;
        box-shadow: 0 5px 15px rgba(108, 117, 125, 0.3);
    }

    .btn-secondary-nutrition:hover {
        background: linear-gradient(135deg, #5a6268 0%, #495057 100%);
        transform: translateY(-3px);
        box-shadow: 0 8px 25px rgba(108, 117, 125, 0.4);
        color: white;
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
        padding: 18px 20px;
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

    .calories-display {
        background: linear-gradient(135deg, #fff5f0 0%, #ffe8e0 100%);
        border-radius: 15px;
        padding: 25px;
        margin-top: 20px;
        border: 2px solid rgba(236, 139, 94, 0.3);
        text-align: center;
    }

    .calories-display-label {
        font-size: 0.9rem;
        color: var(--text-light);
        text-transform: uppercase;
        letter-spacing: 0.5px;
        margin-bottom: 10px;
        font-weight: 600;
    }

    .calories-display-value {
        font-size: 2.5rem;
        font-weight: 900;
        color: var(--accent);
        margin-bottom: 5px;
    }

    .calories-display-unit {
        font-size: 1rem;
        color: var(--text-light);
        font-weight: 600;
    }

    .protein-display {
        margin-top: 15px;
        padding-top: 15px;
        border-top: 1px solid rgba(236, 139, 94, 0.2);
    }

    .protein-display-value {
        font-size: 1.5rem;
        font-weight: 700;
        color: var(--primary);
    }

    .action-buttons {
        display: flex;
        gap: 15px;
        justify-content: center;
        margin-top: 25px;
        flex-wrap: wrap;
        padding-top: 25px;
        border-top: 1px solid #e9ecef;
    }

    .alert {
        border-radius: 12px;
        border: none;
        padding: 18px 25px;
        margin-bottom: 25px;
        display: flex;
        align-items: center;
        gap: 12px;
        font-weight: 600;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        animation: slideIn 0.3s ease;
    }

    @keyframes slideIn {
        from {
            opacity: 0;
            transform: translateY(-10px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .alert-success {
        background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
        border-left: 5px solid #28a745;
        color: #155724;
    }

    .alert-danger {
        background: linear-gradient(135deg, #f8d7da 0%, #f5c6cb 100%);
        border-left: 5px solid #dc3545;
        color: #721c24;
    }
    
    @media (max-width: 768px) {
        .body-goals-container {
            padding: 0 15px;
        }

        .form-card-body {
            padding: 20px;
        }

        .action-buttons {
            flex-direction: column;
        }
        
        .action-buttons .btn-nutrition,
        .action-buttons .btn-secondary-nutrition {
            width: 100%;
            justify-content: center;
        }

        .goal-options {
            grid-template-columns: 1fr;
        }
    }

    @media (max-width: 480px) {
        .goal-options {
            grid-template-columns: 1fr;
        }
    }
</style>

<div class="body-goals-page">
    <div class="body-goals-container">
        <!-- Page Header -->
        <div class="form-card text-center" style="background: var(--gradient-primary); color: white; padding: 25px; position: relative;">
            <h1 class="mb-2" style="color: white; font-weight: 900; font-size: 2rem;">📊 Chỉ số & Mục tiêu</h1>
            <p style="font-size: 1rem; opacity: 0.9; margin: 0;">Cập nhật chỉ số cơ thể và thiết lập mục tiêu tập luyện của bạn</p>
            <a href="${pageContext.request.contextPath}/member/dashboard" 
               class="btn-nutrition" 
               style="position: absolute; top: 20px; right: 20px; padding: 10px 20px; font-size: 0.9rem; z-index: 10;">
                <i class="fas fa-arrow-left"></i>
                <span>Quay lại</span>
            </a>
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

    <form action="${pageContext.request.contextPath}/member/body-goals/update" method="post" id="bodyGoalsForm">
        <!-- Body Metrics Card -->
        <div class="form-card">
            <div class="form-card-header">
                <h5><i class="fas fa-ruler-combined"></i>Chỉ số cơ thể</h5>
            </div>
            <div class="form-card-body">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="height" class="form-label">
                                <i class="fas fa-ruler-vertical"></i>Chiều cao (cm)
                            </label>
                            <input type="number" 
                                   class="form-control" 
                                   id="height" 
                                   name="height" 
                                   value="${profileData.height != null ? profileData.height : ''}"
                                   min="100" 
                                   max="250" 
                                   step="0.1"
                                   placeholder="Nhập chiều cao (cm)"
                                   onchange="calculateBMI()">
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="weight" class="form-label">
                                <i class="fas fa-weight"></i>Cân nặng (kg)
                            </label>
                            <input type="number" 
                                   class="form-control" 
                                   id="weight" 
                                   name="weight" 
                                   value="${profileData.weight != null ? profileData.weight : ''}"
                                   min="20" 
                                   max="300" 
                                   step="0.1"
                                   placeholder="Nhập cân nặng (kg)"
                                   onchange="calculateBMI()">
                        </div>
                    </div>
                </div>

                <!-- BMI Display -->
                <div class="bmi-display" id="bmiDisplay" style="display: ${profileData.bmi != null ? 'block' : 'none'};">
                    <div class="bmi-value" id="bmiValue">
                        <c:if test="${profileData.bmi != null}">
                            <fmt:formatNumber value="${profileData.bmi}" maxFractionDigits="1" />
                        </c:if>
                    </div>
                    <div style="color: var(--text-light); margin-bottom: 10px;">Chỉ số BMI</div>
                    <div class="bmi-category" id="bmiCategory">
                        <c:if test="${not empty profileData.bmiCategory}">
                            ${profileData.bmiCategory}
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <!-- Goals Card -->
        <div class="form-card">
            <div class="form-card-header">
                <h5><i class="fas fa-bullseye"></i>Mục tiêu tập luyện</h5>
            </div>
            <div class="form-card-body">
                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-target"></i>Mục tiêu chính của bạn
                    </label>
                    <div class="goal-options">
                        <div class="goal-option ${nutritionGoal.goalType == 'giu dang' || empty nutritionGoal.goalType ? 'selected' : ''}" onclick="selectGoal('goal_giu_dang')">
                            <input type="radio" id="goal_giu_dang" name="goalType" value="giu dang" 
                                   ${nutritionGoal.goalType == 'giu dang' || empty nutritionGoal.goalType ? 'checked' : ''}
                                   onchange="updateCaloriesPreview()">
                            <label for="goal_giu_dang">Giữ dáng</label>
                        </div>
                        <div class="goal-option ${nutritionGoal.goalType == 'giam can' ? 'selected' : ''}" onclick="selectGoal('goal_giam_can')">
                            <input type="radio" id="goal_giam_can" name="goalType" value="giam can" 
                                   ${nutritionGoal.goalType == 'giam can' ? 'checked' : ''}
                                   onchange="updateCaloriesPreview()">
                            <label for="goal_giam_can">Giảm cân</label>
                        </div>
                        <div class="goal-option ${nutritionGoal.goalType == 'tang can' ? 'selected' : ''}" onclick="selectGoal('goal_tang_can')">
                            <input type="radio" id="goal_tang_can" name="goalType" value="tang can" 
                                   ${nutritionGoal.goalType == 'tang can' ? 'checked' : ''}
                                   onchange="updateCaloriesPreview()">
                            <label for="goal_tang_can">Tăng cân</label>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="activityFactor" class="form-label">
                        <i class="fas fa-running"></i>Hệ số hoạt động
                    </label>
                    <select class="form-control" id="activityFactor" name="activityFactor" onchange="updateCaloriesPreview()">
                        <c:set var="af" value="${nutritionGoal.activityFactor != null ? nutritionGoal.activityFactor : 1.55}" />
                        <option value="1.2" ${af == 1.2 ? 'selected' : ''}>Ít vận động (1.2)</option>
                        <option value="1.375" ${af == 1.375 ? 'selected' : ''}>Vận động nhẹ (1.375)</option>
                        <option value="1.55" ${af == 1.55 ? 'selected' : ''}>Vận động vừa (1.55)</option>
                        <option value="1.725" ${af == 1.725 ? 'selected' : ''}>Vận động nhiều (1.725)</option>
                        <option value="1.9" ${af == 1.9 ? 'selected' : ''}>Vận động rất nhiều (1.9)</option>
                    </select>
                    <small class="text-muted">Hệ số này phản ánh mức độ hoạt động hàng ngày của bạn</small>
                </div>

                <!-- Calories Preview -->
                <div class="calories-display" id="caloriesDisplay" style="display: ${nutritionGoal.dailyCaloriesTarget != null ? 'block' : 'none'};">
                    <div class="calories-display-label">Calories mục tiêu mỗi ngày</div>
                    <div class="calories-display-value" id="caloriesValue">
                        <c:if test="${nutritionGoal.dailyCaloriesTarget != null}">
                            <fmt:formatNumber value="${nutritionGoal.dailyCaloriesTarget}" maxFractionDigits="0" />
                        </c:if>
                    </div>
                    <div class="calories-display-unit">kcal</div>
                    <div class="protein-display">
                        <div class="calories-display-label">Protein mục tiêu</div>
                        <div class="protein-display-value" id="proteinValue">
                            <c:if test="${nutritionGoal.dailyProteinTarget != null}">
                                <fmt:formatNumber value="${nutritionGoal.dailyProteinTarget}" maxFractionDigits="1" />g
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Action Buttons -->
        <div class="action-buttons">
            <button type="submit" class="btn-nutrition">
                <i class="fas fa-save"></i>
                <span>Lưu thay đổi</span>
            </button>
            <a href="${pageContext.request.contextPath}/member/dashboard" class="btn-secondary-nutrition">
                <i class="fas fa-times"></i>
                <span>Hủy</span>
            </a>
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
            updateCaloriesPreview();
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
            bmiValue.textContent = bmi.toFixed(1);
            
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
            
            bmiCategory.textContent = category;
            bmiCategory.className = 'bmi-category ' + categoryClass;
            bmiDisplay.style.display = 'block';
            
            // Trigger calories preview update
            updateCaloriesPreview();
        } else {
            bmiDisplay.style.display = 'none';
        }
    }

    // Update calories preview (client-side calculation using same formula as server-side)
    function updateCaloriesPreview() {
        const height = parseFloat(document.getElementById('height').value);
        const weight = parseFloat(document.getElementById('weight').value);
        const goalType = document.querySelector('input[name="goalType"]:checked')?.value || 'giu dang';
        const activityFactorInput = document.getElementById('activityFactor');
        const activityFactor = activityFactorInput ? parseFloat(activityFactorInput.value) : 1.55;
        const caloriesDisplay = document.getElementById('caloriesDisplay');
        const caloriesValue = document.getElementById('caloriesValue');
        const proteinValue = document.getElementById('proteinValue');
        
        // Hide if no height/weight
        if (!height || !weight || isNaN(height) || isNaN(weight)) {
            if (caloriesDisplay) {
                // Only hide if there's no saved value from DB
                const savedCalories = caloriesValue ? caloriesValue.textContent.trim() : '';
                if (!savedCalories || savedCalories === '') {
                    caloriesDisplay.style.display = 'none';
                }
            }
            return;
        }
        
        // Validate activity factor
        if (!activityFactor || isNaN(activityFactor) || activityFactor <= 0) {
            if (caloriesDisplay) {
                const savedCalories = caloriesValue ? caloriesValue.textContent.trim() : '';
                if (!savedCalories || savedCalories === '') {
                    caloriesDisplay.style.display = 'none';
                }
            }
            return;
        }
        
        // BMR calculation using Harris-Benedict equation (same as server-side)
        // Male: BMR = 88.362 + (13.397 × weight) + (4.799 × height) - (5.677 × age)
        // Female: BMR = 447.593 + (9.247 × weight) + (3.098 × height) - (4.330 × age)
        // Using 25 as default age (same as server-side)
        const age = 25;
        const gender = '${profileData.gender}';
        let bmr;
        if (gender === 'Nam' || gender === 'Male') {
            // Male: BMR = 88.362 + (13.397 × weight) + (4.799 × height) - (5.677 × age)
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            // Female: BMR = 447.593 + (9.247 × weight) + (3.098 × height) - (4.330 × age)
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }
        
        // Calculate TDEE (Total Daily Energy Expenditure) = BMR × Activity Factor
        let tdee = bmr * activityFactor;
        
        // Apply goal adjustments (same as server-side)
        let caloriesTarget;
        if (goalType === 'giam can') {
            // Deficit: TDEE - 500 kcal
            caloriesTarget = tdee - 500;
        } else if (goalType === 'tang can') {
            // Surplus: TDEE + 500 kcal
            caloriesTarget = tdee + 500;
        } else {
            // "giu dang" (maintain): TDEE
            caloriesTarget = tdee;
        }
        
        // Ensure minimum calories (don't go below 1200) - same as server-side
        if (caloriesTarget < 1200) {
            caloriesTarget = 1200;
        }
        
        // Protein: 1.8g per kg (same as server-side)
        const protein = weight * 1.8;
        
        // Round to 2 decimal places for consistency, then format for display
        caloriesTarget = Math.round(caloriesTarget);
        caloriesValue.textContent = caloriesTarget.toLocaleString();
        proteinValue.textContent = protein.toFixed(1) + 'g';
        caloriesDisplay.style.display = 'block';
    }

    // Initialize on page load
    document.addEventListener('DOMContentLoaded', function() {
        calculateBMI();
        // Don't override DB values on initial load - only update when user changes values
        // The updateCaloriesPreview() will be called by event listeners when user edits
        
        // Add event listeners for radio buttons to update selected state
        document.querySelectorAll('.goal-option input[type="radio"]').forEach(radio => {
            radio.addEventListener('change', function() {
                document.querySelectorAll('.goal-option').forEach(opt => {
                    opt.classList.remove('selected');
                });
                if (this.checked) {
                    this.closest('.goal-option').classList.add('selected');
                }
                updateCaloriesPreview();
            });
        });

        // Ensure selected state is set on page load
        document.querySelectorAll('input[name="goalType"]:checked').forEach(radio => {
            radio.closest('.goal-option').classList.add('selected');
        });
    });
</script>

<%@ include file="/views/common/footer.jsp" %>

