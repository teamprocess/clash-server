package com.process.clash.application.user.port.out;

import com.process.clash.domain.user.user.model.entity.User;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
