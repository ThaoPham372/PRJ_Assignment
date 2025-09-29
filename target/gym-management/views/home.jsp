<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Trang Chủ - Gym Management System" />

<jsp:include page="common/header.jsp" />

<!-- Main Content -->
<div class="container-fluid py-4">
    <!-- Page Header -->
    <div class="page-header text-center">
        <h1 class="page-title">
            <i class="fas fa-dumbbell me-3"></i>
            Hệ Thống Quản Lý Phòng Gym
        </h1>
        <p class="page-subtitle">Chào mừng bạn đến với hệ thống quản lý hiện đại và toàn diện</p>
    </div>

    <!-- Dashboard Cards -->
    <div class="row mb-4">
        <div class="col-lg-3 col-md-6 mb-4">
            <div class="stat-card text-center">
                <div class="stat-icon mx-auto" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                    <i class="fas fa-users text-white fa-2x"></i>
                </div>
                <div class="stat-number">2,450</div>
                <div class="stat-label">Tổng Khách Hàng</div>
                <a href="${pageContext.request.contextPath}/views/customers/list.jsp" class="btn btn-sm btn-outline-primary mt-2">
                    Xem Chi Tiết
                </a>
            </div>
        </div>
        
        <div class="col-lg-3 col-md-6 mb-4">
            <div class="stat-card text-center">
                <div class="stat-icon mx-auto" style="background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);">
                    <i class="fas fa-cogs text-white fa-2x"></i>
                </div>
                <div class="stat-number">45</div>
                <div class="stat-label">Dịch Vụ</div>
                <a href="${pageContext.request.contextPath}/views/services/list.jsp" class="btn btn-sm btn-outline-primary mt-2">
                    Xem Chi Tiết
                </a>
            </div>
        </div>
        
        <div class="col-lg-3 col-md-6 mb-4">
            <div class="stat-card text-center">
                <div class="stat-icon mx-auto" style="background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);">
                    <i class="fas fa-shopping-cart text-white fa-2x"></i>
                </div>
                <div class="stat-number">128</div>
                <div class="stat-label">Sản Phẩm</div>
                <a href="${pageContext.request.contextPath}/views/products/list.jsp" class="btn btn-sm btn-outline-primary mt-2">
                    Xem Chi Tiết
                </a>
            </div>
        </div>
        
        <div class="col-lg-3 col-md-6 mb-4">
            <div class="stat-card text-center">
                <div class="stat-icon mx-auto" style="background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);">
                    <i class="fas fa-file-contract text-white fa-2x"></i>
                </div>
                <div class="stat-number">1,890</div>
                <div class="stat-label">Hợp Đồng</div>
                <a href="${pageContext.request.contextPath}/views/contracts/list.jsp" class="btn btn-sm btn-outline-primary mt-2">
                    Xem Chi Tiết
                </a>
            </div>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="row">
        <div class="col-lg-6 mb-4">
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">
                        <i class="fas fa-bolt me-2"></i>
                        Thao Tác Nhanh
                    </h5>
                </div>
                <div class="card-body">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/views/customers/add.jsp" 
                               class="btn btn-custom w-100 h-100 d-flex flex-column align-items-center justify-content-center py-4">
                                <i class="fas fa-user-plus fa-2x mb-2"></i>
                                <span>Thêm Khách Hàng</span>
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/views/services/add.jsp" 
                               class="btn btn-custom w-100 h-100 d-flex flex-column align-items-center justify-content-center py-4">
                                <i class="fas fa-plus fa-2x mb-2"></i>
                                <span>Thêm Dịch Vụ</span>
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/views/contracts/add.jsp" 
                               class="btn btn-custom w-100 h-100 d-flex flex-column align-items-center justify-content-center py-4">
                                <i class="fas fa-file-signature fa-2x mb-2"></i>
                                <span>Tạo Hợp Đồng</span>
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="${pageContext.request.contextPath}/views/statistics.jsp" 
                               class="btn btn-custom w-100 h-100 d-flex flex-column align-items-center justify-content-center py-4">
                                <i class="fas fa-chart-bar fa-2x mb-2"></i>
                                <span>Xem Thống Kê</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="col-lg-6 mb-4">
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">
                        <i class="fas fa-clock me-2"></i>
                        Hoạt Động Gần Đây
                    </h5>
                </div>
                <div class="card-body">
                    <div class="list-group list-group-flush">
                        <div class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <i class="fas fa-user-plus text-success me-2"></i>
                                Khách hàng mới: Nguyễn Văn A
                            </div>
                            <small class="text-muted">5 phút trước</small>
                        </div>
                        <div class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <i class="fas fa-file-contract text-info me-2"></i>
                                Hợp đồng mới: HD-2024001
                            </div>
                            <small class="text-muted">15 phút trước</small>
                        </div>
                        <div class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <i class="fas fa-edit text-warning me-2"></i>
                                Cập nhật dịch vụ: Yoga buổi sáng
                            </div>
                            <small class="text-muted">1 giờ trước</small>
                        </div>
                        <div class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <i class="fas fa-shopping-cart text-primary me-2"></i>
                                Sản phẩm mới: Protein Powder
                            </div>
                            <small class="text-muted">2 giờ trước</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- System Status -->
    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">
                        <i class="fas fa-server me-2"></i>
                        Trạng Thái Hệ Thống
                    </h5>
                </div>
                <div class="card-body">
                    <div class="row text-center">
                        <div class="col-md-3">
                            <div class="d-flex align-items-center justify-content-center">
                                <i class="fas fa-circle text-success me-2"></i>
                                <span>Database: <strong>Hoạt động</strong></span>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="d-flex align-items-center justify-content-center">
                                <i class="fas fa-circle text-success me-2"></i>
                                <span>Web Server: <strong>Hoạt động</strong></span>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="d-flex align-items-center justify-content-center">
                                <i class="fas fa-circle text-success me-2"></i>
                                <span>API: <strong>Hoạt động</strong></span>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="d-flex align-items-center justify-content-center">
                                <i class="fas fa-circle text-success me-2"></i>
                                <span>Backup: <strong>Hoạt động</strong></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</main>

<jsp:include page="common/footer.jsp" />