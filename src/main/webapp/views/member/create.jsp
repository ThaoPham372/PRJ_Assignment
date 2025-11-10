<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/views/common/header.jsp" %>

<style>
  :root {
    --primary: #141a46;
    --accent: #ec8b5e;
    --accent-hover: #d67a4f;
  }

  body {
    background: linear-gradient(135deg, #f5f7fa 0%, #e9ecef 100%);
    min-height: 100vh;
  }

  .create-container {
    max-width: 900px;
    margin: 50px auto;
    background: white;
    padding: 40px 50px;
    border-radius: 20px;
    box-shadow: 0 6px 25px rgba(0, 0, 0, 0.1);
  }

  h2.title {
    font-weight: 800;
    color: var(--primary);
    margin-bottom: 25px;
    text-transform: uppercase;
    border-bottom: 3px solid var(--accent);
    display: inline-block;
    padding-bottom: 10px;
  }

  .form-label {
    font-weight: 600;
    color: #333;
  }

  .form-control, .form-select {
    border-radius: 12px;
    padding: 12px 15px;
    border: 1px solid #ccc;
    transition: border-color 0.2s ease;
  }

  .form-control:focus, .form-select:focus {
    border-color: var(--accent);
    box-shadow: 0 0 0 0.2rem rgba(236, 139, 94, 0.25);
  }

  .btn-submit {
    background: linear-gradient(135deg, #ec8b5e, #d67a4f);
    color: white;
    font-weight: 700;
    border: none;
    padding: 14px 25px;
    border-radius: 25px;
    transition: all 0.3s ease;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    box-shadow: 0 4px 15px rgba(236, 139, 94, 0.3);
    cursor: pointer;
  }

  .btn-submit:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 25px rgba(236, 139, 94, 0.4);
  }

  .btn-submit:disabled {
      background: #ccc;
      transform: none;
      box-shadow: none;
      cursor: not-allowed;
  }

  .btn-back {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    background: rgba(20, 26, 70, 0.1);
    color: var(--primary);
    border-radius: 25px;
    padding: 10px 20px;
    font-weight: 600;
    text-decoration: none;
    transition: all 0.3s;
    margin-bottom: 25px;
  }

  .btn-back:hover {
    background: rgba(20, 26, 70, 0.2);
  }
</style>

<div class="create-container">
  <a href="${pageContext.request.contextPath}/member/schedule" class="btn-back">
    <i class="fas fa-arrow-left"></i> Quay lại lịch tập
  </a>

  <h2 class="title">Đặt Lịch Mới</h2>

  <form id="bookingForm" action="${pageContext.request.contextPath}/api/member/schedule/create" method="post">
    <div class="mb-3">
      <label class="form-label">Huấn luyện viên</label>
      <select class="form-select" id="trainerSelect" name="trainerId" required>
        <option value="">-- Chọn HLV --</option>
        <option value="1">Nguyễn Văn A</option>
        <option value="2">Trần Thị B</option>
      </select>
    </div>

    <div class="mb-3">
      <label class="form-label">Phòng Gym</label>
       <select class="form-select" id="gymSelect" name="gymId" required>
        <option value="">-- Chọn phòng --</option>
        <option value="1">GymFit Center 1</option>
        <option value="2">GymFit Center 2</option>
      </select>
    </div>

    <div class="mb-3">
      <label class="form-label">Ngày tập</label>
      <input type="date" class="form-control" id="bookingDate" name="bookingDate" required />
    </div>

    <div class="mb-3">
      <label class="form-label">Khung giờ trống</label>
      <select class="form-select" id="slotSelect" name="slotId" required>
        <option value="">-- Vui lòng chọn HLV, Phòng và Ngày trước --</option>
      </select>
    </div>

    <div class="mb-3">
      <label class="form-label">Ghi chú</label>
      <textarea class="form-control" name="notes" rows="3" placeholder="VD: Tôi muốn tập trung vào nhóm cơ lưng..."></textarea>
    </div>

    <button type="submit" class="btn-submit w-100" id="btnSubmit">
      <i class="fas fa-calendar-plus"></i> Xác nhận đặt lịch
    </button>
  </form>
</div>

<script>
    // Hàm fetch available slots
    async function fetchSlots() {
        const trainerId = document.getElementById('trainerSelect').value;
        const gymId = document.getElementById('gymSelect').value;
        const date = document.getElementById('bookingDate').value;
        const slotSelect = document.getElementById('slotSelect');
        const btnSubmit = document.getElementById('btnSubmit');

        // Reset slot dropdown
        slotSelect.innerHTML = '<option value="">-- Đang tải... --</option>';
        btnSubmit.disabled = true;

        if (!trainerId || !gymId || !date) {
            slotSelect.innerHTML = '<option value="">-- Vui lòng chọn đầy đủ thông tin trên --</option>';
            return;
        }

        const apiUrl = `${pageContext.request.contextPath}/api/member/schedule/availability?trainerId=${trainerId}&gymId=${gymId}&date=${date}`;

        try {
            const res = await fetch(apiUrl);
            if (res.ok) {
                const slots = await res.json();
                slotSelect.innerHTML = '<option value="">-- Chọn khung giờ --</option>';
                
                if (slots.length === 0) {
                     const opt = document.createElement('option');
                     opt.disabled = true;
                     opt.textContent = 'Không còn giờ trống';
                     slotSelect.appendChild(opt);
                } else {
                    slots.forEach(s => {
                      const opt = document.createElement('option');
                      // Đảm bảo object 's' có các trường slotId, startTime, endTime từ JSON trả về
                      opt.value = s.slotId; 
                      opt.textContent = `${s.startTime} - ${s.endTime} (${s.slotName || 'Slot'})`;
                      slotSelect.appendChild(opt);
                    });
                    btnSubmit.disabled = false;
                }
            } else {
                slotSelect.innerHTML = '<option value="">Lỗi khi tải dữ liệu</option>';
                console.error("Failed to fetch slots");
            }
        } catch (error) {
            slotSelect.innerHTML = '<option value="">Lỗi kết nối</option>';
            console.error("Error fetching slots:", error);
        }
    }

    // Gán sự kiện change cho cả 3 trường để tự động load lại slot khi bất kỳ trường nào thay đổi
    document.getElementById('trainerSelect').addEventListener('change', fetchSlots);
    document.getElementById('gymSelect').addEventListener('change', fetchSlots);
    document.getElementById('bookingDate').addEventListener('change', fetchSlots);

    // Xử lý submit form
    document.getElementById('bookingForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const form = e.target;
        const btn = document.getElementById('btnSubmit');
        const originalBtnText = btn.innerHTML;
        
        // Hiển thị trạng thái đang xử lý
        btn.disabled = true;
        btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';

        try {
            const formData = new URLSearchParams(new FormData(form));
            const res = await fetch(form.action, {
                method: 'POST',
                body: formData,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

            const data = await res.json();
            if (res.ok) {
                alert(data.message || "Đặt lịch thành công!");
                window.location.href = "${pageContext.request.contextPath}/member/schedule";
            } else {
                alert("Lỗi: " + (data.error || "Không thể đặt lịch"));
                btn.disabled = false;
                btn.innerHTML = originalBtnText;
            }
        } catch (error) {
            alert("Đã xảy ra lỗi kết nối đến máy chủ.");
            btn.disabled = false;
            btn.innerHTML =