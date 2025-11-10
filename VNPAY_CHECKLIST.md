# ✅ Checklist Kiểm Tra Cấu Hình VNPay

## 📋 Thông Tin Cấu Hình (Từ VNPay)

- **Terminal ID (vnp_TmnCode):** `JIM4N47C` ✅
- **Secret Key (vnp_HashSecret):** `3321BJ8VACED3QE8RCZK83F6ES2AO7DY` ✅
- **Payment URL:** `https://sandbox.vnpayment.vn/paymentv2/vpcpay.html` ✅
- **Merchant Admin:** `https://sandbox.vnpayment.vn/merchantv2/` ✅
- **Login:** `danhgaming917@gmail.com` ✅

## 🔍 Kiểm Tra Code

### 1. ✅ email.properties
- [x] `vnp_TmnCode=JIM4N47C` - ĐÚNG
- [x] `vnp_HashSecret=3321BJ8VACED3QE8RCZK83F6ES2AO7DY` - ĐÚNG
- [x] `vnp_Url=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html` - ĐÚNG
- [x] `vnp_ReturnUrl` - Có fallback, nhưng hệ thống sẽ dùng dynamic context path

### 2. ✅ VNPayService.java
- [x] Trim HashSecret để loại bỏ whitespace
- [x] Build Sign Data đúng format (alphabetical order, key=value&...)
- [x] Loại bỏ ký tự đặc biệt trong OrderInfo
- [x] Convert IPv6 sang IPv4
- [x] Dynamic ReturnUrl từ request
- [x] HMAC SHA512 signature generation
- [x] Timezone Asia/Ho_Chi_Minh
- [x] Expire date 30 phút

### 3. ✅ CheckoutServlet.java
- [x] Xử lý VNPAY payment method
- [x] Build baseUrl động từ request (bao gồm context path)
- [x] Truyền baseUrl vào processVNPayPayment()
- [x] Logging để debug

### 4. ✅ VNPayReturnServlet.java
- [x] URL pattern: `/vnpay-return` ✅
- [x] Xử lý GET và POST
- [x] Collect tất cả parameters
- [x] Verify signature
- [x] Xử lý response code
- [x] Update payment status

### 5. ✅ Verify Signature Logic
- [x] Chỉ lấy parameters bắt đầu bằng `vnp_`
- [x] Loại bỏ `vnp_SecureHash` trước khi tính signature
- [x] Sắp xếp alphabetical (TreeMap)
- [x] Chỉ tính các giá trị không rỗng
- [x] HMAC SHA512
- [x] Case-insensitive comparison

## ⚠️ Vấn Đề Quan Trọng: Context Path

**Vấn đề:** 
- Servlet URL pattern: `/vnpay-return` (không có context path)
- Nhưng trong VNPay merchant portal bạn đã đăng ký: `/Gym_Management_Systems/vnpay-return`

**Giải pháp đã áp dụng:**
- Code hiện tại sẽ tự động detect context path từ request
- ReturnUrl sẽ được build động: `baseUrl + "/vnpay-return"`
- Nếu context path là `/Gym_Management_Systems`, ReturnUrl sẽ là: `.../Gym_Management_Systems/vnpay-return`

**Cần kiểm tra:**
1. Context path thực tế khi deploy (xem log khi checkout)
2. Đảm bảo context path khớp với URL đã đăng ký trong VNPay

## 🧪 Test Checklist

### Bước 1: Kiểm tra Context Path
1. Build và deploy ứng dụng
2. Test checkout với VNPay
3. Xem log:
   ```
   [CheckoutServlet] Context Path: /Gym_Management_Systems
   [CheckoutServlet] Base URL: http://localhost:8080/Gym_Management_Systems
   [VNPayService] Using ReturnUrl: http://localhost:8080/Gym_Management_Systems/vnpay-return
   ```
4. **QUAN TRỌNG:** ReturnUrl trong log PHẢI khớp với URL đã đăng ký trong VNPay merchant portal

### Bước 2: Kiểm tra Sign Data
1. Xem log Sign Data khi tạo URL:
   ```
   VNPay Sign Data: vnp_Amount=...&vnp_Command=...&...&vnp_ReturnUrl=...&...
   ```
2. Đảm bảo:
   - Không có ký tự `#` trong OrderInfo
   - IP Address là IPv4 (127.0.0.1 hoặc IP thực)
   - ReturnUrl đúng format

### Bước 3: Test Thanh Toán
1. Sử dụng thẻ test: `9704198526191432198`
2. OTP: `123456`
3. Sau khi thanh toán, xem log callback:
   ```
   [VNPayReturnServlet] Full Query String: ...
   [VNPayService] Callback Sign Data: ...
   [VNPayService] Signature verification: SUCCESS/FAILED
   ```

### Bước 4: Nếu Signature FAILED
1. So sánh Sign Data:
   - Sign Data khi tạo URL vs Callback Sign Data
   - Callback sẽ có thêm các tham số: `vnp_ResponseCode`, `vnp_TransactionNo`, etc.
2. So sánh Hash:
   - Calculated Hash vs Received Hash
   - Phải giống nhau (case-insensitive)

## 🔧 Cách Xác Định Context Path

### Cách 1: Xem log khi checkout
```
[CheckoutServlet] Context Path: /Gym_Management_Systems
```

### Cách 2: Kiểm tra WAR file name
- Nếu WAR file là `Gym_Management_Systems.war`, context path thường là `/Gym_Management_Systems`
- Nếu WAR file là `ROOT.war` hoặc deploy vào `webapps/ROOT`, context path là `/` (empty)

### Cách 3: Kiểm tra context.xml
- File: `src/main/webapp/META-INF/context.xml`
- Hiện tại: `<Context path=""/>` (empty = root context)
- Nếu muốn set cố định: `<Context path="/Gym_Management_Systems"/>`

## 📝 Lưu Ý Quan Trọng

1. **ReturnUrl PHẢI khớp chính xác:**
   - URL trong sign data khi tạo payment
   - URL mà VNPay redirect về
   - URL đã đăng ký trong VNPay merchant portal

2. **Nếu context path không đúng:**
   - Cách 1: Đổi tên WAR file khi deploy
   - Cách 2: Cập nhật `context.xml` để set context path cố định
   - Cách 3: Cập nhật URL trong VNPay merchant portal cho khớp

3. **HashSecret:**
   - Không có khoảng trắng thừa
   - Copy chính xác từ VNPay merchant portal
   - Code đã tự động trim

## ✅ Kết Luận

Code đã được cấu hình đúng. Vấn đề có thể là:
- Context path không khớp với URL đã đăng ký trong VNPay
- Hoặc có vấn đề với signature verification (cần xem log callback)

**Hãy test và xem log để xác định vấn đề cụ thể!**

