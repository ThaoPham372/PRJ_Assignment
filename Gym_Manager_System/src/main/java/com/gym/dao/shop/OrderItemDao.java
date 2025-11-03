package com.gym.dao.shop;

import com.gym.model.shop.OrderItem;
import com.gym.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for OrderItem entity
 */
public class OrderItemDao {
    private static final Logger LOGGER = Logger.getLogger(OrderItemDao.class.getName());

    /**
     * Insert batch of order items
     * Uses provided connection if available, otherwise creates new one
     */
    public void insertBatch(Long orderId, List<OrderItem> items) {
        insertBatch(orderId, items, null);
    }
    
    /**
     * Insert batch of order items using provided connection (for transactions)
     */
    public void insertBatch(Long orderId, List<OrderItem> items, Connection conn) {
        if (items == null || items.isEmpty()) {
            return;
        }
        
        // NOTE: Schema mới dùng bảng order_details (thay vì order_items)
        // Cột: order_detail_id, order_id, product_id, package_id, product_name, quantity, 
        //      unit_price, discount_percent, discount_amount, subtotal (generated), notes
        String sql = "INSERT INTO order_details (order_id, product_id, package_id, product_name, quantity, " +
                    "unit_price, discount_percent, discount_amount, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                for (OrderItem item : items) {
                    stmt.setLong(1, orderId);
                    // product_id: NULL for packages, value for products
                    if (item.getProductId() != null && item.getProductId() > 0) {
                        stmt.setLong(2, item.getProductId());
                    } else {
                        stmt.setNull(2, Types.BIGINT);
                    }
                    // package_id: NULL for products, value for packages
                    if (item.getPackageId() != null && item.getPackageId() > 0) {
                        stmt.setLong(3, item.getPackageId());
                    } else {
                        stmt.setNull(3, Types.BIGINT);
                    }
                    stmt.setString(4, item.getProductName());
                    stmt.setInt(5, item.getQuantity());
                    stmt.setBigDecimal(6, item.getUnitPrice());
                    // discount_percent - calculate from discount_amount if needed
                    BigDecimal discountPercent = BigDecimal.ZERO;
                    if (item.getDiscountPercent() != null) {
                        discountPercent = item.getDiscountPercent();
                    } else if (item.getDiscountAmount() != null && item.getUnitPrice() != null && item.getUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
                        // Calculate discount_percent from discount_amount
                        discountPercent = item.getDiscountAmount()
                            .divide(item.getUnitPrice(), 4, java.math.RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));
                    }
                    stmt.setBigDecimal(7, discountPercent);
                    stmt.setBigDecimal(8, item.getDiscountAmount() != null ? item.getDiscountAmount() : BigDecimal.ZERO);
                    setStringOrNull(stmt, 9, item.getNotes());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting order items: " + e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException("Failed to insert order items: " + e.getMessage(), e);
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

    /**
     * Find all items for an order
     */
    public List<OrderItem> findByOrderId(Long orderId) {
        List<OrderItem> items = new ArrayList<>();
        // NOTE: Schema mới dùng bảng order_details với cột order_detail_id
        String sql = "SELECT order_detail_id, order_id, product_id, package_id, product_name, quantity, " +
                    "unit_price, discount_percent, discount_amount, subtotal, notes " +
                    "FROM order_details WHERE order_id = ? ORDER BY order_detail_id";

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                return items;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, orderId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    items.add(mapResultSetToOrderItem(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding order items for order: " + orderId, e);
        }
        
        return items;
    }

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        // Schema mới: order_detail_id (thay vì order_item_id)
        item.setOrderItemId(rs.getLong("order_detail_id"));
        item.setOrderId(rs.getLong("order_id"));
        
        // product_id can be NULL for packages
        Long productId = rs.getLong("product_id");
        if (rs.wasNull()) {
            productId = null;
        }
        item.setProductId(productId);
        
        // package_id can be NULL for products
        Long packageId = rs.getLong("package_id");
        if (rs.wasNull()) {
            packageId = null;
        }
        item.setPackageId(packageId);
        
        item.setProductName(rs.getString("product_name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        
        // New columns in order_details
        BigDecimal discountPercent = rs.getBigDecimal("discount_percent");
        item.setDiscountPercent(discountPercent);
        
        item.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        item.setSubtotal(rs.getBigDecimal("subtotal"));
        item.setNotes(rs.getString("notes"));
        
        return item;
    }

    /**
     * Helper: Set String or null
     */
    private void setStringOrNull(PreparedStatement stmt, int index, String value) throws SQLException {
        if (value != null && !value.trim().isEmpty()) {
            stmt.setString(index, value);
        } else {
            stmt.setNull(index, Types.VARCHAR);
        }
    }
}

