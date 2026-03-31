package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BattleFinishService {

    private final BattleRepositoryPort battleRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;

    public void finishExpiredBattles() {
        List<Battle> expiredInProgress = battleRepositoryPort.findExpiredInProgressBattles();
        List<Battle> expiredNotStarted = battleRepositoryPort.findExpiredNotStartedBattles();

        List<Battle> battlesToUpdate = new ArrayList<>();

        if (!expiredInProgress.isEmpty()) {
            // 라이벌 일괄 조회
            Set<Long> rivalIds = expiredInProgress.stream()
                    .map(Battle::rivalId)
                    .collect(Collectors.toSet());
            Map<Long, Rival> rivalMap = rivalRepositoryPort.findByIdIn(rivalIds).stream()
                    .collect(Collectors.toMap(Rival::id, r -> r));

            // 유저별 배틀 그룹화 후 평균 exp 일괄 조회
            Map<Long, List<Battle>> battlesByUserId = new HashMap<>();
            for (Battle battle : expiredInProgress) {
                Rival rival = rivalMap.get(battle.rivalId());
                if (rival == null) continue;
                battlesByUserId.computeIfAbsent(rival.firstUserId(), k -> new ArrayList<>()).add(battle);
                battlesByUserId.computeIfAbsent(rival.secondUserId(), k -> new ArrayList<>()).add(battle);
            }

            Map<Long, Map<Long, Double>> avgExpByUserAndBattle = new HashMap<>();
            for (Map.Entry<Long, List<Battle>> entry : battlesByUserId.entrySet()) {
                avgExpByUserAndBattle.put(
                        entry.getKey(),
                        userExpHistoryRepositoryPort.findAverageExpForBattles(entry.getKey(), entry.getValue())
                );
            }

            for (Battle battle : expiredInProgress) {
                try {
                    Rival rival = Optional.ofNullable(rivalMap.get(battle.rivalId()))
                            .orElseThrow(() -> new IllegalStateException("Rival not found. rivalId=" + battle.rivalId()));
                    Long winnerId = determineWinner(battle.id(), rival.firstUserId(), rival.secondUserId(), avgExpByUserAndBattle);
                    battlesToUpdate.add(battle.finishWithWinner(winnerId));
                } catch (Exception e) {
                    log.error("배틀 종료 처리 중 오류 발생. battleId={}", battle.id(), e);
                }
            }
        }

        expiredNotStarted.stream()
                .map(Battle::cancel)
                .forEach(battlesToUpdate::add);

        if (!battlesToUpdate.isEmpty()) {
            battleRepositoryPort.saveAll(battlesToUpdate);
        }

        log.info("Expired battles processed. done={}, canceled={}", expiredInProgress.size(), expiredNotStarted.size());
    }

    private Long determineWinner(Long battleId, Long firstUserId, Long secondUserId,
                                  Map<Long, Map<Long, Double>> avgExpByUserAndBattle) {
        double firstExp = avgExpByUserAndBattle.getOrDefault(firstUserId, Map.of()).getOrDefault(battleId, 0.0);
        double secondExp = avgExpByUserAndBattle.getOrDefault(secondUserId, Map.of()).getOrDefault(battleId, 0.0);

        if (firstExp > secondExp) return firstUserId;
        if (secondExp > firstExp) return secondUserId;
        return null; // 무승부
    }
}