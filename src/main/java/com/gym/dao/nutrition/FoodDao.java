package com.gym.dao.nutrition;

import com.gym.dao.GenericDAO;
import com.gym.model.Food;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO for Food entity using JPA
 * Extends GenericDAO for basic CRUD operations
 * Handles database operations for foods table
 */
public class FoodDao extends GenericDAO<Food> {

    public FoodDao() {
        super(Food.class);
    }

    /**
     * Search active foods by name (case-insensitive LIKE search)
     * @param keyword search keyword
     * @param limit maximum number of results
     * @return list of active foods matching the keyword
     */
    public List<Food> searchActiveByName(String keyword, int limit) {
        try {
            TypedQuery<Food> query = em.createQuery(
                "SELECT f FROM Food f WHERE f.isActive = true AND LOWER(f.name) LIKE LOWER(:keyword) ORDER BY f.name",
                Food.class
            );
            query.setParameter("keyword", "%" + keyword + "%");
            query.setMaxResults(limit);
            
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[FoodDao] Error searching foods: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Find food by ID
     * Uses GenericDAO.findByIdOptional(Long)
     * @param id food ID
     * @return Optional containing Food if found, empty otherwise
     */
    public Optional<Food> findById(long id) {
        return findByIdOptional(id);
    }

    /**
     * Get default foods to display (popular/recent foods)
     * @param limit maximum number of results
     * @return list of active foods
     */
    public List<Food> getDefaultFoods(int limit) {
        try {
            TypedQuery<Food> query = em.createQuery(
                "SELECT f FROM Food f WHERE f.isActive = true ORDER BY f.name",
                Food.class
            );
            query.setMaxResults(limit);
            
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[FoodDao] Error getting default foods: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
}
