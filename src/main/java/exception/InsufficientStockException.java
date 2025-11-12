package exception;

/**
 * Exception thrown when product stock is insufficient for checkout
 * Follows Exception Handling best practices
 */
public class InsufficientStockException extends RuntimeException {
    private Integer productId;
    private String productName;
    private Integer requestedQuantity;
    private Integer availableQuantity;
    
    public InsufficientStockException() {
        super("Sản phẩm không đủ tồn kho.");
    }
    
    public InsufficientStockException(String message) {
        super(message);
    }
    
    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public InsufficientStockException(Integer productId, String productName, 
                                     Integer requestedQuantity, Integer availableQuantity) {
        super(String.format("Sản phẩm '%s' không đủ tồn kho. Yêu cầu: %d, Có sẵn: %d", 
              productName, requestedQuantity, availableQuantity));
        this.productId = productId;
        this.productName = productName;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    // Getters
    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getRequestedQuantity() {
        return requestedQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }
}

