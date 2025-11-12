package dao;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

/**
 * DAO for trainer's students views
 * (tự quản lý EntityManager để giảm phụ thuộc)
 */
public class TrainerStudentDAO {

    private static final Logger LOGGER = Logger.getLogger(TrainerStudentDAO.class.getName());
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("gymPU");

    @SuppressWarnings("unchecked")
    public List<Object[]> getStudentsByTrainer(Integer trainerId) {
        try {
            String sql = """
                SELECT 
                    m.member_id,
                    u.name,
                    u.phone,
                    u.email,
                    u.gender,
                    u.dob,
                    m.weight,
                    m.height,
                    m.bmi,
                    m.goal,
                    m.pt_note,
                    COUNT(DISTINCT b.booking_id),
                    SUM(CASE WHEN b.booking_status = 'COMPLETED' THEN 1 ELSE 0 END),
                    SUM(CASE WHEN b.booking_status = 'CONFIRMED' THEN 1 ELSE 0 END),
                    SUM(CASE WHEN b.booking_status = 'PENDING' THEN 1 ELSE 0 END)
                FROM pt_bookings b
                JOIN members m ON b.member_id = m.member_id
                JOIN user u ON m.member_id = u.user_id
                WHERE b.trainer_id = ?
                  AND b.booking_status IN ('CONFIRMED','COMPLETED')
                GROUP BY m.member_id, u.name, u.phone, u.email, u.gender, u.dob,
                         m.weight, m.height, m.bmi, m.goal, m.pt_note
                ORDER BY u.name
            """;
            EntityManager em = EMF.createEntityManager();
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, trainerId);
            List<Object[]> rs = q.getResultList();
            em.close();
            return rs;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getStudentsByTrainer error", e);
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getStudentsByTrainerWithFilter(Integer trainerId, String keyword, String packageName) {
        try {
            StringBuilder sql = new StringBuilder("""
                SELECT 
                    m.member_id,
                    u.name,
                    u.phone,
                    u.email,
                    u.gender,
                    u.dob,
                    m.weight,
                    m.height,
                    m.bmi,
                    m.goal,
                    m.pt_note,
                    pkg.name AS package_name,
                    COUNT(DISTINCT b.booking_id),
                    SUM(CASE WHEN b.booking_status = 'COMPLETED' THEN 1 ELSE 0 END),
                    SUM(CASE WHEN b.booking_status = 'CONFIRMED' THEN 1 ELSE 0 END),
                    SUM(CASE WHEN b.booking_status = 'PENDING' THEN 1 ELSE 0 END)
                FROM pt_bookings b
                JOIN members m ON b.member_id = m.member_id
                JOIN user u ON m.member_id = u.user_id
                LEFT JOIN memberships mem ON mem.member_id = m.member_id AND mem.status = 'active'
                LEFT JOIN packages pkg ON mem.package_id = pkg.package_id
                WHERE b.trainer_id = ?
                  AND b.booking_status IN ('CONFIRMED','COMPLETED')
            """);

            int idx = 1;
            Query q;
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND (LOWER(u.name) LIKE LOWER(?) OR LOWER(u.phone) LIKE LOWER(?) OR LOWER(u.email) LIKE LOWER(?))");
            }
            if (packageName != null && !packageName.trim().isEmpty()) {
                sql.append(" AND pkg.name = ?");
            }

            sql.append("""
                GROUP BY m.member_id, u.name, u.phone, u.email, u.gender, u.dob,
                         m.weight, m.height, m.bmi, m.goal, m.pt_note, pkg.name
                ORDER BY u.name
            """);

            EntityManager em = EMF.createEntityManager();
            q = em.createNativeQuery(sql.toString());
            q.setParameter(idx++, trainerId);
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = "%" + keyword + "%";
                q.setParameter(idx++, kw);
                q.setParameter(idx++, kw);
                q.setParameter(idx++, kw);
            }
            if (packageName != null && !packageName.trim().isEmpty()) {
                q.setParameter(idx++, packageName);
            }

            List<Object[]> rs = q.getResultList();
            em.close();
            return rs;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getStudentsByTrainerWithFilter error", e);
            return Collections.emptyList();
        }
    }

    public Object[] getStudentStatistics(Integer trainerId) {
        try {
            String sql = """
                SELECT 
                  COUNT(DISTINCT m.member_id) AS total_students,
                  COUNT(DISTINCT CASE WHEN b.booking_status IN ('CONFIRMED','COMPLETED') THEN m.member_id END) AS active_students,
                  COUNT(DISTINCT CASE WHEN m.goal IS NOT NULL AND m.goal <> '' THEN m.member_id END) AS achieved_goal_count
                FROM pt_bookings b
                JOIN members m ON b.member_id = m.member_id
                WHERE b.trainer_id = ?
                  AND b.booking_status IN ('CONFIRMED','COMPLETED')
            """;
            EntityManager em = EMF.createEntityManager();
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, trainerId);
            Object result = q.getSingleResult();
            em.close();
            return (result instanceof Object[]) ? (Object[]) result : new Object[]{result, 0L, 0L};
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getStudentStatistics error", e);
            return new Object[]{0L,0L,0L};
        }
    }

    @SuppressWarnings("unchecked")
    public Object[] getStudentDetail(Integer memberId, Integer trainerId) {
        try {
            String sql = """
                SELECT 
                    m.member_id, u.name, u.phone, u.email, u.gender, u.dob, u.address,
                    m.weight, m.height, m.bmi, m.goal, m.pt_note,
                    pkg.name AS package_name, pkg.duration_months,
                    COUNT(DISTINCT b.booking_id) AS total_bookings,
                    SUM(CASE WHEN b.booking_status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_sessions,
                    SUM(CASE WHEN b.booking_status = 'CONFIRMED' THEN 1 ELSE 0 END) AS confirmed_sessions
                FROM pt_bookings b
                JOIN members m ON b.member_id = m.member_id
                JOIN user u ON m.member_id = u.user_id
                LEFT JOIN memberships mem ON mem.member_id = m.member_id AND mem.status = 'active'
                LEFT JOIN packages pkg ON mem.package_id = pkg.package_id
                WHERE b.trainer_id = ?
                  AND m.member_id = ?
                  AND b.booking_status IN ('CONFIRMED','COMPLETED')
                GROUP BY m.member_id, u.name, u.phone, u.email, u.gender, u.dob, u.address,
                         m.weight, m.height, m.bmi, m.goal, m.pt_note, pkg.name, pkg.duration_months
            """;
            EntityManager em = EMF.createEntityManager();
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, trainerId);
            q.setParameter(2, memberId);
            List<Object[]> rs = q.getResultList();
            em.close();
            return (rs != null && !rs.isEmpty()) ? rs.get(0) : null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getStudentDetail error", e);
            return null;
        }
    }
}

