package com.process.clash.adapter.web.shop.recommendedproduct.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "추천 상품 등록 요청")
public class CreateRecommendedProductRequestDocument {

    @Schema(description = "상품 ID")
    public Long productId;

    @Schema(description = "진열 순서")
    public Integer displayOrder;

    @Schema(description = "시작일")
    public LocalDate startDate;

    @Schema(description = "종료일")
    public LocalDate endDate;
}
