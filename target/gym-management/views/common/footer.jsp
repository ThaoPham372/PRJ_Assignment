<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    </main>
    
    <!-- Footer -->
    <footer class="footer mt-5">
        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <h5><i class="fas fa-dumbbell"></i> Gym Management System</h5>
                    <p class="mb-3">Hệ thống quản lý phòng gym hiện đại, giúp tối ưu hóa việc quản lý khách hàng, dịch vụ và doanh thu.</p>
                    <div class="social-links">
                        <a href="#" class="text-white me-3"><i class="fab fa-facebook"></i></a>
                        <a href="#" class="text-white me-3"><i class="fab fa-instagram"></i></a>
                        <a href="#" class="text-white me-3"><i class="fab fa-youtube"></i></a>
                        <a href="#" class="text-white"><i class="fab fa-tiktok"></i></a>
                    </div>
                </div>
                
                <div class="col-md-2">
                    <h6>Quản Lý</h6>
                    <ul class="list-unstyled">
                        <li><a href="${pageContext.request.contextPath}/views/customers/list.jsp" class="text-white-50">Khách Hàng</a></li>
                        <li><a href="${pageContext.request.contextPath}/views/services/list.jsp" class="text-white-50">Dịch Vụ</a></li>
                        <li><a href="${pageContext.request.contextPath}/views/products/list.jsp" class="text-white-50">Sản Phẩm</a></li>
                        <li><a href="${pageContext.request.contextPath}/views/contracts/list.jsp" class="text-white-50">Hợp Đồng</a></li>
                    </ul>
                </div>
                
                <div class="col-md-2">
                    <h6>Báo Cáo</h6>
                    <ul class="list-unstyled">
                        <li><a href="${pageContext.request.contextPath}/views/statistics.jsp" class="text-white-50">Thống Kê</a></li>
                        <li><a href="#" class="text-white-50">Doanh Thu</a></li>
                        <li><a href="#" class="text-white-50">Khách Hàng VIP</a></li>
                        <li><a href="#" class="text-white-50">Sản Phẩm Bán Chạy</a></li>
                    </ul>
                </div>
                
                <div class="col-md-4">
                    <h6>Liên Hệ</h6>
                    <ul class="list-unstyled">
                        <li class="mb-2">
                            <i class="fas fa-map-marker-alt me-2"></i>
                            123 Đường ABC, Quận XYZ, TP.HCM
                        </li>
                        <li class="mb-2">
                            <i class="fas fa-phone me-2"></i>
                            <a href="tel:+84901234567" class="text-white-50">0901 234 567</a>
                        </li>
                        <li class="mb-2">
                            <i class="fas fa-envelope me-2"></i>
                            <a href="mailto:info@gymmanager.com" class="text-white-50">info@gymmanager.com</a>
                        </li>
                        <li>
                            <i class="fas fa-clock me-2"></i>
                            24/7 - Hỗ trợ liên tục
                        </li>
                    </ul>
                </div>
            </div>
            
            <hr class="my-4" style="border-color: rgba(255,255,255,0.2);">
            
            <div class="row align-items-center">
                <div class="col-md-6">
                    <p class="mb-0">&copy; 2024 Gym Management System. All rights reserved.</p>
                </div>
                <div class="col-md-6 text-md-end">
                    <small class="text-white-50">
                        Built with ❤️ using JSP, JSTL & Servlet | Version 1.0.0
                    </small>
                </div>
            </div>
        </div>
    </footer>
    
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
