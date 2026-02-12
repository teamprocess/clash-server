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
import com.process.clash.application.record.port.out.RecordActivitySegmentRepositoryPort;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.entity.RecordActivitySegment;
import com.process.clash.domain.record.entity.StudySession;
import com.process.clash.domain.record.entity.Task;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import java.time.LocalDateTime;
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
    private StudySessionRepositoryPort studySessionRepositoryPort;

    @Mock
    private RecordActivitySegmentRepositoryPort recordActivitySegmentRepositoryPort;

    private SwitchActivityAppService switchActivityAppService;

    @BeforeEach
    void setUp() {
        switchActivityAppService = new SwitchActivityAppService(
            studySessionRepositoryPort,
            recordActivitySegmentRepositoryPort,
            new MonitoredAppPolicy(),
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("활성 세션이 없으면 예외가 발생한다")
    void execute_throwsWhenNoActiveSession() {
        Actor actor = new Actor(1L);
        SwitchActivityAppData.Command command = new SwitchActivityAppData.Command(actor, "Code");
        when(studySessionRepositoryPort.findActiveSessionByUserIdForUpdate(actor.id())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> switchActivityAppService.execute(command))
            .isInstanceOf(ActiveSessionNotFound.class);
    }

    @Test
    @DisplayName("TASK 세션에서 활동 앱 전환 요청 시 예외가 발생한다")
    void execute_throwsWhenActiveSessionIsTask() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        Task task = createTask(11L, user);
        StudySession taskSession = StudySession.create(
            100L,
            user,
            task,
            LocalDateTime.now().minusMinutes(10),
            null
        );
        SwitchActivityAppData.Command command = new SwitchActivityAppData.Command(actor, "Code");

        when(studySessionRepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.of(taskSession));

        assertThatThrownBy(() -> switchActivityAppService.execute(command))
            .isInstanceOf(InvalidActivitySwitchRequestException.class);
    }

    @Test
    @DisplayName("활동 앱 전환 시 열린 segment를 닫고 새 segment를 생성한다")
    void execute_switchesActivitySegment() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        StudySession activitySession = StudySession.createActivity(
            100L,
            user,
            "Code",
            LocalDateTime.now().minusMinutes(30),
            null
        );
        RecordActivitySegment openSegment = new RecordActivitySegment(
            200L,
            100L,
            "Code",
            LocalDateTime.now().minusMinutes(30),
            null
        );
        SwitchActivityAppData.Command command = new SwitchActivityAppData.Command(actor, "IntelliJ IDEA");

        when(studySessionRepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.of(activitySession));
        when(recordActivitySegmentRepositoryPort.findOpenSegmentBySessionIdForUpdate(activitySession.id()))
            .thenReturn(Optional.of(openSegment));
        when(recordActivitySegmentRepositoryPort.save(any(RecordActivitySegment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(studySessionRepositoryPort.save(any(StudySession.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        SwitchActivityAppData.Result result = switchActivityAppService.execute(command);

        assertThat(result.session().activity()).isNotNull();
        assertThat(result.session().activity().appName()).isEqualTo("IntelliJ IDEA");
        verify(recordActivitySegmentRepositoryPort, times(2)).save(any(RecordActivitySegment.class));
        verify(studySessionRepositoryPort).save(any(StudySession.class));
    }

    @Test
    @DisplayName("같은 앱으로 전환 요청하면 segment를 추가하지 않는다")
    void execute_noopWhenSameApp() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        StudySession activitySession = StudySession.createActivity(
            100L,
            user,
            "Code",
            LocalDateTime.now().minusMinutes(30),
            null
        );
        SwitchActivityAppData.Command command = new SwitchActivityAppData.Command(actor, "Code");

        when(studySessionRepositoryPort.findActiveSessionByUserIdForUpdate(actor.id()))
            .thenReturn(Optional.of(activitySession));

        SwitchActivityAppData.Result result = switchActivityAppService.execute(command);

        assertThat(result.session().activity()).isNotNull();
        assertThat(result.session().activity().appName()).isEqualTo("Code");
        verify(recordActivitySegmentRepositoryPort, never()).save(any(RecordActivitySegment.class));
        verify(studySessionRepositoryPort, never()).save(any(StudySession.class));
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
