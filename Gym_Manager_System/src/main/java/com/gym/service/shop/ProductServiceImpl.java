package com.gym.service.shop;

import com.gym.dao.shop.ProductDao;
import com.gym.model.shop.Product;
import com.gym.model.shop.ProductType;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ProductService
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
}



