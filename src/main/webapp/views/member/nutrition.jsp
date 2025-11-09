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

    /* Summary Card - Horizontal Layout */
    .summary-card {
        background: white;
        border-radius: 20px;
        padding: 30px;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
        margin-bottom: 25px;
    }

    .summary-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 30px;
        align-items: center;
    }

    .summary-item {
        text-align: center;
        padding: 15px;
        border-radius: 12px;
        background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
        transition: all 0.3s ease;
    }

    .summary-item:hover {
        transform: translateY(-3px);
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }

    .summary-item.calories {
        border-left: 4px solid #ff6b6b;
    }

    .summary-item.protein {
        border-left: 4px solid #4ecdc4;
    }

    .summary-item.carbs {
        border-left: 4px solid #ffe66d;
    }

    .summary-item.fat {
        border-left: 4px solid #95e1d3;
    }

    .summary-label {
        color: var(--text-light);
        font-size: 0.85rem;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        margin-bottom: 10px;
    }

    .summary-value {
        font-size: 2rem;
        font-weight: 900;
        margin: 8px 0;
        line-height: 1;
    }

    .summary-item.calories .summary-value {
        color: #ff6b6b;
    }

    .summary-item.protein .summary-value {
        color: #4ecdc4;
    }

    .summary-item.carbs .summary-value {
        color: #f39c12;
    }

    .summary-item.fat .summary-value {
        color: #95e1d3;
    }

    .summary-unit {
        font-size: 0.9rem;
        color: var(--text-light);
        font-weight: 600;
        margin-left: 5px;
    }

    /* Progress Bars */
    .progress-container {
        margin-top: 12px;
    }

    .progress-label {
        display: flex;
        justify-content: space-between;
        margin-bottom: 6px;
        font-size: 0.75rem;
        font-weight: 600;
        color: var(--text);
    }

    .progress {
        height: 8px;
        border-radius: 10px;
        background: var(--muted);
        overflow: hidden;
        box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .progress-bar {
        height: 100%;
        border-radius: 10px;
        transition: width 0.6s ease;
    }

    .progress-bar.calories-bar {
        background: linear-gradient(90deg, #ff6b6b 0%, #ff5252 100%);
        box-shadow: 0 2px 8px rgba(255, 107, 107, 0.4);
    }

    .progress-bar.protein-bar {
        background: linear-gradient(90deg, #4ecdc4 0%, #44b3ac 100%);
        box-shadow: 0 2px 8px rgba(78, 205, 196, 0.4);
    }

    /* Search Section */
    .search-section {
        background: linear-gradient(135deg, var(--primary) 0%, var(--primary-light) 100%);
        border-radius: 15px;
        padding: 20px;
        margin-bottom: 20px;
        color: white;
    }

    .search-form {
        margin-top: 15px;
    }

    .search-form .input-group {
        display: flex;
        gap: 10px;
    }

    .search-form input {
        flex: 1;
        border-radius: 25px;
        border: none;
        padding: 15px 25px;
        font-size: 1rem;
        transition: all 0.3s ease;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }

    .search-form input:focus {
        outline: none;
        box-shadow: 0 5px 25px rgba(236, 139, 94, 0.3);
        transform: translateY(-2px);
    }

    /* Food List */
    .food-list {
        max-height: 450px;
        overflow-y: auto;
        padding: 10px;
        background: var(--muted);
        border-radius: 15px;
        margin: 20px 0;
    }

    .food-list::-webkit-scrollbar {
        width: 8px;
    }

    .food-list::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 10px;
    }

    .food-list::-webkit-scrollbar-thumb {
        background: var(--accent);
        border-radius: 10px;
    }

    .food-item {
        padding: 18px;
        margin-bottom: 12px;
        background: white;
        border-radius: 12px;
        border: 2px solid transparent;
        transition: all 0.2s ease;
        cursor: pointer;
        position: relative;
    }

    .food-item:hover {
        border-color: var(--accent);
        box-shadow: 0 5px 20px rgba(236, 139, 94, 0.2);
        transform: translateX(5px);
    }

    .food-item input[type="radio"]:checked + label {
        color: var(--accent);
        font-weight: 700;
    }

    .food-item input[type="radio"] {
        margin-right: 12px;
        cursor: pointer;
        width: 20px;
        height: 20px;
        accent-color: var(--accent);
    }

    .food-item label {
        cursor: pointer;
        width: 100%;
        margin: 0;
        display: flex;
        align-items: center;
        font-size: 1rem;
    }

    .food-info {
        flex: 1;
    }

    .food-name {
        font-weight: 700;
        color: var(--text);
        margin-bottom: 5px;
    }

    .food-details {
        font-size: 0.9rem;
        color: var(--text-light);
    }

    .food-nutrition {
        text-align: right;
        color: var(--accent);
        font-weight: 600;
    }

    /* Servings Input */
    .servings-section {
        background: linear-gradient(135deg, #fff5f0 0%, #ffe8e0 100%);
        border-radius: 15px;
        padding: 20px;
        margin-top: 20px;
        border: 2px solid rgba(236, 139, 94, 0.2);
    }

    .servings-input {
        display: flex;
        align-items: center;
        gap: 15px;
        margin-top: 15px;
    }

    .servings-input label {
        font-weight: 700;
        color: var(--text);
        min-width: 120px;
    }

    .servings-input input {
        flex: 1;
        padding: 12px 20px;
        border: 2px solid var(--accent);
        border-radius: 10px;
        font-size: 1.1rem;
        font-weight: 600;
        text-align: center;
        transition: all 0.3s ease;
    }

    .servings-input input:focus {
        outline: none;
        box-shadow: 0 0 0 3px rgba(236, 139, 94, 0.2);
        transform: scale(1.05);
    }

    /* Buttons */
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

    .btn-delete {
        background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
        color: white;
        border: none;
        border-radius: 8px;
        padding: 8px 15px;
        font-size: 0.85rem;
        font-weight: 600;
        transition: all 0.3s ease;
        cursor: pointer;
        box-shadow: 0 3px 10px rgba(255, 107, 107, 0.3);
    }

    .btn-delete:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(255, 107, 107, 0.4);
        background: linear-gradient(135deg, #ee5a6f 0%, #dc3545 100%);
    }

    /* Meal Items */
    .meal-item {
        background: white;
        border-radius: 15px;
        padding: 20px;
        margin-bottom: 15px;
        border-left: 5px solid var(--accent);
        box-shadow: 0 3px 15px rgba(0, 0, 0, 0.08);
        transition: all 0.3s ease;
        position: relative;
        overflow: hidden;
    }

    .meal-item::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 5px;
        height: 100%;
        background: var(--gradient-accent);
    }

    .meal-item:hover {
        transform: translateX(5px);
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.12);
    }

    .meal-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 12px;
    }

    .meal-name {
        font-weight: 800;
        font-size: 1.2rem;
        color: var(--text);
        margin: 0;
    }

    .meal-time {
        color: var(--text-light);
        font-size: 0.9rem;
        display: flex;
        align-items: center;
        gap: 5px;
    }

    .meal-details {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 10px;
        padding-top: 15px;
        border-top: 1px solid var(--muted);
    }

    .meal-nutrition {
        display: flex;
        gap: 20px;
    }

    .meal-nutrition-item {
        text-align: center;
    }

    .meal-nutrition-value {
        font-size: 1.3rem;
        font-weight: 900;
        color: var(--accent);
        line-height: 1;
    }

    .meal-nutrition-label {
        font-size: 0.75rem;
        color: var(--text-light);
        text-transform: uppercase;
        margin-top: 5px;
    }

    .meal-servings {
        color: var(--text-light);
        font-size: 0.9rem;
    }

    /* Alerts */
    .alert {
        padding: 18px 25px;
        border-radius: 12px;
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
        border-left: 5px solid var(--success);
        color: #155724;
    }

    .alert-danger {
        background: linear-gradient(135deg, #f8d7da 0%, #f5c6cb 100%);
        border-left: 5px solid var(--danger);
        color: #721c24;
    }

    /* Empty States */
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

        .summary-grid {
            grid-template-columns: repeat(2, 1fr);
            gap: 15px;
        }

        .summary-value {
            font-size: 1.6rem;
        }

        .summary-card {
            padding: 20px;
        }

        .meal-header {
            flex-direction: column;
            gap: 10px;
        }

        .meal-details {
            flex-direction: column;
            align-items: flex-start;
            gap: 15px;
        }

        .servings-input {
            flex-direction: column;
            align-items: stretch;
        }

        .servings-input label {
            min-width: auto;
        }
    }

    @media (max-width: 480px) {
        .summary-grid {
            grid-template-columns: 1fr;
        }
    }

    /* Animation for totals */
    .summary-value {
        animation: countUp 1s ease-out;
    }

    @keyframes countUp {
        from {
            opacity: 0;
            transform: translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
</style>

<div class="nutrition-page">
    <div class="nutrition-container">
        <!-- Page Header -->
        <div class="nutrition-card text-center" style="background: var(--gradient-primary); color: white; padding: 25px; position: relative;">
            <h1 class="mb-2" style="color: white; font-weight: 900; font-size: 2rem;">🍎 Dinh Dưỡng Hôm Nay</h1>
            <p style="font-size: 1rem; opacity: 0.9; margin: 0;">Theo dõi lượng calo và dinh dưỡng hàng ngày một cách thông minh</p>
            <a href="${pageContext.request.contextPath}/member/nutrition/history" 
               class="btn-nutrition" 
               style="position: absolute; top: 20px; right: 20px; padding: 10px 20px; font-size: 0.9rem; z-index: 10;">
                <i class="fas fa-history"></i>
                <span>Lịch Sử</span>
            </a>
        </div>

        <!-- Success/Error Messages -->
        <c:if test="${not empty sessionScope.nutritionSuccess}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <span>${sessionScope.nutritionSuccess}</span>
                <c:remove var="nutritionSuccess" scope="session"/>
            </div>
        </c:if>
        <c:if test="${not empty sessionScope.nutritionError}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-circle"></i>
                <span>${sessionScope.nutritionError}</span>
                <c:remove var="nutritionError" scope="session"/>
            </div>
        </c:if>

        <!-- Summary Card - Horizontal Layout -->
        <div class="summary-card">
            <div class="summary-grid">
                <div class="summary-item calories">
                    <div class="summary-label">Calories</div>
                    <div class="summary-value">
                        <fmt:formatNumber value="${totals != null && totals.caloriesKcal != null ? totals.caloriesKcal : 0}" maxFractionDigits="0" />
                        <span class="summary-unit">kcal</span>
                    </div>
                    <div class="progress-container">
                        <div class="progress-label">
                            <span><fmt:formatNumber value="${targetCalories != null ? targetCalories : 2000}" maxFractionDigits="0" /> kcal</span>
                            <span>
                                <fmt:formatNumber value="${caloriesPercent != null ? caloriesPercent : 0}" maxFractionDigits="0" />%
                            </span>
                        </div>
                        <div class="progress">
                            <div class="progress-bar calories-bar" style="width: ${caloriesPercent != null ? (caloriesPercent > 100 ? 100 : caloriesPercent) : 0}%;"></div>
                        </div>
                    </div>
                </div>
                <div class="summary-item protein">
                    <div class="summary-label">Protein</div>
                    <div class="summary-value">
                        <fmt:formatNumber value="${totals != null && totals.proteinG != null ? totals.proteinG : 0}" maxFractionDigits="1" />
                        <span class="summary-unit">g</span>
                    </div>
                    <div class="progress-container">
                        <div class="progress-label">
                            <span><fmt:formatNumber value="${targetProtein != null ? targetProtein : 150}" maxFractionDigits="1" />g</span>
                            <span>
                                <fmt:formatNumber value="${proteinPercent != null ? proteinPercent : 0}" maxFractionDigits="0" />%
                            </span>
                        </div>
                        <div class="progress">
                            <div class="progress-bar protein-bar" style="width: ${proteinPercent != null ? (proteinPercent > 100 ? 100 : proteinPercent) : 0}%;"></div>
                        </div>
                    </div>
                </div>
                <c:if test="${totals.carbsG != null && totals.carbsG >= 0}">
                    <div class="summary-item carbs">
                        <div class="summary-label">Carbs</div>
                        <div class="summary-value">
                            <fmt:formatNumber value="${totals.carbsG}" maxFractionDigits="1" />
                            <span class="summary-unit">g</span>
                        </div>
                    </div>
                </c:if>
                <c:if test="${totals.fatG != null && totals.fatG >= 0}">
                    <div class="summary-item fat">
                        <div class="summary-label">Fat</div>
                        <div class="summary-value">
                            <fmt:formatNumber value="${totals.fatG}" maxFractionDigits="1" />
                            <span class="summary-unit">g</span>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="row">
            <!-- Left Column: Search and Add Food -->
            <div class="col-lg-6">
                <div class="nutrition-card">
                    <h4 class="nutrition-title">🔍 Tìm & Thêm Món Ăn</h4>
                    
                    <!-- Search Section -->
                    <div class="search-section">
                        <h5 style="color: white; margin-bottom: 10px; font-weight: 700;">Tìm kiếm món ăn</h5>
                        <form method="get" action="${pageContext.request.contextPath}/member/nutrition" class="search-form">
                            <div class="input-group">
                                <input type="text" 
                                       name="q" 
                                       placeholder="Nhập tên món ăn..." 
                                       value="${searchKeyword}"
                                       class="form-control"/>
                                <button type="submit" class="btn-nutrition" style="white-space: nowrap;">
                                    <i class="fas fa-search"></i>
                                    <span>Tìm</span>
                                </button>
                            </div>
                        </form>
                    </div>

                    <!-- Food List -->
                    <c:if test="${not empty foods}">
                        <form method="post" action="${pageContext.request.contextPath}/member/nutrition/addMeal" id="addMealForm">
                            <!-- Section Header -->
                            <div style="margin: 20px 0 15px 0; padding: 15px; background: linear-gradient(135deg, #fff5f0 0%, #ffe8e0 100%); border-radius: 12px; border-left: 4px solid var(--accent);">
                                <c:choose>
                                    <c:when test="${not empty searchKeyword}">
                                        <h5 style="margin: 0; color: var(--text); font-weight: 700; display: flex; align-items: center; gap: 10px;">
                                            <i class="fas fa-search" style="color: var(--accent);"></i>
                                            <span>Kết Quả Tìm Kiếm: "${fn:escapeXml(searchKeyword)}"</span>
                                            <span style="background: var(--accent); color: white; padding: 4px 12px; border-radius: 20px; font-size: 0.85rem; margin-left: auto;">
                                                ${foods.size()} món
                                            </span>
                                        </h5>
                                    </c:when>
                                    <c:otherwise>
                                        <h5 style="margin: 0; color: var(--text); font-weight: 700; display: flex; align-items: center; gap: 10px;">
                                            <i class="fas fa-utensils" style="color: var(--accent);"></i>
                                            <span>Món Ăn Phổ Biến</span>
                                            <span style="background: var(--accent); color: white; padding: 4px 12px; border-radius: 20px; font-size: 0.85rem; margin-left: auto;">
                                                ${foods.size()} món
                                            </span>
                                        </h5>
                                        <p style="margin: 8px 0 0 0; color: var(--text-light); font-size: 0.9rem;">
                                            <i class="fas fa-info-circle"></i> Tìm kiếm để xem thêm món ăn hoặc chọn một món từ danh sách bên dưới
                                        </p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            
                            <div class="food-list">
                                <c:forEach items="${foods}" var="food">
                                    <div class="food-item">
                                        <input type="radio" 
                                               name="foodId" 
                                               id="food_${food.id}"
                                               value="${food.id}" 
                                               required/>
                                        <label for="food_${food.id}">
                                            <div class="food-info">
                                                <div class="food-name">${food.name}</div>
                                                <div class="food-details">
                                                    <c:if test="${not empty food.servingLabel}">
                                                        ${food.servingLabel} • 
                                                    </c:if>
                                                    <fmt:formatNumber value="${food.calories}" maxFractionDigits="0" /> kcal
                                                    <c:if test="${food.proteinG != null && food.proteinG > 0}">
                                                        • <fmt:formatNumber value="${food.proteinG}" maxFractionDigits="1" />g protein
                                                    </c:if>
                                                </div>
                                            </div>
                                        </label>
                                    </div>
                                </c:forEach>
                            </div>

                            <div class="servings-section">
                                <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 10px;">
                                    <i class="fas fa-weight" style="color: var(--accent); font-size: 1.2rem;"></i>
                                    <strong style="color: var(--text); font-size: 1.1rem;">Khẩu phần</strong>
                                </div>
                                <div class="servings-input">
                                    <label for="servings">Số lượng:</label>
                                    <input type="number" 
                                           id="servings" 
                                           name="servings" 
                                           step="0.1" 
                                           min="0.1" 
                                           value="1.0" 
                                           required
                                           class="form-control"/>
                                </div>
                                <small style="color: var(--text-light); margin-top: 10px; display: block;">
                                    <i class="fas fa-info-circle"></i> Nhập số khẩu phần bạn muốn thêm (ví dụ: 1.0, 1.5, 2.0)
                                </small>
                            </div>

                            <div class="text-center mt-4">
                                <button type="submit" class="btn-nutrition" style="font-size: 1.1rem; padding: 16px 40px;">
                                    <i class="fas fa-plus-circle"></i>
                                    <span>Thêm Món Ăn</span>
                                </button>
                            </div>
                        </form>
                    </c:if>

                    <c:if test="${empty foods && not empty searchKeyword}">
                        <div class="empty-state">
                            <i class="fas fa-search"></i>
                            <p><strong>Không tìm thấy món ăn</strong></p>
                            <p>Không có kết quả nào cho từ khóa "${searchKeyword}"</p>
                            <p style="color: var(--text-light); font-size: 0.9rem;">Thử tìm kiếm với từ khóa khác</p>
                        </div>
                    </c:if>

                    <c:if test="${empty foods && empty searchKeyword}">
                        <div class="empty-state">
                            <i class="fas fa-utensils"></i>
                            <p><strong>Bắt đầu tìm kiếm món ăn</strong></p>
                            <p>Nhập tên món ăn vào ô tìm kiếm ở trên</p>
                        </div>
                    </c:if>
                </div>
            </div>

            <!-- Right Column: Today's Meals -->
            <div class="col-lg-6">
                <div class="nutrition-card">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px;">
                        <h4 class="nutrition-title" style="margin: 0;">📋 Bữa Ăn Hôm Nay</h4>
                        <c:if test="${not empty todayMeals}">
                            <span style="background: var(--accent); color: white; padding: 8px 15px; border-radius: 20px; font-weight: 700; font-size: 0.9rem;">
                                ${todayMeals.size()} món
                            </span>
                        </c:if>
                    </div>
                    
                    <c:choose>
                        <c:when test="${not empty todayMeals}">
                            <div style="max-height: 600px; overflow-y: auto; padding-right: 5px;">
                                <c:forEach items="${todayMeals}" var="meal">
                                    <div class="meal-item">
                                        <div class="meal-header">
                                            <div style="flex: 1;">
                                                <h5 class="meal-name">${meal.foodName}</h5>
                                                <div class="meal-time">
                                                    <i class="fas fa-clock"></i>
                                                    <span>
                                                        <c:choose>
                                                            <c:when test="${not empty meal.formattedEatenTime}">
                                                                ${meal.formattedEatenTime}
                                                            </c:when>
                                                            <c:otherwise>
                                                                --
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                    <span style="margin: 0 10px;">•</span>
                                                    <span class="meal-servings">
                                                        <fmt:formatNumber value="${meal.servings}" maxFractionDigits="1" /> khẩu phần
                                                    </span>
                                                </div>
                                            </div>
                                            <form method="post" action="${pageContext.request.contextPath}/member/nutrition/deleteMeal" 
                                                  style="margin: 0;"
                                                  onsubmit="return confirm('Bạn có chắc muốn xóa món ăn này?');">
                                                <input type="hidden" name="mealId" value="${meal.id}"/>
                                                <button type="submit" class="btn-delete">
                                                    <i class="fas fa-trash"></i> Xóa
                                                </button>
                                            </form>
                                        </div>
                                        <div class="meal-details">
                                            <div class="meal-nutrition">
                                                <div class="meal-nutrition-item">
                                                    <div class="meal-nutrition-value" style="color: #ff6b6b;">
                                                        <fmt:formatNumber value="${meal.totalCalories}" maxFractionDigits="0" />
                                                    </div>
                                                    <div class="meal-nutrition-label">Calories</div>
                                                </div>
                                                <div class="meal-nutrition-item">
                                                    <div class="meal-nutrition-value" style="color: #4ecdc4;">
                                                        <fmt:formatNumber value="${meal.totalProteinG}" maxFractionDigits="1" />
                                                    </div>
                                                    <div class="meal-nutrition-label">Protein (g)</div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="fas fa-clipboard-list"></i>
                                <p><strong>Chưa có bữa ăn nào</strong></p>
                                <p>Hãy tìm kiếm và thêm món ăn đầu tiên của bạn!</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    // Validate form before submit
    document.getElementById('addMealForm')?.addEventListener('submit', function(e) {
        const selectedFood = document.querySelector('input[name="foodId"]:checked');
        const servings = document.getElementById('servings').value;
        
        if (!selectedFood) {
            e.preventDefault();
            alert('⚠️ Vui lòng chọn một món ăn');
            return false;
        }
        
        if (!servings || parseFloat(servings) <= 0) {
            e.preventDefault();
            alert('⚠️ Khẩu phần phải lớn hơn 0');
            return false;
        }
    });

    // Smooth scroll for food list
    document.querySelectorAll('.food-item').forEach(item => {
        item.addEventListener('click', function() {
            const radio = this.querySelector('input[type="radio"]');
            if (radio) {
                radio.checked = true;
                this.style.borderColor = 'var(--accent)';
                this.style.boxShadow = '0 5px 20px rgba(236, 139, 94, 0.3)';
            }
        });
    });
</script>

<%@ include file="/views/common/footer.jsp" %>