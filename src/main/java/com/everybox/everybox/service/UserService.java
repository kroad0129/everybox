package com.everybox.everybox.service;

import com.everybox.everybox.domain.User;
import com.everybox.everybox.dto.UserLoginRequestDto;
import com.everybox.everybox.dto.UserSignupRequestDto;
import com.everybox.everybox.dto.UserUpdateRequestDto;
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
    private final MailService mailService;

    public UserResponseDto registerUser(UserSignupRequestDto request) {
        // 중복 ID 체크 등 비즈니스 정책
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 가입된 아이디입니다.");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .isVerified(false)
                .build();
        User saved = userRepository.save(user);
        return UserResponseDto.from(saved);
    }

    public void sendVerificationCode(Long userId, String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.ac\\.kr$")) {
            throw new IllegalArgumentException("학교 이메일(@xxx.ac.kr)만 인증 가능합니다.");
        }
        // 이미 인증된 이메일인지 확인
        userRepository.findByUniversityEmail(email).ifPresent(user -> {
            if (user.getIsVerified() && !user.getId().equals(userId)) {
                throw new IllegalArgumentException("이미 다른 계정에서 인증된 이메일입니다.");
            }
        });
        mailService.sendVerificationCode(userId, email);
    }

    public void verifyCode(Long userId, String code) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (mailService.verifyCode(userId, code)) {
            String verifiedEmail = mailService.getVerifiedEmail(userId); // 인증된 이메일 조회
            // 중복 체크는 이미 sendVerificationCode에서 했으므로 바로 저장
            user.setUniversityEmail(verifiedEmail);
            user.setIsVerified(true);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("인증코드가 올바르지 않습니다.");
        }
    }

    public UserResponseDto login(UserLoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));
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

    public User findOrCreateKakaoUser(String username, String nickname) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username(이메일)이 없습니다!");
        }
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User user = User.builder()
                            .username(username)
                            .nickname(nickname)
                            .password(null)
                            .isVerified(false)
                            .build();
                    return userRepository.save(user);
                });
    }

    public UserResponseDto updateUser(Long userId, Long loginUserId, UserUpdateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.getId().equals(loginUserId)) {
            throw new IllegalArgumentException("본인만 수정할 수 있습니다.");
        }
        if (request.getNickname() != null) user.setNickname(request.getNickname());
        if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
        return UserResponseDto.from(userRepository.save(user));
    }

    public void deleteUser(Long userId, Long loginUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.getId().equals(loginUserId)) {
            throw new IllegalArgumentException("본인만 삭제할 수 있습니다.");
        }
        userRepository.delete(user);
    }
}
