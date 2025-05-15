package com.everybox.everybox.controller;

import com.everybox.everybox.dto.SignupRequest;
import com.everybox.everybox.dto.UserResponseDto;
import com.everybox.everybox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody SignupRequest request) {
        UserResponseDto user = userService.registerUser(request);
        return ResponseEntity.ok(user);
    }
}
