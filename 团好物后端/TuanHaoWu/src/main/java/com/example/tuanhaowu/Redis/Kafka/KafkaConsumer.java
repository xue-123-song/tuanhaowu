package com.example.tuanhaowu.Redis.Kafka;


import com.example.tuanhaowu.Entity.Response.SecKillPollingResponse;
import com.example.tuanhaowu.Redis.Lock.RedissonDistributedLocker;
import com.example.tuanhaowu.Redis.Repository.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
public class KafkaConsumer {
    private Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private RedissonDistributedLocker redissonDistributedLocker;

    /**
     * 监听seckill主题,有消息就读取
     * 主要消费秒杀进入到下订单操作的队列数据，此处的数据已经过滤了绝大部分请求，只有真正得到下单机会的用户才会进入到这一环节
     * @param message
     */
    @KafkaListener(topics = {"demo_seckill_queue"})
    public void receiveMessage(String message){
        JSONObject jsonObject = JSONObject.parseObject(message);
        String userId = jsonObject.getString("userId");
        int itemId = jsonObject.getInteger("itemId");
        int buyNum = jsonObject.getInteger("buyNum");
        String lockKey = "marketOrder" + itemId;//控制锁的颗粒度(摊位活动ID)
        redissonDistributedLocker.lock(lockKey, 1L);
        try {
            JSONObject result = new JSONObject();
            SecKillPollingResponse response = new SecKillPollingResponse();
            String redisStock = redisRepository.get("BM_MARKET_SECKILL_STOCKNUM_" + itemId);
            int surplusStock = Integer.parseInt(redisStock == null ? "0" : redisStock);    //剩余库存
            if(surplusStock > buyNum)//剩余库存充足
            {
                response.setSuccess(true);
                response.setResponseCode(0);
                response.setResponseMsg("您已获得下单资格，请尽快下单");
                response.setRefreshTime(0);
                String code = "uuid";
                response.setOrderQualificationCode(code);
                //将下单资格码维护到redis中，用于下单时候的检验；有效时间10分钟；
                redisRepository.setExpire("BM_MARKET_SECKILL_QUALIFICATION_CODE_" + itemId + "_" + userId, code, 10*60);
                //维护一个key，防止获得下单资格用户重新抢购，当支付过期之后应该维护删除该标志
                redisRepository.setExpire("BM_MARKET_SECKILL_LIMIT_" + itemId + "_" + userId, "true", 3600*24*7);
                //扣减锁定库存
                redisRepository.decrBy("BM_MARKET_SECKILL_STOCKNUM_" + itemId, buyNum);
            }
            else {
                response.setSuccess(false);
                response.setResponseCode(6102);
                response.setResponseMsg("秒杀失败，商品已经售罄");
                response.setRefreshTime(0);
            }
            result.put("response", response);

        }
        finally {
            redissonDistributedLocker.unlock(lockKey);
        }
        }}
