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
        box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
        filter: contrast(1.1) brightness(1.05);
        transition: all 0.3s ease;
      }

      .article-banner:hover {
        transform: scale(1.01);
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.25);
        filter: contrast(1.15) brightness(1.1);
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

      .timeline-image-container {
        margin: 30px 0;
        text-align: center;
      }

      .timeline-image {
        width: 100%;
        max-width: 800px;
        height: auto;
        border-radius: 15px;
        box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
        transition: all 0.3s ease;
        filter: contrast(1.1) brightness(1.05);
        object-fit: contain;
        background: rgba(255, 255, 255, 0.05);
        padding: 20px;
      }

      .timeline-image:hover {
        transform: scale(1.02);
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.25);
        filter: contrast(1.15) brightness(1.1);
      }

      .timeline-image-container {
        background: linear-gradient(135deg, rgba(20, 26, 73, 0.05) 0%, rgba(20, 26, 73, 0.1) 100%);
        padding: 20px;
        border-radius: 20px;
        margin: 40px 0;
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
          <span><i class="fas fa-calendar-alt"></i> 05/12/2024</span>
          <span><i class="fas fa-user"></i> Chuyên gia dinh dưỡng</span>
          <span><i class="fas fa-eye"></i> 1,450 lượt xem</span>
        </div>
        <h1 class="article-title">8 tuần giảm cân khoa học</h1>
      </article>

      <img
        src="${pageContext.request.contextPath}/images/news/menu.jpg"
        alt="Thực đơn dinh dưỡng cho người tập gym"
        class="article-banner"
        loading="lazy"
      />

      <div class="article-content">
        <h2>Phương pháp giảm cân an toàn và hiệu quả</h2>
        <p>
          Chương trình "8 tuần giảm cân khoa học" tại GymFit được thiết kế dựa
          trên các nghiên cứu khoa học mới nhất về dinh dưỡng và tập luyện.
          Chúng tôi cam kết mang đến cho bạn một hành trình giảm cân an toàn,
          bền vững và hiệu quả.
        </p>

        <div class="timeline-image-container">
          <img
            src="${pageContext.request.contextPath}/images/news/menu2.webp"
            alt="Thực đơn dinh dưỡng chi tiết"
            class="timeline-image"
            loading="lazy"
          />
        </div>

        <h3>Nguyên lý khoa học của chương trình</h3>
        <p>Chương trình được xây dựng dựa trên 3 trụ cột chính:</p>
        <ul>
          <li>
            <strong>Tập luyện có khoa học:</strong> Kết hợp cardio và strength
            training để đốt cháy calo hiệu quả
          </li>
          <li>
            <strong>Dinh dưỡng cân bằng:</strong> Chế độ ăn phù hợp với từng
            giai đoạn giảm cân
          </li>
          <li>
            <strong>Theo dõi định kỳ:</strong> Đo InBody mỗi 2 tuần để đánh giá
            tiến độ chính xác
          </li>
        </ul>

        <h3>Chế độ tập luyện cá nhân hóa</h3>
        <p>
          Mỗi thành viên sẽ được thiết kế chương trình tập luyện riêng biệt dựa
          trên:
        </p>
        <ul>
          <li>Kết quả đo InBody chi tiết</li>
          <li>Tình trạng sức khỏe hiện tại</li>
          <li>Mục tiêu giảm cân cụ thể</li>
          <li>Thời gian có thể dành cho tập luyện</li>
          <li>Sở thích và khả năng thể chất</li>
        </ul>

        <h3>Chế độ dinh dưỡng khoa học</h3>
        <p>Chuyên gia dinh dưỡng của chúng tôi sẽ tư vấn chế độ ăn phù hợp:</p>
        <ul>
          <li>
            <strong>Tuần 1-2:</strong> Giảm dần calo, tập trung vào protein và
            chất xơ
          </li>
          <li>
            <strong>Tuần 3-4:</strong> Tối ưu hóa tỷ lệ macro (carb/protein/fat)
          </li>
          <li>
            <strong>Tuần 5-6:</strong> Điều chỉnh dựa trên kết quả đo InBody
          </li>
          <li><strong>Tuần 7-8:</strong> Xây dựng thói quen ăn uống lâu dài</li>
        </ul>

        <div class="highlight-box">
          <p>
            "Sau 8 tuần, tôi đã giảm được 8kg và giảm 5% mỡ cơ thể. Quan trọng
            nhất là tôi học được cách duy trì lối sống lành mạnh." - Anh Hùng,
            thành viên GymFit
          </p>
        </div>

        <h3>Công nghệ InBody tiên tiến</h3>
        <p>
          Máy đo InBody tại GymFit sử dụng công nghệ phân tích thành phần cơ thể
          tiên tiến nhất:
        </p>
        <ul>
          <li>Đo chính xác tỷ lệ mỡ, cơ, nước trong cơ thể</li>
          <li>Phân tích sự phân bố mỡ theo từng vùng</li>
          <li>Đánh giá khối lượng cơ bắp và mật độ xương</li>
          <li>Theo dõi sự thay đổi qua từng giai đoạn</li>
        </ul>

        <h3>Hỗ trợ từ đội ngũ chuyên gia</h3>
        <p>Tham gia chương trình, bạn sẽ được hỗ trợ bởi:</p>
        <ul>
          <li>
            <strong>Huấn luyện viên cá nhân:</strong> Hướng dẫn tập luyện an
            toàn và hiệu quả
          </li>
          <li>
            <strong>Chuyên gia dinh dưỡng:</strong> Tư vấn chế độ ăn phù hợp
          </li>
          <li><strong>Bác sĩ thể thao:</strong> Kiểm tra sức khỏe định kỳ</li>
          <li><strong>Nhóm hỗ trợ:</strong> Kết nối với các thành viên khác</li>
        </ul>

        <h3>Cam kết kết quả</h3>
        <p>Với sự tuân thủ đúng chương trình, chúng tôi cam kết:</p>
        <ul>
          <li>Giảm 5-10% trọng lượng cơ thể trong 8 tuần</li>
          <li>Giảm 3-7% tỷ lệ mỡ cơ thể</li>
          <li>Tăng cường sức mạnh và độ bền</li>
          <li>Cải thiện sức khỏe tổng thể</li>
          <li>Xây dựng thói quen sống lành mạnh</li>
        </ul>
      </div>

      <div class="cta-section">
        <h3>Bắt đầu hành trình giảm cân khoa học ngay hôm nay!</h3>
        <p>
          Đừng để cân nặng làm cản trở cuộc sống của bạn. Hãy tham gia chương
          trình 8 tuần giảm cân khoa học để có được vóc dáng mơ ước và sức khỏe
          tốt nhất.
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
                  'https://via.placeholder.com/1200x400/141a46/ffffff?text=Weight+Loss+Image';
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
