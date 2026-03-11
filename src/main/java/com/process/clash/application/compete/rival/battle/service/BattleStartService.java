package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BattleStartService {

    private final BattleRepositoryPort battleRepositoryPort;

    public void startScheduledBattles(LocalDate today) {
        List<Battle> battlesToStart = battleRepositoryPort.findNotStartedBattlesToStart(today);

        battleRepositoryPort.saveAll(battlesToStart.stream().map(Battle::start).toList());

        log.info("Scheduled battles started. count={}", battlesToStart.size());
    }
}