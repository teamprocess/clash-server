package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.CreateSubjectV2Data;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateSubjectV2ServiceTest {

    @Mock
    private RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    private CreateSubjectV2Service createSubjectV2Service;

    @BeforeEach
    void setUp() {
        createSubjectV2Service = new CreateSubjectV2Service(
            recordSubjectV2RepositoryPort,
            userRepositoryPort
        );
    }

    @Test
    @DisplayName("유효한 사용자면 과목 그룹을 생성한다")
    void execute_createsSubject() {
        Actor actor = new Actor(1L);
        CreateSubjectV2Data.Command command = new CreateSubjectV2Data.Command(actor, "백엔드 프로젝트");

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(createUser(actor.id())));
        when(recordSubjectV2RepositoryPort.save(any(RecordSubjectV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        createSubjectV2Service.execute(command);

        verify(recordSubjectV2RepositoryPort).save(any(RecordSubjectV2.class));
    }

    @Test
    @DisplayName("사용자가 없으면 예외가 발생한다")
    void execute_throwsWhenUserNotFound() {
        Actor actor = new Actor(1L);
        CreateSubjectV2Data.Command command = new CreateSubjectV2Data.Command(actor, "백엔드 프로젝트");
        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createSubjectV2Service.execute(command))
            .isInstanceOf(UserNotFoundException.class);
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
}
