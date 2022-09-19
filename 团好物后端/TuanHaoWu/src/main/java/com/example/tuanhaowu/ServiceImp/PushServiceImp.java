package com.example.tuanhaowu.ServiceImp;

import com.example.tuanhaowu.Dao.SubscribeDao;
import com.example.tuanhaowu.Dao.UserDao;
import com.example.tuanhaowu.Entity.Request.SubScribeRequest;
import com.example.tuanhaowu.Entity.Subscribe;
import com.example.tuanhaowu.Entity.User;
import com.example.tuanhaowu.Service.MessageService;
import com.example.tuanhaowu.Service.PushService;
import com.example.tuanhaowu.Service.SubscribeService;
import com.example.tuanhaowu.util.PushUtil;
import com.example.tuanhaowu.util.TimeUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
@Service
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class PushServiceImp implements PushService {

    @Autowired
    UserDao userDao;

    @Autowired
    PushUtil pushUtil;
    @Autowired
    MessageService messageService;
    @Autowired
    SubscribeDao subscribeDao;




    @Override
    public void TimedPushGroup(int groupId, Timestamp date) throws SchedulerException {
        List<Subscribe>  subscribeList= subscribeDao.getAllByGroupId(groupId);
        System.out.println(subscribeList);
        List<String> userIdList = new ArrayList<>();
        for (Subscribe subscribe:subscribeList)
         userIdList.add(subscribe.getSubscribeUserId());
        System.out.println(userIdList);
    for (int j = 0 ; j < userIdList.size(); j++) {
            User user = userDao.getUserByUserId(userIdList.get(j));
            String userCid = user.getUserCid();


        // Trigger the job to run now, and then every 40 seconds
        //记得是团购开始前一个小时！

        int[] dates;
        dates = TimeUtil.parseTimeStamp(date);
        for (int d : dates)
            System.out.println(d);
        String dateString = " ";
        for (int i = 5; i >= -1; i--) {
            if (i != 0 && i != -1)
                dateString = dateString + dates[i] + " ";
            else if (i != 0)
                dateString = dateString + dates[0];
            else
                dateString = dateString + "?" + " ";
        }
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        Scheduler sched = schedFact.getScheduler();

            sched.start();
            JobDetail job = newJob(JobService.class)
                    .withIdentity("myJob", "group1") // name "myJob", group "group1"
                    .usingJobData("groupId", groupId)
                    .usingJobData("userCid", userCid)
                    .usingJobData("userId", userIdList.get(j))
                    .build();


            Trigger trigger = newTrigger()
                    .withIdentity("myTrigger", "group1")
                    .withSchedule(cronSchedule(dateString))
                    .forJob("myJob", "group1")
                    .build();
            System.out.println("设置定时任务，cron表达式为：" + dateString);

            // Tell quartz to schedule the job using our trigger
            sched.scheduleJob(job, trigger);
        }
    }

    @Override
    public void RenewPush(int groupId) {
        List<Subscribe>  subscribeList= subscribeDao.getAllByGroupId(groupId);
        List<String> userIdList = new ArrayList<>();
        for (Subscribe subscribe:subscribeList)
            userIdList.add(subscribe.getSubscribeUserId());

        String title = "团购信息发生变化";
        String message = "你关注的团购商品信息已被团长修改";

        for (String userId:userIdList) {
            messageService.addMessage(userId,title,message);
            User user = userDao.getUserByUserId(userId);
            String userCid = user.getUserCid();
//            pushUtil.pushToSingle(userCid,title ,message , "something");
        }
        System.out.println("通知修改团购信息");


    }

    @Override
    public void SubScribeTest(SubScribeRequest subScribeTest) throws SchedulerException{
        int groupId = subScribeTest.getGroupId();
        String userId = subScribeTest.getUserId();
        Timestamp date = TimeUtil.timeTrim(subScribeTest.getDate());
        User user = userDao.getUserByUserId(userId);
        String userCid = user.getUserCid();
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        Scheduler sched = schedFact.getScheduler();

        sched.start();
        JobDetail job = newJob(JobService.class)
                .withIdentity("myJob", "group1") // name "myJob", group "group1"
                .usingJobData("groupId",groupId)
                .usingJobData("userCid",userCid)
                .usingJobData("userId",userId)
                .build();

        // Trigger the job to run now, and then every 40 seconds
        //记得是团购开始前一个小时！
        int []dates;
        dates = TimeUtil.parseTimeStamp(date);
        for (int d :dates)
            System.out.println(d);
        String dateString = " ";
        for(int i = 5; i >= -1; i--)
        {
            if(i != 0 && i != -1)
                dateString = dateString + dates[i] + " ";
            else if (i != 0)
                dateString = dateString + dates[0];
            else
                dateString = dateString + "?" + " ";
        }
        System.out.println("cron =" +dateString);
        Trigger trigger = newTrigger()
                .withIdentity("myTrigger", "group1")
                .withSchedule(cronSchedule(dateString))
                .forJob("myJob", "group1")
                .build();
        System.out.println("设置定时任务，cron表达式为：" + dateString);

        // Tell quartz to schedule the job using our trigger
        sched.scheduleJob(job, trigger);
    }
}