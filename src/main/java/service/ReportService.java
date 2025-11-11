package service;

import dao.AdminReportsDAO;
import model.report.ChartData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import model.report.ReportSummary;

public class ReportService {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("gymPU");

    public ReportSummary getSummaryStats() {
        EntityManager em = emf.createEntityManager();
        ReportSummary summary = new ReportSummary();
        try {
            AdminReportsDAO dao = new AdminReportsDAO(em);
            LocalDateTime now = LocalDateTime.now();
            LocalDate todayDate = LocalDate.now();

            // 1. Xác định mốc thời gian (Tháng này & Tháng trước)
            LocalDateTime startOfThisMonth = now.withDayOfMonth(1).with(LocalTime.MIN);
            LocalDateTime startOfLastMonth = startOfThisMonth.minusMonths(1);
            LocalDateTime endOfLastMonth = startOfThisMonth.minusSeconds(1);

            // 2. Member Stats (Dùng java.util.Date vì model User dùng loại này)
            summary.setTotalMembers(dao.countTotalMembers());

            long newMembersThisMonth = dao.countNewMembers(toDate(startOfThisMonth.toLocalDate()), toDate(todayDate));
            long newMembersLastMonth = dao.countNewMembers(toDate(startOfLastMonth.toLocalDate()), toDate(endOfLastMonth.toLocalDate()));

            summary.setNewMembersThisMonth(newMembersThisMonth);
            summary.setMemberGrowthRate(calculateGrowth(newMembersThisMonth, newMembersLastMonth));

            // 3. Revenue Stats (Dùng LocalDateTime vì model Payment dùng loại này)
            BigDecimal revenueThisMonth = dao.calculateRevenue(startOfThisMonth, now);
            BigDecimal revenueLastMonth = dao.calculateRevenue(startOfLastMonth, endOfLastMonth);

            summary.setRevenueThisMonth(revenueThisMonth);
            summary.setRevenueLastMonth(revenueLastMonth);
            summary.setRevenueGrowthRate(calculateGrowth(revenueThisMonth.doubleValue(), revenueLastMonth.doubleValue()));

            // 4. Dummy Stats (Check-in) - Placeholder
            summary.setCheckInsToday(892);
            summary.setAvgCheckIns(745.0);

        } finally {
            em.close();
        }
        return summary;
    }

    public List<ChartData> getRevenueChartData() {
        EntityManager em = emf.createEntityManager();
        try {
            AdminReportsDAO dao = new AdminReportsDAO(em);
            // Lấy dữ liệu từ 12 tháng trước đến nay (tổng 12 tháng)
            // Sử dụng timezone GMT+7
            java.time.ZoneId vietnamZone = java.time.ZoneId.of("Asia/Ho_Chi_Minh");
            java.time.LocalDate nowVietnam = java.time.LocalDate.now(vietnamZone);
            java.time.LocalDate oneYearAgo = nowVietnam.minusMonths(11).withDayOfMonth(1); // 11 tháng trước + tháng hiện tại = 12 tháng
            return dao.getMonthlyRevenueNative(oneYearAgo.toString());
        } finally {
            em.close();
        }
    }

    /**
     * Get revenue chart data by day for current month
     */
    public List<ChartData> getRevenueChartDataByDay() {
        EntityManager em = emf.createEntityManager();
        try {
            AdminReportsDAO dao = new AdminReportsDAO(em);
            return dao.getRevenueByDayCurrentMonth();
        } finally {
            em.close();
        }
    }

    /**
     * Get revenue chart data by month (last 12 months)
     */
    public List<ChartData> getRevenueChartDataByMonth() {
        return getRevenueChartData();
    }

    /**
     * Get revenue chart data by year (last 5 years)
     */
    public List<ChartData> getRevenueChartDataByYear() {
        EntityManager em = emf.createEntityManager();
        try {
            AdminReportsDAO dao = new AdminReportsDAO(em);
            return dao.getRevenueByYear(5);
        } finally {
            em.close();
        }
    }

    public List<ChartData> getPackageRevenueChartData() {
        EntityManager em = emf.createEntityManager();
        try {
            return new AdminReportsDAO(em).getRevenueByPackage();
        } finally {
            em.close();
        }
    }

    /**
     * Get member growth chart data for the last 6 months
     */
    public List<ChartData> getMemberGrowthChartData() {
        EntityManager em = emf.createEntityManager();
        try {
            AdminReportsDAO dao = new AdminReportsDAO(em);
            return dao.getMonthlyMemberGrowth(6);
        } finally {
            em.close();
        }
    }

    /**
     * Get active memberships chart data by day (last 30 days)
     */
    public List<ChartData> getActiveMembershipsByDay() {
        EntityManager em = emf.createEntityManager();
        try {
            AdminReportsDAO dao = new AdminReportsDAO(em);
            return dao.getActiveMembershipsByDay(30);
        } finally {
            em.close();
        }
    }

    /**
     * Get active memberships chart data by day for current month
     */
    public List<ChartData> getActiveMembershipsByDayCurrentMonth() {
        EntityManager em = emf.createEntityManager();
        try {
            AdminReportsDAO dao = new AdminReportsDAO(em);
            return dao.getActiveMembershipsByDayCurrentMonth();
        } finally {
            em.close();
        }
    }

    /**
     * Get active memberships chart data by month (last 12 months)
     */
    public List<ChartData> getActiveMembershipsByMonth() {
        EntityManager em = emf.createEntityManager();
        try {
            AdminReportsDAO dao = new AdminReportsDAO(em);
            return dao.getActiveMembershipsByMonth(12);
        } finally {
            em.close();
        }
    }

    /**
     * Get active memberships chart data by year (last 5 years)
     */
    public List<ChartData> getActiveMembershipsByYear() {
        EntityManager em = emf.createEntityManager();
        try {
            AdminReportsDAO dao = new AdminReportsDAO(em);
            return dao.getActiveMembershipsByYear(5);
        } finally {
            em.close();
        }
    }

    /**
     * Get top 5 spenders in current month
     */
    public List<model.report.TopSpender> getTopSpendersThisMonth() {
        EntityManager em = emf.createEntityManager();
        try {
            AdminReportsDAO dao = new AdminReportsDAO(em);
            return dao.getTopSpendersThisMonth(5);
        } finally {
            em.close();
        }
    }

    // --- Helpers ---
    private double calculateGrowth(double current, double previous) {
        if (previous == 0) return current > 0 ? 100.0 : 0.0;
        double growth = ((current - previous) / previous) * 100;
        return BigDecimal.valueOf(growth).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}