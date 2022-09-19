package com.example.tuanhaowu.ServiceImp;

import com.alibaba.fastjson.JSON;
import com.example.tuanhaowu.Dao.GroupDao;
import com.example.tuanhaowu.Dao.MessageDao;
import com.example.tuanhaowu.Dao.SubscribeDao;
import com.example.tuanhaowu.Entity.Group;
import com.example.tuanhaowu.Entity.Message;
import com.example.tuanhaowu.Entity.Subscribe;
import com.example.tuanhaowu.Service.PushService;
import com.example.tuanhaowu.Service.SubscribeService;
import com.example.tuanhaowu.util.MsgUtil.Msg;
import com.example.tuanhaowu.util.MsgUtil.MsgUtil;
import com.example.tuanhaowu.util.TimeUtil;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubscribeServiceImp implements SubscribeService {
    @Autowired
    SubscribeDao subscribeDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    PushService pushService;
    @Autowired
    MessageDao messageDao;

    @Override
    public Subscribe add(String userId, Integer groupId) /*throws SchedulerException*/ {
        Subscribe subscribe = new Subscribe();
        subscribe.setSubscribeUserId(userId);
        subscribe.setGroupId(groupId);
        subscribe.setCreateTime(TimeUtil.getNow());

        System.out.println("订阅成功！！！");

        Group group = groupDao.getGroupByID(groupId);
        Timestamp date = group.getStartTime();
        System.out.println("调用函数");
       // pushService.TimedPushGroup(groupId, date);
        System.out.println("调用函数完成");
        Message message = new Message();
        message.setTitle("团购开始推送");
        message.setMessage("您订阅的团购还有一小时就要开始啦！");
        message.setUserId(userId);
        message.setDate(date);
        messageDao.saveMessage(message);


        return subscribeDao.saveSubscribe(subscribe);
    }

    @Override
    public Msg cancel(Integer subscribeId) {
        if (subscribeDao.deleteSubscribe(subscribeId) >= 0) {
            return MsgUtil.makeMsg(MsgUtil.SUCCESS,"已取消订阅");
        }
        else {
            return MsgUtil.makeMsg(MsgUtil.ERROR,"取消订阅操作异常");
        }
    }

    @Override
    public List<String> getAllSubscribeUsers(Integer groupId) {
        System.out.println("groupId in debug = "+groupId);
        List<Subscribe> subscribeList = subscribeDao.getAllByGroupId(groupId);
        System.out.println("the subscribe = "+JSON.toJSONString(subscribeList.get(0)));
        List<String> userList = new ArrayList<>();
        for (Subscribe subscribe:subscribeList) {
            userList.add(subscribe.getSubscribeUserId());
            System.out.println("userId="+subscribe.getSubscribeUserId());
        }
        return userList;
    }

    @Override
    public Integer get_SubscribeId_ByUserIdAndGroupId(String userId, Integer groupId) {
        return subscribeDao.get_SubscribeId_ByUserIdAndGroupId(userId,groupId);
    }
}
