package com.process.clash.application.compete.rival.battle.policy;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.battle.exception.exception.badrequest.AlreadyInBattleException;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyBattlePolicy {

    private final BattleRepositoryPort battleRepositoryPort;

    public void check(Actor actor) {

        if (battleRepositoryPort.existsActiveBattleByUserId(actor.id())) {
            throw new AlreadyInBattleException();
        }
    }
}
