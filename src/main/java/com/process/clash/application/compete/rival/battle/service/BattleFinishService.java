package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BattleFinishService {

    private final BattleRepositoryPort battleRepositoryPort;

    public void finishExpiredBattles(LocalDate today) {
        List<Battle> expiredInProgress = battleRepositoryPort.findExpiredInProgressBattles(today);
        List<Battle> expiredNotStarted = battleRepositoryPort.findExpiredNotStartedBattles(today);

        List<Battle> battlesToUpdate = Stream.concat(
                expiredInProgress.stream().map(Battle::finish),
                expiredNotStarted.stream().map(Battle::cancel)
        ).toList();

        if (!battlesToUpdate.isEmpty()) {
            battleRepositoryPort.saveAll(battlesToUpdate);
        }

        log.info("Expired battles processed. done={}, canceled={}", expiredInProgress.size(), expiredNotStarted.size());
    }
}
