package com.process.clash.adapter.web.shop.recommendedproduct.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.recommendedproduct.data.UpdateRecommendedProductData;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateRecommendedProductDto {

    @Schema(name = "UpdateRecommendedProductDtoRequest")

    public record Request(
            @NotNull(message = "진열 순서는 필수 입력값입니다.")
            @Min(value = 0, message = "진열 순서는 0 이상이어야 합니다.")
            Integer displayOrder,
            @NotNull(message = "시작일은 필수 입력값입니다.")
            LocalDate startDate,
            @NotNull(message = "종료일은 필수 입력값입니다.")
            LocalDate endDate,
            @NotNull(message = "활성화 상태는 필수 입력값입니다.")
            Boolean isActive
    ) {
        public UpdateRecommendedProductData.Command toCommand(Long recommendedProductId, Actor actor) {
            return new UpdateRecommendedProductData.Command(
                    actor,
                    recommendedProductId,
                    displayOrder,
                    startDate,
                    endDate,
                    isActive
            );
        }
    }

    @Schema(name = "UpdateRecommendedProductDtoResponse")

    public record Response(
            Long id,
            Long productId,
            String seasonName,
            Integer displayOrder,
            String startDate,
            String endDate,
            Boolean isActive
    ) {
        public static Response from(UpdateRecommendedProductData.Result result) {
            return new Response(
                    result.id(),
                    result.productId(),
                    result.seasonName(),
                    result.displayOrder(),
                    result.startDate(),
                    result.endDate(),
                    result.isActive()
            );
        }
    }
}
