package com.process.clash.application.user.user.port.in;

public interface VerifyEmailUseCase {

    void execute(String email, String code);
}
