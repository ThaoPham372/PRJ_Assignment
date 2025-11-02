<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ include
file="/views/common/header.jsp" %>

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

  .schedule-image-container {
    margin: 30px 0;
    text-align: center;
  }

  .schedule-image {
    width: 100%;
    max-width: 600px;
    height: auto;
    border-radius: 15px;
    box-shadow: 0 8px 25px var(--shadow);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
  }

  .schedule-image:hover {
    transform: scale(1.02);
    box-shadow: 0 12px 35px var(--shadow-hover);
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

  /* RESPONSIVE */
  @media (max-width: 768px) {
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
      <span><i class="fas fa-calendar-alt"></i> 10/12/2024</span>
      <span><i class="fas fa-user"></i> HLV Yoga</span>
      <span><i class="fas fa-eye"></i> 890 lượt xem</span>
    </div>
    <h1 class="article-title">Yoga buổi sáng 6:00</h1>
  </article>

  <img
    src="${pageContext.request.contextPath}/images/news/yoga1.jpeg"
    alt="Yoga buổi sáng tại GymFit"
    class="article-banner"
    loading="lazy"
  />

  <div class="article-content">
    <h2>Khởi đầu ngày mới với năng lượng tích cực</h2>
    <p>
      Yoga buổi sáng tại GymFit không chỉ là một bài tập thể chất mà còn là cách
      tuyệt vời để bắt đầu ngày mới với tinh thần thoải mái và năng lượng tích
      cực. Lớp học được thiết kế đặc biệt để phù hợp với nhịp sinh học buổi sáng
      của cơ thể.
    </p>

    <div class="schedule-image-container">
      <img
        src="${pageContext.request.contextPath}/images/news/yoga2.jpeg"
        alt="Lịch học Yoga buổi sáng"
        class="schedule-image"
        loading="lazy"
      />
    </div>

    <h3>Lợi ích của Yoga buổi sáng</h3>
    <p>
      Tập yoga vào buổi sáng mang lại nhiều lợi ích tuyệt vời cho cả thể chất và
      tinh thần:
    </p>
    <ul>
      <li>
        <strong>Tăng cường năng lượng:</strong> Kích hoạt hệ tuần hoàn và trao
        đổi chất
      </li>
      <li>
        <strong>Cải thiện tư thế:</strong> Căn chỉnh cột sống và tăng cường cơ
        lưng
      </li>
      <li>
        <strong>Giảm căng thẳng:</strong> Thư giãn tinh thần và giảm stress
      </li>
      <li>
        <strong>Tăng cường tập trung:</strong> Cải thiện khả năng tập trung
        trong công việc
      </li>
      <li>
        <strong>Hỗ trợ tiêu hóa:</strong> Kích thích hệ tiêu hóa hoạt động tốt
        hơn
      </li>
    </ul>

    <h3>Chương trình tập luyện</h3>
    <p>
      Lớp Yoga buổi sáng tại GymFit được thiết kế với các bài tập nhẹ nhàng
      nhưng hiệu quả:
    </p>
    <ul>
      <li>
        <strong>Khởi động (10 phút):</strong> Các động tác thở và giãn cơ nhẹ
      </li>
      <li>
        <strong>Asana cơ bản (35 phút):</strong> Các tư thế yoga căn bản phù hợp
        với buổi sáng
      </li>
      <li><strong>Thư giãn (10 phút):</strong> Savasana và thiền định ngắn</li>
      <li><strong>Kết thúc (5 phút):</strong> Chia sẻ và tư vấn dinh dưỡng</li>
    </ul>

    <div class="highlight-box">
      <p>
        "Yoga buổi sáng giúp tôi bắt đầu ngày mới với tinh thần tích cực và cơ
        thể khỏe mạnh. Sau 2 tháng tập luyện, tôi cảm thấy năng lượng và sự tập
        trung được cải thiện đáng kể." - Chị Minh, thành viên GymFit
      </p>
    </div>

    <h3>Đối tượng phù hợp</h3>
    <p>
      Lớp Yoga buổi sáng phù hợp với mọi đối tượng, từ người mới bắt đầu đến
      những người đã có kinh nghiệm tập yoga. Huấn luyện viên sẽ điều chỉnh bài
      tập phù hợp với từng trình độ.
    </p>

    <h3>Chuẩn bị cho buổi tập</h3>
    <p>Để có buổi tập hiệu quả nhất, bạn nên:</p>
    <ul>
      <li>Thức dậy sớm 30 phút để cơ thể tỉnh táo</li>
      <li>Uống một ly nước ấm trước khi tập</li>
      <li>Mang theo thảm yoga và khăn lau mồ hôi</li>
      <li>Mặc quần áo thoải mái, co giãn tốt</li>
      <li>Không ăn sáng trước khi tập</li>
    </ul>

    <h3>Hướng dẫn đăng ký</h3>
    <p>
      Để tham gia lớp Yoga buổi sáng, bạn có thể đăng ký trực tiếp tại phòng tập
      hoặc qua ứng dụng GymFit. Lớp học có giới hạn số lượng để đảm bảo chất
      lượng giảng dạy tốt nhất.
    </p>
  </div>

  <div class="cta-section">
    <h3>Đăng ký lớp Yoga buổi sáng ngay hôm nay!</h3>
    <p>
      Trải nghiệm sự khác biệt của việc bắt đầu ngày mới với Yoga. Hãy tham gia
      cùng chúng tôi để cảm nhận năng lượng tích cực và sự cân bằng trong cuộc
      sống.
    </p>
    <a href="${pageContext.request.contextPath}/register" class="btn"
      >ĐĂNG KÝ NGAY</a
    >
  </div>
</main>

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
              'https://via.placeholder.com/1200x400/141a46/ffffff?text=Yoga+Image';
            img.style.opacity = '0.7';
          }
        },
        { once: true },
      );
    });
  }
  attachImageFallback();
</script>

<%@ include file="/views/common/footer.jsp" %>
