package service.schedule;

import dao.schedule.TimeSlotDAO;
import model.schedule.TimeSlot;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service for TimeSlot operations
 * Follows MVC pattern - Service layer between Controller and DAO
 */
public class TimeSlotService {
    private static final Logger LOGGER = Logger.getLogger(TimeSlotService.class.getName());
    private final TimeSlotDAO timeSlotDAO;

    public TimeSlotService() {
        this.timeSlotDAO = new TimeSlotDAO();
    }

    /**
     * Get all time slots
     */
    public List<TimeSlot> getAllTimeSlots() {
        try {
            return timeSlotDAO.findAll();
        } catch (Exception e) {
            LOGGER.severe("Error getting all time slots: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve time slots", e);
        }
    }

    /**
     * Get active time slots only
     */
    public List<TimeSlot> getActiveTimeSlots() {
        try {
            return timeSlotDAO.findActiveSlots();
        } catch (Exception e) {
            LOGGER.severe("Error getting active time slots: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve active time slots", e);
        }
    }

    /**
     * Get time slot by ID
     */
    public TimeSlot getTimeSlotById(Integer slotId) {
        if (slotId == null) {
            throw new IllegalArgumentException("Slot ID cannot be null");
        }
        try {
            return timeSlotDAO.findById(slotId);
        } catch (Exception e) {
            LOGGER.severe("Error getting time slot by ID: " + slotId + " - " + e.getMessage());
            throw new RuntimeException("Failed to retrieve time slot", e);
        }
    }

    /**
     * Find time slot by time range
     */
    public TimeSlot getTimeSlotByTimeRange(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        try {
            return timeSlotDAO.findByTimeRange(startTime, endTime);
        } catch (Exception e) {
            LOGGER.severe("Error finding time slot by time range: " + e.getMessage());
            throw new RuntimeException("Failed to find time slot", e);
        }
    }

    /**
     * Create new time slot
     */
    public int createTimeSlot(TimeSlot timeSlot) {
        validateTimeSlot(timeSlot);
        try {
            // Calculate duration if not provided
            if (timeSlot.getDurationMinutes() == null && 
                timeSlot.getStartTime() != null && 
                timeSlot.getEndTime() != null) {
                int duration = (int) java.time.Duration.between(
                    timeSlot.getStartTime(), 
                    timeSlot.getEndTime()
                ).toMinutes();
                timeSlot.setDurationMinutes(duration);
            }
            
            return timeSlotDAO.save(timeSlot);
        } catch (Exception e) {
            LOGGER.severe("Error creating time slot: " + e.getMessage());
            throw new RuntimeException("Failed to create time slot", e);
        }
    }

    /**
     * Update time slot
     */
    public int updateTimeSlot(TimeSlot timeSlot) {
        validateTimeSlot(timeSlot);
        if (timeSlot.getSlotId() == null) {
            throw new IllegalArgumentException("Slot ID is required for update");
        }
        try {
            // Recalculate duration if times changed
            if (timeSlot.getStartTime() != null && timeSlot.getEndTime() != null) {
                int duration = (int) java.time.Duration.between(
                    timeSlot.getStartTime(), 
                    timeSlot.getEndTime()
                ).toMinutes();
                timeSlot.setDurationMinutes(duration);
            }
            
            return timeSlotDAO.update(timeSlot);
        } catch (Exception e) {
            LOGGER.severe("Error updating time slot: " + e.getMessage());
            throw new RuntimeException("Failed to update time slot", e);
        }
    }

    /**
     * Delete time slot
     */
    public int deleteTimeSlot(TimeSlot timeSlot) {
        if (timeSlot == null || timeSlot.getSlotId() == null) {
            throw new IllegalArgumentException("Time slot cannot be null");
        }
        try {
            return timeSlotDAO.delete(timeSlot);
        } catch (Exception e) {
            LOGGER.severe("Error deleting time slot: " + e.getMessage());
            throw new RuntimeException("Failed to delete time slot", e);
        }
    }

    /**
     * Validate time slot data
     */
    private void validateTimeSlot(TimeSlot timeSlot) {
        if (timeSlot == null) {
            throw new IllegalArgumentException("Time slot cannot be null");
        }
        if (timeSlot.getStartTime() == null || timeSlot.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time are required");
        }
        if (timeSlot.getEndTime().isBefore(timeSlot.getStartTime()) || 
            timeSlot.getEndTime().equals(timeSlot.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (timeSlot.getSlotName() == null || timeSlot.getSlotName().trim().isEmpty()) {
            throw new IllegalArgumentException("Slot name is required");
        }
    }
}

