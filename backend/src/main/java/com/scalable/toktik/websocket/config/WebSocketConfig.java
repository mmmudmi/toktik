package com.scalable.toktik.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


// https://stackoverflow.com/questions/65891650/how-to-combine-spring-rest-and-websocket
// https://stackoverflow.com/questions/56410584/does-spring-support-rest-and-websocket-controllers-connection-at-the-same-time
// https://spring.io/guides/gs/messaging-stomp-websocket/
// https://www.baeldung.com/spring-boot-scheduled-websocket
// https://dzone.com/articles/build-a-secure-app-using-spring-boot-and-websocket


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("socket").setAllowedOrigins("*");
    }

}
