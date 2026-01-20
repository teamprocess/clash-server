package com.process.clash.application.record.port.out;

import com.process.clash.domain.record.model.entity.StudySession;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudySessionRepositoryPort {
    void save(StudySession studySession);
    Optional<StudySession> findById(Long id);
    List<StudySession> findAllByUserId(Long userId);
    Boolean existsActiveSessionByUserId(Long userId);
    Optional<StudySession> findActiveSessionByUserId(Long userId);
    List<StudySession> findAllByUserIdAndStartedAtAfter(Long userId, LocalDate startedAt);
    Long getTotalStudyTimeInSeconds(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
    Map<Long, Long> getTotalStudyTimeInSecondsByUserIds(List<Long> userIds, LocalDateTime startTime, LocalDateTime endTime);
}
