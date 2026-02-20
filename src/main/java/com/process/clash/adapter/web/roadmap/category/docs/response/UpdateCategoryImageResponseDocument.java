package com.process.clash.adapter.web.roadmap.category.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.category.dto.UpdateCategoryImageDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 이미지 URL 저장 응답")
public class UpdateCategoryImageResponseDocument extends SuccessResponseDocument {

    @Schema(description = "업데이트된 카테고리 이미지 정보", implementation = UpdateCategoryImageDto.Response.class)
    public UpdateCategoryImageDto.Response data;
}
