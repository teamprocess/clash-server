package com.process.clash.application.shop.recommendedproduct.data;

import com.process.clash.adapter.web.shop.recommendedproduct.dto.UpdateRecommendedProductOrderDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;

public class UpdateRecommendedProductOrderData {

    public record Command(
            Actor actor,
            Long recommendedProductId,
            Integer displayOrder
    ) {
        public static Command from(Long recommendedProductId, UpdateRecommendedProductOrderDto.Request request, Actor actor) {
            return new Command(
                    actor,
                    recommendedProductId,
                    request.displayOrder()
            );
        }
    }

    public record Result(
            Long id,
            Integer displayOrder
    ) {
        public static Result from(RecommendedProduct recommendedProduct) {
            return new Result(
                    recommendedProduct.id(),
                    recommendedProduct.displayOrder()
            );
        }
    }
}
