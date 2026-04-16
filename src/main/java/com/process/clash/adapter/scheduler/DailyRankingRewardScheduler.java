package com.process.clash.adapter.scheduler;

import com.process.clash.application.ranking.service.DailyRankingRewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyRankingRewardScheduler {

    private final DailyRankingRewardService dailyRankingRewardService;

    /**
     * 매일 KST 06:00에 전날 하루 EXP 기준 상위 3명에게 쿠키를 지급한다.
     */
    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    public void rewardDailyRanking() {
        try {
            log.info("일별 랭킹 보상 스케줄 실행 시작");
            dailyRankingRewardService.rewardDailyRankingWinners();
            log.info("일별 랭킹 보상 스케줄 실행 완료");
        } catch (Exception e) {
            log.error("일별 랭킹 보상 스케줄 실행 실패", e);
        }
    }
}