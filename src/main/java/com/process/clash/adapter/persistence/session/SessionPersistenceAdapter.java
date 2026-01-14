package com.process.clash.adapter.persistence.session;

import com.process.clash.adapter.persistence.task.TaskJpaEntity;
import com.process.clash.adapter.persistence.task.TaskJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
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
    private final UserJpaRepository userJpaRepository;
    private final TaskJpaRepository taskJpaRepository;

    @Override
    public void save(Session session) {
        UserJpaEntity user = userJpaRepository.getReferenceById(session.user().id());
        TaskJpaEntity task = taskJpaRepository.getReferenceById(session.task().id());
        SessionJpaEntity sessionJpaEntity = sessionJpaMapper.toJpaEntity(session, user, task);
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
