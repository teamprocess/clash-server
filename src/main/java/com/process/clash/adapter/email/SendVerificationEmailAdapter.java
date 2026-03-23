package com.process.clash.adapter.email;

import com.process.clash.application.mail.exception.exception.InvalidMailException;
import com.process.clash.application.mail.exception.exception.MailDeliveryException;
import com.process.clash.application.mail.exception.exception.MailMessageCreationException;
import com.process.clash.application.mail.exception.exception.MailServerAuthenticationException;
import com.process.clash.application.mail.port.out.SendVerificationEmailPort;
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
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class SendVerificationEmailAdapter implements SendVerificationEmailPort {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${mail.sender.address}")
    private String senderAddress;

    @Value("${mail.sender.name}")
    private String senderName;

    @Async
    @Override
    public void execute(String email, String verificationCode) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress(senderAddress, senderName, "UTF-8"));
            helper.setTo(email);
            helper.setSubject("[Clash] 계정 활성화 이메일 인증 코드");

            Context context = new Context();
            context.setVariable("verificationCode", verificationCode);
            String htmlContent = templateEngine.process("email/verification-email", context);

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
