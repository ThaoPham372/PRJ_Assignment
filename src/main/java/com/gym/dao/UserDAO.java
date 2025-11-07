package com.gym.dao;

import java.time.LocalDateTime;
import java.util.List;

import com.gym.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 * UserDAO - Data Access Object for user table using JPA
 * Extends GenericDAO for basic CRUD operations
 * Handles user authentication, registration, duplicate checks, and login
 * history
 */
public class UserDAO extends GenericDAO<User> {

    public UserDAO() {
        super(User.class);
    }

    /**
     * Check if username already exists
     */
    public boolean existsByUsername(String username) {
        try {
            User user = findByField("username", username);
            return user != null;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error checking username existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if email already exists
     */
    public boolean existsByEmail(String email) {
        try {
            User user = findByField("email", email);
            return user != null;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error checking email existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Insert new user and return generated ID
     * Uses GenericDAO save() method
     */
    public long insertUser(String username, String email, String passwordHash, String salt) {
        return insertUser(username, null, email, passwordHash, salt);
    }

    /**
     * Insert new user with explicit name (full name) and return generated ID
     */
    public long insertUser(String username, String name, String email, String passwordHash, String salt) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordHash);
            user.setStatus("ACTIVE");
            user.setCreatedDate(LocalDateTime.now());

            beginTransaction();
            em.persist(user);
            commitTransaction();

            // Refresh to get generated ID
            em.refresh(user);

            Integer userId = user.getUserId();
            if (userId != null) {
                return userId.longValue();
            }
            return -1;
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[UserDAO] Error inserting user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to insert user: " + e.getMessage(), e);
        }
    }

    /**
     * Insert login history record for registration
     * Note: This method may need a separate LoginHistory entity if login_history
     * table exists
     */
    public void insertLoginHistory(long userId, String ipAddress, String userAgent) {
        // TODO: Implement if login_history table exists
        // For now, this is a placeholder
        System.out.println("[UserDAO] Login history: userId=" + userId + ", ip=" + ipAddress);
    }

    /**
     * Find user by username
     */
    public User findByUsername(String username) {
        return findByField("username", username);
    }

    /**
     * Find user by email (case-insensitive, trimmed)
     */
    public User findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        try {
            // Normalize: trim + lowercase để tránh lỗi case-sensitive
            String normalizedEmail = email.trim().toLowerCase();

            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE LOWER(TRIM(u.email)) = :email",
                    User.class);
            query.setParameter("email", normalizedEmail);

            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("[UserDAO] No user found with email: " + email);
            return null;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error finding user by email: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find user by username or email
     */
    public User findByUsernameOrEmail(String usernameOrEmail) {
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :value OR u.email = :value",
                    User.class);
            query.setParameter("value", usernameOrEmail);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error finding user by username or email: " + e.getMessage());
            return null;
        }

    }

    /**
     * Find user by username or email using provided EntityManager (for
     * transactions)
     * Note: In JPA, we use EntityManager instead of Connection
     */
    public User findByUsernameOrEmail(EntityManager sharedEm, String usernameOrEmail) {
        try {
            TypedQuery<User> query = sharedEm.createQuery(
                    "SELECT u FROM User u WHERE u.username = :value OR u.email = :value",
                    User.class);
            query.setParameter("value", usernameOrEmail);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error finding user by username or email: " + e.getMessage());
            return null;
        }

    }

    /**
     * Find user by username or email using default EntityManager
     * Backward compatibility method for code that doesn't use transactions
     */
    public User findByUsernameOrEmailWithConnection(java.sql.Connection conn, String usernameOrEmail) {
        // In JPA, we use EntityManager instead of Connection
        // This method is for backward compatibility
        return findByUsernameOrEmail(em, usernameOrEmail);
    }

    /**
     * Increment failed login attempts using EntityManager
     */
    public void incrementFailedLoginAttempts(EntityManager sharedEm, long userId) {
        boolean transactionStarted = false;
        try {
            if (sharedEm.getTransaction().isActive()) {
                // Transaction already active, don't start new one
                transactionStarted = false;
            } else {
                sharedEm.getTransaction().begin();
                transactionStarted = true;
            }
            User user = sharedEm.find(User.class, (int) userId);
            if (user != null) {
                Integer currentAttempts = user.getFailedLoginAttempts();
                user.setFailedLoginAttempts(currentAttempts != null ? currentAttempts + 1 : 1);
                sharedEm.merge(user);
            }
            if (transactionStarted) {
                sharedEm.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && sharedEm.getTransaction().isActive()) {
                try {
                    sharedEm.getTransaction().rollback();
                } catch (Exception rollbackEx) {
                    System.err.println("[UserDAO] Error rolling back transaction: " + rollbackEx.getMessage());
                }
            }
            System.err.println("[UserDAO] Error incrementing failed login attempts: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to increment failed login attempts", e);
        }
    }

    /**
     * Increment failed login attempts using default EntityManager
     * Backward compatibility method
     */
    public void incrementFailedLoginAttempts(java.sql.Connection conn, long userId) {
        incrementFailedLoginAttempts(em, userId);
    }

    /**
     * Reset failed login attempts using EntityManager
     */
    public void resetFailedLoginAttempts(EntityManager sharedEm, long userId) {
        boolean transactionStarted = false;
        try {
            if (sharedEm.getTransaction().isActive()) {
                transactionStarted = false;
            } else {
                sharedEm.getTransaction().begin();
                transactionStarted = true;
            }
            User user = sharedEm.find(User.class, (int) userId);
            if (user != null) {
                user.setFailedLoginAttempts(0);
                user.setLockedUntil(null);
                sharedEm.merge(user);
            }
            if (transactionStarted) {
                sharedEm.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && sharedEm.getTransaction().isActive()) {
                try {
                    sharedEm.getTransaction().rollback();
                } catch (Exception rollbackEx) {
                    System.err.println("[UserDAO] Error rolling back transaction: " + rollbackEx.getMessage());
                }
            }
            System.err.println("[UserDAO] Error resetting failed login attempts: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to reset failed login attempts", e);
        }
    }

    /**
     * Reset failed login attempts using default EntityManager
     * Backward compatibility method
     */
    public void resetFailedLoginAttempts(java.sql.Connection conn, long userId) {
        resetFailedLoginAttempts(em, userId);
    }

    /**
     * Lock account for specified minutes using EntityManager
     */
    public void lockAccount(EntityManager sharedEm, long userId, int minutes) {
        boolean transactionStarted = false;
        try {
            if (sharedEm.getTransaction().isActive()) {
                transactionStarted = false;
            } else {
                sharedEm.getTransaction().begin();
                transactionStarted = true;
            }
            User user = sharedEm.find(User.class, (int) userId);
            if (user != null) {
                LocalDateTime lockedUntil = LocalDateTime.now().plusMinutes(minutes);
                user.setLockedUntil(lockedUntil);
                sharedEm.merge(user);
            }
            if (transactionStarted) {
                sharedEm.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && sharedEm.getTransaction().isActive()) {
                try {
                    sharedEm.getTransaction().rollback();
                } catch (Exception rollbackEx) {
                    System.err.println("[UserDAO] Error rolling back transaction: " + rollbackEx.getMessage());
                }
            }
            System.err.println("[UserDAO] Error locking account: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to lock account", e);
        }
    }

    /**
     * Lock account using default EntityManager
     * Backward compatibility method
     */
    public void lockAccount(java.sql.Connection conn, long userId, int minutes) {
        lockAccount(em, userId, minutes);
    }

    /**
     * Update last login time using EntityManager
     */
    public void updateLastLogin(EntityManager sharedEm, long userId) {
        boolean transactionStarted = false;
        try {
            if (sharedEm.getTransaction().isActive()) {
                transactionStarted = false;
            } else {
                sharedEm.getTransaction().begin();
                transactionStarted = true;
            }
            User user = sharedEm.find(User.class, (int) userId);
            if (user != null) {
                user.setLastLogin(LocalDateTime.now());
                sharedEm.merge(user);
            }
            if (transactionStarted) {
                sharedEm.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && sharedEm.getTransaction().isActive()) {
                try {
                    sharedEm.getTransaction().rollback();
                } catch (Exception rollbackEx) {
                    System.err.println("[UserDAO] Error rolling back transaction: " + rollbackEx.getMessage());
                }
            }
            System.err.println("[UserDAO] Error updating last login: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update last login", e);
        }
    }

    /**
     * Update last login using default EntityManager
     * Backward compatibility method
     */
    public void updateLastLogin(java.sql.Connection conn, long userId) {
        updateLastLogin(em, userId);
    }

    /**
     * Get user by ID
     * IMPORTANT: With JOINED inheritance, this will return the correct subclass
     * (Student/Admin)
     * if DTYPE is set correctly in database
     * Uses EntityManager.find() which respects inheritance
     */
    public User getUserById(long userId) {
        try {
            // Use EntityManager.find() which automatically handles JOINED inheritance
            // If user is Student, it will return Student instance
            // If user is Admin, it will return Admin instance
            // If user is base User, it will return User instance
            User user = em.find(User.class, (int) userId);
            if (user != null) {
                System.out.println("[UserDAO] getUserById - Loaded user type: " + user.getClass().getSimpleName() +
                        ", userId: " + user.getUserId());
            }
            return user;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Update user information
     * Uses GenericDAO update() method
     */
    public boolean updateUser(User user) {
        try {
            int result = update(user);
            return result != -1;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update avatar URL for a user
     */
    public boolean updateAvatarUrl(long userId, String avatarUrl) {
        try {
            User user = getUserById(userId);
            if (user != null) {
                user.setAvatarUrl(avatarUrl);
                user.setLastUpdate(LocalDateTime.now());
                return updateUser(user);
            }
            return false;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error updating avatar URL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete user (soft delete by setting status to INACTIVE)
     */
    public boolean deleteUser(long userId) {
        try {
            User user = getUserById(userId);
            if (user != null) {
                user.setStatus("INACTIVE");
                user.setLastUpdate(LocalDateTime.now());
                return updateUser(user);
            }
            return false;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Hard delete user (permanent removal from database)
     */
    public boolean hardDeleteUser(long userId) {
        try {
            User user = getUserById(userId);
            if (user != null) {
                int result = delete(user);
                return result != -1;
            }
            return false;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error hard deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return findAll();
    }

    /**
     * Find users by status
     */
    public List<User> findByStatus(String status) {
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.status = :status",
                    User.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[UserDAO] Error finding users by status: " + e.getMessage());
            return List.of();
        }
    }

    // ==================== ADMIN SEARCH & PAGINATION ====================

    /**
     * Search users by keyword (username, name, email, phone) with pagination
     * ✅ Tái sử dụng GenericDAO EntityManager
     */
    public List<User> searchUsers(String keyword, int page, int pageSize) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT u FROM User u WHERE 1=1");

            if (keyword != null && !keyword.trim().isEmpty()) {
                jpql.append(" AND (LOWER(u.username) LIKE LOWER(:keyword) ")
                        .append("OR LOWER(u.name) LIKE LOWER(:keyword) ")
                        .append("OR LOWER(u.email) LIKE LOWER(:keyword) ")
                        .append("OR u.phone LIKE :keyword)");
            }

            jpql.append(" ORDER BY u.createdDate DESC");

            TypedQuery<User> query = em.createQuery(jpql.toString(), User.class);

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword.trim() + "%");
            }

            // Pagination
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            return query.getResultList();
        } catch (Exception e) {
            System.err.println("[UserDAO] Error searching users: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Count users matching search criteria
     */
    public int countUsers(String keyword) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(u) FROM User u WHERE 1=1");

            if (keyword != null && !keyword.trim().isEmpty()) {
                jpql.append(" AND (LOWER(u.username) LIKE LOWER(:keyword) ")
                        .append("OR LOWER(u.name) LIKE LOWER(:keyword) ")
                        .append("OR LOWER(u.email) LIKE LOWER(:keyword) ")
                        .append("OR u.phone LIKE :keyword)");
            }

            TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword.trim() + "%");
            }

            return query.getSingleResult().intValue();
        } catch (Exception e) {
            System.err.println("[UserDAO] Error counting users: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Find active user by username (or email) with status filter
     * Useful for finding users with specific status (e.g., ACTIVE, INACTIVE)
     * Searches by username OR email
     */
    public User getUserByNameAndStatus(String name, String requiredStatus) {
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE (u.username = :name OR u.email = :name) AND u.status = :status",
                    User.class);
            query.setParameter("name", name);
            query.setParameter("status", requiredStatus);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.err.println("[UserDAO] Error getUserByNameAndStatus: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get EntityManager for transaction management
     */
    public EntityManager getEntityManager() {
        return em;
    }
}
