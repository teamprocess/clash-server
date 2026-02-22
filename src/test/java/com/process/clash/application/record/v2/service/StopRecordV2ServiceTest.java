package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.data.StopRecordV2Data;
import com.process.clash.application.record.v2.exception.exception.notfound.ActiveSessionV2NotFoundException;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordDevelopSessionSegmentV2;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StopRecordV2ServiceTest {

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    @Mock
    private RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;

    @Mock
    private RecordActivityNotifierPort recordActivityNotifierPort;

    private StopRecordV2Service stopRecordV2Service;

    @BeforeEach
    void setUp() {
        stopRecordV2Service = new StopRecordV2Service(
            recordSessionV2RepositoryPort,
            recordDevelopSessionSegmentV2RepositoryPort,
            recordActivityNotifierPort,
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("DEVELOP 세션 종료 시 열린 세그먼트를 닫는다")
    void execute_closesOpenSegmentForDevelopSession() {
        Actor actor = new Actor(1L);
        StopRecordV2Data.Command command = new StopRecordV2Data.Command(actor);

        RecordSessionV2 activeSession = new RecordSessionV2(
            100L,
            1L,
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            null,
            null,
            MonitoredApp.VSCODE,
            Instant.now().minusSeconds(600),
            null
        );
        RecordDevelopSessionSegmentV2 openSegment = new RecordDevelopSessionSegmentV2(
            200L,
            100L,
            MonitoredApp.VSCODE,
            Instant.now().minusSeconds(600),
            null
        );

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.of(activeSession));
        when(recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(100L))
            .thenReturn(Optional.of(openSegment));
        when(recordDevelopSessionSegmentV2RepositoryPort.save(any(RecordDevelopSessionSegmentV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        stopRecordV2Service.execute(command);

        verify(recordDevelopSessionSegmentV2RepositoryPort).save(any(RecordDevelopSessionSegmentV2.class));
        verify(recordSessionV2RepositoryPort).save(any(RecordSessionV2.class));
        verify(recordActivityNotifierPort).notifyActivityStopped(actor);
    }

    @Test
    @DisplayName("활성 세션이 없으면 예외가 발생한다")
    void execute_throwsWhenNoActiveSession() {
        Actor actor = new Actor(1L);
        StopRecordV2Data.Command command = new StopRecordV2Data.Command(actor);

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> stopRecordV2Service.execute(command))
            .isInstanceOf(ActiveSessionV2NotFoundException.class);

        verify(recordActivityNotifierPort, never()).notifyActivityStopped(any());
    }
}
