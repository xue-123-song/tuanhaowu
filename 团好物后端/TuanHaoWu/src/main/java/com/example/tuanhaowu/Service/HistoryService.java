package com.example.tuanhaowu.Service;

import com.example.tuanhaowu.Entity.History;

import java.text.ParseException;
import java.util.List;

public interface HistoryService {
    /**
     * 增加一条历史记录
     * @param userId
     * @param groupId
     * @return
     * by Xu
     */
    History add(String userId,Integer groupId);

    /**
     * 删除一条历史记录
     * @param historyId
     * @return 0，代表成功删除，-1代表出现异常
     * by Xu
     */
    int deleteOne(Integer historyId);

    /**
     * 清空历史记录
     * @param userId
     * @return 0，代表成功删除，-1代表出现异常
     * by Xu
     */
    int clearAll(String userId);

    /**
     * 查询用户的所有历史记录
     * @param userId
     * @return 历史记录的List
     * by Xu
     */
    List<History> QueryAll(String userId);

    /**
     * 返回团购的浏览次数
     * @param groupId
     * @return 浏览次数
     * by Xu
     */
    int getVisitTimes(int groupId);

    /**
     * 返回今天新增的浏览次数
     * @param groupId
     * @return int
     * by Xu
     */
    int todayIncrementVisit(int groupId) throws ParseException;


}
