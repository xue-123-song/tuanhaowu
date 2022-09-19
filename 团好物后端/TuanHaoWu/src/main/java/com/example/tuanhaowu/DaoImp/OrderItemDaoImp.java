package com.example.tuanhaowu.DaoImp;

import com.example.tuanhaowu.Dao.OrderItemDao;
import com.example.tuanhaowu.Entity.Orderitem;
import com.example.tuanhaowu.Repository.OrderItemRepository;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemDaoImp implements OrderItemDao {

    @Autowired
    OrderItemRepository orderItemRepository;
    @Override
    public void saveOrderItem(Orderitem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Override
    public List<Orderitem> getOrderItemsByOrderId(Integer orderId) {
        return orderItemRepository.getOrderitemsByBelongOrderId(orderId);
    }

    /**
     *查询某个团购中某个商品的销售数量
     * @param itemId
     * @return 商品名称和销售数量
     * by Xu
     */
    @Override
    public JSONArray getOrderItemSellNum(int itemId) {
        return orderItemRepository.getOrderItemSellNum(itemId);
    }

    /**
     * 判断某个商品是否卖出去过，即是否在orderItem表里
     * @param itemId
     * @return 在则返回true，不在返回false
     * by Xu
     */
    @Override
    public Boolean isItemInOrderItem(int itemId) {
        List<Orderitem> orderitem = orderItemRepository.getOrderitemByItemId(itemId);
        return !orderitem.isEmpty();
    }
}

