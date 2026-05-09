package com.example.campushelp.ws;

import com.example.campushelp.mapper.ChatMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat/{userId}")
@Component
public class WebSocketServer {

    private static final Map<String, Session> onlineSessions = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 关键点：WebSocket 中注入 Mapper 需要用静态变量
    private static ChatMapper chatMapper;

    @Autowired
    public void setChatMapper(ChatMapper chatMapper) {
        WebSocketServer.chatMapper = chatMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        onlineSessions.put(userId, session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            Map msgMap = objectMapper.readValue(message, Map.class);
            String toUserId = String.valueOf(msgMap.get("toUserId"));
            String content = (String) msgMap.get("content");
            // 获取发送者的 ID
            String fromUserId = (String) session.getUserProperties().get("userId");
            // 注意：因为 OnOpen 没法直接存 userProperties，我们简单点，前端传过来或者假设 session.pathParam 有
            // 这里为了简单，我们假设前端不传 fromUserId，我们靠 pathParam 拿不到，
            // 修正方案：OnOpen 的时候其实拿不到 pathParam 存进 userProp。
            // 最简单的办法：从 session 的 path 截取，或者依赖 onlineSessions 的 key 遍历（性能差）。

            // 简单修正：我们直接让前端把 fromUserId 也传过来，或者解析 path
            String path = session.getRequestURI().getPath();
            String currentUserId = path.substring(path.lastIndexOf('/') + 1); // 从 URL 拿 ID

            // 1. 【新增】不管对方在不在线，先保存到数据库！
            chatMapper.insert(Integer.parseInt(currentUserId), Integer.parseInt(toUserId), content);

            // 2. 如果在线，就转发
            Session receiverSession = onlineSessions.get(toUserId);
            if (receiverSession != null) {
                String jsonToSend = objectMapper.writeValueAsString(Map.of("content", content, "type", "received"));
                receiverSession.getBasicRemote().sendText(jsonToSend);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        onlineSessions.remove(userId);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}