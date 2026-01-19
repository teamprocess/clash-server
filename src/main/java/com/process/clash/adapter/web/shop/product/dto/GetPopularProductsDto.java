package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.shop.product.data.GetPopularProductsData;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetPopularProductsDto {

    @Schema(name = "GetPopularProductsDtoResponse")

    public record Response(
            List<CommonProductDto> products
    ) {
        public static Response from(GetPopularProductsData.Result result) {
            List<CommonProductDto> products = result.products().stream()
                    .map(CommonProductDto::from)
                    .toList();
            return new Response(products);
        }
    }


}
