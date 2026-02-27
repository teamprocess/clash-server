package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "보유 아이템")
public class GetMyItemDocument {

    @Schema(description = "아이템 ID", example = "1")
    public Long id;

    @Schema(description = "아이템 제목", example = "기본 인시그니아")
    public String title;

    @Schema(description = "아이템 설명", example = "기본 아이템")
    public String description;

    @Schema(description = "카테고리", example = "INSIGNIA")
    public String category;

    @Schema(description = "가격", example = "100")
    public Integer price;

    @Schema(description = "할인율", example = "0")
    public Integer discount;

    @Schema(description = "인기도", example = "10")
    public Long popularity;

    @Schema(description = "시즌 여부", example = "false")
    public Boolean isSeasonal;

    @Schema(description = "시즌 정보 (시즌 상품인 경우)", implementation = GetMyItemSeasonDocument.class)
    public GetMyItemSeasonDocument season;
}
