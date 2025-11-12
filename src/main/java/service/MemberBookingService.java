package service;

import dao.TrainerDAO;
import dao.schedule.PTBookingDAO;
import model.Trainer;
import model.schedule.BookingStatus;
import model.schedule.PTBooking;
import model.schedule.TimeSlot;
import service.schedule.TimeSlotService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * MemberBookingService - Service cho Member booking operations
 * 
 * Follows MVC pattern: Service layer separates business logic from controllers
 * Follows OOP principles: Uses composition with DAOs and Services
 */
public class MemberBookingService {
    
    private final PTBookingDAO bookingDAO;
    private final TimeSlotService timeSlotService;
    private final TrainerDAO trainerDAO;
    
    public MemberBookingService() {
        this.bookingDAO = new PTBookingDAO();
        this.timeSlotService = new TimeSlotService();
        this.trainerDAO = new TrainerDAO();
    }
    
    /**
     * Lấy lịch sử đặt lịch của member
     * @param memberId Member ID
     * @return List of PTBooking objects
     */
    public List<PTBooking> getMemberBookingHistory(int memberId) {
        return bookingDAO.findByMember(memberId);
    }
    
    /**
     * Lấy danh sách khung giờ trống dựa trên trainer, gym và ngày
     * 
     * Logic: Lấy tất cả time slots, loại bỏ các slot đã bị booked (CONFIRMED hoặc PENDING)
     * 
     * @param trainerId Trainer ID
     * @param gymId Gym ID
     * @param dateStr Date string (yyyy-MM-dd format)
     * @return List of available TimeSlot objects
     */
    public List<TimeSlot> getAvailableSlots(int trainerId, int gymId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        
        // Lấy tất cả time slots
        List<TimeSlot> allSlots = timeSlotService.getAllTimeSlots();
        
        // Lấy danh sách bookings đã có cho trainer/gym/date này
        List<PTBooking> existingBookings = bookingDAO.findByTrainer(trainerId);
        
        // Filter bookings theo gym và date, chỉ lấy các booking có status CONFIRMED hoặc PENDING
        Set<Integer> bookedSlotIds = existingBookings.stream()
                .filter(booking -> booking.getGymId().equals(gymId))
                .filter(booking -> booking.getBookingDate().equals(date))
                .filter(booking -> booking.getBookingStatus() == BookingStatus.CONFIRMED 
                        || booking.getBookingStatus() == BookingStatus.PENDING)
                .map(PTBooking::getSlotId)
                .collect(Collectors.toSet());
        
        // Trả về các slot không bị booked
        return allSlots.stream()
                .filter(slot -> slot.getIsActive() != null && slot.getIsActive())
                .filter(slot -> !bookedSlotIds.contains(slot.getSlotId()))
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy danh sách trainers theo gym
     * 
     * Logic: Tìm trainers có bookings tại gym này
     * 
     * @param gymId Gym ID
     * @return List of Trainer objects
     */
    public List<Trainer> getTrainersByGym(int gymId) {
        // Lấy tất cả bookings tại gym này
        List<PTBooking> bookings = bookingDAO.findAll();
        
        // Lấy danh sách trainer IDs từ bookings
        Set<Integer> trainerIds = bookings.stream()
                .filter(booking -> booking.getGymId() != null && booking.getGymId().equals(gymId))
                .map(PTBooking::getTrainerId)
                .collect(Collectors.toSet());
        
        // Lấy trainers từ IDs
        List<Trainer> trainers = new ArrayList<>();
        for (Integer trainerId : trainerIds) {
            Trainer trainer = trainerDAO.findById(trainerId);
            if (trainer != null) {
                trainers.add(trainer);
            }
        }
        
        // Nếu không tìm thấy trainers từ bookings, trả về tất cả trainers (fallback)
        if (trainers.isEmpty()) {
            trainers = trainerDAO.findAll();
        }
        
        return trainers;
    }
    
    /**
     * Tạo booking mới
     * 
     * @param memberId Member ID
     * @param trainerId Trainer ID
     * @param gymId Gym ID
     * @param slotId Slot ID
     * @param dateStr Date string (yyyy-MM-dd format)
     * @param notes Notes
     * @return true if success, false otherwise
     */
    public boolean createBooking(int memberId, int trainerId, int gymId, int slotId, String dateStr, String notes) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            
            // Kiểm tra xem slot đã bị booked chưa
            List<PTBooking> existingBookings = bookingDAO.findByTrainer(trainerId);
            boolean isSlotBooked = existingBookings.stream()
                    .anyMatch(booking -> booking.getGymId().equals(gymId)
                            && booking.getBookingDate().equals(date)
                            && booking.getSlotId().equals(slotId)
                            && (booking.getBookingStatus() == BookingStatus.CONFIRMED 
                                    || booking.getBookingStatus() == BookingStatus.PENDING));
            
            if (isSlotBooked) {
                return false; // Slot đã bị booked
            }
            
            // Tạo booking mới
            PTBooking booking = new PTBooking();
            booking.setMemberId(memberId);
            booking.setTrainerId(trainerId);
            booking.setGymId(gymId);
            booking.setSlotId(slotId);
            booking.setBookingDate(date);
            booking.setNotes(notes);
            booking.setBookingStatus(BookingStatus.PENDING);
            
            Integer bookingId = bookingDAO.save(booking);
            return bookingId != null && bookingId > 0;
            
        } catch (Exception e) {
            System.err.println("Error creating booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}


