package com.process.clash.application.shop.product.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductGoodsType;

public class CreateProductData {

    public record Command(
            Actor actor,
            String title,
            ProductCategory category,
            String image,
            ProductGoodsType type,
            Long price,
            Integer discount,
            String description,
            Long seasonId
    ) {}

    public record Result(
            Long productId
    ) {
        public static CreateProductData.Result from(Long productId) {
            return new CreateProductData.Result(productId);
        }
    }
}