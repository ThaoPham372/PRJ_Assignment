package service;

import java.util.HashMap;
import java.util.Map;

import dao.TrainerDashboardDAO;

/**
 * Service layer for PT dashboard insights.
 */
public class TrainerDashboardService {

    private final TrainerDashboardDAO dashboardDAO;

    public TrainerDashboardService() {
        this.dashboardDAO = new TrainerDashboardDAO();
    }

    /**
     * Returns a map with keys: totalStudents, completedSessions, todaySessions.
     */
    public Map<String, Long> getQuickStats(int trainerId) {
        Object[] raw = dashboardDAO.getQuickStats(trainerId);
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalStudents", toLong(raw, 0));
        stats.put("completedSessions", toLong(raw, 1));
        stats.put("todaySessions", toLong(raw, 2));
        return stats;
    }

    private long toLong(Object[] raw, int idx) {
        if (raw == null || raw.length <= idx || raw[idx] == null) {
            return 0L;
        }
        if (raw[idx] instanceof Number num) {
            return num.longValue();
        }
        try {
            return Long.parseLong(raw[idx].toString());
        } catch (NumberFormatException ex) {
            return 0L;
        }
    }
}

