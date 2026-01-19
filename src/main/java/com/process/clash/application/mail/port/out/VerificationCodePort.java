package com.process.clash.application.mail.port.out;

import java.util.Optional;

public interface VerificationCodePort {

    void saveCode(String email, String code, long expirationMilliseconds);
    Optional<String> getCode(String email);
    void deleteCode(String email);
}
