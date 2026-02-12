package com.process.clash.application.record.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.data.StartRecordData;
import com.process.clash.application.record.exception.exception.badrequest.InvalidMonitoredAppException;
import com.process.clash.application.record.exception.exception.conflict.StudySessionAlreadyStartedException;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.policy.TaskPolicy;
import com.process.clash.application.record.port.out.RecordActivitySegmentRepositoryPort;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.entity.Task;
import com.process.clash.domain.record.entity.StudySession;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartRecordServiceTest {

    @Mock
    private StudySessionRepositoryPort studySessionRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private RecordActivityNotifierPort recordActivityNotifierPort;

    @Mock
    private RecordActivitySegmentRepositoryPort recordActivitySegmentRepositoryPort;

    private StartRecordService startRecordService;

    @BeforeEach
    void setUp() {
        startRecordService = new StartRecordService(
            studySessionRepositoryPort,
            userRepositoryPort,
            taskRepositoryPort,
            new TaskPolicy(),
            new MonitoredAppPolicy(),
            recordActivitySegmentRepositoryPort,
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
        Task task = createTask(11L, user);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(taskRepositoryPort.findById(command.taskId())).thenReturn(Optional.of(task));
        when(studySessionRepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(false);
        when(studySessionRepositoryPort.save(any(StudySession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        startRecordService.execute(command);

        verify(studySessionRepositoryPort).save(any(StudySession.class));
        verify(recordActivityNotifierPort).notifyActivityStarted(actor);
    }

    @Test
    @DisplayName("이미 활성 세션이 있으면 publish 하지 않는다")
    void execute_skipsPublishWhenAlreadyStarted() {
        Actor actor = new Actor(1L);
        StartRecordData.Command command = new StartRecordData.Command(RecordType.TASK, 11L, null, actor);
        User user = createUser(1L);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(studySessionRepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(true);

        assertThatThrownBy(() -> startRecordService.execute(command))
            .isInstanceOf(StudySessionAlreadyStartedException.class);

        verify(recordActivityNotifierPort, never()).notifyActivityStarted(any());
    }

    @Test
    @DisplayName("활동 기록 시작 시 task 조회 없이 저장한다")
    void execute_activityStartWithoutTaskLookup() {
        Actor actor = new Actor(1L);
        StartRecordData.Command command = new StartRecordData.Command(RecordType.ACTIVITY, null, "Code", actor);
        User user = createUser(1L);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(studySessionRepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(false);
        when(studySessionRepositoryPort.save(any(StudySession.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(recordActivitySegmentRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        startRecordService.execute(command);

        verify(taskRepositoryPort, never()).findById(any());
        verify(studySessionRepositoryPort).save(any(StudySession.class));
        verify(recordActivityNotifierPort).notifyActivityStarted(actor);
    }

    @Test
    @DisplayName("활동 기록 앱이 모니터링 목록이 아니면 예외가 발생한다")
    void execute_throwsWhenActivityAppIsNotMonitored() {
        Actor actor = new Actor(1L);
        StartRecordData.Command command = new StartRecordData.Command(RecordType.ACTIVITY, null, "Slack", actor);
        User user = createUser(1L);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(studySessionRepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(false);

        assertThatThrownBy(() -> startRecordService.execute(command))
            .isInstanceOf(InvalidMonitoredAppException.class);

        verify(taskRepositoryPort, never()).findById(any());
        verify(recordActivityNotifierPort, never()).notifyActivityStarted(any());
    }

    private User createUser(Long id) {
        return new User(
            id,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "username",
            "user@example.com",
            "name",
            "encoded-password",
            Role.USER,
            "",
            0,
            0,
            0,
            Major.NONE,
            UserStatus.ACTIVE
        );
    }

    private Task createTask(Long id, User user) {
        return new Task(
            id,
            "task",
            0L,
            LocalDateTime.now(),
            LocalDateTime.now(),
            user
        );
    }
}
