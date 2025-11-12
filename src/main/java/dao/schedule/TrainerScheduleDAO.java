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
    
    // Get schedules by gym ID
    public List<TrainerSchedule> findByGymId(Integer gymId) {
        try {
            TypedQuery<TrainerSchedule> q = em.createQuery(
                    "SELECT s FROM TrainerSchedule s WHERE s.gymId = :gymId",
                    TrainerSchedule.class);
            q.setParameter("gymId", gymId);
            return q.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting schedules by gym ID: " + e.getMessage());
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

    // Lấy lịch thực tế cho tuần (linh hoạt từ tất cả time slots + bookings +
    // exceptions)
    // Trả về: [slotId, actualDate, dayOfWeek, confirmedCount, pendingCount,
    // hasException, exceptionType, hasSchedule, isAvailable]
    public List<Object[]> getWeeklyFixedSchedule(int trainerId, java.sql.Date weekStart, java.sql.Date weekEnd) {
        // Query linh hoạt: Lấy tất cả time slots, với mỗi slot và mỗi ngày trong tuần,
        // kiểm tra bookings, exceptions và schedules
        String sql = """
                    SELECT
                        ts.slot_id,
                        DATE_ADD(?, INTERVAL day_offset DAY) AS actual_date,
                        CASE day_offset
                            WHEN 0 THEN 'MONDAY'
                            WHEN 1 THEN 'TUESDAY'
                            WHEN 2 THEN 'WEDNESDAY'
                            WHEN 3 THEN 'THURSDAY'
                            WHEN 4 THEN 'FRIDAY'
                            WHEN 5 THEN 'SATURDAY'
                            WHEN 6 THEN 'SUNDAY'
                        END AS day_of_week,
                        COALESCE(SUM(CASE WHEN b.booking_status = 'CONFIRMED' THEN 1 ELSE 0 END), 0) AS confirmed_count,
                        COALESCE(SUM(CASE WHEN b.booking_status = 'PENDING' THEN 1 ELSE 0 END), 0) AS pending_count,
                        CASE WHEN e.exception_id IS NOT NULL THEN 1 ELSE 0 END AS has_exception,
                        e.exception_type,
                        CASE WHEN s.schedule_id IS NOT NULL THEN 1 ELSE 0 END AS has_schedule,
                        COALESCE(s.is_available, 0) AS is_available
                    FROM time_slots ts
                    CROSS JOIN (
                        SELECT 0 AS day_offset UNION SELECT 1 UNION SELECT 2 UNION SELECT 3
                        UNION SELECT 4 UNION SELECT 5 UNION SELECT 6
                    ) days
                    LEFT JOIN trainer_schedules s
                      ON s.trainer_id = ?
                     AND s.slot_id = ts.slot_id
                     AND s.day_of_week = CASE days.day_offset
                        WHEN 0 THEN 'MONDAY'
                        WHEN 1 THEN 'TUESDAY'
                        WHEN 2 THEN 'WEDNESDAY'
                        WHEN 3 THEN 'THURSDAY'
                        WHEN 4 THEN 'FRIDAY'
                        WHEN 5 THEN 'SATURDAY'
                        WHEN 6 THEN 'SUNDAY'
                     END
                    LEFT JOIN pt_bookings b
                      ON b.trainer_id = ?
                     AND b.slot_id = ts.slot_id
                     AND b.booking_date = DATE_ADD(?, INTERVAL days.day_offset DAY)
                     AND b.booking_status IN ('CONFIRMED', 'PENDING')
                    LEFT JOIN trainer_exceptions e
                      ON e.trainer_id = ?
                     AND e.exception_date = DATE_ADD(?, INTERVAL days.day_offset DAY)
                     AND (e.slot_id = ts.slot_id OR e.slot_id IS NULL)
                    WHERE ts.is_active = 1
                    GROUP BY ts.slot_id, days.day_offset, e.exception_id, e.exception_type, s.schedule_id, s.is_available
                    ORDER BY ts.slot_id, days.day_offset
                """;
        var q = em.createNativeQuery(sql);
        q.setParameter(1, weekStart);
        q.setParameter(2, trainerId);
        q.setParameter(3, trainerId);
        q.setParameter(4, weekStart);
        q.setParameter(5, trainerId);
        q.setParameter(6, weekStart);
        @SuppressWarnings("unchecked")
        List<Object[]> results = q.getResultList();
        return results;
    }

    // Lấy danh sách time_slots (để làm hàng đầu dòng)
    public List<Object[]> getTimeSlotsBasic() {
        // Trả về [slot_id, start_time, end_time] (giả định entity TimeSlot đã map 3 cột
        // này)
        String jpql = "SELECT t.slotId, t.startTime, t.endTime FROM TimeSlot t ORDER BY t.slotId";
        return em.createQuery(jpql, Object[].class).getResultList();
    }

    // CRUD operations for TrainerSchedule
    public void saveSchedule(TrainerSchedule schedule) {
        try {
            beginTransaction();
            em.persist(schedule);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("Error saving schedule: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save schedule", e);
        }
    }

    public TrainerSchedule getScheduleById(int scheduleId) {
        try {
            return em.find(TrainerSchedule.class, scheduleId);
        } catch (Exception e) {
            System.err.println("Error getting schedule by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void updateSchedule(TrainerSchedule schedule) {
        try {
            beginTransaction();
            em.merge(schedule);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("Error updating schedule: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update schedule", e);
        }
    }

    public void deleteSchedule(TrainerSchedule schedule) {
        try {
            beginTransaction();
            TrainerSchedule s = em.find(TrainerSchedule.class, schedule.getScheduleId());
            if (s != null) {
                em.remove(s);
            }
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("Error deleting schedule: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete schedule", e);
        }
    }

    // Get available schedules by trainer (alias for getWeeklySchedule)
    public List<TrainerSchedule> getAvailableSchedulesByTrainer(int trainerId) {
        return getWeeklySchedule(trainerId);
    }

    // Get all trainer schedules with full details for admin view
    public List<TrainerSchedule> findAll() {
        try {
            TypedQuery<TrainerSchedule> q = em.createQuery(
                    "SELECT s FROM TrainerSchedule s " +
                            "LEFT JOIN FETCH s.trainer " +
                            "LEFT JOIN FETCH s.timeSlot " +
                            "ORDER BY s.trainerId, s.dayOfWeek, s.slotId",
                    TrainerSchedule.class);
            return q.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting all trainer schedules: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
