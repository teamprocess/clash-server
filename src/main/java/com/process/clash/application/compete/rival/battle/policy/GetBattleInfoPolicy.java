package com.process.clash.application.compete.rival.battle.policy;

import com.process.clash.application.compete.rival.battle.exception.exception.badrequest.NotAcceptedBattleException;
import com.process.clash.application.compete.rival.battle.exception.exception.notfound.BattleNotFoundException;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.battle.enums.BattleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetBattleInfoPolicy {

    private final BattleRepositoryPort battleRepositoryPort;

    public Battle check(Long id) {

        Battle battle = battleRepositoryPort.findById(id)
                .orElseThrow(BattleNotFoundException::new);

        if (battle.battleStatus().equals(BattleStatus.PENDING) || battle.battleStatus().equals(BattleStatus.REJECTED)) {
            throw new NotAcceptedBattleException();
        }

        return battle;
    }
}
