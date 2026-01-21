package com.process.clash.application.compete.rival.battle.policy;

import com.process.clash.application.compete.rival.battle.exception.exception.badrequest.AlreadyInBattleException;
import com.process.clash.application.compete.rival.battle.exception.exception.notfound.BattleNotFoundException;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyBattlePolicy {

    private final BattleRepositoryPort battleRepositoryPort;

    public void check(Long id) {

        Battle battle = battleRepositoryPort.findById(id)
                .orElseThrow(BattleNotFoundException::new);

        if (battleRepositoryPort.existsActiveBattleByRivalId(battle.rivalId())) {
            throw new AlreadyInBattleException();
        }
    }
}
