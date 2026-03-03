package com.process.clash.adapter.persistence.rival.battle;

import com.process.clash.adapter.persistence.rival.rival.RivalJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.rival.battle.entity.Battle;
import com.process.clash.domain.rival.rival.entity.Rival;
import org.springframework.stereotype.Component;

@Component
public class BattleJpaMapper {

    public BattleJpaEntity toJpaEntity(Battle battle, UserJpaEntity userJpaEntity, RivalJpaEntity rivalJpaEntity) {

        return new BattleJpaEntity(
                battle.id(),
                battle.createdAt(),
                battle.updatedAt(),
                battle.startDate(),
                battle.endDate(),
                battle.battleStatus(),
                userJpaEntity,
                rivalJpaEntity
        );
    }

    public Battle toDomain(BattleJpaEntity battleJpaEntity) {

        return new Battle(
                battleJpaEntity.getId(),
                battleJpaEntity.getCreatedAt(),
                battleJpaEntity.getUpdatedAt(),
                battleJpaEntity.getStartDate(),
                battleJpaEntity.getEndDate(),
                battleJpaEntity.getBattleStatus(),
                battleJpaEntity.getWinner() != null ? battleJpaEntity.getWinner().getId() : null,
                battleJpaEntity.getRival() != null ? battleJpaEntity.getRival().getId() : null
        );
    }
}
