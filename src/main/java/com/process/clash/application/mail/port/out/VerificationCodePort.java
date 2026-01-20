package com.process.clash.application.mail.port.out;

import java.util.Optional;

public interface VerificationCodePort {

    void saveCode(String token, String code, long expirationMilliseconds);
    Optional<String> getCode(String token);
    void deleteCode(String token);
}
