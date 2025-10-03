<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - Lỗi Máy Chủ</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid min-vh-100 d-flex align-items-center justify-content-center">
        <div class="text-center">
            <div class="error-icon mb-4">
                <i class="fas fa-exclamation-triangle fa-5x text-danger"></i>
            </div>
            <h1 class="display-1 fw-bold text-danger">500</h1>
            <h2 class="mb-4">Lỗi Máy Chủ Nội Bộ</h2>
            <p class="lead text-muted mb-4">
                Xin lỗi, đã xảy ra lỗi trong quá trình xử lý yêu cầu của bạn.
                Vui lòng thử lại sau hoặc liên hệ quản trị viên.
            </p>
            
            <!-- Error Details (chỉ hiển thị trong development) -->
            <c:if test="${pageContext.request.serverName == 'localhost'}">
                <div class="alert alert-danger text-start mt-4" style="max-width: 600px; margin: 0 auto;">
                    <h6><i class="fas fa-bug me-2"></i>Chi Tiết Lỗi (Development Mode):</h6>
                    <c:if test="${not empty pageContext.exception}">
                        <strong>Exception:</strong> ${pageContext.exception.class.simpleName}<br>
                        <strong>Message:</strong> ${pageContext.exception.message}<br>
                        <c:if test="${not empty pageContext.exception.cause}">
                            <strong>Cause:</strong> ${pageContext.exception.cause.message}<br>
                        </c:if>
                    </c:if>
                    <c:if test="${not empty requestScope['javax.servlet.error.exception']}">
                        <strong>Servlet Exception:</strong> ${requestScope['javax.servlet.error.exception'].class.simpleName}<br>
                        <strong>Message:</strong> ${requestScope['javax.servlet.error.exception'].message}<br>
                    </c:if>
                    <c:if test="${not empty requestScope['javax.servlet.error.message']}">
                        <strong>Error Message:</strong> ${requestScope['javax.servlet.error.message']}<br>
                    </c:if>
                    <c:if test="${not empty requestScope['javax.servlet.error.request_uri']}">
                        <strong>Request URI:</strong> ${requestScope['javax.servlet.error.request_uri']}<br>
                    </c:if>
                </div>
            </c:if>
            
            <div class="d-flex justify-content-center gap-3 mt-4">
                <a href="${pageContext.request.contextPath}/views/home.jsp" class="btn btn-custom">
                    <i class="fas fa-home me-2"></i>Về Trang Chủ
                </a>
                <button class="btn btn-outline-secondary" onclick="history.back()">
                    <i class="fas fa-arrow-left me-2"></i>Quay Lại
                </button>
                <button class="btn btn-outline-info" onclick="location.reload()">
                    <i class="fas fa-redo me-2"></i>Thử Lại
                </button>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
