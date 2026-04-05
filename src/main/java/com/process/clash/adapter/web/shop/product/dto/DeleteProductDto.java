package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.shop.product.data.DeleteProductData;
import io.swagger.v3.oas.annotations.media.Schema;

public class DeleteProductDto {

    @Schema(name = "DeleteProductDtoResponse")
    public record Response(
            Long productId
    ) {
        public static Response from(DeleteProductData.Result result) {
            return new Response(result.productId());
        }
    }
}
