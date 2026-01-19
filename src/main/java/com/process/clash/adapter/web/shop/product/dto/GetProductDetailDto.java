package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.shop.product.data.GetProductDetailData;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetProductDetailDto {

    @Schema(name = "GetProductDetailDtoResponse")

    public record Response(
            CommonProductDto product
    ) {
        public static Response from(GetProductDetailData.Result result) {
            CommonProductDto product = CommonProductDto.from(result.product());
            return new Response(product);
        }
    }
}
