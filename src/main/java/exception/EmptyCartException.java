package exception;

/**
 * Exception thrown when attempting to checkout with an empty cart
 * Follows Exception Handling best practices
 */
public class EmptyCartException extends RuntimeException {
    
    public EmptyCartException() {
        super("Giỏ hàng trống. Vui lòng thêm sản phẩm trước khi thanh toán.");
    }
    
    public EmptyCartException(String message) {
        super(message);
    }
    
    public EmptyCartException(String message, Throwable cause) {
        super(message, cause);
    }
}

