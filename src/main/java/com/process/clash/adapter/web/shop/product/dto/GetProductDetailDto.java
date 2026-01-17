package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.shop.product.data.GetProductDetailData;
import com.process.clash.application.shop.product.vo.ProductVo;

public class GetProductDetailDto {

    public record Response(
            CommonProductDto product
    ) {
        public static Response from(GetProductDetailData.Result result) {
            CommonProductDto product = CommonProductDto.from(result.product());
            return new Response(product);
        }
    }
}
