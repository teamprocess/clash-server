package com.process.clash.domain.user.usergoodshistory.entity;

import com.process.clash.domain.common.enums.GoodsActingCategory;

import java.time.Instant;

public record UserGoodsHistory(
        Long id,
        Instant createdAt,
        GoodsActingCategory goodsActingCategory,
        int variation,
        Long productId,
        Long userId
) {}
