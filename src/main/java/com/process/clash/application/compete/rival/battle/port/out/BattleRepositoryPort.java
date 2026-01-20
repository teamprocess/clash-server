package com.process.clash.application.compete.rival.battle.port.out;

import com.process.clash.domain.rival.battle.entity.Battle;


public interface BattleRepositoryPort {

    Battle save(Battle battle);
    boolean existsActiveBattleByUserId(Long userId);
}
