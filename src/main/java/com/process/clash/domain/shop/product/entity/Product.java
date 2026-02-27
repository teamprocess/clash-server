package com.process.clash.domain.shop.product.entity;

import com.process.clash.application.shop.product.exception.exception.badrequest.InvalidDiscountException;
import com.process.clash.application.shop.product.exception.exception.badrequest.InvalidPriceException;
import com.process.clash.application.shop.product.exception.exception.badrequest.InvalidTitleException;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.season.entity.Season;
import java.time.Instant;

public record Product(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        String title,
        ProductCategory category,
        String image,
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
                price,
                discount != null ? discount : 0,
                description,
                0L,
                season,
                season != null
        );
    }

    public Product increasePopularity() {
        long nextPopularity = this.popularity != null ? this.popularity + 1 : 1L;
        return new Product(
                this.id,
                this.createdAt,
                this.updatedAt,
                this.title,
                this.category,
                this.image,
                this.price,
                this.discount,
                this.description,
                nextPopularity,
                this.season,
                this.isSeasonal
        );
    }
}
