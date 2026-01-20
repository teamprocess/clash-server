package com.process.clash.application.record.service;

import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.domain.record.model.entity.StudySession;
import com.process.clash.infrastructure.config.RecordProperties;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordDayBoundaryService {

    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    public void rolloverActiveSessionsAtBoundary() {
        int boundaryHour = recordProperties.dayBoundaryHour();
        ZonedDateTime nowZoned = ZonedDateTime.now(recordZoneId);
        LocalDate boundaryDate = nowZoned.toLocalDate();
        LocalDateTime boundaryStart = boundaryDate.atTime(boundaryHour, 0);
        LocalDateTime boundaryEndOfPrevious = boundaryStart.minusSeconds(1);

        List<StudySession> activeSessions = studySessionRepositoryPort.findAllActiveSessions();

        activeSessions.stream()
            .filter(session -> session.startedAt().isBefore(boundaryStart))
            .forEach(session -> {
                StudySession closedSession = session.changeEndedAt(boundaryEndOfPrevious);
                studySessionRepositoryPort.save(closedSession);

                StudySession newSession = StudySession.create(
                    null,
                    session.user(),
                    session.task(),
                    boundaryStart,
                    null
                );
                studySessionRepositoryPort.save(newSession);
            });
    }
}
