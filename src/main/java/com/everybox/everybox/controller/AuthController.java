package com.everybox.everybox.controller;

import com.everybox.everybox.dto.LoginRequest;
import com.everybox.everybox.dto.SignupRequest;
import com.everybox.everybox.dto.UserResponseDto;
import com.everybox.everybox.service.UserService;
import com.everybox.everybox.util.JwtUtil;
import com.everybox.everybox.security.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody SignupRequest request) {
        UserResponseDto user = userService.registerUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        UserResponseDto user = userService.login(request);
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        return ResponseEntity.ok(Map.of("token", "Bearer " + token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        UserResponseDto user = userService.findDtoById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/kakao/success")
    public ResponseEntity<Map<String, String>> kakaoLoginSuccess(OAuth2AuthenticationToken authentication) {
        Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");

        UserResponseDto user = userService.findOrCreateKakaoUserDto(email, nickname);
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        return ResponseEntity.ok(Map.of("token", "Bearer " + token));
    }
}
