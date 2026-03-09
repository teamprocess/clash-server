package com.process.clash.adapter.scheduler;

import com.process.clash.application.ranking.service.RankingTierUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RankingTierScheduler {

    private final RankingTierUpdateService rankingTierUpdateService;

    /**
     * 랭킹 기반 티어(Master, Aura)를 KST 06:00, 12:00, 18:00, 00:00에 갱신
     */
    @Scheduled(cron = "0 0 0,6,12,18 * * *", zone = "Asia/Seoul")
    public void updateRankTiers() {
        try {
            log.info("RankTier 스케줄 실행 시작");
            rankingTierUpdateService.updateRankTiers();
        } catch (Exception e) {
            log.error("RankTier 스케줄 실행 실패", e);
        }
    }
}