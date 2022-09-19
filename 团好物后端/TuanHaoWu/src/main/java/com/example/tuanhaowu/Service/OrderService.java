package com.example.tuanhaowu.Service;

import com.example.tuanhaowu.Entity.Order;
import com.example.tuanhaowu.Entity.Request.JoinGroupRequest;
import com.example.tuanhaowu.Entity.Request.PayOrderRequest;
import com.example.tuanhaowu.Entity.Request.SecKillRequest;
import com.example.tuanhaowu.Entity.Response.SecKillResponse;
import com.example.tuanhaowu.Entity.Response.SuccessResponse;

import java.util.List;

public interface OrderService {
    List<Order> searchOrderByUserId(String userId);

    Order joinGroup(JoinGroupRequest joinGroupRequest);
    /*
     *支付订单
     * 会判断用户余额是否充足，不够的话支付失败
     */
    SuccessResponse payOrder(PayOrderRequest payOrderRequest);

    /*
     *取消订单
     * 参数为订单的id
     * 设置订单状态为被取消，然后给用户退款
     */
    SuccessResponse cancelOrder(Integer orderId);
    int getOrderTotalPrice(int orderId);
    /*

    /*
     * 团长发货
     * */
    SuccessResponse deliverOrder(Integer orderId);

    /*
     * 团员收货
     * */
    SuccessResponse receiveOrder(Integer orderId);
    /**
     * 修改订单信息
     * by Xu
     * */
    Order modifyOrderInfo(int orderId,String tel,String address,String receiverName);
    /**
     * 团长删除订单
     * by Xu
     */
    int LeaderDeleteOrder(Integer orderId);
    /**
     * 团员删除订单
     * by Xu
     */
    int MemberDeleteOrder(Integer orderId);

    SecKillResponse secKill(SecKillRequest secKillRequest);

    /**
     * 根据orderId找到所属的groupId
     * @param orderId
     * @return
     */
    int getGroupIdByOrderId(int orderId);


}
