package com.gym.service;

import com.gym.dao.UserDAO;
import com.gym.dao.nutrition.NutritionGoalDao;
import com.gym.model.User;
import com.gym.model.Student;
import com.gym.model.NutritionGoal;
import com.gym.service.nutrition.NutritionService;
import com.gym.service.nutrition.NutritionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * UserService - Service layer for user business logic
 * Provides operations for user management (CRUD)
 * For student-specific information, delegates to StudentService
 * All business logic should be here, not in Servlet
 */
public class UserService {
    
    private final UserDAO userDAO;
    private final StudentService studentService;
    private final NutritionGoalDao nutritionGoalDao;
    private final NutritionService nutritionService;
    private final com.gym.service.membership.MembershipService membershipService;
    private final com.gym.dao.StudentDAO studentDAO;
    private final PasswordService passwordService;
    
    public UserService() {
        this.userDAO = new UserDAO();
        this.studentService = new StudentService();
        this.nutritionGoalDao = new NutritionGoalDao();
        this.nutritionService = new NutritionServiceImpl();
        this.membershipService = new com.gym.service.membership.MembershipServiceImpl();
        this.studentDAO = new com.gym.dao.StudentDAO();
        this.passwordService = new PasswordService();
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(long userId) {
        if (userId <= 0) {
            return null;
        }
        return userDAO.getUserById(userId);
    }
    
    /**
     * Update user information
     * Validates data before updating
     */
    public boolean updateUser(User user) {
        if (user == null || user.getUserId() == null || user.getUserId() <= 0) {
            return false;
        }
        
        // Validate required fields
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return false;
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return false;
        }
        
        // Validate email format (basic check)
        if (!isValidEmail(user.getEmail())) {
            return false;
        }
        
        return userDAO.updateUser(user);
    }
    
    /**
     * Delete user (soft delete)
     */
    public boolean deleteUser(long userId) {
        if (userId <= 0) {
            return false;
        }
        return userDAO.deleteUser(userId);
    }
    
    /**
     * Hard delete user (permanent removal)
     * Use with caution!
     */
    public boolean hardDeleteUser(long userId) {
        if (userId <= 0) {
            return false;
        }
        return userDAO.hardDeleteUser(userId);
    }
    
    /**
     * Check if username exists
     */
    public boolean usernameExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userDAO.existsByUsername(username);
    }
    
    /**
     * Check if email exists
     */
    public boolean emailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userDAO.existsByEmail(email);
    }
    
    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return userDAO.findByUsername(username);
    }
    
    /**
     * Get user by email
     */
    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return userDAO.findByEmail(email);
    }
    
    // ==================== ADMIN CRUD METHODS ====================
    
    /**
     * Get all users (for admin management)
     * ✅ Tái sử dụng UserDAO.findAll() từ GenericDAO
     */
    public List<User> getAllUsers() {
        try {
            return userDAO.findAll();
        } catch (Exception e) {
            System.err.println("[UserService] Error getting all users: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Search users with pagination (for admin management)
     * ✅ Tái sử dụng GenericDAO với custom JPQL
     */
    public List<User> searchUsers(String keyword, int page, int pageSize) {
        try {
            return userDAO.searchUsers(keyword, page, pageSize);
        } catch (Exception e) {
            System.err.println("[UserService] Error searching users: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Count users matching search criteria
     */
    public int countUsers(String keyword) {
        try {
            return userDAO.countUsers(keyword);
        } catch (Exception e) {
            System.err.println("[UserService] Error counting users: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Create new user (for admin)
     * ✅ Auto-create Student record when role=USER (JPA inheritance)
     */
    public boolean createUser(String username, String name, String email, String phone,
                              String password, String role, String status) {
        try {
            // Check duplicates
            if (usernameExists(username)) {
                System.err.println("[UserService] Username already exists: " + username);
                return false;
            }
            
            if (emailExists(email)) {
                System.err.println("[UserService] Email already exists: " + email);
                return false;
            }
            
            // Hash password
            String passwordHash = passwordService.hashPassword(password);
            
            // Create User (or Student if role is USER)
            User user;
            if ("USER".equalsIgnoreCase(role)) {
                // ✅ Create Student directly to leverage JPA JOINED inheritance
                Student student = new Student();
                student.setUsername(username);
                student.setName(name);
                student.setEmail(email);
                student.setPhone(phone);
                student.setPassword(passwordHash);
                student.setRole(role);
                student.setStatus(status != null ? status : "ACTIVE");
                student.setCreatedDate(java.time.LocalDateTime.now());
                user = student;
            } else {
                // For ADMIN, PT, etc. - create regular User
                user = new User();
                user.setUsername(username);
                user.setName(name);
                user.setEmail(email);
                user.setPhone(phone);
                user.setPassword(passwordHash);
                user.setRole(role);
                user.setStatus(status != null ? status : "ACTIVE");
                user.setCreatedDate(java.time.LocalDateTime.now());
            }
            
            // ✅ Tái sử dụng UserDAO.save() từ GenericDAO
            int userId = userDAO.save(user);
            
            if (userId > 0) {
                System.out.println("[UserService] Created user: " + username + 
                                 " (ID: " + userId + ", Role: " + role + ")");
                if ("USER".equalsIgnoreCase(role)) {
                    System.out.println("[UserService] Student record auto-created via JPA inheritance");
                }
                return true;
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("[UserService] Error creating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update existing user with role change handling (for admin)
     * ✅ Tái sử dụng UserDAO.update() từ GenericDAO
     */
    public boolean updateUserAdmin(Integer userId, String username, String name, String email, 
                                    String phone, String role, String status, String newPassword) {
        try {
            User user = getUserById(userId);
            if (user == null) {
                System.err.println("[UserService] User not found: " + userId);
                return false;
            }
            
            String oldRole = user.getRole();
            
            // Update basic info
            user.setUsername(username);
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setRole(role);
            user.setStatus(status);
            user.setLastUpdate(java.time.LocalDateTime.now());
            
            // Update password if provided
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                String passwordHash = passwordService.hashPassword(newPassword);
                user.setPassword(passwordHash);
            }
            
            // ✅ Tái sử dụng UserDAO.update() từ GenericDAO
            userDAO.update(user);
            
            // ✅ Handle role change: USER role requires Student record
            if ("USER".equalsIgnoreCase(role) && !"USER".equalsIgnoreCase(oldRole)) {
                java.util.Optional<Student> existingStudentOpt = studentDAO.findByUserId(userId);
                if (!existingStudentOpt.isPresent()) {
                    Student student = new Student();
                    student.setUserId(userId);
                    studentDAO.upsert(student);
                    System.out.println("[UserService] Created Student record for user_id: " + userId);
                }
            }
            
            System.out.println("[UserService] Updated user: " + userId);
            return true;
        } catch (Exception e) {
            System.err.println("[UserService] Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Calculate and get BMI category (delegates to StudentService)
     */
    public String getBMICategory(BigDecimal bmi) {
        return studentService.getBMICategory(bmi);
    }
    
    /**
     * Validate email format (basic validation)
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    /**
     * Get dashboard data for a user
     */
    public Map<String, Object> getDashboardData(long userId) {
        User user = getUserById(userId);
        if (user == null) {
            System.err.println("[UserService] getDashboardData - User not found for userId: " + userId);
            return null;
        }
        
        System.out.println("[UserService] getDashboardData - User loaded: " + user.getClass().getSimpleName() + 
                         " (ID: " + user.getUserId() + ")");
        
        // Get student profile for student-specific data
        // IMPORTANT: With JOINED inheritance, user might already be a Student instance
        // If not, try to load Student separately
        Student student = null;
        try {
            // Check if user is already a Student instance
            if (user instanceof Student) {
                student = (Student) user;
                System.out.println("[UserService] getDashboardData - User is already a Student instance");
            } else {
                // User is not a Student, try to load Student profile
                student = studentService.getStudentByUserId((int) userId);
                System.out.println("[UserService] getDashboardData - Student loaded separately: " + (student != null ? "YES" : "NO"));
            }
        } catch (Exception e) {
            System.err.println("[UserService] Error loading Student for dashboard: " + e.getMessage());
            e.printStackTrace();
            student = null; // Will handle null case below
        }
        
        Map<String, Object> dashboardData = new HashMap<>();
        
        // Basic member info
        // ALWAYS prioritize name (full name) over username for display
        // This ensures "Xin chào, [Full Name]" instead of "Xin chào, [Username]"
        String displayName;
        if (user.getName() != null && !user.getName().trim().isEmpty()) {
            displayName = user.getName(); // Use full name if available
        } else {
            // Fallback to username only if name is not set (for backward compatibility)
            displayName = (user.getUsername() != null ? user.getUsername() : "Member");
        }
        dashboardData.put("memberName", displayName);
        dashboardData.put("username", user.getUsername()); // Also include username for reference
        dashboardData.put("packageType", user.getStatus() != null ? user.getStatus() : "ACTIVE");
        dashboardData.put("avatarUrl", user.getAvatarUrl()); // Get from User table
        java.time.LocalDateTime createdDate = user.getCreatedDate();
        dashboardData.put("joinDate", createdDate != null ? 
            java.sql.Timestamp.valueOf(createdDate) : new java.util.Date());
        
        // Stats
        Map<String, Object> stats = new HashMap<>();
        stats.put("streak", 0); // Not tracked yet
        
        // Get today's calories consumed from nutrition service
        BigDecimal caloriesConsumed = BigDecimal.ZERO;
        try {
            com.gym.model.DailyIntakeDTO todayTotals = nutritionService.todayTotals(userId);
            if (todayTotals != null && todayTotals.getCaloriesKcal() != null) {
                caloriesConsumed = todayTotals.getCaloriesKcal();
            }
        } catch (Exception e) {
            System.err.println("[UserService] Error getting today's calories: " + e.getMessage());
        }
        stats.put("caloriesConsumed", caloriesConsumed);
        stats.put("waterIntake", null); // Not tracked yet
        
        // Get membership info and calculate remaining days
        // Try to get active membership first, if not found, get the most recent one from history
        String packageRemaining = null;
        String packageName = null;
        try {
            System.out.println("[UserService] Getting membership for userId: " + userId);
            Integer userIdInt = (int) userId;
            
            // First, try to get current active membership
            java.util.Optional<com.gym.model.membership.Membership> membershipOpt = 
                membershipService.getCurrentMembership(userIdInt);
            
            System.out.println("[UserService] Active membership isPresent: " + membershipOpt.isPresent());
            
            // If no active membership, try to get the most recent one from history
            // (in case membership exists but is expired or has different status)
            if (!membershipOpt.isPresent()) {
                System.out.println("[UserService] No active membership found, checking membership history...");
                java.util.List<com.gym.model.membership.Membership> membershipHistory = 
                    membershipService.getMembershipHistory(userIdInt);
                
                if (membershipHistory != null && !membershipHistory.isEmpty()) {
                    // Get the most recent membership (first in list since it's ordered by created_date DESC)
                    com.gym.model.membership.Membership recentMembership = membershipHistory.get(0);
                    System.out.println("[UserService] Found recent membership ID: " + recentMembership.getMembershipId() + 
                                     ", Status: " + recentMembership.getStatus() + 
                                     ", End Date: " + recentMembership.getEndDate());
                    
                    // Use recent membership if it exists (even if expired)
                    membershipOpt = java.util.Optional.of(recentMembership);
                }
            }
            
            if (membershipOpt.isPresent()) {
                com.gym.model.membership.Membership membership = membershipOpt.get();
                System.out.println("[UserService] Using membership ID: " + membership.getMembershipId() + 
                                 ", Package ID: " + membership.getPackageId() + 
                                 ", Status: " + membership.getStatus());
                
                // Get package name
                packageName = membership.getPackageName();
                
                if (packageName == null || packageName.isEmpty()) {
                    // Try to get from package table
                    java.util.Optional<com.gym.model.membership.Package> packageOpt = 
                        membershipService.getPackageById(membership.getPackageId());
                    if (packageOpt.isPresent()) {
                        packageName = packageOpt.get().getName();
                    }
                }
                
                // Calculate remaining days
                if (membership.getEndDate() != null) {
                    java.time.LocalDate endDate = membership.getEndDate();
                    java.time.LocalDate today = java.time.LocalDate.now();
                    
                    if (endDate.isAfter(today) || endDate.isEqual(today)) {
                        long daysRemaining = java.time.temporal.ChronoUnit.DAYS.between(today, endDate);
                        packageRemaining = daysRemaining + " ngày";
                    } else {
                        packageRemaining = "Đã hết hạn";
                    }
                }
                
                System.out.println("[UserService] Final - Package: " + packageName + ", Remaining: " + packageRemaining);
            } else {
                System.out.println("[UserService] No membership found for userId: " + userId);
            }
        } catch (Exception e) {
            System.err.println("[UserService] Error getting membership for userId " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        stats.put("packageRemaining", packageRemaining);
        dashboardData.put("packageName", packageName);
        
        // Handle BMI from Student (if Student exists)
        // IMPORTANT: Student may be null, so check before accessing
        if (student != null && student.getUserId() != null) {
            try {
                // Convert Float to BigDecimal for BMI
                BigDecimal bmi = student.getBmiAsBigDecimal();
                stats.put("bmi", bmi);
                if (bmi != null) {
                    stats.put("bmiCategory", studentService.getBMICategory(bmi));
                } else {
                    stats.put("bmi", null);
                    stats.put("bmiCategory", null);
                }
            } catch (Exception e) {
                System.err.println("[UserService] Error getting BMI from Student: " + e.getMessage());
                stats.put("bmi", null);
                stats.put("bmiCategory", null);
            }
        } else {
            // Student not found or not a Student - set BMI to null
            stats.put("bmi", null);
            stats.put("bmiCategory", null);
        }
        
        dashboardData.put("stats", stats);
        
        // Recent activities
        List<Map<String, Object>> recentActivities = new ArrayList<>();
        if (user.getCreatedDate() != null) {
            java.sql.Timestamp createdTimestamp = java.sql.Timestamp.valueOf(user.getCreatedDate());
            recentActivities.add(createActivity("Tham gia GymFit", "System", formatTimeAgo(createdTimestamp)));
        }
        dashboardData.put("recentActivities", recentActivities);
        
        // Upcoming sessions (empty until schedule feature exists)
        dashboardData.put("upcomingSessions", new ArrayList<>());
        
        return dashboardData;
    }
    
    /**
     * Get profile data for a user (combines User and Student data)
     * NOTE: Student table only has: weight, height, bmi, emergency_contact_*
     * Other fields (fullName, email, phone, address, avatarUrl, gender) come from User table
     * Converts LocalDateTime/LocalDate to Date/Timestamp for JSP compatibility
     */
    public Map<String, Object> getProfileData(long userId) {
        System.out.println("[UserService] getProfileData - Starting for userId: " + userId);
        
        User user = getUserById(userId);
        if (user == null) {
            System.err.println("[UserService] getProfileData - User not found for userId: " + userId);
            return null;
        }
        
        System.out.println("[UserService] getProfileData - User loaded: " + user.getUsername() + " (ID: " + user.getUserId() + ")");
        System.out.println("[UserService] getProfileData - User name: " + user.getName());
        System.out.println("[UserService] getProfileData - User email: " + user.getEmail());
        
        // Get student profile for student-specific data (weight, height, bmi, emergency_contact)
        // Use StudentService which handles DAO lifecycle correctly
        Student student = null;
        try {
            student = studentService.getStudentByUserId((int) userId);
            System.out.println("[UserService] getProfileData - Student loaded: " + (student != null ? "YES" : "NO"));
            if (student != null) {
                System.out.println("[UserService] getProfileData - Student userId: " + student.getUserId());
                System.out.println("[UserService] getProfileData - Student height: " + student.getHeight());
                System.out.println("[UserService] getProfileData - Student weight: " + student.getWeight());
            }
        } catch (Exception e) {
            System.err.println("[UserService] Error loading Student: " + e.getMessage());
            e.printStackTrace();
            student = null; // Will be handled below
        }
        
        Map<String, Object> profileData = new HashMap<>();
        
        // Basic Info from User table
        profileData.put("username", user.getUsername());
        profileData.put("email", user.getEmail());
        profileData.put("status", user.getStatus());
        
        // Convert LocalDateTime to Timestamp for JSP fmt:formatDate
        if (user.getCreatedDate() != null) {
            profileData.put("createdDate", java.sql.Timestamp.valueOf(user.getCreatedDate()));
        } else {
            profileData.put("createdDate", null);
        }
        
        // Extended info from User table
        // Use name (full name) if available, otherwise use username as fallback
        profileData.put("fullName", (user.getName() != null && !user.getName().trim().isEmpty()) 
                                   ? user.getName() 
                                   : user.getUsername());
        profileData.put("name", user.getName()); // Store name separately
        profileData.put("avatarUrl", user.getAvatarUrl()); // Get from User table
        profileData.put("phone", user.getPhone()); // Get phone from User table
        
        // Convert LocalDate to java.sql.Date for JSP fmt:formatDate
        if (user.getDob() != null) {
            profileData.put("dob", java.sql.Date.valueOf(user.getDob()));
        } else {
            profileData.put("dob", null);
        }
        
        profileData.put("address", user.getAddress()); // Get address from User table
        profileData.put("gender", user.getGender()); // Get gender from User table
        
        // Physical Info from Student table (convert Float to BigDecimal)
        // IMPORTANT: Load all data from Student entity BEFORE closing EntityManager
        if (student != null && student.getUserId() != null) {
            try {
                // Get values directly from Student entity (values are already loaded)
                Float heightFloat = student.getHeight();
                Float weightFloat = student.getWeight();
                Float bmiFloat = student.getBmi();
                
                // Convert to BigDecimal for JSP display
                profileData.put("height", heightFloat != null ? BigDecimal.valueOf(heightFloat) : null);
                profileData.put("weight", weightFloat != null ? BigDecimal.valueOf(weightFloat) : null);
                profileData.put("bmi", bmiFloat != null ? BigDecimal.valueOf(bmiFloat) : null);
                
                // Calculate BMI category
                BigDecimal bmi = bmiFloat != null ? BigDecimal.valueOf(bmiFloat) : null;
                String bmiCategory = bmi != null ? studentService.getBMICategory(bmi) : null;
                profileData.put("bmiCategory", bmiCategory);
                if (bmiCategory != null) {
                    profileData.put("bmiCategoryClass", bmiCategory.toLowerCase().replace(" ", ""));
                } else {
                    profileData.put("bmiCategoryClass", null);
                }
                
                // Emergency Contact from Student table
                profileData.put("emergencyContactName", student.getEmergencyContactName());
                profileData.put("emergencyContactPhone", student.getEmergencyContactPhone());
                profileData.put("emergencyContactRelation", student.getEmergencyContactRelation());
                profileData.put("emergencyContactAddress", student.getEmergencyContactAddress());
                
                System.out.println("[UserService] getProfileData - Student data loaded: height=" + heightFloat + ", weight=" + weightFloat);
            } catch (Exception e) {
                System.err.println("[UserService] Error extracting Student data: " + e.getMessage());
                e.printStackTrace();
                // Set to null on error
                profileData.put("height", null);
                profileData.put("weight", null);
                profileData.put("bmi", null);
                profileData.put("bmiCategory", null);
                profileData.put("bmiCategoryClass", null);
                profileData.put("emergencyContactName", null);
                profileData.put("emergencyContactPhone", null);
                profileData.put("emergencyContactRelation", null);
                profileData.put("emergencyContactAddress", null);
            }
        } else {
            // If Student not found, set all Student fields to null
            System.out.println("[UserService] getProfileData - Student is null, setting all Student fields to null");
            profileData.put("height", null);
            profileData.put("weight", null);
            profileData.put("bmi", null);
            profileData.put("bmiCategory", null);
            profileData.put("bmiCategoryClass", null);
            profileData.put("emergencyContactName", null);
            profileData.put("emergencyContactPhone", null);
            profileData.put("emergencyContactRelation", null);
            profileData.put("emergencyContactAddress", null);
        }
        
        // Debug: Log final profileData
        System.out.println("[UserService] getProfileData - Final profileData size: " + profileData.size());
        System.out.println("[UserService] getProfileData - Keys: " + profileData.keySet());
        
        return profileData;
    }
    
    /**
     * Update user profile from request parameters
     * Updates both User (if username/email changed) and Student profile
     */
    public boolean updateProfileFromRequest(User user, HttpServletRequest request) {
        if (user == null || request == null) {
            return false;
        }
        
        // Update User fields: name, phone, dob, address, gender
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String dobStr = request.getParameter("dob");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");
        
        if (name != null) {
            user.setName(name.trim());
        }
        if (phone != null) {
            user.setPhone(phone.trim().isEmpty() ? null : phone.trim());
        }
        if (dobStr != null && !dobStr.trim().isEmpty()) {
            try {
                java.time.LocalDate dob = java.time.LocalDate.parse(dobStr);
                user.setDob(dob);
            } catch (Exception e) {
                System.err.println("[UserService] Invalid date format for dob: " + dobStr);
            }
        } else {
            user.setDob(null);
        }
        if (address != null) {
            user.setAddress(address.trim().isEmpty() ? null : address.trim());
        }
        if (gender != null && !gender.trim().isEmpty()) {
            user.setGender(gender.trim());
        } else {
            user.setGender(null);
        }
        
        // Update User in database
        boolean userUpdated = userDAO.updateUser(user);
        if (!userUpdated) {
            System.err.println("[UserService] Error updating user profile");
            return false;
        }
        
        // Get or create student profile
        Integer userId = user.getUserId();
        if (userId == null) {
            System.err.println("[UserService] Error: user.getUserId() returned null");
            return false;
        }
        
        System.out.println("[UserService] updateProfileFromRequest - userId: " + userId);
        
        try {
            // Get student profile - this will return existing Student or create new one with userId set
            Student student = studentService.getStudentByUserId(userId);
            if (student == null) {
                System.err.println("[UserService] Error: getProfile() returned null for userId: " + userId);
                return false;
            }
            
            System.out.println("[UserService] updateProfileFromRequest - Student loaded: " + (student.getUserId() != null ? "YES (userId: " + student.getUserId() + ")" : "NO"));
            
            // Ensure userId is set (in case getProfile returned new Student)
            if (student.getUserId() == null) {
                student.setUserIdAsInt(userId);
            }
            
            // Update student profile from request parameters
            System.out.println("[UserService] updateProfileFromRequest - Updating Student fields from request...");
            studentService.updateStudentFromRequest(student, request.getParameterMap());
            
            System.out.println("[UserService] updateProfileFromRequest - Student fields updated - Height: " + student.getHeight() + ", Weight: " + student.getWeight());
            
            // Save student profile
            System.out.println("[UserService] updateProfileFromRequest - Saving Student profile...");
            studentService.saveStudent(student);
            System.out.println("[UserService] updateProfileFromRequest - Student profile saved successfully");
            
            return true;
        } catch (Exception e) {
            System.err.println("[UserService] Error updating student profile: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update user profile from multipart request (handles file uploads)
     * Updates Student profile
     */
    public boolean updateProfileFromMultipartRequest(User user, HttpServletRequest request) {
        if (user == null || request == null) {
            return false;
        }
        
        try {
            // Update User fields: name, phone, dob, address, gender
            String name = getPartParameter(request, "name");
            String phone = getPartParameter(request, "phone");
            String dobStr = getPartParameter(request, "dob");
            String address = getPartParameter(request, "address");
            String gender = getPartParameter(request, "gender");
            
            if (name != null) {
                user.setName(name.trim());
            }
            if (phone != null) {
                user.setPhone(phone.trim().isEmpty() ? null : phone.trim());
            }
            if (dobStr != null && !dobStr.trim().isEmpty()) {
                try {
                    java.time.LocalDate dob = java.time.LocalDate.parse(dobStr);
                    user.setDob(dob);
                } catch (Exception e) {
                    System.err.println("[UserService] Invalid date format for dob: " + dobStr);
                }
            } else {
                user.setDob(null);
            }
            if (address != null) {
                user.setAddress(address.trim().isEmpty() ? null : address.trim());
            }
            if (gender != null && !gender.trim().isEmpty()) {
                user.setGender(gender.trim());
            } else {
                user.setGender(null);
            }
            
            // Handle avatar URL from form parameter or file upload
            // Avatar is stored in User table, not Student table
            String avatarUrlParam = getPartParameter(request, "avatarUrl");
            String avatarUrl = null;
            if (avatarUrlParam != null && !avatarUrlParam.trim().isEmpty()) {
                avatarUrl = avatarUrlParam.trim();
            } else {
                // Check for file upload
                Part avatarFilePart = request.getPart("avatarFile");
                if (avatarFilePart != null && avatarFilePart.getSize() > 0) {
                    String fileName = getFileName(avatarFilePart);
                    if (fileName != null && !fileName.isEmpty()) {
                        // File uploaded but no URL provided
                        // TODO: Implement file upload to server storage and generate URL
                        System.out.println("[UserService] File uploaded but no URL provided. Please provide URL or implement file storage.");
                        // Keep existing avatar from User table
                        avatarUrl = user.getAvatarUrl();
                    }
                } else {
                    // No URL provided and no file uploaded, keep existing avatar from User table
                    avatarUrl = user.getAvatarUrl();
                }
            }
            
            // Update avatar in User table
            if (avatarUrl != null) {
                user.setAvatarUrl(avatarUrl);
            }
            
            // Update User in database (includes name, phone, dob, address, avatar, gender)
            // Note: userDAO.updateUser() handles transaction and merge internally
            boolean userUpdated = userDAO.updateUser(user);
            if (!userUpdated) {
                System.err.println("[UserService] Error updating User table");
                return false;
            }
            
            System.out.println("[UserService] User info updated successfully - Name: " + user.getName() + ", Phone: " + user.getPhone());
            
            // Get or create student profile
            Integer userId = user.getUserId();
            if (userId == null) {
                System.err.println("[UserService] Error: user.getUserId() returned null in multipart request");
                return false;
            }
            
            System.out.println("[UserService] updateProfileFromMultipartRequest - userId: " + userId);
            
            try {
                // Get student profile - this will return existing Student or create new one with userId set
                Student student = studentService.getStudentByUserId(userId);
                if (student == null) {
                    System.err.println("[UserService] Error: getProfile() returned null for userId: " + userId);
                    return false;
                }
                
                System.out.println("[UserService] updateProfileFromMultipartRequest - Student loaded: " + (student.getUserId() != null ? "YES (userId: " + student.getUserId() + ")" : "NO"));
                
                // Ensure userId is set (in case getProfile returned new Student)
                if (student.getUserId() == null) {
                    student.setUserIdAsInt(userId);
                }
                
                // Update student profile from request parameters (weight, height, BMI, emergency contacts, etc.)
                System.out.println("[UserService] updateProfileFromMultipartRequest - Updating Student fields from request...");
                studentService.updateStudentFromRequest(student, request.getParameterMap());
                
                System.out.println("[UserService] updateProfileFromMultipartRequest - Student fields updated - Height: " + student.getHeight() + ", Weight: " + student.getWeight());
                
                // Save student profile
                System.out.println("[UserService] updateProfileFromMultipartRequest - Saving Student profile...");
                studentService.saveStudent(student);
                System.out.println("[UserService] updateProfileFromMultipartRequest - Student profile saved successfully");
                
                return true;
            } catch (Exception e) {
                System.err.println("[UserService] Error updating student profile from multipart: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            System.err.println("[UserService] Error updating profile from multipart request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Helper: Get parameter value from multipart request
     */
    private String getPartParameter(HttpServletRequest request, String paramName) {
        try {
            // Try to get as regular parameter first (some servers support this even with multipart)
            String value = request.getParameter(paramName);
            if (value != null) {
                return value;
            }
            
            // Try to get as Part
            Part part = request.getPart(paramName);
            if (part != null) {
                // Check if it's a file part or form field
                String contentType = part.getContentType();
                if (contentType == null || contentType.isEmpty()) {
                    // It's a form field, read the value
                    java.io.InputStream inputStream = part.getInputStream();
                    java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(inputStream, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (sb.length() > 0) {
                            sb.append("\n");
                        }
                        sb.append(line);
                    }
                    reader.close();
                    return sb.toString();
                }
            }
            
            return null;
        } catch (Exception e) {
            System.err.println("[UserService] Error getting part parameter '" + paramName + "': " + e.getMessage());
            // Fallback to regular parameter
            try {
                return request.getParameter(paramName);
            } catch (Exception ex) {
                return null;
            }
        }
    }
    
    /**
     * Helper: Get filename from Part
     */
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition != null) {
            String[] tokens = contentDisposition.split(";");
            for (String token : tokens) {
                if (token.trim().startsWith("filename")) {
                    return token.substring(token.indexOf("=") + 2, token.length() - 1);
                }
            }
        }
        return null;
    }
    
    /**
     * Update body metrics from request parameters
     */
    public boolean updateBodyMetricsFromRequest(User user, HttpServletRequest request) {
        if (user == null || request == null) {
            System.err.println("[UserService] updateBodyMetricsFromRequest: user or request is null");
            return false;
        }
        
        // Get or create student profile
        Integer userId = user.getUserId();
        if (userId == null) {
            System.err.println("[UserService] updateBodyMetricsFromRequest: userId is null");
            return false;
        }
        
        System.out.println("[UserService] updateBodyMetricsFromRequest: Starting for userId: " + userId);
        
        try {
            Student student = studentService.getStudentByUserId(userId);
            student.setUserIdAsInt(userId);
            
            System.out.println("[UserService] updateBodyMetricsFromRequest: Current student height: " + student.getHeight() + ", weight: " + student.getWeight());
            
            // Update height
            String heightStr = request.getParameter("height");
            if (heightStr != null && !heightStr.trim().isEmpty()) {
                try {
                    BigDecimal height = new BigDecimal(heightStr);
                    student.setHeightFromBigDecimal(height);
                    System.out.println("[UserService] updateBodyMetricsFromRequest: Set height: " + height);
                } catch (NumberFormatException e) {
                    System.err.println("[UserService] Invalid height format: " + heightStr);
                }
            } else {
                System.out.println("[UserService] updateBodyMetricsFromRequest: Height param is empty, keeping current value");
            }
            
            // Update weight
            String weightStr = request.getParameter("weight");
            if (weightStr != null && !weightStr.trim().isEmpty()) {
                try {
                    BigDecimal weight = new BigDecimal(weightStr);
                    student.setWeightFromBigDecimal(weight);
                    System.out.println("[UserService] updateBodyMetricsFromRequest: Set weight: " + weight);
                } catch (NumberFormatException e) {
                    System.err.println("[UserService] Invalid weight format: " + weightStr);
                }
            } else {
                System.out.println("[UserService] updateBodyMetricsFromRequest: Weight param is empty, keeping current value");
            }
            
            System.out.println("[UserService] updateBodyMetricsFromRequest: After update - height: " + student.getHeight() + ", weight: " + student.getWeight());
            
            // Save student profile
            System.out.println("[UserService] updateBodyMetricsFromRequest: Saving student profile...");
            studentService.saveStudent(student);
            System.out.println("[UserService] updateBodyMetricsFromRequest: Successfully saved student profile");
            return true;
        } catch (Exception e) {
            System.err.println("[UserService] Error saving body metrics: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update goals from request parameters
     */
    public boolean updateGoalsFromRequest(User user, HttpServletRequest request) {
        if (user == null || request == null) {
            System.err.println("[UserService] updateGoalsFromRequest: user or request is null");
            return false;
        }
        
        // Get or create nutrition goal
        Integer userId = user.getUserId();
        if (userId == null) {
            System.err.println("[UserService] updateGoalsFromRequest: userId is null");
            return false;
        }
        
        System.out.println("[UserService] updateGoalsFromRequest: Starting for userId: " + userId);
        
        try {
            Optional<NutritionGoal> goalOpt = nutritionGoalDao.findByUserId(userId.longValue());
            NutritionGoal goal = goalOpt.orElse(new NutritionGoal());
            goal.setUserId(userId.longValue());
            
            System.out.println("[UserService] updateGoalsFromRequest: Goal found: " + goalOpt.isPresent());
            
            // Update goal type
            String goalType = request.getParameter("goalType");
            if (goalType != null && !goalType.trim().isEmpty()) {
                goal.setGoalType(goalType);
                System.out.println("[UserService] updateGoalsFromRequest: Set goalType: " + goalType);
            }
            
            // Update activity factor
            String activityFactorStr = request.getParameter("activityFactor");
            if (activityFactorStr != null && !activityFactorStr.trim().isEmpty()) {
                try {
                    goal.setActivityFactor(new BigDecimal(activityFactorStr));
                    System.out.println("[UserService] updateGoalsFromRequest: Set activityFactor: " + activityFactorStr);
                } catch (NumberFormatException e) {
                    System.err.println("[UserService] Invalid activity factor: " + activityFactorStr);
                }
            }
            
            // Calculate and set calories target based on user's weight, height, gender, age, goal
            BigDecimal caloriesTarget = calculateCaloriesTarget(user, goal);
            goal.setDailyCaloriesTarget(caloriesTarget);
            System.out.println("[UserService] updateGoalsFromRequest: Calculated caloriesTarget: " + caloriesTarget);
            
            // Calculate protein target (typically 1.6-2.2g per kg body weight for athletes)
            // Get student profile for weight (userId already defined above)
            Student student = studentService.getStudentByUserId(userId);
            BigDecimal proteinTarget = null;
            BigDecimal weight = student.getWeightAsBigDecimal();
            if (weight != null) {
                // Use 1.8g per kg as default
                proteinTarget = weight.multiply(new BigDecimal("1.8")).setScale(2, RoundingMode.HALF_UP);
                goal.setDailyProteinTarget(proteinTarget);
                System.out.println("[UserService] updateGoalsFromRequest: Calculated proteinTarget: " + proteinTarget);
            } else {
                System.err.println("[UserService] updateGoalsFromRequest: Weight is null, cannot calculate protein target");
            }
            
            // Save nutrition goal
            System.out.println("[UserService] updateGoalsFromRequest: Saving nutrition goal...");
            boolean saved = nutritionGoalDao.saveOrUpdate(goal);
            System.out.println("[UserService] updateGoalsFromRequest: Save result: " + saved);
            return saved;
        } catch (Exception e) {
            System.err.println("[UserService] updateGoalsFromRequest: Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Calculate daily calories target based on user's body metrics and goal
     * Uses Harris-Benedict equation for BMR, then applies activity factor and goal adjustments
     */
    public BigDecimal calculateCaloriesTarget(User user, NutritionGoal goal) {
        // Get student profile for body metrics
        Integer userId = user.getUserId();
        if (userId == null) {
            return null;
        }
        Student student = studentService.getStudentByUserId(userId);
        
        BigDecimal weight = student.getWeightAsBigDecimal();
        BigDecimal height = student.getHeightAsBigDecimal(); // in cm
        
        if (weight == null || height == null) {
            return null;
        }
        
        String gender = student.getGender();
        
        // Estimate age if not available (default to 25)
        int age = 25; // You can add age field to User model later if needed
        
        // Calculate BMR using Harris-Benedict equation
        BigDecimal bmr;
        if ("Nam".equalsIgnoreCase(gender) || "Male".equalsIgnoreCase(gender)) {
            // Male: BMR = 88.362 + (13.397 × weight) + (4.799 × height) - (5.677 × age)
            bmr = new BigDecimal("88.362")
                .add(new BigDecimal("13.397").multiply(weight))
                .add(new BigDecimal("4.799").multiply(height))
                .subtract(new BigDecimal("5.677").multiply(new BigDecimal(age)));
        } else {
            // Female: BMR = 447.593 + (9.247 × weight) + (3.098 × height) - (4.330 × age)
            bmr = new BigDecimal("447.593")
                .add(new BigDecimal("9.247").multiply(weight))
                .add(new BigDecimal("3.098").multiply(height))
                .subtract(new BigDecimal("4.330").multiply(new BigDecimal(age)));
        }
        
        // Calculate TDEE (Total Daily Energy Expenditure) = BMR × Activity Factor
        BigDecimal activityFactor = goal.getActivityFactor() != null ? 
            goal.getActivityFactor() : new BigDecimal("1.55");
        BigDecimal tdee = bmr.multiply(activityFactor);
        
        // Apply goal adjustments
        BigDecimal caloriesTarget;
        String goalType = goal.getGoalType();
        if ("giam can".equalsIgnoreCase(goalType)) {
            // Deficit: TDEE - 500 kcal
            caloriesTarget = tdee.subtract(new BigDecimal("500"));
        } else if ("tang can".equalsIgnoreCase(goalType)) {
            // Surplus: TDEE + 500 kcal
            caloriesTarget = tdee.add(new BigDecimal("500"));
        } else {
            // "giu dang" (maintain): TDEE
            caloriesTarget = tdee;
        }
        
        // Ensure minimum calories (don't go below 1200)
        if (caloriesTarget.compareTo(new BigDecimal("1200")) < 0) {
            caloriesTarget = new BigDecimal("1200");
        }
        
        return caloriesTarget.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Get nutrition goal for a user
     */
    public Optional<NutritionGoal> getNutritionGoal(long userId) {
        return nutritionGoalDao.findByUserId(userId);
    }
    
    /**
     * Helper: Create activity map
     */
    private Map<String, Object> createActivity(String description, String type, String time) {
        Map<String, Object> map = new HashMap<>();
        map.put("description", description);
        map.put("type", type);
        map.put("time", time);
        return map;
    }
    
    /**
     * Helper: Format time ago from timestamp
     */
    private String formatTimeAgo(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        Date date = new Date(timestamp.getTime());
        long diffMs = System.currentTimeMillis() - date.getTime();
        long minutes = diffMs / (1000 * 60);
        if (minutes < 60) return minutes + " phút trước";
        long hours = minutes / 60;
        if (hours < 24) return hours + " giờ trước";
        long days = hours / 24;
        if (days < 30) return days + " ngày trước";
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }
}

