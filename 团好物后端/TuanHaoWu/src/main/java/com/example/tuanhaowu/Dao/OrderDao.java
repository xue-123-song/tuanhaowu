package com.example.tuanhaowu.Dao;

import com.example.tuanhaowu.Entity.Order;



import java.util.List;

public interface OrderDao {
    List<Order> searchOrderByUserId(String userId);

    void joinGroup(Order order);

    Order searchOrderByUserIdAndGroupId(String userId, int groupId);

    void payGroup(Order order);

    Order searchOrderByUserIdAndOrderId(String userId, int OrderId);

    Order searchOrderByOrderId(int orderId);

    void saveOrder(Order order);
    Order saveOneOrder(Order order);

    List<Order> getOrdersByGroupId(int groupId);
    int getGroupIdByOrderId(int orderId);


}
