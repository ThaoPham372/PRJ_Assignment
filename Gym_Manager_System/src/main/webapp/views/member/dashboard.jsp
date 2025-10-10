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

    .member-card {
        background: var(--gradient-primary);
        color: white;
        border-radius: 15px;
        padding: 30px;
        margin-bottom: 30px;
        box-shadow: 0 8px 25px var(--shadow);
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
    }

    .stat-item:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 25px var(--shadow-hover);
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
        grid-template-columns: repeat(5, 1fr);
        gap: 20px;
        margin-bottom: 30px;
    }

    .qa-item {
        background: var(--card);
        border-radius: 15px;
        padding: 30px 20px;
        text-align: center;
        box-shadow: 0 4px 15px var(--shadow);
        transition: all 0.3s ease;
        text-decoration: none;
        color: var(--text);
        display: flex;
        align-items: center;
        justify-content: center;
        min-height: 120px;
    }

    .qa-item:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 25px var(--shadow-hover);
        background: var(--gradient-accent);
        color: white;
    }

    .qa-item .qa-title {
        font-weight: 600;
        font-size: 1rem;
        line-height: 1.4;
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

<!-- Member Navigation -->
<nav class="member-nav">
    <div class="nav-container">
        <a href="${pageContext.request.contextPath}/views/member/dashboard.jsp" class="nav-link active">
            <i class="fas fa-tachometer-alt"></i>Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/views/member/profile.jsp" class="nav-link">
            <i class="fas fa-user"></i>Profile
        </a>
        <a href="${pageContext.request.contextPath}/views/member/workout.jsp" class="nav-link">
            <i class="fas fa-dumbbell"></i>Workout
        </a>
        <a href="${pageContext.request.contextPath}/views/member/schedule.jsp" class="nav-link">
            <i class="fas fa-calendar"></i>Schedule
        </a>
        <a href="${pageContext.request.contextPath}/views/member/nutrition.jsp" class="nav-link">
            <i class="fas fa-apple-alt"></i>Nutrition
        </a>
        <a href="${pageContext.request.contextPath}/views/member/membership.jsp" class="nav-link">
            <i class="fas fa-id-card"></i>Membership
        </a>
        <a href="${pageContext.request.contextPath}/views/member/support.jsp" class="nav-link">
            <i class="fas fa-headset"></i>Support
        </a>
    </div>
</nav>

<div class="container mt-4">
    <!-- Welcome Card -->
    <div class="member-card mb-4">
        <div class="row align-items-center">
            <div class="col-md-8">
                <h2 class="mb-3">
                    <i class="fas fa-user-circle me-2"></i>
                    Chào mừng, <c:out value="${dashboardData.memberName}"/>
                </h2>
                <p class="mb-2">
                    <i class="fas fa-crown me-1"></i>
                    Gói thành viên: <strong><c:out value="${dashboardData.packageType}"/></strong>
                </p>
                <p class="mb-0">
                    <i class="fas fa-calendar me-1"></i>
                    Tham gia từ: <fmt:formatDate value="${dashboardData.joinDate}" pattern="dd/MM/yyyy"/>
                </p>
            </div>
            <div class="col-md-4 text-end">
                <i class="fas fa-heart fa-4x opacity-50"></i>
            </div>
        </div>
    </div>

    <!-- Statistics Cards -->
    <div class="stats-row">
        <div class="stat-item">
            <div class="stat-label">Streak</div>
            <div class="stat-value">
                <c:choose>
                    <c:when test="${not empty dashboardData.stats.streak}">
                        ${dashboardData.stats.streak}
                    </c:when>
                    <c:otherwise>0</c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="stat-item">
            <div class="stat-label">Calo đã tiêu hao</div>
            <div class="stat-value">
                <c:choose>
                    <c:when test="${not empty dashboardData.stats.caloriesBurned}">
                        ${dashboardData.stats.caloriesBurned}
                    </c:when>
                    <c:otherwise>0</c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="stat-item">
            <div class="stat-label">Lượng nước đã uống</div>
            <div class="stat-value">
                <c:choose>
                    <c:when test="${not empty dashboardData.stats.waterIntake}">
                        ${dashboardData.stats.waterIntake}
                    </c:when>
                    <c:otherwise>0L</c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="stat-item">
            <div class="stat-label">Thời gian còn lại</div>
            <div class="stat-value">
                <c:choose>
                    <c:when test="${not empty dashboardData.stats.packageRemaining}">
                        ${dashboardData.stats.packageRemaining}
                    </c:when>
                    <c:otherwise>--</c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="stat-item">
            <div class="stat-label">Chỉ số BMI</div>
            <div class="stat-value">
                <c:choose>
                    <c:when test="${not empty dashboardData.stats.bmi}">
                        ${dashboardData.stats.bmi}
                    </c:when>
                    <c:otherwise>--</c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="stat-card mb-4">
        <h5 class="dashboard-title mb-4">
            <i class="fas fa-bolt me-2"></i>Hành động nhanh
        </h5>
        <div class="qa-grid">
            <a class="qa-item" href="${pageContext.request.contextPath}/views/member/workout.jsp">
                <span class="qa-title">Ghi nhận buổi tập</span>
            </a>
            <a class="qa-item" href="${pageContext.request.contextPath}/tools/bmi">
                <span class="qa-title">Tính toán BMI</span>
            </a>
            <a class="qa-item" href="${pageContext.request.contextPath}/profile/body-metrics">
                <span class="qa-title">Cập nhật chỉ số cơ thể</span>
            </a>
            <a class="qa-item" href="${pageContext.request.contextPath}/goals/settings">
                <span class="qa-title">Cài đặt mục tiêu</span>
            </a>
            <a class="qa-item" href="${pageContext.request.contextPath}/views/member/workout.jsp">
                <span class="qa-title">Lịch sử tập luyện</span>
            </a>
            <a class="qa-item" href="${pageContext.request.contextPath}/views/member/nutrition.jsp">
                <span class="qa-title">Ghi nhận bữa ăn</span>
            </a>
            <a class="qa-item" href="${pageContext.request.contextPath}/views/member/profile.jsp">
                <span class="qa-title">Thông tin cá nhân</span>
            </a>
            <a class="qa-item" href="${pageContext.request.contextPath}/views/member/membership.jsp">
                <span class="qa-title">Membership</span>
            </a>
            <a class="qa-item" href="${pageContext.request.contextPath}/views/member/support.jsp">
                <span class="qa-title">Liên hệ hỗ trợ</span>
            </a>
        </div>
    </div>


    <div class="row">
        <!-- Recent Activities -->
        <div class="col-md-6">
            <div class="stat-card">
                <h5 class="dashboard-title mb-4">
                    <i class="fas fa-history me-2"></i>Hoạt động gần đây
                </h5>
                <c:forEach var="activity" items="${dashboardData.recentActivities}">
                    <div class="activity-item">
                        <div class="d-flex justify-content-between align-items-start">
                            <div>
                                <h6 class="mb-1">
                                    <i class="fas fa-circle me-1" style="font-size: 8px; color: var(--accent);"></i>
                                    <c:out value="${activity.description}"/>
                                </h6>
                                <small class="text-muted">${activity.type}</small>
                            </div>
                            <small class="text-muted">${activity.time}</small>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Upcoming Sessions -->
        <div class="col-md-6">
            <div class="stat-card">
                <h5 class="dashboard-title mb-4">
                    <i class="fas fa-calendar-alt me-2"></i>Buổi tập sắp tới
                </h5>
                <c:forEach var="session" items="${dashboardData.upcomingSessions}">
                    <div class="session-item">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="mb-1">${session.type}</h6>
                                <small class="text-muted">
                                    <i class="fas fa-user me-1"></i>${session.coach}
                                </small>
                            </div>
                            <div class="text-end">
                                <div class="fw-bold">${session.date}</div>
                                <small class="text-muted">${session.time}</small>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<%@ include file="/views/common/footer.jsp" %>