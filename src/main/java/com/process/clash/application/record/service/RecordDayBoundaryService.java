package com.process.clash.application.record.service;

import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.domain.record.entity.StudySession;
import com.process.clash.infrastructure.config.RecordProperties;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        ZonedDateTime nowZoned = ZonedDateTime.now(recordZoneId);
        LocalDateTime now = nowZoned.toLocalDateTime();

        List<StudySession> activeSessions = studySessionRepositoryPort.findAllActiveSessions();

        List<StudySession> sessionsToClose = new ArrayList<>();
        List<StudySession> sessionsToCreate = new ArrayList<>();

        activeSessions.forEach(session -> {
            LocalDateTime firstBoundary = nextBoundaryAfter(session.startedAt(), boundaryHour);
            if (!firstBoundary.isBefore(now)) {
                return;
            }

            sessionsToClose.add(session.changeEndedAt(firstBoundary.minusSeconds(1)));

            LocalDateTime segmentStart = firstBoundary;
            LocalDateTime nextBoundary = firstBoundary.plusDays(1);
            while (nextBoundary.isBefore(now)) {
                sessionsToCreate.add(StudySession.create(
                    null,
                    session.user(),
                    session.task(),
                    segmentStart,
                    nextBoundary.minusSeconds(1)
                ));
                segmentStart = nextBoundary;
                nextBoundary = nextBoundary.plusDays(1);
            }

            sessionsToCreate.add(StudySession.create(
                null,
                session.user(),
                session.task(),
                segmentStart,
                null
            ));
        });

        studySessionRepositoryPort.saveAll(sessionsToClose);
        studySessionRepositoryPort.saveAll(sessionsToCreate);
    }

    private LocalDateTime nextBoundaryAfter(LocalDateTime startedAt, int boundaryHour) {
        LocalDate boundaryDate = startedAt.toLocalDate();
        LocalDateTime boundary = boundaryDate.atTime(boundaryHour, 0);
        if (!startedAt.isBefore(boundary)) {
            boundary = boundary.plusDays(1);
        }
        return boundary;
    }
}
