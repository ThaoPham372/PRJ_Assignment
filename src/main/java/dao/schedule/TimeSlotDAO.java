package dao.schedule;

import dao.GenericDAO;
import model.schedule.TimeSlot;
import jakarta.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeSlotDAO extends GenericDAO<TimeSlot> {
    private static final Logger LOGGER = Logger.getLogger(TimeSlotDAO.class.getName());

    public TimeSlotDAO() {
        super(TimeSlot.class);
    }

    public List<TimeSlot> findActiveSlots() {
        try {
            String jpql = "SELECT t FROM TimeSlot t WHERE t.isActive = true ORDER BY t.startTime";
            TypedQuery<TimeSlot> query = em.createQuery(jpql, TimeSlot.class);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding active time slots", e);
            return Collections.emptyList();
        }
    }

    /**
     * Find time slot by time range - reuses GenericDAO.findByField()
     */
    public TimeSlot findByTimeRange(java.time.LocalTime startTime, java.time.LocalTime endTime) {
        try {
            String jpql = "SELECT t FROM TimeSlot t WHERE t.startTime = :startTime AND t.endTime = :endTime";
            TypedQuery<TimeSlot> query = em.createQuery(jpql, TimeSlot.class);
            query.setParameter("startTime", startTime);
            query.setParameter("endTime", endTime);
            
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding time slot by time range", e);
            return null;
        }
    }
}

