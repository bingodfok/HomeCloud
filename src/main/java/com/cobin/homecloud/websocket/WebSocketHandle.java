package com.cobin.homecloud.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * websocket 消息处理 用于设备绑定成功消息通知
 * @Author 1_bit
 * @Date 2023/4/26 19:37
 */
@Component
@ServerEndpoint("/websocket/equt_binding/{hcid}")
public class WebSocketHandle {

    private final Logger logger = LoggerFactory.getLogger(WebSocketHandle.class);

    private final static ConcurrentMap<String,Session> sessionMap = new ConcurrentHashMap<>();
    /**
     * 建立连接时触发
     */
    @OnOpen
    public void OnOpen(@PathParam("hcid") String hcid,Session session){
        logger.info("配对设备[{}]客户端与服务器建立webSocket连接",hcid);
        sessionMap.put(hcid,session);
    }
    /**
     * 接收消息时触发
     * @param msg 消息
     * @param session session
     */
    @OnMessage
    public void OnMessage(String msg, Session session){

    }
    /**
     * 客户端连接关闭时触发
     */
    @OnClose
    public void OnClose(@PathParam("hcid") String hcid,Session session){
        logger.info("配对设备[{}]客户端与服务器断开webSocket连接",hcid);
        sessionMap.remove(hcid);
    }
    /**
     * 连接异常时触发
     */
    @OnError
    public void OnError(Session session, Throwable throwable){

    }
    /**
     * 发送消息
     * @param msg 消息
     */
    public static void SenMessage(String hcid,String msg){
        Assert.notNull(msg,"websocket message not null...");
        Assert.notNull(hcid,"hcid not null...");
        Session session = sessionMap.get(hcid);
        if(session!=null){
            try{
                session.getBasicRemote().sendText(msg);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        LoggerFactory.getLogger(WebSocketHandle.class).info("客户端[{}]未与服务器建立连接",hcid);
    }
}
