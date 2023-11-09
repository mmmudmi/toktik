package com.scalable.toktik.websocket.config;

import com.scalable.toktik.security.JWTService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

// https://dzone.com/articles/build-a-secure-app-using-spring-boot-and-websocket
// https://community.pivotal.io/s/question/0D50e00006Jy30ICAR/how-to-authenticate-web-socket-endpoints-with-jwt-and-spring-boot?language=en_US
//@Configuration
//@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {

    private final JWTService jwtService;

    private final UserDetailsService userDetailsService;

    public WebSocketAuthenticationConfig(JWTService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    System.out.println("_______________________AUTH_____________________");
                    List<String> authorization = accessor.getNativeHeader("X-Authorization");
                    String accessToken = authorization.get(0).split(" ")[1];
                    if (jwtService.valid(accessToken)) {
                        String username = jwtService.getSubject(accessToken);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        if (SecurityContextHolder.getContext().getAuthentication() == null)

                            SecurityContextHolder.getContext().setAuthentication(authentication);

                        accessor.setUser(authentication);
                    }
                }
                return message;
            }
        });
    }

}

