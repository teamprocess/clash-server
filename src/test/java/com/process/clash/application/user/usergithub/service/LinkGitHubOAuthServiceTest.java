package com.process.clash.application.user.usergithub.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.usergithub.data.LinkGitHubOAuthData;
import com.process.clash.application.user.usergithub.model.GithubOAuthToken;
import com.process.clash.application.user.usergithub.model.GithubOAuthUser;
import com.process.clash.application.user.usergithub.port.out.GithubOAuthPort;
import com.process.clash.application.user.usergithub.port.out.UserGitHubRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.usergithub.entity.UserGitHub;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkGitHubOAuthServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private GithubOAuthPort githubOAuthPort;

    @Mock
    private UserGitHubRepositoryPort userGitHubRepositoryPort;

    @Mock
    private UserGitHubLinkedEventPublisher userGitHubLinkedEventPublisher;

    private LinkGitHubOAuthService linkGitHubOAuthService;

    @BeforeEach
    void setUp() {
        linkGitHubOAuthService = new LinkGitHubOAuthService(
                userRepositoryPort,
                githubOAuthPort,
                userGitHubRepositoryPort,
                userGitHubLinkedEventPublisher
        );
    }

    @Test
    @DisplayName("최초 GitHub 연동이면 저장 후 동기화 이벤트를 발행한다")
    void execute_publishesEventWhenFirstLink() {
        User user = createUser(1L);
        Actor actor = new Actor(user.id());
        LinkGitHubOAuthData.Command command = new LinkGitHubOAuthData.Command(actor, "oauth-code");

        when(userRepositoryPort.findById(user.id())).thenReturn(Optional.of(user));
        when(githubOAuthPort.exchangeCodeForAccessToken("oauth-code"))
                .thenReturn(new GithubOAuthToken("access-token", "bearer", "user:email"));
        when(githubOAuthPort.fetchUser("access-token"))
                .thenReturn(new GithubOAuthUser("octocat", "node-id"));
        when(githubOAuthPort.fetchEmails("access-token"))
                .thenReturn(List.of("octocat@example.com"));
        when(userGitHubRepositoryPort.findByGitHubId("octocat")).thenReturn(Optional.empty());
        when(userGitHubRepositoryPort.findByUserId(user.id())).thenReturn(Optional.empty());
        when(userGitHubRepositoryPort.save(any()))
                .thenReturn(new UserGitHub(
                        10L,
                        "octocat",
                        user.id(),
                        "node-id",
                        "octocat@example.com",
                        "access-token"
                ));

        LinkGitHubOAuthData.Result result = linkGitHubOAuthService.execute(command);

        verify(userGitHubLinkedEventPublisher).publish(user.id());
        assertThat(result.gitHubId()).isEqualTo("octocat");
        assertThat(result.linked()).isTrue();
    }

    @Test
    @DisplayName("이미 연동된 사용자는 토큰 갱신만 하고 동기화 이벤트를 발행하지 않는다")
    void execute_skipsEventWhenAlreadyLinked() {
        User user = createUser(1L);
        Actor actor = new Actor(user.id());
        LinkGitHubOAuthData.Command command = new LinkGitHubOAuthData.Command(actor, "oauth-code");
        UserGitHub linked = new UserGitHub(
                10L,
                "octocat",
                user.id(),
                "old-node-id",
                "old@example.com",
                "old-token"
        );

        when(userRepositoryPort.findById(user.id())).thenReturn(Optional.of(user));
        when(githubOAuthPort.exchangeCodeForAccessToken("oauth-code"))
                .thenReturn(new GithubOAuthToken("access-token", "bearer", "user:email"));
        when(githubOAuthPort.fetchUser("access-token"))
                .thenReturn(new GithubOAuthUser("octocat", "node-id"));
        when(githubOAuthPort.fetchEmails("access-token"))
                .thenReturn(List.of("octocat@example.com"));
        when(userGitHubRepositoryPort.findByGitHubId("octocat")).thenReturn(Optional.of(linked));
        when(userGitHubRepositoryPort.findByUserId(user.id())).thenReturn(Optional.of(linked));
        when(userGitHubRepositoryPort.save(any()))
                .thenReturn(new UserGitHub(
                        10L,
                        "octocat",
                        user.id(),
                        "node-id",
                        "octocat@example.com",
                        "access-token"
                ));

        LinkGitHubOAuthData.Result result = linkGitHubOAuthService.execute(command);

        verify(userGitHubLinkedEventPublisher, never()).publish(anyLong());
        assertThat(result.gitHubId()).isEqualTo("octocat");
        assertThat(result.linked()).isTrue();
    }

    private User createUser(Long id) {
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
                0,
                0,
                Major.NONE,
                UserStatus.ACTIVE
        );
    }
}
