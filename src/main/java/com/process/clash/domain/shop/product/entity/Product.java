package com.process.clash.domain.shop.product.entity;

import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductGoodsType;
import java.time.LocalDateTime;

public record Product(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String title,
        ProductCategory category,
        String image,
        ProductGoodsType type,
        Long price,
        Integer discount,
        String description,
        Integer popularity,
        Long seasonId,
        Boolean isSeasonal
) {
    public Product {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("상품 제목은 필수입니다.");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
        if (discount == null || discount < 0 || discount > 100) {
            throw new IllegalArgumentException("할인율은 0~100 사이여야 합니다.");
        }
    }

    public static Product create(
            String title,
            ProductCategory category,
            String image,
            ProductGoodsType type,
            Long price,
            Integer discount,
            String description,
            Long seasonId
    ) {
        return new Product(
                null,
                null,
                null,
                title,
                category,
                image,
                type,
                price,
                discount != null ? discount : 0,
                description,
                0,
                seasonId,
                false
        );
    }
}