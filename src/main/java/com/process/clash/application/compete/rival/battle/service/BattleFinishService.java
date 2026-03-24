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
public class BattleFinishService {

    private final BattleRepositoryPort battleRepositoryPort;

    public void finishExpiredBattles(LocalDate today) {
        List<Battle> expiredInProgress = battleRepositoryPort.findExpiredInProgressBattles(today);
        battleRepositoryPort.saveAll(expiredInProgress.stream().map(Battle::finish).toList());

        List<Battle> expiredNotStarted = battleRepositoryPort.findExpiredNotStartedBattles(today);
        battleRepositoryPort.saveAll(expiredNotStarted.stream().map(Battle::cancel).toList());

        log.info("Expired battles processed. done={}, canceled={}", expiredInProgress.size(), expiredNotStarted.size());
    }
}
