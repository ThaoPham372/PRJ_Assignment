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
          <span><i class="fas fa-calendar-alt"></i> 15/12/2024</span>
          <span><i class="fas fa-user"></i> Bộ phận Marketing</span>
          <span><i class="fas fa-eye"></i> 3,200 lượt xem</span>
        </div>
        <h1 class="article-title">Khai trương cơ sở mới tại Đà Nẵng</h1>
      </article>

      <img
        src="https://images.unsplash.com/photo-1534438327276-14e5300c3a48?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80"
        alt="Cơ sở mới GymFit tại Đà Nẵng"
        class="article-banner"
        loading="lazy"
        decoding="async"
        referrerpolicy="no-referrer"
      />

      <div class="article-content">
        <h2>Một bước tiến mới trong hành trình phát triển của GymFit</h2>
        <p>
          Với mong muốn mang đến trải nghiệm tập luyện tốt nhất cho cộng đồng Đà
          Nẵng, GymFit tự hào thông báo về việc khai trương cơ sở mới tại thành
          phố biển xinh đẹp này. Đây là một dấu mốc quan trọng trong hành trình
          mở rộng và phát triển của chúng tôi.
        </p>

        <h3>Không gian tập luyện hiện đại</h3>
        <p>
          Cơ sở mới tại Đà Nẵng được thiết kế với tổng diện tích 1.200m², bao
          gồm:
        </p>
        <ul>
          <li>
            <strong>Khu vực tập tạ tự do:</strong> 400m² với đầy đủ thiết bị
            hiện đại
          </li>
          <li>
            <strong>Phòng máy cardio:</strong> 200m² với máy chạy bộ, xe đạp
            tập, máy elliptical
          </li>
          <li>
            <strong>Studio Yoga & Dance:</strong> 150m² với sàn gỗ và hệ thống
            âm thanh chuyên nghiệp
          </li>
          <li><strong>Khu vực tập nhóm:</strong> 200m² cho các lớp tập nhóm</li>
          <li>
            <strong>Phòng thay đồ & tắm:</strong> 150m² với tiện nghi đầy đủ
          </li>
          <li>
            <strong>Khu vực tiếp tân:</strong> 100m² với không gian chờ thoải
            mái
          </li>
        </ul>

        <h3>Thiết bị tập luyện thế hệ mới</h3>
        <p>Cơ sở mới được trang bị những thiết bị tập luyện tiên tiến nhất:</p>
        <ul>
          <li>
            <strong>Máy tập tạ:</strong> Dòng máy Life Fitness và Technogym mới
            nhất
          </li>
          <li>
            <strong>Cardio:</strong> Máy chạy bộ Precor với màn hình cảm ứng
          </li>
          <li>
            <strong>Functional Training:</strong> Khu vực TRX, battle ropes,
            kettlebells
          </li>
          <li>
            <strong>Yoga & Pilates:</strong> Thảm tập cao cấp và dụng cụ hỗ trợ
            đầy đủ
          </li>
          <li>
            <strong>Hệ thống âm thanh:</strong> Âm thanh chất lượng cao cho các
            lớp tập nhóm
          </li>
        </ul>

        <div class="highlight-box">
          <p>
            "Cơ sở mới tại Đà Nẵng thực sự ấn tượng! Không gian rộng rãi, thiết
            bị hiện đại và đội ngũ nhân viên chuyên nghiệp. Tôi rất hài lòng với
            quyết định chuyển sang tập tại đây." - Anh Minh, thành viên mới
          </p>
        </div>

        <h3>Đội ngũ huấn luyện viên chuyên nghiệp</h3>
        <p>
          Cơ sở mới được hỗ trợ bởi đội ngũ huấn luyện viên giàu kinh nghiệm:
        </p>
        <ul>
          <li>
            <strong>HLV Fitness:</strong> Chứng chỉ quốc tế và kinh nghiệm trên
            5 năm
          </li>
          <li>
            <strong>HLV Yoga:</strong> Chứng chỉ Yoga Alliance và chuyên môn về
            các loại hình yoga khác nhau
          </li>
          <li>
            <strong>HLV Dance:</strong> Chuyên gia về Zumba, Aerobic và các bộ
            môn dance fitness
          </li>
          <li>
            <strong>Chuyên gia dinh dưỡng:</strong> Tư vấn chế độ ăn phù hợp với
            từng mục tiêu tập luyện
          </li>
        </ul>

        <h3>Chương trình khai trương đặc biệt</h3>
        <p>
          Để chào mừng sự kiện khai trương, GymFit mang đến những ưu đãi đặc
          biệt:
        </p>
        <ul>
          <li>
            <strong>Tuần đầu khai trương:</strong> Tặng 01 buổi Personal
            Training trải nghiệm miễn phí
          </li>
          <li>
            <strong>Voucher ưu đãi:</strong> Giảm giá lên đến 40% cho các gói
            tập dài hạn
          </li>
          <li>
            <strong>Kiểm tra InBody miễn phí:</strong> Đánh giá thành phần cơ
            thể chi tiết
          </li>
          <li>
            <strong>Tư vấn dinh dưỡng:</strong> Miễn phí cho 100 thành viên đầu
            tiên
          </li>
          <li>
            <strong>Bộ dụng cụ tập luyện:</strong> Tặng kèm cho thành viên mới
            đăng ký trong tháng đầu
          </li>
        </ul>

        <h3>Vị trí thuận lợi</h3>
        <p>Cơ sở mới tọa lạc tại vị trí đắc địa trong thành phố Đà Nẵng:</p>
        <ul>
          <li>
            <strong>Địa chỉ:</strong> 123 Nguyễn Văn Linh, Quận Hải Châu, Đà
            Nẵng
          </li>
          <li>
            <strong>Giao thông:</strong> Dễ dàng tiếp cận bằng xe máy, ô tô và
            xe buýt
          </li>
          <li>
            <strong>Bãi đỗ xe:</strong> Bãi đỗ xe rộng rãi với 50 chỗ đỗ miễn
            phí
          </li>
          <li>
            <strong>Xung quanh:</strong> Gần các trung tâm thương mại, trường
            học và khu dân cư
          </li>
        </ul>

        <h3>Giờ hoạt động</h3>
        <p>Cơ sở mới hoạt động với lịch trình linh hoạt:</p>
        <ul>
          <li><strong>Thứ 2 - Thứ 6:</strong> 5:00 - 22:00</li>
          <li><strong>Thứ 7 - Chủ nhật:</strong> 6:00 - 21:00</li>
          <li><strong>Ngày lễ:</strong> 7:00 - 20:00</li>
        </ul>

        <h3>Cách thức đăng ký</h3>
        <p>Để trở thành thành viên của cơ sở mới, bạn có thể:</p>
        <ul>
          <li>
            <strong>Đăng ký trực tiếp:</strong> Đến cơ sở và đăng ký tại quầy lễ
            tân
          </li>
          <li>
            <strong>Đăng ký online:</strong> Truy cập website GymFit và chọn cơ
            sở Đà Nẵng
          </li>
          <li><strong>Hotline:</strong> Gọi số 0123-456-789 để được tư vấn</li>
          <li>
            <strong>Fanpage:</strong> Liên hệ qua Facebook chính thức của GymFit
          </li>
        </ul>
      </div>

      <div class="cta-section">
        <h3>Tham gia cùng chúng tôi tại cơ sở mới!</h3>
        <p>
          Hãy đến và trải nghiệm không gian tập luyện hiện đại tại cơ sở mới của
          GymFit Đà Nẵng. Chúng tôi cam kết mang đến cho bạn những trải nghiệm
          tập luyện tốt nhất với đội ngũ chuyên nghiệp và thiết bị tiên tiến.
        </p>
        <a href="${pageContext.request.contextPath}/views/register.jsp" class="btn">ĐĂNG KÝ NGAY</a>
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
                  'https://via.placeholder.com/1200x400/141a46/ffffff?text=New+Gym+Opening';
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
