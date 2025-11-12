package dao;

import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

/**
 * Data-access helper for PT dashboard quick statistics.
 */
public class TrainerDashboardDAO {

    private static final Logger LOGGER = Logger.getLogger(TrainerDashboardDAO.class.getName());
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("gymPU");

    /**
     * Returns quick stats for the trainer:
     * [0] total students being handled (confirmed/completed bookings)
     * [1] total completed sessions
     * [2] sessions scheduled for today (confirmed/completed)
     */
    public long[] getQuickStats(int trainerId) {
        EntityManager em = null;
        try {
            em = EMF.createEntityManager();
            String sql = """
                SELECT
                    COUNT(DISTINCT CASE WHEN b.booking_status IN ('CONFIRMED','COMPLETED') THEN b.member_id END) AS total_students,
                    COALESCE(SUM(CASE WHEN b.booking_status = 'COMPLETED' THEN 1 ELSE 0 END), 0) AS completed_sessions,
                    COALESCE(SUM(CASE WHEN b.booking_status IN ('CONFIRMED','COMPLETED') AND b.booking_date = CURRENT_DATE THEN 1 ELSE 0 END), 0) AS today_sessions
                FROM pt_bookings b
                WHERE b.trainer_id = ?
            """;
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, trainerId);
            Object raw = query.getSingleResult();
            Object[] arr = raw instanceof Object[] ? (Object[]) raw : new Object[]{raw};
            long total = toLong(arr, 0);
            long completed = toLong(arr, 1);
            long today = toLong(arr, 2);
            return new long[]{total, completed, today};
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[TrainerDashboardDAO] Failed to get quick stats for trainer " + trainerId, e);
            return new long[]{0L, 0L, 0L};
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private long toLong(Object[] arr, int idx) {
        if (arr == null || arr.length <= idx || arr[idx] == null) {
            return 0L;
        }
        Object val = arr[idx];
        if (val instanceof Number num) {
            return num.longValue();
        }
        try {
            return Long.parseLong(val.toString());
        } catch (NumberFormatException ex) {
            return 0L;
        }
    }
}

