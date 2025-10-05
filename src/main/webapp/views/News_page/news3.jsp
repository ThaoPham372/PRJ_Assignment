<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

      /* MAIN CONTENT */
      .main-content {
        max-width: 1000px;
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
        color: var(--accent-hover);
      }

      .article-header {
        margin-bottom: 40px;
      }

      .article-meta {
        display: flex;
        align-items: center;
        gap: 20px;
        margin-bottom: 20px;
        font-size: 0.9rem;
        color: var(--text-light);
      }

      .article-meta span {
        display: flex;
        align-items: center;
        gap: 5px;
      }

      .article-meta i {
        color: var(--accent);
      }

      .article-title {
        font-size: 2.5rem;
        font-weight: 900;
        color: var(--primary);
        line-height: 1.2;
        margin-bottom: 20px;
      }

      .article-banner {
        width: 100%;
        height: 400px;
        object-fit: cover;
        border-radius: 15px;
        margin-bottom: 40px;
        box-shadow: 0 10px 30px var(--shadow);
      }

      .article-content {
        background: var(--card);
        padding: 40px;
        border-radius: 15px;
        box-shadow: 0 8px 30px var(--shadow);
        margin-bottom: 40px;
      }

      .article-content h2 {
        color: var(--primary);
        font-size: 1.8rem;
        font-weight: 800;
        margin: 30px 0 15px;
        line-height: 1.3;
      }

      .article-content h2:first-child {
        margin-top: 0;
      }

      .article-content h3 {
        color: var(--primary);
        font-size: 1.4rem;
        font-weight: 700;
        margin: 25px 0 12px;
        line-height: 1.3;
      }

      .article-content p {
        margin-bottom: 20px;
        font-size: 1.1rem;
        line-height: 1.7;
        color: var(--text);
      }

      .article-content ul {
        margin: 20px 0;
        padding-left: 30px;
      }

      .article-content li {
        margin-bottom: 10px;
        font-size: 1.1rem;
        line-height: 1.6;
        color: var(--text);
      }

      .highlight-box {
        background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
        border-left: 4px solid var(--accent);
        padding: 25px;
        margin: 30px 0;
        border-radius: 0 10px 10px 0;
        box-shadow: 0 4px 15px var(--shadow);
      }

      .highlight-box p {
        margin: 0;
        font-size: 1.1rem;
        font-weight: 600;
        color: var(--primary);
        font-style: italic;
      }

      .offer-image-container {
        margin: 30px 0;
        text-align: center;
      }

      .offer-image {
        width: 100%;
        max-width: 800px;
        height: auto;
        border-radius: 15px;
        box-shadow: 0 8px 25px var(--shadow);
        transition: transform 0.3s ease, box-shadow 0.3s ease;
      }

      .offer-image:hover {
        transform: scale(1.02);
        box-shadow: 0 12px 35px var(--shadow-hover);
      }

      .offer-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
        gap: 25px;
        margin: 30px 0;
      }

      .offer-card {
        background: var(--gradient-primary);
        color: #fff;
        padding: 25px;
        border-radius: 15px;
        text-align: center;
        position: relative;
        overflow: hidden;
      }

      .offer-card::before {
        content: '';
        position: absolute;
        top: -50%;
        right: -50%;
        width: 100%;
        height: 100%;
        background: linear-gradient(
          45deg,
          transparent,
          rgba(255, 255, 255, 0.1),
          transparent
        );
        transform: rotate(45deg);
        transition: all 0.3s ease;
      }

      .offer-card:hover::before {
        animation: shimmer 1.5s ease-in-out;
      }

      @keyframes shimmer {
        0% {
          transform: translateX(-100%) translateY(-100%) rotate(45deg);
        }
        100% {
          transform: translateX(100%) translateY(100%) rotate(45deg);
        }
      }

      .offer-card h4 {
        color: var(--accent);
        font-size: 1.3rem;
        margin-bottom: 10px;
        font-weight: 700;
      }

      .offer-card .price {
        font-size: 2rem;
        font-weight: 900;
        margin-bottom: 10px;
        color: #fff;
      }

      .offer-card .original-price {
        text-decoration: line-through;
        opacity: 0.7;
        font-size: 1.2rem;
        margin-right: 10px;
      }

      .offer-card .discount {
        background: var(--accent);
        color: #fff;
        padding: 5px 10px;
        border-radius: 20px;
        font-size: 0.9rem;
        font-weight: 600;
        display: inline-block;
        margin-bottom: 15px;
      }

      .offer-card p {
        margin: 0;
        opacity: 0.9;
        font-size: 0.95rem;
      }

      .urgency-box {
        background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
        color: #fff;
        padding: 20px;
        border-radius: 15px;
        text-align: center;
        margin: 30px 0;
        animation: pulse 2s infinite;
      }

      @keyframes pulse {
        0% {
          transform: scale(1);
        }
        50% {
          transform: scale(1.02);
        }
        100% {
          transform: scale(1);
        }
      }

      .urgency-box h3 {
        color: #fff;
        margin-bottom: 10px;
        font-size: 1.3rem;
      }

      .urgency-box p {
        margin: 0;
        font-weight: 600;
      }

      .cta-section {
        background: var(--gradient-primary);
        color: #fff;
        padding: 40px;
        border-radius: 15px;
        text-align: center;
        margin-top: 40px;
      }

      .cta-section h3 {
        font-size: 1.8rem;
        margin-bottom: 15px;
        color: #fff;
      }

      .cta-section p {
        margin-bottom: 25px;
        opacity: 0.9;
        font-size: 1.1rem;
      }

      .cta-section .btn {
        background: var(--gradient-accent);
        font-size: 1.1rem;
        padding: 15px 30px;
      }

      /* FOOTER */
      footer {
        background: var(--gradient-primary);
        color: #fff;
        text-align: center;
        padding: 30px;
        margin-top: 60px;
        font-size: 0.9rem;
        opacity: 0.9;
      }

      /* RESPONSIVE */
      @media (max-width: 768px) {
        header {
          flex-direction: column;
          gap: 15px;
          padding: 20px;
        }

        nav ul {
          gap: 20px;
          flex-wrap: wrap;
          justify-content: center;
        }

        .main-content {
          padding: 20px;
        }

        .article-title {
          font-size: 2rem;
        }

        .article-banner {
          height: 250px;
        }

        .article-content {
          padding: 25px;
        }

        .article-content h2 {
          font-size: 1.5rem;
        }

        .article-content h3 {
          font-size: 1.2rem;
        }

        .article-content p,
        .article-content li {
          font-size: 1rem;
        }

        .offer-grid {
          grid-template-columns: 1fr;
        }

        .cta-section {
          padding: 30px 20px;
        }

        .cta-section h3 {
          font-size: 1.5rem;
        }
      }

      @media (max-width: 480px) {
        .article-title {
          font-size: 1.8rem;
        }

        .article-banner {
          height: 200px;
        }

        .article-content {
          padding: 20px;
        }

        .article-meta {
          flex-direction: column;
          align-items: flex-start;
          gap: 10px;
        }

        .offer-card {
          padding: 20px;
        }

        .offer-card .price {
          font-size: 1.5rem;
        }
      }
    </style>
  </head>
  <body>

    <!-- MAIN CONTENT -->
    <main class="main-content">
      <a
        href="${pageContext.request.contextPath}/views/News_page/news_main.jsp"
        class="back-btn"
      >
        <i class="fas fa-arrow-left"></i>
        Quay lại tin tức
      </a>

      <article class="article-header">
        <div class="article-meta">
          <span><i class="fas fa-calendar-alt"></i> 01/12/2024</span>
          <span><i class="fas fa-user"></i> Bộ phận Marketing</span>
          <span><i class="fas fa-eye"></i> 2,100 lượt xem</span>
        </div>
        <h1 class="article-title">Ưu đãi hội viên tháng này</h1>
      </article>

      <img
        src="https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80"
        alt="Ưu đãi hội viên GymFit"
        class="article-banner"
        loading="lazy"
        decoding="async"
        referrerpolicy="no-referrer"
      />

      <div class="article-content">
        <h2>Cơ hội vàng để trở thành thành viên GymFit</h2>
        <p>
          Tháng 12 này, GymFit mang đến những ưu đãi đặc biệt nhất trong năm
          dành cho các thành viên mới và hiện tại. Đây là cơ hội tuyệt vời để
          bạn bắt đầu hoặc tiếp tục hành trình tập luyện với những điều kiện tốt
          nhất.
        </p>

        <div class="urgency-box">
          <h3>⚠️ Ưu đãi có thời hạn!</h3>
          <p>Chỉ áp dụng đến hết ngày 31/12/2024. Số lượng có hạn!</p>
        </div>

        <h3>Gói ưu đãi đặc biệt</h3>

        <div class="offer-image-container">
          <img
            src="https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80"
            alt="Gói ưu đãi đặc biệt GymFit"
            class="offer-image"
            loading="lazy"
            decoding="async"
            referrerpolicy="no-referrer"
          />
        </div>

        <div class="offer-grid">
          <div class="offer-card">
            <h4>Gói 3 tháng</h4>
            <div class="price">
              <span class="original-price">2,100,000đ</span>
              <span>1,680,000đ</span>
            </div>
            <div class="discount">-20%</div>
            <p>Tiết kiệm 420,000đ. Phù hợp cho người mới bắt đầu</p>
          </div>

          <div class="offer-card">
            <h4>Gói 6 tháng</h4>
            <div class="price">
              <span class="original-price">3,600,000đ</span>
              <span>2,520,000đ</span>
            </div>
            <div class="discount">-30%</div>
            <p>Tiết kiệm 1,080,000đ. Bao gồm tất cả dịch vụ cơ bản</p>
          </div>

          <div class="offer-card">
            <h4>Gói 12 tháng</h4>
            <div class="price">
              <span class="original-price">6,000,000đ</span>
              <span>3,600,000đ</span>
            </div>
            <div class="discount">-40%</div>
            <p>Tiết kiệm 2,400,000đ. Tặng kèm 2 tháng tập miễn phí</p>
          </div>

          <div class="offer-card">
            <h4>Gói VIP Premium</h4>
            <div class="price">
              <span class="original-price">8,400,000đ</span>
              <span>5,040,000đ</span>
            </div>
            <div class="discount">-40%</div>
            <p>Bao gồm Personal Training và tất cả dịch vụ cao cấp</p>
          </div>
        </div>

        <h3>Quà tặng đặc biệt cho thành viên mới</h3>
        <p>
          Khi đăng ký trong tháng 12, bạn sẽ nhận được những phần quà giá trị:
        </p>
        <ul>
          <li>
            <strong>01 buổi Personal Training miễn phí</strong> - Trị giá
            500,000đ
          </li>
          <li><strong>Kiểm tra InBody chi tiết</strong> - Trị giá 200,000đ</li>
          <li><strong>Tư vấn dinh dưỡng cá nhân</strong> - Trị giá 300,000đ</li>
          <li>
            <strong>Bộ dụng cụ tập luyện cao cấp</strong> - Trị giá 150,000đ
          </li>
          <li><strong>Voucher giảm giá 20%</strong> cho các dịch vụ bổ sung</li>
        </ul>

        <div class="highlight-box">
          <p>
            "Tôi đã đăng ký gói 12 tháng và tiết kiệm được hơn 2 triệu đồng.
            Cộng với các quà tặng, tổng giá trị lên đến 3 triệu đồng!" - Chị
            Lan, thành viên mới
          </p>
        </div>

        <h3>Ưu đãi dành cho thành viên hiện tại</h3>
        <p>
          Các thành viên hiện tại của GymFit cũng được hưởng những ưu đãi đặc
          biệt:
        </p>
        <ul>
          <li>
            <strong>Gia hạn với giá ưu đãi:</strong> Giảm 25% khi gia hạn gói
            tập
          </li>
          <li>
            <strong>Mời bạn bè:</strong> Nhận 1 tháng tập miễn phí khi mời được
            1 người
          </li>
          <li>
            <strong>Nâng cấp gói:</strong> Chỉ cần trả thêm 50% phí chênh lệch
          </li>
          <li>
            <strong>Dịch vụ bổ sung:</strong> Giảm 30% cho tất cả dịch vụ spa và
            massage
          </li>
        </ul>

        <h3>Điều kiện áp dụng</h3>
        <p>Để được hưởng các ưu đãi này, bạn cần lưu ý:</p>
        <ul>
          <li>
            Ưu đãi chỉ áp dụng cho thành viên đăng ký từ 01/12/2024 đến
            31/12/2024
          </li>
          <li>
            Số lượng voucher có hạn, áp dụng theo nguyên tắc "ai đến trước được
            phục vụ trước"
          </li>
          <li>Không áp dụng cộng dồn với các chương trình khuyến mãi khác</li>
          <li>Cần thanh toán đầy đủ trong vòng 7 ngày kể từ ngày đăng ký</li>
          <li>Không hoàn tiền sau khi đã đăng ký thành công</li>
        </ul>

        <h3>Cách thức đăng ký</h3>
        <p>
          Bạn có thể đăng ký tham gia chương trình ưu đãi bằng các cách sau:
        </p>
        <ul>
          <li>
            <strong>Đăng ký trực tiếp:</strong> Đến phòng tập và đăng ký tại
            quầy lễ tân
          </li>
          <li>
            <strong>Đăng ký online:</strong> Truy cập website GymFit và điền
            form đăng ký
          </li>
          <li>
            <strong>Hotline:</strong> Gọi số 0123-456-789 để được tư vấn và đăng
            ký
          </li>
          <li>
            <strong>Zalo/Facebook:</strong> Liên hệ qua fanpage chính thức của
            GymFit
          </li>
        </ul>

        <h3>Cam kết chất lượng dịch vụ</h3>
        <p>
          Dù với giá ưu đãi, GymFit vẫn cam kết mang đến chất lượng dịch vụ tốt
          nhất:
        </p>
        <ul>
          <li>Thiết bị tập luyện hiện đại và được bảo trì thường xuyên</li>
          <li>Đội ngũ huấn luyện viên chuyên nghiệp và tận tâm</li>
          <li>Không gian tập luyện sạch sẽ và thoáng mát</li>
          <li>Hỗ trợ khách hàng 24/7</li>
          <li>Chương trình tập luyện đa dạng và phù hợp</li>
        </ul>
      </div>

      <div class="cta-section">
        <h3>Đừng bỏ lỡ cơ hội vàng này!</h3>
        <p>
          Với những ưu đãi hấp dẫn và quà tặng giá trị, đây là thời điểm tốt
          nhất để bạn bắt đầu hành trình tập luyện tại GymFit. Hãy liên hệ với
          chúng tôi ngay hôm nay!
        </p>
        <a href="#" class="btn">ĐĂNG KÝ NGAY</a>
      </div>
    </main>

<%@ include file="/views/common/footer.jsp" %>

    <!-- Enhanced image fallback -->
    <script>
      function attachImageFallback() {
        document.querySelectorAll('img').forEach(function (img) {
          img.addEventListener(
            'error',
            function () {
              if (!img.dataset.fallback) {
                img.dataset.fallback = '1';
                img.src =
                  'https://via.placeholder.com/1200x400/141a46/ffffff?text=Special+Offer+Image';
                img.style.opacity = '0.7';
              }
            },
            { once: true },
          );
        });
      }
      attachImageFallback();
    </script>
  </body>
</html>
