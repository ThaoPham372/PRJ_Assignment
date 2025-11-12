package service.shop;

import Utils.ConfigManager;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * VNPay Payment Service
 * Handles VNPay payment URL generation and signature verification
 * Follows Single Responsibility Principle - handles VNPay integration logic
 */
public class VNPayService {
    private static final Logger LOGGER = Logger.getLogger(VNPayService.class.getName());
    
    private final ConfigManager configManager;
    private final String vnp_TmnCode;
    private final String vnp_HashSecret;
    private final String vnp_Url;
    private final String vnp_ReturnUrl;
    
    public VNPayService() {
        this.configManager = ConfigManager.getInstance();
        this.vnp_TmnCode = configManager.getProperty("vnp_TmnCode");
        // Trim HashSecret to remove any whitespace
        String hashSecret = configManager.getProperty("vnp_HashSecret");
        this.vnp_HashSecret = hashSecret != null ? hashSecret.trim() : null;
        this.vnp_Url = configManager.getProperty("vnp_Url", "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");
        this.vnp_ReturnUrl = configManager.getProperty("vnp_ReturnUrl");
        
        // Validate required configuration
        if (vnp_TmnCode == null || vnp_TmnCode.trim().isEmpty()) {
            LOGGER.warning("[VNPayService] WARNING: vnp_TmnCode is missing in email.properties");
        }
        if (vnp_HashSecret == null || vnp_HashSecret.trim().isEmpty()) {
            LOGGER.warning("[VNPayService] WARNING: vnp_HashSecret is missing in email.properties");
        }
        if (vnp_ReturnUrl == null || vnp_ReturnUrl.trim().isEmpty()) {
            LOGGER.warning("[VNPayService] WARNING: vnp_ReturnUrl is missing in email.properties");
        }
    }
    
    /**
     * Build VNPay payment URL
     * @param orderId Order ID
     * @param amount Payment amount (in VND)
     * @param orderInfo Order description
     * @param ipAddress Client IP address
     * @param returnUrl Return URL for callback (if null, use from config)
     * @return VNPay payment URL
     */
    public String buildPaymentUrl(Integer orderId, Long amount, String orderInfo, String ipAddress, String returnUrl) {
        LOGGER.info("Building VNPay payment URL for orderId: " + orderId + ", amount: " + amount);
        
        try {
            // Validate configuration
            if (vnp_TmnCode == null || vnp_HashSecret == null) {
                throw new IllegalStateException("VNPay configuration is incomplete. vnp_TmnCode and vnp_HashSecret are required in email.properties");
            }
            
            // VNPay parameters
            Map<String, String> vnp_Params = new TreeMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // Convert to cents (VNPay requirement)
            vnp_Params.put("vnp_CurrCode", "VND");
            
            // Order information
            vnp_Params.put("vnp_TxnRef", String.valueOf(orderId));
            // VNPay OrderInfo: Remove ALL special characters including # to avoid signature issues
            // VNPay is very strict about OrderInfo format
            String safeOrderInfo = orderInfo != null ? orderInfo : "Thanh toan don hang " + orderId;
            // Remove ALL special characters - only keep letters, numbers, and spaces
            safeOrderInfo = safeOrderInfo.replaceAll("[^a-zA-Z0-9\\s]", "");
            // Replace multiple spaces with single space and trim
            safeOrderInfo = safeOrderInfo.replaceAll("\\s+", " ").trim();
            // Limit length to 255 characters (VNPay requirement)
            if (safeOrderInfo.length() > 255) {
                safeOrderInfo = safeOrderInfo.substring(0, 255);
            }
            vnp_Params.put("vnp_OrderInfo", safeOrderInfo);
            vnp_Params.put("vnp_OrderType", "other");
            
            // Locale and IP
            vnp_Params.put("vnp_Locale", "vn");
            // Convert IPv6 localhost to IPv4, or use provided IP
            String safeIpAddress = ipAddress != null ? ipAddress : "127.0.0.1";
            // VNPay may not accept IPv6 format, convert to IPv4 if needed
            if (safeIpAddress.equals("0:0:0:0:0:0:0:1") || safeIpAddress.equals("::1")) {
                safeIpAddress = "127.0.0.1";
            }
            vnp_Params.put("vnp_IpAddr", safeIpAddress);
            
            // Return URL - Use provided returnUrl or fallback to config
            String finalReturnUrl = (returnUrl != null && !returnUrl.trim().isEmpty()) 
                ? returnUrl.trim() 
                : vnp_ReturnUrl;
            if (finalReturnUrl == null || finalReturnUrl.trim().isEmpty()) {
                throw new IllegalStateException("Return URL is required. Please provide returnUrl parameter or configure vnp_ReturnUrl in email.properties");
            }
            
            // IMPORTANT: Ensure Return URL ends with /vnpay-return, not /auth/login
            if (!finalReturnUrl.endsWith("/vnpay-return")) {
                LOGGER.warning("[VNPayService] WARNING: Return URL does not end with /vnpay-return: " + finalReturnUrl);
                // Fix it by appending /vnpay-return if missing
                if (finalReturnUrl.endsWith("/")) {
                    finalReturnUrl = finalReturnUrl + "vnpay-return";
                } else {
                    finalReturnUrl = finalReturnUrl + "/vnpay-return";
                }
                LOGGER.info("[VNPayService] Fixed Return URL to: " + finalReturnUrl);
            }
            
            vnp_Params.put("vnp_ReturnUrl", finalReturnUrl);
            LOGGER.info("[VNPayService] ========================================");
            LOGGER.info("[VNPayService] VNPay Return URL: " + finalReturnUrl);
            LOGGER.info("[VNPayService] IMPORTANT: This URL must match the URL registered in VNPay merchant portal!");
            LOGGER.info("[VNPayService] ========================================");
            
            // Create date - VNPay requires timezone GMT+7 (Vietnam time)
            TimeZone vnTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
            Calendar cld = Calendar.getInstance(vnTimeZone);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter.setTimeZone(vnTimeZone);
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            
            // Expire date (30 minutes from now) - VNPay requires minimum time
            // Note: According to VNPay docs, expire date is optional but recommended
            // If not set, transaction will expire after default timeout
            Calendar expireCld = (Calendar) cld.clone();
            expireCld.add(Calendar.MINUTE, 30); // Increased to 30 minutes for safety
            String vnp_ExpireDate = formatter.format(expireCld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
            
            // Build query string
            StringBuilder queryUrl = new StringBuilder();
            StringBuilder signData = new StringBuilder();
            
            for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                
                if (value != null && !value.isEmpty()) {
                    // Build query URL
                    queryUrl.append(URLEncoder.encode(key, StandardCharsets.UTF_8.toString()));
                    queryUrl.append("=");
                    queryUrl.append(URLEncoder.encode(value, StandardCharsets.UTF_8.toString()));
                    queryUrl.append("&");
                    
                    // Build sign data (FOLLOW VNPAY DEMO: encode field VALUE before hashing)
                    // Important: VNPay demo uses US-ASCII for URL-encoding in hash data
                    // to avoid signature mismatch we mirror that behavior here.
                    signData.append(key);
                    signData.append("=");
                    signData.append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()));
                    signData.append("&");
                }
            }
            
            // Remove last '&'
            if (queryUrl.length() > 0) {
                queryUrl.setLength(queryUrl.length() - 1);
            }
            if (signData.length() > 0) {
                signData.setLength(signData.length() - 1);
            }
            
            // Log sign data for debugging (DO NOT log in production with real HashSecret)
            String signDataString = signData.toString();
            LOGGER.info("VNPay Sign Data: " + signDataString);
            LOGGER.info("VNPay HashSecret length: " + (vnp_HashSecret != null ? vnp_HashSecret.length() : 0));
            
            // Generate secure hash
            String vnp_SecureHash = hmacSHA512(vnp_HashSecret, signDataString);
            LOGGER.info("VNPay SecureHash: " + vnp_SecureHash);
            
            queryUrl.append("&vnp_SecureHash=").append(vnp_SecureHash);
            
            // Build final URL
            String paymentUrl = vnp_Url + "?" + queryUrl.toString();
            
            LOGGER.info("VNPay payment URL generated successfully");
            LOGGER.info("VNPay CreateDate: " + vnp_CreateDate + ", ExpireDate: " + vnp_ExpireDate);
            LOGGER.info("VNPay ReturnUrl used: " + finalReturnUrl);
            return paymentUrl;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error building VNPay payment URL", e);
            throw new RuntimeException("Không thể tạo URL thanh toán VNPay: " + e.getMessage(), e);
        }
    }
    
    /**
     * Verify VNPay callback signature
     * @param params VNPay callback parameters
     * @return true if signature is valid
     */
    public boolean verifySignature(Map<String, String> params) {
        try {
            if (vnp_HashSecret == null || vnp_HashSecret.trim().isEmpty()) {
                LOGGER.warning("[VNPayService] Cannot verify signature: vnp_HashSecret is missing");
                return false;
            }
            
            // Get SecureHash from params first (before processing)
            String vnp_SecureHash = params.get("vnp_SecureHash");
            if (vnp_SecureHash == null || vnp_SecureHash.isEmpty()) {
                LOGGER.warning("[VNPayService] vnp_SecureHash is missing in callback");
                return false;
            }
            // Trim whitespace from SecureHash
            vnp_SecureHash = vnp_SecureHash.trim();
            
            // IMPORTANT: Only process parameters that start with "vnp_"
            // VNPay may send additional parameters that should not be included in signature
            Map<String, String> vnpParams = new TreeMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key != null && key.startsWith("vnp_")) {
                    // Normalize: trim whitespace and handle null
                    if (value != null) {
                        vnpParams.put(key, value.trim());
                    } else {
                        vnpParams.put(key, "");
                    }
                }
            }
            
            LOGGER.info("[VNPayService] VNPay parameters for signature (before removing SecureHash): " + vnpParams.keySet());
            
            // Remove vnp_SecureHash from params for signature calculation
            vnpParams.remove("vnp_SecureHash");
            
            LOGGER.info("[VNPayService] VNPay parameters for signature (after removing SecureHash): " + vnpParams.keySet());
            
            // Build sign data - MUST match the exact format used when creating payment URL
            // 1. Sort by key (already done by TreeMap)
            // 2. Only include non-empty values
            // 3. Format: key1=value1&key2=value2&...
            StringBuilder signData = new StringBuilder();
            for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                
                // Only include non-null and non-empty values
                // IMPORTANT: Use the trimmed value (already trimmed when adding to vnpParams)
                // IMPORTANT: Mirror VNPay demo behavior by URL-encoding VALUE with US-ASCII before hashing
                if (value != null && !value.trim().isEmpty()) {
                    signData.append(key);
                    signData.append("=");
                    signData.append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()));
                    signData.append("&");
                }
            }
            
            // Remove last '&'
            if (signData.length() > 0) {
                signData.setLength(signData.length() - 1);
            }
            
            String signDataString = signData.toString();
            LOGGER.info("[VNPayService] Callback Sign Data: " + signDataString);
            
            // Calculate hash using HMAC SHA512 (same algorithm as when creating URL)
            String calculatedHash = hmacSHA512(vnp_HashSecret, signDataString);
            
            // Compare (case-insensitive comparison as VNPay may return uppercase)
            boolean isValid = calculatedHash.equalsIgnoreCase(vnp_SecureHash);
            
            if (isValid) {
                LOGGER.info("[VNPayService] Signature verification: SUCCESS");
            } else {
                LOGGER.warning("[VNPayService] Signature verification: FAILED");
                LOGGER.warning("[VNPayService] Calculated Hash: " + calculatedHash);
                LOGGER.warning("[VNPayService] Received Hash:   " + vnp_SecureHash);
                LOGGER.warning("[VNPayService] Sign Data used:  " + signDataString);
            }
            
            return isValid;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[VNPayService] Error verifying VNPay signature", e);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Generate HMAC SHA512 hash for VNPay signature
     * VNPay uses HMAC-SHA512 for signature generation
     * @param secret Secret key
     * @param data Data to hash
     * @return HMAC SHA512 hash
     */
    private String hmacSHA512(String secret, String data) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA512");
            javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
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
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating HMAC SHA512", e);
            throw new RuntimeException("Cannot generate HMAC SHA512: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get client IP address from request
     * @param request HttpServletRequest
     * @return IP address
     */
    public static String getClientIpAddress(jakarta.servlet.http.HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}

