package dao.nutrition;

import dao.GenericDAO;
import model.Food;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * DAO for Food entity using JPA
 * Extends GenericDAO for basic CRUD operations
 * Follows DAO pattern and OOP principles
 * Handles database operations for foods table
 */
public class FoodDao extends GenericDAO<Food> {

    public FoodDao() {
        super(Food.class);
    }

    /**
     * Find food by ID (Long)
     * Reuses GenericDAO pattern with type conversion
     * @param id food ID
     * @return Optional containing Food if found, empty otherwise
     */
    public Optional<Food> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Food food = em.find(Food.class, id);
        return Optional.ofNullable(food);
    }

    /**
     * Search active foods by name (case-insensitive LIKE search)
     * Custom query method following Single Responsibility Principle
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
            return List.of();
        }
    }

    /**
     * Get default foods to display (active foods ordered by name)
     * Reusable method following DRY principle
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
            return List.of();
        }
    }
}
