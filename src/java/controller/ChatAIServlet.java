package controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.ConfigUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@WebServlet("/ChatAIServlet")
public class ChatAIServlet extends HttpServlet {

    private static final String API_KEY = ConfigUtil.getProperty("GEMINI_API_KEY");
private static final String API_URL =
    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String userMessage = "";
        try (Reader reader = new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)) {
            RequestPayload payload = gson.fromJson(reader, RequestPayload.class);
            userMessage = (payload != null && payload.getMessage() != null) ? payload.getMessage().trim() : "";
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
            return;
        }

        if (userMessage.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Message is empty");
            return;
        }

        try {
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
                String aiResponse = parseGeminiResponse(httpResponse.body());
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(gson.toJson(new AIResponse(aiResponse)));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(gson.toJson(new AIResponse("Lỗi khi gọi Gemini API. Code: "
                        + httpResponse.statusCode())));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(gson.toJson(new AIResponse("Lỗi máy chủ: " + e.getMessage())));
        }
    }

 private String buildGeminiPayload(String userMessage) {
    String gymInfo = loadGymInfo();

    String systemPrompt =
        "Bạn là GymFit AI, trợ lý ảo của phòng tập GymFit. "
      + "Hãy sử dụng thông tin dưới đây về phòng gym khi trả lời các câu hỏi liên quan.\n\n"
      + "===== THÔNG TIN PHÒNG GYM =====\n"
      + gymInfo + "\n\n"
      + "===== HƯỚNG DẪN TRẢ LỜI =====\n"
      + "Nếu câu hỏi liên quan đến phòng GymFit, hãy trả lời dựa trên thông tin trên. "
      + "Nếu là câu hỏi chung về tập luyện, hãy trả lời như chuyên gia thể hình.\n\n"
      + "Câu hỏi của khách: " + userMessage;

    RequestPayload.Part part = new RequestPayload.Part(systemPrompt);
    RequestPayload.Content content = new RequestPayload.Content(new RequestPayload.Part[]{part});
    RequestPayload.GeminiRequest geminiRequest =
        new RequestPayload.GeminiRequest(new RequestPayload.Content[]{content});

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

    // --- JSON Model Classes ---
    private static class RequestPayload {

        private String message;

        public String getMessage() {
            return message;
        }

        static class GeminiRequest {

            Content[] contents;

            GeminiRequest(Content[] c) {
                this.contents = c;
            }
        }

        static class Content {

            Part[] parts;

            Content(Part[] p) {
                this.parts = p;
            }
        }

        static class Part {

            String text;

            Part(String t) {
                this.text = t;
            }
        }
    }

    private static class GeminiResponse {

        Candidate[] candidates;

        static class Candidate {

            Content content;
        }

        static class Content {

            Part[] parts;
        }

        static class Part {

            String text;
        }
    }

    private static class AIResponse {

        String reply;

        AIResponse(String r) {
            this.reply = r;
        }
    }
    
  private String loadGymInfo() {
    StringBuilder sb = new StringBuilder();
    try (InputStream is = getClass().getClassLoader().getResourceAsStream("info.properties")) {
        if (is != null) {
            Properties p = new Properties();
            p.load(new InputStreamReader(is, StandardCharsets.UTF_8));

            sb.append("Tên phòng: ").append(p.getProperty("ten", "Chưa có")).append("\n");
            sb.append("Địa chỉ: ").append(p.getProperty("diachi", "Chưa có")).append("\n");
            sb.append("Giờ mở cửa: ").append(p.getProperty("thoigian", "Chưa có")).append("\n");
            sb.append("PT nổi bật: ").append(p.getProperty("pt", "Chưa có")).append("\n");
            sb.append("Các gói tập: ").append(p.getProperty("goi", "Chưa có")).append("\n");
            sb.append("Ưu đãi: ").append(p.getProperty("uudai", "Chưa có")).append("\n");
            sb.append("Hotline: ").append(p.getProperty("hotline", "Chưa có"));
        } else {
            System.err.println("⚠️ Không tìm thấy file info.properties trong classpath!");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return sb.toString();
}

}
