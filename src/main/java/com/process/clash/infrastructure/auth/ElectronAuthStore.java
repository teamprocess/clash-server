package com.process.clash.infrastructure.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ElectronAuthStore {

    private final StringRedisTemplate redis;

    private static final Duration STATE_TTL = Duration.ofMinutes(5);
    private static final Duration CODE_TTL = Duration.ofSeconds(60);

    public void saveState(String state) {
        redis.opsForValue().set(stateKey(state), "1", STATE_TTL);
    }

    public boolean consumeState(String state) {
        Boolean existed = redis.hasKey(stateKey(state));
        if (Boolean.TRUE.equals(existed)) {
            redis.delete(stateKey(state));
            return true;
        }
        return false;
    }

    public void saveOneTimeCode(String code, String state, Long userId) {
        String value = state + "|" + userId;
        redis.opsForValue().set(codeKey(code), value, CODE_TTL);
    }

    public OneTimeCodePayload consumeOneTimeCode(String code) {
        String value = redis.opsForValue().get(codeKey(code));
        if (value == null) return null;
        redis.delete(codeKey(code));

        String[] parts = value.split("\\|", 2);
        if (parts.length != 2) return null;

        return new OneTimeCodePayload(parts[0], Long.parseLong(parts[1]));
    }

    private String stateKey(String state) {
        return "electron:auth:state:" + state;
    }

    private String codeKey(String code) {
        return "electron:auth:code:" + code;
    }

    public record OneTimeCodePayload(String state, Long userId) {}
}
