package com.example.tuanhaowu.DaoImp;


import com.example.tuanhaowu.Dao.MessageDao;
import com.example.tuanhaowu.Entity.Message;
import com.example.tuanhaowu.Repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class MessageDaoImp  implements MessageDao {
    @Autowired
    MessageRepository messageRepository;
    @Override
    public Message saveMessage(Message newMessage) {
        return messageRepository.save(newMessage);
    }

    @Override
    public List<Message> getMessageByUserId(String userId) {
        return messageRepository.getMessageByUserId(userId);
    }
}
