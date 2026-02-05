package com.process.clash.application.shop.purchase.data;

import com.process.clash.application.common.actor.Actor;

public class CreatePurchaseData {

    public record Command(
            Actor actor,
            Long productId
    ) {}

    public record Result(
            Long purchaseId
    ) {
        public static Result from(Long purchaseId) {
            return new Result(purchaseId);
        }
    }
}
