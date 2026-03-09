package com.process.clash.application.user.user.port.out;

import java.util.Optional;

public interface PasswordResetTokenPort {
    void saveToken(String token, Long userId, long expirationMs);
    Optional<Long> getUserId(String token);
    void deleteToken(String token);
}
