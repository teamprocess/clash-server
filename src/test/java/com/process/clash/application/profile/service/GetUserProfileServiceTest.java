package com.process.clash.application.profile.service;

import com.process.clash.application.profile.data.EquippedItemsData;
import com.process.clash.application.profile.data.GetUserProfileData;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.out.UserPresencePort;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserProfileServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private UserPresencePort userPresencePort;

    @Mock
    private EquippedItemsAssembler equippedItemsAssembler;

    private GetOtherUserProfileService getUserProfileService;

    @BeforeEach
    void setUp() {
        getUserProfileService = new GetOtherUserProfileService(
            userRepositoryPort, userPresencePort, equippedItemsAssembler
        );
    }

    @Test
    @DisplayName("다른 유저 프로필 조회 시 activityStatus와 equippedItems를 반환한다")
    void execute_returnsProfileWithActivityStatusAndEquippedItems() {
        Long targetUserId = 2L;
        User user = createUser(targetUserId, false);
        GetUserProfileData.Command command = new GetUserProfileData.Command(targetUserId);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.of(user));
        when(userPresencePort.getStatus(user.id())).thenReturn(UserActivityStatus.ONLINE);
        when(equippedItemsAssembler.loadByUserId(user.id())).thenReturn(EquippedItemsData.empty());

        GetUserProfileData.Result result = getUserProfileService.execute(command);

        assertThat(result.id()).isEqualTo(targetUserId);
        assertThat(result.activityStatus()).isEqualTo(UserActivityStatus.ONLINE);
        assertThat(result.isPrivate()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 유저 조회 시 UserNotFoundException을 던진다")
    void execute_throwsUserNotFoundException_whenUserNotFound() {
        Long targetUserId = 999L;
        GetUserProfileData.Command command = new GetUserProfileData.Command(targetUserId);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getUserProfileService.execute(command))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("탈퇴한 유저 조회 시 UserNotFoundException을 던진다")
    void execute_throwsUserNotFoundException_whenUserDeleted() {
        Long targetUserId = 2L;
        User deletedUser = new User(
            targetUserId, Instant.now(), Instant.now(),
            "username", "user@example.com", "name", "encoded-password",
            Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE,
            Instant.now(), false, RankTier.NONE, ExpTier.UNRANKED
        );
        GetUserProfileData.Command command = new GetUserProfileData.Command(targetUserId);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.of(deletedUser));

        assertThatThrownBy(() -> getUserProfileService.execute(command))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("비공개 유저도 기본 프로필은 반환한다")
    void execute_returnsProfile_evenWhenPrivate() {
        Long targetUserId = 3L;
        User user = createUser(targetUserId, true);
        GetUserProfileData.Command command = new GetUserProfileData.Command(targetUserId);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.of(user));
        when(userPresencePort.getStatus(user.id())).thenReturn(UserActivityStatus.OFFLINE);
        when(equippedItemsAssembler.loadByUserId(user.id())).thenReturn(EquippedItemsData.empty());

        GetUserProfileData.Result result = getUserProfileService.execute(command);

        assertThat(result.isPrivate()).isTrue();
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
