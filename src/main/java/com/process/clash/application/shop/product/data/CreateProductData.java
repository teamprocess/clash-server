package com.process.clash.application.shop.product.data;

import com.process.clash.adapter.web.shop.product.dto.CreateProductDto;
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
    ) {
        public static CreateProductData.Command from(CreateProductDto.Request request, Actor actor) {
            return new CreateProductData.Command(
                    actor,
                    request.title(),
                    request.category(),
                    request.image(),
                    request.type(),
                    request.price(),
                    request.discount(),
                    request.description(),
                    request.seasonId()
            );
        }
    }

    public record Result(
            Long productId
    ) {
        public static CreateProductData.Result from(Long productId) {
            return new CreateProductData.Result(productId);
        }
    }
}