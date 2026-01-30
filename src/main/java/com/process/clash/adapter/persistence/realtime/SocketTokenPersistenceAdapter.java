package com.process.clash.adapter.persistence.realtime;

import com.process.clash.application.realtime.port.out.SocketTokenPort;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocketTokenPersistenceAdapter implements SocketTokenPort {

    private static final String KEY_PREFIX = "socket:token:";

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void storeToken(String token, Long userId, Duration ttl) {
        if (token == null || userId == null || ttl == null) {
            return;
        }
        String key = KEY_PREFIX + token;
        stringRedisTemplate.opsForValue().set(key, String.valueOf(userId), ttl);
    }

    @Override
    public Optional<Long> resolveUserId(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        String key = KEY_PREFIX + token;
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Long.parseLong(value));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteToken(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        stringRedisTemplate.delete(KEY_PREFIX + token);
    }
}
