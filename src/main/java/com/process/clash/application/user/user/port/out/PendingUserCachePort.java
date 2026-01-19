package com.process.clash.application.user.user.port.out;

import com.process.clash.domain.user.user.entity.User;

import java.util.Optional;

public interface PendingUserCachePort {
    void save(String email, User user, long ttlMs);
    Optional<User> findByEmail(String email);
    void delete(String email);
}
