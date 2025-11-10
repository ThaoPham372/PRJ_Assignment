package dao.schedule;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.GenericDAO;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.schedule.BookingStatus;
import model.schedule.CancelledBy;
import model.schedule.PTBooking;

/**
 * DAO for PTBooking entity - extends GenericDAO for CRUD operations
 * Follows OOP principles and reuses GenericDAO methods
 */
public class PTBookingDAO extends GenericDAO<PTBooking> {
    private static final Logger LOGGER = Logger.getLogger(PTBookingDAO.class.getName());

    public PTBookingDAO() {
        super(PTBooking.class);
    }

    /**
     * Find booking by trainer, gym, date and slot
     */
    public Optional<PTBooking> findByTrainerGymDateSlot(Integer trainerId, Integer gymId, LocalDate bookingDate,
            Integer slotId) {
        try {
            String jpql = "SELECT b FROM PTBooking b " +
                    "WHERE b.trainerId = :trainerId " +
                    "AND b.gymId = :gymId " +
                    "AND b.bookingDate = :bookingDate " +
                    "AND b.slotId = :slotId";

            TypedQuery<PTBooking> query = em.createQuery(jpql, PTBooking.class);
            query.setParameter("trainerId", trainerId);
            query.setParameter("gymId", gymId);
            query.setParameter("bookingDate", bookingDate);
            query.setParameter("slotId", slotId);

            try {
                return Optional.of(query.getSingleResult());
            } catch (NoResultException e) {
                return Optional.empty();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding booking", e);
            return Optional.empty();
        }
    }

    /**
     * Find bookings by member
     */
    public List<PTBooking> findByMember(Integer memberId) {
        try {
            String jpql = "SELECT b FROM PTBooking b " +
                    "LEFT JOIN FETCH b.trainer " +
                    "LEFT JOIN FETCH b.timeSlot " +
                    "WHERE b.memberId = :memberId " +
                    "ORDER BY b.bookingDate DESC, b.slotId";

            TypedQuery<PTBooking> query = em.createQuery(jpql, PTBooking.class);
            query.setParameter("memberId", memberId);

            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding bookings by member", e);
            return Collections.emptyList();
        }
    }

    /**
     * Find bookings by trainer
     */
    public List<PTBooking> findByTrainer(Integer trainerId) {
        try {
            String jpql = "SELECT b FROM PTBooking b " +
                    "LEFT JOIN FETCH b.member " +
                    "LEFT JOIN FETCH b.timeSlot " +
                    "WHERE b.trainerId = :trainerId " +
                    "ORDER BY b.bookingDate DESC, b.slotId";

            TypedQuery<PTBooking> query = em.createQuery(jpql, PTBooking.class);
            query.setParameter("trainerId", trainerId);

            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding bookings by trainer", e);
            return Collections.emptyList();
        }
    }

    /**
     * Find bookings by status
     */
    public List<PTBooking> findByStatus(BookingStatus status) {
        try {
            String jpql = "SELECT b FROM PTBooking b " +
                    "LEFT JOIN FETCH b.member " +
                    "LEFT JOIN FETCH b.trainer " +
                    "LEFT JOIN FETCH b.timeSlot " +
                    "WHERE b.bookingStatus = :status " +
                    "ORDER BY b.bookingDate, b.slotId";

            TypedQuery<PTBooking> query = em.createQuery(jpql, PTBooking.class);
            query.setParameter("status", status);

            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding bookings by status", e);
            return Collections.emptyList();
        }
    }

    /**
     * Find bookings by date range
     */
    public List<PTBooking> findByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            String jpql = "SELECT b FROM PTBooking b " +
                    "LEFT JOIN FETCH b.member " +
                    "LEFT JOIN FETCH b.trainer " +
                    "LEFT JOIN FETCH b.timeSlot " +
                    "WHERE b.bookingDate >= :startDate " +
                    "AND b.bookingDate <= :endDate " +
                    "ORDER BY b.bookingDate, b.slotId";

            TypedQuery<PTBooking> query = em.createQuery(jpql, PTBooking.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding bookings by date range", e);
            return Collections.emptyList();
        }
    }

    /**
     * Update booking status - reuses GenericDAO.update()
     */
    public void updateStatus(Integer bookingId, BookingStatus status) {
        try {
            PTBooking booking = findById(bookingId);
            if (booking == null) {
                throw new RuntimeException("Booking not found: " + bookingId);
            }

            booking.setBookingStatus(status);

            // Update timestamps based on status
            LocalDate now = LocalDate.now();
            switch (status) {
                case CONFIRMED:
                    booking.setConfirmedAt(now);
                    break;
                case COMPLETED:
                    booking.setCompletedAt(now);
                    break;
                case CANCELLED:
                    booking.setCancelledAt(now);
                    break;
                case PENDING:
                    // PENDING status doesn't require timestamp update
                    break;
            }

            update(booking);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating booking status", e);
            throw new RuntimeException("Failed to update booking status: " + e.getMessage(), e);
        }
    }

    /**
     * Cancel booking - reuses GenericDAO.update()
     */
    public void cancelBooking(Integer bookingId, String reason, CancelledBy cancelledBy) {
        try {
            PTBooking booking = findById(bookingId);
            if (booking == null) {
                throw new RuntimeException("Booking not found: " + bookingId);
            }

            booking.setBookingStatus(BookingStatus.CANCELLED);
            booking.setCancelledReason(reason);
            booking.setCancelledBy(cancelledBy);
            booking.setCancelledAt(LocalDate.now());

            update(booking);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error cancelling booking", e);
            throw new RuntimeException("Failed to cancel booking: " + e.getMessage(), e);
        }
    }
}
