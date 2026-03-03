package com.process.clash.adapter.scheduler;

import com.process.clash.application.compete.rival.battle.service.BattleFinishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@Slf4j
public class BattleFinishScheduler {

    private final BattleFinishService battleFinishService;
    private final ZoneId battleZoneId;

    public BattleFinishScheduler(
            BattleFinishService battleFinishService,
            @Value("${battle.timezone:Asia/Seoul}") String battleTimezone
    ) {
        this.battleFinishService = battleFinishService;
        this.battleZoneId = ZoneId.of(battleTimezone);
    }

    @Scheduled(cron = "0 0 6 * * *", zone = "${battle.timezone:Asia/Seoul}")
    public void finishExpiredBattles() {
        safeFinish("scheduled");
    }

    private void safeFinish(String trigger) {
        try {
            log.info("Battle finish check started. trigger={}", trigger);
            battleFinishService.finishExpiredBattles(LocalDate.now(battleZoneId));
        } catch (Exception exception) {
            log.error("Battle finish check failed. trigger={}", trigger, exception);
        }
    }
}
