package com.process.clash.adapter.web.roadmap.category.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 이미지 업로드 URL 발급 요청")
public class IssueCategoryImageUploadUrlRequestDocument {

    @Schema(description = "파일명", example = "image.png")
    public String fileName;

    @Schema(description = "파일 Content-Type", example = "image/png")
    public String contentType;
}
