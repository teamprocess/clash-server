package com.process.clash.adapter.web.shop.recommendedproduct.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.recommendedproduct.data.CreateRecommendedProductData;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

public class CreateRecommendedProductDto {

    @Schema(name = "CreateRecommendedProductDtoRequest")

    public record Request(
            @NotNull(message = "상품 ID는 필수 입력값입니다.")
            Long productId,
            @NotNull(message = "진열 순서는 필수 입력값입니다.")
            @Min(value = 0, message = "진열 순서는 0 이상이어야 합니다.")
            Integer displayOrder,
            @NotNull(message = "시작일은 필수 입력값입니다.")
            LocalDate startDate, // 기본적으로 yyyy-mm-dd 형식으로 입력받습니다.
            @NotNull(message = "종료일은 필수 입력값입니다.")
            LocalDate endDate
    ) {
        public CreateRecommendedProductData.Command toCommand(Actor actor) {
            return new CreateRecommendedProductData.Command(
                    actor,
                    productId,
                    displayOrder,
                    startDate,
                    endDate
            );
        }
    }

    @Schema(name = "CreateRecommendedProductDtoResponse")

    public record Response(
            Long id,
            Long productId,
            String seasonName,
            Integer displayOrder,
            String startDate,
            String endDate,
            Boolean isActive
    ) {
        public static Response from(CreateRecommendedProductData.Result result) {
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
