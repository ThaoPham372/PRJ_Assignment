package com.gym.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * JPAUtil - Singleton utility for EntityManagerFactory
 * Prevents multiple EMF instances and connection pool leaks
 */
public class JPAUtil {
    
    private static EntityManagerFactory emf;
    private static final Object lock = new Object();
    
    /**
     * Get or create EntityManagerFactory (singleton)
     * This should be called once at application startup
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            synchronized (lock) {
                if (emf == null) {
                    try {
                        emf = Persistence.createEntityManagerFactory("gymPU");
                        System.out.println("[JPAUtil] Created EntityManagerFactory singleton");
                    } catch (Exception e) {
                        System.err.println("[JPAUtil] ERROR creating EntityManagerFactory: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Failed to initialize EntityManagerFactory", e);
                    }
                }
            }
        }
        return emf;
    }
    
    /**
     * Create a new EntityManager (should be closed after use)
     * EntityManager is NOT thread-safe, create one per request/thread
     */
    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }
    
    /**
     * Close EntityManagerFactory (call on application shutdown)
     */
    public static void closeEntityManagerFactory() {
        synchronized (lock) {
            if (emf != null && emf.isOpen()) {
                emf.close();
                emf = null;
                System.out.println("[JPAUtil] Closed EntityManagerFactory");
            }
        }
    }
    
    /**
     * Check if EntityManagerFactory is initialized
     */
    public static boolean isInitialized() {
        return emf != null && emf.isOpen();
    }
}

