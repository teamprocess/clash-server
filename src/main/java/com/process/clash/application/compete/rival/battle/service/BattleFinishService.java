package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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

        if (expiredInProgress.isEmpty()) {
            return;
        }

        List<Battle> battlesToUpdate = new ArrayList<>();

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

        battleRepositoryPort.saveAll(battlesToUpdate);
        log.info("Expired battles processed. done={}", expiredInProgress.size());
    }

    /**
     * 조회 시점에 만료된 단일 배틀을 DONE으로 처리하고 DB에 저장한다.
     * 스케줄러 미실행 구간에 조회가 발생할 때 호출되며, REQUIRES_NEW 트랜잭션으로 독립 커밋한다.
     * 이미 처리됐거나 라이벌이 없는 경우 in-memory resolveStatus로 fallback한다.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Battle finishSingleIfExpired(Battle battle) {
        Instant now = Instant.now();
        if (!battle.isExpiredInProgress(now)) {
            return battle;
        }
        if (battle.rivalId() == null) {
            return battle.resolveStatus(now);
        }

        // 실제 DB 상태를 재확인 (다른 요청이나 스케줄러가 이미 처리했을 수 있음)
        Battle latest = battleRepositoryPort.findById(battle.id()).orElse(battle);
        if (!latest.isExpiredInProgress(now)) {
            return latest;
        }

        try {
            Rival rival = rivalRepositoryPort.findById(latest.rivalId())
                    .orElseThrow(() -> new IllegalStateException("Rival not found. rivalId=" + latest.rivalId()));

            Map<Long, Map<Long, Double>> avgExpByUserAndBattle = new HashMap<>();
            avgExpByUserAndBattle.put(rival.firstUserId(),
                    userExpHistoryRepositoryPort.findAverageExpForBattles(rival.firstUserId(), List.of(latest)));
            avgExpByUserAndBattle.put(rival.secondUserId(),
                    userExpHistoryRepositoryPort.findAverageExpForBattles(rival.secondUserId(), List.of(latest)));

            Long winnerId = determineWinner(latest.id(), rival.firstUserId(), rival.secondUserId(), avgExpByUserAndBattle);
            Battle finished = latest.finishWithWinner(winnerId);
            Battle saved = battleRepositoryPort.save(finished);
            log.info("Battle lazily finished on query. battleId={}, winnerId={}", saved.id(), winnerId);
            return saved;
        } catch (Exception e) {
            log.error("Lazy finish failed for battle {}. Falling back to in-memory resolution.", battle.id(), e);
            return latest.resolveStatus(now);
        }
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