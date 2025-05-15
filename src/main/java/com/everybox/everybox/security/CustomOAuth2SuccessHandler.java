package com.everybox.everybox.security;

import com.everybox.everybox.domain.User;
import com.everybox.everybox.service.UserService;
import com.everybox.everybox.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> kakaoAccount = (Map<String, Object>) oauth2User.getAttributes().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");

        User user = userService.findOrCreateKakaoUser(email, nickname);
        String jwt = jwtUtil.generateToken(user.getId(), user.getEmail());

        // ✅ JSON 응답 제거 → index.html로 리다이렉트하며 JWT 쿼리 파라미터로 넘김
        String redirectUrl = "http://localhost:8080/index.html?token=" + jwt;
        response.sendRedirect(redirectUrl);
    }
}
