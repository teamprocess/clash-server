package com.process.clash.adapter.persistence.user;

import com.process.clash.application.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public void save(User user) {
        UserJpaEntity userJpaEntity = UserJpaMapper.toJpaEntity(user);
        userJpaRepository.save(userJpaEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(UserJpaMapper::toDomain);
    }
}
