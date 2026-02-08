package com.process.clash.application.record.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.data.StartRecordData;
import com.process.clash.application.record.exception.exception.conflict.StudySessionAlreadyStartedException;
import com.process.clash.application.record.policy.TaskPolicy;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.application.record.realtime.PublishToIncludedGroups;
import com.process.clash.application.realtime.data.ChangeType;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.entity.Task;
import com.process.clash.domain.record.entity.StudySession;
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
    private PublishToIncludedGroups publishToIncludedGroups;

    private StartRecordService startRecordService;

    @BeforeEach
    void setUp() {
        startRecordService = new StartRecordService(
            studySessionRepositoryPort,
            userRepositoryPort,
            taskRepositoryPort,
            new TaskPolicy(),
            publishToIncludedGroups,
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("기록 시작 시 포함된 그룹 멤버들에게 ACTIVITY_STARTED를 publish한다")
    void execute_publishesActivityStarted() {
        Actor actor = new Actor(1L);
        StartRecordData.Command command = new StartRecordData.Command(11L, actor);
        User user = createUser(1L);
        Task task = createTask(11L, user);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(taskRepositoryPort.findById(command.taskId())).thenReturn(Optional.of(task));
        when(studySessionRepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(false);

        startRecordService.execute(command);

        verify(studySessionRepositoryPort).save(any(StudySession.class));
        verify(publishToIncludedGroups).publish(actor, ChangeType.ACTIVITY_STARTED);
    }

    @Test
    @DisplayName("이미 활성 세션이 있으면 publish 하지 않는다")
    void execute_skipsPublishWhenAlreadyStarted() {
        Actor actor = new Actor(1L);
        StartRecordData.Command command = new StartRecordData.Command(11L, actor);
        User user = createUser(1L);
        Task task = createTask(11L, user);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(taskRepositoryPort.findById(command.taskId())).thenReturn(Optional.of(task));
        when(studySessionRepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(true);

        assertThatThrownBy(() -> startRecordService.execute(command))
            .isInstanceOf(StudySessionAlreadyStartedException.class);

        verify(publishToIncludedGroups, never()).publish(any(), any());
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

