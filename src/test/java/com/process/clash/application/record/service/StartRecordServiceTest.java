package com.process.clash.application.record.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.data.StartRecordData;
import com.process.clash.application.record.exception.exception.badrequest.InvalidRecordStartRequestException;
import com.process.clash.application.record.exception.exception.conflict.RecordSessionAlreadyStartedException;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.policy.TaskPolicy;
import com.process.clash.application.record.port.out.RecordSessionSegmentRepositoryPort;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.record.port.out.RecordTaskRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.entity.RecordTask;
import com.process.clash.domain.record.entity.RecordSession;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartRecordServiceTest {

    @Mock
    private RecordSessionRepositoryPort recordSessionRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private RecordTaskRepositoryPort taskRepositoryPort;

    @Mock
    private RecordActivityNotifierPort recordActivityNotifierPort;

    @Mock
    private RecordSessionSegmentRepositoryPort recordSessionSegmentRepositoryPort;

    private StartRecordService startRecordService;

    @BeforeEach
    void setUp() {
        startRecordService = new StartRecordService(
            recordSessionRepositoryPort,
            userRepositoryPort,
            taskRepositoryPort,
            new TaskPolicy(),
            new MonitoredAppPolicy(),
            recordSessionSegmentRepositoryPort,
            recordActivityNotifierPort,
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("기록 시작 시 activity started 알림을 notify한다")
    void execute_publishesActivityStarted() {
        Actor actor = new Actor(1L);
        StartRecordData.Command command = new StartRecordData.Command(RecordType.TASK, 11L, null, actor);
        User user = createUser(1L);
        RecordTask task = createTask(11L, user);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(taskRepositoryPort.findById(command.taskId())).thenReturn(Optional.of(task));
        when(recordSessionRepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(false);
        when(recordSessionRepositoryPort.save(any(RecordSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        startRecordService.execute(command);

        verify(recordSessionRepositoryPort).save(any(RecordSession.class));
        verify(recordActivityNotifierPort).notifyActivityStarted(actor);
    }

    @Test
    @DisplayName("이미 활성 세션이 있으면 publish 하지 않는다")
    void execute_skipsPublishWhenAlreadyStarted() {
        Actor actor = new Actor(1L);
        StartRecordData.Command command = new StartRecordData.Command(RecordType.TASK, 11L, null, actor);
        User user = createUser(1L);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(recordSessionRepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(true);

        assertThatThrownBy(() -> startRecordService.execute(command))
            .isInstanceOf(RecordSessionAlreadyStartedException.class);

        verify(recordActivityNotifierPort, never()).notifyActivityStarted(any());
    }

    @Test
    @DisplayName("활동 기록 시작 시 task 조회 없이 저장한다")
    void execute_activityStartWithoutTaskLookup() {
        Actor actor = new Actor(1L);
        StartRecordData.Command command = new StartRecordData.Command(RecordType.ACTIVITY, null, MonitoredApp.VSCODE, actor);
        User user = createUser(1L);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(recordSessionRepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(false);
        when(recordSessionRepositoryPort.save(any(RecordSession.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(recordSessionSegmentRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        startRecordService.execute(command);

        verify(taskRepositoryPort, never()).findById(any());
        verify(recordSessionRepositoryPort).save(any(RecordSession.class));
        verify(recordActivityNotifierPort).notifyActivityStarted(actor);
    }

    @Test
    @DisplayName("활동 기록 시작 시 appId가 없으면 예외가 발생한다")
    void execute_throwsWhenActivityAppIdIsMissing() {
        Actor actor = new Actor(1L);
        StartRecordData.Command command = new StartRecordData.Command(RecordType.ACTIVITY, null, null, actor);
        User user = createUser(1L);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(recordSessionRepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(false);

        assertThatThrownBy(() -> startRecordService.execute(command))
            .isInstanceOf(InvalidRecordStartRequestException.class);

        verify(taskRepositoryPort, never()).findById(any());
        verify(recordActivityNotifierPort, never()).notifyActivityStarted(any());
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
