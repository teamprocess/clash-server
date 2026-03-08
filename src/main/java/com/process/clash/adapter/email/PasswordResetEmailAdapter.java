package com.process.clash.adapter.email;

import com.process.clash.application.mail.exception.exception.InvalidMailException;
import com.process.clash.application.mail.exception.exception.MailDeliveryException;
import com.process.clash.application.mail.exception.exception.MailMessageCreationException;
import com.process.clash.application.mail.exception.exception.MailServerAuthenticationException;
import com.process.clash.application.mail.port.out.SendPasswordResetEmailPort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class PasswordResetEmailAdapter implements SendPasswordResetEmailPort {

    private final JavaMailSender javaMailSender;

    @Value("${mail.sender.address}")
    private String senderAddress;

    @Value("${mail.sender.name}")
    private String senderName;

    @Async
    @Override
    public void execute(String email, String resetLink) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress(senderAddress, senderName, "UTF-8"));
            helper.setTo(email);
            helper.setSubject("[Clash] 비밀번호 재설정");

            String htmlContent = String.format("""
            <div style="font-family: 'Apple SD Gothic Neo', sans-serif; max-width: 400px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
                <h2 style="color: #333;">비밀번호 재설정</h2>
                <p style="font-size: 16px; color: #666;">아래 버튼을 클릭해 비밀번호를 재설정하세요.</p>
                <div style="text-align: center; margin: 30px 0;">
                    <a href="%s" style="background-color: #007bff; color: #fff; padding: 12px 24px; border-radius: 5px; text-decoration: none; font-size: 16px;">비밀번호 재설정</a>
                </div>
                <p style="font-size: 13px; color: #999;">이 링크는 30분간 유효합니다. 본인이 요청하지 않았다면 이 메일을 무시하세요.</p>
                <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">
                <p style="font-size: 11px; color: #ccc;">© 2026 PROCESS. All rights reserved.</p>
            </div>
            """, resetLink);

            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new MailMessageCreationException(e);
        } catch (MailAuthenticationException e) {
            throw new MailServerAuthenticationException(e);
        } catch (MailParseException e) {
            throw new InvalidMailException(e);
        } catch (MailException e) {
            throw new MailDeliveryException(e);
        }
    }
}
