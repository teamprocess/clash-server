package com.process.clash.application.compete.rival.battle.policy;

import com.process.clash.application.compete.rival.battle.exception.exception.badrequest.AlreadyInBattleException;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyBattlePolicy {

    private final BattleRepositoryPort battleRepositoryPort;
    private final RivalRepositoryPort rivalRepositoryPort;

    public void check(Long id) {

        Rival rival = rivalRepositoryPort.findById(id)
                .orElseThrow(RivalNotFoundException::new);

        if (battleRepositoryPort.existsActiveBattleByRivalId(rival.id())) {
            throw new AlreadyInBattleException();
        }
    }
}
