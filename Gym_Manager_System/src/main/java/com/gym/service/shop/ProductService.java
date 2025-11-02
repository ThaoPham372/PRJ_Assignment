package com.gym.service.shop;

import com.gym.model.shop.Product;
import com.gym.model.shop.ProductType;

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
}



