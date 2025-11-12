package dao;

import model.Member;
import org.junit.jupiter.api.*;
import util.TestDataInitializer;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests cho MemberDAO
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberDAOTest {

    private static MemberDAO memberDAO;

    @BeforeAll
    public static void setUp() {
        // Khởi tạo dữ liệu test trước khi chạy tests
        TestDataInitializer.initialize();
        memberDAO = new MemberDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Test tìm member theo username - memberuser")
    public void testFindByUsername() {
        Member member = memberDAO.findByUsername("memberuser");
        assertNotNull(member, "Member memberuser phải tồn tại");
        assertEquals("memberuser", member.getUsername(), "Username phải là memberuser");
        assertEquals("Member", member.getRole(), "Role phải là Member");
        assertEquals("ACTIVE", member.getStatus(), "Status phải là ACTIVE");
    }

    @Test
    @Order(2)
    @DisplayName("Test tìm member theo email")
    public void testFindByEmail() {
        Member member = memberDAO.findByEmail("member@gymfit.vn");
        assertNotNull(member, "Member với email member@gymfit.vn phải tồn tại");
        assertEquals("memberuser", member.getUsername(), "Username phải là memberuser");
    }

    @Test
    @Order(3)
    @DisplayName("Test kiểm tra member có thông tin cá nhân")
    public void testMemberHasPersonalInfo() {
        Member member = memberDAO.findByUsername("memberuser");
        assertNotNull(member, "Member phải tồn tại");
        assertNotNull(member.getName(), "Member phải có tên");
        assertNotNull(member.getEmail(), "Member phải có email");
    }

    @Test
    @Order(4)
    @DisplayName("Test lấy tất cả members")
    public void testFindAll() {
        var members = memberDAO.findAll();
        assertNotNull(members, "Danh sách members không được null");
        assertFalse(members.isEmpty(), "Danh sách members không được rỗng");
    }

    @Test
    @Order(5)
    @DisplayName("Test kiểm tra email tồn tại")
    public void testExistsByEmail() {
        assertTrue(memberDAO.existsByEmail("member@gymfit.vn") > -1, "Email member@gymfit.vn phải tồn tại");
        assertEquals(-1, memberDAO.existsByEmail("nonexistent@gymfit.vn"), "Email không tồn tại phải trả về -1");
    }
}

