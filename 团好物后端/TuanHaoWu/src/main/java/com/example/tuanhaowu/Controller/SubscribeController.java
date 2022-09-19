package com.example.tuanhaowu.Controller;
import com.example.tuanhaowu.Entity.Request.SubScribeRequest;
import com.example.tuanhaowu.Service.PushService;
import com.example.tuanhaowu.Service.SubscribeService;
import com.example.tuanhaowu.util.MsgUtil.Msg;
import com.example.tuanhaowu.util.MsgUtil.MsgUtil;
import net.sf.json.JSONObject;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubscribeController {
    @Autowired
    SubscribeService subscribeService;
    @Autowired
    PushService pushService;

    /**
     * 增加一条订阅记录
     */
    @RequestMapping(value = "/Subscribe/add")
    public Msg add(@RequestBody JSONObject info) {
        try {
            String userId = info.getString("userId");
            Integer groupId = info.getInt("groupId");
            System.out.println("订阅 = " + groupId);
            subscribeService.add(userId, groupId);
            return MsgUtil.makeMsg(MsgUtil.SUCCESS, "订阅成功");
        } catch (Exception e) {
            return MsgUtil.makeMsg(MsgUtil.ERROR, "订阅失败");}}
    /**
     * 取消订阅
     * 直接删除对应的记录
     */
    @RequestMapping(value = "/Subscribe/cancel/{subscribeId}")
    public Msg cancel(@PathVariable("subscribeId") Integer subscribeId) {
        return subscribeService.cancel(subscribeId);
    }

    @RequestMapping(value = "/Subscribe/getAll/{groupId}")
    public List<String> getAll(@PathVariable("groupId") Integer groupId) {return subscribeService.getAllSubscribeUsers(groupId);}
}
