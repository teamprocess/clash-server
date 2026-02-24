package com.process.clash.application.record.v2.service;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordDevelopSessionSegmentV2;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordDayBoundaryV2ServiceTest {

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    @Mock
    private RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;

    @Test
    @DisplayName("경계 이후에 시작한 열린 개발 세그먼트는 새 세션으로 이관한다")
    void rollover_transfersOpenSegmentStartedAfterBoundary() {
        ZoneId zoneId = ZoneId.of("UTC");
        Instant now = Instant.now();
        int boundaryHour = LocalDateTime.ofInstant(now, zoneId).minusHours(1).getHour();

        RecordDayBoundaryV2Service service = new RecordDayBoundaryV2Service(
            recordSessionV2RepositoryPort,
            recordDevelopSessionSegmentV2RepositoryPort,
            new RecordProperties("UTC", boundaryHour),
            zoneId
        );

        LocalDateTime nowLocal = LocalDateTime.ofInstant(now, zoneId);
        LocalDateTime boundaryLocal = nowLocal.withHour(boundaryHour).withMinute(0).withSecond(0).withNano(0);
        if (!boundaryLocal.isBefore(nowLocal)) {
            boundaryLocal = boundaryLocal.minusDays(1);
        }
        Instant boundary = boundaryLocal.atZone(zoneId).toInstant();

        RecordSessionV2 activeSession = new RecordSessionV2(
            100L,
            1L,
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            null,
            null,
            MonitoredApp.VSCODE,
            boundary.minusSeconds(60),
            null
        );
        RecordDevelopSessionSegmentV2 postBoundaryOpenSegment = new RecordDevelopSessionSegmentV2(
            200L,
            100L,
            MonitoredApp.VSCODE,
            boundary.plusSeconds(60),
            null
        );

        when(recordSessionV2RepositoryPort.findAllActiveSessions()).thenReturn(List.of(activeSession));
        when(recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(100L))
            .thenReturn(Optional.of(postBoundaryOpenSegment));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> {
                RecordSessionV2 session = invocation.getArgument(0);
                if (session.id() != null) {
                    return session;
                }
                return new RecordSessionV2(
                    101L,
                    session.userId(),
                    session.sessionType(),
                    session.subjectId(),
                    session.subjectName(),
                    session.taskId(),
                    session.taskName(),
                    session.appId(),
                    session.startedAt(),
                    session.endedAt()
                );
            });
        when(recordDevelopSessionSegmentV2RepositoryPort.save(any(RecordDevelopSessionSegmentV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        service.rolloverActiveSessionsAtBoundary();

        verify(recordDevelopSessionSegmentV2RepositoryPort).save(argThat(segment ->
            Long.valueOf(200L).equals(segment.id())
                && Long.valueOf(101L).equals(segment.sessionId())
                && segment.endedAt() == null
        ));
        verify(recordDevelopSessionSegmentV2RepositoryPort, never()).save(argThat(segment ->
            segment.id() == null
                && Long.valueOf(101L).equals(segment.sessionId())
                && boundary.equals(segment.startedAt())
        ));
    }

    @Test
    @DisplayName("경계 이전에 시작한 열린 개발 세그먼트는 경계 시각으로 종료한다")
    void rollover_closesOpenSegmentStartedBeforeBoundary() {
        ZoneId zoneId = ZoneId.of("UTC");
        Instant now = Instant.now();
        int boundaryHour = LocalDateTime.ofInstant(now, zoneId).minusHours(1).getHour();

        RecordDayBoundaryV2Service service = new RecordDayBoundaryV2Service(
            recordSessionV2RepositoryPort,
            recordDevelopSessionSegmentV2RepositoryPort,
            new RecordProperties("UTC", boundaryHour),
            zoneId
        );

        LocalDateTime nowLocal = LocalDateTime.ofInstant(now, zoneId);
        LocalDateTime boundaryLocal = nowLocal.withHour(boundaryHour).withMinute(0).withSecond(0).withNano(0);
        if (!boundaryLocal.isBefore(nowLocal)) {
            boundaryLocal = boundaryLocal.minusDays(1);
        }
        Instant boundary = boundaryLocal.atZone(zoneId).toInstant();

        RecordSessionV2 activeSession = new RecordSessionV2(
            100L,
            1L,
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            null,
            null,
            MonitoredApp.VSCODE,
            boundary.minusSeconds(120),
            null
        );
        RecordDevelopSessionSegmentV2 preBoundaryOpenSegment = new RecordDevelopSessionSegmentV2(
            200L,
            100L,
            MonitoredApp.VSCODE,
            boundary.minusSeconds(60),
            null
        );

        when(recordSessionV2RepositoryPort.findAllActiveSessions()).thenReturn(List.of(activeSession));
        when(recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(100L))
            .thenReturn(Optional.of(preBoundaryOpenSegment));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> {
                RecordSessionV2 session = invocation.getArgument(0);
                if (session.id() != null) {
                    return session;
                }
                return new RecordSessionV2(
                    101L,
                    session.userId(),
                    session.sessionType(),
                    session.subjectId(),
                    session.subjectName(),
                    session.taskId(),
                    session.taskName(),
                    session.appId(),
                    session.startedAt(),
                    session.endedAt()
                );
            });
        when(recordDevelopSessionSegmentV2RepositoryPort.save(any(RecordDevelopSessionSegmentV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        service.rolloverActiveSessionsAtBoundary();

        verify(recordDevelopSessionSegmentV2RepositoryPort).save(argThat(segment ->
            Long.valueOf(200L).equals(segment.id()) && boundary.equals(segment.endedAt())
        ));
    }
}
