package com.gym.service.shop;

/**
 * Exception thrown when attempting to checkout with empty cart
 */
public class EmptyCartException extends Exception {
    public EmptyCartException(String message) {
        super(message);
    }
}



