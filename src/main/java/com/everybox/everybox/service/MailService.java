package com.everybox.everybox.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final Map<Long, String> codeStore = new HashMap<>();      // userId -> 인증코드 저장
    private final Map<Long, String> emailStore = new HashMap<>();     // userId -> 인증 이메일 저장

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 인증코드 발송 및 이메일 저장
    public void sendVerificationCode(Long userId, String email) {
        String code = generateCode();
        codeStore.put(userId, code);
        emailStore.put(userId, email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Everybox 이메일 인증코드");
        message.setText("인증코드: " + code);
        mailSender.send(message);
    }

    // 인증코드 검증
    public boolean verifyCode(Long userId, String code) {
        String savedCode = codeStore.get(userId);
        if (savedCode != null && savedCode.equals(code)) {
            // 인증 성공 시 인증코드 삭제 (클린업)
            codeStore.remove(userId);
            return true;
        }
        return false;
    }

    // 인증된 이메일 조회 (인증이 완료된 사용자용)
    public String getVerifiedEmail(Long userId) {
        return emailStore.get(userId);
    }

    // 6자리 숫자 인증코드 생성
    private String generateCode() {
        int len = 6;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
