package com.process.clash.adapter.persistence.session;

import com.process.clash.application.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.model.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SessionPersistenceAdapter implements UserRepositoryPort {

    private final SessionJpaRepository sessionJpaRepository;
    private final SessionJpaMapper sessionJpaMapper;

    @Override
    public void save(User user) {
        SessionJpaEntity sessionJpaEntity = sessionJpaMapper.toJpaEntity(user);
        userJpaRepository.save(sessionJpaEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(sessionJpaMapper::toDomain);
    }
}
