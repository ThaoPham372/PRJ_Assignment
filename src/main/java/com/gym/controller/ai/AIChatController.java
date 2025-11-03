package com.gym.controller.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.model.ai.ChatRequest;
import com.gym.model.ai.ChatResponse;
import com.gym.service.ai.GeminiAIService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Controller để xử lý requests cho AI chatbot
 */
@WebServlet("/api/ai/chat")
public class AIChatController extends HttpServlet {

    private GeminiAIService aiService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        this.aiService = new GeminiAIService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Set character encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        
        // Enable CORS (nếu cần)
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try {
            // Parse request body
            ChatRequest chatRequest;
            try (InputStreamReader reader = new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)) {
                chatRequest = objectMapper.readValue(reader, ChatRequest.class);
            }

            // Validate request
            if (chatRequest == null || chatRequest.getMessage() == null || chatRequest.getMessage().trim().isEmpty()) {
                sendErrorResponse(response, "Tin nhắn không được để trống", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // Log request for debugging
            System.out.println("AI Chat Request: " + chatRequest.getMessage());

            // Process message through AI service
            ChatResponse chatResponse = aiService.sendMessage(chatRequest.getMessage());

            // Send response
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), chatResponse);

        } catch (Exception e) {
            System.err.println("Error processing AI chat request: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(response, "Lỗi hệ thống: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle CORS preflight requests
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage, int statusCode) 
            throws IOException {
        response.setStatus(statusCode);
        ChatResponse errorResponse = ChatResponse.error(errorMessage);
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
