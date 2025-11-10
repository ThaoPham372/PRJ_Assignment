# Hướng dẫn tích hợp VNPay - Step by Step

## 📋 Tổng quan

Hệ thống đã được tích hợp VNPay để xử lý thanh toán online. Tài liệu này hướng dẫn cách cấu hình và kiểm thử VNPay sandbox.

## 🔧 Bước 1: Đăng ký tài khoản VNPay Sandbox

1. Truy cập: https://sandbox.vnpayment.vn/
2. Đăng ký tài khoản merchant mới (nếu chưa có)
3. Đăng nhập vào merchant portal

## 🔑 Bước 2: Đăng ký merchant và lấy thông tin cấu hình

### 2.1. Đăng ký merchant mới

1. Truy cập: https://sandbox.vnpayment.vn/
2. Nhấn **"Đăng ký"** hoặc **"Đăng ký merchant"**
3. Điền thông tin đăng ký:
   - **Tên hệ thống:** `Gym Manager System` (hoặc tên bạn muốn)
   - **URL trả về (Return URL):** 
     ```
     https://bianca-appendicular-boldheartedly.ngrok-free.dev/Gym_Manager_System/vnpay-return
     ```
     ⚠️ **LƯU Ý:** Sử dụng ngrok URL, **KHÔNG** dùng `localhost`!
   - **Email đăng ký:** Email của bạn
   - **Mật khẩu:** Mật khẩu bạn muốn
   - **Nhập lại mật khẩu:** Nhập lại mật khẩu
   - **Mã xác nhận:** Nhập mã captcha hiển thị
4. Nhấn **"Đăng ký"**

### 2.2. Lấy thông tin cấu hình

Sau khi đăng ký thành công và đăng nhập vào merchant portal, bạn sẽ có:

1. **vnp_TmnCode**: Mã Terminal ID (ví dụ: `2QXUI4J4`)
2. **vnp_HashSecret**: Mã bảo mật để tạo chữ ký (ví dụ: `RAOCTKRKRJDJIEJNQOANQHCMTXUTXVKI`)
3. **vnp_Url**: URL thanh toán (sandbox: `https://sandbox.vnpayment.vn/paymentv2/vpcpay.html`)
4. **vnp_ReturnUrl**: URL callback sau khi thanh toán (sử dụng ngrok URL của bạn)

## ⚙️ Bước 3: Cấu hình ngrok (cho local development)

**⚠️ QUAN TRỌNG:** VNPay **KHÔNG chấp nhận** URL `localhost`. Bạn **PHẢI** sử dụng ngrok hoặc một URL công khai.

### 3.1. Cài đặt và chạy ngrok

1. **Tải ngrok:** https://ngrok.com/download
2. **Chạy ngrok để expose port 8080:**
   ```bash
   ngrok http 8080
   ```
3. **Lấy ngrok URL:** Từ output, bạn sẽ thấy:
   ```
   Forwarding    https://xxxxx-xxxxx-xxxxx.ngrok-free.dev -> http://localhost:8080
   ```
   Copy URL `https://xxxxx-xxxxx-xxxxx.ngrok-free.dev` (phần trước dấu `->`)

### 3.2. Cấu hình trong email.properties

Mở file `src/main/resources/email.properties` và cập nhật các giá trị sau:

```properties
# VNPay Configuration
# IMPORTANT: VNPay requires a public URL (not localhost). Use ngrok for local development.
vnp_TmnCode=YOUR_TMN_CODE          # Thay bằng TMN Code của bạn
vnp_HashSecret=YOUR_HASH_SECRET    # Thay bằng Hash Secret thực tế từ VNPay
vnp_Url=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
# Use ngrok URL (update this when ngrok URL changes)
vnp_ReturnUrl=https://YOUR_NGROK_URL.ngrok-free.dev/Gym_Manager_System/vnpay-return
```

**Ví dụ với ngrok URL của bạn:**
```properties
vnp_ReturnUrl=https://bianca-appendicular-boldheartedly.ngrok-free.dev/Gym_Manager_System/vnpay-return
```

**Lưu ý quan trọng:**
- ⚠️ **KHÔNG** sử dụng `localhost` - VNPay sẽ từ chối
- Thay `YOUR_NGROK_URL` bằng ngrok URL thực tế của bạn
- Ngrok URL có thể thay đổi mỗi lần restart ngrok (trừ khi dùng ngrok account có domain cố định)
- Nếu ngrok URL thay đổi, bạn cần:
  1. Cập nhật lại `vnp_ReturnUrl` trong `email.properties`
  2. Cập nhật lại Return URL trong VNPay merchant portal
  3. Restart ứng dụng

## 🏗️ Bước 4: Build và chạy ứng dụng

1. **Build project:**
   ```bash
   mvn clean install
   ```

2. **Chạy ứng dụng:**
   - Nếu dùng Tomcat: Deploy WAR file vào Tomcat
   - Nếu dùng Maven: `mvn tomcat7:run` hoặc tương tự
   - Nếu dùng IDE: Run on server

3. **Kiểm tra log:**
   - Xem console log để đảm bảo không có lỗi khi khởi động
   - Kiểm tra log: `[VNPayService] Successfully loaded configuration`

## 🧪 Bước 5: Kiểm thử thanh toán

### 5.1. Tạo đơn hàng test

1. Đăng nhập vào hệ thống
2. Thêm sản phẩm vào giỏ hàng
3. Vào trang checkout (`/checkout`)
4. Chọn phương thức thanh toán: **"Thanh toán qua VNPay"**
5. Điền thông tin giao hàng
6. Nhấn **"Xác nhận đặt hàng"**

### 5.2. Thanh toán trên VNPay Sandbox

Sau khi nhấn "Xác nhận đặt hàng", bạn sẽ được redirect đến trang thanh toán VNPay.

**Thông tin thẻ test (VNPay Sandbox):**

| Ngân hàng | Số thẻ | Tên chủ thẻ | Ngày phát hành | OTP |
|-----------|--------|-------------|----------------|-----|
| NCB | `9704198526191432198` | `NGUYEN VAN A` | `07/15` | `123456` |

**Lưu ý:**
- Đây là thẻ test để thanh toán thành công
- Mật khẩu OTP: `123456`
- Ngân hàng: NCB (Ngân hàng Quốc Dân)

### 5.3. Kiểm tra kết quả

Sau khi thanh toán:

1. **Nếu thành công:**
   - Bạn sẽ được redirect về `/order/success?orderId=XXX`
   - Hiển thị thông báo: "Thanh toán thành công! Đơn hàng của bạn đã được xác nhận."
   - Trong database:
     - `payment.status` = `PAID`
     - `order.order_status` = `COMPLETED`
     - `payment.external_ref` = VNPay Transaction ID

2. **Nếu thất bại:**
   - Bạn sẽ được redirect về `/cart`
   - Hiển thị thông báo lỗi tương ứng
   - Trong database:
     - `payment.status` = `FAILED`

## 🔍 Bước 6: Kiểm tra log và debug

### Log quan trọng cần theo dõi:

1. **Khi tạo payment URL:**
   ```
   [VNPayService] Building VNPay payment URL for orderId: XXX
   [VNPayService] VNPay payment URL generated successfully
   [CheckoutServlet] Redirecting to VNPay payment URL
   ```

2. **Khi nhận callback:**
   ```
   [VNPayReturnServlet] Processing VNPay return callback
   [VNPayReturnServlet] Response Code: 00
   [VNPayReturnServlet] Order ID (TxnRef): XXX
   [VNPayReturnServlet] VNPay signature verification: SUCCESS
   [VNPayReturnServlet] Payment status updated to PAID successfully
   ```

### Nếu gặp lỗi:

1. **Lỗi "VNPay configuration is incomplete":**
   - Kiểm tra lại `email.properties` đã điền đầy đủ chưa
   - Đảm bảo không có khoảng trắng thừa trong giá trị

2. **Lỗi "Invalid signature":**
   - Kiểm tra `vnp_HashSecret` có đúng không
   - Đảm bảo `vnp_ReturnUrl` khớp với URL thực tế

3. **Lỗi "Cannot connect to VNPay":**
   - Kiểm tra kết nối internet
   - Kiểm tra `vnp_Url` có đúng không
   - Thử truy cập trực tiếp: https://sandbox.vnpayment.vn/

## 📝 Cấu trúc code

### Các file đã tạo/cập nhật:

1. **`VNPayService.java`** (`src/main/java/service/shop/VNPayService.java`)
   - Service xử lý logic VNPay
   - Build payment URL
   - Verify signature

2. **`VNPayReturnServlet.java`** (`src/main/java/controller/VNPayReturnServlet.java`)
   - Servlet xử lý callback từ VNPay
   - Validate signature
   - Update payment status

3. **`CheckoutServlet.java`** (đã cập nhật)
   - Xử lý redirect đến VNPay khi chọn payment method = VNPAY

4. **`CheckoutService.java`** và **`CheckoutServiceImpl.java`** (đã cập nhật)
   - Thêm method `processVNPayPayment()`

5. **`checkout.jsp`** (đã cập nhật)
   - Thay MoMo option bằng VNPay option

6. **`email.properties`** (đã cập nhật)
   - Thêm cấu hình VNPay

## 🚀 Deploy lên production

Khi deploy lên production:

1. **Đăng ký tài khoản VNPay Production:**
   - Liên hệ VNPay để đăng ký merchant account production
   - Lấy TMN Code và Hash Secret mới (khác với sandbox)

2. **Cập nhật email.properties:**
   ```properties
   vnp_TmnCode=PRODUCTION_TMN_CODE
   vnp_HashSecret=PRODUCTION_HASH_SECRET
   vnp_Url=https://www.vnpayment.vn/paymentv2/vpcpay.html
   vnp_ReturnUrl=https://yourdomain.com/Gym_Manager_System/vnpay-return
   ```

3. **Đảm bảo HTTPS:**
   - VNPay production yêu cầu HTTPS
   - Cấu hình SSL certificate cho domain

## 📚 Tài liệu tham khảo

- VNPay Documentation: https://sandbox.vnpayment.vn/apis/
- VNPay Integration Guide: https://sandbox.vnpayment.vn/apis/docs/

## ⚠️ Lưu ý bảo mật

1. **KHÔNG commit `email.properties` có thông tin thật lên Git:**
   - Sử dụng `.gitignore` để loại trừ file này
   - Hoặc sử dụng environment variables

2. **Bảo vệ Hash Secret:**
   - Hash Secret là thông tin nhạy cảm
   - Chỉ những người cần thiết mới được biết

3. **Validate signature:**
   - Luôn validate signature từ VNPay callback
   - Không tin tưởng dữ liệu từ client

## ✅ Checklist

- [ ] Đã đăng ký tài khoản VNPay Sandbox
- [ ] Đã lấy TMN Code và Hash Secret
- [ ] Đã cập nhật `email.properties`
- [ ] Đã build và chạy ứng dụng
- [ ] Đã test thanh toán thành công
- [ ] Đã test thanh toán thất bại
- [ ] Đã kiểm tra log không có lỗi
- [ ] Đã kiểm tra database được cập nhật đúng

---

**Chúc bạn tích hợp thành công! 🎉**

