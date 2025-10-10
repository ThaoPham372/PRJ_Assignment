//package test;
//
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Properties;
//import java.util.Scanner;
//
///**
// * TestDatabaseSetup - Utility class để setup test database
// * Sử dụng H2 in-memory database cho testing
// */
//public class TestDatabaseSetup {
//    
//    private static final String TEST_PROPERTIES_FILE = "test-database.properties";
//    private static final String TEST_SCHEMA_FILE = "test-schema.sql";
//    
//    private static String dbUrl;
//    private static String dbUsername;
//    private static String dbPassword;
//    private static String dbDriver;
//    
//    static {
//        loadTestProperties();
//    }
//    
//    /**
//     * Load test database properties từ file
//     */
//    private static void loadTestProperties() {
//        try (InputStream input = TestDatabaseSetup.class.getClassLoader()
//                .getResourceAsStream(TEST_PROPERTIES_FILE)) {
//            
//            if (input == null) {
//                // Fallback values nếu không tìm thấy properties file
//                dbUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
//                dbUsername = "sa";
//                dbPassword = "";
//                dbDriver = "org.h2.Driver";
//                return;
//            }
//            
//            Properties prop = new Properties();
//            prop.load(input);
//            
//            dbUrl = prop.getProperty("db.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
//            dbUsername = prop.getProperty("db.username", "sa");
//            dbPassword = prop.getProperty("db.password", "");
//            dbDriver = prop.getProperty("db.driver", "org.h2.Driver");
//            
//        } catch (Exception e) {
//            System.err.println("Error loading test properties: " + e.getMessage());
//            // Fallback values
//            dbUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
//            dbUsername = "sa";
//            dbPassword = "";
//            dbDriver = "org.h2.Driver";
//        }
//    }
//    
//    /**
//     * Tạo connection đến test database
//     */
//    public static Connection getTestConnection() throws SQLException {
//        try {
//            Class.forName(dbDriver);
//            return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//        } catch (ClassNotFoundException e) {
//            throw new SQLException("Database driver not found: " + dbDriver, e);
//        }
//    }
//    
//    /**
//     * Setup test database với schema và test data
//     */
//    public static void setupTestDatabase() {
//        try (Connection conn = getTestConnection()) {
//            // Load và execute schema file
//            loadAndExecuteSchema(conn);
//            System.out.println("✅ Test database setup completed successfully");
//        } catch (SQLException e) {
//            System.err.println("❌ Error setting up test database: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//    
//    /**
//     * Load và execute SQL schema từ file
//     */
//    private static void loadAndExecuteSchema(Connection conn) throws SQLException {
//        try (InputStream input = TestDatabaseSetup.class.getClassLoader()
//                .getResourceAsStream(TEST_SCHEMA_FILE)) {
//            
//            if (input == null) {
//                System.err.println("⚠️ Test schema file not found: " + TEST_SCHEMA_FILE);
//                return;
//            }
//            
//            // Read SQL file content
//            Scanner scanner = new Scanner(input).useDelimiter("\\A");
//            String sqlContent = scanner.hasNext() ? scanner.next() : "";
//            scanner.close();
//            
//            // Split SQL statements (simple approach)
//            String[] statements = sqlContent.split(";");
//            
//            try (Statement stmt = conn.createStatement()) {
//                for (String statement : statements) {
//                    statement = statement.trim();
//                    if (!statement.isEmpty() && !statement.startsWith("--")) {
//                        try {
//                            stmt.execute(statement);
//                        } catch (SQLException e) {
//                            // Log warning but continue (some statements might fail)
//                            System.out.println("⚠️ Warning executing SQL: " + e.getMessage());
//                        }
//                    }
//                }
//            }
//            
//        } catch (Exception e) {
//            System.err.println("❌ Error loading schema file: " + e.getMessage());
//            throw new SQLException("Failed to load schema", e);
//        }
//    }
//    
//    /**
//     * Cleanup test database (drop all tables)
//     */
//    public static void cleanupTestDatabase() {
//        try (Connection conn = getTestConnection()) {
//            try (Statement stmt = conn.createStatement()) {
//                stmt.execute("DROP ALL OBJECTS");
//                System.out.println("✅ Test database cleaned up successfully");
//            }
//        } catch (SQLException e) {
//            System.err.println("⚠️ Warning during cleanup: " + e.getMessage());
//        }
//    }
//    
//    /**
//     * Reset test database (cleanup + setup)
//     */
//    public static void resetTestDatabase() {
//        cleanupTestDatabase();
//        setupTestDatabase();
//    }
//    
//    /**
//     * Get test database URL
//     */
//    public static String getTestDatabaseUrl() {
//        return dbUrl;
//    }
//    
//    /**
//     * Get test database username
//     */
//    public static String getTestDatabaseUsername() {
//        return dbUsername;
//    }
//    
//    /**
//     * Get test database password
//     */
//    public static String getTestDatabasePassword() {
//        return dbPassword;
//    }
//    
//    /**
//     * Test database connection
//     */
//    public static boolean testConnection() {
//        try (Connection conn = getTestConnection()) {
//            return conn != null && !conn.isClosed();
//        } catch (SQLException e) {
//            System.err.println("❌ Database connection test failed: " + e.getMessage());
//            return false;
//        }
//    }
//    
//    /**
//     * Main method để test database setup
//     */
//    public static void main(String[] args) {
//        System.out.println("🧪 Testing Database Setup...");
//        
//        // Test connection
//        if (testConnection()) {
//            System.out.println("✅ Database connection successful");
//        } else {
//            System.out.println("❌ Database connection failed");
//            return;
//        }
//        
//        // Setup database
//        setupTestDatabase();
//        
//        // Test again after setup
//        if (testConnection()) {
//            System.out.println("✅ Database setup completed successfully");
//        } else {
//            System.out.println("❌ Database setup failed");
//        }
//    }
//}
