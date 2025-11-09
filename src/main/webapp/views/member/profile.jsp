<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %> <%@ include
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
    --success: #28a745;
    --warning: #ffc107;
    --danger: #dc3545;
    --info: #17a2b8;
  }

  .profile-container {
    max-width: 1400px;
    margin: 0 auto;
    padding: 30px 15px;
  }

  .profile-hero {
    background: var(--gradient-primary);
    border-radius: 20px;
    padding: 40px;
    margin-bottom: 40px;
    color: white;
    position: relative;
    overflow: hidden;
    box-shadow: 0 10px 40px var(--shadow);
  }

  .profile-hero::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -10%;
    width: 400px;
    height: 400px;
    background: radial-gradient(
      circle,
      rgba(236, 139, 94, 0.2) 0%,
      transparent 70%
    );
    border-radius: 50%;
  }

  .profile-hero-content {
    position: relative;
    z-index: 1;
    display: flex;
    align-items: center;
    gap: 30px;
  }

  .profile-avatar-container {
    position: relative;
  }

  .profile-avatar {
    width: 150px;
    height: 150px;
    border-radius: 50%;
    background: var(--gradient-accent);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 4rem;
    color: white;
    border: 5px solid rgba(255, 255, 255, 0.3);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
  }

  .profile-avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 50%;
  }

  .status-badge {
    position: absolute;
    bottom: 10px;
    right: 10px;
    background: var(--success);
    width: 25px;
    height: 25px;
    border-radius: 50%;
    border: 3px solid white;
  }

  .profile-info {
    flex: 1;
  }

  .profile-name {
    font-size: 2.5rem;
    font-weight: 900;
    margin-bottom: 10px;
    text-transform: uppercase;
    letter-spacing: 1px;
  }

  .profile-meta {
    display: flex;
    gap: 30px;
    flex-wrap: wrap;
    margin-top: 15px;
  }

  .profile-meta-item {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .profile-meta-item i {
    opacity: 0.8;
    font-size: 1.2rem;
  }

  .btn-edit-profile {
    background: var(--accent);
    color: white;
    border: none;
    border-radius: 25px;
    padding: 12px 30px;
    font-weight: 600;
    transition: all 0.3s ease;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 8px;
  }

  .btn-edit-profile:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(236, 139, 94, 0.4);
    color: white;
  }

  .btn-back-top {
    background: var(--gradient-primary);
    color: white;
    border: none;
    border-radius: 25px;
    padding: 10px 20px;
    font-weight: 600;
    transition: all 0.3s ease;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 8px;
    font-size: 0.9rem;
  }

  .btn-back-top:hover {
    transform: translateX(-5px);
    box-shadow: 0 4px 15px rgba(20, 26, 70, 0.3);
    color: white;
  }

  .alert {
    padding: 15px 20px;
    border-radius: 10px;
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .alert-success {
    background: rgba(40, 167, 69, 0.1);
    border-left: 4px solid var(--success);
    color: var(--success);
  }

  .alert-danger {
    background: rgba(220, 53, 69, 0.1);
    border-left: 4px solid var(--danger);
    color: var(--danger);
  }

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
    margin-bottom: 30px;
  }

  .stat-box {
    background: var(--card);
    border-radius: 15px;
    padding: 25px;
    box-shadow: 0 4px 15px var(--shadow);
    transition: all 0.3s ease;
    border-left: 4px solid var(--accent);
  }

  .stat-box:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 25px var(--shadow-hover);
  }

  .stat-icon {
    width: 50px;
    height: 50px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.5rem;
    margin-bottom: 15px;
  }

  .stat-icon.workout {
    background: rgba(40, 167, 69, 0.1);
    color: var(--success);
  }
  .stat-icon.streak {
    background: rgba(255, 193, 7, 0.1);
    color: var(--warning);
  }
  .stat-icon.calories {
    background: rgba(220, 53, 69, 0.1);
    color: var(--danger);
  }
  .stat-icon.membership {
    background: rgba(236, 139, 94, 0.1);
    color: var(--accent);
  }

  .stat-value {
    font-size: 2rem;
    font-weight: 900;
    color: var(--text);
    margin-bottom: 5px;
  }

  .stat-label {
    color: var(--text-light);
    font-size: 0.9rem;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
  }

  .info-card {
    background: var(--card);
    border-radius: 15px;
    padding: 30px;
    margin-bottom: 30px;
    box-shadow: 0 4px 15px var(--shadow);
  }

  .info-card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 25px;
    padding-bottom: 15px;
    border-bottom: 2px solid var(--muted);
  }

  .info-card-title {
    font-size: 1.3rem;
    font-weight: 800;
    color: var(--text);
    display: flex;
    align-items: center;
    gap: 10px;
    text-transform: uppercase;
    letter-spacing: 0.5px;
  }

  .info-card-title i {
    color: var(--accent);
  }

  .info-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;
  }

  .info-item {
    padding: 15px;
    background: var(--muted);
    border-radius: 10px;
    transition: all 0.3s ease;
  }

  .info-item:hover {
    background: #e9ecef;
  }

  .info-label {
    font-size: 0.85rem;
    color: var(--text-light);
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    margin-bottom: 8px;
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .info-label i {
    font-size: 0.9rem;
    opacity: 0.7;
  }

  .info-value {
    font-size: 1.1rem;
    color: var(--text);
    font-weight: 600;
  }

  .info-value.empty {
    color: var(--text-light);
    font-style: italic;
  }

  .bmi-indicator {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    padding: 6px 12px;
    border-radius: 20px;
    font-weight: 600;
    font-size: 0.9rem;
  }

  .bmi-indicator.underweight {
    background: rgba(23, 162, 184, 0.1);
    color: var(--info);
  }
  .bmi-indicator.normal {
    background: rgba(40, 167, 69, 0.1);
    color: var(--success);
  }
  .bmi-indicator.overweight {
    background: rgba(255, 193, 7, 0.1);
    color: var(--warning);
  }
  .bmi-indicator.obese {
    background: rgba(220, 53, 69, 0.1);
    color: var(--danger);
  }

  .emergency-alert {
    background: linear-gradient(135deg, #fff3cd 0%, #ffeaa7 100%);
    border-left: 4px solid var(--warning);
    padding: 20px;
    border-radius: 10px;
    margin-bottom: 20px;
  }

  .emergency-alert-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 10px;
  }

  .emergency-alert-icon {
    font-size: 1.8rem;
    color: var(--warning);
  }

  .empty-state {
    text-align: center;
    padding: 40px;
    color: var(--text-light);
  }

  .empty-state i {
    font-size: 3rem;
    opacity: 0.3;
    margin-bottom: 15px;
  }

  .badge-status {
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 0.85rem;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
  }

  .badge-status.active {
    background: rgba(40, 167, 69, 0.1);
    color: var(--success);
  }

  .badge-status.expired {
    background: rgba(220, 53, 69, 0.1);
    color: var(--danger);
  }

  @media (max-width: 768px) {
    .profile-hero-content {
      flex-direction: column;
      text-align: center;
    }

    .profile-name {
      font-size: 1.8rem;
    }

    .profile-meta {
      justify-content: center;
    }

    .stats-grid {
      grid-template-columns: 1fr;
    }

    .info-grid {
      grid-template-columns: 1fr;
    }
  }
</style>

<div class="profile-container">
  <!-- Back Button -->
  <div style="margin-bottom: 25px">
    <a
      href="${pageContext.request.contextPath}/member/dashboard"
      class="btn-back-top"
    >
      <i class="fas fa-arrow-left"></i>
      Quay lại Dashboard
    </a>
  </div>

  <!-- Alert Messages -->
  <c:if test="${not empty success}">
    <div class="alert alert-success">
      <i class="fas fa-check-circle"></i>
      <span>${success}</span>
    </div>
  </c:if>

  <c:if test="${not empty error}">
    <div class="alert alert-danger">
      <i class="fas fa-exclamation-circle"></i>
      <span>${error}</span>
    </div>
  </c:if>

  <!-- Profile Hero Section -->
  <div class="profile-hero">
    <div class="profile-hero-content">
      <div class="profile-avatar-container">
        <div class="profile-avatar">
          <i class="fas fa-user"></i>
        </div>
        <div class="status-badge" title="Active"></div>
      </div>
      <div class="profile-info">
        <h1 class="profile-name">
          <c:out
            value="${member.name != null && !empty member.name ? member.name : (member.username != null ? member.username : 'Member')}"
          />
        </h1>
        <div class="profile-meta">
          <div class="profile-meta-item">
            <i class="fas fa-calendar-plus"></i>
            <span
              ><strong>Tham gia:</strong>
              <c:choose>
                <c:when test="${member.createdDate != null}">
                  <fmt:formatDate
                    value="${member.createdDate}"
                    pattern="dd/MM/yyyy"
                  />
                </c:when>
                <c:otherwise>N/A</c:otherwise>
              </c:choose>
            </span>
          </div>
          <div class="profile-meta-item">
            <i class="fas fa-envelope"></i>
            <span><c:out value="${member.email}" /></span>
          </div>
          <div class="profile-meta-item">
            <i class="fas fa-info-circle"></i>
            <span
              ><strong>Trạng thái:</strong>
              <c:choose>
                <c:when
                  test="${member.status == 'active' || member.status == 'ACTIVE'}"
                  >Đang hoạt động</c:when
                >
                <c:when test="${member.status == 'INACTIVE'}"
                  >Không hoạt động</c:when
                >
                <c:when test="${member.status == 'SUSPENDED'}">Tạm khóa</c:when>
                <c:otherwise><c:out value="${member.status}" /></c:otherwise>
              </c:choose>
            </span>
          </div>
        </div>
        <div style="margin-top: 20px">
          <a
            href="${pageContext.request.contextPath}/member/profile-edit"
            class="btn-edit-profile"
          >
            <i class="fas fa-edit"></i>
            Chỉnh sửa thông tin
          </a>
        </div>
      </div>
    </div>
  </div>

  <!-- Basic Information Card -->
  <div class="info-card">
    <div class="info-card-header">
      <h2 class="info-card-title">
        <i class="fas fa-user-circle"></i>
        Thông tin cơ bản
      </h2>
    </div>
    <div class="info-grid">
      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-signature"></i>
          Username
        </div>
        <div class="info-value">
          <c:out value="${member.username != null ? member.username : 'N/A'}" />
        </div>
      </div>

      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-user"></i>
          Tên đầy đủ
        </div>
        <div class="info-value ${empty member.name ? 'empty' : ''}">
          <c:out
            value="${member.name != null && !empty member.name ? member.name : 'Chưa cập nhật'}"
          />
        </div>
      </div>

      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-envelope"></i>
          Email
        </div>
        <div class="info-value">
          <c:out value="${member.email != null ? member.email : 'N/A'}" />
        </div>
      </div>

      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-phone-alt"></i>
          Số điện thoại
        </div>
        <div class="info-value ${empty member.phone ? 'empty' : ''}">
          <c:out
            value="${member.phone != null ? member.phone : 'Chưa cập nhật'}"
          />
        </div>
      </div>

      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-birthday-cake"></i>
          Ngày sinh
        </div>
        <div class="info-value ${empty member.dob ? 'empty' : ''}">
          <c:choose>
            <c:when test="${member.dob != null}">
              <fmt:formatDate value="${member.dob}" pattern="dd/MM/yyyy" />
            </c:when>
            <c:otherwise>Chưa cập nhật</c:otherwise>
          </c:choose>
        </div>
      </div>

      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-venus-mars"></i>
          Giới tính
        </div>
        <div class="info-value ${empty member.gender ? 'empty' : ''}">
          <c:choose>
            <c:when
              test="${member.gender == 'Nam' || member.gender == 'male' || member.gender == 'Male'}"
              >Nam</c:when
            >
            <c:when
              test="${member.gender == 'Nữ' || member.gender == 'female' || member.gender == 'Female'}"
              >Nữ</c:when
            >
            <c:when
              test="${member.gender == 'Khác' || member.gender == 'other' || member.gender == 'Other'}"
              >Khác</c:when
            >
            <c:otherwise>Chưa cập nhật</c:otherwise>
          </c:choose>
        </div>
      </div>

      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-calendar-plus"></i>
          Ngày tham gia
        </div>
        <div class="info-value">
          <c:choose>
            <c:when test="${member.createdDate != null}">
              <fmt:formatDate
                value="${member.createdDate}"
                pattern="dd/MM/yyyy"
              />
            </c:when>
            <c:otherwise>N/A</c:otherwise>
          </c:choose>
        </div>
      </div>

      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-info-circle"></i>
          Trạng thái
        </div>
        <div class="info-value">
          <c:choose>
            <c:when
              test="${member.status == 'active' || member.status == 'ACTIVE'}"
              >Đang hoạt động</c:when
            >
            <c:when test="${member.status == 'INACTIVE'}"
              >Không hoạt động</c:when
            >
            <c:when test="${member.status == 'SUSPENDED'}">Tạm khóa</c:when>
            <c:otherwise><c:out value="${member.status}" /></c:otherwise>
          </c:choose>
        </div>
      </div>

      <div class="info-item" style="grid-column: 1 / -1">
        <div class="info-label">
          <i class="fas fa-map-marker-alt"></i>
          Địa chỉ
        </div>
        <div class="info-value ${empty member.address ? 'empty' : ''}">
          <c:out
            value="${member.address != null ? member.address : 'Chưa cập nhật'}"
          />
        </div>
      </div>
    </div>
  </div>

  <!-- Physical Information Card -->
  <div class="info-card">
    <div class="info-card-header">
      <h2 class="info-card-title">
        <i class="fas fa-heartbeat"></i>
        Chỉ số cơ thể
      </h2>
    </div>
    <div class="info-grid">
      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-ruler-vertical"></i>
          Chiều cao
        </div>
        <div class="info-value ${empty member.height ? 'empty' : ''}">
          <c:choose>
            <c:when test="${member.height != null}">
              <fmt:formatNumber value="${member.height}" pattern="#,##0.0" /> cm
            </c:when>
            <c:otherwise>Chưa cập nhật</c:otherwise>
          </c:choose>
        </div>
      </div>

      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-weight"></i>
          Cân nặng
        </div>
        <div class="info-value ${empty member.weight ? 'empty' : ''}">
          <c:choose>
            <c:when test="${member.weight != null}">
              <fmt:formatNumber value="${member.weight}" pattern="#,##0.0" /> kg
            </c:when>
            <c:otherwise>Chưa cập nhật</c:otherwise>
          </c:choose>
        </div>
      </div>

      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-balance-scale"></i>
          Chỉ số BMI
        </div>
        <div class="info-value">
          <c:choose>
            <c:when test="${member.bmi != null}">
              <fmt:formatNumber value="${member.bmi}" pattern="#,##0.00" />
              <c:if test="${bmiCategory != null}">
                <c:set var="bmiClass" value="" />
                <c:choose>
                  <c:when test="${bmiCategory == 'Thiếu cân'}"
                    ><c:set var="bmiClass" value="underweight"
                  /></c:when>
                  <c:when test="${bmiCategory == 'Bình thường'}"
                    ><c:set var="bmiClass" value="normal"
                  /></c:when>
                  <c:when test="${bmiCategory == 'Thừa cân'}"
                    ><c:set var="bmiClass" value="overweight"
                  /></c:when>
                  <c:when test="${bmiCategory == 'Béo phì'}"
                    ><c:set var="bmiClass" value="obese"
                  /></c:when>
                </c:choose>
                <span class="bmi-indicator ${bmiClass}"> ${bmiCategory} </span>
              </c:if>
            </c:when>
            <c:otherwise>
              <span class="empty">Chưa cập nhật</span>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </div>
  </div>

  <!-- Emergency Contact Card -->
  <div class="info-card">
    <div class="emergency-alert">
      <div class="emergency-alert-header">
        <i class="fas fa-exclamation-triangle emergency-alert-icon"></i>
        <div>
          <strong>Thông tin liên hệ khẩn cấp</strong>
          <div style="font-size: 0.9rem; margin-top: 5px">
            Thông tin này sẽ được sử dụng trong trường hợp khẩn cấp
          </div>
        </div>
      </div>
    </div>

    <div class="info-grid">
      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-user-friends"></i>
          Họ tên người thân
        </div>
        <div
          class="info-value ${empty member.emergencyContactName ? 'empty' : ''}"
        >
          <c:out
            value="${member.emergencyContactName != null ? member.emergencyContactName : 'Chưa cập nhật'}"
          />
        </div>
      </div>

      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-phone-alt"></i>
          Số điện thoại
        </div>
        <div
          class="info-value ${empty member.emergencyContactPhone ? 'empty' : ''}"
        >
          <c:out
            value="${member.emergencyContactPhone != null ? member.emergencyContactPhone : 'Chưa cập nhật'}"
          />
        </div>
      </div>

      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-heart"></i>
          Mối quan hệ
        </div>
        <div
          class="info-value ${empty member.emergencyContactRelation ? 'empty' : ''}"
        >
          <c:out
            value="${member.emergencyContactRelation != null ? member.emergencyContactRelation : 'Chưa cập nhật'}"
          />
        </div>
      </div>

      <div class="info-item">
        <div class="info-label">
          <i class="fas fa-home"></i>
          Địa chỉ người thân
        </div>
        <div
          class="info-value ${empty member.emergencyContactAddress ? 'empty' : ''}"
        >
          <c:out
            value="${member.emergencyContactAddress != null ? member.emergencyContactAddress : 'Chưa cập nhật'}"
          />
        </div>
      </div>
    </div>
  </div>
</div>

<%@ include file="/views/common/footer.jsp" %>
