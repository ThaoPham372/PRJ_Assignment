package com.gym.service;

import com.google.gson.Gson;
import com.gym.dao.GymInfoDAO;
import com.gym.model.ai.AIResponse;
import com.gym.model.ai.GeminiRequest;
import com.gym.model.ai.GeminiResponse;
import com.gym.model.ai.RequestPayload;
import com.gym.util.ConfigUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ChatAIService {

    private static final String API_KEY = ConfigUtil.getProperty("GEMINI_API_KEY");
    private static final String API_URL
            = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final GymInfoDAO gymInfoDAO = new GymInfoDAO();

    public AIResponse getAIResponse(String userMessage) throws Exception {
        String requestBody = buildGeminiPayload(userMessage);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Gemini status: " + httpResponse.statusCode());
        System.out.println("Gemini body: " + httpResponse.body());

        if (httpResponse.statusCode() == 200) {
            String aiReply = parseGeminiResponse(httpResponse.body());
            return new AIResponse(aiReply);
        } else {
            return new AIResponse("Lỗi khi gọi Gemini API. Code: " + httpResponse.statusCode());
        }
    }

    private String buildGeminiPayload(String userMessage) {
        String gymInfo = gymInfoDAO.loadGymInfo();

        String systemPrompt
                = "Bạn là GymFit AI, trợ lý ảo của phòng tập GymFit. "
                + "Hãy sử dụng thông tin dưới đây về phòng gym khi trả lời các câu hỏi liên quan.\n\n"
                + "===== THÔNG TIN TỪ PHÒNG GYM =====\n"
                + gymInfo + "\n\n"
                + "===== HƯỚNG DẪN TRẢ LỜI =====\n"
                + "Nếu câu hỏi liên quan đến phòng GymFit, hãy trả lời dựa trên thông tin phòng gym cung cấp. "
                + "Kiểu chữ của văn bản thường hết, không in hoa, in đậm, hay font chữ đặc biệt."
                + "Hãy trả lời với ngôn ngữ tự nhiên, thân thiện, văn phong genZ. Trả lời trọng tâm, ngắn gọn, xúc tích"
                + "Nếu là câu hỏi chung về tập luyện, hãy trả lời như chuyên gia thể hình.\n\n"
                + "Câu hỏi của khách: " + userMessage;

        RequestPayload.Part part = new RequestPayload.Part(systemPrompt);
        RequestPayload.Content content = new RequestPayload.Content(new RequestPayload.Part[]{part});
        GeminiRequest geminiRequest = new GeminiRequest(new RequestPayload.Content[]{content});

        return gson.toJson(geminiRequest);
    }

    private String parseGeminiResponse(String body) {
        try {
            GeminiResponse res = gson.fromJson(body, GeminiResponse.class);
            if (res != null && res.candidates != null && res.candidates.length > 0
                    && res.candidates[0].content != null && res.candidates[0].content.parts != null
                    && res.candidates[0].content.parts.length > 0) {
                return res.candidates[0].content.parts[0].text;
            }
        } catch (Exception e) {
            System.err.println("Parse Gemini failed: " + e.getMessage());
        }
        return "Xin lỗi, tôi không nhận được phản hồi hợp lệ từ AI.";
    }
}