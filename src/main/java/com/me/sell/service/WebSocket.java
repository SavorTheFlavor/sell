package com.me.sell.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Administrator on 2018/2/1.
 */
@Component
@ServerEndpoint("/websocket")
public class WebSocket {

    private Logger logger = LoggerFactory.getLogger(WebSocket.class);

    private CopyOnWriteArraySet<WebSocket> websocketSet = new CopyOnWriteArraySet<>();

    private Session session;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        websocketSet.add(this);
        logger.info("A new socket connected!");
    }

    @OnClose
    public void onClose(){
        websocketSet.remove(this);
        logger.info("A socket disconnected!");
    }

    @OnMessage
    public void onMessage(String msg){
        logger.info("message from client socket:"+msg);
    }

    public void sendMessage(String msg){
        for(WebSocket webSocket : websocketSet){
            try {
                webSocket.session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
