<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ include
file="/views/common/header.jsp" %>

<style>
  :root {
    --primary: #141a49;
    --accent: #ec8b5a;
    --support: #ffde59;
    --white: #ffffff;
    --text-dark: #333333;
    --text-light: #666666;
    --gray-bg: #f5f5f5;
    --shadow: rgba(0, 0, 0, 0.1);
    --shadow-hover: rgba(0, 0, 0, 0.15);
  }

  body {
    font-family: 'Inter', sans-serif;
    background: var(--gray-bg);
    color: var(--text-dark);
    line-height: 1.6;
  }

  .calendar-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 40px 20px;
  }

  .page-title {
    text-align: center;
    font-size: 2.5rem;
    font-weight: 800;
    color: var(--primary);
    margin-bottom: 10px;
  }

  .page-subtitle {
    text-align: center;
    font-size: 1.1rem;
    color: var(--text-light);
    margin-bottom: 40px;
  }

  .schedule-container {
    background: var(--primary);
    border-radius: 20px;
    padding: 40px;
    color: white;
    margin-bottom: 40px;
    box-shadow: 0 8px 30px rgba(20, 26, 73, 0.3);
  }

  .schedule-header {
    display: grid;
    grid-template-columns: 120px repeat(6, 1fr);
    gap: 2px;
    margin-bottom: 2px;
    text-align: center;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 10px 10px 0 0;
    overflow: hidden;
  }

  .schedule-header span {
    font-weight: 700;
    padding: 15px 10px;
    background: rgba(255, 222, 89, 0.9);
    font-size: 0.9rem;
    text-transform: uppercase;
    letter-spacing: 1px;
    color: var(--primary);
    text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
    border-bottom: 2px solid rgba(20, 26, 73, 0.1);
  }

  .schedule-header span:first-child {
    border-radius: 10px 0 0 0;
  }

  .schedule-header span:last-child {
    border-radius: 0 10px 0 0;
  }

  .schedule-grid {
    display: grid;
    grid-template-columns: 120px repeat(6, 1fr);
    gap: 2px;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 0 0 10px 10px;
    overflow: hidden;
  }

  .time-slot {
    padding: 15px 10px;
    background: rgba(255, 222, 89, 0.9);
    font-size: 0.9rem;
    font-weight: 700;
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    min-height: 80px;
    color: var(--primary);
    border-right: 2px solid rgba(20, 26, 73, 0.1);
    text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
  }

  .schedule-slot {
    background: rgba(255, 255, 255, 0.05);
    padding: 10px;
    min-height: 80px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    position: relative;
    border: 1px solid rgba(255, 255, 255, 0.1);
    transition: all 0.3s ease;
    cursor: pointer;
  }

  .schedule-slot:hover {
    transform: translateY(-3px);
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
    filter: brightness(1.1);
    border-color: rgba(255, 255, 255, 0.2);
  }

  /* Cardio classes */
  .schedule-slot[class*='cardio'] {
    background: rgba(236, 139, 90, 0.8);
  }

  /* Strength classes */
  .schedule-slot[class*='strength'] {
    background: rgba(255, 255, 255, 0.2);
  }

  /* Yoga/Stretching classes */
  .schedule-slot[class*='yoga'],
  .schedule-slot[class*='stretching'] {
    background: rgba(255, 222, 89, 0.4);
  }

  /* HIIT/CrossFit classes */
  .schedule-slot[class*='hiit'],
  .schedule-slot[class*='crossfit'] {
    background: rgba(236, 139, 90, 0.4);
  }

  /* Other classes */
  .schedule-slot[class*='zumba'],
  .schedule-slot[class*='dance'] {
    background: rgba(236, 139, 90, 0.8); /* Cardio */
  }

  .schedule-slot[class*='boxing'] {
    background: rgba(255, 255, 255, 0.2); /* Strength */
  }

  .schedule-slot[class*='pilates'] {
    background: rgba(255, 222, 89, 0.4); /* Yoga/Stretching */
  }

  .schedule-slot[class*='teen'],
  .schedule-slot[class*='family'],
  .schedule-slot[class*='open'] {
    background: rgba(255, 255, 255, 0.15);
  }

  .class-name {
    font-weight: 600;
    font-size: 0.9rem;
    margin-bottom: 5px;
    color: white;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
  }

  .trainer-name {
    font-size: 0.8rem;
    color: rgba(255, 255, 255, 0.9);
    text-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
  }

  .legend {
    display: flex;
    gap: 20px;
    margin-top: 30px;
    justify-content: center;
    flex-wrap: wrap;
    padding: 15px;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 10px;
  }

  .legend-item {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 0.9rem;
    color: rgba(255, 255, 255, 0.9);
    padding: 5px 10px;
    border-radius: 5px;
    cursor: pointer;
    transition: all 0.3s ease;
  }

  .legend-item:hover {
    background: rgba(255, 255, 255, 0.1);
    transform: translateY(-2px);
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
  }

  .legend-color {
    width: 20px;
    height: 20px;
    border-radius: 4px;
  }

  .legend-color.cardio {
    background: rgba(236, 139, 90, 0.8);
  }

  .legend-color.strength {
    background: rgba(255, 255, 255, 0.2);
  }

  .legend-color.yoga {
    background: rgba(255, 222, 89, 0.4);
  }

  .legend-color.hiit {
    background: rgba(236, 139, 90, 0.4);
  }

  .cta-section {
    text-align: center;
    background: var(--white);
    border-radius: 20px;
    padding: 40px;
    box-shadow: 0 8px 25px var(--shadow);
  }

  .cta-title {
    font-size: 1.8rem;
    font-weight: 700;
    color: var(--primary);
    margin-bottom: 20px;
  }

  .cta-buttons {
    display: flex;
    gap: 20px;
    justify-content: center;
    flex-wrap: wrap;
  }

  .btn-primary {
    background: var(--accent);
    color: var(--white);
    padding: 15px 30px;
    border-radius: 25px;
    font-weight: 600;
    text-decoration: none;
    transition: all 0.3s ease;
    border: none;
    cursor: pointer;
  }

  .btn-primary:hover {
    background: #d67a4f;
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(236, 139, 90, 0.4);
  }

  .btn-secondary {
    background: var(--support);
    color: var(--text-dark);
    padding: 15px 30px;
    border-radius: 25px;
    font-weight: 600;
    text-decoration: none;
    transition: all 0.3s ease;
    border: none;
    cursor: pointer;
  }

  .btn-secondary:hover {
    background: #f4d03f;
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(255, 222, 89, 0.4);
  }

  @media (max-width: 768px) {
    .calendar-container {
      padding: 20px 15px;
    }

    .page-title {
      font-size: 2rem;
    }

    .calendar-grid {
      grid-template-columns: 1fr;
      gap: 20px;
    }

    .cta-buttons {
      flex-direction: column;
      align-items: center;
    }
  }
</style>

<div class="calendar-container">
  <h1 class="page-title">Lịch Tập Luyện</h1>
  <p class="page-subtitle">
    Khám phá các lớp học và lịch trình tập luyện tại GymFit
  </p>

  <div class="schedule-container">
    <div class="schedule-header">
      <span>Thời gian</span>
      <span>Thứ Hai</span>
      <span>Thứ Ba</span>
      <span>Thứ Tư</span>
      <span>Thứ Năm</span>
      <span>Thứ Sáu</span>
      <span>Thứ Bảy</span>
    </div>

    <div class="schedule-grid">
      <!-- 5:45AM - 6:30AM -->
      <div class="time-slot">5:45AM - 6:30AM</div>
      <div class="schedule-slot yoga">
        <div class="class-name">Yoga Buổi Sáng</div>
        <div class="trainer-name">HLV: Minh</div>
      </div>
      <div class="schedule-slot yoga">
        <div class="class-name">Pilates</div>
        <div class="trainer-name">HLV: Hương</div>
      </div>
      <div class="schedule-slot yoga">
        <div class="class-name">Yoga Buổi Sáng</div>
        <div class="trainer-name">HLV: Minh</div>
      </div>
      <div class="schedule-slot yoga">
        <div class="class-name">Pilates</div>
        <div class="trainer-name">HLV: Hương</div>
      </div>
      <div class="schedule-slot yoga">
        <div class="class-name">Yoga Buổi Sáng</div>
        <div class="trainer-name">HLV: Minh</div>
      </div>
      <div class="schedule-slot yoga">
        <div class="class-name">Stretching</div>
        <div class="trainer-name">HLV: Mai</div>
      </div>

      <!-- 9:30AM - 10:30AM -->
      <div class="time-slot">9:30AM - 10:30AM</div>
      <div class="schedule-slot cardio">
        <div class="class-name">HIIT Cardio</div>
        <div class="trainer-name">HLV: Lan</div>
      </div>
      <div class="schedule-slot strength">
        <div class="class-name">Strength Training</div>
        <div class="trainer-name">HLV: Tuấn</div>
      </div>
      <div class="schedule-slot strength">
        <div class="class-name">Boxing</div>
        <div class="trainer-name">HLV: Nam</div>
      </div>
      <div class="schedule-slot hiit">
        <div class="class-name">CrossFit</div>
        <div class="trainer-name">HLV: Đức</div>
      </div>
      <div class="schedule-slot cardio">
        <div class="class-name">HIIT Cardio</div>
        <div class="trainer-name">HLV: Lan</div>
      </div>
      <div class="schedule-slot cardio">
        <div class="class-name">Family Fitness</div>
        <div class="trainer-name">HLV: Mai</div>
      </div>

      <!-- 17:30 - 18:30 -->
      <div class="time-slot">17:30 - 18:30</div>
      <div class="schedule-slot cardio">
        <div class="class-name">Zumba Dance</div>
        <div class="trainer-name">HLV: Mai</div>
      </div>
      <div class="schedule-slot strength">
        <div class="class-name">Boxing</div>
        <div class="trainer-name">HLV: Nam</div>
      </div>
      <div class="schedule-slot cardio">
        <div class="class-name">HIIT Cardio</div>
        <div class="trainer-name">HLV: Lan</div>
      </div>
      <div class="schedule-slot strength">
        <div class="class-name">Strength Training</div>
        <div class="trainer-name">HLV: Tuấn</div>
      </div>
      <div class="schedule-slot hiit">
        <div class="class-name">CrossFit</div>
        <div class="trainer-name">HLV: Đức</div>
      </div>
      <div class="schedule-slot cardio">
        <div class="class-name">Teen Training</div>
        <div class="trainer-name">HLV: Linh</div>
      </div>

      <!-- 19:00 - 20:00 -->
      <div class="time-slot">19:00 - 20:00</div>
      <div class="schedule-slot hiit">
        <div class="class-name">CrossFit</div>
        <div class="trainer-name">HLV: Đức</div>
      </div>
      <div class="schedule-slot cardio">
        <div class="class-name">HIIT Cardio</div>
        <div class="trainer-name">HLV: Lan</div>
      </div>
      <div class="schedule-slot strength">
        <div class="class-name">Strength Training</div>
        <div class="trainer-name">HLV: Tuấn</div>
      </div>
      <div class="schedule-slot cardio">
        <div class="class-name">Zumba Dance</div>
        <div class="trainer-name">HLV: Mai</div>
      </div>
      <div class="schedule-slot strength">
        <div class="class-name">Boxing</div>
        <div class="trainer-name">HLV: Nam</div>
      </div>
      <div class="schedule-slot">
        <div class="class-name">Open Gym</div>
        <div class="trainer-name">HLV: Tất cả</div>
      </div>
    </div>

    <div class="legend">
      <div class="legend-item">
        <div class="legend-color cardio"></div>
        <span>Cardio</span>
      </div>
      <div class="legend-item">
        <div class="legend-color strength"></div>
        <span>Strength</span>
      </div>
      <div class="legend-item">
        <div class="legend-color yoga"></div>
        <span>Yoga/Stretching</span>
      </div>
      <div class="legend-item">
        <div class="legend-color hiit"></div>
        <span>HIIT/CrossFit</span>
      </div>
    </div>
  </div>

  <!-- CTA Section -->
  <div class="cta-section">
    <h2 class="cta-title">Đăng Ký Lớp Học Ngay!</h2>
    <p style="color: var(--text-light); margin-bottom: 30px">
      Tham gia các lớp học đa dạng và đạt được mục tiêu fitness của bạn
    </p>
    <div class="cta-buttons">
      <a
        href="${pageContext.request.contextPath}/views/register.jsp"
        class="btn-primary"
      >
        <i class="fas fa-user-plus"></i> ĐĂNG KÝ NGAY
      </a>
      <a
        href="${pageContext.request.contextPath}/views/Service_page/services_main.jsp"
        class="btn-secondary"
      >
        <i class="fas fa-info-circle"></i> TÌM HIỂU DỊCH VỤ
      </a>
    </div>
  </div>
</div>

<!-- Floating Buttons -->
<div class="floating-buttons">
  <button
    class="floating-btn tu-van"
    onclick="alert('Chức năng tư vấn sẽ được triển khai sớm!')"
  >
    <i class="fas fa-user-tie"></i> TƯ VẤN
  </button>
  <button
    class="floating-btn chat-bot"
    onclick="alert('Chức năng chat bot đang được phát triển!')"
  >
    <i class="fas fa-comments"></i> CHAT BOT
  </button>
</div>

<script>
  // Add floating buttons CSS
  const link = document.createElement('link');
  link.rel = 'stylesheet';
  link.href = '${pageContext.request.contextPath}/css/floating-buttons.css';
  document.head.appendChild(link);

  // Add click animation to buttons
  const buttons = document.querySelectorAll('.btn-primary, .btn-secondary');
  buttons.forEach((button) => {
    button.addEventListener('click', function (e) {
      // Add ripple effect
      const ripple = document.createElement('span');
      const rect = this.getBoundingClientRect();
      const size = Math.max(rect.width, rect.height);
      const x = e.clientX - rect.left - size / 2;
      const y = e.clientY - rect.top - size / 2;

      ripple.style.width = ripple.style.height = size + 'px';
      ripple.style.left = x + 'px';
      ripple.style.top = y + 'px';
      ripple.style.position = 'absolute';
      ripple.style.borderRadius = '50%';
      ripple.style.background = 'rgba(255, 255, 255, 0.3)';
      ripple.style.transform = 'scale(0)';
      ripple.style.animation = 'ripple 0.6s linear';
      ripple.style.pointerEvents = 'none';

      this.style.position = 'relative';
      this.style.overflow = 'hidden';
      this.appendChild(ripple);

      setTimeout(() => {
        ripple.remove();
      }, 600);
    });
  });

  // Add CSS for ripple animation
  const style = document.createElement('style');
  style.textContent = `
    @keyframes ripple {
      to {
        transform: scale(4);
        opacity: 0;
      }
    }
  `;
  document.head.appendChild(style);
</script>

<%@ include file="/views/common/footer.jsp" %>
