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
    background: url('${pageContext.request.contextPath}/images/service/group.jpg');
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
      <h1>GROUP TRAINING</h1>
      <p>
        Lớp tập nhóm tạo động lực và gắn kết, giúp bạn có thêm bạn bè và động
        lực tập luyện mỗi ngày trong môi trường thân thiện và chuyên nghiệp.
      </p>
    </div>
  </section>

  <div class="content-section">
    <h2>Lớp tập nhóm tại GymFit</h2>

    <p>
      Group Training là hình thức tập luyện nhóm tại GymFit, mang đến trải
      nghiệm tập luyện vui vẻ, động lực cao và chi phí hợp lý cho mọi thành
      viên.
    </p>

    <h3>Đặc điểm của Group Training</h3>
    <ul>
      <li>
        <strong>Số lượng thành viên:</strong> 8-15 người/lớp để đảm bảo chất
        lượng
      </li>
      <li>
        <strong>Thời gian:</strong> 45-60 phút/lớp, phù hợp với lịch trình bận
        rộn
      </li>
      <li><strong>Tương tác:</strong> Tạo động lực và cạnh tranh lành mạnh</li>
      <li><strong>Hiệu quả:</strong> Tập luyện có hệ thống và khoa học</li>
      <li><strong>Chi phí:</strong> Tiết kiệm hơn Personal Training</li>
    </ul>

    <h3>Loại hình Group Training</h3>
    <ul>
      <li>
        <strong>HIIT (High-Intensity Interval Training):</strong> Đốt cháy calo
        tối đa
      </li>
      <li><strong>CrossFit:</strong> Tăng cường sức mạnh toàn diện</li>
      <li><strong>Yoga:</strong> Tăng cường sự dẻo dai và thư giãn</li>
      <li><strong>Dance Fitness:</strong> Vừa tập luyện vừa giải trí</li>
      <li><strong>Boxing:</strong> Tăng cường sức mạnh và phản xạ</li>
      <li><strong>Pilates:</strong> Tăng cường cơ bụng và cải thiện tư thế</li>
    </ul>

    <div class="highlight-box">
      <p>
        "Tập Group Training tại GymFit giúp tôi có thêm nhiều bạn bè và động lực
        tập luyện. Mỗi buổi tập đều vui vẻ và hiệu quả!" - Anh Nam, thành viên
        GymFit
      </p>
    </div>

    <h3>Lợi ích của Group Training</h3>
    <ul>
      <li><strong>Động lực cao:</strong> Tập cùng nhiều người tạo động lực</li>
      <li><strong>Chi phí hợp lý:</strong> Chia sẻ chi phí huấn luyện viên</li>
      <li><strong>Kết bạn:</strong> Mở rộng mối quan hệ xã hội</li>
      <li><strong>Lịch trình cố định:</strong> Dễ dàng sắp xếp thời gian</li>
      <li><strong>Đa dạng:</strong> Nhiều loại hình tập luyện</li>
    </ul>

    <h3>Lịch tập Group Training</h3>
    <ul>
      <li><strong>Thứ 2,4,6:</strong> HIIT (18:00-19:00)</li>
      <li><strong>Thứ 3,5,7:</strong> Yoga (19:00-20:00)</li>
      <li><strong>Thứ 2,6:</strong> Boxing (20:00-21:00)</li>
      <li><strong>Thứ 4,7:</strong> Dance Fitness (18:30-19:30)</li>
      <li><strong>Thứ 3,5:</strong> Pilates (18:00-19:00)</li>
    </ul>

    <h3>Gói Group Training</h3>
    <ul>
      <li><strong>Gói 1 tháng:</strong> 800,000đ - Không giới hạn lớp</li>
      <li><strong>Gói 3 tháng:</strong> 2,100,000đ - Tiết kiệm 12%</li>
      <li><strong>Gói 6 tháng:</strong> 3,800,000đ - Tiết kiệm 20%</li>
      <li><strong>Gói 12 tháng:</strong> 6,500,000đ - Tiết kiệm 32%</li>
    </ul>
  </div>

  <div class="cta-section">
    <h3>Tham gia Group Training ngay hôm nay!</h3>
    <p>
      Đăng ký ngay để được trải nghiệm lớp tập nhóm miễn phí và nhận ưu đãi đặc
      biệt.
    </p>
    <a
      href="${pageContext.request.contextPath}/register"
      class="cta-btn"
      >ĐĂNG KÝ NGAY</a
    >
  </div>
</main>

<%@ include file="/views/common/footer.jsp" %>
