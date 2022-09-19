package com.example.tuanhaowu.DaoImp;

import com.example.tuanhaowu.Dao.SubscribeDao;
import com.example.tuanhaowu.Entity.Subscribe;
import com.example.tuanhaowu.Repository.SubscribeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class SubscribeDaoImp implements SubscribeDao {
    @Autowired
    SubscribeRepository subscribeRepository;
    @Override
    public Subscribe saveSubscribe(Subscribe newSubscribe) {
        return subscribeRepository.save(newSubscribe);
    }

    @Override
    public int deleteSubscribe(Integer subscribeId) {
        try {
            subscribeRepository.deleteById(subscribeId);
            return 0;
        }catch (Exception e) {
            return -1;
        }

    }

    @Override
    public List<Subscribe> getAllByGroupId(Integer groupId) {
       return subscribeRepository.getAllByGroupId(groupId);
    }

    @Override
    public Integer get_SubscribeId_ByUserIdAndGroupId(String userId, Integer groupId) {
        Integer subscribeId = 0;
        try {
             subscribeId =  subscribeRepository.get_SubscribeId_ByUserIdAndGroupId(userId,groupId);
        }catch (Exception e)
        {
            System.out.println("获取订阅记录异常");
        }
        return subscribeId;
    }
}
