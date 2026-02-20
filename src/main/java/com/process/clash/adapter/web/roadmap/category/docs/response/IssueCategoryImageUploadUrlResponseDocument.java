package com.process.clash.adapter.web.roadmap.category.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.category.dto.IssueCategoryImageUploadUrlDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 이미지 업로드 URL 발급 응답")
public class IssueCategoryImageUploadUrlResponseDocument extends SuccessResponseDocument {

    @Schema(description = "발급된 업로드 URL 정보", implementation = IssueCategoryImageUploadUrlDto.Response.class)
    public IssueCategoryImageUploadUrlDto.Response data;
}
