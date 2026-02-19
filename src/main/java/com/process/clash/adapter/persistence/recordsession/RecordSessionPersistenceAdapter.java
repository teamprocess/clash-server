package com.process.clash.adapter.persistence.recordsession;

import com.process.clash.adapter.persistence.recordtask.RecordTaskJpaEntity;
import com.process.clash.adapter.persistence.recordtask.RecordTaskJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.ranking.data.UserRanking;
import com.process.clash.application.record.exception.exception.notfound.RecordSessionNotFound;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.domain.record.entity.RecordSession;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecordSessionPersistenceAdapter implements RecordSessionRepositoryPort {

    private final RecordSessionJpaRepository recordSessionJpaRepository;
    private final RecordSessionJpaMapper recordSessionJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final RecordTaskJpaRepository recordTaskJpaRepository;
    private final ZoneId recordZoneId;

    @Override
    public RecordSession save(RecordSession recordSession) {
        if (recordSession.id() == null) {
            UserJpaEntity user = userJpaRepository.getReferenceById(recordSession.user().id());
            RecordTaskJpaEntity task = recordSession.task() == null
                    ? null
                    : recordTaskJpaRepository.getReferenceById(recordSession.task().id());
            RecordSessionJpaEntity recordSessionJpaEntity = recordSessionJpaMapper.toJpaEntity(recordSession, user, task);
            recordSessionJpaRepository.save(recordSessionJpaEntity);
            return recordSessionJpaMapper.toDomain(recordSessionJpaEntity);
        }

        RecordSessionJpaEntity existing = recordSessionJpaRepository.findById(recordSession.id())
                .orElseThrow(RecordSessionNotFound::new);
        existing.changeEndedAt(recordSession.endedAt());
        existing.changeAppName(recordSession.appName());
        recordSessionJpaRepository.save(existing);
        return recordSessionJpaMapper.toDomain(existing);
    }

    @Override
    public void saveAll(List<RecordSession> recordSessions) {
        if (recordSessions == null || recordSessions.isEmpty()) {
            return;
        }

        List<RecordSession> newSessions = new ArrayList<>();
        List<RecordSession> existingSessions = new ArrayList<>();

        for (RecordSession recordSession : recordSessions) {
            if (recordSession.id() == null) {
                newSessions.add(recordSession);
            } else {
                existingSessions.add(recordSession);
            }
        }

        if (!newSessions.isEmpty()) {
            List<RecordSessionJpaEntity> entitiesToCreate = newSessions.stream()
                    .map(session -> {
                        UserJpaEntity user = userJpaRepository.getReferenceById(session.user().id());
                        RecordTaskJpaEntity task = session.task() == null
                                ? null
                                : recordTaskJpaRepository.getReferenceById(session.task().id());
                        return recordSessionJpaMapper.toJpaEntity(session, user, task);
                    })
                    .toList();
            recordSessionJpaRepository.saveAll(entitiesToCreate);
        }

        if (!existingSessions.isEmpty()) {
            List<Long> ids = existingSessions.stream()
                    .map(RecordSession::id)
                    .toList();
            Map<Long, RecordSessionJpaEntity> existingEntities = recordSessionJpaRepository.findAllById(ids).stream()
                    .collect(Collectors.toMap(RecordSessionJpaEntity::getId, entity -> entity));

            for (RecordSession session : existingSessions) {
                RecordSessionJpaEntity entity = existingEntities.get(session.id());
                if (entity != null) {
                    entity.changeEndedAt(session.endedAt());
                    entity.changeAppName(session.appName());
                }
            }

            recordSessionJpaRepository.saveAll(existingEntities.values());
        }
    }

    @Override
    public Optional<RecordSession> findById(Long id) {
        return recordSessionJpaRepository.findById(id).map(recordSessionJpaMapper::toDomain);
    }

    @Override
    public List<RecordSession> findAllByUserId(Long userId) {
        return recordSessionJpaRepository.findAllByUserId(userId).stream()
                .map(recordSessionJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Boolean existsActiveSessionByUserId(Long userId) {
        return recordSessionJpaRepository.existsByUserIdAndEndedAtIsNull(userId);
    }

    @Override
    public Optional<RecordSession> findActiveSessionByUserId(Long userId) {
        return recordSessionJpaRepository.findByUserIdAndEndedAtIsNull(userId)
                .map(recordSessionJpaMapper::toDomain);
    }

    @Override
    public Optional<RecordSession> findActiveSessionByUserIdForUpdate(Long userId) {
        return recordSessionJpaRepository.findActiveByUserIdForUpdate(userId)
                .map(recordSessionJpaMapper::toDomain);
    }

    @Override
    public List<RecordSession> findAllByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return recordSessionJpaRepository.findAllOverlappingByUserId(
                        userId,
                        toInstant(startTime),
                        toInstant(endTime)
                ).stream()
                .map(recordSessionJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<RecordSession> findAllActiveSessions() {
        return recordSessionJpaRepository.findAllByEndedAtIsNull().stream()
                .map(recordSessionJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Boolean existsActiveSessionByTaskId(Long taskId) {
        return recordSessionJpaRepository.existsByTaskIdAndEndedAtIsNull(taskId);
    }

    @Override
    public Long getTotalStudyTimeInSeconds(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        Instant now = Instant.now();
        return recordSessionJpaRepository.getTotalStudyTimeInSeconds(
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
        return recordSessionJpaRepository
                .getTotalStudyTimeInSecondsByUserIds(userIds, toInstant(startTime), toInstant(endTime), now)
                .stream()
                .collect(Collectors.toMap(
                        RecordSessionJpaRepository.UserStudyTimeProjection::getUserId,
                        RecordSessionJpaRepository.UserStudyTimeProjection::getTotalSeconds
                ));
    }

    @Override
    public List<UserRanking> findStudyTimeRankingByUserIdAndPeriod(Long userId, LocalDateTime startDate, LocalDateTime endDate) {

        return recordSessionJpaRepository.findStudyTimeRankingByUserIdAndPeriod(
                userId,
                toInstant(startDate),
                toInstant(endDate)
        );
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(recordZoneId).toInstant();
    }

    @Override
    public List<Object[]> findDailyStudyTimeByUserIds(List<Long> userIds, Instant startDate, Instant endDate) {

        return recordSessionJpaRepository.findDailyStudyTimeByUserIds(userIds, startDate, endDate);
    }

    @Override
    public List<Object[]> findWeeklyStudyTimeByUserIds(List<Long> userIds, Instant startDate, Instant endDate) {

        return recordSessionJpaRepository.findWeeklyStudyTimeByUserIds(userIds, startDate, endDate);
    }

    @Override
    public List<Object[]> findMonthlyStudyTimeByUserIds(List<Long> userIds, Instant startDate, Instant endDate) {

        return recordSessionJpaRepository.findMonthlyStudyTimeByUserIds(userIds, startDate, endDate);
    }
}
