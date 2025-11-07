package com.gym.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    private static final Properties props = new Properties();
    private static final String PROPERTIES_FILE = "db.properties";

    static {
        try (InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (is == null) {
                throw new IllegalStateException(PROPERTIES_FILE + " not found on classpath");
            }
            props.load(is);
        } catch (Exception e) {
            System.err.println("Failed to load " + PROPERTIES_FILE + ": " + e.getMessage());
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static String getProperty(String key) {
        String value = System.getenv(key); 
        if (value == null) {
            value = props.getProperty(key); 
        }
        if (value == null) {
            throw new RuntimeException("Missing required property: " + key);
        }
        return value;
    }
}