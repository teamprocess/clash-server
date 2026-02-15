package com.process.clash.application.shop.product.vo;

import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductGoodsType;

import java.time.Instant;

public record ProductVo(
        Long id,
        String title,
        ProductCategory category,
        String image,
        ProductGoodsType type,
        Long price,
        Integer discount,
        String description,
        Long popularity,
        String seasonName,
        Boolean isSeasonal,
        Boolean isBought,
        Instant createdAt
) {
    public static ProductVo from(Product domain) {
        return from(domain, false);
    }

    public static ProductVo from(Product domain, boolean isBought) {
        String seasonName = domain.season() != null ? domain.season().name() : null;

        return new ProductVo(
                domain.id(),
                domain.title(),
                domain.category(),
                domain.image(),
                domain.type(),
                domain.price(),
                domain.discount(),
                domain.description(),
                domain.popularity(),
                seasonName,
                domain.isSeasonal(),
                isBought,
                domain.createdAt()
        );
    }
}
