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
 * For student-specific information, delegates to StudentProfileService
 * All business logic should be here, not in Servlet
 */
public class UserService {
    
    private final UserDAO userDAO;
    private final StudentProfileService studentProfileService;
    private final NutritionGoalDao nutritionGoalDao;
    private final NutritionService nutritionService;
    private final com.gym.service.membership.MembershipService membershipService;
    
    public UserService() {
        this.userDAO = new UserDAO();
        this.studentProfileService = new StudentProfileService();
        this.nutritionGoalDao = new NutritionGoalDao();
        this.nutritionService = new NutritionServiceImpl();
        this.membershipService = new com.gym.service.membership.MembershipServiceImpl();
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
        if (user == null || user.getId() <= 0) {
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
    
    /**
     * Calculate and get BMI category (delegates to StudentProfileService)
     */
    public String getBMICategory(BigDecimal bmi) {
        return studentProfileService.getBMICategory(bmi);
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
            return null;
        }
        
        // Get student profile for student-specific data
        Student student = studentProfileService.getProfile((int) userId);
        
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
        Timestamp createdDate = user.getCreatedDate();
        dashboardData.put("joinDate", createdDate != null ? new Date(createdDate.getTime()) : new Date());
        
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
        stats.put("bmi", student.getBmi());
        if (student.getBmi() != null) {
            stats.put("bmiCategory", studentProfileService.getBMICategory(student.getBmi()));
        }
        dashboardData.put("stats", stats);
        
        // Recent activities
        List<Map<String, Object>> recentActivities = new ArrayList<>();
        if (user.getCreatedDate() != null) {
            recentActivities.add(createActivity("Tham gia GymFit", "System", formatTimeAgo(user.getCreatedDate())));
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
     */
    public Map<String, Object> getProfileData(long userId) {
        User user = getUserById(userId);
        if (user == null) {
            return null;
        }
        
        // Get student profile for student-specific data (weight, height, bmi, emergency_contact)
        Student student = studentProfileService.getProfile((int) userId);
        
        Map<String, Object> profileData = new HashMap<>();
        
        // Basic Info from User table
        profileData.put("username", user.getUsername());
        profileData.put("email", user.getEmail());
        profileData.put("status", user.getStatus());
        profileData.put("createdDate", user.getCreatedDate());
        
        // Extended info from User table
        // Use name (full name) if available, otherwise use username as fallback
        profileData.put("fullName", (user.getName() != null && !user.getName().trim().isEmpty()) 
                                   ? user.getName() 
                                   : user.getUsername());
        profileData.put("name", user.getName()); // Store name separately
        profileData.put("avatarUrl", user.getAvatarUrl()); // Get from User table
        profileData.put("phone", user.getPhone()); // Get phone from User table
        profileData.put("dob", user.getDob()); // Get date of birth from User table
        profileData.put("address", user.getAddress()); // Get address from User table
        profileData.put("gender", null); // Gender not in User table currently
        
        // Physical Info from Student table
        profileData.put("height", student.getHeight());
        profileData.put("weight", student.getWeight());
        profileData.put("bmi", student.getBmi());
        String bmiCategory = student.getBmi() != null ? studentProfileService.getBMICategory(student.getBmi()) : null;
        profileData.put("bmiCategory", bmiCategory);
        if (bmiCategory != null) {
            profileData.put("bmiCategoryClass", bmiCategory.toLowerCase().replace(" ", ""));
        }
        
        // Emergency Contact from Student table
        profileData.put("emergencyContactName", student.getEmergencyContactName());
        profileData.put("emergencyContactPhone", student.getEmergencyContactPhone());
        profileData.put("emergencyContactRelation", student.getEmergencyContactRelation());
        profileData.put("emergencyContactAddress", student.getEmergencyContactAddress());
        
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
        
        // Update User fields: name, phone, dob, address
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String dobStr = request.getParameter("dob");
        String address = request.getParameter("address");
        
        if (name != null) {
            user.setName(name.trim());
        }
        if (phone != null) {
            user.setPhone(phone.trim().isEmpty() ? null : phone.trim());
        }
        if (dobStr != null && !dobStr.trim().isEmpty()) {
            try {
                java.sql.Date dob = java.sql.Date.valueOf(dobStr);
                user.setDob(dob);
            } catch (IllegalArgumentException e) {
                System.err.println("[UserService] Invalid date format for dob: " + dobStr);
            }
        } else {
            user.setDob(null);
        }
        if (address != null) {
            user.setAddress(address.trim().isEmpty() ? null : address.trim());
        }
        
        // Update User in database
        boolean userUpdated = userDAO.updateUser(user);
        if (!userUpdated) {
            System.err.println("[UserService] Error updating user profile");
            return false;
        }
        
        // Get or create student profile
        Student student = studentProfileService.getProfile((int) user.getId());
        student.setUserId((int) user.getId());
        
        // Update student profile from request
        studentProfileService.updateProfileFromRequest(student, request.getParameterMap());
        
        // Save student profile
        try {
            studentProfileService.saveProfile(student);
        } catch (Exception e) {
            System.err.println("[UserService] Error saving student profile: " + e.getMessage());
            return false;
        }
        
        return true;
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
            // Update User fields: name, phone, dob, address
            String name = getPartParameter(request, "name");
            String phone = getPartParameter(request, "phone");
            String dobStr = getPartParameter(request, "dob");
            String address = getPartParameter(request, "address");
            
            if (name != null) {
                user.setName(name.trim());
            }
            if (phone != null) {
                user.setPhone(phone.trim().isEmpty() ? null : phone.trim());
            }
            if (dobStr != null && !dobStr.trim().isEmpty()) {
                try {
                    java.sql.Date dob = java.sql.Date.valueOf(dobStr);
                    user.setDob(dob);
                } catch (IllegalArgumentException e) {
                    System.err.println("[UserService] Invalid date format for dob: " + dobStr);
                }
            } else {
                user.setDob(null);
            }
            if (address != null) {
                user.setAddress(address.trim().isEmpty() ? null : address.trim());
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
            
            // Update User in database (includes name, phone, dob, address, avatar)
            boolean userUpdated = userDAO.updateUser(user);
            if (!userUpdated) {
                System.err.println("[UserService] Error updating user profile");
                return false;
            }
            
            // Get or create student profile
            Student student = studentProfileService.getProfile((int) user.getId());
            student.setUserId((int) user.getId());
            
            // Update student profile from request parameters (weight, height, BMI, etc.)
            studentProfileService.updateProfileFromRequest(student, request.getParameterMap());
            
            // Save student profile
            studentProfileService.saveProfile(student);
            return true;
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
            return false;
        }
        
        // Get or create student profile
        Student student = studentProfileService.getProfile((int) user.getId());
        student.setUserId((int) user.getId());
        
        // Update height
        String heightStr = request.getParameter("height");
        if (heightStr != null && !heightStr.trim().isEmpty()) {
            try {
                student.setHeight(new BigDecimal(heightStr));
            } catch (NumberFormatException e) {
                System.err.println("[UserService] Invalid height format: " + heightStr);
            }
        } else {
            student.setHeight(null);
        }
        
        // Update weight
        String weightStr = request.getParameter("weight");
        if (weightStr != null && !weightStr.trim().isEmpty()) {
            try {
                student.setWeight(new BigDecimal(weightStr));
            } catch (NumberFormatException e) {
                System.err.println("[UserService] Invalid weight format: " + weightStr);
            }
        } else {
            student.setWeight(null);
        }
        
        // Save student profile
        try {
            studentProfileService.saveProfile(student);
            return true;
        } catch (Exception e) {
            System.err.println("[UserService] Error saving body metrics: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update goals from request parameters
     */
    public boolean updateGoalsFromRequest(User user, HttpServletRequest request) {
        if (user == null || request == null) {
            return false;
        }
        
        // Get or create nutrition goal
        Optional<NutritionGoal> goalOpt = nutritionGoalDao.findByUserId(user.getId());
        NutritionGoal goal = goalOpt.orElse(new NutritionGoal());
        goal.setUserId(user.getId());
        
        // Update goal type
        String goalType = request.getParameter("goalType");
        if (goalType != null && !goalType.trim().isEmpty()) {
            goal.setGoalType(goalType);
        }
        
        // Update activity factor
        String activityFactorStr = request.getParameter("activityFactor");
        if (activityFactorStr != null && !activityFactorStr.trim().isEmpty()) {
            try {
                goal.setActivityFactor(new BigDecimal(activityFactorStr));
            } catch (NumberFormatException e) {
                System.err.println("[UserService] Invalid activity factor: " + activityFactorStr);
            }
        }
        
        // Calculate and set calories target based on user's weight, height, gender, age, goal
        BigDecimal caloriesTarget = calculateCaloriesTarget(user, goal);
        goal.setDailyCaloriesTarget(caloriesTarget);
        
        // Calculate protein target (typically 1.6-2.2g per kg body weight for athletes)
        // Get student profile for weight
        Student student = studentProfileService.getProfile((int) user.getId());
        BigDecimal proteinTarget = null;
        if (student.getWeight() != null) {
            // Use 1.8g per kg as default
            proteinTarget = student.getWeight().multiply(new BigDecimal("1.8")).setScale(2, RoundingMode.HALF_UP);
            goal.setDailyProteinTarget(proteinTarget);
        }
        
        // Save nutrition goal
        return nutritionGoalDao.saveOrUpdate(goal);
    }
    
    /**
     * Calculate daily calories target based on user's body metrics and goal
     * Uses Harris-Benedict equation for BMR, then applies activity factor and goal adjustments
     */
    public BigDecimal calculateCaloriesTarget(User user, NutritionGoal goal) {
        // Get student profile for body metrics
        Student student = studentProfileService.getProfile((int) user.getId());
        
        if (student.getWeight() == null || student.getHeight() == null) {
            return null;
        }
        
        BigDecimal weight = student.getWeight();
        BigDecimal height = student.getHeight(); // in cm
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
     * Helper: Convert empty string to null
     */
    private String emptyToNull(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value;
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

