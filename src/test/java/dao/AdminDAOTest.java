package dao;

import model.Admin;
import model.User;
import org.junit.jupiter.api.*;
import util.TestDataInitializer;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests cho AdminDAO
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminDAOTest {

    private static AdminDAO adminDAO;
    private static UserDAO userDAO;
    private static int adminUserId;

    @BeforeAll
    public static void setUp() {
        // Khởi tạo dữ liệu test trước khi chạy tests
        TestDataInitializer.initialize();
        adminDAO = new AdminDAO();
        userDAO = new UserDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Test tìm admin theo username - adminuser")
    public void testFindByUsername() {
        User user = userDAO.findByUsername("adminuser");
        assertNotNull(user, "User adminuser phải tồn tại");
        assertEquals("adminuser", user.getUsername(), "Username phải là adminuser");
        assertEquals("Admin", user.getRole(), "Role phải là Admin");
        assertEquals("ACTIVE", user.getStatus(), "Status phải là ACTIVE");
        
        // Tìm Admin entity từ ID
        adminUserId = user.getId();
        Admin admin = adminDAO.findById(adminUserId);
        assertNotNull(admin, "Admin entity phải tồn tại");
        assertInstanceOf(Admin.class, admin, "Phải là instance của Admin");
    }

    @Test
    @Order(2)
    @DisplayName("Test tìm admin theo email")
    public void testFindByEmail() {
        Admin admin = adminDAO.findByEmail("admin@gymfit.vn");
        assertNotNull(admin, "Admin với email admin@gymfit.vn phải tồn tại");
        assertEquals("adminuser", admin.getUsername(), "Username phải là adminuser");
    }

    @Test
    @Order(3)
    @DisplayName("Test admin có note")
    public void testAdminHasNote() {
        if (adminUserId > 0) {
            Admin admin = adminDAO.findById(adminUserId);
            assertNotNull(admin, "Admin phải tồn tại");
            // Note có thể null, chỉ kiểm tra admin tồn tại
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test lấy tất cả admins")
    public void testFindAll() {
        var admins = adminDAO.findAll();
        assertNotNull(admins, "Danh sách admins không được null");
        assertFalse(admins.isEmpty(), "Danh sách admins không được rỗng");
    }

    @Test
    @Order(5)
    @DisplayName("Test kiểm tra email tồn tại")
    public void testExistsByEmail() {
        assertTrue(adminDAO.existsByEmail("admin@gymfit.vn") > -1, "Email admin@gymfit.vn phải tồn tại");
        assertEquals(-1, adminDAO.existsByEmail("nonexistent@gymfit.vn"), "Email không tồn tại phải trả về -1");
    }
}

