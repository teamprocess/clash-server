package com.process.clash.adapter.web.shop.recommendedproduct.dto;

import com.process.clash.application.shop.recommendedproduct.data.CreateRecommendedProductData;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateRecommendedProductDto {

    public record Request(
            @NotNull(message = "상품 ID는 필수 입력값입니다.")
            Long productId,
            @NotNull(message = "진열 순서는 필수 입력값입니다.")
            @Min(value = 0, message = "진열 순서는 0 이상이어야 합니다.")
            Integer displayOrder,
            @NotBlank(message = "시작일은 필수 입력값입니다.")
            String startDate,
            @NotBlank(message = "종료일은 필수 입력값입니다.")
            String endDate
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
        public static Response from(CreateRecommendedProductData.Result result) {
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
