package com.gym.service;

import com.gym.dao.TrainerDAO;
import com.gym.model.Trainer;
import java.util.List;
import java.util.Optional;

/**
 * TrainerService - Service layer for trainer operations
 * Handles business logic for trainer-specific information
 * Reusable service for all trainer-related operations
 */
public class TrainerService {
    
    private final TrainerDAO trainerDAO;
    
    public TrainerService() {
        this.trainerDAO = new TrainerDAO();
    }
    
    /**
     * Get all trainers
     */
    public List<Trainer> getAll() {
        return trainerDAO.findAllTrainers();
    }
    
    /**
     * Get trainer by ID
     */
    public Trainer getTrainerById(int id) {
        return trainerDAO.findById(id);
    }
    
    /**
     * Get trainer by user_id
     * Returns empty Trainer with userId set if not found
     */
    public Trainer getTrainerByUserId(int userId) {
        Optional<Trainer> trainerOpt = trainerDAO.findByUserId(userId);
        if (trainerOpt.isPresent()) {
            return trainerOpt.get();
        } else {
            // Return empty trainer with userId set
            Trainer trainer = new Trainer();
            trainer.setUserId(userId);
            return trainer;
        }
    }
    
    /**
     * Get trainer by name
     */
    public Trainer getTrainerByName(String name) {
        return trainerDAO.findByName(name);
    }
    
    /**
     * Get trainer by email
     */
    public Trainer getTrainerByEmail(String email) {
        return trainerDAO.findByEmail(email);
    }
    
    /**
     * Get trainer by name or email
     */
    public Trainer getTrainerByNameOrEmail(String nameOrEmail) {
        return trainerDAO.findByNameOrEmail(nameOrEmail);
    }
    
    /**
     * Save trainer (insert new or update existing)
     */
    public int saveTrainer(Trainer trainer) {
        return trainerDAO.saveTrainer(trainer);
    }
    
    /**
     * Update trainer
     */
    public boolean updateTrainer(Trainer trainer) {
        return trainerDAO.updateTrainer(trainer);
    }
    
    /**
     * Soft delete trainer (set status to INACTIVE)
     */
    public boolean deleteTrainer(Trainer trainer) {
        return trainerDAO.softDelete(trainer);
    }
    
    /**
     * Get trainers by specialization
     */
    public List<Trainer> getTrainersBySpecialization(String specialization) {
        return trainerDAO.findBySpecialization(specialization);
    }
    
    /**
     * Get trainers by certification level
     */
    public List<Trainer> getTrainersByCertificationLevel(String certificationLevel) {
        return trainerDAO.findByCertificationLevel(certificationLevel);
    }
    
    /**
     * Get top rated trainers
     */
    public List<Trainer> getTopRatedTrainers(int limit) {
        return trainerDAO.findTopRatedTrainers(limit);
    }
    
    /**
     * Check if trainer exists by name
     */
    public boolean existsByName(String name) {
        return trainerDAO.existsByName(name);
    }
    
    /**
     * Check if trainer exists by email
     */
    public boolean existsByEmail(String email) {
        return trainerDAO.existsByEmail(email);
    }
}
