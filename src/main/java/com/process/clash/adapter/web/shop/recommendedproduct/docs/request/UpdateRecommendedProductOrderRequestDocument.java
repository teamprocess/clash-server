package com.process.clash.adapter.web.shop.recommendedproduct.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "추천 상품 순서 변경 요청")
public class UpdateRecommendedProductOrderRequestDocument {

    @Schema(description = "진열 순서")
    public Integer displayOrder;
}
