package com.gym.listener;

import com.gym.util.JPAUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * JPAContextListener - Initializes and destroys JPA EntityManagerFactory
 * Called when application starts/stops
 */
@WebListener
public class JPAContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[JPAContextListener] Initializing JPA...");
        try {
            // Initialize EntityManagerFactory (singleton)
            JPAUtil.getEntityManagerFactory();
            // Store in ServletContext for reference
            sce.getServletContext().setAttribute("emf", JPAUtil.getEntityManagerFactory());
            System.out.println("[JPAContextListener] JPA initialized successfully");
        } catch (Exception e) {
            System.err.println("[JPAContextListener] ERROR initializing JPA: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize JPA", e);
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[JPAContextListener] Destroying JPA...");
        try {
            // Close EntityManagerFactory
            JPAUtil.closeEntityManagerFactory();
            System.out.println("[JPAContextListener] JPA destroyed successfully");
        } catch (Exception e) {
            System.err.println("[JPAContextListener] ERROR destroying JPA: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

