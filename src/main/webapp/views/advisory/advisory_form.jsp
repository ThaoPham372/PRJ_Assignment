<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <%@ include
file="/views/common/header.jsp" %>

<style>
  :root {
    --primary: #141a49;
    --accent: #ec8b5a;
    --text-light: #f5f5dc;
    --input-bg: #f5f5dc;
  }

  /* Body styling với background ảnh chiếm toàn màn hình */
  body {
    background-image: url('${pageContext.request.contextPath}/images/home/backrough.jpg');
    background-size: cover;
    background-position: center center;
    background-repeat: no-repeat;
    background-attachment: fixed;
    min-height: 100vh;
    margin: 0;
    padding: 0;
    position: relative;
    overflow-x: hidden;
  }

  /* Overlay mờ để form nổi bật hơn */
  body::before {
    content: '';
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(20, 26, 73, 0.75); /* Overlay màu xanh đậm với độ mờ 75% */
    z-index: 0;
    pointer-events: none;
  }

  /* Ẩn header và footer trên trang advisory để form chiếm toàn màn hình */
  header {
    display: none;
  }

  footer {
    display: none;
  }

  /* Main container căn giữa hoàn toàn */
  main {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 20px;
    z-index: 1;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch; /* Smooth scroll trên iOS */
  }

  /* Container chứa form */
  .advisory-container {
    width: 100%;
    max-width: 600px;
    margin: auto;
    position: relative;
    z-index: 2;
    animation: fadeInUp 0.6s ease-out;
  }

  /* Animation fade-in cho form */
  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(30px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  /* Header với logo và chữ GYMFIT */
  .advisory-header {
    text-align: center;
    margin-bottom: 40px;
  }

  .advisory-header .logo-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 15px;
  }

  .advisory-header img {
    height: 240px; /* Tăng gấp 3 lần từ 80px */
    width: auto;
    max-width: 600px; /* Tăng gấp 3 lần từ 200px */
    object-fit: contain;
  }

  .advisory-header h1 {
    font-size: 3rem;
    font-weight: 900;
    color: #ffffff;
    margin: 0;
    letter-spacing: 2px;
    text-transform: uppercase;
    font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,
      sans-serif;
  }

  /* Form container */
  .form-container {
    background-color: var(--primary);
    border-radius: 15px;
    padding: 40px;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.6);
    position: relative;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(236, 139, 90, 0.2);
  }

  /* Container chứa form wrapper để có thể đặt nút back */
  .form-wrapper {
    position: relative;
  }

  /* Form group - mỗi cặp label + input */
  .form-group {
    margin-bottom: 25px;
  }

  /* Labels - màu cam, in hoa, đậm */
  .form-group label {
    display: block;
    color: var(--accent);
    font-size: 0.95rem;
    font-weight: 700;
    margin-bottom: 8px;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,
      sans-serif;
  }

  /* Input fields - màu be/kem nhạt, bo góc, không viền */
  .form-group input {
    width: 100%;
    padding: 14px 18px;
    background-color: var(--input-bg);
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    color: #333;
    font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,
      sans-serif;
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
  }

  .form-group input:focus {
    outline: none;
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1),
      0 0 0 3px rgba(236, 139, 90, 0.2);
    background-color: #fffef8;
  }

  /* Submit button - màu cam nổi bật */
  .submit-btn {
    width: 100%;
    padding: 16px;
    background-color: var(--accent);
    color: #ffffff;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 1px;
    cursor: pointer;
    transition: all 0.3s ease;
    box-shadow: 0 4px 15px rgba(236, 139, 90, 0.3);
    font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,
      sans-serif;
    margin-top: 10px;
  }

  .submit-btn:hover {
    background-color: #d67a4f;
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(236, 139, 90, 0.4);
  }

  .submit-btn:active {
    transform: translateY(0);
  }

  /* Message styling */
  .message {
    padding: 15px;
    border-radius: 8px;
    margin-bottom: 20px;
    font-weight: 500;
    text-align: center;
  }

  .message.success {
    background-color: #4caf50;
    color: white;
  }

  .message.error {
    background-color: #f44336;
    color: white;
  }

  /* Nút quay lại */
  .back-button {
    position: fixed;
    top: 30px;
    right: 30px;
    background: rgba(236, 139, 90, 0.2);
    border: 2px solid var(--accent);
    color: #ffffff;
    padding: 12px 24px;
    border-radius: 25px;
    font-size: 0.9rem;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 8px;
    z-index: 100;
    backdrop-filter: blur(10px);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
    animation: fadeIn 0.5s ease-out 0.3s both;
  }

  /* Animation fade-in cho nút back */
  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }

  .back-button:hover {
    background: var(--accent);
    transform: translateY(-2px);
    box-shadow: 0 4px 15px rgba(236, 139, 90, 0.4);
  }

  /* Responsive design */
  @media (max-width: 768px) {
    main {
      padding: 15px;
    }

    .advisory-header h1 {
      font-size: 2.2rem;
    }

    .form-container {
      padding: 30px 20px;
    }

    .advisory-header img {
      height: 180px; /* Tăng gấp 3 lần từ 60px */
      max-width: 450px; /* Điều chỉnh cho mobile */
    }

    .advisory-container {
      max-width: 100%;
    }

    .back-button {
      top: 15px;
      right: 15px;
      padding: 10px 18px;
      font-size: 0.85rem;
    }
  }

  @media (max-height: 700px) {
    .advisory-header {
      margin-bottom: 20px;
    }

    .advisory-header h1 {
      font-size: 2rem;
    }

    .advisory-header img {
      height: 150px; /* Tăng gấp 3 lần từ 50px */
      max-width: 375px; /* Điều chỉnh cho màn hình thấp */
    }

    .form-group {
      margin-bottom: 15px;
    }

    .form-container {
      padding: 25px 20px;
    }

    .back-button {
      top: 10px;
      right: 10px;
      padding: 8px 16px;
      font-size: 0.8rem;
    }
  }

  /* Tối ưu cho màn hình rất nhỏ */
  @media (max-width: 480px) {
    .back-button {
      top: 10px;
      right: 10px;
      padding: 8px 14px;
      font-size: 0.75rem;
    }

    .back-button i {
      font-size: 0.7rem;
    }
  }
</style>

<!-- Nút quay lại - đặt ngoài để dễ kiểm soát vị trí -->
<a href="${pageContext.request.contextPath}/home.jsp" class="back-button">
  <i class="fas fa-arrow-left"></i> Quay lại
</a>

<main>
  <div class="advisory-container">
    <!-- Header với logo và chữ GYMFIT -->
    <div class="advisory-header">
      <div class="logo-container">
        <img
          src="${pageContext.request.contextPath}/images/logo/logo.png"
          alt="GymFit Logo"
        />
        <h1>GYMFIT</h1>
      </div>
    </div>

    <!-- Form container -->
    <div class="form-container">
      <!-- Hiển thị thông báo nếu có -->
      <c:if test="${not empty successMessage}">
        <div class="message success">
          <i class="fas fa-check-circle"></i> ${successMessage}
        </div>
      </c:if>

      <c:if test="${not empty errorMessage}">
        <div class="message error">
          <i class="fas fa-exclamation-circle"></i> ${errorMessage}
        </div>
      </c:if>

      <!-- Form tư vấn -->
      <form action="${pageContext.request.contextPath}/advisory" method="POST">
        <!-- Full Name field -->
        <div class="form-group">
          <label for="fullName">FULL NAME</label>
          <input
            type="text"
            id="fullName"
            name="fullName"
            required
            value="${fullName != null ? fullName : (param.fullName != null ? param.fullName : '')}"
            placeholder="Nhập họ và tên của bạn"
          />
        </div>

        <!-- Phone field -->
        <div class="form-group">
          <label for="phone">PHONE</label>
          <input
            type="tel"
            id="phone"
            name="phone"
            required
            pattern="[0-9]{10,11}"
            title="Vui lòng nhập số điện thoại hợp lệ (10-11 chữ số)"
            value="${phone != null ? phone : (param.phone != null ? param.phone : '')}"
            placeholder="Nhập số điện thoại của bạn (chỉ số)"
            onkeypress="return isNumberKey(event)"
            oninput="this.value = this.value.replace(/[^0-9]/g, '')"
          />
        </div>

        <!-- Email field -->
        <div class="form-group">
          <label for="email">EMAIL</label>
          <input
            type="email"
            id="email"
            name="email"
            required
            value="${email != null ? email : (param.email != null ? param.email : '')}"
            placeholder="Nhập địa chỉ email của bạn"
          />
        </div>

        <!-- Address field -->
        <div class="form-group">
          <label for="address">ADDRESS</label>
          <input
            type="text"
            id="address"
            name="address"
            required
            value="${address != null ? address : (param.address != null ? param.address : '')}"
            placeholder="Nhập địa chỉ của bạn"
          />
        </div>

        <!-- Submit button -->
        <button type="submit" class="submit-btn">SUBMIT</button>
      </form>
    </div>
  </div>
</main>

<script>
  // Hàm chỉ cho phép nhập số vào ô phone
  function isNumberKey(evt) {
    var charCode = evt.which ? evt.which : evt.keyCode;
    // Chỉ cho phép số (0-9)
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      evt.preventDefault();
      return false;
    }
    return true;
  }

  // Xử lý khi người dùng paste vào ô phone - chỉ giữ lại số
  document.addEventListener('DOMContentLoaded', function () {
    var phoneInput = document.getElementById('phone');
    if (phoneInput) {
      phoneInput.addEventListener('paste', function (e) {
        e.preventDefault();
        var pastedText = (e.clipboardData || window.clipboardData).getData(
          'text',
        );
        var numbersOnly = pastedText.replace(/[^0-9]/g, '');
        this.value = numbersOnly;
      });

      // Xử lý khi blur - kiểm tra lại
      phoneInput.addEventListener('blur', function () {
        this.value = this.value.replace(/[^0-9]/g, '');
      });
    }
  });
</script>

<%@ include file="/views/common/footer.jsp" %>
