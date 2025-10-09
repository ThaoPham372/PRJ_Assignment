package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * CustomerNutritionServlet handles customer nutrition tracking
 * Allows customers to view nutrition plans and track food intake
 */
@WebServlet(name = "CustomerNutritionServlet", urlPatterns = {"/member/nutrition"})
public class CustomerNutritionServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in and is a member
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String role = (String) session.getAttribute("role");
        if (!"member".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }
        
        String memberId = (String) session.getAttribute("userId");
        
        // TODO: Load nutrition data from database
        // For now, set mock data
        request.setAttribute("dailyCalories", 2200);
        request.setAttribute("consumedCalories", 1850);
        request.setAttribute("protein", 120);
        request.setAttribute("carbs", 250);
        request.setAttribute("fat", 85);
        request.setAttribute("water", 6);
        
        // Forward to nutrition page
        request.getRequestDispatcher("/views/member/nutrition.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        String memberId = (String) session.getAttribute("userId");
        
        if ("logFood".equals(action)) {
            // Handle logging food intake
            String foodName = request.getParameter("foodName");
            String quantity = request.getParameter("quantity");
            String mealType = request.getParameter("mealType");
            
            // TODO: Implement food logging logic
            request.setAttribute("message", "Đã ghi nhận bữa ăn thành công!");
            
        } else if ("updateGoal".equals(action)) {
            // Handle updating nutrition goals
            String dailyCalories = request.getParameter("dailyCalories");
            String proteinGoal = request.getParameter("proteinGoal");
            String carbGoal = request.getParameter("carbGoal");
            String fatGoal = request.getParameter("fatGoal");
            
            // TODO: Implement goal update logic
            request.setAttribute("message", "Cập nhật mục tiêu dinh dưỡng thành công!");
            
        } else if ("requestConsultation".equals(action)) {
            // Handle requesting nutrition consultation
            String goals = request.getParameter("goals");
            String restrictions = request.getParameter("restrictions");
            String message = request.getParameter("message");
            
            // TODO: Implement consultation request logic
            request.setAttribute("message", "Đã gửi yêu cầu tư vấn dinh dưỡng thành công!");
        }
        
        // Redirect back to nutrition page
        response.sendRedirect(request.getContextPath() + "/member/nutrition");
    }
}
