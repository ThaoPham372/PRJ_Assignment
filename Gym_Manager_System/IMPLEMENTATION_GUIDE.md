# 🛠️ HƯỚNG DẪN TRIỂN KHAI - GYM MANAGEMENT SYSTEM

## 📋 MỤC LỤC
1. [Setup môi trường](#setup-môi-trường)
2. [Cấu trúc dự án](#cấu-trúc-dự-án)
3. [Triển khai DAO Layer](#triển-khai-dao-layer)
4. [Triển khai Service Layer](#triển-khai-service-layer)
5. [Triển khai Controller](#triển-khai-controller)
6. [Testing](#testing)

---

## 🔧 SETUP MÔI TRƯỜNG

### 1. Dependencies cần thiết (pom.xml)

```xml
<dependencies>
    <!-- Servlet API -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JSTL -->
    <dependency>
        <groupId>jakarta.servlet.jsp.jstl</groupId>
        <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
        <version>3.0.0</version>
    </dependency>
    
    <!-- SQL Server JDBC Driver -->
    <dependency>
        <groupId>com.microsoft.sqlserver</groupId>
        <artifactId>mssql-jdbc</artifactId>
        <version>12.4.2.jre11</version>
    </dependency>
    
    <!-- BCrypt for password hashing -->
    <dependency>
        <groupId>org.mindrot</groupId>
        <artifactId>jbcrypt</artifactId>
        <version>0.4</version>
    </dependency>
    
    <!-- Gson for JSON -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
    
    <!-- JUnit for testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 2. Database Configuration

```properties
# src/main/resources/database.properties
db.url=jdbc:sqlserver://localhost:1433;databaseName=GymManagementDB;encrypt=true;trustServerCertificate=true
db.username=sa
db.password=your_password
db.driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
db.pool.size=10
```

---

## 📁 CẤU TRÚC DỰ ÁN

```
src/main/java/
├── model/                    # Models (DTO)
│   ├── User.java
│   ├── Member.java
│   ├── Coach.java
│   ├── MembershipPackage.java
│   ├── WorkoutSession.java
│   └── Payment.java
│
├── DAO/                      # Data Access Layer
│   ├── DBConnection.java     # Database connection pool
│   ├── UserDAO.java
│   ├── MemberDAO.java
│   ├── CoachDAO.java
│   ├── MembershipPackageDAO.java
│   ├── WorkoutSessionDAO.java
│   └── PaymentDAO.java
│
├── service/                  # Business Logic Layer
│   ├── UserService.java
│   ├── MemberService.java
│   ├── CoachService.java
│   ├── MembershipService.java
│   ├── WorkoutService.java
│   └── PaymentService.java
│
├── controller/               # Servlet Controllers
│   ├── AuthController.java
│   ├── MemberController.java
│   ├── CoachController.java
│   ├── WorkoutController.java
│   └── PaymentController.java
│
├── filter/                   # Servlet Filters
│   ├── AuthenticationFilter.java
│   └── AuthorizationFilter.java
│
└── util/                     # Utilities
    ├── PasswordUtil.java
    ├── DateUtil.java
    ├── ValidationUtil.java
    └── EmailUtil.java
```

---

## 💾 TRIỂN KHAI DAO LAYER

### Example: MemberDAO.java

```java
package DAO;

import model.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    
    // CREATE
    public boolean createMember(Member member) {
        String sql = "INSERT INTO members (user_id, member_code, height, weight, " +
                    "fitness_goal, emergency_contact_name, emergency_contact_phone, " +
                    "emergency_contact_relation, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, member.getUserId());
            ps.setString(2, member.getMemberCode());
            ps.setDouble(3, member.getHeight());
            ps.setDouble(4, member.getWeight());
            ps.setString(5, member.getFitnessGoal());
            ps.setString(6, member.getEmergencyContactName());
            ps.setString(7, member.getEmergencyContactPhone());
            ps.setString(8, member.getEmergencyContactRelation());
            ps.setString(9, member.getStatus());
            
            int affected = ps.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    member.setMemberId(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // READ - Get by ID
    public Member getMemberById(int memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractMemberFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // READ - Get by User ID
    public Member getMemberByUserId(int userId) {
        String sql = "SELECT * FROM members WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractMemberFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // READ - Get all members
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY registration_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }
    
    // READ - Get active members
    public List<Member> getActiveMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE status = 'active' " +
                    "ORDER BY registration_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }
    
    // UPDATE
    public boolean updateMember(Member member) {
        String sql = "UPDATE members SET height = ?, weight = ?, bmi = ?, " +
                    "fitness_goal = ?, target_weight = ?, " +
                    "emergency_contact_name = ?, emergency_contact_phone = ?, " +
                    "emergency_contact_relation = ?, emergency_contact_address = ?, " +
                    "assigned_coach_id = ?, status = ?, " +
                    "allergies = ?, injuries = ?, notes = ?, " +
                    "updated_at = CURRENT_TIMESTAMP " +
                    "WHERE member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, member.getHeight());
            ps.setDouble(2, member.getWeight());
            ps.setDouble(3, member.getBmi());
            ps.setString(4, member.getFitnessGoal());
            ps.setDouble(5, member.getTargetWeight());
            ps.setString(6, member.getEmergencyContactName());
            ps.setString(7, member.getEmergencyContactPhone());
            ps.setString(8, member.getEmergencyContactRelation());
            ps.setString(9, member.getEmergencyContactAddress());
            ps.setObject(10, member.getAssignedCoachId());
            ps.setString(11, member.getStatus());
            ps.setString(12, member.getAllergies());
            ps.setString(13, member.getInjuries());
            ps.setString(14, member.getNotes());
            ps.setInt(15, member.getMemberId());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // DELETE (Soft delete)
    public boolean deleteMember(int memberId) {
        String sql = "UPDATE members SET status = 'inactive', " +
                    "updated_at = CURRENT_TIMESTAMP WHERE member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, memberId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // UPDATE - Increment workout session count
    public boolean incrementWorkoutSession(int memberId) {
        String sql = "UPDATE members SET " +
                    "total_workout_sessions = total_workout_sessions + 1, " +
                    "last_workout_date = CURRENT_TIMESTAMP " +
                    "WHERE member_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, memberId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // SEARCH - Search members by name or email
    public List<Member> searchMembers(String keyword) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT m.* FROM members m " +
                    "JOIN users u ON m.user_id = u.user_id " +
                    "WHERE u.full_name LIKE ? OR u.email LIKE ? " +
                    "ORDER BY m.registration_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }
    
    // Helper method to extract Member from ResultSet
    private Member extractMemberFromResultSet(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setMemberId(rs.getInt("member_id"));
        member.setUserId(rs.getInt("user_id"));
        member.setMemberCode(rs.getString("member_code"));
        member.setRegistrationDate(rs.getDate("registration_date"));
        member.setHeight(rs.getDouble("height"));
        member.setWeight(rs.getDouble("weight"));
        member.setBmi(rs.getDouble("bmi"));
        member.setBloodType(rs.getString("blood_type"));
        member.setMedicalConditions(rs.getString("medical_conditions"));
        member.setFitnessGoal(rs.getString("fitness_goal"));
        member.setTargetWeight(rs.getDouble("target_weight"));
        member.setActivityLevel(rs.getString("activity_level"));
        member.setMembershipPackageId(rs.getInt("membership_package_id"));
        member.setPackageStartDate(rs.getDate("package_start_date"));
        member.setPackageEndDate(rs.getDate("package_end_date"));
        member.setEmergencyContactName(rs.getString("emergency_contact_name"));
        member.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        member.setEmergencyContactRelation(rs.getString("emergency_contact_relation"));
        member.setEmergencyContactAddress(rs.getString("emergency_contact_address"));
        member.setAssignedCoachId(rs.getInt("assigned_coach_id"));
        member.setPreferredTrainingTime(rs.getString("preferred_training_time"));
        member.setTotalWorkoutSessions(rs.getInt("total_workout_sessions"));
        member.setCurrentStreak(rs.getInt("current_streak"));
        member.setLongestStreak(rs.getInt("longest_streak"));
        member.setLastWorkoutDate(rs.getDate("last_workout_date"));
        member.setAllergies(rs.getString("allergies"));
        member.setInjuries(rs.getString("injuries"));
        member.setNotes(rs.getString("notes"));
        member.setStatus(rs.getString("status"));
        member.setCreatedAt(rs.getTimestamp("created_at"));
        member.setUpdatedAt(rs.getTimestamp("updated_at"));
        return member;
    }
}
```

---

## 🎯 TRIỂN KHAI SERVICE LAYER

### Example: MemberService.java

```java
package service;

import DAO.MemberDAO;
import DAO.UserDAO;
import model.Member;
import model.User;
import java.util.List;

public class MemberService {
    private MemberDAO memberDAO;
    private UserDAO userDAO;
    
    public MemberService() {
        this.memberDAO = new MemberDAO();
        this.userDAO = new UserDAO();
    }
    
    /**
     * Đăng ký member mới
     */
    public boolean registerMember(User user, Member member) {
        try {
            // 1. Tạo User account trước
            if (userDAO.createUser(user)) {
                // 2. Lấy user_id vừa tạo
                User createdUser = userDAO.getUserByUsername(user.getUsername());
                
                if (createdUser != null) {
                    // 3. Tạo Member với user_id
                    member.setUserId(createdUser.getId());
                    
                    // 4. Generate member code
                    member.setMemberCode(generateMemberCode());
                    
                    // 5. Tính BMI nếu có height và weight
                    if (member.getHeight() != null && member.getWeight() != null) {
                        member.calculateBMI();
                    }
                    
                    return memberDAO.createMember(member);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cập nhật thông tin member
     */
    public boolean updateMemberProfile(Member member, User user) {
        try {
            // 1. Cập nhật User info
            boolean userUpdated = userDAO.updateUser(user);
            
            // 2. Tính lại BMI
            if (member.getHeight() != null && member.getWeight() != null) {
                member.calculateBMI();
            }
            
            // 3. Cập nhật Member info
            boolean memberUpdated = memberDAO.updateMember(member);
            
            return userUpdated && memberUpdated;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Lấy thông tin đầy đủ của member (bao gồm User info)
     */
    public MemberWithUser getMemberFullInfo(int memberId) {
        Member member = memberDAO.getMemberById(memberId);
        if (member != null) {
            User user = userDAO.getUserById(member.getUserId());
            return new MemberWithUser(member, user);
        }
        return null;
    }
    
    /**
     * Assign coach cho member
     */
    public boolean assignCoach(int memberId, int coachId) {
        Member member = memberDAO.getMemberById(memberId);
        if (member != null) {
            member.setAssignedCoachId(coachId);
            return memberDAO.updateMember(member);
        }
        return false;
    }
    
    /**
     * Kiểm tra gói membership còn hạn không
     */
    public boolean isMembershipActive(int memberId) {
        Member member = memberDAO.getMemberById(memberId);
        return member != null && member.isPackageActive();
    }
    
    /**
     * Lấy danh sách members theo coach
     */
    public List<Member> getMembersByCoach(int coachId) {
        return memberDAO.getMembersByCoachId(coachId);
    }
    
    /**
     * Search members
     */
    public List<Member> searchMembers(String keyword) {
        return memberDAO.searchMembers(keyword);
    }
    
    /**
     * Get member statistics
     */
    public MemberStatistics getMemberStatistics(int memberId) {
        Member member = memberDAO.getMemberById(memberId);
        if (member != null) {
            MemberStatistics stats = new MemberStatistics();
            stats.setTotalSessions(member.getTotalWorkoutSessions());
            stats.setCurrentStreak(member.getCurrentStreak());
            stats.setLongestStreak(member.getLongestStreak());
            stats.setBmi(member.getBmi());
            stats.setBmiCategory(member.getBMICategory());
            // Calculate more statistics...
            return stats;
        }
        return null;
    }
    
    /**
     * Generate unique member code
     */
    private String generateMemberCode() {
        // Format: GYM + YYYY + sequential number
        // Example: GYM2024001
        int year = java.time.Year.now().getValue();
        int lastNumber = memberDAO.getLastMemberNumber();
        return String.format("GYM%d%03d", year, lastNumber + 1);
    }
    
    // Inner class for returning member with user info
    public static class MemberWithUser {
        private Member member;
        private User user;
        
        public MemberWithUser(Member member, User user) {
            this.member = member;
            this.user = user;
        }
        
        public Member getMember() { return member; }
        public User getUser() { return user; }
    }
    
    // Inner class for member statistics
    public static class MemberStatistics {
        private Integer totalSessions;
        private Integer currentStreak;
        private Integer longestStreak;
        private Double bmi;
        private String bmiCategory;
        // Add more fields as needed
        
        // Getters and setters...
    }
}
```

---

## 🌐 TRIỂN KHAI CONTROLLER

### Example: MemberController.java (Servlet)

```java
package controller;

import model.Member;
import model.User;
import service.MemberService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MemberController", urlPatterns = {"/member/*"})
public class MemberController extends HttpServlet {
    private MemberService memberService;
    
    @Override
    public void init() {
        memberService = new MemberService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // List all members
            listMembers(request, response);
        } else if (pathInfo.startsWith("/view/")) {
            // View member detail
            viewMember(request, response);
        } else if (pathInfo.equals("/new")) {
            // Show registration form
            request.getRequestDispatcher("/views/member/register.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/edit/")) {
            // Show edit form
            showEditForm(request, response);
        } else if (pathInfo.startsWith("/delete/")) {
            // Delete member
            deleteMember(request, response);
        } else if (pathInfo.equals("/search")) {
            // Search members
            searchMembers(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo.equals("/register")) {
            registerMember(request, response);
        } else if (pathInfo.equals("/update")) {
            updateMember(request, response);
        }
    }
    
    private void listMembers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Member> members = memberService.getAllActiveMembers();
        request.setAttribute("members", members);
        request.getRequestDispatcher("/views/admin/member-list.jsp").forward(request, response);
    }
    
    private void viewMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] pathParts = request.getPathInfo().split("/");
        int memberId = Integer.parseInt(pathParts[2]);
        
        MemberService.MemberWithUser memberInfo = memberService.getMemberFullInfo(memberId);
        
        if (memberInfo != null) {
            request.setAttribute("member", memberInfo.getMember());
            request.setAttribute("user", memberInfo.getUser());
            request.getRequestDispatcher("/views/member/profile.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Member not found");
        }
    }
    
    private void registerMember(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get user info
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password")); // Remember to hash!
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setRole("member");
        
        // Get member info
        Member member = new Member();
        member.setHeight(Double.parseDouble(request.getParameter("height")));
        member.setWeight(Double.parseDouble(request.getParameter("weight")));
        member.setFitnessGoal(request.getParameter("fitnessGoal"));
        member.setEmergencyContactName(request.getParameter("emergencyContactName"));
        member.setEmergencyContactPhone(request.getParameter("emergencyContactPhone"));
        member.setEmergencyContactRelation(request.getParameter("emergencyContactRelation"));
        
        boolean success = memberService.registerMember(user, member);
        
        if (success) {
            request.getSession().setAttribute("message", "Registration successful!");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
        }
    }
    
    private void searchMembers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("q");
        List<Member> members = memberService.searchMembers(keyword);
        request.setAttribute("members", members);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/views/admin/member-list.jsp").forward(request, response);
    }
}
```

---

## 🧪 TESTING

### Example: MemberServiceTest.java

```java
package service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import model.Member;
import model.User;

public class MemberServiceTest {
    private static MemberService memberService;
    
    @BeforeAll
    public static void setUp() {
        memberService = new MemberService();
    }
    
    @Test
    public void testRegisterMember() {
        User user = new User("testuser", "password123", "Test User", 
                            "test@email.com", "member");
        Member member = new Member();
        member.setHeight(175.0);
        member.setWeight(70.0);
        member.setFitnessGoal("maintain");
        
        boolean result = memberService.registerMember(user, member);
        assertTrue(result, "Member registration should succeed");
    }
    
    @Test
    public void testBMICalculation() {
        Member member = new Member();
        member.setHeight(175.0); // 1.75m
        member.setWeight(70.0);  // 70kg
        
        // BMI should be around 22.86
        assertNotNull(member.getBmi());
        assertTrue(member.getBmi() > 22 && member.getBmi() < 23);
        assertEquals("Normal", member.getBMICategory());
    }
}
```

---

## 📝 NOTES

### Best Practices:
1. **Always use PreparedStatement** để tránh SQL Injection
2. **Hash passwords** trước khi lưu database
3. **Validate input** ở cả client và server side
4. **Use transactions** cho các operations phức tạp
5. **Implement proper error handling** và logging
6. **Close database connections** properly (use try-with-resources)

### Security Checklist:
- ✅ Password hashing (BCrypt)
- ✅ Input validation
- ✅ SQL injection prevention
- ✅ XSS protection
- ✅ CSRF tokens
- ✅ Session management
- ✅ Role-based access control

---

📝 **Document version**: 1.0  
📅 **Last updated**: 2024-10-10

