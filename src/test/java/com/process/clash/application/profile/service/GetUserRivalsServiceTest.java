package com.process.clash.application.profile.service;

import com.process.clash.application.compete.rival.rival.data.GetMyRivalActingData;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.profile.data.GetUserRivalsData;
import com.process.clash.application.profile.exception.exception.forbidden.ProfilePrivatedException;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.out.UserPresencePort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import com.process.clash.infrastructure.config.record.RecordProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserRivalsServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private RivalRepositoryPort rivalRepositoryPort;

    @Mock
    private RecordSessionV2RepositoryPort recordSessionRepositoryPort;

    @Mock
    private UserPresencePort userPresencePort;

    @Mock
    private EquippedItemsAssembler equippedItemsAssembler;

    private GetUserRivalsService getUserRivalsService;

    @BeforeEach
    void setUp() {
        getUserRivalsService = new GetUserRivalsService(
            userRepositoryPort,
            rivalRepositoryPort,
            recordSessionRepositoryPort,
            userPresencePort,
            new RecordProperties("UTC", 0),
            ZoneId.of("UTC"),
            equippedItemsAssembler
        );
    }

    @Test
    @DisplayName("존재하지 않는 유저 조회 시 UserNotFoundException을 던진다")
    void execute_throwsUserNotFoundException_whenUserNotFound() {
        Long targetUserId = 999L;
        GetUserRivalsData.Command command = new GetUserRivalsData.Command(targetUserId);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getUserRivalsService.execute(command))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("탈퇴한 유저 조회 시 UserNotFoundException을 던진다")
    void execute_throwsUserNotFoundException_whenUserDeleted() {
        Long targetUserId = 2L;
        User deletedUser = createDeletedUser(targetUserId);
        GetUserRivalsData.Command command = new GetUserRivalsData.Command(targetUserId);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.of(deletedUser));

        assertThatThrownBy(() -> getUserRivalsService.execute(command))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("비공개 유저 조회 시 ProfilePrivatedException을 던진다")
    void execute_throwsProfilePrivatedException_whenUserIsPrivate() {
        Long targetUserId = 2L;
        User privateUser = createUser(targetUserId, true);
        GetUserRivalsData.Command command = new GetUserRivalsData.Command(targetUserId);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.of(privateUser));

        assertThatThrownBy(() -> getUserRivalsService.execute(command))
            .isInstanceOf(ProfilePrivatedException.class);
    }

    @Test
    @DisplayName("라이벌이 없으면 빈 리스트를 반환한다")
    void execute_returnsEmptyList_whenNoRivals() {
        Long targetUserId = 2L;
        User targetUser = createUser(targetUserId, false);
        GetUserRivalsData.Command command = new GetUserRivalsData.Command(targetUserId);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(rivalRepositoryPort.findAllByUserId(targetUserId)).thenReturn(List.of());

        GetMyRivalActingData.Result result = getUserRivalsService.execute(command);

        assertThat(result.myRivals()).isEmpty();
    }

    @Test
    @DisplayName("라이벌 목록을 정상적으로 반환한다")
    void execute_returnsRivalList() {
        Long targetUserId = 2L;
        User targetUser = createUser(targetUserId, false);
        GetUserRivalsData.Command command = new GetUserRivalsData.Command(targetUserId);

        Rival rival = new Rival(
            100L,
            Instant.now().minusSeconds(86_400),
            Instant.now(),
            RivalLinkingStatus.ACCEPTED,
            targetUserId,
            3L
        );

        User opponentUser = createUser(3L, false);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(rivalRepositoryPort.findAllByUserId(targetUserId)).thenReturn(List.of(rival));
        when(userRepositoryPort.findAllByIds(anyList())).thenReturn(List.of(opponentUser));
        when(recordSessionRepositoryPort.getTotalStudyTimeInSecondsByUserIds(anyList(), any(), any()))
            .thenReturn(Map.of());
        when(recordSessionRepositoryPort.findAllActiveSessionsByUserIds(anyList())).thenReturn(List.of());
        when(userPresencePort.getStatuses(anyList())).thenReturn(Map.of(3L, UserActivityStatus.OFFLINE));
        when(equippedItemsAssembler.loadByUserIds(anyList())).thenReturn(Map.of());

        GetMyRivalActingData.Result result = getUserRivalsService.execute(command);

        assertThat(result.myRivals()).hasSize(1);
        assertThat(result.myRivals().get(0).rivalId()).isEqualTo(100L);
    }

    private User createUser(Long id, boolean isPrivate) {
        return new User(
            id,
            Instant.now().minusSeconds(86_400),
            Instant.now(),
            "username-" + id,
            "user" + id + "@example.com",
            "name-" + id,
            "encoded-password",
            Role.USER,
            "",
            0,
            0,
            Major.NONE,
            UserStatus.ACTIVE,
            null,
            isPrivate,
            RankTier.NONE,
            ExpTier.UNRANKED,
            0
        );
    }

    private User createDeletedUser(Long id) {
        return new User(
            id,
            Instant.now().minusSeconds(86_400),
            Instant.now(),
            "username-" + id,
            "user" + id + "@example.com",
            "name-" + id,
            "encoded-password",
            Role.USER,
            "",
            0,
            0,
            Major.NONE,
            UserStatus.ACTIVE,
            Instant.now(),
            false,
            RankTier.NONE,
            ExpTier.UNRANKED,
            0
        );
    }
}
