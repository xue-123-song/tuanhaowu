package com.example.tuanhaowu.ServiceImp;

import com.example.tuanhaowu.Dao.MessageDao;
import com.example.tuanhaowu.Entity.Message;
import com.example.tuanhaowu.Service.MessageService;
import com.example.tuanhaowu.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessageServiceImp implements MessageService {
    @Autowired
    MessageDao messageDao;
    @Override
    public List<Message> getMessageByUserId(String userId) {
        return messageDao.getMessageByUserId(userId);
    }

    @Override
    public Message addMessage(String userId, String title,String message) {
        Message newMessage = new Message();
        newMessage.setUserId(userId);
        newMessage.setMessage(message);
        newMessage.setDate(TimeUtil.getNow());
        newMessage.setTitle(title);
        return messageDao.saveMessage(newMessage);
    }
}
