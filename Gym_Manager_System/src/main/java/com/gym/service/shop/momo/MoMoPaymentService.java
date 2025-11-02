package com.gym.service.shop.momo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service for integrating with MoMo Payment Gateway
 * 
 * HƯỚNG DẪN THIẾT LẬP:
 * 
 * 1. Đăng ký tài khoản MoMo Business:
 *    - Truy cập: https://business.momo.vn/
 *    - Đăng ký tài khoản và tạo app để lấy PartnerCode, AccessKey, SecretKey
 * 
 * 2. Cấu hình trong file properties hoặc environment variables:
 *    - MOMO_PARTNER_CODE: Mã đối tác từ MoMo
 *    - MOMO_ACCESS_KEY: Access Key từ MoMo
 *    - MOMO_SECRET_KEY: Secret Key từ MoMo
 *    - MOMO_ENVIRONMENT: "sandbox" hoặc "production"
 * 
 * 3. Sandbox URL: https://test-payment.momo.vn/v2/gateway/api/create
 *    Production URL: https://payment.momo.vn/v2/gateway/api/create
 * 
 * 4. Callback URL: Cần cung cấp cho MoMo khi đăng ký
 *    - Ví dụ: https://yourdomain.com/payment/momo/callback
 */
public class MoMoPaymentService {
    private static final Logger LOGGER = Logger.getLogger(MoMoPaymentService.class.getName());
    
    // TODO: Load from configuration file or environment variables
    private static final String PARTNER_CODE = System.getenv("MOMO_PARTNER_CODE") != null 
            ? System.getenv("MOMO_PARTNER_CODE") : "MOMO_PARTNER_CODE";
    private static final String ACCESS_KEY = System.getenv("MOMO_ACCESS_KEY") != null 
            ? System.getenv("MOMO_ACCESS_KEY") : "MOMO_ACCESS_KEY";
    private static final String SECRET_KEY = System.getenv("MOMO_SECRET_KEY") != null 
            ? System.getenv("MOMO_SECRET_KEY") : "MOMO_SECRET_KEY";
    private static final String ENVIRONMENT = System.getenv("MOMO_ENVIRONMENT") != null 
            ? System.getenv("MOMO_ENVIRONMENT") : "sandbox";
    
    // Sandbox
    private static final String MOMO_SANDBOX_URL = "https://test-payment.momo.vn/v2/gateway/api/create";
    // Production
    private static final String MOMO_PRODUCTION_URL = "https://payment.momo.vn/v2/gateway/api/create";
    
    // Callback URL - TODO: Cần cập nhật theo domain thật của bạn
    // Hoặc load từ environment/config
    private static final String CALLBACK_URL = System.getenv("MOMO_CALLBACK_URL") != null
            ? System.getenv("MOMO_CALLBACK_URL")
            : "http://localhost:8080/testmuaban/payment/momo/callback";
    private static final String RETURN_URL = System.getenv("MOMO_RETURN_URL") != null
            ? System.getenv("MOMO_RETURN_URL")
            : "http://localhost:8080/testmuaban/order/success";
    
    /**
     * Create payment request and return payment URL
     * @param orderId MoMo order ID (usually order number)
     * @param amount Payment amount
     * @param orderInfo Order description
     * @param extraData Extra data (can be order ID)
     * @return Payment URL for redirect
     */
    public String createPayment(String orderId, java.math.BigDecimal amount, String orderInfo, String extraData) {
        try {
            // Convert amount to long (MoMo uses long, amount in VND)
            long amountLong = amount.multiply(java.math.BigDecimal.valueOf(1000)).longValue();
            
            // Request ID
            String requestId = String.valueOf(System.currentTimeMillis());
            String orderIdFormatted = orderId;
            
            // Create raw hash
            String rawHash = "accessKey=" + ACCESS_KEY +
                           "&amount=" + amountLong +
                           "&extraData=" + extraData +
                           "&ipnUrl=" + CALLBACK_URL +
                           "&orderId=" + orderIdFormatted +
                           "&orderInfo=" + orderInfo +
                           "&partnerCode=" + PARTNER_CODE +
                           "&redirectUrl=" + RETURN_URL +
                           "&requestId=" + requestId +
                           "&requestType=captureWallet";
            
            // Sign with HMAC SHA256
            String signature = signData(rawHash, SECRET_KEY);
            
            // Build JSON request using Map
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("partnerCode", PARTNER_CODE);
            requestData.put("partnerName", "Gym Shop");
            requestData.put("storeId", "MomoTestStore");
            requestData.put("requestId", requestId);
            requestData.put("amount", amountLong);
            requestData.put("orderId", orderIdFormatted);
            requestData.put("orderInfo", orderInfo);
            requestData.put("redirectUrl", RETURN_URL);
            requestData.put("ipnUrl", CALLBACK_URL);
            requestData.put("lang", "vi");
            requestData.put("extraData", extraData);
            requestData.put("requestType", "captureWallet");
            requestData.put("signature", signature);
            
            // Convert to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequest = objectMapper.writeValueAsString(requestData);
            
            // Send request to MoMo
            String apiUrl = "sandbox".equals(ENVIRONMENT) ? MOMO_SANDBOX_URL : MOMO_PRODUCTION_URL;
            String response = sendRequest(apiUrl, jsonRequest);
            
            // Parse response
            @SuppressWarnings("unchecked")
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            int resultCode = (Integer) responseMap.get("resultCode");
            
            if (resultCode == 0) {
                // Success - return payment URL
                return (String) responseMap.get("payUrl");
            } else {
                String message = (String) responseMap.get("message");
                LOGGER.log(Level.SEVERE, "MoMo payment creation failed: " + message);
                throw new RuntimeException("MoMo payment failed: " + message);
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating MoMo payment", e);
            throw new RuntimeException("Failed to create MoMo payment: " + e.getMessage(), e);
        }
    }
    
    /**
     * Handle MoMo payment callback (IPN - Instant Payment Notification)
     * This should be called from a servlet that receives callback from MoMo
     * @param callbackData JSON data from MoMo callback
     * @return Map containing orderId and payment status
     */
    public Map<String, Object> handleCallback(String callbackData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> data = objectMapper.readValue(callbackData, Map.class);
            
            // Verify signature
            String signature = (String) data.get("signature");
            String rawHash = buildRawHash(data);
            
            if (!verifySignature(rawHash, signature)) {
                LOGGER.log(Level.WARNING, "Invalid MoMo callback signature");
                throw new RuntimeException("Invalid signature");
            }
            
            // Extract order info
            Map<String, Object> result = new HashMap<>();
            result.put("orderId", data.get("orderId"));
            result.put("resultCode", data.get("resultCode"));
            result.put("message", data.get("message"));
            
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling MoMo callback", e);
            throw new RuntimeException("Failed to handle callback: " + e.getMessage(), e);
        }
    }
    
    /**
     * Build raw hash string for signature verification
     */
    @SuppressWarnings("unchecked")
    private String buildRawHash(Map<String, Object> data) {
        return "accessKey=" + data.get("accessKey") +
               "&amount=" + data.get("amount") +
               "&extraData=" + data.get("extraData") +
               "&message=" + data.get("message") +
               "&orderId=" + data.get("orderId") +
               "&orderInfo=" + data.get("orderInfo") +
               "&orderType=" + data.get("orderType") +
               "&partnerCode=" + data.get("partnerCode") +
               "&payType=" + data.get("payType") +
               "&requestId=" + data.get("requestId") +
               "&responseTime=" + data.get("responseTime") +
               "&resultCode=" + data.get("resultCode") +
               "&transId=" + data.get("transId");
    }
    
    /**
     * Verify payment callback signature
     * @param data Raw data string
     * @param signature Signature from MoMo
     * @return true if signature is valid
     */
    public boolean verifySignature(String data, String signature) {
        try {
            String calculatedSignature = signData(data, SECRET_KEY);
            return calculatedSignature.equals(signature);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error verifying signature", e);
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
        byte[] hashBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes);
    }
    
    /**
     * Send HTTP POST request to MoMo API
     */
    private String sendRequest(String urlString, String jsonData) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        
        // Send request
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.writeBytes(jsonData);
            wr.flush();
        }
        
        // Read response
        int responseCode = conn.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(
            responseCode == 200 ? conn.getInputStream() : conn.getErrorStream(),
            StandardCharsets.UTF_8
        ));
        
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        if (responseCode != 200) {
            throw new RuntimeException("MoMo API error: " + responseCode + " - " + response.toString());
        }
        
        return response.toString();
    }
}

