package com.example.tuanhaowu.ServiceImp;

import com.example.tuanhaowu.Dao.HistoryDao;
import com.example.tuanhaowu.Entity.History;
import com.example.tuanhaowu.Service.HistoryService;
import com.example.tuanhaowu.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
@Service
public class HistoryServiceImp implements HistoryService {
    @Autowired
    private HistoryDao historyDao;
    /**
     * 增加一条历史记录
     * @param userId
     * @param groupId
     * @return
     * by Xu
     */
    @Override
    public History add(String userId, Integer groupId) {
        History history = new History();
        history.setBelongUserId(userId);
        history.setGroupId(groupId);
        history.setVisitTime(TimeUtil.getNow());
        return historyDao.saveOne(history);
    }
    /**
     * 删除一条历史记录
     * @param historyId
     * @return 0，代表成功删除，-1代表出现异常
     * by Xu
     */
    @Override
    public int deleteOne(Integer historyId) {
        try {
            History history = historyDao.getOne(historyId);
            history.setHistoryStatus(-1);
            historyDao.saveOne(history);
            return 0;
        }
        catch (Exception e)
        {
            return -1;
        }
    }
    /**
     * 清空历史记录
     * @param userId
     * @return 0，代表成功删除，-1代表出现异常
     * by Xu
     */
    @Override
    public int clearAll(String userId) {
        try {
            List<History> histories = historyDao.QueryAll(userId);
            for (History history:histories)
            {
                history.setHistoryStatus(-1);
                historyDao.saveOne(history);
            };
            return 0;
        }catch (Exception e)
        {
            return -1;
        }

    }
    /**
     * 查询用户的所有历史记录
     * @param userId
     * @return 历史记录的List
     * by Xu
     */
    @Override
    public List<History> QueryAll(String userId) {
        return historyDao.QueryAll(userId);
    }

    /**
     * 返回团购的浏览次数
     * @param groupId
     * @return 浏览次数
     * by Xu
     */
    @Override
    public int getVisitTimes(int groupId) {
        return historyDao.getVisitTimes(groupId);
    }

    @Override
    public int todayIncrementVisit(int groupId) throws ParseException {
        return historyDao.todayIncrementVisit(groupId);
    }
}
