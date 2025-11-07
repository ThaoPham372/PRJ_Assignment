package com.gym.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.gym.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

/**
 * UserDAO - Data Access Object for users table
 * Handles user registration, duplicate checks, and login history
 */
public class UserDAO {
    private EntityManagerFactory entityManagerFactory;

    public UserDAO() {
        // Lazy initialization
        if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
            entityManagerFactory = Persistence.createEntityManagerFactory("gym-pu");
        }
    }

    /**
     * Check if username already exists
     */
    public boolean existsByUsername(String username) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.username = :username",
                    Long.class);
            query.setParameter("username", username);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            System.err.println("Error checking username existence: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Check if email already exists
     */
    public boolean existsByEmail(String email) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.email = :email",
                    Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Insert new user and return generated ID
     */
    public long insertUser(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(user);
            em.flush(); // Ensure ID is generated
            transaction.commit();
            return user.getId();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error inserting user: " + e.getMessage());
            throw new RuntimeException("Error inserting user", e);
        } finally {
            em.close();
        }
    }

    /**
     * Find user by username for login
     */
    public User findByUsername(String username) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username",
                    User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.err.println("Error finding user by username: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Find active user by username (or email) with status filter
     */
    public User getUserByNameAndStatus(String name, String requiredStatus) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            // Try username or email match with status
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE (u.username = :name OR u.email = :name) AND u.status = :status",
                    User.class);
            query.setParameter("name", name);
            query.setParameter("status", requiredStatus);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.err.println("Error getUserByNameAndStatus: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Find user by ID
     */
    public User findById(long userId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.find(User.class, userId);
        } catch (Exception e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Find user by ID (overload for backward compatibility with Connection
     * parameter)
     * 
     * @deprecated Use findById(long userId) instead
     */
    @Deprecated
    public User findById(Connection conn, long userId) throws SQLException {
        return findById(userId);
    }

    /**
     * Find user by username or email for login
     */
    public User findByUsernameOrEmail(String usernameOrEmail) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :value OR u.email = :value",
                    User.class);
            query.setParameter("value", usernameOrEmail);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.err.println("Error finding user by username or email: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Find user by username or email (overload for backward compatibility)
     * 
     * @deprecated Use findByUsernameOrEmail(String usernameOrEmail) instead
     */
    @Deprecated
    public User findByUsernameOrEmail(Connection conn, String usernameOrEmail) throws SQLException {
        return findByUsernameOrEmail(usernameOrEmail);
    }

    /**
     * Increment failed login attempts
     */
    public void incrementFailedLoginAttempts(long userId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                em.merge(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error incrementing failed login attempts: " + e.getMessage());
            throw new RuntimeException("Error incrementing failed login attempts", e);
        } finally {
            em.close();
        }
    }

    /**
     * Increment failed login attempts (overload for backward compatibility)
     * 
     * @deprecated Use incrementFailedLoginAttempts(long userId) instead
     */
    @Deprecated
    public void incrementFailedLoginAttempts(Connection conn, long userId) throws SQLException {
        incrementFailedLoginAttempts(userId);
    }

    /**
     * Reset failed login attempts
     */
    public void resetFailedLoginAttempts(long userId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setFailedLoginAttempts(0);
                user.setLockedUntil(null);
                em.merge(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error resetting failed login attempts: " + e.getMessage());
            throw new RuntimeException("Error resetting failed login attempts", e);
        } finally {
            em.close();
        }
    }

    /**
     * Reset failed login attempts (overload for backward compatibility)
     * 
     * @deprecated Use resetFailedLoginAttempts(long userId) instead
     */
    @Deprecated
    public void resetFailedLoginAttempts(Connection conn, long userId) throws SQLException {
        resetFailedLoginAttempts(userId);
    }

    /**
     * Lock account for specified minutes
     */
    public void lockAccount(long userId, int minutes) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                long lockedUntilMillis = System.currentTimeMillis() + (minutes * 60 * 1000L);
                user.setLockedUntil(new Timestamp(lockedUntilMillis));
                em.merge(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error locking account: " + e.getMessage());
            throw new RuntimeException("Error locking account", e);
        } finally {
            em.close();
        }
    }

    /**
     * Lock account (overload for backward compatibility)
     * 
     * @deprecated Use lockAccount(long userId, int minutes) instead
     */
    @Deprecated
    public void lockAccount(Connection conn, long userId, int minutes) throws SQLException {
        lockAccount(userId, minutes);
    }

    /**
     * Update last login time
     */
    public void updateLastLogin(long userId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setLastLogin(new Timestamp(System.currentTimeMillis()));
                em.merge(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error updating last login: " + e.getMessage());
            throw new RuntimeException("Error updating last login", e);
        } finally {
            em.close();
        }
    }

    /**
     * Update last login (overload for backward compatibility)
     * 
     * @deprecated Use updateLastLogin(long userId) instead
     */
    @Deprecated
    public void updateLastLogin(Connection conn, long userId) throws SQLException {
        updateLastLogin(userId);
    }

    /**
     * Update user password
     */
    public boolean updatePassword(long userId, String newPasswordHash) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setPassword(newPasswordHash);
                em.merge(user);
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error updating password: " + e.getMessage());
            throw new RuntimeException("Error updating password", e);
        } finally {
            em.close();
        }
    }

    /**
     * Update password (overload for backward compatibility)
     * 
     * @deprecated Use updatePassword(long userId, String newPasswordHash) instead
     */
    @Deprecated
    public boolean updatePassword(Connection conn, long userId, String newPasswordHash) throws SQLException {
        return updatePassword(userId, newPasswordHash);
    }

    /**
     * Update user information
     */
    public boolean updateUser(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            User merged = em.merge(user);
            transaction.commit();
            return merged != null;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error updating user: " + e.getMessage());
            throw new RuntimeException("Error updating user", e);
        } finally {
            em.close();
        }
    }

    /**
     * Update user (overload for backward compatibility)
     * 
     * @deprecated Use updateUser(User user) instead
     */
    @Deprecated
    public boolean updateUser(Connection conn, User user) throws SQLException {
        return updateUser(user);
    }

    /**
     * Trả về toàn bộ user (dùng cho dropdown chọn học viên)
     */
    public java.util.List<User> getAllUsers() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u ORDER BY u.username",
                    User.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error getting all users: " + e.getMessage());
            return new java.util.ArrayList<>();
        } finally {
            em.close();
        }
    }
}
