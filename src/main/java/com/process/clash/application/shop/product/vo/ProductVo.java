package com.process.clash.application.shop.product.vo;

import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import java.time.Instant;

public record ProductVo(
        Long id,
        String title,
        ProductCategory category,
        String image,
        String audio,
        Long price,
        Integer discount,
        String description,
        Long popularity,
        String seasonName,
        Boolean isSeasonal,
        Boolean isAblePurchase,
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
                domain.audio(),
                domain.price(),
                domain.discount(),
                domain.description(),
                domain.popularity(),
                seasonName,
                domain.isSeasonal(),
                domain.isAblePurchase(),
                isBought,
                domain.createdAt()
        );
    }
}
