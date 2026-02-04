package com.process.clash.domain.shop.purchase.entity;

import com.process.clash.domain.common.enums.GoodsType;
import java.time.LocalDateTime;

public record Purchase(
        Long id,
        LocalDateTime createdAt,
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
