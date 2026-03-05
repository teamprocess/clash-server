package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.data.FindDetailedBattleInfoData;
import com.process.clash.application.compete.rival.battle.policy.GetBattleInfoPolicy;
import com.process.clash.application.compete.rival.battle.port.in.FindDetailedBattleInfoUseCase;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.battle.enums.BattleStatus;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindDetailedBattleInfoService implements FindDetailedBattleInfoUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;
    private final GetBattleInfoPolicy getBattleInfoPolicy;
    private final ZoneId battleZoneId;

    @Override
    public FindDetailedBattleInfoData.Result execute(FindDetailedBattleInfoData.Command command) {

        Battle battle = getBattleInfoPolicy.check(command.id());

        // 상대방이 탈퇴해 라이벌이 삭제된 경우 상대 정보 없이 반환
        if (battle.rivalId() == null) {
            LocalDate endDate = battle.battleStatus().equals(BattleStatus.DONE)
                    ? battle.endDate()
                    : LocalDate.now(battleZoneId);
            double myAverageExp = userExpHistoryRepositoryPort
                    .findAverageExpByUserIdAndPeriod(command.actor().id(), battle.startDate(), endDate);
            double myOverallPercentage = myAverageExp == 0 ? 0 : 100.0;
            return FindDetailedBattleInfoData.Result.of(battle, null, myOverallPercentage, null);
        }

        Rival rival = rivalRepositoryPort.findById(battle.rivalId())
                .orElseThrow(RivalNotFoundException::new);

        Long rivalId = rivalRepositoryPort.findOpponentIdByIdAndUserId(rival.id(), command.actor().id());

        User user = userRepositoryPort.findById(rivalId)
                .orElseThrow(UserNotFoundException::new);

        LocalDate endDate = LocalDate.now(battleZoneId);

        if (battle.battleStatus().equals(BattleStatus.DONE)) {
            endDate = battle.endDate();
        }

        double myAverageExp = userExpHistoryRepositoryPort
                .findAverageExpByUserIdAndPeriod(command.actor().id(), battle.startDate(), endDate);

        double enemyAverageExp = userExpHistoryRepositoryPort
                .findAverageExpByUserIdAndPeriod(rivalId, battle.startDate(), endDate);

        double totalAverageExp = myAverageExp + enemyAverageExp;
        double myOverallPercentage;
        double enemyOverallPercentage;

        if (myAverageExp == 0 && enemyAverageExp == 0) {
            myOverallPercentage = 50;
            enemyOverallPercentage = 50;
        } else {
            myOverallPercentage = (myAverageExp / totalAverageExp) * 100;
            enemyOverallPercentage = (enemyAverageExp / totalAverageExp) * 100;
        }

        return FindDetailedBattleInfoData.Result.of(battle, user, myOverallPercentage, enemyOverallPercentage);
    }
}
