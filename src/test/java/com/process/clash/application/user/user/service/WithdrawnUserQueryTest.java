package com.process.clash.application.user.user.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.profile.data.GetMyProfileData;
import com.process.clash.application.profile.service.EquippedItemsAssembler;
import com.process.clash.application.profile.service.GetMyProfileService;
import com.process.clash.application.realtime.port.out.UserPresencePort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.usergithub.port.out.UserGitHubRepositoryPort;
import com.process.clash.domain.common.enums.Major;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * 탈퇴한 유저는 일반 조회에서 보이지 않아야 한다.
 *
 * findById → deleted_at IS NULL 조건이 적용되어 탈퇴 유저를 반환하지 않음
 * findByIdIncludingDeleted → 배틀 이력 표시 등 내부 용도로만 사용
 */
@ExtendWith(MockitoExtension.class)
class WithdrawnUserQueryTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private UserGitHubRepositoryPort userGitHubRepositoryPort;

    @Mock
    private UserPresencePort userPresencePort;

    @Mock
    private EquippedItemsAssembler equippedItemsAssembler;

    private GetMyProfileService getMyProfileService;

    @BeforeEach
    void setUp() {
        getMyProfileService = new GetMyProfileService(
                userRepositoryPort,
                userGitHubRepositoryPort,
                userPresencePort,
                equippedItemsAssembler
        );
    }

    @Test
    @DisplayName("탈퇴한 유저는 일반 조회(findById)에서 찾을 수 없다")
    void findById_returnsEmpty_forWithdrawnUser() {
        Long withdrawnUserId = 1L;

        // findById는 deleted_at IS NULL 조건이 적용되므로 탈퇴 유저는 empty 반환
        when(userRepositoryPort.findById(withdrawnUserId)).thenReturn(Optional.empty());

        assertThat(userRepositoryPort.findById(withdrawnUserId)).isEmpty();
    }

    @Test
    @DisplayName("탈퇴한 유저로 프로필 조회 시 UserNotFoundException이 발생한다")
    void getMyProfile_throwsUserNotFoundException_whenUserIsWithdrawn() {
        Actor actor = new Actor(1L);
        GetMyProfileData.Command command = new GetMyProfileData.Command(actor);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getMyProfileService.execute(command))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("findByIdIncludingDeleted는 탈퇴한 유저도 반환한다 (배틀 이력 표시용)")
    void findByIdIncludingDeleted_returnsWithdrawnUser() {
        Long withdrawnUserId = 1L;
        User withdrawnUser = withdrawnUser(withdrawnUserId);

        when(userRepositoryPort.findByIdIncludingDeleted(withdrawnUserId))
                .thenReturn(Optional.of(withdrawnUser));

        Optional<User> result = userRepositoryPort.findByIdIncludingDeleted(withdrawnUserId);

        assertThat(result).isPresent();
        assertThat(result.get().isDeleted()).isTrue();
        assertThat(result.get().id()).isEqualTo(withdrawnUserId);
    }

    @Test
    @DisplayName("탈퇴한 유저의 이름은 findByIdIncludingDeleted로 접근할 수 있다")
    void findByIdIncludingDeleted_returnsUserNameForBattleHistory() {
        Long withdrawnUserId = 1L;
        User withdrawnUser = withdrawnUser(withdrawnUserId);

        when(userRepositoryPort.findByIdIncludingDeleted(withdrawnUserId))
                .thenReturn(Optional.of(withdrawnUser));

        User user = userRepositoryPort.findByIdIncludingDeleted(withdrawnUserId).orElseThrow();

        assertThat(user.name()).isEqualTo("탈퇴유저");
        assertThat(user.isDeleted()).isTrue();
    }

    private User withdrawnUser(Long id) {
        return new User(
                id,
                Instant.now().minusSeconds(86_400),
                Instant.now(),
                "withdrawn_user",
                "withdrawn@example.com",
                "탈퇴유저",
                "encoded-password",
                Role.USER,
                "",
                0,
                0,
                Major.NONE,
                UserStatus.ACTIVE,
                Instant.now()   // deletedAt 설정 → 탈퇴 상태
        );
    }
}
