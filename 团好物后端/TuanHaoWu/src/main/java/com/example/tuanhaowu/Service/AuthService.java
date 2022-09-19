package com.example.tuanhaowu.Service;

/**
 * Note：该类是用于权限鉴定的，你可以根据自己的需求进行添加，加油！
 * created by Xu
 */
public interface AuthService {
    /**
     * 判断某个订单是不是某个用户所拥有的
     * by Xu
     */
    boolean isOrderOwner(String userId, int orderId);
    /**
     * 判断是不是这个group的groupLeader，来鉴定是否有权限进行操作
     * by Xu
     */
    boolean isGroupLeader(String userId, int groupId);

    /**
     * 判断某个订单是不是归团长管
     * by Xu
     */
    boolean isOrderLeader(String userId,int orderId);
    /**
     * 判断某个item是不是某个团长管理的
     * by Xu
     */
    boolean isItemLeader(String userId,int ItemId);


}
