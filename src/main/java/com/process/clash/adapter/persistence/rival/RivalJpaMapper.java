package com.process.clash.adapter.persistence.rival;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.rival.entity.Rival;
import org.springframework.stereotype.Component;

@Component
public class RivalJpaMapper {

    public RivalJpaEntity toJpaEntity(Rival rival, UserJpaEntity my, UserJpaEntity opponent) {

        return new RivalJpaEntity(
                rival.id(),
                rival.createdAt(),
                rival.updatedAt(),
                rival.rivalStatus(),
                my,
                opponent
        );
    }

    public Rival toDomain(RivalJpaEntity rivalJpaEntity) {

        return new Rival(
                rivalJpaEntity.getId(),
                rivalJpaEntity.getCreatedAt(),
                rivalJpaEntity.getUpdatedAt(),
                rivalJpaEntity.getRivalStatus(),
                rivalJpaEntity.getMy().getId(),
                rivalJpaEntity.getOpponent().getId()
        );
    }
}
