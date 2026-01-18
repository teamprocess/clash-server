package com.process.clash.adapter.web.roadmap.category.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.roadmap.category.dto.UpdateCategoryDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 수정 응답")
public class UpdateCategoryResponseDoc extends SuccessResponseDoc {

    @Schema(description = "수정된 카테고리", implementation = UpdateCategoryDto.Response.class)
    public UpdateCategoryDto.Response data;
}
