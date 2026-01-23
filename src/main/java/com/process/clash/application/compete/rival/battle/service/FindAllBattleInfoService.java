package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.data.FindAllBattleInfoData.*;
import com.process.clash.application.compete.rival.battle.exception.exception.notfound.BattleNotFoundException;
import com.process.clash.application.compete.rival.battle.port.in.FindAllBattleInfoUseCase;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.battle.enums.BattleStatus;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAllBattleInfoService implements FindAllBattleInfoUseCase {

    private static final String RESULT_WON = "WON";
    private static final String RESULT_LOST = "LOST";
    private static final String RESULT_DRAW = "DRAW";
    private static final String RESULT_WINNING = "WINNING";
    private static final String RESULT_LOSING = "LOSING";
    private static final String RESULT_PENDING = "PENDING";

    private final BattleRepositoryPort battleRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;

    @Override
    public Result execute(Command command) {
        Long userId = command.actor().id();

        List<Battle> battles = battleRepositoryPort.findByUserIdWithOutRejected(userId);

        if (battles.isEmpty()) {
            return Result.from(Collections.emptyList());
        }

        Set<Long> rivalIds = battles.stream()
                .map(Battle::rivalId)
                .collect(Collectors.toSet());

        Map<Long, Rival> rivalMap = rivalRepositoryPort.findByIdIn(rivalIds).stream()
                .collect(Collectors.toMap(Rival::id, rival -> rival));

        Set<Long> enemyUserIds = battles.stream()
                .map(battle -> {
                    Rival rival = rivalMap.get(battle.rivalId());
                    if (rival == null) {
                        throw new RivalNotFoundException();
                    }
                    return getEnemyId(rival, userId);
                })
                .collect(Collectors.toSet());

        Map<Long, User> userMap = userRepositoryPort.findByIdIn(enemyUserIds).stream()
                .collect(Collectors.toMap(User::id, user -> user));

        List<Battle> inProgressBattles = battles.stream()
                .filter(battle -> battle.battleStatus() == BattleStatus.IN_PROGRESS)
                .collect(Collectors.toList());

        Map<Long, Double> currentUserAvgExpMap;
        Map<Long, Double> enemyAvgExpMap = new HashMap<>();

        if (!inProgressBattles.isEmpty()) {
            currentUserAvgExpMap = userExpHistoryRepositoryPort
                    .findAverageExpForBattles(userId, inProgressBattles);

            Set<Long> enemyIdsForInProgress = inProgressBattles.stream()
                    .map(battle -> getEnemyId(rivalMap.get(battle.rivalId()), userId))
                    .collect(Collectors.toSet());

            Map<Long, List<Battle>> battlesByEnemyId = new HashMap<>();
            for (Battle battle : inProgressBattles) {
                Long enemyId = getEnemyId(rivalMap.get(battle.rivalId()), userId);
                battlesByEnemyId.computeIfAbsent(enemyId, k -> new ArrayList<>()).add(battle);
            }

            for (Map.Entry<Long, List<Battle>> entry : battlesByEnemyId.entrySet()) {
                Long enemyId = entry.getKey();
                List<Battle> enemyBattles = entry.getValue();

                Map<Long, Double> enemyAvgForBattle = userExpHistoryRepositoryPort
                        .findAverageExpForBattles(enemyId, enemyBattles);
                enemyAvgExpMap.putAll(enemyAvgForBattle);
            }
        } else {
            currentUserAvgExpMap = new HashMap<>();
        }

        List<BattleInfo> battleInfos = battles.stream()
                .map(battle -> toBattleInfo(
                        battle,
                        userId,
                        rivalMap,
                        userMap,
                        currentUserAvgExpMap,
                        enemyAvgExpMap
                ))
                .collect(Collectors.toList());

        return Result.from(battleInfos);
    }

    private BattleInfo toBattleInfo(
            Battle battle,
            Long currentUserId,
            Map<Long, Rival> rivalMap,
            Map<Long, User> userMap,
            Map<Long, Double> currentUserAvgExpMap,
            Map<Long, Double> enemyAvgExpMap
    ) {
        // Rival 조회
        Rival rival = rivalMap.get(battle.rivalId());
        if (rival == null) {
            throw new RivalNotFoundException();
        }

        // 상대방 ID 추출
        Long enemyId = getEnemyId(rival, currentUserId);

        // 상대방 User 조회
        User enemyUser = userMap.get(enemyId);
        if (enemyUser == null) {
            throw new RivalNotFoundException();
        }

        Enemy enemy = Enemy.of(
                enemyUser.id(),
                enemyUser.name(),
                enemyUser.profileImage()
        );

        // 결과 판단
        String result = determineResult(
                battle,
                currentUserId,
                enemyId,
                currentUserAvgExpMap,
                enemyAvgExpMap
        );

        return BattleInfo.of(
                battle.id(),
                enemy,
                battle.endDate(),
                result
        );
    }

    private String determineResult(
            Battle battle,
            Long currentUserId,
            Long enemyId,
            Map<Long, Double> currentUserAvgExpMap,
            Map<Long, Double> enemyAvgExpMap
    ) {
        BattleStatus status = battle.battleStatus();

        // 배틀이 종료된 경우
        if (status == BattleStatus.DONE) {
            Long winnerId = battle.winnerId();
            if (winnerId == null) {
                return RESULT_DRAW;
            }
            return winnerId.equals(currentUserId) ? RESULT_WON : RESULT_LOST;
        }

        // 배틀이 진행 중인 경우
        if (status == BattleStatus.IN_PROGRESS) {
            Double currentUserAvgExp = currentUserAvgExpMap.getOrDefault(battle.id(), 0.0);
            Double enemyAvgExp = enemyAvgExpMap.getOrDefault(battle.id(), 0.0);

            if (currentUserAvgExp > enemyAvgExp) {
                return RESULT_WINNING;
            } else if (currentUserAvgExp < enemyAvgExp) {
                return RESULT_LOSING;
            } else {
                return RESULT_DRAW;
            }
        }

        return RESULT_PENDING;
    }

    private Long getEnemyId(Rival rival, Long currentUserId) {
        return rival.firstUserId().equals(currentUserId)
                ? rival.secondUserId()
                : rival.firstUserId();
    }
}