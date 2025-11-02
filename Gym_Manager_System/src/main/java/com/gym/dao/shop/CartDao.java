package com.gym.dao.shop;

import com.gym.model.shop.CartItem;
import com.gym.util.DatabaseUtil;

import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Cart operations
 */
public class CartDao {
    private static final Logger LOGGER = Logger.getLogger(CartDao.class.getName());

    /**
     * Get all cart items for a user (with product details)
     */
    public List<CartItem> findByUserId(Long userId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT c.cart_id, c.user_id, c.product_id, c.quantity, c.added_at, " +
                    "p.product_name, p.price, p.unit, p.image_path " +
                    "FROM cart c " +
                    "INNER JOIN products p ON c.product_id = p.product_id " +
                    "WHERE c.user_id = ? AND p.is_active = 1 " +
                    "ORDER BY c.added_at DESC";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return items;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    items.add(mapResultSetToCartItem(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding cart items for user: " + userId, e);
        }
        
        return items;
    }

    /**
     * Add or update cart item (INSERT ... ON DUPLICATE KEY UPDATE for MySQL)
     */
    public void addOrUpdate(Long userId, Long productId, Integer quantity) {
        String sql = "INSERT INTO cart (user_id, product_id, quantity, added_at) " +
                    "VALUES (?, ?, ?, NOW()) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "quantity = quantity + ?, added_at = NOW()";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                stmt.setLong(2, productId);
                stmt.setInt(3, quantity);
                stmt.setInt(4, quantity);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding/updating cart item", e);
            throw new RuntimeException("Failed to update cart", e);
        }
    }

    /**
     * Update quantity for a cart item
     */
    public void updateQuantity(Long userId, Long productId, Integer quantity) {
        String sql = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, quantity);
                stmt.setLong(2, userId);
                stmt.setLong(3, productId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Cart item not found");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating cart item quantity", e);
            throw new RuntimeException("Failed to update cart quantity", e);
        }
    }

    /**
     * Remove item from cart
     */
    public void remove(Long userId, Long productId) {
        String sql = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                stmt.setLong(2, productId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing cart item", e);
            throw new RuntimeException("Failed to remove cart item", e);
        }
    }

    /**
     * Clear all items for a user
     */
    public void clear(Long userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error clearing cart", e);
            throw new RuntimeException("Failed to clear cart", e);
        }
    }

    private CartItem mapResultSetToCartItem(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setCartId(rs.getLong("cart_id"));
        item.setUserId(rs.getLong("user_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        
        Timestamp addedAt = rs.getTimestamp("added_at");
        if (addedAt != null) {
            item.setAddedAt(addedAt.toInstant().atOffset(ZoneOffset.UTC));
        }
        
        // Product details from join
        item.setProductName(rs.getString("product_name"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setUnit(rs.getString("unit"));
        item.setImagePath(rs.getString("image_path"));
        
        return item;
    }
}

