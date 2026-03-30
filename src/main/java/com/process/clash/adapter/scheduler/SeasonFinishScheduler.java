package com.process.clash.adapter.scheduler;

import com.process.clash.application.shop.season.service.SeasonFinishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeasonFinishScheduler {

    private final SeasonFinishService seasonFinishService;

    /**
     * 매 시즌 종료 시 EXP 초기화(SEASON_RESET) 및 쿠키 지급(SEASON_REWARD)
     * 4개월마다 매달 6일 KST 06:00 실행 (4월, 8월, 12월)
     */
    @Scheduled(cron = "0 0 6 6 4,8,12 *", zone = "Asia/Seoul")
    public void runSeasonFinish() {
        try {
            log.info("시즌 종료 스케줄러 시작");
            seasonFinishService.resetAllUsersExp();
        } catch (Exception e) {
            log.error("시즌 종료 스케줄러 실패", e);
        }
    }
}