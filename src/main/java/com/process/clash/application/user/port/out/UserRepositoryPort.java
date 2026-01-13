package com.process.clash.application.user.port.out;

import com.process.clash.domain.user.model.entity.User;

import java.util.Optional;

public interface UserRepositoryPort {
    void save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
}
