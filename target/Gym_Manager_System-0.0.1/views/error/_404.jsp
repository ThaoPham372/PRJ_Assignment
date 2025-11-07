<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>404 - Không Tìm Thấy Trang</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
      rel="stylesheet"
    />
    <link
      href="${pageContext.request.contextPath}/css/styles.css"
      rel="stylesheet"
    />
  </head>
  <body>
    <div
      class="container-fluid min-vh-100 d-flex align-items-center justify-content-center"
    >
      <div class="text-center">
        <div class="error-icon mb-4">
          <i class="fas fa-search fa-5x text-muted"></i>
        </div>
        <h1 class="display-1 fw-bold text-primary">404</h1>
        <h2 class="mb-4">Không Tìm Thấy Trang</h2>
        <p class="lead text-muted mb-4">
          Xin lỗi, trang bạn đang tìm kiếm không tồn tại hoặc đã bị di chuyển.
        </p>
        <div class="d-flex justify-content-center gap-3">
          <a
            href="${pageContext.request.contextPath}/views/home.jsp"
            class="btn btn-custom"
          >
            <i class="fas fa-home me-2"></i>Về Trang Chủ
          </a>
          <button class="btn btn-outline-secondary" onclick="history.back()">
            <i class="fas fa-arrow-left me-2"></i>Quay Lại
          </button>
        </div>
      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
