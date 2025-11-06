package com.gym.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DatabaseUtil initializes a HikariCP connection pool using values from
 * src/main/resources/db.properties. Fill in DB_URL, DB_USERNAME, DB_PASSWORD.
 */
public class DatabaseUtil {

    private static HikariDataSource dataSource;
    private static volatile boolean initialized = false;
    private static final Object initLock = new Object();

    static {
        // Initialize lazily to avoid conflicts with JPA's HikariCP pool
        // Only initialize if db.properties exists and is needed
        try {
            initializeIfNeeded();
        } catch (Exception e) {
            // Don't throw - allow JPA to handle connections
            System.err.println("[DatabaseUtil] WARNING: Could not initialize DatabaseUtil: " + e.getMessage());
            System.err.println("[DatabaseUtil] This is OK if using JPA EntityManager instead");
            // dataSource remains null, getConnection() will handle it gracefully
        }
    }
    
    /**
     * Initialize DataSource if not already initialized
     * This is called lazily to avoid conflicts with JPA
     */
    private static void initializeIfNeeded() {
        if (initialized) {
            return;
        }
        
        synchronized (initLock) {
            if (initialized) {
                return;
            }
            
            try {
                Properties props = new Properties();
                try (InputStream is = DatabaseUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
                    if (is == null) {
                        System.err.println("[DatabaseUtil] db.properties not found - using JPA connections only");
                        initialized = true; // Mark as initialized to avoid retrying
                        return;
                    }
                    props.load(is);

                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl(props.getProperty("DB_URL"));
                    config.setUsername(props.getProperty("DB_USERNAME"));
                    config.setPassword(props.getProperty("DB_PASSWORD"));

                    // Optional pool settings with sensible defaults
                    config.setMaximumPoolSize(parseInt(props.getProperty("DB_POOL_MAX_SIZE"), 5)); // Reduced to avoid conflicts
                    config.setMinimumIdle(parseInt(props.getProperty("DB_POOL_MIN_IDLE"), 1));
                    config.setMaxLifetime(parseLong(props.getProperty("DB_POOL_MAX_LIFETIME_MS"), 30 * 60_000));
                    config.setConnectionTimeout(parseLong(props.getProperty("DB_POOL_CONNECTION_TIMEOUT_MS"), 30_000));
                    config.setIdleTimeout(parseLong(props.getProperty("DB_POOL_IDLE_TIMEOUT_MS"), 10 * 60_000));

                    // Set MySQL driver class explicitly
                    config.setDriverClassName("com.mysql.cj.jdbc.Driver");

                    dataSource = new HikariDataSource(config);
                    initialized = true;
                    System.out.println("[DatabaseUtil] Successfully initialized HikariCP DataSource");
                }
            } catch (IOException e) {
                System.err.println("[DatabaseUtil] Failed to load db.properties: " + e.getMessage());
                initialized = true; // Mark as initialized to avoid retrying
                // Don't throw - allow JPA to handle connections
            } catch (Exception e) {
                System.err.println("[DatabaseUtil] Failed to initialize HikariCP: " + e.getMessage());
                e.printStackTrace();
                initialized = true; // Mark as initialized to avoid retrying
                // Don't throw - allow JPA to handle connections
            }
        }
    }

    private static int parseInt(String value, int def) {
        try { return value == null ? def : Integer.parseInt(value); } catch (NumberFormatException e) { return def; }
    }

    private static long parseLong(String value, long def) {
        try { return value == null ? def : Long.parseLong(value); } catch (NumberFormatException e) { return def; }
    }

    public static Connection getConnection() throws SQLException {
        // Try to initialize if not already done
        if (!initialized) {
            initializeIfNeeded();
        }
        
        if (dataSource == null) {
            // If JPA is being used, try to get connection from JPA EntityManagerFactory
            // This is a fallback for legacy code that still uses DatabaseUtil
            throw new SQLException("DatabaseUtil DataSource not available. Please use JPA EntityManager instead.");
        }
        return dataSource.getConnection();
    }

    /**
     * Get the HikariDataSource instance
     * This is used by Hibernate to configure connection pooling
     */
    public static HikariDataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource not initialized");
        }
        return dataSource;
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}


