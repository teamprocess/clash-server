package com.process.clash.adapter.web.roadmap.category.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import com.process.clash.adapter.web.roadmap.category.dto.DeleteCategoryDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 삭제 응답")
public class DeleteCategoryResponseDocument extends SuccessResponseDocument {

    @Schema(description = "삭제된 카테고리", implementation = DeleteCategoryDto.Response.class)
    public DeleteCategoryDto.Response data;
}
