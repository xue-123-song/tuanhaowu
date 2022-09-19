package com.example.tuanhaowu.Service;

import com.example.tuanhaowu.Entity.Subscribe;
import com.example.tuanhaowu.Entity.Subscribe;
import com.example.tuanhaowu.util.MsgUtil.Msg;
import org.quartz.SchedulerException;

import java.util.List;

public interface SubscribeService {
    /**
     * 增加一条订阅记录
     * @param userId
     * @param groupId
     * @return
     * by Xu
     */
    Subscribe add(String userId, Integer groupId) throws SchedulerException;

    /**
     * 取消某个订阅记录
     * @param subscribeId
     * @return
     * by Xu
     */
    Msg cancel(Integer subscribeId);

    /**
     * 返回团购的所有订购的用户Id
     * @param groupId
     * @return
     */
    List<String> getAllSubscribeUsers(Integer groupId);
    /**
     * 返回某个团购的订阅记录Id
     * @param userId
     * @param groupId
     * @return
     * by Xu
     */
    Integer get_SubscribeId_ByUserIdAndGroupId(String userId,Integer groupId);
}
