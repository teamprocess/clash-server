package com.process.clash.adapter.web.roadmap.category.dto;

import com.process.clash.application.roadmap.category.data.DeleteCategoryData;
import io.swagger.v3.oas.annotations.media.Schema;

public class DeleteCategoryDto {

    @Schema(name = "DeleteCategoryDtoResponse")

    public record Response(Long categoryId) {
        public static Response from(DeleteCategoryData.Result result) {
            return new Response(result.categoryId());
        }
    }
}