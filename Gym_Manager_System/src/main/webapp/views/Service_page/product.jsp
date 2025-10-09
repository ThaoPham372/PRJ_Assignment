<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ include
file="/views/common/header.jsp" %>

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

  .main-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 40px 50px;
  }

  .back-btn {
    margin-bottom: 30px;
    display: inline-flex;
    align-items: center;
    gap: 8px;
    color: var(--accent);
    text-decoration: none;
    font-weight: 600;
    transition: all 0.3s ease;
  }

  .back-btn:hover {
    transform: translateX(-5px);
    color: var(--primary);
  }

  .hero-section {
    background: var(--gradient-primary);
    color: #fff;
    padding: 80px 50px;
    text-align: center;
    margin-bottom: 60px;
    border-radius: 20px;
  }

  .hero-section h1 {
    font-size: 3rem;
    font-weight: 900;
    margin-bottom: 20px;
  }

  .hero-section p {
    font-size: 1.2rem;
    opacity: 0.9;
    max-width: 600px;
    margin: 0 auto;
  }

  .products-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 30px;
    margin-bottom: 60px;
  }

  .product-card {
    background: var(--card);
    border-radius: 20px;
    padding: 30px;
    box-shadow: 0 8px 30px var(--shadow);
    transition: all 0.3s ease;
    text-align: center;
  }

  .product-card:hover {
    transform: translateY(-10px);
    box-shadow: 0 15px 40px var(--shadow-hover);
  }

  .product-card img {
    width: 100%;
    height: 200px;
    object-fit: cover;
    border-radius: 15px;
    margin-bottom: 20px;
  }

  .product-card h3 {
    color: var(--primary);
    font-size: 1.5rem;
    font-weight: 700;
    margin-bottom: 15px;
  }

  .product-card p {
    color: var(--text);
    font-size: 1rem;
    line-height: 1.6;
    margin-bottom: 20px;
  }

  .product-price {
    font-size: 1.5rem;
    font-weight: 800;
    color: var(--accent);
    margin-bottom: 20px;
  }

  .product-actions {
    display: flex;
    gap: 15px;
    justify-content: center;
  }

  .btn-buy {
    background: var(--gradient-accent);
    color: #fff;
    padding: 12px 24px;
    border-radius: 25px;
    font-weight: 600;
    text-decoration: none;
    transition: all 0.3s ease;
    flex: 1;
  }

  .btn-buy:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(236, 139, 90, 0.4);
  }

  .btn-cart {
    background: var(--primary);
    color: #fff;
    padding: 12px;
    border-radius: 50%;
    text-decoration: none;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 48px;
    height: 48px;
  }

  .btn-cart:hover {
    background: var(--accent);
    transform: scale(1.1);
  }

  .cta-section {
    background: var(--gradient-primary);
    color: #fff;
    padding: 50px;
    border-radius: 20px;
    text-align: center;
    margin-top: 40px;
  }

  .cta-section h3 {
    font-size: 2rem;
    margin-bottom: 20px;
    color: #fff;
  }

  .cta-section p {
    margin-bottom: 30px;
    opacity: 0.9;
    font-size: 1.1rem;
  }

  .cta-btn {
    background: var(--gradient-accent);
    color: #fff;
    padding: 18px 36px;
    border-radius: 50px;
    font-weight: 700;
    font-size: 1.1rem;
    text-decoration: none;
    display: inline-block;
    transition: all 0.3s ease;
    box-shadow: 0 8px 25px rgba(236, 139, 90, 0.4);
  }

  .cta-btn:hover {
    transform: translateY(-3px);
    box-shadow: 0 12px 35px rgba(236, 139, 90, 0.5);
  }

  @media (max-width: 768px) {
    .main-content {
      padding: 20px;
    }

    .hero-section {
      padding: 60px 30px;
    }

    .hero-section h1 {
      font-size: 2.5rem;
    }

    .products-grid {
      grid-template-columns: 1fr;
      gap: 20px;
    }

    .product-card {
      padding: 20px;
    }

    .cta-section {
      padding: 40px 30px;
    }
  }
</style>

<!-- MAIN CONTENT -->
<main class="main-content">
  <a
    href="${pageContext.request.contextPath}/views/Service_page/services_main.jsp"
    class="back-btn"
  >
    <i class="fas fa-arrow-left"></i>
    Quay lại Dịch vụ
  </a>

  <section class="hero-section">
    <h1>SẢN PHẨM DINH DƯỠNG</h1>
    <p>
      Bổ sung dinh dưỡng chất lượng cao để hỗ trợ quá trình tập luyện và phát
      triển cơ bắp của bạn.
    </p>
  </section>

  <div class="products-grid">
    <div class="product-card">
      <img
        src="https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"
        alt="Whey Protein"
      />
      <h3>Whey Protein</h3>
      <p>
        Bổ sung protein chất lượng cao, giúp phục hồi và phát triển cơ bắp sau
        tập luyện.
      </p>
      <div class="product-price">1,200,000đ</div>
      <div class="product-actions">
        <a href="#" class="btn-buy">Mua ngay</a>
        <a href="#" class="btn-cart"><i class="fas fa-shopping-cart"></i></a>
      </div>
    </div>

    <div class="product-card">
      <img
        src="https://images.unsplash.com/photo-1594736797933-d0c29c8a0a8e?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"
        alt="Creatine"
      />
      <h3>Creatine Monohydrate</h3>
      <p>Tăng cường sức mạnh và sức bền, hỗ trợ tập luyện cường độ cao.</p>
      <div class="product-price">800,000đ</div>
      <div class="product-actions">
        <a href="#" class="btn-buy">Mua ngay</a>
        <a href="#" class="btn-cart"><i class="fas fa-shopping-cart"></i></a>
      </div>
    </div>

    <div class="product-card">
      <img
        src="https://images.unsplash.com/photo-1594736797933-d0c29c8a0a8e?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"
        alt="BCAA"
      />
      <h3>BCAA Essential</h3>
      <p>
        Bổ sung axit amin thiết yếu, giảm mệt mỏi và tăng cường phục hồi cơ bắp.
      </p>
      <div class="product-price">600,000đ</div>
      <div class="product-actions">
        <a href="#" class="btn-buy">Mua ngay</a>
        <a href="#" class="btn-cart"><i class="fas fa-shopping-cart"></i></a>
      </div>
    </div>

    <div class="product-card">
      <img
        src="https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"
        alt="Pre-Workout"
      />
      <h3>Pre-Workout Energy</h3>
      <p>Tăng cường năng lượng và tập trung trước khi tập luyện.</p>
      <div class="product-price">900,000đ</div>
      <div class="product-actions">
        <a href="#" class="btn-buy">Mua ngay</a>
        <a href="#" class="btn-cart"><i class="fas fa-shopping-cart"></i></a>
      </div>
    </div>

    <div class="product-card">
      <img
        src="https://images.unsplash.com/photo-1594736797933-d0c29c8a0a8e?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"
        alt="Mass Gainer"
      />
      <h3>Mass Gainer</h3>
      <p>Hỗ trợ tăng cân và tăng cơ cho người gầy, bổ sung calo và protein.</p>
      <div class="product-price">1,500,000đ</div>
      <div class="product-actions">
        <a href="#" class="btn-buy">Mua ngay</a>
        <a href="#" class="btn-cart"><i class="fas fa-shopping-cart"></i></a>
      </div>
    </div>

    <div class="product-card">
      <img
        src="https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"
        alt="Multivitamin"
      />
      <h3>Multivitamin</h3>
      <p>Bổ sung vitamin và khoáng chất thiết yếu cho cơ thể khỏe mạnh.</p>
      <div class="product-price">500,000đ</div>
      <div class="product-actions">
        <a href="#" class="btn-buy">Mua ngay</a>
        <a href="#" class="btn-cart"><i class="fas fa-shopping-cart"></i></a>
      </div>
    </div>
  </div>

  <div class="cta-section">
    <h3>Mua sản phẩm dinh dưỡng ngay hôm nay!</h3>
    <p>
      Đăng ký ngay để được tư vấn miễn phí về sản phẩm phù hợp và nhận ưu đãi
      đặc biệt.
    </p>
    <a href="#" class="cta-btn">TƯ VẤN NGAY</a>
  </div>
</main>

<%@ include file="/views/common/footer.jsp" %>
