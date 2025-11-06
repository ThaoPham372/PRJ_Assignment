package com.gym.dao.shop;

import com.gym.dao.GenericDAO;
import com.gym.model.shop.Product;
import com.gym.model.shop.ProductType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Product entity using JPA
 * Extends GenericDAO for basic CRUD operations
 */
public class ProductDao extends GenericDAO<Product> {
    private static final Logger LOGGER = Logger.getLogger(ProductDao.class.getName());

    public ProductDao() {
        super(Product.class);
    }

    /**
     * Search active products with pagination
     */
    public List<Product> search(String keyword, ProductType type, int page, int pageSize) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> root = query.from(Product.class);

            List<Predicate> predicates = new ArrayList<>();
            
            // Always filter by active = true
            predicates.add(cb.equal(root.get("active"), true));
            
            // Add keyword filter if provided
            if (keyword != null && !keyword.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("productName")), 
                    "%" + keyword.trim().toLowerCase() + "%"));
            }
            
            // Add type filter if provided
            if (type != null) {
                predicates.add(cb.equal(root.get("productType"), type));
            }
            
            query.where(predicates.toArray(new Predicate[0]));
            query.orderBy(cb.desc(root.get("createdAt")));
            
            TypedQuery<Product> typedQuery = em.createQuery(query);
            
            // Pagination
            int offset = (page - 1) * pageSize;
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(pageSize);
            
            return typedQuery.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching products", e);
            return new ArrayList<>();
        }
    }

    /**
     * Count total products matching criteria
     */
    public int count(String keyword, ProductType type) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<Product> root = query.from(Product.class);

            List<Predicate> predicates = new ArrayList<>();
            
            // Always filter by active = true
            predicates.add(cb.equal(root.get("active"), true));
            
            // Add keyword filter if provided
            if (keyword != null && !keyword.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("productName")), 
                    "%" + keyword.trim().toLowerCase() + "%"));
            }
            
            // Add type filter if provided
            if (type != null) {
                predicates.add(cb.equal(root.get("productType"), type));
            }
            
            query.where(predicates.toArray(new Predicate[0]));
            query.select(cb.count(root));
            
            Long count = em.createQuery(query).getSingleResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error counting products", e);
            return 0;
        }
    }

    /**
     * Find product by ID
     * Returns Optional for better null safety
     * Uses GenericDAO.findByIdOptional(Long)
     */
    public Optional<Product> findById(Long productId) {
        return findByIdOptional(productId);
    }

    /**
     * Decrease stock for multiple products (with lock)
     * @param productQuantities Map of productId -> quantity to decrease
     * @throws SQLException if insufficient stock
     */
    public void decreaseStockBatch(Map<Long, Integer> productQuantities) throws SQLException {
        decreaseStockBatchWithEntityManager(productQuantities, null);
    }

    /**
     * Decrease stock for multiple products using provided EntityManager (for transactions)
     * @param productQuantities Map of productId -> quantity to decrease
     * @param sharedEm EntityManager to use (if null, uses default em)
     * @throws SQLException if insufficient stock
     */
    public void decreaseStockBatchWithEntityManager(Map<Long, Integer> productQuantities, jakarta.persistence.EntityManager sharedEm) throws SQLException {
        if (productQuantities == null || productQuantities.isEmpty()) {
            return;
        }
        
        boolean transactionStarted = false;
        jakarta.persistence.EntityManager emToUse = sharedEm != null ? sharedEm : em;
        
        try {
            if (!emToUse.getTransaction().isActive()) {
                emToUse.getTransaction().begin();
                transactionStarted = true;
            }
            
            for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
                Long productId = entry.getKey();
                Integer quantity = entry.getValue();
                
                Product product = emToUse.find(Product.class, productId);
                if (product == null) {
                    throw new SQLException("Product not found: " + productId);
                }
                
                // Check stock
                if (product.getStockQuantity() == null || product.getStockQuantity() < quantity) {
                    throw new SQLException("Insufficient stock for product ID: " + productId + 
                        ". Available: " + (product.getStockQuantity() != null ? product.getStockQuantity() : 0) + 
                        ", Required: " + quantity);
                }
                
                // Decrease stock
                product.setStockQuantity(product.getStockQuantity() - quantity);
                emToUse.merge(product);
            }
            
            if (transactionStarted) {
                emToUse.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionStarted && emToUse.getTransaction().isActive()) {
                try {
                    emToUse.getTransaction().rollback();
                } catch (Exception rollbackEx) {
                    LOGGER.log(Level.WARNING, "Error rolling back transaction", rollbackEx);
                }
            }
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            throw new SQLException("Error decreasing stock: " + e.getMessage(), e);
        }
    }

    /**
     * Backward compatibility method - accepts Connection but uses EntityManager
     */
    public void decreaseStockBatch(Map<Long, Integer> productQuantities, java.sql.Connection conn) throws SQLException {
        // In JPA, we use EntityManager instead of Connection
        // This method is for backward compatibility
        decreaseStockBatchWithEntityManager(productQuantities, null);
    }
    
    // ==================== ADMIN SEARCH & PAGINATION ====================
    
    /**
     * Search products for admin (includes inactive products)
     */
    public List<Product> searchProductsAdmin(String keyword, int page, int pageSize) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT p FROM Product p WHERE 1=1");
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                jpql.append(" AND (LOWER(p.productName) LIKE LOWER(:keyword) ")
                    .append("OR LOWER(p.description) LIKE LOWER(:keyword))");
            }
            
            jpql.append(" ORDER BY p.createdAt DESC");
            
            TypedQuery<Product> query = em.createQuery(jpql.toString(), Product.class);
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword.trim() + "%");
            }
            
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching products for admin", e);
            return List.of();
        }
    }
    
    /**
     * Count products for admin (includes inactive products)
     */
    public int countProductsAdmin(String keyword) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(p) FROM Product p WHERE 1=1");
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                jpql.append(" AND (LOWER(p.productName) LIKE LOWER(:keyword) ")
                    .append("OR LOWER(p.description) LIKE LOWER(:keyword))");
            }
            
            TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword.trim() + "%");
            }
            
            return query.getSingleResult().intValue();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error counting products for admin", e);
            return 0;
        }
    }
}
