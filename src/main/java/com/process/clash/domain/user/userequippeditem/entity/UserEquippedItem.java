package com.process.clash.domain.user.userequippeditem.entity;

import com.process.clash.domain.shop.product.enums.ProductCategory;
import java.time.Instant;

public record UserEquippedItem(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        Long userId,
        Long productId,
        ProductCategory category
) {
    public static UserEquippedItem create(Long userId, Long productId, ProductCategory category) {
        return new UserEquippedItem(
                null,
                null,
                null,
                userId,
                productId,
                category
        );
    }

    public UserEquippedItem changeProduct(Long productId) {
        return new UserEquippedItem(
                this.id,
                this.createdAt,
                null,
                this.userId,
                productId,
                this.category
        );
    }
}
