package com.connect.chatroom.insfrastructure.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WebSocketEventHandler {
    private SimpMessagingTemplate template;

    @EventListener
    public void onConnectEvent(SessionSubscribeEvent event){
        String destination = (String) event.getMessage()
                .getHeaders()
                .get("simpDestination");

        Optional.of(destination)
                .ifPresent(System.out::println);
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null){
            System.out.println("is null");
            System.out.println(event);
        }
    }
}
