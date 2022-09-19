package com.example.tuanhaowu.Subscribe;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * 目前的我的思路是通过webSocket推送消息给所有关注了某个团长的用户
 * 这个类是用来在某个事件发生后给团长的所有粉丝推送消息
 * 现在需要解决的就是session里能不能保存登陆的用户信息，还有如何测试的问题
 * 后续的扩展就是增加粉丝关注的有关表格和功能
 * */
@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {
    /*保存信息的session，如果可以的话，希望这里可以保存登陆的用户所包含的信息，然后这里就能用来判断是否是关注了的用户*/
    private Session session;

    /*储存这些webSocket*/
    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSockets.add(this);
        log.info("[webSocket消息]有新的连接，总数：{}",webSockets.size());
    }
    @OnClose
    public void onClose(){
        webSockets.remove(this);
        log.info("[webSocket消息]有连接断开，总数：{}",webSockets.size());
    }
    @OnMessage
    public  void OnMessage(String message){
        log.info("[webSocket消息]收到来自客户端的消息:{}",message);
    }
    /*这里是发送消息的函数，具体的用法是在某个controller里注入websocket，然后在某个api里的事情发生之后发送消息，这里目前写在GroupController里的creat函数里*/
    public  void sendMessage(String message){
        for (WebSocket webSocket:webSockets) {
            log.info("[webSocket消息],广播消息：{}",message);
            try {
                System.out.println("[获取用户信息，目前是在遍历用户，希望这个用户信息之后可以用来判断是否关注]userInfo = "+JSON.toJSONString(webSocket.session.getUserPrincipal()));
                webSocket.session.getBasicRemote().sendText(message);//发送消息
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
