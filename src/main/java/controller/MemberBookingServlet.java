package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.schedulemember.PTBookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import model.User;
import model.schedule.PTBooking;
import model.schedule.TimeSlot;
import service.schedulemember.MemberBookingService;
// Đảm bảo bạn đã có 2 class này trong package utils
import utils.LocalDateAdapter;
import utils.LocalTimeAdapter;

@WebServlet("/member/schedule/*")
public class MemberBookingServlet extends HttpServlet {

    private final MemberBookingService bookingService = new MemberBookingService();
    private final PTBookingDAO bookingDao = new PTBookingDAO();
    
    // Gson với adapter để xử lý LocalDate/LocalTime
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    private void sendJson(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(data));
    }

    private void sendError(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Vui lòng đăng nhập.");
            return;
        }

        try {
            if ("/history".equals(pathInfo)) {
                List<PTBooking> bookings = bookingService.getMemberBookingHistory(currentUser.getId());
                sendJson(resp, bookings);
            } else if ("/availability".equals(pathInfo)) {
                String trainerIdStr = req.getParameter("trainerId");
                String gymIdStr = req.getParameter("gymId");
                String dateStr = req.getParameter("date");

                if (trainerIdStr == null || gymIdStr == null || dateStr == null || dateStr.isEmpty()) {
                     sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Thiếu thông tin cần thiết để tra cứu.");
                     return;
                }

                int trainerId = Integer.parseInt(trainerIdStr);
                int gymId = Integer.parseInt(gymIdStr);

                List<TimeSlot> slots = bookingService.getAvailableSlots(trainerId, gymId, dateStr);
                sendJson(resp, slots);
            } else {
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Endpoint không tồn tại.");
            }
        } catch (NumberFormatException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu đầu vào không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if ("/create".equals(pathInfo)) {
            handleCreateBooking(req, resp);
        } else {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Endpoint POST không tồn tại.");
        }
    }

    private void handleCreateBooking(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null || !"Member".equalsIgnoreCase(currentUser.getRole())) {
            sendError(resp, HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền đặt lịch.");
            return;
        }

        try {
            int memberId = currentUser.getId();
            int trainerId = Integer.parseInt(req.getParameter("trainerId"));
            int gymId = Integer.parseInt(req.getParameter("gymId"));
            int slotId = Integer.parseInt(req.getParameter("slotId"));
            String dateStr = req.getParameter("bookingDate");
            String notes = req.getParameter("notes");

            boolean success = bookingService.createBooking(memberId, trainerId, gymId, slotId, dateStr, notes);

            if (success) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("{\"message\": \"Đặt lịch thành công!\"}");
            } else {
                sendError(resp, HttpServletResponse.SC_CONFLICT, "Đặt lịch thất bại. Khung giờ này có thể đã bị người khác đặt.");
            }
        } catch (NumberFormatException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu gửi lên không đúng định dạng.");
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi không xác định khi đặt lịch.");
        }
    }
    
    // ... (Phần doPut xử lý cancel nếu cần thì giữ nguyên như cũ)
}