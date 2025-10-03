<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Thiết Bị - Stamina Gym</title>
    
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
                <a class="nav-link active" href="${pageContext.request.contextPath}/views/admin/equipment.jsp">
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
                <a class="nav-link" href="${pageContext.request.contextPath}/views/admin/point-of-sale.jsp">
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
                    <i class="fas fa-dumbbell me-2"></i>Quản Lý Thiết Bị
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
                        <h1 class="page-title">Quản Lý Thiết Bị & Tồn Kho</h1>
                        <p class="page-subtitle">Theo dõi tình trạng và bảo trì thiết bị phòng gym</p>
                    </div>
                    <div class="col-auto">
                        <button class="btn btn-secondary me-2" data-bs-toggle="modal" data-bs-target="#addEquipmentModal">
                            <i class="fas fa-plus me-2"></i>Thêm Thiết Bị
                        </button>
                        <button class="btn btn-warning" onclick="scheduleMaintenanceAll()">
                            <i class="fas fa-tools me-2"></i>Lập Lịch Bảo Trì
                        </button>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Equipment Content -->
        <div class="container-fluid">
            <!-- Statistics Cards -->
            <div class="row mb-4">
                <c:set var="totalEquipment" value="150" />
                <c:set var="workingEquipment" value="142" />
                <c:set var="maintenanceEquipment" value="5" />
                <c:set var="brokenEquipment" value="3" />
                
                <div class="col-xl-3 col-md-6 mb-3">
                    <div class="stat-card">
                        <div class="stat-icon bg-gradient-purple">
                            <i class="fas fa-dumbbell"></i>
                        </div>
                        <div class="stat-number">${totalEquipment}</div>
                        <div class="stat-label">Tổng Thiết Bị</div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6 mb-3">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #28a745, #20c997);">
                            <i class="fas fa-check-circle"></i>
                        </div>
                        <div class="stat-number">${workingEquipment}</div>
                        <div class="stat-label">Hoạt Động Tốt</div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6 mb-3">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #ffc107, #fd7e14);">
                            <i class="fas fa-tools"></i>
                        </div>
                        <div class="stat-number">${maintenanceEquipment}</div>
                        <div class="stat-label">Đang Bảo Trì</div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6 mb-3">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #dc3545, #e74c3c);">
                            <i class="fas fa-exclamation-triangle"></i>
                        </div>
                        <div class="stat-number">${brokenEquipment}</div>
                        <div class="stat-label">Cần Sửa Chữa</div>
                    </div>
                </div>
            </div>
            
            <!-- Filter and Search -->
            <div class="card border-radius-custom shadow-custom mb-4">
                <div class="card-body">
                    <form class="row g-3" id="filterForm">
                        <div class="col-md-3">
                            <div class="search-box">
                                <i class="fas fa-search search-icon"></i>
                                <input type="text" class="form-control" placeholder="Tìm kiếm thiết bị..." 
                                       id="searchInput">
                            </div>
                        </div>
                        <div class="col-md-2">
                            <select class="form-control" id="categoryFilter">
                                <option value="">Tất cả danh mục</option>
                                <option value="cardio">Cardio</option>
                                <option value="strength">Strength</option>
                                <option value="functional">Functional</option>
                                <option value="accessories">Phụ kiện</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <select class="form-control" id="statusFilter">
                                <option value="">Tất cả trạng thái</option>
                                <option value="working">Hoạt động</option>
                                <option value="maintenance">Bảo trì</option>
                                <option value="broken">Hỏng</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <select class="form-control" id="locationFilter">
                                <option value="">Tất cả khu vực</option>
                                <option value="cardio-area">Khu Cardio</option>
                                <option value="weight-area">Khu Tạ</option>
                                <option value="functional-area">Khu Functional</option>
                                <option value="group-class">Phòng Group Class</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <div class="btn-group w-100">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search me-1"></i>Lọc
                                </button>
                                <button type="button" class="btn btn-outline-secondary" onclick="exportInventory()">
                                    <i class="fas fa-download me-1"></i>Xuất
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Equipment Table -->
            <div class="card border-radius-custom shadow-custom">
                <div class="card-header bg-gradient-purple text-white">
                    <h5 class="card-title mb-0">
                        <i class="fas fa-list me-2"></i>Danh Sách Thiết Bị
                    </h5>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0" id="equipmentTable">
                            <thead>
                                <tr>
                                    <th>Mã TB</th>
                                    <th>Hình Ảnh</th>
                                    <th>Tên Thiết Bị</th>
                                    <th>Danh Mục</th>
                                    <th>Khu Vực</th>
                                    <th>Trạng Thái</th>
                                    <th>Ngày Mua</th>
                                    <th>Bảo Trì Cuối</th>
                                    <th>Giá Trị</th>
                                    <th>Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%-- Mock equipment data using JSTL forEach --%>
                                <c:forEach var="i" begin="1" end="15">
                                    <tr>
                                        <td><strong>EQ${String.format("%03d", i)}</strong></td>
                                        <td>
                                            <img src="https://via.placeholder.com/50x50/3B1E78/FFD700?text=${i}" 
                                                 class="rounded" alt="Equipment ${i}">
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${i % 5 == 1}">Máy Chạy Bộ TechnoGym</c:when>
                                                <c:when test="${i % 5 == 2}">Xe Đạp Tập Life Fitness</c:when>
                                                <c:when test="${i % 5 == 3}">Máy Tạ Đa Năng Hammer</c:when>
                                                <c:when test="${i % 5 == 4}">Ghế Tập Cơ Bụng</c:when>
                                                <c:otherwise>Bộ Tạ Đơn (5-50kg)</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:set var="category" value="${(i % 4 == 1) ? 'Cardio' : ((i % 4 == 2) ? 'Strength' : ((i % 4 == 3) ? 'Functional' : 'Phụ kiện'))}" />
                                            <span class="badge ${(category == 'Cardio') ? 'bg-info' : ((category == 'Strength') ? 'bg-danger' : ((category == 'Functional') ? 'bg-success' : 'bg-secondary'))}">
                                                ${category}
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${i % 4 == 1}">Khu Cardio</c:when>
                                                <c:when test="${i % 4 == 2}">Khu Tạ</c:when>
                                                <c:when test="${i % 4 == 3}">Khu Functional</c:when>
                                                <c:otherwise>Phòng Group Class</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:set var="status" value="${(i == 3 || i == 8 || i == 13) ? 'maintenance' : ((i == 7 || i == 15) ? 'broken' : 'working')}" />
                                            <span class="status-badge ${(status == 'working') ? 'status-active' : ((status == 'maintenance') ? 'status-pending' : 'status-inactive')}">
                                                <c:choose>
                                                    <c:when test="${status == 'working'}">Hoạt động</c:when>
                                                    <c:when test="${status == 'maintenance'}">Bảo trì</c:when>
                                                    <c:otherwise>Hỏng</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </td>
                                        <td>
                                            <c:set var="purchaseDate" value="${2020 + (i % 4)}-${String.format('%02d', (i % 12) + 1)}-${String.format('%02d', (i % 28) + 1)}" />
                                            ${purchaseDate}
                                        </td>
                                        <td>
                                            <c:set var="maintenanceDate" value="${2024}-${String.format('%02d', ((i % 6) + 7) % 12 + 1)}-${String.format('%02d', (i % 28) + 1)}" />
                                            ${maintenanceDate}
                                        </td>
                                        <td>
                                            <c:set var="value" value="${(i % 5 + 1) * 25000000}" />
                                            <fmt:formatNumber value="${value}" type="currency" 
                                                            currencySymbol="₫" pattern="#,##0₫" />
                                        </td>
                                        <td>
                                            <div class="action-buttons">
                                                <button class="btn btn-view" title="Xem chi tiết" 
                                                        onclick="viewEquipment(${i})">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <button class="btn btn-edit" title="Chỉnh sửa" 
                                                        onclick="editEquipment(${i})">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button class="btn" style="background-color: var(--warning); color: white;" 
                                                        title="Bảo trì" onclick="maintainEquipment(${i})">
                                                    <i class="fas fa-tools"></i>
                                                </button>
                                                <button class="btn btn-delete" title="Xóa" 
                                                        onclick="deleteEquipment(${i})">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                
                <!-- Pagination -->
                <div class="card-footer bg-white">
                    <div class="row align-items-center">
                        <div class="col">
                            <small class="text-muted">Hiển thị 1-15 trong tổng số ${totalEquipment} thiết bị</small>
                        </div>
                        <div class="col-auto">
                            <nav>
                                <ul class="pagination pagination-sm mb-0">
                                    <li class="page-item disabled">
                                        <a class="page-link" href="#">Trước</a>
                                    </li>
                                    <li class="page-item active">
                                        <a class="page-link" href="#">1</a>
                                    </li>
                                    <li class="page-item">
                                        <a class="page-link" href="#">2</a>
                                    </li>
                                    <li class="page-item">
                                        <a class="page-link" href="#">3</a>
                                    </li>
                                    <li class="page-item">
                                        <a class="page-link" href="#">Sau</a>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Add Equipment Modal -->
    <div class="modal fade" id="addEquipmentModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content border-radius-custom">
                <div class="modal-header bg-gradient-purple text-white">
                    <h5 class="modal-title">
                        <i class="fas fa-plus me-2"></i>Thêm Thiết Bị Mới
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="addEquipmentForm">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Tên Thiết Bị *</label>
                                <input type="text" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Danh Mục *</label>
                                <select class="form-control" required>
                                    <option value="">Chọn danh mục</option>
                                    <option value="cardio">Cardio</option>
                                    <option value="strength">Strength</option>
                                    <option value="functional">Functional</option>
                                    <option value="accessories">Phụ kiện</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Hãng Sản Xuất</label>
                                <input type="text" class="form-control">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Model</label>
                                <input type="text" class="form-control">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Khu Vực *</label>
                                <select class="form-control" required>
                                    <option value="">Chọn khu vực</option>
                                    <option value="cardio-area">Khu Cardio</option>
                                    <option value="weight-area">Khu Tạ</option>
                                    <option value="functional-area">Khu Functional</option>
                                    <option value="group-class">Phòng Group Class</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Ngày Mua *</label>
                                <input type="date" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Giá Mua (VNĐ) *</label>
                                <input type="number" class="form-control" min="0" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Thời Gian Bảo Hành (tháng)</label>
                                <input type="number" class="form-control" min="0" max="60">
                            </div>
                            <div class="col-12 mb-3">
                                <label class="form-label">Mô Tả</label>
                                <textarea class="form-control" rows="3"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="button" class="btn btn-primary" onclick="saveEquipment()">
                        <i class="fas fa-save me-1"></i>Lưu Thiết Bị
                    </button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Sidebar toggle for mobile
            const sidebarToggle = document.getElementById('sidebarToggle');
            const sidebar = document.getElementById('sidebar');
            
            if (sidebarToggle) {
                sidebarToggle.addEventListener('click', function() {
                    sidebar.classList.toggle('show');
                });
            }
            
            // Filter functionality
            const filterForm = document.getElementById('filterForm');
            const searchInput = document.getElementById('searchInput');
            const categoryFilter = document.getElementById('categoryFilter');
            const statusFilter = document.getElementById('statusFilter');
            const locationFilter = document.getElementById('locationFilter');
            
            filterForm.addEventListener('submit', function(e) {
                e.preventDefault();
                filterEquipment();
            });
            
            searchInput.addEventListener('input', filterEquipment);
            categoryFilter.addEventListener('change', filterEquipment);
            statusFilter.addEventListener('change', filterEquipment);
            locationFilter.addEventListener('change', filterEquipment);
        });
        
        function filterEquipment() {
            const searchTerm = document.getElementById('searchInput').value.toLowerCase();
            const categoryFilter = document.getElementById('categoryFilter').value;
            const statusFilter = document.getElementById('statusFilter').value;
            const locationFilter = document.getElementById('locationFilter').value;
            const tableRows = document.querySelectorAll('#equipmentTable tbody tr');
            
            tableRows.forEach(row => {
                const name = row.cells[2].textContent.toLowerCase();
                const category = row.cells[3].textContent.toLowerCase();
                const location = row.cells[4].textContent.toLowerCase();
                const status = row.cells[5].textContent.toLowerCase();
                
                const matchesSearch = name.includes(searchTerm);
                const matchesCategory = !categoryFilter || category.includes(categoryFilter);
                const matchesStatus = !statusFilter || status.includes(statusFilter);
                const matchesLocation = !locationFilter || location.includes(locationFilter.replace('-', ' '));
                
                if (matchesSearch && matchesCategory && matchesStatus && matchesLocation) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }
        
        function viewEquipment(id) {
            alert('Xem thông tin chi tiết thiết bị ID: EQ' + String(id).padStart(3, '0'));
            // In real app, this would open a detail modal or navigate to detail page
        }
        
        function editEquipment(id) {
            alert('Chỉnh sửa thông tin thiết bị ID: EQ' + String(id).padStart(3, '0'));
            // In real app, this would open an edit modal with pre-filled data
        }
        
        function maintainEquipment(id) {
            if (confirm('Đặt thiết bị này vào trạng thái bảo trì?')) {
                alert('Thiết bị EQ' + String(id).padStart(3, '0') + ' đã được đặt vào lịch bảo trì');
                // In real app, this would update equipment status via AJAX
            }
        }
        
        function deleteEquipment(id) {
            if (confirm('Bạn có chắc chắn muốn xóa thiết bị này không?')) {
                alert('Đã xóa thiết bị ID: EQ' + String(id).padStart(3, '0'));
                // In real app, this would make an AJAX call to delete the equipment
            }
        }
        
        function saveEquipment() {
            const form = document.getElementById('addEquipmentForm');
            if (form.checkValidity()) {
                alert('Thiết bị mới đã được thêm thành công!');
                const modal = bootstrap.Modal.getInstance(document.getElementById('addEquipmentModal'));
                modal.hide();
                form.reset();
                // In real app, this would submit form data via AJAX
            } else {
                form.reportValidity();
            }
        }
        
        function exportInventory() {
            alert('Chức năng xuất báo cáo tồn kho sẽ được triển khai.');
            // In real app, this would generate and download an inventory report
        }
        
        function scheduleMaintenanceAll() {
            alert('Mở trang lập lịch bảo trì định kỳ cho tất cả thiết bị.');
            // In real app, this would open a maintenance scheduling interface
        }
    </script>
</body>
</html>
