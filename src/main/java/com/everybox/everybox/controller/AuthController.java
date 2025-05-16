package com.everybox.everybox.controller;

import com.everybox.everybox.dto.UserLoginRequestDto;
import com.everybox.everybox.dto.UserSignupRequestDto;
import com.everybox.everybox.dto.UserResponseDto;
import com.everybox.everybox.service.UserService;
import com.everybox.everybox.util.JwtUtil;
import com.everybox.everybox.security.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserSignupRequestDto request) {
        UserResponseDto user = userService.registerUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginRequestDto request) {
        UserResponseDto user = userService.login(request);
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return ResponseEntity.ok(Map.of("token", "Bearer " + token));
    }

    @GetMapping("/me")
    public UserResponseDto getMyInfo(Authentication authentication) {
        if (authentication instanceof JwtAuthentication jwtAuth) {
            Long userId = jwtAuth.getUserId();
            return userService.findDtoById(userId);
        }
        throw new IllegalArgumentException(
                "잘못된 인증 방식입니다. 반드시 JWT 토큰으로 접근해야 합니다. (토큰 누락, 만료, 헤더 확인 필요)"
        );
    }

    @GetMapping("/kakao/success")
    public ResponseEntity<Map<String, String>> kakaoLoginSuccess(OAuth2AuthenticationToken authentication) {
        Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");

        // email(실제 username)이 null일 경우, 카카오 ID를 기반으로 대체값 생성
        if (email == null || email.isBlank()) {
            Object idObj = attributes.get("id");
            String kakaoId = (idObj != null) ? idObj.toString() : "unknown";
            email = "kakao_" + kakaoId + "@kakao.com"; // ex) kakao_1234567890@kakao.com
        }

        UserResponseDto user = userService.findOrCreateKakaoUserDto(email, nickname);
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return ResponseEntity.ok(Map.of("token", "Bearer " + token));
    }

}
