package com.process.clash.adapter.persistence.auth;

import com.process.clash.application.mail.port.out.VerificationCodePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class VerificationCodePersistenceAdapter implements VerificationCodePort {

    private final StringRedisTemplate redisTemplate;
    private static final String MAIL_PREFIX = "mail:";


    @Override
    public void saveCode(String email, String code, long expirationMilliseconds) {
        redisTemplate.opsForValue().set(MAIL_PREFIX + email, code, expirationMilliseconds, TimeUnit.MILLISECONDS);
    }

    @Override
    public Optional<String> getCode(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(MAIL_PREFIX + email));
    }

    @Override
    public void deleteCode(String email) {
        redisTemplate.delete(MAIL_PREFIX + email);
    }
}
