package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * HomeServlet handles requests for the home page
 * Displays gym information, membership packages, coaches, and contact form
 */
@WebServlet(name = "HomeServlet", urlPatterns = { "/home", "/HomeServlet" })
public class HomeServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Set statistics data
    request.setAttribute("totalMembers", "1200");
    request.setAttribute("trainers", "24");
    request.setAttribute("equipments", "150");
    request.setAttribute("successStories", "980");

    // Set membership packages data
    request.setAttribute("basicPrice", "300K");
    request.setAttribute("premiumPrice", "500K");
    request.setAttribute("vipPrice", "800K");

    // Set coaches data
    String[] coachNames = {
        "Nguyễn Văn Nam",
        "Trần Thị Lan",
        "Lê Hoàng Minh",
        "Phạm Thu Hà"
    };

    String[] coachSpecialties = {
        "Chuyên gia Bodybuilding",
        "Yoga & Pilates Instructor",
        "Strength & Conditioning",
        "Nutritionist & Fitness"
    };

    String[] coachDescriptions = {
        "5+ năm kinh nghiệm, chuyên về xây dựng cơ bắp",
        "Chứng chỉ quốc tế Yoga Alliance, 3+ năm",
        "HLV đội tuyển quốc gia, 7+ năm kinh nghiệm",
        "Thạc sĩ Dinh dưỡng, chuyên gia tư vấn"
    };

    request.setAttribute("coachNames", coachNames);
    request.setAttribute("coachSpecialties", coachSpecialties);
    request.setAttribute("coachDescriptions", coachDescriptions);

    // Set contact information
    request.setAttribute("gymAddress", "123 Đường Fitness, Quận 1, TP.HCM");
    request.setAttribute("gymPhone", "(028) 1234-5678");
    request.setAttribute("gymHours", "Thứ 2 - Chủ Nhật: 5:00 - 23:00");
    request.setAttribute("gymEmail", "info@staminagym.vn");

    // Forward to home.jsp
    request.getRequestDispatcher("/views/home.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Handle contact form submission
    String name = request.getParameter("name");
    String phone = request.getParameter("phone");
    String email = request.getParameter("email");
    String packageInterest = request.getParameter("package");
    String message = request.getParameter("message");

    // Basic validation
    if (name != null && !name.trim().isEmpty() &&
        phone != null && !phone.trim().isEmpty() &&
        email != null && !email.trim().isEmpty()) {

      // In a real application, you would save this to database
      // For now, just set success message
      request.setAttribute("successMessage",
          "Cảm ơn " + name + "! Chúng tôi sẽ liên hệ với bạn sớm nhất có thể.");

      // Log the registration (in real app, save to database)
      System.out.println("New registration: " + name + " - " + email + " - " + packageInterest);

    } else {
      request.setAttribute("errorMessage",
          "Vui lòng điền đầy đủ thông tin bắt buộc (Họ tên, Số điện thoại, Email).");
    }

    // Reload page data and show form result
    doGet(request, response);
      }
}