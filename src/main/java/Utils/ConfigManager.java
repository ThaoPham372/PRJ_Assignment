package Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager for loading application properties
 * Loads configuration from email.properties file
 */
public class ConfigManager {
    
    private static ConfigManager instance;
    private final Properties properties;
    
    private ConfigManager() {
        this.properties = new Properties();
        loadProperties();
    }
    
    /**
     * Get singleton instance of ConfigManager
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * Load properties from email.properties file
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("email.properties")) {
            if (input == null) {
                System.err.println("[ConfigManager] ERROR: email.properties file not found in classpath");
                return;
            }
            
            properties.load(input);
            System.out.println("[ConfigManager] Successfully loaded email.properties");
            
        } catch (IOException e) {
            System.err.println("[ConfigManager] ERROR: Failed to load email.properties - " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get a property value by key
     * @param key Property key
     * @return Property value or null if not found
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get a property value with default fallback
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get Google OAuth Client ID
     * @return Google Client ID
     */
    public String getGoogleClientId() {
        String clientId = getProperty("google.client.id");
        if (clientId == null || clientId.trim().isEmpty()) {
            System.err.println("[ConfigManager] WARNING: google.client.id is missing in email.properties");
        }
        return clientId;
    }
    
    /**
     * Get Gemini API Key
     * @return Gemini API Key
     */
    public String getGeminiApiKey() {
        String apiKey = getProperty("GEMINI_API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("[ConfigManager] WARNING: GEMINI_API_KEY is missing in email.properties");
        }
        return apiKey;
    }
    
    /**
     * Check if a property exists
     * @param key Property key
     * @return true if property exists
     */
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }
}
