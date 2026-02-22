package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.policy.MonitoredAppPolicy;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.data.StartRecordV2Data;
import com.process.clash.application.record.v2.exception.exception.badrequest.InvalidRecordV2StartRequestException;
import com.process.clash.application.record.v2.exception.exception.conflict.RecordSessionV2AlreadyStartedException;
import com.process.clash.application.record.v2.policy.SubjectV2Policy;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
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
class StartRecordV2ServiceTest {

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    @Mock
    private RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;

    @Mock
    private RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;

    @Mock
    private RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private RecordActivityNotifierPort recordActivityNotifierPort;

    private StartRecordV2Service startRecordV2Service;

    @BeforeEach
    void setUp() {
        startRecordV2Service = new StartRecordV2Service(
            recordSessionV2RepositoryPort,
            recordSubjectV2RepositoryPort,
            recordTaskV2RepositoryPort,
            recordDevelopSessionSegmentV2RepositoryPort,
            userRepositoryPort,
            new SubjectV2Policy(),
            new MonitoredAppPolicy(),
            recordActivityNotifierPort,
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("TASK 세션 시작 시 subject/task를 검증하고 저장한다")
    void execute_startsTaskSession() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        RecordSubjectV2 subject = new RecordSubjectV2(10L, 1L, "자료구조", 0L, Instant.now(), Instant.now());
        RecordTaskV2 task = new RecordTaskV2(11L, 10L, "해시테이블", 0L, Instant.now(), Instant.now());
        StartRecordV2Data.Command command = new StartRecordV2Data.Command(
            RecordSessionTypeV2.TASK,
            10L,
            11L,
            null,
            actor
        );

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(recordSessionV2RepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(false);
        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.of(subject));
        when(recordTaskV2RepositoryPort.findByIdAndSubjectId(11L, 10L)).thenReturn(Optional.of(task));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> withId(invocation.getArgument(0), 100L));

        startRecordV2Service.execute(command);

        verify(recordSessionV2RepositoryPort).save(any(RecordSessionV2.class));
        verify(recordDevelopSessionSegmentV2RepositoryPort, never()).save(any());
        verify(recordActivityNotifierPort).notifyActivityStarted(actor);
    }

    @Test
    @DisplayName("DEVELOP 세션 시작 시 세그먼트를 생성한다")
    void execute_startsDevelopSessionAndCreatesSegment() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        StartRecordV2Data.Command command = new StartRecordV2Data.Command(
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            MonitoredApp.VSCODE,
            actor
        );

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(recordSessionV2RepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(false);
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> withId(invocation.getArgument(0), 200L));
        when(recordDevelopSessionSegmentV2RepositoryPort.save(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        startRecordV2Service.execute(command);

        verify(recordTaskV2RepositoryPort, never()).findByIdAndSubjectId(any(), any());
        verify(recordDevelopSessionSegmentV2RepositoryPort).save(any());
        verify(recordActivityNotifierPort).notifyActivityStarted(actor);
    }

    @Test
    @DisplayName("이미 활성 세션이 있으면 예외가 발생한다")
    void execute_throwsWhenAlreadyStarted() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        StartRecordV2Data.Command command = new StartRecordV2Data.Command(
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            MonitoredApp.VSCODE,
            actor
        );

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(recordSessionV2RepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(true);

        assertThatThrownBy(() -> startRecordV2Service.execute(command))
            .isInstanceOf(RecordSessionV2AlreadyStartedException.class);

        verify(recordActivityNotifierPort, never()).notifyActivityStarted(any());
    }

    @Test
    @DisplayName("잘못된 TASK 시작 요청은 예외가 발생한다")
    void execute_throwsWhenTaskRequestIsInvalid() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        StartRecordV2Data.Command command = new StartRecordV2Data.Command(
            RecordSessionTypeV2.TASK,
            null,
            11L,
            null,
            actor
        );

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(recordSessionV2RepositoryPort.existsActiveSessionByUserId(actor.id())).thenReturn(false);

        assertThatThrownBy(() -> startRecordV2Service.execute(command))
            .isInstanceOf(InvalidRecordV2StartRequestException.class);
    }

    private RecordSessionV2 withId(RecordSessionV2 session, Long id) {
        return new RecordSessionV2(
            id,
            session.userId(),
            session.sessionType(),
            session.subjectId(),
            session.subjectName(),
            session.taskId(),
            session.taskName(),
            session.appId(),
            session.startedAt(),
            session.endedAt()
        );
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
            0,
            Major.NONE,
            UserStatus.ACTIVE
        );
    }
}
