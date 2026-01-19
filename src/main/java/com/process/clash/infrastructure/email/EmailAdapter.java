package com.process.clash.infrastructure.email;

import com.process.clash.application.mail.exception.exception.InvalidMailException;
import com.process.clash.application.mail.exception.exception.MailDeliveryException;
import com.process.clash.application.mail.exception.exception.MailMessageCreationException;
import com.process.clash.application.mail.exception.exception.MailServerAuthenticationException;
import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class EmailAdapter implements SendVerificationEmailPort {

    private final JavaMailSender javaMailSender;

    @Async
    @Override
    public void execute(String email, String verificationCode) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress("no-reply@clash.kr", "Clash", "UTF-8"));
            helper.setTo(email);
            helper.setSubject("[Clash] 계정 활성화 이메일 인증 코드");

            String htmlContent = String.format("""
            <div style="font-family: 'Apple SD Gothic Neo', sans-serif; max-width: 400px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
                <h2 style="color: #333;">Welcome to CLASH!</h2>
                <p style="font-size: 16px; color: #666;">서비스 이용을 위해 아래 인증 코드를 입력해주세요.</p>
                <div style="background-color: #f4f4f4; padding: 20px; text-align: center; border-radius: 5px; margin: 20px 0;">
                    <span style="font-size: 30px; font-weight: bold; color: #007bff; letter-spacing: 5px;">%s</span>
                </div>
                <p style="font-size: 13px; color: #999;">이 코드는 5분간 유효합니다. 본인이 요청하지 않았다면 이 메일을 무시하세요.</p>
                <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">
                <p style="font-size: 11px; color: #ccc;">© 2026 PROCESS. All rights reserved.</p>
            </div>
            """, verificationCode);

            helper.setText(htmlContent, true); // true: HTML 형식 사용

            javaMailSender.send(message);

        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new MailMessageCreationException(e);
        } catch (MailAuthenticationException e) {
            // SMTP 비밀번호가 틀렸거나 AWS SES 권한 문제 (인증 단계)
            throw new MailServerAuthenticationException(e);
        } catch (MailParseException e) {
            throw new InvalidMailException(e);
        } catch (MailException e) {
            throw new MailDeliveryException(e);
        }
    }
}
