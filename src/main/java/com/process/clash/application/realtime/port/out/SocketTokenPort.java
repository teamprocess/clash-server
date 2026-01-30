package com.process.clash.application.realtime.port.out;

import java.time.Duration;
import java.util.Optional;

public interface SocketTokenPort {
    void storeToken(String token, Long userId, Duration ttl);
    Optional<Long> resolveUserId(String token);
    void deleteToken(String token);
}
