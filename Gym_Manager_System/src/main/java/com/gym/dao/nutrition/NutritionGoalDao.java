package com.gym.dao.nutrition;

import com.gym.dao.BaseDAO;
import com.gym.model.NutritionGoal;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

/**
 * DAO for Nutrition Goal entity
 * Handles database operations for user_nutrition_goals table using JPA
 */
public class NutritionGoalDao extends BaseDAO<NutritionGoal> {
    
    public NutritionGoalDao() {
        super();
    }

    /**
     * Get nutrition goal for a user
     * @param userId user ID
     * @return Optional containing NutritionGoal if found, empty otherwise
     */
    public Optional<NutritionGoal> findByUserId(long userId) {
        try {
            TypedQuery<NutritionGoal> query = em.createQuery(
                "SELECT g FROM NutritionGoal g WHERE g.userId = :userId",
                NutritionGoal.class
            );
            query.setParameter("userId", userId);
            NutritionGoal goal = query.getSingleResult();
            return Optional.of(goal);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("[NutritionGoalDao] Error finding nutrition goal: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Save or update nutrition goal (UPSERT)
     * Uses JPA merge() which handles both insert and update
     * @param goal NutritionGoal to save
     * @return true if successful, false otherwise
     */
    public boolean saveOrUpdate(NutritionGoal goal) {
        try {
            beginTransaction();
            
            // Check if goal already exists using direct query (avoid calling findByUserId which may have transaction issues)
            TypedQuery<NutritionGoal> query = em.createQuery(
                "SELECT g FROM NutritionGoal g WHERE g.userId = :userId",
                NutritionGoal.class
            );
            query.setParameter("userId", goal.getUserId());
            
            NutritionGoal existing = null;
            try {
                existing = query.getSingleResult();
            } catch (NoResultException e) {
                // Goal doesn't exist yet, will create new
                existing = null;
            }
            
            if (existing != null) {
                // Update existing goal
                existing.setGoalType(goal.getGoalType());
                existing.setActivityFactor(goal.getActivityFactor());
                existing.setDailyCaloriesTarget(goal.getDailyCaloriesTarget());
                existing.setDailyProteinTarget(goal.getDailyProteinTarget());
                // updatedAt will be set automatically by @PreUpdate
                em.merge(existing);
                System.out.println("[NutritionGoalDao] Updated existing nutrition goal for userId: " + goal.getUserId());
            } else {
                // Insert new goal
                // updatedAt will be set automatically by @PrePersist
                em.persist(goal);
                System.out.println("[NutritionGoalDao] Created new nutrition goal for userId: " + goal.getUserId());
            }
            
            commitTransaction();
            System.out.println("[NutritionGoalDao] Successfully saved nutrition goal for userId: " + goal.getUserId());
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[NutritionGoalDao] Error saving nutrition goal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}



