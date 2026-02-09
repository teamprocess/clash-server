package com.process.clash.adapter.web.roadmap.category.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.category.dto.GetCategoriesDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 목록 조회 응답")
public class GetCategoriesResponseDocument extends SuccessResponseDocument {

    @Schema(description = "카테고리 목록", implementation = GetCategoriesDto.Response.class)
    public GetCategoriesDto.Response data;
}
