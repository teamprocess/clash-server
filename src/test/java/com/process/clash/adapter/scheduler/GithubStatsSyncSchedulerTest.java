package com.process.clash.adapter.scheduler;

import com.process.clash.application.github.service.GithubDailyStatsSyncService;
import com.process.clash.application.ranking.service.ZeroRankingDataInitService;
import com.process.clash.application.user.exp.service.GithubExpGrantService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GithubStatsSyncSchedulerTest {

    @Mock
    private GithubDailyStatsSyncService syncService;

    @Mock
    private GithubExpGrantService githubExpGrantService;

    @Mock
    private ZeroRankingDataInitService zeroRankingDataInitService;

    private GithubStatsSyncScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new GithubStatsSyncScheduler(syncService, githubExpGrantService, zeroRankingDataInitService);
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
    @DisplayName("30일 스케줄러는 0 EXP 초기화를 호출하지 않는다")
    void runHourly30DaysSyncExceptMorningSix_doesNotCallZeroExpInit() {
        scheduler.runHourly30DaysSyncExceptMorningSix();

        verify(zeroRankingDataInitService, times(0)).initZeroExpForToday();
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

    @Test
    @DisplayName("6시 스케줄러는 0 EXP 초기화를 호출한다")
    void runDaily365DaysSyncAtMorningSix_callsZeroExpInit() {
        scheduler.runDaily365DaysSyncAtMorningSix();

        verify(zeroRankingDataInitService, times(1)).initZeroExpForToday();
    }

    @Test
    @DisplayName("6시 GitHub 동기화가 실패해도 0 EXP 초기화를 호출한다")
    void runDaily365DaysSyncAtMorningSix_callsZeroExpInitWhenSyncFails() {
        doThrow(new RuntimeException("github sync failed")).when(syncService).syncRecent365Days();

        scheduler.runDaily365DaysSyncAtMorningSix();

        verify(zeroRankingDataInitService, times(1)).initZeroExpForToday();
    }

    @Test
    @DisplayName("이전 GitHub 동기화가 실행 중이면 다음 GitHub 동기화를 건너뛴다")
    void skipOverlappingGithubSyncJob() throws Exception {
        CountDownLatch firstSyncStarted = new CountDownLatch(1);
        CountDownLatch releaseFirstSync = new CountDownLatch(1);

        doAnswer(invocation -> {
            firstSyncStarted.countDown();
            assertThat(releaseFirstSync.await(1, TimeUnit.SECONDS)).isTrue();
            return null;
        }).when(syncService).syncRecent30Days();

        FutureTask<Void> firstJob = new FutureTask<>(() -> {
            scheduler.runHourly30DaysSyncExceptMorningSix();
            return null;
        });
        Thread firstJobThread = new Thread(firstJob, "github-sync-test");
        firstJobThread.start();

        assertThat(firstSyncStarted.await(1, TimeUnit.SECONDS)).isTrue();

        scheduler.runDaily365DaysSyncAtMorningSix();

        verify(syncService, never()).syncRecent365Days();
        verify(zeroRankingDataInitService, never()).initZeroExpForToday();

        releaseFirstSync.countDown();
        firstJob.get(1, TimeUnit.SECONDS);

        verify(githubExpGrantService).grantForToday();

        scheduler.runDaily365DaysSyncAtMorningSix();

        verify(syncService).syncRecent365Days();
        verify(githubExpGrantService, times(2)).grantForToday();
        verify(zeroRankingDataInitService).initZeroExpForToday();
    }
}
