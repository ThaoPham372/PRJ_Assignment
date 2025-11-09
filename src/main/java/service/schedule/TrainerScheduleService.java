package service.schedule;

import dao.schedule.TrainerScheduleDAO;
import model.schedule.TrainerSchedule;
import model.schedule.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service for TrainerSchedule operations
 * Follows MVC pattern - Service layer between Controller and DAO
 */
public class TrainerScheduleService {
    private static final Logger LOGGER = Logger.getLogger(TrainerScheduleService.class.getName());
    private final TrainerScheduleDAO trainerScheduleDAO;

    public TrainerScheduleService() {
        this.trainerScheduleDAO = new TrainerScheduleDAO();
    }

    /**
     * Get all schedules
     */
    public List<TrainerSchedule> getAllSchedules() {
        try {
            return trainerScheduleDAO.findAll();
        } catch (Exception e) {
            LOGGER.severe("Error getting all schedules: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve schedules", e);
        }
    }

    /**
     * Get schedule by ID
     */
    public TrainerSchedule getScheduleById(Integer scheduleId) {
        if (scheduleId == null) {
            throw new IllegalArgumentException("Schedule ID cannot be null");
        }
        try {
            return trainerScheduleDAO.findById(scheduleId);
        } catch (Exception e) {
            LOGGER.severe("Error getting schedule by ID: " + scheduleId + " - " + e.getMessage());
            throw new RuntimeException("Failed to retrieve schedule", e);
        }
    }

    /**
     * Get schedule by trainer, gym, day and slot
     */
    public Optional<TrainerSchedule> getScheduleByTrainerGymDaySlot(
            Integer trainerId, Integer gymId, DayOfWeek dayOfWeek, Integer slotId) {
        validateScheduleParams(trainerId, gymId, dayOfWeek, slotId);
        try {
            return trainerScheduleDAO.findByTrainerGymDaySlot(trainerId, gymId, dayOfWeek, slotId);
        } catch (Exception e) {
            LOGGER.severe("Error finding schedule: " + e.getMessage());
            throw new RuntimeException("Failed to find schedule", e);
        }
    }

    /**
     * Get schedules by trainer and day
     */
    public List<TrainerSchedule> getSchedulesByTrainerAndDay(Integer trainerId, DayOfWeek dayOfWeek) {
        if (trainerId == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null");
        }
        try {
            return trainerScheduleDAO.findByTrainerAndDay(trainerId, dayOfWeek);
        } catch (Exception e) {
            LOGGER.severe("Error finding schedules by trainer and day: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve schedules", e);
        }
    }

    /**
     * Get all available schedules by trainer
     */
    public List<TrainerSchedule> getAvailableSchedulesByTrainer(Integer trainerId) {
        if (trainerId == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        try {
            return trainerScheduleDAO.findAvailableByTrainer(trainerId);
        } catch (Exception e) {
            LOGGER.severe("Error finding available schedules: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve available schedules", e);
        }
    }

    /**
     * Create new schedule
     */
    public int createSchedule(TrainerSchedule schedule) {
        validateSchedule(schedule);
        try {
            // Check if schedule already exists
            Optional<TrainerSchedule> existing = trainerScheduleDAO.findByTrainerGymDaySlot(
                schedule.getTrainerId(), 
                schedule.getGymId(), 
                schedule.getDayOfWeek(), 
                schedule.getSlotId()
            );
            
            if (existing.isPresent()) {
                throw new IllegalArgumentException("Schedule already exists for this trainer, gym, day and slot");
            }
            
            return trainerScheduleDAO.save(schedule);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.severe("Error creating schedule: " + e.getMessage());
            throw new RuntimeException("Failed to create schedule", e);
        }
    }

    /**
     * Update schedule
     */
    public int updateSchedule(TrainerSchedule schedule) {
        validateSchedule(schedule);
        if (schedule.getScheduleId() == null) {
            throw new IllegalArgumentException("Schedule ID is required for update");
        }
        try {
            return trainerScheduleDAO.update(schedule);
        } catch (Exception e) {
            LOGGER.severe("Error updating schedule: " + e.getMessage());
            throw new RuntimeException("Failed to update schedule", e);
        }
    }

    /**
     * Update schedule availability
     */
    public void updateScheduleAvailability(Integer scheduleId, Boolean isAvailable) {
        if (scheduleId == null) {
            throw new IllegalArgumentException("Schedule ID cannot be null");
        }
        if (isAvailable == null) {
            throw new IllegalArgumentException("Availability status cannot be null");
        }
        try {
            trainerScheduleDAO.updateAvailability(scheduleId, isAvailable);
        } catch (Exception e) {
            LOGGER.severe("Error updating schedule availability: " + e.getMessage());
            throw new RuntimeException("Failed to update schedule availability", e);
        }
    }

    /**
     * Delete schedule
     */
    public int deleteSchedule(TrainerSchedule schedule) {
        if (schedule == null || schedule.getScheduleId() == null) {
            throw new IllegalArgumentException("Schedule cannot be null");
        }
        try {
            return trainerScheduleDAO.delete(schedule);
        } catch (Exception e) {
            LOGGER.severe("Error deleting schedule: " + e.getMessage());
            throw new RuntimeException("Failed to delete schedule", e);
        }
    }

    /**
     * Validate schedule parameters
     */
    private void validateScheduleParams(Integer trainerId, Integer gymId, DayOfWeek dayOfWeek, Integer slotId) {
        if (trainerId == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        if (gymId == null) {
            throw new IllegalArgumentException("Gym ID cannot be null");
        }
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null");
        }
        if (slotId == null) {
            throw new IllegalArgumentException("Slot ID cannot be null");
        }
    }

    /**
     * Validate schedule entity
     */
    private void validateSchedule(TrainerSchedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null");
        }
        validateScheduleParams(
            schedule.getTrainerId(), 
            schedule.getGymId(), 
            schedule.getDayOfWeek(), 
            schedule.getSlotId()
        );
    }
}

