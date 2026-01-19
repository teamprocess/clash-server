package com.process.clash.adapter.web.shop.recommendedproduct.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "추천 상품 수정 요청")
public class UpdateRecommendedProductRequestDoc {

    @Schema(description = "진열 순서")
    public Integer displayOrder;

    @Schema(description = "시작일")
    public LocalDate startDate;

    @Schema(description = "종료일")
    public LocalDate endDate;

    @Schema(description = "활성화 여부")
    public Boolean isActive;
}
