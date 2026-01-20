package com.process.clash.application.compete.rival.battle.service;

import com.process.clash.application.compete.rival.battle.data.FindDetailedBattleInfoData;
import com.process.clash.application.compete.rival.battle.exception.exception.notfound.BattleNotFoundException;
import com.process.clash.application.compete.rival.battle.port.in.FindDetailedBattleInfoUseCase;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindDetailedBattleInfoService implements FindDetailedBattleInfoUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final BattleRepositoryPort battleRepositoryPort;
    private final UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;

    @Override
    public FindDetailedBattleInfoData.Result execute(FindDetailedBattleInfoData.Command command) {

        Battle battle = battleRepositoryPort.findById(command.id())
                .orElseThrow(BattleNotFoundException::new);

        Rival rival = rivalRepositoryPort.findById(battle.rivalId())
                .orElseThrow(RivalNotFoundException::new);

        Long rivalId = rivalRepositoryPort.findOpponentIdByIdAndUserId(rival.id(), command.actor().id());

        User user = userRepositoryPort.findById(rivalId)
                .orElseThrow(UserNotFoundException::new);

        LocalDate endDate = LocalDate.now();

        if (battle.battleStatus().equals(BattleStatus.DONE)) {
            endDate = battle.endDate();
        }

        double myAverageExp = userExpHistoryRepositoryPort
                .findAverageExpByUserIdAndCategoryAndPeriod(command.actor().id(), battle.startDate(), endDate);

        double enemyAverageExp = userExpHistoryRepositoryPort
                .findAverageExpByUserIdAndCategoryAndPeriod(rivalId, battle.startDate(), endDate);

        double myOverallPercentage = myAverageExp / (myAverageExp + enemyAverageExp) * 100;

        return FindDetailedBattleInfoData.Result.from(battle, user, myOverallPercentage);
    }
}
