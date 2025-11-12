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
        font-weight: 600;
        color: var(--text);
        margin-bottom: 8px;
        display: block;
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

    .form-control:readonly {
        background-color: var(--muted);
        color: var(--text-light);
    }

    .form-check {
        margin-bottom: 15px;
    }

    .form-check-input {
        margin-top: 0.25rem;
        margin-right: 0.5rem;
    }

    .form-check-label {
        font-weight: 500;
        color: var(--text);
        cursor: pointer;
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
                        <h1><i class="fas fa-bullseye me-2"></i>Cài đặt mục tiêu</h1>
                        <p>Thiết lập mục tiêu tập luyện và sức khỏe của bạn để có kế hoạch phù hợp</p>
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

    <form action="${pageContext.request.contextPath}/member/goals/update" method="post" id="goalsForm">
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
                        <div class="goal-option ${profileData.fitnessGoal == 'lose_weight' ? 'selected' : ''}" onclick="selectGoal('goal_lose_weight')">
                            <input type="radio" id="goal_lose_weight" name="fitnessGoal" value="lose_weight" 
                                   ${profileData.fitnessGoal == 'lose_weight' ? 'checked' : ''}>
                            <label for="goal_lose_weight">Giảm cân</label>
                        </div>
                        <div class="goal-option ${profileData.fitnessGoal == 'gain_muscle' ? 'selected' : ''}" onclick="selectGoal('goal_gain_muscle')">
                            <input type="radio" id="goal_gain_muscle" name="fitnessGoal" value="gain_muscle" 
                                   ${profileData.fitnessGoal == 'gain_muscle' ? 'checked' : ''}>
                            <label for="goal_gain_muscle">Tăng cơ bắp</label>
                        </div>
                        <div class="goal-option ${profileData.fitnessGoal == 'maintain' ? 'selected' : ''}" onclick="selectGoal('goal_maintain')">
                            <input type="radio" id="goal_maintain" name="fitnessGoal" value="maintain" 
                                   ${profileData.fitnessGoal == 'maintain' ? 'checked' : ''}>
                            <label for="goal_maintain">Duy trì sức khỏe</label>
                        </div>
                        <div class="goal-option ${profileData.fitnessGoal == 'improve_health' ? 'selected' : ''}" onclick="selectGoal('goal_improve_health')">
                            <input type="radio" id="goal_improve_health" name="fitnessGoal" value="improve_health" 
                                   ${profileData.fitnessGoal == 'improve_health' ? 'checked' : ''}>
                            <label for="goal_improve_health">Cải thiện sức khỏe</label>
                        </div>
                        <div class="goal-option ${profileData.fitnessGoal == 'athletic_performance' ? 'selected' : ''}" onclick="selectGoal('goal_athletic_performance')">
                            <input type="radio" id="goal_athletic_performance" name="fitnessGoal" value="athletic_performance" 
                                   ${profileData.fitnessGoal == 'athletic_performance' ? 'checked' : ''}>
                            <label for="goal_athletic_performance">Nâng cao thể lực</label>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="targetWeight" class="form-label">
                                <i class="fas fa-weight"></i>Cân nặng mục tiêu (kg)
                            </label>
                            <input type="number" 
                                   class="form-control" 
                                   id="targetWeight" 
                                   name="targetWeight" 
                                   value="${profileData.targetWeight != null ? profileData.targetWeight : ''}"
                                   min="20" 
                                   max="300" 
                                   step="0.1"
                                   placeholder="Nhập cân nặng mục tiêu">
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="trainingFrequency" class="form-label">
                                <i class="fas fa-calendar-week"></i>Tần suất tập luyện
                            </label>
                            <select class="form-control" id="trainingFrequency" name="trainingFrequency">
                                <option value="">Chọn tần suất tập</option>
                                <option value="1-2" ${profileData.trainingFrequency == '1-2' ? 'selected' : ''}>1-2 lần/tuần</option>
                                <option value="3-4" ${profileData.trainingFrequency == '3-4' ? 'selected' : ''}>3-4 lần/tuần</option>
                                <option value="5-6" ${profileData.trainingFrequency == '5-6' ? 'selected' : ''}>5-6 lần/tuần</option>
                                <option value="daily" ${profileData.trainingFrequency == 'daily' ? 'selected' : ''}>Hàng ngày</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Training Preferences Card -->
        <div class="form-card">
            <div class="form-card-header">
                <h5><i class="fas fa-clock"></i>Sở thích tập luyện</h5>
            </div>
            <div class="form-card-body">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="preferredTrainingTime" class="form-label">
                                <i class="fas fa-sun"></i>Thời gian tập ưa thích
                            </label>
                            <select class="form-control" id="preferredTrainingTime" name="preferredTrainingTime">
                                <option value="">Chọn thời gian tập</option>
                                <option value="morning" ${profileData.preferredTrainingTime == 'morning' ? 'selected' : ''}>Sáng sớm (6-9h)</option>
                                <option value="late_morning" ${profileData.preferredTrainingTime == 'late_morning' ? 'selected' : ''}>Cuối sáng (9-12h)</option>
                                <option value="afternoon" ${profileData.preferredTrainingTime == 'afternoon' ? 'selected' : ''}>Chiều (12-17h)</option>
                                <option value="evening" ${profileData.preferredTrainingTime == 'evening' ? 'selected' : ''}>Tối (17-21h)</option>
                                <option value="night" ${profileData.preferredTrainingTime == 'night' ? 'selected' : ''}>Đêm (21-24h)</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="activityLevel" class="form-label">
                                <i class="fas fa-running"></i>Mức độ hoạt động hiện tại
                            </label>
                            <select class="form-control" id="activityLevel" name="activityLevel">
                                <option value="">Chọn mức độ hoạt động</option>
                                <option value="sedentary" ${profileData.activityLevel == 'sedentary' ? 'selected' : ''}>Ít vận động</option>
                                <option value="light" ${profileData.activityLevel == 'light' ? 'selected' : ''}>Vận động nhẹ</option>
                                <option value="moderate" ${profileData.activityLevel == 'moderate' ? 'selected' : ''}>Vận động vừa phải</option>
                                <option value="active" ${profileData.activityLevel == 'active' ? 'selected' : ''}>Vận động nhiều</option>
                                <option value="very_active" ${profileData.activityLevel == 'very_active' ? 'selected' : ''}>Rất năng động</option>
                            </select>
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
                    <p id="previewWeight">Cân nặng mục tiêu: Chưa thiết lập</p>
                    <p id="previewFrequency">Tần suất tập: Chưa thiết lập</p>
                    <p id="previewTime">Thời gian tập: Chưa thiết lập</p>
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

    // Goal preview update
    function updateGoalPreview() {
        const selectedGoal = document.querySelector('input[name="fitnessGoal"]:checked');
        const targetWeight = document.getElementById('targetWeight').value;
        const trainingFrequency = document.getElementById('trainingFrequency').value;
        const preferredTime = document.getElementById('preferredTrainingTime').value;

        const goalTexts = {
            'lose_weight': 'Giảm cân',
            'gain_muscle': 'Tăng cơ bắp',
            'maintain': 'Duy trì sức khỏe',
            'improve_health': 'Cải thiện sức khỏe',
            'athletic_performance': 'Nâng cao thể lực'
        };

        const timeTexts = {
            'morning': 'Sáng sớm (6-9h)',
            'late_morning': 'Cuối sáng (9-12h)',
            'afternoon': 'Chiều (12-17h)',
            'evening': 'Tối (17-21h)',
            'night': 'Đêm (21-24h)'
        };

        document.getElementById('previewGoal').textContent = 
            selectedGoal ? goalTexts[selectedGoal.value] || 'Mục tiêu khác' : 'Chưa chọn mục tiêu';
        
        document.getElementById('previewWeight').textContent = 
            targetWeight ? `Cân nặng mục tiêu: ${targetWeight} kg` : 'Cân nặng mục tiêu: Chưa thiết lập';
        
        document.getElementById('previewFrequency').textContent = 
            trainingFrequency ? `Tần suất tập: ${trainingFrequency} lần/tuần` : 'Tần suất tập: Chưa thiết lập';
        
        document.getElementById('previewTime').textContent = 
            preferredTime ? `Thời gian tập: ${timeTexts[preferredTime] || preferredTime}` : 'Thời gian tập: Chưa thiết lập';
    }

    // Add event listeners
    document.addEventListener('DOMContentLoaded', function() {
        // Add event listeners for all form inputs
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
        
        document.getElementById('targetWeight').addEventListener('input', updateGoalPreview);
        document.getElementById('trainingFrequency').addEventListener('change', updateGoalPreview);
        document.getElementById('preferredTrainingTime').addEventListener('change', updateGoalPreview);
        
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
            document.getElementById('goalsForm').reset();
            updateGoalPreview();
        }
    }

    // Form validation
    document.getElementById('goalsForm').addEventListener('submit', function(e) {
        const targetWeight = parseFloat(document.getElementById('targetWeight').value);
        
        if (targetWeight && (targetWeight < 20 || targetWeight > 300)) {
            alert('Cân nặng mục tiêu phải nằm trong khoảng 20-300 kg');
            e.preventDefault();
            return;
        }
    });
</script>

<%@ include file="/views/common/footer.jsp" %>
