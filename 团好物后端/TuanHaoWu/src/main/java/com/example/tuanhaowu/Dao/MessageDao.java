package com.example.tuanhaowu.Dao;

import com.example.tuanhaowu.Entity.Message;

import java.util.List;

public interface MessageDao {
    Message saveMessage(Message newMessage);
    List<Message> getMessageByUserId(String userId);
}
