package com.process.clash.adapter.web.shop.recommendedproduct.dto;

import com.process.clash.application.shop.recommendedproduct.data.UpdateRecommendedProductData;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateRecommendedProductDto {

    public record Request(
            @NotNull(message = "진열 순서는 필수 입력값입니다.")
            @Min(value = 0, message = "진열 순서는 0 이상이어야 합니다.")
            Integer displayOrder,
            @NotBlank(message = "시작일은 필수 입력값입니다.")
            String startDate,
            @NotBlank(message = "종료일은 필수 입력값입니다.")
            String endDate,
            @NotNull(message = "활성화 상태는 필수 입력값입니다.")
            Boolean isActive
    ) {}

    public record Response(
            Long id,
            Long productId,
            String season,
            Integer displayOrder,
            String startDate,
            String endDate,
            Boolean isActive
    ) {
        public static Response from(UpdateRecommendedProductData.Result result) {
            return new Response(
                    result.id(),
                    result.productId(),
                    result.season(),
                    result.displayOrder(),
                    result.startDate(),
                    result.endDate(),
                    result.isActive()
            );
        }
    }
}
