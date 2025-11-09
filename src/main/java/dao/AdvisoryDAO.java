package dao;

import model.AdvisoryRequest;
import jakarta.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for AdvisoryRequest entity - extends GenericDAO for CRUD operations
 * Follows OOP principles and reuses GenericDAO methods
 */
public class AdvisoryDAO extends GenericDAO<AdvisoryRequest> {
    private static final Logger LOGGER = Logger.getLogger(AdvisoryDAO.class.getName());

    public AdvisoryDAO() {
        super(AdvisoryRequest.class);
    }

    /**
     * Find requests by email
     */
    public List<AdvisoryRequest> findByEmail(String email) {
        try {
            String jpql = "SELECT a FROM AdvisoryRequest a WHERE a.email = :email ORDER BY a.createdAt DESC";
            TypedQuery<AdvisoryRequest> query = em.createQuery(jpql, AdvisoryRequest.class);
            query.setParameter("email", email);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding requests by email: " + email, e);
            return Collections.emptyList();
        }
    }

    /**
     * Find requests by phone
     */
    public List<AdvisoryRequest> findByPhone(String phone) {
        try {
            String jpql = "SELECT a FROM AdvisoryRequest a WHERE a.phone = :phone ORDER BY a.createdAt DESC";
            TypedQuery<AdvisoryRequest> query = em.createQuery(jpql, AdvisoryRequest.class);
            query.setParameter("phone", phone);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding requests by phone: " + phone, e);
            return Collections.emptyList();
        }
    }

    /**
     * Find recent requests (ordered by created_at DESC)
     */
    public List<AdvisoryRequest> findRecent(int limit) {
        try {
            String jpql = "SELECT a FROM AdvisoryRequest a ORDER BY a.createdAt DESC";
            TypedQuery<AdvisoryRequest> query = em.createQuery(jpql, AdvisoryRequest.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding recent requests", e);
            return Collections.emptyList();
        }
    }
}
