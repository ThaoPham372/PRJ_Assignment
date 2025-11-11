package service.schedule;

import dao.schedule.TimeSlotDAO;
import model.schedule.TimeSlot;
import java.time.LocalTime;
import java.util.List;

/**
 * TimeSlotService - Service layer for TimeSlot operations
 * Follows service layer pattern and OOP principles
 */
public class TimeSlotService {
    private final TimeSlotDAO timeSlotDAO;

    public TimeSlotService() {
        this.timeSlotDAO = new TimeSlotDAO();
    }

    /**
     * Get all active time slots
     * @return List of active TimeSlot entities
     */
    public List<TimeSlot> getActiveTimeSlots() {
        return timeSlotDAO.findActiveSlots();
    }

    /**
     * Get all time slots (active and inactive)
     * @return List of all TimeSlot entities
     */
    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotDAO.findAll();
    }

    /**
     * Get time slot by ID
     * @param slotId The slot ID
     * @return TimeSlot entity or null if not found
     */
    public TimeSlot getTimeSlotById(Integer slotId) {
        return timeSlotDAO.findById(slotId);
    }

    /**
     * Find time slot by time range
     * @param startTime Start time
     * @param endTime End time
     * @return TimeSlot entity or null if not found
     */
    public TimeSlot findByTimeRange(LocalTime startTime, LocalTime endTime) {
        return timeSlotDAO.findByTimeRange(startTime, endTime);
    }

    /**
     * Create a new time slot
     * @param timeSlot TimeSlot entity to create
     * @return ID of the created time slot
     */
    public Integer createTimeSlot(TimeSlot timeSlot) {
        return timeSlotDAO.save(timeSlot);
    }

    /**
     * Update an existing time slot
     * @param timeSlot TimeSlot entity to update
     * @return Number of affected rows
     */
    public int updateTimeSlot(TimeSlot timeSlot) {
        return timeSlotDAO.update(timeSlot);
    }

    /**
     * Delete a time slot
     * @param timeSlot TimeSlot entity to delete
     * @return Number of affected rows
     */
    public int deleteTimeSlot(TimeSlot timeSlot) {
        return timeSlotDAO.delete(timeSlot);
    }

    /**
     * Delete a time slot by ID
     * @param slotId The slot ID
     * @return Number of affected rows
     */
    public int deleteTimeSlotById(Integer slotId) {
        TimeSlot timeSlot = timeSlotDAO.findById(slotId);
        if (timeSlot != null) {
            return timeSlotDAO.delete(timeSlot);
        }
        return -1;
    }
}

