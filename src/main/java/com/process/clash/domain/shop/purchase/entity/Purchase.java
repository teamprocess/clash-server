package com.process.clash.domain.shop.purchase.entity;

import com.process.clash.domain.common.enums.GoodsType;
import java.time.Instant;

public record Purchase(
        Long id,
        Instant createdAt,
        GoodsType goodsType,
        Long price,
        Long productId,
        Long userId
) {
    public static Purchase create(
            GoodsType goodsType,
            Long price,
            Long productId,
            Long userId
    )
    {
        return new Purchase(null, null, goodsType, price, productId, userId);
    }
}
