package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     */
    public List<Trainer> getTrainersByGym(Integer gymId) {
        try {
            List<Trainer> allTrainers = trainerDAO.findAll();
            if (gymId == null) {
                return allTrainers;
            }
            // Filter active trainers only
            List<Trainer> activeTrainers = new ArrayList<>();
            for (Trainer t : allTrainers) {
                if ("ACTIVE".equalsIgnoreCase(t.getStatus())) {
                    activeTrainers.add(t);
                }
            }
            return activeTrainers;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting trainers", e);
            return new ArrayList<>();
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
            
            // If no schedule for this day, no slots available
            if (scheduledSlots.isEmpty()) {
                return availableSlots;
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
            
            // Check each slot
            for (Integer slotId : scheduledSlots) {
                // Skip if already booked
                if (!bookedSlots.contains(slotId)) {
                    availableSlots.add(slotId);
                }
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting available slots", e);
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
            
            // Check if date is in the past
            if (date.isBefore(LocalDate.now())) {
                result.put("success", false);
                result.put("message", "Không thể đặt lịch cho ngày trong quá khứ");
                return result;
            }
            
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
     * Cancel booking by member (must be 24h before)
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
            
            // Check 24h rule
            LocalDate bookingDate = booking.getBookingDate();
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            
            if (!bookingDate.isAfter(tomorrow)) {
                result.put("success", false);
                result.put("message", "Chỉ có thể hủy booking trước 24 giờ");
                return result;
            }
            
            // Cancel booking
            booking.setBookingStatus(BookingStatus.CANCELLED);
            booking.setCancelledBy(CancelledBy.MEMBER);
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
