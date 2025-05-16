package com.everybox.everybox.config;

import com.everybox.everybox.security.JwtAuthentication;
import com.everybox.everybox.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 웹소켓 연결 요청 시 JWT 검사 (CONNECT 명령어)
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    Claims claims = jwtUtil.validateToken(token);
                    if (claims != null) {
                        Long userId = claims.get("userId", Integer.class).longValue();
                        String email = claims.getSubject();

                        JwtAuthentication authentication = new JwtAuthentication(userId, email);
                        accessor.setUser(authentication);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid JWT token: " + e.getMessage());
                }
            } else {
                throw new IllegalArgumentException("No JWT token found in WebSocket CONNECT headers");
            }
        }
        return message;
    }
}
