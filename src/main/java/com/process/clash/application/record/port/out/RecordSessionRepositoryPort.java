package com.process.clash.application.record.port.out;

import com.process.clash.application.ranking.data.UserRanking;
import com.process.clash.domain.record.entity.RecordSession;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RecordSessionRepositoryPort {
    RecordSession save(RecordSession recordSession);
    void saveAll(List<RecordSession> recordSessions);
    Optional<RecordSession> findById(Long id);
    List<RecordSession> findAllByUserId(Long userId);
    Boolean existsActiveSessionByUserId(Long userId);
    Optional<RecordSession> findActiveSessionByUserId(Long userId);
    Optional<RecordSession> findActiveSessionByUserIdForUpdate(Long userId);
    List<RecordSession> findAllByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    List<RecordSession> findAllActiveSessions();
    Boolean existsActiveSessionByTaskId(Long taskId);
    Long getTotalStudyTimeInSeconds(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
    Map<Long, Long> getTotalStudyTimeInSecondsByUserIds(List<Long> userIds, LocalDateTime startTime, LocalDateTime endTime);
    List<UserRanking> findStudyTimeRankingByUserIdAndPeriod(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    // 실시간 학습시간 집계 (라이벌 비교용)
    List<Object[]> findDailyStudyTimeByUserIds(List<Long> userIds, Instant startDate, Instant endDate, Instant now);
    List<Object[]> findWeeklyStudyTimeByUserIds(List<Long> userIds, Instant startDate, Instant endDate, Instant now);
    List<Object[]> findMonthlyStudyTimeByUserIds(List<Long> userIds, Instant startDate, Instant endDate, Instant now);
}
