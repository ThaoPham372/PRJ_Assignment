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

    /* Container centered */
    .membership-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 40px 20px;
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
        border-radius: 15px;
        box-shadow: 0 8px 25px var(--shadow);
        padding: 40px 30px;
        text-align: center;
        transition: all 0.3s ease;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        border: 2px solid transparent;
    }

    .action-card:hover {
        transform: translateY(-8px);
        box-shadow: 0 15px 40px var(--shadow-hover);
        border-color: var(--accent);
    }

    .action-card-icon {
        font-size: 3.5rem;
        color: var(--accent);
        margin-bottom: 20px;
    }

    .action-card-title {
        color: var(--text);
        font-weight: 700;
        font-size: 1.5rem;
        margin-bottom: 15px;
    }

    .action-card-description {
        color: var(--text-light);
        margin-bottom: 25px;
        font-size: 1rem;
    }

    .action-card .btn-membership {
        width: 100%;
        max-width: 250px;
    }

    /* Responsive */
    @media (max-width: 768px) {
        .membership-container {
            padding: 20px 15px;
        }

        .payment-history-item {
            flex-direction: column;
            align-items: flex-start;
            gap: 8px;
        }

        .payment-history-value {
            text-align: left;
        }
    }
</style>

<div class="membership-container">
    <!-- Current Membership -->
    <div class="membership-card">
        <h2 class="membership-title">
            <i class="fas fa-id-card me-2"></i>Gói Thành Viên Hiện Tại
        </h2>
        <c:choose>
            <c:when test="${not empty currentMembership}">
                <div class="row align-items-center">
                    <div class="col-md-8">
                        <h4 class="mb-2">
                            <c:choose>
                                <c:when test="${currentMembership.packageO != null}">
                                    ${fn:escapeXml(currentMembership.packageO.name)}
                                </c:when>
                                <c:otherwise>Gói tập</c:otherwise>
                            </c:choose>
                        </h4>
                        <p class="text-muted mb-2">
                            Bạn đang dùng gói tập, hết hạn: 
                            <%
                                model.Membership mem = (model.Membership) request.getAttribute("currentMembership");
                                if (mem != null && mem.getEndDate() != null) {
                                    out.print(sdf.format(mem.getEndDate()));
                                } else {
                                    out.print("N/A");
                                }
                            %>
                        </p>
                        <p class="mb-2">
                            <i class="fas fa-calendar me-2"></i>
                            <strong>Bắt đầu:</strong> 
                            <%
                                if (mem != null && mem.getStartDate() != null) {
                                    out.print(sdf.format(mem.getStartDate()));
                                } else {
                                    out.print("N/A");
                                }
                            %>
                        </p>
                        <p class="mb-2">
                            <i class="fas fa-calendar-times me-2"></i>
                            <strong>Hết hạn:</strong> 
                            <%
                                if (mem != null && mem.getEndDate() != null) {
                                    out.print(sdf.format(mem.getEndDate()));
                                } else {
                                    out.print("N/A");
                                }
                            %>
                        </p>
                        <p class="mb-0">
                            <i class="fas fa-clock me-2"></i>
                            <strong>Còn lại:</strong>
                            <%
                                if (mem != null && mem.getEndDate() != null) {
                                    Date endDate = mem.getEndDate();
                                    Date now = new Date();
                                    if (endDate.after(now)) {
                                        long diff = endDate.getTime() - now.getTime();
                                        long daysRemaining = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                            %>
                                        <span style="color: var(--accent); font-weight: bold;"><%= daysRemaining %> ngày</span>
                            <%
                                    } else {
                            %>
                                        <span style="color: #dc3545; font-weight: bold;">Đã hết hạn</span>
                            <%
                                    }
                                } else {
                            %>
                                    N/A
                            <%
                                }
                            %>
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
        <h4 class="membership-title">
            <i class="fas fa-box-open me-2"></i>Gói Thành Viên Khác
        </h4>
        <c:choose>
            <c:when test="${not empty packages}">
                <div class="row">
                    <c:forEach items="${packages}" var="pkg">
                        <div class="col-md-4 mb-3">
                            <div class="package-card <c:if test="${currentPackageId != null && currentPackageId == pkg.id}">featured</c:if>">
                                <div style="display: flex; justify-content: space-between; align-items: start; margin-bottom: 15px;">
                                    <h5 class="mb-3" style="margin: 0;">${fn:escapeXml(pkg.name)}</h5>
                                    <c:if test="${currentPackageId != null && currentPackageId == pkg.id}">
                                        <span class="badge bg-success">
                                            <i class="fas fa-check-circle"></i> Gói của bạn
                                        </span>
                                    </c:if>
                                </div>
                                <div class="price-display mb-3">
                                    <fmt:formatNumber value="${pkg.price}" type="number" maxFractionDigits="0" />đ
                                </div>
                                <small class="text-muted">/ ${pkg.durationMonths} tháng</small>
                                
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
                                        <input type="hidden" name="packageId" value="${pkg.id}"/>
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
            <h4 class="membership-title">
                <i class="fas fa-history me-2"></i>Lịch Sử Thanh Toán
            </h4>
            <%
                model.Membership memHistory = (model.Membership) request.getAttribute("currentMembership");
            %>
            <div class="payment-history-card">
                <div class="payment-history-item">
                    <span class="payment-history-label">
                        <i class="fas fa-calendar-check me-2"></i>Ngày Mua
                    </span>
                    <span class="payment-history-value">
                        <%
                            if (memHistory != null && memHistory.getCreatedDate() != null) {
                                out.print(sdf.format(memHistory.getCreatedDate()));
                            } else {
                                out.print("N/A");
                            }
                        %>
                    </span>
                </div>
                <div class="payment-history-item">
                    <span class="payment-history-label">
                        <i class="fas fa-box me-2"></i>Gói Thành Viên
                    </span>
                    <span class="payment-history-value">
                        <c:choose>
                            <c:when test="${currentMembership.packageO != null}">
                                <strong>${fn:escapeXml(currentMembership.packageO.name)}</strong>
                            </c:when>
                            <c:otherwise>Gói tập</c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="payment-history-item">
                    <span class="payment-history-label">
                        <i class="fas fa-play-circle me-2"></i>Bắt Đầu
                    </span>
                    <span class="payment-history-value">
                        <%
                            if (memHistory != null && memHistory.getStartDate() != null) {
                                out.print(sdf.format(memHistory.getStartDate()));
                            } else {
                                out.print("N/A");
                            }
                        %>
                    </span>
                </div>
                <div class="payment-history-item">
                    <span class="payment-history-label">
                        <i class="fas fa-stop-circle me-2"></i>Hết Hạn
                    </span>
                    <span class="payment-history-value">
                        <%
                            if (memHistory != null && memHistory.getEndDate() != null) {
                                out.print(sdf.format(memHistory.getEndDate()));
                            } else {
                                out.print("N/A");
                            }
                        %>
                    </span>
                </div>
                <div class="payment-history-item">
                    <span class="payment-history-label">
                        <i class="fas fa-info-circle me-2"></i>Trạng Thái
                    </span>
                    <span class="payment-history-value">
                        <c:choose>
                            <c:when test="${currentMembership.status == 'ACTIVE'}">
                                <span class="badge bg-success" style="padding: 8px 15px; font-size: 0.9rem;">
                                    <i class="fas fa-check-circle me-1"></i>Đang Hoạt Động
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary" style="padding: 8px 15px; font-size: 0.9rem;">
                                    ${fn:escapeXml(currentMembership.status)}
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Quick Actions -->
    <div class="row g-4">
        <c:if test="${not empty currentMembership}">
            <div class="col-md-6">
                <div class="action-card">
                    <div class="action-card-icon">
                        <i class="fas fa-sync-alt"></i>
                    </div>
                    <h5 class="action-card-title">Gia Hạn Gói</h5>
                    <p class="action-card-description">Gia hạn gói thành viên hiện tại để tiếp tục sử dụng dịch vụ</p>
                    <form method="post" action="${pageContext.request.contextPath}/member/membership/buyNow">
                        <input type="hidden" name="packageId" value="${currentMembership.packageO.id}"/>
                        <button type="submit" class="btn-membership">
                            <i class="fas fa-credit-card me-2"></i>Gia Hạn Ngay
                        </button>
                    </form>
                </div>
            </div>
        </c:if>
        <div class="${not empty currentMembership ? 'col-md-6' : 'col-md-12'}">
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