package com.process.clash.application.shop.recommendedproduct.data;

import com.process.clash.application.common.actor.Actor;

public class DeleteRecommendedProductData {

    public record Command(
            Actor actor,
            Long recommendedProductId
    ) {}
}
