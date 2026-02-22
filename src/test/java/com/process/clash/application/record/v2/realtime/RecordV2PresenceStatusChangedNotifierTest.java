package com.process.clash.application.record.v2.realtime;

import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordDevelopSessionSegmentV2;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordV2PresenceStatusChangedNotifierTest {

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    @Mock
    private RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;

    @Mock
    private RecordActivityNotifierPort recordActivityNotifierPort;

    private RecordV2PresenceStatusChangedNotifier notifier;

    @BeforeEach
    void setUp() {
        notifier = new RecordV2PresenceStatusChangedNotifier(
            recordSessionV2RepositoryPort,
            recordDevelopSessionSegmentV2RepositoryPort,
            recordActivityNotifierPort
        );
    }

    @Test
    @DisplayName("자리비움/오프라인 전환 시 DEVELOP 세션은 자동 종료한다")
    void notifyStatusChanged_stopsDevelopSession() {
        Long userId = 1L;
        Instant startedAt = Instant.now().minusSeconds(600);
        RecordSessionV2 activeSession = new RecordSessionV2(
            100L,
            userId,
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            null,
            null,
            MonitoredApp.VSCODE,
            startedAt,
            null
        );
        RecordDevelopSessionSegmentV2 openSegment = RecordDevelopSessionSegmentV2.start(
            activeSession.id(),
            MonitoredApp.VSCODE,
            startedAt
        );

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(userId))
            .thenReturn(Optional.of(activeSession));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(activeSession.id()))
            .thenReturn(Optional.of(openSegment));
        when(recordDevelopSessionSegmentV2RepositoryPort.save(any(RecordDevelopSessionSegmentV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        notifier.notifyStatusChanged(userId, UserActivityStatus.ONLINE, UserActivityStatus.AWAY);

        verify(recordSessionV2RepositoryPort).save(
            argThat(session -> session.id().equals(activeSession.id()) && session.endedAt() != null)
        );
        verify(recordDevelopSessionSegmentV2RepositoryPort).save(
            argThat(segment -> segment.sessionId().equals(activeSession.id()) && segment.endedAt() != null)
        );
        verify(recordActivityNotifierPort).notifyActivityStopped(
            argThat(actor -> actor != null && userId.equals(actor.id()))
        );
    }

    @Test
    @DisplayName("TASK 세션은 자리비움/오프라인 전환 시에도 종료하지 않는다")
    void notifyStatusChanged_doesNotStopTaskSession() {
        Long userId = 1L;
        RecordSessionV2 taskSession = new RecordSessionV2(
            200L,
            userId,
            RecordSessionTypeV2.TASK,
            10L,
            "자료구조",
            null,
            null,
            null,
            Instant.now().minusSeconds(120),
            null
        );

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(userId))
            .thenReturn(Optional.of(taskSession));

        notifier.notifyStatusChanged(userId, UserActivityStatus.ONLINE, UserActivityStatus.OFFLINE);

        verify(recordSessionV2RepositoryPort, never()).save(any(RecordSessionV2.class));
        verify(recordDevelopSessionSegmentV2RepositoryPort, never()).findOpenSegmentBySessionIdForUpdate(any());
        verify(recordActivityNotifierPort, never()).notifyActivityStopped(any());
    }

    @Test
    @DisplayName("자리비움/오프라인 전환이 아니면 세션 종료를 시도하지 않는다")
    void notifyStatusChanged_skipsWhenStatusNotAwayOrOffline() {
        notifier.notifyStatusChanged(1L, UserActivityStatus.AWAY, UserActivityStatus.ONLINE);

        verifyNoInteractions(
            recordSessionV2RepositoryPort,
            recordDevelopSessionSegmentV2RepositoryPort,
            recordActivityNotifierPort
        );
    }

    @Test
    @DisplayName("활성 세션이 없으면 종료를 시도하지 않는다")
    void notifyStatusChanged_skipsWhenNoActiveSession() {
        Long userId = 1L;
        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(userId))
            .thenReturn(Optional.empty());

        notifier.notifyStatusChanged(userId, UserActivityStatus.ONLINE, UserActivityStatus.AWAY);

        verify(recordSessionV2RepositoryPort).findActiveSessionByUserIdForUpdate(userId);
        verify(recordSessionV2RepositoryPort, never()).save(any(RecordSessionV2.class));
        verify(recordDevelopSessionSegmentV2RepositoryPort, never()).save(any(RecordDevelopSessionSegmentV2.class));
        verify(recordActivityNotifierPort, never()).notifyActivityStopped(any());
    }
}
