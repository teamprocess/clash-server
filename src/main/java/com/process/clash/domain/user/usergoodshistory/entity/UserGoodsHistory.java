package com.process.clash.domain.user.usergoodshistory.entity;

import com.process.clash.domain.common.enums.GoodsActingCategory;
import com.process.clash.domain.common.enums.GoodsType;

import java.time.LocalDateTime;

public record UserGoodsHistory(
        Long id,
        LocalDateTime createdAt,
        GoodsType goodsType,
        GoodsActingCategory goodsActingCategory,
        int variation,
        Long productId,
        Long userId
) {}
