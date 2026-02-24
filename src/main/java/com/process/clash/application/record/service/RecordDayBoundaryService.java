package com.process.clash.application.record.service;

import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.domain.record.entity.RecordSession;
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

    private final RecordSessionRepositoryPort recordSessionRepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    public void rolloverActiveSessionsAtBoundary() {
        int boundaryHour = recordProperties.dayBoundaryHour();
        Instant now = Instant.now();

        List<RecordSession> activeSessions = recordSessionRepositoryPort.findAllActiveSessions();

        List<RecordSession> sessionsToClose = new ArrayList<>();
        List<RecordSession> sessionsToCreate = new ArrayList<>();

        activeSessions.forEach(session -> {
            Instant firstBoundary = nextBoundaryAfter(session.startedAt(), boundaryHour);
            if (!firstBoundary.isBefore(now)) {
                return;
            }

            sessionsToClose.add(session.changeEndedAt(firstBoundary));

            Instant segmentStart = firstBoundary;
            Instant nextBoundary = nextBoundaryByLocalDay(firstBoundary);
            while (nextBoundary.isBefore(now)) {
                sessionsToCreate.add(RecordSession.create(
                    null,
                    session.user(),
                    session.task(),
                    session.recordType(),
                    session.appId(),
                    segmentStart,
                    nextBoundary
                ));
                segmentStart = nextBoundary;
                nextBoundary = nextBoundaryByLocalDay(nextBoundary);
            }

            sessionsToCreate.add(RecordSession.create(
                null,
                session.user(),
                session.task(),
                session.recordType(),
                session.appId(),
                segmentStart,
                null
            ));
        });

        recordSessionRepositoryPort.saveAll(sessionsToClose);
        if (!sessionsToClose.isEmpty()) {
            recordSessionRepositoryPort.flush();
        }
        recordSessionRepositoryPort.saveAll(sessionsToCreate);
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
