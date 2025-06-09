package com.example.matchclient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class MatchWebsocketHandler {
    private StompSession stompSession;
    private static final String WS_URL = "ws://localhost:8080/ws";

    public void connectToWebSocket() {
        try {
            List<Transport> transports = new ArrayList<>(1);
            transports.add(new WebSocketTransport(new StandardWebSocketClient()));
            SockJsClient sockJsClient = new SockJsClient(transports);
            
            WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
            this.stompSession = stompClient.connectAsync(
                WS_URL, 
                headers,
                new MyStompSessionHandler()
            ).get();
            
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("WebSocket connection failed: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void updateScore(String team, String matchId) {
        if (isConnected()) {
            stompSession.send("/app/match/update/" + matchId, new Message(team, false));
        }
    }

    public void endMatch(String matchId) {
        if (isConnected()) {
            stompSession.send("/app/match/end/" + matchId, new Message("", true));
        }
    }

    public void subscribeToMatch(String matchId) {
        if (!isConnected()) {
            connectToWebSocket();
        }
        stompSession.subscribe("/topic/match/" + matchId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Message msg = (Message) payload;
                System.out.println("Current Score: " + msg.matchScore());
                if (msg.isEnded()) {
                    System.out.println("Match has ended. Exiting...");
                    close();
                    System.exit(0);
                }
            }
        });
    }

    private boolean isConnected() {
        return stompSession != null && stompSession.isConnected();
    }

    public void close() {
        if (isConnected()) {
            stompSession.disconnect();
            System.out.println("Closed WebSocket connection");
        }
    }
}

class MyStompSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Successfully connected to WebSocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, 
                              StompHeaders headers, byte[] payload, Throwable exception) {
        System.err.println("WebSocket error: " + exception.getMessage());
    }
}



