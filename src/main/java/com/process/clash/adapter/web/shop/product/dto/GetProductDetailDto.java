package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.shop.product.data.GetProductDetailData;
import com.process.clash.application.shop.product.vo.ProductVo;

public class GetProductDetailDto {

    public record Response(
            Long id,
            String title,
            String category,
            String image,
            String type,
            Long price,
            Integer discount,
            String description,
            Long popularity,
            String seasonName,
            Boolean isSeasonal,
            String createdAt
    ) {
        public static Response from(GetProductDetailData.Result result) {
            ProductVo product = result.product();
            return new Response(
                    product.id(),
                    product.title(),
                    product.category().name(),
                    product.image(),
                    product.type().name(),
                    product.price(),
                    product.discount(),
                    product.description(),
                    product.popularity(),
                    product.seasonName(),
                    product.isSeasonal(),
                    product.createdAt().toString()
            );
        }
    }
}
