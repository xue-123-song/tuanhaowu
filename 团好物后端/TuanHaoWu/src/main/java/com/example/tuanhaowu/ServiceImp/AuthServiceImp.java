package com.example.tuanhaowu.ServiceImp;

import com.example.tuanhaowu.Dao.GroupDao;
import com.example.tuanhaowu.Dao.ItemDao;
import com.example.tuanhaowu.Dao.OrderDao;
import com.example.tuanhaowu.Dao.UserDao;
import com.example.tuanhaowu.Entity.Group;
import com.example.tuanhaowu.Entity.Order;
import com.example.tuanhaowu.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImp implements AuthService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ItemDao itemDao;

    /**
     * 判断某个订单是否归属某个用户
     * @param userId
     * @param orderId
     * @return
     * by Xu
     */
    @Override
    public boolean isOrderOwner(String userId, int orderId) {
        List<Order> orders = orderDao.searchOrderByUserId(userId);
        System.out.println("orderUserId = "+userId);
        for (Order order : orders) {
            System.out.println("orderId = "+order.getOrderId());
            if (order.getOrderId() == orderId)
                return true;
        }
        return false;
    }

    /**
     * 判断用户是否是团购的团长
     * @param userId
     * @param groupId
     * @return
     * by Xu
     */
    @Override
    public boolean isGroupLeader(String userId, int groupId) {
        Group group = groupDao.getGroupByID(groupId);
        return  group.getGroupLeader().equals(userDao.getLeaderName(userId));
    }

    /**
     * 判断用户管理的是不是自己团购的订单
     * @param userId
     * @param orderId
     * @return
     * by Xu
     */
    @Override
    public boolean isOrderLeader(String userId, int orderId) {
        return isGroupLeader(userId,orderDao.getGroupIdByOrderId(orderId));
    }

    /**
     * 判断某个商品是不是属于某个团张团购的，用于删除商品的权限鉴定
     * @param userId
     * @param itemId
     * @return
     */
    @Override
    public boolean isItemLeader(String userId, int itemId) {
        return isGroupLeader(userId,itemDao.getGroupIdByItemId(itemId));
    }
}
