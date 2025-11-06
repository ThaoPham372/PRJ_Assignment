package com.gym.service.shop;

import com.gym.dao.shop.ProductDao;
import com.gym.model.shop.Product;
import com.gym.model.shop.ProductType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ProductService
 * ✅ Tái sử dụng ProductDao (extends GenericDAO)
 */
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;

    public ProductServiceImpl() {
        this.productDao = new ProductDao();
    }

    @Override
    public List<Product> search(String keyword, ProductType type, int page, int pageSize) {
        return productDao.search(keyword, type, page, pageSize);
    }

    @Override
    public int count(String keyword, ProductType type) {
        return productDao.count(keyword, type);
    }

    @Override
    public Product findById(Long productId) {
        Optional<Product> product = productDao.findById(productId);
        return product.orElse(null);
    }
    
    // ==================== ADMIN CRUD METHODS ====================
    
    @Override
    public List<Product> getAllProducts() {
        try {
            // ✅ Tái sử dụng ProductDao.findAll() từ GenericDAO
            return productDao.findAll();
        } catch (Exception e) {
            System.err.println("[ProductService] Error getting all products: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public Product getProductById(Long productId) {
        try {
            // ✅ Tái sử dụng ProductDao.findByIdOptional() từ GenericDAO
            return productDao.findByIdOptional(productId).orElse(null);
        } catch (Exception e) {
            System.err.println("[ProductService] Error getting product: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Product> searchProductsAdmin(String keyword, int page, int pageSize) {
        try {
            return productDao.searchProductsAdmin(keyword, page, pageSize);
        } catch (Exception e) {
            System.err.println("[ProductService] Error searching products: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public int countProductsAdmin(String keyword) {
        try {
            return productDao.countProductsAdmin(keyword);
        } catch (Exception e) {
            System.err.println("[ProductService] Error counting products: " + e.getMessage());
            return 0;
        }
    }
    
    @Override
    public boolean createProduct(String productName, String productType, BigDecimal price, 
                                 BigDecimal costPrice, String description, Integer stockQuantity, 
                                 String unit, boolean active) {
        try {
            Product product = new Product();
            product.setProductName(productName);
            product.setProductType(ProductType.valueOf(productType));
            product.setPrice(price);
            product.setCostPrice(costPrice);
            product.setDescription(description);
            product.setStockQuantity(stockQuantity);
            product.setUnit(unit);
            product.setActive(active);
            product.setCreatedAt(java.time.LocalDateTime.now());
            product.setUpdatedAt(java.time.LocalDateTime.now());
            
            // ✅ Tái sử dụng ProductDao.save() từ GenericDAO
            productDao.save(product);
            System.out.println("[ProductService] Created product: " + productName);
            return true;
        } catch (Exception e) {
            System.err.println("[ProductService] Error creating product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateProduct(Long productId, String productName, String productType, 
                                 BigDecimal price, BigDecimal costPrice, String description, 
                                 Integer stockQuantity, String unit, boolean active) {
        try {
            // ✅ Tái sử dụng ProductDao.findByIdOptional() từ GenericDAO
            Product product = productDao.findByIdOptional(productId).orElse(null);
            if (product == null) {
                System.err.println("[ProductService] Product not found: " + productId);
                return false;
            }
            
            product.setProductName(productName);
            product.setProductType(ProductType.valueOf(productType));
            product.setPrice(price);
            product.setCostPrice(costPrice);
            product.setDescription(description);
            product.setStockQuantity(stockQuantity);
            product.setUnit(unit);
            product.setActive(active);
            product.setUpdatedAt(java.time.LocalDateTime.now());
            
            // ✅ Tái sử dụng ProductDao.updateEntity() từ GenericDAO
            productDao.updateEntity(product);
            System.out.println("[ProductService] Updated product: " + productId);
            return true;
        } catch (Exception e) {
            System.err.println("[ProductService] Error updating product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteProduct(Long productId) {
        try {
            Product product = productDao.findByIdOptional(productId).orElse(null);
            if (product == null) {
                System.err.println("[ProductService] Product not found: " + productId);
                return false;
            }
            
            // Soft delete
            product.setActive(false);
            product.setUpdatedAt(java.time.LocalDateTime.now());
            
            // ✅ Tái sử dụng ProductDao.updateEntity() từ GenericDAO
            productDao.updateEntity(product);
            System.out.println("[ProductService] Deleted (deactivated) product: " + productId);
            return true;
        } catch (Exception e) {
            System.err.println("[ProductService] Error deleting product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}



