package com.process.clash.adapter.web.shop.recommendedproduct.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.shop.recommendedproduct.dto.UpdateRecommendedProductOrderDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "추천 상품 순서 변경 응답")
public class UpdateRecommendedProductOrderResponseDoc extends SuccessResponseDoc {

    @Schema(description = "변경된 순서", implementation = UpdateRecommendedProductOrderDto.Response.class)
    public UpdateRecommendedProductOrderDto.Response data;
}
