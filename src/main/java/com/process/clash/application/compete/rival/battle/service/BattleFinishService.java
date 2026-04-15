package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.usergoodshistory.port.out.UserGoodsHistoryRepositoryPort;
import com.process.clash.domain.common.enums.GoodsActingCategory;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

    private static final Map<Integer, Integer> WINNER_COOKIE_BY_DURATION = Map.of(
            3, 300,
            5, 500,
            7, 700
    );

    private final BattleRepositoryPort battleRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final UserGoodsHistoryRepositoryPort userGoodsHistoryRepositoryPort;

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

        if (!battlesToUpdate.isEmpty()) {
            battleRepositoryPort.saveAll(battlesToUpdate);
            grantBatchBattleRewards(battlesToUpdate, rivalMap);
        }
        log.info("Expired battles processed. done={}", battlesToUpdate.size());
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
            if (winnerId != null) {
                Long loserId = rival.firstUserId().equals(winnerId) ? rival.secondUserId() : rival.firstUserId();
                grantBattleRewards(saved.duration(), winnerId, loserId);
            } else {
                grantDrawRewards(saved.duration(), rival.firstUserId(), rival.secondUserId());
            }
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

    private void grantBatchBattleRewards(List<Battle> finishedBattles, Map<Long, Rival> rivalMap) {
        Set<Long> userIds = new HashSet<>();
        for (Battle battle : finishedBattles) {
            Rival rival = rivalMap.get(battle.rivalId());
            if (rival == null) continue;
            userIds.add(rival.firstUserId());
            userIds.add(rival.secondUserId());
        }
        if (userIds.isEmpty()) return;

        Map<Long, User> userMap = userRepositoryPort.findByIdIn(userIds).stream()
                .collect(Collectors.toMap(User::id, u -> u));

        List<UserGoodsHistory> historyToSave = new ArrayList<>();

        for (Battle battle : finishedBattles) {
            Rival rival = rivalMap.get(battle.rivalId());
            if (rival == null) continue;

            int winnerCookie = WINNER_COOKIE_BY_DURATION.getOrDefault(battle.duration(), 0);
            int loserCookie = winnerCookie / 2;

            if (battle.winnerId() != null) {
                Long loserId = rival.firstUserId().equals(battle.winnerId())
                        ? rival.secondUserId()
                        : rival.firstUserId();

                User winner = userMap.get(battle.winnerId());
                User loser = userMap.get(loserId);

                if (winner != null) {
                    userMap.put(winner.id(), winner.addCookie(winnerCookie));
                    historyToSave.add(new UserGoodsHistory(null, null, GoodsActingCategory.BATTLE_WIN_REWARD, winnerCookie, null, winner.id()));
                }
                if (loser != null) {
                    userMap.put(loser.id(), loser.addCookie(loserCookie));
                    historyToSave.add(new UserGoodsHistory(null, null, GoodsActingCategory.BATTLE_LOSE_REWARD, loserCookie, null, loser.id()));
                }
            } else {
                int drawCookie = (winnerCookie + loserCookie) / 2;
                User first = userMap.get(rival.firstUserId());
                User second = userMap.get(rival.secondUserId());

                if (first != null) {
                    userMap.put(first.id(), first.addCookie(drawCookie));
                    historyToSave.add(new UserGoodsHistory(null, null, GoodsActingCategory.BATTLE_DRAW_REWARD, drawCookie, null, first.id()));
                }
                if (second != null) {
                    userMap.put(second.id(), second.addCookie(drawCookie));
                    historyToSave.add(new UserGoodsHistory(null, null, GoodsActingCategory.BATTLE_DRAW_REWARD, drawCookie, null, second.id()));
                }
            }
        }

        userRepositoryPort.saveAll(new ArrayList<>(userMap.values()));
        userGoodsHistoryRepositoryPort.saveAll(historyToSave);
    }

    private void grantBattleRewards(int duration, Long winnerId, Long loserId) {
        int winnerCookie = WINNER_COOKIE_BY_DURATION.getOrDefault(duration, 0);
        int loserCookie = winnerCookie / 2;

        List<User> usersToSave = new ArrayList<>();
        List<UserGoodsHistory> historyToSave = new ArrayList<>();

        userRepositoryPort.findById(winnerId).ifPresent(winner -> {
            usersToSave.add(winner.addCookie(winnerCookie));
            historyToSave.add(new UserGoodsHistory(null, null, GoodsActingCategory.BATTLE_WIN_REWARD, winnerCookie, null, winnerId));
        });
        userRepositoryPort.findById(loserId).ifPresent(loser -> {
            usersToSave.add(loser.addCookie(loserCookie));
            historyToSave.add(new UserGoodsHistory(null, null, GoodsActingCategory.BATTLE_LOSE_REWARD, loserCookie, null, loserId));
        });

        userRepositoryPort.saveAll(usersToSave);
        userGoodsHistoryRepositoryPort.saveAll(historyToSave);
    }

    private void grantDrawRewards(int duration, Long firstUserId, Long secondUserId) {
        int winnerCookie = WINNER_COOKIE_BY_DURATION.getOrDefault(duration, 0);
        int drawCookie = (winnerCookie + winnerCookie / 2) / 2;

        List<User> usersToSave = new ArrayList<>();
        List<UserGoodsHistory> historyToSave = new ArrayList<>();

        userRepositoryPort.findById(firstUserId).ifPresent(user -> {
            usersToSave.add(user.addCookie(drawCookie));
            historyToSave.add(new UserGoodsHistory(null, null, GoodsActingCategory.BATTLE_DRAW_REWARD, drawCookie, null, firstUserId));
        });
        userRepositoryPort.findById(secondUserId).ifPresent(user -> {
            usersToSave.add(user.addCookie(drawCookie));
            historyToSave.add(new UserGoodsHistory(null, null, GoodsActingCategory.BATTLE_DRAW_REWARD, drawCookie, null, secondUserId));
        });

        userRepositoryPort.saveAll(usersToSave);
        userGoodsHistoryRepositoryPort.saveAll(historyToSave);
    }
}