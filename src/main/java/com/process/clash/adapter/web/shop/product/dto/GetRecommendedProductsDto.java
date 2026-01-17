package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.shop.recommendedproduct.data.GetRecommendedProductsData;
import com.process.clash.application.shop.product.vo.ProductVo;

import java.util.List;

public class GetRecommendedProductsDto {

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
