package util;

import dao.UserDAO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test để verify TestDataInitializer hoạt động đúng
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestDataInitializerTest {

    private static UserDAO userDAO;

    @BeforeAll
    public static void setUp() {
        // Khởi tạo dữ liệu test
        TestDataInitializer.initialize();
        userDAO = new UserDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Test verify adminuser được tạo")
    public void testAdminUserCreated() {
        boolean exists = userDAO.existsByUsername("adminuser");
        assertTrue(exists, "adminuser phải tồn tại sau khi TestDataInitializer.initialize()");
    }

    @Test
    @Order(2)
    @DisplayName("Test verify memberuser được tạo")
    public void testMemberUserCreated() {
        boolean exists = userDAO.existsByUsername("memberuser");
        assertTrue(exists, "memberuser phải tồn tại sau khi TestDataInitializer.initialize()");
    }

    @Test
    @Order(3)
    @DisplayName("Test verify traineruser được tạo")
    public void testTrainerUserCreated() {
        boolean exists = userDAO.existsByUsername("traineruser");
        assertTrue(exists, "traineruser phải tồn tại sau khi TestDataInitializer.initialize()");
    }
}

