package com.example.tuanhaowu.Service;

import com.example.tuanhaowu.Entity.Request.SubScribeRequest;
import org.quartz.SchedulerException;

import java.sql.Timestamp;

public interface PushService {
    void TimedPushGroup(int groupId, Timestamp date) throws SchedulerException;
    void RenewPush(int groupId);
    void SubScribeTest(SubScribeRequest subScribeTest) throws SchedulerException;
}