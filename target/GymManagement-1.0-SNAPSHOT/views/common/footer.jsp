<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    </main>
    
    <!-- Footer -->
    <footer>
        <div class="footer-container">
            <div class="footer-column">
                <div class="footer-brand">
                  <img src="${pageContext.request.contextPath}/images/logo/logo.png" alt="GymFit Logo" style="height: 2em; width: auto; margin-right: 10px; vertical-align: middle; max-height: 40px; object-fit: contain" />
                  GymFit
                </div>
                <p>Nơi thay đổi sức khỏe và vóc dáng của bạn.</p>
                <p><i class="fas fa-phone"></i> 0123-456-789</p>
                <p><i class="fas fa-envelope"></i> contact@gymfit.vn</p>
                <p><i class="fas fa-clock"></i> 6:00 - 22:00 (Hàng ngày)</p>
            </div>
            <div class="footer-column">
                <h3>Địa chỉ</h3>
                <ul>
                    <li>123 Nguyễn Văn Linh, Đà Nẵng</li>
                    <li>45 Lý Thường Kiệt, Hà Nội</li>
                    <li>98 Trần Hưng Đạo, TP.HCM</li>
                    <li>56 Võ Văn Kiệt, Cần Thơ</li>
                </ul>
            </div>
            <div class="footer-column">
                <h3>Dịch vụ</h3>
                <ul>
                    <li>Personal Training</li>
                    <li>Group Classes</li>
                    <li>Yoga & Dance</li>
                    <li>Nutrition Products</li>
                </ul>
            </div>
            <div class="footer-column">
                <h3>Theo dõi chúng tôi</h3>
                <div class="footer-social">
                    <a href="#"><i class="fab fa-facebook"></i></a>
                    <a href="#"><i class="fab fa-instagram"></i></a>
                    <a href="#"><i class="fab fa-youtube"></i></a>
                </div>
            </div>
        </div>
        <div class="footer-bottom">© 2025 GymFit. All rights reserved.</div>
    </footer>
    
    <style>
        :root {
            --primary: #141a49;
            --accent: #ec8b5a;
            --support: #ffde59;
            --text: #2c3e50;
            --text-light: #5a6c7d;
            --card: #ffffff;
            --shadow: rgba(0, 0, 0, 0.1);
            --shadow-hover: rgba(0, 0, 0, 0.15);
            --gradient-primary: linear-gradient(135deg, #141a49 0%, #1e2a5c 100%);
            --gradient-accent: linear-gradient(135deg, #ec8b5a 0%, #d67a4f 100%);
            --gradient-support: linear-gradient(135deg, #ffde59 0%, #f4d03f 100%);
        }

        footer {
            background: var(--primary);
            color: #fff;
            padding: 50px 60px 20px;
        }

        .footer-container {
            display: flex;
            gap: 40px;
            flex-wrap: wrap;
            justify-content: space-between;
        }

        .footer-column {
            flex: 1;
            min-width: 220px;
        }

        .footer-brand {
            font-size: 1.5rem;
            font-weight: 800;
            margin-bottom: 15px;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        /* Logo đã được thay bằng hình ảnh, không cần ::before emoji nữa */

        .footer-column h3 {
            color: var(--support);
            margin-bottom: 15px;
            font-size: 1.1rem;
            font-weight: 700;
        }

        .footer-column p {
            margin-bottom: 8px;
            font-size: 0.9rem;
            line-height: 1.4;
        }

        .footer-column ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .footer-column li {
            margin-bottom: 8px;
            font-size: 0.9rem;
            line-height: 1.4;
        }

        .footer-social a {
            margin-right: 12px;
            color: #fff;
            font-size: 1.2rem;
            transition: color 0.25s;
            display: inline-block;
            width: 40px;
            height: 40px;
            background: var(--primary);
            border-radius: 50%;
            text-align: center;
            line-height: 40px;
            margin-bottom: 10px;
        }

        .footer-social a:hover {
            color: var(--support);
            background: var(--support);
            color: var(--primary);
        }

        .footer-bottom {
            text-align: center;
            margin-top: 30px;
            border-top: 1px solid rgba(255, 255, 255, 0.2);
            padding-top: 15px;
            font-size: 0.85rem;
            opacity: 0.8;
        }

        @media (max-width: 768px) {
            footer {
                padding: 30px 20px 15px;
            }

            .footer-container {
                flex-direction: column;
                gap: 25px;
            }

            .footer-column {
                min-width: 100%;
            }

            .footer-social a {
                margin-right: 15px;
            }
        }
    </style>
    
    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JavaScript -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    
    <!-- Auto-hide alerts after 5 seconds -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }, 5000);
            });
        });
    </script>
    
</body>
</html>
