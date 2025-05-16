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
    private final Map<Long, String> codeStore = new HashMap<>();

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 인증코드 발송
    public void sendVerificationCode(Long userId, String email) {
        String code = generateCode();
        codeStore.put(userId, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Everybox 이메일 인증코드");
        message.setText("인증코드: " + code);
        mailSender.send(message);
    }

    // 인증코드 검증
    public boolean verifyCode(Long userId, String code) {
        String saved = codeStore.get(userId);
        return saved != null && saved.equals(code);
    }

    private String generateCode() {
        int len = 6;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) sb.append(random.nextInt(10));
        return sb.toString();
    }
}
