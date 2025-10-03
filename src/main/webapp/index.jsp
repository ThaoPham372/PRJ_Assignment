<%@ page contentType="text/html;charset=UTF-8" language="java" %> <% // Redirect
to HomeServlet when index.jsp is accessed
response.sendRedirect(request.getContextPath() + "/home"); %>
