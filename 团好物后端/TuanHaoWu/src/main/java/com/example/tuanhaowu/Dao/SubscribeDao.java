package com.example.tuanhaowu.Dao;

import com.example.tuanhaowu.Entity.Subscribe;

import java.util.List;

public interface SubscribeDao {
    /**
     * 保存一个新的订阅记录
     * @param newSubscribe
     * @return
     * by Xu
     */
    Subscribe saveSubscribe(Subscribe newSubscribe);

    /**
     * 删除一条订阅记录
     * @param subscribeId
     * by Xu
     * @return
     */
    int deleteSubscribe(Integer subscribeId);

    /**
     * 返回某个团购的所有订阅记录
     * @param groupId
     * @return
     * by Xu
     */
    List<Subscribe> getAllByGroupId(Integer groupId);


    /**
     * 返回某个团购的订阅记录Id
     * @param userId
     * @param groupId
     * @return
     * by Xu
     */
    Integer get_SubscribeId_ByUserIdAndGroupId(String userId,Integer groupId);

}
