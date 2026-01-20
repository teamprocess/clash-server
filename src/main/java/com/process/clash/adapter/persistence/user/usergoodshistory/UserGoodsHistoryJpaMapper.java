package com.process.clash.adapter.persistence.user.usergoodshistory;

import com.process.clash.adapter.persistence.shop.product.ProductJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;
import org.springframework.stereotype.Component;

@Component
public class UserGoodsHistoryJpaMapper {

    public UserGoodsHistoryJpaEntity toJpaEntity(UserGoodsHistory userGoodsHistory, UserJpaEntity userJpaEntity, ProductJpaEntity productJpaEntity) {

        return new UserGoodsHistoryJpaEntity(
                userGoodsHistory.id(),
                userGoodsHistory.createdAt(),
                userGoodsHistory.goodsType(),
                userGoodsHistory.actingCategory(),
                userGoodsHistory.variation(),
                productJpaEntity,
                userJpaEntity
        );
    }

    public UserGoodsHistory toDomain(UserGoodsHistoryJpaEntity userGoodsHistoryJpaEntity) {

        return new UserGoodsHistory(
                userGoodsHistoryJpaEntity.getId(),
                userGoodsHistoryJpaEntity.getCreatedAt(),
                userGoodsHistoryJpaEntity.getGoodsType(),
                userGoodsHistoryJpaEntity.getActingCategory(),
                userGoodsHistoryJpaEntity.getVariation(),
                userGoodsHistoryJpaEntity.getProduct() != null ? userGoodsHistoryJpaEntity.getProduct().getId() : null,
                userGoodsHistoryJpaEntity.getUser().getId()
        );
    }
}
