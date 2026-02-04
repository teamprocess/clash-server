package com.process.clash.adapter.web.shop.purchase.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.purchase.data.CreatePurchaseData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class CreatePurchaseDto {

    @Schema(name = "CreatePurchaseDtoRequest")
    public record Request(
            @NotNull(message = "상품 ID는 필수 입력값입니다.")
            Long productId
    ) {
        public CreatePurchaseData.Command toCommand(Actor actor) {
            return new CreatePurchaseData.Command(actor, productId);
        }
    }

    @Schema(name = "CreatePurchaseDtoResponse")
    public record Response(
            Long purchaseId
    ) {
        public static Response from(CreatePurchaseData.Result result) {
            return new Response(result.purchaseId());
        }
    }
}
