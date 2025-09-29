<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
isErrorPage="true" %> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đã Xảy Ra Lỗi</title>
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
          <i class="fas fa-exclamation-circle fa-5x text-warning"></i>
        </div>
        <h1 class="display-4 fw-bold text-primary">Oops!</h1>
        <h2 class="mb-4">Đã Xảy Ra Lỗi</h2>
        <p class="lead text-muted mb-4">
          Có vẻ như đã xảy ra sự cố không mong muốn. Đội ngũ kỹ thuật đã được
          thông báo và sẽ khắc phục sớm nhất.
        </p>

        <!-- Error Information -->
        <div class="row justify-content-center">
          <div class="col-md-8">
            <div class="card border-warning">
              <div class="card-header bg-warning text-dark">
                <i class="fas fa-info-circle me-2"></i>Thông Tin Lỗi
              </div>
              <div class="card-body text-start">
                <c:choose>
                  <c:when test="${not empty pageContext.exception}">
                    <p>
                      <strong>Loại lỗi:</strong>
                      ${pageContext.exception.class.simpleName}
                    </p>
                    <c:if test="${not empty pageContext.exception.message}">
                      <p>
                        <strong>Thông báo:</strong>
                        ${pageContext.exception.message}
                      </p>
                    </c:if>
                  </c:when>
                  <c:when
                    test="${not empty requestScope['jakarta.servlet.error.exception']}"
                  >
                    <p>
                      <strong>Loại lỗi:</strong>
                      ${requestScope['jakarta.servlet.error.exception'].class.simpleName}
                    </p>
                    <c:if
                      test="${not empty requestScope['jakarta.servlet.error.message']}"
                    >
                      <p>
                        <strong>Thông báo:</strong>
                        ${requestScope['jakarta.servlet.error.message']}
                      </p>
                    </c:if>
                  </c:when>
                  <c:otherwise>
                    <p>
                      <strong>Thông báo:</strong> Đã xảy ra lỗi không xác định
                      trong hệ thống.
                    </p>
                  </c:otherwise>
                </c:choose>

                <c:if
                  test="${not empty requestScope['jakarta.servlet.error.request_uri']}"
                >
                  <p>
                    <strong>URL yêu cầu:</strong>
                    ${requestScope['jakarta.servlet.error.request_uri']}
                  </p>
                </c:if>

                <p><strong>Thời gian:</strong> <%= new java.util.Date() %></p>
              </div>
            </div>
          </div>
        </div>

        <div class="d-flex justify-content-center gap-3 mt-4">
          <a
            href="${pageContext.request.contextPath}/views/home.jsp"
            class="btn btn-custom"
          >
            <i class="fas fa-home me-2"></i>Về Trang Chủ
          </a>
          <button class="btn btn-outline-secondary" onclick="history.back()">
            <i class="fas fa-arrow-left me-2"></i>Quay Lại
          </button>
          <button class="btn btn-outline-success" onclick="reportError()">
            <i class="fas fa-bug me-2"></i>Báo Cáo Lỗi
          </button>
        </div>
      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
      function reportError() {
        // In real application, this would send error report to server
        alert(
          'Cảm ơn bạn đã báo cáo lỗi! Chúng tôi sẽ khắc phục sớm nhất có thể.',
        );
      }
    </script>
  </body>
</html>
