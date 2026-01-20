package com.process.clash.adapter.persistence.rival.battle;

import com.process.clash.adapter.persistence.rival.rival.RivalJpaEntity;
import com.process.clash.adapter.persistence.rival.rival.RivalJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class BattlePersistenceAdapter implements BattleRepositoryPort {

    private final BattleJpaMapper battleJpaMapper;
    private final BattleJpaRepository battleJpaRepository;
    private final RivalJpaRepository rivalJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Battle save(Battle battle) {

        UserJpaEntity winner = userJpaRepository.getReferenceById(battle.winnerId());
        RivalJpaEntity rivalJpaEntity = rivalJpaRepository.getReferenceById(battle.rivalId());
        BattleJpaEntity savedEntity = battleJpaRepository.save(battleJpaMapper.toJpaEntity(battle, winner, rivalJpaEntity));
        return battleJpaMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsActiveBattleByUserId(Long userId) {

        return battleJpaRepository.existsActiveBattleByUserId(userId);
    }
}
