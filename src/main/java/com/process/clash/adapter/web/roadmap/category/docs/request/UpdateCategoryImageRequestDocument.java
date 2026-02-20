package com.process.clash.adapter.web.roadmap.category.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 이미지 URL 저장 요청")
public class UpdateCategoryImageRequestDocument {

    @Schema(description = "S3에 업로드된 이미지 URL", example = "https://bucket.s3.ap-northeast-2.amazonaws.com/categories/images/category-1/abc123.png")
    public String imageUrl;
}
