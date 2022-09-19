package com.example.tuanhaowu.Service;

import com.example.tuanhaowu.Entity.Message;

import java.util.List;

public interface MessageService {

    List<Message> getMessageByUserId(String userId);
    Message addMessage(String userId, String title,String message);
}
