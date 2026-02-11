package com.process.clash.application.profile.service;

import com.process.clash.application.common.actor.Actor;
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
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private GetMyProfileService getMyProfileService;

    @BeforeEach
    void setUp() {
        getMyProfileService = new GetMyProfileService(
            userRepositoryPort,
            userGitHubRepositoryPort,
            userPresencePort
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

        GetMyProfileData.Result result = getMyProfileService.execute(command);

        assertThat(result.githubLinked()).isTrue();
        assertThat(result.activityStatus()).isEqualTo(UserActivityStatus.AWAY);
    }

    private User createUser(Long id) {
        return new User(
            id,
            LocalDateTime.now().minusDays(1),
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
