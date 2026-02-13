package com.process.clash.adapter.persistence.studysession;

import com.process.clash.adapter.persistence.task.TaskJpaEntity;
import com.process.clash.adapter.persistence.task.TaskJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.ranking.data.UserRanking;
import com.process.clash.application.record.exception.exception.notfound.StudySessionNotFound;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.domain.record.entity.StudySession;
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
public class StudySessionPersistenceAdapter implements StudySessionRepositoryPort {

    private final StudySessionJpaRepository studySessionJpaRepository;
    private final StudySessionJpaMapper studySessionJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final TaskJpaRepository taskJpaRepository;
    private final ZoneId recordZoneId;

    @Override
    public StudySession save(StudySession studySession) {
        if (studySession.id() == null) {
            UserJpaEntity user = userJpaRepository.getReferenceById(studySession.user().id());
            TaskJpaEntity task = studySession.task() == null
                ? null
                : taskJpaRepository.getReferenceById(studySession.task().id());
            StudySessionJpaEntity studySessionJpaEntity = studySessionJpaMapper.toJpaEntity(studySession, user, task);
            studySessionJpaRepository.save(studySessionJpaEntity);
            return studySessionJpaMapper.toDomain(studySessionJpaEntity);
        }

        StudySessionJpaEntity existing = studySessionJpaRepository.findById(studySession.id())
                .orElseThrow(StudySessionNotFound::new);
        existing.changeEndedAt(studySession.endedAt());
        existing.changeAppName(studySession.appName());
        studySessionJpaRepository.save(existing);
        return studySessionJpaMapper.toDomain(existing);
    }

    @Override
    public void saveAll(List<StudySession> studySessions) {
        if (studySessions == null || studySessions.isEmpty()) {
            return;
        }

        List<StudySession> newSessions = new ArrayList<>();
        List<StudySession> existingSessions = new ArrayList<>();

        for (StudySession studySession : studySessions) {
            if (studySession.id() == null) {
                newSessions.add(studySession);
            } else {
                existingSessions.add(studySession);
            }
        }

        if (!newSessions.isEmpty()) {
            List<StudySessionJpaEntity> entitiesToCreate = newSessions.stream()
                .map(session -> {
                    UserJpaEntity user = userJpaRepository.getReferenceById(session.user().id());
                    TaskJpaEntity task = session.task() == null
                        ? null
                        : taskJpaRepository.getReferenceById(session.task().id());
                    return studySessionJpaMapper.toJpaEntity(session, user, task);
                })
                .toList();
            studySessionJpaRepository.saveAll(entitiesToCreate);
        }

        if (!existingSessions.isEmpty()) {
            List<Long> ids = existingSessions.stream()
                .map(StudySession::id)
                .toList();
            Map<Long, StudySessionJpaEntity> existingEntities = studySessionJpaRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(StudySessionJpaEntity::getId, entity -> entity));

            for (StudySession session : existingSessions) {
                StudySessionJpaEntity entity = existingEntities.get(session.id());
                if (entity != null) {
                    entity.changeEndedAt(session.endedAt());
                    entity.changeAppName(session.appName());
                }
            }

            studySessionJpaRepository.saveAll(existingEntities.values());
        }
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
        return studySessionJpaRepository.findByUserIdAndEndedAtIsNull(userId)
                .map(studySessionJpaMapper::toDomain);
    }

    @Override
    public Optional<StudySession> findActiveSessionByUserIdForUpdate(Long userId) {
        return studySessionJpaRepository.findActiveByUserIdForUpdate(userId)
            .map(studySessionJpaMapper::toDomain);
    }

    @Override
    public List<StudySession> findAllByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return studySessionJpaRepository.findAllOverlappingByUserId(userId, startTime, endTime).stream()
            .map(studySessionJpaMapper::toDomain)
            .toList();
    }

    @Override
    public List<StudySession> findAllActiveSessions() {
        return studySessionJpaRepository.findAllByEndedAtIsNull().stream()
            .map(studySessionJpaMapper::toDomain)
            .toList();
    }

    @Override
    public Boolean existsActiveSessionByTaskId(Long taskId) {
        return studySessionJpaRepository.existsByTaskIdAndEndedAtIsNull(taskId);
    }

    @Override
    public Long getTotalStudyTimeInSeconds(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        LocalDateTime now = LocalDateTime.now(recordZoneId);
        return studySessionJpaRepository.getTotalStudyTimeInSeconds(userId, startOfDay, endOfDay, now);
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

        LocalDateTime now = LocalDateTime.now(recordZoneId);
        return studySessionJpaRepository
                .getTotalStudyTimeInSecondsByUserIds(userIds, startTime, endTime, now)
                .stream()
                .collect(Collectors.toMap(
                        StudySessionJpaRepository.UserStudyTimeProjection::getUserId,
                        StudySessionJpaRepository.UserStudyTimeProjection::getTotalSeconds
                ));
    }

    @Override
    public List<UserRanking> findStudyTimeRankingByUserIdAndPeriod(Long userId, LocalDateTime startDate, LocalDateTime endDate) {

        return studySessionJpaRepository.findStudyTimeRankingByUserIdAndPeriod(userId, startDate, endDate);
    }

    @Override
    public List<Object[]> findDailyStudyTimeByUserIds(List<Long> userIds, LocalDateTime startDate, LocalDateTime endDate) {

        return studySessionJpaRepository.findDailyStudyTimeByUserIds(userIds, startDate, endDate);
    }

    @Override
    public List<Object[]> findWeeklyStudyTimeByUserIds(List<Long> userIds, LocalDateTime startDate, LocalDateTime endDate) {

        return studySessionJpaRepository.findWeeklyStudyTimeByUserIds(userIds, startDate, endDate);
    }

    @Override
    public List<Object[]> findMonthlyStudyTimeByUserIds(List<Long> userIds, LocalDateTime startDate, LocalDateTime endDate) {

        return studySessionJpaRepository.findMonthlyStudyTimeByUserIds(userIds, startDate, endDate);
    }
}
