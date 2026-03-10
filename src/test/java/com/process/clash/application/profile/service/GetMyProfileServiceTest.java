package com.process.clash.application.profile.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.profile.data.EquippedItemsData;
import com.process.clash.application.profile.data.GetMyProfileData;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.out.UserPresencePort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.usergithub.port.out.UserGitHubRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.usergithub.entity.UserGitHub;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMyProfileServiceTest {

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
    @DisplayName("내 프로필 조회 시 github 연동 여부와 활동 상태를 반환한다")
    void execute_returnsProfileWithActivityStatus() {
        Actor actor = new Actor(1L);
        GetMyProfileData.Command command = new GetMyProfileData.Command(actor);
        User user = createUser(1L);
        UserGitHub userGitHub = new UserGitHub(
            10L,
            "octocat",
            user.id(),
            "node-id",
            "octocat@example.com",
            "access-token"
        );

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(userGitHubRepositoryPort.findByUserId(user.id())).thenReturn(Optional.of(userGitHub));
        when(userPresencePort.getStatus(user.id())).thenReturn(UserActivityStatus.AWAY);
        when(equippedItemsAssembler.loadByUserId(user.id())).thenReturn(EquippedItemsData.empty());

        GetMyProfileData.Result result = getMyProfileService.execute(command);

        assertThat(result.githubLinked()).isTrue();
        assertThat(result.activityStatus()).isEqualTo(UserActivityStatus.AWAY);
        assertThat(result.equippedItems()).isEqualTo(EquippedItemsData.empty());
    }

    @Test
    @DisplayName("totalExp가 SILVER 범위(10000~29999)이면 expTier가 SILVER로 반환된다")
    void execute_returnsCorrectExpTier() {
        Actor actor = new Actor(1L);
        User user = createUser(1L, 15_000, RankTier.NONE);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(userGitHubRepositoryPort.findByUserId(user.id())).thenReturn(Optional.empty());
        when(userPresencePort.getStatus(user.id())).thenReturn(UserActivityStatus.AWAY);
        when(equippedItemsAssembler.loadByUserId(user.id())).thenReturn(EquippedItemsData.empty());

        GetMyProfileData.Result result = getMyProfileService.execute(new GetMyProfileData.Command(actor));

        assertThat(result.expTier()).isEqualTo(ExpTier.SILVER);
    }

    @Test
    @DisplayName("currentRankTier가 MASTER이면 프로필에 MASTER로 반환된다")
    void execute_returnsCurrentRankTier() {
        Actor actor = new Actor(1L);
        User user = createUser(1L, 80_000, RankTier.MASTER);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(userGitHubRepositoryPort.findByUserId(user.id())).thenReturn(Optional.empty());
        when(userPresencePort.getStatus(user.id())).thenReturn(UserActivityStatus.AWAY);
        when(equippedItemsAssembler.loadByUserId(user.id())).thenReturn(EquippedItemsData.empty());

        GetMyProfileData.Result result = getMyProfileService.execute(new GetMyProfileData.Command(actor));

        assertThat(result.currentRankTier()).isEqualTo(RankTier.MASTER);
    }

    private User createUser(Long id) {
        return createUser(id, 0, RankTier.NONE);
    }

    private User createUser(Long id, int totalExp, RankTier currentRankTier) {
        return new User(
            id,
            Instant.now().minusSeconds(86_400),
            Instant.now(),
            "username",
            "user@example.com",
            "name",
            "encoded-password",
            Role.USER,
            "",
            totalExp,
            0,
            Major.NONE,
            UserStatus.ACTIVE,
            null,
            currentRankTier,
            ExpTier.fromExp(totalExp)
        );
    }
}
