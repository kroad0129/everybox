package com.everybox.everybox.security;

import com.everybox.everybox.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = jwtUtil.validateToken(token);

                if (claims != null) {
                    Object userIdObj = claims.get("userId");
                    Long userId = null;
                    if (userIdObj instanceof Integer) {
                        userId = ((Integer) userIdObj).longValue();
                    } else if (userIdObj instanceof Long) {
                        userId = (Long) userIdObj;
                    } else {
                        log.warn("Unexpected userId claim type: {}", userIdObj == null ? "null" : userIdObj.getClass());
                    }

                    String email = claims.getSubject();

                    if (userId != null) {
                        JwtAuthentication authentication = new JwtAuthentication(userId, email);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        log.warn("❌ JWT 유효성 검사 실패: userId가 null입니다");
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                } else {
                    log.warn("❌ JWT 유효성 검사 실패: claims가 null입니다");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            } catch (Exception e) {
                log.error("❌ JWT 처리 중 예외 발생: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
