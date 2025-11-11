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
        --danger: #dc3545;
        --gradient-primary: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
    }

    .member-card {
        background: var(--gradient-primary);
        color: white;
        border-radius: 0px;
        padding: 40px;
        margin-bottom: 30px;
        box-shadow: 0 10px 40px var(--shadow);
        position: relative;
        overflow: hidden;
    }
    
    .member-card::before {
        content: '';
        position: absolute;
        top: -50%;
        right: -10%;
        width: 300px;
        height: 300px;
        background: radial-gradient(circle, rgba(236, 139, 94, 0.2) 0%, transparent 70%);
        border-radius: 50%;
    }
    
    .member-card-content {
        position: relative;
        z-index: 1;
    }
    
    .member-avatar {
        width: 120px;
        height: 120px;
        border-radius: 50%;
        background: var(--gradient-accent);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 3rem;
        color: white;
        border: 5px solid rgba(255, 255, 255, 0.3);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
        overflow: hidden;
        flex-shrink: 0;
    }
    
    .member-avatar img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }
    
    .welcome-info {
        display: flex;
        align-items: center;
        gap: 30px;
    }
    
    .welcome-text h2 {
        font-size: 2.2rem;
        font-weight: 800;
        margin-bottom: 10px;
        text-transform: uppercase;
        letter-spacing: 1px;
    }
    
    .welcome-meta {
        display: flex;
        gap: 30px;
        margin-top: 15px;
        flex-wrap: wrap;
    }
    
    .welcome-meta-item {
        display: flex;
        align-items: center;
        gap: 8px;
        opacity: 0.95;
    }
    
    .welcome-meta-item i {
        opacity: 0.8;
    }
    
    .stat-card {
        background: var(--card);
        border-radius: 15px;
        box-shadow: 0 4px 15px var(--shadow);
        transition: all 0.3s ease;
        padding: 25px;
        margin-bottom: 20px;
    }
    
    .stat-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 25px var(--shadow-hover);
    }
    
    .activity-item {
        border-left: 4px solid var(--accent);
        padding-left: 20px;
        margin-bottom: 20px;
        background: var(--muted);
        border-radius: 8px;
        padding: 15px;
    }
    
    .session-item {
        background: var(--muted);
        border-radius: 10px;
        padding: 20px;
        margin-bottom: 15px;
        border: 1px solid #e9ecef;
        transition: all 0.3s ease;
    }
    
    .session-item:hover {
        background: var(--card);
        box-shadow: 0 4px 15px var(--shadow);
    }

    .dashboard-title {
        color: var(--text);
        font-weight: 800;
        margin-bottom: 30px;
        text-transform: uppercase;
        letter-spacing: 1px;
    }

    .stat-number {
        font-size: 2.5rem;
        font-weight: 900;
        color: var(--accent);
    }

    .stat-label {
        color: var(--text-light);
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }

    .btn-member {
        background: var(--gradient-accent);
        color: white;
        border: none;
        border-radius: 25px;
        padding: 12px 25px;
        font-weight: 600;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-block;
    }

    .btn-member:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
        color: white;
    }

    .member-nav {
        background: var(--gradient-primary);
        padding: 0;
        margin-bottom: 30px;
        border-radius: 20px;
        box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        overflow: hidden;
        position: relative;
    }

    .member-nav::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 4px;
        background: var(--gradient-accent);
    }

    .member-nav .nav-container {
        padding: 20px 30px;
    }

    .member-nav .nav-link {
        color: rgba(255, 255, 255, 0.9);
        font-weight: 600;
        padding: 12px 20px;
        border-radius: 12px;
        transition: background-color 0.2s ease, color 0.2s ease;
        margin: 0 6px;
        position: relative;
        border: 1px solid rgba(255, 255, 255, 0.1);
        text-decoration: none;
        min-width: 140px;
        text-align: center;
    }

    .member-nav .nav-link:hover {
        background: rgba(255, 255, 255, 0.1);
        color: white;
    }

    .member-nav .nav-link.active {
        background: var(--accent);
        color: white;
    }

    .member-nav .nav-link i {
        margin-right: 8px;
        font-size: 1em;
        opacity: 0.9;
    }

    .member-nav .nav-link.active i {
        opacity: 1;
    }

    .member-nav .nav-container {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-wrap: wrap;
        gap: 10px;
    }

    /* Statistics Row */
    .stats-row {
        display: flex;
        justify-content: space-between;
        gap: 20px;
        margin-bottom: 30px;
        flex-wrap: wrap;
    }

    .stat-item {
        flex: 1;
        min-width: 150px;
        background: var(--card);
        border-radius: 15px;
        padding: 25px;
        text-align: center;
        box-shadow: 0 4px 15px var(--shadow);
        transition: all 0.3s ease;
        border-left: 4px solid var(--accent);
    }
    
    .stat-item:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 25px var(--shadow-hover);
        border-left-width: 6px;
    }

    .stat-item .stat-label {
        font-size: 0.9rem;
        color: var(--text-light);
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        margin-bottom: 10px;
    }

    .stat-item .stat-value {
        font-size: 2rem;
        font-weight: 900;
        color: var(--accent);
    }

    /* Quick Actions Grid */
    .qa-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
        gap: 20px;
        margin-bottom: 30px;
    }
    
    /* Wrapper to create fixed space for hover effects */
    .qa-item-wrapper {
        position: relative;
        height: 100%;
        min-height: 140px;
    }

    .qa-item {
        background: var(--card);
        border-radius: 15px;
        padding: 25px 20px;
        text-align: center;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        transition: background 0.3s ease,
                    color 0.3s ease,
                    box-shadow 0.3s ease;
        text-decoration: none;
        color: var(--text);
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        gap: 8px;
        min-height: 140px;
        position: relative;
        border: 2px solid transparent;
        overflow: hidden;
        height: 100%;
        box-sizing: border-box;
    }

    .qa-item:hover {
        background: var(--gradient-accent);
        color: white;
        box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
    }

    .qa-item .qa-icon {
        width: 56px;
        height: 56px;
        border-radius: 14px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        background: var(--gradient-accent);
        color: white;
        box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
        flex-shrink: 0;
        font-size: 1.5rem;
        transition: all 0.3s ease;
    }

    .qa-item:hover .qa-icon {
        background: rgba(255, 255, 255, 0.25);
        color: #fff;
        box-shadow: 0 4px 15px rgba(255, 255, 255, 0.2);
    }

    .qa-item .qa-title {
        font-weight: 700;
        font-size: 1rem;
        line-height: 1.4;
        text-align: center;
    }
    
    .qa-item .qa-description {
        font-size: 0.85rem;
        opacity: 0;
        max-height: 0;
        overflow: hidden;
        text-align: center;
        margin-top: 0;
        transition: opacity 0.3s ease, max-height 0.3s ease, margin-top 0.3s ease;
        line-height: 1.4;
    }
    
    .qa-item:hover .qa-description {
        opacity: 0.9;
        max-height: 50px;
        margin-top: 8px;
    }
    
    
    .qa-section-title {
        font-size: 1.2rem;
        font-weight: 800;
        color: var(--primary);
        margin: 40px 0 20px 0;
        padding-bottom: 10px;
        border-bottom: 3px solid var(--accent);
        text-transform: uppercase;
        letter-spacing: 1px;
        display: flex;
        align-items: center;
        gap: 10px;
    }
    
    .qa-section-title:first-child {
        margin-top: 0;
    }
    
    .qa-search-container {
        margin-bottom: 30px;
        position: relative;
    }
    
    .qa-search-box {
        width: 100%;
        padding: 15px 50px 15px 20px;
        border: 2px solid #e9ecef;
        border-radius: 12px;
        font-size: 1rem;
        transition: all 0.3s ease;
        background: white;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    }
    
    .qa-search-box:focus {
        outline: none;
        border-color: var(--accent);
        box-shadow: 0 0 0 0.3rem rgba(236, 139, 94, 0.2);
    }
    
    .qa-search-box::placeholder {
        color: #adb5bd;
        font-style: italic;
    }
    
    .qa-search-icon {
        position: absolute;
        right: 18px;
        top: 50%;
        transform: translateY(-50%);
        color: var(--text-light);
        font-size: 1.2rem;
        pointer-events: none;
    }
    
    .qa-search-box:focus + .qa-search-icon {
        color: var(--accent);
    }
    
    .qa-search-clear {
        position: absolute;
        right: 50px;
        top: 50%;
        transform: translateY(-50%);
        background: var(--text-light);
        color: white;
        border: none;
        border-radius: 50%;
        width: 24px;
        height: 24px;
        display: none;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        font-size: 0.8rem;
        transition: all 0.3s ease;
    }
    
    .qa-search-clear:hover {
        background: var(--danger);
        transform: translateY(-50%) scale(1.1);
    }
    
    .qa-search-clear.visible {
        display: flex;
    }
    
    .qa-section.hidden {
        display: none;
    }
    
    .qa-item-wrapper.hidden {
        display: none;
    }
    
    .qa-item.hidden {
        display: none;
    }
    
    .qa-no-results {
        text-align: center;
        padding: 60px 20px;
        color: var(--text-light);
        display: none;
    }
    
    .qa-no-results.visible {
        display: block;
    }
    
    .qa-no-results i {
        font-size: 4rem;
        opacity: 0.3;
        margin-bottom: 20px;
        display: block;
    }
    
    .qa-no-results h4 {
        font-size: 1.5rem;
        font-weight: 700;
        margin-bottom: 10px;
        color: var(--text);
    }
    
    .qa-search-results-count {
        font-size: 0.9rem;
        color: var(--text-light);
        margin-bottom: 15px;
        font-weight: 600;
    }
    
    .qa-search-hint {
        font-size: 0.8rem;
        color: var(--text-light);
        margin-top: 8px;
        font-style: italic;
        display: flex;
        align-items: center;
        gap: 5px;
    }
    
    .qa-search-hint kbd {
        background: var(--muted);
        border: 1px solid #dee2e6;
        border-radius: 4px;
        padding: 2px 6px;
        font-family: monospace;
        font-size: 0.85rem;
        color: var(--text);
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
    }
    
    @media (max-width: 768px) {
        .qa-search-container {
            margin-bottom: 20px;
        }
        
        .qa-search-box {
            padding: 12px 45px 12px 15px;
            font-size: 0.95rem;
        }
        
        .qa-search-hint {
            display: none;
        }
    }
    
    .qa-item.quick-stat {
        min-height: 120px;
    }
    
    .qa-stat-value {
        font-size: 1.8rem;
        font-weight: 900;
        color: var(--accent);
        margin-top: 5px;
    }
    
    .qa-item:hover .qa-stat-value {
        color: white;
    }

    /* Responsive Grid */
    @media (max-width: 1200px) {
        .qa-grid {
            grid-template-columns: repeat(4, 1fr);
        }
    }

    @media (max-width: 992px) {
        .qa-grid {
            grid-template-columns: repeat(3, 1fr);
        }
        
        .stats-row {
            gap: 15px;
        }
        
        .stat-item {
            min-width: 130px;
        }
    }

    @media (max-width: 768px) {
        .member-card {
            padding: 30px 20px;
        }
        
        .welcome-info {
            flex-direction: column;
            text-align: center;
            gap: 20px;
        }
        
        .member-avatar {
            width: 100px;
            height: 100px;
            font-size: 2.5rem;
        }
        
        .welcome-text h2 {
            font-size: 1.8rem;
        }
        
        .welcome-meta {
            justify-content: center;
            flex-direction: column;
            gap: 15px;
        }
        
        .member-nav .nav-container {
            padding: 15px 20px;
        }
        
        .member-nav .nav-link {
            padding: 12px 20px;
            margin: 0 4px;
            font-size: 0.9rem;
        }
        
        .member-nav .nav-link i {
            margin-right: 8px;
            font-size: 1em;
        }

        .qa-grid {
            grid-template-columns: repeat(2, 1fr);
        }
        
        .qa-item-wrapper {
            min-height: 120px;
        }
        
        .qa-item {
            min-height: 120px;
            padding: 20px 15px;
        }
        
        .qa-item .qa-icon {
            width: 48px;
            height: 48px;
            font-size: 1.3rem;
        }
        
        .qa-section-title {
            font-size: 1rem;
            margin: 30px 0 15px 0;
        }

        .stats-row {
            flex-direction: column;
        }

        .stat-item {
            min-width: 100%;
        }
    }

    @media (max-width: 576px) {
        .member-nav .nav-container {
            padding: 10px 15px;
        }
        
        .member-nav .nav-link {
            padding: 10px 15px;
            margin: 0 2px;
            font-size: 0.85rem;
        }
        
        .member-nav .nav-link i {
            margin-right: 6px;
            font-size: 0.9em;
        }

        .qa-grid {
            grid-template-columns: 1fr;
        }
    }
</style>


<div class="container mt-4">
    <!-- Welcome Card -->
    <div class="member-card mb-4">
        <div class="member-card-content">
            <div class="welcome-info">
                <div class="member-avatar">
                    <c:choose>
                        <c:when test="${not empty dashboardData && not empty dashboardData.avatarUrl}">
                            <img src="<c:out value='${dashboardData.avatarUrl}'/>" alt="Avatar">
                        </c:when>
                        <c:otherwise>
                            <i class="fas fa-user"></i>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="welcome-text">
                    <h2>Xin chào, <c:out value="${not empty memberName ? memberName : (not empty member.name ? member.name : member.username)}"/>!</h2>
                    <div class="welcome-meta">
                        <div class="welcome-meta-item">
                            <i class="fas fa-crown"></i>
                            <span><strong>Trạng thái:</strong> Thành viên</span>
                        </div>
                        <div class="welcome-meta-item">
                            <i class="fas fa-calendar"></i>
                            <span><strong>Tham gia:</strong> 
                                <c:choose>
                                    <c:when test="${not empty joinDate}">
                                        <fmt:formatDate value="${joinDate}" pattern="dd/MM/yyyy"/>
                                    </c:when>
                                    <c:when test="${not empty member.createdDate}">
                                        <fmt:formatDate value="${member.createdDate}" pattern="dd/MM/yyyy"/>
                                    </c:when>
                                    <c:otherwise>N/A</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Statistics Cards -->
    <div class="stats-row">
        <div class="stat-item">
            <div class="stat-label">Chỉ số BMI</div>
            <div class="stat-value">
                <c:choose>
                    <c:when test="${not empty member.bmi}">
                        <fmt:formatNumber value="${member.bmi}" pattern="#,##0.0"/>
                    </c:when>
                    <c:otherwise>--</c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="stat-item">
            <div class="stat-label">Calories hôm nay</div>
            <div class="stat-value">
                <c:choose>
                    <c:when test="${not empty todayCalories}">
                        <fmt:formatNumber value="${todayCalories}" pattern="#,##0"/> kcal
                    </c:when>
                    <c:otherwise>0 kcal</c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="stat-item">
            <div class="stat-label">Tổng đã chi tiêu</div>
            <div class="stat-value">
                <c:choose>
                    <c:when test="${not empty totalSpent}">
                        <fmt:formatNumber value="${totalSpent}" pattern="#,##0"/> đ
                    </c:when>
                    <c:otherwise>0 đ</c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="stat-item">
            <div class="stat-label">Gói đang sở hữu</div>
            <div class="stat-value" style="font-size: 1.1rem;">
                <c:choose>
                    <c:when test="${not empty currentPackageName}">
                        <c:out value="${currentPackageName}"/>
                    </c:when>
                    <c:otherwise>Chưa có gói</c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="stat-card mb-4">
        <h5 class="dashboard-title mb-4">
            <i class="fas fa-bolt me-2"></i>Hành động nhanh
        </h5>
        
        <!-- Search Box -->
        <div class="qa-search-container">
            <input type="text" 
                   id="qaSearchBox" 
                   class="qa-search-box" 
                   placeholder="Tìm kiếm hành động nhanh... (VD: tập luyện, hồ sơ, membership)"
                   autocomplete="off">
            <i class="fas fa-search qa-search-icon"></i>
            <button type="button" class="qa-search-clear" id="qaSearchClear" onclick="clearQuickActionSearch()" title="Xóa tìm kiếm">
                <i class="fas fa-times"></i>
            </button>
        </div>
        
        <div class="qa-search-results-count" id="qaResultsCount"></div>
        
        <!-- No Results Message -->
        <div class="qa-no-results" id="qaNoResults">
            <i class="fas fa-search"></i>
            <h4>Không tìm thấy kết quả</h4>
            <p>Hãy thử tìm kiếm với từ khóa khác</p>
        </div>
        
        <!-- Tập luyện & Sức khỏe -->
        <h6 class="qa-section-title qa-section" data-section="training">
            <i class="fas fa-dumbbell"></i>
            Tập luyện & Sức khỏe
        </h6>
        <div class="qa-grid" data-section="training">
            <div class="qa-item-wrapper">
                <a class="qa-item" href="${pageContext.request.contextPath}/member/body-goals">
                    <span class="qa-icon"><i class="fas fa-user-circle"></i></span>
                    <span class="qa-title">Chỉ số & Mục tiêu</span>
                    <span class="qa-description">Cập nhật BMI, mục tiêu và calories</span>
                </a>
            </div>
            <div class="qa-item-wrapper">
                <a class="qa-item" href="${pageContext.request.contextPath}/member/nutrition">
                    <span class="qa-icon"><i class="fas fa-utensils"></i></span>
                    <span class="qa-title">Dinh dưỡng</span>
                    <span class="qa-description">Ghi nhận bữa ăn và calo</span>
                </a>
            </div>
        </div>
        
        <!-- Thông tin & Quản lý -->
        <h6 class="qa-section-title qa-section" data-section="management">
            <i class="fas fa-user-circle"></i>
            Thông tin & Quản lý
        </h6>
        <div class="qa-grid" data-section="management">
            <div class="qa-item-wrapper">
                <a class="qa-item" href="${pageContext.request.contextPath}/member/profile">
                    <span class="qa-icon"><i class="fas fa-id-card"></i></span>
                    <span class="qa-title">Hồ sơ cá nhân</span>
                    <span class="qa-description">Xem và chỉnh sửa thông tin</span>
                </a>
            </div>
            <div class="qa-item-wrapper">
                <a class="qa-item" href="${pageContext.request.contextPath}/member/schedule">
                    <span class="qa-icon"><i class="fas fa-calendar-alt"></i></span>
                    <span class="qa-title">Lịch tập</span>
                    <span class="qa-description">Xem lịch tập sắp tới</span>
                </a>
            </div>
            <div class="qa-item-wrapper">
                <a class="qa-item" href="${pageContext.request.contextPath}/member/membership">
                    <span class="qa-icon"><i class="fas fa-crown"></i></span>
                    <span class="qa-title">Membership</span>
                    <span class="qa-description">Quản lý gói thành viên</span>
                </a>
            </div>
        </div>
        
        <!-- Dịch vụ & Hỗ trợ -->
        <h6 class="qa-section-title qa-section" data-section="support">
            <i class="fas fa-handshake"></i>
            Dịch vụ & Hỗ trợ
        </h6>
        <div class="qa-grid" data-section="support">
            <div class="qa-item-wrapper">
                <a class="qa-item" href="${pageContext.request.contextPath}/products">
                    <span class="qa-icon"><i class="fas fa-cart-shopping"></i></span>
                    <span class="qa-title">Mua hàng</span>
                    <span class="qa-description">Mua sản phẩm và dịch vụ</span>
                </a>
            </div>
            <div class="qa-item-wrapper">
                <a class="qa-item" href="${pageContext.request.contextPath}/cart">
                    <span class="qa-icon"><i class="fas fa-shopping-cart"></i></span>
                    <span class="qa-title">Giỏ hàng</span>
                    <span class="qa-description">Xem và quản lý giỏ hàng</span>
                </a>
            </div>
            <div class="qa-item-wrapper">
                <a class="qa-item" href="${pageContext.request.contextPath}/order/list">
                    <span class="qa-icon"><i class="fas fa-file-invoice-dollar"></i></span>
                    <span class="qa-title">Hóa đơn</span>
                    <span class="qa-description">Xem lịch sử hóa đơn</span>
                </a>
            </div>
            <div class="qa-item-wrapper">
                <a class="qa-item" href="${pageContext.request.contextPath}/views/News_page/news_main.jsp">
                    <span class="qa-icon"><i class="fas fa-newspaper"></i></span>
                    <span class="qa-title">Tin tức</span>
                    <span class="qa-description">Tin tức và bài viết mới</span>
                </a>
            </div>
        </div>
    </div>


</div>

<script>
    // Quick Actions Search Functionality
    const searchBox = document.getElementById('qaSearchBox');
    const searchClear = document.getElementById('qaSearchClear');
    const resultsCount = document.getElementById('qaResultsCount');
    const noResults = document.getElementById('qaNoResults');
    
    if (searchBox) {
        // Detect OS for keyboard shortcut hint
        const isMac = navigator.platform.toUpperCase().indexOf('MAC') >= 0;
        const keyboardKey = document.getElementById('keyboardKey');
        if (keyboardKey) {
            keyboardKey.textContent = isMac ? 'Cmd' : 'Ctrl';
        }
        
        // Add search keywords as data attributes to each item
        document.querySelectorAll('.qa-item').forEach(item => {
            const title = item.querySelector('.qa-title')?.textContent.toLowerCase() || '';
            const description = item.querySelector('.qa-description')?.textContent.toLowerCase() || '';
            const keywords = (title + ' ' + description).trim();
            item.setAttribute('data-keywords', keywords);
        });
        
        searchBox.addEventListener('input', function(e) {
            const searchTerm = e.target.value.toLowerCase().trim();
            
            // Show/hide clear button
            if (searchTerm.length > 0) {
                searchClear.classList.add('visible');
            } else {
                searchClear.classList.remove('visible');
            }
            
            // Filter items
            let visibleCount = 0;
            let visibleSections = new Set();
            
            document.querySelectorAll('.qa-item-wrapper').forEach(wrapper => {
                const item = wrapper.querySelector('.qa-item');
                if (!item) return;
                
                const keywords = item.getAttribute('data-keywords') || '';
                const section = wrapper.closest('.qa-grid')?.getAttribute('data-section') || '';
                
                if (keywords.includes(searchTerm)) {
                    wrapper.classList.remove('hidden');
                    item.classList.remove('hidden');
                    visibleCount++;
                    if (section) {
                        visibleSections.add(section);
                    }
                } else {
                    wrapper.classList.add('hidden');
                    item.classList.add('hidden');
                }
            });
            
            // Show/hide section titles
            document.querySelectorAll('.qa-section').forEach(section => {
                const sectionKey = section.getAttribute('data-section');
                const sectionGrid = section.nextElementSibling;
                
                if (searchTerm.length === 0) {
                    // Show all sections when no search
                    section.classList.remove('hidden');
                    if (sectionGrid) sectionGrid.classList.remove('hidden');
                } else {
                    // Show section only if it has visible items
                    if (visibleSections.has(sectionKey)) {
                        section.classList.remove('hidden');
                        if (sectionGrid) sectionGrid.classList.remove('hidden');
                    } else {
                        section.classList.add('hidden');
                        if (sectionGrid) sectionGrid.classList.add('hidden');
                    }
                }
            });
            
            // Update results count
            if (searchTerm.length > 0) {
                if (visibleCount === 0) {
                    resultsCount.textContent = '';
                    noResults.classList.add('visible');
                } else {
                    resultsCount.textContent = 'Tìm thấy ' + visibleCount + ' kết quả' + (visibleCount > 1 ? '' : '');
                    noResults.classList.remove('visible');
                }
                
                // Highlight search terms in results
                document.querySelectorAll('.qa-item:not(.hidden) .qa-title').forEach(titleEl => {
                    const originalText = titleEl.getAttribute('data-original') || titleEl.textContent;
                    if (!titleEl.hasAttribute('data-original')) {
                        titleEl.setAttribute('data-original', originalText);
                    }
                    // Escape special regex characters, including $ and {
                    const escapedTerm = searchTerm.replace(/[.*+?^$()|[\]\\]/g, function(match) {
                        return '\\' + match;
                    }).replace(/\{/g, '\\{').replace(/\}/g, '\\}');
                    const regex = new RegExp('(' + escapedTerm + ')', 'gi');
                    titleEl.innerHTML = originalText.replace(regex, '<mark style="background: rgba(236, 139, 94, 0.3); padding: 2px 4px; border-radius: 3px;">$1</mark>');
                });
            } else {
                resultsCount.textContent = '';
                noResults.classList.remove('visible');
                
                // Reset highlights
                document.querySelectorAll('.qa-item .qa-title').forEach(titleEl => {
                    const originalText = titleEl.getAttribute('data-original');
                    if (originalText) {
                        titleEl.textContent = originalText;
                        titleEl.removeAttribute('data-original');
                    }
                });
            }
        });
        
        // Clear search
        window.clearQuickActionSearch = function() {
            searchBox.value = '';
            searchBox.dispatchEvent(new Event('input'));
            searchBox.focus();
        };
        
        // Keyboard shortcut (Ctrl/Cmd + K to focus search)
        document.addEventListener('keydown', function(e) {
            if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
                e.preventDefault();
                searchBox.focus();
                searchBox.select();
            }
            
            // ESC to clear search
            if (e.key === 'Escape' && document.activeElement === searchBox) {
                clearQuickActionSearch();
            }
        });
    }
</script>

<!-- ==================== GYMFIT AI CHATBOT ==================== -->
<style>
    :root {
        --primary-dark-cb: #141a46; 
        --primary-accent-cb: #ec8b5a;
        --bot-bg-cb: #e5e7eb;
        --container-bg-cb: #ffffff;
        --text-light-cb: #f9fafb;
        --text-dark-cb: #1f2937;
    }

    .chat-popup {
        display: none;
        position: fixed;
        bottom: 100px;
        right: 30px;
        z-index: 9999 !important;
        opacity: 0;
        transform: translateY(20px);
        transition: opacity 0.3s ease, transform 0.3s ease;
    }

    .chat-popup.show {
        display: block;
        opacity: 1;
        transform: translateY(0);
    }

    .chat-container {
        width: 370px;
        height: 80vh;
        max-height: 600px;
        background: var(--container-bg-cb);
        border-radius: 16px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        display: flex;
        flex-direction: column;
        overflow: hidden;
        border: 1px solid #e5e7eb;
        font-family: 'Inter', sans-serif;
    }

    .chat-header {
        background-color: var(--primary-dark-cb);
        color: var(--text-light-cb);
        padding: 16px 20px;
        font-size: 18px;
        font-weight: 600;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-bottom: 1px solid var(--primary-dark-cb);
    }

    .chat-header .logo { font-size: 28px; margin-right: 12px; }
    .chat-header .title-group { display: flex; align-items: center; }
    .chat-header .title { display: flex; flex-direction: column; line-height: 1.2; }
    .chat-header .status { font-size: 13px; font-weight: 400; color: #22c55e; display: flex; align-items: center; margin-top: 4px; }
    .chat-header .status-dot { width: 8px; height: 8px; background-color: #22c55e; border-radius: 50%; margin-right: 6px; animation: pulse-dot 1.5s infinite ease-in-out; }
    .chat-header .close-btn { font-size: 28px; color: var(--text-light-cb); cursor: pointer; transition: transform 0.2s; }
    .chat-header .close-btn:hover { transform: scale(1.2); }

    @keyframes pulse-dot { 0%, 100% { opacity: 1; } 50% { opacity: 0.5; } }

    .chat-box {
        flex: 1;
        padding: 16px;
        background-color: #f9fafb;
        overflow-y: auto;
        scroll-behavior: smooth;
        display: flex;
        flex-direction: column;
        gap: 10px;
    }
    .chat-box::-webkit-scrollbar { width: 6px; }
    .chat-box::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 3px; }

    .message {
        padding: 10px 16px;
        margin: 0;
        line-height: 1.5;
        max-width: 80%;
        word-wrap: break-word;
        font-size: 15px;
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
        animation: fadeInMsg 0.3s ease-out;
    }
    @keyframes fadeInMsg { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

    .message.user {
        background-color: var(--primary-accent-cb);
        color: var(--text-light-cb);
        align-self: flex-end;
        border-radius: 18px 18px 4px 18px;
    }
    .message.bot {
        background-color: var(--bot-bg-cb);
        color: var(--text-dark-cb);
        align-self: flex-start;
        border-radius: 18px 18px 18px 4px;
    }
    .message.error { background-color: #fee2e2; color: #b91c1c; align-self: flex-start; border-radius: 18px; padding: 10px 16px; }

    .input-area {
        display: flex;
        align-items: center;
        padding: 12px 16px;
        border-top: 1px solid #e5e7eb;
        background-color: var(--container-bg-cb);
        gap: 10px;
    }
    #userInput {
        flex: 1;
        padding: 12px 18px;
        border: 1px solid #cbd5e1;
        border-radius: 22px;
        outline: none;
        font-size: 15px;
        transition: all 0.2s ease;
    }
    #userInput:focus {
        border-color: var(--primary-dark-cb);
        box-shadow: 0 0 0 3px rgba(20, 26, 70, 0.1);
    }
    .send-button {
        background-color: var(--primary-accent-cb);
        color: white;
        border: none;
        border-radius: 50%;
        width: 44px;
        height: 44px;
        cursor: pointer;
        transition: background-color 0.2s ease;
        display: flex; justify-content: center; align-items: center; flex-shrink: 0;
    }
    .send-button:hover { filter: brightness(90%); }
    .send-button:disabled { 
        opacity: 0.5; 
        cursor: not-allowed; 
        filter: none;
    }
    .send-button svg { width: 22px; height: 22px; transform: translate(1px, -1px); }
    
    .typing-indicator { padding: 12px 18px; }
    .typing-indicator .dot {
        display: inline-block; width: 8px; height: 8px; border-radius: 50%;
        background-color: #6b7280; margin: 0 2px;
        animation: typing-bounce 1.2s infinite ease-in-out;
    }
    .typing-indicator .dot:nth-child(2) { animation-delay: -0.2s; }
    .typing-indicator .dot:nth-child(3) { animation-delay: -0.4s; }
    @keyframes typing-bounce { 0%, 60%, 100% { transform: translateY(0); } 30% { transform: translateY(-4px); } }

    .clear-chat-btn {
        background: none;
        border: none;
        color: var(--text-light-cb);
        font-size: 14px;
        cursor: pointer;
        opacity: 0.8;
        transition: opacity 0.2s;
    }
    .clear-chat-btn:hover { opacity: 1; }
    
    .floating-btn.chat-bot {
        position: fixed;
        bottom: 30px;
        right: 30px;
        background: linear-gradient(135deg, #ffde59 0%, #f5d040 100%);
        color: #141a46;
        border: none;
        border-radius: 50px;
        padding: 16px 28px;
        font-size: 16px;
        font-weight: 700;
        cursor: pointer;
        box-shadow: 0 8px 24px rgba(255, 222, 89, 0.4);
        transition: all 0.3s ease;
        z-index: 9998;
        display: flex;
        align-items: center;
        gap: 10px;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }
    
    .floating-btn.chat-bot:hover {
        transform: translateY(-3px);
        box-shadow: 0 12px 32px rgba(255, 222, 89, 0.6);
    }
    
    .floating-btn.chat-bot i {
        font-size: 20px;
    }
</style>

<button class="floating-btn chat-bot" id="chatToggleButton" title="Mở Chat AI">
    <i class="fas fa-comments"></i> GYMFIT AI
</button>

<div class="chat-popup" id="chatPopup">
    <div class="chat-container">
        <div class="chat-header">
            <div class="title-group">
                <div class="logo">🤖</div>
                <div class="title">
                    <div>GymFit AI</div>
                    <div class="status">
                        <span class="status-dot"></span> Đang hoạt động
                    </div>
                </div>
            </div>
            <div style="display: flex; align-items: center; gap: 10px;">
                <button class="clear-chat-btn" id="clearChatBtn" title="Xóa lịch sử chat">
                    🗑️
                </button>
                <span class="close-btn" id="closeChatBtn">×</span>
            </div>
        </div>

        <div id="chatBox" class="chat-box">
            <!-- Chat messages will be loaded here -->
        </div>

        <div class="input-area">
            <input id="userInput" type="text" placeholder="Nhập câu hỏi của bạn..." autocomplete="off" maxlength="500">
            <button class="send-button" id="sendBtn" title="Gửi tin nhắn">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M3.478 2.405a.75.75 0 00-.926.94l2.432 7.905H13.5a.75.75 0 010 1.5H4.984l-2.432 7.905a.75.75 0 00.926.94 60.519 60.519 0 0018.445-8.986.75.75 0 000-1.218A60.517 60.517 0 003.478 2.405z" />
                </svg>
            </button>
        </div>
    </div>
</div>

<script>
(function() {
    const chatPopup = document.getElementById("chatPopup");
    const chatToggleButton = document.getElementById("chatToggleButton");
    const closeChatBtn = document.getElementById("closeChatBtn");
    const clearChatBtn = document.getElementById("clearChatBtn");
    const userInput = document.getElementById("userInput");
    const chatBox = document.getElementById("chatBox");
    const sendBtn = document.getElementById("sendBtn");

    if (!chatPopup || !chatToggleButton || !closeChatBtn || !userInput || !chatBox || !sendBtn) {
        console.warn("[ChatBot] Some elements not found");
        return;
    }

    // Chat history persistence với sessionStorage - chỉ duy trì trong phiên hiện tại
    const STORAGE_KEY = "gymfit_chat_session";
    let chatHistory = [];
    let isLoading = false;

    // Load history từ sessionStorage (chỉ tồn tại trong phiên)
    function loadChatHistory() {
        try {
            const stored = sessionStorage.getItem(STORAGE_KEY);
            chatHistory = stored ? JSON.parse(stored) : [];
            
            if (chatHistory.length > 50) {
                chatHistory = chatHistory.slice(-50);
                saveChatHistory();
            }
            
            displayChatHistory();
        } catch (e) {
            console.error("[ChatBot] Error loading chat history:", e);
            chatHistory = [];
            addWelcomeMessage();
        }
    }

    function saveChatHistory() {
        try {
            sessionStorage.setItem(STORAGE_KEY, JSON.stringify(chatHistory));
        } catch (e) {
            console.error("[ChatBot] Error saving chat history:", e);
        }
    }

    function displayChatHistory() {
        chatBox.innerHTML = '';
        
        if (chatHistory.length === 0) {
            addWelcomeMessage();
        } else {
            chatHistory.forEach(msg => {
                appendMessage(msg.type, msg.text, false, false);
            });
        }
        
        scrollToBottom();
    }

    function addWelcomeMessage() {
        const welcomeMsg = "Chào bạn! Tôi là GymFit AI 💪<br>Hãy hỏi tôi về tập luyện, dinh dưỡng, các gói tập hoặc thông tin phòng gym nhé!";
        chatHistory = [{ type: 'bot', text: welcomeMsg, timestamp: Date.now() }];
        saveChatHistory();
        displayChatHistory();
    }

    function clearChatHistory() {
        if (confirm("Bạn có chắc muốn xóa toàn bộ lịch sử chat không?")) {
            chatHistory = [];
            sessionStorage.removeItem(STORAGE_KEY);
            addWelcomeMessage();
        }
    }

    chatToggleButton.addEventListener("click", () => {
        chatPopup.classList.toggle("show");
        if (chatPopup.classList.contains("show")) {
            setTimeout(() => userInput.focus(), 300);
        }
    });

    closeChatBtn.addEventListener("click", () => {
        chatPopup.classList.remove("show");
    });

    clearChatBtn.addEventListener("click", clearChatHistory);

    userInput.addEventListener("keydown", (e) => {
        if (e.key === "Enter" && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });

    sendBtn.addEventListener("click", sendMessage);

    async function sendMessage() {
        const message = userInput.value.trim();
        if (!message || isLoading) return;

        setLoading(true);
        userInput.value = "";

        appendMessage("user", message, false, true);

        const typingDiv = appendMessage("bot", "", true, false);

        try {
            const response = await fetch("${pageContext.request.contextPath}/ChatAIServlet", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ message: message })
            });

            if (!response.ok) {
                throw new Error("Kết nối bị lỗi");
            }

            const data = await response.json();
            
            if (typingDiv && chatBox.contains(typingDiv)) {
                chatBox.removeChild(typingDiv);
            }
            
            const botReply = data.reply || "Hệ thống đang bị gián đoạn, hãy thử lại sau nhé...";
            appendMessage("bot", botReply, false, true);

        } catch (error) {
            console.error("[ChatBot] API Error:", error);
            
            if (typingDiv && chatBox.contains(typingDiv)) {
                chatBox.removeChild(typingDiv);
            }
            
            appendMessage("bot", "Hệ thống đang bị gián đoạn, hãy thử lại sau nhé...", false, true);
        } finally {
            setLoading(false);
        }
    }

    function appendMessage(type, text, isTyping = false, save = true) {
        const div = document.createElement("div");
        div.classList.add("message");
        
        if (type === "user") {
            div.classList.add("user");
        } else {
            div.classList.add("bot");
        }
        
        if (isTyping) {
            div.classList.add("typing-indicator");
            div.innerHTML = '<span class="dot"></span><span class="dot"></span><span class="dot"></span>';
        } else {
            div.innerHTML = text.replace(/\n/g, '<br>');
        }
        
        chatBox.appendChild(div);
        scrollToBottom();

        if (save && !isTyping && text.trim()) {
            chatHistory.push({
                type: type,
                text: text,
                timestamp: Date.now()
            });
            saveChatHistory();
        }
        
        return div;
    }

    function setLoading(loading) {
        isLoading = loading;
        sendBtn.disabled = loading;
        userInput.disabled = loading;
        
        if (loading) {
            sendBtn.style.opacity = "0.5";
            userInput.placeholder = "Đang xử lý...";
        } else {
            sendBtn.style.opacity = "1";
            userInput.placeholder = "Nhập câu hỏi của bạn...";
            userInput.focus();
        }
    }

    function scrollToBottom() {
        setTimeout(() => {
            chatBox.scrollTop = chatBox.scrollHeight;
        }, 100);
    }

    loadChatHistory();
    console.log("[ChatBot] Initialized with session-based storage");

})();
</script>
<!-- ==================== END CHATBOT ==================== -->

<%@ include file="/views/common/footer.jsp" %>