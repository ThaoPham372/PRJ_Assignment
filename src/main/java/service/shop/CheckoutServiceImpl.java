package service.shop;

import dao.ProductDAO;
import dao.shop.OrderDao;
import dao.shop.OrderItemDao;
import dto.CartItemDTO;
import exception.EmptyCartException;
import exception.InsufficientStockException;
import model.Product;
import model.shop.*;
import service.shop.PaymentService;
import service.shop.PaymentServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of CheckoutService
 * Follows Single Responsibility Principle
 * Handles order creation, stock validation, and payment processing
 */
public class CheckoutServiceImpl implements CheckoutService {
    private static final Logger LOGGER = Logger.getLogger(CheckoutServiceImpl.class.getName());
    
    private final CartService cartService;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final ProductDAO productDao;
    private final PaymentService paymentService;
    
    public CheckoutServiceImpl() {
        this.cartService = new CartServiceImpl();
        this.orderDao = new OrderDao();
        this.orderItemDao = new OrderItemDao();
        this.productDao = new ProductDAO();
        this.paymentService = new PaymentServiceImpl();
    }
    
    // Constructor for dependency injection
    public CheckoutServiceImpl(CartService cartService, 
                              OrderDao orderDao, OrderItemDao orderItemDao, 
                              ProductDAO productDao, PaymentService paymentService) {
        this.cartService = cartService;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.productDao = productDao;
        this.paymentService = paymentService;
    }

    @Override
    public Order checkout(Long userId, PaymentMethod paymentMethod, 
                         String deliveryName, String deliveryAddress, 
                         String deliveryPhone, DeliveryMethod deliveryMethod) 
                         throws EmptyCartException, InsufficientStockException {
        
        LOGGER.info(String.format("Starting checkout for user: %d", userId));
        
        try {
            // 1. Get cart items
            List<CartItemDTO> cartItems = cartService.view(userId);
            if (cartItems == null || cartItems.isEmpty()) {
                throw new EmptyCartException();
            }
            
            // 2. Validate stock availability
            validateStockAvailability(cartItems);
            
            // 3. Calculate total
            BigDecimal totalAmount = cartService.calculateTotal(cartItems);
            
            // 4. Create order
            Order order = new Order();
            order.setMemberId(userId.intValue());
            order.setOrderNumber(generateOrderNumber());
            order.setOrderDate(LocalDateTime.now());
            order.setTotalAmount(totalAmount);
            order.setDiscountAmount(BigDecimal.ZERO);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setDeliveryMethod(deliveryMethod);
            order.setDeliveryAddress(deliveryAddress);
            order.setDeliveryPhone(deliveryPhone);
            order.setDeliveryName(deliveryName);
            order.setPaymentMethod(paymentMethod);
            order.setPaymentStatus(PaymentStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now());
            
            // Save order
            Integer orderId = orderDao.create(order);
            order.setOrderId(orderId);
            
            LOGGER.info("Order created with ID: " + orderId);
            
            // 5. Create order items from cart
            List<OrderItem> orderItems = createOrderItemsFromCart(cartItems);
            orderItemDao.insertBatch(orderId, orderItems);
            
            LOGGER.info("Order items created: " + orderItems.size());
            
            // 6. Update product stock
            updateProductStock(cartItems);
            
            // 7. Create payment record
            createPaymentForOrder(userId.intValue(), orderId, totalAmount, paymentMethod);
            
            // 8. Clear cart
            cartService.clear(userId);
            
            LOGGER.info("Checkout completed successfully");
            
            return order;
            
        } catch (EmptyCartException | InsufficientStockException e) {
            LOGGER.log(Level.WARNING, "Checkout validation failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during checkout", e);
            throw new RuntimeException("Không thể tạo đơn hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public Order checkoutPackage(Long userId, Long packageId, PaymentMethod paymentMethod,
                                String customerName, String customerPhone) {
        
        LOGGER.info(String.format("Starting package checkout for user: %d, package: %d", 
                                 userId, packageId));
        
        try {
            // 1. Get package details
            dao.PackageDAO packageDao = new dao.PackageDAO();
            model.Package pkg = packageDao.findById(packageId.intValue());
            
            if (pkg == null) {
                throw new IllegalArgumentException("Không tìm thấy gói thành viên");
            }
            
            // 2. Create order
            Order order = new Order();
            order.setMemberId(userId.intValue());
            order.setOrderNumber(generateOrderNumber());
            order.setOrderDate(LocalDateTime.now());
            order.setTotalAmount(pkg.getPrice());
            order.setDiscountAmount(BigDecimal.ZERO);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setDeliveryMethod(DeliveryMethod.PICKUP); // Package is pickup only
            order.setDeliveryPhone(customerPhone);
            order.setDeliveryName(customerName);
            order.setPaymentMethod(paymentMethod);
            order.setPaymentStatus(PaymentStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now());
            
            // Save order
            Integer orderId = orderDao.create(order);
            order.setOrderId(orderId);
            
            LOGGER.info("Package order created with ID: " + orderId);
            
            // 3. Create order item for package
            List<OrderItem> orderItems = new ArrayList<>();
            OrderItem packageItem = new OrderItem();
            packageItem.setOrderId(orderId);
            packageItem.setPackageId(packageId.intValue());
            packageItem.setProductName(pkg.getName());
            packageItem.setQuantity(1);
            packageItem.setUnitPrice(pkg.getPrice());
            packageItem.setDiscountAmount(BigDecimal.ZERO);
            packageItem.setDiscountPercent(BigDecimal.ZERO);
            orderItems.add(packageItem);
            
            orderItemDao.insertBatch(orderId, orderItems);
            
            // 4. Create payment record for package order
            createPaymentForOrder(userId.intValue(), orderId, pkg.getPrice(), paymentMethod);
            
            LOGGER.info("Package checkout completed successfully");
            
            return order;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during package checkout", e);
            throw new RuntimeException("Không thể tạo đơn hàng gói thành viên: " + e.getMessage(), e);
        }
    }

    @Override
    public String processMoMoPayment(Integer orderId, String baseUrl) {
        LOGGER.info("Processing MoMo payment for order: " + orderId);
        
        // TODO: Implement MoMo payment integration
        // This would typically involve:
        // 1. Create payment request to MoMo API
        // 2. Get payment URL from MoMo
        // 3. Return payment URL for redirect
        
        // For now, return null (payment gateway not implemented)
        LOGGER.warning("MoMo payment integration not implemented");
        return null;
    }
    
    @Override
    public String processVNPayPayment(Integer orderId, Long amount, String orderInfo, String ipAddress, String baseUrl) {
        LOGGER.info("Processing VNPay payment for order: " + orderId + ", amount: " + amount);
        
        try {
            VNPayService vnPayService = new VNPayService();
            // Build return URL dynamically from baseUrl
            String returnUrl = baseUrl + "/vnpay-return";
            String paymentUrl = vnPayService.buildPaymentUrl(orderId, amount, orderInfo, ipAddress, returnUrl);
            LOGGER.info("VNPay payment URL generated successfully");
            return paymentUrl;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing VNPay payment", e);
            throw new RuntimeException("Không thể tạo URL thanh toán VNPay: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validate stock availability for all cart items
     */
    private void validateStockAvailability(List<CartItemDTO> cartItems) 
            throws InsufficientStockException {
        
        for (CartItemDTO cartItem : cartItems) {
            Product product = productDao.findById(cartItem.getProductId());
            
            if (product == null) {
                throw new RuntimeException("Sản phẩm không tồn tại: " + cartItem.getProductName());
            }
            
            if (product.getActive() == null || !product.getActive()) {
                throw new RuntimeException("Sản phẩm không còn kinh doanh: " + cartItem.getProductName());
            }
            
            if (product.getStockQuantity() == null || 
                product.getStockQuantity() < cartItem.getQuantity()) {
                throw new InsufficientStockException(
                    cartItem.getProductId(),
                    cartItem.getProductName(),
                    cartItem.getQuantity(),
                    product.getStockQuantity()
                );
            }
        }
    }
    
    /**
     * Create order items from cart items
     */
    private List<OrderItem> createOrderItemsFromCart(List<CartItemDTO> cartItems) {
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (CartItemDTO cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getPrice());
            orderItem.setDiscountAmount(BigDecimal.ZERO);
            orderItem.setDiscountPercent(BigDecimal.ZERO);
            
            orderItems.add(orderItem);
        }
        
        return orderItems;
    }
    
    /**
     * Update product stock after checkout
     */
    private void updateProductStock(List<CartItemDTO> cartItems) {
        for (CartItemDTO cartItem : cartItems) {
            Product product = productDao.findById(cartItem.getProductId());
            if (product != null) {
                int newStock = product.getStockQuantity() - cartItem.getQuantity();
                product.setStockQuantity(newStock);
                productDao.update(product);
                
                LOGGER.info(String.format("Updated stock for product %d: %d -> %d",
                                         product.getProductId(), 
                                         product.getStockQuantity() + cartItem.getQuantity(),
                                         newStock));
            }
        }
    }
    
    /**
     * Generate unique order number
     */
    private String generateOrderNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        int random = (int) (Math.random() * 1000);
        return "ORD" + timestamp + String.format("%03d", random);
    }
    
    /**
     * Create payment record for an order
     * Follows Single Responsibility Principle - handles payment creation logic
     * Payment status will be PENDING regardless of payment method
     * 
     * @param userId User ID
     * @param orderId Order ID
     * @param amount Payment amount
     * @param paymentMethod Payment method
     */
    private void createPaymentForOrder(Integer userId, Integer orderId, 
                                      BigDecimal amount, PaymentMethod paymentMethod) {
        try {
            String referenceId = "ORDER-" + orderId + "-" + System.currentTimeMillis();
            Integer paymentId = paymentService.createPaymentForOrder(
                userId,
                orderId.longValue(),
                amount,
                paymentMethod,
                referenceId,
                null // No shared EntityManager
            );
            LOGGER.info(String.format("Payment record created - ID: %d, Order: %d, Amount: %s, Method: %s, Status: PENDING",
                paymentId, orderId, amount, paymentMethod));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, 
                String.format("Failed to create payment record for order: %d", orderId), e);
            // Don't fail checkout if payment record creation fails - order is already created
        }
    }
}

