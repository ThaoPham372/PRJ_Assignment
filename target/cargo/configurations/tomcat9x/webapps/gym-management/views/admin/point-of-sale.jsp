<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Point of Sale - Stamina Gym</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
    <!-- Admin Sidebar -->
    <nav class="admin-sidebar" id="sidebar">
        <div class="sidebar-header">
            <a href="#" class="sidebar-brand">
                <i class="fas fa-dumbbell me-2"></i>STAMINA GYM
            </a>
            <p class="text-center mb-0 opacity-75">Admin Panel</p>
        </div>
        
        <ul class="nav flex-column mt-4">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/views/admin/dashboard.jsp">
                    <i class="fas fa-tachometer-alt"></i>
                    Dashboard
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/views/admin/members.jsp">
                    <i class="fas fa-users"></i>
                    Quản Lý Thành Viên
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/views/admin/coaches.jsp">
                    <i class="fas fa-user-tie"></i>
                    Huấn Luyện Viên
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/views/admin/equipment.jsp">
                    <i class="fas fa-dumbbell"></i>
                    Thiết Bị & Tồn Kho
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/views/admin/sales-report.jsp">
                    <i class="fas fa-chart-line"></i>
                    Báo Cáo Doanh Thu
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link active" href="${pageContext.request.contextPath}/views/admin/point-of-sale.jsp">
                    <i class="fas fa-cash-register"></i>
                    Point of Sale
                </a>
            </li>
            <hr class="mx-3 my-3" style="border-color: rgba(255,255,255,0.2);">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/views/admin/settings.jsp">
                    <i class="fas fa-cog"></i>
                    Cài Đặt
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/index.jsp">
                    <i class="fas fa-home"></i>
                    Về Trang Chủ
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                    <i class="fas fa-sign-out-alt"></i>
                    Đăng Xuất
                </a>
            </li>
        </ul>
    </nav>
    
    <!-- Main Content -->
    <div class="admin-content">
        <!-- Top Navigation -->
        <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm mb-4">
            <div class="container-fluid">
                <button class="btn btn-outline-primary d-lg-none" type="button" id="sidebarToggle">
                    <i class="fas fa-bars"></i>
                </button>
                
                <h4 class="navbar-brand mb-0 fw-bold text-primary">
                    <i class="fas fa-cash-register me-2"></i>Point of Sale
                </h4>
                
                <div class="navbar-nav ms-auto">
                    <div class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" 
                           id="userDropdown" role="button" data-bs-toggle="dropdown">
                            <img src="https://via.placeholder.com/32x32/3B1E78/FFD700?text=A" 
                                 class="rounded-circle me-2" alt="Admin">
                            <span class="fw-semibold">Admin User</span>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="#"><i class="fas fa-user me-2"></i>Hồ Sơ</a></li>
                            <li><a class="dropdown-item" href="#"><i class="fas fa-cog me-2"></i>Cài Đặt</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                <i class="fas fa-sign-out-alt me-2"></i>Đăng Xuất</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </nav>
        
        <!-- Page Header -->
        <div class="page-header">
            <div class="container-fluid">
                <div class="row align-items-center">
                    <div class="col">
                        <h1 class="page-title">Point of Sale</h1>
                        <p class="page-subtitle">Xử lý thanh toán membership, PT và sản phẩm</p>
                    </div>
                    <div class="col-auto">
                        <button class="btn btn-secondary" onclick="newTransaction()">
                            <i class="fas fa-plus me-2"></i>Giao Dịch Mới
                        </button>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- POS Content -->
        <div class="container-fluid">
            <div class="row">
                <!-- Left Panel - Products & Services -->
                <div class="col-lg-8 mb-4">
                    <!-- Quick Actions -->
                    <div class="card border-radius-custom shadow-custom mb-4">
                        <div class="card-header bg-gradient-purple text-white">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-bolt me-2"></i>Thao Tác Nhanh
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-3 mb-3">
                                    <button class="btn btn-outline-primary w-100 h-100 p-3" onclick="addMembership()">
                                        <i class="fas fa-id-card fa-2x mb-2 d-block"></i>
                                        <small>Đăng Ký<br>Membership</small>
                                    </button>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <button class="btn btn-outline-success w-100 h-100 p-3" onclick="addPTSession()">
                                        <i class="fas fa-user-tie fa-2x mb-2 d-block"></i>
                                        <small>Mua Buổi<br>Personal Training</small>
                                    </button>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <button class="btn btn-outline-warning w-100 h-100 p-3" onclick="renewMembership()">
                                        <i class="fas fa-redo fa-2x mb-2 d-block"></i>
                                        <small>Gia Hạn<br>Membership</small>
                                    </button>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <button class="btn btn-outline-info w-100 h-100 p-3" onclick="addProduct()">
                                        <i class="fas fa-shopping-cart fa-2x mb-2 d-block"></i>
                                        <small>Mua Sản Phẩm<br>& Phụ Kiện</small>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Products Grid -->
                    <div class="card border-radius-custom shadow-custom">
                        <div class="card-header bg-gradient-purple text-white">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-shopping-bag me-2"></i>Sản Phẩm & Dịch Vụ
                            </h5>
                        </div>
                        <div class="card-body">
                            <!-- Category Tabs -->
                            <ul class="nav nav-pills mb-3" id="categoryTabs">
                                <li class="nav-item">
                                    <a class="nav-link active" data-bs-toggle="pill" href="#membership">Membership</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" data-bs-toggle="pill" href="#pt-services">Personal Training</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" data-bs-toggle="pill" href="#products">Sản Phẩm</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" data-bs-toggle="pill" href="#beverages">Nước Uống</a>
                                </li>
                            </ul>
                            
                            <div class="tab-content">
                                <!-- Membership Tab -->
                                <div class="tab-pane fade show active" id="membership">
                                    <div class="row">
                                        <c:forEach var="i" begin="1" end="3">
                                            <div class="col-md-4 mb-3">
                                                <div class="card h-100 product-card" onclick="addToCart('membership', ${i})">
                                                    <div class="card-body text-center">
                                                        <i class="fas fa-crown fa-2x text-primary mb-2"></i>
                                                        <h6 class="card-title">
                                                            ${i == 1 ? 'Basic' : (i == 2 ? 'Premium' : 'VIP')} Package
                                                        </h6>
                                                        <p class="card-text text-muted small">
                                                            ${i == 1 ? 'Truy cập cơ bản' : (i == 2 ? 'Bao gồm 2 buổi PT' : 'PT không giới hạn')}
                                                        </p>
                                                        <h5 class="text-primary fw-bold">
                                                            ${i == 1 ? '300,000₫' : (i == 2 ? '500,000₫' : '800,000₫')}/tháng
                                                        </h5>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                
                                <!-- PT Services Tab -->
                                <div class="tab-pane fade" id="pt-services">
                                    <div class="row">
                                        <c:forEach var="i" begin="1" end="4">
                                            <div class="col-md-3 mb-3">
                                                <div class="card h-100 product-card" onclick="addToCart('pt', ${i})">
                                                    <div class="card-body text-center">
                                                        <i class="fas fa-dumbbell fa-2x text-success mb-2"></i>
                                                        <h6 class="card-title">
                                                            ${i == 1 ? '1 Buổi PT' : (i == 2 ? '5 Buổi PT' : (i == 3 ? '10 Buổi PT' : '20 Buổi PT'))}
                                                        </h6>
                                                        <p class="card-text text-muted small">
                                                            ${i == 1 ? 'Thử nghiệm' : (i == 2 ? 'Gói cơ bản' : (i == 3 ? 'Phổ biến' : 'Tiết kiệm nhất'))}
                                                        </p>
                                                        <h5 class="text-success fw-bold">
                                                            ${i == 1 ? '200,000₫' : (i == 2 ? '950,000₫' : (i == 3 ? '1,800,000₫' : '3,400,000₫'))}
                                                        </h5>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                
                                <!-- Products Tab -->
                                <div class="tab-pane fade" id="products">
                                    <div class="row">
                                        <c:forEach var="i" begin="1" end="6">
                                            <div class="col-md-2 mb-3">
                                                <div class="card h-100 product-card" onclick="addToCart('product', ${i})">
                                                    <div class="card-body text-center p-2">
                                                        <img src="https://via.placeholder.com/60x60/3B1E78/FFD700?text=P${i}" 
                                                             class="img-fluid mb-2" alt="Product ${i}">
                                                        <h6 class="card-title small">
                                                            <c:choose>
                                                                <c:when test="${i == 1}">Protein Powder</c:when>
                                                                <c:when test="${i == 2}">Whey Protein</c:when>
                                                                <c:when test="${i == 3}">BCAA</c:when>
                                                                <c:when test="${i == 4}">Creatine</c:when>
                                                                <c:when test="${i == 5}">Găng Tay Tập</c:when>
                                                                <c:otherwise>Khăn Gym</c:otherwise>
                                                            </c:choose>
                                                        </h6>
                                                        <h6 class="text-warning fw-bold">
                                                            ${i <= 4 ? String.format('%,d₫', 850000 + (i * 150000)) : String.format('%,d₫', 150000 + (i * 50000))}
                                                        </h6>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                
                                <!-- Beverages Tab -->
                                <div class="tab-pane fade" id="beverages">
                                    <div class="row">
                                        <c:forEach var="i" begin="1" end="8">
                                            <div class="col-md-2 mb-3">
                                                <div class="card h-100 product-card" onclick="addToCart('beverage', ${i})">
                                                    <div class="card-body text-center p-2">
                                                        <i class="fas fa-${i <= 4 ? 'coffee' : 'tint'} fa-2x text-info mb-2"></i>
                                                        <h6 class="card-title small">
                                                            <c:choose>
                                                                <c:when test="${i == 1}">Energy Drink</c:when>
                                                                <c:when test="${i == 2}">Protein Shake</c:when>
                                                                <c:when test="${i == 3}">Vitamin Water</c:when>
                                                                <c:when test="${i == 4}">Sports Drink</c:when>
                                                                <c:when test="${i == 5}">Nước Lọc</c:when>
                                                                <c:when test="${i == 6}">Nước Dừa</c:when>
                                                                <c:when test="${i == 7}">Nước Ép</c:when>
                                                                <c:otherwise>Trà Xanh</c:otherwise>
                                                            </c:choose>
                                                        </h6>
                                                        <h6 class="text-info fw-bold">
                                                            ${String.format('%,d₫', 15000 + (i * 10000))}
                                                        </h6>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Right Panel - Cart & Checkout -->
                <div class="col-lg-4">
                    <!-- Customer Search -->
                    <div class="card border-radius-custom shadow-custom mb-4">
                        <div class="card-header bg-gradient-purple text-white">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-user me-2"></i>Khách Hàng
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="search-box mb-3">
                                <i class="fas fa-search search-icon"></i>
                                <input type="text" class="form-control" placeholder="Tìm khách hàng..." 
                                       id="customerSearch">
                            </div>
                            <div id="customerInfo" class="d-none">
                                <div class="d-flex align-items-center p-3 bg-light rounded">
                                    <img src="https://via.placeholder.com/50x50/3B1E78/FFD700?text=KH" 
                                         class="rounded-circle me-3" alt="Customer">
                                    <div>
                                        <h6 class="mb-1" id="customerName">Nguyễn Văn A</h6>
                                        <small class="text-muted" id="customerMembership">Premium Member</small>
                                    </div>
                                </div>
                            </div>
                            <button class="btn btn-outline-primary w-100 mt-2" onclick="walkInCustomer()">
                                <i class="fas fa-walking me-2"></i>Khách Vãng Lai
                            </button>
                        </div>
                    </div>
                    
                    <!-- Shopping Cart -->
                    <div class="card border-radius-custom shadow-custom mb-4">
                        <div class="card-header bg-gradient-purple text-white">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-shopping-cart me-2"></i>Giỏ Hàng
                            </h5>
                        </div>
                        <div class="card-body">
                            <div id="cartItems">
                                <!-- Cart items will be dynamically added here -->
                                <div class="text-center text-muted py-4">
                                    <i class="fas fa-shopping-cart fa-3x mb-3"></i>
                                    <p>Giỏ hàng trống</p>
                                    <small>Thêm sản phẩm để bắt đầu</small>
                                </div>
                            </div>
                            
                            <!-- Cart Total -->
                            <div class="border-top pt-3 mt-3">
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Tạm tính:</span>
                                    <span id="subtotal">0₫</span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Giảm giá:</span>
                                    <span id="discount" class="text-success">0₫</span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>VAT (10%):</span>
                                    <span id="tax">0₫</span>
                                </div>
                                <hr>
                                <div class="d-flex justify-content-between fw-bold h5">
                                    <span>Tổng cộng:</span>
                                    <span id="total" class="text-primary">0₫</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Payment -->
                    <div class="card border-radius-custom shadow-custom">
                        <div class="card-header bg-gradient-purple text-white">
                            <h5 class="card-title mb-0">
                                <i class="fas fa-credit-card me-2"></i>Thanh Toán
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label class="form-label">Phương thức thanh toán</label>
                                <select class="form-control" id="paymentMethod">
                                    <option value="cash">Tiền mặt</option>
                                    <option value="card">Thẻ tín dụng</option>
                                    <option value="bank">Chuyển khoản</option>
                                    <option value="qr">QR Code</option>
                                </select>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label">Tiền nhận</label>
                                <input type="number" class="form-control" id="receivedAmount" 
                                       placeholder="Nhập số tiền nhận">
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label">Tiền thừa</label>
                                <input type="text" class="form-control" id="changeAmount" readonly>
                            </div>
                            
                            <div class="d-grid gap-2">
                                <button class="btn btn-success btn-lg" onclick="processPayment()">
                                    <i class="fas fa-check me-2"></i>Thanh Toán
                                </button>
                                <button class="btn btn-outline-secondary" onclick="clearCart()">
                                    <i class="fas fa-trash me-2"></i>Xóa Giỏ Hàng
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Recent Transactions -->
            <div class="card border-radius-custom shadow-custom mt-4">
                <div class="card-header bg-gradient-purple text-white">
                    <h5 class="card-title mb-0">
                        <i class="fas fa-history me-2"></i>Giao Dịch Gần Đây
                    </h5>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead>
                                <tr>
                                    <th>Mã GD</th>
                                    <th>Thời Gian</th>
                                    <th>Khách Hàng</th>
                                    <th>Sản Phẩm/Dịch Vụ</th>
                                    <th>Số Tiền</th>
                                    <th>Thanh Toán</th>
                                    <th>Trạng Thái</th>
                                    <th>Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%-- Mock recent transactions data using JSTL forEach --%>
                                <c:forEach var="i" begin="1" end="5">
                                    <tr>
                                        <td><strong>TXN${String.format("%04d", 1000 + i)}</strong></td>
                                        <td>${String.format("%02d", 14 + i)}:${String.format("%02d", 30 + (i * 10))}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${i == 1}">Nguyễn Văn A</c:when>
                                                <c:when test="${i == 2}">Trần Thị B</c:when>
                                                <c:when test="${i == 3}">Lê Văn C</c:when>
                                                <c:when test="${i == 4}">Phạm Thị D</c:when>
                                                <c:otherwise>Khách vãng lai</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${i == 1}">Premium Membership</c:when>
                                                <c:when test="${i == 2}">5 Buổi PT</c:when>
                                                <c:when test="${i == 3}">Protein Powder</c:when>
                                                <c:when test="${i == 4}">VIP Membership</c:when>
                                                <c:otherwise>Energy Drink x2</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="fw-bold">
                                            <c:choose>
                                                <c:when test="${i == 1}">500,000₫</c:when>
                                                <c:when test="${i == 2}">950,000₫</c:when>
                                                <c:when test="${i == 3}">1,000,000₫</c:when>
                                                <c:when test="${i == 4}">800,000₫</c:when>
                                                <c:otherwise>50,000₫</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${i % 3 == 1}">Tiền mặt</c:when>
                                                <c:when test="${i % 3 == 2}">Thẻ</c:when>
                                                <c:otherwise>Chuyển khoản</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <span class="status-badge status-active">Hoàn thành</span>
                                        </td>
                                        <td>
                                            <div class="action-buttons">
                                                <button class="btn btn-view" title="Xem chi tiết" 
                                                        onclick="viewTransaction(${1000 + i})">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <button class="btn btn-edit" title="In hóa đơn" 
                                                        onclick="printReceipt(${1000 + i})">
                                                    <i class="fas fa-print"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        let cart = [];
        let customer = null;
        
        document.addEventListener('DOMContentLoaded', function() {
            // Sidebar toggle for mobile
            const sidebarToggle = document.getElementById('sidebarToggle');
            const sidebar = document.getElementById('sidebar');
            
            if (sidebarToggle) {
                sidebarToggle.addEventListener('click', function() {
                    sidebar.classList.toggle('show');
                });
            }
            
            // Customer search
            const customerSearch = document.getElementById('customerSearch');
            customerSearch.addEventListener('input', function() {
                if (this.value.length > 2) {
                    // Simulate customer search
                    setTimeout(() => {
                        if (this.value.toLowerCase().includes('nguyen')) {
                            showCustomerInfo();
                        }
                    }, 500);
                }
            });
            
            // Received amount calculation
            const receivedAmount = document.getElementById('receivedAmount');
            receivedAmount.addEventListener('input', calculateChange);
            
            // Add product card click styles
            const productCards = document.querySelectorAll('.product-card');
            productCards.forEach(card => {
                card.style.cursor = 'pointer';
                card.style.transition = 'all 0.3s ease';
                
                card.addEventListener('mouseenter', function() {
                    this.style.transform = 'scale(1.05)';
                    this.style.boxShadow = '0 10px 20px rgba(59, 30, 120, 0.2)';
                });
                
                card.addEventListener('mouseleave', function() {
                    this.style.transform = 'scale(1)';
                    this.style.boxShadow = '0 2px 4px rgba(0,0,0,0.1)';
                });
            });
        });
        
        function showCustomerInfo() {
            document.getElementById('customerInfo').classList.remove('d-none');
            customer = {
                id: 'GM0001',
                name: 'Nguyễn Văn A',
                membership: 'Premium Member',
                discount: 0.05 // 5% discount for members
            };
        }
        
        function walkInCustomer() {
            document.getElementById('customerInfo').classList.add('d-none');
            document.getElementById('customerSearch').value = '';
            customer = null;
            updateCartDisplay();
        }
        
        function addMembership() {
            addToCart('membership', 2); // Default to Premium
        }
        
        function addPTSession() {
            addToCart('pt', 3); // Default to 10 sessions
        }
        
        function renewMembership() {
            if (!customer) {
                alert('Vui lòng chọn khách hàng trước');
                return;
            }
            addToCart('membership', 2); // Renew Premium
        }
        
        function addProduct() {
            addToCart('product', 1); // Default to Protein Powder
        }
        
        function addToCart(type, id) {
            const products = {
                membership: {
                    1: {name: 'Basic Package', price: 300000},
                    2: {name: 'Premium Package', price: 500000},
                    3: {name: 'VIP Package', price: 800000}
                },
                pt: {
                    1: {name: '1 Buổi PT', price: 200000},
                    2: {name: '5 Buổi PT', price: 950000},
                    3: {name: '10 Buổi PT', price: 1800000},
                    4: {name: '20 Buổi PT', price: 3400000}
                },
                product: {
                    1: {name: 'Protein Powder', price: 1000000},
                    2: {name: 'Whey Protein', price: 1150000},
                    3: {name: 'BCAA', price: 1300000},
                    4: {name: 'Creatine', price: 1450000},
                    5: {name: 'Găng Tay Tập', price: 300000},
                    6: {name: 'Khăn Gym', price: 350000}
                },
                beverage: {
                    1: {name: 'Energy Drink', price: 25000},
                    2: {name: 'Protein Shake', price: 35000},
                    3: {name: 'Vitamin Water', price: 45000},
                    4: {name: 'Sports Drink', price: 55000},
                    5: {name: 'Nước Lọc', price: 65000},
                    6: {name: 'Nước Dừa', price: 75000},
                    7: {name: 'Nước Ép', price: 85000},
                    8: {name: 'Trà Xanh', price: 95000}
                }
            };
            
            const product = products[type][id];
            if (!product) return;
            
            // Check if item already exists in cart
            const existingItem = cart.find(item => item.type === type && item.id === id);
            if (existingItem) {
                existingItem.quantity += 1;
            } else {
                cart.push({
                    type: type,
                    id: id,
                    name: product.name,
                    price: product.price,
                    quantity: 1
                });
            }
            
            updateCartDisplay();
        }
        
        function removeFromCart(index) {
            cart.splice(index, 1);
            updateCartDisplay();
        }
        
        function updateQuantity(index, quantity) {
            if (quantity <= 0) {
                removeFromCart(index);
            } else {
                cart[index].quantity = quantity;
                updateCartDisplay();
            }
        }
        
        function updateCartDisplay() {
            const cartItems = document.getElementById('cartItems');
            
            if (cart.length === 0) {
                cartItems.innerHTML = `
                    <div class="text-center text-muted py-4">
                        <i class="fas fa-shopping-cart fa-3x mb-3"></i>
                        <p>Giỏ hàng trống</p>
                        <small>Thêm sản phẩm để bắt đầu</small>
                    </div>
                `;
            } else {
                cartItems.innerHTML = cart.map((item, index) => `
                    <div class="d-flex align-items-center justify-content-between mb-3 p-2 border rounded">
                        <div class="flex-grow-1">
                            <h6 class="mb-1">\${item.name}</h6>
                            <small class="text-muted">\${item.price.toLocaleString('vi-VN')}₫</small>
                        </div>
                        <div class="d-flex align-items-center">
                            <button class="btn btn-sm btn-outline-secondary" onclick="updateQuantity(\${index}, \${item.quantity - 1})">-</button>
                            <span class="mx-2 fw-bold">\${item.quantity}</span>
                            <button class="btn btn-sm btn-outline-secondary" onclick="updateQuantity(\${index}, \${item.quantity + 1})">+</button>
                            <button class="btn btn-sm btn-outline-danger ms-2" onclick="removeFromCart(\${index})">
                                <i class="fas fa-trash"></i>
                            </button>
                        </div>
                    </div>
                `).join('');
            }
            
            // Calculate totals
            const subtotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
            const discountAmount = customer ? subtotal * customer.discount : 0;
            const taxableAmount = subtotal - discountAmount;
            const tax = taxableAmount * 0.1; // 10% VAT
            const total = taxableAmount + tax;
            
            document.getElementById('subtotal').textContent = subtotal.toLocaleString('vi-VN') + '₫';
            document.getElementById('discount').textContent = discountAmount > 0 ? '-' + discountAmount.toLocaleString('vi-VN') + '₫' : '0₫';
            document.getElementById('tax').textContent = tax.toLocaleString('vi-VN') + '₫';
            document.getElementById('total').textContent = total.toLocaleString('vi-VN') + '₫';
            
            // Update received amount calculation
            calculateChange();
        }
        
        function calculateChange() {
            const total = parseFloat(document.getElementById('total').textContent.replace(/[^\d]/g, ''));
            const received = parseFloat(document.getElementById('receivedAmount').value) || 0;
            const change = received - total;
            
            document.getElementById('changeAmount').value = change >= 0 ? change.toLocaleString('vi-VN') + '₫' : '0₫';
        }
        
        function processPayment() {
            if (cart.length === 0) {
                alert('Giỏ hàng trống!');
                return;
            }
            
            const paymentMethod = document.getElementById('paymentMethod').value;
            const total = parseFloat(document.getElementById('total').textContent.replace(/[^\d]/g, ''));
            const received = parseFloat(document.getElementById('receivedAmount').value) || 0;
            
            if (paymentMethod === 'cash' && received < total) {
                alert('Số tiền nhận không đủ!');
                return;
            }
            
            // Simulate payment processing
            const loading = document.createElement('div');
            loading.innerHTML = '<div class="text-center"><div class="loading"></div><p class="mt-2">Đang xử lý thanh toán...</p></div>';
            document.querySelector('.card-body').appendChild(loading);
            
            setTimeout(() => {
                alert('Thanh toán thành công!');
                printReceipt();
                clearCart();
                loading.remove();
            }, 2000);
        }
        
        function clearCart() {
            cart = [];
            document.getElementById('receivedAmount').value = '';
            document.getElementById('changeAmount').value = '';
            updateCartDisplay();
        }
        
        function newTransaction() {
            clearCart();
            walkInCustomer();
        }
        
        function viewTransaction(id) {
            alert('Xem chi tiết giao dịch: TXN' + String(id).padStart(4, '0'));
        }
        
        function printReceipt(id) {
            if (id) {
                alert('In hóa đơn giao dịch: TXN' + String(id).padStart(4, '0'));
            } else {
                alert('In hóa đơn cho giao dịch hiện tại');
            }
            // In real app, this would generate and print receipt
        }
    </script>
</body>
</html>
