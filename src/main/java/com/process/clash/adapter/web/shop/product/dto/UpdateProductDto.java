package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.product.data.UpdateProductData;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateProductDto {

    @Schema(name = "UpdateProductDtoRequest")
    public record Request(
            @NotBlank(message = "상품명은 필수 입력값입니다.")
            String title,
            @NotNull(message = "카테고리는 필수 입력값입니다.")
            ProductCategory category,
            @NotBlank(message = "이미지 링크는 필수 입력값입니다.")
            String image,
            String audio,
            @NotNull(message = "상품 가격은 필수 입력값입니다.")
            @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
            Long price,
            @Min(value = 0, message = "할인율은 0 이상이어야 합니다.")
            @Max(value = 100, message = "할인율은 100 이하여야 합니다.")
            Integer discount,
            String description,
            Long seasonId
    ) {
        public UpdateProductData.Command toCommand(Long productId, Actor actor) {
            return new UpdateProductData.Command(
                    actor,
                    productId,
                    title,
                    category,
                    image,
                    audio,
                    price,
                    discount,
                    description,
                    seasonId
            );
        }
    }

    @Schema(name = "UpdateProductDtoResponse")
    public record Response(
            Long productId
    ) {
        public static Response from(UpdateProductData.Result result) {
            return new Response(result.productId());
        }
    }
}
