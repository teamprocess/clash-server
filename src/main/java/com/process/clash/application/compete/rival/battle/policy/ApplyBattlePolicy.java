package com.process.clash.application.compete.rival.battle.policy;

import com.process.clash.application.compete.rival.battle.exception.exception.conflict.AlreadyAppliedBattleException;
import com.process.clash.application.compete.rival.battle.exception.exception.conflict.AlreadyInBattleException;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplyBattlePolicy {

    private final BattleRepositoryPort battleRepositoryPort;

    public void check(Long id) {

        if (battleRepositoryPort.existsPendingBattleByRivalId(id)) {
            throw new AlreadyAppliedBattleException();
        }

        if (battleRepositoryPort.existsActiveBattleByRivalId(id)) {
            throw new AlreadyInBattleException();
        }
    }
}
