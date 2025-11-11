package dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;
import model.shop.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.report.ChartData;
import model.report.TopSpender;

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
    try {
        // Parse startDate từ string (format: YYYY-MM)
        java.time.LocalDate startDateLocal = java.time.LocalDate.parse(startDateStr + "-01");
        
        // Tháng hiện tại trong GMT+7
        java.time.ZoneId vietnamZone = java.time.ZoneId.of("Asia/Ho_Chi_Minh");
        java.time.LocalDate nowVietnam = java.time.LocalDate.now(vietnamZone);
        java.time.LocalDate endDateLocal = nowVietnam.plusMonths(1).withDayOfMonth(1);
        
        // Convert sang Date cho query
        java.util.Date startDate = java.util.Date.from(startDateLocal.atStartOfDay(vietnamZone).toInstant());
        java.util.Date endDate = java.util.Date.from(endDateLocal.atStartOfDay(vietnamZone).toInstant());
        
        // Query: sử dụng COALESCE để lấy paid_at nếu có, nếu không thì dùng payment_date
        // Chuyển đổi sang LocalDateTime nếu cần
        String sql = "SELECT DATE_FORMAT(COALESCE(p.paid_at, p.payment_date), '%Y-%m') as month_year, " +
                     "COALESCE(SUM(p.amount), 0) as total_amount " +
                     "FROM payments p " +
                     "WHERE p.status = 'PAID' " +
                     "AND (p.paid_at IS NOT NULL OR p.payment_date IS NOT NULL) " +
                     "AND COALESCE(p.paid_at, p.payment_date) >= ?1 " +
                     "AND COALESCE(p.paid_at, p.payment_date) < ?2 " +
                     "GROUP BY month_year " +
                     "ORDER BY month_year ASC";

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, startDate, TemporalType.TIMESTAMP);
        query.setParameter(2, endDate, TemporalType.TIMESTAMP);

        List<Object[]> results = query.getResultList();
        
        System.out.println("[AdminReportsDAO] Query returned " + results.size() + " rows");
        
        // Tạo map để dễ dàng tìm kiếm
        java.util.Map<String, BigDecimal> revenueMap = new java.util.HashMap<>();
        for (Object[] row : results) {
            if (row[0] != null && row[1] != null) {
                String monthYear = row[0].toString();
                BigDecimal amount = (BigDecimal) row[1];
                System.out.println("[AdminReportsDAO] Month: " + monthYear + ", Amount: " + amount);
                revenueMap.put(monthYear, amount != null ? amount : BigDecimal.ZERO);
            }
        }
        
        // Tạo danh sách đầy đủ 12 tháng (từ startDate đến tháng hiện tại)
        java.time.LocalDate month = startDateLocal;
        java.time.LocalDate currentMonth = nowVietnam.withDayOfMonth(1);
        List<ChartData> chartData = new ArrayList<>();
        
        while (!month.isAfter(currentMonth) && chartData.size() < 12) {
            String monthKey = month.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
            BigDecimal amount = revenueMap.getOrDefault(monthKey, BigDecimal.ZERO);
            chartData.add(new ChartData(monthKey, amount));
            System.out.println("[AdminReportsDAO] Added chart data: " + monthKey + " = " + amount);
            month = month.plusMonths(1);
        }
        
        System.out.println("[AdminReportsDAO] Total chart data items: " + chartData.size());
        return chartData;
    } catch (Exception e) {
        System.err.println("[AdminReportsDAO] Error in getMonthlyRevenueNative: " + e.getMessage());
        e.printStackTrace();
        // Fallback: thử query đơn giản hơn
        try {
            return getMonthlyRevenueNativeFallback(startDateStr);
        } catch (Exception e2) {
            System.err.println("[AdminReportsDAO] Error in fallback: " + e2.getMessage());
            e2.printStackTrace();
            return new ArrayList<>();
        }
    }
}

/**
 * Fallback method nếu query chính không hoạt động
 */
@SuppressWarnings("unchecked")
private List<ChartData> getMonthlyRevenueNativeFallback(String startDateStr) {
    try {
        // Query đơn giản hơn, chỉ sử dụng payment_date
        String sql = "SELECT DATE_FORMAT(p.payment_date, '%Y-%m') as month_year, " +
                     "COALESCE(SUM(p.amount), 0) as total_amount " +
                     "FROM payments p " +
                     "WHERE p.status = 'PAID' " +
                     "AND p.payment_date >= ?1 " +
                     "GROUP BY month_year " +
                     "ORDER BY month_year ASC " +
                     "LIMIT 12";

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, startDateStr + "-01");

        List<Object[]> results = query.getResultList();
        List<ChartData> chartData = new ArrayList<>();
        
        for (Object[] row : results) {
            if (row[0] != null && row[1] != null) {
                String monthYear = row[0].toString();
                BigDecimal amount = (BigDecimal) row[1];
                chartData.add(new ChartData(monthYear, amount != null ? amount : BigDecimal.ZERO));
            }
        }
        
        return chartData;
    } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
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

    /**
     * Get active memberships count by day (GMT+7)
     * Returns list of ChartData with date label and count of active memberships
     */
    @SuppressWarnings("unchecked")
    public List<ChartData> getActiveMembershipsByDay(int days) {
        try {
            // Use native SQL with CONVERT_TZ for GMT+7 timezone
            String sql = "SELECT DATE(CONVERT_TZ(m.created_date, '+00:00', '+07:00')) as day_date, COUNT(*) as membership_count " +
                        "FROM memberships m " +
                        "WHERE m.status = 'active' " +
                        "AND CONVERT_TZ(m.created_date, '+00:00', '+07:00') >= DATE_SUB(CONVERT_TZ(NOW(), '+00:00', '+07:00'), INTERVAL ?1 DAY) " +
                        "GROUP BY day_date " +
                        "ORDER BY day_date ASC";
            
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, days);
            
            List<Object[]> results = query.getResultList();
            List<ChartData> chartData = new ArrayList<>();
            for (Object[] row : results) {
                String dateStr = row[0].toString();
                Long count = ((Number) row[1]).longValue();
                chartData.add(new ChartData(dateStr, BigDecimal.valueOf(count)));
            }
            return chartData;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Get active memberships count by month (GMT+7)
     * Returns list of ChartData with month label and count of active memberships
     */
    @SuppressWarnings("unchecked")
    public List<ChartData> getActiveMembershipsByMonth(int months) {
        try {
            // Use native SQL with CONVERT_TZ for GMT+7 timezone
            String sql = "SELECT DATE_FORMAT(CONVERT_TZ(m.created_date, '+00:00', '+07:00'), '%Y-%m') as month_year, COUNT(*) as membership_count " +
                        "FROM memberships m " +
                        "WHERE m.status = 'active' " +
                        "AND CONVERT_TZ(m.created_date, '+00:00', '+07:00') >= DATE_SUB(CONVERT_TZ(NOW(), '+00:00', '+07:00'), INTERVAL ?1 MONTH) " +
                        "GROUP BY month_year " +
                        "ORDER BY month_year ASC";
            
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, months);
            
            List<Object[]> results = query.getResultList();
            List<ChartData> chartData = new ArrayList<>();
            for (Object[] row : results) {
                String monthStr = row[0].toString();
                Long count = ((Number) row[1]).longValue();
                chartData.add(new ChartData(monthStr, BigDecimal.valueOf(count)));
            }
            return chartData;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Get top 5 spenders in current month (GMT+7)
     * Returns list of TopSpender with member info and total spending
     */
    @SuppressWarnings("unchecked")
    public List<TopSpender> getTopSpendersThisMonth(int limit) {
        try {
            // Get current month start and end in GMT+7
            ZonedDateTime nowVietnam = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            ZonedDateTime startOfMonth = nowVietnam.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            ZonedDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
            
            // Convert to UTC for database comparison
            ZonedDateTime startUTC = startOfMonth.withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endUTC = endOfMonth.withZoneSameInstant(ZoneId.of("UTC"));
            
            // Convert to Date for JPQL
            Date startDate = Date.from(startUTC.toInstant());
            Date endDate = Date.from(endUTC.toInstant());
            
            // Use native SQL to get top spenders with user info
            String sql = "SELECT p.member_id, u.username, u.name, SUM(p.amount) as total_spent " +
                         "FROM payments p " +
                         "INNER JOIN user u ON u.user_id = p.member_id " +
                         "WHERE p.status = 'PAID' " +
                         "AND p.paid_at >= ?1 " +
                         "AND p.paid_at <= ?2 " +
                         "GROUP BY p.member_id, u.username, u.name " +
                         "ORDER BY total_spent DESC " +
                         "LIMIT ?3";
            
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, startDate, TemporalType.TIMESTAMP);
            query.setParameter(2, endDate, TemporalType.TIMESTAMP);
            query.setParameter(3, limit);
            
            List<Object[]> results = query.getResultList();
            List<TopSpender> topSpenders = new ArrayList<>();
            for (Object[] row : results) {
                Integer memberId = ((Number) row[0]).intValue();
                String username = (String) row[1];
                String name = (String) row[2];
                BigDecimal totalSpent = (BigDecimal) row[3];
                topSpenders.add(new TopSpender(memberId, username, name, totalSpent));
            }
            return topSpenders;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}