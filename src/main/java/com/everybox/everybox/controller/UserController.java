package com.everybox.everybox.controller;

import com.everybox.everybox.domain.User;
import com.everybox.everybox.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(request.getPassword())
                .build();

        return userRepository.save(user);
    }

    @Data
    static class CreateUserRequest {
        private String email;
        private String nickname;
        private String password;
    }
}