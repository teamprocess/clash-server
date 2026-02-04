package com.process.clash.adapter.persistence.shop.purchase;

import com.process.clash.adapter.persistence.shop.product.ProductJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.shop.purchase.entity.Purchase;
import org.springframework.stereotype.Component;

@Component
public class PurchaseJpaMapper {

    public PurchaseJpaEntity toJpaEntity(Purchase purchase, UserJpaEntity userJpaEntity, ProductJpaEntity productJpaEntity) {
        return new PurchaseJpaEntity(
                purchase.id(),
                purchase.createdAt(),
                null,
                purchase.goodsType(),
                purchase.price(),
                productJpaEntity,
                userJpaEntity
        );
    }

    public Purchase toDomain(PurchaseJpaEntity purchaseJpaEntity) {
        return new Purchase(
                purchaseJpaEntity.getId(),
                purchaseJpaEntity.getCreatedAt(),
                purchaseJpaEntity.getGoodsType(),
                purchaseJpaEntity.getPrice(),
                purchaseJpaEntity.getProduct().getId(),
                purchaseJpaEntity.getUser().getId()
        );
    }
}
