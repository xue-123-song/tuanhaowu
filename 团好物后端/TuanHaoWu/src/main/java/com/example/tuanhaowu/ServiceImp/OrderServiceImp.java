package com.example.tuanhaowu.ServiceImp;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.example.tuanhaowu.Dao.*;
import com.example.tuanhaowu.Entity.*;
import com.example.tuanhaowu.Entity.Request.JoinGroupRequest;
import com.example.tuanhaowu.Entity.Request.PayOrderRequest;
import com.example.tuanhaowu.Entity.Request.SecKillRequest;
import com.example.tuanhaowu.Entity.Response.SecKillResponse;
import com.example.tuanhaowu.Entity.Response.SuccessResponse;
import com.example.tuanhaowu.Redis.Kafka.KafkaConsumer;
import com.example.tuanhaowu.Redis.Kafka.KafkaSender;
import com.example.tuanhaowu.Redis.Repository.RedisRepository;
import com.example.tuanhaowu.Service.OrderService;
import com.example.tuanhaowu.Service.UserService;
import com.example.tuanhaowu.util.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    ItemDao itemDao;
    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    UserDao userDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    UserService userService;
    @Autowired
    RedisRepository redisRepository;
    @Autowired
    KafkaSender kafkaSender;

    static  Lock lock = new ReentrantLock();

    @Override
    public List<Order> searchOrderByUserId(String userId) {
                                /*这里查出来的就是没有被删除的订单*/
        List<Order> orders= orderDao.searchOrderByUserId(userId);
        User user = userDao.getUserByUserId(userId);
        String name = user.getName();
        String picture = user.getPicture();
        int gpID = 0;
        Group group;
        User user1 = new User();
        for (Order order :orders)
        {
            /*当订单的团购变化之后，需要重新设置团长姓名和picture*/
            if(gpID != order.getGroupId()) {
                gpID = order.getGroupId();
                group = groupDao.getGroupByID(gpID);
                user1 = userDao.getUserByUserName(group.getGroupLeader());
            }
            order.setGroupLeader(user1.getName());
            order.setLeaderPicture(user1.getPicture());
            order.setName(name);
            order.setPicture(picture);
        }

        return orders;
    }

    /*
    *
    * by Zhou
    * */
    @Override
    public Order joinGroup(JoinGroupRequest joinGroupRequest) {
        Order order = new Order();
        //解析信息
        Integer groupId = joinGroupRequest.getGroupId();
        String userId = joinGroupRequest.getUserId();
        String tel = joinGroupRequest.getTel();
        String address = joinGroupRequest.getAddress();
        String receiverName = joinGroupRequest.getReceiverName();
        List<Integer> ItemId = joinGroupRequest.getItemId();//团购中选择要买的商品id
        if(ItemId == null)
            System.out.println("empty!");
        else
            System.out.println(ItemId.get(0));
        List<Integer> num = joinGroupRequest.getNum();//每个商品的数量

        System.out.println("add order");
        //先加入订单简略信息，即加入order里
        order.setBelongUserid(userId);
        order.setGroupId(groupId);
        order.setBelongUserid(userId);
        order.setOrderStatus(0);//未付款
        order.setAddress(address);
        order.setTel(tel);
        order.setReceiverName(receiverName);
        order.setOrderFinishTime(new Timestamp(System.currentTimeMillis()));
        orderDao.joinGroup(order);

        System.out.println("get orderId");
        //找到刚刚创建的order，获取orderId
        //Order newOrder = orderDao.searchOrderByUserIdAndGroupId(userId, groupId);

        System.out.println("add orderitem");
        List<Orderitem> orderitems = new ArrayList<>();
        //加入orderItem
        for(int i = 0; i < ItemId.size(); i++)
        {
            Orderitem orderitem = new Orderitem();
            Item item = itemDao.getItemByItemId(ItemId.get(i));
            System.out.println("拿到了item");
            System.out.println(item);
            orderitem.setBelongOrderId(order.getOrderId());
            orderitem.setItemBuynum(num.get(i));
            orderitem.setItemName(item.getItemName());
            orderitem.setItemId(item.getItemId());
            orderitem.setItemDescription(item.getItemDescription());
            orderitem.setItemImage(item.getItemImage());
            orderitem.setItemPrice(item.getItemPrice());
            orderitems.add(orderitem);
            orderItemDao.saveOrderItem(orderitem);
            //item库存量减少
            int itemStock = item.getItemStock();
            itemStock -= num.get(i);
            item.setItemStock(itemStock);
            itemDao.saveOneItem(item);
        }
        order.setOrderitemList(orderitems);
        return order;

    }

    @Override
    public SuccessResponse payOrder(PayOrderRequest payOrderRequest) {
        String userId = payOrderRequest.getUserId();
        int orderId = payOrderRequest.getOrderId();
        SuccessResponse successResponse = new SuccessResponse();
        User user = userDao.getUserByUserId(userId);
        Order prevOrder = orderDao.searchOrderByUserIdAndOrderId(userId, orderId);
        //拿到leader
        int groupId = prevOrder.getGroupId();
        User leader = userService.getLeaderByGroupId(groupId);
        //check
        if (user == null)
        {
            successResponse.setSuccess(0);
            successResponse.setMeg("用户不存在！");
            return successResponse;
        }
        if(prevOrder == null)
        {
            successResponse.setSuccess(0);
            successResponse.setMeg("订单不存在！");
            return successResponse;
        }
        prevOrder.setOrderStatus(1);
        //统计订单总价格，进行支付
        int totalPrice = getOrderTotalPrice(orderId);
        //判断用户余额是否充足
        if (user.getMoney() < totalPrice)
        {
            successResponse.setSuccess(0);
            successResponse.setMeg("余额不足！");
            return successResponse;
        }
        else
        {
            //用户支付
            int money = user.getMoney();
            money -= totalPrice;
            user.setMoney(money);
            successResponse.setSuccess(1);
            successResponse.setMeg("支付成功！");
            //团长获得支付
            money = leader.getMoney();
            money += totalPrice;
            leader.setMoney(money);
        }
        userDao.saveUser(user);
        userDao.saveUser(leader);
        orderDao.payGroup(prevOrder);
        return successResponse;
    }

    @Override
    public SuccessResponse cancelOrder(Integer orderId) {
        Order order = orderDao.searchOrderByOrderId(orderId);
        SuccessResponse successResponse = new SuccessResponse();
        //拿到leader
        int groupId = order.getGroupId();
        User leader = userService.getLeaderByGroupId(groupId);

        if (order == null)
        {
            successResponse.setSuccess(0);
            successResponse.setMeg("订单不存在！");
        }
        else
        {
            successResponse.setSuccess(1);
            successResponse.setMeg("成功取消订单！");
        }

        if(order.getOrderStatus() != 0) {
            //给用户退款
            String userId = order.getBelongUserid();
            //统计总金额
            int totalPrice = getOrderTotalPrice(orderId);
            User user = userDao.getUserByUserId(userId);
            int money = user.getMoney();
            money += totalPrice;
            user.setMoney(money);
            //团长扣钱
            money = leader.getMoney();
            money -= totalPrice;
            leader.setMoney(money);
            userDao.saveUser(user);
            userDao.saveUser(leader);
        }
        //设置状态为取消
        order.setOrderStatus(4);
        orderDao.saveOrder(order);
        return successResponse;
    }

    @Override
    public int getOrderTotalPrice(int orderId)
    {
        List<Orderitem> orderitems = orderItemDao.getOrderItemsByOrderId(orderId);
        //统计总金额
        int totalPrice = 0;
        for (Orderitem orderitem : orderitems) {
            int price = orderitem.getItemPrice();
            int num = orderitem.getItemBuynum();
            int total = price * num;
            totalPrice += total;
        }
        return totalPrice;
    }



    @Override
    public SuccessResponse deliverOrder(Integer orderId) {
        Order order = orderDao.searchOrderByOrderId(orderId);
        SuccessResponse successResponse = new SuccessResponse();
        if(order.getOrderStatus() != 1){
            successResponse.setSuccess(0);
            successResponse.setMeg("非未发货订单！");
        }
        else {
            order.setOrderStatus(2);
            successResponse.setSuccess(1);
            successResponse.setMeg("成功发货！");
            /*在发货后设置快递单号                 by xu*/
            order.setExpressId(SnowFlakeUtil.nextId());
        }
        orderDao.saveOrder(order);
        return successResponse;
    }

    @Override
    public SuccessResponse receiveOrder(Integer orderId) {
        Order order = orderDao.searchOrderByOrderId(orderId);
        SuccessResponse successResponse = new SuccessResponse();
        if(order.getOrderStatus() != 2){
            successResponse.setSuccess(0);
            successResponse.setMeg("非待收货订单！");
        }
        else {
            order.setOrderStatus(3);
            successResponse.setSuccess(1);
            successResponse.setMeg("成功收货！");
        }
        orderDao.saveOrder(order);
        return successResponse;
    }

    /**
     *修改订单信息
     * 一个冷知识的记录：String.isEmpty()在string = null的时候结果无法判断，会抛出意外，所以不能以此来判断其是不是null
     * by Xu
     */
    @Override
    public Order modifyOrderInfo(int orderId, String tel, String address, String receiverName) {
        Order order = orderDao.searchOrderByOrderId(orderId);
        try {
            if (tel != null) order.setTel(tel);
            if (address != null) order.setAddress(address);
            if (receiverName!= null) order.setReceiverName(receiverName);
        }
        catch (Exception e)
        {
            System.out.println("没有修改任何信息");
        }

        return  orderDao.saveOneOrder(order);
    }

    /**
     * @param orderId
     * @return 0代表设置成功，-1代表不能删除
     * 团长删除订单
     * by Xu
     */
    @Override
    public int LeaderDeleteOrder(Integer orderId) {
        Order order = orderDao.searchOrderByOrderId(orderId);
        int status = order.getOrderStatus();
        int ret = 0;
        System.out.println("status = "+status);
        switch (status)
        {
            case 3:order.setOrderStatus(-3);break;
            case 4:order.setOrderStatus(-4);break;
            case -13:order.setOrderStatus(-103);break;
            case -14:order.setOrderStatus(-104);break;
            default: ret = -1;
        }
        orderDao.saveOneOrder(order);
        return ret;
    }

    /**
     * @param orderId
     * @return 0代表正常删除，-1代表不可删除
     * 团员删除订单
     * by Xu
     */
    @Override
    public int MemberDeleteOrder(Integer orderId) {
        Order order = orderDao.searchOrderByOrderId(orderId);
        int status = order.getOrderStatus();
        int ret = 0;
        System.out.println("status = "+status);
        switch (status)
        {
            case 3:order.setOrderStatus(-13);break;
            case 4:order.setOrderStatus(-14);break;
            case -3:order.setOrderStatus(-103);break;
            case -4:order.setOrderStatus(-104);break;
            default:ret = -1;
        }
        orderDao.saveOrder(order);
        return ret;
    }

    @Override
    public int getGroupIdByOrderId(int orderId) {
        return orderDao.getGroupIdByOrderId(orderId);
    }

//    @Override
//    public SecKillResponse secKill(SecKillRequest secKillRequest) {
//
//        int itemId = secKillRequest.getItemId();
//        SecKillResponse secKillResponse = new SecKillResponse();
//        Thread secKillThread = new Thread(() -> {
//            lock.lock();
//            Item item = itemDao.getItemByItemId(itemId);
//            if (item.getItemStock() > 0) {
//                itemDao.secKillProduct(itemId);
//                secKillResponse.setSuccess(true);
//                secKillResponse.setResponseMsg("恭喜你，抢到商品！");
////                System.out.println(secKillResponse.getResponseMsg() + "1");
//            }
//            else
//            {
//                secKillResponse.setSuccess(false);
//                secKillResponse.setResponseMsg("商品已被抢光");
//            }
//            lock.unlock();
//        });
//        secKillThread.start();
////        System.out.println(secKillResponse.getResponseMsg() + "2");
//        return secKillResponse;
//    }


    @Override
    public SecKillResponse secKill(SecKillRequest secKillRequest) {

        SecKillResponse secKillResponse = new SecKillResponse();
        String userId = secKillRequest.getUserId();
        int itemId = secKillRequest.getItemId();
        Item item = itemDao.getItemByItemId(itemId);
        //判断当前库存是否充足
        long count = redisRepository.incr("BM_MARKET_SECKILL_COUNT_" + item.getItemId());//用itemid表示活动编号
        if (count > item.getItemStock())//商品已售完
        {
            secKillResponse.setSuccess(false);
            secKillResponse.setResponseMsg("商品已被售完");
        }
        else {
            JSONObject jsonStr = new JSONObject();
            jsonStr.put("userId",userId);
            jsonStr.put("itemId",itemId);
            jsonStr.put("buyNum",1);
            //放入消息队列进行处理
            kafkaSender.sendChannelMess("demo_seckill_queue", jsonStr.toString());
        }
        return secKillResponse;

    }

}






