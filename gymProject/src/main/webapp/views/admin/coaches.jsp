<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Huấn Luyện Viên - Stamina Gym</title>
    
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
                <a class="nav-link active" href="${pageContext.request.contextPath}/views/admin/coaches.jsp">
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
                    <i class="fas fa-user-tie me-2"></i>Quản Lý Huấn Luyện Viên
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
                        <h1 class="page-title">Quản Lý Huấn Luyện Viên</h1>
                        <p class="page-subtitle">Quản lý thông tin và lịch làm việc của đội ngũ HLV</p>
                    </div>
                    <div class="col-auto">
                        <button class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#addCoachModal">
                            <i class="fas fa-user-plus me-2"></i>Thêm Huấn Luyện Viên
                        </button>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Coach Cards Grid -->
        <div class="container-fluid">
            <!-- Statistics Cards -->
            <div class="row mb-4">
                <c:set var="totalCoaches" value="24" />
                <c:set var="activeCoaches" value="22" />
                <c:set var="ptSessions" value="145" />
                <c:set var="avgRating" value="4.8" />
                
                <div class="col-xl-3 col-md-6 mb-3">
                    <div class="stat-card">
                        <div class="stat-icon bg-gradient-purple">
                            <i class="fas fa-user-tie"></i>
                        </div>
                        <div class="stat-number">${totalCoaches}</div>
                        <div class="stat-label">Tổng HLV</div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6 mb-3">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #28a745, #20c997);">
                            <i class="fas fa-user-check"></i>
                        </div>
                        <div class="stat-number">${activeCoaches}</div>
                        <div class="stat-label">Đang Hoạt Động</div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6 mb-3">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #17a2b8, #6f42c1);">
                            <i class="fas fa-calendar-check"></i>
                        </div>
                        <div class="stat-number">${ptSessions}</div>
                        <div class="stat-label">Buổi PT Tháng Này</div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6 mb-3">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #ffc107, #fd7e14);">
                            <i class="fas fa-star"></i>
                        </div>
                        <div class="stat-number">${avgRating}/5</div>
                        <div class="stat-label">Đánh Giá TB</div>
                    </div>
                </div>
            </div>
            
            <!-- Coaches Grid -->
            <div class="row">
                <%-- Mock coach data using JSTL forEach --%>
                <c:forEach var="i" begin="1" end="8">
                    <div class="col-xl-3 col-lg-4 col-md-6 mb-4">
                        <div class="card coach-card border-radius-custom shadow-custom">
                            <div class="card-body text-center">
                                <div class="position-relative mb-3">
                                    <img src="https://via.placeholder.com/120x120/3B1E78/FFD700?text=PT${i}" 
                                         class="coach-image" alt="Coach ${i}">
                                    <span class="position-absolute top-0 end-0 badge ${(i % 4 != 0) ? 'bg-success' : 'bg-warning'}">
                                        ${(i % 4 != 0) ? 'Online' : 'Busy'}
                                    </span>
                                </div>
                                
                                <h5 class="card-title mb-1">
                                    <c:choose>
                                        <c:when test="${i == 1}">Nguyễn Văn Nam</c:when>
                                        <c:when test="${i == 2}">Trần Thị Lan</c:when>
                                        <c:when test="${i == 3}">Lê Hoàng Minh</c:when>
                                        <c:when test="${i == 4}">Phạm Thu Hà</c:when>
                                        <c:when test="${i == 5}">Vũ Đình Khoa</c:when>
                                        <c:when test="${i == 6}">Hoàng Thị Mai</c:when>
                                        <c:when test="${i == 7}">Đặng Văn Tùng</c:when>
                                        <c:otherwise>Bùi Thị Nga</c:otherwise>
                                    </c:choose>
                                </h5>
                                
                                <p class="text-muted small mb-2">
                                    <c:choose>
                                        <c:when test="${i == 1}">Chuyên gia Bodybuilding</c:when>
                                        <c:when test="${i == 2}">Yoga & Pilates Instructor</c:when>
                                        <c:when test="${i == 3}">Strength & Conditioning</c:when>
                                        <c:when test="${i == 4}">Nutritionist & Fitness</c:when>
                                        <c:when test="${i == 5}">Cardio & Weight Loss</c:when>
                                        <c:when test="${i == 6}">Functional Training</c:when>
                                        <c:when test="${i == 7}">CrossFit & HIIT</c:when>
                                        <c:otherwise>Rehabilitation & Recovery</c:otherwise>
                                    </c:choose>
                                </p>
                                
                                <div class="d-flex justify-content-center mb-3">
                                    <div class="star-rating text-yellow">
                                        <c:forEach var="star" begin="1" end="5">
                                            <i class="fas fa-star ${(star <= (4 + (i % 2))) ? '' : 'text-muted'}"></i>
                                        </c:forEach>
                                    </div>
                                    <small class="ms-2 text-muted">(${4 + (i % 2)}.${(i % 10)}/5)</small>
                                </div>
                                
                                <div class="row text-center mb-3">
                                    <div class="col-4">
                                        <h6 class="mb-0 text-primary">${15 + (i * 3)}</h6>
                                        <small class="text-muted">Khách hàng</small>
                                    </div>
                                    <div class="col-4">
                                        <h6 class="mb-0 text-success">${2 + (i % 5)}</h6>
                                        <small class="text-muted">Năm KN</small>
                                    </div>
                                    <div class="col-4">
                                        <h6 class="mb-0 text-warning">${8 + (i % 15)}</h6>
                                        <small class="text-muted">Buổi/tuần</small>
                                    </div>
                                </div>
                                
                                <div class="btn-group w-100" role="group">
                                    <button class="btn btn-outline-primary btn-sm" onclick="viewCoach(${i})">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                    <button class="btn btn-outline-info btn-sm" onclick="editCoach(${i})">
                                        <i class="fas fa-edit"></i>
                                    </button>
                                    <button class="btn btn-outline-success btn-sm" onclick="scheduleCoach(${i})">
                                        <i class="fas fa-calendar"></i>
                                    </button>
                                    <button class="btn btn-outline-danger btn-sm" onclick="deleteCoach(${i})">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            
            <!-- Weekly Schedule Table -->
            <div class="card border-radius-custom shadow-custom mt-4">
                <div class="card-header bg-gradient-purple text-white">
                    <h5 class="card-title mb-0">
                        <i class="fas fa-calendar-week me-2"></i>Lịch Làm Việc Tuần Này
                    </h5>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th>Thời Gian</th>
                                    <th>Thứ 2</th>
                                    <th>Thứ 3</th>
                                    <th>Thứ 4</th>
                                    <th>Thứ 5</th>
                                    <th>Thứ 6</th>
                                    <th>Thứ 7</th>
                                    <th>Chủ Nhật</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%-- Mock schedule data using JSTL --%>
                                <c:forEach var="hour" begin="6" end="21" step="2">
                                    <tr>
                                        <td class="fw-bold">${hour}:00 - ${hour + 2}:00</td>
                                        <c:forEach var="day" begin="1" end="7">
                                            <td>
                                                <c:if test="${(hour + day) % 3 == 0}">
                                                    <div class="small p-1 bg-primary text-white rounded mb-1">
                                                        PT Nam - Nguyễn A
                                                    </div>
                                                </c:if>
                                                <c:if test="${(hour + day) % 4 == 0}">
                                                    <div class="small p-1 bg-success text-white rounded mb-1">
                                                        Yoga Lan - Group
                                                    </div>
                                                </c:if>
                                                <c:if test="${(hour + day) % 5 == 0}">
                                                    <div class="small p-1 bg-warning text-dark rounded">
                                                        HIIT Tùng - Class
                                                    </div>
                                                </c:if>
                                            </td>
                                        </c:forEach>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Add Coach Modal -->
    <div class="modal fade" id="addCoachModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content border-radius-custom">
                <div class="modal-header bg-gradient-purple text-white">
                    <h5 class="modal-title">
                        <i class="fas fa-user-plus me-2"></i>Thêm Huấn Luyện Viên
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="addCoachForm">
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
                                <label class="form-label">Chuyên Môn *</label>
                                <select class="form-control" required>
                                    <option value="">Chọn chuyên môn</option>
                                    <option value="bodybuilding">Bodybuilding</option>
                                    <option value="yoga">Yoga & Pilates</option>
                                    <option value="strength">Strength & Conditioning</option>
                                    <option value="nutrition">Nutrition & Fitness</option>
                                    <option value="cardio">Cardio & Weight Loss</option>
                                    <option value="functional">Functional Training</option>
                                    <option value="crossfit">CrossFit & HIIT</option>
                                    <option value="rehabilitation">Rehabilitation</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Kinh Nghiệm (năm) *</label>
                                <input type="number" class="form-control" min="0" max="50" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Mức Lương (VNĐ/tháng) *</label>
                                <input type="number" class="form-control" min="0" required>
                            </div>
                            <div class="col-12 mb-3">
                                <label class="form-label">Chứng Chỉ</label>
                                <textarea class="form-control" rows="2" placeholder="Danh sách các chứng chỉ..."></textarea>
                            </div>
                            <div class="col-12 mb-3">
                                <label class="form-label">Mô Tả</label>
                                <textarea class="form-control" rows="3" placeholder="Mô tả về huấn luyện viên..."></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="button" class="btn btn-primary" onclick="saveCoach()">
                        <i class="fas fa-save me-1"></i>Lưu HLV
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
        });
        
        function viewCoach(id) {
            alert('Xem thông tin chi tiết HLV ID: ' + id);
            // In real app, this would open a detail modal or navigate to detail page
        }
        
        function editCoach(id) {
            alert('Chỉnh sửa thông tin HLV ID: ' + id);
            // In real app, this would open an edit modal with pre-filled data
        }
        
        function scheduleCoach(id) {
            alert('Xem/chỉnh sửa lịch làm việc HLV ID: ' + id);
            // In real app, this would open a schedule management modal
        }
        
        function deleteCoach(id) {
            if (confirm('Bạn có chắc chắn muốn xóa huấn luyện viên này không?')) {
                alert('Đã xóa HLV ID: ' + id);
                // In real app, this would make an AJAX call to delete the coach
            }
        }
        
        function saveCoach() {
            const form = document.getElementById('addCoachForm');
            if (form.checkValidity()) {
                alert('Huấn luyện viên mới đã được thêm thành công!');
                const modal = bootstrap.Modal.getInstance(document.getElementById('addCoachModal'));
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
