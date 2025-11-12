package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.GymInfoDAO;
import dao.TrainerDAO;
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
import model.GymInfo;
import model.Trainer;
import model.User;
import model.schedule.PTBooking;
import model.schedule.TimeSlot;
import service.MemberBookingService;

/**
 * MemberBookingServlet - Controller xử lý đặt lịch tập PT cho Member
 * 
 * Tuân thủ mô hình MVC và nguyên tắc Single Responsibility
 * 
 * Endpoints:
 * GET  /member/schedule/history      - Lấy lịch sử đặt lịch của member
 * GET  /member/schedule/availability  - Lấy danh sách khung giờ trống
 * GET  /member/schedule/trainers      - Lấy danh sách huấn luyện viên
 * GET  /member/schedule/gyms         - Lấy danh sách phòng gym
 * POST /member/schedule/create       - Tạo booking mới
 */
@WebServlet("/member/schedule/*")
public class MemberBookingServlet extends HttpServlet {

    private final MemberBookingService bookingService = new MemberBookingService();
    private final TrainerDAO trainerDAO = new TrainerDAO();
    private final GymInfoDAO gymDAO = new GymInfoDAO();
    
    /**
     * Gson với adapter để xử lý LocalDate/LocalTime trong JSON
     */
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    /**
     * Gửi response JSON
     */
    private void sendJson(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(data));
    }

    /**
     * Gửi response lỗi dạng JSON
     */
    private void sendError(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    /**
     * Kiểm tra authentication
     */
    private User requireAuth(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (User) session.getAttribute("user") : null;
    }

    /**
     * Xử lý GET requests
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        User currentUser = requireAuth(req);

        if (currentUser == null) {
            sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Vui lòng đăng nhập.");
            return;
        }

        try {
            if ("/history".equals(pathInfo)) {
                // Lấy lịch sử đặt lịch của member
                handleGetHistory(resp, currentUser.getId());
            } else if ("/availability".equals(pathInfo)) {
                // Lấy danh sách khung giờ trống
                handleGetAvailability(req, resp);
            } else if ("/trainers".equals(pathInfo)) {
                // Lấy danh sách huấn luyện viên theo gym
                handleGetTrainers(req, resp);
            } else if ("/gyms".equals(pathInfo)) {
                // Lấy danh sách phòng gym
                handleGetGyms(resp);
            } else if ("/create".equals(pathInfo)) {
                // Hiển thị trang đặt lịch mới
                handleShowCreatePage(req, resp);
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

    /**
     * Lấy lịch sử đặt lịch của member
     */
    private void handleGetHistory(HttpServletResponse resp, int memberId) throws IOException {
        List<PTBooking> bookings = bookingService.getMemberBookingHistory(memberId);
        sendJson(resp, bookings);
    }

    /**
     * Lấy danh sách khung giờ trống dựa trên trainer, gym và ngày
     */
    private void handleGetAvailability(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
    }

    /**
     * Lấy danh sách huấn luyện viên theo gym
     * Nếu có gymId parameter thì chỉ lấy trainers của gym đó
     */
    private void handleGetTrainers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String gymIdStr = req.getParameter("gymId");
        
        try {
            List<Trainer> trainers;
            if (gymIdStr != null && !gymIdStr.isEmpty()) {
                try {
                    int gymId = Integer.parseInt(gymIdStr);
                    trainers = bookingService.getTrainersByGym(gymId);
                    
                    // Log để debug
                    System.out.println("Found " + (trainers != null ? trainers.size() : 0) + " trainers for gym " + gymId);
                    
                } catch (NumberFormatException e) {
                    sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Gym ID không hợp lệ.");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lấy danh sách trainers: " + e.getMessage());
                    return;
                }
            } else {
                // Nếu không có gymId, trả về tất cả trainers (fallback)
                trainers = trainerDAO.findAll();
            }
            
            // Đảm bảo trainers không null
            if (trainers == null) {
                trainers = new java.util.ArrayList<>();
            }
            
            sendJson(resp, trainers);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống khi lấy trainers: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách phòng gym
     */
    private void handleGetGyms(HttpServletResponse resp) throws IOException {
        List<GymInfo> gyms = gymDAO.findAll();
        sendJson(resp, gyms);
    }

    /**
     * Hiển thị trang đặt lịch mới
     */
    private void handleShowCreatePage(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        // Kiểm tra quyền Member
        User currentUser = requireAuth(req);
        if (currentUser == null || !"Member".equalsIgnoreCase(currentUser.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/member/schedule");
            return;
        }
        
        // Forward đến trang create.jsp
        req.getRequestDispatcher("/views/member/create.jsp").forward(req, resp);
    }

    /**
     * Xử lý POST requests
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if ("/create".equals(pathInfo)) {
            handleCreateBooking(req, resp);
        } else {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Endpoint POST không tồn tại.");
        }
    }

    /**
     * Xử lý tạo booking mới
     */
    private void handleCreateBooking(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = requireAuth(req);

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
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
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
}