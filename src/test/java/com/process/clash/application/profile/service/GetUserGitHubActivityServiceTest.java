package com.process.clash.application.profile.service;

import com.process.clash.application.github.data.GitHubDailyContributionDto;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.profile.data.GetMyGitHubActivityData;
import com.process.clash.application.profile.data.GetUserGitHubActivityData;
import com.process.clash.application.profile.exception.exception.forbidden.ProfilePrivatedException;
import com.process.clash.application.profile.policy.ProfilePolicy;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.common.enums.PeriodCategory;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserGitHubActivityServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;

    @Mock
    private ProfilePolicy profilePolicy;

    private GetUserGitHubActivityService getUserGitHubActivityService;

    @BeforeEach
    void setUp() {
        getUserGitHubActivityService = new GetUserGitHubActivityService(
            userRepositoryPort, gitHubDailyStatsQueryPort, profilePolicy
        );
    }

    @Test
    @DisplayName("비공개 유저 GitHub 활동 조회 시 ProfilePrivatedException을 던진다")
    void execute_throwsProfilePrivatedException_whenUserIsPrivate() {
        Long targetUserId = 2L;
        User privateUser = createUser(targetUserId, true);
        GetUserGitHubActivityData.Command command = new GetUserGitHubActivityData.Command(targetUserId, PeriodCategory.WEEK);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.of(privateUser));

        assertThatThrownBy(() -> getUserGitHubActivityService.execute(command))
            .isInstanceOf(ProfilePrivatedException.class);
    }

    @Test
    @DisplayName("공개 유저 GitHub 활동 조회 시 빈 기여도를 반환한다")
    void execute_returnsEmptyContributions_whenNoActivity() {
        Long targetUserId = 2L;
        User publicUser = createUser(targetUserId, false);
        GetUserGitHubActivityData.Command command = new GetUserGitHubActivityData.Command(targetUserId, PeriodCategory.WEEK);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.of(publicUser));
        when(gitHubDailyStatsQueryPort.findDailyContributionsByUserId(any(), any(), any(), any()))
            .thenReturn(List.of());

        GetMyGitHubActivityData.Result result = getUserGitHubActivityService.execute(command);

        assertThat(result.totalContributions()).isZero();
        assertThat(result.contributions()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 유저 조회 시 UserNotFoundException을 던진다")
    void execute_throwsUserNotFoundException_whenUserNotFound() {
        Long targetUserId = 999L;
        GetUserGitHubActivityData.Command command = new GetUserGitHubActivityData.Command(targetUserId, PeriodCategory.WEEK);

        when(userRepositoryPort.findById(targetUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getUserGitHubActivityService.execute(command))
            .isInstanceOf(UserNotFoundException.class);
    }

    private User createUser(Long id, boolean isPrivate) {
        return new User(
            id, Instant.now(), Instant.now(),
            "username", "user@example.com", "name", "encoded-password",
            Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE,
            null, isPrivate, RankTier.NONE, ExpTier.UNRANKED,
            0
        );
    }
}
