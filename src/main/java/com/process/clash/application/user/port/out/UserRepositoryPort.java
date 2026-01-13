package com.process.clash.application.user.port.out;

import java.util.Optional;

import com.process.clash.domain.user.model.entity.User;

public interface UserRepositoryPort {
    void save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
}
