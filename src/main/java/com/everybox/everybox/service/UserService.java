package com.everybox.everybox.service;

import com.everybox.everybox.domain.User;
import com.everybox.everybox.dto.LoginRequest;
import com.everybox.everybox.dto.SignupRequest;
import com.everybox.everybox.dto.UserResponseDto;
import com.everybox.everybox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserResponseDto registerUser(SignupRequest request) {
        if (!request.getEmail().matches("^[\\w._%+-]+@[\\w.-]+\\.ac\\.kr$")) {
            throw new IllegalArgumentException("대학생 이메일(@xxx.ac.kr)만 가입할 수 있습니다.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();
        User saved = userRepository.save(user);
        return UserResponseDto.from(saved);
    }

    public UserResponseDto login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return UserResponseDto.from(user);
    }

    public UserResponseDto findDtoById(Long id) {
        return userRepository.findById(id).map(UserResponseDto::from).orElse(null);
    }

    public UserResponseDto findOrCreateKakaoUserDto(String email, String nickname) {
        return UserResponseDto.from(findOrCreateKakaoUser(email, nickname));
    }

    public User findOrCreateKakaoUser(String email, String nickname) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = User.builder()
                            .email(email)
                            .nickname(nickname)
                            .password(null)
                            .build();
                    return userRepository.save(user);
                });
    }
}
