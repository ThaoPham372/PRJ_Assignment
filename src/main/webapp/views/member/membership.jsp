<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.concurrent.TimeUnit" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/views/common/header.jsp" %>

<%
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>

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
        --shadow: rgba(0, 0, 0, 0.08);
        --shadow-hover: rgba(0, 0, 0, 0.12);
        --shadow-lg: rgba(0, 0, 0, 0.15);
        --gradient-primary: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
        --gradient-card: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
        --gradient-success: linear-gradient(135deg, #28a745 0%, #20c997 100%);
    }

    body {
        background: linear-gradient(135deg, #f5f7fa 0%, #e9ecef 100%);
        min-height: 100vh;
    }

    .membership-card {
        background: var(--card);
        border-radius: 20px;
        box-shadow: 0 10px 40px var(--shadow);
        padding: 25px;
        margin-bottom: 30px;
        transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        border: 1px solid rgba(255, 255, 255, 0.8);
        position: relative;
        overflow: hidden;
    }

    .membership-card::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 5px;
        background: var(--gradient-accent);
        transform: scaleX(0);
        transform-origin: left;
        transition: transform 0.4s ease;
    }

    .membership-card:hover::before {
        transform: scaleX(1);
    }

    .membership-card:hover {
        transform: translateY(-8px);
        box-shadow: 0 20px 60px var(--shadow-lg);
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

    /* Responsive Navigation */
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
    }

    .membership-title {
        color: var(--text);
        font-weight: 800;
        margin-bottom: 20px;
        text-transform: uppercase;
        letter-spacing: 1.5px;
        font-size: 1.3rem;
        position: relative;
        padding-bottom: 12px;
        display: flex;
        align-items: center;
        gap: 12px;
    }

    .membership-title::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        width: 60px;
        height: 4px;
        background: var(--gradient-accent);
        border-radius: 2px;
    }

    .membership-title i {
        color: var(--accent);
        font-size: 1.3em;
    }

    /* Packages Grid - Similar to products-grid */
    .packages-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 30px;
        margin-bottom: 40px;
    }

    .package-card {
        background: var(--card);
        border-radius: 20px;
        box-shadow: 0 8px 30px var(--shadow);
        padding: 30px;
        border: 2px solid transparent;
        transition: all 0.3s ease;
        position: relative;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        text-align: center;
        min-height: 520px;
        width: 100%;
    }

    .package-card:hover {
        transform: translateY(-10px);
        box-shadow: 0 15px 40px var(--shadow-hover);
        border-color: var(--accent);
    }

    .package-card.featured {
        border-color: var(--accent);
        background: linear-gradient(135deg, #ffffff 0%, rgba(236, 139, 94, 0.08) 100%);
        box-shadow: 0 10px 40px rgba(236, 139, 94, 0.2);
        position: relative;
    }

    .package-card.featured::before {
        content: '⭐';
        position: absolute;
        top: 15px;
        right: 15px;
        font-size: 1.5rem;
        opacity: 0.7;
        z-index: 2;
        animation: pulse 2s infinite;
    }

    @keyframes pulse {
        0%, 100% { transform: scale(1); opacity: 0.7; }
        50% { transform: scale(1.1); opacity: 0.9; }
    }

    .btn-membership {
        background: var(--gradient-accent);
        color: white;
        border: none;
        border-radius: 25px;
        padding: 14px 28px;
        font-weight: 600;
        font-size: 1.05rem;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        width: 100%;
        box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
        margin-top: auto;
    }

    .btn-membership:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(236, 139, 90, 0.4);
        color: white;
    }

    .price-display {
        font-size: 2rem;
        font-weight: 800;
        color: var(--accent);
        margin: 18px 0;
        line-height: 1.2;
    }

    .package-duration {
        color: var(--text-light);
        font-weight: 600;
        font-size: 1rem;
        margin-bottom: 25px;
    }

    /* Container centered */
    .membership-container {
        max-width: 1400px;
        margin: 0 auto;
        padding: 30px 20px;
    }

    /* Enhanced Package Info Styles */
    .package-info-item {
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 8px 0;
        border-bottom: 1px solid rgba(0, 0, 0, 0.05);
        transition: all 0.3s ease;
        font-size: 0.9rem;
    }

    .package-info-item:last-child {
        border-bottom: none;
    }

    .package-info-item:hover {
        padding-left: 6px;
        background: rgba(236, 139, 94, 0.05);
        border-radius: 6px;
        margin: 0 -8px;
        padding-left: 14px;
        padding-right: 8px;
    }

    .package-info-item i {
        color: var(--accent);
        font-size: 0.95rem;
        width: 20px;
        text-align: center;
    }

    .package-info-label {
        font-weight: 600;
        color: var(--text);
        min-width: 100px;
        font-size: 0.85rem;
    }

    .package-info-value {
        color: var(--text-light);
        flex: 1;
    }

    .days-remaining {
        display: inline-block;
        padding: 4px 10px;
        border-radius: 15px;
        font-weight: 700;
        font-size: 0.85rem;
        background: linear-gradient(135deg, rgba(236, 139, 94, 0.15) 0%, rgba(236, 139, 94, 0.25) 100%);
        color: var(--accent);
        border: 2px solid rgba(236, 139, 94, 0.3);
    }

    .days-expired {
        display: inline-block;
        padding: 4px 10px;
        border-radius: 15px;
        font-weight: 700;
        font-size: 0.85rem;
        background: linear-gradient(135deg, rgba(220, 53, 69, 0.15) 0%, rgba(220, 53, 69, 0.25) 100%);
        color: #dc3545;
        border: 2px solid rgba(220, 53, 69, 0.3);
    }

    /* Enhanced Badge Styles */
    .badge {
        padding: 5px 12px;
        border-radius: 15px;
        font-weight: 600;
        font-size: 0.75rem;
        display: inline-flex;
        align-items: center;
        gap: 4px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .badge.bg-success {
        background: var(--gradient-success) !important;
        color: white;
        border: none;
    }

    /* Empty State */
    .empty-state {
        text-align: center;
        padding: 60px 20px;
    }

    .empty-state-icon {
        font-size: 5rem;
        color: var(--text-light);
        opacity: 0.3;
        margin-bottom: 25px;
        animation: float 3s ease-in-out infinite;
    }

    @keyframes float {
        0%, 100% { transform: translateY(0px); }
        50% { transform: translateY(-10px); }
    }

    .empty-state h5 {
        color: var(--text);
        font-weight: 700;
        margin-bottom: 10px;
        font-size: 1.3rem;
    }

    .empty-state p {
        color: var(--text-light);
        font-size: 1rem;
    }

    /* Payment History Card Style */
    .payment-history-card {
        background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
        border-radius: 12px;
        padding: 20px;
        margin-bottom: 15px;
        border-left: 4px solid var(--accent);
        transition: all 0.3s ease;
    }

    .payment-history-card:hover {
        transform: translateX(5px);
        box-shadow: 0 4px 15px var(--shadow);
    }

    .payment-history-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 0;
        border-bottom: 1px solid #e9ecef;
    }

    .payment-history-item:last-child {
        border-bottom: none;
    }

    .payment-history-label {
        font-weight: 600;
        color: var(--text);
        min-width: 120px;
    }

    .payment-history-value {
        color: var(--text-light);
        text-align: right;
    }

    /* Quick Actions Cards */
    .action-card {
        background: var(--card);
        border-radius: 20px;
        box-shadow: 0 10px 40px var(--shadow);
        padding: 50px 40px;
        text-align: center;
        transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        border: 2px solid transparent;
        position: relative;
        overflow: hidden;
    }

    .action-card::before {
        content: '';
        position: absolute;
        top: -50%;
        right: -50%;
        width: 200%;
        height: 200%;
        background: radial-gradient(circle, rgba(236, 139, 94, 0.1) 0%, transparent 70%);
        transform: scale(0);
        transition: transform 0.6s ease;
    }

    .action-card:hover::before {
        transform: scale(1);
    }

    .action-card:hover {
        transform: translateY(-12px) scale(1.02);
        box-shadow: 0 20px 60px var(--shadow-lg);
        border-color: var(--accent);
    }

    .action-card-icon {
        font-size: 4rem;
        background: var(--gradient-accent);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        margin-bottom: 25px;
        transition: all 0.3s ease;
        position: relative;
        z-index: 1;
    }

    .action-card:hover .action-card-icon {
        transform: scale(1.1) rotate(5deg);
    }

    .action-card-title {
        color: var(--text);
        font-weight: 800;
        font-size: 1.6rem;
        margin-bottom: 15px;
        position: relative;
        z-index: 1;
    }

    .action-card-description {
        color: var(--text-light);
        margin-bottom: 30px;
        font-size: 1.05rem;
        line-height: 1.6;
        position: relative;
        z-index: 1;
    }

    .action-card .btn-membership {
        width: 100%;
        max-width: 280px;
        position: relative;
        z-index: 1;
    }

    /* Package Header Styles */
    /* Package Image/Icon Area */
    .package-icon-area {
        width: 100%;
        height: 200px;
        border-radius: 15px;
        margin-bottom: 25px;
        padding: 40px;
        background: rgba(20, 26, 70, 0.05);
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s ease;
    }

    .package-card:hover .package-icon-area {
        background: rgba(20, 26, 70, 0.1);
        transform: scale(1.05);
    }

    .package-icon-area i {
        font-size: 4rem;
        color: var(--accent);
        opacity: 0.8;
    }

    .package-header {
        margin-bottom: 15px;
    }

    .package-name {
        font-size: 1.6rem;
        font-weight: 700;
        color: var(--primary);
        margin: 0 0 12px 0;
        line-height: 1.3;
    }

    /* Package Details Container */
    .package-details {
        margin: 18px 0;
        padding: 20px;
        background: rgba(236, 139, 94, 0.03);
        border-radius: 12px;
        border-left: 4px solid var(--accent);
        text-align: left;
        flex: 1;
    }

    .package-description {
        color: var(--text);
        font-size: 1rem;
        line-height: 1.6;
        margin-bottom: 18px;
        text-align: left;
    }

    .package-features {
        list-style: none;
        padding: 0;
        margin: 0;
        text-align: left;
    }

    .package-features li {
        color: var(--text-light);
        font-size: 0.95rem;
        padding: 12px 0;
        display: flex;
        align-items: flex-start;
        gap: 14px;
        border-bottom: 1px solid rgba(0, 0, 0, 0.05);
    }

    .package-features li:last-child {
        border-bottom: none;
    }

    .package-features li i {
        color: var(--accent);
        font-size: 1.1rem;
        margin-top: 2px;
        flex-shrink: 0;
    }

    .package-features li span {
        flex: 1;
        line-height: 1.6;
    }

    .package-features li strong {
        color: var(--text);
        display: block;
        margin-bottom: 5px;
        font-size: 0.95rem;
    }

    /* Responsive */
    @media (max-width: 1200px) {
        .packages-grid {
            grid-template-columns: repeat(3, 1fr);
            gap: 25px;
        }
    }

    @media (max-width: 992px) {
        .packages-grid {
            grid-template-columns: repeat(2, 1fr);
            gap: 25px;
        }

        .package-card {
            min-height: 500px;
        }
    }

    @media (max-width: 768px) {
        .membership-container {
            padding: 20px 15px;
        }

        .membership-card {
            padding: 20px 15px;
        }

        .packages-grid {
            grid-template-columns: 1fr;
            gap: 20px;
        }

        .package-card {
            min-height: auto;
            padding: 25px;
        }

        .package-icon-area {
            height: 180px;
            padding: 30px;
        }

        .package-icon-area i {
            font-size: 3.5rem;
        }

        .price-display {
            font-size: 1.8rem;
        }

        .package-name {
            font-size: 1.4rem;
        }

        .action-card {
            padding: 30px 20px;
        }

        .action-card-icon {
            font-size: 2.5rem;
        }
    }

    @media (max-width: 576px) {
        .membership-container {
            padding: 15px 10px;
        }

        .membership-title {
            font-size: 1.1rem;
        }

        .membership-card {
            padding: 15px 12px;
        }

        .package-card {
            padding: 20px 18px;
        }

        .package-icon-area {
            height: 160px;
            padding: 25px;
        }

        .package-icon-area i {
            font-size: 3rem;
        }

        .price-display {
            font-size: 1.6rem;
        }

        .package-name {
            font-size: 1.3rem;
        }
    }
</style>

<div class="membership-container">
    <!-- Current Active Memberships -->
    <div class="membership-card">
        <h2 class="membership-title">
            <i class="fas fa-id-card me-2"></i>Gói Thành Viên Đang Sở Hữu
        </h2>
        <c:choose>
            <c:when test="${not empty activeMemberships}">
                <div class="packages-grid">
                    <c:forEach items="${activeMemberships}" var="membership" varStatus="status">
                        <div class="package-card featured">
                            <!-- Icon/Image Area -->
                            <div class="package-icon-area">
                                <i class="fas fa-id-card"></i>
                            </div>
                            
                            <!-- Package Header -->
                            <div class="package-header">
                                <h3 class="package-name">
                                    <c:choose>
                                        <c:when test="${membership.packageO != null}">
                                            ${fn:escapeXml(membership.packageO.name)}
                                        </c:when>
                                        <c:otherwise>Gói tập</c:otherwise>
                                    </c:choose>
                                </h3>
                                <span class="badge bg-success" style="display: inline-block; margin-bottom: 10px;">
                                    <i class="fas fa-check-circle"></i> Đang hoạt động
                                </span>
                            </div>
                            
                            <!-- Package Details -->
                            <div class="package-details">
                                <ul class="package-features">
                                    <li>
                                        <i class="fas fa-calendar"></i>
                                        <span>
                                            <strong>Ngày bắt đầu:</strong><br>
                                            <%
                                                model.Membership mem = (model.Membership) pageContext.getAttribute("membership");
                                                if (mem != null && mem.getStartDate() != null) {
                                                    out.print(sdf.format(mem.getStartDate()));
                                                } else {
                                                    out.print("N/A");
                                                }
                                            %>
                                        </span>
                                    </li>
                                    <li>
                                        <i class="fas fa-calendar-times"></i>
                                        <span>
                                            <strong>Ngày kết thúc:</strong><br>
                                            <%
                                                if (mem != null && mem.getEndDate() != null) {
                                                    out.print(sdf.format(mem.getEndDate()));
                                                } else {
                                                    out.print("N/A");
                                                }
                                            %>
                                        </span>
                                    </li>
                                    <li>
                                        <i class="fas fa-clock"></i>
                                        <span>
                                            <strong>Còn lại:</strong><br>
                                            <%
                                                if (mem != null && mem.getEndDate() != null) {
                                                    Date endDate = mem.getEndDate();
                                                    Date now = new Date();
                                                    if (endDate.after(now)) {
                                                        long diff = endDate.getTime() - now.getTime();
                                                        long daysRemaining = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                            %>
                                                        <span class="days-remaining"><%= daysRemaining %> ngày</span>
                                            <%
                                                    } else {
                                            %>
                                                        <span class="days-expired">Đã hết hạn</span>
                                            <%
                                                    }
                                                } else {
                                            %>
                                                    N/A
                                            <%
                                                }
                                            %>
                                        </span>
                                    </li>
                                </ul>
                            </div>
                            
                            <!-- Action Button -->
                            <c:if test="${membership.packageO != null}">
                                <a href="${pageContext.request.contextPath}/checkout?type=membership&packageId=${membership.packageO.id}" 
                                   class="btn-membership">
                                    <i class="fas fa-sync-alt"></i>
                                    <span>Gia hạn gói này</span>
                                </a>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">
                        <i class="fas fa-id-card"></i>
                    </div>
                    <h5>Bạn chưa có gói thành viên</h5>
                    <p>Hãy chọn một gói thành viên phù hợp bên dưới để bắt đầu hành trình tập luyện của bạn</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Available Packages -->
    <div class="membership-card">
        <h4 class="membership-title">
            <i class="fas fa-box-open me-2"></i>Gói Thành Viên Khác
        </h4>
        <c:choose>
            <c:when test="${not empty packages}">
                <div class="packages-grid">
                    <c:forEach items="${packages}" var="pkg">
                        <c:set var="isOwned" value="false"/>
                        <c:if test="${not empty activePackageIds}">
                            <c:forEach items="${activePackageIds}" var="activePkgId">
                                <c:if test="${activePkgId == pkg.id}">
                                    <c:set var="isOwned" value="true"/>
                                </c:if>
                            </c:forEach>
                        </c:if>
                        <div class="package-card <c:if test="${isOwned}">featured</c:if>">
                            <!-- Icon/Image Area -->
                            <div class="package-icon-area">
                                <i class="fas fa-dumbbell"></i>
                            </div>
                            
                            <!-- Package Header -->
                            <div class="package-header">
                                <h3 class="package-name">${fn:escapeXml(pkg.name)}</h3>
                                <c:if test="${isOwned}">
                                    <span class="badge bg-success" style="display: inline-block; margin-bottom: 10px;">
                                        <i class="fas fa-check-circle"></i> Đang sở hữu
                                    </span>
                                </c:if>
                            </div>
                            
                            <!-- Price and Duration -->
                            <div class="price-display">
                                <fmt:formatNumber value="${pkg.price}" type="number" maxFractionDigits="0" />đ
                            </div>
                            <div class="package-duration">
                                <i class="fas fa-calendar-alt"></i> ${pkg.durationMonths} tháng
                            </div>
                            
                            <!-- Package Details -->
                            <div class="package-details">
                                <c:if test="${not empty pkg.description}">
                                    <p class="package-description">
                                        ${fn:escapeXml(pkg.description)}
                                    </p>
                                </c:if>
                                
                                <ul class="package-features">
                                    <c:if test="${pkg.maxSessions != null}">
                                        <li>
                                            <i class="fas fa-dumbbell"></i>
                                            <span>Tối đa <strong>${pkg.maxSessions}</strong> buổi tập</span>
                                        </li>
                                    </c:if>
                                    <li>
                                        <i class="fas fa-check-circle"></i>
                                        <span>Truy cập đầy đủ phòng tập</span>
                                    </li>
                                    <li>
                                        <i class="fas fa-check-circle"></i>
                                        <span>Hỗ trợ 24/7</span>
                                    </li>
                                </ul>
                            </div>
                            
                            <!-- Action Button -->
                            <a href="${pageContext.request.contextPath}/checkout?type=membership&packageId=${pkg.id}" 
                               class="btn-membership">
                                <i class="fas fa-credit-card"></i>
                                <span>${isOwned ? 'Gia hạn' : 'Mua ngay'}</span>
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">
                        <i class="fas fa-box-open"></i>
                    </div>
                    <h5>Không có gói thành viên nào</h5>
                    <p>Vui lòng liên hệ quản trị viên để biết thêm chi tiết</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>


    <!-- Quick Actions -->
    <div class="row g-4">
        <div class="col-md-12">
            <div class="action-card">
                <div class="action-card-icon">
                    <i class="fas fa-headset"></i>
                </div>
                <h5 class="action-card-title">Hỗ Trợ</h5>
                <p class="action-card-description">Cần hỗ trợ về gói thành viên? Chúng tôi luôn sẵn sàng giúp đỡ bạn</p>
                <a href="${pageContext.request.contextPath}/member/support" class="btn-membership">
                    <i class="fas fa-comments me-2"></i>Liên Hệ Hỗ Trợ
                </a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/views/common/footer.jsp" %>