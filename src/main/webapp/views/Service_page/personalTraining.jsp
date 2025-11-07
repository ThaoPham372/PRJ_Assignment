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
    position: relative;
    color: #fff;
    padding: 120px 50px;
    text-align: center;
    margin-bottom: 60px;
    border-radius: 20px;
    overflow: hidden;
    min-height: 500px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  }

  .hero-section::before {
    content: '';
    position: absolute;
    top: -10%;
    left: -10%;
    right: -10%;
    bottom: -10%;
    background: url('${pageContext.request.contextPath}/images/service/personalTrain.jpg');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    filter: brightness(1) contrast(1.1) saturate(1.1);
    z-index: 1;
    transform: scale(1.1);
    transition: all 0.3s ease;
  }

  .hero-section::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(
      135deg,
      rgba(20, 26, 73, 0.7) 0%,
      rgba(20, 26, 73, 0.6) 100%
    );
    z-index: 2;
  }

  .hero-section:hover::before {
    transform: scale(1.15);
    filter: brightness(1.1) contrast(1.15) saturate(1.15);
  }

  .hero-content {
    position: relative;
    z-index: 3;
    width: 100%;
    max-width: 1000px;
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

  .content-section {
    background: var(--card);
    padding: 50px;
    border-radius: 20px;
    box-shadow: 0 8px 30px var(--shadow);
    margin-bottom: 40px;
  }

  .content-section h2 {
    color: var(--primary);
    font-size: 2rem;
    font-weight: 800;
    margin-bottom: 30px;
  }

  .content-section h3 {
    color: var(--primary);
    font-size: 1.5rem;
    font-weight: 700;
    margin: 30px 0 15px;
  }

  .content-section p {
    color: var(--text);
    font-size: 1.1rem;
    line-height: 1.7;
    margin-bottom: 20px;
  }

  .content-section ul {
    margin: 20px 0;
    padding-left: 30px;
  }

  .content-section li {
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

    .content-section {
      padding: 30px;
    }

    .content-section h2 {
      font-size: 1.5rem;
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
    <div class="hero-content">
      <h1>PERSONAL TRAINING</h1>
      <p>
        Huấn luyện cá nhân 1-1 với HLV chuyên nghiệp, chương trình tập luyện
        được thiết kế riêng cho từng cá nhân để đạt được mục tiêu fitness hiệu
        quả nhất.
      </p>
    </div>
  </section>

  <div class="content-section">
    <h2>Chương trình Personal Training tại GymFit</h2>

    <p>
      Personal Training là dịch vụ huấn luyện cá nhân cao cấp nhất tại GymFit,
      được thiết kế dành riêng cho những ai muốn đạt được kết quả tập luyện tối
      ưu trong thời gian ngắn nhất.
    </p>

    <h3>Lợi ích của Personal Training</h3>
    <ul>
      <li>
        <strong>Chương trình cá nhân hóa:</strong> Được thiết kế dựa trên mục
        tiêu, thể trạng và khả năng của từng cá nhân
      </li>
      <li>
        <strong>Huấn luyện viên chuyên nghiệp:</strong> HLV có chứng chỉ quốc tế
        và kinh nghiệm trên 5 năm
      </li>
      <li>
        <strong>Tiến độ nhanh:</strong> Đạt được kết quả nhanh hơn 3-5 lần so
        với tập luyện thông thường
      </li>
      <li>
        <strong>An toàn tuyệt đối:</strong> Được giám sát và điều chỉnh kỹ thuật
        trong suốt quá trình tập
      </li>
      <li>
        <strong>Dinh dưỡng cá nhân:</strong> Tư vấn chế độ ăn phù hợp với mục
        tiêu tập luyện
      </li>
    </ul>

    <h3>Đối tượng phù hợp</h3>
    <ul>
      <li>Người mới bắt đầu tập gym</li>
      <li>Người muốn giảm cân nhanh chóng</li>
      <li>Vận động viên cần tăng cường sức mạnh</li>
      <li>Người cao tuổi cần tập luyện an toàn</li>
      <li>Người có chấn thương cần phục hồi</li>
    </ul>

    <div class="highlight-box">
      <p>
        "Sau 3 tháng Personal Training tại GymFit, tôi đã giảm được 15kg và có
        được body săn chắc như mong muốn. HLV rất chuyên nghiệp và tận tâm!" -
        Chị Minh, thành viên GymFit
      </p>
    </div>

    <h3>Quy trình Personal Training</h3>
    <ul>
      <li><strong>Bước 1:</strong> Đánh giá thể trạng và đo InBody chi tiết</li>
      <li>
        <strong>Bước 2:</strong> Xác định mục tiêu và lập kế hoạch tập luyện
      </li>
      <li><strong>Bước 3:</strong> Bắt đầu chương trình tập luyện cá nhân</li>
      <li><strong>Bước 4:</strong> Theo dõi và điều chỉnh định kỳ</li>
      <li><strong>Bước 5:</strong> Đánh giá kết quả và duy trì thành tích</li>
    </ul>

    <h3>Gói Personal Training</h3>
    <ul>
      <li>
        <strong>Gói 8 buổi:</strong> 2,400,000đ - Phù hợp cho người mới bắt đầu
      </li>
      <li><strong>Gói 16 buổi:</strong> 4,200,000đ - Tiết kiệm 15%</li>
      <li><strong>Gói 24 buổi:</strong> 5,800,000đ - Tiết kiệm 25%</li>
      <li>
        <strong>Gói VIP:</strong> 8,500,000đ - Bao gồm dinh dưỡng và massage
      </li>
    </ul>
  </div>

  <div class="cta-section">
    <h3>Bắt đầu hành trình Personal Training ngay hôm nay!</h3>
    <p>
      Đăng ký ngay để được tư vấn miễn phí và nhận ưu đãi đặc biệt cho thành
      viên mới.
    </p>
    <a
      href="${pageContext.request.contextPath}/register"
      class="cta-btn"
      >ĐĂNG KÝ NGAY</a
    >
  </div>
</main>

<%@ include file="/views/common/footer.jsp" %>
