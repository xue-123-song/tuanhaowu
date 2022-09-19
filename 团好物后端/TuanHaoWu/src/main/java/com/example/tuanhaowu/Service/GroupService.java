package com.example.tuanhaowu.Service;

import com.example.tuanhaowu.Entity.Group;
import com.example.tuanhaowu.Entity.Order;
import com.example.tuanhaowu.Entity.Response.SuccessResponse;

import java.sql.Timestamp;
import java.util.List;

public interface GroupService {
    List<Group> searchGroupByGroupLeader(String GroupLeader);
    List<Group> searchAllGroup();
    Group getGroupByID(Integer groupID);
    Group CreateGroup(String gpLeader, String gpTitle, String gpDescription, Integer logistics, Timestamp startTime, Timestamp endTime);
    Group saveOneGroup(Group newGroup);
    Group searchGroupByGroupId(int groupId);
    int deleteGroupByID(int groupID);

    //取消所有订单，包括设置订单状态和退款，并设置团购状态
    SuccessResponse cancelGroup(int groupId);

    /**模糊查询h*/
    List<Group> FuzzyQueryByGroupLeader(String groupLeader);
    List<Group> FuzzyQueryByGroupTitle(String groupTitle);
    List<Group> FuzzyQueryByItemName(String itemName);

    /**团购信息统计功能函数*/
    int getGroupParticipants(List<Order> orders);
    int[] getGroupSales(List<Order> orders);
    int[] getOrderNum(List<Order> orders);

    /**工具函数，给group添加picture*/
    String getPictureOfGroup(Group group);
    /**工具函数，给团购增加subscribeId
     * @return*/
    Group addSubscribeId(Group group);

}
