package com.process.clash.infrastructure.email;

import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailAdapter implements SendVerificationEmailPort {

    private final JavaMailSender javaMailSender;

    @Override
    public void execute(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@clash.kr");
        message.setTo(email);
        message.setSubject("[Clash] 회원가입 인증 번호");
        message.setText(verificationCode);
        javaMailSender.send(message);
    }
}
