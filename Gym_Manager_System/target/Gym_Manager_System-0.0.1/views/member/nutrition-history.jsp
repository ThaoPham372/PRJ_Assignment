<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
        --success: #28a745;
        --danger: #dc3545;
        --warning: #ffc107;
        --info: #17a2b8;
        --gradient-primary: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
    }

    .nutrition-page {
        background: linear-gradient(135deg, #f5f7fa 0%, #e9ecef 100%);
        min-height: 100vh;
        padding: 30px 0;
    }

    .nutrition-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 20px;
    }

    .nutrition-card {
        background: var(--card);
        border-radius: 20px;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
        padding: 25px;
        margin-bottom: 20px;
        transition: all 0.3s ease;
        border: 1px solid rgba(0, 0, 0, 0.05);
    }

    .nutrition-card:hover {
        transform: translateY(-3px);
        box-shadow: 0 15px 50px rgba(0, 0, 0, 0.12);
    }

    .nutrition-title {
        color: var(--text);
        font-weight: 800;
        margin-bottom: 18px;
        text-transform: uppercase;
        letter-spacing: 1px;
        font-size: 1.3rem;
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

    .btn-secondary {
        background: linear-gradient(135deg, #6c757d 0%, #5a6268 100%);
        color: white;
        border: none;
        border-radius: 25px;
        padding: 10px 20px;
        font-weight: 600;
        font-size: 0.9rem;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        box-shadow: 0 3px 10px rgba(108, 117, 125, 0.3);
    }

    .btn-secondary:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(108, 117, 125, 0.4);
        color: white;
    }

    /* Date Picker Section */
    .date-picker-card {
        background: linear-gradient(135deg, var(--primary) 0%, var(--primary-light) 100%);
        border-radius: 20px;
        padding: 30px;
        margin-bottom: 25px;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
        position: relative;
        overflow: hidden;
    }

    .date-picker-card::before {
        content: '';
        position: absolute;
        top: -50%;
        right: -50%;
        width: 200%;
        height: 200%;
        background: radial-gradient(circle, rgba(236, 139, 94, 0.1) 0%, transparent 70%);
        animation: rotate 20s linear infinite;
    }

    @keyframes rotate {
        from { transform: rotate(0deg); }
        to { transform: rotate(360deg); }
    }

    .date-picker-header {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 20px;
        position: relative;
        z-index: 1;
    }

    .date-picker-header i {
        font-size: 1.5rem;
        color: var(--accent);
        background: white;
        padding: 10px;
        border-radius: 12px;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }

    .date-picker-header h3 {
        color: white;
        font-weight: 800;
        font-size: 1.3rem;
        margin: 0;
        text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    }

    .date-picker-form {
        display: flex;
        gap: 15px;
        align-items: center;
        flex-wrap: wrap;
        position: relative;
        z-index: 1;
    }

    .date-input-wrapper {
        flex: 1;
        min-width: 250px;
        position: relative;
    }

    .date-input-wrapper label {
        display: block;
        color: rgba(255, 255, 255, 0.9);
        font-weight: 700;
        font-size: 0.9rem;
        margin-bottom: 8px;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }

    .date-input-wrapper input[type="date"] {
        width: 100%;
        padding: 14px 18px;
        border: none;
        border-radius: 12px;
        font-size: 1rem;
        font-weight: 600;
        background: white;
        color: var(--text);
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
        transition: all 0.3s ease;
        cursor: pointer;
    }

    .date-input-wrapper input[type="date"]:focus {
        outline: none;
        box-shadow: 0 5px 25px rgba(236, 139, 94, 0.4);
        transform: translateY(-2px);
    }

    .date-input-wrapper input[type="date"]::-webkit-calendar-picker-indicator {
        cursor: pointer;
        font-size: 1.2rem;
        opacity: 0.7;
        transition: opacity 0.3s ease;
    }

    .date-input-wrapper input[type="date"]::-webkit-calendar-picker-indicator:hover {
        opacity: 1;
    }

    .quick-date-buttons {
        display: flex;
        gap: 10px;
        flex-wrap: wrap;
        margin-top: 15px;
        position: relative;
        z-index: 1;
    }

    .quick-date-btn {
        padding: 10px 20px;
        border: 2px solid rgba(255, 255, 255, 0.3);
        border-radius: 25px;
        background: rgba(255, 255, 255, 0.15);
        color: white;
        font-weight: 600;
        font-size: 0.9rem;
        cursor: pointer;
        transition: all 0.3s ease;
        backdrop-filter: blur(10px);
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
    }

    .quick-date-btn:hover {
        background: rgba(255, 255, 255, 0.25);
        border-color: rgba(255, 255, 255, 0.5);
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        color: white;
    }

    .quick-date-btn.active {
        background: var(--gradient-accent);
        border-color: var(--accent);
        box-shadow: 0 5px 20px rgba(236, 139, 94, 0.4);
        color: white;
    }

    .date-submit-btn {
        background: var(--gradient-accent);
        color: white;
        border: none;
        border-radius: 12px;
        padding: 14px 30px;
        font-weight: 700;
        font-size: 1rem;
        transition: all 0.3s ease;
        cursor: pointer;
        box-shadow: 0 5px 20px rgba(236, 139, 94, 0.4);
        display: inline-flex;
        align-items: center;
        gap: 10px;
        white-space: nowrap;
        position: relative;
        z-index: 1;
    }

    .date-submit-btn:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 30px rgba(236, 139, 94, 0.5);
    }

    .date-submit-btn:active {
        transform: translateY(-1px);
    }

    /* Daily Summary Cards */
    .daily-summary-card {
        background: white;
        border-radius: 15px;
        padding: 20px;
        margin-bottom: 15px;
        box-shadow: 0 3px 15px rgba(0, 0, 0, 0.08);
        border-left: 5px solid var(--accent);
        transition: all 0.3s ease;
    }

    .daily-summary-card:hover {
        transform: translateX(5px);
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.12);
    }

    .daily-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
        padding-bottom: 15px;
        border-bottom: 2px solid var(--muted);
    }

    .daily-date {
        font-size: 1.2rem;
        font-weight: 800;
        color: var(--text);
    }

    .daily-stats {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
        gap: 15px;
        margin-top: 15px;
    }

    .daily-stat-item {
        text-align: center;
        padding: 10px;
        background: var(--muted);
        border-radius: 10px;
    }

    .daily-stat-value {
        font-size: 1.5rem;
        font-weight: 900;
        margin-bottom: 5px;
    }

    .daily-stat-label {
        font-size: 0.8rem;
        color: var(--text-light);
        font-weight: 600;
        text-transform: uppercase;
    }

    .daily-stat-item.calories .daily-stat-value {
        color: #ff6b6b;
    }

    .daily-stat-item.protein .daily-stat-value {
        color: #4ecdc4;
    }

    .daily-stat-item.carbs .daily-stat-value {
        color: #f39c12;
    }

    .daily-stat-item.fat .daily-stat-value {
        color: #95e1d3;
    }

    /* Meal Items in History */
    .meal-item-history {
        background: var(--muted);
        border-radius: 12px;
        padding: 15px;
        margin-bottom: 10px;
        margin-left: 30px;
        border-left: 3px solid var(--accent);
        display: flex;
        justify-content: space-between;
        align-items: center;
        transition: all 0.2s ease;
    }

    .meal-item-history:hover {
        background: white;
        box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
    }

    .meal-item-history .meal-info {
        flex: 1;
    }

    .meal-item-history .meal-name {
        font-weight: 700;
        color: var(--text);
        margin-bottom: 5px;
    }

    .meal-item-history .meal-details {
        font-size: 0.85rem;
        color: var(--text-light);
        display: flex;
        gap: 15px;
        align-items: center;
    }

    .meal-item-history .meal-nutrition {
        text-align: right;
    }

    .meal-item-history .meal-calories {
        font-size: 1.2rem;
        font-weight: 900;
        color: var(--accent);
        line-height: 1;
    }

    .meal-item-history .meal-time {
        font-size: 0.8rem;
        color: var(--text-light);
    }

    .empty-state {
        text-align: center;
        padding: 60px 20px;
        color: var(--text-light);
    }

    .empty-state i {
        font-size: 5em;
        margin-bottom: 20px;
        opacity: 0.3;
        color: var(--text-light);
    }

    .empty-state p {
        font-size: 1.1rem;
        margin: 10px 0;
    }

    /* Responsive */
    @media (max-width: 768px) {
        .nutrition-container {
            padding: 0 15px;
        }

        .nutrition-card {
            padding: 20px;
        }

        .date-picker-card {
            padding: 20px;
        }

        .date-picker-header h3 {
            font-size: 1.1rem;
        }

        .date-picker-form {
            flex-direction: column;
            align-items: stretch;
        }

        .date-input-wrapper {
            min-width: 100%;
        }

        .date-submit-btn {
            width: 100%;
            justify-content: center;
        }

        .quick-date-buttons {
            flex-direction: column;
        }

        .quick-date-btn {
            width: 100%;
            justify-content: center;
        }

        .daily-stats {
            grid-template-columns: repeat(2, 1fr);
        }

        .meal-item-history {
            margin-left: 0;
            flex-direction: column;
            align-items: flex-start;
            gap: 10px;
        }

        .meal-item-history .meal-nutrition {
            text-align: left;
            width: 100%;
        }
    }

    @media (max-width: 480px) {
        .daily-stats {
            grid-template-columns: 1fr;
        }

        .date-picker-header {
            flex-direction: column;
            align-items: flex-start;
            gap: 10px;
        }
    }
</style>

<div class="nutrition-page">
    <div class="nutrition-container">
        <!-- Page Header -->
        <div class="nutrition-card text-center" style="background: var(--gradient-primary); color: white; padding: 25px; position: relative;">
            <h1 class="mb-2" style="color: white; font-weight: 900; font-size: 2rem;">📊 Lịch Sử Dinh Dưỡng</h1>
            <p style="font-size: 1rem; opacity: 0.9; margin: 0;">Xem lại các món ăn và lượng calo đã nạp trong những ngày qua</p>
            <a href="${pageContext.request.contextPath}/member/nutrition" 
               class="btn-secondary" 
               style="position: absolute; top: 20px; right: 20px;">
                <i class="fas fa-arrow-left"></i>
                <span>Quay Lại</span>
            </a>
        </div>

        <!-- Date Picker -->
        <div class="date-picker-card">
            <div class="date-picker-header">
                <i class="fas fa-calendar-alt"></i>
                <h3>Chọn Ngày Xem Lịch Sử</h3>
            </div>
            
            <form method="get" action="${pageContext.request.contextPath}/member/nutrition/history" class="date-picker-form" id="datePickerForm">
                <div class="date-input-wrapper">
                    <label for="datePicker">
                        <i class="fas fa-calendar-day"></i> Ngày
                    </label>
                    <input 
                        type="date" 
                        id="datePicker" 
                        name="date" 
                        value="${selectedDateStr != null ? selectedDateStr : todayStr}"
                        max="${todayStr != null ? todayStr : ''}"
                    />
                </div>
                <button type="submit" class="date-submit-btn">
                    <i class="fas fa-search"></i>
                    <span>Xem Lịch Sử</span>
                </button>
            </form>

            <!-- Quick Date Buttons -->
            <div class="quick-date-buttons">
                <a href="javascript:void(0)" class="quick-date-btn" data-days="0" onclick="setQuickDateDays(0)">
                    <i class="fas fa-calendar-day"></i>
                    <span>Hôm Nay</span>
                </a>
                <a href="javascript:void(0)" class="quick-date-btn" data-days="1" onclick="setQuickDateDays(1)">
                    <i class="fas fa-chevron-left"></i>
                    <span>Hôm Qua</span>
                </a>
                <a href="javascript:void(0)" class="quick-date-btn" data-days="7" onclick="setQuickDateDays(7)">
                    <i class="fas fa-calendar-week"></i>
                    <span>7 Ngày Trước</span>
                </a>
                <a href="javascript:void(0)" class="quick-date-btn" data-days="30" onclick="setQuickDateDays(30)">
                    <i class="fas fa-calendar"></i>
                    <span>30 Ngày Trước</span>
                </a>
            </div>
        </div>

        <!-- History Content -->
        <c:choose>
            <c:when test="${not empty meals && meals.size() > 0}">
                <div class="nutrition-card">
                    <div class="daily-header">
                        <div class="daily-date">
                            <i class="fas fa-calendar-day"></i>
                            <c:set var="dateParts" value="${fn:split(selectedDateStr, '-')}"/>
                            <c:if test="${fn:length(dateParts) == 3}">
                                ${dateParts[2]}/${dateParts[1]}/${dateParts[0]}
                            </c:if>
                            <c:if test="${fn:length(dateParts) != 3}">
                                ${selectedDateStr}
                            </c:if>
                        </div>
                        <span style="background: var(--accent); color: white; padding: 6px 15px; border-radius: 20px; font-weight: 700; font-size: 0.9rem;">
                            ${meals.size()} món
                        </span>
                    </div>

                    <div class="daily-stats">
                        <div class="daily-stat-item calories">
                            <div class="daily-stat-value">
                                <fmt:formatNumber value="${totals.caloriesKcal}" maxFractionDigits="0" />
                            </div>
                            <div class="daily-stat-label">Calories</div>
                        </div>
                        <div class="daily-stat-item protein">
                            <div class="daily-stat-value">
                                <fmt:formatNumber value="${totals.proteinG}" maxFractionDigits="1" />
                            </div>
                            <div class="daily-stat-label">Protein (g)</div>
                        </div>
                        <c:if test="${totals.carbsG != null && totals.carbsG > 0}">
                            <div class="daily-stat-item carbs">
                                <div class="daily-stat-value">
                                    <fmt:formatNumber value="${totals.carbsG}" maxFractionDigits="1" />
                                </div>
                                <div class="daily-stat-label">Carbs (g)</div>
                            </div>
                        </c:if>
                        <c:if test="${totals.fatG != null && totals.fatG > 0}">
                            <div class="daily-stat-item fat">
                                <div class="daily-stat-value">
                                    <fmt:formatNumber value="${totals.fatG}" maxFractionDigits="1" />
                                </div>
                                <div class="daily-stat-label">Fat (g)</div>
                            </div>
                        </c:if>
                    </div>

                    <!-- Meals for this day -->
                    <div style="margin-top: 20px;">
                        <h6 style="color: var(--text-light); font-weight: 700; margin-bottom: 15px; text-transform: uppercase; font-size: 0.85rem;">
                            <i class="fas fa-utensils"></i> Các Món Đã Ăn:
                        </h6>
                        <c:forEach items="${meals}" var="meal">
                            <div class="meal-item-history">
                                <div class="meal-info">
                                    <div class="meal-name">${fn:escapeXml(meal.foodName)}</div>
                                    <div class="meal-details">
                                        <span>
                                            <i class="fas fa-weight"></i> 
                                            <fmt:formatNumber value="${meal.servings}" maxFractionDigits="1" /> khẩu phần
                                        </span>
                                        <span>
                                            <i class="fas fa-clock"></i> 
                                            ${meal.formattedEatenTime}
                                        </span>
                                        <c:if test="${meal.totalProteinG != null && meal.totalProteinG > 0}">
                                            <span>
                                                <i class="fas fa-dumbbell"></i> 
                                                <fmt:formatNumber value="${meal.totalProteinG}" maxFractionDigits="1" />g protein
                                            </span>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="meal-nutrition">
                                    <div class="meal-calories">
                                        <fmt:formatNumber value="${meal.totalCalories}" maxFractionDigits="0" /> kcal
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="nutrition-card">
                    <div class="empty-state">
                        <i class="fas fa-calendar-times"></i>
                        <p><strong>Không có món ăn nào được lưu ngày này</strong></p>
                        <p>
                            <c:choose>
                                <c:when test="${selectedDate != null}">
                                    Bạn chưa có bữa ăn nào được ghi nhận vào ngày ${selectedDateStr}
                                </c:when>
                                <c:otherwise>
                                    Vui lòng chọn ngày để xem lịch sử
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    // Set quick date by days ago
    function setQuickDateDays(daysAgo) {
        const datePicker = document.getElementById('datePicker');
        if (!datePicker) return;
        
        const today = new Date();
        const targetDate = new Date(today);
        targetDate.setDate(today.getDate() - daysAgo);
        
        // Format as YYYY-MM-DD
        const year = targetDate.getFullYear();
        const month = String(targetDate.getMonth() + 1).padStart(2, '0');
        const day = String(targetDate.getDate()).padStart(2, '0');
        const dateStr = `${year}-${month}-${day}`;
        
        datePicker.value = dateStr;
        
        // Update active state
        document.querySelectorAll('.quick-date-btn').forEach(btn => {
            btn.classList.remove('active');
            if (btn.getAttribute('data-days') === String(daysAgo)) {
                btn.classList.add('active');
            }
        });
        
        // Auto submit form
        document.getElementById('datePickerForm').submit();
    }

    // Set active state for current selected date
    document.addEventListener('DOMContentLoaded', function() {
        const selectedDate = '${selectedDateStr != null ? selectedDateStr : todayStr}';
        const todayStr = '${todayStr != null ? todayStr : ""}';
        const datePicker = document.getElementById('datePicker');
        
        if (datePicker && selectedDate) {
            datePicker.value = selectedDate;
            
            // Calculate which quick button should be active
            if (selectedDate && todayStr) {
                const selected = new Date(selectedDate + 'T00:00:00');
                const today = new Date(todayStr + 'T00:00:00');
                const diffTime = today - selected;
                const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
                
                // Highlight active quick button
                document.querySelectorAll('.quick-date-btn').forEach(btn => {
                    const days = parseInt(btn.getAttribute('data-days') || '-1');
                    if (days === diffDays) {
                        btn.classList.add('active');
                    }
                });
            }
        }

        // Add animation on load
        const datePickerCard = document.querySelector('.date-picker-card');
        if (datePickerCard) {
            datePickerCard.style.opacity = '0';
            datePickerCard.style.transform = 'translateY(-20px)';
            setTimeout(() => {
                datePickerCard.style.transition = 'all 0.5s ease';
                datePickerCard.style.opacity = '1';
                datePickerCard.style.transform = 'translateY(0)';
            }, 100);
        }
    });

    // Add smooth scroll to results after submit
    document.getElementById('datePickerForm')?.addEventListener('submit', function() {
        setTimeout(() => {
            const resultsCard = document.querySelector('.nutrition-card');
            if (resultsCard) {
                resultsCard.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
        }, 300);
    });
</script>

<%@ include file="/views/common/footer.jsp" %>

