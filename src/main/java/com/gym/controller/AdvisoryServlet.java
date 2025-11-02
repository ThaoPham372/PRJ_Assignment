package com.gym.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gym.dao.AdvisoryDAO;

/**
 * AdvisoryServlet - Xử lý các yêu cầu tư vấn
 * GET: Hiển thị form tư vấn
 * POST: Xử lý submit form và lưu vào database
 */
@WebServlet(name = "AdvisoryServlet", urlPatterns = { "/advisory" })
public class AdvisoryServlet extends HttpServlet {

  private AdvisoryDAO advisoryDAO;

  @Override
  public void init() throws ServletException {
    super.init();
    // Khởi tạo AdvisoryDAO
    this.advisoryDAO = new AdvisoryDAO();
  }

  /**
   * GET: Hiển thị form tư vấn
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Forward đến trang form tư vấn
    request.getRequestDispatcher("/views/advisory/advisory_form.jsp").forward(request, response);
  }

  /**
   * POST: Xử lý submit form
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Lấy dữ liệu từ form
    String fullName = request.getParameter("fullName");
    String phone = request.getParameter("phone");
    String email = request.getParameter("email");
    String address = request.getParameter("address");

    // Validate dữ liệu cơ bản
    if (fullName == null || fullName.trim().isEmpty() ||
        phone == null || phone.trim().isEmpty() ||
        email == null || email.trim().isEmpty() ||
        address == null || address.trim().isEmpty()) {

      // Nếu thiếu thông tin, hiển thị lỗi
      request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin!");

      // Giữ lại dữ liệu đã nhập để hiển thị lại
      request.setAttribute("fullName", fullName);
      request.setAttribute("phone", phone);
      request.setAttribute("email", email);
      request.setAttribute("address", address);

      request.getRequestDispatcher("/views/advisory/advisory_form.jsp").forward(request, response);
      return;
    }

    // Trim các giá trị
    fullName = fullName.trim();
    phone = phone.trim();
    email = email.trim();
    address = address.trim();

    // Validate số điện thoại - chỉ cho phép số
    if (!isValidPhone(phone)) {
      request.setAttribute("errorMessage", "Số điện thoại không hợp lệ! Vui lòng chỉ nhập số (10-11 chữ số).");
      request.setAttribute("fullName", fullName);
      request.setAttribute("phone", phone);
      request.setAttribute("email", email);
      request.setAttribute("address", address);

      request.getRequestDispatcher("/views/advisory/advisory_form.jsp").forward(request, response);
      return;
    }

    // Validate email format đơn giản
    if (!isValidEmail(email)) {
      request.setAttribute("errorMessage", "Email không hợp lệ! Vui lòng nhập lại.");
      request.setAttribute("fullName", fullName);
      request.setAttribute("phone", phone);
      request.setAttribute("email", email);
      request.setAttribute("address", address);

      request.getRequestDispatcher("/views/advisory/advisory_form.jsp").forward(request, response);
      return;
    }

    // Lưu vào database
    boolean success = advisoryDAO.saveAdvisoryRequest(fullName, phone, email, address);

    if (success) {
      // Thành công: hiển thị thông báo và xóa form
      request.setAttribute("successMessage",
          "Cảm ơn bạn đã gửi yêu cầu tư vấn! Chúng tôi sẽ liên hệ với bạn sớm nhất có thể.");

      // Xóa các giá trị form
      request.removeAttribute("fullName");
      request.removeAttribute("phone");
      request.removeAttribute("email");
      request.removeAttribute("address");
    } else {
      // Lỗi: hiển thị thông báo lỗi
      request.setAttribute("errorMessage",
          "Đã có lỗi xảy ra khi gửi yêu cầu. Vui lòng thử lại sau!");

      // Giữ lại dữ liệu đã nhập
      request.setAttribute("fullName", fullName);
      request.setAttribute("phone", phone);
      request.setAttribute("email", email);
      request.setAttribute("address", address);
    }

    // Forward lại trang form
    request.getRequestDispatcher("/views/advisory/advisory_form.jsp").forward(request, response);
  }

  /**
   * Kiểm tra số điện thoại - chỉ cho phép số, độ dài 10-11 chữ số
   * 
   * @param phone Số điện thoại cần kiểm tra
   * @return true nếu số điện thoại hợp lệ
   */
  private boolean isValidPhone(String phone) {
    if (phone == null || phone.isEmpty()) {
      return false;
    }

    // Chỉ cho phép số, độ dài từ 10 đến 11 chữ số
    return phone.matches("^[0-9]{10,11}$");
  }

  /**
   * Kiểm tra định dạng email đơn giản
   * 
   * @param email Email cần kiểm tra
   * @return true nếu email hợp lệ
   */
  private boolean isValidEmail(String email) {
    if (email == null || email.isEmpty()) {
      return false;
    }

    // Kiểm tra định dạng email cơ bản
    return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
  }
}
