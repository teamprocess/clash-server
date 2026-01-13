package com.process.clash.application.user.port.out;

import com.process.clash.domain.user.model.entity.User;

public interface UserRepositoryPort {
    User save(User user);
}
