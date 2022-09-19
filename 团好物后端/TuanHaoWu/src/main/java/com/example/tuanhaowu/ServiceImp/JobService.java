package com.example.tuanhaowu.ServiceImp;

import com.example.tuanhaowu.Dao.UserDao;
import com.example.tuanhaowu.Service.MessageService;
import com.example.tuanhaowu.Service.SubscribeService;
import com.example.tuanhaowu.util.PushUtil;
import com.example.tuanhaowu.util.TimeUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService implements Job {

    @Autowired
    UserDao userDao;

    @Autowired
    SubscribeService subscribeService;

//    @Autowired
//    MessageService messageService;
//    @Autowired
//    MessageDao messageDao;

    MessageService messageService;

   // private MessageDao messageDao= ApplicationContextHelperUtil.getBean(MessageDao.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int groupId = dataMap.getInt("groupId");
        String userId = dataMap.getString("userId");
        String userCid = dataMap.getString("userCid");
        System.out.println("groupId = "+groupId);

        System.out.println("发布订阅！");
//        List<String> userIdList = subscribeService.getAllSubscribeUsers(groupId);
//        System.out.println(userIdList);
        System.out.println("userId:" + userId);
        System.out.println("userCid:" + userCid);

        String title = "团购即将开始";
        String message = "你关注的团购一小时后就要开始啦！";

//        if(messageDao == null)
//            System.out.println("messageService 为 null");
        if (userDao == null)
            System.out.println("userDao 为null");
        if(title == null)
            System.out.println("title 为 null");
        if(message == null)
            System.out.println("message 为 null");
        PushUtil pushUtil = new PushUtil();
        if(pushUtil == null)
            System.out.println("pushUtil 为 null");
        if (userCid == null)
            System.out.println("userCid 为 null");
        System.out.println("发送成功！");

        pushUtil.pushToSingle(userCid,title ,message , "something");
        System.out.println("发送成功！");
     //   messageDao.addMessage(userId,title,message);
//        for (String userId:userIdList) {
//            messageService.addMessage(userId,title,message);
//            User user = userDao.getUserByUserId(userId);
//            String userCid = user.getUserCid();
//            pushUtil.pushToSingle(userCid,title ,message , "something");
//        }
    }
}