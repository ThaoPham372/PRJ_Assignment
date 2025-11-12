package service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests cho PasswordService
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PasswordServiceTest {

    private static PasswordService passwordService;
    private static String hashedPassword;

    @BeforeAll
    public static void setUp() {
        passwordService = new PasswordService();
    }

    @Test
    @Order(1)
    @DisplayName("Test hash password")
    public void testHashPassword() {
        String plainPassword = "testpassword123";
        hashedPassword = passwordService.hashPassword(plainPassword);
        
        assertNotNull(hashedPassword, "Hashed password không được null");
        assertNotEquals(plainPassword, hashedPassword, "Hashed password phải khác plain password");
        assertTrue(hashedPassword.startsWith("$2a$"), "BCrypt hash phải bắt đầu với $2a$");
    }

    @Test
    @Order(2)
    @DisplayName("Test verify password thành công")
    public void testVerifyPassword_Success() {
        String plainPassword = "testpassword123";
        boolean isValid = passwordService.verifyPassword(plainPassword, hashedPassword);
        assertTrue(isValid, "Password verification phải thành công");
    }

    @Test
    @Order(3)
    @DisplayName("Test verify password thất bại với password sai")
    public void testVerifyPassword_Failure() {
        String wrongPassword = "wrongpassword";
        boolean isValid = passwordService.verifyPassword(wrongPassword, hashedPassword);
        assertFalse(isValid, "Password verification phải thất bại với password sai");
    }

    @Test
    @Order(4)
    @DisplayName("Test verify password với adminuser1")
    public void testVerifyPassword_AdminUser() {
        // Test với password của adminuser
        String plainPassword = "adminuser1";
        String hash = passwordService.hashPassword(plainPassword);
        boolean isValid = passwordService.verifyPassword(plainPassword, hash);
        assertTrue(isValid, "Password verification phải thành công cho adminuser1");
    }

    @Test
    @Order(5)
    @DisplayName("Test verify password với memberuser1")
    public void testVerifyPassword_MemberUser() {
        // Test với password của memberuser
        String plainPassword = "memberuser1";
        String hash = passwordService.hashPassword(plainPassword);
        boolean isValid = passwordService.verifyPassword(plainPassword, hash);
        assertTrue(isValid, "Password verification phải thành công cho memberuser1");
    }

    @Test
    @Order(6)
    @DisplayName("Test verify password với traineruser1")
    public void testVerifyPassword_TrainerUser() {
        // Test với password của traineruser
        String plainPassword = "traineruser1";
        String hash = passwordService.hashPassword(plainPassword);
        boolean isValid = passwordService.verifyPassword(plainPassword, hash);
        assertTrue(isValid, "Password verification phải thành công cho traineruser1");
    }

    @Test
    @Order(7)
    @DisplayName("Test validate password hợp lệ")
    public void testIsValidPassword_Valid() {
        assertTrue(passwordService.isValidPassword("password123"), "Password hợp lệ phải pass validation");
        assertTrue(passwordService.isValidPassword("adminuser1"), "adminuser1 phải hợp lệ");
        assertTrue(passwordService.isValidPassword("memberuser1"), "memberuser1 phải hợp lệ");
        assertTrue(passwordService.isValidPassword("traineruser1"), "traineruser1 phải hợp lệ");
    }

    @Test
    @Order(8)
    @DisplayName("Test validate password không hợp lệ")
    public void testIsValidPassword_Invalid() {
        assertFalse(passwordService.isValidPassword("short"), "Password ngắn phải không hợp lệ");
        assertFalse(passwordService.isValidPassword("12345678"), "Password chỉ có số phải không hợp lệ");
        assertFalse(passwordService.isValidPassword("abcdefgh"), "Password chỉ có chữ phải không hợp lệ");
        assertFalse(passwordService.isValidPassword(null), "Password null phải không hợp lệ");
    }

    @Test
    @Order(9)
    @DisplayName("Test hash password với null hoặc empty")
    public void testHashPassword_InvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            passwordService.hashPassword(null);
        }, "Hash password với null phải throw IllegalArgumentException");
        
        assertThrows(IllegalArgumentException.class, () -> {
            passwordService.hashPassword("");
        }, "Hash password với empty string phải throw IllegalArgumentException");
        
        assertThrows(IllegalArgumentException.class, () -> {
            passwordService.hashPassword("   ");
        }, "Hash password với whitespace only phải throw IllegalArgumentException");
    }

    @Test
    @Order(10)
    @DisplayName("Test verify password với null")
    public void testVerifyPassword_Null() {
        assertFalse(passwordService.verifyPassword(null, hashedPassword), "Verify với null password phải trả về false");
        assertFalse(passwordService.verifyPassword("test", null), "Verify với null hash phải trả về false");
        assertFalse(passwordService.verifyPassword(null, null), "Verify với cả hai null phải trả về false");
    }
}

