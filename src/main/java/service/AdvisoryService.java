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
     */
    public int createRequest(String fullName, String phone, String email, String address) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }

        // Validate phone format
        if (!isValidPhone(phone.trim())) {
            throw new IllegalArgumentException("Phone number is invalid. Must be 10-11 digits.");
        }

        // Validate email format
        if (!isValidEmail(email.trim())) {
            throw new IllegalArgumentException("Email format is invalid.");
        }

        AdvisoryRequest request = new AdvisoryRequest();
        request.setFullName(fullName.trim());
        request.setPhone(phone.trim());
        request.setEmail(email.trim().toLowerCase());
        request.setAddress(address.trim());

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
     */
    private void validateRequest(AdvisoryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getAddress() == null || request.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (!isValidPhone(request.getPhone().trim())) {
            throw new IllegalArgumentException("Phone number is invalid. Must be 10-11 digits.");
        }
        if (!isValidEmail(request.getEmail().trim())) {
            throw new IllegalArgumentException("Email format is invalid.");
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

