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

    static {
        Properties props = new Properties();
        try (InputStream is = DatabaseUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                throw new IllegalStateException("db.properties not found on classpath");
            }
            props.load(is);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("DB_URL"));
            config.setUsername(props.getProperty("DB_USERNAME"));
            config.setPassword(props.getProperty("DB_PASSWORD"));

            // Optional pool settings with sensible defaults
            config.setMaximumPoolSize(parseInt(props.getProperty("DB_POOL_MAX_SIZE"), 10));
            config.setMinimumIdle(parseInt(props.getProperty("DB_POOL_MIN_IDLE"), 2));
            config.setMaxLifetime(parseLong(props.getProperty("DB_POOL_MAX_LIFETIME_MS"), 30 * 60_000));
            config.setConnectionTimeout(parseLong(props.getProperty("DB_POOL_CONNECTION_TIMEOUT_MS"), 30_000));
            config.setIdleTimeout(parseLong(props.getProperty("DB_POOL_IDLE_TIMEOUT_MS"), 10 * 60_000));

            // Set MySQL driver class explicitly
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            dataSource = new HikariDataSource(config);
        } catch (IOException e) {
            System.err.println("Failed to load db.properties: " + e.getMessage());
            throw new RuntimeException("Failed to load db.properties", e);
        } catch (Exception e) {
            System.err.println("Failed to initialize HikariCP: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize HikariCP", e);
        }
    }

    private static int parseInt(String value, int def) {
        try { return value == null ? def : Integer.parseInt(value); } catch (NumberFormatException e) { return def; }
    }

    private static long parseLong(String value, long def) {
        try { return value == null ? def : Long.parseLong(value); } catch (NumberFormatException e) { return def; }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource not initialized");
        }
        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}


