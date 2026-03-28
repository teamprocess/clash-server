package com.process.clash.adapter.persistence.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;
    private static final String KEY_PREFIX = "pwd_reset:";

    @Override
    public void saveToken(String token, TokenPayload payload, long expirationMs) {
        redisTemplate.opsForValue().set(
                KEY_PREFIX + token,
                serialize(payload),
                expirationMs,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public Optional<TokenPayload> getTokenPayload(String token) {
        String value = redisTemplate.opsForValue().get(KEY_PREFIX + token);
        if (value == null) {
            return Optional.empty();
        }

        return deserialize(value);
    }

    @Override
    public void deleteToken(String token) {
        redisTemplate.delete(KEY_PREFIX + token);
    }

    private String serialize(TokenPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize password reset token payload", exception);
        }
    }

    private Optional<TokenPayload> deserialize(String value) {
        if (!value.startsWith("{")) {
            return Optional.of(new TokenPayload(Long.parseLong(value), null, null));
        }

        try {
            return Optional.of(objectMapper.readValue(value, TokenPayload.class));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to deserialize password reset token payload", exception);
        }
    }
}
