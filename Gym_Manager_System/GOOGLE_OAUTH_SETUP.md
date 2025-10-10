# Hướng Dẫn Setup Google OAuth

## 🎯 Tổng Quan

Hệ thống đã được tích hợp Google OAuth để cho phép người dùng đăng nhập/đăng ký bằng tài khoản Google. Tuy nhiên, cần cấu hình Google Cloud Console để hoạt động.

## 🔧 Cấu Hình Google Cloud Console

### Bước 1: Tạo Google Cloud Project

1. Truy cập [Google Cloud Console](https://console.cloud.google.com/)
2. Tạo project mới hoặc chọn project hiện có
3. Ghi nhớ Project ID

### Bước 2: Enable Google+ API

1. Vào **APIs & Services** > **Library**
2. Tìm kiếm "Google+ API" hoặc "Google Identity"
3. Click **Enable**

### Bước 3: Tạo OAuth 2.0 Credentials

1. Vào **APIs & Services** > **Credentials**
2. Click **Create Credentials** > **OAuth 2.0 Client IDs**
3. Chọn **Web application**
4. Đặt tên: "GymFit Web Client"
5. **Authorized JavaScript origins**:
   ```
   http://localhost:8080
   http://localhost:8080/Gym_Manager_System
   ```
6. **Authorized redirect URIs**:
   ```
   http://localhost:8080/Gym_Manager_System/auth/google-login
   http://localhost:8080/Gym_Manager_System/auth/google-register
   ```
7. Click **Create**
8. Copy **Client ID** (sẽ cần dùng trong code)

### Bước 4: Cập Nhật Code

Thay thế `YOUR_GOOGLE_CLIENT_ID` trong các file JSP:

**File: `src/main/webapp/views/login.jsp`**
```javascript
google.accounts.id.initialize({
    client_id: 'YOUR_ACTUAL_GOOGLE_CLIENT_ID_HERE', // Thay thế bằng Client ID thực
    callback: handleGoogleResponse,
    auto_select: false,
    cancel_on_tap_outside: true
});
```

**File: `src/main/webapp/views/register.jsp`**
```javascript
google.accounts.id.initialize({
    client_id: 'YOUR_ACTUAL_GOOGLE_CLIENT_ID_HERE', // Thay thế bằng Client ID thực
    callback: handleGoogleResponse,
    auto_select: false,
    cancel_on_tap_outside: true
});
```

## 🔐 Cải Thiện Bảo Mật

### 1. JWT Token Verification

File `GoogleAuthController.java` hiện tại sử dụng mock data. Để production, cần:

1. **Thêm JWT Library** vào `pom.xml`:
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
</dependency>
```

2. **Cập nhật `decodeGoogleToken` method**:
```java
private GoogleUserInfo decodeGoogleToken(String credential) {
    try {
        // Verify JWT token with Google's public keys
        Claims claims = Jwts.parserBuilder()
            .setSigningKeyResolver(new GoogleSigningKeyResolver())
            .build()
            .parseClaimsJws(credential)
            .getBody();
        
        GoogleUserInfo user = new GoogleUserInfo();
        user.setEmail(claims.getSubject());
        user.setName((String) claims.get("name"));
        user.setPicture((String) claims.get("picture"));
        return user;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
```

### 2. Environment Variables

Tạo file `config.properties`:
```properties
# Google OAuth Configuration
google.client.id=YOUR_GOOGLE_CLIENT_ID
google.client.secret=YOUR_GOOGLE_CLIENT_SECRET
google.redirect.uri=http://localhost:8080/Gym_Manager_System/auth/google-callback
```

## 🚀 Testing

### 1. Test Local Development

1. Start server: `mvn tomcat7:run`
2. Truy cập: `http://localhost:8080/Gym_Manager_System/views/login.jsp`
3. Click "Tiếp tục với Google"
4. Chọn tài khoản Google
5. Verify redirect về dashboard

### 2. Test Production

1. Update **Authorized JavaScript origins** trong Google Console:
   ```
   https://yourdomain.com
   https://yourdomain.com/Gym_Manager_System
   ```
2. Update **Authorized redirect URIs**:
   ```
   https://yourdomain.com/Gym_Manager_System/auth/google-login
   https://yourdomain.com/Gym_Manager_System/auth/google-register
   ```

## 🐛 Troubleshooting

### Lỗi Thường Gặp

#### 1. "This app isn't verified"
- **Nguyên nhân**: App chưa được verify bởi Google
- **Giải pháp**: 
  - Click "Advanced" > "Go to [app name] (unsafe)"
  - Hoặc submit app để Google review (cho production)

#### 2. "Error 400: redirect_uri_mismatch"
- **Nguyên nhân**: Redirect URI không khớp với cấu hình
- **Giải pháp**: Kiểm tra lại **Authorized redirect URIs** trong Google Console

#### 3. "Error 403: access_denied"
- **Nguyên nhân**: User từ chối permission
- **Giải pháp**: Bình thường, user có thể thử lại

#### 4. "Invalid client_id"
- **Nguyên nhân**: Client ID không đúng
- **Giải pháp**: Kiểm tra lại Client ID trong code

### Debug Steps

1. **Check Browser Console**:
   ```javascript
   // Mở Developer Tools > Console
   // Kiểm tra lỗi JavaScript
   ```

2. **Check Network Tab**:
   - Xem request/response của Google OAuth
   - Kiểm tra status code

3. **Check Server Logs**:
   ```bash
   # Xem Tomcat logs
   tail -f logs/catalina.out
   ```

## 📱 Mobile Support

### Responsive Design
- Login/Register pages đã responsive
- Google OAuth button hoạt động trên mobile
- Touch-friendly interface

### PWA Support (Optional)
```html
<!-- Thêm vào <head> để support PWA -->
<link rel="manifest" href="/manifest.json">
<meta name="theme-color" content="#141a46">
```

## 🔄 Alternative OAuth Providers

### Facebook Login
```javascript
// Có thể thêm Facebook Login tương tự
FB.login(function(response) {
    if (response.authResponse) {
        // Handle Facebook login
    }
}, {scope: 'email'});
```

### GitHub Login
```javascript
// GitHub OAuth
window.location.href = 'https://github.com/login/oauth/authorize?client_id=YOUR_CLIENT_ID&redirect_uri=YOUR_REDIRECT_URI';
```

## 📊 Analytics & Monitoring

### Google Analytics Integration
```javascript
// Thêm vào login success
gtag('event', 'login', {
    method: 'Google OAuth'
});

// Thêm vào register success
gtag('event', 'sign_up', {
    method: 'Google OAuth'
});
```

### Error Tracking
```javascript
// Track OAuth errors
function trackOAuthError(error) {
    gtag('event', 'exception', {
        description: 'OAuth Error: ' + error,
        fatal: false
    });
}
```

## 🎉 Kết Luận

Google OAuth đã được tích hợp thành công với:

✅ **Modern UI/UX** - Design đồng nhất với hệ thống  
✅ **Security** - JWT token verification (cần implement)  
✅ **Responsive** - Hoạt động trên mọi thiết bị  
✅ **Error Handling** - Xử lý lỗi đầy đủ  
✅ **Session Management** - Tích hợp với session system hiện tại  

**Next Steps:**
1. Cấu hình Google Cloud Console
2. Thay thế Client ID trong code
3. Implement JWT verification (cho production)
4. Test thoroughly trên các browser khác nhau
