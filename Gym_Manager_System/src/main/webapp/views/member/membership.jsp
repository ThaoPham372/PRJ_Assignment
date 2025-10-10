<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/views/common/header.jsp" %>

    <style>
        :root {
        --primary: #141a46;
        --primary-light: #1e2a5c;
        --accent: #ec8b5e;
        --accent-hover: #d67a4f;
        --text: #2c3e50;
        --text-light: #5a6c7d;
        --muted: #f8f9fa;
        --card: #ffffff;
        --shadow: rgba(0, 0, 0, 0.1);
        --shadow-hover: rgba(0, 0, 0, 0.15);
        --gradient-primary: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%);
        --gradient-accent: linear-gradient(135deg, #ec8b5e 0%, #d67a4f 100%);
        }

        .membership-card {
        background: var(--card);
            border-radius: 15px;
        box-shadow: 0 8px 25px var(--shadow);
            padding: 30px;
            margin-bottom: 30px;
            transition: all 0.3s ease;
        }

        .membership-card:hover {
            transform: translateY(-5px);
        box-shadow: 0 12px 35px var(--shadow-hover);
        }

    .member-nav {
        background: var(--gradient-primary);
        padding: 0;
            margin-bottom: 30px;
        border-radius: 20px;
        box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
        overflow: hidden;
        position: relative;
    }

    .member-nav::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 4px;
        background: var(--gradient-accent);
    }

    .member-nav .nav-container {
        padding: 20px 30px;
    }

    .member-nav .nav-link {
        color: rgba(255, 255, 255, 0.9);
        font-weight: 600;
        padding: 12px 20px;
        border-radius: 12px;
        transition: background-color 0.2s ease, color 0.2s ease;
        margin: 0 6px;
        position: relative;
        border: 1px solid rgba(255, 255, 255, 0.1);
        text-decoration: none;
        min-width: 140px;
        text-align: center;
    }

    .member-nav .nav-link:hover {
        background: rgba(255, 255, 255, 0.1);
        color: white;
    }

    .member-nav .nav-link.active {
        background: var(--accent);
        color: white;
    }

    .member-nav .nav-link i {
        margin-right: 8px;
        font-size: 1em;
        opacity: 0.9;
    }

    .member-nav .nav-link.active i {
        opacity: 1;
    }

    .member-nav .nav-container {
            display: flex;
        justify-content: center;
            align-items: center;
        flex-wrap: wrap;
        gap: 10px;
    }

    /* Responsive Navigation */
    @media (max-width: 768px) {
        .member-nav .nav-container {
            padding: 15px 20px;
        }
        
        .member-nav .nav-link {
            padding: 12px 20px;
            margin: 0 4px;
            font-size: 0.9rem;
        }
        
        .member-nav .nav-link i {
            margin-right: 8px;
            font-size: 1em;
        }
    }

    @media (max-width: 576px) {
        .member-nav .nav-container {
            padding: 10px 15px;
        }
        
        .member-nav .nav-link {
            padding: 10px 15px;
            margin: 0 2px;
            font-size: 0.85rem;
        }
        
        .member-nav .nav-link i {
            margin-right: 6px;
            font-size: 0.9em;
        }
    }

    .membership-title {
        color: var(--text);
        font-weight: 800;
            margin-bottom: 30px;
        text-transform: uppercase;
        letter-spacing: 1px;
        }

        .package-card {
        background: var(--card);
            border-radius: 15px;
        box-shadow: 0 8px 25px var(--shadow);
        padding: 30px;
            margin-bottom: 20px;
            border: 2px solid transparent;
            transition: all 0.3s ease;
        }

        .package-card:hover {
        border-color: var(--accent);
            transform: translateY(-5px);
        }

    .package-card.featured {
        border-color: var(--accent);
        background: linear-gradient(135deg, var(--card) 0%, rgba(236, 139, 94, 0.05) 100%);
    }

    .btn-membership {
        background: var(--gradient-accent);
            color: white;
        border: none;
        border-radius: 25px;
        padding: 12px 25px;
        font-weight: 600;
        transition: all 0.3s ease;
        text-decoration: none;
            display: inline-block;
        }

    .btn-membership:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
        color: white;
    }

    .price-display {
        font-size: 2.5rem;
        font-weight: 900;
        color: var(--accent);
        }
    </style>

<div class="container mt-5">
    <!-- Current Membership -->
    <div class="membership-card">
        <h2 class="membership-title">Gói Thành Viên Hiện Tại</h2>
            <div class="row align-items-center">
                <div class="col-md-8">
                <h4 class="mb-2">Premium Membership</h4>
                <p class="text-muted mb-2">Truy cập không giới hạn tất cả các dịch vụ</p>
                <p class="mb-0">
                    <i class="fas fa-calendar me-2"></i>
                    Hết hạn: 15/03/2024
                </p>
            </div>
            <div class="col-md-4 text-end">
                <div class="price-display">2,500,000đ</div>
                <small class="text-muted">/ tháng</small>
            </div>
            </div>
        </div>

    <!-- Available Packages -->
    <div class="membership-card">
        <h4 class="membership-title">Gói Thành Viên Khác</h4>
        <div class="row">
            <!-- Basic Package -->
            <div class="col-md-4 mb-3">
                <div class="package-card">
                    <h5 class="mb-3">Basic</h5>
                    <div class="price-display mb-3">1,200,000đ</div>
                    <small class="text-muted">/ tháng</small>
                    <ul class="list-unstyled mt-3">
                        <li><i class="fas fa-check text-success me-2"></i>Truy cập phòng tập</li>
                        <li><i class="fas fa-check text-success me-2"></i>Lớp học nhóm</li>
                        <li><i class="fas fa-times text-danger me-2"></i>Personal Training</li>
                        <li><i class="fas fa-times text-danger me-2"></i>Phòng xông hơi</li>
                            </ul>
                    <button class="btn-membership w-100">Chọn Gói</button>
                </div>
            </div>
            
            <!-- Premium Package -->
            <div class="col-md-4 mb-3">
                <div class="package-card featured">
                    <div class="text-center mb-2">
                        <span class="badge bg-warning">Phổ Biến</span>
                    </div>
                    <h5 class="mb-3">Premium</h5>
                    <div class="price-display mb-3">2,500,000đ</div>
                    <small class="text-muted">/ tháng</small>
                    <ul class="list-unstyled mt-3">
                        <li><i class="fas fa-check text-success me-2"></i>Truy cập phòng tập</li>
                        <li><i class="fas fa-check text-success me-2"></i>Lớp học nhóm</li>
                        <li><i class="fas fa-check text-success me-2"></i>Personal Training</li>
                        <li><i class="fas fa-check text-success me-2"></i>Phòng xông hơi</li>
                    </ul>
                    <button class="btn-membership w-100">Chọn Gói</button>
            </div>
        </div>

            <!-- VIP Package -->
            <div class="col-md-4 mb-3">
                    <div class="package-card">
                    <h5 class="mb-3">VIP</h5>
                    <div class="price-display mb-3">4,000,000đ</div>
                    <small class="text-muted">/ tháng</small>
                    <ul class="list-unstyled mt-3">
                        <li><i class="fas fa-check text-success me-2"></i>Truy cập phòng tập</li>
                        <li><i class="fas fa-check text-success me-2"></i>Lớp học nhóm</li>
                        <li><i class="fas fa-check text-success me-2"></i>Personal Training</li>
                        <li><i class="fas fa-check text-success me-2"></i>Phòng xông hơi</li>
                        <li><i class="fas fa-check text-success me-2"></i>Dịch vụ VIP</li>
                        </ul>
                    <button class="btn-membership w-100">Chọn Gói</button>
                </div>
                </div>
            </div>
        </div>

    <!-- Payment History -->
                <div class="membership-card">
        <h4 class="membership-title">Lịch Sử Thanh Toán</h4>
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                        <th>Ngày</th>
                        <th>Gói</th>
                        <th>Số Tiền</th>
                                    <th>Trạng Thái</th>
                                    <th>Hành Động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                        <td>15/01/2024</td>
                        <td>Premium</td>
                        <td>2,500,000đ</td>
                        <td><span class="badge bg-success">Đã Thanh Toán</span></td>
                        <td><button class="btn btn-sm btn-outline-primary">Xem Hóa Đơn</button></td>
                                </tr>
                                <tr>
                        <td>15/12/2023</td>
                        <td>Premium</td>
                        <td>2,500,000đ</td>
                        <td><span class="badge bg-success">Đã Thanh Toán</span></td>
                        <td><button class="btn btn-sm btn-outline-primary">Xem Hóa Đơn</button></td>
                                </tr>
                                <tr>
                        <td>15/11/2023</td>
                        <td>Premium</td>
                        <td>2,500,000đ</td>
                        <td><span class="badge bg-success">Đã Thanh Toán</span></td>
                        <td><button class="btn btn-sm btn-outline-primary">Xem Hóa Đơn</button></td>
                                </tr>
                            </tbody>
                        </table>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="row">
        <div class="col-md-6">
            <div class="membership-card text-center">
                <h5 class="membership-title">Gia Hạn</h5>
                <p class="text-muted mb-4">Gia hạn gói hiện tại</p>
                <button class="btn-membership">
                    <i class="fas fa-credit-card me-2"></i>Gia Hạn Ngay
                </button>
            </div>
        </div>
        <div class="col-md-6">
            <div class="membership-card text-center">
                <h5 class="membership-title">Hỗ Trợ</h5>
                <p class="text-muted mb-4">Cần hỗ trợ về gói thành viên?</p>
                <button class="btn-membership">
                    <i class="fas fa-headset me-2"></i>Liên Hệ Hỗ Trợ
                </button>
                </div>
            </div>
        </div>
    </div>

<%@ include file="/views/common/footer.jsp" %>