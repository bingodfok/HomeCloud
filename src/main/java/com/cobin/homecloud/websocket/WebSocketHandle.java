package com.cobin.homecloud.websocket;

import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * websocket 消息处理 用于设备绑定成功消息通知
 *
 * @Author 1_bit
 * @Date 2023/4/26 19:37
 */
@Component
@ServerEndpoint("/websocket/equt_binding/{hcid}")
public class WebSocketHandle {

    private final Logger logger = LoggerFactory.getLogger(WebSocketHandle.class);

    private final static ConcurrentMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 建立连接时触发
     */
    @OnOpen
    public void OnOpen(@PathParam("hcid") String hcid, Session session) {
        logger.info("配对设备[{}]客户端与服务器建立webSocket连接", hcid);
        sessionMap.put(hcid, session);
    }

    /**
     * 接收消息时触发
     *
     * @param msg 消息
     */
    @OnMessage
    public void OnMessage(@PathParam("hcid") String hcid, String msg) {
        logger.info("配对设备[{}]客户端向服务器发送消息", hcid);
    }

    /**
     * 客户端连接关闭时触发
     */
    @OnClose
    public void OnClose(@PathParam("hcid") String hcid, Session session) {
        logger.info("配对设备[{}]客户端与服务器断开webSocket连接", hcid);
        sessionMap.remove(hcid);
    }

    /**
     * 连接异常时触发
     */
    @OnError
    public void OnError(Session session, Throwable throwable) {

    }

    /**
     * 发送消息
     *
     * @param msg 消息
     */
    public static <T> void SenMessage(String hcid, T msg) {
        Assert.notNull(msg, "websocket message not null...");
        Assert.notNull(hcid, "hcid not null...");
        Session session = sessionMap.get(hcid);
        if (session != null) {
            try {
                if (msg instanceof String) {
                    session.getBasicRemote().sendText((String) msg);
                } else {
                    String s = JSONUtil.parse(msg).toJSONString(2);
                    session.getBasicRemote().sendText(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            LoggerFactory.getLogger(WebSocketHandle.class).info("配对设备[{}]客户端未与服务器建立连接", hcid);
        }
    }
}
