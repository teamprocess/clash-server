package com.process.clash.adapter.persistence.auth;

import com.process.clash.application.user.user.port.out.PasswordResetTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class PasswordResetTokenPersistenceAdapter implements PasswordResetTokenPort {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "pwd_reset:";

    @Override
    public void saveToken(String token, Long userId, long expirationMs) {
        redisTemplate.opsForValue().set(KEY_PREFIX + token, userId.toString(), expirationMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public Optional<Long> getUserId(String token) {
        String value = redisTemplate.opsForValue().get(KEY_PREFIX + token);
        if (value == null) return Optional.empty();
        return Optional.of(Long.parseLong(value));
    }

    @Override
    public void deleteToken(String token) {
        redisTemplate.delete(KEY_PREFIX + token);
    }
}
