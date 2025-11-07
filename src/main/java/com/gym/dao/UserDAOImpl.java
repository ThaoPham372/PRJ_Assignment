package com.gym.dao;

import java.util.Optional;
import com.gym.model.User;
import jakarta.persistence.NoResultException;

/**
 * UserDAOImpl - Implementation of IUserDAO for specialized User search operations
 * Uses GenericDAO's shared EntityManager for consistency
 * Provides search methods for finding students/users by username or name
 */
public class UserDAOImpl extends GenericDAO<User> implements IUserDAO {
    
    public UserDAOImpl() {
        super(User.class);
    }
    
    /**
     * Find user by username or name with DTYPE='User' and status='ACTIVE'
     * Priority: username first (exact match), then name
     * Useful for PT management - finding students by username or full name
     */
    @Override
    public Optional<User> findByUsernameOrNameAndDtypeAndStatus(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Optional.empty();
        }
        
        try {
            // Priority 1: Find by username (unique field)
            try {
                User user = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :input AND u.dtype = 'User' AND u.status = 'ACTIVE'",
                    User.class)
                    .setParameter("input", input)
                    .getSingleResult();
                return Optional.of(user);
            } catch (NoResultException e) {
                // Priority 2: Find by name (may have multiple matches)
                // Return first result if multiple users have the same name
                java.util.List<User> users = em.createQuery(
                    "SELECT u FROM User u WHERE u.name = :input AND u.dtype = 'User' AND u.status = 'ACTIVE'",
                    User.class)
                    .setParameter("input", input)
                    .getResultList();
                
                if (!users.isEmpty()) {
                    return Optional.of(users.get(0));
                }
                return Optional.empty();
            }
        } catch (Exception ex) {
            System.err.println("[UserDAOImpl] Error findByUsernameOrNameAndDtypeAndStatus: " + ex.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Find user by username or name with ROLE='User' and status='ACTIVE'
     * Priority: username first (exact match), then name
     * Alternative method using role field instead of dtype
     */
    @Override
    public Optional<User> findByUsernameOrNameAndRoleAndStatus(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Optional.empty();
        }
        
        try {
            // Priority 1: Find by username (unique field)
            try {
                User user = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :input AND u.role = 'User' AND u.status = 'ACTIVE'",
                    User.class)
                    .setParameter("input", input)
                    .getSingleResult();
                return Optional.of(user);
            } catch (NoResultException e) {
                // Priority 2: Find by name (may have multiple matches)
                java.util.List<User> users = em.createQuery(
                    "SELECT u FROM User u WHERE u.name = :input AND u.role = 'User' AND u.status = 'ACTIVE'",
                    User.class)
                    .setParameter("input", input)
                    .getResultList();
                
                if (!users.isEmpty()) {
                    return Optional.of(users.get(0));
                }
                return Optional.empty();
            }
        } catch (Exception ex) {
            System.err.println("[UserDAOImpl] Error findByUsernameOrNameAndRoleAndStatus: " + ex.getMessage());
            return Optional.empty();
        }
    }
}

