package com.gym.dao.shop;

import com.gym.model.shop.Product;
import com.gym.model.shop.ProductType;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Product entity
 */
public class ProductDao {
    private static final Logger LOGGER = Logger.getLogger(ProductDao.class.getName());

    /**
     * Search active products with pagination
     */
    public List<Product> search(String keyword, ProductType type, int page, int pageSize) {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        
        // Build SQL with MySQL pagination using LIMIT/OFFSET
        // NOTE: Schema does not have image_path column
        StringBuilder sql = new StringBuilder(
            "SELECT product_id, product_name, product_type, price, stock_quantity, " +
            "unit, is_active, created_at " +
            "FROM products " +
            "WHERE is_active = 1 "
        );
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND product_name LIKE ? ");
        }
        
        if (type != null) {
            sql.append("AND product_type = ? ");
        }
        
        sql.append("ORDER BY created_at DESC ");
        sql.append("LIMIT ? OFFSET ?");

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Database connection is null");
                return products;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                int paramIndex = 1;
                if (keyword != null && !keyword.trim().isEmpty()) {
                    stmt.setString(paramIndex++, "%" + keyword.trim() + "%");
                }
                if (type != null) {
                    stmt.setString(paramIndex++, type.getCode());
                }
                stmt.setInt(paramIndex++, pageSize);
                stmt.setInt(paramIndex, offset);
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching products", e);
        }
        
        return products;
    }

    /**
     * Count total products matching criteria
     */
    public int count(String keyword, ProductType type) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM products WHERE is_active = 1 "
        );
        
        List<String> conditions = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            conditions.add("product_name LIKE ?");
        }
        if (type != null) {
            conditions.add("product_type = ?");
        }
        
        if (!conditions.isEmpty()) {
            sql.append("AND ").append(String.join(" AND ", conditions));
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return 0;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                int paramIndex = 1;
                if (keyword != null && !keyword.trim().isEmpty()) {
                    stmt.setString(paramIndex++, "%" + keyword.trim() + "%");
                }
                if (type != null) {
                    stmt.setString(paramIndex++, type.getCode());
                }
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting products", e);
        }
        
        return 0;
    }

    /**
     * Find product by ID
     */
    public Optional<Product> findById(Long productId) {
        // NOTE: Schema does not have image_path column
        String sql = "SELECT product_id, product_name, product_type, price, stock_quantity, " +
                    "unit, is_active, created_at " +
                    "FROM products WHERE product_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return Optional.empty();
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, productId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding product by ID: " + productId, e);
        }
        
        return Optional.empty();
    }

    /**
     * Decrease stock for multiple products (with lock)
     * @param productQuantities Map of productId -> quantity to decrease
     * @throws SQLException if insufficient stock
     */
    public void decreaseStockBatch(Map<Long, Integer> productQuantities) throws SQLException {
        decreaseStockBatch(productQuantities, null);
    }

    /**
     * Decrease stock for multiple products using provided connection (for transactions)
     * @param productQuantities Map of productId -> quantity to decrease
     * @param conn Connection to use (if null, creates new connection)
     * @throws SQLException if insufficient stock
     */
    public void decreaseStockBatch(Map<Long, Integer> productQuantities, Connection conn) throws SQLException {
        if (productQuantities == null || productQuantities.isEmpty()) {
            return;
        }
        
        String sql = "UPDATE products SET stock_quantity = stock_quantity - ? " +
                    "WHERE product_id = ? AND stock_quantity >= ?";
        
        boolean shouldCloseConnection = false;
        try {
            if (conn == null) {
                conn = DatabaseUtil.getConnection();
                shouldCloseConnection = true;
            }
            
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
                    Long productId = entry.getKey();
                    Integer quantity = entry.getValue();
                    
                    stmt.setInt(1, quantity);
                    stmt.setLong(2, productId);
                    stmt.setInt(3, quantity);
                    
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected == 0) {
                        throw new SQLException("Insufficient stock for product ID: " + productId);
                    }
                }
            }
        } finally {
            // Only close connection if we created it
            if (shouldCloseConnection && conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing connection", e);
                }
            }
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getLong("product_id"));
        product.setProductName(rs.getString("product_name"));
        
        String typeStr = rs.getString("product_type");
        product.setProductType(ProductType.fromCode(typeStr));
        
        product.setPrice(rs.getBigDecimal("price"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        product.setUnit(rs.getString("unit"));
        product.setActive(rs.getBoolean("is_active"));
        
        // NOTE: image_path column does not exist in current schema
        // Set to null - can be added later if needed
        product.setImagePath(null);
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            product.setCreatedAt(createdAt.toInstant().atOffset(ZoneOffset.UTC));
        }
        
        return product;
    }
}

