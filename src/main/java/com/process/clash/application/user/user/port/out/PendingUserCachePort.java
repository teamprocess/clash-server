package com.process.clash.application.user.user.port.out;

import com.process.clash.domain.user.user.entity.User;

import java.util.Optional;

public interface PendingUserCachePort {
    void save(String token, User user, long ttlMs);
    Optional<User> findByToken(String token);
    void delete(String token);
}
