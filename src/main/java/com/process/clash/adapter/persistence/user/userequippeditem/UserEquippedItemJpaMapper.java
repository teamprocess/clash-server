package com.process.clash.adapter.persistence.user.userequippeditem;

import com.process.clash.adapter.persistence.shop.product.ProductJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.userequippeditem.entity.UserEquippedItem;
import org.springframework.stereotype.Component;

@Component
public class UserEquippedItemJpaMapper {

    public UserEquippedItemJpaEntity toJpaEntity(
            UserEquippedItem userEquippedItem,
            UserJpaEntity userJpaEntity,
            ProductJpaEntity productJpaEntity
    ) {
        return new UserEquippedItemJpaEntity(
                userEquippedItem.id(),
                userEquippedItem.createdAt(),
                userEquippedItem.updatedAt(),
                userJpaEntity,
                productJpaEntity,
                userEquippedItem.category()
        );
    }

    public UserEquippedItem toDomain(UserEquippedItemJpaEntity userEquippedItemJpaEntity) {
        return new UserEquippedItem(
                userEquippedItemJpaEntity.getId(),
                userEquippedItemJpaEntity.getCreatedAt(),
                userEquippedItemJpaEntity.getUpdatedAt(),
                userEquippedItemJpaEntity.getUser().getId(),
                userEquippedItemJpaEntity.getProduct().getId(),
                userEquippedItemJpaEntity.getCategory()
        );
    }
}
