<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Session Debug Info</title>
    <style>
        body {
            font-family: monospace;
            padding: 20px;
            background: #f5f5f5;
        }
        .container {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #141a46;
            border-bottom: 2px solid #ec8b5e;
            padding-bottom: 10px;
        }
        .info-row {
            margin: 10px 0;
            padding: 10px;
            background: #f8f9fa;
            border-left: 3px solid #ec8b5e;
        }
        .label {
            font-weight: bold;
            color: #141a46;
        }
        .value {
            color: #2c3e50;
        }
        .null {
            color: #dc3545;
            font-style: italic;
        }
        .success {
            color: #28a745;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔍 Session Debug Information</h1>
        
        <div class="info-row">
            <span class="label">Session ID:</span>
            <span class="value">${pageContext.session.id}</span>
        </div>
        
        <div class="info-row">
            <span class="label">Session Creation Time:</span>
            <span class="value">${pageContext.session.creationTime}</span>
        </div>
        
        <div class="info-row">
            <span class="label">Last Accessed Time:</span>
            <span class="value">${pageContext.session.lastAccessedTime}</span>
        </div>
        
        <div class="info-row">
            <span class="label">isLoggedIn:</span>
            <c:choose>
                <c:when test="${sessionScope.isLoggedIn != null}">
                    <span class="value ${sessionScope.isLoggedIn ? 'success' : 'null'}">${sessionScope.isLoggedIn}</span>
                </c:when>
                <c:otherwise>
                    <span class="null">null</span>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div class="info-row">
            <span class="label">User:</span>
            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    <span class="value success">${sessionScope.user.username} (ID: ${sessionScope.user.id})</span>
                </c:when>
                <c:otherwise>
                    <span class="null">null</span>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div class="info-row">
            <span class="label">User Role (from user object):</span>
            <c:choose>
                <c:when test="${sessionScope.user != null && sessionScope.user.role != null}">
                    <span class="value success">${sessionScope.user.role}</span>
                </c:when>
                <c:otherwise>
                    <span class="null">null</span>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div class="info-row">
            <span class="label">userRoles (from session):</span>
            <c:choose>
                <c:when test="${sessionScope.userRoles != null}">
                    <span class="value success">${sessionScope.userRoles}</span>
                    <br/>
                    <small>(Type: ${sessionScope.userRoles.class.name})</small>
                </c:when>
                <c:otherwise>
                    <span class="null">null</span>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div class="info-row">
            <span class="label">Context Path:</span>
            <span class="value">${pageContext.request.contextPath}</span>
        </div>
        
        <h2 style="margin-top: 30px; color: #141a46;">🔗 Dashboard Links Test</h2>
        
        <div class="info-row">
            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    <p>Detected Role: <strong>${sessionScope.user.role}</strong></p>
                    <p>Expected Dashboard:</p>
                    <ul>
                        <c:choose>
                            <c:when test="${sessionScope.user.role == 'ADMIN'}">
                                <li><a href="${pageContext.request.contextPath}/admin/dashboard">Admin Dashboard</a></li>
                            </c:when>
                            <c:when test="${sessionScope.user.role == 'PT' || sessionScope.user.role == 'TRAINER'}">
                                <li><a href="${pageContext.request.contextPath}/pt/dashboard">PT Dashboard</a></li>
                            </c:when>
                            <c:when test="${sessionScope.user.role == 'MEMBER' || sessionScope.user.role == 'USER'}">
                                <li><a href="${pageContext.request.contextPath}/member/dashboard">Member Dashboard</a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="${pageContext.request.contextPath}/home">Home Page</a></li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </c:when>
                <c:otherwise>
                    <p class="null">Not logged in</p>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div style="margin-top: 20px;">
            <a href="${pageContext.request.contextPath}/home" style="color: #ec8b5e; text-decoration: none; font-weight: bold;">← Back to Home</a>
        </div>
    </div>
</body>
</html>

