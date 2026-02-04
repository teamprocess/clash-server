package com.process.clash.domain.shop.product.enums;

import com.process.clash.domain.common.enums.GoodsType;

public enum ProductGoodsType {
    COOKIE,
    TOKEN;

    public GoodsType toGoodsType() {
        return switch (this) {
            case COOKIE -> GoodsType.COOKIE;
            case TOKEN -> GoodsType.TOKEN;
        };
    }
}
