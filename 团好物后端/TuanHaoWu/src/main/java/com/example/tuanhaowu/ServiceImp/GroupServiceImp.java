package com.example.tuanhaowu.ServiceImp;

import com.alibaba.fastjson.JSON;
import com.example.tuanhaowu.Dao.GroupDao;
import com.example.tuanhaowu.Dao.OrderDao;
import com.example.tuanhaowu.Dao.SubscribeDao;
import com.example.tuanhaowu.Dao.UserDao;
import com.example.tuanhaowu.Entity.Group;
import com.example.tuanhaowu.Entity.Order;
import com.example.tuanhaowu.Entity.Orderitem;
import com.example.tuanhaowu.Entity.Response.SuccessResponse;
import com.example.tuanhaowu.Entity.User;
import com.example.tuanhaowu.Service.GroupService;
import com.example.tuanhaowu.Service.OrderService;
import com.example.tuanhaowu.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class GroupServiceImp implements GroupService {

    @Autowired
    GroupDao groupDao;

    @Autowired
    OrderDao orderDao;
    @Autowired
    UserDao userDao;

    @Autowired
    OrderService orderService;

    @Autowired
    SubscribeDao subscribeDao;

    /*
    * 增加了picture字段
    * */
    @Override
    public List<Group> searchGroupByGroupLeader(String GroupLeader)
    {
        List<Group>  groups = groupDao.searchGroupByGroupLeader(GroupLeader);
        User gpLeader = userDao.getUserByUserName(GroupLeader);
        String picture = gpLeader.getPicture();
        for (Group group:groups)
        {
            /*增加subscribeId*/
            group = addSubscribeId(group);
            group.setPicture(picture);
        }
        return groups;
    }

    @Override
    public List<Group> searchAllGroup() {
        List<Group> groups =  groupDao.searchAllGroup();
        String gpLeader = "",picture = "";
        for (Group group:groups)
        {
            if (!gpLeader.equals(group.getGroupLeader())) {
                picture = getPictureOfGroup(group);
            }
            group = addSubscribeId(group);
            group.setPicture(picture);
        }
        return groups;
    }

    @Override
    public Group getGroupByID(Integer groupID) {
        Group group =  groupDao.getGroupByID(groupID);
        group = addSubscribeId(group);
        group.setPicture(getPictureOfGroup(group));
        return group;
    }

    @Override
    public Group CreateGroup(String gpLeader, String gpTitle, String gpDescription, Integer logistics, Timestamp startTime, Timestamp endTime) {
        Group group = new Group();
        group.setGroupLeader(gpLeader);
        group.setGroupTitle(gpTitle);
        group.setGroupDescription(gpDescription);
        group.setLogistics(logistics);
        group.setStartTime(startTime);
        group.setEndTime(endTime);
        group.setStatus(1);
        return groupDao.saveOneGroup(group);
    }

    @Override
    public Group saveOneGroup(Group newGroup) {
        return groupDao.saveOneGroup(newGroup);
    }

    @Override
    public Group searchGroupByGroupId(int groupId) {
        Group group = groupDao.searchGroupByGroupId(groupId);
        group.setPicture(getPictureOfGroup(group));

        return addSubscribeId(group);
    }

    @Override
    public int deleteGroupByID(int groupID) {
        Group group = groupDao.getGroupByID(groupID);
        List<Order> orders = group.getOrders();
        int status = 0;
        for(Order order : orders)
        {
            status = order.getOrderStatus();
            if (status == 0 || status == 1 || status == 2)
                return -1;
        }
        try {
            group.setStatus(-1);
            groupDao.saveOneGroup(group);
            return 0;
        }catch (Exception e)
        {
            return -2;//出现异常
        }

    }

    @Override
    public SuccessResponse cancelGroup(int groupId) {
        Group group = groupDao.getGroupByID(groupId);
        SuccessResponse successResponse = new SuccessResponse();
        String leaderName = group.getGroupLeader();
        User leader = userDao.getUserByUserName(leaderName);
        if (group == null)
        {
            successResponse.setSuccess(0);
            successResponse.setMeg("团购不存在！");
            return successResponse;
        }
        group.setStatus(0);
        //获取所有订单，取消订单
        List<Order> orders = orderDao.getOrdersByGroupId(groupId);
        //获取所有订单总额，从团长余额中给用户退款
        int totalPrice = 0;
        for (int i = 0; i < orders.size(); i++)
        {
            int price = orderService.getOrderTotalPrice(orders.get(i).getOrderId());
            totalPrice += price;
        }
        //团长余额不足
        if (totalPrice > leader.getMoney())
        {
            successResponse.setSuccess(0);
            successResponse.setMeg("团长余额不足，无法取消");
            return successResponse;
        }
        //取消订单,cancelOrder函数中已包含对团长余额的修改
        for(int i = 0; i < orders.size(); i++)
        {
            int orderId = orders.get(i).getOrderId();
            orderService.cancelOrder(orderId);
        }
        groupDao.saveOneGroup(group);
        successResponse.setSuccess(1);
        successResponse.setMeg("成功取消团购！");
        return successResponse;
    }

    /**
     * 团购信息的模糊查询，根据三种条件去查询
     * by Xu
     * */
    @Override
    public List<Group> FuzzyQueryByGroupLeader(String groupLeader) {return groupDao.FuzzyQueryByGroupLeader(groupLeader);}

    @Override
    public List<Group> FuzzyQueryByGroupTitle(String groupTitle) {
        return groupDao.FuzzyQueryByGroupTitle(groupTitle);
    }

    @Override
    public List<Group> FuzzyQueryByItemName(String itemName) {
        return groupDao.FuzzyQueryByItemName(itemName);
    }

    /**
     *团购信息的统计业务实现，查询参与团购的人数，不包含取消订单的人数
     * by Xu
     * */
    @Override
    public  int getGroupParticipants(List<Order> orders) {
        int size = getOrderNum(orders)[0];
        int num = 1,j = 0;
        String [] a =new String[size];
        for (int i = 0; i < size; ++i )
        {
            if (orders.get(i).getOrderStatus() > 0) {
                a[j] = orders.get(i).getBelongUserid();
                j++;
            }
        }
        Arrays.sort(a);
        System.out.println(JSON.toJSONString(a)+"j = "+j);
        for(int i = 1; i < j+1; i++) {
            System.out.println("a[i] = "+a[i]);
            if (!Objects.equals(a[i], a[i - 1])) {
                System.out.println("i = " +i);
                num ++;
                System.out.println("num in cycle = "+num);
            }
        }
        System.out.println("num = "+num);
        for (String str : a) System.out.println(str);
        return num;
    }
    /**
     * 统计团购中的销售额
     * 返回一个int类型的数组，第一个元素保存的是销售额，第二个元素是退款金额
     * by Xy
     * */
    @Override
    public int[] getGroupSales(List<Order> orders) {
        int[] sales = new int [2];
        int status;
        for (Order order : orders) {
           status = order.getOrderStatus();
           //System.out.println("orderStatus = "+status);
            List<Orderitem> items = order.getOrderitemList();
            /*计算销售额，理论上应该时包含了所有已经支付了的订单金额，和已经完成了的订单金额*/
            if ((status < 4  && status > 0 ) || (status == -103 || status == -13 || status == -3)) {
                for (Orderitem tmp : items) {
                   sales[0] += tmp.getItemBuynum() * tmp.getItemPrice();
               }
           }//所有取消的订单计算退款总金额
           else if(status == 4 || status == -4 || status == -14 || status == -104)
           {
               for (Orderitem tmp : items) {
                   sales[1] += tmp.getItemBuynum() * tmp.getItemPrice();
               }
           }
        }
        return sales;
    }

    /**
     * 计算已经支付的订单数量
     * 返回一个int类型的数组，第一个元素代表的是已经支付且没有取消的有效订单数量，第二个元素代表取消的订单数量
     *
     * @return*/
    @Override
    public int[] getOrderNum(List<Order> orders) {
        int[] orderNum = new int[2];
        int status ;
        for(Order order : orders)
        {
            status = order.getOrderStatus();
            if (status !=0 && status != -4 && status != -14 && status != 4 && status != -104 )
            {
                orderNum[0]++;
            }
            else if (status == -4 || status == -14 || status == 4 || status == -104)
            {
                orderNum[1]++;
            }
        }
        return orderNum;
    }

    @Override
    public String getPictureOfGroup(Group group) {
        User user = userDao.getUserByUserName(group.getGroupLeader());
        return user.getPicture();
    }

    @Override
    public Group addSubscribeId(Group group) {
        String userId = AuthUtil.getAuthUserId();
        Integer  subscribeId = subscribeDao.get_SubscribeId_ByUserIdAndGroupId(userId,group.getGroupId());
        group.setSubscribeId(subscribeId);
        return group;
    }


}
