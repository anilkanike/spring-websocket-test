package org.springframework.samples.websocket.chat;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by anilkanike on 24/01/2016.
 */
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, TextMessage> clients = new ConcurrentHashMap<>();

    private static final Map<String, WebSocketSession> sessionToWebSocket = new ConcurrentHashMap<>();

    final String msg = "Welcome to websocket....";


    public void sendMessage(String sessionId) throws IOException {
        final WebSocketSession session = sessionToWebSocket.get(sessionId);
        session.sendMessage(new TextMessage(msg));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        clients.put(session.getId(), message);

        for (Map.Entry<String, WebSocketSession> entry : sessionToWebSocket.entrySet()){
            final WebSocketSession socketSession = entry.getValue();
            sendMessage(entry.getKey());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessionToWebSocket.put(session.getId(), session);
        session.sendMessage(new TextMessage("ChatWebSocketHandler Connection Established for session "+session.getId()));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        clients.remove(session.getId());
        session.sendMessage(new TextMessage("ChatWebSocketHandler Connection Closed for session "+session.getId()));

    }
}
