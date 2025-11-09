package service.shop;

import dao.ProductDAO;
import dao.shop.CartDao;
import dto.CartItemDTO;
import model.Product;
import model.shop.CartItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of CartService
 * Follows Single Responsibility Principle - handles cart business logic
 * Uses DAO for data access (Separation of Concerns)
 */
public class CartServiceImpl implements CartService {
    private static final Logger LOGGER = Logger.getLogger(CartServiceImpl.class.getName());
    
    private final CartDao cartDao;
    private final ProductDAO productDao;
    
    public CartServiceImpl() {
        this.cartDao = new CartDao();
        this.productDao = new ProductDAO();
    }
    
    // Constructor for dependency injection (testability)
    public CartServiceImpl(CartDao cartDao, ProductDAO productDao) {
        this.cartDao = cartDao;
        this.productDao = productDao;
    }

    @Override
    public List<CartItemDTO> view(Long userId) {
        try {
            LOGGER.info("Viewing cart for user: " + userId);
            
            List<CartItem> cartItems = cartDao.findByMemberId(userId.intValue());
            List<CartItemDTO> dtoList = new ArrayList<>();
            
            for (CartItem item : cartItems) {
                CartItemDTO dto = convertToDTO(item);
                dtoList.add(dto);
            }
            
            LOGGER.info("Found " + dtoList.size() + " items in cart");
            return dtoList;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error viewing cart for user: " + userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public void add(Long userId, Long productId, Integer quantity) {
        try {
            LOGGER.info(String.format("Adding product %d (qty: %d) to cart for user: %d", 
                                     productId, quantity, userId));
            
            // Validate quantity
            if (quantity == null || quantity <= 0) {
                throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
            }
            
            // Validate product exists
            Product product = productDao.findById(productId.intValue());
            if (product == null) {
                throw new IllegalArgumentException("Không tìm thấy sản phẩm");
            }
            
            // Check if product is active
            if (product.getActive() == null || !product.getActive()) {
                throw new IllegalArgumentException("Sản phẩm không còn kinh doanh");
            }
            
            // Add or update cart item
            cartDao.addOrUpdate(userId.intValue(), productId.intValue(), quantity);
            
            LOGGER.info("Successfully added product to cart");
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Validation error adding to cart: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding product to cart", e);
            throw new RuntimeException("Không thể thêm sản phẩm vào giỏ hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public void setQuantity(Long userId, Long productId, Integer quantity) {
        try {
            LOGGER.info(String.format("Updating cart item quantity for user %d, product %d to %d", 
                                     userId, productId, quantity));
            
            // Validate quantity
            if (quantity == null || quantity <= 0) {
                throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
            }
            
            cartDao.updateQuantity(userId.intValue(), productId.intValue(), quantity);
            
            LOGGER.info("Successfully updated cart item quantity");
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Validation error updating quantity: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating cart quantity", e);
            throw new RuntimeException("Không thể cập nhật số lượng: " + e.getMessage(), e);
        }
    }

    @Override
    public void remove(Long userId, Long productId) {
        try {
            LOGGER.info(String.format("Removing product %d from cart for user: %d", 
                                     productId, userId));
            
            cartDao.remove(userId.intValue(), productId.intValue());
            
            LOGGER.info("Successfully removed product from cart");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error removing product from cart", e);
            throw new RuntimeException("Không thể xóa sản phẩm: " + e.getMessage(), e);
        }
    }

    @Override
    public void clear(Long userId) {
        try {
            LOGGER.info("Clearing cart for user: " + userId);
            
            cartDao.clear(userId.intValue());
            
            LOGGER.info("Successfully cleared cart");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error clearing cart", e);
            throw new RuntimeException("Không thể xóa giỏ hàng: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal calculateTotal(List<CartItemDTO> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal total = BigDecimal.ZERO;
        for (CartItemDTO item : cartItems) {
            if (item.getSubtotal() != null) {
                total = total.add(item.getSubtotal());
            }
        }
        
        return total;
    }

    @Override
    public int getCartItemCount(Long userId) {
        try {
            List<CartItem> items = cartDao.findByMemberId(userId.intValue());
            return items != null ? items.size() : 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting cart item count", e);
            return 0;
        }
    }
    
    /**
     * Convert CartItem entity to DTO
     * Follows DTO pattern for data transfer
     */
    private CartItemDTO convertToDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartId(item.getCartId());
        dto.setMemberId(item.getMemberId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        
        // Get product details from joined product or fetch from DB
        if (item.getProduct() != null) {
            Product product = item.getProduct();
            dto.setProductName(product.getProductName());
            dto.setPrice(product.getPrice());
            dto.setUnit(product.getUnit());
            // Note: Product model doesn't have imagePath field
            dto.setImagePath(null);
        } else {
            // Fallback: fetch product details if not loaded
            Product product = productDao.findById(item.getProductId());
            if (product != null) {
                dto.setProductName(product.getProductName());
                dto.setPrice(product.getPrice());
                dto.setUnit(product.getUnit());
                // Note: Product model doesn't have imagePath field
                dto.setImagePath(null);
            }
        }
        
        return dto;
    }
}

