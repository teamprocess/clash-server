package com.process.clash.adapter.persistence.record.v2.session;

import com.process.clash.adapter.persistence.record.v2.subject.RecordSubjectV2JpaEntity;
import com.process.clash.adapter.persistence.record.v2.subject.RecordSubjectV2JpaRepository;
import com.process.clash.adapter.persistence.record.v2.task.RecordTaskV2JpaEntity;
import com.process.clash.adapter.persistence.record.v2.task.RecordTaskV2JpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.ranking.data.UserRanking;
import com.process.clash.application.record.v2.exception.exception.notfound.ActiveSessionV2NotFoundException;
import com.process.clash.application.record.v2.exception.exception.notfound.RecordDevelopSessionV2NotFoundException;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class RecordSessionV2PersistenceAdapter implements RecordSessionV2RepositoryPort {

    private final RecordActiveSessionV2JpaRepository recordActiveSessionV2JpaRepository;
    private final RecordTaskSessionV2JpaRepository recordTaskSessionV2JpaRepository;
    private final RecordSubjectV2JpaRepository recordSubjectV2JpaRepository;
    private final RecordTaskV2JpaRepository recordTaskV2JpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final RecordSessionV2JpaMapper recordSessionV2JpaMapper;
    private final ZoneId recordZoneId;

    @Override
    @Transactional
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
    public List<RecordSessionV2> findAllActiveSessions() {
        return recordActiveSessionV2JpaRepository.findAllActiveSessions().stream()
            .map(recordSessionV2JpaMapper::toDomain)
            .toList();
    }

    @Override
    public List<Long> findAllActiveUserIds() {
        return recordActiveSessionV2JpaRepository.findAllActiveUserIds();
    }

    @Override
    public Boolean existsActiveSessionBySubjectId(Long subjectId) {
        return recordTaskSessionV2JpaRepository.existsActiveBySubjectId(subjectId);
    }

    @Override
    public Boolean existsActiveSessionByTaskId(Long taskId) {
        return recordTaskSessionV2JpaRepository.existsActiveByTaskId(taskId);
    }

    @Override
    public Long getTotalStudyTimeInSeconds(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        Instant now = Instant.now();
        return recordActiveSessionV2JpaRepository.getTotalStudyTimeInSeconds(
            userId,
            toInstant(startOfDay),
            toInstant(endOfDay),
            now
        );
    }

    @Override
    public Map<Long, Long> getTotalStudyTimeInSecondsByUserIds(
        List<Long> userIds,
        LocalDateTime startTime,
        LocalDateTime endTime
    ) {
        if (userIds.isEmpty()) {
            return Map.of();
        }

        Instant now = Instant.now();
        return recordActiveSessionV2JpaRepository
            .getTotalStudyTimeInSecondsByUserIds(userIds, toInstant(startTime), toInstant(endTime), now)
            .stream()
            .collect(Collectors.toMap(
                RecordActiveSessionV2JpaRepository.UserStudyTimeProjectionV2::getUserId,
                RecordActiveSessionV2JpaRepository.UserStudyTimeProjectionV2::getTotalSeconds
            ));
    }

    @Override
    public List<UserRanking> findStudyTimeRankingByUserIdAndPeriod(
        Long userId,
        LocalDateTime startDate,
        LocalDateTime endDate
    ) {
        return recordActiveSessionV2JpaRepository.findStudyTimeRankingByUserIdAndPeriod(
            userId,
            toInstant(startDate),
            toInstant(endDate)
        );
    }

    @Override
    public List<Object[]> findDailyStudyTimeByUserIds(List<Long> userIds, Instant startDate, Instant endDate) {
        return recordActiveSessionV2JpaRepository.findDailyStudyTimeByUserIds(userIds, startDate, endDate);
    }

    @Override
    public List<Object[]> findWeeklyStudyTimeByUserIds(List<Long> userIds, Instant startDate, Instant endDate) {
        return recordActiveSessionV2JpaRepository.findWeeklyStudyTimeByUserIds(userIds, startDate, endDate);
    }

    @Override
    public List<Object[]> findMonthlyStudyTimeByUserIds(List<Long> userIds, Instant startDate, Instant endDate) {
        return recordActiveSessionV2JpaRepository.findMonthlyStudyTimeByUserIds(userIds, startDate, endDate);
    }

    private RecordSessionV2 createSession(RecordSessionV2 session) {
        // Aggregate 루트(active) 기준으로 자식을 연결하고, 부모 저장 시 cascade로 함께 저장한다.
        UserJpaEntity user = userJpaRepository.getReferenceById(session.userId());
        RecordActiveSessionV2JpaEntity activeEntity = recordSessionV2JpaMapper.toActiveEntity(session, user);

        if (session.sessionType() == RecordSessionTypeV2.DEVELOP) {
            activeEntity.attachDevelopSession(
                RecordDevelopSessionV2JpaEntity.create(activeEntity, session.appId())
            );
        } else {
            RecordSubjectV2JpaEntity subject = recordSubjectV2JpaRepository.getReferenceById(session.subjectId());
            RecordTaskV2JpaEntity task = session.taskId() == null
                ? null
                : recordTaskV2JpaRepository.getReferenceById(session.taskId());
            activeEntity.attachTaskSession(
                RecordTaskSessionV2JpaEntity.create(activeEntity, subject, task)
            );
        }
        RecordActiveSessionV2JpaEntity savedActive = recordActiveSessionV2JpaRepository.save(activeEntity);

        return recordActiveSessionV2JpaRepository.findByIdWithDetails(savedActive.getId())
            .map(recordSessionV2JpaMapper::toDomain)
            .orElseThrow(ActiveSessionV2NotFoundException::new);
    }

    private RecordSessionV2 updateSession(RecordSessionV2 session) {
        RecordActiveSessionV2JpaEntity activeEntity = recordActiveSessionV2JpaRepository.findByIdWithDetails(session.id())
            .orElseThrow(ActiveSessionV2NotFoundException::new);

        activeEntity.changeEndedAt(session.endedAt());

        // DEVELOP 세션만 appId 변경 가능하므로 자식 develop 테이블도 동기화
        if (session.sessionType() == RecordSessionTypeV2.DEVELOP) {
            RecordDevelopSessionV2JpaEntity developSession = activeEntity.getDevelopSession();
            if (developSession == null) {
                throw new RecordDevelopSessionV2NotFoundException();
            }
            developSession.changeAppId(session.appId());
        }

        return recordSessionV2JpaMapper.toDomain(activeEntity);
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(recordZoneId).toInstant();
    }
}
