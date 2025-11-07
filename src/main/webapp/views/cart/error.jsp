<%-- 
    Document   : error
    Created on : Oct 8, 2025, 11:26:19 AM
    Author     : ADMIN
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Lỗi thanh toán</title></head>
<body>
  <h2>${requestScope.error}</h2>
  <a href="${pageContext.request.contextPath}/carts?action=list">Quay lại giỏ hàng</a>
</body>
</html>
