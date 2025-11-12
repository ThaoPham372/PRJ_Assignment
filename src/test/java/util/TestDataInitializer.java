package util;

import dao.AdminDAO;
import dao.GymInfoDAO;
import dao.MemberDAO;
import dao.TrainerDAO;
import dao.UserDAO;
import model.Admin;
import model.GymInfo;
import model.Member;
import model.Trainer;
import service.PasswordService;

import java.util.Date;
import java.util.List;

/**
 * TestDataInitializer - Khởi tạo dữ liệu demo cho JUnit tests
 * 
 * Utility class để tạo dữ liệu test trước khi chạy tests
 */
public class TestDataInitializer {

    private static final String ADMIN_USERNAME = "adminuser";
    private static final String ADMIN_PASSWORD = "adminuser1";
    private static final String ADMIN_EMAIL = "admin@gymfit.vn";
    
    private static final String MEMBER_USERNAME = "memberuser";
    private static final String MEMBER_PASSWORD = "memberuser1";
    private static final String MEMBER_EMAIL = "member@gymfit.vn";
    
    private static final String TRAINER_USERNAME = "traineruser";
    private static final String TRAINER_PASSWORD = "traineruser1";
    private static final String TRAINER_EMAIL = "trainer@gymfit.vn";

    private static boolean initialized = false;

    /**
     * Khởi tạo dữ liệu demo nếu chưa được khởi tạo
     */
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }

        System.out.println("========================================");
        System.out.println("[TestDataInitializer] Bắt đầu khởi tạo dữ liệu test...");
        System.out.println("========================================");

        try {
            PasswordService passwordService = new PasswordService();

            // 1. Tạo Admin user (tạo DAO mới trong method)
            createAdminUser(null, null, passwordService);

            // 2. Tạo Member user (tạo DAO mới trong method)
            createMemberUser(null, null, passwordService);

            // 3. Tạo Trainer user (tạo DAO mới trong method)
            createTrainerUser(null, null, null, passwordService);

            // Đợi một chút để đảm bảo database commit hoàn tất
            Thread.sleep(500);

            initialized = true;
            System.out.println("[TestDataInitializer] Hoàn tất khởi tạo dữ liệu test.");
        } catch (Exception e) {
            System.err.println("[TestDataInitializer] Lỗi khi khởi tạo dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("========================================");
    }

    /**
     * Tạo tài khoản Admin
     */
    private static void createAdminUser(UserDAO userDAO, AdminDAO adminDAO, PasswordService passwordService) {
        try {
            // Kiểm tra xem đã tồn tại chưa với UserDAO mới để đảm bảo không cache
            UserDAO checkDAO = new UserDAO();
            if (checkDAO.existsByUsername(ADMIN_USERNAME)) {
                System.out.println("[TestDataInitializer] Tài khoản adminuser đã tồn tại, bỏ qua.");
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

            // Lưu vào database với AdminDAO mới
            AdminDAO saveDAO = new AdminDAO();
            int adminId = saveDAO.save(admin);
            
            if (adminId > 0) {
                // Verify với UserDAO mới để đảm bảo dữ liệu đã được commit
                UserDAO verifyDAO = new UserDAO();
                boolean exists = verifyDAO.existsByUsername(ADMIN_USERNAME);
                if (exists) {
                    System.out.println("[TestDataInitializer] ✓ Đã tạo tài khoản Admin: " + ADMIN_USERNAME + " (ID: " + adminId + ")");
                } else {
                    System.err.println("[TestDataInitializer] ⚠ Tài khoản Admin đã được tạo nhưng chưa commit vào database");
                }
            } else {
                System.err.println("[TestDataInitializer] ✗ Không thể tạo tài khoản Admin");
            }
        } catch (Exception e) {
            System.err.println("[TestDataInitializer] Lỗi khi tạo Admin user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tạo tài khoản Member
     */
    private static void createMemberUser(UserDAO userDAO, MemberDAO memberDAO, PasswordService passwordService) {
        try {
            // Kiểm tra xem đã tồn tại chưa với UserDAO mới
            UserDAO checkDAO = new UserDAO();
            if (checkDAO.existsByUsername(MEMBER_USERNAME)) {
                System.out.println("[TestDataInitializer] Tài khoản memberuser đã tồn tại, bỏ qua.");
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

            // Lưu vào database với MemberDAO mới
            MemberDAO saveDAO = new MemberDAO();
            int memberId = saveDAO.save(member);
            
            if (memberId > 0) {
                // Verify với UserDAO mới
                UserDAO verifyDAO = new UserDAO();
                boolean exists = verifyDAO.existsByUsername(MEMBER_USERNAME);
                if (exists) {
                    System.out.println("[TestDataInitializer] ✓ Đã tạo tài khoản Member: " + MEMBER_USERNAME + " (ID: " + memberId + ")");
                } else {
                    System.err.println("[TestDataInitializer] ⚠ Tài khoản Member đã được tạo nhưng chưa commit vào database");
                }
            } else {
                System.err.println("[TestDataInitializer] ✗ Không thể tạo tài khoản Member");
            }
        } catch (Exception e) {
            System.err.println("[TestDataInitializer] Lỗi khi tạo Member user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tạo tài khoản Trainer
     */
    private static void createTrainerUser(UserDAO userDAO, TrainerDAO trainerDAO, GymInfoDAO gymInfoDAO, PasswordService passwordService) {
        try {
            // Kiểm tra xem đã tồn tại chưa với UserDAO mới
            UserDAO checkDAO = new UserDAO();
            if (checkDAO.existsByUsername(TRAINER_USERNAME)) {
                System.out.println("[TestDataInitializer] Tài khoản traineruser đã tồn tại, bỏ qua.");
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

            // Gán gym cho trainer (lấy gym đầu tiên nếu có) với GymInfoDAO mới
            GymInfoDAO gymDAO = new GymInfoDAO();
            List<GymInfo> gyms = gymDAO.findAll();
            if (gyms != null && !gyms.isEmpty()) {
                GymInfo firstGym = gyms.get(0);
                trainer.setWorkAt(String.valueOf(firstGym.getGymId()));
                System.out.println("[TestDataInitializer] Gán trainer vào gym: " + firstGym.getName() + " (ID: " + firstGym.getGymId() + ")");
            } else {
                System.out.println("[TestDataInitializer] Cảnh báo: Không tìm thấy gym nào, trainer sẽ không được gán vào gym.");
            }

            // Lưu vào database với TrainerDAO mới
            TrainerDAO saveDAO = new TrainerDAO();
            int trainerId = saveDAO.save(trainer);
            
            if (trainerId > 0) {
                // Verify với UserDAO mới
                UserDAO verifyDAO = new UserDAO();
                boolean exists = verifyDAO.existsByUsername(TRAINER_USERNAME);
                if (exists) {
                    System.out.println("[TestDataInitializer] ✓ Đã tạo tài khoản Trainer: " + TRAINER_USERNAME + " (ID: " + trainerId + ")");
                } else {
                    System.err.println("[TestDataInitializer] ⚠ Tài khoản Trainer đã được tạo nhưng chưa commit vào database");
                }
            } else {
                System.err.println("[TestDataInitializer] ✗ Không thể tạo tài khoản Trainer");
            }
        } catch (Exception e) {
            System.err.println("[TestDataInitializer] Lỗi khi tạo Trainer user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reset flag để có thể khởi tạo lại (dùng cho test cleanup)
     */
    public static void reset() {
        initialized = false;
    }
}

