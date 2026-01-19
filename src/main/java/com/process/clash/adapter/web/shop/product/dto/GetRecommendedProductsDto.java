package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.shop.recommendedproduct.data.GetRecommendedProductsData;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetRecommendedProductsDto {

    @Schema(name = "GetRecommendedProductsDtoResponse")

    public record Response(
            List<CommonProductDto> products
    ) {
        public static Response from(GetRecommendedProductsData.Result result) {
            List<CommonProductDto> products = result.products().stream()
                    .map(CommonProductDto::from)
                    .toList();
            return new Response(products);
        }
    }
}
