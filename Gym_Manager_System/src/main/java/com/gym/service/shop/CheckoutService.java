package com.gym.service.shop;

import com.gym.model.shop.Order;
import com.gym.model.shop.PaymentMethod;
import com.gym.model.shop.DeliveryMethod;

/**
 * Service interface for Checkout operations
 */
public interface CheckoutService {
    /**
     * Process checkout and create order
     * @param userId user ID
     * @param paymentMethod payment method
     * @param deliveryName delivery name
     * @param deliveryAddress delivery address (required for delivery method)
     * @param deliveryPhone delivery phone
     * @param deliveryMethod delivery method (PICKUP or DELIVERY)
     * @return created order with items
     * @throws EmptyCartException if cart is empty
     * @throws InsufficientStockException if stock is insufficient
     */
    Order checkout(Long userId, PaymentMethod paymentMethod, String deliveryName, 
                   String deliveryAddress, String deliveryPhone, DeliveryMethod deliveryMethod) 
            throws EmptyCartException, InsufficientStockException;
    
    /**
     * Get order by ID
     */
    Order getOrder(Long orderId);
    
    /**
     * Process MoMo payment and return payment URL
     * @param orderId order ID
     * @param baseUrl base URL of application (for callback URLs)
     * @return MoMo payment URL for redirect
     */
    String processMoMoPayment(Long orderId, String baseUrl);
    
    /**
     * Process package checkout and create order + membership
     * @param userId user ID
     * @param packageId package ID (from packages table)
     * @param paymentMethod payment method
     * @param deliveryName customer name
     * @param deliveryPhone customer phone
     * @return created order
     */
    Order checkoutPackage(Long userId, Long packageId, PaymentMethod paymentMethod,
                         String deliveryName, String deliveryPhone);
    
    /**
     * Process checkout with both membership and cart items
     * @param userId user ID
     * @param membershipId membership ID
     * @param paymentMethod payment method
     * @param deliveryName delivery name
     * @param deliveryAddress delivery address (required for delivery method)
     * @param deliveryPhone delivery phone
     * @param deliveryMethod delivery method (PICKUP or DELIVERY)
     * @return created order with items
     * @throws EmptyCartException if cart is empty (membership checkout should still proceed)
     * @throws InsufficientStockException if stock is insufficient
     */
    Order checkoutWithMembership(Long userId, Long membershipId, PaymentMethod paymentMethod,
                                 String deliveryName, String deliveryAddress, String deliveryPhone, 
                                 DeliveryMethod deliveryMethod) 
            throws EmptyCartException, InsufficientStockException;
}

