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

        .nutrition-card {
        background: var(--card);
            border-radius: 15px;
        box-shadow: 0 8px 25px var(--shadow);
        padding: 30px;
        margin-bottom: 30px;
        transition: all 0.3s ease;
        }

        .nutrition-card:hover {
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

    .nutrition-title {
        color: var(--text);
        font-weight: 800;
        margin-bottom: 30px;
        text-transform: uppercase;
        letter-spacing: 1px;
    }

    .meal-item {
        background: var(--muted);
        border-radius: 10px;
        padding: 20px;
        margin-bottom: 15px;
        border-left: 4px solid var(--accent);
    }

    .btn-nutrition {
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

    .btn-nutrition:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
            color: white;
    }

    .calorie-circle {
        width: 120px;
        height: 120px;
        border-radius: 50%;
        background: conic-gradient(var(--accent) 0deg, var(--accent) 216deg, var(--muted) 216deg);
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto;
            position: relative;
    }

    .calorie-circle::before {
        content: '';
        width: 80px;
        height: 80px;
        background: var(--card);
        border-radius: 50%;
            position: absolute;
    }

    .calorie-text {
        position: relative;
        z-index: 1;
        text-align: center;
        }
    </style>

<div class="container mt-5">
    <!-- Nutrition Header -->
    <div class="nutrition-card text-center">
        <h2 class="nutrition-title">Dinh Dưỡng Hôm Nay</h2>
        <p class="text-muted mb-4">Theo dõi lượng calo và dinh dưỡng hàng ngày</p>
        </div>

    <!-- Calorie Overview -->
        <div class="row mb-4">
        <div class="col-md-4">
                <div class="nutrition-card text-center">
                <h5 class="nutrition-title">Calories</h5>
                <div class="calorie-circle">
                    <div class="calorie-text">
                        <h3 class="mb-0">1,850</h3>
                        <small class="text-muted">/ 2,200</small>
                    </div>
                </div>
                <p class="text-muted mt-3">Còn lại: 350 cal</p>
            </div>
        </div>
            <div class="col-md-8">
                <div class="nutrition-card">
                <h5 class="nutrition-title">Macros Hôm Nay</h5>
                    <div class="row">
                    <div class="col-md-4 text-center">
                        <div class="mb-3">
                            <h4 style="color: var(--accent);">120g</h4>
                            <small class="text-muted">Protein</small>
                        </div>
                            </div>
                    <div class="col-md-4 text-center">
                        <div class="mb-3">
                            <h4 style="color: var(--accent);">180g</h4>
                            <small class="text-muted">Carbs</small>
                        </div>
                    </div>
                    <div class="col-md-4 text-center">
                        <div class="mb-3">
                            <h4 style="color: var(--accent);">65g</h4>
                            <small class="text-muted">Fat</small>
                        </div>
                    </div>
                </div>
                </div>
            </div>
        </div>

    <!-- Meals -->
        <div class="row">
        <div class="col-md-8">
            <div class="nutrition-card">
                <h4 class="nutrition-title">Bữa Ăn Hôm Nay</h4>
                
                <!-- Breakfast -->
                <div class="meal-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-1">Bữa Sáng</h5>
                            <p class="text-muted mb-0">Bánh mì, trứng, sữa</p>
                        </div>
                        <div class="text-end">
                            <h6 class="mb-0">450 cal</h6>
                            <small class="text-muted">08:00</small>
                        </div>
                    </div>
                </div>
                
                <!-- Lunch -->
                <div class="meal-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-1">Bữa Trưa</h5>
                            <p class="text-muted mb-0">Cơm, thịt gà, rau xanh</p>
                        </div>
                        <div class="text-end">
                            <h6 class="mb-0">650 cal</h6>
                            <small class="text-muted">12:30</small>
                        </div>
                </div>
            </div>
            
                <!-- Dinner -->
                <div class="meal-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-1">Bữa Tối</h5>
                            <p class="text-muted mb-0">Cá hồi, khoai tây, salad</p>
                        </div>
                        <div class="text-end">
                            <h6 class="mb-0">750 cal</h6>
                            <small class="text-muted">19:00</small>
                        </div>
                    </div>
                </div>
                
                <!-- Add Meal Button -->
                <div class="text-center mt-4">
                    <button class="btn-nutrition">
                        <i class="fas fa-plus me-2"></i>Thêm Bữa Ăn
                    </button>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <!-- Water Intake -->
                <div class="nutrition-card">
                <h5 class="nutrition-title">Nước Uống</h5>
                            <div class="text-center">
                    <div class="mb-3">
                        <i class="fas fa-tint fa-3x" style="color: var(--accent);"></i>
                    </div>
                    <h4 class="mb-1">6/8</h4>
                    <p class="text-muted">Cốc nước</p>
                    <div class="progress mb-3">
                        <div class="progress-bar" style="width: 75%"></div>
                    </div>
                    <button class="btn-nutrition">
                        <i class="fas fa-plus me-2"></i>Thêm Nước
                    </button>
        </div>
    </div>

            <!-- Quick Add -->
            <div class="nutrition-card">
                <h5 class="nutrition-title">Thêm Nhanh</h5>
                <div class="d-grid gap-2">
                    <button class="btn-nutrition">
                        <i class="fas fa-apple-alt me-2"></i>Trái Cây
                    </button>
                    <button class="btn-nutrition">
                        <i class="fas fa-carrot me-2"></i>Rau Củ
                    </button>
                    <button class="btn-nutrition">
                        <i class="fas fa-drumstick-bite me-2"></i>Protein
                    </button>
                    <button class="btn-nutrition">
                        <i class="fas fa-bread-slice me-2"></i>Carbs
                    </button>
                </div>
                </div>
            </div>
        </div>
    </div>

<%@ include file="/views/common/footer.jsp" %>