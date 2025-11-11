package dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;
import model.shop.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.report.ChartData;

public class AdminReportsDAO {

    private final EntityManager em;

    public AdminReportsDAO(EntityManager em) {
        this.em = em;
    }

    // --- MEMBER STATS ---
    public long countTotalMembers() {
        try {
            // Đếm tất cả user có role là Member (dựa trên model Member extends User)
            String jpql = "SELECT COUNT(m) FROM Member m";
            return (Long) em.createQuery(jpql).getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public long countNewMembers(Date startDate, Date endDate) {
        try {
            // createdDate trong User là java.util.Date
            String jpql = "SELECT COUNT(m) FROM Member m WHERE m.createdDate BETWEEN :startDate AND :endDate";
            Query query = em.createQuery(jpql);
            query.setParameter("startDate", startDate, TemporalType.DATE);
            query.setParameter("endDate", endDate, TemporalType.DATE);
            return (Long) query.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    // --- REVENUE STATS ---
    public BigDecimal calculateRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            // Payment dùng LocalDateTime và Enum PaymentStatus
            String jpql = "SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status AND p.paymentDate BETWEEN :startDate AND :endDate";
            Query query = em.createQuery(jpql);
            query.setParameter("status", PaymentStatus.PAID);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            BigDecimal result = (BigDecimal) query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    // Lấy doanh thu theo tên gói tập (cho biểu đồ tròn)
    @SuppressWarnings("unchecked")
    public List<ChartData> getRevenueByPackage() {
        // Join thủ công vì Payment chỉ lưu membershipId dưới dạng Integer
        String jpql = "SELECT pk.name, SUM(p.amount) " +
                      "FROM Payment p, Membership m, Package pk " +
                      "WHERE p.membershipId = m.id AND m.packageO.id = pk.id " +
                      "AND p.status = :status " +
                      "GROUP BY pk.name";

        Query query = em.createQuery(jpql);
        query.setParameter("status", PaymentStatus.PAID);

        List<Object[]> results = query.getResultList();
        List<ChartData> chartData = new ArrayList<>();
        for (Object[] row : results) {
            chartData.add(new ChartData((String) row[0], (BigDecimal) row[1]));
        }
        return chartData;
    }

@SuppressWarnings("unchecked")
public List<ChartData> getMonthlyRevenueNative(String startDateStr) {
    // Sửa :startDate thành ?1 để dùng tham số vị trí
    String sql = "SELECT DATE_FORMAT(payment_date, '%Y-%m') as month_year, SUM(amount) " +
                 "FROM payments " +
                 "WHERE status = 'PAID' AND payment_date >= ?1 " +
                 "GROUP BY month_year " +
                 "ORDER BY month_year ASC";

    Query query = em.createNativeQuery(sql);
    // Sửa setParameter("startDate", ...) thành setParameter(1, ...)
    query.setParameter(1, startDateStr);

    List<Object[]> results = query.getResultList();
    List<ChartData> chartData = new ArrayList<>();
    for (Object[] row : results) {
        // Đảm bảo kiểu dữ liệu trả về phù hợp (String và BigDecimal)
        chartData.add(new ChartData(row[0].toString(), (BigDecimal) row[1]));
    }
    return chartData;
}

    /**
     * Get monthly member growth data for the last 6 months
     * Returns list of ChartData with month label and count of new members
     */
    @SuppressWarnings("unchecked")
    public List<ChartData> getMonthlyMemberGrowth(int months) {
        try {
            // Use native SQL to group by month
            String sql = "SELECT DATE_FORMAT(created_date, '%Y-%m') as month_year, COUNT(*) as member_count " +
                        "FROM user " +
                        "WHERE DTYPE = 'Member' " +
                        "AND created_date >= DATE_SUB(CURDATE(), INTERVAL ?1 MONTH) " +
                        "GROUP BY month_year " +
                        "ORDER BY month_year ASC";
            
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, months);
            
            List<Object[]> results = query.getResultList();
            List<ChartData> chartData = new ArrayList<>();
            for (Object[] row : results) {
                // Convert count to BigDecimal for consistency with ChartData
                Long count = ((Number) row[1]).longValue();
                chartData.add(new ChartData(row[0].toString(), BigDecimal.valueOf(count)));
            }
            return chartData;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}