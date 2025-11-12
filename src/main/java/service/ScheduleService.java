package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.GymInfoDAO;
import dao.TrainerDAO;
import dao.schedule.PTBookingDAO;
import dao.schedule.TimeSlotDAO;
import dao.schedule.TrainerScheduleDAO;
import model.GymInfo;
import model.Trainer;
import model.schedule.BookingStatus;
import model.schedule.CancelledBy;
import model.schedule.DayOfWeek;
import model.schedule.PTBooking;
import model.schedule.TimeSlot;
import model.schedule.TrainerSchedule;

/**
 * Service layer for Schedule/Booking logic
 * Handles business rules for PT booking
 */
public class ScheduleService {
    private static final Logger LOGGER = Logger.getLogger(ScheduleService.class.getName());
    
    private final GymInfoDAO gymInfoDAO;
    private final TrainerDAO trainerDAO;
    private final TimeSlotDAO timeSlotDAO;
    private final TrainerScheduleDAO scheduleDAO;
    private final PTBookingDAO bookingDAO;
    
    public ScheduleService() {
        this.gymInfoDAO = new GymInfoDAO();
        this.trainerDAO = new TrainerDAO();
        this.timeSlotDAO = new TimeSlotDAO();
        this.scheduleDAO = new TrainerScheduleDAO();
        this.bookingDAO = new PTBookingDAO();
    }
    
    /**
     * Get all active gyms
     */
    public List<GymInfo> getAllGyms() {
        try {
            return gymInfoDAO.findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting gyms", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Get all trainers (optionally filter by gym)
     * If gymId is provided, returns trainers who:
     * 1. Have schedules at that gym (priority)
     * 2. Or have workAt field matching the gymId (fallback)
     * 3. Or all active trainers if no matches found
     */
    public List<Trainer> getTrainersByGym(Integer gymId) {
        try {
            if (gymId == null) {
                // Return all active trainers if no gym specified
                List<Trainer> allTrainers = trainerDAO.findAll();
                List<Trainer> activeTrainers = new ArrayList<>();
                for (Trainer t : allTrainers) {
                    if ("ACTIVE".equalsIgnoreCase(t.getStatus())) {
                        activeTrainers.add(t);
                    }
                }
                return activeTrainers;
            }
            
            Set<Integer> trainerIds = new HashSet<>();
            String gymIdStr = String.valueOf(gymId);
            
            // Method 1: Get trainers who have schedules at this gym
            try {
                List<TrainerSchedule> schedules = scheduleDAO.findByGymId(gymId);
                for (TrainerSchedule schedule : schedules) {
                    if (Boolean.TRUE.equals(schedule.getIsAvailable())) {
                        trainerIds.add(schedule.getTrainerId());
                    }
                }
                LOGGER.info("Found " + trainerIds.size() + " trainers from schedules for gymId: " + gymId);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error getting trainers from schedules", e);
            }
            
            // Method 2: Also get trainers by workAt field (gymId as string)
            // This ensures we get all trainers assigned to this gym, even if they don't have schedules yet
            try {
                List<Trainer> allTrainers = trainerDAO.findAll();
                int workAtCount = 0;
                for (Trainer trainer : allTrainers) {
                    if ("ACTIVE".equalsIgnoreCase(trainer.getStatus())) {
                        String workAt = trainer.getWorkAt();
                        if (workAt != null && (workAt.equals(gymIdStr) || workAt.trim().equals(gymIdStr))) {
                            trainerIds.add(trainer.getId());
                            workAtCount++;
                        }
                    }
                }
                LOGGER.info("Found " + workAtCount + " trainers from workAt field for gymId: " + gymId);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error getting trainers from workAt field", e);
            }
            
            // Method 3: If still no trainers found, return all active trainers as fallback
            if (trainerIds.isEmpty()) {
                LOGGER.warning("No trainers found for gymId: " + gymId + ", returning all active trainers");
                List<Trainer> allTrainers = trainerDAO.findAll();
                List<Trainer> activeTrainers = new ArrayList<>();
                for (Trainer t : allTrainers) {
                    if ("ACTIVE".equalsIgnoreCase(t.getStatus())) {
                        activeTrainers.add(t);
                    }
                }
                return activeTrainers;
            }
            
            // Get trainer objects
            List<Trainer> trainers = new ArrayList<>();
            for (Integer trainerId : trainerIds) {
                Trainer trainer = trainerDAO.findById(trainerId);
                if (trainer != null && "ACTIVE".equalsIgnoreCase(trainer.getStatus())) {
                    trainers.add(trainer);
                }
            }
            
            LOGGER.info("Found " + trainers.size() + " trainers for gymId: " + gymId);
            return trainers;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting trainers by gym", e);
            // Fallback: return all active trainers on error
            try {
                List<Trainer> allTrainers = trainerDAO.findAll();
                List<Trainer> activeTrainers = new ArrayList<>();
                for (Trainer t : allTrainers) {
                    if ("ACTIVE".equalsIgnoreCase(t.getStatus())) {
                        activeTrainers.add(t);
                    }
                }
                return activeTrainers;
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error in fallback getting all trainers", ex);
                return new ArrayList<>();
            }
        }
    }
    
    /**
     * Get all active time slots
     */
    public List<TimeSlot> getAllTimeSlots() {
        try {
            return timeSlotDAO.findActiveSlots();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting time slots", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Get available slots for trainer on a specific date at a gym
     * Returns list of slot IDs that are available
     */
    public List<Integer> getAvailableSlots(Integer trainerId, Integer gymId, LocalDate date) {
        List<Integer> availableSlots = new ArrayList<>();
        
        try {
            // Get day of week from date
            java.time.DayOfWeek javaDow = date.getDayOfWeek();
            DayOfWeek dayOfWeek = convertDayOfWeek(javaDow);
            
            // Get trainer schedule for this day of week
            List<TrainerSchedule> schedules = scheduleDAO.getWeeklySchedule(trainerId);
            List<Integer> scheduledSlots = new ArrayList<>();
            
            for (TrainerSchedule schedule : schedules) {
                if (schedule.getDayOfWeek() == dayOfWeek 
                    && schedule.getGymId().equals(gymId)
                    && Boolean.TRUE.equals(schedule.getIsAvailable())) {
                    scheduledSlots.add(schedule.getSlotId());
                }
            }
            
            // Get booked slots for this trainer/gym/date
            List<PTBooking> bookings = bookingDAO.findByDateRange(date, date);
            List<Integer> bookedSlots = new ArrayList<>();
            for (PTBooking booking : bookings) {
                if (booking.getTrainerId().equals(trainerId) 
                    && booking.getGymId().equals(gymId)
                    && booking.getBookingDate().equals(date)
                    && (booking.getBookingStatus() == BookingStatus.CONFIRMED 
                        || booking.getBookingStatus() == BookingStatus.PENDING)) {
                    bookedSlots.add(booking.getSlotId());
                }
            }
            
            // Get current time for filtering past slots on current date
            LocalTime currentTime = LocalTime.now();
            LocalDate today = LocalDate.now();
            boolean isToday = date.equals(today);
            
            // If trainer has schedule for this day, only show scheduled slots (minus booked)
            if (!scheduledSlots.isEmpty()) {
                for (Integer slotId : scheduledSlots) {
                    // Skip if already booked
                    if (!bookedSlots.contains(slotId)) {
                        // If booking for today, filter out past time slots
                        if (isToday) {
                            TimeSlot slot = timeSlotDAO.findById(slotId);
                            if (slot != null && slot.getStartTime() != null) {
                                // Only add if slot start time is after current time
                                if (slot.getStartTime().isAfter(currentTime)) {
                                    availableSlots.add(slotId);
                                }
                            } else {
                                // If slot info not found, add it anyway (shouldn't happen)
                                availableSlots.add(slotId);
                            }
                        } else {
                            // For future dates, add all available slots
                            availableSlots.add(slotId);
                        }
                    }
                }
            } else {
                // If no schedule, check if trainer works at this gym
                // If yes, show all slots (minus booked) - trainer can work any time
                // Get all active time slots
                List<TimeSlot> allSlots = timeSlotDAO.findActiveSlots();
                for (TimeSlot slot : allSlots) {
                    Integer slotId = slot.getSlotId();
                    // Only add if not booked
                    if (!bookedSlots.contains(slotId)) {
                        // If booking for today, filter out past time slots
                        if (isToday) {
                            if (slot.getStartTime() != null) {
                                // Only add if slot start time is after current time
                                if (slot.getStartTime().isAfter(currentTime)) {
                                    availableSlots.add(slotId);
                                }
                            } else {
                                // If start time is null, skip it
                                continue;
                            }
                        } else {
                            // For future dates, add all available slots
                            availableSlots.add(slotId);
                        }
                    }
                }
                LOGGER.info("No schedule found for trainer " + trainerId + " on " + dayOfWeek + 
                           " at gym " + gymId + ", returning all non-booked slots: " + availableSlots.size() +
                           (isToday ? " (filtered for today after " + currentTime + ")" : ""));
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting available slots", e);
            // On error, return empty list to be safe
        }
        
        return availableSlots;
    }
    
    /**
     * Convert Java DayOfWeek to model DayOfWeek
     */
    private DayOfWeek convertDayOfWeek(java.time.DayOfWeek javaDow) {
        switch (javaDow) {
            case MONDAY: return DayOfWeek.MONDAY;
            case TUESDAY: return DayOfWeek.TUESDAY;
            case WEDNESDAY: return DayOfWeek.WEDNESDAY;
            case THURSDAY: return DayOfWeek.THURSDAY;
            case FRIDAY: return DayOfWeek.FRIDAY;
            case SATURDAY: return DayOfWeek.SATURDAY;
            case SUNDAY: return DayOfWeek.SUNDAY;
            default: return DayOfWeek.MONDAY;
        }
    }
    
    /**
     * Create a new booking
     */
    public Map<String, Object> createBooking(Integer memberId, Integer trainerId, 
                                             Integer gymId, LocalDate date, Integer slotId, String notes) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Validate inputs
            if (memberId == null || trainerId == null || gymId == null || date == null || slotId == null) {
                result.put("success", false);
                result.put("message", "Thiếu thông tin bắt buộc");
                return result;
            }
            
            // Check if date is in the past (allow today and future dates)
            // Use atStartOfDay() to ensure we're comparing dates only, not times
            LocalDate today = LocalDate.now();
            if (date.isBefore(today)) {
                result.put("success", false);
                result.put("message", "Không thể đặt lịch cho ngày trong quá khứ");
                return result;
            }
            
            // Allow booking for today and future dates
            // No time restriction - user can book for any available slot today
            LOGGER.info("Booking date: " + date + ", Today: " + today + ", Is today: " + date.equals(today));
            
            // Check if slot is still available
            List<Integer> availableSlots = getAvailableSlots(trainerId, gymId, date);
            if (!availableSlots.contains(slotId)) {
                result.put("success", false);
                result.put("message", "Khung giờ này đã được đặt hoặc không khả dụng");
                return result;
            }
            
            // Create new booking
            PTBooking booking = new PTBooking();
            booking.setMemberId(memberId);
            booking.setTrainerId(trainerId);
            booking.setGymId(gymId);
            booking.setBookingDate(date);
            booking.setSlotId(slotId);
            booking.setBookingStatus(BookingStatus.PENDING);
            booking.setNotes(notes);
            // createdAt will be set by @PrePersist
            
            bookingDAO.save(booking);
            
            result.put("success", true);
            result.put("message", "Đặt lịch thành công! Vui lòng chờ PT xác nhận.");
            result.put("bookingId", booking.getBookingId());
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating booking", e);
            result.put("success", false);
            result.put("message", "Lỗi hệ thống: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Cancel booking by member
     * Must cancel at least 24 hours before the booking date
     * Slot will be automatically released for others to book
     */
    public Map<String, Object> cancelBookingByMember(Integer bookingId, Integer memberId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            PTBooking booking = bookingDAO.findById(bookingId);
            
            if (booking == null) {
                result.put("success", false);
                result.put("message", "Không tìm thấy booking");
                return result;
            }
            
            // Check ownership
            if (!booking.getMemberId().equals(memberId)) {
                result.put("success", false);
                result.put("message", "Bạn không có quyền hủy booking này");
                return result;
            }
            
            // Check status
            if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
                result.put("success", false);
                result.put("message", "Booking đã bị hủy trước đó");
                return result;
            }
            
            if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
                result.put("success", false);
                result.put("message", "Không thể hủy booking đã hoàn thành");
                return result;
            }
            
            // Check 24-hour cancellation rule
            // Must cancel at least 24 hours before the booking time
            TimeSlot slot = timeSlotDAO.findById(booking.getSlotId());
            if (slot == null || slot.getStartTime() == null) {
                result.put("success", false);
                result.put("message", "Không tìm thấy thông tin khung giờ");
                return result;
            }
            
            // Calculate booking datetime (booking date + slot start time)
            LocalDateTime bookingDateTime = booking.getBookingDate().atTime(slot.getStartTime());
            LocalDateTime now = LocalDateTime.now();
            
            // Check if booking is at least 24 hours away
            long hoursUntilBooking = ChronoUnit.HOURS.between(now, bookingDateTime);
            if (hoursUntilBooking < 24) {
                result.put("success", false);
                result.put("message", "Chỉ có thể hủy lịch trước 24 giờ diễn ra buổi tập. Còn " + hoursUntilBooking + " giờ nữa đến giờ tập.");
                return result;
            }
            
            // Delete booking from database (xóa hoàn toàn khỏi database)
            // This will automatically free up the slot for others to book
            bookingDAO.delete(booking);
            
            LOGGER.info("Booking " + bookingId + " deleted by member " + memberId + 
                       ". Slot " + booking.getSlotId() + " is now available for " + 
                       booking.getBookingDate() + " at gym " + booking.getGymId());
            
            result.put("success", true);
            result.put("message", "Hủy lịch thành công. Khung giờ đã được giải phóng.");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error cancelling booking", e);
            result.put("success", false);
            result.put("message", "Lỗi hệ thống: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Confirm booking by trainer
     */
    public Map<String, Object> confirmBooking(Integer bookingId, Integer trainerId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            PTBooking booking = bookingDAO.findById(bookingId);
            
            if (booking == null) {
                result.put("success", false);
                result.put("message", "Không tìm thấy booking");
                return result;
            }
            
            // Check ownership
            if (!booking.getTrainerId().equals(trainerId)) {
                result.put("success", false);
                result.put("message", "Bạn không có quyền xác nhận booking này");
                return result;
            }
            
            // Check status
            if (booking.getBookingStatus() != BookingStatus.PENDING) {
                result.put("success", false);
                result.put("message", "Chỉ có thể xác nhận booking đang chờ");
                return result;
            }
            
            // Confirm
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingDAO.update(booking);
            
            result.put("success", true);
            result.put("message", "Xác nhận booking thành công");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error confirming booking", e);
            result.put("success", false);
            result.put("message", "Lỗi hệ thống: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Cancel booking by trainer (can cancel anytime)
     */
    public Map<String, Object> cancelBookingByTrainer(Integer bookingId, Integer trainerId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            PTBooking booking = bookingDAO.findById(bookingId);
            
            if (booking == null) {
                result.put("success", false);
                result.put("message", "Không tìm thấy booking");
                return result;
            }
            
            // Check ownership
            if (!booking.getTrainerId().equals(trainerId)) {
                result.put("success", false);
                result.put("message", "Bạn không có quyền hủy booking này");
                return result;
            }
            
            // Check status
            if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
                result.put("success", false);
                result.put("message", "Booking đã bị hủy trước đó");
                return result;
            }
            
            if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
                result.put("success", false);
                result.put("message", "Không thể hủy booking đã hoàn thành");
                return result;
            }
            
            // Cancel booking
            booking.setBookingStatus(BookingStatus.CANCELLED);
            booking.setCancelledBy(CancelledBy.TRAINER);
            booking.setCancelledAt(LocalDate.now());
            bookingDAO.update(booking);
            
            result.put("success", true);
            result.put("message", "Hủy booking thành công");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error cancelling booking", e);
            result.put("success", false);
            result.put("message", "Lỗi hệ thống: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Get member bookings
     */
    public List<PTBooking> getMemberBookings(Integer memberId) {
        try {
            return bookingDAO.findByMember(memberId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting member bookings", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Get trainer bookings
     */
    public List<PTBooking> getTrainerBookings(Integer trainerId) {
        try {
            return bookingDAO.findByTrainer(trainerId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting trainer bookings", e);
            return new ArrayList<>();
        }
    }
}
