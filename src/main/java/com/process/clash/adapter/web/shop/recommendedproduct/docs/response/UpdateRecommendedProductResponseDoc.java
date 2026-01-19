package com.process.clash.adapter.web.shop.recommendedproduct.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.shop.recommendedproduct.dto.UpdateRecommendedProductDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "추천 상품 수정 응답")
public class UpdateRecommendedProductResponseDoc extends SuccessResponseDoc {

    @Schema(description = "수정된 추천 상품", implementation = UpdateRecommendedProductDto.Response.class)
    public UpdateRecommendedProductDto.Response data;
}
