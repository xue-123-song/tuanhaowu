package com.example.tuanhaowu.DaoImp;

import com.example.tuanhaowu.Dao.OrderDao;
import com.example.tuanhaowu.Entity.Item;
import com.example.tuanhaowu.Entity.Order;
import com.example.tuanhaowu.Entity.Orderitem;
import com.example.tuanhaowu.Entity.Request.JoinGroupRequest;
import com.example.tuanhaowu.Repository.ItemRepository;
import com.example.tuanhaowu.Repository.OrderItemRepository;
import com.example.tuanhaowu.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDaoImp implements OrderDao {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ItemRepository itemRepository;

    @Override
    public List<Order> searchOrderByUserId(String userId) {
        try {
            return orderRepository.searchAllByBelongUserid(userId);
        }catch (Error err)
        {
            System.out.println("get null order");
            return null;

        }

    }

    @Override
    public void joinGroup(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Order searchOrderByUserIdAndGroupId(String userId, int groupId) {
        return orderRepository.getOrderByBelongUseridAndGroupId(userId, groupId);
    }

    @Override
    public void payGroup(Order order) {
        orderRepository.save(order);
        List<Orderitem> orderitemList =  orderItemRepository.getOrderitemsByBelongOrderId(order.getOrderId());
        for(Orderitem orderitem:orderitemList)
        {
            Item item = itemRepository.getItemByItemId(orderitem.getItemId());
            item.setNumber(item.getNumber() + 1);
            itemRepository.save(item);
        }
    }

    @Override
    public Order searchOrderByUserIdAndOrderId(String userId, int orderId) {
        return orderRepository.getOrderByBelongUseridAndOrderId(userId, orderId);
    }

    @Override
    public Order searchOrderByOrderId(int orderId) {
        return orderRepository.getOrderByOrderId(orderId);
    }

    @Override
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Order saveOneOrder(Order order) {
        return  orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByGroupId(int groupId) {
        return orderRepository.getOrdersByGroupId(groupId);
    }

    @Override
    public int getGroupIdByOrderId(int orderId) {
        Order order = orderRepository.getOrderByOrderId(orderId);
        return order.getGroupId();
    }


}
