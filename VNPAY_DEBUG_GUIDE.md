# Hướng dẫn Debug Lỗi Chữ Ký VNPay

## 🔍 Cách Debug Lỗi "Sai Chữ Ký"

Khi gặp lỗi "Sai chữ ký" từ VNPay, hãy làm theo các bước sau:

### Bước 1: Xem Log Khi Tạo Payment URL

Khi checkout, tìm các log sau trong console:

```
VNPay Sign Data: vnp_Amount=...&vnp_Command=...&...
VNPay SecureHash: [hash_128_characters]
```

**Lưu lại:**
- Sign Data khi tạo URL
- SecureHash khi tạo URL

### Bước 2: Xem Log Khi Nhận Callback

Khi VNPay redirect về `/vnpay-return`, tìm các log sau:

```
[VNPayReturnServlet] Full Query String: ...
[VNPayReturnServlet] Parameter: vnp_Amount = ...
[VNPayReturnServlet] Parameter: vnp_ResponseCode = ...
...
[VNPayService] VNPay parameters for signature (before removing SecureHash): [...]
[VNPayService] VNPay parameters for signature (after removing SecureHash): [...]
[VNPayService] Callback Sign Data: ...
[VNPayService] Calculated Hash: ...
[VNPayService] Received Hash: ...
[VNPayService] Signature verification: SUCCESS/FAILED
```

### Bước 3: So Sánh Sign Data

**So sánh 2 Sign Data:**

1. **Sign Data khi tạo URL** (từ Bước 1)
2. **Callback Sign Data** (từ Bước 2)

**Chúng PHẢI khác nhau vì:**
- Callback có thêm các tham số mới từ VNPay:
  - `vnp_ResponseCode` (00 = thành công)
  - `vnp_TransactionNo` (Mã giao dịch VNPay)
  - `vnp_BankCode` (Mã ngân hàng)
  - `vnp_PayDate` (Thời gian thanh toán)
  - `vnp_TransactionStatus` (Trạng thái giao dịch)
  - Có thể có thêm các tham số khác

**Nhưng các tham số gốc PHẢI giống nhau:**
- `vnp_Amount`
- `vnp_TxnRef`
- `vnp_OrderInfo`
- `vnp_TmnCode`
- etc.

### Bước 4: Kiểm Tra Các Vấn Đề Thường Gặp

#### ❌ Vấn đề 1: HashSecret không đúng
**Triệu chứng:**
- Calculated Hash và Received Hash hoàn toàn khác nhau
- Sign Data trông đúng

**Giải pháp:**
- Kiểm tra `email.properties`: `vnp_HashSecret` có đúng không?
- Đảm bảo không có khoảng trắng thừa
- Copy lại từ VNPay merchant portal

#### ❌ Vấn đề 2: Tham số bị thiếu hoặc thừa
**Triệu chứng:**
- Callback Sign Data thiếu một số tham số
- Hoặc có tham số không phải của VNPay

**Giải pháp:**
- Kiểm tra log: `VNPay parameters for signature (after removing SecureHash)`
- Đảm bảo chỉ có các tham số bắt đầu bằng `vnp_`
- Đảm bảo không có tham số duplicate

#### ❌ Vấn đề 3: Giá trị tham số bị thay đổi
**Triệu chứng:**
- `vnp_OrderInfo` khác nhau giữa request và callback
- `vnp_Amount` khác nhau

**Giải pháp:**
- Kiểm tra xem VNPay có modify giá trị không
- Đảm bảo OrderInfo không có ký tự đặc biệt

#### ❌ Vấn đề 4: Encoding/Decoding
**Triệu chứng:**
- Sign Data có ký tự lạ
- URL encoding không đúng

**Giải pháp:**
- Đảm bảo Servlet container đã decode parameters
- Không encode lại khi build sign data

### Bước 5: Test Case

**Test với thẻ thành công:**
- Số thẻ: `9704198526191432198`
- OTP: `123456`
- Kỳ vọng: `vnp_ResponseCode = 00`

**Kiểm tra log:**
```
[VNPayService] Signature verification: SUCCESS
[VNPayReturnServlet] Payment successful for order: XX
```

## 📋 Checklist Debug

- [ ] Đã xem log Sign Data khi tạo URL
- [ ] Đã xem log Callback Sign Data
- [ ] Đã so sánh Calculated Hash vs Received Hash
- [ ] Đã kiểm tra HashSecret trong email.properties
- [ ] Đã kiểm tra các tham số trong callback
- [ ] Đã kiểm tra không có duplicate parameters
- [ ] Đã kiểm tra OrderInfo không có ký tự đặc biệt

## 🔧 Các Lệnh Hữu Ích

### Xem log real-time (nếu dùng Tomcat):
```bash
tail -f $CATALINA_HOME/logs/catalina.out | grep VNPay
```

### Tìm tất cả log VNPay:
```bash
grep -i "VNPay" $CATALINA_HOME/logs/catalina.out
```

## 📞 Liên Hệ Hỗ Trợ

Nếu vẫn không giải quyết được, hãy gửi:
1. Log Sign Data khi tạo URL
2. Log Callback Sign Data
3. Log Calculated Hash và Received Hash
4. Log tất cả parameters từ callback

---

**Lưu ý:** Không share HashSecret trong log khi gửi cho người khác!

