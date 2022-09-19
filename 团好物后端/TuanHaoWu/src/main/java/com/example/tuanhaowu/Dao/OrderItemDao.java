package com.example.tuanhaowu.Dao;

import com.example.tuanhaowu.Entity.Orderitem;
import net.sf.json.JSONArray;

import java.util.List;

public interface OrderItemDao {
    void saveOrderItem(Orderitem orderItem);
    List<Orderitem> getOrderItemsByOrderId(Integer orderId);

    /**
     *查询某个团购中某个商品的销售数量
     * @param itemId
     * @return 商品名称和销售数量
     * by Xu
     */
    JSONArray getOrderItemSellNum(int itemId);

    /**
     * 判断某个商品是否卖出去过，即是否在orderItem表里
     * @param itemId
     * @return 在则返回true，不在返回false
     * by Xu
     */
    Boolean isItemInOrderItem(int itemId);
}
