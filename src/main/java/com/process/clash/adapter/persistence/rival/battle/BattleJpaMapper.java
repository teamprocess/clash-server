package com.process.clash.adapter.persistence.rival.battle;

import com.process.clash.adapter.persistence.rival.rival.RivalJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.rival.battle.entity.Battle;
import org.springframework.stereotype.Component;

@Component
public class BattleJpaMapper {

    public BattleJpaEntity toJpaEntity(Battle battle, UserJpaEntity winner, UserJpaEntity applicant, RivalJpaEntity rivalJpaEntity) {

        return new BattleJpaEntity(
                battle.id(),
                battle.createdAt(),
                battle.updatedAt(),
                battle.startedAt(),
                battle.endAt(),
                battle.duration(),
                battle.battleStatus(),
                winner,
                rivalJpaEntity,
                applicant
        );
    }

    public Battle toDomain(BattleJpaEntity battleJpaEntity) {

        return new Battle(
                battleJpaEntity.getId(),
                battleJpaEntity.getCreatedAt(),
                battleJpaEntity.getUpdatedAt(),
                battleJpaEntity.getStartedAt(),
                battleJpaEntity.getEndAt(),
                battleJpaEntity.getDuration(),
                battleJpaEntity.getBattleStatus(),
                battleJpaEntity.getWinner() != null ? battleJpaEntity.getWinner().getId() : null,
                battleJpaEntity.getRival() != null ? battleJpaEntity.getRival().getId() : null,
                battleJpaEntity.getApplicant() != null ? battleJpaEntity.getApplicant().getId() : null
        );
    }
}