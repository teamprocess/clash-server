package com.process.clash.adapter.web.shop.recommendedproduct.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.shop.recommendedproduct.dto.CreateRecommendedProductDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "추천 상품 등록 응답")
public class CreateRecommendedProductResponseDocument extends SuccessResponseDocument {

    @Schema(description = "등록된 추천 상품", implementation = CreateRecommendedProductDto.Response.class)
    public CreateRecommendedProductDto.Response data;
}
