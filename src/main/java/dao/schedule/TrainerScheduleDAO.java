package dao.schedule;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import dao.BaseDAO;
import jakarta.persistence.TypedQuery;
import model.schedule.BookingStatus;
import model.schedule.ExceptionType;
import model.schedule.PTBooking;
import model.schedule.TrainerException;
import model.schedule.TrainerSchedule;

public class TrainerScheduleDAO extends BaseDAO {

    public TrainerScheduleDAO() {
        super();
    }

    // 1. Lấy danh sách buổi tập của trainer
    public List<PTBooking> getBookingsByTrainer(int trainerId) {
        try {
            TypedQuery<PTBooking> q = em.createQuery(
                    "SELECT b FROM PTBooking b " +
                            "LEFT JOIN FETCH b.member " +
                            "LEFT JOIN FETCH b.timeSlot " +
                            "WHERE b.trainerId = :tid ORDER BY b.bookingDate DESC, b.slotId",
                    PTBooking.class);
            q.setParameter("tid", trainerId);
            return q.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting bookings by trainer: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 2. Cập nhật trạng thái buổi tập
    public void updateBookingStatus(int bookingId, BookingStatus status) {
        try {
            PTBooking booking = em.find(PTBooking.class, bookingId);
            if (booking != null) {
                booking.setBookingStatus(status);
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
                beginTransaction();
                em.merge(booking);
                commitTransaction();
            }
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("Error updating booking status: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update booking status", e);
        }
    }

    // 3. Lấy lịch làm việc định kỳ (theo tuần)
    public List<TrainerSchedule> getWeeklySchedule(int trainerId) {
        try {
            TypedQuery<TrainerSchedule> q = em.createQuery(
                    "SELECT s FROM TrainerSchedule s WHERE s.trainerId = :tid ORDER BY s.dayOfWeek",
                    TrainerSchedule.class);
            q.setParameter("tid", trainerId);
            return q.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting weekly schedule: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 4. Lấy danh sách exception (nghỉ/bận/đặc biệt)
    public List<TrainerException> getTrainerExceptions(int trainerId) {
        try {
            TypedQuery<TrainerException> q = em.createQuery(
                    "SELECT e FROM TrainerException e WHERE e.trainerId = :tid ORDER BY e.exceptionDate DESC",
                    TrainerException.class);
            q.setParameter("tid", trainerId);
            return q.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting trainer exceptions: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 5. Thêm exception mới
    public void addException(int trainerId, LocalDate date, int slotId, ExceptionType type, String reason) {
        try {
            TrainerException e = new TrainerException();
            e.setTrainerId(trainerId);
            e.setExceptionDate(date);
            e.setSlotId(slotId);
            e.setExceptionType(type);
            e.setReason(reason);
            e.setCreatedAt(LocalDate.now());
            beginTransaction();
            em.persist(e);
            commitTransaction();
        } catch (Exception ex) {
            rollbackTransaction();
            System.err.println("Error adding exception: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Failed to add exception", ex);
        }
    }

    // 6. Xóa exception
    public void deleteException(int exceptionId) {
        try {
            TrainerException e = em.find(TrainerException.class, exceptionId);
            if (e != null) {
                beginTransaction();
                em.remove(e);
                commitTransaction();
            }
        } catch (Exception ex) {
            rollbackTransaction();
            System.err.println("Error deleting exception: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Failed to delete exception", ex);
        }
    }

    // Lấy lịch cố định + đếm booking trong tuần [weekStart, weekEnd]
    public List<Object[]> getWeeklyFixedSchedule(int trainerId, java.sql.Date weekStart, java.sql.Date weekEnd) {
        String jpql = """
                    SELECT s.dayOfWeek, s.slotId, s.isAvailable, s.maxBookings, s.notes,
                           COUNT(b.bookingId) AS totalBooked,
                           SUM(CASE WHEN b.bookingStatus = 'CONFIRMED' THEN 1 ELSE 0 END) AS confirmedBooked,
                           SUM(CASE WHEN b.bookingStatus = 'PENDING'  THEN 1 ELSE 0 END) AS pendingBooked
                    FROM TrainerSchedule s
                    LEFT JOIN PTBooking b
                      ON b.trainerId = s.trainerId
                     AND b.slotId    = s.slotId
                     AND b.bookingDate BETWEEN :start AND :end
                    WHERE s.trainerId = :tid
                    GROUP BY s.dayOfWeek, s.slotId, s.isAvailable, s.maxBookings, s.notes
                    ORDER BY FIELD(s.dayOfWeek,'MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY'), s.slotId
                """;
        var q = em.createQuery(jpql, Object[].class);
        q.setParameter("tid", trainerId);
        q.setParameter("start", weekStart);
        q.setParameter("end", weekEnd);
        return q.getResultList();
    }

    // Lấy danh sách time_slots (để làm hàng đầu dòng)
    public List<Object[]> getTimeSlotsBasic() {
        // Trả về [slot_id, start_time, end_time] (giả định entity TimeSlot đã map 3 cột
        // này)
        String jpql = "SELECT t.slotId, t.startTime, t.endTime FROM TimeSlot t ORDER BY t.slotId";
        return em.createQuery(jpql, Object[].class).getResultList();
    }
}
