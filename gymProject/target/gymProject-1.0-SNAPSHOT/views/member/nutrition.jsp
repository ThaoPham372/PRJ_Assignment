<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dinh Dưỡng - Stamina Gym</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #3B1E78;
            --secondary-color: #FFD700;
            --accent-color: #EC8B5E;
            --text-dark: #2C3E50;
            --bg-light: #F8F9FA;
        }

        body {
            background: var(--bg-light);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .navbar {
            background: var(--primary-color) !important;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .navbar-brand {
            color: var(--secondary-color) !important;
            font-weight: bold;
        }

        .nav-link {
            color: white !important;
            transition: color 0.3s ease;
        }

        .nav-link:hover {
            color: var(--secondary-color) !important;
        }

        .main-content {
            margin-top: 80px;
            padding: 20px 0;
        }

        .page-header {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }

        .nutrition-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 20px;
            transition: transform 0.3s ease;
        }

        .nutrition-card:hover {
            transform: translateY(-5px);
        }

        .macro-circle {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 15px;
            position: relative;
        }

        .macro-protein {
            background: conic-gradient(#28a745 0deg 144deg, #e9ecef 144deg 360deg);
        }

        .macro-carbs {
            background: conic-gradient(#ffc107 0deg 180deg, #e9ecef 180deg 360deg);
        }

        .macro-fat {
            background: conic-gradient(#dc3545 0deg 108deg, #e9ecef 108deg 360deg);
        }

        .macro-water {
            background: conic-gradient(#17a2b8 0deg 216deg, #e9ecef 216deg 360deg);
        }

        .macro-inner {
            width: 80px;
            height: 80px;
            background: white;
            border-radius: 50%;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
        }

        .macro-value {
            font-size: 1.2rem;
            font-weight: bold;
            color: var(--primary-color);
        }

        .macro-unit {
            font-size: 0.8rem;
            color: #666;
        }

        .progress-bar {
            border-radius: 10px;
        }

        .progress {
            height: 12px;
            border-radius: 10px;
            background: #e9ecef;
        }

        .progress-protein {
            background: #28a745;
        }

        .progress-carbs {
            background: #ffc107;
        }

        .progress-fat {
            background: #dc3545;
        }

        .progress-water {
            background: #17a2b8;
        }

        .meal-card {
            background: white;
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            margin-bottom: 15px;
            transition: transform 0.3s ease;
        }

        .meal-card:hover {
            transform: translateY(-3px);
        }

        .meal-icon {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
            color: white;
            font-size: 1.2rem;
        }

        .meal-breakfast {
            background: linear-gradient(135deg, #ffc107, #ff8c00);
        }

        .meal-lunch {
            background: linear-gradient(135deg, #28a745, #20c997);
        }

        .meal-dinner {
            background: linear-gradient(135deg, #6f42c1, #e83e8c);
        }

        .meal-snack {
            background: linear-gradient(135deg, #fd7e14, #dc3545);
        }

        .form-control {
            border: 2px solid #e9ecef;
            border-radius: 10px;
            padding: 12px 20px;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }

        .form-control:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 0.2rem rgba(59, 30, 120, 0.25);
        }

        .btn-primary {
            background: var(--primary-color);
            border: none;
            padding: 12px 30px;
            border-radius: 25px;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            background: #2a1656;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(59, 30, 120, 0.3);
        }

        .btn-success {
            background: var(--accent-color);
            border: none;
            padding: 12px 30px;
            border-radius: 25px;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        .btn-success:hover {
            background: #d6734a;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(236, 139, 94, 0.3);
        }

        .food-item {
            display: flex;
            justify-content: between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #e9ecef;
        }

        .food-item:last-child {
            border-bottom: none;
        }

        .food-name {
            font-weight: 500;
            color: var(--text-dark);
        }

        .food-calories {
            color: var(--accent-color);
            font-weight: bold;
        }

        .water-tracker {
            background: linear-gradient(135deg, #17a2b8, #20c997);
            color: white;
            border-radius: 15px;
            padding: 25px;
            text-align: center;
            margin-bottom: 30px;
        }

        .water-glass {
            width: 60px;
            height: 80px;
            border: 3px solid rgba(255,255,255,0.3);
            border-radius: 0 0 30px 30px;
            margin: 0 auto 15px;
            position: relative;
            overflow: hidden;
        }

        .water-fill {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            background: rgba(255,255,255,0.6);
            transition: height 0.3s ease;
        }

        .water-button {
            background: rgba(255,255,255,0.2);
            border: 2px solid rgba(255,255,255,0.3);
            color: white;
            padding: 8px 16px;
            border-radius: 20px;
            margin: 5px;
            transition: all 0.3s ease;
        }

        .water-button:hover {
            background: rgba(255,255,255,0.3);
            color: white;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark fixed-top">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/member/dashboard">
                <i class="fas fa-dumbbell me-2"></i>STAMINA GYM
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/dashboard">
                            <i class="fas fa-home me-1"></i>Trang Chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/schedule">
                            <i class="fas fa-calendar me-1"></i>Lịch Tập
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/workout">
                            <i class="fas fa-dumbbell me-1"></i>Buổi Tập
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/member/nutrition">
                            <i class="fas fa-apple-alt me-1"></i>Dinh Dưỡng
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/membership">
                            <i class="fas fa-id-card me-1"></i>Gói Tập
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/member/profile">
                            <i class="fas fa-user me-1"></i>Hồ Sơ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                            <i class="fas fa-sign-out-alt me-1"></i>Đăng Xuất
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container main-content">
        <!-- Page Header -->
        <div class="page-header">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h1 class="mb-2"><i class="fas fa-apple-alt me-2"></i>Dinh Dưỡng</h1>
                    <p class="text-muted mb-0">Theo dõi và quản lý chế độ dinh dưỡng của bạn</p>
                </div>
                <div class="col-md-4 text-end">
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#logFoodModal">
                        <i class="fas fa-plus me-2"></i>Ghi Nhận Bữa Ăn
                    </button>
                </div>
            </div>
        </div>

        <!-- Daily Nutrition Overview -->
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="nutrition-card text-center">
                    <div class="macro-circle macro-protein">
                        <div class="macro-inner">
                            <div class="macro-value">120g</div>
                            <div class="macro-unit">Protein</div>
                        </div>
                    </div>
                    <h6 class="mb-2">Protein</h6>
                    <div class="progress">
                        <div class="progress-bar progress-protein" role="progressbar" style="width: 80%"></div>
                    </div>
                    <small class="text-muted">120/150g (80%)</small>
                </div>
            </div>
            
            <div class="col-md-3">
                <div class="nutrition-card text-center">
                    <div class="macro-circle macro-carbs">
                        <div class="macro-inner">
                            <div class="macro-value">250g</div>
                            <div class="macro-unit">Carbs</div>
                        </div>
                    </div>
                    <h6 class="mb-2">Carbohydrates</h6>
                    <div class="progress">
                        <div class="progress-bar progress-carbs" role="progressbar" style="width: 71%"></div>
                    </div>
                    <small class="text-muted">250/350g (71%)</small>
                </div>
            </div>
            
            <div class="col-md-3">
                <div class="nutrition-card text-center">
                    <div class="macro-circle macro-fat">
                        <div class="macro-inner">
                            <div class="macro-value">85g</div>
                            <div class="macro-unit">Fat</div>
                        </div>
                    </div>
                    <h6 class="mb-2">Fat</h6>
                    <div class="progress">
                        <div class="progress-bar progress-fat" role="progressbar" style="width: 68%"></div>
                    </div>
                    <small class="text-muted">85/125g (68%)</small>
                </div>
            </div>
            
            <div class="col-md-3">
                <div class="nutrition-card text-center">
                    <div class="macro-circle macro-water">
                        <div class="macro-inner">
                            <div class="macro-value">6L</div>
                            <div class="macro-unit">Water</div>
                        </div>
                    </div>
                    <h6 class="mb-2">Nước</h6>
                    <div class="progress">
                        <div class="progress-bar progress-water" role="progressbar" style="width: 75%"></div>
                    </div>
                    <small class="text-muted">6/8L (75%)</small>
                </div>
            </div>
        </div>

        <!-- Calories Overview -->
        <div class="row mb-4">
            <div class="col-md-8">
                <div class="nutrition-card">
                    <h5 class="mb-4"><i class="fas fa-fire me-2"></i>Calories Hôm Nay</h5>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="text-center mb-4">
                                <h2 class="text-primary mb-2">1,850</h2>
                                <p class="text-muted mb-0">Calories đã tiêu thụ</p>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="text-center mb-4">
                                <h2 class="text-success mb-2">2,200</h2>
                                <p class="text-muted mb-0">Mục tiêu calories</p>
                            </div>
                        </div>
                    </div>
                    
                    <div class="progress mb-3">
                        <div class="progress-bar bg-primary" role="progressbar" style="width: 84%"></div>
                    </div>
                    
                    <div class="row text-center">
                        <div class="col-md-4">
                            <h6 class="text-muted">Còn lại</h6>
                            <h5 class="text-success">350</h5>
                        </div>
                        <div class="col-md-4">
                            <h6 class="text-muted">Đã đốt</h6>
                            <h5 class="text-warning">450</h5>
                        </div>
                        <div class="col-md-4">
                            <h6 class="text-muted">Tổng</h6>
                            <h5 class="text-primary">2,300</h5>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-4">
                <div class="water-tracker">
                    <h5 class="mb-4"><i class="fas fa-tint me-2"></i>Theo Dõi Nước</h5>
                    
                    <div class="water-glass">
                        <div class="water-fill" style="height: 75%;"></div>
                    </div>
                    
                    <h4 class="mb-3">6/8 Lít</h4>
                    
                    <div class="mb-3">
                        <button class="water-button" onclick="addWater(0.25)">
                            <i class="fas fa-plus"></i> 250ml
                        </button>
                        <button class="water-button" onclick="addWater(0.5)">
                            <i class="fas fa-plus"></i> 500ml
                        </button>
                    </div>
                    
                    <button class="btn btn-light" onclick="resetWater()">
                        <i class="fas fa-refresh me-2"></i>Reset
                    </button>
                </div>
            </div>
        </div>

        <!-- Meals Today -->
        <div class="row">
            <div class="col-md-6">
                <h4 class="mb-4"><i class="fas fa-utensils me-2"></i>Bữa Ăn Hôm Nay</h4>
                
                <!-- Breakfast -->
                <div class="meal-card">
                    <div class="d-flex align-items-center mb-3">
                        <div class="meal-icon meal-breakfast">
                            <i class="fas fa-sun"></i>
                        </div>
                        <div>
                            <h6 class="mb-1">Bữa Sáng</h6>
                            <small class="text-muted">07:30 - 450 calories</small>
                        </div>
                    </div>
                    
                    <div class="food-item">
                        <div>
                            <div class="food-name">Bánh mì nướng + Bơ</div>
                            <small class="text-muted">2 lát bánh mì, 20g bơ</small>
                        </div>
                        <div class="food-calories">280 cal</div>
                    </div>
                    
                    <div class="food-item">
                        <div>
                            <div class="food-name">Trứng chiên</div>
                            <small class="text-muted">2 quả trứng, 1 muỗng dầu</small>
                        </div>
                        <div class="food-calories">170 cal</div>
                    </div>
                    
                    <button class="btn btn-sm btn-outline-primary w-100 mt-2">
                        <i class="fas fa-plus me-2"></i>Thêm Món
                    </button>
                </div>
                
                <!-- Lunch -->
                <div class="meal-card">
                    <div class="d-flex align-items-center mb-3">
                        <div class="meal-icon meal-lunch">
                            <i class="fas fa-sun"></i>
                        </div>
                        <div>
                            <h6 class="mb-1">Bữa Trưa</h6>
                            <small class="text-muted">12:00 - 650 calories</small>
                        </div>
                    </div>
                    
                    <div class="food-item">
                        <div>
                            <div class="food-name">Cơm trắng</div>
                            <small class="text-muted">1 chén cơm</small>
                        </div>
                        <div class="food-calories">200 cal</div>
                    </div>
                    
                    <div class="food-item">
                        <div>
                            <div class="food-name">Thịt gà nướng</div>
                            <small class="text-muted">150g thịt gà</small>
                        </div>
                        <div class="food-calories">250 cal</div>
                    </div>
                    
                    <div class="food-item">
                        <div>
                            <div class="food-name">Rau xào</div>
                            <small class="text-muted">200g rau cải</small>
                        </div>
                        <div class="food-calories">80 cal</div>
                    </div>
                    
                    <div class="food-item">
                        <div>
                            <div class="food-name">Canh chua</div>
                            <small class="text-muted">1 tô canh</small>
                        </div>
                        <div class="food-calories">120 cal</div>
                    </div>
                    
                    <button class="btn btn-sm btn-outline-primary w-100 mt-2">
                        <i class="fas fa-plus me-2"></i>Thêm Món
                    </button>
                </div>
            </div>
            
            <div class="col-md-6">
                <!-- Dinner -->
                <div class="meal-card">
                    <div class="d-flex align-items-center mb-3">
                        <div class="meal-icon meal-dinner">
                            <i class="fas fa-moon"></i>
                        </div>
                        <div>
                            <h6 class="mb-1">Bữa Tối</h6>
                            <small class="text-muted">18:30 - 550 calories</small>
                        </div>
                    </div>
                    
                    <div class="food-item">
                        <div>
                            <div class="food-name">Cá hồi nướng</div>
                            <small class="text-muted">150g cá hồi</small>
                        </div>
                        <div class="food-calories">280 cal</div>
                    </div>
                    
                    <div class="food-item">
                        <div>
                            <div class="food-name">Khoai tây nghiền</div>
                            <small class="text-muted">200g khoai tây</small>
                        </div>
                        <div class="food-calories">150 cal</div>
                    </div>
                    
                    <div class="food-item">
                        <div>
                            <div class="food-name">Salad rau xanh</div>
                            <small class="text-muted">100g rau, 1 muỗng dầu olive</small>
                        </div>
                        <div class="food-calories">120 cal</div>
                    </div>
                    
                    <button class="btn btn-sm btn-outline-primary w-100 mt-2">
                        <i class="fas fa-plus me-2"></i>Thêm Món
                    </button>
                </div>
                
                <!-- Snacks -->
                <div class="meal-card">
                    <div class="d-flex align-items-center mb-3">
                        <div class="meal-icon meal-snack">
                            <i class="fas fa-cookie-bite"></i>
                        </div>
                        <div>
                            <h6 class="mb-1">Đồ Ăn Vặt</h6>
                            <small class="text-muted">15:30 - 200 calories</small>
                        </div>
                    </div>
                    
                    <div class="food-item">
                        <div>
                            <div class="food-name">Hạt hạnh nhân</div>
                            <small class="text-muted">30g hạnh nhân</small>
                        </div>
                        <div class="food-calories">180 cal</div>
                    </div>
                    
                    <div class="food-item">
                        <div>
                            <div class="food-name">Táo</div>
                            <small class="text-muted">1 quả táo</small>
                        </div>
                        <div class="food-calories">80 cal</div>
                    </div>
                    
                    <button class="btn btn-sm btn-outline-primary w-100 mt-2">
                        <i class="fas fa-plus me-2"></i>Thêm Món
                    </button>
                </div>
            </div>
        </div>

        <!-- Nutrition Goals -->
        <div class="row">
            <div class="col-12">
                <div class="nutrition-card">
                    <h5 class="mb-4"><i class="fas fa-target me-2"></i>Mục Tiêu Dinh Dưỡng</h5>
                    
                    <div class="row">
                        <div class="col-md-3 mb-3">
                            <div class="text-center">
                                <h6 class="text-muted">Calories/Ngày</h6>
                                <div class="progress mb-2">
                                    <div class="progress-bar bg-primary" role="progressbar" style="width: 84%"></div>
                                </div>
                                <p class="mb-0"><strong>1,850/2,200</strong></p>
                            </div>
                        </div>
                        
                        <div class="col-md-3 mb-3">
                            <div class="text-center">
                                <h6 class="text-muted">Protein/Ngày</h6>
                                <div class="progress mb-2">
                                    <div class="progress-bar progress-protein" role="progressbar" style="width: 80%"></div>
                                </div>
                                <p class="mb-0"><strong>120/150g</strong></p>
                            </div>
                        </div>
                        
                        <div class="col-md-3 mb-3">
                            <div class="text-center">
                                <h6 class="text-muted">Carbs/Ngày</h6>
                                <div class="progress mb-2">
                                    <div class="progress-bar progress-carbs" role="progressbar" style="width: 71%"></div>
                                </div>
                                <p class="mb-0"><strong>250/350g</strong></p>
                            </div>
                        </div>
                        
                        <div class="col-md-3 mb-3">
                            <div class="text-center">
                                <h6 class="text-muted">Fat/Ngày</h6>
                                <div class="progress mb-2">
                                    <div class="progress-bar progress-fat" role="progressbar" style="width: 68%"></div>
                                </div>
                                <p class="mb-0"><strong>85/125g</strong></p>
                            </div>
                        </div>
                    </div>
                    
                    <div class="text-center mt-4">
                        <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#updateGoalsModal">
                            <i class="fas fa-edit me-2"></i>Cập Nhật Mục Tiêu
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Log Food Modal -->
    <div class="modal fade" id="logFoodModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Ghi Nhận Bữa Ăn</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/member/nutrition">
                        <input type="hidden" name="action" value="logFood">
                        
                        <div class="mb-3">
                            <label for="mealType" class="form-label">Loại Bữa Ăn</label>
                            <select class="form-control" id="mealType" name="mealType" required>
                                <option value="">Chọn loại bữa ăn</option>
                                <option value="breakfast">Bữa Sáng</option>
                                <option value="lunch">Bữa Trưa</option>
                                <option value="dinner">Bữa Tối</option>
                                <option value="snack">Đồ Ăn Vặt</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="foodName" class="form-label">Tên Món Ăn</label>
                            <input type="text" class="form-control" id="foodName" name="foodName" 
                                   placeholder="Nhập tên món ăn..." required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="quantity" class="form-label">Số Lượng</label>
                            <input type="text" class="form-control" id="quantity" name="quantity" 
                                   placeholder="VD: 150g, 1 chén, 2 quả..." required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="calories" class="form-label">Calories</label>
                            <input type="number" class="form-control" id="calories" name="calories" 
                                   min="1" max="2000" required>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="protein" class="form-label">Protein (g)</label>
                                <input type="number" class="form-control" id="protein" name="protein" 
                                       min="0" max="200" step="0.1">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="carbs" class="form-label">Carbs (g)</label>
                                <input type="number" class="form-control" id="carbs" name="carbs" 
                                       min="0" max="200" step="0.1">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="fat" class="form-label">Fat (g)</label>
                                <input type="number" class="form-control" id="fat" name="fat" 
                                       min="0" max="200" step="0.1">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary" form="logFoodForm">Ghi Nhận</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Update Goals Modal -->
    <div class="modal fade" id="updateGoalsModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Cập Nhật Mục Tiêu Dinh Dưỡng</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form method="post" action="${pageContext.request.contextPath}/member/nutrition">
                        <input type="hidden" name="action" value="updateGoal">
                        
                        <div class="mb-3">
                            <label for="dailyCalories" class="form-label">Calories/Ngày</label>
                            <input type="number" class="form-control" id="dailyCalories" name="dailyCalories" 
                                   value="2200" min="1000" max="5000" required>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="proteinGoal" class="form-label">Protein (g)</label>
                                <input type="number" class="form-control" id="proteinGoal" name="proteinGoal" 
                                       value="150" min="50" max="300" required>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="carbGoal" class="form-label">Carbs (g)</label>
                                <input type="number" class="form-control" id="carbGoal" name="carbGoal" 
                                       value="350" min="100" max="500" required>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="fatGoal" class="form-label">Fat (g)</label>
                                <input type="number" class="form-control" id="fatGoal" name="fatGoal" 
                                       value="125" min="30" max="200" required>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="waterGoal" class="form-label">Nước (Lít)</label>
                            <input type="number" class="form-control" id="waterGoal" name="waterGoal" 
                                   value="8" min="1" max="15" step="0.5" required>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary" form="updateGoalsForm">Cập Nhật</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script>
        let currentWater = 6;
        const maxWater = 8;

        function addWater(amount) {
            currentWater = Math.min(currentWater + amount, maxWater);
            updateWaterDisplay();
        }

        function resetWater() {
            currentWater = 0;
            updateWaterDisplay();
        }

        function updateWaterDisplay() {
            const percentage = (currentWater / maxWater) * 100;
            document.querySelector('.water-fill').style.height = percentage + '%';
            document.querySelector('.water-tracker h4').textContent = `${currentWater.toFixed(1)}/${maxWater} Lít`;
            
            // Update progress bar
            document.querySelector('.progress-water').style.width = percentage + '%';
            document.querySelector('.progress-water').parentElement.nextElementSibling.textContent = 
                `${currentWater.toFixed(1)}/${maxWater}L (${Math.round(percentage)}%)`;
        }

        // Handle form submissions
        document.querySelectorAll('form').forEach(form => {
            form.addEventListener('submit', function(e) {
                e.preventDefault();
                
                const formData = new FormData(this);
                const action = formData.get('action');
                
                switch(action) {
                    case 'logFood':
                        alert('Ghi nhận bữa ăn thành công!');
                        break;
                    case 'updateGoal':
                        alert('Cập nhật mục tiêu dinh dưỡng thành công!');
                        break;
                }
                
                // TODO: Implement actual form submission
                this.reset();
                $('.modal').modal('hide');
            });
        });

        // Animate progress bars on page load
        window.addEventListener('load', function() {
            const progressBars = document.querySelectorAll('.progress-bar');
            progressBars.forEach(bar => {
                const width = bar.style.width;
                bar.style.width = '0%';
                setTimeout(() => {
                    bar.style.width = width;
                }, 500);
            });
        });

        // Handle meal type selection
        document.getElementById('mealType').addEventListener('change', function() {
            const mealType = this.value;
            let suggestions = [];
            
            switch(mealType) {
                case 'breakfast':
                    suggestions = ['Bánh mì', 'Trứng', 'Sữa', 'Yogurt', 'Trái cây'];
                    break;
                case 'lunch':
                    suggestions = ['Cơm', 'Thịt gà', 'Cá', 'Rau xào', 'Canh'];
                    break;
                case 'dinner':
                    suggestions = ['Cá hồi', 'Thịt bò', 'Khoai tây', 'Salad', 'Súp'];
                    break;
                case 'snack':
                    suggestions = ['Hạt hạnh nhân', 'Táo', 'Chuối', 'Sữa chua', 'Bánh quy'];
                    break;
            }
            
            if (suggestions.length > 0) {
                // TODO: Show food suggestions
                console.log('Food suggestions for', mealType, ':', suggestions);
            }
        });
    </script>
</body>
</html>
