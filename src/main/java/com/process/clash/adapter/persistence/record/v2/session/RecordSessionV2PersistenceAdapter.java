package com.process.clash.adapter.persistence.record.v2.session;

import com.process.clash.adapter.persistence.record.v2.subject.RecordSubjectV2JpaEntity;
import com.process.clash.adapter.persistence.record.v2.subject.RecordSubjectV2JpaRepository;
import com.process.clash.adapter.persistence.record.v2.task.RecordTaskV2JpaEntity;
import com.process.clash.adapter.persistence.record.v2.task.RecordTaskV2JpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.record.v2.exception.exception.notfound.ActiveSessionV2NotFoundException;
import com.process.clash.application.record.v2.exception.exception.notfound.RecordDevelopSessionV2NotFoundException;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecordSessionV2PersistenceAdapter implements RecordSessionV2RepositoryPort {

    private final RecordActiveSessionV2JpaRepository recordActiveSessionV2JpaRepository;
    private final RecordDevelopSessionV2JpaRepository recordDevelopSessionV2JpaRepository;
    private final RecordTaskSessionV2JpaRepository recordTaskSessionV2JpaRepository;
    private final RecordSubjectV2JpaRepository recordSubjectV2JpaRepository;
    private final RecordTaskV2JpaRepository recordTaskV2JpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final RecordSessionV2JpaMapper recordSessionV2JpaMapper;
    private final ZoneId recordZoneId;

    @Override
    public RecordSessionV2 save(RecordSessionV2 session) {
        if (session.id() == null) {
            return createSession(session);
        }

        return updateSession(session);
    }

    @Override
    public Boolean existsActiveSessionByUserId(Long userId) {
        return recordActiveSessionV2JpaRepository.existsByUserIdAndEndedAtIsNull(userId);
    }

    @Override
    public Optional<RecordSessionV2> findActiveSessionByUserId(Long userId) {
        return recordActiveSessionV2JpaRepository.findActiveByUserId(userId)
            .map(recordSessionV2JpaMapper::toDomain);
    }

    @Override
    public Optional<RecordSessionV2> findActiveSessionByUserIdForUpdate(Long userId) {
        return recordActiveSessionV2JpaRepository.findActiveByUserIdForUpdate(userId)
            .map(recordSessionV2JpaMapper::toDomain);
    }

    @Override
    public List<RecordSessionV2> findAllByUserIdAndTimeRange(
        Long userId,
        LocalDateTime startTime,
        LocalDateTime endTime
    ) {
        return recordActiveSessionV2JpaRepository.findAllOverlappingByUserId(
                userId,
                toInstant(startTime),
                toInstant(endTime)
            ).stream()
            .map(recordSessionV2JpaMapper::toDomain)
            .toList();
    }

    @Override
    public Boolean existsActiveSessionBySubjectId(Long subjectId) {
        return recordTaskSessionV2JpaRepository.existsActiveBySubjectId(subjectId);
    }

    @Override
    public Boolean existsActiveSessionByTaskId(Long taskId) {
        return recordTaskSessionV2JpaRepository.existsActiveByTaskId(taskId);
    }

    private RecordSessionV2 createSession(RecordSessionV2 session) {
        // 부모(active) 저장 후 타입별 자식(task/develop) 테이블에 분기 저장
        UserJpaEntity user = userJpaRepository.getReferenceById(session.userId());
        RecordActiveSessionV2JpaEntity activeEntity = recordSessionV2JpaMapper.toActiveEntity(session, user);
        RecordActiveSessionV2JpaEntity savedActive = recordActiveSessionV2JpaRepository.save(activeEntity);

        if (session.sessionType() == RecordSessionTypeV2.DEVELOP) {
            recordDevelopSessionV2JpaRepository.save(
                RecordDevelopSessionV2JpaEntity.create(savedActive, session.appId())
            );
        } else {
            RecordSubjectV2JpaEntity subject = recordSubjectV2JpaRepository.getReferenceById(session.subjectId());
            RecordTaskV2JpaEntity task = session.taskId() == null
                ? null
                : recordTaskV2JpaRepository.getReferenceById(session.taskId());
            recordTaskSessionV2JpaRepository.save(
                RecordTaskSessionV2JpaEntity.create(savedActive, subject, task)
            );
        }

        return recordActiveSessionV2JpaRepository.findByIdWithDetails(savedActive.getId())
            .map(recordSessionV2JpaMapper::toDomain)
            .orElseThrow(ActiveSessionV2NotFoundException::new);
    }

    private RecordSessionV2 updateSession(RecordSessionV2 session) {
        RecordActiveSessionV2JpaEntity activeEntity = recordActiveSessionV2JpaRepository.findById(session.id())
            .orElseThrow(ActiveSessionV2NotFoundException::new);

        activeEntity.changeEndedAt(session.endedAt());
        recordActiveSessionV2JpaRepository.save(activeEntity);

        // DEVELOP 세션만 appId 변경 가능하므로 자식 develop 테이블도 동기화
        if (session.sessionType() == RecordSessionTypeV2.DEVELOP) {
            RecordDevelopSessionV2JpaEntity developSession = recordDevelopSessionV2JpaRepository.findById(session.id())
                .orElseThrow(RecordDevelopSessionV2NotFoundException::new);
            developSession.changeAppId(session.appId());
            recordDevelopSessionV2JpaRepository.save(developSession);
        }

        return recordActiveSessionV2JpaRepository.findByIdWithDetails(session.id())
            .map(recordSessionV2JpaMapper::toDomain)
            .orElseThrow(ActiveSessionV2NotFoundException::new);
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(recordZoneId).toInstant();
    }
}
