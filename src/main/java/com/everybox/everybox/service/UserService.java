package com.everybox.everybox.service;

import com.everybox.everybox.domain.User;
import com.everybox.everybox.dto.SignupRequest;
import com.everybox.everybox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void registerUser(SignupRequest request) {
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

        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User findOrCreateKakaoUser(String email, String nickname) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = User.builder()
                            .email(email)
                            .nickname(nickname)
                            .password(null)  // 소셜 로그인은 비밀번호 없음
                            .build();
                    return userRepository.save(user);
                });
    }
}
