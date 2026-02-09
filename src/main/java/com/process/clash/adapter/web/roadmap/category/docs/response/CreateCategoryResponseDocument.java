package com.process.clash.adapter.web.roadmap.category.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.category.dto.CreateCategoryDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 생성 응답")
public class CreateCategoryResponseDocument extends SuccessResponseDocument {

    @Schema(description = "생성된 카테고리", implementation = CreateCategoryDto.Response.class)
    public CreateCategoryDto.Response data;
}
