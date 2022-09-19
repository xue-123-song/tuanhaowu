package com.example.tuanhaowu.Controller;


import com.example.tuanhaowu.Entity.Message;
import com.example.tuanhaowu.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {
    @Autowired
    MessageService messageService;

    @RequestMapping("/SearchMessage/{userId}")
    public List<Message> SearchMessage(@PathVariable("userId") String userId)
    {
        return messageService.getMessageByUserId(userId);
    }

}
