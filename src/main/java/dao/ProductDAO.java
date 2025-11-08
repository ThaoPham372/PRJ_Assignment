package dao;

import java.util.List;
import model.Product;


public class ProductDAO {

    GenericDAO<Product> genericDAO;

    public ProductDAO() {
        genericDAO = new GenericDAO<>(Product.class);
    }

    public int save(Product product) {
        genericDAO.save(product);
        return product.getId();
    }

    public List<Product> findAll() {
        List<Product> products = genericDAO.findAll();
        return products != null ? products : List.of();
    }

    public Product findById(int id) {
        return genericDAO.findById(id);
    }

    public Product findByProductName(String productName) {
        return genericDAO.findByField("productName", productName);
    }

    public int existsByProductName(String productName) {
        Product product = genericDAO.findByField("productName", productName);
        return product != null ? product.getId() : -1;
    }

    public int update(Product product) {
        return genericDAO.update(product);
    }

    public int delete(Product product) {
        product.setActive(false);
        return genericDAO.update(product);
    }
}
