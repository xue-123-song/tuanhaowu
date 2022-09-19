package com.example.tuanhaowu.Repository;

import com.example.tuanhaowu.Entity.Orderitem;
import net.sf.json.JSONArray;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<Orderitem, Integer> {

    @Query(value = "from Orderitem where belongOrderId = :orderId")
    List<Orderitem> getOrderitemsByBelongOrderId(@Param("orderId") Integer orderId);

    /**
     * @param itemId
     * @return 查询商品名称和对应的销售数量
     */
    @Query(value = "select obj.itemName ,sum(obj.itemBuynum) as sales from Orderitem obj where obj.itemId =:itemId group by obj.itemName")
    JSONArray getOrderItemSellNum(@Param("itemId") int itemId);

    @Query(value = "from Orderitem where itemId =:ItemId")
    List<Orderitem> getOrderitemByItemId(@Param("ItemId") Integer ItemId);

}
