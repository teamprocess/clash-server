package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.data.FindAllBattleInfoData.*;
import com.process.clash.application.compete.rival.battle.exception.exception.notfound.BattleNotFoundException;
import com.process.clash.application.compete.rival.battle.port.in.FindAllBattleInfoUseCase;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAllBattleInfoService implements FindAllBattleInfoUseCase {

    private final BattleRepositoryPort battleRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;

    @Override
    public Result execute(Command command) {
        Long userId = command.actor().id();

        List<Battle> battles = battleRepositoryPort.findByUserId(userId);

        List<BattleInfo> battleInfos = battles.stream()
                .map(battle -> toBattleInfo(battle, userId))
                .collect(Collectors.toList());

        return Result.from(battleInfos);
    }

    private BattleInfo toBattleInfo(Battle battle, Long currentUserId) {
        // Rival 도메인 객체 조회
        Rival rival = rivalRepositoryPort.findById(battle.rivalId())
                .orElseThrow(() -> new IllegalStateException("Rival not found"));

        // 상대방 ID 추출
        Long enemyId = getEnemyId(rival, currentUserId);

        // 상대방 User 도메인 객체 조회
        User enemyUser = userRepositoryPort.findById(enemyId)
                .orElseThrow(() -> new IllegalStateException("Enemy user not found"));

        Enemy enemy = Enemy.of(
                enemyUser.id(),
                enemyUser.name(),
                enemyUser.profileImage()
        );

        // 결과 판단
        String result = determineResult(battle, currentUserId, enemyId);

        return BattleInfo.of(
                battle.id(),
                enemy,
                battle.endDate(),
                result
        );
    }

    private String determineResult(Battle battle, Long currentUserId, Long enemyId) {
        BattleStatus status = battle.battleStatus();

        // 배틀이 종료된 경우
        if (status == BattleStatus.DONE) {
            Long winnerId = battle.winnerId();
            if (winnerId == null) {
                return "DRAW";
            }
            return winnerId.equals(currentUserId) ? "WON" : "LOST";
        }

        // 배틀이 진행 중인 경우
        if (status == BattleStatus.IN_PROGRESS) {
            Double currentUserAvgExp = userExpHistoryRepositoryPort
                    .findAverageExpByUserIdAndCategoryAndPeriod(
                            currentUserId,
                            battle.startDate(),
                            battle.endDate()
                    );

            Double enemyAvgExp = userExpHistoryRepositoryPort
                    .findAverageExpByUserIdAndCategoryAndPeriod(
                            enemyId,
                            battle.startDate(),
                            battle.endDate()
                    );

            double currentAvg = currentUserAvgExp != null ? currentUserAvgExp : 0.0;
            double enemyAvg = enemyAvgExp != null ? enemyAvgExp : 0.0;

            if (currentAvg > enemyAvg) {
                return "WINNING";
            } else if (currentAvg < enemyAvg) {
                return "LOSING";
            } else {
                return "DRAW";
            }
        }

        return "PENDING";
    }

    private Long getEnemyId(Rival rival, Long currentUserId) {
        return rival.firstUserId().equals(currentUserId)
                ? rival.secondUserId()
                : rival.firstUserId();
    }
}