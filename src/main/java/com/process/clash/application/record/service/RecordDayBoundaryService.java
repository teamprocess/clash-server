package com.process.clash.application.record.service;

import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.domain.record.entity.StudySession;
import com.process.clash.infrastructure.config.RecordProperties;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
        Instant now = Instant.now();

        List<StudySession> activeSessions = studySessionRepositoryPort.findAllActiveSessions();

        List<StudySession> sessionsToClose = new ArrayList<>();
        List<StudySession> sessionsToCreate = new ArrayList<>();

        activeSessions.forEach(session -> {
            Instant firstBoundary = nextBoundaryAfter(session.startedAt(), boundaryHour);
            if (!firstBoundary.isBefore(now)) {
                return;
            }

            sessionsToClose.add(session.changeEndedAt(firstBoundary.minusSeconds(1)));

            Instant segmentStart = firstBoundary;
            Instant nextBoundary = nextBoundaryByLocalDay(firstBoundary);
            while (nextBoundary.isBefore(now)) {
                sessionsToCreate.add(StudySession.create(
                    null,
                    session.user(),
                    session.task(),
                    session.recordType(),
                    session.appName(),
                    segmentStart,
                    nextBoundary.minusSeconds(1)
                ));
                segmentStart = nextBoundary;
                nextBoundary = nextBoundaryByLocalDay(nextBoundary);
            }

            sessionsToCreate.add(StudySession.create(
                null,
                session.user(),
                session.task(),
                session.recordType(),
                session.appName(),
                segmentStart,
                null
            ));
        });

        studySessionRepositoryPort.saveAll(sessionsToClose);
        studySessionRepositoryPort.saveAll(sessionsToCreate);
    }

    private Instant nextBoundaryAfter(Instant startedAt, int boundaryHour) {
        LocalDateTime startedAtLocal = LocalDateTime.ofInstant(startedAt, recordZoneId);
        LocalDate boundaryDate = startedAtLocal.toLocalDate();
        LocalDateTime boundary = boundaryDate.atTime(boundaryHour, 0);
        if (!startedAtLocal.isBefore(boundary)) {
            boundary = boundary.plusDays(1);
        }
        return boundary.atZone(recordZoneId).toInstant();
    }

    private Instant nextBoundaryByLocalDay(Instant boundaryInstant) {
        return LocalDateTime.ofInstant(boundaryInstant, recordZoneId)
            .plusDays(1)
            .atZone(recordZoneId)
            .toInstant();
    }
}
