package listener;

import dao.AdminDAO;
import dao.GymInfoDAO;
import dao.MemberDAO;
import dao.TrainerDAO;
import dao.UserDAO;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import model.Admin;
import model.GymInfo;
import model.Member;
import model.Trainer;
import service.PasswordService;

import java.util.Date;
import java.util.List;

/**
 * DataInitializer - Khởi tạo dữ liệu demo khi ứng dụng được khởi động lần đầu
 * 
 * Tạo 3 tài khoản demo:
 * - adminuser / adminuser1 (role: Admin)
 * - memberuser / memberuser1 (role: Member)
 * - traineruser / traineruser1 (role: Trainer)
 * 
 * Nếu dữ liệu đã tồn tại, chỉ in log và tiếp tục chạy ứng dụng
 */
@WebListener
public class DataInitializer implements ServletContextListener {

    private static final String ADMIN_USERNAME = "adminuser";
    private static final String ADMIN_PASSWORD = "adminuser1";
    private static final String ADMIN_EMAIL = "admin@gymfit.vn";
    
    private static final String MEMBER_USERNAME = "memberuser";
    private static final String MEMBER_PASSWORD = "memberuser1";
    private static final String MEMBER_EMAIL = "member@gymfit.vn";
    
    private static final String TRAINER_USERNAME = "traineruser";
    private static final String TRAINER_PASSWORD = "traineruser1";
    private static final String TRAINER_EMAIL = "trainer@gymfit.vn";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("========================================");
        System.out.println("[DataInitializer] Bắt đầu khởi tạo dữ liệu demo...");
        System.out.println("========================================");
        
        try {
            initializeDemoUsers();
            
            // Khởi tạo dữ liệu mua hàng (orders, payments, memberships)
            SalesDataInitializer.initialize();
            
            System.out.println("[DataInitializer] Hoàn tất khởi tạo dữ liệu demo.");
        } catch (Exception e) {
            System.err.println("[DataInitializer] Lỗi khi khởi tạo dữ liệu: " + e.getMessage());
            e.printStackTrace();
            // Không throw exception để ứng dụng vẫn có thể chạy
        }
        
        System.out.println("========================================");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup nếu cần
    }

    /**
     * Khởi tạo các tài khoản demo
     */
    private void initializeDemoUsers() {
        PasswordService passwordService = new PasswordService();
        UserDAO userDAO = new UserDAO();
        AdminDAO adminDAO = new AdminDAO();
        MemberDAO memberDAO = new MemberDAO();
        TrainerDAO trainerDAO = new TrainerDAO();
        GymInfoDAO gymInfoDAO = new GymInfoDAO();

        // 1. Tạo Admin user
        createAdminUser(userDAO, adminDAO, passwordService);

        // 2. Tạo Member user
        createMemberUser(userDAO, memberDAO, passwordService);

        // 3. Tạo Trainer user
        createTrainerUser(userDAO, trainerDAO, gymInfoDAO, passwordService);
    }

    /**
     * Tạo tài khoản Admin
     */
    private void createAdminUser(UserDAO userDAO, AdminDAO adminDAO, PasswordService passwordService) {
        try {
            // Kiểm tra xem đã tồn tại chưa
            if (userDAO.existsByUsername(ADMIN_USERNAME)) {
                System.out.println("[DataInitializer] Tài khoản adminuser đã tồn tại, bỏ qua.");
                return;
            }

            // Hash password
            String passwordHash = passwordService.hashPassword(ADMIN_PASSWORD);

            // Tạo Admin entity
            Admin admin = new Admin();
            admin.setUsername(ADMIN_USERNAME);
            admin.setPassword(passwordHash);
            admin.setEmail(ADMIN_EMAIL);
            admin.setName("Admin User");
            admin.setRole("Admin");
            admin.setStatus("ACTIVE");
            admin.setCreatedDate(new Date());
            admin.setNote("Tài khoản admin demo");

            // Lưu vào database
            int adminId = adminDAO.save(admin);
            if (adminId > 0) {
                System.out.println("[DataInitializer] ✓ Đã tạo tài khoản Admin: " + ADMIN_USERNAME + " (ID: " + adminId + ")");
            } else {
                System.err.println("[DataInitializer] ✗ Không thể tạo tài khoản Admin");
            }
        } catch (Exception e) {
            System.err.println("[DataInitializer] Lỗi khi tạo Admin user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tạo tài khoản Member
     */
    private void createMemberUser(UserDAO userDAO, MemberDAO memberDAO, PasswordService passwordService) {
        try {
            // Kiểm tra xem đã tồn tại chưa
            if (userDAO.existsByUsername(MEMBER_USERNAME)) {
                System.out.println("[DataInitializer] Tài khoản memberuser đã tồn tại, bỏ qua.");
                return;
            }

            // Hash password
            String passwordHash = passwordService.hashPassword(MEMBER_PASSWORD);

            // Tạo Member entity
            Member member = new Member();
            member.setUsername(MEMBER_USERNAME);
            member.setPassword(passwordHash);
            member.setEmail(MEMBER_EMAIL);
            member.setName("Member User");
            member.setRole("Member");
            member.setStatus("ACTIVE");
            member.setCreatedDate(new Date());
            member.setPhone("0901234567");
            member.setAddress("123 Đường ABC, Quận XYZ, TP.HCM");
            member.setWeight(70.0f);
            member.setHeight(175.0f);
            member.setGoal("Giảm cân và tăng cơ");

            // Lưu vào database
            int memberId = memberDAO.save(member);
            if (memberId > 0) {
                System.out.println("[DataInitializer] ✓ Đã tạo tài khoản Member: " + MEMBER_USERNAME + " (ID: " + memberId + ")");
            } else {
                System.err.println("[DataInitializer] ✗ Không thể tạo tài khoản Member");
            }
        } catch (Exception e) {
            System.err.println("[DataInitializer] Lỗi khi tạo Member user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tạo tài khoản Trainer
     */
    private void createTrainerUser(UserDAO userDAO, TrainerDAO trainerDAO, GymInfoDAO gymInfoDAO, PasswordService passwordService) {
        try {
            // Kiểm tra xem đã tồn tại chưa
            if (userDAO.existsByUsername(TRAINER_USERNAME)) {
                System.out.println("[DataInitializer] Tài khoản traineruser đã tồn tại, bỏ qua.");
                return;
            }

            // Hash password
            String passwordHash = passwordService.hashPassword(TRAINER_PASSWORD);

            // Tạo Trainer entity
            Trainer trainer = new Trainer();
            trainer.setUsername(TRAINER_USERNAME);
            trainer.setPassword(passwordHash);
            trainer.setEmail(TRAINER_EMAIL);
            trainer.setName("Trainer User");
            trainer.setRole("Trainer");
            trainer.setStatus("ACTIVE");
            trainer.setCreatedDate(new Date());
            trainer.setPhone("0907654321");
            trainer.setSpecialization("Cardio, Strength Training, Yoga");
            trainer.setYearsOfExperience(5);
            trainer.setCertificationLevel("Certified Personal Trainer");
            trainer.setSalary(15000000.0f);

            // Gán gym cho trainer (lấy gym đầu tiên nếu có)
            List<GymInfo> gyms = gymInfoDAO.findAll();
            if (gyms != null && !gyms.isEmpty()) {
                GymInfo firstGym = gyms.get(0);
                trainer.setWorkAt(String.valueOf(firstGym.getGymId()));
                System.out.println("[DataInitializer] Gán trainer vào gym: " + firstGym.getName() + " (ID: " + firstGym.getGymId() + ")");
            } else {
                System.out.println("[DataInitializer] Cảnh báo: Không tìm thấy gym nào, trainer sẽ không được gán vào gym.");
            }

            // Lưu vào database
            int trainerId = trainerDAO.save(trainer);
            if (trainerId > 0) {
                System.out.println("[DataInitializer] ✓ Đã tạo tài khoản Trainer: " + TRAINER_USERNAME + " (ID: " + trainerId + ")");
            } else {
                System.err.println("[DataInitializer] ✗ Không thể tạo tài khoản Trainer");
            }
        } catch (Exception e) {
            System.err.println("[DataInitializer] Lỗi khi tạo Trainer user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

