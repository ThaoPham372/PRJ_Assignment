package com.gym.dao;

import com.gym.model.Trainer;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * TrainerDAO - Data Access Object for trainer table using JPA
 * Extends GenericDAO for basic CRUD operations
 * Handles trainer-specific information (specialization, experience, ratings, etc.)
 */
public class TrainerDAO extends GenericDAO<Trainer> {

    public TrainerDAO() {
        super(Trainer.class);
    }

    /**
     * Find trainer by user_id
     * Uses JPA find() which will load Trainer entity if it exists (JOINED inheritance)
     */
    public Optional<Trainer> findByUserId(int userId) {
        try {
            Trainer trainer = em.find(Trainer.class, userId);
            return Optional.ofNullable(trainer);
        } catch (Exception e) {
            System.err.println("[TrainerDAO] ERROR finding trainer by user_id: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Find trainer by name
     */
    public Trainer findByName(String name) {
        return findByField("name", name);
    }

    /**
     * Find trainer by email
     */
    public Trainer findByEmail(String email) {
        return findByField("email", email);
    }

    /**
     * Check if trainer exists by name
     */
    public boolean existsByName(String name) {
        Trainer trainer = findByField("name", name);
        return trainer != null;
    }

    /**
     * Check if trainer exists by email
     */
    public boolean existsByEmail(String email) {
        Trainer trainer = findByField("email", email);
        return trainer != null;
    }

    /**
     * Find trainer by name or email
     */
    public Trainer findByNameOrEmail(String nameOrEmail) {
        Trainer trainer = findByField("name", nameOrEmail);
        if (trainer == null) {
            trainer = findByField("email", nameOrEmail);
        }
        return trainer;
    }

    /**
     * Save trainer (insert new or update existing)
     * Uses GenericDAO save() method
     */
    public int saveTrainer(Trainer trainer) {
        try {
            return save(trainer);
        } catch (Exception e) {
            System.err.println("[TrainerDAO] ERROR saving trainer: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save trainer: " + e.getMessage(), e);
        }
    }

    /**
     * Update trainer
     * Uses GenericDAO update() method
     */
    public boolean updateTrainer(Trainer trainer) {
        try {
            int result = update(trainer);
            return result != -1;
        } catch (Exception e) {
            System.err.println("[TrainerDAO] ERROR updating trainer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Find all trainers
     */
    public List<Trainer> findAllTrainers() {
        return findAll();
    }

    /**
     * Find trainers by specialization
     */
    public List<Trainer> findBySpecialization(String specialization) {
        try {
            TypedQuery<Trainer> query = em.createQuery(
                "SELECT t FROM Trainer t WHERE t.specialization = :specialization",
                Trainer.class
            );
            query.setParameter("specialization", specialization);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[TrainerDAO] Error finding trainers by specialization: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Find trainers by certification level
     */
    public List<Trainer> findByCertificationLevel(String certificationLevel) {
        try {
            TypedQuery<Trainer> query = em.createQuery(
                "SELECT t FROM Trainer t WHERE t.certificationLevel = :certificationLevel",
                Trainer.class
            );
            query.setParameter("certificationLevel", certificationLevel);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[TrainerDAO] Error finding trainers by certification level: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Find top rated trainers (by average rating)
     */
    public List<Trainer> findTopRatedTrainers(int limit) {
        try {
            TypedQuery<Trainer> query = em.createQuery(
                "SELECT t FROM Trainer t WHERE t.averageRating IS NOT NULL ORDER BY t.averageRating DESC",
                Trainer.class
            );
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[TrainerDAO] Error finding top rated trainers: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Soft delete trainer (set status to INACTIVE)
     */
    public boolean softDelete(Trainer trainer) {
        try {
            trainer.setStatus("INACTIVE");
            return updateTrainer(trainer);
        } catch (Exception e) {
            System.err.println("[TrainerDAO] ERROR soft deleting trainer: " + e.getMessage());
            return false;
        }
    }
}
