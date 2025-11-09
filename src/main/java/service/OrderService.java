
package service;

import java.util.List;
import model.shop.Order;
import dao.shop.OrderDao;

/*
    Note: 
 */
public class OrderService {

    private final OrderDao orderDAO;

    public OrderService() {
        orderDAO = new OrderDao();
    }

    // Order
    public List<Order> getAll() {
        List<Order> orders = orderDAO.findAll();
        return orders;
    }

    public Order getOrderById(int id) {
        return orderDAO.findById(id);
    }

    public int add(Order order) {
        return orderDAO.save(order);
    }

    public int update(Order order) {
        return orderDAO.update(order);
    }

    public int delete(Order order) {
        return orderDAO.delete(order);
    }
}
