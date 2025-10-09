package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * CustomerWorkoutServlet handles customer workout tracking
 * Allows customers to view workout history and track progress
 */
@WebServlet(name = "CustomerWorkoutServlet", urlPatterns = {"/member/workout"})
public class CustomerWorkoutServlet extends HttpServlet {
    
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
        
        // TODO: Load workout data from database
        // For now, set mock data
        request.setAttribute("totalWorkouts", 45);
        request.setAttribute("thisMonthWorkouts", 8);
        request.setAttribute("streakDays", 12);
        request.setAttribute("totalCalories", 15600);
        request.setAttribute("avgWorkoutDuration", 75);
        
        // Forward to workout page
        request.getRequestDispatcher("/views/member/workout.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        String memberId = (String) session.getAttribute("userId");
        
        if ("logWorkout".equals(action)) {
            // Handle logging a new workout
            String workoutType = request.getParameter("workoutType");
            String duration = request.getParameter("duration");
            String calories = request.getParameter("calories");
            String notes = request.getParameter("notes");
            
            // TODO: Implement workout logging logic
            request.setAttribute("message", "Đã ghi nhận buổi tập thành công!");
            
        } else if ("updateProgress".equals(action)) {
            // Handle updating progress
            String weight = request.getParameter("weight");
            String bodyFat = request.getParameter("bodyFat");
            String muscleMass = request.getParameter("muscleMass");
            
            // TODO: Implement progress update logic
            request.setAttribute("message", "Cập nhật tiến độ thành công!");
        }
        
        // Redirect back to workout page
        response.sendRedirect(request.getContextPath() + "/member/workout");
    }
}
