package com.process.clash.application.shop.product.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import java.time.Instant;

public class UpdateProductData {

    public record Command(
            Actor actor,
            Long productId,
            String title,
            ProductCategory category,
            String image,
            String audio,
            Long price,
            Integer discount,
            String description,
            Long seasonId,
            Boolean isAblePurchase
    ) {}

    public record Result(
            Long productId,
            Instant updatedAt
    ) {
        public static Result from(Product product) {
            return new Result(product.id(), product.updatedAt());
        }
    }
}
