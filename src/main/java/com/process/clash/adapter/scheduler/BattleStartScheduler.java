package com.process.clash.adapter.scheduler;

import com.process.clash.application.compete.rival.battle.service.BattleStartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@Slf4j
public class BattleStartScheduler {

    private static final String BATTLE_TIMEZONE = "${battle.timezone:Asia/Seoul}";

    private final BattleStartService battleStartService;
    private final ZoneId battleZoneId;

    public BattleStartScheduler(
            BattleStartService battleStartService,
            @Value(BATTLE_TIMEZONE) String battleTimezone
    ) {
        this.battleStartService = battleStartService;
        this.battleZoneId = ZoneId.of(battleTimezone);
    }

    @Scheduled(cron = "0 0 6 * * *", zone = BATTLE_TIMEZONE)
    public void startScheduledBattles() {
        safeStart("scheduled");
    }

    private void safeStart(String trigger) {
        try {
            log.info("Battle start check started. trigger={}", trigger);
            battleStartService.startScheduledBattles(LocalDate.now(battleZoneId));
        } catch (Exception exception) {
            log.error("Battle start check failed. trigger={}", trigger, exception);
        }
    }
}