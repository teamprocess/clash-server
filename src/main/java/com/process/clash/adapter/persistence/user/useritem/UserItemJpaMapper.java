package com.process.clash.adapter.persistence.user.useritem;

import com.process.clash.adapter.persistence.shop.product.ProductJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.useritem.entity.UserItem;
import org.springframework.stereotype.Component;

@Component
public class UserItemJpaMapper {

    public UserItemJpaEntity toJpaEntity(UserItem userItem, UserJpaEntity userJpaEntity, ProductJpaEntity productJpaEntity) {
        return new UserItemJpaEntity(
                userItem.id(),
                userItem.createdAt(),
                null,
                userJpaEntity,
                productJpaEntity
        );
    }

    public UserItem toDomain(UserItemJpaEntity userItemJpaEntity) {
        return new UserItem(
                userItemJpaEntity.getId(),
                userItemJpaEntity.getCreatedAt(),
                userItemJpaEntity.getUser().getId(),
                userItemJpaEntity.getProduct().getId()
        );
    }
}
