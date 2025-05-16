package com.everybox.everybox.config;

import com.everybox.everybox.security.JwtAuthentication;
import com.everybox.everybox.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.*;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");  // /sub 로 변경
        registry.setApplicationDestinationPrefixes("/pub");  // /pub 으로 변경
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@Nonnull Message<?> message, @Nonnull MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authHeaders = accessor.getNativeHeader("Authorization");
                    if (authHeaders == null || authHeaders.isEmpty()) {
                        throw new IllegalArgumentException("No Authorization header");
                    }
                    String token = authHeaders.get(0);
                    if (token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    } else {
                        throw new IllegalArgumentException("Invalid Authorization header");
                    }
                    try {
                        Claims claims = jwtUtil.validateToken(token);
                        Long userId = claims.get("userId", Integer.class).longValue();
                        String email = claims.getSubject();
                        JwtAuthentication authentication = new JwtAuthentication(userId, email);
                        accessor.setUser(authentication);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Invalid token: " + e.getMessage());
                    }
                }
                return message;
            }
        });
    }
}
