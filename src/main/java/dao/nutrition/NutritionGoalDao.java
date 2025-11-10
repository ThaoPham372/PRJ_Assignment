package dao.nutrition;

import dao.GenericDAO;
import model.NutritionGoal;
import java.util.Optional;

/**
 * DAO for Nutrition Goal entity
 * Extends GenericDAO for basic CRUD operations
 * Follows DAO pattern and OOP principles
 * Handles database operations for user_nutrition_goals table using JPA
 */
public class NutritionGoalDao extends GenericDAO<NutritionGoal> {
    
    public NutritionGoalDao() {
        super(NutritionGoal.class);
    }

    /**
     * Get nutrition goal for a user
     * Reuses GenericDAO.findByField pattern
     * @param userId user ID
     * @return Optional containing NutritionGoal if found, empty otherwise
     */
    public Optional<NutritionGoal> findByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }
        try {
            NutritionGoal goal = findByField("userId", userId);
            return Optional.ofNullable(goal);
        } catch (Exception e) {
            System.err.println("[NutritionGoalDao] Error finding nutrition goal: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Save or update nutrition goal (UPSERT)
     * Reuses GenericDAO methods following DRY principle
     * @param goal NutritionGoal to save
     * @return true if successful, false otherwise
     */
    public boolean saveOrUpdate(NutritionGoal goal) {
        if (goal == null || goal.getUserId() == null) {
            return false;
        }
        
        try {
            // Check if goal already exists
            NutritionGoal existing = findByField("userId", goal.getUserId());
            
            if (existing != null) {
                // Update existing goal
                existing.setGoalType(goal.getGoalType());
                existing.setActivityFactor(goal.getActivityFactor());
                existing.setDailyCaloriesTarget(goal.getDailyCaloriesTarget());
                existing.setDailyProteinTarget(goal.getDailyProteinTarget());
                // updatedAt will be set automatically by @PreUpdate
                update(existing);
            } else {
                // Insert new goal
                // updatedAt will be set automatically by @PrePersist
                save(goal);
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("[NutritionGoalDao] Error saving nutrition goal: " + e.getMessage());
            return false;
        }
    }
}



