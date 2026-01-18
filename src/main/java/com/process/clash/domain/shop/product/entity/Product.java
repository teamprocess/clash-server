package com.process.clash.domain.shop.product.entity;

import com.process.clash.application.shop.product.exception.exception.badrequest.InvalidDiscountException;
import com.process.clash.application.shop.product.exception.exception.badrequest.InvalidPriceException;
import com.process.clash.application.shop.product.exception.exception.badrequest.InvalidTitleException;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductGoodsType;
import com.process.clash.domain.shop.season.entity.Season;
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
        Long popularity,
        Season season,
        Boolean isSeasonal
) {
    public Product {
        if (title == null || title.isBlank()) {
            throw new InvalidTitleException();
        }
        if (price == null || price < 0) {
            throw new InvalidPriceException();
        }
        if (discount == null || discount < 0 || discount > 100) {
            throw new InvalidDiscountException();
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
            Season season
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
                0L,
                season,
                season != null
        );
    }
}