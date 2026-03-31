package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BattleFinishService {

    private final BattleRepositoryPort battleRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;

    public void finishExpiredBattles(LocalDate today) {
        List<Battle> expiredInProgress = battleRepositoryPort.findExpiredInProgressBattles(today);
        List<Battle> expiredNotStarted = battleRepositoryPort.findExpiredNotStartedBattles(today);

        List<Battle> battlesToUpdate = new ArrayList<>();

        for (Battle battle : expiredInProgress) {
            Rival rival = rivalRepositoryPort.findById(battle.rivalId())
                    .orElseThrow(RivalNotFoundException::new);
            Long winnerId = determineWinner(battle, rival.firstUserId(), rival.secondUserId());
            battlesToUpdate.add(battle.finishWithWinner(winnerId));
        }

        expiredNotStarted.stream()
                .map(Battle::cancel)
                .forEach(battlesToUpdate::add);

        if (!battlesToUpdate.isEmpty()) {
            battleRepositoryPort.saveAll(battlesToUpdate);
        }

        log.info("Expired battles processed. done={}, canceled={}", expiredInProgress.size(), expiredNotStarted.size());
    }

    private Long determineWinner(Battle battle, Long firstUserId, Long secondUserId) {
        double firstUserAvgExp = userExpHistoryRepositoryPort
                .findAverageExpByUserIdAndPeriod(firstUserId, battle.startDate(), battle.endDate());
        double secondUserAvgExp = userExpHistoryRepositoryPort
                .findAverageExpByUserIdAndPeriod(secondUserId, battle.startDate(), battle.endDate());

        if (firstUserAvgExp > secondUserAvgExp) return firstUserId;
        if (secondUserAvgExp > firstUserAvgExp) return secondUserId;
        return null; // 무승부
    }
}