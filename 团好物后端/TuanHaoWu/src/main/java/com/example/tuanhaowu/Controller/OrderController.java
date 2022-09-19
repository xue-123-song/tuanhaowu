package com.example.tuanhaowu.Controller;


import com.alibaba.fastjson.JSON;
import com.example.tuanhaowu.Dao.OrderDao;
import com.example.tuanhaowu.Entity.Group;
import com.example.tuanhaowu.Entity.Item;
import com.example.tuanhaowu.Entity.Order;
import com.example.tuanhaowu.Entity.Request.JoinGroupRequest;
import com.example.tuanhaowu.Entity.Request.PayOrderRequest;
import com.example.tuanhaowu.Entity.Request.SecKillRequest;
import com.example.tuanhaowu.Entity.Response.SecKillResponse;
import com.example.tuanhaowu.Entity.Response.SuccessResponse;
import com.example.tuanhaowu.Entity.User;
import com.example.tuanhaowu.Service.AuthService;
import com.example.tuanhaowu.Service.GroupService;
import com.example.tuanhaowu.Service.OrderService;
import com.example.tuanhaowu.Service.UserService;
import com.example.tuanhaowu.util.AuthUtil;
import com.example.tuanhaowu.util.MsgUtil.Msg;
import com.example.tuanhaowu.util.MsgUtil.MsgUtil;
import com.example.tuanhaowu.util.recommendUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import javax.crypto.MacSpi;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;
    @Autowired
    OrderDao orderDao;
    @Autowired
    AuthService authService;
    SuccessResponse successResponse;
    @Autowired
    recommendUtil recommendUtil;



    /*该函数是返回用户的订单
     * modified by Xu
     * 7/17 修改了传参方式，改为更简洁的路径传参
     * ！attention：这里返回的List里的order增加了name和picture，但是picture中url是随便写的，如果要在前端显示的话，建议新注册上传一个真正的url到后端数据库
     * */
    @RequestMapping("/searchOrderByUserId/{userId}")
    public List<Order> searchOrderByUserId(@PathVariable("userId") String userId) {
        if (!AuthUtil.getAuthUserId().equals(userId)) return null;
        return orderService.searchOrderByUserId(userId);
    }

    /**
     * update:2022 7/26 增加了删除状态的判断
     * 该函数时返回团长团购的所有订单
     * 接受参数：团长的userid
     * 返回参数：某团长的所有团购的订单
     * by Xu
     */
    @RequestMapping("/getOrdersForGroupLeader/{userid}")
    public List<Order> getOrdersForGroupLeader(@PathVariable("userid") String userid) {
        if (!userid.equals(AuthUtil.getAuthUserId())) return null; //是该团购的团长才能进行查询
        String userName = userService.getLeaderName(userid);
        List<Group> groups = groupService.searchGroupByGroupLeader(userName);//获取团长所有的团购

        List<Order> orders = new ArrayList<>();
        int status;
        for (Group group : groups) {
            List<Order> tmpOrders = group.getOrders();//获取团购的订单
            String userID = null;
            User owner = new User();
            for (Order tmp : tmpOrders) {
                status = tmp.getOrderStatus();
                if (status != -3 && status != -4 && status != -103 && status != -104) {
                    tmp.setGroupLeader(group.getGroupLeader());
                    tmp.setLeaderPicture(group.getPicture());
                    if (!Objects.equals(userID, tmp.getBelongUserid())) {
                        userID = tmp.getBelongUserid();
                        owner = userService.getUserInfo(userID);
                    }
                    tmp.setName(owner.getName());
                    tmp.setPicture(owner.getPicture());
                    orders.add(tmp);//把未被删除的订单全部放到一起返回
                }
            }
        }
        return orders;
    }

    /*该函数用于处理用户参加团购，将订单信息放入数据库中，并把itemcopy一份，订单状态设为未支付
     *接收参数：一个requestbody
     * 返回参数：创建好的订单信息
     * by：Zhou
     */
    @RequestMapping("/joinGroup")
    public Order joinGroup(@RequestBody JoinGroupRequest joinGroupRequest) {
        return orderService.joinGroup(joinGroupRequest);
    }

    /*该函数用于处理用户支付订单，将订单状态修改
     *接收参数：useid和orderid组成地requestbody
     */
    @RequestMapping("/payOrder")
    public SuccessResponse payOrder(@RequestBody PayOrderRequest payOrderRequest) {
        return orderService.payOrder(payOrderRequest);
    }

    @RequestMapping("/cancelOrder/{orderId}")
    public SuccessResponse cancelOrder(@PathVariable("orderId") Integer orderId) {
        return orderService.cancelOrder(orderId);
    }

    /*
     * 团长发货
     * */
    @RequestMapping("/deliverOrder/{orderId}")
    public SuccessResponse deliverOrder(@PathVariable("orderId") Integer orderId) {
        return orderService.deliverOrder(orderId);
    }

    /*
     * 团员收货
     * */
    @RequestMapping("/receiveOrder/{orderId}")
    public SuccessResponse receiveOrder(@PathVariable("orderId") Integer orderId) {
        if (authService.isOrderOwner(AuthUtil.getAuthUserId(), orderId)) {
            return orderService.receiveOrder(orderId);
        } else {
            return null;
        }
    }

    /**
     * 修改地址和电话，传入orderId，和需要修改的信息
     * by Xu
     */
    @RequestMapping("/modifyOrderInfo")
    public Msg modifyOrderInfo(@RequestBody JSONObject OrderInfo) {
        int orderId = OrderInfo.getInt("orderId");
        if (!authService.isOrderOwner(AuthUtil.getAuthUserId(), orderId))
            return MsgUtil.makeMsg(MsgUtil.ERROR, "您没有权限修改不属于自己的订单");
        String tel = getModifyInfo(OrderInfo, "tel");
        String address = getModifyInfo(OrderInfo, "address");
        String receiverName = getModifyInfo(OrderInfo, "receiverName");
        try {
            orderService.modifyOrderInfo(orderId, tel, address, receiverName);
            return MsgUtil.makeMsg(MsgUtil.SUCCESS, "修改订单信息成功");
        } catch (Exception e) {
            return MsgUtil.makeMsg(MsgUtil.ERROR, "修改信息出现错误");
        }
    }

    /**
     * 团长删除订单
     * by Xu
     */
    @RequestMapping("/LeaderDeleteOrder/{orderId}")
    public Msg LeaderDeleteOrder(@PathVariable("orderId") int orderId) {
        if (!authService.isOrderLeader(AuthUtil.getAuthUserId(),orderId)) return MsgUtil.makeMsg(MsgUtil.ERROR,"该团购不属于您，您无权限管理不属于您的团购的订单");
        if (orderService.LeaderDeleteOrder(orderId) < 0)
            return MsgUtil.makeMsg(MsgUtil.ERROR, "该状态下的订单无法删除！");
        else
            return MsgUtil.makeMsg(MsgUtil.SUCCESS, "删除订单成功！");
    }

    /**
     * 团员删除订单
     * by Xu
     */
    @RequestMapping("/MemberDeleteOrder/{orderId}")
    public Msg MemberDeleteOrder(@PathVariable("orderId") int orderId) {
        if (authService.isOrderOwner(AuthUtil.getAuthUserId(), orderId)) {
            if (orderService.MemberDeleteOrder(orderId) < 0)
                return MsgUtil.makeMsg(MsgUtil.ERROR, "该状态下的订单无法删除！");
            else
                return MsgUtil.makeMsg(MsgUtil.SUCCESS, "删除订单成功！");
        } else {
            return MsgUtil.makeMsg(MsgUtil.ERROR, "您无权限删除不属于您的订单");
        }
    }

    /**
     * 一个获取JSONObject中可能非空String信息的工具函数，既然不存在这个信息，还会报错，那我就自己写个处理函数
     *
     * @param OrderInfo
     * @param key
     * @return by Xu
     */
    private String getModifyInfo(@RequestBody JSONObject OrderInfo, String key) {
        String info;
        try {
            info = OrderInfo.getString(key);
            return info;
        } catch (Exception e) {
            return null;
        }
    }


    @RequestMapping("/tourist/recommend")
    public List<Item> getRecommend(@RequestBody JSONObject info)
    {
        String userid = getModifyInfo(info,"userId");
        List<Integer> list = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();
        if (userid == null) {
            System.out.println(" enter null");
            list = recommendUtil.recommendByNumber();
        }
        else
            list = recommendUtil.recommend(userid);
        return  recommendUtil.getItem(list);
    }

    @RequestMapping("/tourist/secKill")
    public SecKillResponse secKill(@RequestBody SecKillRequest secKillRequest)
    {
       return orderService.secKill(secKillRequest);
    }

    @RequestMapping("/tourist/getLatestGroup")
    public List<Group> getLatestGroup()
    {
        return recommendUtil.getLatestGroup();
    }

}

