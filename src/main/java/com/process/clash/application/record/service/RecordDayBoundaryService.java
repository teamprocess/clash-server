package com.process.clash.application.record.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.user.exp.service.StudyTimeExpGrantService;
import com.process.clash.domain.record.entity.RecordSession;
import com.process.clash.infrastructure.config.RecordProperties;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RecordDayBoundaryService {

    private final RecordSessionRepositoryPort recordSessionRepositoryPort;
    private final RecordActivityNotifierPort recordActivityNotifierPort;
    private final StudyTimeExpGrantService studyTimeExpGrantService;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    public void rolloverActiveSessionsAtBoundary() {
        int boundaryHour = recordProperties.dayBoundaryHour();
        Instant now = Instant.now();

        List<RecordSession> activeSessions = recordSessionRepositoryPort.findAllActiveSessions();

        List<RecordSession> sessionsToClose = new ArrayList<>();
        List<RecordSession> sessionsToCreate = new ArrayList<>();
        Set<Long> affectedUserIds = new LinkedHashSet<>();

        // 아래 forEach는 최대 유저 수만큼 반복되며 큰 시간복잡도를 요하진 않습니다.
        activeSessions.forEach(session -> {
            Instant firstBoundary = nextBoundaryAfter(session.startedAt(), boundaryHour);
            if (!firstBoundary.isBefore(now)) {
                return;
            }

            sessionsToClose.add(session.changeEndedAt(firstBoundary));
            if (session.user() != null && session.user().id() != null) {
                affectedUserIds.add(session.user().id());
            }

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

        // 종료된 세그먼트 수집 (학습시간 EXP 지급 대상)
        List<RecordSession> closedSegments = new ArrayList<>(sessionsToClose);
        sessionsToCreate.stream()
                .filter(s -> s.endedAt() != null)
                .forEach(closedSegments::add);

        recordSessionRepositoryPort.saveAll(sessionsToClose);
        recordSessionRepositoryPort.saveAll(sessionsToCreate);

        // 종료된 세그먼트에 대해 학습시간 EXP 지급
        closedSegments.forEach(segment -> {
            try {
                studyTimeExpGrantService.grant(segment.user().id(), segment.startedAt(), segment.endedAt());
            } catch (Exception e) {
                log.error("학습시간 EXP 지급 실패 (boundary rollover). userId={}", segment.user().id(), e);
            }
        });

        for (Long userId : affectedUserIds) {
            recordActivityNotifierPort.notifySessionChanged(new Actor(userId));
        }

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
