package com.process.clash.application.record.v2.service;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

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
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class RecordDayBoundaryV2ServiceTest {

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    @Mock
    private RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;

    @Test
    @DisplayName("경계 롤오버 시 기존 세션 종료 저장 후 flush를 호출한다")
    void rollover_flushesAfterClosingSession() {
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
            RecordSessionTypeV2.TASK,
            10L,
            "subject",
            20L,
            "task",
            null,
            boundary.minusSeconds(60),
            null
        );

        when(recordSessionV2RepositoryPort.findAllActiveSessions()).thenReturn(List.of(activeSession));
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

        service.rolloverActiveSessionsAtBoundary();

        InOrder inOrder = inOrder(recordSessionV2RepositoryPort);
        inOrder.verify(recordSessionV2RepositoryPort).findAllActiveSessions();
        inOrder.verify(recordSessionV2RepositoryPort).save(argThat(session ->
            Long.valueOf(100L).equals(session.id()) && session.endedAt() != null
        ));
        inOrder.verify(recordSessionV2RepositoryPort).flush();
        inOrder.verify(recordSessionV2RepositoryPort).save(argThat(session ->
            session.id() == null
                && boundary.equals(session.startedAt())
                && session.endedAt() == null
        ));
    }

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

    @Test
    @DisplayName("다중 경계 캐치업에서는 최신 경계 이후 세그먼트만 열린 세션으로 이관한다")
    void rollover_transfersOnlySegmentsStartedAfterLatestBoundary() {
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
        LocalDateTime latestBoundaryLocal = nowLocal.withHour(boundaryHour).withMinute(0).withSecond(0).withNano(0);
        if (!latestBoundaryLocal.isBefore(nowLocal)) {
            latestBoundaryLocal = latestBoundaryLocal.minusDays(1);
        }
        Instant latestBoundary = latestBoundaryLocal.atZone(zoneId).toInstant();
        Instant firstBoundary = latestBoundary.minusSeconds(60L * 60L * 24L * 2L);

        RecordSessionV2 activeSession = new RecordSessionV2(
            100L,
            1L,
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            null,
            null,
            MonitoredApp.VSCODE,
            firstBoundary.minusSeconds(60),
            null
        );
        RecordDevelopSessionSegmentV2 midRangeOpenSegment = new RecordDevelopSessionSegmentV2(
            200L,
            100L,
            MonitoredApp.VSCODE,
            firstBoundary.plusSeconds(60L * 60L * 12L),
            null
        );

        when(recordSessionV2RepositoryPort.findAllActiveSessions()).thenReturn(List.of(activeSession));
        when(recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(100L))
            .thenReturn(Optional.of(midRangeOpenSegment));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(new Answer<RecordSessionV2>() {
                private long nextId = 101L;

                @Override
                public RecordSessionV2 answer(InvocationOnMock invocation) {
                    RecordSessionV2 session = invocation.getArgument(0);
                    if (session.id() != null) {
                        return session;
                    }
                    return new RecordSessionV2(
                        nextId++,
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
                }
            });
        when(recordDevelopSessionSegmentV2RepositoryPort.save(any(RecordDevelopSessionSegmentV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        service.rolloverActiveSessionsAtBoundary();

        verify(recordDevelopSessionSegmentV2RepositoryPort, never()).save(argThat(segment ->
            Long.valueOf(200L).equals(segment.id())
                && Long.valueOf(103L).equals(segment.sessionId())
        ));
        verify(recordDevelopSessionSegmentV2RepositoryPort).save(argThat(segment ->
            segment.id() == null
                && Long.valueOf(103L).equals(segment.sessionId())
                && latestBoundary.equals(segment.startedAt())
                && segment.endedAt() == null
        ));
    }

    @Test
    @DisplayName("종료 세그먼트 생성 시 INSERT 1회로 저장한다")
    void rollover_savesClosedDevelopSegmentOnce() {
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
            boundary.minusSeconds(60L * 60L * 24L + 60L),
            null
        );

        when(recordSessionV2RepositoryPort.findAllActiveSessions()).thenReturn(List.of(activeSession));
        when(recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(100L))
            .thenReturn(Optional.empty());
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(new Answer<RecordSessionV2>() {
                private long nextId = 101L;

                @Override
                public RecordSessionV2 answer(InvocationOnMock invocation) {
                    RecordSessionV2 session = invocation.getArgument(0);
                    if (session.id() != null) {
                        return session;
                    }
                    return new RecordSessionV2(
                        nextId++,
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
                }
            });
        when(recordDevelopSessionSegmentV2RepositoryPort.save(any(RecordDevelopSessionSegmentV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        service.rolloverActiveSessionsAtBoundary();

        ArgumentCaptor<RecordDevelopSessionSegmentV2> segmentCaptor =
            ArgumentCaptor.forClass(RecordDevelopSessionSegmentV2.class);
        verify(recordDevelopSessionSegmentV2RepositoryPort, times(2)).save(segmentCaptor.capture());

        List<RecordDevelopSessionSegmentV2> savedSegments = segmentCaptor.getAllValues();
        long closedSegmentCount = savedSegments.stream().filter(segment -> segment.endedAt() != null).count();
        long openSegmentCount = savedSegments.stream().filter(segment -> segment.endedAt() == null).count();

        assertThat(closedSegmentCount).isEqualTo(1);
        assertThat(openSegmentCount).isEqualTo(1);
    }
}
