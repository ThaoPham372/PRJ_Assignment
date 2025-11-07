package com.gym.dao.membership;

import com.gym.dao.GenericDAO;
import com.gym.model.membership.Package;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Package entity - JPA Implementation
 * Handles CRUD operations for packages table
 */
public class PackageDAO extends GenericDAO<Package> {
    private static final Logger LOGGER = Logger.getLogger(PackageDAO.class.getName());

    public PackageDAO() {
        super(Package.class);
    }

    /**
     * Find all active packages (optionally filtered by gym_id)
     * @param gymId Optional gym ID to filter packages. If null, returns all active packages.
     * @return List of active packages
     */
    public List<Package> findAllActive(Integer gymId) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Package> query = cb.createQuery(Package.class);
            Root<Package> root = query.from(Package.class);
            
            List<Predicate> predicates = new ArrayList<>();
            
            // Active filter: is_active = 1 OR is_active IS NULL
            Predicate isActiveTrue = cb.equal(root.get("isActive"), true);
            Predicate isActiveNull = cb.isNull(root.get("isActive"));
            Predicate isActive = cb.or(isActiveTrue, isActiveNull);
            predicates.add(isActive);
            
            // Gym filter
            if (gymId != null) {
                Predicate gymIdEqual = cb.equal(root.get("gymId"), gymId);
                Predicate gymIdNull = cb.isNull(root.get("gymId"));
                Predicate gymFilter = cb.or(gymIdEqual, gymIdNull);
                predicates.add(gymFilter);
            }
            
            query.where(predicates.toArray(new Predicate[0]));
            query.orderBy(cb.desc(root.get("createdDate")));
            
            TypedQuery<Package> typedQuery = em.createQuery(query);
            return typedQuery.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding all active packages", e);
            return new ArrayList<>();
        }
    }

    /**
     * Find all active packages (no gym filter)
     */
    public List<Package> findAllActive() {
        return findAllActive(null);
    }

    /**
     * Find package by ID
     */
    public Optional<Package> findById(Long packageId) {
        return findByIdOptional(packageId);
    }

    /**
     * Create a new package
     */
    public Long create(Package pkg) {
        try {
            beginTransaction();
            em.persist(pkg);
            commitTransaction();
            return pkg.getPackageId();
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error creating package", e);
            throw new RuntimeException("Failed to create package: " + e.getMessage(), e);
        }
    }

    /**
     * Update an existing package
     */
    public boolean updatePackage(Package pkg) {
        try {
            beginTransaction();
            em.merge(pkg);
            commitTransaction();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error updating package: " + pkg.getPackageId(), e);
            throw new RuntimeException("Failed to update package: " + e.getMessage(), e);
        }
    }

    /**
     * Soft disable a package (set is_active = 0 instead of deleting)
     */
    public boolean softDisable(Long packageId) {
        try {
            beginTransaction();
            
            Package pkg = em.find(Package.class, packageId);
            if (pkg == null) {
                rollbackTransaction();
                return false;
            }
            
            pkg.setIsActive(false);
            em.merge(pkg);
            
            commitTransaction();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            LOGGER.log(Level.SEVERE, "Error soft disabling package: " + packageId, e);
            throw new RuntimeException("Failed to disable package: " + e.getMessage(), e);
        }
    }
}
