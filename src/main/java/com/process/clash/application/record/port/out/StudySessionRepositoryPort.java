package com.process.clash.application.record.port.out;

import com.process.clash.domain.record.model.entity.StudySession;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudySessionRepositoryPort {
    void save(StudySession studySession);
    Optional<StudySession> findById(Long id);
    List<StudySession> findAllByUserId(Long userId);
    Boolean existsActiveSessionByUserId(Long userId);
    Optional<StudySession> findActiveSessionByUserId(Long userId);
    List<StudySession> findAllByUserIdAndStartedAtAfter(Long userId, LocalDate startedAt);
}
