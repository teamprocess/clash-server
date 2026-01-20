package com.process.clash.application.user.user.port.out;

import com.process.clash.domain.user.user.entity.User;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    void saveAndFlush(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    void flush();
}
