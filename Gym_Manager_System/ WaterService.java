 physical com.gym.service;

import com.gym.dao.UserDAO;
import com.gym.model.User;
import java.math.BigDecimal;
import java.util.List;

/**
 * UserService - Service layer for user business logic
 * Provides operations for user management (CRUD)
 */
public class UserService {
    
    private final UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(long userId) {
        if (userId <= 0) {
            return null;
        }
        return userDAO.getUserById(userId);
    }
    
    /**
     * Update user information
     * Validates data before updating
     */
    public boolean updateUser(User user) {
        if (user == null || user.getId() <= 0) {
            return false;
        }
        
        // Validate required fields
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return false;
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return false;
        }
        
        // Validate email format (basic check)
        if (!isValidEmail(user.getEmail())) {
            return false;
        }
        
        return userDAO.updateUser(user);
    }
    
    /**
     * Delete user (soft delete)
     */
    public boolean deleteUser(long userId) {
        if (userId <= 0) {
            return false;
        }
        return userDAO.deleteUser(userId);
    }
    
    /**
     * Hard delete user (permanent removal)
     * Use with caution!
     */
    public boolean hardDeleteUser(long userId) {
        if (userId <= 0) {
            return false;
        }
        return userDAO.hardDeleteUser(userId);
    }
    
    /**
     * Check if username exists
     */
    public boolean usernameExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userDAO.existsByUsername(username);
    }
    
    /**
     * Check if email exists
     */
    public boolean emailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userDAO.existsByEmail(email);
    }
    
    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return userDAO.findByUsername(username);
    }
    
    /**
     * Get user by email
     */
    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return userDAO.findByEmail(email);
    }
    
    /**
     * Calculate and get BMI category
     */
    public String getBMICategory(BigDecimal bmi) {
        if (bmi == null) {
            return null;
        }
        
        double bmiValue = bmi.doubleValue();
        if (bmiValue < 18.5) {
            return "Underweight";
        } else if (bmiValue < 25) {
            return "Normal";
        } else if (bmiValue < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }
    
    /**
     * Validate email format (basic validation)
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty guard
