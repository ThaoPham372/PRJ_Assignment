package com.gym.dao;

import com.gym.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * BaseDAO - Base class for all DAO implementations using JPA
 * Uses JPAUtil to get singleton EntityManagerFactory
 * EntityManager is created per-instance but should be closed when DAO is no longer needed
 */
public abstract class BaseDAO<T> {

    /**
     * Get EntityManagerFactory (singleton, managed by JPAUtil)
     */
    protected EntityManagerFactory getEntityManagerFactory() {
        return JPAUtil.getEntityManagerFactory();
    }

    /**
     * EntityManager instance (one per DAO instance)
     * IMPORTANT: This should be closed when DAO is no longer needed
     * For request-scoped operations, consider creating new EntityManager per request
     */
    protected EntityManager em;

    public BaseDAO() {
        // Create EntityManager from singleton EntityManagerFactory
        em = getEntityManagerFactory().createEntityManager();
    }

    /**
     * Begin transaction
     */
    public void beginTransaction() {
        if (em != null && !em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    /**
     * Commit transaction
     */
    public void commitTransaction() {
        if (em != null && em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    /**
     * Rollback transaction
     */
    public void rollbackTransaction() {
        if (em != null && em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    /**
     * Close EntityManager (call when DAO is no longer needed)
     */
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
            em = null;
        }
    }

    /**
     * Get EntityManager (for use in subclasses)
     */
    protected EntityManager getEntityManager() {
        return em;
    }
}