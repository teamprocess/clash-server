package com.process.clash.application.compete.rival.battle.port.out;

import com.process.clash.domain.rival.battle.entity.Battle;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface BattleRepositoryPort {

    Battle save(Battle battle);
    List<Battle> saveAll(List<Battle> battles);
    Optional<Battle> findById(Long id);
    boolean existsActiveBattleByUserId(Long userId);
    boolean existsActiveBattleByRivalId(Long rivalId);
    List<Battle> findByUserIdWithOutRejected(Long userId);
    Optional<Battle> findActiveByUserId(Long userId);
    boolean existsPendingBattleByRivalId(Long rivalId);
    void rejectAllActiveBattlesByUserId(Long userId);
    List<Battle> findExpiredInProgressBattles(LocalDate today);
}
