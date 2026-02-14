package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.shop.product.vo.ProductVo;
import java.time.Instant;

public record CommonProductDto(
        Long id,
        String title,
        String category,
        String image,
        String type,
        Long price,
        Integer discount,
        String description,
        Long popularity,
        String seasonName,
        Boolean isSeasonal,
        Boolean isBought,
        Instant createdAt
) {
    public static CommonProductDto from(ProductVo product) {
        return new CommonProductDto(
                product.id(),
                product.title(),
                product.category().name(),
                product.image(),
                product.type().name(),
                product.price(),
                product.discount(),
                product.description(),
                product.popularity(),
                product.seasonName(),
                product.isSeasonal(),
                product.isBought(),
                product.createdAt()
        );
    }
}