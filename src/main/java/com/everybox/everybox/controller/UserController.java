package com.everybox.everybox.controller;

import com.everybox.everybox.dto.*;
import com.everybox.everybox.security.JwtAuthentication;
import com.everybox.everybox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank() ||
                request.getNickname() == null || request.getNickname().isBlank()) {
            return ResponseEntity.badRequest().body("필수 항목이 누락되었습니다.");
        }

        userService.registerUser(request);
        return ResponseEntity.ok("회원가입 성공! 로그인 후 이메일 인증을 진행하세요.");
    }

    @PostMapping("/email/verify-request")
    public ResponseEntity<?> sendVerificationMail(
            @RequestBody EmailRequest request,
            Authentication authentication) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("이메일을 입력하세요.");
        }
        Long userId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        userService.sendVerificationCode(userId, request.getEmail());
        return ResponseEntity.ok("인증 메일을 발송했습니다.");
    }

    @PostMapping("/email/verify-confirm")
    public ResponseEntity<?> verifyEmail(
            @RequestBody EmailVerifyRequest request,
            Authentication authentication) {
        if (request.getCode() == null || request.getCode().isBlank()) {
            return ResponseEntity.badRequest().body("인증코드를 입력하세요.");
        }
        Long userId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        userService.verifyCode(userId, request.getCode());
        return ResponseEntity.ok("이메일 인증 완료! 이제 모든 서비스 이용이 가능합니다.");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long userId,
            @RequestBody UpdateUserRequestDto request,
            Authentication authentication) {
        Long loginUserId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        return ResponseEntity.ok(userService.updateUser(userId, loginUserId, request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId,
            Authentication authentication) {
        Long loginUserId = ((JwtAuthentication) authentication.getPrincipal()).getUserId();
        userService.deleteUser(userId, loginUserId);
        return ResponseEntity.noContent().build();
    }
}
