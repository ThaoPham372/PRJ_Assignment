package com.gym.controller;

import com.google.gson.Gson;
import com.gym.dao.GymInfoDAO;
import com.gym.model.ai.AIResponse;
import com.gym.model.ai.RequestPayload;
import com.gym.service.ChatAIService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@WebServlet("/ChatAIServlet")
public class ChatAIServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final ChatAIService chatAIService = new ChatAIService();
    private final GymInfoDAO gymInfoDAO = new GymInfoDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String userMessage = "";
        
        //------------đoạn này Vanh add thêm để test-------------//
        //Lấy session hiện có (dùng 'false' để không tạo session mới)
//        HttpSession session = request.getSession(false); 
//
//        // Kiểm tra xem người dùng đã đăng nhập hay chưa
//        boolean isLoggedIn = (session != null && session.getAttribute("isLoggedIn") != null && (Boolean) session.getAttribute("isLoggedIn"));
//
//        if (isLoggedIn) {
//            // NẾU ĐÃ ĐĂNG NHẬP: Lấy thông tin ra từ session
//            // Bạn phải ép kiểu (cast) về đúng kiểu dữ liệu của nó
//            String user = (String) session.getAttribute("username");
//            String role = (String) session.getAttribute("role");
//            
//            // Gửi thông tin sang lớp service khác để xử lý
//            gymInfoDAO.setUser(user);
//            gymInfoDAO.setRole(role);
//        } 
        //------------đoạn này Vanh add thêm để test-------------//

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
            AIResponse aiResponse = chatAIService.getAIResponse(userMessage);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(gson.toJson(aiResponse));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(gson.toJson(new AIResponse("Lỗi máy chủ: " + e.getMessage())));
        }
    }
}