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
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = request.getServletPath();
        }
        
        LOGGER.info("POST request path: " + pathInfo);
        
        try {
            // Check if this is a cancel request
            if (pathInfo != null && pathInfo.contains("/cancel")) {
                handleCancelBooking(request, response);
                return;
            }
            
            // Regular POST requests
            if ("/api/schedule/bookings".equals(pathInfo) || pathInfo.endsWith("/api/schedule/bookings")) {
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
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = request.getServletPath();
        }
        
        LOGGER.info("PUT request path: " + pathInfo);
        
        try {
            if (pathInfo != null && pathInfo.contains("/cancel")) {
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
            
            // Trainer info - Load if null but trainerId exists
            if (booking.getTrainer() != null) {
                Map<String, Object> trainerData = new HashMap<>();
                trainerData.put("id", booking.getTrainer().getId());
                trainerData.put("name", booking.getTrainer().getName());
                bookingData.put("trainer", trainerData);
            } else if (booking.getTrainerId() != null) {
                // Load trainer from database if relationship is null
                Trainer trainer = scheduleService.getTrainerById(booking.getTrainerId());
                if (trainer != null) {
                    Map<String, Object> trainerData = new HashMap<>();
                    trainerData.put("id", trainer.getId());
                    trainerData.put("name", trainer.getName());
                    bookingData.put("trainer", trainerData);
                }
            }
            
            // Gym info - Load if null but gymId exists
            if (booking.getGym() != null) {
                Map<String, Object> gymData = new HashMap<>();
                gymData.put("gymId", booking.getGym().getGymId());
                gymData.put("gymName", booking.getGym().getName());
                gymData.put("name", booking.getGym().getName()); // Alias for compatibility
                gymData.put("address", booking.getGym().getAddress());
                bookingData.put("gym", gymData);
            } else if (booking.getGymId() != null) {
                // Load gym from database if relationship is null
                GymInfo gym = scheduleService.getGymById(booking.getGymId());
                if (gym != null) {
                    Map<String, Object> gymData = new HashMap<>();
                    gymData.put("gymId", gym.getGymId());
                    gymData.put("gymName", gym.getName());
                    gymData.put("name", gym.getName()); // Alias for compatibility
                    gymData.put("address", gym.getAddress());
                    bookingData.put("gym", gymData);
                }
            }
            
            // Time slot info - Load if null but slotId exists
            if (booking.getTimeSlot() != null) {
                Map<String, Object> slotData = new HashMap<>();
                slotData.put("slotId", booking.getTimeSlot().getSlotId());
                slotData.put("slotName", booking.getTimeSlot().getSlotName());
                slotData.put("startTime", booking.getTimeSlot().getStartTime().toString());
                slotData.put("endTime", booking.getTimeSlot().getEndTime().toString());
                bookingData.put("timeSlot", slotData);
            } else if (booking.getSlotId() != null) {
                // Load time slot from database if relationship is null
                TimeSlot timeSlot = scheduleService.getTimeSlotById(booking.getSlotId());
                if (timeSlot != null) {
                    Map<String, Object> slotData = new HashMap<>();
                    slotData.put("slotId", timeSlot.getSlotId());
                    slotData.put("slotName", timeSlot.getSlotName());
                    slotData.put("startTime", timeSlot.getStartTime().toString());
                    slotData.put("endTime", timeSlot.getEndTime().toString());
                    bookingData.put("timeSlot", slotData);
                }
            }
            
            bookingList.add(bookingData);
        }
        
        sendSuccess(response, bookingList);
    }
    
    /**
     * PUT/POST /api/schedule/bookings/{id}/cancel - Cancel booking
     */
    private void handleCancelBooking(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        // Always set JSON content type first
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Check if member is logged in
        Member member = (Member) request.getSession().getAttribute("member");
        if (member == null) {
            sendError(response, 401, "Vui lòng đăng nhập");
            return;
        }
        
        // Extract booking ID from path
        // URL pattern: /api/schedule/bookings/{id}/cancel
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String pathInfo = request.getPathInfo();
        
        LOGGER.info("Cancel booking - requestURI: " + requestURI + ", contextPath: " + contextPath + ", pathInfo: " + pathInfo);
        
        Integer bookingId = null;
        
        try {
            // Method 1: Try to extract from pathInfo
            if (pathInfo != null) {
                String[] parts = pathInfo.split("/");
                for (int i = 0; i < parts.length; i++) {
                    if ("bookings".equals(parts[i]) && i + 1 < parts.length) {
                        try {
                            bookingId = Integer.parseInt(parts[i + 1]);
                            LOGGER.info("Extracted booking ID from pathInfo: " + bookingId);
                            break;
                        } catch (NumberFormatException e) {
                            // Not a number, continue
                        }
                    }
                }
            }
            
            // Method 2: Try to extract from requestURI
            if (bookingId == null && requestURI != null) {
                String relativePath = requestURI;
                if (contextPath != null && requestURI.startsWith(contextPath)) {
                    relativePath = requestURI.substring(contextPath.length());
                }
                
                // Pattern: /api/schedule/bookings/{id}/cancel
                String pattern = "/api/schedule/bookings/";
                int patternIndex = relativePath.indexOf(pattern);
                if (patternIndex >= 0) {
                    int startIndex = patternIndex + pattern.length();
                    int endIndex = relativePath.indexOf("/", startIndex);
                    if (endIndex < 0) {
                        endIndex = relativePath.length();
                    }
                    String idStr = relativePath.substring(startIndex, endIndex);
                    try {
                        bookingId = Integer.parseInt(idStr);
                        LOGGER.info("Extracted booking ID from requestURI: " + bookingId);
                    } catch (NumberFormatException e) {
                        LOGGER.warning("Could not parse booking ID from: " + idStr);
                    }
                }
            }
            
            if (bookingId == null) {
                LOGGER.warning("Could not extract booking ID. requestURI: " + requestURI + ", pathInfo: " + pathInfo);
                sendError(response, 400, "Không tìm thấy ID lịch đặt");
                return;
            }
            
            Map<String, Object> result = scheduleService.cancelBookingByMember(bookingId, member.getId());
            
            if ((Boolean) result.get("success")) {
                // Send result directly, not wrapped
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(result));
            } else {
                String errorMsg = (String) result.get("message");
                if (errorMsg == null || errorMsg.isEmpty()) {
                    errorMsg = "Không thể hủy lịch đặt";
                }
                sendError(response, 400, errorMsg);
            }
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid booking ID format", e);
            sendError(response, 400, "ID lịch đặt không hợp lệ");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in cancel booking", e);
            sendError(response, 500, "Lỗi hệ thống. Vui lòng thử lại sau.");
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
