package com.process.clash.application.shop.recommendedproduct.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;

import java.time.LocalDate;

public class CreateRecommendedProductData {

    public record Command(
            Actor actor,
            Long productId,
            Integer displayOrder,
            LocalDate startDate,
            LocalDate endDate
    ) {}

    public record Result(
            Long id,
            Long productId,
            String seasonName,
            Integer displayOrder,
            String startDate,
            String endDate,
            Boolean isActive
    ) {
        public static Result from(RecommendedProduct recommendedProduct, String seasonName) {
            return new Result(
                    recommendedProduct.id(),
                    recommendedProduct.productId(),
                    seasonName,
                    recommendedProduct.displayOrder(),
                    recommendedProduct.startDate().toString(),
                    recommendedProduct.endDate().toString(),
                    recommendedProduct.isActive()
            );
        }
    }
}
