package com.gym.dao;

import java.util.Optional;
import com.gym.model.User;

/**
 * IUserDAO - Interface for specialized User search operations
 * Provides methods to find users by username/name with role and status filters
 * Useful for PT management and student assignment features
 */
public interface IUserDAO {
    
    /**
     * Find user by username or name with DTYPE filter and ACTIVE status
     * Searches by:
     * 1. username first (exact match)
     * 2. name if username not found (returns first result if multiple matches)
     * 
     * @param input username or name to search
     * @return Optional containing User if found, empty otherwise
     */
    Optional<User> findByUsernameOrNameAndDtypeAndStatus(String input);
    
    /**
     * Find user by username or name with ROLE filter and ACTIVE status
     * Searches by:
     * 1. username first (exact match)
     * 2. name if username not found (returns first result if multiple matches)
     * 
     * @param input username or name to search
     * @return Optional containing User if found, empty otherwise
     */
    Optional<User> findByUsernameOrNameAndRoleAndStatus(String input);
}

