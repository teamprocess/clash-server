package com.process.clash.application.user.user.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.user.data.UpdateMyPrivacyData;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateMyPrivacyServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    private UpdateMyPrivacyService updateMyPrivacyService;

    @BeforeEach
    void setUp() {
        updateMyPrivacyService = new UpdateMyPrivacyService(userRepositoryPort);
    }

    @Test
    @DisplayName("isPrivate=true로 설정하면 비공개로 변경된다")
    void execute_setsPrivateToTrue() {
        Actor actor = new Actor(1L);
        User user = createUser(1L, false);
        User updatedUser = createUser(1L, true);
        UpdateMyPrivacyData.Command command = new UpdateMyPrivacyData.Command(actor, true);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(userRepositoryPort.save(any())).thenReturn(updatedUser);

        UpdateMyPrivacyData.Result result = updateMyPrivacyService.execute(command);

        assertThat(result.isPrivate()).isTrue();
    }

    @Test
    @DisplayName("isPrivate=false로 설정하면 공개로 변경된다")
    void execute_setsPrivateToFalse() {
        Actor actor = new Actor(1L);
        User user = createUser(1L, true);
        User updatedUser = createUser(1L, false);
        UpdateMyPrivacyData.Command command = new UpdateMyPrivacyData.Command(actor, false);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(userRepositoryPort.save(any())).thenReturn(updatedUser);

        UpdateMyPrivacyData.Result result = updateMyPrivacyService.execute(command);

        assertThat(result.isPrivate()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 유저면 UserNotFoundException을 던진다")
    void execute_throwsUserNotFoundException_whenUserNotFound() {
        Actor actor = new Actor(999L);
        UpdateMyPrivacyData.Command command = new UpdateMyPrivacyData.Command(actor, true);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateMyPrivacyService.execute(command))
            .isInstanceOf(UserNotFoundException.class);
    }

    private User createUser(Long id, boolean isPrivate) {
        return new User(
            id, Instant.now(), Instant.now(),
            "username", "user@example.com", "name", "encoded-password",
            Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE,
            null, isPrivate, RankTier.NONE, ExpTier.UNRANKED
        );
    }
}
