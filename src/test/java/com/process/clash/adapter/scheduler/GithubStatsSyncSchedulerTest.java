package com.process.clash.adapter.scheduler;

import com.process.clash.application.github.service.GithubDailyStatsSyncService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class GithubStatsSyncSchedulerTest {

    @Test
    @DisplayName("30일 스케줄러는 30일 동기화를 호출한다")
    void runHourly30DaysSyncExceptMorningSix_callsThirtyDaysSync() {
        GithubDailyStatsSyncService syncService = mock(GithubDailyStatsSyncService.class);
        GithubStatsSyncScheduler scheduler = new GithubStatsSyncScheduler(syncService);

        scheduler.runHourly30DaysSyncExceptMorningSix();

        verify(syncService, times(1)).syncRecent30Days();
    }

    @Test
    @DisplayName("6시 스케줄러는 365일 동기화를 호출한다")
    void runDaily365DaysSyncAtMorningSix_callsThreeHundredSixtyFiveDaysSync() {
        GithubDailyStatsSyncService syncService = mock(GithubDailyStatsSyncService.class);
        GithubStatsSyncScheduler scheduler = new GithubStatsSyncScheduler(syncService);

        scheduler.runDaily365DaysSyncAtMorningSix();

        verify(syncService, times(1)).syncRecent365Days();
    }
}
