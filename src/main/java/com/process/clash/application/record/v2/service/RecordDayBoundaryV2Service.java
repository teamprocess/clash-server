package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordDevelopSessionSegmentV2;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import com.process.clash.infrastructure.config.RecordProperties;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordDayBoundaryV2Service {

    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    public void rolloverActiveSessionsAtBoundary() {
        int boundaryHour = recordProperties.dayBoundaryHour();
        Instant now = Instant.now();

        List<RecordSessionV2> activeSessions = recordSessionV2RepositoryPort.findAllActiveSessions();
        activeSessions.forEach(session -> rolloverIfCrossedBoundary(session, boundaryHour, now));
    }

    private void rolloverIfCrossedBoundary(
        RecordSessionV2 session,
        int boundaryHour,
        Instant now
    ) {
        Instant firstBoundary = nextBoundaryAfter(session.startedAt(), boundaryHour);
        if (!firstBoundary.isBefore(now)) {
            return;
        }

        Optional<RecordDevelopSessionSegmentV2> openDevelopSegment = findOpenDevelopSegmentForUpdateIfNeeded(session);
        recordSessionV2RepositoryPort.save(session.changeEndedAt(firstBoundary));
        closeDevelopSegmentAtBoundaryIfNeeded(openDevelopSegment, firstBoundary);

        Instant segmentStart = firstBoundary;
        Instant nextBoundary = nextBoundaryByLocalDay(firstBoundary);
        while (nextBoundary.isBefore(now)) {
            RecordSessionV2 closedSegmentSession = createContinuationSession(session, segmentStart, nextBoundary);
            RecordSessionV2 savedClosedSegmentSession = recordSessionV2RepositoryPort.save(closedSegmentSession);
            createDevelopSegmentIfNeeded(savedClosedSegmentSession, segmentStart, nextBoundary);
            segmentStart = nextBoundary;
            nextBoundary = nextBoundaryByLocalDay(nextBoundary);
        }

        RecordSessionV2 openSession = createContinuationSession(session, segmentStart, null);
        RecordSessionV2 savedOpenSession = recordSessionV2RepositoryPort.save(openSession);
        createOpenDevelopSegmentIfNeeded(savedOpenSession, segmentStart, firstBoundary, openDevelopSegment);
    }

    private RecordSessionV2 createContinuationSession(
        RecordSessionV2 source,
        Instant startedAt,
        Instant endedAt
    ) {
        RecordSessionV2 continuation = source.sessionType() == RecordSessionTypeV2.DEVELOP
            ? RecordSessionV2.createDevelop(source.userId(), source.appId(), startedAt)
            : RecordSessionV2.createTask(
                source.userId(),
                source.subjectId(),
                source.subjectName(),
                source.taskId(),
                source.taskName(),
                startedAt
            );

        if (endedAt == null) {
            return continuation;
        }
        return continuation.changeEndedAt(endedAt);
    }

    private Optional<RecordDevelopSessionSegmentV2> findOpenDevelopSegmentForUpdateIfNeeded(RecordSessionV2 session) {
        if (session.sessionType() != RecordSessionTypeV2.DEVELOP) {
            return Optional.empty();
        }
        return recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(session.id());
    }

    private void closeDevelopSegmentAtBoundaryIfNeeded(
        Optional<RecordDevelopSessionSegmentV2> openSegment,
        Instant boundary
    ) {
        openSegment
            .filter(segment -> segment.startedAt().isBefore(boundary))
            .ifPresent(segment -> recordDevelopSessionSegmentV2RepositoryPort.save(segment.changeEndedAt(boundary)));
    }

    private void createDevelopSegmentIfNeeded(
        RecordSessionV2 session,
        Instant startedAt,
        Instant endedAt
    ) {
        if (session.sessionType() != RecordSessionTypeV2.DEVELOP || session.appId() == null) {
            return;
        }

        RecordDevelopSessionSegmentV2 savedSegment = recordDevelopSessionSegmentV2RepositoryPort.save(
            RecordDevelopSessionSegmentV2.start(session.id(), session.appId(), startedAt)
        );

        if (endedAt != null) {
            recordDevelopSessionSegmentV2RepositoryPort.save(savedSegment.changeEndedAt(endedAt));
        }
    }

    private void createOpenDevelopSegmentIfNeeded(
        RecordSessionV2 openSession,
        Instant startedAt,
        Instant boundary,
        Optional<RecordDevelopSessionSegmentV2> previousOpenSegment
    ) {
        Optional<RecordDevelopSessionSegmentV2> transferCandidate = previousOpenSegment
            .filter(segment -> !segment.startedAt().isBefore(boundary));

        if (transferCandidate.isPresent()) {
            transferOpenDevelopSegment(transferCandidate.get(), openSession.id());
            return;
        }

        createDevelopSegmentIfNeeded(openSession, startedAt, null);
    }

    private void transferOpenDevelopSegment(
        RecordDevelopSessionSegmentV2 segment,
        Long nextSessionId
    ) {
        recordDevelopSessionSegmentV2RepositoryPort.save(
            new RecordDevelopSessionSegmentV2(
                segment.id(),
                nextSessionId,
                segment.appId(),
                segment.startedAt(),
                segment.endedAt()
            )
        );
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
