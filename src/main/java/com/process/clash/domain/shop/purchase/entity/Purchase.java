package com.process.clash.domain.shop.purchase.entity;

import java.time.Instant;

public record Purchase(
        Long id,
        Instant createdAt,
        Long price,
        Long productId,
        Long userId
) {
    public static Purchase create(
            Long price,
            Long productId,
            Long userId
    )
    {
        return new Purchase(null, null, price, productId, userId);
    }
}
