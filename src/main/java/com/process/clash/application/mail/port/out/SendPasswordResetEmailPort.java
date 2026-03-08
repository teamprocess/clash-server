package com.process.clash.application.mail.port.out;

public interface SendPasswordResetEmailPort {
    void execute(String email, String resetLink);
}
