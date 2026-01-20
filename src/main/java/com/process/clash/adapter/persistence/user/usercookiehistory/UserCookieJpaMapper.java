package com.process.clash.adapter.persistence.user.usercookiehistory;

import com.process.clash.adapter.persistence.shop.product.ProductJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.usercookiehistory.entity.UserCookieHistory;
import org.springframework.stereotype.Component;

@Component
public class UserCookieJpaMapper {

    public UserCookieHistoryJpaEntity toJpaEntity(UserCookieHistory userCookieHistory, UserJpaEntity userJpaEntity, ProductJpaEntity productJpaEntity) {

        return new UserCookieHistoryJpaEntity(
                userCookieHistory.id(),
                userCookieHistory.createdAt(),
                userCookieHistory.actingCategory(),
                userCookieHistory.variation(),
                productJpaEntity,
                userJpaEntity
        );
    }

    public UserCookieHistory toDomain(UserCookieHistoryJpaEntity userCookieHistoryJpaEntity) {

        return new UserCookieHistory(
                userCookieHistoryJpaEntity.getId(),
                userCookieHistoryJpaEntity.getCreatedAt(),
                userCookieHistoryJpaEntity.getActingCategory(),
                userCookieHistoryJpaEntity.getVariation(),
                userCookieHistoryJpaEntity.getProduct().getId(),
                userCookieHistoryJpaEntity.getUser().getId()
        );
    }
}
