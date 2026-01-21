package com.process.clash.adapter.persistence.rival.battle;

import com.process.clash.adapter.persistence.rival.rival.RivalJpaEntity;
import com.process.clash.adapter.persistence.rival.rival.RivalJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.compete.rival.battle.port.out.BattleRepositoryPort;
import com.process.clash.domain.rival.battle.entity.Battle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BattlePersistenceAdapter implements BattleRepositoryPort {

    private final BattleJpaMapper battleJpaMapper;
    private final BattleJpaRepository battleJpaRepository;
    private final RivalJpaRepository rivalJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Battle save(Battle battle) {

        UserJpaEntity winner = battle.winnerId() != null ? userJpaRepository.getReferenceById(battle.winnerId()) : null;
        RivalJpaEntity rivalJpaEntity = rivalJpaRepository.getReferenceById(battle.rivalId());
        BattleJpaEntity savedEntity = battleJpaRepository.save(battleJpaMapper.toJpaEntity(battle, winner, rivalJpaEntity));
        return battleJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Battle> findById(Long id) {

        return battleJpaRepository.findById(id)
                .map(battleJpaMapper::toDomain);
    }

    @Override
    public boolean existsActiveBattleByUserId(Long userId) {

        return battleJpaRepository.existsActiveBattleByUserId(userId);
    }

    @Override
    public boolean existsActiveBattleByRivalId(Long rivalId) {

        return battleJpaRepository.existsActiveBattleByRivalId(rivalId);
    }

    @Override
    public List<Battle> findByUserIdWithOutRejected(Long userId) {

        return battleJpaRepository.findByUserIdWithOutRejected(userId)
                .stream()
                .map(battleJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Battle> findActiveByUserId(Long userId) {

        return battleJpaRepository.findActiveByUserId(userId)
                .map(battleJpaMapper::toDomain);
    }
}
