<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Thành Viên - Stamina Gym</title>
    
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
                <a class="nav-link active" href="${pageContext.request.contextPath}/views/admin/members.jsp">
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
                    <i class="fas fa-users me-2"></i>Quản Lý Thành Viên
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
                        <h1 class="page-title">Quản Lý Thành Viên</h1>
                        <p class="page-subtitle">Quản lý thông tin và theo dõi hoạt động của các thành viên</p>
                    </div>
                    <div class="col-auto">
                        <button class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#addMemberModal">
                            <i class="fas fa-user-plus me-2"></i>Thêm Thành Viên
                        </button>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Members Content -->
        <div class="container-fluid">
            <!-- Statistics Cards -->
            <div class="row mb-4">
                <c:set var="totalMembers" value="1247" />
                <c:set var="activeMembers" value="892" />
                <c:set var="newThisMonth" value="45" />
                <c:set var="expiredMembers" value="355" />
                
                <div class="col-xl-3 col-md-6 mb-3">
                    <div class="stat-card">
                        <div class="stat-icon bg-gradient-purple">
                            <i class="fas fa-users"></i>
                        </div>
                        <div class="stat-number">${totalMembers}</div>
                        <div class="stat-label">Tổng Thành Viên</div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6 mb-3">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #28a745, #20c997);">
                            <i class="fas fa-user-check"></i>
                        </div>
                        <div class="stat-number">${activeMembers}</div>
                        <div class="stat-label">Đang Hoạt Động</div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6 mb-3">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #17a2b8, #6f42c1);">
                            <i class="fas fa-user-plus"></i>
                        </div>
                        <div class="stat-number">${newThisMonth}</div>
                        <div class="stat-label">Mới Tháng Này</div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6 mb-3">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #dc3545, #e74c3c);">
                            <i class="fas fa-user-times"></i>
                        </div>
                        <div class="stat-number">${expiredMembers}</div>
                        <div class="stat-label">Hết Hạn</div>
                    </div>
                </div>
            </div>
            
            <!-- Search and Filter -->
            <div class="card border-radius-custom shadow-custom mb-4">
                <div class="card-body">
                    <form class="row g-3" id="searchForm">
                        <div class="col-md-4">
                            <div class="search-box">
                                <i class="fas fa-search search-icon"></i>
                                <input type="text" class="form-control" placeholder="Tìm kiếm thành viên..." 
                                       id="searchInput">
                            </div>
                        </div>
                        <div class="col-md-2">
                            <select class="form-control" id="statusFilter">
                                <option value="">Tất cả trạng thái</option>
                                <option value="active">Hoạt động</option>
                                <option value="expired">Hết hạn</option>
                                <option value="suspended">Tạm ngưng</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <select class="form-control" id="packageFilter">
                                <option value="">Tất cả gói</option>
                                <option value="basic">Basic</option>
                                <option value="premium">Premium</option>
                                <option value="vip">VIP</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-primary w-100">
                                <i class="fas fa-search me-1"></i>Tìm kiếm
                            </button>
                        </div>
                        <div class="col-md-2">
                            <button type="button" class="btn btn-outline-secondary w-100" id="exportBtn">
                                <i class="fas fa-download me-1"></i>Xuất Excel
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Members Table -->
            <div class="card border-radius-custom shadow-custom">
                <div class="card-header bg-gradient-purple text-white">
                    <h5 class="card-title mb-0">
                        <i class="fas fa-list me-2"></i>Danh Sách Thành Viên
                    </h5>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0" id="membersTable">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Ảnh</th>
                                    <th>Họ Tên</th>
                                    <th>Email</th>
                                    <th>Điện Thoại</th>
                                    <th>Gói</th>
                                    <th>Ngày Đăng Ký</th>
                                    <th>Ngày Hết Hạn</th>
                                    <th>Trạng Thái</th>
                                    <th>Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%-- Mock member data using JSTL forEach --%>
                                <c:set var="memberNames" value="Nguyễn Văn An,Trần Thị Bình,Lê Hoàng Châu,Phạm Thu Dung,Vũ Minh Đức,Hoàng Thị Em,Đặng Văn Phúc,Bùi Thị Giang,Cao Văn Hòa,Lý Thị Ích" />
                                <c:set var="memberEmails" value="an.nguyen@email.com,binh.tran@email.com,chau.le@email.com,dung.pham@email.com,duc.vu@email.com,em.hoang@email.com,phuc.dang@email.com,giang.bui@email.com,hoa.cao@email.com,ich.ly@email.com" />
                                <c:set var="memberPhones" value="0901234567,0912345678,0923456789,0934567890,0945678901,0956789012,0967890123,0978901234,0989012345,0990123456" />
                                <c:set var="memberPackages" value="Premium,Basic,VIP,Premium,Basic,VIP,Premium,Basic,VIP,Premium" />
                                <c:set var="memberStatuses" value="active,active,expired,active,suspended,active,active,expired,active,active" />
                                
                                <c:forEach var="i" begin="1" end="10">
                                    <tr>
                                        <td><strong>GM${String.format("%04d", i)}</strong></td>
                                        <td>
                                            <img src="https://via.placeholder.com/40x40/3B1E78/FFD700?text=${i}" 
                                                 class="rounded-circle" alt="Member ${i}">
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${i == 1}">Nguyễn Văn An</c:when>
                                                <c:when test="${i == 2}">Trần Thị Bình</c:when>
                                                <c:when test="${i == 3}">Lê Hoàng Châu</c:when>
                                                <c:when test="${i == 4}">Phạm Thu Dung</c:when>
                                                <c:when test="${i == 5}">Vũ Minh Đức</c:when>
                                                <c:when test="${i == 6}">Hoàng Thị Em</c:when>
                                                <c:when test="${i == 7}">Đặng Văn Phúc</c:when>
                                                <c:when test="${i == 8}">Bùi Thị Giang</c:when>
                                                <c:when test="${i == 9}">Cao Văn Hòa</c:when>
                                                <c:otherwise>Lý Thị Ích</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${i == 1}">an.nguyen@email.com</c:when>
                                                <c:when test="${i == 2}">binh.tran@email.com</c:when>
                                                <c:when test="${i == 3}">chau.le@email.com</c:when>
                                                <c:when test="${i == 4}">dung.pham@email.com</c:when>
                                                <c:when test="${i == 5}">duc.vu@email.com</c:when>
                                                <c:when test="${i == 6}">em.hoang@email.com</c:when>
                                                <c:when test="${i == 7}">phuc.dang@email.com</c:when>
                                                <c:when test="${i == 8}">giang.bui@email.com</c:when>
                                                <c:when test="${i == 9}">hoa.cao@email.com</c:when>
                                                <c:otherwise>ich.ly@email.com</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${i == 1}">0901234567</c:when>
                                                <c:when test="${i == 2}">0912345678</c:when>
                                                <c:when test="${i == 3}">0923456789</c:when>
                                                <c:when test="${i == 4}">0934567890</c:when>
                                                <c:when test="${i == 5}">0945678901</c:when>
                                                <c:when test="${i == 6}">0956789012</c:when>
                                                <c:when test="${i == 7}">0967890123</c:when>
                                                <c:when test="${i == 8}">0978901234</c:when>
                                                <c:when test="${i == 9}">0989012345</c:when>
                                                <c:otherwise>0990123456</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:set var="packageType" value="${(i % 3 == 1) ? 'Premium' : ((i % 3 == 2) ? 'Basic' : 'VIP')}" />
                                            <span class="badge ${(packageType == 'Premium') ? 'bg-success' : ((packageType == 'Basic') ? 'bg-primary' : 'bg-warning')}">
                                                ${packageType}
                                            </span>
                                        </td>
                                        <td>
                                            <c:set var="joinDate" value="${2024 - (i % 2)}-${String.format('%02d', (i % 12) + 1)}-${String.format('%02d', (i % 28) + 1)}" />
                                            ${joinDate}
                                        </td>
                                        <td>
                                            <c:set var="expiryDate" value="${2024 + ((i % 3 == 0) ? 0 : 1)}-${String.format('%02d', ((i % 12) + 3) % 12 + 1)}-${String.format('%02d', (i % 28) + 1)}" />
                                            ${expiryDate}
                                        </td>
                                        <td>
                                            <c:set var="status" value="${(i == 3 || i == 8) ? 'expired' : ((i == 5) ? 'suspended' : 'active')}" />
                                            <span class="status-badge ${(status == 'active') ? 'status-active' : ((status == 'expired') ? 'status-inactive' : 'status-pending')}">
                                                <c:choose>
                                                    <c:when test="${status == 'active'}">Hoạt động</c:when>
                                                    <c:when test="${status == 'expired'}">Hết hạn</c:when>
                                                    <c:otherwise>Tạm ngưng</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </td>
                                        <td>
                                            <div class="action-buttons">
                                                <button class="btn btn-view" title="Xem chi tiết" 
                                                        onclick="viewMember(${i})">
                                                    <i class="fas fa-eye"></i>
                                                </button>
                                                <button class="btn btn-edit" title="Chỉnh sửa" 
                                                        onclick="editMember(${i})">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button class="btn btn-delete" title="Xóa" 
                                                        onclick="deleteMember(${i})">
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
                            <small class="text-muted">Hiển thị 1-10 trong tổng số ${totalMembers} thành viên</small>
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
    
    <!-- Add Member Modal -->
    <div class="modal fade" id="addMemberModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content border-radius-custom">
                <div class="modal-header bg-gradient-purple text-white">
                    <h5 class="modal-title">
                        <i class="fas fa-user-plus me-2"></i>Thêm Thành Viên Mới
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="addMemberForm">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Họ và Tên *</label>
                                <input type="text" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Email *</label>
                                <input type="email" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Điện Thoại *</label>
                                <input type="tel" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Gói Membership *</label>
                                <select class="form-control" required>
                                    <option value="">Chọn gói</option>
                                    <option value="basic">Basic - 300K/tháng</option>
                                    <option value="premium">Premium - 500K/tháng</option>
                                    <option value="vip">VIP - 800K/tháng</option>
                                </select>
                            </div>
                            <div class="col-12 mb-3">
                                <label class="form-label">Địa Chỉ</label>
                                <textarea class="form-control" rows="3"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="button" class="btn btn-primary" onclick="saveMember()">
                        <i class="fas fa-save me-1"></i>Lưu Thành Viên
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
            
            // Search functionality
            const searchForm = document.getElementById('searchForm');
            const searchInput = document.getElementById('searchInput');
            const statusFilter = document.getElementById('statusFilter');
            const packageFilter = document.getElementById('packageFilter');
            
            searchForm.addEventListener('submit', function(e) {
                e.preventDefault();
                filterMembers();
            });
            
            searchInput.addEventListener('input', filterMembers);
            statusFilter.addEventListener('change', filterMembers);
            packageFilter.addEventListener('change', filterMembers);
            
            // Export functionality
            document.getElementById('exportBtn').addEventListener('click', function() {
                alert('Chức năng xuất Excel sẽ được triển khai trong phiên bản sau.');
            });
        });
        
        function filterMembers() {
            const searchTerm = document.getElementById('searchInput').value.toLowerCase();
            const statusFilter = document.getElementById('statusFilter').value;
            const packageFilter = document.getElementById('packageFilter').value;
            const tableRows = document.querySelectorAll('#membersTable tbody tr');
            
            tableRows.forEach(row => {
                const name = row.cells[2].textContent.toLowerCase();
                const email = row.cells[3].textContent.toLowerCase();
                const phone = row.cells[4].textContent.toLowerCase();
                const packageType = row.cells[5].textContent.toLowerCase();
                const status = row.cells[8].textContent.toLowerCase();
                
                const matchesSearch = name.includes(searchTerm) || 
                                    email.includes(searchTerm) || 
                                    phone.includes(searchTerm);
                const matchesStatus = !statusFilter || status.includes(statusFilter);
                const matchesPackage = !packageFilter || packageType.includes(packageFilter);
                
                if (matchesSearch && matchesStatus && matchesPackage) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }
        
        function viewMember(id) {
            alert('Xem thông tin chi tiết thành viên ID: GM' + String(id).padStart(4, '0'));
            // In real app, this would open a detail modal or navigate to detail page
        }
        
        function editMember(id) {
            alert('Chỉnh sửa thông tin thành viên ID: GM' + String(id).padStart(4, '0'));
            // In real app, this would open an edit modal with pre-filled data
        }
        
        function deleteMember(id) {
            if (confirm('Bạn có chắc chắn muốn xóa thành viên này không?')) {
                alert('Đã xóa thành viên ID: GM' + String(id).padStart(4, '0'));
                // In real app, this would make an AJAX call to delete the member
            }
        }
        
        function saveMember() {
            const form = document.getElementById('addMemberForm');
            if (form.checkValidity()) {
                alert('Thành viên mới đã được thêm thành công!');
                const modal = bootstrap.Modal.getInstance(document.getElementById('addMemberModal'));
                modal.hide();
                form.reset();
                // In real app, this would submit form data via AJAX
            } else {
                form.reportValidity();
            }
        }
    </script>
</body>
</html>
