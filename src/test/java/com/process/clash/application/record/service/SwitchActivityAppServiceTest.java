package com.process.clash.application.record.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.data.SwitchActivityAppData;
import com.process.clash.application.record.exception.exception.badrequest.InvalidActivitySwitchRequestException;
import com.process.clash.application.record.exception.exception.notfound.ActiveSessionNotFound;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.RecordSessionSegmentRepositoryPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.entity.RecordSessionSegment;
import com.process.clash.domain.record.entity.RecordSession;
import com.process.clash.domain.record.entity.RecordTask;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
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
class SwitchActivityAppServiceTest {

    @Mock
    private RecordSessionRepositoryPort recordSessionRepositoryPort;

    @Mock
    private RecordSessionSegmentRepositoryPort recordSessionSegmentRepositoryPort;

    @Mock
    private RecordActivityNotifierPort recordActivityNotifierPort;

    private SwitchActivityAppService switchActivityAppService;

    @BeforeEach
    void setUp() {
        switchActivityAppService = new SwitchActivityAppService(
            recordSessionRepositoryPort,
            recordSessionSegmentRepositoryPort,
            recordActivityNotifierPort,
            new MonitoredAppPolicy(),
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("활성 세션이 없으면 예외가 발생한다")
    void execute_throwsWhenNoActiveSession() {
        Actor actor = new Actor(1L);
        SwitchActivityAppData.Command command = new SwitchActivityAppData.Command(actor, MonitoredApp.VSCODE);
        when(recordSessionRepositoryPort.findActiveSessionByUserIdForUpdate(actor.id())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> switchActivityAppService.execute(command))
            .isInstanceOf(ActiveSessionNotFound.class);
    }

    @Test
    @DisplayName("TASK 세션에서 활동 앱 전환 요청 시 예외가 발생한다")
    void execute_throwsWhenActiveSessionIsTask() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        RecordTask task = createTask(11L, user);
        RecordSession taskSession = RecordSession.create(
            100L,
            user,
            task,
            Instant.now().minusSeconds(600),
            null
        );
        SwitchActivityAppData.Command command = new SwitchActivityAppData.Command(actor, MonitoredApp.VSCODE);

        when(recordSessionRepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.of(taskSession));

        assertThatThrownBy(() -> switchActivityAppService.execute(command))
            .isInstanceOf(InvalidActivitySwitchRequestException.class);
    }

    @Test
    @DisplayName("활동 앱 전환 시 열린 segment를 닫고 새 segment를 생성한다")
    void execute_switchesActivitySegment() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        RecordSession activitySession = RecordSession.createActivity(
            100L,
            user,
            MonitoredApp.VSCODE,
            Instant.now().minusSeconds(1_800),
            null
        );
        RecordSessionSegment openSegment = new RecordSessionSegment(
            200L,
            100L,
            MonitoredApp.VSCODE,
            Instant.now().minusSeconds(1_800),
            null
        );
        SwitchActivityAppData.Command command = new SwitchActivityAppData.Command(actor, MonitoredApp.INTELLIJ_IDEA);

        when(recordSessionRepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.of(activitySession));
        when(recordSessionSegmentRepositoryPort.findOpenSegmentBySessionIdForUpdate(activitySession.id()))
            .thenReturn(Optional.of(openSegment));
        when(recordSessionSegmentRepositoryPort.save(any(RecordSessionSegment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(recordSessionRepositoryPort.save(any(RecordSession.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        SwitchActivityAppData.Result result = switchActivityAppService.execute(command);

        assertThat(result.session().activity()).isNotNull();
        assertThat(result.session().activity().appId()).isEqualTo(MonitoredApp.INTELLIJ_IDEA);
        verify(recordSessionSegmentRepositoryPort, times(2)).save(any(RecordSessionSegment.class));
        verify(recordSessionRepositoryPort).save(any(RecordSession.class));
        verify(recordActivityNotifierPort).notifySessionChanged(actor);
    }

    @Test
    @DisplayName("같은 앱으로 전환 요청하면 segment를 추가하지 않는다")
    void execute_noopWhenSameApp() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        RecordSession activitySession = RecordSession.createActivity(
            100L,
            user,
            MonitoredApp.VSCODE,
            Instant.now().minusSeconds(1_800),
            null
        );
        SwitchActivityAppData.Command command = new SwitchActivityAppData.Command(actor, MonitoredApp.VSCODE);

        when(recordSessionRepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.of(activitySession));

        SwitchActivityAppData.Result result = switchActivityAppService.execute(command);

        assertThat(result.session().activity()).isNotNull();
        assertThat(result.session().activity().appId()).isEqualTo(MonitoredApp.VSCODE);
        verify(recordSessionSegmentRepositoryPort, never()).save(any(RecordSessionSegment.class));
        verify(recordSessionRepositoryPort, never()).save(any(RecordSession.class));
        verify(recordActivityNotifierPort, never()).notifySessionChanged(any());
    }

    private User createUser(Long id) {
        return new User(
            id,
            Instant.now(),
            Instant.now(),
            "username",
            "user@example.com",
            "name",
            "encoded-password",
            Role.USER,
            "",
            0,
            0,
            Major.NONE,
            UserStatus.ACTIVE,
            null
        );
    }

    private RecordTask createTask(Long id, User user) {
        return new RecordTask(
            id,
            "task",
            0L,
            Instant.now(),
            Instant.now(),
            user
        );
    }
}
