package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordV2TaskSessionTimeoutServiceTest {

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    @Mock
    private RecordActivityNotifierPort recordActivityNotifierPort;

    private RecordV2TaskSessionTimeoutService recordV2TaskSessionTimeoutService;

    @BeforeEach
    void setUp() {
        recordV2TaskSessionTimeoutService = new RecordV2TaskSessionTimeoutService(
            recordSessionV2RepositoryPort,
            recordActivityNotifierPort
        );
    }

    @Test
    @DisplayName("3시간 이상 지난 TASK 세션은 자동 종료한다")
    void stopExpiredTaskSessions_stopsExpiredSessions() {
        RecordSessionV2 firstSession = new RecordSessionV2(
            100L,
            1L,
            RecordSessionTypeV2.TASK,
            10L,
            "자료구조",
            11L,
            "해시테이블",
            null,
            Instant.now().minus(Duration.ofHours(3)).minusSeconds(10),
            null
        );
        RecordSessionV2 secondSession = new RecordSessionV2(
            200L,
            2L,
            RecordSessionTypeV2.TASK,
            20L,
            "운영체제",
            null,
            null,
            null,
            Instant.now().minus(Duration.ofHours(5)),
            null
        );

        when(recordSessionV2RepositoryPort.findActiveTaskSessionsStartedBeforeForUpdate(any(Instant.class)))
            .thenReturn(List.of(firstSession, secondSession));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        int stoppedCount = recordV2TaskSessionTimeoutService.stopExpiredTaskSessions();

        assertThat(stoppedCount).isEqualTo(2);
        verify(recordSessionV2RepositoryPort).findActiveTaskSessionsStartedBeforeForUpdate(any(Instant.class));
        verify(recordSessionV2RepositoryPort, times(2)).save(
            argThat(session -> session.endedAt() != null)
        );

        ArgumentCaptor<Actor> actorCaptor = ArgumentCaptor.forClass(Actor.class);
        verify(recordActivityNotifierPort, times(2)).notifyActivityStopped(actorCaptor.capture());
        assertThat(actorCaptor.getAllValues())
            .extracting(Actor::id)
            .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    @DisplayName("만료 TASK 세션이 없으면 종료 처리를 수행하지 않는다")
    void stopExpiredTaskSessions_skipsWhenNoExpiredSessions() {
        when(recordSessionV2RepositoryPort.findActiveTaskSessionsStartedBeforeForUpdate(any(Instant.class)))
            .thenReturn(List.of());

        int stoppedCount = recordV2TaskSessionTimeoutService.stopExpiredTaskSessions();

        assertThat(stoppedCount).isZero();
        verify(recordSessionV2RepositoryPort, never()).save(any(RecordSessionV2.class));
        verify(recordActivityNotifierPort, never()).notifyActivityStopped(any(Actor.class));
    }
}
