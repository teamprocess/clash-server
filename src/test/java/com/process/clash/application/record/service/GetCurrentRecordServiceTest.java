package com.process.clash.application.record.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.data.GetCurrentRecordData;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.entity.StudySession;
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
class GetCurrentRecordServiceTest {

    @Mock
    private StudySessionRepositoryPort studySessionRepositoryPort;

    private GetCurrentRecordService getCurrentRecordService;

    @BeforeEach
    void setUp() {
        getCurrentRecordService = new GetCurrentRecordService(
            studySessionRepositoryPort,
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("활성 활동 세션이 있으면 activity 타입으로 반환한다")
    void execute_returnsActiveActivitySession() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        StudySession activeSession = StudySession.createActivity(
            100L,
            user,
            "Code",
            LocalDateTime.of(2026, 2, 11, 10, 0),
            null
        );

        when(studySessionRepositoryPort.findActiveSessionByUserId(actor.id()))
            .thenReturn(Optional.of(activeSession));

        GetCurrentRecordData.Result result = getCurrentRecordService.execute(new GetCurrentRecordData.Command(actor));

        assertThat(result.session()).isNotNull();
        assertThat(result.session().recordType().name()).isEqualTo("ACTIVITY");
        assertThat(result.session().activity().appName()).isEqualTo("Code");
        assertThat(result.session().task()).isNull();
    }

    @Test
    @DisplayName("활성 세션이 없으면 null 세션을 반환한다")
    void execute_returnsNullWhenNoActiveSession() {
        Actor actor = new Actor(1L);
        when(studySessionRepositoryPort.findActiveSessionByUserId(actor.id()))
            .thenReturn(Optional.empty());

        GetCurrentRecordData.Result result = getCurrentRecordService.execute(new GetCurrentRecordData.Command(actor));

        assertThat(result.session()).isNull();
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
}
