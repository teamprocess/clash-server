package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.v2.data.SwitchDevelopAppV2Data;
import com.process.clash.application.record.v2.exception.exception.badrequest.InvalidDevelopAppSwitchRequestException;
import com.process.clash.application.record.v2.exception.exception.notfound.ActiveSessionV2NotFoundException;
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

@ExtendWith(MockitoExtension.class)
class SwitchDevelopAppV2ServiceTest {

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    @Mock
    private RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;

    private SwitchDevelopAppV2Service switchDevelopAppV2Service;

    @BeforeEach
    void setUp() {
        switchDevelopAppV2Service = new SwitchDevelopAppV2Service(
            recordSessionV2RepositoryPort,
            recordDevelopSessionSegmentV2RepositoryPort,
            new MonitoredAppPolicy()
        );
    }

    @Test
    @DisplayName("활성 세션이 없으면 예외가 발생한다")
    void execute_throwsWhenNoActiveSession() {
        Actor actor = new Actor(1L);
        SwitchDevelopAppV2Data.Command command = new SwitchDevelopAppV2Data.Command(actor, MonitoredApp.VSCODE);

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> switchDevelopAppV2Service.execute(command))
            .isInstanceOf(ActiveSessionV2NotFoundException.class);
    }

    @Test
    @DisplayName("앱 ID가 없으면 예외가 발생한다")
    void execute_throwsWhenAppIdIsNull() {
        Actor actor = new Actor(1L);
        SwitchDevelopAppV2Data.Command command = new SwitchDevelopAppV2Data.Command(actor, null);

        assertThatThrownBy(() -> switchDevelopAppV2Service.execute(command))
            .isInstanceOf(InvalidDevelopAppSwitchRequestException.class);
    }

    @Test
    @DisplayName("TASK 세션에서 앱 전환 요청 시 예외가 발생한다")
    void execute_throwsWhenSessionTypeIsTask() {
        Actor actor = new Actor(1L);
        RecordSessionV2 taskSession = new RecordSessionV2(
            100L,
            1L,
            RecordSessionTypeV2.TASK,
            10L,
            "자료구조",
            null,
            null,
            null,
            Instant.now().minusSeconds(600),
            null
        );
        SwitchDevelopAppV2Data.Command command = new SwitchDevelopAppV2Data.Command(actor, MonitoredApp.VSCODE);

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.of(taskSession));

        assertThatThrownBy(() -> switchDevelopAppV2Service.execute(command))
            .isInstanceOf(InvalidDevelopAppSwitchRequestException.class);
    }

    @Test
    @DisplayName("앱 전환 시 열린 세그먼트를 닫고 새 세그먼트를 생성한다")
    void execute_switchesDevelopSegment() {
        Actor actor = new Actor(1L);
        RecordSessionV2 developSession = new RecordSessionV2(
            100L,
            1L,
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            null,
            null,
            MonitoredApp.VSCODE,
            Instant.now().minusSeconds(1800),
            null
        );
        RecordDevelopSessionSegmentV2 openSegment = new RecordDevelopSessionSegmentV2(
            200L,
            100L,
            MonitoredApp.VSCODE,
            Instant.now().minusSeconds(1800),
            null
        );
        SwitchDevelopAppV2Data.Command command = new SwitchDevelopAppV2Data.Command(actor, MonitoredApp.INTELLIJ_IDEA);

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.of(developSession));
        when(recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(100L))
            .thenReturn(Optional.of(openSegment));
        when(recordDevelopSessionSegmentV2RepositoryPort.save(any(RecordDevelopSessionSegmentV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        SwitchDevelopAppV2Data.Result result = switchDevelopAppV2Service.execute(command);

        assertThat(result.session().develop()).isNotNull();
        assertThat(result.session().develop().appId()).isEqualTo(MonitoredApp.INTELLIJ_IDEA);
        verify(recordDevelopSessionSegmentV2RepositoryPort, times(2)).save(any(RecordDevelopSessionSegmentV2.class));
        verify(recordSessionV2RepositoryPort).save(any(RecordSessionV2.class));
    }

    @Test
    @DisplayName("같은 앱으로 전환 요청하면 세그먼트를 추가하지 않는다")
    void execute_noopWhenSameApp() {
        Actor actor = new Actor(1L);
        RecordSessionV2 developSession = new RecordSessionV2(
            100L,
            1L,
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            null,
            null,
            MonitoredApp.VSCODE,
            Instant.now().minusSeconds(1800),
            null
        );
        SwitchDevelopAppV2Data.Command command = new SwitchDevelopAppV2Data.Command(actor, MonitoredApp.VSCODE);

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.of(developSession));

        SwitchDevelopAppV2Data.Result result = switchDevelopAppV2Service.execute(command);

        assertThat(result.session().develop()).isNotNull();
        assertThat(result.session().develop().appId()).isEqualTo(MonitoredApp.VSCODE);
        verify(recordDevelopSessionSegmentV2RepositoryPort, never()).save(any(RecordDevelopSessionSegmentV2.class));
        verify(recordSessionV2RepositoryPort, never()).save(any(RecordSessionV2.class));
    }
}
