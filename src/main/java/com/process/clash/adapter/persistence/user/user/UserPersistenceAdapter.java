package com.process.clash.adapter.persistence.user.user;

import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserJpaMapper userJpaMapper;

    @Override
    public User save(User user) {
        UserJpaEntity userJpaEntity = userJpaMapper.toJpaEntity(user);
        UserJpaEntity savedEntity = userJpaRepository.save(userJpaEntity);
        return userJpaMapper.toDomain(savedEntity);
    }

    @Override
    public void saveAndFlush(User user) {

        userJpaRepository.saveAndFlush(userJpaMapper.toJpaEntity(user));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(userJpaMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(userJpaMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(userJpaMapper::toDomain);
    }

    @Override
    public void flush() {

        userJpaRepository.flush();
    }

    @Override
    public List<User> findAllByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        return userJpaRepository.findAllById(ids).stream()
                .map(userJpaMapper::toDomain).toList();
    }

    @Override
    public List<User> findByIdIn(Set<Long> ids) {
        return userJpaRepository.findByIdIn(ids).stream()
                .map(userJpaMapper::toDomain)
                .toList();
    }
}