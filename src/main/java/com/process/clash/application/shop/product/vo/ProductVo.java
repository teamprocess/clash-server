package com.process.clash.application.shop.product.vo;

import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductGoodsType;

import java.time.LocalDateTime;

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
        String season,
        Boolean isSeasonal,
        LocalDateTime createdAt
) {
    public static ProductVo from(Product domain) {
        String seasonTitle = domain.season() != null ? domain.season().title() : null;

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
                seasonTitle,
                domain.isSeasonal(),
                domain.createdAt()
        );
    }
}
