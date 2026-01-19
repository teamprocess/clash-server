package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.product.data.CreateProductData;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductGoodsType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public class CreateProductDto {

    @Schema(name = "CreateProductDtoRequest")

    public record Request(
            @NotBlank(message = "상품명은 필수 입력값입니다.")
            String title,
            @NotNull(message = "카테고리는 필수 입력값입니다.")
            ProductCategory category,
            @NotBlank(message = "이미지 링크는 필수 입력값입니다.")
            String image,
            @NotNull(message = "재화 종류는 필수 입력값입니다.")
            ProductGoodsType type,
            @NotNull(message = "상품 가격은 필수 입력값입니다.")
            @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
            Long price,
            @Min(value = 0, message = "할인율은 0 이상이어야 합니다.")
            @Max(value = 100, message = "할인율은 100 이하여야 합니다.")
            Integer discount,
            String description,
            Long seasonId  // 시즌 상품이 아니면 null
    ) {
        public CreateProductData.Command toCommand(Actor actor) {
            return new CreateProductData.Command(
                    actor,
                    title,
                    category,
                    image,
                    type,
                    price,
                    discount,
                    description,
                    seasonId
            );
        }
    }

    @Schema(name = "CreateProductDtoResponse")

    public record Response(
            Long productId
    ) {
        public static Response from(CreateProductData.Result result) {
            return new Response(result.productId());
        }
    }
}