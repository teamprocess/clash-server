package com.process.clash.adapter.persistence.user.userrankhistory;

import com.process.clash.adapter.persistence.shop.season.SeasonJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.userrankhistory.entity.UserRankHistory;
import org.springframework.stereotype.Component;

@Component
public class UserRankHistoryJpaMapper {

    public UserRankHistoryJpaEntity toJpaEntity(UserRankHistory userRankHistory, UserJpaEntity userJpaEntity, SeasonJpaEntity seasonJpaEntity) {

        return new UserRankHistoryJpaEntity(
                userRankHistory.id(),
                userRankHistory.createdAt(),
                userRankHistory.date(),
                userRankHistory.rank(),
                userRankHistory.expTier(),
                userRankHistory.rankTier(),
                userJpaEntity,
                seasonJpaEntity
        );
    }

    public UserRankHistory toDomain(UserRankHistoryJpaEntity userRankHistoryJpaEntity) {

        return new UserRankHistory(
                userRankHistoryJpaEntity.getId(),
                userRankHistoryJpaEntity.getCreatedAt(),
                userRankHistoryJpaEntity.getDate(),
                userRankHistoryJpaEntity.getRank(),
                userRankHistoryJpaEntity.getExpTier(),
                userRankHistoryJpaEntity.getRankTier(),
                userRankHistoryJpaEntity.getUser().getId(),
                userRankHistoryJpaEntity.getSeason().getId()
        );
    }
}
