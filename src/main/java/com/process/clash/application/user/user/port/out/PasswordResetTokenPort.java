package com.process.clash.application.user.user.port.out;

import java.util.Optional;

public interface PasswordResetTokenPort {
    record TokenPayload(Long userId, String state, String redirectUri) {
        public boolean hasAuthContext() {
            return state != null && !state.isBlank() && redirectUri != null && !redirectUri.isBlank();
        }
    }

    void saveToken(String token, TokenPayload payload, long expirationMs);
    Optional<TokenPayload> getTokenPayload(String token);
    void deleteToken(String token);
}
