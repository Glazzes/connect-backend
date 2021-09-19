package com.connect.chatroom.insfrastructure.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:19006")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.setApplicationDestinationPrefixes("/app")
                .setUserDestinationPrefix("/user")
                .setPreservePublishOrder(true)
                .enableSimpleBroker("/chatroom");

                /*
                .enableStompBrokerRelay("/app", "/user")
                .setRelayHost("localhost")
                .setRelayPort(13669)
                .setClientLogin("guest")
                .setClientPasscode("guest");
                 */
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

    }
}
