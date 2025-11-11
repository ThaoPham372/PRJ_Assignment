package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.GymInfo;
import model.Member;
import model.Trainer;
import model.schedule.PTBooking;
import model.schedule.TimeSlot;
import service.ScheduleService;

/**
 * ScheduleServlet - Handles API endpoints for PT booking schedule
 * API endpoints:
 * - GET /api/schedule/gyms - Get all gyms
 * - GET /api/schedule/trainers?gymId={gymId} - Get trainers by gym
 * - GET /api/schedule/timeslots - Get all time slots
 * - GET /api/schedule/available-slots?trainerId={trainerId}&gymId={gymId}&date={date} - Get available slots
 * - POST /api/schedule/bookings - Create booking
 * - GET /api/schedule/bookings - Get member bookings
 * - PUT /api/schedule/bookings/{id}/cancel - Cancel booking
 */
@WebServlet({
    "/api/schedule/gyms",
    "/api/schedule/trainers",
    "/api/schedule/timeslots",
    "/api/schedule/available-slots",
    "/api/schedule/bookings",
    "/api/schedule/bookings/*/cancel"
})
public class ScheduleServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ScheduleServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private final ScheduleService scheduleService;
    private final Gson gson;
    
    public ScheduleServlet() {
        this.scheduleService = new ScheduleService();
        this.gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getServletPath();
        
        try {
            switch (pathInfo) {
                case "/api/schedule/gyms":
                    handleGetGyms(request, response);
                    break;
                case "/api/schedule/trainers":
                    handleGetTrainers(request, response);
                    break;
                case "/api/schedule/timeslots":
                    handleGetTimeSlots(request, response);
                    break;
                case "/api/schedule/available-slots":
                    handleGetAvailableSlots(request, response);
                    break;
                case "/api/schedule/bookings":
                    handleGetBookings(request, response);
                    break;
                default:
                    sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling GET request", e);
            sendError(response, 500, "Internal server error: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getServletPath();
        
        try {
            if ("/api/schedule/bookings".equals(pathInfo)) {
                handleCreateBooking(request, response);
            } else {
                sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling POST request", e);
            sendError(response, 500, "Internal server error: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getServletPath();
        
        try {
            if (pathInfo.contains("/cancel")) {
                handleCancelBooking(request, response);
            } else {
                sendError(response, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling PUT request", e);
            sendError(response, 500, "Internal server error: " + e.getMessage());
        }
    }
    
    /**
     * GET /api/schedule/gyms - Get all gyms
     */
    private void handleGetGyms(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        List<GymInfo> gyms = scheduleService.getAllGyms();
        
        // Convert to simple DTO
        List<Map<String, Object>> gymList = new ArrayList<>();
        for (GymInfo gym : gyms) {
            Map<String, Object> gymData = new HashMap<>();
            gymData.put("gymId", gym.getGymId());
            gymData.put("gymName", gym.getName());
            gymData.put("location", gym.getAddress());
            gymData.put("phone", gym.getHotline());
            gymList.add(gymData);
        }
        
        sendSuccess(response, gymList);
    }
    
    /**
     * GET /api/schedule/trainers?gymId={gymId} - Get trainers by gym
     */
    private void handleGetTrainers(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String gymIdStr = request.getParameter("gymId");
        Integer gymId = null;
        
        if (gymIdStr != null && !gymIdStr.isEmpty()) {
            try {
                gymId = Integer.parseInt(gymIdStr);
            } catch (NumberFormatException e) {
                sendError(response, 400, "Invalid gymId");
                return;
            }
        }
        
        List<Trainer> trainers = scheduleService.getTrainersByGym(gymId);
        
        // Convert to simple DTO
        List<Map<String, Object>> trainerList = new ArrayList<>();
        for (Trainer trainer : trainers) {
            Map<String, Object> trainerData = new HashMap<>();
            trainerData.put("id", trainer.getId());
            trainerData.put("name", trainer.getName());
            trainerData.put("email", trainer.getEmail());
            trainerData.put("specialization", trainer.getSpecialization());
            trainerData.put("experience", trainer.getYearsOfExperience());
            trainerData.put("certification", trainer.getCertificationLevel());
            trainerList.add(trainerData);
        }
        
        sendSuccess(response, trainerList);
    }
    
    /**
     * GET /api/schedule/timeslots - Get all time slots
     */
    private void handleGetTimeSlots(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        List<TimeSlot> slots = scheduleService.getAllTimeSlots();
        
        // Convert to simple DTO
        List<Map<String, Object>> slotList = new ArrayList<>();
        for (TimeSlot slot : slots) {
            Map<String, Object> slotData = new HashMap<>();
            slotData.put("slotId", slot.getSlotId());
            slotData.put("startTime", slot.getStartTime().toString());
            slotData.put("endTime", slot.getEndTime().toString());
            slotData.put("slotName", slot.getSlotName());
            slotList.add(slotData);
        }
        
        sendSuccess(response, slotList);
    }
    
    /**
     * GET /api/schedule/available-slots?trainerId={trainerId}&gymId={gymId}&date={date}
     */
    private void handleGetAvailableSlots(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String trainerIdStr = request.getParameter("trainerId");
        String gymIdStr = request.getParameter("gymId");
        String dateStr = request.getParameter("date");
        
        // Validate inputs
        if (trainerIdStr == null || gymIdStr == null || dateStr == null) {
            sendError(response, 400, "Missing required parameters");
            return;
        }
        
        try {
            Integer trainerId = Integer.parseInt(trainerIdStr);
            Integer gymId = Integer.parseInt(gymIdStr);
            LocalDate date = LocalDate.parse(dateStr);
            
            List<Integer> availableSlots = scheduleService.getAvailableSlots(trainerId, gymId, date);
            
            sendSuccess(response, availableSlots);
            
        } catch (NumberFormatException e) {
            sendError(response, 400, "Invalid trainerId or gymId");
        } catch (DateTimeParseException e) {
            sendError(response, 400, "Invalid date format. Use yyyy-MM-dd");
        }
    }
    
    /**
     * POST /api/schedule/bookings - Create booking
     */
    private void handleCreateBooking(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        // Check if member is logged in
        Member member = (Member) request.getSession().getAttribute("member");
        if (member == null) {
            sendError(response, 401, "Vui lòng đăng nhập để đặt lịch");
            return;
        }
        
        // Read JSON body
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> bookingData = gson.fromJson(sb.toString(), Map.class);
            
            Integer trainerId = getIntValue(bookingData, "trainerId");
            Integer gymId = getIntValue(bookingData, "gymId");
            String dateStr = (String) bookingData.get("date");
            Integer slotId = getIntValue(bookingData, "slotId");
            String notes = (String) bookingData.get("notes");
            
            if (trainerId == null || gymId == null || dateStr == null || slotId == null) {
                sendError(response, 400, "Thiếu thông tin bắt buộc");
                return;
            }
            
            LocalDate date = LocalDate.parse(dateStr);
            
            Map<String, Object> result = scheduleService.createBooking(
                    member.getId(), trainerId, gymId, date, slotId, notes);
            
            if ((Boolean) result.get("success")) {
                sendSuccess(response, result);
            } else {
                sendError(response, 400, (String) result.get("message"));
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating booking", e);
            sendError(response, 400, "Invalid request: " + e.getMessage());
        }
    }
    
    /**
     * GET /api/schedule/bookings - Get member bookings
     */
    private void handleGetBookings(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        // Check if member is logged in
        Member member = (Member) request.getSession().getAttribute("member");
        if (member == null) {
            sendError(response, 401, "Vui lòng đăng nhập");
            return;
        }
        
        List<PTBooking> bookings = scheduleService.getMemberBookings(member.getId());
        
        // Convert to DTO
        List<Map<String, Object>> bookingList = new ArrayList<>();
        for (PTBooking booking : bookings) {
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("bookingId", booking.getBookingId());
            bookingData.put("bookingDate", booking.getBookingDate().toString());
            bookingData.put("bookingStatus", booking.getBookingStatus().toString());
            bookingData.put("notes", booking.getNotes());
            bookingData.put("cancelledReason", booking.getCancelledReason());
            
            // Trainer info
            if (booking.getTrainer() != null) {
                Map<String, Object> trainerData = new HashMap<>();
                trainerData.put("id", booking.getTrainer().getId());
                trainerData.put("name", booking.getTrainer().getName());
                bookingData.put("trainer", trainerData);
            }
            
            // Time slot info
            if (booking.getTimeSlot() != null) {
                Map<String, Object> slotData = new HashMap<>();
                slotData.put("slotId", booking.getTimeSlot().getSlotId());
                slotData.put("slotName", booking.getTimeSlot().getSlotName());
                slotData.put("startTime", booking.getTimeSlot().getStartTime().toString());
                slotData.put("endTime", booking.getTimeSlot().getEndTime().toString());
                bookingData.put("timeSlot", slotData);
            }
            
            bookingList.add(bookingData);
        }
        
        sendSuccess(response, bookingList);
    }
    
    /**
     * PUT /api/schedule/bookings/{id}/cancel - Cancel booking
     */
    private void handleCancelBooking(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        // Check if member is logged in
        Member member = (Member) request.getSession().getAttribute("member");
        if (member == null) {
            sendError(response, 401, "Vui lòng đăng nhập");
            return;
        }
        
        // Extract booking ID from path
        String pathInfo = request.getServletPath();
        String[] parts = pathInfo.split("/");
        
        try {
            Integer bookingId = null;
            for (int i = 0; i < parts.length; i++) {
                if ("bookings".equals(parts[i]) && i + 1 < parts.length) {
                    bookingId = Integer.parseInt(parts[i + 1]);
                    break;
                }
            }
            
            if (bookingId == null) {
                sendError(response, 400, "Invalid booking ID");
                return;
            }
            
            Map<String, Object> result = scheduleService.cancelBookingByMember(bookingId, member.getId());
            
            if ((Boolean) result.get("success")) {
                sendSuccess(response, result);
            } else {
                sendError(response, 400, (String) result.get("message"));
            }
            
        } catch (NumberFormatException e) {
            sendError(response, 400, "Invalid booking ID");
        }
    }
    
    /**
     * Send success response
     */
    private void sendSuccess(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", data);
        
        response.getWriter().write(gson.toJson(result));
    }
    
    /**
     * Send error response
     */
    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        
        response.getWriter().write(gson.toJson(result));
    }
    
    /**
     * Helper to get integer value from map
     */
    private Integer getIntValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
