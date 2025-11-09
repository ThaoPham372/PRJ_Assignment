package service.schedule;

import dao.schedule.PTBookingDAO;
import dao.schedule.TrainerScheduleDAO;
import dao.schedule.TrainerExceptionDAO;
import model.schedule.PTBooking;
import model.schedule.BookingStatus;
import model.schedule.CancelledBy;
import model.schedule.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service for PTBooking operations
 * Follows MVC pattern - Service layer between Controller and DAO
 * Contains complex business logic for booking management
 */
public class PTBookingService {
    private static final Logger LOGGER = Logger.getLogger(PTBookingService.class.getName());
    private final PTBookingDAO ptBookingDAO;
    private final TrainerScheduleDAO trainerScheduleDAO;
    private final TrainerExceptionDAO trainerExceptionDAO;

    public PTBookingService() {
        this.ptBookingDAO = new PTBookingDAO();
        this.trainerScheduleDAO = new TrainerScheduleDAO();
        this.trainerExceptionDAO = new TrainerExceptionDAO();
    }

    /**
     * Get all bookings
     */
    public List<PTBooking> getAllBookings() {
        try {
            return ptBookingDAO.findAll();
        } catch (Exception e) {
            LOGGER.severe("Error getting all bookings: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve bookings", e);
        }
    }

    /**
     * Get booking by ID
     */
    public PTBooking getBookingById(Integer bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        try {
            return ptBookingDAO.findById(bookingId);
        } catch (Exception e) {
            LOGGER.severe("Error getting booking by ID: " + bookingId + " - " + e.getMessage());
            throw new RuntimeException("Failed to retrieve booking", e);
        }
    }

    /**
     * Get bookings by member
     */
    public List<PTBooking> getBookingsByMember(Integer memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("Member ID cannot be null");
        }
        try {
            return ptBookingDAO.findByMember(memberId);
        } catch (Exception e) {
            LOGGER.severe("Error finding bookings by member: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve bookings", e);
        }
    }

    /**
     * Get bookings by trainer
     */
    public List<PTBooking> getBookingsByTrainer(Integer trainerId) {
        if (trainerId == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        try {
            return ptBookingDAO.findByTrainer(trainerId);
        } catch (Exception e) {
            LOGGER.severe("Error finding bookings by trainer: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve bookings", e);
        }
    }

    /**
     * Get bookings by status
     */
    public List<PTBooking> getBookingsByStatus(BookingStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Booking status cannot be null");
        }
        try {
            return ptBookingDAO.findByStatus(status);
        } catch (Exception e) {
            LOGGER.severe("Error finding bookings by status: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve bookings", e);
        }
    }

    /**
     * Get bookings by date range
     */
    public List<PTBooking> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after or equal to start date");
        }
        try {
            return ptBookingDAO.findByDateRange(startDate, endDate);
        } catch (Exception e) {
            LOGGER.severe("Error finding bookings by date range: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve bookings", e);
        }
    }

    /**
     * Check if slot is available for booking
     * Business logic: Check schedule, exceptions, and existing bookings
     */
    public boolean isSlotAvailable(Integer trainerId, Integer gymId, LocalDate bookingDate, Integer slotId) {
        if (trainerId == null || gymId == null || bookingDate == null || slotId == null) {
            throw new IllegalArgumentException("All parameters are required for availability check");
        }
        
        try {
            // Check if date is in the past
            if (bookingDate.isBefore(LocalDate.now())) {
                return false;
            }
            
            // Get day of week
            DayOfWeek dayOfWeek = getDayOfWeek(bookingDate);
            
            // Check if trainer has schedule for this day and slot
            Optional<model.schedule.TrainerSchedule> schedule = trainerScheduleDAO
                .findByTrainerGymDaySlot(trainerId, gymId, dayOfWeek, slotId);
            
            if (schedule.isEmpty() || !schedule.get().getIsAvailable()) {
                return false;
            }
            
            // Check if trainer has exception on this date and slot
            if (trainerExceptionDAO.hasException(trainerId, bookingDate, slotId)) {
                return false;
            }
            
            // Check if there's already a booking for this slot
            Optional<PTBooking> existingBooking = ptBookingDAO
                .findByTrainerGymDateSlot(trainerId, gymId, bookingDate, slotId);
            
            if (existingBooking.isPresent()) {
                BookingStatus status = existingBooking.get().getBookingStatus();
                // Slot is available if existing booking is cancelled
                return status == BookingStatus.CANCELLED;
            }
            
            return true;
        } catch (Exception e) {
            LOGGER.severe("Error checking slot availability: " + e.getMessage());
            throw new RuntimeException("Failed to check slot availability", e);
        }
    }

    /**
     * Create new booking with validation
     */
    public int createBooking(PTBooking booking) {
        validateBooking(booking);
        
        // Check availability
        if (!isSlotAvailable(
                booking.getTrainerId(), 
                booking.getGymId(), 
                booking.getBookingDate(), 
                booking.getSlotId())) {
            throw new IllegalStateException("Slot is not available for booking");
        }
        
        // Check for existing booking
        Optional<PTBooking> existing = ptBookingDAO.findByTrainerGymDateSlot(
            booking.getTrainerId(),
            booking.getGymId(),
            booking.getBookingDate(),
            booking.getSlotId()
        );
        
        if (existing.isPresent() && existing.get().getBookingStatus() != BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking already exists for this slot");
        }
        
        try {
            return ptBookingDAO.save(booking);
        } catch (Exception e) {
            LOGGER.severe("Error creating booking: " + e.getMessage());
            throw new RuntimeException("Failed to create booking", e);
        }
    }

    /**
     * Update booking
     */
    public int updateBooking(PTBooking booking) {
        validateBooking(booking);
        if (booking.getBookingId() == null) {
            throw new IllegalArgumentException("Booking ID is required for update");
        }
        try {
            return ptBookingDAO.update(booking);
        } catch (Exception e) {
            LOGGER.severe("Error updating booking: " + e.getMessage());
            throw new RuntimeException("Failed to update booking", e);
        }
    }

    /**
     * Confirm booking
     */
    public void confirmBooking(Integer bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        try {
            PTBooking booking = ptBookingDAO.findById(bookingId);
            if (booking == null) {
                throw new IllegalArgumentException("Booking not found: " + bookingId);
            }
            if (booking.getBookingStatus() != BookingStatus.PENDING) {
                throw new IllegalStateException("Only pending bookings can be confirmed");
            }
            ptBookingDAO.updateStatus(bookingId, BookingStatus.CONFIRMED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.severe("Error confirming booking: " + e.getMessage());
            throw new RuntimeException("Failed to confirm booking", e);
        }
    }

    /**
     * Complete booking
     */
    public void completeBooking(Integer bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        try {
            PTBooking booking = ptBookingDAO.findById(bookingId);
            if (booking == null) {
                throw new IllegalArgumentException("Booking not found: " + bookingId);
            }
            if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
                throw new IllegalStateException("Only confirmed bookings can be completed");
            }
            ptBookingDAO.updateStatus(bookingId, BookingStatus.COMPLETED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.severe("Error completing booking: " + e.getMessage());
            throw new RuntimeException("Failed to complete booking", e);
        }
    }

    /**
     * Cancel booking
     */
    public void cancelBooking(Integer bookingId, String reason, CancelledBy cancelledBy) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Cancellation reason is required");
        }
        if (cancelledBy == null) {
            throw new IllegalArgumentException("Cancelled by information is required");
        }
        try {
            PTBooking booking = ptBookingDAO.findById(bookingId);
            if (booking == null) {
                throw new IllegalArgumentException("Booking not found: " + bookingId);
            }
            if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
                throw new IllegalStateException("Booking is already cancelled");
            }
            if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
                throw new IllegalStateException("Completed bookings cannot be cancelled");
            }
            ptBookingDAO.cancelBooking(bookingId, reason, cancelledBy);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.severe("Error cancelling booking: " + e.getMessage());
            throw new RuntimeException("Failed to cancel booking", e);
        }
    }

    /**
     * Delete booking (only for pending or cancelled bookings)
     */
    public int deleteBooking(PTBooking booking) {
        if (booking == null || booking.getBookingId() == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        if (booking.getBookingStatus() == BookingStatus.CONFIRMED || 
            booking.getBookingStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot delete confirmed or completed bookings");
        }
        try {
            return ptBookingDAO.delete(booking);
        } catch (Exception e) {
            LOGGER.severe("Error deleting booking: " + e.getMessage());
            throw new RuntimeException("Failed to delete booking", e);
        }
    }

    /**
     * Get upcoming bookings for member
     */
    public List<PTBooking> getUpcomingBookingsByMember(Integer memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("Member ID cannot be null");
        }
        try {
            List<PTBooking> bookings = ptBookingDAO.findByMember(memberId);
            LocalDate today = LocalDate.now();
            return bookings.stream()
                .filter(b -> b.getBookingDate().isAfter(today) || b.getBookingDate().equals(today))
                .filter(b -> b.getBookingStatus() != BookingStatus.CANCELLED)
                .sorted((a, b) -> {
                    int dateCompare = a.getBookingDate().compareTo(b.getBookingDate());
                    if (dateCompare != 0) return dateCompare;
                    return a.getSlotId().compareTo(b.getSlotId());
                })
                .toList();
        } catch (Exception e) {
            LOGGER.severe("Error getting upcoming bookings: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve upcoming bookings", e);
        }
    }

    /**
     * Get upcoming bookings for trainer
     */
    public List<PTBooking> getUpcomingBookingsByTrainer(Integer trainerId) {
        if (trainerId == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        try {
            List<PTBooking> bookings = ptBookingDAO.findByTrainer(trainerId);
            LocalDate today = LocalDate.now();
            return bookings.stream()
                .filter(b -> b.getBookingDate().isAfter(today) || b.getBookingDate().equals(today))
                .filter(b -> b.getBookingStatus() != BookingStatus.CANCELLED)
                .sorted((a, b) -> {
                    int dateCompare = a.getBookingDate().compareTo(b.getBookingDate());
                    if (dateCompare != 0) return dateCompare;
                    return a.getSlotId().compareTo(b.getSlotId());
                })
                .toList();
        } catch (Exception e) {
            LOGGER.severe("Error getting upcoming bookings: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve upcoming bookings", e);
        }
    }

    /**
     * Validate booking entity
     */
    private void validateBooking(PTBooking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        if (booking.getMemberId() == null) {
            throw new IllegalArgumentException("Member ID is required");
        }
        if (booking.getTrainerId() == null) {
            throw new IllegalArgumentException("Trainer ID is required");
        }
        if (booking.getGymId() == null) {
            throw new IllegalArgumentException("Gym ID is required");
        }
        if (booking.getSlotId() == null) {
            throw new IllegalArgumentException("Slot ID is required");
        }
        if (booking.getBookingDate() == null) {
            throw new IllegalArgumentException("Booking date is required");
        }
        if (booking.getBookingDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot book in the past");
        }
    }

    /**
     * Get day of week from date
     */
    private DayOfWeek getDayOfWeek(LocalDate date) {
        java.time.DayOfWeek dayOfWeek = date.getDayOfWeek();
        return switch (dayOfWeek) {
            case MONDAY -> DayOfWeek.MONDAY;
            case TUESDAY -> DayOfWeek.TUESDAY;
            case WEDNESDAY -> DayOfWeek.WEDNESDAY;
            case THURSDAY -> DayOfWeek.THURSDAY;
            case FRIDAY -> DayOfWeek.FRIDAY;
            case SATURDAY -> DayOfWeek.SATURDAY;
            case SUNDAY -> DayOfWeek.SUNDAY;
        };
    }
}

