package com.process.clash.application.shop.product.data;

import com.process.clash.application.common.actor.Actor;

public class DeleteProductData {

    public record Command(
            Actor actor,
            Long productId
    ) {}

    public record Result(
            Long productId
    ) {
        public static Result from(Long productId) {
            return new Result(productId);
        }
    }
}
