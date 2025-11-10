# 🔍 VNPay Signature Debug Guide

## ❌ Vấn đề: "Sai chữ ký" (Wrong Signature)

### 📊 Phân tích log hiện tại

Từ log của bạn:
```
VNPay Sign Data: vnp_Amount=109900000&vnp_Command=pay&vnp_CreateDate=20251111034211&vnp_CurrCode=VND&vnp_ExpireDate=20251111041211&vnp_IpAddr=14.236.169.57&vnp_Locale=vn&vnp_OrderInfo=Thanh toan don hang 31&vnp_OrderType=other&vnp_ReturnUrl=https://bianca-appendicular-boldheartedly.ngrok-free.dev/Gym_Management_Systems/vnpay-return&vnp_TmnCode=JIM4N47C&vnp_TxnRef=31&vnp_Version=2.1.0
VNPay SecureHash: 06379bfe2e16971ac2983b8e99b536b70450cb0fc04cf271c518fa1b50f5bc58aa4bbef27d136b6df306b7fd69650d0d525cc4f86a1dd3eaf6994d8d910ac4d1
```

**Sign Data khi tạo URL có vẻ đúng!** ✅

### 🔎 Cần kiểm tra callback

Vấn đề có thể xảy ra khi VNPay gửi callback. Cần xem log callback để so sánh:

1. **Khi VNPay redirect về `/vnpay-return`**, log sẽ hiển thị:
   ```
   [VNPayReturnServlet] Processing VNPay return callback
   [VNPayReturnServlet] Full Query String: ...
   [VNPayReturnServlet] Parameter: vnp_Amount = [109900000]
   [VNPayReturnServlet] Parameter: vnp_OrderInfo = [...]
   ...
   [VNPayService] Callback Sign Data: ...
   [VNPayService] Calculated Hash: ...
   [VNPayService] Received Hash: ...
   ```

2. **So sánh Sign Data:**
   - Sign Data khi tạo URL vs Callback Sign Data
   - Phải **GIỐNG HỆT** nhau (trừ `vnp_SecureHash`)

### 🐛 Các nguyên nhân thường gặp

#### 1. VNPay thay đổi giá trị tham số
- `vnp_OrderInfo` có thể bị VNPay modify
- `vnp_ReturnUrl` có thể bị encode khác
- `vnp_IpAddr` có thể khác (VNPay dùng IP của họ)

**Giải pháp:** Kiểm tra log callback để xem VNPay gửi gì

#### 2. URL Encoding/Decoding
- Servlet container tự động decode parameters
- Nhưng có thể có edge cases

**Giải pháp:** Đảm bảo dùng raw values (không encode lại)

#### 3. Whitespace/Trim
- Có thể có whitespace thừa

**Giải pháp:** Code đã trim values ✅

#### 4. Tham số bị thiếu/thừa
- VNPay có thể gửi thêm tham số không phải `vnp_*`
- Hoặc thiếu một số tham số

**Giải pháp:** Code đã filter chỉ lấy `vnp_*` parameters ✅

### 📝 Các bước debug

1. **Test thanh toán và xem log callback:**
   - Khi VNPay redirect về, xem log ngay lập tức
   - Copy toàn bộ log từ `[VNPayReturnServlet]` đến `[VNPayService] Signature verification`

2. **So sánh Sign Data:**
   ```
   Sign Data khi tạo URL:
   vnp_Amount=109900000&vnp_Command=pay&...
   
   Callback Sign Data:
   vnp_Amount=109900000&vnp_Command=pay&...
   ```
   - Phải giống hệt nhau!

3. **So sánh Hash:**
   ```
   Calculated Hash: abc123...
   Received Hash:   xyz789...
   ```
   - Nếu khác nhau → Sign Data khác nhau

### 🔧 Cách test

1. **Build và restart:**
   ```bash
   mvn clean package
   # Restart Tomcat
   ```

2. **Test thanh toán:**
   - Vào `/cart` → Checkout → Chọn VNPay
   - Thanh toán với thẻ test
   - **Quan trọng:** Xem log ngay khi VNPay redirect về

3. **Copy log callback:**
   - Tìm log bắt đầu từ `[VNPayReturnServlet] Processing VNPay return callback`
   - Copy toàn bộ log callback
   - Gửi cho tôi để phân tích

### 📋 Checklist

- [ ] Đã xem log Sign Data khi tạo URL
- [ ] Đã xem log Callback Sign Data
- [ ] Đã so sánh Calculated Hash vs Received Hash
- [ ] Đã kiểm tra HashSecret trong email.properties
- [ ] Đã kiểm tra các tham số trong callback
- [ ] Đã kiểm tra không có duplicate parameters
- [ ] Đã kiểm tra OrderInfo không có ký tự đặc biệt

### 💡 Lưu ý quan trọng

1. **VNPay có thể modify một số tham số:**
   - `vnp_IpAddr` có thể là IP của VNPay server
   - `vnp_OrderInfo` có thể bị truncate nếu quá dài
   - `vnp_ReturnUrl` có thể bị encode khác

2. **Sign Data phải match 100%:**
   - Thứ tự tham số (alphabetical)
   - Giá trị tham số (exact match)
   - Format (key=value&key=value)

3. **HashSecret phải chính xác:**
   - Không có whitespace
   - Copy từ VNPay merchant portal
   - Đúng 32 ký tự

### 🆘 Nếu vẫn lỗi

Gửi cho tôi:
1. Log Sign Data khi tạo URL (đã có)
2. Log Callback Sign Data (cần xem khi test)
3. Log Calculated Hash vs Received Hash
4. Full Query String từ callback

Tôi sẽ phân tích và fix!

