package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.data.AnalyzeBattleInfoData;
import com.process.clash.application.compete.rival.battle.exception.exception.notfound.BattleNotFoundException;
import com.process.clash.application.compete.rival.battle.port.in.AnalyzeBattleInfoUseCase;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.github.port.out.GitHubDailyStatsQueryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import com.process.clash.domain.common.enums.TargetCategory;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyzeBattleInfoService implements AnalyzeBattleInfoUseCase {

    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;
    private final GitHubDailyStatsQueryPort githubDailyStatsQueryPort;
    private final BattleRepositoryPort battleRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;

    @Override
    public AnalyzeBattleInfoData.Result execute(AnalyzeBattleInfoData.Command command) {
        Long userId = command.actor().id();
        TargetCategory category = command.category();

        Battle battle = battleRepositoryPort.findById(command.id())
                .orElseThrow(BattleNotFoundException::new);

        // Rival 정보 조회
        Rival rival = rivalRepositoryPort.findById(battle.rivalId())
                .orElseThrow(RivalNotFoundException::new);

        // 상대방 ID 추출
        Long enemyId = rival.firstUserId().equals(userId)
                ? rival.secondUserId()
                : rival.firstUserId();

        // 카테고리별 점수 계산
        Integer myPoint = calculatePointByCategory(
                userId, category, battle.startDate(), battle.endDate()
        );
        Integer enemyPoint = calculatePointByCategory(
                enemyId, category, battle.startDate(), battle.endDate()
        );

        return AnalyzeBattleInfoData.Result.of(category, battle.id(), enemyPoint, myPoint);
    }

    private Integer calculatePointByCategory(
            Long userId,
            TargetCategory category,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return switch (category) {
            case ACTIVE_TIME -> calculateStudyPoint(userId, startDate, endDate);
            case GITHUB -> calculateGithubPoint(userId, startDate, endDate);
            case EXP -> calculateExpPoint(userId, startDate, endDate);
            case SOLVED_AC -> 0; //TODO: 나중에 구현
        };
    }

    private Integer calculateStudyPoint(Long userId, LocalDate startDate, LocalDate endDate) {
        Double avgStudyTime = userStudyTimeRepositoryPort
                .findAverageStudyTimeByUserIdAndPeriod(userId, startDate, endDate);
        return avgStudyTime != null ? (int) Math.round(avgStudyTime) : 0;
    }

    private Integer calculateGithubPoint(Long userId, LocalDate startDate, LocalDate endDate) {
        Double avgCommits = githubDailyStatsQueryPort
                .findAverageContributionByUserIdAndPeriod(userId, startDate, endDate);
        return avgCommits != null ? (int) Math.round(avgCommits) : 0;
    }

    private Integer calculateExpPoint(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Double avgExp = userExpHistoryRepositoryPort
                .findAverageExpByUserIdAndCategoryAndPeriod(
                        userId, startDate, endDate
                );
        return avgExp != null ? (int) Math.round(avgExp) : 0;
    }
}