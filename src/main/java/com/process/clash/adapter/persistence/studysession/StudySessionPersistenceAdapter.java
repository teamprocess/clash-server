package com.process.clash.adapter.persistence.studysession;

import com.process.clash.adapter.persistence.task.TaskJpaEntity;
import com.process.clash.adapter.persistence.task.TaskJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.domain.record.model.entity.StudySession;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudySessionPersistenceAdapter implements StudySessionRepositoryPort {

    private final StudySessionJpaRepository studySessionJpaRepository;
    private final StudySessionJpaMapper studySessionJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final TaskJpaRepository taskJpaRepository;

    @Override
    public void save(StudySession studySession) {
        UserJpaEntity user = userJpaRepository.getReferenceById(studySession.user().id());
        TaskJpaEntity task = taskJpaRepository.getReferenceById(studySession.task().id());
        StudySessionJpaEntity studySessionJpaEntity = studySessionJpaMapper.toJpaEntity(studySession, user, task);
        studySessionJpaRepository.save(studySessionJpaEntity);
    }

    @Override
    public Optional<StudySession> findById(Long id) {
        return studySessionJpaRepository.findById(id).map(studySessionJpaMapper::toDomain);
    }

    @Override
    public List<StudySession> findAllByUserId(Long userId) {
        return studySessionJpaRepository.findAllByUserId(userId).stream()
            .map(studySessionJpaMapper::toDomain)
            .toList();
    }

    @Override
    public Boolean existsActiveSessionByUserId(Long userId) {
        return studySessionJpaRepository.existsByUserIdAndEndedAtIsNull(userId);
    }

    @Override
    public Optional<StudySession> findActiveSessionByUserId(Long userId) {
        return studySessionJpaRepository.findByUserIdAndEndedAtIsNull(userId);
    }
}
