package com.connect.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping({"/chat/{chatRoomId}"})
    public void sendMessageToChatRoom(
            @Payload String message,
            @DestinationVariable String chatRoomId
    ){
        String destination = "/chatroom/"+chatRoomId;
        messagingTemplate.convertAndSend(destination, message);
    }

}
