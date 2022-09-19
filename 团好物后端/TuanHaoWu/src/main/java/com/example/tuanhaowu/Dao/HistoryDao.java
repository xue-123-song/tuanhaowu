package com.example.tuanhaowu.Dao;

import com.example.tuanhaowu.Entity.History;

import java.text.ParseException;
import java.util.List;

public interface HistoryDao {
    /**
     * 增加一条记录或者更新一条记录的状态
     * @param record
     * @return History
     * by Xu
     */
    History saveOne(History record);

    /**
     * 获取一条历史记录
     * @param historyId
     * @return History
     * by Xu
     */
    History getOne(Integer historyId);

    /**
     * 返回某个用户的所有历史记录
     * @param userId
     * @return List of History
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
