package com.everybox.everybox.security;

import com.everybox.everybox.domain.User;
import com.everybox.everybox.service.UserService;
import com.everybox.everybox.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = null;
        String nickname = null;
        if (kakaoAccount != null) {
            email = (String) kakaoAccount.get("email");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                nickname = (String) profile.get("nickname");
            }
        }
        // email이 null이면 id로 대체
        if (email == null || email.isBlank()) {
            Object idObj = attributes.get("id");
            String kakaoId = (idObj != null) ? idObj.toString() : "unknown";
            email = "kakao_" + kakaoId + "@kakao.com";
        }
        if (nickname == null || nickname.isBlank()) {
            nickname = "카카오유저";
        }

        User user = userService.findOrCreateKakaoUser(email, nickname);
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 팝업에서 부모창에 토큰을 넘기고 팝업 닫기!
        String html = """
        <html><body>
        <script>
            window.opener.postMessage({ token: 'Bearer %s' }, "*");
            window.close();
        </script>
        <h3>카카오 로그인 성공! 창이 닫힙니다...</h3>
        </body></html>
        """.formatted(token);

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(html);
    }
}
