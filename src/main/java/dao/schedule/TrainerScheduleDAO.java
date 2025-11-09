package dao.schedule;

import dao.GenericDAO;
import model.schedule.TrainerSchedule;
import model.schedule.DayOfWeek;
import jakarta.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for TrainerSchedule entity - extends GenericDAO for CRUD operations
 * Follows OOP principles and reuses GenericDAO methods
 */
public class TrainerScheduleDAO extends GenericDAO<TrainerSchedule> {
    private static final Logger LOGGER = Logger.getLogger(TrainerScheduleDAO.class.getName());

    public TrainerScheduleDAO() {
        super(TrainerSchedule.class);
    }

    /**
     * Find schedule by trainer, gym, day and slot
     */
    public Optional<TrainerSchedule> findByTrainerGymDaySlot(Integer trainerId, Integer gymId, DayOfWeek dayOfWeek, Integer slotId) {
        try {
            String jpql = "SELECT ts FROM TrainerSchedule ts " +
                         "WHERE ts.trainerId = :trainerId " +
                         "AND ts.gymId = :gymId " +
                         "AND ts.dayOfWeek = :dayOfWeek " +
                         "AND ts.slotId = :slotId";
            
            TypedQuery<TrainerSchedule> query = em.createQuery(jpql, TrainerSchedule.class);
            query.setParameter("trainerId", trainerId);
            query.setParameter("gymId", gymId);
            query.setParameter("dayOfWeek", dayOfWeek);
            query.setParameter("slotId", slotId);
            
            try {
                return Optional.of(query.getSingleResult());
            } catch (NoResultException e) {
                return Optional.empty();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding trainer schedule", e);
            return Optional.empty();
        }
    }

    /**
     * Find all schedules by trainer and day
     */
    public List<TrainerSchedule> findByTrainerAndDay(Integer trainerId, DayOfWeek dayOfWeek) {
        try {
            String jpql = "SELECT ts FROM TrainerSchedule ts " +
                         "LEFT JOIN FETCH ts.timeSlot " +
                         "WHERE ts.trainerId = :trainerId " +
                         "AND ts.dayOfWeek = :dayOfWeek " +
                         "AND ts.isAvailable = true " +
                         "ORDER BY ts.slotId";
            
            TypedQuery<TrainerSchedule> query = em.createQuery(jpql, TrainerSchedule.class);
            query.setParameter("trainerId", trainerId);
            query.setParameter("dayOfWeek", dayOfWeek);
            
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding schedules by trainer and day", e);
            return Collections.emptyList();
        }
    }

    /**
     * Find all available schedules by trainer
     */
    public List<TrainerSchedule> findAvailableByTrainer(Integer trainerId) {
        try {
            String jpql = "SELECT ts FROM TrainerSchedule ts " +
                         "LEFT JOIN FETCH ts.timeSlot " +
                         "WHERE ts.trainerId = :trainerId " +
                         "AND ts.isAvailable = true " +
                         "ORDER BY ts.dayOfWeek, ts.slotId";
            
            TypedQuery<TrainerSchedule> query = em.createQuery(jpql, TrainerSchedule.class);
            query.setParameter("trainerId", trainerId);
            
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding available schedules by trainer", e);
            return Collections.emptyList();
        }
    }

    /**
     * Update schedule availability - reuses GenericDAO.update()
     */
    public void updateAvailability(Integer scheduleId, Boolean isAvailable) {
        try {
            TrainerSchedule schedule = findById(scheduleId);
            if (schedule == null) {
                throw new RuntimeException("Schedule not found: " + scheduleId);
            }
            
            schedule.setIsAvailable(isAvailable);
            update(schedule);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating schedule availability", e);
            throw new RuntimeException("Failed to update schedule availability: " + e.getMessage(), e);
        }
    }
}

