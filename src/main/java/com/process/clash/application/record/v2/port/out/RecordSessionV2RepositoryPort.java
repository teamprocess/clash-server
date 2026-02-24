package com.process.clash.application.record.v2.port.out;

import com.process.clash.application.ranking.data.UserRanking;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RecordSessionV2RepositoryPort {

    RecordSessionV2 save(RecordSessionV2 session);

    void flush();

    Boolean existsActiveSessionByUserId(Long userId);

    Optional<RecordSessionV2> findActiveSessionByUserId(Long userId);

    Optional<RecordSessionV2> findActiveSessionByUserIdForUpdate(Long userId);

    List<RecordSessionV2> findAllByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    List<RecordSessionV2> findAllActiveSessions();

    List<Long> findAllActiveUserIds();

    Boolean existsActiveSessionBySubjectId(Long subjectId);

    Boolean existsActiveSessionByTaskId(Long taskId);

    Long getTotalStudyTimeInSeconds(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    Map<Long, Long> getTotalStudyTimeInSecondsByUserIds(List<Long> userIds, LocalDateTime startTime, LocalDateTime endTime);

    List<UserRanking> findStudyTimeRankingByUserIdAndPeriod(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Object[]> findDailyStudyTimeByUserIds(List<Long> userIds, Instant startDate, Instant endDate);

    List<Object[]> findWeeklyStudyTimeByUserIds(List<Long> userIds, Instant startDate, Instant endDate);

    List<Object[]> findMonthlyStudyTimeByUserIds(List<Long> userIds, Instant startDate, Instant endDate);
}
