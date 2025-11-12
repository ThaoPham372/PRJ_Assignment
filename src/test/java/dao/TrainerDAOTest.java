package dao;

import model.Trainer;
import model.User;
import org.junit.jupiter.api.*;
import util.TestDataInitializer;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests cho TrainerDAO
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrainerDAOTest {

    private static TrainerDAO trainerDAO;
    private static UserDAO userDAO;
    private static int trainerUserId;

    @BeforeAll
    public static void setUp() {
        // Khởi tạo dữ liệu test trước khi chạy tests
        TestDataInitializer.initialize();
        trainerDAO = new TrainerDAO();
        userDAO = new UserDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Test tìm trainer theo username - traineruser")
    public void testFindByUsername() {
        User user = userDAO.findByUsername("traineruser");
        assertNotNull(user, "User traineruser phải tồn tại");
        assertEquals("traineruser", user.getUsername(), "Username phải là traineruser");
        assertEquals("Trainer", user.getRole(), "Role phải là Trainer");
        assertEquals("ACTIVE", user.getStatus(), "Status phải là ACTIVE");
        
        // Tìm Trainer entity từ ID
        trainerUserId = user.getId();
        Trainer trainer = trainerDAO.findById(trainerUserId);
        assertNotNull(trainer, "Trainer entity phải tồn tại");
        assertInstanceOf(Trainer.class, trainer, "Phải là instance của Trainer");
    }

    @Test
    @Order(2)
    @DisplayName("Test tìm trainer theo email")
    public void testFindByEmail() {
        Trainer trainer = trainerDAO.findByEmail("trainer@gymfit.vn");
        assertNotNull(trainer, "Trainer với email trainer@gymfit.vn phải tồn tại");
        assertEquals("traineruser", trainer.getUsername(), "Username phải là traineruser");
    }

    @Test
    @Order(3)
    @DisplayName("Test trainer có thông tin chuyên môn")
    public void testTrainerHasSpecialization() {
        if (trainerUserId > 0) {
            Trainer trainer = trainerDAO.findById(trainerUserId);
            assertNotNull(trainer, "Trainer phải tồn tại");
            assertNotNull(trainer.getSpecialization(), "Trainer phải có specialization");
            assertNotNull(trainer.getCertificationLevel(), "Trainer phải có certification level");
            assertNotNull(trainer.getYearsOfExperience(), "Trainer phải có years of experience");
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test lấy tất cả trainers")
    public void testFindAll() {
        var trainers = trainerDAO.findAll();
        assertNotNull(trainers, "Danh sách trainers không được null");
        assertFalse(trainers.isEmpty(), "Danh sách trainers không được rỗng");
    }

    @Test
    @Order(5)
    @DisplayName("Test kiểm tra email tồn tại")
    public void testExistsByEmail() {
        assertTrue(trainerDAO.existsByEmail("trainer@gymfit.vn") > -1, "Email trainer@gymfit.vn phải tồn tại");
        assertEquals(-1, trainerDAO.existsByEmail("nonexistent@gymfit.vn"), "Email không tồn tại phải trả về -1");
    }
}

