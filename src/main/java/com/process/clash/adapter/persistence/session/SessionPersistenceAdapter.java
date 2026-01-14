package com.process.clash.adapter.persistence.session;

import com.process.clash.application.record.port.out.SessionRepositoryPort;
import com.process.clash.domain.record.model.entity.Session;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SessionPersistenceAdapter implements SessionRepositoryPort {

    private final SessionJpaRepository sessionJpaRepository;
    private final SessionJpaMapper sessionJpaMapper;

    @Override
    public void save(Session session) {
        SessionJpaEntity sessionJpaEntity = sessionJpaMapper.toJpaEntity(session);
        sessionJpaRepository.save(sessionJpaEntity);
    }

    @Override
    public Optional<Session> findById(Long id) {
        return sessionJpaRepository.findById(id).map(sessionJpaMapper::toDomain);
    }

    @Override
    public List<Session> findAllByUserId(Long userId) {
        return sessionJpaRepository.findAllByUserId(userId).stream()
            .map(sessionJpaMapper::toDomain)
            .toList();
    }
}
