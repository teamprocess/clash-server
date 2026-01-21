package com.process.clash.adapter.persistence.user.userexphistory;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;
import org.springframework.stereotype.Component;

@Component
public class UserExpHistoryJpaMapper {

    public UserExpHistoryJpaEntity toJpaEntity(UserExpHistory userExpHistory, UserJpaEntity userJpaEntity) {

        return new UserExpHistoryJpaEntity(
                userExpHistory.id(),
                userExpHistory.createdAt(),
                userExpHistory.date(),
                userExpHistory.earnExp(),
                userExpHistory.actingCategory(),
                userJpaEntity
        );
    }

    public UserExpHistory toDomain(UserExpHistoryJpaEntity userExpHistoryJpaEntity) {

        return new UserExpHistory(
                userExpHistoryJpaEntity.getId(),
                userExpHistoryJpaEntity.getCreatedAt(),
                userExpHistoryJpaEntity.getDate(),
                userExpHistoryJpaEntity.getEarnExp(),
                userExpHistoryJpaEntity.getActingCategory(),
                userExpHistoryJpaEntity.getUser().getId()
        );
    }
}
