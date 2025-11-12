package controller;

import com.google.gson.Gson;
import model.ai.AIResponse;
import model.ai.RequestPayload;
import service.ChatAIService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "ChatAIServlet", urlPatterns = {"/chat-ai", "/ChatAIServlet"})
public class ChatAIServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private ChatAIService chatAIService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        chatAIService = new ChatAIService();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        try {
            String userMessage = extractUserMessage(request);
            
            if (userMessage == null || userMessage.trim().isEmpty()) {
                response.getWriter().write(gson.toJson(
                    new AIResponse("Hãy nhập câu hỏi của bạn.")
                ));
                return;
            }

            // Giới hạn độ dài tin nhắn
            if (userMessage.length() > 500) {
                response.getWriter().write(gson.toJson(
                    new AIResponse("Tin nhắn quá dài. Vui lòng nhập tin nhắn ngắn hơn 500 ký tự.")
                ));
                return;
            }

            // Lấy memberId từ session nếu user đã đăng nhập
            Integer memberId = null;
            Object memberObj = request.getSession().getAttribute("member");
            if (memberObj != null && memberObj instanceof model.Member) {
                memberId = ((model.Member) memberObj).getId();
            }

            // Gọi service với member context
            AIResponse aiResponse = chatAIService.getAIResponseWithMemberContext(userMessage, memberId);
            response.getWriter().write(gson.toJson(aiResponse));
            
        } catch (Exception e) {
            System.err.println("[ChatAIServlet] Error: " + e.getMessage());
            // Không in stack trace ra log để tránh spam
            
            response.setStatus(HttpServletResponse.SC_OK); // Vẫn trả 200 OK
            response.getWriter().write(gson.toJson(
                new AIResponse("Hệ thống đang bị gián đoạn, hãy thử lại sau nhé...")
            ));
        }
    }
    
    private String extractUserMessage(HttpServletRequest request) {
        try (Reader reader = new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)) {
            RequestPayload payload = gson.fromJson(reader, RequestPayload.class);
            return (payload != null && payload.getMessage() != null) ? payload.getMessage().trim() : "";
        } catch (Exception e) {
            System.err.println("[ChatAIServlet] Error parsing JSON: " + e.getMessage());
            return null;
        }
    }
    
}