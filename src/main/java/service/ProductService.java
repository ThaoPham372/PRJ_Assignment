package service;

import dao.ProductDAO;
import model.Product;
import model.ProductType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductService - Service layer cho Product
 * Tuân thủ mô hình MVC và nguyên tắc OOP
 */
public class ProductService {
    
    private final ProductDAO productDAO;
    
    public ProductService() {
        productDAO = new ProductDAO();
    }
    
    public List<Product> getAll() {
        return productDAO.findAll();
    }
    
    public List<Product> getActiveProducts() {
        return getAll().stream()
                .filter(p -> p.getActive() != null && p.getActive())
                .collect(Collectors.toList());
    }
    
    public List<Product> search(String keyword, ProductType type, int page, int pageSize) {
        List<Product> products = getActiveProducts();
        
        // Filter by keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKeyword = keyword.toLowerCase().trim();
            products = products.stream()
                    .filter(p -> p.getProductName() != null && 
                            p.getProductName().toLowerCase().contains(lowerKeyword))
                    .collect(Collectors.toList());
        }
        
        // Filter by type
        if (type != null) {
            products = products.stream()
                    .filter(p -> p.getProductType() == type)
                    .collect(Collectors.toList());
        }
        
        // Pagination
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, products.size());
        
        if (start >= products.size()) {
            return List.of();
        }
        
        return products.subList(start, end);
    }
    
    public int count(String keyword, ProductType type) {
        List<Product> products = getActiveProducts();
        
        // Filter by keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKeyword = keyword.toLowerCase().trim();
            products = products.stream()
                    .filter(p -> p.getProductName() != null && 
                            p.getProductName().toLowerCase().contains(lowerKeyword))
                    .collect(Collectors.toList());
        }
        
        // Filter by type
        if (type != null) {
            products = products.stream()
                    .filter(p -> p.getProductType() == type)
                    .collect(Collectors.toList());
        }
        
        return products.size();
    }
    
    public Product getById(int id) {
        return productDAO.findById(id);
    }
    
    public Product getProductByName(String productName) {
        return productDAO.findByProductName(productName);
    }
    
    public int add(Product product) {
        return productDAO.save(product);
    }

    public int update(Product product) {
        return productDAO.update(product);
    }

    public int delete(Product product) {
        return productDAO.delete(product);
    }
}
