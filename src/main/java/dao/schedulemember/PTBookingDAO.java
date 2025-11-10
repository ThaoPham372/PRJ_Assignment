package dao.schedulemember;

import dao.GenericDAO;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.schedule.BookingStatus;
import model.schedule.CancelledBy;
import model.schedule.PTBooking;
import java.time.LocalDate;

public class PTBookingDAO extends GenericDAO<PTBooking> {

    private static final Logger LOGGER = Logger.getLogger(PTBookingDAO.class.getName());

    public PTBookingDAO() {
        super(PTBooking.class);
    }

    public void saveBooking(PTBooking booking) throws Exception {
        super.save(booking);
    }

    public List<PTBooking> findByMemberId(int memberId) {
        try {
            // Sử dụng 'em' được kế thừa từ BaseDAO/GenericDAO
            String jpql = "SELECT b FROM PTBooking b WHERE b.member.id = :mid ORDER BY b.bookingDate DESC";

            TypedQuery<PTBooking> query = em.createQuery(jpql, PTBooking.class);
            query.setParameter("mid", memberId);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding bookings by member ID: " + memberId, e);
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
                default:
                    // PENDING or others might not need timestamp update
                    break;
            }

            super.update(booking);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating booking status for ID: " + bookingId, e);
            throw new RuntimeException("Failed to update booking status", e);
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

            super.update(booking);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error cancelling booking ID: " + bookingId, e);
            throw new RuntimeException("Failed to cancel booking", e);
        }
    }
}