package com.gym.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.model.ai.ChatResponse;
import com.gym.model.ai.GeminiModels.*;
import com.gym.util.ConfigUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Service để tương tác với Gemini AI API
 */
public class GeminiAIService {
    
    private static final String API_KEY = ConfigUtil.getProperty("GEMINI_API_KEY");
    private static final String API_URL = 
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public GeminiAIService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.objectMapper = new ObjectMapper();
        
        // Validate API key
        if (API_KEY == null || API_KEY.trim().isEmpty() || !API_KEY.startsWith("AIza")) {
            System.err.println("⚠️  CẢNH BÁO: API Key không hợp lệ! Vui lòng kiểm tra file db.properties");
            System.err.println("API Key hiện tại: " + (API_KEY != null ? API_KEY.substring(0, Math.min(10, API_KEY.length())) + "..." : "null"));
        }
    }
    
    /**
     * Gửi tin nhắn đến Gemini AI và nhận phản hồi
     * @param userMessage tin nhắn của người dùng
     * @return ChatResponse chứa phản hồi từ AI
     */
    public ChatResponse sendMessage(String userMessage) {
        try {
            // Validate input
            if (userMessage == null || userMessage.trim().isEmpty()) {
                return ChatResponse.error("Tin nhắn không được để trống");
            }
            
            // Build request payload
            String requestBody = buildGeminiPayload(userMessage.trim());
            
            // Debug logging
            System.out.println("=== GEMINI API DEBUG ===");
            System.out.println("API URL: " + API_URL);
            System.out.println("Request Body: " + requestBody);
            System.out.println("API Key (first 10 chars): " + API_KEY.substring(0, Math.min(10, API_KEY.length())) + "...");
            
            // Create HTTP request
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("X-goog-api-key", API_KEY)
                    .timeout(Duration.ofSeconds(60))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();
            
            // Send request
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, 
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            
            // Log response for debugging
            System.out.println("Gemini API Status: " + httpResponse.statusCode());
            System.out.println("Gemini API Response: " + httpResponse.body());
            System.out.println("=== END DEBUG ===");
            
            // Process response
            if (httpResponse.statusCode() == 200) {
                String aiResponse = parseGeminiResponse(httpResponse.body());
                return ChatResponse.success(aiResponse);
            } else {
                String errorMsg = "Lỗi khi gọi Gemini API. Mã lỗi: " + httpResponse.statusCode();
                String responseBody = httpResponse.body();
                System.err.println(errorMsg + " - Response body: " + responseBody);
                
                // Parse error response để có thông báo lỗi rõ ràng hơn
                String userFriendlyError = parseErrorResponse(responseBody, httpResponse.statusCode());
                return ChatResponse.error(userFriendlyError);
            }
            
        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            e.printStackTrace();
            return ChatResponse.error("Lỗi hệ thống: " + e.getMessage());
        }
    }
    
    /**
     * Tạo payload cho Gemini API
     */
    private String buildGeminiPayload(String userMessage) throws Exception {
        String gymInfo = ConfigUtil.getAllGymInfo();
        
        String systemPrompt = 
            "Bạn là GYMFIT AI, trợ lý ảo thông minh của phòng tập GYMFIT. " +
            "Hãy sử dụng thông tin dưới đây về phòng gym khi trả lời các câu hỏi liên quan.\n\n" +
            "===== THÔNG TIN PHÒNG GYM =====\n" +
            gymInfo + "\n\n" +
            "===== HƯỚNG DẪN TRẢ LỜI =====\n" +
            "• Nếu câu hỏi liên quan đến GYMFIT, hãy trả lời dựa trên thông tin trên.\n" +
            "• Nếu là câu hỏi chung về tập luyện, dinh dưỡng, sức khỏe, hãy trả lời như một chuyên gia thể hình.\n" +
            "• Luôn thân thiện, nhiệt tình và chuyên nghiệp.\n" +
            "• Trả lời bằng tiếng Việt.\n" +
            "• Nếu không biết thông tin cụ thể, hãy khuyên khách hàng liên hệ hotline.\n\n" +
            "Câu hỏi của khách hàng: " + userMessage;
        
        Part part = new Part(systemPrompt);
        Content content = new Content(new Part[]{part});
        GeminiRequest geminiRequest = new GeminiRequest(new Content[]{content});
        
        return objectMapper.writeValueAsString(geminiRequest);
    }
    
    /**
     * Parse response từ Gemini API
     */
    private String parseGeminiResponse(String responseBody) {
        try {
            GeminiResponse response = objectMapper.readValue(responseBody, GeminiResponse.class);
            
            if (response != null && 
                response.getCandidates() != null && 
                response.getCandidates().length > 0 &&
                response.getCandidates()[0].getContent() != null &&
                response.getCandidates()[0].getContent().getParts() != null &&
                response.getCandidates()[0].getContent().getParts().length > 0) {
                
                return response.getCandidates()[0].getContent().getParts()[0].getText();
            }
        } catch (Exception e) {
            System.err.println("Error parsing Gemini response: " + e.getMessage());
        }
        
        return "Xin lỗi, tôi không thể xử lý câu hỏi của bạn lúc này. Vui lòng thử lại sau hoặc liên hệ hotline: " + 
               ConfigUtil.getGymInfo("hotline");
    }
    
    /**
     * Parse error response để hiển thị thông báo lỗi thân thiện với người dùng
     */
    private String parseErrorResponse(String responseBody, int statusCode) {
        try {
            if (responseBody != null && responseBody.contains("error")) {
                // Cố gắng parse JSON error response
                var errorNode = objectMapper.readTree(responseBody);
                if (errorNode.has("error") && errorNode.get("error").has("message")) {
                    String apiError = errorNode.get("error").get("message").asText();
                    return "Lỗi API: " + apiError;
                }
            }
        } catch (Exception e) {
            System.err.println("Could not parse error response: " + e.getMessage());
        }
        
        // Default error messages based on status code
        switch (statusCode) {
            case 400:
                return "Yêu cầu không hợp lệ. Vui lòng thử lại với câu hỏi khác.";
            case 401:
                return "API Key không hợp lệ. Vui lòng liên hệ quản trị viên.";
            case 403:
                return "Không có quyền truy cập API. Vui lòng kiểm tra cấu hình.";
            case 429:
                return "Quá nhiều yêu cầu. Vui lòng thử lại sau ít phút.";
            case 500:
                return "Lỗi server API. Vui lòng thử lại sau.";
            default:
                return "Lỗi khi gọi AI API (Mã: " + statusCode + "). Vui lòng thử lại.";
        }
    }
}
