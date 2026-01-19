package com.process.clash.adapter.web.shop.product.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.shop.product.dto.GetRecommendedProductsDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "추천 상품 목록 조회 응답")
public class GetRecommendedProductsResponseDoc extends SuccessResponseDoc {

    @Schema(description = "추천 상품 목록", implementation = GetRecommendedProductsDto.Response.class)
    public GetRecommendedProductsDto.Response data;
}
