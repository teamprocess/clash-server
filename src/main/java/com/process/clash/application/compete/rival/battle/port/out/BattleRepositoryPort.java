package com.process.clash.application.compete.rival.battle.port.out;

import com.process.clash.domain.rival.battle.entity.Battle;

import java.util.List;
import java.util.Optional;


public interface BattleRepositoryPort {

    Battle save(Battle battle);
    Optional<Battle> findById(Long id);
    boolean existsActiveBattleByUserId(Long userId);
    List<Battle> findByUserId(Long userId);
    Optional<Battle> findActiveByUserId(Long userId);
}
