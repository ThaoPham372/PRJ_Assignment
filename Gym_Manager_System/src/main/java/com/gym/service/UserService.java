package com.gym.service;

import com.gym.dao.UserDAO;
import com.gym.dao.nutrition.NutritionGoalDao;
import com.gym.model.User;
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
 * All business logic should be here, not in Servlet
 */
public class UserService {
    
    private final UserDAO userDAO;
    private final NutritionGoalDao nutritionGoalDao;
    private final NutritionService nutritionService;
    
    public UserService() {
        this.userDAO = new UserDAO();
        this.nutritionGoalDao = new NutritionGoalDao();
        this.nutritionService = new NutritionServiceImpl();
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
     * Calculate and get BMI category
     */
    public String getBMICategory(BigDecimal bmi) {
        if (bmi == null) {
            return null;
        }
        
        double bmiValue = bmi.doubleValue();
        if (bmiValue < 18.5) {
            return "Underweight";
        } else if (bmiValue < 25) {
            return "Normal";
        } else if (bmiValue < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
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
        
        Map<String, Object> dashboardData = new HashMap<>();
        
        // Basic member info
        dashboardData.put("memberName", user.getUsername() != null ? user.getUsername() : "Member");
        dashboardData.put("packageType", user.getStatus() != null ? user.getStatus() : "ACTIVE");
        dashboardData.put("avatarUrl", user.getAvatarUrl());
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
        stats.put("packageRemaining", null); // Not tracked yet
        stats.put("bmi", user.getBmi());
        if (user.getBmi() != null) {
            stats.put("bmiCategory", getBMICategory(user.getBmi()));
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
     * Get profile data for a user
     */
    public Map<String, Object> getProfileData(long userId) {
        User user = getUserById(userId);
        if (user == null) {
            return null;
        }
        
        Map<String, Object> profileData = new HashMap<>();
        
        // Basic Info
        profileData.put("username", user.getUsername());
        profileData.put("email", user.getEmail());
        profileData.put("gender", user.getGender());
        profileData.put("address", user.getAddress());
        profileData.put("avatarUrl", user.getAvatarUrl());
        profileData.put("status", user.getStatus());
        profileData.put("createdDate", user.getCreatedDate());
        
        // Physical Info
        profileData.put("height", user.getHeight());
        profileData.put("weight", user.getWeight());
        profileData.put("bmi", user.getBmi());
        String bmiCategory = user.getBmi() != null ? getBMICategory(user.getBmi()) : null;
        profileData.put("bmiCategory", bmiCategory);
        if (bmiCategory != null) {
            profileData.put("bmiCategoryClass", bmiCategory.toLowerCase().replace(" ", ""));
        }
        
        // Emergency Contact
        profileData.put("emergencyContactName", user.getEmergencyContactName());
        profileData.put("emergencyContactPhone", user.getEmergencyContactPhone());
        profileData.put("emergencyContactRelation", user.getEmergencyContactRelation());
        profileData.put("emergencyContactAddress", user.getEmergencyContactAddress());
        
        return profileData;
    }
    
    /**
     * Update user profile from request parameters
     */
    public boolean updateProfileFromRequest(User user, HttpServletRequest request) {
        if (user == null || request == null) {
            return false;
        }
        
        // Update basic info
        String gender = emptyToNull(request.getParameter("gender"));
        String address = emptyToNull(request.getParameter("address"));
        String avatarUrl = emptyToNull(request.getParameter("avatarUrl"));
        
        user.setGender(gender);
        user.setAddress(address);
        user.setAvatarUrl(avatarUrl);
        
        // Update physical info
        String heightStr = request.getParameter("height");
        if (heightStr != null && !heightStr.trim().isEmpty()) {
            try {
                user.setHeight(new BigDecimal(heightStr));
            } catch (NumberFormatException e) {
                System.err.println("[UserService] Invalid height format: " + heightStr);
            }
        } else {
            user.setHeight(null);
        }
        
        String weightStr = request.getParameter("weight");
        if (weightStr != null && !weightStr.trim().isEmpty()) {
            try {
                user.setWeight(new BigDecimal(weightStr));
            } catch (NumberFormatException e) {
                System.err.println("[UserService] Invalid weight format: " + weightStr);
            }
        } else {
            user.setWeight(null);
        }
        // Update emergency contact
        user.setEmergencyContactName(emptyToNull(request.getParameter("emergencyContactName")));
        user.setEmergencyContactPhone(emptyToNull(request.getParameter("emergencyContactPhone")));
        user.setEmergencyContactRelation(emptyToNull(request.getParameter("emergencyContactRelation")));
        user.setEmergencyContactAddress(emptyToNull(request.getParameter("emergencyContactAddress")));
        
        return updateUser(user);
    }
    
    /**
     * Update user profile from multipart request (handles file uploads)
     */
    public boolean updateProfileFromMultipartRequest(User user, HttpServletRequest request) {
        if (user == null || request == null) {
            return false;
        }
        
        try {
            // Update basic info - use getPartParameter helper
            String gender = emptyToNull(getPartParameter(request, "gender"));
            String address = emptyToNull(getPartParameter(request, "address"));
            String avatarUrlParam = getPartParameter(request, "avatarUrl");
            
            // Handle avatar: 
            // - If URL is provided (not empty), use it
            // - If URL is empty/null, check for file upload
            // - If no file upload and no URL, keep existing avatar
            String avatarUrl = null;
            if (avatarUrlParam != null && !avatarUrlParam.trim().isEmpty()) {
                // User provided a URL, use it (can be to clear avatar if empty string was intentional)
                avatarUrl = avatarUrlParam.trim();
            } else {
                // Check for file upload
                Part avatarFilePart = request.getPart("avatarFile");
                if (avatarFilePart != null && avatarFilePart.getSize() > 0) {
                    String fileName = getFileName(avatarFilePart);
                    if (fileName != null && !fileName.isEmpty()) {
                        // File uploaded but no URL provided
                        // TODO: Implement file upload to server storage and generate URL
                        // For now, keep existing avatar or prompt user to provide URL
                        System.out.println("[UserService] File uploaded but no URL provided. Please provide URL or implement file storage.");
                        // Keep existing avatar
                        avatarUrl = user.getAvatarUrl();
                    }
                } else {
                    // No URL provided and no file uploaded, keep existing avatar
                    avatarUrl = user.getAvatarUrl();
                }
            }
            
            user.setGender(gender);
            user.setAddress(address);
            user.setAvatarUrl(avatarUrl);
            
            // Update physical info
            String heightStr = getPartParameter(request, "height");
            if (heightStr != null && !heightStr.trim().isEmpty()) {
                try {
                    user.setHeight(new BigDecimal(heightStr));
                } catch (NumberFormatException e) {
                    System.err.println("[UserService] Invalid height format: " + heightStr);
                }
            } else {
                user.setHeight(null);
            }
            
            String weightStr = getPartParameter(request, "weight");
            if (weightStr != null && !weightStr.trim().isEmpty()) {
                try {
                    user.setWeight(new BigDecimal(weightStr));
                } catch (NumberFormatException e) {
                    System.err.println("[UserService] Invalid weight format: " + weightStr);
                }
            } else {
                user.setWeight(null);
            }
            
            // Update emergency contact
            user.setEmergencyContactName(emptyToNull(getPartParameter(request, "emergencyContactName")));
            user.setEmergencyContactPhone(emptyToNull(getPartParameter(request, "emergencyContactPhone")));
            user.setEmergencyContactRelation(emptyToNull(getPartParameter(request, "emergencyContactRelation")));
            user.setEmergencyContactAddress(emptyToNull(getPartParameter(request, "emergencyContactAddress")));
            
            return updateUser(user);
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
        
        // Update height
        String heightStr = request.getParameter("height");
        if (heightStr != null && !heightStr.trim().isEmpty()) {
            try {
                user.setHeight(new BigDecimal(heightStr));
            } catch (NumberFormatException e) {
                System.err.println("[UserService] Invalid height format: " + heightStr);
            }
        } else {
            user.setHeight(null);
        }
        
        // Update weight
        String weightStr = request.getParameter("weight");
        if (weightStr != null && !weightStr.trim().isEmpty()) {
            try {
                user.setWeight(new BigDecimal(weightStr));
            } catch (NumberFormatException e) {
                System.err.println("[UserService] Invalid weight format: " + weightStr);
            }
        } else {
            user.setWeight(null);
        }
        
        return updateUser(user);
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
        BigDecimal proteinTarget = null;
        if (user.getWeight() != null) {
            // Use 1.8g per kg as default
            proteinTarget = user.getWeight().multiply(new BigDecimal("1.8")).setScale(2, RoundingMode.HALF_UP);
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
        if (user.getWeight() == null || user.getHeight() == null) {
            return null;
        }
        
        BigDecimal weight = user.getWeight();
        BigDecimal height = user.getHeight(); // in cm
        String gender = user.getGender();
        
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

