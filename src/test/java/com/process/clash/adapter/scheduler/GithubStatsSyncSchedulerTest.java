package com.process.clash.adapter.scheduler;

import com.process.clash.application.github.service.GithubDailyStatsSyncService;
import com.process.clash.application.user.exp.service.GithubExpGrantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GithubStatsSyncSchedulerTest {

    @Mock
    private GithubDailyStatsSyncService syncService;

    @Mock
    private GithubExpGrantService githubExpGrantService;

    private GithubStatsSyncScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new GithubStatsSyncScheduler(syncService, githubExpGrantService);
    }

    @Test
    @DisplayName("30일 스케줄러는 30일 동기화를 호출한다")
    void runHourly30DaysSyncExceptMorningSix_callsThirtyDaysSync() {
        scheduler.runHourly30DaysSyncExceptMorningSix();

        verify(syncService, times(1)).syncRecent30Days();
    }

    @Test
    @DisplayName("30일 스케줄러는 GitHub EXP 지급을 호출한다")
    void runHourly30DaysSyncExceptMorningSix_callsGithubExpGrant() {
        scheduler.runHourly30DaysSyncExceptMorningSix();

        verify(githubExpGrantService, times(1)).grantForToday();
    }

    @Test
    @DisplayName("6시 스케줄러는 365일 동기화를 호출한다")
    void runDaily365DaysSyncAtMorningSix_callsThreeHundredSixtyFiveDaysSync() {
        scheduler.runDaily365DaysSyncAtMorningSix();

        verify(syncService, times(1)).syncRecent365Days();
    }

    @Test
    @DisplayName("6시 스케줄러는 GitHub EXP 지급을 호출한다")
    void runDaily365DaysSyncAtMorningSix_callsGithubExpGrant() {
        scheduler.runDaily365DaysSyncAtMorningSix();

        verify(githubExpGrantService, times(1)).grantForToday();
    }
}
