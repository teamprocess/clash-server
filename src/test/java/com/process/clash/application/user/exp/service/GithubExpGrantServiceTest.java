package com.process.clash.application.user.exp.service;

import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.github.service.StudyDateCalculator;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.github.entity.GitHubDailyStats;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;
import com.process.clash.domain.user.userexphistory.enums.ExpActingCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubExpGrantServiceTest {

    @Mock
    private GitHubDailyStatsQueryPort gitHubDailyStatsQueryPort;

    @Mock
    private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private StudyDateCalculator studyDateCalculator;

    private GithubExpGrantService githubExpGrantService;

    private static final LocalDate TODAY = LocalDate.of(2025, 1, 15);
    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        githubExpGrantService = new GithubExpGrantService(
            gitHubDailyStatsQueryPort,
            userExpHistoryRepositoryPort,
            userRepositoryPort,
            studyDateCalculator
        );
        when(studyDateCalculator.toStudyDate(any(Instant.class))).thenReturn(TODAY);
    }

    @Test
    @DisplayName("GitHub 통계가 없으면 EXP를 지급하지 않는다")
    void grantForToday_doesNothingWhenNoStats() {
        when(gitHubDailyStatsQueryPort.findAllByStudyDate(TODAY)).thenReturn(List.of());

        githubExpGrantService.grantForToday();

        verify(userExpHistoryRepositoryPort, never()).save(any());
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("기존 EXP 레코드가 없으면 신규 레코드를 생성하고 user.totalExp를 증가시킨다")
    void grantForToday_createsNewRecordWhenNoExisting() {
        GitHubDailyStats stats = createStats(USER_ID, 5, 2, 1, 3);
        // EXP = min(5,50)*50 + min(2,5)*100 + min(1,5)*100 + min(3,5)*10 = 250+200+100+30 = 580
        int expectedExp = 580;
        User user = createUser(USER_ID, 0);

        when(gitHubDailyStatsQueryPort.findAllByStudyDate(TODAY)).thenReturn(List.of(stats));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, TODAY, ExpActingCategory.GITHUB))
            .thenReturn(Optional.empty());
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(user));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        githubExpGrantService.grantForToday();

        ArgumentCaptor<UserExpHistory> historyCaptor = ArgumentCaptor.forClass(UserExpHistory.class);
        verify(userExpHistoryRepositoryPort).save(historyCaptor.capture());
        assertThat(historyCaptor.getValue().id()).isNull();
        assertThat(historyCaptor.getValue().earnExp()).isEqualTo(expectedExp);
        assertThat(historyCaptor.getValue().actingCategory()).isEqualTo(ExpActingCategory.GITHUB);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort).save(userCaptor.capture());
        assertThat(userCaptor.getValue().totalExp()).isEqualTo(expectedExp);
    }

    @Test
    @DisplayName("기존 EXP 레코드가 있고 새 EXP가 더 크면 delta만큼 user.totalExp를 증가시킨다")
    void grantForToday_updatesExistingRecordAndAddsOnlyDelta() {
        GitHubDailyStats stats = createStats(USER_ID, 10, 0, 0, 0);
        // 새 EXP = min(10,50)*50 = 500
        int prevExp = 300;
        int newExp = 500;
        int delta = newExp - prevExp;

        UserExpHistory existingHistory = new UserExpHistory(
            99L, Instant.now(), TODAY, prevExp, ExpActingCategory.GITHUB, USER_ID
        );
        User user = createUser(USER_ID, 300);

        when(gitHubDailyStatsQueryPort.findAllByStudyDate(TODAY)).thenReturn(List.of(stats));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, TODAY, ExpActingCategory.GITHUB))
            .thenReturn(Optional.of(existingHistory));
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(user));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        githubExpGrantService.grantForToday();

        ArgumentCaptor<UserExpHistory> historyCaptor = ArgumentCaptor.forClass(UserExpHistory.class);
        verify(userExpHistoryRepositoryPort).save(historyCaptor.capture());
        assertThat(historyCaptor.getValue().id()).isEqualTo(99L);
        assertThat(historyCaptor.getValue().earnExp()).isEqualTo(newExp);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort).save(userCaptor.capture());
        assertThat(userCaptor.getValue().totalExp()).isEqualTo(300 + delta);
    }

    @Test
    @DisplayName("기존 EXP와 새 EXP가 같으면 아무것도 저장하지 않는다")
    void grantForToday_doesNothingWhenExpUnchanged() {
        GitHubDailyStats stats = createStats(USER_ID, 5, 0, 0, 0);
        int sameExp = 250; // min(5,50)*50 = 250

        UserExpHistory existingHistory = new UserExpHistory(
            99L, Instant.now(), TODAY, sameExp, ExpActingCategory.GITHUB, USER_ID
        );

        when(gitHubDailyStatsQueryPort.findAllByStudyDate(TODAY)).thenReturn(List.of(stats));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(createUser(USER_ID, sameExp)));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, TODAY, ExpActingCategory.GITHUB))
            .thenReturn(Optional.of(existingHistory));

        githubExpGrantService.grantForToday();

        verify(userExpHistoryRepositoryPort, never()).save(any());
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("EXP가 0이면 새 레코드를 생성하지 않는다")
    void grantForToday_doesNotCreateRecordWhenExpIsZero() {
        GitHubDailyStats stats = createStats(USER_ID, 0, 0, 0, 0);

        when(gitHubDailyStatsQueryPort.findAllByStudyDate(TODAY)).thenReturn(List.of(stats));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(createUser(USER_ID, 0)));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, TODAY, ExpActingCategory.GITHUB))
            .thenReturn(Optional.empty());

        githubExpGrantService.grantForToday();

        verify(userExpHistoryRepositoryPort, never()).save(any());
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("커밋 수가 50개를 초과해도 50개로 제한해 EXP를 계산한다")
    void grantForToday_capsCommitsAt50() {
        GitHubDailyStats stats = createStats(USER_ID, 100, 0, 0, 0);
        int expectedExp = 50 * 50; // 2500

        when(gitHubDailyStatsQueryPort.findAllByStudyDate(TODAY)).thenReturn(List.of(stats));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, TODAY, ExpActingCategory.GITHUB))
            .thenReturn(Optional.empty());
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(createUser(USER_ID, 0)));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        githubExpGrantService.grantForToday();

        ArgumentCaptor<UserExpHistory> captor = ArgumentCaptor.forClass(UserExpHistory.class);
        verify(userExpHistoryRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().earnExp()).isEqualTo(expectedExp);
    }

    @Test
    @DisplayName("PR 수가 5개를 초과해도 5개로 제한해 EXP를 계산한다")
    void grantForToday_capsPrsAt5() {
        GitHubDailyStats stats = createStats(USER_ID, 0, 10, 0, 0);
        int expectedExp = 5 * 100; // 500

        when(gitHubDailyStatsQueryPort.findAllByStudyDate(TODAY)).thenReturn(List.of(stats));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, TODAY, ExpActingCategory.GITHUB))
            .thenReturn(Optional.empty());
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(createUser(USER_ID, 0)));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        githubExpGrantService.grantForToday();

        ArgumentCaptor<UserExpHistory> captor = ArgumentCaptor.forClass(UserExpHistory.class);
        verify(userExpHistoryRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().earnExp()).isEqualTo(expectedExp);
    }

    @Test
    @DisplayName("리뷰 수가 5개를 초과해도 5개로 제한해 EXP를 계산한다")
    void grantForToday_capsReviewsAt5() {
        GitHubDailyStats stats = createStats(USER_ID, 0, 0, 10, 0);
        int expectedExp = 5 * 100; // 500

        when(gitHubDailyStatsQueryPort.findAllByStudyDate(TODAY)).thenReturn(List.of(stats));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, TODAY, ExpActingCategory.GITHUB))
            .thenReturn(Optional.empty());
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(createUser(USER_ID, 0)));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        githubExpGrantService.grantForToday();

        ArgumentCaptor<UserExpHistory> captor = ArgumentCaptor.forClass(UserExpHistory.class);
        verify(userExpHistoryRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().earnExp()).isEqualTo(expectedExp);
    }

    @Test
    @DisplayName("이슈 수가 5개를 초과해도 5개로 제한해 EXP를 계산한다")
    void grantForToday_capsIssuesAt5() {
        GitHubDailyStats stats = createStats(USER_ID, 0, 0, 0, 10);
        int expectedExp = 5 * 10; // 50

        when(gitHubDailyStatsQueryPort.findAllByStudyDate(TODAY)).thenReturn(List.of(stats));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, TODAY, ExpActingCategory.GITHUB))
            .thenReturn(Optional.empty());
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(createUser(USER_ID, 0)));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        githubExpGrantService.grantForToday();

        ArgumentCaptor<UserExpHistory> captor = ArgumentCaptor.forClass(UserExpHistory.class);
        verify(userExpHistoryRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().earnExp()).isEqualTo(expectedExp);
    }

    @Test
    @DisplayName("한 유저에서 예외가 발생해도 다른 유저 EXP 지급은 계속된다")
    void grantForToday_continuesForOtherUsersWhenOneUserFails() {
        GitHubDailyStats stats1 = createStats(1L, 5, 0, 0, 0);
        GitHubDailyStats stats2 = createStats(2L, 5, 0, 0, 0);
        User user2 = createUser(2L, 0);

        when(gitHubDailyStatsQueryPort.findAllByStudyDate(TODAY)).thenReturn(List.of(stats1, stats2));
        when(userRepositoryPort.findByIdForUpdate(1L)).thenThrow(new RuntimeException("DB error"));
        when(userRepositoryPort.findByIdForUpdate(2L)).thenReturn(Optional.of(user2));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(eq(2L), eq(TODAY), eq(ExpActingCategory.GITHUB)))
            .thenReturn(Optional.empty());
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        githubExpGrantService.grantForToday();

        // user2의 EXP는 지급됨
        verify(userRepositoryPort).findByIdForUpdate(2L);
    }

    // --- helpers ---

    private GitHubDailyStats createStats(Long userId, int commits, int prs, int reviews, int issues) {
        return new GitHubDailyStats(userId, TODAY, commits, prs, issues, reviews,
            0L, 0L, null, null, null, null, 0, 0, 0, Instant.now());
    }

    private User createUser(Long id, int totalExp) {
        return new User(id, Instant.now(), Instant.now(), "user" + id, "user@example.com",
            "name", "pw", Role.USER, "", totalExp, 0, Major.NONE, UserStatus.ACTIVE, null);
    }
}
