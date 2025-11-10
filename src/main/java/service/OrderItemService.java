
package service;

import dao.OrderItemDAO;
import java.util.List;
import model.shop.OrderItem;

/*
    Note: 
 */
public class OrderItemService {

    private final OrderItemDAO orderItemDAO;

    public OrderItemService() {
        orderItemDAO = new OrderItemDAO();
    }

    // OrderItem
    public List<OrderItem> getAll() {
        List<OrderItem> orderItems = orderItemDAO.findAll();
        return orderItems;
    }

    public OrderItem getOrderItemById(int id) {
        return orderItemDAO.findById(id);
    }

    public int add(OrderItem orderItem) {
        return orderItemDAO.save(orderItem);
    }

    public int update(OrderItem orderItem) {
        return orderItemDAO.update(orderItem);
    }
}
