package service;

import dao.AdvisoryDAO;
import model.AdvisoryRequest;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service for AdvisoryRequest operations
 * Follows MVC pattern - Service layer between Controller and DAO
 */
public class AdvisoryService {
    private static final Logger LOGGER = Logger.getLogger(AdvisoryService.class.getName());
    private final AdvisoryDAO advisoryDAO;

    public AdvisoryService() {
        this.advisoryDAO = new AdvisoryDAO();
    }

    /**
     * Get all advisory requests
     */
    public List<AdvisoryRequest> getAllRequests() {
        try {
            return advisoryDAO.findAll();
        } catch (Exception e) {
            LOGGER.severe("Error getting all requests: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve advisory requests", e);
        }
    }

    /**
     * Get request by ID
     */
    public AdvisoryRequest getRequestById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Request ID cannot be null");
        }
        try {
            // GenericDAO.findById() uses int, but AdvisoryRequest.id is Long
            // Convert Long to int (assuming ID won't exceed int max value)
            return advisoryDAO.findById(id.intValue());
        } catch (Exception e) {
            LOGGER.severe("Error getting request by ID: " + id + " - " + e.getMessage());
            throw new RuntimeException("Failed to retrieve advisory request", e);
        }
    }

    /**
     * Get requests by email
     */
    public List<AdvisoryRequest> getRequestsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        try {
            return advisoryDAO.findByEmail(email.trim());
        } catch (Exception e) {
            LOGGER.severe("Error finding requests by email: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve requests by email", e);
        }
    }

    /**
     * Get requests by phone
     */
    public List<AdvisoryRequest> getRequestsByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        try {
            return advisoryDAO.findByPhone(phone.trim());
        } catch (Exception e) {
            LOGGER.severe("Error finding requests by phone: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve requests by phone", e);
        }
    }

    /**
     * Get recent requests
     */
    public List<AdvisoryRequest> getRecentRequests(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }
        try {
            return advisoryDAO.findRecent(limit);
        } catch (Exception e) {
            LOGGER.severe("Error finding recent requests: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve recent requests", e);
        }
    }

    /**
     * Create new advisory request
     */
    public int createRequest(AdvisoryRequest request) {
        validateRequest(request);
        try {
            return advisoryDAO.save(request);
        } catch (Exception e) {
            LOGGER.severe("Error creating advisory request: " + e.getMessage());
            throw new RuntimeException("Failed to create advisory request", e);
        }
    }

    /**
     * Create advisory request from form data
     * Enhanced validation with detailed error messages
     */
    public int createRequest(String fullName, String phone, String email, String address) {
        // Validate full name
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập họ và tên");
        }
        String trimmedName = fullName.trim();
        if (trimmedName.length() < 2) {
            throw new IllegalArgumentException("Họ và tên phải có ít nhất 2 ký tự");
        }
        if (trimmedName.length() > 100) {
            throw new IllegalArgumentException("Họ và tên không được vượt quá 100 ký tự");
        }
        // Check if name contains only letters, spaces, and Vietnamese characters
        if (!trimmedName.matches("^[\\p{L}\\s]+$")) {
            throw new IllegalArgumentException("Họ và tên chỉ được chứa chữ cái và khoảng trắng");
        }

        // Validate phone
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập số điện thoại");
        }
        String trimmedPhone = phone.trim();
        if (!isValidPhone(trimmedPhone)) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ. Vui lòng nhập 10-11 chữ số");
        }

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập địa chỉ email");
        }
        String trimmedEmail = email.trim();
        if (trimmedEmail.length() > 255) {
            throw new IllegalArgumentException("Địa chỉ email không được vượt quá 255 ký tự");
        }
        if (!isValidEmail(trimmedEmail)) {
            throw new IllegalArgumentException("Địa chỉ email không hợp lệ. Vui lòng kiểm tra lại");
        }

        // Validate address
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập địa chỉ");
        }
        String trimmedAddress = address.trim();
        if (trimmedAddress.length() < 5) {
            throw new IllegalArgumentException("Địa chỉ phải có ít nhất 5 ký tự");
        }
        if (trimmedAddress.length() > 500) {
            throw new IllegalArgumentException("Địa chỉ không được vượt quá 500 ký tự");
        }

        AdvisoryRequest request = new AdvisoryRequest();
        request.setFullName(trimmedName);
        request.setPhone(trimmedPhone);
        request.setEmail(trimmedEmail.toLowerCase());
        request.setAddress(trimmedAddress);

        return createRequest(request);
    }

    /**
     * Update request
     */
    public int updateRequest(AdvisoryRequest request) {
        validateRequest(request);
        if (request.getId() == null) {
            throw new IllegalArgumentException("Request ID is required for update");
        }
        try {
            return advisoryDAO.update(request);
        } catch (Exception e) {
            LOGGER.severe("Error updating advisory request: " + e.getMessage());
            throw new RuntimeException("Failed to update advisory request", e);
        }
    }

    /**
     * Delete request
     */
    public int deleteRequest(AdvisoryRequest request) {
        if (request == null || request.getId() == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        try {
            return advisoryDAO.delete(request);
        } catch (Exception e) {
            LOGGER.severe("Error deleting advisory request: " + e.getMessage());
            throw new RuntimeException("Failed to delete advisory request", e);
        }
    }

    /**
     * Validate request entity
     * Enhanced validation with detailed error messages
     */
    private void validateRequest(AdvisoryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Yêu cầu không hợp lệ");
        }
        
        // Validate full name
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập họ và tên");
        }
        String trimmedName = request.getFullName().trim();
        if (trimmedName.length() < 2) {
            throw new IllegalArgumentException("Họ và tên phải có ít nhất 2 ký tự");
        }
        if (trimmedName.length() > 100) {
            throw new IllegalArgumentException("Họ và tên không được vượt quá 100 ký tự");
        }
        if (!trimmedName.matches("^[\\p{L}\\s]+$")) {
            throw new IllegalArgumentException("Họ và tên chỉ được chứa chữ cái và khoảng trắng");
        }
        
        // Validate phone
        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập số điện thoại");
        }
        String trimmedPhone = request.getPhone().trim();
        if (!isValidPhone(trimmedPhone)) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ. Vui lòng nhập 10-11 chữ số");
        }
        
        // Validate email
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập địa chỉ email");
        }
        String trimmedEmail = request.getEmail().trim();
        if (trimmedEmail.length() > 255) {
            throw new IllegalArgumentException("Địa chỉ email không được vượt quá 255 ký tự");
        }
        if (!isValidEmail(trimmedEmail)) {
            throw new IllegalArgumentException("Địa chỉ email không hợp lệ. Vui lòng kiểm tra lại");
        }
        
        // Validate address
        if (request.getAddress() == null || request.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập địa chỉ");
        }
        String trimmedAddress = request.getAddress().trim();
        if (trimmedAddress.length() < 5) {
            throw new IllegalArgumentException("Địa chỉ phải có ít nhất 5 ký tự");
        }
        if (trimmedAddress.length() > 500) {
            throw new IllegalArgumentException("Địa chỉ không được vượt quá 500 ký tự");
        }
    }

    /**
     * Validate phone number format (10-11 digits)
     */
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return phone.matches("^[0-9]{10,11}$");
    }

    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}

