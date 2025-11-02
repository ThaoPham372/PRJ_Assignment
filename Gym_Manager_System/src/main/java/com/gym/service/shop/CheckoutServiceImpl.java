package com.gym.service.shop;

import com.gym.dao.shop.*;
import com.gym.model.shop.*;
import com.gym.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of CheckoutService with transaction handling
 */
public class CheckoutServiceImpl implements CheckoutService {
    private static final Logger LOGGER = Logger.getLogger(CheckoutServiceImpl.class.getName());
    
    private final CartDao cartDao;
    private final ProductDao productDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public CheckoutServiceImpl() {
        this.cartDao = new CartDao();
        this.productDao = new ProductDao();
        this.orderDao = new OrderDao();
        this.orderItemDao = new OrderItemDao();
    }

    @Override
    public Order checkout(Long userId, PaymentMethod paymentMethod, String deliveryName,
                          String deliveryAddress, String deliveryPhone, DeliveryMethod deliveryMethod) 
            throws EmptyCartException, InsufficientStockException {
        
        // Step 1: Get cart items
        List<CartItem> cartItems = cartDao.findByUserId(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new EmptyCartException("Giỏ hàng trống");
        }

        // Step 2: Snap current prices and names from products
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<Long, Integer> productQuantities = new HashMap<>();

        for (CartItem cartItem : cartItems) {
            Optional<Product> productOpt = productDao.findById(cartItem.getProductId());
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found: " + cartItem.getProductId());
            }
            
            Product product = productOpt.get();
            
            // Check stock
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new InsufficientStockException(
                    "Sản phẩm '" + product.getProductName() + "' không đủ tồn kho", 
                    product.getProductId()
                );
            }
            
            // Create order item snapshot
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getProductId());
            orderItem.setProductName(product.getProductName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setDiscountAmount(BigDecimal.ZERO);
            
            BigDecimal subtotal = product.getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                .subtract(BigDecimal.ZERO);
            orderItem.setSubtotal(subtotal);
            
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(subtotal);
            productQuantities.put(product.getProductId(), cartItem.getQuantity());
        }

        // Step 3: Generate order number
        String orderNumber = generateOrderNumber(userId);

        // Step 4: Create order object
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNumber(orderNumber);
        order.setOrderDate(OffsetDateTime.now(ZoneOffset.UTC));
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(PaymentStatus.PENDING); // Always PENDING initially
        order.setOrderStatus(OrderStatus.PENDING);
        
        // Set delivery information
        order.setDeliveryName(deliveryName);
        order.setDeliveryAddress(deliveryAddress);
        order.setDeliveryPhone(deliveryPhone);
        order.setDeliveryMethod(deliveryMethod);

        // Step 5: BEGIN TRANSACTION
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }
            
            // Disable auto-commit for transaction
            conn.setAutoCommit(false);

            try {
                // 5.1: Insert order (pass connection for transaction)
                Long orderId = orderDao.insert(order, conn);
                order.setOrderId(orderId);

                // 5.2: Insert order items (pass connection for transaction)
                orderItemDao.insertBatch(orderId, orderItems, conn);

                // 5.3: Decrease stock with lock
                productDao.decreaseStockBatch(productQuantities);

                // 5.4: Clear cart
                cartDao.clear(userId);

                // 5.5: Update payment status (already set during insert, but explicit update) (pass connection for transaction)
                orderDao.updatePayment(orderId, PaymentStatus.PENDING, paymentMethod, conn);

                // COMMIT
                conn.commit();
                
                // Reload order with items from DB to get computed values
                Optional<Order> reloadedOrder = orderDao.findById(orderId);
                if (reloadedOrder.isPresent()) {
                    Order fullOrder = reloadedOrder.get();
                    fullOrder.setItems(orderItemDao.findByOrderId(orderId));
                    return fullOrder;
                }
                
                return order;
                
            } catch (SQLException e) {
                // ROLLBACK on any error
                if (conn != null) {
                    conn.rollback();
                }
                LOGGER.log(Level.SEVERE, "Error during checkout transaction", e);
                
                // Check if it's an insufficient stock error
                if (e.getMessage() != null && e.getMessage().contains("Insufficient stock")) {
                    throw new InsufficientStockException(e.getMessage(), null);
                }
                
                throw new RuntimeException("Checkout failed: " + e.getMessage(), e);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error establishing transaction connection", e);
            throw new RuntimeException("Checkout failed: " + e.getMessage(), e);
        } finally {
            // Restore auto-commit and close connection if needed
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    // Note: Don't close connection here if using connection pool
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error restoring auto-commit", e);
                }
            }
        }
    }

    @Override
    public Order getOrder(Long orderId) {
        Optional<Order> orderOpt = orderDao.findById(orderId);
        if (orderOpt.isEmpty()) {
            return null;
        }
        
        Order order = orderOpt.get();
        List<OrderItem> items = orderItemDao.findByOrderId(orderId);
        order.setItems(items);
        
        return order;
    }

    @Override
    public String processMoMoPayment(Long orderId, String baseUrl) {
        Optional<Order> orderOpt = orderDao.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("Order not found: " + orderId);
        }
        
        Order order = orderOpt.get();
        
        // Use provided baseUrl or fallback to system property
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = System.getProperty("app.base.url", "http://localhost:8080");
        }
        
        String returnUrl = baseUrl + "/order/momo/return?orderId=" + orderId;
        String notifyUrl = baseUrl + "/order/momo/notify";
        
        MoMoPaymentService momoService = new MoMoPaymentService();
        
        String orderInfo = "Thanh toan don hang " + order.getOrderNumber();
        
        // Get amount (in VND)
        BigDecimal amount = order.getFinalAmount() != null ? order.getFinalAmount() : order.getTotalAmount();
        
        String paymentUrl = momoService.createPaymentRequest(
            order.getOrderNumber(),
            amount,
            orderInfo,
            returnUrl,
            notifyUrl
        );
        
        return paymentUrl;
    }


    @Override
    public Order checkoutMembership(Long userId, Long membershipId, PaymentMethod paymentMethod,
                                     String deliveryName, String deliveryPhone) {
        // Get membership
        com.gym.dao.membership.MembershipDao membershipDao = new com.gym.dao.membership.MembershipDao();
        Optional<com.gym.model.membership.Membership> membershipOpt = membershipDao.findById(membershipId);
        if (membershipOpt.isEmpty()) {
            throw new RuntimeException("Membership not found: " + membershipId);
        }
        
        com.gym.model.membership.Membership membership = membershipOpt.get();
        
        // Create order item for membership
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(null); // Membership is not a product
        orderItem.setProductName("Gói thành viên: " + membership.getDisplayName());
        orderItem.setQuantity(1);
        orderItem.setUnitPrice(membership.getPrice());
        orderItem.setDiscountAmount(BigDecimal.ZERO);
        orderItem.setSubtotal(membership.getPrice());
        orderItems.add(orderItem);
        
        // Generate order number
        String orderNumber = generateOrderNumber(userId);
        
        // Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNumber(orderNumber);
        order.setOrderDate(OffsetDateTime.now(ZoneOffset.UTC));
        order.setTotalAmount(membership.getPrice());
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(com.gym.model.shop.PaymentStatus.PENDING);
        order.setOrderStatus(com.gym.model.shop.OrderStatus.PENDING);
        order.setDeliveryName(deliveryName);
        order.setDeliveryPhone(deliveryPhone);
        order.setDeliveryMethod(DeliveryMethod.PICKUP); // Membership doesn't need delivery
        
        // Transaction
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }
            
            conn.setAutoCommit(false);
            
            try {
                // Insert order (pass connection for transaction)
                Long orderId = orderDao.insert(order, conn);
                order.setOrderId(orderId);
                
                // Insert order items (membership as order item) (pass connection for transaction)
                orderItemDao.insertBatch(orderId, orderItems, conn);
                
                // Create user membership (only if payment is successful for now, or always for pending)
                com.gym.dao.membership.UserMembershipDao userMembershipDao = 
                    new com.gym.dao.membership.UserMembershipDao();
                java.time.LocalDate startDate = java.time.LocalDate.now();
                java.time.LocalDate expiryDate = startDate.plusMonths(membership.getDurationMonths());
                userMembershipDao.create(userId, membershipId, startDate, expiryDate, orderId);
                
                // Update payment status (pass connection for transaction)
                orderDao.updatePayment(orderId, com.gym.model.shop.PaymentStatus.PENDING, paymentMethod, conn);
                
                // COMMIT
                conn.commit();
                
                // Reload order
                Optional<Order> reloadedOrder = orderDao.findById(orderId);
                if (reloadedOrder.isPresent()) {
                    Order fullOrder = reloadedOrder.get();
                    fullOrder.setItems(orderItemDao.findByOrderId(orderId));
                    return fullOrder;
                }
                
                return order;
                
            } catch (SQLException e) {
                if (conn != null) {
                    conn.rollback();
                }
                LOGGER.log(Level.SEVERE, "Error during membership checkout transaction", e);
                throw new RuntimeException("Membership checkout failed: " + e.getMessage(), e);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error establishing transaction connection", e);
            throw new RuntimeException("Membership checkout failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error restoring auto-commit", e);
                }
            }
        }
    }

    @Override
    public Order checkoutWithMembership(Long userId, Long membershipId, PaymentMethod paymentMethod,
                                        String deliveryName, String deliveryAddress, String deliveryPhone,
                                        DeliveryMethod deliveryMethod) 
            throws EmptyCartException, InsufficientStockException {
        
        // Get membership
        com.gym.dao.membership.MembershipDao membershipDao = new com.gym.dao.membership.MembershipDao();
        Optional<com.gym.model.membership.Membership> membershipOpt = membershipDao.findById(membershipId);
        if (membershipOpt.isEmpty()) {
            throw new RuntimeException("Membership not found: " + membershipId);
        }
        com.gym.model.membership.Membership membership = membershipOpt.get();
        
        // Get cart items (allow empty cart since we have membership)
        List<CartItem> cartItems = cartDao.findByUserId(userId);
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        
        // Build order items: membership + cart items
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<Long, Integer> productQuantities = new HashMap<>();
        
        // Add membership as order item
        OrderItem membershipOrderItem = new OrderItem();
        membershipOrderItem.setProductId(null); // Membership is not a product
        membershipOrderItem.setProductName("Gói thành viên: " + membership.getDisplayName());
        membershipOrderItem.setQuantity(1);
        membershipOrderItem.setUnitPrice(membership.getPrice());
        membershipOrderItem.setDiscountAmount(BigDecimal.ZERO);
        membershipOrderItem.setSubtotal(membership.getPrice());
        orderItems.add(membershipOrderItem);
        totalAmount = totalAmount.add(membership.getPrice());
        
        // Add cart items to order items
        for (CartItem cartItem : cartItems) {
            Optional<Product> productOpt = productDao.findById(cartItem.getProductId());
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found: " + cartItem.getProductId());
            }
            
            Product product = productOpt.get();
            
            // Check stock
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new InsufficientStockException(
                    "Sản phẩm " + product.getProductName() + " không đủ tồn kho. " +
                    "Còn lại: " + product.getStockQuantity() + ", yêu cầu: " + cartItem.getQuantity(),
                    product.getProductId());
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getProductId());
            orderItem.setProductName(product.getProductName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setDiscountAmount(BigDecimal.ZERO);
            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItems.add(orderItem);
            
            totalAmount = totalAmount.add(orderItem.getSubtotal());
            productQuantities.put(product.getProductId(), cartItem.getQuantity());
        }
        
        // Generate order number
        String orderNumber = generateOrderNumber(userId);
        
        // Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNumber(orderNumber);
        order.setOrderDate(OffsetDateTime.now(ZoneOffset.UTC));
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setFinalAmount(totalAmount);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setDeliveryName(deliveryName);
        order.setDeliveryAddress(deliveryAddress);
        order.setDeliveryPhone(deliveryPhone);
        order.setDeliveryMethod(deliveryMethod);
        
        // Transaction
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }
            
            conn.setAutoCommit(false);
            
            try {
                // Insert order (pass connection for transaction)
                Long orderId = orderDao.insert(order, conn);
                order.setOrderId(orderId);
                
                // Insert order items (membership + products) (pass connection for transaction)
                orderItemDao.insertBatch(orderId, orderItems, conn);
                
                // Decrease stock for products only (membership doesn't have stock)
                if (!productQuantities.isEmpty()) {
                    productDao.decreaseStockBatch(productQuantities);
                }
                
                // Create user membership
                com.gym.dao.membership.UserMembershipDao userMembershipDao = 
                    new com.gym.dao.membership.UserMembershipDao();
                java.time.LocalDate startDate = java.time.LocalDate.now();
                java.time.LocalDate expiryDate = startDate.plusMonths(membership.getDurationMonths());
                userMembershipDao.create(userId, membershipId, startDate, expiryDate, orderId);
                
                // Clear cart (only after successful transaction)
                if (!cartItems.isEmpty()) {
                    cartDao.clear(userId);
                }
                
                // Update payment status (pass connection for transaction)
                orderDao.updatePayment(orderId, PaymentStatus.PENDING, paymentMethod, conn);
                
                // COMMIT
                conn.commit();
                
                // Reload order with items
                Optional<Order> reloadedOrder = orderDao.findById(orderId);
                if (reloadedOrder.isPresent()) {
                    Order fullOrder = reloadedOrder.get();
                    fullOrder.setItems(orderItemDao.findByOrderId(orderId));
                    return fullOrder;
                }
                
                return order;
                
            } catch (SQLException e) {
                // ROLLBACK on any error - cart is NOT cleared
                if (conn != null) {
                    conn.rollback();
                }
                LOGGER.log(Level.SEVERE, "Error during checkout with membership transaction", e);
                
                // Check if it's an insufficient stock error
                if (e.getMessage() != null && e.getMessage().contains("Insufficient stock")) {
                    throw new InsufficientStockException(e.getMessage(), null);
                }
                
                throw new RuntimeException("Checkout with membership failed: " + e.getMessage(), e);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error establishing transaction connection", e);
            throw new RuntimeException("Checkout with membership failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error restoring auto-commit", e);
                }
            }
        }
    }

    /**
     * Generate unique order number: ORDyyyyMMddHHmmssSSS-{userId}
     */
    private String generateOrderNumber(Long userId) {
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        return "ORD" + timestamp + "-" + userId;
    }
}

