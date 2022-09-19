package com.example.tuanhaowu.DaoImp;

import com.example.tuanhaowu.Dao.HistoryDao;
import com.example.tuanhaowu.Entity.History;
import com.example.tuanhaowu.Repository.HistoryRepository;
import com.example.tuanhaowu.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Slf4j
@Repository
public class HistoryDaoImp implements HistoryDao {
    @Autowired
    private HistoryRepository historyRepository;

    /**
     * 增加一条记录或者更新一条记录的状态
     * @param record
     * @return History
     * by Xu
     */
    @Override
    public History saveOne(History record) {
        return historyRepository.save(record);
    }
    /**
     * 获取一条历史记录
     * @param historyId
     * @return History
     * by Xu
     */
    @Override
    public History getOne(Integer historyId) {
        return historyRepository.getById(historyId);
    }
    /**
     * 返回某个用户的所有历史记录
     * @param userId
     * @return List of History
     * by Xu
     */
    @Override
    public List<History> QueryAll(String userId) {
        return historyRepository.QueryAll(userId);
    }
    /**
     * 返回团购的浏览次数
     * @param groupId
     * @return 浏览次数
     * by Xu
     */
    @Override
    public int getVisitTimes(int groupId) {
        return historyRepository.getVisitTimes(groupId);
    }

    @Override
    public int todayIncrementVisit(int groupId) throws ParseException {

        return historyRepository.todayIncrementVisit(groupId, TimeUtil.getZero(),TimeUtil.getNow());
    }
}
