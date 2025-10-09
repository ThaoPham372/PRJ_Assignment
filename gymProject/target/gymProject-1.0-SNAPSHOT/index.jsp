<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Sau 2 giây, tự động chuyển hướng tới home.jsp -->
    <meta http-equiv="refresh" content="2;url=home"> 
    <title>Redirecting to Home</title>
    <style>
        /* Reset */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #4facfe, #00f2fe);
            color: #fff;
            text-align: center;
        }

        .container {
            background: rgba(0, 0, 0, 0.3);
            padding: 30px 50px;
            border-radius: 12px;
            box-shadow: 0 8px 16px rgba(0,0,0,0.2);
            animation: fadeIn 1s ease-in-out;
        }

        h2 {
            font-size: 1.5rem;
            margin-bottom: 12px;
        }

        .loader {
            margin: 15px auto;
            width: 40px;
            height: 40px;
            border: 4px solid rgba(255,255,255,0.3);
            border-top: 4px solid #fff;
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            100% { transform: rotate(360deg); }
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        p {
            font-size: 0.9rem;
            opacity: 0.8;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>You are being redirected to the Home page...</h2>
        <div class="loader"></div>
        <p>Please wait a moment.</p>
    </div>
</body>
</html>
