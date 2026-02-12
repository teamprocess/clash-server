package com.process.clash.application.record.port.out;

import com.process.clash.application.ranking.data.UserRanking;
import com.process.clash.domain.record.entity.StudySession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudySessionRepositoryPort {
    StudySession save(StudySession studySession);
    void saveAll(List<StudySession> studySessions);
    Optional<StudySession> findById(Long id);
    List<StudySession> findAllByUserId(Long userId);
    Boolean existsActiveSessionByUserId(Long userId);
    Optional<StudySession> findActiveSessionByUserId(Long userId);
    Optional<StudySession> findActiveSessionByUserIdForUpdate(Long userId);
    List<StudySession> findAllByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    List<StudySession> findAllActiveSessions();
    Boolean existsActiveSessionByTaskId(Long taskId);
    Long getTotalStudyTimeInSeconds(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
    Map<Long, Long> getTotalStudyTimeInSecondsByUserIds(List<Long> userIds, LocalDateTime startTime, LocalDateTime endTime);
    List<UserRanking> findStudyTimeRankingByUserIdAndPeriod(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
