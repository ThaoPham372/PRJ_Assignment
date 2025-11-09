package service.schedule;

import dao.schedule.TrainerExceptionDAO;
import model.schedule.TrainerException;
import model.schedule.ExceptionType;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service for TrainerException operations
 * Follows MVC pattern - Service layer between Controller and DAO
 */
public class TrainerExceptionService {
    private static final Logger LOGGER = Logger.getLogger(TrainerExceptionService.class.getName());
    private final TrainerExceptionDAO trainerExceptionDAO;

    public TrainerExceptionService() {
        this.trainerExceptionDAO = new TrainerExceptionDAO();
    }

    /**
     * Get all exceptions
     */
    public List<TrainerException> getAllExceptions() {
        try {
            return trainerExceptionDAO.findAll();
        } catch (Exception e) {
            LOGGER.severe("Error getting all exceptions: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve exceptions", e);
        }
    }

    /**
     * Get exception by ID
     */
    public TrainerException getExceptionById(Integer exceptionId) {
        if (exceptionId == null) {
            throw new IllegalArgumentException("Exception ID cannot be null");
        }
        try {
            return trainerExceptionDAO.findById(exceptionId);
        } catch (Exception e) {
            LOGGER.severe("Error getting exception by ID: " + exceptionId + " - " + e.getMessage());
            throw new RuntimeException("Failed to retrieve exception", e);
        }
    }

    /**
     * Get exceptions by trainer and date
     */
    public List<TrainerException> getExceptionsByTrainerAndDate(Integer trainerId, LocalDate date) {
        if (trainerId == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        try {
            return trainerExceptionDAO.findByTrainerAndDate(trainerId, date);
        } catch (Exception e) {
            LOGGER.severe("Error finding exceptions by trainer and date: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve exceptions", e);
        }
    }

    /**
     * Get exceptions by trainer in date range
     */
    public List<TrainerException> getExceptionsByTrainerInDateRange(
            Integer trainerId, LocalDate startDate, LocalDate endDate) {
        if (trainerId == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after or equal to start date");
        }
        try {
            return trainerExceptionDAO.findByTrainerInDateRange(trainerId, startDate, endDate);
        } catch (Exception e) {
            LOGGER.severe("Error finding exceptions in date range: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve exceptions", e);
        }
    }

    /**
     * Check if trainer has exception on specific date and slot
     */
    public boolean hasException(Integer trainerId, LocalDate date, Integer slotId) {
        if (trainerId == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        try {
            return trainerExceptionDAO.hasException(trainerId, date, slotId);
        } catch (Exception e) {
            LOGGER.severe("Error checking exception: " + e.getMessage());
            throw new RuntimeException("Failed to check exception", e);
        }
    }

    /**
     * Get exceptions by type
     */
    public List<TrainerException> getExceptionsByType(Integer trainerId, ExceptionType exceptionType) {
        if (trainerId == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        if (exceptionType == null) {
            throw new IllegalArgumentException("Exception type cannot be null");
        }
        try {
            return trainerExceptionDAO.findByType(trainerId, exceptionType);
        } catch (Exception e) {
            LOGGER.severe("Error finding exceptions by type: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve exceptions", e);
        }
    }

    /**
     * Create new exception
     */
    public int createException(TrainerException exception) {
        validateException(exception);
        try {
            return trainerExceptionDAO.save(exception);
        } catch (Exception e) {
            LOGGER.severe("Error creating exception: " + e.getMessage());
            throw new RuntimeException("Failed to create exception", e);
        }
    }

    /**
     * Update exception
     */
    public int updateException(TrainerException exception) {
        validateException(exception);
        if (exception.getExceptionId() == null) {
            throw new IllegalArgumentException("Exception ID is required for update");
        }
        try {
            return trainerExceptionDAO.update(exception);
        } catch (Exception e) {
            LOGGER.severe("Error updating exception: " + e.getMessage());
            throw new RuntimeException("Failed to update exception", e);
        }
    }

    /**
     * Delete exception
     */
    public int deleteException(TrainerException exception) {
        if (exception == null || exception.getExceptionId() == null) {
            throw new IllegalArgumentException("Exception cannot be null");
        }
        try {
            return trainerExceptionDAO.delete(exception);
        } catch (Exception e) {
            LOGGER.severe("Error deleting exception: " + e.getMessage());
            throw new RuntimeException("Failed to delete exception", e);
        }
    }

    /**
     * Validate exception entity
     */
    private void validateException(TrainerException exception) {
        if (exception == null) {
            throw new IllegalArgumentException("Exception cannot be null");
        }
        if (exception.getTrainerId() == null) {
            throw new IllegalArgumentException("Trainer ID is required");
        }
        if (exception.getExceptionDate() == null) {
            throw new IllegalArgumentException("Exception date is required");
        }
        if (exception.getExceptionType() == null) {
            throw new IllegalArgumentException("Exception type is required");
        }
    }
}

