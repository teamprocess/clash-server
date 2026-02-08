package com.process.clash.application.record.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.data.StopRecordData;
import com.process.clash.application.record.exception.exception.notfound.ActiveSessionNotFound;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.record.realtime.PublishToIncludedGroups;
import com.process.clash.application.realtime.data.ChangeType;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.entity.StudySession;
import com.process.clash.domain.record.entity.Task;
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
class StopRecordServiceTest {

    @Mock
    private StudySessionRepositoryPort studySessionRepositoryPort;

    @Mock
    private PublishToIncludedGroups publishToIncludedGroups;

    private StopRecordService stopRecordService;

    @BeforeEach
    void setUp() {
        stopRecordService = new StopRecordService(
            studySessionRepositoryPort,
            publishToIncludedGroups,
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("기록 종료 시 포함된 그룹 멤버들에게 ACTIVITY_STOPPED를 publish한다")
    void execute_publishesActivityStopped() {
        Actor actor = new Actor(1L);
        StopRecordData.Command command = new StopRecordData.Command(actor);
        User user = createUser(1L);
        Task task = createTask(11L, user);
        StudySession activeSession = StudySession.create(
            100L,
            user,
            task,
            LocalDateTime.now().minusMinutes(10),
            null
        );

        when(studySessionRepositoryPort.findActiveSessionByUserId(actor.id()))
            .thenReturn(Optional.of(activeSession));

        stopRecordService.execute(command);

        verify(studySessionRepositoryPort).save(any(StudySession.class));
        verify(publishToIncludedGroups).publish(actor, ChangeType.ACTIVITY_STOPPED);
    }

    @Test
    @DisplayName("활성 세션이 없으면 publish 하지 않는다")
    void execute_skipsPublishWhenNoActiveSession() {
        Actor actor = new Actor(1L);
        StopRecordData.Command command = new StopRecordData.Command(actor);

        when(studySessionRepositoryPort.findActiveSessionByUserId(actor.id()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> stopRecordService.execute(command))
            .isInstanceOf(ActiveSessionNotFound.class);

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

