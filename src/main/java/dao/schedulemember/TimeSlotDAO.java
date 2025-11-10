package dao.schedulemember;

import dao.GenericDAO;
import jakarta.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.schedule.TimeSlot;

public class TimeSlotDAO extends GenericDAO<TimeSlot> {

    private static final Logger LOGGER = Logger.getLogger(TimeSlotDAO.class.getName());

    public TimeSlotDAO() {
        super(TimeSlot.class);
    }

    // Hàm quan trọng nhất: Tìm slot trống
    // Sử dụng Native Query vì logic join 3-4 bảng và check NOT EXISTS khá phức tạp nếu dùng JPQL thuần.
    @SuppressWarnings("unchecked")
    public List<TimeSlot> findAvailableSlots(int trainerId, int gymId, LocalDate date) {
        try {
            String dayOfWeek = date.getDayOfWeek().toString(); // MONDAY, TUESDAY...

            // Sử dụng 'em' được kế thừa từ BaseDAO/GenericDAO
            // Không cần tự đóng 'em' vì nó được quản lý bởi lớp cha
            String sql = "SELECT ts.* FROM time_slots ts " +
                    "JOIN trainer_schedules sch ON ts.slot_id = sch.slot_id " +
                    "WHERE sch.trainer_id = ?1 AND sch.gym_id = ?2 AND sch.day_of_week = ?3 AND sch.is_available = 1 " +
                    // Loại bỏ ngày Trainer xin nghỉ (Exceptions)
                    "AND NOT EXISTS (SELECT 1 FROM trainer_exceptions ex WHERE ex.trainer_id = ?1 AND ex.exception_date = ?4 AND (ex.slot_id = ts.slot_id OR ex.slot_id IS NULL)) " +
                    // Loại bỏ slot đã có người book (Pending hoặc Confirmed)
                    "AND NOT EXISTS (SELECT 1 FROM pt_bookings bk WHERE bk.trainer_id = ?1 AND bk.booking_date = ?4 AND bk.slot_id = ts.slot_id AND bk.booking_status IN ('PENDING', 'CONFIRMED')) " +
                    "ORDER BY ts.start_time";

            Query query = em.createNativeQuery(sql, TimeSlot.class);
            query.setParameter(1, trainerId);
            query.setParameter(2, gymId);
            query.setParameter(3, dayOfWeek);
            query.setParameter(4, Date.valueOf(date));

            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding available slots for trainer " + trainerId + " on " + date, e);
            return Collections.emptyList();
        }
    }
}