package com.example.tuanhaowu.Repository;

import com.example.tuanhaowu.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order,Integer> {
    @Query(value ="select  obj from Order obj  where obj.belongUserid = :userId and obj.orderStatus > -10")
    List<Order> searchAllByBelongUserid(@Param("userId")String userId);

    @Query(value = "from Order where belongUserid = :userId and groupId = :groupId")
    Order getOrderByBelongUseridAndGroupId(@Param("userId") String userId, @Param("groupId") Integer groupId);

    @Query(value = "from Order where belongUserid = :userId and orderId = :orderId")
    Order getOrderByBelongUseridAndOrderId(@Param("userId") String userId, @Param("orderId") Integer orderId);

    @Query(value = "from Order where orderId = :orderId")
    Order getOrderByOrderId(@Param("orderId") Integer orderId);

    @Query(value = "from Order where groupId = :groupId")
    List<Order> getOrdersByGroupId(@Param("groupId") Integer groupId);
}
