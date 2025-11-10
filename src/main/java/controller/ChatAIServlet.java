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

        String userMessage = extractUserMessage(request);
        
        if (userMessage == null || userMessage.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Message is empty");
            return;
        }

        try {
            AIResponse aiResponse = chatAIService.getAIResponse(userMessage);
            response.getWriter().write(gson.toJson(aiResponse));
        } catch (Exception e) {
            System.err.println("[ChatAIServlet] Error: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new AIResponse("Lỗi máy chủ: " + e.getMessage())));
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