package com.gym.service.shop;

import com.gym.dao.shop.CartDao;
import com.gym.model.shop.CartItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation of CartService
 */
public class CartServiceImpl implements CartService {
    private final CartDao cartDao;

    public CartServiceImpl() {
        this.cartDao = new CartDao();
    }

    @Override
    public List<CartItem> view(Long userId) {
        return cartDao.findByUserId(userId);
    }

    @Override
    public void add(Long userId, Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        cartDao.addOrUpdate(userId, productId, quantity);
    }

    @Override
    public void setQuantity(Long userId, Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        cartDao.updateQuantity(userId, productId, quantity);
    }

    @Override
    public void remove(Long userId, Long productId) {
        cartDao.remove(userId, productId);
    }

    @Override
    public BigDecimal calculateTotal(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }
        
        return total;
    }
}



