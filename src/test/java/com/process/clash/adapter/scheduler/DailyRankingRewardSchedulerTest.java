package com.process.clash.adapter.scheduler;

import com.process.clash.application.ranking.service.DailyRankingRewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DailyRankingRewardSchedulerTest {

    @Mock
    private DailyRankingRewardService dailyRankingRewardService;

    private DailyRankingRewardScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new DailyRankingRewardScheduler(dailyRankingRewardService);
    }

    @Test
    @DisplayName("스케줄러 실행 시 일별 랭킹 보상 서비스를 1회 호출한다")
    void rewardDailyRanking_callsService() {
        scheduler.rewardDailyRanking();

        verify(dailyRankingRewardService, times(1)).rewardDailyRankingWinners();
    }
}