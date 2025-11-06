package com.gym.service.shop;

import com.gym.model.shop.Product;
import com.gym.model.shop.ProductType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Product operations
 */
public interface ProductService {
    /**
     * Search products with pagination
     */
    List<Product> search(String keyword, ProductType type, int page, int pageSize);
    
    /**
     * Count total products matching criteria
     */
    int count(String keyword, ProductType type);
    
    /**
     * Find product by ID
     */
    Product findById(Long productId);
    
    // ==================== ADMIN CRUD METHODS ====================
    
    /**
     * Get all products (for admin management)
     */
    List<Product> getAllProducts();
    
    /**
     * Get product by ID (for admin)
     */
    Product getProductById(Long productId);
    
    /**
     * Search products with pagination (for admin)
     */
    List<Product> searchProductsAdmin(String keyword, int page, int pageSize);
    
    /**
     * Count products matching search criteria (for admin)
     */
    int countProductsAdmin(String keyword);
    
    /**
     * Create new product
     */
    boolean createProduct(String productName, String productType, BigDecimal price, 
                         BigDecimal costPrice, String description, Integer stockQuantity, 
                         String unit, boolean active);
    
    /**
     * Update existing product
     */
    boolean updateProduct(Long productId, String productName, String productType, 
                         BigDecimal price, BigDecimal costPrice, String description, 
                         Integer stockQuantity, String unit, boolean active);
    
    /**
     * Delete product (soft delete)
     */
    boolean deleteProduct(Long productId);
}



