package controller;

import com.google.gson.Gson; // Cần thư viện Gson
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import model.schedule.TimeSlot;
import service.schedulemember.MemberBookingService;
import utils.LocalDateAdapter; // Class tự tạo để Gson hiểu LocalDate (xem bên dưới)
import utils.LocalTimeAdapter; // Class tự tạo để Gson hiểu LocalTime (xem bên dưới)

@WebServlet("/api/availability")
public class AvailabilityServlet extends HttpServlet {

    private MemberBookingService bookingService = new MemberBookingService();
    // Cấu hình Gson để xử lý LocalDate và LocalTime nếu cần
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        try (PrintWriter out = resp.getWriter()) {
            // Nhận params: trainerId, gymId, date (yyyy-mm-dd)
            String trainerIdStr = req.getParameter("trainerId");
            String gymIdStr = req.getParameter("gymId");
            String dateStr = req.getParameter("date");

            if (trainerIdStr == null || gymIdStr == null || dateStr == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\": \"Missing required parameters\"}");
                return;
            }

            int trainerId = Integer.parseInt(trainerIdStr);
            int gymId = Integer.parseInt(gymIdStr);

            List<TimeSlot> slots = bookingService.getAvailableSlots(trainerId, gymId, dateStr);

            // Chuyển danh sách slots thành JSON và trả về
            String json = gson.toJson(slots);
            out.write(json);

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid ID format\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"An error occurred while fetching availability\"}");
            e.printStackTrace(); // In lỗi ra console server để debug
        }
    }
}