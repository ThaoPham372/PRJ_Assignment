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
        long[] raw = dashboardDAO.getQuickStats(trainerId);
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalStudents", raw.length > 0 ? raw[0] : 0L);
        stats.put("completedSessions", raw.length > 1 ? raw[1] : 0L);
        stats.put("todaySessions", raw.length > 2 ? raw[2] : 0L);
        return stats;
    }
}

