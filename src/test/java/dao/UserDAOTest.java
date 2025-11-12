package dao;

import model.User;
import org.junit.jupiter.api.*;
import util.TestDataInitializer;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests cho UserDAO
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTest {

    private static UserDAO userDAO;
    private static int testUserId;

    @BeforeAll
    public static void setUp() {
        // Khởi tạo dữ liệu test trước khi chạy tests
        TestDataInitializer.initialize();
        userDAO = new UserDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Test tìm user theo username - adminuser")
    public void testFindByUsername_AdminUser() {
        User user = userDAO.findByUsername("adminuser");
        assertNotNull(user, "User adminuser phải tồn tại");
        assertEquals("adminuser", user.getUsername(), "Username phải là adminuser");
        assertEquals("Admin", user.getRole(), "Role phải là Admin");
        assertEquals("ACTIVE", user.getStatus(), "Status phải là ACTIVE");
        testUserId = user.getId();
    }

    @Test
    @Order(2)
    @DisplayName("Test tìm user theo username - memberuser")
    public void testFindByUsername_MemberUser() {
        User user = userDAO.findByUsername("memberuser");
        assertNotNull(user, "User memberuser phải tồn tại");
        assertEquals("memberuser", user.getUsername(), "Username phải là memberuser");
        assertEquals("Member", user.getRole(), "Role phải là Member");
    }

    @Test
    @Order(3)
    @DisplayName("Test tìm user theo username - traineruser")
    public void testFindByUsername_TrainerUser() {
        User user = userDAO.findByUsername("traineruser");
        assertNotNull(user, "User traineruser phải tồn tại");
        assertEquals("traineruser", user.getUsername(), "Username phải là traineruser");
        assertEquals("Trainer", user.getRole(), "Role phải là Trainer");
    }

    @Test
    @Order(4)
    @DisplayName("Test tìm user theo email")
    public void testFindByEmail() {
        User user = userDAO.findByEmail("admin@gymfit.vn");
        assertNotNull(user, "User với email admin@gymfit.vn phải tồn tại");
        assertEquals("adminuser", user.getUsername(), "Username phải là adminuser");
    }

    @Test
    @Order(5)
    @DisplayName("Test kiểm tra username tồn tại")
    public void testExistsByUsername() {
        assertTrue(userDAO.existsByUsername("adminuser"), "adminuser phải tồn tại");
        assertTrue(userDAO.existsByUsername("memberuser"), "memberuser phải tồn tại");
        assertTrue(userDAO.existsByUsername("traineruser"), "traineruser phải tồn tại");
        assertFalse(userDAO.existsByUsername("nonexistentuser"), "nonexistentuser không tồn tại");
    }

    @Test
    @Order(6)
    @DisplayName("Test kiểm tra email tồn tại")
    public void testExistsByEmail() {
        assertTrue(userDAO.existsByEmail("admin@gymfit.vn") > -1, "Email admin@gymfit.vn phải tồn tại");
        assertTrue(userDAO.existsByEmail("member@gymfit.vn") > -1, "Email member@gymfit.vn phải tồn tại");
        assertTrue(userDAO.existsByEmail("trainer@gymfit.vn") > -1, "Email trainer@gymfit.vn phải tồn tại");
        assertEquals(-1, userDAO.existsByEmail("nonexistent@gymfit.vn"), "Email không tồn tại phải trả về -1");
    }

    @Test
    @Order(7)
    @DisplayName("Test tìm user theo ID")
    public void testFindById() {
        if (testUserId > 0) {
            User user = userDAO.findById(testUserId);
            assertNotNull(user, "User phải tồn tại với ID: " + testUserId);
            assertEquals(testUserId, user.getId(), "ID phải khớp");
        }
    }

    @Test
    @Order(8)
    @DisplayName("Test lấy tất cả users")
    public void testFindAll() {
        var users = userDAO.findAll();
        assertNotNull(users, "Danh sách users không được null");
        assertFalse(users.isEmpty(), "Danh sách users không được rỗng");
        assertTrue(users.size() >= 3, "Phải có ít nhất 3 users (admin, member, trainer)");
    }
}

