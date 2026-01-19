package com.process.clash.application.mail.port.out;

public interface SendVerificationEmailPort {
    void execute(String email, String verificationCode);
}
