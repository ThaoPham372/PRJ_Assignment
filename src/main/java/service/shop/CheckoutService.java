package service.shop;

import model.shop.DeliveryMethod;
import model.shop.Order;
import model.shop.PaymentMethod;
import exception.EmptyCartException;
import exception.InsufficientStockException;

/**
 * Service interface for Checkout operations
 * Follows Interface Segregation Principle
 */
public interface CheckoutService {
    
    /**
     * Checkout cart items and create order
     * @param userId User ID
     * @param paymentMethod Payment method
     * @param deliveryName Delivery recipient name
     * @param deliveryAddress Delivery address (for DELIVERY method)
     * @param deliveryPhone Delivery phone
     * @param deliveryMethod Delivery method (PICKUP or DELIVERY)
     * @return Created order
     * @throws EmptyCartException if cart is empty
     * @throws InsufficientStockException if product stock is insufficient
     */
    Order checkout(Long userId, PaymentMethod paymentMethod, 
                  String deliveryName, String deliveryAddress, 
                  String deliveryPhone, DeliveryMethod deliveryMethod) 
                  throws EmptyCartException, InsufficientStockException;
    
    /**
     * Checkout membership package (no cart items)
     * @param userId User ID
     * @param packageId Package ID
     * @param paymentMethod Payment method
     * @param customerName Customer name
     * @param customerPhone Customer phone
     * @return Created order
     */
    Order checkoutPackage(Long userId, Long packageId, PaymentMethod paymentMethod,
                         String customerName, String customerPhone);
    
    /**
     * Process MoMo payment for order
     * @param orderId Order ID
     * @param baseUrl Base URL for callback
     * @return MoMo payment URL
     */
    String processMoMoPayment(Integer orderId, String baseUrl);
}

