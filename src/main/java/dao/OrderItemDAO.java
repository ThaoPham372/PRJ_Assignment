package dao;

import java.util.List;
import model.shop.OrderItem;

/*
    Note: 
 */
public class OrderItemDAO {

    GenericDAO<OrderItem> genericDAO;

    public OrderItemDAO() {
        genericDAO = new GenericDAO<>(OrderItem.class);
    }

    public int save(OrderItem orderItem) {
        genericDAO.save(orderItem);
        return orderItem.getOrderItemId();
    }

    public List<OrderItem> findAll() {
        System.out.println(">>OrderItem: FIND ALL");
        System.out.println("result: ");
        List<OrderItem> orderItems = genericDAO.findAll();
        if (orderItems != null) {
            System.out.println("");
            for (OrderItem a : orderItems) {
                System.out.println(a);
            }
        }
        System.out.println("-----------------------------");
        return orderItems != null ? orderItems : List.of();
    }

    public OrderItem findById(int id) {
        System.out.println(">>OrderItem: Find by ID");
        OrderItem orderItem = genericDAO.findById(id);
        System.out.println("result: " + orderItem);
        System.out.println("-------------------------------");
        return orderItem;
    }
    public int update(OrderItem orderItem) {
        System.out.println(">>OrderItem: Update");
        int id = genericDAO.update(orderItem);
        System.out.println("Result id: " + id);
        System.out.println("---------------------------");
        return id;
    }

}
