package com.gym.service.shop;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for MoMo Payment Gateway Integration
 * 
 * HƯỚNG DẪN THIẾT LẬP:
 * 1. Đăng ký tài khoản tại: https://developers.momo.vn/
 * 2. Tạo app và lấy:
 *    - Partner Code (Mã đối tác)
 *    - Access Key
 *    - Secret Key
 * 3. Cấu hình trong file properties hoặc environment variables:
 *    - momo.partner.code
 *    - momo.access.key
 *    - momo.secret.key
 *    - momo.api.endpoint (https://test-payment.momo.vn/v2/gateway/api/create cho test)
 *    - momo.return.url (URL callback sau khi thanh toán)
 * 4. Đảm bảo callback URL được whitelist trong MoMo Dashboard
 */
public class MoMoPaymentService {
    
    // Cấu hình MoMo - Đổi các giá trị này sau khi đăng ký
    private static final String PARTNER_CODE = "MOMOBKUN20180529"; // Thay bằng Partner Code của bạn
    private static final String ACCESS_KEY = "klm05TvNBzhg7h7j"; // Thay bằng Access Key của bạn
    private static final String SECRET_KEY = "at67qH6mk8w5Y1nAyMoYKMWACiEi2bsa"; // Thay bằng Secret Key của bạn
    
    // Test environment
    private static final String API_ENDPOINT = "https://test-payment.momo.vn/v2/gateway/api/create";
    
    // Production environment (sử dụng khi go live)
    // private static final String API_ENDPOINT = "https://payment.momo.vn/v2/gateway/api/create";
    
    /**
     * Create MoMo payment URL
     * 
     * @param orderNumber Order number
     * @param amount Total amount (in VND)
     * @param orderInfo Order description
     * @param returnUrl URL to redirect after payment (callback)
     * @param notifyUrl URL for MoMo to notify payment result (webhook)
     * @return MoMo payment URL for redirect
     */
    public String createPaymentRequest(String orderNumber, BigDecimal amount, 
                                       String orderInfo, String returnUrl, String notifyUrl) {
        try {
            // Convert amount to long (MoMo uses VND as integer - smallest unit)
            // If amount is already in VND (e.g., 100000 = 100,000 VND), convert to smallest unit
            // MoMo expects amount in smallest unit: 100000 VND = 100000 (not multiplied by 1000)
            long amountLong = amount.longValue(); // Amount is already in VND (dong)
            
            // Request ID
            String requestId = String.valueOf(System.currentTimeMillis());
            
            // Extra data (optional, base64 encoded)
            String extraData = "";
            
            // Build raw hash string for signature
            String rawHash = "accessKey=" + ACCESS_KEY +
                           "&amount=" + amountLong +
                           "&extraData=" + extraData +
                           "&ipnUrl=" + notifyUrl +
                           "&orderId=" + orderNumber +
                           "&orderInfo=" + orderInfo +
                           "&partnerCode=" + PARTNER_CODE +
                           "&redirectUrl=" + returnUrl +
                           "&requestId=" + requestId +
                           "&requestType=captureWallet";
            
            // Create signature (HMAC SHA256)
            String signature;
            try {
                signature = signData(rawHash, SECRET_KEY);
            } catch (InvalidKeyException e) {
                throw new RuntimeException("Failed to create signature", e);
            }
            
            // Build request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", PARTNER_CODE);
            requestBody.put("partnerName", "GymFit Store");
            requestBody.put("storeId", "MomoTestStore");
            requestBody.put("requestId", requestId);
            requestBody.put("amount", amountLong);
            requestBody.put("orderId", orderNumber);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", returnUrl);
            requestBody.put("ipnUrl", notifyUrl);
            requestBody.put("lang", "vi");
            requestBody.put("extraData", extraData);
            requestBody.put("requestType", "captureWallet");
            requestBody.put("signature", signature);
            
            // Send POST request to MoMo API
            String paymentUrl = sendPaymentRequest(requestBody);
            
            return paymentUrl;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create MoMo payment request: " + e.getMessage(), e);
        }
    }
    
    /**
     * Verify MoMo payment callback signature
     * 
     * @param orderNumber Order number
     * @param amount Amount
     * @param orderInfo Order info
     * @param orderStatus Order status from MoMo
     * @param signature Signature from MoMo
     * @return true if signature is valid
     */
    public boolean verifySignature(String orderNumber, long amount, String orderInfo, 
                                   int orderStatus, String responseTime, String requestId, 
                                   String transId, String message, String extraData, String signature) {
        try {
            String rawHash = "accessKey=" + ACCESS_KEY +
                           "&amount=" + amount +
                           "&extraData=" + (extraData != null ? extraData : "") +
                           "&message=" + (message != null ? message : "") +
                           "&orderId=" + orderNumber +
                           "&orderInfo=" + orderInfo +
                           "&orderStatus=" + orderStatus +
                           "&partnerCode=" + PARTNER_CODE +
                           "&responseTime=" + (responseTime != null ? responseTime : "") +
                           "&requestId=" + (requestId != null ? requestId : "") +
                           "&transId=" + (transId != null ? transId : "");
            
            String expectedSignature = signData(rawHash, SECRET_KEY);
            return expectedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Sign data using HMAC SHA256
     */
    private String signData(String data, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    /**
     * Send HTTP POST request to MoMo API
     * Note: This is a simplified version. In production, use proper HTTP client library
     */
    private String sendPaymentRequest(Map<String, Object> requestBody) throws Exception {
        // Convert Map to JSON (simplified - should use Jackson/Gson in production)
        StringBuilder jsonBuilder = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : requestBody.entrySet()) {
            if (!first) jsonBuilder.append(",");
            jsonBuilder.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                jsonBuilder.append("\"").append(entry.getValue()).append("\"");
            } else {
                jsonBuilder.append(entry.getValue());
            }
            first = false;
        }
        jsonBuilder.append("}");
        
        // Use Java 11+ HttpClient or Apache HttpClient
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(API_ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBuilder.toString()))
                .build();
        
        java.net.http.HttpResponse<String> response = client.send(request, 
                java.net.http.HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            // Parse JSON response to get payUrl
            String responseBody = response.body();
            // Simple JSON parsing (should use Jackson/Gson in production)
            int payUrlIndex = responseBody.indexOf("\"payUrl\"");
            if (payUrlIndex > 0) {
                int start = responseBody.indexOf("\"", payUrlIndex + 8) + 1;
                int end = responseBody.indexOf("\"", start);
                return responseBody.substring(start, end);
            }
        }
        
        throw new RuntimeException("Failed to get payment URL from MoMo. Status: " + response.statusCode());
    }
}

