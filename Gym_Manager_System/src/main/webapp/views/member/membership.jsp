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
        --gradient-primary: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
        }

        .membership-card {
        background: var(--card);
            border-radius: 15px;
        box-shadow: 0 8px 25px var(--shadow);
            padding: 30px;
            margin-bottom: 30px;
            transition: all 0.3s ease;
        }

        .membership-card:hover {
            transform: translateY(-5px);
        box-shadow: 0 12px 35px var(--shadow-hover);
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
            margin-bottom: 30px;
        text-transform: uppercase;
        letter-spacing: 1px;
        }

        .package-card {
        background: var(--card);
            border-radius: 15px;
        box-shadow: 0 8px 25px var(--shadow);
        padding: 30px;
            margin-bottom: 20px;
            border: 2px solid transparent;
            transition: all 0.3s ease;
        }

        .package-card:hover {
        border-color: var(--accent);
            transform: translateY(-5px);
        }

    .package-card.featured {
        border-color: var(--accent);
        background: linear-gradient(135deg, var(--card) 0%, rgba(236, 139, 94, 0.05) 100%);
    }

    .btn-membership {
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

    .btn-membership:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
        color: white;
    }

    .price-display {
        font-size: 2.5rem;
        font-weight: 900;
        color: var(--accent);
        }
    </style>

<div class="container mt-5">
    <!-- Current Membership -->
    <div class="membership-card">
        <h2 class="membership-title">Gói Thành Viên Hiện Tại</h2>
        <c:choose>
            <c:when test="${not empty currentMembership}">
                <div class="row align-items-center">
                    <div class="col-md-8">
                        <h4 class="mb-2">${fn:escapeXml(currentMembership.displayName)}</h4>
                        <p class="text-muted mb-2">
                            <c:choose>
                                <c:when test="${not empty currentMembership.membershipName}">
                                    ${fn:escapeXml(currentMembership.membershipName)} Membership
                                </c:when>
                                <c:otherwise>
                                    Gói thành viên đang hoạt động
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <p class="mb-2">
                            <i class="fas fa-calendar me-2"></i>
                            <strong>Bắt đầu:</strong> 
                            <fmt:formatDate value="${currentMembership.startDate}" pattern="dd/MM/yyyy" />
                        </p>
                        <p class="mb-0">
                            <i class="fas fa-calendar-times me-2"></i>
                            <strong>Hết hạn:</strong> 
                            <fmt:formatDate value="${currentMembership.expiryDate}" pattern="dd/MM/yyyy" />
                        </p>
                    </div>
                    <div class="col-md-4 text-end">
                        <span class="badge bg-success" style="font-size: 1rem; padding: 10px 20px;">
                            <i class="fas fa-check-circle"></i> Đang hoạt động
                        </span>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-4">
                    <i class="fas fa-exclamation-circle" style="font-size: 3rem; color: var(--text-light); margin-bottom: 15px;"></i>
                    <h5 class="text-muted">Bạn chưa có gói thành viên</h5>
                    <p class="text-muted">Hãy chọn một gói thành viên phù hợp bên dưới</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Available Packages -->
    <div class="membership-card">
        <h4 class="membership-title">Gói Thành Viên Khác</h4>
        <c:choose>
            <c:when test="${not empty memberships}">
                <div class="row">
                    <c:forEach items="${memberships}" var="membership">
                        <div class="col-md-4 mb-3">
                            <div class="package-card ${membership.isFeatured == true ? 'featured' : ''}">
                                <c:if test="${membership.isFeatured == true}">
                                    <div class="text-center mb-2">
                                        <span class="badge bg-warning">Phổ Biến</span>
                                    </div>
                                </c:if>
                                <div style="display: flex; justify-content: space-between; align-items: start; margin-bottom: 15px;">
                                    <h5 class="mb-3" style="margin: 0;">${fn:escapeXml(membership.displayName)}</h5>
                                    <c:if test="${currentMembershipId != null && currentMembershipId == membership.membershipId}">
                                        <span class="badge bg-success">
                                            <i class="fas fa-check-circle"></i> Gói của bạn
                                        </span>
                                    </c:if>
                                </div>
                                <div class="price-display mb-3">
                                    <fmt:formatNumber value="${membership.price}" type="number" maxFractionDigits="0" />đ
                                </div>
                                <small class="text-muted">/ ${membership.durationMonths} ${membership.durationMonths == 1 ? 'tháng' : 'tháng'}</small>
                                
                                <c:if test="${not empty membership.description}">
                                    <p class="text-muted mt-2" style="font-size: 0.9rem;">
                                        ${fn:escapeXml(membership.description)}
                                    </p>
                                </c:if>
                                
                                <ul class="list-unstyled mt-3">
                                    <c:choose>
                                        <c:when test="${not empty membership.features}">
                                            <c:set var="featuresStr" value="${fn:replace(fn:replace(fn:replace(membership.features, '[', ''), ']', ''), '\"', '')}" />
                                            <c:set var="featuresArray" value="${fn:split(featuresStr, ',')}" />
                                            <c:forEach items="${featuresArray}" var="feature">
                                                <c:set var="featureTrimmed" value="${fn:trim(feature)}" />
                                                <c:if test="${not empty featureTrimmed}">
                                                    <li><i class="fas fa-check text-success me-2"></i>${fn:escapeXml(featureTrimmed)}</li>
                                                </c:if>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <li><i class="fas fa-info-circle text-muted me-2"></i>Chi tiết tính năng</li>
                                        </c:otherwise>
                                    </c:choose>
                                </ul>
                                
                                <div style="margin-top: 20px;">
                                    <form method="post" action="${pageContext.request.contextPath}/member/membership/buyNow">
                                        <input type="hidden" name="membershipId" value="${membership.membershipId}"/>
                                        <button type="submit" class="btn-membership w-100">
                                            <i class="fas fa-credit-card"></i> Mua ngay
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5">
                    <i class="fas fa-box-open" style="font-size: 4rem; color: var(--text-light); margin-bottom: 20px;"></i>
                    <h5 class="text-muted">Không có gói thành viên nào</h5>
                    <p class="text-muted">Vui lòng liên hệ quản trị viên để biết thêm chi tiết</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Payment History -->
    <c:if test="${not empty currentMembership}">
        <div class="membership-card">
            <h4 class="membership-title">Lịch Sử Thanh Toán</h4>
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Ngày Mua</th>
                            <th>Gói</th>
                            <th>Bắt Đầu</th>
                            <th>Hết Hạn</th>
                            <th>Trạng Thái</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><fmt:formatDate value="${currentMembership.createdAt}" pattern="dd/MM/yyyy" /></td>
                            <td>${fn:escapeXml(currentMembership.displayName)}</td>
                            <td><fmt:formatDate value="${currentMembership.startDate}" pattern="dd/MM/yyyy" /></td>
                            <td><fmt:formatDate value="${currentMembership.expiryDate}" pattern="dd/MM/yyyy" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${currentMembership.status == 'active'}">
                                        <span class="badge bg-success">Đang Hoạt Động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">${fn:escapeXml(currentMembership.status)}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>

    <!-- Quick Actions -->
    <div class="row">
        <c:if test="${not empty currentMembership}">
            <div class="col-md-6">
                <div class="membership-card text-center">
                    <h5 class="membership-title">Gia Hạn</h5>
                    <p class="text-muted mb-4">Gia hạn gói hiện tại</p>
                    <form method="post" action="${pageContext.request.contextPath}/member/membership/buyNow">
                        <input type="hidden" name="membershipId" value="${currentMembership.membershipId}"/>
                        <button type="submit" class="btn-membership">
                            <i class="fas fa-credit-card me-2"></i>Gia Hạn Ngay
                        </button>
                    </form>
                </div>
            </div>
        </c:if>
        <div class="${not empty currentMembership ? 'col-md-6' : 'col-md-12'}">
            <div class="membership-card text-center">
                <h5 class="membership-title">Hỗ Trợ</h5>
                <p class="text-muted mb-4">Cần hỗ trợ về gói thành viên?</p>
                <a href="${pageContext.request.contextPath}/member/support" class="btn-membership">
                    <i class="fas fa-headset me-2"></i>Liên Hệ Hỗ Trợ
                </a>
            </div>
        </div>
    </div>

<%@ include file="/views/common/footer.jsp" %>