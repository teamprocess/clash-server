package com.process.clash.adapter.persistence.auth;

import com.process.clash.application.mail.port.out.VerificationCodePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class VerificationCodePersistenceAdapter implements VerificationCodePort {

    private final StringRedisTemplate redisTemplate;
    private static final String TOKEN_PREFIX = "token:";

    @Override
    public void saveCode(String token, String code, long expirationMilliseconds) {
        redisTemplate.opsForValue().set(TOKEN_PREFIX + token, code, expirationMilliseconds, TimeUnit.MILLISECONDS);
    }

    @Override
    public Optional<String> getCode(String token) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(TOKEN_PREFIX + token));
    }

    @Override
    public void deleteCode(String token) {
        redisTemplate.delete(TOKEN_PREFIX + token);
    }
}
