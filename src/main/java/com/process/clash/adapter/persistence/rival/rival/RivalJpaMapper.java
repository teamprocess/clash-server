package com.process.clash.adapter.persistence.rival.rival;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.rival.rival.entity.Rival;
import org.springframework.stereotype.Component;

@Component
public class RivalJpaMapper {

    public RivalJpaEntity toJpaEntity(Rival rival, UserJpaEntity firstUser, UserJpaEntity secondUser) {

        return new RivalJpaEntity(
                rival.id(),
                rival.createdAt(),
                rival.updatedAt(),
                rival.rivalLinkingStatus(),
                firstUser,
                secondUser
        );
    }

    public Rival toDomain(RivalJpaEntity rivalJpaEntity) {

        return new Rival(
                rivalJpaEntity.getId(),
                rivalJpaEntity.getCreatedAt(),
                rivalJpaEntity.getUpdatedAt(),
                rivalJpaEntity.getRivalLinkingStatus(),
                rivalJpaEntity.getFirstUser().getId(),
                rivalJpaEntity.getSecondUser().getId()
        );
    }
}
