package com.process.clash.application.user.port.out;

import com.process.clash.domain.user.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
