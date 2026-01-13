package com.process.clash.adapter.persistence.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.process.clash.application.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.model.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserJpaMapper userJpaMapper;

    @Override
    public void save(User user) {
        UserJpaEntity userJpaEntity = userJpaMapper.toJpaEntity(user);
        userJpaRepository.save(userJpaEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(userJpaMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(userJpaMapper::toDomain);
    }
}
