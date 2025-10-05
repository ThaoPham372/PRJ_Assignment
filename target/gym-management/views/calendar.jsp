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

  .calendar-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 30px;
    margin-bottom: 40px;
  }

  .calendar-card {
    background: var(--white);
    border-radius: 20px;
    padding: 30px;
    box-shadow: 0 8px 25px var(--shadow);
    transition: all 0.3s ease;
  }

  .calendar-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 35px var(--shadow-hover);
  }

  .calendar-card h3 {
    color: var(--primary);
    font-size: 1.5rem;
    font-weight: 700;
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .calendar-card h3 i {
    color: var(--accent);
  }

  .schedule-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 0;
    border-bottom: 1px solid #eee;
  }

  .schedule-item:last-child {
    border-bottom: none;
  }

  .schedule-time {
    font-weight: 600;
    color: var(--accent);
    font-size: 1rem;
  }

  .schedule-class {
    font-weight: 500;
    color: var(--text-dark);
  }

  .schedule-trainer {
    font-size: 0.9rem;
    color: var(--text-light);
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

  <div class="calendar-grid">
    <!-- Thứ 2 -->
    <div class="calendar-card">
      <h3><i class="fas fa-calendar-day"></i>Thứ Hai</h3>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">06:00 - 07:00</div>
          <div class="schedule-class">Yoga Buổi Sáng</div>
          <div class="schedule-trainer">HLV: Minh</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">18:00 - 19:00</div>
          <div class="schedule-class">HIIT Cardio</div>
          <div class="schedule-trainer">HLV: Lan</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">19:30 - 20:30</div>
          <div class="schedule-class">Strength Training</div>
          <div class="schedule-trainer">HLV: Tuấn</div>
        </div>
      </div>
    </div>

    <!-- Thứ 3 -->
    <div class="calendar-card">
      <h3><i class="fas fa-calendar-day"></i>Thứ Ba</h3>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">06:30 - 07:30</div>
          <div class="schedule-class">Pilates</div>
          <div class="schedule-trainer">HLV: Hương</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">17:30 - 18:30</div>
          <div class="schedule-class">Zumba Dance</div>
          <div class="schedule-trainer">HLV: Mai</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">19:00 - 20:00</div>
          <div class="schedule-class">CrossFit</div>
          <div class="schedule-trainer">HLV: Đức</div>
        </div>
      </div>
    </div>

    <!-- Thứ 4 -->
    <div class="calendar-card">
      <h3><i class="fas fa-calendar-day"></i>Thứ Tư</h3>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">06:00 - 07:00</div>
          <div class="schedule-class">Yoga Buổi Sáng</div>
          <div class="schedule-trainer">HLV: Minh</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">18:00 - 19:00</div>
          <div class="schedule-class">Boxing</div>
          <div class="schedule-trainer">HLV: Nam</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">19:30 - 20:30</div>
          <div class="schedule-class">Functional Training</div>
          <div class="schedule-trainer">HLV: Linh</div>
        </div>
      </div>
    </div>

    <!-- Thứ 5 -->
    <div class="calendar-card">
      <h3><i class="fas fa-calendar-day"></i>Thứ Năm</h3>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">06:30 - 07:30</div>
          <div class="schedule-class">Pilates</div>
          <div class="schedule-trainer">HLV: Hương</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">17:30 - 18:30</div>
          <div class="schedule-class">Aerobics</div>
          <div class="schedule-trainer">HLV: Mai</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">19:00 - 20:00</div>
          <div class="schedule-class">Weight Training</div>
          <div class="schedule-trainer">HLV: Đức</div>
        </div>
      </div>
    </div>

    <!-- Thứ 6 -->
    <div class="calendar-card">
      <h3><i class="fas fa-calendar-day"></i>Thứ Sáu</h3>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">06:00 - 07:00</div>
          <div class="schedule-class">Yoga Buổi Sáng</div>
          <div class="schedule-trainer">HLV: Minh</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">18:00 - 19:00</div>
          <div class="schedule-class">HIIT Cardio</div>
          <div class="schedule-trainer">HLV: Lan</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">19:30 - 20:30</div>
          <div class="schedule-class">Strength Training</div>
          <div class="schedule-trainer">HLV: Tuấn</div>
        </div>
      </div>
    </div>

    <!-- Thứ 7 -->
    <div class="calendar-card">
      <h3><i class="fas fa-calendar-day"></i>Thứ Bảy</h3>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">08:00 - 09:00</div>
          <div class="schedule-class">Yoga Weekend</div>
          <div class="schedule-trainer">HLV: Minh</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">09:30 - 10:30</div>
          <div class="schedule-class">Family Fitness</div>
          <div class="schedule-trainer">HLV: Mai</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">16:00 - 17:00</div>
          <div class="schedule-class">Teen Training</div>
          <div class="schedule-trainer">HLV: Linh</div>
        </div>
      </div>
    </div>

    <!-- Chủ Nhật -->
    <div class="calendar-card">
      <h3><i class="fas fa-calendar-day"></i>Chủ Nhật</h3>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">08:00 - 09:00</div>
          <div class="schedule-class">Yoga Weekend</div>
          <div class="schedule-trainer">HLV: Minh</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">09:30 - 10:30</div>
          <div class="schedule-class">Stretching & Recovery</div>
          <div class="schedule-trainer">HLV: Hương</div>
        </div>
      </div>
      <div class="schedule-item">
        <div>
          <div class="schedule-time">16:00 - 17:00</div>
          <div class="schedule-class">Open Gym</div>
          <div class="schedule-trainer">HLV: Tất cả</div>
        </div>
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

<script>
  document.addEventListener('DOMContentLoaded', function () {
    // Add hover effects to schedule items
    const scheduleItems = document.querySelectorAll('.schedule-item');
    scheduleItems.forEach((item) => {
      item.addEventListener('mouseenter', function () {
        this.style.backgroundColor = 'rgba(236, 139, 90, 0.1)';
        this.style.borderRadius = '10px';
        this.style.padding = '15px 10px';
      });

      item.addEventListener('mouseleave', function () {
        this.style.backgroundColor = 'transparent';
        this.style.borderRadius = '0';
        this.style.padding = '15px 0';
      });
    });

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
  });
</script>

<%@ include file="/views/common/footer.jsp" %>
