package com.process.clash.adapter.web.shop.recommendedproduct.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.recommendedproduct.data.UpdateRecommendedProductOrderData;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateRecommendedProductOrderDto {

    @Schema(name = "UpdateRecommendedProductOrderDtoRequest")

    public record Request(
            @NotNull(message = "진열 순서는 필수 입력값입니다.")
            @Min(value = 0, message = "진열 순서는 0 이상이어야 합니다.")
            Integer displayOrder
    ) {
        public UpdateRecommendedProductOrderData.Command toCommand(Long recommendedProductId, Actor actor) {
            return new UpdateRecommendedProductOrderData.Command(
                    actor,
                    recommendedProductId,
                    displayOrder
            );
        }
    }

    @Schema(name = "UpdateRecommendedProductOrderDtoResponse")

    public record Response(
            Long id,
            Integer displayOrder
    ) {
        public static Response from(UpdateRecommendedProductOrderData.Result result) {
            return new Response(
                    result.id(),
                    result.displayOrder()
            );
        }
    }
}
