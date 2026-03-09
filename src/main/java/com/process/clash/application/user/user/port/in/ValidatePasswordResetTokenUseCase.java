package com.process.clash.application.user.user.port.in;

public interface ValidatePasswordResetTokenUseCase {
    void execute(String token);
}
