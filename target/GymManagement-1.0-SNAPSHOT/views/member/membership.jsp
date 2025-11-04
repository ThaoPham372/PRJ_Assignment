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
                        <h4 class="mb-2">${fn:escapeXml(currentMembership.packageName != null ? currentMembership.packageName : 'Gói tập')}</h4>
                        <p class="text-muted mb-2">
                            Bạn đang dùng gói tập, hết hạn: 
                            <c:choose>
                                <c:when test="${currentMembership.endDate != null}">
                                    <%
                                        com.gym.model.membership.Membership mem = (com.gym.model.membership.Membership) request.getAttribute("currentMembership");
                                        if (mem != null && mem.getEndDate() != null) {
                                            java.time.LocalDate endDate = mem.getEndDate();
                                            out.print(String.format("%02d/%02d/%04d", endDate.getDayOfMonth(), endDate.getMonthValue(), endDate.getYear()));
                                        }
                                    %>
                                </c:when>
                                <c:otherwise>N/A</c:otherwise>
                            </c:choose>
                        </p>
                        <p class="mb-2">
                            <i class="fas fa-calendar me-2"></i>
                            <strong>Bắt đầu:</strong> 
                            <c:choose>
                                <c:when test="${currentMembership.startDate != null}">
                                    <%
                                        com.gym.model.membership.Membership mem2 = (com.gym.model.membership.Membership) request.getAttribute("currentMembership");
                                        if (mem2 != null && mem2.getStartDate() != null) {
                                            java.time.LocalDate startDate = mem2.getStartDate();
                                            out.print(String.format("%02d/%02d/%04d", startDate.getDayOfMonth(), startDate.getMonthValue(), startDate.getYear()));
                                        }
                                    %>
                                </c:when>
                                <c:otherwise>N/A</c:otherwise>
                            </c:choose>
                        </p>
                        <p class="mb-2">
                            <i class="fas fa-calendar-times me-2"></i>
                            <strong>Hết hạn:</strong> 
                            <c:choose>
                                <c:when test="${currentMembership.endDate != null}">
                                    <%
                                        com.gym.model.membership.Membership mem3 = (com.gym.model.membership.Membership) request.getAttribute("currentMembership");
                                        if (mem3 != null && mem3.getEndDate() != null) {
                                            java.time.LocalDate endDate = mem3.getEndDate();
                                            out.print(String.format("%02d/%02d/%04d", endDate.getDayOfMonth(), endDate.getMonthValue(), endDate.getYear()));
                                        }
                                    %>
                                </c:when>
                                <c:otherwise>N/A</c:otherwise>
                            </c:choose>
                        </p>
                        <p class="mb-0">
                            <i class="fas fa-clock me-2"></i>
                            <strong>Còn lại:</strong>
                            <c:set var="endDate" value="${currentMembership.endDate}" />
                            <c:set var="now" value="<%=java.time.LocalDate.now()%>" />
                            <c:choose>
                                <c:when test="${endDate != null && endDate.isAfter(now)}">
                                    <c:set var="daysRemaining" value="${endDate.toEpochDay() - now.toEpochDay()}" />
                                    <span style="color: var(--accent); font-weight: bold;">${daysRemaining} ngày</span>
                                </c:when>
                                <c:otherwise>
                                    <span style="color: var(--danger); font-weight: bold;">Đã hết hạn</span>
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                    <div class="col-md-4 text-end">
                        <c:choose>
                            <c:when test="${currentMembership.status == 'ACTIVE'}">
                                <span class="badge bg-success" style="font-size: 1rem; padding: 10px 20px;">
                                    <i class="fas fa-check-circle"></i> Đang hoạt động
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary" style="font-size: 1rem; padding: 10px 20px;">
                                    ${fn:escapeXml(currentMembership.status)}
                                </span>
                            </c:otherwise>
                        </c:choose>
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
        <!-- Debug info -->
        <c:if test="${empty packages}">
            <div class="alert alert-warning">
                <strong>Debug:</strong> packages attribute is empty or null.
                <br>packages size: ${packages != null ? packages.size() : 'NULL'}
            </div>
        </c:if>
        <c:choose>
            <c:when test="${not empty packages}">
                <div class="row">
                    <c:forEach items="${packages}" var="pkg">
                        <div class="col-md-4 mb-3">
                            <div class="package-card">
                                <div style="display: flex; justify-content: space-between; align-items: start; margin-bottom: 15px;">
                                    <h5 class="mb-3" style="margin: 0;">${fn:escapeXml(pkg.name)}</h5>
                                    <c:if test="${currentPackageId != null && currentPackageId == pkg.packageId}">
                                        <span class="badge bg-success">
                                            <i class="fas fa-check-circle"></i> Gói của bạn
                                        </span>
                                    </c:if>
                                </div>
                                <div class="price-display mb-3">
                                    <fmt:formatNumber value="${pkg.price}" type="number" maxFractionDigits="0" />đ
                                </div>
                                <small class="text-muted">/ ${pkg.durationMonths} ${pkg.durationMonths == 1 ? 'tháng' : 'tháng'}</small>
                                
                                <c:if test="${not empty pkg.description}">
                                    <p class="text-muted mt-2" style="font-size: 0.9rem;">
                                        ${fn:escapeXml(pkg.description)}
                                    </p>
                                </c:if>
                                
                                <c:if test="${pkg.maxSessions != null}">
                                    <p class="text-muted mt-2" style="font-size: 0.9rem;">
                                        <i class="fas fa-dumbbell me-2"></i>Tối đa ${pkg.maxSessions} buổi tập
                                    </p>
                                </c:if>
                                
                                <div style="margin-top: 20px;">
                                    <form method="post" action="${pageContext.request.contextPath}/member/membership/buyNow">
                                        <input type="hidden" name="packageId" value="${pkg.packageId}"/>
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
                            <td>
                                <c:choose>
                                    <c:when test="${currentMembership.createdDate != null}">
                                        <%
                                            com.gym.model.membership.Membership mem4 = (com.gym.model.membership.Membership) request.getAttribute("currentMembership");
                                            if (mem4 != null && mem4.getCreatedDate() != null) {
                                                java.time.LocalDateTime createdDate = mem4.getCreatedDate();
                                                java.time.LocalDate date = createdDate.toLocalDate();
                                                out.print(String.format("%02d/%02d/%04d", date.getDayOfMonth(), date.getMonthValue(), date.getYear()));
                                            }
                                        %>
                                    </c:when>
                                    <c:otherwise>N/A</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${fn:escapeXml(currentMembership.packageName != null ? currentMembership.packageName : 'Gói tập')}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${currentMembership.startDate != null}">
                                        <%
                                            com.gym.model.membership.Membership mem5 = (com.gym.model.membership.Membership) request.getAttribute("currentMembership");
                                            if (mem5 != null && mem5.getStartDate() != null) {
                                                java.time.LocalDate startDate = mem5.getStartDate();
                                                out.print(String.format("%02d/%02d/%04d", startDate.getDayOfMonth(), startDate.getMonthValue(), startDate.getYear()));
                                            }
                                        %>
                                    </c:when>
                                    <c:otherwise>N/A</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${currentMembership.endDate != null}">
                                        <%
                                            com.gym.model.membership.Membership mem6 = (com.gym.model.membership.Membership) request.getAttribute("currentMembership");
                                            if (mem6 != null && mem6.getEndDate() != null) {
                                                java.time.LocalDate endDate = mem6.getEndDate();
                                                out.print(String.format("%02d/%02d/%04d", endDate.getDayOfMonth(), endDate.getMonthValue(), endDate.getYear()));
                                            }
                                        %>
                                    </c:when>
                                    <c:otherwise>N/A</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${currentMembership.status == 'ACTIVE'}">
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
                        <input type="hidden" name="packageId" value="${currentMembership.packageId}"/>
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