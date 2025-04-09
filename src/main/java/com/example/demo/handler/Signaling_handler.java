package com.example.demo.handler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.demo.dto.Signal_message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Signaling_handler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON処理用

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        System.out.println("New session established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // メッセージをパースして処理を分岐
        String payload = message.getPayload();
        Signal_message signalMessage = objectMapper.readValue(payload, Signal_message.class);

        switch (signalMessage.getType()) {
            case "offer":
                handleOffer(session, signalMessage);
                break;
            case "answer":
                handleAnswer(session, signalMessage);
                break;
            case "candidate":
                handleCandidate(session, signalMessage);
                break;
            default:
                System.out.println("Unknown message type: " + signalMessage.getType());
        }
    }

    private void handleOffer(WebSocketSession session, Signal_message message) throws IOException {
        System.out.println("Processing offer from: " + session.getId());
        sendToPeer(session, message); // 対象のクライアントに送信
    }

    private void handleAnswer(WebSocketSession session, Signal_message message) throws IOException {
        System.out.println("Processing answer from: " + session.getId());
        sendToPeer(session, message); // 対象のクライアントに送信
    }

    private void handleCandidate(WebSocketSession session, Signal_message message) throws IOException {
        System.out.println("Processing ICE candidate from: " + session.getId());
        sendToPeer(session, message); // 対象のクライアントに送信
    }

    private void sendToPeer(WebSocketSession sender, Signal_message message) throws IOException {
        String targetId = message.getTarget(); // 対象セッションのID
        WebSocketSession targetSession = sessions.get(targetId);

        if (targetSession != null && targetSession.isOpen()) {
            targetSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } else {
            System.out.println("Target session not found or closed: " + targetId);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session.getId());
        System.out.println("Session closed: " + session.getId());
    }
}