package com.process.clash.adapter.scheduler;

import com.process.clash.application.compete.rival.battle.service.BattleFinishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BattleFinishScheduler {

    private final BattleFinishService battleFinishService;

    @Scheduled(cron = "0 0 6 * * *", zone = "${battle.timezone:Asia/Seoul}")
    public void finishExpiredBattles() {
        safeFinish("scheduled");
    }

    private void safeFinish(String trigger) {
        try {
            log.info("Battle finish check started. trigger={}", trigger);
            battleFinishService.finishExpiredBattles();
        } catch (Exception exception) {
            log.error("Battle finish check failed. trigger={}", trigger, exception);
        }
    }
}