package dao.schedule;

import dao.GenericDAO;
import model.schedule.TrainerException;
import model.schedule.ExceptionType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for TrainerException entity - extends GenericDAO for CRUD operations
 * Follows OOP principles and reuses GenericDAO methods
 */
public class TrainerExceptionDAO extends GenericDAO<TrainerException> {
    private static final Logger LOGGER = Logger.getLogger(TrainerExceptionDAO.class.getName());

    public TrainerExceptionDAO() {
        super(TrainerException.class);
    }

    /**
     * Find exceptions by trainer and date
     */
    public List<TrainerException> findByTrainerAndDate(Integer trainerId, LocalDate date) {
        try {
            String jpql = "SELECT te FROM TrainerException te " +
                         "LEFT JOIN FETCH te.timeSlot " +
                         "WHERE te.trainerId = :trainerId " +
                         "AND te.exceptionDate = :date";
            
            TypedQuery<TrainerException> query = em.createQuery(jpql, TrainerException.class);
            query.setParameter("trainerId", trainerId);
            query.setParameter("date", date);
            
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding exceptions by trainer and date", e);
            return Collections.emptyList();
        }
    }

    /**
     * Find exceptions by trainer in date range
     */
    public List<TrainerException> findByTrainerInDateRange(Integer trainerId, LocalDate startDate, LocalDate endDate) {
        try {
            String jpql = "SELECT te FROM TrainerException te " +
                         "LEFT JOIN FETCH te.timeSlot " +
                         "WHERE te.trainerId = :trainerId " +
                         "AND te.exceptionDate >= :startDate " +
                         "AND te.exceptionDate <= :endDate " +
                         "ORDER BY te.exceptionDate";
            
            TypedQuery<TrainerException> query = em.createQuery(jpql, TrainerException.class);
            query.setParameter("trainerId", trainerId);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding exceptions in date range", e);
            return Collections.emptyList();
        }
    }

    /**
     * Check if trainer has exception on specific date and slot
     */
    public boolean hasException(Integer trainerId, LocalDate date, Integer slotId) {
        try {
            String jpql = "SELECT COUNT(te) FROM TrainerException te " +
                         "WHERE te.trainerId = :trainerId " +
                         "AND te.exceptionDate = :date " +
                         "AND (te.slotId = :slotId OR te.slotId IS NULL)";
            
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("trainerId", trainerId);
            query.setParameter("date", date);
            query.setParameter("slotId", slotId);
            
            Long count = query.getSingleResult();
            return count != null && count > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking exception", e);
            return false;
        }
    }

    /**
     * Find exceptions by type
     */
    public List<TrainerException> findByType(Integer trainerId, ExceptionType exceptionType) {
        try {
            String jpql = "SELECT te FROM TrainerException te " +
                         "LEFT JOIN FETCH te.timeSlot " +
                         "WHERE te.trainerId = :trainerId " +
                         "AND te.exceptionType = :exceptionType " +
                         "ORDER BY te.exceptionDate DESC";
            
            TypedQuery<TrainerException> query = em.createQuery(jpql, TrainerException.class);
            query.setParameter("trainerId", trainerId);
            query.setParameter("exceptionType", exceptionType);
            
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding exceptions by type", e);
            return Collections.emptyList();
        }
    }
}

