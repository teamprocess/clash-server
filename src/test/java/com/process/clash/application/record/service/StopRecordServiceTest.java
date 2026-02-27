package com.process.clash.application.record.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.data.StopRecordData;
import com.process.clash.application.record.exception.exception.notfound.ActiveSessionNotFound;
import com.process.clash.application.record.port.out.RecordSessionSegmentRepositoryPort;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.entity.RecordSession;
import com.process.clash.domain.record.entity.RecordTask;
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
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StopRecordServiceTest {

    @Mock
    private RecordSessionRepositoryPort recordSessionRepositoryPort;

    @Mock
    private RecordActivityNotifierPort recordActivityNotifierPort;

    @Mock
    private RecordSessionSegmentRepositoryPort recordSessionSegmentRepositoryPort;

    private StopRecordService stopRecordService;

    @BeforeEach
    void setUp() {
        stopRecordService = new StopRecordService(
            recordSessionRepositoryPort,
            recordSessionSegmentRepositoryPort,
            recordActivityNotifierPort,
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("기록 종료 시 activity stopped 알림을 notify한다")
    void execute_publishesActivityStopped() {
        Actor actor = new Actor(1L);
        StopRecordData.Command command = new StopRecordData.Command(actor);
        User user = createUser(1L);
        RecordTask task = createTask(11L, user);
        RecordSession activeSession = RecordSession.create(
            100L,
            user,
            task,
            Instant.now().minusSeconds(600),
            null
        );

        when(recordSessionRepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.of(activeSession));
        when(recordSessionRepositoryPort.save(any(RecordSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        stopRecordService.execute(command);

        verify(recordSessionRepositoryPort).save(any(RecordSession.class));
        verify(recordActivityNotifierPort).notifyActivityStopped(actor);
    }

    @Test
    @DisplayName("활성 세션이 없으면 publish 하지 않는다")
    void execute_skipsPublishWhenNoActiveSession() {
        Actor actor = new Actor(1L);
        StopRecordData.Command command = new StopRecordData.Command(actor);

        when(recordSessionRepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> stopRecordService.execute(command))
            .isInstanceOf(ActiveSessionNotFound.class);

        verify(recordActivityNotifierPort, never()).notifyActivityStopped(any());
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
            UserStatus.ACTIVE
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
